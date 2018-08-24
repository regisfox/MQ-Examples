/*******************************************************************************
 * Copyright (C)  2018 - CAIXA Econômica Federal 
 * 
 * Todos os direitos reservados
 ******************************************************************************/
package br.gov.caixa.simtx.agendamento.servico;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.apache.log4j.Logger;

import br.gov.caixa.simtx.persistencia.atualizador.dados.AtualizadorDadosTransacao;
import br.gov.caixa.simtx.persistencia.cache.agendamento.FornecedorDadosAgendamento;
import br.gov.caixa.simtx.persistencia.constante.AcaoRetorno;
import br.gov.caixa.simtx.persistencia.constante.Constantes;
import br.gov.caixa.simtx.persistencia.constante.MensagemRetorno;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb003ServicoTarefa;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb004Canal;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb006Mensagem;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb008MeioEntrada;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb011VersaoServico;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb014Transacao;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb016IteracaoCanal;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb032MarcaConta;
import br.gov.caixa.simtx.util.ParametrosAdicionais;
import br.gov.caixa.simtx.util.SimtxConfig;
import br.gov.caixa.simtx.util.exception.ServicoException;
import br.gov.caixa.simtx.util.gerenciador.GerenciadorTarefas;
import br.gov.caixa.simtx.util.gerenciador.GerenciadorTransacao;
import br.gov.caixa.simtx.util.gerenciador.TratadorDeExcecao;
import br.gov.caixa.simtx.util.gerenciador.ValidadorRegrasNegocio;
import br.gov.caixa.simtx.util.gerenciador.cache.RepositorioArquivo;
import br.gov.caixa.simtx.util.gerenciador.servico.ProcessadorServicos;
import br.gov.caixa.simtx.util.integrador.sicco.ProcessadorEnvioSicco;
import br.gov.caixa.simtx.util.mensagem.GerenciadorFilasMQ;
import br.gov.caixa.simtx.util.to.DadosTransacaoComuns;
import br.gov.caixa.simtx.util.to.TarefasServicoResposta;
import br.gov.caixa.simtx.util.xml.BuscadorTextoXml;
import br.gov.caixa.simtx.util.xml.DadosBarramento;
import br.gov.caixa.simtx.util.xml.GeradorPassosMigrado;
import br.gov.caixa.simtx.util.xml.ParametroXsl;
import br.gov.caixa.simtx.util.xml.Resposta;
import br.gov.caixa.simtx.util.xml.TransformadorXsl;

@Stateless
public class ValidaBoletoAgendamento extends GerenciadorTransacao implements ProcessadorServicos, Serializable {

	private static final long serialVersionUID = 1L;

	private static final Logger logger = Logger.getLogger(ValidaBoletoAgendamento.class);

	protected DadosBarramento dadosBarramento;

	@Inject
	protected GerenciadorFilasMQ execucaoMq;

	@Inject
	protected TratadorDeExcecao tratadorDeExcecao;

	@Inject
	protected GeradorPassosMigrado geradorPassosMigrado;

	@Inject
	protected SimtxConfig simtxConfig;

	@Inject
	protected FornecedorDadosAgendamento fornecedorDadosAgendamento;

	@Inject
	protected ProcessadorEnvioSicco processadorEnvioSicco;

	@Inject
	private RepositorioArquivo repositorioArquivo;
	
	@Inject
	private AtualizadorDadosTransacao atualizadorDadosTransacao;
	
	
	@Override
	public void processar(ParametrosAdicionais parametrosAdicionais) {
		
		Mtxtb004Canal canal = null;
		Mtxtb014Transacao transacao = null;
		Mtxtb016IteracaoCanal iteracaoCanal = null;
		Mtxtb011VersaoServico servico = null;
		try {
			logger.info(" ==== Processo Valida Boleto Agendamento Iniciado ==== ");
			
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
			this.validadorRegrasNegocio.validarRegrasAgendamento(transacao);
			
			processarEntradaSaida(canal, meioEntrada, servico, transacao, iteracaoCanal, parametrosAdicionais);
		
		}
		catch (ServicoException se) {
			logger.error(se.getMensagem());
			parametrosAdicionais.setDadosBarramento(dadosBarramento);
			DadosTransacaoComuns dadosTransacaoComuns = new DadosTransacaoComuns(servico, transacao, iteracaoCanal);
			this.tratadorDeExcecao.tratarExcecaoRetomada(dadosTransacaoComuns, se, parametrosAdicionais, true);
		}
		catch (Exception e) {
			logger.error(e.getMessage(), e);
			Mtxtb006Mensagem mensagemErroInterno = this.fornecedorDados.buscarMensagem(MensagemRetorno.ERRO_INTERNO);
			ServicoException se = new ServicoException(mensagemErroInterno,Constantes.ORIGEM_SIMTX);
			DadosTransacaoComuns dadosTransacaoComuns = new DadosTransacaoComuns(servico, transacao,iteracaoCanal);
			this.tratadorDeExcecao.tratarExcecaoRetomada(dadosTransacaoComuns, se, parametrosAdicionais, true);
		}
		finally {
			logger.info(" ==== Processo Valida Boleto Agendamento Finalizado ==== ");
		}
	}
	
