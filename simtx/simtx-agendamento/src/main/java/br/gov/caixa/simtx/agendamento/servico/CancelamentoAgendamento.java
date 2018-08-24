package br.gov.caixa.simtx.agendamento.servico;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.apache.log4j.Logger;
import org.w3c.dom.Node;

import br.gov.caixa.simtx.agendamento.main.Agendamento;
import br.gov.caixa.simtx.persistencia.agendamento.entidade.Mtxtb034TransacaoAgendamento;
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
import br.gov.caixa.simtx.util.ConstantesAgendamento;
import br.gov.caixa.simtx.util.ParametrosAdicionais;
import br.gov.caixa.simtx.util.SimtxConfig;
import br.gov.caixa.simtx.util.exception.ServicoException;
import br.gov.caixa.simtx.util.gerenciador.GerenciadorTarefas;
import br.gov.caixa.simtx.util.gerenciador.GerenciadorTransacao;
import br.gov.caixa.simtx.util.gerenciador.TratadorDeExcecao;
import br.gov.caixa.simtx.util.gerenciador.ValidadorRegrasNegocio;
import br.gov.caixa.simtx.util.gerenciador.cache.RepositorioArquivo;
import br.gov.caixa.simtx.util.integrador.sicco.ProcessadorEnvioSicco;
import br.gov.caixa.simtx.util.mensagem.GerenciadorFilasMQ;
import br.gov.caixa.simtx.util.to.DadosTransacaoComuns;
import br.gov.caixa.simtx.util.to.TarefasServicoResposta;
import br.gov.caixa.simtx.util.xml.DadosBarramento;
import br.gov.caixa.simtx.util.xml.GeradorPassosMigrado;
import br.gov.caixa.simtx.util.xml.ParametroXsl;
import br.gov.caixa.simtx.util.xml.Resposta;
import br.gov.caixa.simtx.util.xml.TransformadorXsl;

@Stateless
public class CancelamentoAgendamento extends GerenciadorTransacao implements Serializable {

	private static final long serialVersionUID = 1L;

	private static final Logger logger = Logger.getLogger(CancelamentoAgendamento.class);
	
	protected DadosBarramento dadosBarramento;
	
	@Inject
	protected Agendamento agendamento;
	
	@Inject
	protected FornecedorDadosAgendamento fornecedorDadosAgendamento;
	
	@Inject
	protected GerenciadorFilasMQ execucaoMq;
	
	@Inject
	protected GeradorPassosMigrado geradorPassosMigrado;
	
	@Inject
	protected ValidadorRegrasNegocio validadorRegrasNegocio;
	
	@Inject
	protected SimtxConfig simtxConfig;
	
	@Inject
	protected ProcessadorEnvioSicco processadorEnvioSicco;
	
	@Inject
	private RepositorioArquivo repositorioArquivo;
	
	@Inject
	private AtualizadorDadosTransacao atualizadorDadosTransacao;
	
