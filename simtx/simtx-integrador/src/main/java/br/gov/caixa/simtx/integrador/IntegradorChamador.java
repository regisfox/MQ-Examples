///*******************************************************************************
// * Copyright (C)  2017 - CAIXA EconÃ´mica Federal 
// * 
// * Todos os direitos reservados
// ******************************************************************************/
//package br.gov.caixa.simtx.integrador;
//
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.IOException;
//import java.math.BigDecimal;
//import java.rmi.RemoteException;
//import java.util.Date;
//import java.util.List;
//import java.util.Properties;
//
//import javax.annotation.PostConstruct;
//import javax.ejb.EJB;
//import javax.ejb.Stateful;
//import javax.inject.Inject;
//import javax.xml.parsers.ParserConfigurationException;
//
//import org.apache.log4j.Logger;
//import org.w3c.dom.NamedNodeMap;
//import org.w3c.dom.Node;
//import org.xml.sax.SAXException;
//
//import br.gov.caixa.simtx.agendamento.enuns.ServicoAgendamentoEnum;
//import br.gov.caixa.simtx.agendamento.processador.ProcessadorAgendamento;
//import br.gov.caixa.simtx.agendamento.processador.ProcessadorOperacoesAgendamento;
//import br.gov.caixa.simtx.agendamento.servico.ValidaBoletoAgendamento;
//import br.gov.caixa.simtx.assinaturamultipla.processador.ProcessadorMensagemAssinaturaMultipla;
//import br.gov.caixa.simtx.comprovante.enuns.ServicosComprovanteEnum;
//import br.gov.caixa.simtx.comprovante.processador.ProcessadorComprovante;
//import br.gov.caixa.simtx.core.canal.ProcessadorCore;
//import br.gov.caixa.simtx.manutencao.desfazimento.canal.ProcessadorDesfazimento;
//import br.gov.caixa.simtx.manutencao.retomar.transacao.ProcessadorRetomadaTransacao;
//import br.gov.caixa.simtx.persistencia.cache.core.FornecedorDados;
//import br.gov.caixa.simtx.persistencia.constante.Constantes;
//import br.gov.caixa.simtx.persistencia.constante.MensagemRetorno;
//import br.gov.caixa.simtx.persistencia.constante.VersaoLeaiuteEnum;
//import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb001Servico;
//import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb004Canal;
//import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb006Mensagem;
//import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb008MeioEntrada;
//import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb011VersaoServico;
//import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb011VersaoServicoPK;
//import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb014Transacao;
//import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb016IteracaoCanal;
//import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb016IteracaoCanalPK;
//import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb017VersaoSrvcoTrnso;
//import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb017VersaoSrvcoTrnsoPK;
//import br.gov.caixa.simtx.util.ParametrosAdicionais;
//import br.gov.caixa.simtx.util.SimtxConfig;
//import br.gov.caixa.simtx.util.StringUtil;
//import br.gov.caixa.simtx.util.data.DataUtil;
//import br.gov.caixa.simtx.util.exception.ServicoException;
//import br.gov.caixa.simtx.util.exception.SimtxException;
//import br.gov.caixa.simtx.util.gerenciador.TratadorDeExcecao;
//import br.gov.caixa.simtx.util.gerenciador.ValidadorMensagemCanal;
//import br.gov.caixa.simtx.util.gerenciador.ValidadorRegrasNegocio;
//import br.gov.caixa.simtx.util.integrador.sicco.ProcessadorEnvioSicco;
//import br.gov.caixa.simtx.util.mensagem.FilaCanal;
//import br.gov.caixa.simtx.util.mensagem.GerenciadorFilasMQ;
//import br.gov.caixa.simtx.util.mensagem.PropriedadesMQ;
//import br.gov.caixa.simtx.util.xml.BuscadorTextoXml;
//import br.gov.caixa.simtx.util.xml.DadosBarramento;
//import br.gov.caixa.simtx.util.xml.JsonConversor;
//import br.gov.caixa.simtx.util.xml.XmlUtils;
//import br.gov.caixa.simtx.util.xml.xsd.sibar.HeaderSibar;
//import br.gov.caixa.simtx.util.xml.xsd.sibar.HeaderSibarFactory;
//
//@Stateful
//public class IntegradorChamador {
//
//	public IntegradorChamador() {
//		super();
//	}
//
//	public IntegradorChamador(String mensagemCanal, ProcessadorOperacoesAgendamento processadorOperacoesAgendamento) {
//		super();
//		this.mensagemCanal = mensagemCanal;
//		this.processadorOperacoesAgendamento = processadorOperacoesAgendamento;
//	}
//
//	private static final Logger logger = Logger.getLogger(IntegradorChamador.class);
//	
//	public static final Integer MEIO_ENTRADA_ASSINATURA_MULTIPLA = 5;
//
//	protected Properties dadosProperties;
//
//	private String mensagemCanal;
//
//	private String idMessage;
//
//	protected Properties properties;
//
//	private HeaderSibar header;
//
//	/**
//	 * NameSpace do xml do Canal
//	 * <p>
//	 * Ex: http://caixa.gov.br/sibar/<b>consulta_cadastro_positivo
//	 */
//	private String[] nameSpaceCanal;
//
//	protected Mtxtb001Servico servicoChamado;
//
//	/**
//	 * Servicos que nao existem devem ser repassados para o Sibar direto, sem
//	 * validar nada.
//	 */
//	private boolean servicoComValidacao = true;
//
//	private String jndiQueueConnectionFactory;
//
//	private String jndiResponseQueue;
//
//	private Mtxtb004Canal canal;
//
//	protected Mtxtb011VersaoServico versaoServicoChamado;
//
//	protected Mtxtb014Transacao transacao;
//
//	protected FilaCanal filaCanal;
//
//	protected ValidadorMensagemCanal validadorMensagemCanal;
//	
//	private Mtxtb016IteracaoCanal iteracaoCanal;
//
//	private BuscadorTextoXml buscador;
//	
//	@Inject
//	protected SimtxConfig simtxConfig;
//	
//	@EJB
//	private ProcessadorMensagemAssinaturaMultipla processadorMsgCanalAssinaturaMultipla;
//
//	@Inject
//	private ProcessadorAgendamento processadorAgendamento;
//	
//	@Inject
//	private ProcessadorOperacoesAgendamento processadorOperacoesAgendamento;
//	
//	@Inject
//	private ValidaBoletoAgendamento validaBoletoAgendamento;
//	
//	@Inject
//	private ProcessadorComprovante processadorComprovante;
//
//	@Inject
//	private FornecedorDados fornecedorDados;
//	
//	@Inject
//	private GerenciadorFilasMQ execucaoMq;
//	
//	@Inject
//	private TratadorDeExcecao tratadorDeExcecao;
//	
//	@Inject
//	protected ProcessadorEnvioSicco processadorEnvioSicco;
//	
//	@Inject
//	private ValidadorRegrasNegocio validadorRegrasNegocio;
//	
//	@Inject
//	private ProcessadorDesfazimento processadorDesfazimento;
//	
//	@Inject
//	private ProcessadorRetomadaTransacao processadorRetomadaTransacao;
//	
//	private Mtxtb008MeioEntrada meioEntrada;
//	
//	@Inject
//	private ProcessadorCore processadorCore;
//	
//
//	/**
//	 * Construtor principal
//	 */
//	@PostConstruct
//	public void iniciarDados() {
//		try {
//			carregaArquivoPropriedade();
//			this.filaCanal = new FilaCanal();
//		} catch (Exception e) {
//			logger.error(e.getMessage(), e);
//		}
//	}
//
//	/**
//	 * Metodo responsavel de chamar o modulo correspondente a solicitacao.
//	 * 
//	 * @throws RemoteException
//	 */
//	public void chamar() throws RemoteException {
//		boolean converterRespostaParaJson = false;
//
//		try {
//			logger.info(" ==== Processo Iniciado ==== ");
//			logger.info("Mensagem recebida:\n" + this.mensagemCanal.trim());
//			if(!XmlUtils.isXml(this.mensagemCanal)) {
//				try {
//					this.mensagemCanal = JsonConversor.toXML(this.mensagemCanal);
//					logger.info("Mensagem JSON convertida para XML:");
//					logger.info(this.mensagemCanal);
//					converterRespostaParaJson = true;
//				}catch (Exception e) {
//					throw new SimtxException("MN002");
//				}
//			}
//			
//			this.mensagemCanal = this.mensagemCanal.replaceAll("<\\?xml.*\\?>", "");
//			
//			this.buscador = new BuscadorTextoXml(this.mensagemCanal);
//			String servicoProcessarAgendamento = this.buscador.xpathTexto("/*[1]/AGENDAMENTO");
//			if(!servicoProcessarAgendamento.isEmpty() && servicoProcessarAgendamento.equals("SIM")) {
////				this.processadorAgendamento.processarMensagem(this.idMessage, this.mensagemCanal,
////						this.jndiQueueConnectionFactory, this.jndiResponseQueue, converterRespostaParaJson);
//				return;
//			}
//			
//			String codigoServicoMigrado = this.buscador.xpathTexto("/*[1]/HEADER/SERVICO/CODIGO");
//			
//			if(Long.valueOf(codigoServicoMigrado).equals(Constantes.SERVICO_DESFAZIMENTO)) {
////				this.processadorDesfazimento.processar(this.mensagemCanal);
//				return;
//			}
//			
//			if(Long.valueOf(codigoServicoMigrado).equals(Constantes.SERVICO_RETOMAR_TRANSACAO)) {
////				this.processadorRetomadaTransacao.processar(this.mensagemCanal);
//				return;
//			}
//
//			if(110031l == Long.valueOf(codigoServicoMigrado) || 110038l == Long.valueOf(codigoServicoMigrado)) {
//				String dataXml = this.buscador.xpathTexto("/*[1]/VALIDA_BOLETO/DATA_PAGAMENTO");
//				if(dataXml != null && !StringUtil.isEmpty(dataXml)) {
//					Date dataAgendamento = DataUtil.simpleDateFormat(DataUtil.FORMATO_DATA_XML).parse(this.buscador.xpathTexto("/*[1]/VALIDA_BOLETO/DATA_PAGAMENTO"));
//					ParametrosAdicionais parametrosAdicionais = new ParametrosAdicionais(idMessage, jndiQueueConnectionFactory, jndiResponseQueue, converterRespostaParaJson);
//					
//					buscarServicoParaMigrado(this.buscador);
//					if(DataUtil.dataEntradaEhDepoisDeDataHoje(dataAgendamento)) {
////						this.validaBoletoAgendamento.processar(this.versaoServicoChamado, this.mensagemCanal,
////								parametrosAdicionais);
//						return;
//					}
//				}
//			}
//			
//			List<Long> codigosServicosListaTransacoes = ServicoAgendamentoEnum.listCodigosServicos();
//			boolean servicoOperacoesAgendamento = codigosServicosListaTransacoes.contains(Long.valueOf(codigoServicoMigrado));
//			
//			if(servicoOperacoesAgendamento) {
////				this.processadorOperacoesAgendamento.processarMensagem(this.idMessage, this.mensagemCanal,
////						this.jndiQueueConnectionFactory, this.jndiResponseQueue, converterRespostaParaJson);
//				return;
//			}
//
//			List<Long> codigosServicosComprovantes = ServicosComprovanteEnum.listCodigosServicos();
//			boolean servicoComprovante = codigosServicosComprovantes.contains(Long.valueOf(codigoServicoMigrado));
//			if(servicoComprovante) {
////				this.processadorComprovante.processarMensagem(this.idMessage, this.mensagemCanal,
////						this.jndiQueueConnectionFactory, this.jndiResponseQueue, converterRespostaParaJson);
//				return;
//			}
//			
//			
//			Integer meioEntradaParam = Integer.valueOf(this.buscador.xpathTexto("/*[1]/HEADER/MEIOENTRADA"));
//			if(codigoServicoMigrado != null && !codigoServicoMigrado.equals("")) {
//				buscarServicoParaMigrado(this.buscador);
//				if(meioEntradaParam.equals(MEIO_ENTRADA_ASSINATURA_MULTIPLA) || Constantes.getServicosAssinaturaMultipla().contains(this.servicoChamado.getNuServico()))  {
//					chamarAssinaturaMultipla(converterRespostaParaJson);
//				}
//				else {
////					processadorCore.processarMensagem(this.idMessage, this.mensagemCanal,
////							this.jndiQueueConnectionFactory, this.jndiResponseQueue, converterRespostaParaJson);
//				}
//				return;
//			}
//			
//			this.nameSpaceCanal = buscarNameSpace(this.mensagemCanal);
//			if (this.nameSpaceCanal == null) {
//				processarMensagemSemValidacao();
//				return;
//			}
//
//			lerHeaderDaMensagem();
//
//			if (this.header == null) {
//				processarMensagemSemValidacao();
//				return;
//			}
//
//			buscarServico();
//
//			if (this.servicoComValidacao) {
//				processaServico(this.header.getSISTEMAORIGEM(),
//						DataUtil.simpleDateFormat(DataUtil.FORMATO_DATA_YYYY_MM_DD_HH_MM_SS).parse(this.header.getDATAHORA()),
//						this.header.getVERSAO());
//				
//				prepararValidacaoMensagemEntrada(this.header.getDATAHORA(), this.header.getIDPROCESSO(), null);
//				this.validadorMensagemCanal.validarMensagemEntradaCanalNaoMigrado(this.mensagemCanal);
////				chamarCore(converterRespostaParaJson);
////				processadorCore.processarMensagem(this.idMessage, this.mensagemCanal,
////						this.jndiQueueConnectionFactory, this.jndiResponseQueue, converterRespostaParaJson);
//			} 
//			else {
//				processarMensagemSemValidacao();
//			}
//		} 
//		catch (Exception e) {
//			logger.error(e.getMessage(), e);
//			Mtxtb006Mensagem mensagemErroInterno = this.fornecedorDados.buscarMensagem(MensagemRetorno.ERRO_INTERNO);
//			ServicoException se = new ServicoException(mensagemErroInterno,Constantes.ORIGEM_SIMTX);
//			tratarExcecao(this.versaoServicoChamado, transacao, iteracaoCanal, converterRespostaParaJson, se);
//		}
//	}
//	
//	private void prepararChamadaMigrado() throws Exception {
//		Mtxtb004Canal canalNovo = new Mtxtb004Canal();
//		canalNovo.setNoFilaRspCanal(this.jndiResponseQueue);
//		canalNovo.setNoConexaoCanal(this.jndiQueueConnectionFactory);
//		this.canal = canalNovo;
//		
//		String sigla = this.buscador.xpathTexto("/*[1]/HEADER/CANAL/SIGLA");
//		this.canal.setSigla(sigla);
//		String dataHoraCanalString = buscador.xpathTexto("/*[1]/HEADER/CANAL/DATAHORA");
//		Date dataHoraCanal =  DataUtil.simpleDateFormat(DataUtil.FORMATO_DATA_YYYY_MM_DD_HH_MM_SS).parse(dataHoraCanalString);
//
//		if(this.servicoChamado == null) {
//			Mtxtb006Mensagem mensagemErroInterno = this.fornecedorDados.buscarMensagem(MensagemRetorno.SERVICO_INEXISTENTE_PARA_CANAL);
//			throw new ServicoException(mensagemErroInterno,Constantes.ORIGEM_SIMTX);
//		}
//		
//		processaServico(sigla, dataHoraCanal,
//				buscador.xpathTexto("/*[1]/HEADER/SERVICO/VERSAO"));
//		
//		int nuMeioEntrada = Integer.parseInt(buscador.xpathTexto("/*[1]/HEADER/MEIOENTRADA"));
//		this.meioEntrada = new Mtxtb008MeioEntrada();
//		this.meioEntrada.setNuMeioEntrada(nuMeioEntrada);
//		this.validadorRegrasNegocio.validarRegrasMigrado(this.mensagemCanal, this.canal, this.meioEntrada, this.versaoServicoChamado);
//	}
//	
//	private void chamarAssinaturaMultipla(boolean converterRespostaParaJson) {
//		try {
//			prepararChamadaMigrado();
//			
//			processadorMsgCanalAssinaturaMultipla.setMensagemCanal(this.mensagemCanal);
//			processadorMsgCanalAssinaturaMultipla.setServicoChamado(this.servicoChamado);
//			processadorMsgCanalAssinaturaMultipla.setVersaoServicoChamado(this.versaoServicoChamado);
//			processadorMsgCanalAssinaturaMultipla.setSimtxConfig(this.simtxConfig);
//			processadorMsgCanalAssinaturaMultipla.setProperties(this.properties);
//			processadorMsgCanalAssinaturaMultipla.setTransacao(this.transacao);
//			processadorMsgCanalAssinaturaMultipla.setIdMensagem(this.idMessage);
//			processadorMsgCanalAssinaturaMultipla.setCanal(this.canal);
//			processadorMsgCanalAssinaturaMultipla.setIteracaoCanal(this.iteracaoCanal);
//			processadorMsgCanalAssinaturaMultipla.processarMensagemMQ();
//		} catch (ServicoException se) {
//			logger.error(se.getMensagem(), se);
//			tratarExcecao(this.versaoServicoChamado, this.transacao, this.iteracaoCanal, converterRespostaParaJson, se);
//		} catch (Exception e2) {
//			logger.fatal("Nao foi possivel chamar o modulo assinatura multipla: " + e2);
//			Mtxtb006Mensagem mensagemErroInterno = this.fornecedorDados.buscarMensagem(MensagemRetorno.ERRO_INTERNO);
//			ServicoException se = new ServicoException(mensagemErroInterno,Constantes.ORIGEM_SIMTX);
//			tratarExcecao(this.versaoServicoChamado, transacao, iteracaoCanal, converterRespostaParaJson, se);
//		}
//	}
//
//	/**
//	 * Repassa a mensagem do canal direto para o Sibar, sem precisar validar.
//	 * @throws ServicoException 
//	 * 
//	 * @throws Exception
//	 */
//	public void processarMensagemSemValidacao() throws ServicoException {
//		try {
//			logger.info("Servico inexistente, repassando para o Sibar");
//	
//			PropriedadesMQ propriedadesMQ = definirPropriedadesFila();
//			String xmlSaidaSibar = this.execucaoMq.executarServico(this.mensagemCanal, propriedadesMQ);
//			
//			if (xmlSaidaSibar == null) {
//				throw new SimtxException("MN016");
//			}
//	
//			Mtxtb004Canal novoCanal = new Mtxtb004Canal();
//			novoCanal.setNoFilaRspCanal(this.jndiResponseQueue);
//			novoCanal.setNoConexaoCanal(this.jndiQueueConnectionFactory);
//			this.filaCanal.postarMensagem(this.idMessage, xmlSaidaSibar, novoCanal);
//		} catch (Exception e ) {
//			Mtxtb006Mensagem mensagemErroInterno = this.fornecedorDados.buscarMensagem(MensagemRetorno.ERRO_INTERNO);
//			throw new ServicoException(mensagemErroInterno,Constantes.ORIGEM_SIMTX);
//		}
//	}
//
//	/**
//	 * Prepara a fila para envio ao Sibar.
//	 * 
//	 * @return
//	 */
//	public PropriedadesMQ definirPropriedadesFila() {
//		PropriedadesMQ propriedadesMQ = new PropriedadesMQ();
//		propriedadesMQ.setTimeout(Integer.parseInt(this.properties.getProperty("sibar.ibc.timeout")));
//    	propriedadesMQ.setConnectionFactory(this.properties.getProperty("sibar.ibc.connection.factory"));
//    	propriedadesMQ.setFilaRequisicao(this.properties.getProperty("sibar.ibc.req"));
//    	propriedadesMQ.setFilaResposta(this.properties.getProperty("sibar.ibc.resp"));
//    	return propriedadesMQ;
//	}
//
//	private void processaServico(String sistemaOrigem, Date datahoraCanal, String versao) throws Exception {
//		this.transacao = salvarTransacao(sistemaOrigem, datahoraCanal);
//		this.iteracaoCanal = salvarIteracaoCanal(this.mensagemCanal, transacao);
//
//		logger.info("Servico chamado: " + this.versaoServicoChamado.getMtxtb001Servico().getNoServico() + " - Versao "
//				+ versao);
//		
//		salvarTransacaoServico(this.transacao);
//	}
//
//	/**
//	 * Valida a mensagem de entrada do canal. {@link ValidadorMensagemCanal}
//	 * 
//	 * @param header
//	 * @param mensagem
//	 * @return
//	 * @throws Exception
//	 */
//	public boolean validarMensagemEntradaNaoMigrado() throws Exception {
//		return this.validadorMensagemCanal.validarMensagemEntradaCanalNaoMigrado(this.mensagemCanal);
//	}
//
//	/**
//	 * Valida a mensagem de entrada do canal. {@link ValidadorMensagemCanal}
//	 * 
//	 * @param header
//	 * @param mensagem
//	 * @return
//	 * @throws Exception
//	 */
//	public boolean validarMensagemEntradaMigrado() throws Exception {
//		return this.validadorMensagemCanal.validarMensagemEntradaCanalNaoMigrado(this.mensagemCanal);
//	}
//
//	public void prepararValidacaoMensagemEntrada(String dataHora, String nomeMeioEntrada, Integer numeroMeioEntrada) {
//		this.validadorMensagemCanal = new ValidadorMensagemCanal();
//		this.validadorMensagemCanal.setFornecedorDados(this.fornecedorDados);
//		this.validadorMensagemCanal.setServicoChamado(this.servicoChamado);
//		this.validadorMensagemCanal.setVersaoServicoChamado(this.versaoServicoChamado);
//		this.validadorMensagemCanal.setCanal(this.canal);
//		this.validadorMensagemCanal.setSimtxConfig(this.simtxConfig);
//		this.validadorMensagemCanal.setProperties(this.properties);
//		this.validadorMensagemCanal.setDataHora(dataHora);
//		this.validadorMensagemCanal.setNomeMeioEntrada(nomeMeioEntrada);
//		this.validadorMensagemCanal.setNumeroMeioEntrada(numeroMeioEntrada);
//	}
//
//	/**
//	 * Busca o Servico e a VersaoServico.
//	 * 
//	 * @param header
//	 * @param mensagem
//	 * @return
//	 * @throws Exception
//	 */
//	public void buscarServico() throws Exception {
//		logger.info("Buscando servico");
//		Mtxtb001Servico servico = null;
//		Mtxtb011VersaoServico versaoServico = new Mtxtb011VersaoServico();
//
//		for (int i = 0; i < this.nameSpaceCanal.length; i++) {
//			if (this.nameSpaceCanal[i] != null) {
//				servico = new Mtxtb001Servico();
//				servico.setNoOperacaoBarramento(this.header.getOPERACAO());
//				servico.setNoServicoBarramento(this.nameSpaceCanal[i]);
//
//				versaoServico = new Mtxtb011VersaoServico();
//				Mtxtb011VersaoServicoPK versaoServicoPK = new Mtxtb011VersaoServicoPK();
//				versaoServicoPK.setNuVersaoServico(
//						VersaoLeaiuteEnum.obterEnum(this.header.getVERSAO()).getVersaoSimtx());
//				versaoServico.setId(versaoServicoPK);
//				versaoServico.setMtxtb001Servico(servico);
//				versaoServico = this.fornecedorDados.buscarVersaoServicoPorNomeOperacao(versaoServico);
//				if (versaoServico != null) {
//					this.servicoChamado = versaoServico.getMtxtb001Servico();
//					this.versaoServicoChamado = versaoServico;
//					break;
//				}
//			}
//		}
//		if (versaoServico == null) {
//			this.servicoComValidacao = false;
//		}
//	}
//
//	/**
//	 * Busca o Servico e a VersaoServico.
//	 * 
//	 * @param header
//	 * @param mensagem
//	 * @return
//	 * @throws Exception
//	 */
//	public void buscarServicoParaMigrado(BuscadorTextoXml buscador) {
//		logger.info("Buscando servico");
//		try {
//			String numeroServico = buscador.xpathTexto("/*[1]/HEADER/SERVICO/CODIGO");
//			String numeroVersaoServico = buscador.xpathTexto("/*[1]/HEADER/SERVICO/VERSAO");
//
//			Mtxtb011VersaoServico versaoServico = this.fornecedorDados
//					.buscarVersaoServico(Integer.valueOf(numeroVersaoServico), Long.valueOf(numeroServico));
//			this.servicoChamado = versaoServico.getMtxtb001Servico();
//			this.versaoServicoChamado = versaoServico;
//		} 
//		catch (Exception e) {
//			this.servicoComValidacao = false;
//		}
//	}
//	
//	/**
//	 * Busca o nameSpace na mensagem recebida pelo canal.
//	 * 
//	 * @param mensagem
//	 */
//	public String[] buscarNameSpace(String mensagem) throws ParserConfigurationException, SAXException, IOException {
//		logger.debug("Buscando nameSpace");
//		this.buscador = new BuscadorTextoXml(this.mensagemCanal);
//		String targetNameSpaceCanal = "";
//		String[] nameSpace = new String[1];
//		String[] nameSpaces = null;
//		try {
//			Node nodeReq = this.buscador.buscarNode("HEADER");
//			if (nodeReq == null)
//				return null;
//
//			targetNameSpaceCanal = nodeReq.getOwnerDocument().getFirstChild().getNodeName();
//			if (targetNameSpaceCanal.contains("SERVICO_ENTRADA")) {
//				if (targetNameSpaceCanal.contains(":")) {
//					nameSpace[0] = targetNameSpaceCanal.substring(0, targetNameSpaceCanal.indexOf(":"));
//				}
//				NamedNodeMap atributos = nodeReq.getOwnerDocument().getFirstChild().getAttributes();
//				nameSpaces = new String[atributos.getLength()];
//				for (int i = 0; i < atributos.getLength(); i++) {
//					Node node = atributos.item(i);
//					if (node.getNodeType() == Node.ATTRIBUTE_NODE) {
//						if (nameSpace[0] != null && node.getNodeName().contains(nameSpace[0])) {
//							String[] valores = node.getNodeValue().split("/");
//							String valor = valores[valores.length - 2].trim();
//							if(valor.equals("sibar")) {
//								nameSpace[0] = valores[valores.length - 1].trim();
//							} else {
//								nameSpace[0] = valores[valores.length - 2].trim();
//							}
//							
//							break;
//						} else {
//							String[] valores = node.getNodeValue().split("/");
//							String valor = valores[valores.length - 2].trim();
//							if(valor.equals("sibar")) {
//								nameSpaces[i] = valores[valores.length - 1].trim();
//							} else {
//								nameSpaces[i] = valores[valores.length - 2].trim();
//							}
//						}
//					}
//				}
//			} else {
//				return null;
//			}
//		} catch (Exception e) {
//			return null;
//		}
//		if (nameSpace[0] != null) {
//			return nameSpace;
//		} else {
//			return nameSpaces;
//		}
//	}
//
//	/**
//	 * Salva a transacao.
//	 * 
//	 * @param header
//	 * @param mensagem
//	 * @return
//	 * @throws Exception
//	 */
//	public Mtxtb014Transacao salvarTransacao(String sistemaOrigem, Date datahoraTransacao) throws Exception {
//		logger.info("Gravando transacao");
//		
//		this.canal = new Mtxtb004Canal();
//		Mtxtb014Transacao transacao = null;
//		try {
//			this.canal.setSigla(sistemaOrigem);
//			this.canal = this.fornecedorDados.buscarCanalPorSigla(this.canal);
//			if (this.canal == null) {
//				this.canal = new Mtxtb004Canal();
//				this.canal.setNuCanal(Constantes.CODIGO_CANAL_INEXISTENTE.longValue());
//			}
//			
//			Date dataAtual = DataUtil.simpleDateFormat(DataUtil.FORMATO_DATA_YYYY_MM_DD).parse(DataUtil.simpleDateFormat(DataUtil.FORMATO_DATA_YYYY_MM_DD).format(new Date()));
//			
//			transacao = new Mtxtb014Transacao();
//			transacao.setCoCanalOrigem(String.valueOf(this.canal.getNuCanal()));
//			transacao.setIcSituacao(Constantes.IC_SERVICO_EM_ANDAMENTO);
//			transacao.setDhMultiCanal(new Date());
//			transacao.setDtReferencia(dataAtual);
//			transacao.setDhTransacaoCanal(datahoraTransacao);
//			transacao.setIcEnvio(BigDecimal.ZERO);
//			transacao.setIcRetorno(BigDecimal.ZERO);
//			transacao.setTsAtualizacao(new Date());
//			if(this.versaoServicoChamado.isMigrado()) {
//				/**
//				 * No futuro cada modulo ira criar sua transacao, e a forma de recuperar
//				 * o NSU de origem fara mais sentido.
//				 */
//				long nuNsuOrigem = buscador.xpathTexto("/*[1]/NSU_ORIGEM").isEmpty()
//				? 0L : Long.parseLong(buscador.xpathTexto("/*[1]/NSU_ORIGEM"));
//				
//				if(nuNsuOrigem == 0l) {
//					nuNsuOrigem = buscador.xpathTexto("/*[1]/NSUMTX_ORIGEM").isEmpty()
//					? 0L : Long.parseLong(buscador.xpathTexto("/*[1]/NSUMTX_ORIGEM"));
//				}
//				
//				transacao.setNuNsuTransacaoPai(nuNsuOrigem);
//			}
//			
//			transacao = this.fornecedorDados.salvarTransacao(transacao);
//			this.transacao = transacao;
//			logger.info("NSUMTX gerado: " + transacao.getNuNsuTransacao());
//		} catch (Exception e) {
//			throw new SimtxException("MN006");
//		}
//		return transacao;
//	}
//
//	/**
//	 * Valida o Header da mensagem recebida pelo canal.
//	 * 
//	 * @param mensagem
//	 * @return
//	 * @throws Exception
//	 */
//	public void lerHeaderDaMensagem() throws Exception {
//		logger.info("Validando header");
//		try {
//			Node nodeHeader = this.buscador.buscarNode("HEADER");
//			if (nodeHeader == null)
//				throw new SimtxException(Constantes.CODIGO_EXCECAO);
//
//			HeaderSibar headerSibar = new HeaderSibarFactory().createHeaderSibar(nodeHeader);
//			if (headerSibar.getDATAHORA().length() < 14)
//				throw new SimtxException(Constantes.CODIGO_EXCECAO);
//			this.header = headerSibar;
//		} catch (Exception e) {
//			throw new SimtxException(Constantes.CODIGO_EXCECAO);
//		}
//	}
//
//	/**
//	 * Salva xml do Canal na entidade IteracaoCanal.
//	 * 
//	 * @param mensagem
//	 * @param transacao
//	 * @return
//	 * @throws Exception
//	 */
//	public Mtxtb016IteracaoCanal salvarIteracaoCanal(String mensagem, Mtxtb014Transacao transacao) throws Exception {
//		logger.info("Gravando IteracaoCanal");
//		Mtxtb016IteracaoCanal iteracao = null;
//		try {
//		
//			String terminal = "";
//
//			iteracao = new Mtxtb016IteracaoCanal();
//			iteracao.setMtxtb004Canal(new Mtxtb004Canal());
//			iteracao.getMtxtb004Canal().setNuCanal(Long.parseLong(transacao.getCoCanalOrigem()));
//			iteracao.setMtxtb014Transacao(transacao);
//			iteracao.setTsRecebimentoSolicitacao(DataUtil.getDataAtual());
//			iteracao.setTsRetornoSolicitacao(DataUtil.getDataAtual());
//			iteracao.setId(new Mtxtb016IteracaoCanalPK());
//			iteracao.getId().setNuNsuTransacao014(transacao.getNuNsuTransacao());
//			iteracao.setDeRecebimento(mensagem);
//			iteracao.setDtReferencia(transacao.getDtReferencia());
//			iteracao.setCodTerminal(terminal);
//			iteracao = this.fornecedorDados.salvarIteracaoCanal(iteracao);
//		} catch (Exception e) {
//			throw new SimtxException("MN018");
//		}
//		return iteracao;
//	}
//
//	/**
//	 * Salva o ServicoTransacao.
//	 * 
//	 * @param header
//	 * @param transacao
//	 * @throws Exception
//	 */
//	public void salvarTransacaoServico(Mtxtb014Transacao transacao) throws Exception {
//		logger.info("Salvando Servico Transacao");
//		Mtxtb017VersaoSrvcoTrnso versaoServicoTransacao = new Mtxtb017VersaoSrvcoTrnso();
//		versaoServicoTransacao.setTsSolicitacao(DataUtil.getDataAtual());
//		versaoServicoTransacao.setId(new Mtxtb017VersaoSrvcoTrnsoPK());
//		versaoServicoTransacao.getId().setNuNsuTransacao014(transacao.getNuNsuTransacao());
//		versaoServicoTransacao.getId().setNuServico011(this.servicoChamado.getNuServico());
//		versaoServicoTransacao.getId().setNuVersaoServico011(this.versaoServicoChamado.getId().getNuVersaoServico());
//		versaoServicoTransacao.setDtReferencia(transacao.getDtReferencia());
//		versaoServicoTransacao = this.fornecedorDados.salvarTransacaoServico(versaoServicoTransacao);
//		if (versaoServicoTransacao == null)
//			throw new SimtxException("MN018");
//	}
//
//	/**
//	 * Atualiza a Transacao com a nova situacao.
//	 * 
//	 * @param situacao
//	 * @throws ServicoException
//	 */
//	public void atualizaTransacao(Mtxtb014Transacao transacao, BigDecimal situacao) throws ServicoException {
//		try {
//			if(transacao != null) {
//				logger.info("Atualizando transacao");
//				transacao.setIcSituacao(situacao);
//				this.fornecedorDados.alterarTransacao(transacao);
//			}
//		} 
//		catch (Exception e) {
//			logger.error(e);
//			Mtxtb006Mensagem mensagemErroInterno = this.fornecedorDados.buscarMensagem(MensagemRetorno.ERRO_GRAVACAO_TRANSACAO);
//			throw new ServicoException(mensagemErroInterno,Constantes.ORIGEM_SIMTX);
//		}
//	}
//
//	/**
//	 * Atualiza a IteracaoCanal com o xml de resposta enviada para o Canal.
//	 * 
//	 * @param mensagem
//	 * @throws ServicoException
//	 */
//	public void atualizaIteracaoCanal(Mtxtb016IteracaoCanal iteracaoCanal, Mtxtb014Transacao transacao, String mensagem)
//			throws ServicoException {
//		try {
//			if(iteracaoCanal != null) {
//				logger.info("Atualizando IteracaoCanal");
//				iteracaoCanal.setMtxtb014Transacao(transacao);
//				iteracaoCanal.setDeRetorno(mensagem);
//				iteracaoCanal.setTsRetornoSolicitacao(DataUtil.getDataAtual());
//				this.fornecedorDados.alterarIteracaoCanal(iteracaoCanal);
//			}
//		} 
//		catch (Exception e) {
//			logger.error(e);
//			Mtxtb006Mensagem mensagemErroInterno = this.fornecedorDados.buscarMensagem(MensagemRetorno.ERRO_GRAVACAO_TRANSACAO);
//			throw new ServicoException(mensagemErroInterno,Constantes.ORIGEM_SIMTX);
//		}
//	}
//	
//	/**
//	 * Carrega o arquivo de propriedades Dados.
//	 * @throws IOException 
//	 * @throws Exception
//	 */
//	public void carregaArquivoPropriedade() throws IOException {
//		Properties prop = new Properties();
//		String arquivo = this.simtxConfig.getHome() + "/dados.properties";
//		File popFile = new File(arquivo);
//		FileInputStream stream;
//		stream = new FileInputStream(popFile);
//		prop.load(stream);
//		stream.close();
//		this.properties = prop;
//	}
//	
//	/**
//	 * Trata a excecao.
//	 * 
//	 * @param versaoServico
//	 * @param transacao
//	 * @param iteracaoCanal
//	 * @param converterRespostaParaJson 
//	 * @param se
//	 */
//	public void tratarExcecao(Mtxtb011VersaoServico versaoServico, Mtxtb014Transacao transacao,
//			Mtxtb016IteracaoCanal iteracaoCanal, boolean converterRespostaParaJson, ServicoException se) {
//		String resposta = null;
//		try {
//			logger.debug("Tratando excecao");
//			DadosBarramento dadosBarramento = new DadosBarramento();
//			dadosBarramento.escrever(this.mensagemCanal);
//			
//			resposta = this.tratadorDeExcecao.retornaMensagem(versaoServico, transacao, dadosBarramento, se);
//			if(resposta == null || resposta.isEmpty())
//				resposta = "Nao foi possivel montar resposta";
//			
//			logger.info(Constantes.SITUACAO_FINAL_TRANSACAO_NEGADA);
//			atualizaTransacao(transacao, new BigDecimal(2));
//			atualizaIteracaoCanal(iteracaoCanal, transacao, resposta);
//		} 
//		catch (Exception e) {
//			logger.warn("Nao foi possivel gravar a mensagem de resposta no banco de dados", e);
//		}
//		finally {
//			enviarRespostaCanal(resposta, this.idMessage, converterRespostaParaJson);
//			if (transacao != null)
//				this.processadorEnvioSicco.processarEnvioOnline(transacao, Constantes.ENVIO_UNICA);
//		}
//	}
//	
//	/**
//	 * Envia a resposta para o canal.
//	 * 
//	 * @param canalNovo
//	 * @param mensagem
//	 * @param idMessage
//	 * @param jndiResponseQueue
//	 * @param jndiQueueConnectionFactory
//	 * @param converterRespostaParaJson
//	 */
//	public void enviarRespostaCanal(String mensagem, String idMessage, boolean converterRespostaParaJson) {
//		Mtxtb004Canal canalNovo = new Mtxtb004Canal();
//		canalNovo.setNoFilaRspCanal(this.jndiResponseQueue);
//		canalNovo.setNoConexaoCanal(this.jndiQueueConnectionFactory);
//		new FilaCanal().postarMensagem(idMessage, mensagem, canalNovo, converterRespostaParaJson);
//	}
//	
//	public void popularDadosFilaIntegrador(String idMessage, String mensagemCanal, String jndiQueueConnectionFactory, String jndiResponseQueue) {
//		this.idMessage = idMessage;
//		this.mensagemCanal = mensagemCanal;
//		this.jndiQueueConnectionFactory = jndiQueueConnectionFactory;
//		this.jndiResponseQueue = jndiResponseQueue;
//	}
//
//	public void setMensagemCanal(String mensagemCanal) {
//		this.mensagemCanal = mensagemCanal;
//	}
//
//	public void setIdMessage(String idMessage) {
//		this.idMessage = idMessage;
//	}
//
//	public void setJndiQueueConnectionFactory(String jndiQueueConnectionFactory) {
//		this.jndiQueueConnectionFactory = jndiQueueConnectionFactory;
//	}
//
//	public void setJndiResponseQueue(String jndiResponseQueue) {
//		this.jndiResponseQueue = jndiResponseQueue;
//	}
//}
