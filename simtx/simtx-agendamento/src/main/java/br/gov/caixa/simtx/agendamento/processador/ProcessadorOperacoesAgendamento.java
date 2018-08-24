/*******************************************************************************
 * Copyright (C)  2018 - CAIXA Econômica Federal 
 * 
 * Todos os direitos reservados
 ******************************************************************************/
package br.gov.caixa.simtx.agendamento.processador;

import java.io.Serializable;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.apache.log4j.Logger;

import br.gov.caixa.simtx.agendamento.servico.CancelamentoAgendamento;
import br.gov.caixa.simtx.agendamento.servico.ConsultaListaAgendamentos;
import br.gov.caixa.simtx.agendamento.servico.DetalheAgendamento;
import br.gov.caixa.simtx.persistencia.constante.Constantes;
import br.gov.caixa.simtx.persistencia.constante.MensagemRetorno;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb004Canal;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb006Mensagem;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb008MeioEntrada;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb011VersaoServico;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb014Transacao;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb016IteracaoCanal;
import br.gov.caixa.simtx.util.ParametrosAdicionais;
import br.gov.caixa.simtx.util.SimtxConfig;
import br.gov.caixa.simtx.util.exception.ServicoException;
import br.gov.caixa.simtx.util.gerenciador.GerenciadorTransacao;
import br.gov.caixa.simtx.util.gerenciador.TratadorDeExcecao;
import br.gov.caixa.simtx.util.gerenciador.ValidadorRegrasNegocio;
import br.gov.caixa.simtx.util.gerenciador.servico.ProcessadorServicos;
import br.gov.caixa.simtx.util.to.DadosTransacaoComuns;
import br.gov.caixa.simtx.util.xml.BuscadorTextoXml;
import br.gov.caixa.simtx.util.xml.DadosBarramento;
import br.gov.caixa.simtx.util.xml.GeradorPassosMigrado;

/**
 * Classe responsavel por tratar e executar todos os servicos para consulta lista, detalhe e cancelamentos de
 * agendamentos.
 * 
 * @author rsfagundes
 *
 */
@Stateless
public class ProcessadorOperacoesAgendamento extends GerenciadorTransacao implements ProcessadorServicos, Serializable {

	private static final long serialVersionUID = 1L;

	private static final Logger logger = Logger.getLogger(ProcessadorOperacoesAgendamento.class);
		
	protected DadosBarramento dadosBarramento;
	
	@Inject
	private ConsultaListaAgendamentos consultaListaAgendamentos;
	
	@Inject
	private CancelamentoAgendamento cancelamentoAgendamento;
	
	@Inject
	protected GeradorPassosMigrado geradorPassosMigrado;
	
	@Inject
	protected TratadorDeExcecao tratadorDeExcecao;
	
	@Inject
	protected ValidadorRegrasNegocio validadorRegrasNegocio;
	
	@Inject
	protected SimtxConfig simtxConfig;
	
	@Inject
	private DetalheAgendamento detalheAgendamento;
	

	@Override
	public void processar(ParametrosAdicionais parametrosAdicionais) {
		Mtxtb014Transacao transacao = null;
		Mtxtb016IteracaoCanal iteracaoCanal = null;
		Mtxtb011VersaoServico servico = null;
		Mtxtb004Canal canal = null;
		
		try {
			this.dadosBarramento = new DadosBarramento();
			this.dadosBarramento.escrever(parametrosAdicionais.getXmlMensagem());

			BuscadorTextoXml buscador = new BuscadorTextoXml(parametrosAdicionais.getXmlMensagem());
			Long codigoServico = Long.parseLong(buscador.xpathTexto("/*[1]/HEADER/SERVICO/CODIGO"));
			Integer versaoServico = Integer.parseInt(buscador.xpathTexto("/*[1]/HEADER/SERVICO/VERSAO"));
			servico = buscarServico(codigoServico, versaoServico);
			
			canal = buscarCanal(buscador);

			transacao = salvarTransacao(buscador, canal);
			iteracaoCanal = salvarIteracaoCanal(buscador, parametrosAdicionais.getXmlMensagem(), transacao);
			salvarTransacaoServico(transacao, servico);

			int nuMeioEntrada = Integer.parseInt(buscador.xpathTexto("/*[1]/HEADER/MEIOENTRADA"));
			Mtxtb008MeioEntrada meioEntrada = new Mtxtb008MeioEntrada();
			meioEntrada.setNuMeioEntrada(nuMeioEntrada);
			meioEntrada = this.fornecedorDados.buscarMeioEntrada(nuMeioEntrada);

			this.validadorRegrasNegocio.validarRegrasMigrado(parametrosAdicionais.getXmlMensagem(), canal, meioEntrada, servico);
			
			if (buscador.xpathTexto("/LISTA_AGENDAMENTOS_TRANSACOES_ENTRADA") != null
					&& !buscador.xpathTexto("/LISTA_AGENDAMENTOS_TRANSACOES_ENTRADA").isEmpty()) {
				this.consultaListaAgendamentos.processar(transacao, iteracaoCanal, canal, servico, meioEntrada,
						this.dadosBarramento, parametrosAdicionais);
			} 
			else if (buscador.xpathTexto("/DETALHE_AGENDAMENTO_BOLETO_ENTRADA") != null
					&& !buscador.xpathTexto("/DETALHE_AGENDAMENTO_BOLETO_ENTRADA").isEmpty()) {			  
				this.detalheAgendamento.processar(transacao, canal, servico, meioEntrada, this.dadosBarramento,
						iteracaoCanal, parametrosAdicionais);
			} 
			else if (buscador.xpathTexto("/*[1]/CANCELAMENTO_AGENDAMENTO") != null && !buscador.xpathTexto("/*[1]/CANCELAMENTO_AGENDAMENTO").isEmpty()) {
				this.cancelamentoAgendamento.processar(transacao, iteracaoCanal, canal, servico, meioEntrada,
						this.dadosBarramento, parametrosAdicionais);
			}
		} 
		catch (ServicoException se) {
			logger.error(se.getMensagem());
			DadosTransacaoComuns dadosTransacaoComuns = new DadosTransacaoComuns(servico, transacao, iteracaoCanal);
			parametrosAdicionais.setDadosBarramento(dadosBarramento);
			this.tratadorDeExcecao.tratarExcecaoRetomada(dadosTransacaoComuns, se, parametrosAdicionais, true);		
		} 
		catch (Exception e) {
			logger.error(e);
			Mtxtb006Mensagem mensagemErroInterno = this.fornecedorDados.buscarMensagem(MensagemRetorno.ERRO_INTERNO);
			ServicoException se = new ServicoException(mensagemErroInterno, Constantes.ORIGEM_SIMTX);
			DadosTransacaoComuns dadosTransacaoComuns = new DadosTransacaoComuns(servico, transacao, iteracaoCanal);
			parametrosAdicionais.setDadosBarramento(dadosBarramento);
			this.tratadorDeExcecao.tratarExcecaoRetomada(dadosTransacaoComuns, se, parametrosAdicionais, true);		
		}
		finally {
			logger.info(" ==== Processo Finalizado ==== ");
		}
	}
}
