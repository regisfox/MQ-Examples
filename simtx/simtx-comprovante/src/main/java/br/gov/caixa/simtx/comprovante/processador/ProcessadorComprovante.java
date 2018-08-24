package br.gov.caixa.simtx.comprovante.processador;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.apache.log4j.Logger;

import br.gov.caixa.simtx.comprovante.enuns.ServicosComprovanteEnum;
import br.gov.caixa.simtx.comprovante.servico.DetalheComprovante;
import br.gov.caixa.simtx.comprovante.servico.ListaComprovante;
import br.gov.caixa.simtx.comprovante.util.ConstantesComprovante;
import br.gov.caixa.simtx.persistencia.atualizador.dados.AtualizadorDadosTransacao;
import br.gov.caixa.simtx.persistencia.constante.AcaoRetorno;
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
import br.gov.caixa.simtx.util.gerenciador.cache.RepositorioArquivo;
import br.gov.caixa.simtx.util.gerenciador.servico.ProcessadorServicos;
import br.gov.caixa.simtx.util.to.DadosTransacaoComuns;
import br.gov.caixa.simtx.util.to.TarefasServicoResposta;
import br.gov.caixa.simtx.util.xml.BuscadorTextoXml;
import br.gov.caixa.simtx.util.xml.DadosBarramento;
import br.gov.caixa.simtx.util.xml.ParametroXsl;
import br.gov.caixa.simtx.util.xml.Resposta;
import br.gov.caixa.simtx.util.xml.TransformadorXsl;

@Stateless
public class ProcessadorComprovante extends GerenciadorTransacao implements ProcessadorServicos, Serializable {
    
	private static final long serialVersionUID = 1L;
	
	private static final Logger logger = Logger.getLogger(ProcessadorComprovante.class);
	
	protected DadosBarramento dadosBarramento;
	
	@Inject
	protected TratadorDeExcecao tratadorDeExcecao;
	
	@Inject
	protected SimtxConfig simtxConfig;
	
	@Inject
	protected ListaComprovante listaComprovante;
	
	@Inject
	protected DetalheComprovante detalheComprovante;
	
	@Inject
	private RepositorioArquivo repositorioArquivo;
	
	@Inject
	private AtualizadorDadosTransacao atualizadorDadosTransacao;

	
	
