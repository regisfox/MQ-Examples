package br.gov.caixa.simtx.util.gerenciador;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.apache.log4j.Logger;
import org.w3c.dom.Node;

import br.gov.caixa.simtx.persistencia.agendamento.entidade.Mtxtb034TransacaoAgendamento;
import br.gov.caixa.simtx.persistencia.cache.core.FornecedorDados;
import br.gov.caixa.simtx.persistencia.constante.Constantes;
import br.gov.caixa.simtx.persistencia.constante.MensagemRetorno;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb004Canal;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb006Mensagem;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb008MeioEntrada;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb011VersaoServico;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb014Transacao;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb015SrvcoTrnsoTrfa;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb016IteracaoCanal;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb016IteracaoCanalPK;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb017VersaoSrvcoTrnso;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb017VersaoSrvcoTrnsoPK;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb035TransacaoConta;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb036TransacaoAuditada;
import br.gov.caixa.simtx.persistencia.exception.SemResultadoException;
import br.gov.caixa.simtx.util.ParametrosAdicionais;
import br.gov.caixa.simtx.util.data.DataUtil;
import br.gov.caixa.simtx.util.exception.ServicoException;
import br.gov.caixa.simtx.util.exception.SimtxException;
import br.gov.caixa.simtx.util.integrador.sicco.ProcessadorEnvioSicco;
import br.gov.caixa.simtx.util.mensagem.FilaCanal;
import br.gov.caixa.simtx.util.retomar.transacao.ProcessadorEnvioRetomadaTransacao;
import br.gov.caixa.simtx.util.to.DadosTransacaoComuns;
import br.gov.caixa.simtx.util.xml.BuscadorTextoXml;
import br.gov.caixa.simtx.util.xml.DadosBarramento;