	private void processarEntradaSaida(Mtxtb004Canal canal, Mtxtb008MeioEntrada meioEntrada, Mtxtb011VersaoServico servico, Mtxtb014Transacao transacao, Mtxtb016IteracaoCanal iteracaoCanal, ParametrosAdicionais parametrosAdicionais) {
		TarefasServicoResposta tarefasServicoResposta = new TarefasServicoResposta();
		try {
		List<Mtxtb003ServicoTarefa> listaTarefas = gerenciadorTarefas.buscarTarefasValidaAgendamento(servico, canal, meioEntrada);

		logger.info("Preparando xml de requisicao para enviar ao Sibar");
		StringBuilder marcas = recuperaMarcas(servico.getId().getNuServico001());
		String xmlEntradaSibar = transformarXml(servico.getDeXsltRequisicao(), transacao, canal, marcas, null);
		
		xmlEntradaSibar = this.geradorPassosMigrado.gerarPassos(xmlEntradaSibar, servico, listaTarefas);
		this.dadosBarramento.escrever(xmlEntradaSibar);

		String xmlSaidaSibar = this.execucaoMq.executar(xmlEntradaSibar, servico.getMtxtb001Servico());
		this.dadosBarramento.escrever(xmlSaidaSibar);

		tarefasServicoResposta = this.gerenciadorTarefas.montarTarefasResposta(listaTarefas, servico, transacao,
				this.dadosBarramento, xmlSaidaSibar);
		
		logger.info("Preparando xml de resposta para enviar ao Canal");
		String xmlSaidaCanal = transformarXml(servico.getDeXsltResposta(), transacao, canal, null, tarefasServicoResposta.getResposta());
		enviarRespostaCanal(xmlSaidaCanal, parametrosAdicionais);

		BigDecimal situacaoTransacao = this.validadorRegrasNegocio.situacaoFinalTransacao(servico);
		
		transacao = atualizaStatusTransacao(transacao, situacaoTransacao);
		iteracaoCanal = montaMtxtb016IteracaoCanalTansacao(iteracaoCanal, transacao, xmlSaidaCanal);
		boolean statusAtualizacaoTransacao = atualizadorDadosTransacao.atualizarDadosTransacaoTarefasIteracoes(transacao, tarefasServicoResposta.getListaTransacaoTarefas(), iteracaoCanal);
		direcionaProcessadorMensagemTransacao(false, statusAtualizacaoTransacao, transacao, servico, dadosBarramento, xmlSaidaSibar);

		}catch (ServicoException se) {
			logger.error(se.getMensagem());
			parametrosAdicionais.setDadosBarramento(dadosBarramento);
			DadosTransacaoComuns dadosTransacaoComuns = new DadosTransacaoComuns(servico, transacao, tarefasServicoResposta.getListaTransacaoTarefas(), iteracaoCanal);
			this.tratadorDeExcecao.tratarExcecaoRetomada(dadosTransacaoComuns, se, parametrosAdicionais, true);
		}
		catch (Exception e) {
			logger.error(e.getMessage(), e);
			Mtxtb006Mensagem mensagemErroInterno = this.fornecedorDados.buscarMensagem(MensagemRetorno.ERRO_INTERNO);
			ServicoException se = new ServicoException(mensagemErroInterno,Constantes.ORIGEM_SIMTX);
			DadosTransacaoComuns dadosTransacaoComuns = new DadosTransacaoComuns(servico, transacao,tarefasServicoResposta.getListaTransacaoTarefas(), iteracaoCanal);
			this.tratadorDeExcecao.tratarExcecaoRetomada(dadosTransacaoComuns, se, parametrosAdicionais, true);
		}
	}
	