	@Override
	public void processar(ParametrosAdicionais parametrosAdicionais) {
		logger.info(" ==== Processo Comprovante Iniciado ==== ");
		
		Mtxtb014Transacao transacao = null;
		Mtxtb016IteracaoCanal iteracaoCanal = null;
		Mtxtb011VersaoServico servico = null;
		Mtxtb004Canal canal = null;
		String xmlSaidaCanal = null;
		TarefasServicoResposta tarefasServicoResposta = new TarefasServicoResposta();
		
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
			if(servico.isMigrado()) {
				this.validadorRegrasNegocio.validarRegrasMigrado(parametrosAdicionais.getXmlMensagem(), canal, meioEntrada, servico);
			}
			meioEntrada = this.fornecedorDados.buscarMeioEntrada(nuMeioEntrada);

			ServicosComprovanteEnum servicoEnum = ServicosComprovanteEnum
					.obterServico(servico.getId().getNuServico001());
			if(servicoEnum.getTipoServico().equals(ConstantesComprovante.SERVICO_LISTA)) {
				tarefasServicoResposta = this.listaComprovante.executar(transacao, canal, servico, meioEntrada, this.dadosBarramento);
			}
			else {
				tarefasServicoResposta = this.detalheComprovante.executar(transacao, canal, servico, meioEntrada, this.dadosBarramento);
			}
			
			xmlSaidaCanal = transformarXml(servico.getDeXsltResposta(), transacao, canal, tarefasServicoResposta.getResposta());

			enviarRespostaCanal(xmlSaidaCanal, parametrosAdicionais);

			BigDecimal situacaoTransacao = this.validadorRegrasNegocio.situacaoFinalTransacao(servico);
			transacao = atualizaStatusTransacao(transacao, situacaoTransacao);
			iteracaoCanal = montaMtxtb016IteracaoCanalTansacao(iteracaoCanal, transacao, xmlSaidaCanal);
			boolean statusAtualizacaoTransacao = atualizadorDadosTransacao.atualizarDadosTransacaoTarefasIteracoes(transacao, tarefasServicoResposta.getListaTransacaoTarefas(), iteracaoCanal);
			direcionaProcessadorMensagemTransacao(false, statusAtualizacaoTransacao, transacao, servico, dadosBarramento, xmlSaidaCanal);
		}
		catch (ServicoException se) {
			logger.error(se.getMensagem());
			parametrosAdicionais.setDadosBarramento(dadosBarramento);
			DadosTransacaoComuns dadosTransacaoComuns = new DadosTransacaoComuns(servico, transacao,tarefasServicoResposta.getListaTransacaoTarefas(), iteracaoCanal);
			this.tratadorDeExcecao.tratarExcecaoRetomada(dadosTransacaoComuns, se, parametrosAdicionais, true);
		}
		catch (Exception e) {
			logger.error(e.getMessage(), e);
			Mtxtb006Mensagem mensagemErroInterno = this.fornecedorDados.buscarMensagem(MensagemRetorno.ERRO_INTERNO);
			ServicoException se = new ServicoException(mensagemErroInterno,Constantes.ORIGEM_SIMTX);
			DadosTransacaoComuns dadosTransacaoComuns = new DadosTransacaoComuns(servico, transacao,tarefasServicoResposta.getListaTransacaoTarefas(), iteracaoCanal);
			this.tratadorDeExcecao.tratarExcecaoRetomada(dadosTransacaoComuns, se, parametrosAdicionais, true);
		}
		finally {
			logger.info(" ==== Processo Comprovante Finalizado ==== ");
		}
	}
	
	/**
	 * Gera um xml a partir do xslt.
	 * 
	 * @param xslt
	 * @param transacao
	 * @param canal
	 * @param mensagem
	 * @return
	 * @throws ServicoException
	 */
	public String transformarXml(String xslt, Mtxtb014Transacao transacao, Mtxtb004Canal canal, Resposta mensagem)
			throws ServicoException {
		try {
			List<ParametroXsl> parametrosNovos = new ArrayList<>();
			parametrosNovos.add(new ParametroXsl("nsuSimtx", String.valueOf(transacao.getNuNsuTransacao())));
			parametrosNovos.add(new ParametroXsl("pRedeTransmissora", String.valueOf(canal.getNuRedeTransmissora())));
			if(mensagem != null) {
				parametrosNovos.add(new ParametroXsl(ParametroXsl.XSL_PARAM_COD_RETORNO, mensagem.getCodigo()));
		        parametrosNovos.add(new ParametroXsl(ParametroXsl.XSL_PARAM_ACAO_RETORNO, AcaoRetorno.recuperarAcao(mensagem.getIcTipoMensagem()).getRotulo()));
		        parametrosNovos.add(new ParametroXsl(ParametroXsl.XSL_PARAM_ORIGEM_RETORNO, mensagem.getOrigem()));
		        parametrosNovos.add(new ParametroXsl(ParametroXsl.XSL_PARAM_MENSAGEM_NEGOCIAL,mensagem.getMensagemNegocial()));
		        parametrosNovos.add(new ParametroXsl(ParametroXsl.XSL_PARAM_MENSAGEM_TECNICA, mensagem.getMensagemTecnica()));
			}
			ParametroXsl[] pArr = new ParametroXsl[parametrosNovos.size()];
			
			String caminhoXls = this.simtxConfig.getCaminhoXslt() + xslt;
			String arquivoXsl = this.repositorioArquivo.recuperarArquivo(caminhoXls);
			return new TransformadorXsl().transformar(this.dadosBarramento.getDadosLeitura(), arquivoXsl, parametrosNovos.toArray(pArr));
		} 
		catch (Exception e) {
			logger.error(e);
			Mtxtb006Mensagem mensagemErroInterno = this.fornecedorDados.buscarMensagem(MensagemRetorno.ERRO_INTERNO);
			throw new ServicoException(mensagemErroInterno,Constantes.ORIGEM_SIMTX);
		}
	}

}