	@Inject
	private TratadorDeExcecao tratadorDeExcecao;
	
	
	private static final String AGENDAMETO_NSU = "Agendamento Nsu";
	
	
	
	
	/**
	 * Executa as tarefas no Sibar caso houver e muda o status para Cancelado na
	 * tabela de Agendamento.
	 * 
	 * @param transacao
	 * @param iteracaoCanal
	 * @param canal
	 * @param servico
	 * @param meioEntrada
	 * @param dadosBarramento
	 * @param
	 */
	public void processar(Mtxtb014Transacao transacao, Mtxtb016IteracaoCanal iteracaoCanal, Mtxtb004Canal canal,
			Mtxtb011VersaoServico servico, Mtxtb008MeioEntrada meioEntrada, DadosBarramento dadosBarramento,
			ParametrosAdicionais parametrosAdicionais) throws ServicoException {
		Mtxtb034TransacaoAgendamento transacaoAgendamento = null;
		TarefasServicoResposta tarefasServicoResposta = new TarefasServicoResposta();
		try {
			logger.info(" ==== Processo Cancelamento Agendamento Iniciado ==== ");
	
			this.dadosBarramento = dadosBarramento;
			
			
			List<Mtxtb003ServicoTarefa> listaTarefas = this.fornecedorDados.buscarTarefasMeioEntrada(
					meioEntrada.getNuMeioEntrada(), servico.getId().getNuServico001(),
					servico.getId().getNuVersaoServico());
			
			Node tagSimtx = this.dadosBarramento.xpath("/BUSDATA/*[2]/CANCELAMENTO_AGENDAMENTO/SIMTX");
			if(tagSimtx != null) {
				logger.info("Tag Simtx encontrada");
				String nsuAgendamento = this.dadosBarramento.xpathTexto("/BUSDATA/*[2]/CANCELAMENTO_AGENDAMENTO/SIMTX/NSU");
				if (!nsuAgendamento.isEmpty()) {
					transacaoAgendamento = obterTransacaoAgendamento(nsuAgendamento);
					tarefasServicoResposta = processarSimtx(transacaoAgendamento, transacao, canal, servico, listaTarefas);
					transacaoAgendamento.setIcSituacao(Constantes.AGENDAMENTO_IC_CANCELADO);
				}
			}
			else {
				logger.info("Tag Siaut encontrada");
				List<Mtxtb003ServicoTarefa> listaTarefasNegociais = this.gerenciadorTarefas
						.carregarTarefasServico(servico, canal, null);
				listaTarefasNegociais = listaTarefasNegociais.subList(0, 1);
				listaTarefas.addAll(listaTarefasNegociais);
				
				if(!listaTarefas.isEmpty()) {
					tarefasServicoResposta = executarTarefas(transacao, servico, listaTarefas);
				}
			}
			
			logger.info("Preparando xml de resposta para enviar ao Canal");
			String xmlSaidaCanal = transformarXml(servico.getDeXsltResposta(), transacao, tarefasServicoResposta.getResposta());
			enviarRespostaCanal(xmlSaidaCanal, parametrosAdicionais);
			
			logger.info("Preparando informações para atualizacao");
			BigDecimal situacaoTransacao = this.validadorRegrasNegocio.situacaoFinalTransacao(servico);
			transacao = atualizaStatusTransacao(transacao, situacaoTransacao);
			iteracaoCanal = montaMtxtb016IteracaoCanalTansacao(iteracaoCanal, transacao, xmlSaidaCanal);
			
			boolean statusAtualizacaoTransacao = atualizadorDadosTransacao.atualizarDadosTransacaoAgendamento(transacao, tarefasServicoResposta.getListaTransacaoTarefas(), iteracaoCanal, transacaoAgendamento);
			direcionaProcessadorMensagemTransacao(false, statusAtualizacaoTransacao, transacao, servico, dadosBarramento, tarefasServicoResposta.getXmlSaidaSibar());
		} 
		catch (ServicoException se) {
			logger.error(se.getMensagem());
			DadosTransacaoComuns dadosTransacaoComuns = new DadosTransacaoComuns(servico, transacao,tarefasServicoResposta.getListaTransacaoTarefas(), iteracaoCanal);
			parametrosAdicionais.setDadosBarramento(this.dadosBarramento);
			this.tratadorDeExcecao.tratarExcecaoRetomada(dadosTransacaoComuns, se, parametrosAdicionais, true);		
		} 
		catch (Exception e) {
			logger.error(e);
			Mtxtb006Mensagem mensagemErroInterno = this.fornecedorDados.buscarMensagem(MensagemRetorno.ERRO_INTERNO);
			ServicoException se = new ServicoException(mensagemErroInterno, Constantes.ORIGEM_SIMTX);
			DadosTransacaoComuns dadosTransacaoComuns = new DadosTransacaoComuns(servico, transacao,tarefasServicoResposta.getListaTransacaoTarefas(), iteracaoCanal);
			parametrosAdicionais.setDadosBarramento(this.dadosBarramento);
			this.tratadorDeExcecao.tratarExcecaoRetomada(dadosTransacaoComuns, se, parametrosAdicionais, true);		
		}
	}