@Stateless
public class GerenciadorTransacao implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private static final Logger logger = Logger.getLogger(GerenciadorTransacao.class);
	
	@Inject
	protected FornecedorDados fornecedorDados;
	
	@Inject
	protected Mensagem mensagemServidor;
	
	@Inject
	protected GerenciadorTarefas gerenciadorTarefas;
	
	protected Mtxtb004Canal canalSimtx = new Mtxtb004Canal(Constantes.CODIGO_CANAL_SIMTX);
	
	@Inject
	private ProcessadorEnvioRetomadaTransacao processadorEnvioRetomadaTransacao;
	
	@Inject
	private ProcessadorEnvioSicco processadorEnvioSicco;
	
	@Inject
	protected ValidadorRegrasNegocio validadorRegrasNegocio;
	
	private long codigoCanalSimtx = 114L;
	
	private String canalNaoEncontradoMsg = "Canal nao encontrado";
	
	private String atualizaIteracaoMsg = "Atualizando IteracaoCanal - Transacao Nsu [";

	/**
	 * Busca o servico e a versao na entidade Mtxtb011VersaoServico.
	 * 
	 * @param buscador
	 * @return {@link Mtxtb011VersaoServico}
	 * @throws SemResultadoException
	 */
	public Mtxtb011VersaoServico buscarServico(Long codigoServico, Integer versaoServico) throws ServicoException {
		try {
			logger.info("Buscando o servico ["+codigoServico+"] no banco");
			Mtxtb011VersaoServico servico = fornecedorDados.buscarVersaoServico(versaoServico, codigoServico);
			logger.info("Servico chamado: "+servico.getMtxtb001Servico().getNoServico()+" - Versao "+versaoServico);
			return servico;
		} 
		catch (Exception e) {
			logger.error(e);
			Mtxtb006Mensagem mensagem = this.fornecedorDados.buscarMensagem(MensagemRetorno.VERSAO_SERVICO_INEXISTENTE);
			throw new ServicoException(mensagem, Constantes.ORIGEM_SIMTX);
		}
	}
	
	/**
	 * Salva a entidade Transacao.
	 * 
	 * @param buscador
	 * @param canal
	 * @return {@link Mtxtb014Transacao}
	 * @throws Exception
	 */
	public Mtxtb014Transacao salvarTransacao(BuscadorTextoXml buscador, Mtxtb004Canal canal)
			throws ServicoException {
		try {
			logger.info("Gravando transacao");
			SimpleDateFormat formatoDataHora = new SimpleDateFormat("yyyyMMddHHmmss");
			SimpleDateFormat formatoData = new SimpleDateFormat("yyyyMMdd");
			formatoData.setLenient(false);
			formatoDataHora.setLenient(false);
			
			Date dataAtual = formatoData.parse(formatoData.format(new Date()));
			Date dataHoraCanal =  formatoDataHora.parse(buscador.xpathTexto("/*[1]/HEADER/CANAL/DATAHORA"));
			Long nuNsuOrigem = buscador.xpathTexto("/*[1]/NSUMTX_ORIGEM").isEmpty()
					? 0L : Long.parseLong(buscador.xpathTexto("/*[1]/NSUMTX_ORIGEM"));
			
			Mtxtb014Transacao transacao = new Mtxtb014Transacao();
			transacao.setCoCanalOrigem(String.valueOf(canal.getNuCanal()));
			transacao.setNuNsuTransacaoPai(nuNsuOrigem);
			transacao.setIcSituacao(Constantes.IC_SERVICO_EM_ANDAMENTO);
			transacao.setDhMultiCanal(new Date());
			transacao.setDtReferencia(dataAtual);
			transacao.setDhTransacaoCanal(dataHoraCanal);
			transacao.setIcEnvio(BigDecimal.ZERO);
			transacao.setIcRetorno(BigDecimal.ZERO);
			transacao.setTsAtualizacao(new Date());
			transacao = this.fornecedorDados.salvarTransacao(transacao);
			logger.info("NSUMTX gerado: " + transacao.getNuNsuTransacao());
			return transacao;
		} 
		catch (Exception e) {
			logger.error(e);
			Mtxtb006Mensagem mensagemErroInterno = this.fornecedorDados.buscarMensagem(MensagemRetorno.NSU_NAO_GERADO);
			throw new ServicoException(mensagemErroInterno,Constantes.ORIGEM_SIMTX);
		}
	}
	
	/**
	 * Salva entidade IteracaoCanal.
	 * 
	 * @param buscador
	 * @param mensagem
	 * @param transacao
	 * @return {@link Mtxtb016IteracaoCanal}
	 * @throws Exception
	 */
	public Mtxtb016IteracaoCanal salvarIteracaoCanal(BuscadorTextoXml buscador, String mensagem,
			Mtxtb014Transacao transacao) throws ServicoException {
		logger.info("Gravando IteracaoCanal - Transacao Nsu [" + transacao.getNuNsuTransacao() + "]");
		try {
			String terminal = "";
			if(buscador != null)
				terminal = buscador.xpathTexto("/*[1]/HEADER/IDENTIFICADOR_ORIGEM/TERMINAL");

			Mtxtb016IteracaoCanal iteracao = new Mtxtb016IteracaoCanal();
			iteracao.setMtxtb004Canal(new Mtxtb004Canal());
			iteracao.getMtxtb004Canal().setNuCanal(Long.parseLong(transacao.getCoCanalOrigem()));
			iteracao.setMtxtb014Transacao(transacao);
			iteracao.setTsRecebimentoSolicitacao(DataUtil.getDataAtual());
			iteracao.setTsRetornoSolicitacao(DataUtil.getDataAtual());
			iteracao.setId(new Mtxtb016IteracaoCanalPK());
			iteracao.getId().setNuNsuTransacao014(transacao.getNuNsuTransacao());
			iteracao.setDeRecebimento(mensagem);
			iteracao.setDtReferencia(transacao.getDtReferencia());
			iteracao.setCodTerminal(terminal);
			return this.fornecedorDados.salvarIteracaoCanal(iteracao);
		} 
		catch (Exception e) {
			logger.error(e);
			Mtxtb006Mensagem mensagemErroInterno = this.fornecedorDados.buscarMensagem(MensagemRetorno.ERRO_GRAVACAO_TRANSACAO);
			throw new ServicoException(mensagemErroInterno,Constantes.ORIGEM_SIMTX);
		}
	}
	
	/**
	 * Salva a entidade {@link Mtxtb017VersaoSrvcoTrnso}.
	 * 
	 * @param transacao
	 * @param versaoServico
	 * @throws Exception
	 */
	public void salvarTransacaoServico(Mtxtb014Transacao transacao, Mtxtb011VersaoServico versaoServico)
			throws ServicoException {
		try {
			logger.info("Gravando Servico Transacao Nsu [" + transacao.getNuNsuTransacao() + "]");
			Mtxtb017VersaoSrvcoTrnso versaoServicoTransacao = new Mtxtb017VersaoSrvcoTrnso();
			versaoServicoTransacao.setTsSolicitacao(DataUtil.getDataAtual());
			versaoServicoTransacao.setId(new Mtxtb017VersaoSrvcoTrnsoPK());
			versaoServicoTransacao.getId().setNuNsuTransacao014(transacao.getNuNsuTransacao());
			versaoServicoTransacao.getId().setNuServico011(versaoServico.getId().getNuServico001());
			versaoServicoTransacao.getId().setNuVersaoServico011(versaoServico.getId().getNuVersaoServico());
			versaoServicoTransacao.setDtReferencia(transacao.getDtReferencia());
			this.fornecedorDados.salvarTransacaoServico(versaoServicoTransacao);
		} 
		catch (Exception e) {
			logger.error(e);
			Mtxtb006Mensagem mensagem = this.fornecedorDados.buscarMensagem(MensagemRetorno.ERRO_GRAVACAO_TRANSACAO);
			throw new ServicoException(mensagem, Constantes.ORIGEM_SIMTX);
		}
	}
	
	/**
	 * Atualiza a Transacao com a nova situacao.
	 * 
	 * @param situacao
	 * @throws ServicoException
	 */
	public void atualizaTransacao(Mtxtb014Transacao transacao, BigDecimal situacao) throws ServicoException {
		try {
			if(transacao != null) {
				logger.info("Atualizando Transacao Nsu [" + transacao.getNuNsuTransacao() + "]");
				transacao.setIcSituacao(situacao);
				this.fornecedorDados.alterarTransacao(transacao);
			}
		} 
		catch (Exception e) {
			logger.error(e);
			Mtxtb006Mensagem mensagem = this.fornecedorDados.buscarMensagem(MensagemRetorno.ERRO_GRAVACAO_TRANSACAO);
			throw new ServicoException(mensagem, Constantes.ORIGEM_SIMTX);
		}
	}
	
	/**
	 * Atualiza a Transacao com a nova situacao.
	 * 
	 * @param situacao
	 * @throws ServicoException
	 */
	public Mtxtb014Transacao atualizaStatusTransacao(Mtxtb014Transacao transacao, BigDecimal situacao){
		if(transacao != null) {
			transacao.setIcSituacao(situacao);
		}
		return transacao;
	}

	/**
	 * Atualiza a IteracaoCanal com o xml de resposta enviada para o Canal.
	 * 
	 * @param mensagem
	 * @throws ServicoException
	 */
	public void atualizaIteracaoCanal(Mtxtb016IteracaoCanal iteracaoCanal, Mtxtb014Transacao transacao, String mensagem)
			throws ServicoException {
		try {
			if(iteracaoCanal != null) {
				logger.info(atualizaIteracaoMsg + transacao.getNuNsuTransacao() + "]");
				iteracaoCanal.setMtxtb014Transacao(transacao);
				iteracaoCanal.setDeRetorno(mensagem);
				iteracaoCanal.setTsRetornoSolicitacao(DataUtil.getDataAtual());
				this.fornecedorDados.alterarIteracaoCanal(iteracaoCanal);
			}
		} 
		catch (Exception e) {
			logger.error(e);
			Mtxtb006Mensagem mensagemBanco = this.fornecedorDados.buscarMensagem(MensagemRetorno.ERRO_GRAVACAO_TRANSACAO);
			throw new ServicoException(mensagemBanco, Constantes.ORIGEM_SIMTX);
		}
	}
	
	/**
	 * Atualiza a IteracaoCanal com o xml de resposta enviada para o Canal.
	 * 
	 * @param mensagem
	 * @throws ServicoException
	 */
	public Mtxtb016IteracaoCanal montarAtualizaIteracaoCanal(Mtxtb016IteracaoCanal iteracaoCanal, Mtxtb014Transacao transacao, String mensagem) {
			if(iteracaoCanal != null) {
				logger.info(atualizaIteracaoMsg + transacao.getNuNsuTransacao() + "]");
				iteracaoCanal.setMtxtb014Transacao(transacao);
				iteracaoCanal.setDeRetorno(mensagem);
				iteracaoCanal.setTsRetornoSolicitacao(DataUtil.getDataAtual());
			}
			return iteracaoCanal;
	}
	
	/**
	 * Atualiza a IteracaoCanal com o xml de resposta enviada para o Canal.
	 * 
	 * @param mensagem
	 * @throws ServicoException
	 */
	public Mtxtb016IteracaoCanal montaMtxtb016IteracaoCanalTansacao(Mtxtb016IteracaoCanal iteracaoCanal, Mtxtb014Transacao transacao, String mensagem)
			throws ServicoException {
		try {
			if(iteracaoCanal != null) {
				logger.info(atualizaIteracaoMsg + transacao.getNuNsuTransacao() + "]");
				iteracaoCanal.setMtxtb014Transacao(transacao);
				iteracaoCanal.setDeRetorno(mensagem);
				iteracaoCanal.setTsRetornoSolicitacao(DataUtil.getDataAtual());
			}
		} 
		catch (Exception e) {
			logger.error(e);
			Mtxtb006Mensagem mensagemBanco = this.fornecedorDados.buscarMensagem(MensagemRetorno.ERRO_GRAVACAO_TRANSACAO);
			throw new ServicoException(mensagemBanco, Constantes.ORIGEM_SIMTX);
		}
		return iteracaoCanal;

	}
	
	/**
	 * Montar dados a transacaoConta
	 * 
	 * @param situacaoTransacao
	 * @param transacao
	 * @param iteracaoCanal
	 * @param canal
	 * @param versaoServico
	 * @param xml
	 * @throws ServicoException 
	 */
	public Mtxtb035TransacaoConta montarDadosTransacaoConta(BigDecimal situacaoTransacao, Mtxtb014Transacao transacao,
			Mtxtb016IteracaoCanal iteracaoCanal, Mtxtb004Canal canal, Mtxtb011VersaoServico versaoServico, String xml) throws ServicoException{
			Mtxtb035TransacaoConta transacaoConta = null;
			try {
				if(situacaoTransacao.compareTo(Constantes.IC_SERVICO_FINALIZADO) == 0 && versaoServico.getMtxtb001Servico().isCancelamento()) {
					logger.info("montando dados TransacaoConta");
					
					transacaoConta = new Mtxtb035TransacaoConta();
					transacaoConta.setDataReferencia(transacao.getDtReferencia());
					transacaoConta.setIcSituacao(situacaoTransacao.longValue());
					transacaoConta.setMtxtb016IteracaoCanal(iteracaoCanal);
					
					BuscadorTextoXml buscador = new BuscadorTextoXml(xml);
					Node tag = buscador.xpath(Constantes.PATH_CONTA_SIDEC);
					int tipoConta = 1;
					String tipoContaTexto = Constantes.PATH_CONTA_SIDEC;
					String operacao = "/OPERACAO";
					if (tag == null) {
						tipoConta = 2;
						tipoContaTexto = "/*[1]/CONTA/CONTA_NSGD";
						operacao = "/PRODUTO";
					}
					int nuUnidade 	= Integer.parseInt(buscador.xpathTexto(tipoContaTexto+"/UNIDADE"));
					int nuProduto 	= Integer.parseInt(buscador.xpathTexto(tipoContaTexto+operacao));
					long nuConta 	= Long.parseLong(buscador.xpathTexto(tipoContaTexto+"/CONTA"));
					int dvConta 	= Integer.parseInt(buscador.xpathTexto(tipoContaTexto+"/DV"));
					
					transacaoConta.setNumeroUnidade(nuUnidade);
					transacaoConta.setOpProduto(nuProduto);
					transacaoConta.setNumeroConta(nuConta);
					transacaoConta.setNuDvConta(dvConta);
					transacaoConta.setIndicadorConta(tipoConta);
		
					transacaoConta.setNumeroCanal(canal.getNuCanal());
					transacaoConta.setNumeroServico(versaoServico.getId().getNuServico001());
					transacaoConta.setNumeroVersaoServico( (int) versaoServico.getId().getNuVersaoServico());
					transacaoConta.setNuNsuTransacao(transacao.getNuNsuTransacao());
				}
				return transacaoConta;
			} 
			catch (Exception e) {
				logger.error(e);
				Mtxtb006Mensagem mensagemErroInterno = this.fornecedorDados.buscarMensagem(MensagemRetorno.ERRO_INTERNO);
				throw new ServicoException(mensagemErroInterno,Constantes.ORIGEM_SIMTX);
			}
	}
	
	/**
	 * Salva as tarefas do Servico.
	 * 
	 * @param listaTarefas
	 * @param transacao
	 * @throws ServicoException
	 */
	public void salvarTarefasServico(List<Mtxtb015SrvcoTrnsoTrfa> listaTarefas, Mtxtb014Transacao transacao)
			throws ServicoException {
		try {
			logger.info("Salvando tarefas do servico - Transacao Nsu [" + transacao.getNuNsuTransacao() + "]");
			for(Mtxtb015SrvcoTrnsoTrfa srvcoTrnsoTrfa : listaTarefas) {
				logger.info("Salvando Tarefa ["+srvcoTrnsoTrfa.getId().getNuTarefa012()+"] "
						+ srvcoTrnsoTrfa.getMtxtb012VersaoTarefa().getMtxtb002Tarefa().getNoTarefa()+"");
				srvcoTrnsoTrfa.getId().setNuNsuTransacao017(transacao.getNuNsuTransacao());
				srvcoTrnsoTrfa.setDtReferencia(transacao.getDtReferencia());
				this.fornecedorDados.salvarTransacaoTarefa(srvcoTrnsoTrfa);
			}
		}
		catch (Exception e) {
			logger.error(this.mensagemServidor.recuperarMensagem("erro.salvar.tarefas"), e);
			Mtxtb006Mensagem mensagemErroInterno = this.fornecedorDados.buscarMensagem(MensagemRetorno.ERRO_INTERNO);
			throw new ServicoException(mensagemErroInterno, Constantes.ORIGEM_SIMTX);
		}
	}
	
	/**
	 * Buscar a entidade Canal.
	 * 
	 * @param buscador
	 * @return {@link Mtxtb004Canal}
	 * @throws Exception
	 */
	public Mtxtb004Canal buscarCanal(BuscadorTextoXml buscador) {
		Mtxtb004Canal canal = new Mtxtb004Canal();
		try {
			logger.info("Buscando canal");
			String siglaCanal = buscador.xpathTexto("/*[1]/HEADER/CANAL/SIGLA");
			canal.setSigla(siglaCanal);
			return fornecedorDados.buscarCanalPorSigla(canal);
		} 
		catch (Exception e) {
			logger.error(canalNaoEncontradoMsg);
			canal.setNuCanal(Constantes.CODIGO_CANAL_INEXISTENTE.longValue());
			return canal;
		}
	}
	
	/**
	 * Buscar a entidade Canal.
	 * 
	 * @param buscador
	 * @return {@link Mtxtb004Canal}
	 * @throws Exception
	 */
	public Mtxtb004Canal buscarCanalPorSigla(String sgCanal) {
		Mtxtb004Canal canal = new Mtxtb004Canal();
		try {
			logger.info("Buscando canal");
			canal.setSigla(sgCanal);
			return this.fornecedorDados.buscarCanalPorSigla(canal);
		} 
		catch (Exception e) {
			logger.error(canalNaoEncontradoMsg);
			canal.setNuCanal(Constantes.CODIGO_CANAL_INEXISTENTE.longValue());
			return canal;
		}
	}
	
	/**
	 * Recupera o xml da transacao origem.
	 * 
	 * @param nsu
	 * @return
	 * @throws ServicoException
	 */
	public String recuperarTransacaoOrigemNoDadosBarramento(Long nsu) throws ServicoException {
		Mtxtb014Transacao transacaoOrigem = this.fornecedorDados.buscarTransacaoOrigem(nsu);
		if(transacaoOrigem != null) {
			String xmlEntrada = transacaoOrigem.getMtxtb016IteracaoCanals().get(0).getDeRecebimento();
			String xmlSaida = transacaoOrigem.getMtxtb016IteracaoCanals().get(0).getDeRetorno();
			
			String xmlTarefas = carregarTarefasTransacao(nsu).toString();
			
			return "<TRANSACAO_ORIGEM>" + xmlEntrada + xmlSaida + xmlTarefas + "</TRANSACAO_ORIGEM>";
		}
		else {
			return "";
		}
	}
	
	/**
	 * Carrega os xmls das Tarefas.
	 * 
	 * @param nsu
	 * @throws ServicoException
	 */
	public StringBuilder carregarTarefasTransacao(Long nsu) throws ServicoException {
		try {
			List<Mtxtb015SrvcoTrnsoTrfa> tarefasExecutadas = this.fornecedorDados.buscarTarefasPorNsu(nsu);
			StringBuilder xml = new StringBuilder();
			
			for(Mtxtb015SrvcoTrnsoTrfa tarefa : tarefasExecutadas) {
				xml.append(tarefa.getDeXmlRequisicao().replaceAll("<\\?xml.*\\?>", ""));
				xml.append(tarefa.getDeXmlResposta().replaceAll("<\\?xml.*\\?>", ""));
			}
			return xml;
		} 
		catch (Exception e) {
			logger.error(e);
			Mtxtb006Mensagem mensagemErroInterno = this.fornecedorDados
					.buscarMensagem(MensagemRetorno.ERRO_RECUPERAR_TAREFAS);
			throw new ServicoException(mensagemErroInterno, Constantes.ORIGEM_SIMTX);
		}
	}
	
	/**
	 * Envia a resposta para o canal.
	 * 
	 * @param mensagem
	 * @param parametrosAdicionais
	 */
	public void enviarRespostaCanal(String mensagem, ParametrosAdicionais parametrosAdicionais) {
		Mtxtb004Canal canalNovo = new Mtxtb004Canal();
		canalNovo.setNoFilaRspCanal(parametrosAdicionais.getJndiResponseQueue());
		canalNovo.setNoConexaoCanal(parametrosAdicionais.getJndiQueueConnectionFactory());
		new FilaCanal().postarMensagem(parametrosAdicionais.getIdMessage(), mensagem, canalNovo,
				parametrosAdicionais.isConverterRespostaParaJson());
	}
	
	public void salvarTransacaoConta(BigDecimal situacaoTransacao, Mtxtb014Transacao transacao,
			Mtxtb016IteracaoCanal iteracaoCanal, Mtxtb004Canal canal, Mtxtb011VersaoServico versaoServico, String xml)
			throws ServicoException {
		try {
			if (situacaoTransacao.compareTo(Constantes.IC_SERVICO_FINALIZADO) == 0
					&& versaoServico.getMtxtb001Servico().isCancelamento()) {
				logger.info("Salvando TransacaoConta");
			
				Mtxtb035TransacaoConta transacaoConta = new Mtxtb035TransacaoConta();
				transacaoConta.setDataReferencia(transacao.getDtReferencia());
				transacaoConta.setIcSituacao(situacaoTransacao.longValue());
				transacaoConta.setMtxtb016IteracaoCanal(iteracaoCanal);
				
				BuscadorTextoXml buscador = new BuscadorTextoXml(xml);
				Node tag = buscador.xpath(Constantes.PATH_CONTA_SIDEC);
				int tipoConta = 1;
				String tipoContaTexto = Constantes.PATH_CONTA_SIDEC;
				String operacao = "/OPERACAO";
				if (tag == null) {
					tipoConta = 2;
					tipoContaTexto = "/*[1]/CONTA/CONTA_NSGD";
					operacao = "/PRODUTO";
				}
				int nuUnidade 	= Integer.parseInt(buscador.xpathTexto(tipoContaTexto+"/UNIDADE"));
				int nuProduto 	= Integer.parseInt(buscador.xpathTexto(tipoContaTexto+operacao));
				long nuConta 	= Long.parseLong(buscador.xpathTexto(tipoContaTexto+"/CONTA"));
				int dvConta 	= Integer.parseInt(buscador.xpathTexto(tipoContaTexto+"/DV"));
				
				transacaoConta.setNumeroUnidade(nuUnidade);
				transacaoConta.setOpProduto(nuProduto);
				transacaoConta.setNumeroConta(nuConta);
				transacaoConta.setNuDvConta(dvConta);
				transacaoConta.setIndicadorConta(tipoConta);
	
				transacaoConta.setNumeroCanal(canal.getNuCanal());
				transacaoConta.setNumeroServico(versaoServico.getId().getNuServico001());
				transacaoConta.setNumeroVersaoServico( (int) versaoServico.getId().getNuVersaoServico());
				transacaoConta.setNuNsuTransacao(transacao.getNuNsuTransacao());
				this.fornecedorDados.salvarTransacaoConta(transacaoConta);
			} 
		}
		catch (Exception e) {
			logger.error(e);
			Mtxtb006Mensagem mensagemErroInterno = this.fornecedorDados.buscarMensagem(MensagemRetorno.ERRO_INTERNO);
			throw new ServicoException(mensagemErroInterno, Constantes.ORIGEM_SIMTX);
		}
	}
	
	/**
	 * monta informações para a entidade a Transação Auditada.
	 * 
	 */
	public Mtxtb036TransacaoAuditada montarTransacaoAuditada(Mtxtb014Transacao mtxtb014Transacao, Mtxtb004Canal mtxtb004Canal, Long nsuTransacaoAuditada,
			Mtxtb011VersaoServico servicoCancelamento) {
		Mtxtb036TransacaoAuditada transacaoCancelamento = null;
		try {
			logger.info("inicio montarTransacaoAuditada");
			
			transacaoCancelamento = new Mtxtb036TransacaoAuditada();
			transacaoCancelamento.setNuTransacaoAuditada(nsuTransacaoAuditada);
			transacaoCancelamento.setNuServico(servicoCancelamento.getId().getNuServico001());
			transacaoCancelamento.setNuVersaoServico(servicoCancelamento.getId().getNuVersaoServico());
			
			transacaoCancelamento.setNuTransacaoOrigem(mtxtb014Transacao.getNuNsuTransacao());
			transacaoCancelamento.setNuCanalOrigem(mtxtb004Canal.getNuCanal());
			transacaoCancelamento.setNuServicoOrigem(servicoCancelamento.getId().getNuServico001());
			transacaoCancelamento.setNuVersaoServicoOrigem(servicoCancelamento.getId().getNuVersaoServico());
			
//			transacaoCancelamento.setCoUsuario(mtxTransacaoConta.getCodigoUsuario());
//			transacaoCancelamento.setCoMaquinaInclusao(mtxTransacaoConta.getCodigoMaquina());
			
			transacaoCancelamento.setTsInclusao(DataUtil.getDataAtual());
			
			logger.info("fim montarTransacaoAuditada");


		} 
		catch (Exception e) {
			logger.error("Nao foi possivel gravar na tabela TransacaoAuditada", e);
		}
		return transacaoCancelamento;
	}
	
	/**
	 * Busca o servico e a versao na entidade Mtxtb011VersaoServico.
	 * 
	 * @param buscador
	 * @return {@link Mtxtb011VersaoServico}
	 * @throws SemResultadoException
	 */
	public Mtxtb011VersaoServico buscarServicoCancelamento() throws ServicoException {
		try {
			logger.info("Buscando o servico [" + Constantes.CODIGO_SERVICO_CANCELAMENTO + "] no banco");
			return this.fornecedorDados.buscarVersaoServico(Constantes.VERSAO_SERVICO_CANCELAMENTO, Constantes.CODIGO_SERVICO_CANCELAMENTO);
		} catch (Exception e) {
			logger.error(e);
			Mtxtb006Mensagem mensagemErroInterno = this.fornecedorDados.buscarMensagem(MensagemRetorno.SERVICO_INEXISTENTE_PARA_CANAL);
			throw new ServicoException(mensagemErroInterno,Constantes.ORIGEM_SIMTX);
			
			
		}
	}

	
	
	/**
	 * monta informações para a entidade a Transação Auditada.
	 * 
	 */
	public Mtxtb035TransacaoConta buscarMtxtb035TransacaoConta(Long nsuTransacaoConta) {
		Mtxtb035TransacaoConta mtxtb035TransacaoConta = null;
		try {			
			mtxtb035TransacaoConta = fornecedorDados.buscarTransacaoContaPorNsu(nsuTransacaoConta);
		} 
		catch (Exception e) {
			logger.error("Não foi possivel realizar a busca buscarMtxtb035TransacaoConta", e);
		}
		return mtxtb035TransacaoConta;
	}
	
	public void direcionaProcessadorMensagemTransacao(boolean validaFinalizado, boolean statusAtualizacaoTransacao, Mtxtb014Transacao transacao,
			Mtxtb011VersaoServico servicoOrigem, DadosBarramento dadosBarramento, String xmlSaidaSibar) {
		if (!statusAtualizacaoTransacao) {
			processadorEnvioRetomadaTransacao.envioRetomadaTransacao(transacao, servicoOrigem, dadosBarramento, xmlSaidaSibar);
		} else {
			if (validaFinalizado) {
				if (transacao.getIcSituacao().compareTo(new BigDecimal(3)) != 0) {
					processadorEnvioSicco.processarEnvioOnline(transacao, Constantes.ENVIO_UNICA);
				}
			} else {
				processadorEnvioSicco.processarEnvioOnline(transacao, Constantes.ENVIO_UNICA);
			}
		}
	}
	
	/**
	 * Buscar a entidade Canal.
	 * 
	 * @return {@link Mtxtb004Canal}
	 * @throws Exception
	 */
	public Mtxtb004Canal buscarCanalSIMTX() {
		Mtxtb004Canal canal = new Mtxtb004Canal();
		try {
			logger.info("Buscando buscarCanalSIMTX");
			canal.setNuCanal(codigoCanalSimtx);
			return this.fornecedorDados.buscarCanalPorPK(canal);
		} 
		catch (Exception e) {
			logger.error(canalNaoEncontradoMsg, e);
			canal.setNuCanal(Constantes.CODIGO_CANAL_INEXISTENTE.longValue());
			return canal;
		}
	}
	
	
	/**
	 * Salva a entidade MtxtbxxxTransacaoAgendamento.
	 * 
	 * @param transacao
	 * @param servicoAgendamento
	 * @param canal
	 * @param iteracaoCanal
	 * @param dadosBarramento
	 * @throws ServicoException
	 */
	public Mtxtb034TransacaoAgendamento montarTransacaoAgendamento(Mtxtb014Transacao transacao, Mtxtb011VersaoServico servicoOrigem,
			Mtxtb004Canal canal, Mtxtb016IteracaoCanal iteracaoCanal)
			throws ServicoException {

		try {
			BuscadorTextoXml buscador = new BuscadorTextoXml(iteracaoCanal.getDeRecebimento());

			logger.info("Salvando transacao de agendamento");
			
			Node tag = buscador.xpath("/CONTA/CONTA_SIDEC");
			int tipoConta = 1;
			String tipoContaTexto = "/CONTA/CONTA_SIDEC";
			String operacao = "/OPERACAO";
			if (tag == null) {
				tipoConta = 2;
				tipoContaTexto = "/CONTA/CONTA_NSGD";
				operacao = "/PRODUTO";
			}
			int nuUnidade = Integer.parseInt(buscador.xpathTexto(tipoContaTexto+"/UNIDADE"));
			int nuProduto = Integer.parseInt(buscador.xpathTexto(tipoContaTexto+operacao));
			long nuConta = Long.parseLong(buscador.xpathTexto(tipoContaTexto+"/CONTA"));
			short dvConta = Short.parseShort(buscador.xpathTexto(tipoContaTexto+"/DV"));
			
		
			String dtEfetivacao = buscador.xpathTexto("/PAGAMENTO_BOLETO/INFORMACOES_BOLETO/DATA_EFETIVACAO");
			String identfTransacao = buscador.xpathTexto("/PAGAMENTO_BOLETO/INFORMACOES_BOLETO/IDENTIFICACAO");
			String valorPagamento = buscador.xpathTexto("/PAGAMENTO_BOLETO/INFORMACOES_BOLETO/VALOR_PAGO");
			if (null == dtEfetivacao || dtEfetivacao.isEmpty()) {
				throw new SimtxException("DATA_EFETIVACAO Campo Requerido");
			}
			if (null == valorPagamento || dtEfetivacao.isEmpty()) {
				throw new SimtxException("VALOR_PAGO Campo Requerido");
			}
			
			BigDecimal	valorTransacao = new BigDecimal(valorPagamento);
			
			Mtxtb034TransacaoAgendamento transacaoAgendamento = new Mtxtb034TransacaoAgendamento();
			transacaoAgendamento.setNuNsuTransacaoAgendamento(transacao.getNuNsuTransacao());
			transacaoAgendamento.setDtEfetivacao(DataUtil.simpleDateFormat(DataUtil.FORMATO_DATA_XML).parse(dtEfetivacao));
			transacaoAgendamento.setDtReferencia(transacao.getDtReferencia());
			transacaoAgendamento.setDeXmlAgendamento(iteracaoCanal.getDeRecebimento());
			transacaoAgendamento.setDeIdentificacaoTransacao(identfTransacao);
			transacaoAgendamento.setNuCanal(canal.getNuCanal());
			transacaoAgendamento.setNuServico(servicoOrigem.getId().getNuServico001());
			transacaoAgendamento.setNuVersaoServico(servicoOrigem.getId().getNuVersaoServico());
			transacaoAgendamento.setIcSituacao(Constantes.AGENDAMENTO_IC_AGENDADO);
			transacaoAgendamento.setNuUnidade(nuUnidade);
			transacaoAgendamento.setNuProduto(nuProduto);
			transacaoAgendamento.setNuConta(nuConta);
			transacaoAgendamento.setDvConta(dvConta);
			transacaoAgendamento.setIcTipoConta(tipoConta);
			transacaoAgendamento.setValorTransacao(valorTransacao);
			return transacaoAgendamento;
		} 
		catch (Exception e) {
			logger.error(e);
			Mtxtb006Mensagem mensagemErroInterno = this.fornecedorDados.buscarMensagem(MensagemRetorno.ERRO_GRAVACAO_TRANSACAO);
			throw new ServicoException(mensagemErroInterno,Constantes.ORIGEM_SIMTX);
		}
	}
	
	public DadosTransacaoComuns processarTransacao(ParametrosAdicionais parametrosAdicionais) throws ServicoException {
		Mtxtb014Transacao transacao = null;
		Mtxtb016IteracaoCanal iteracaoCanal = null;
		Mtxtb011VersaoServico servico = null;
		Mtxtb004Canal canal = null;
		
		DadosBarramento dadosBarramento = new DadosBarramento();
		
		try {
			dadosBarramento.escrever(parametrosAdicionais.getXmlMensagem());

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
			
			parametrosAdicionais.setDadosBarramento(dadosBarramento);
			return new DadosTransacaoComuns(transacao, iteracaoCanal, canal, servico, meioEntrada, parametrosAdicionais);
		} 
		catch (ServicoException se) {
			logger.error(se.getMensagem());
			throw se;		
		} 
		catch (Exception e) {
			logger.error(e);
			Mtxtb006Mensagem mensagemErroInterno = this.fornecedorDados.buscarMensagem(MensagemRetorno.ERRO_INTERNO);
			throw new ServicoException(mensagemErroInterno, Constantes.ORIGEM_SIMTX);
		}
	}
	
	
	public void setFornecedorDados(FornecedorDados fornecedorDados) {
		this.fornecedorDados = fornecedorDados;
	}
	
	public void setMensagemServidor(Mensagem mensagemServidor) {
		this.mensagemServidor = mensagemServidor;
	}

	public void setGerenciadorTarefas(GerenciadorTarefas gerenciadorTarefas) {
		this.gerenciadorTarefas = gerenciadorTarefas;
	}

	public void setProcessadorEnvioRetomadaTransacao(ProcessadorEnvioRetomadaTransacao processadorEnvioRetomadaTransacao) {
		this.processadorEnvioRetomadaTransacao = processadorEnvioRetomadaTransacao;
	}

	public void setProcessadorEnvioSicco(ProcessadorEnvioSicco processadorEnvioSicco) {
		this.processadorEnvioSicco = processadorEnvioSicco;
	}

}