	/**
	 * Transforma o xml em outro a partir do xslt.
	 * 
	 * @param xslt
	 * @param dadosBarramento
	 * @return
	 * @throws ServicoException
	 */
	public String transformarXml(String xslt, Mtxtb014Transacao transacao, Mtxtb004Canal canal, StringBuilder marcas,
			Resposta mensagem) throws ServicoException {
		try {
			List<ParametroXsl> parametrosNovos = new ArrayList<>();
			parametrosNovos.add(new ParametroXsl(ParametroXsl.XSL_PARAM_NSU_SIMTX, String.valueOf(transacao.getNuNsuTransacao())));
			parametrosNovos.add(new ParametroXsl("agendamento", "sim"));
			parametrosNovos.add(new ParametroXsl(ParametroXsl.XSL_PARAM_REDE_TRANSMISSORA, String.valueOf(canal.getNuRedeTransmissora())));
			parametrosNovos.add(new ParametroXsl(ParametroXsl.XSL_PARAM_SEGMENTO, String.valueOf(canal.getNuSegmento())));
			
			if(marcas != null) {
				parametrosNovos.add(new ParametroXsl(ParametroXsl.XSL_PARAM_FUNCIONALIDADE, marcas.toString()));
			}
			
			if (mensagem != null) {
				parametrosNovos.add(new ParametroXsl(ParametroXsl.XSL_PARAM_COD_RETORNO, mensagem.getCodigo()));
				parametrosNovos.add(new ParametroXsl(ParametroXsl.XSL_PARAM_ACAO_RETORNO, AcaoRetorno.recuperarAcao(mensagem.getIcTipoMensagem()).getRotulo()));
				parametrosNovos.add(new ParametroXsl(ParametroXsl.XSL_PARAM_ORIGEM_RETORNO, mensagem.getOrigem()));
				parametrosNovos.add(new ParametroXsl(ParametroXsl.XSL_PARAM_MENSAGEM_NEGOCIAL, mensagem.getMensagemNegocial()));
				parametrosNovos.add(new ParametroXsl(ParametroXsl.XSL_PARAM_MENSAGEM_TECNICA, mensagem.getMensagemTecnica()));
			}
			ParametroXsl[] pArr = new ParametroXsl[parametrosNovos.size()];

			String caminhoXls = this.simtxConfig.getCaminhoXslt() + xslt;
			String arquivoXsl = this.repositorioArquivo.recuperarArquivo(caminhoXls);
			
			if(arquivoXsl.contains(Constantes.TAG_TRANSACAO_ORIGEM) && transacao.getNuNsuTransacaoPai() != 0) {
				this.dadosBarramento.escrever(recuperarTransacaoOrigemNoDadosBarramento(transacao.getNuNsuTransacaoPai()));
			}
			
			return new TransformadorXsl().transformar(this.dadosBarramento.getDadosLeitura(), arquivoXsl,
					parametrosNovos.toArray(pArr));
		} catch (Exception e) {
			logger.error(e);
			Mtxtb006Mensagem mensagemErroInterno = this.fornecedorDados.buscarMensagem(MensagemRetorno.ERRO_INTERNO);
			throw new ServicoException(mensagemErroInterno, Constantes.ORIGEM_SIMTX);
		}
	}
	
	public StringBuilder recuperaMarcas(Long nuServico) {
		StringBuilder funcionalidades = new StringBuilder();
		List<Mtxtb032MarcaConta> listaServico = this.fornecedorDados.buscarMarcasPorServico(nuServico);
		if(!listaServico.isEmpty()) {
			for(Mtxtb032MarcaConta servico: listaServico){
				funcionalidades.append("<FUNCIONALIDADE_ESPECIAL>"
					+ servico.getCoMarcaConta() + "</FUNCIONALIDADE_ESPECIAL>");
			}
		}
		return funcionalidades;
	}

	public GerenciadorTarefas getGerenciadorTarefas() {
		return gerenciadorTarefas;
	}


	public ValidadorRegrasNegocio getValidadorRegrasNegocio() {
		return validadorRegrasNegocio;
	}


	public void setValidadorRegrasNegocio(ValidadorRegrasNegocio validadorRegrasNegocio) {
		this.validadorRegrasNegocio = validadorRegrasNegocio;
	}

	public RepositorioArquivo getRepositorioArquivo() {
		return repositorioArquivo;
	}

	public void setRepositorioArquivo(RepositorioArquivo repositorioArquivo) {
		this.repositorioArquivo = repositorioArquivo;
	}

	public GeradorPassosMigrado getGeradorPassosMigrado() {
		return geradorPassosMigrado;
	}

	public void setGeradorPassosMigrado(GeradorPassosMigrado geradorPassosMigrado) {
		this.geradorPassosMigrado = geradorPassosMigrado;
	}

	public GerenciadorFilasMQ getExecucaoMq() {
		return execucaoMq;
	}

	public void setExecucaoMq(GerenciadorFilasMQ execucaoMq) {
		this.execucaoMq = execucaoMq;
	}

	public ProcessadorEnvioSicco getProcessadorEnvioSicco() {
		return processadorEnvioSicco;
	}

}