	/**
	 * Processa quando a transacao esta armazenada no simtx.
	 * 
	 * @param transacao
	 * @param canal
	 * @param servico
	 * @param listaTarefas
	 * @return
	 * @throws ServicoException
	 */
	public TarefasServicoResposta processarSimtx(Mtxtb034TransacaoAgendamento transacaoAgendamento, Mtxtb014Transacao transacao, Mtxtb004Canal canal, Mtxtb011VersaoServico servico,
			List<Mtxtb003ServicoTarefa> listaTarefas) throws ServicoException {
		
		logger.info("Buscando Agendamento Nsu ["+transacaoAgendamento.getNuNsuTransacaoAgendamento()+"]");
		
		validarTransacaoAgendamento(transacaoAgendamento);
		
		this.dadosBarramento.escrever(transacaoAgendamento.getDeXmlAgendamento());
		
		List<Mtxtb003ServicoTarefa> listaTarefasNegociais = this.gerenciadorTarefas
				.carregarTarefasServico(servico, canal, null);
		listaTarefasNegociais = listaTarefasNegociais.subList(1, 2);
		listaTarefas.addAll(listaTarefasNegociais);
		
		TarefasServicoResposta tarefasServicoResposta = executarTarefas(transacao, servico, listaTarefas);
		
		if(tarefasServicoResposta.getResposta() == null) {
			Mtxtb006Mensagem mensagemSucesso = this.fornecedorDados
					.buscarMensagem(MensagemRetorno.SUCESSO);
			Resposta mensagem = new Resposta();
			mensagem.setIcTipoMensagem(mensagemSucesso.getIcTipoMensagem());
			mensagem.setCodigo(mensagemSucesso.getCodigoRetorno());
			mensagem.setMensagemNegocial(mensagemSucesso.getDeMensagemNegocial());
			mensagem.setMensagemTecnica(mensagemSucesso.getDeMensagemTecnica());
			mensagem.setOrigem(Constantes.ORIGEM_SIMTX);
			tarefasServicoResposta = new TarefasServicoResposta();
			tarefasServicoResposta.setResposta(mensagem);
		}
		return tarefasServicoResposta;
	}
	
	public Mtxtb034TransacaoAgendamento obterTransacaoAgendamento(String nsuAgendamento) {
		Mtxtb034TransacaoAgendamento transacaoAgendamento = new Mtxtb034TransacaoAgendamento();
		transacaoAgendamento.setNuNsuTransacaoAgendamento(Long.valueOf(nsuAgendamento));
		transacaoAgendamento = this.fornecedorDadosAgendamento.buscaTransacaoAgendamentoPorPK(transacaoAgendamento);
		return transacaoAgendamento;
	}

	/**
	 * Envias as tarefas/passo para o Sibar.
	 * 
	 * @param transacao
	 * @param servico
	 * @param listaTarefas
	 * @return
	 * @throws ServicoException
	 */
	public TarefasServicoResposta executarTarefas(Mtxtb014Transacao transacao, Mtxtb011VersaoServico servico,
			List<Mtxtb003ServicoTarefa> listaTarefas) throws ServicoException {
		logger.info("Executando Tarefas");
		
		logger.info("Preparando xml de requisicao para enviar ao Sibar");
		String xmlEntradaSibar = transformarXml(servico.getDeXsltRequisicao(), transacao, null);
		
		xmlEntradaSibar = this.geradorPassosMigrado.gerarPassos(xmlEntradaSibar, servico, listaTarefas);
		this.dadosBarramento.escrever(xmlEntradaSibar);
		
		String xmlSaidaSibar = this.execucaoMq.executar(xmlEntradaSibar, servico.getMtxtb001Servico());
		this.dadosBarramento.escrever(xmlSaidaSibar);
		
		return this.gerenciadorTarefas.montarTarefasResposta(listaTarefas, servico, transacao, this.dadosBarramento, xmlSaidaSibar);
	}
	
	/**
	 * Realiza as validacoes na TransacaoAgendamento para prosseguir com o
	 * cancelamento.
	 * 
	 * @param transacaoAgendamento
	 * @param mensagem
	 * @param nsu
	 * @throws ServicoException
	 */
	public void validarTransacaoAgendamento(Mtxtb034TransacaoAgendamento transacaoAgendamento)
			throws ServicoException {
			
		if(transacaoAgendamento == null) {
			logger.error("Agendamento nao encontrado no banco de dados");
			Mtxtb006Mensagem mensagemErroInterno = this.fornecedorDados
					.buscarMensagem(MensagemRetorno.AGENDAMENTO_NAO_ENCONTRADO);
			throw new ServicoException(mensagemErroInterno, Constantes.ORIGEM_SIMTX);
		}
		else if (transacaoAgendamento.getIcSituacao() == ConstantesAgendamento.SITUACAO_CANCELADO) {
			logger.error(AGENDAMETO_NSU + " ["+transacaoAgendamento.getNuNsuTransacaoAgendamento()+"] ja esta cancelado");
			Mtxtb006Mensagem mensagemErroInterno = this.fornecedorDados
					.buscarMensagem(MensagemRetorno.AGENDAMENTO_JA_CANCELADO);
			throw new ServicoException(mensagemErroInterno, Constantes.ORIGEM_SIMTX);
		}
		else if (transacaoAgendamento.getIcSituacao() == ConstantesAgendamento.SITUACAO_EFETIVADO) {
			logger.error(AGENDAMETO_NSU + "[\"+transacaoAgendamento.getNuNsuTransacaoAgendamento()+\"] ja esta efetivado");
			Mtxtb006Mensagem mensagemErroInterno = this.fornecedorDados
					.buscarMensagem(MensagemRetorno.AGENDAMENTO_JA_EFETIVADO);
			throw new ServicoException(mensagemErroInterno, Constantes.ORIGEM_SIMTX);
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
	public String transformarXml(String xslt, Mtxtb014Transacao transacao, Resposta mensagem) throws ServicoException {
		try {
			List<ParametroXsl> parametrosNovos = new ArrayList<>();
			parametrosNovos.add(new ParametroXsl("nsuSimtx", String.valueOf(transacao.getNuNsuTransacao())));
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
			return new TransformadorXsl().transformar(this.dadosBarramento.getDadosLeitura(), arquivoXsl,
					parametrosNovos.toArray(pArr));
		} catch (Exception e) {
			logger.error(e);
			Mtxtb006Mensagem mensagemErroInterno = this.fornecedorDados.buscarMensagem(MensagemRetorno.ERRO_INTERNO);
			throw new ServicoException(mensagemErroInterno, Constantes.ORIGEM_SIMTX);
		}
	}
	
	
	public void setSimtxConfig(SimtxConfig simtxConfig) {
		this.simtxConfig = simtxConfig;
	}

	public void setGerenciadorTarefas(GerenciadorTarefas gerenciadorTarefas) {
		this.gerenciadorTarefas = gerenciadorTarefas;
	}

	public void setFornecedorDadosAgendamento(FornecedorDadosAgendamento fornecedorDadosAgendamento) {
		this.fornecedorDadosAgendamento = fornecedorDadosAgendamento;
	}

	public void setValidadorRegrasNegocio(ValidadorRegrasNegocio validadorRegrasNegocio) {
		this.validadorRegrasNegocio = validadorRegrasNegocio;
	}
	
	public void setDadosBarramento(DadosBarramento dadosBarramento) {
		this.dadosBarramento = dadosBarramento;
	}
	
}
