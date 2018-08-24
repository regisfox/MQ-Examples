/*******************************************************************************
 * Copyright (C)  2017 - CAIXA EconÃ´mica Federal 
 * 
 * Todos os direitos reservados
 ******************************************************************************/
package br.gov.caixa.simtx.util.gerenciador;

import java.io.File;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;

import br.gov.caixa.simtx.persistencia.cache.core.FornecedorDados;
import br.gov.caixa.simtx.persistencia.constante.Constantes;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb001Servico;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb004Canal;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb005ServicoCanal;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb005ServicoCanalPK;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb008MeioEntrada;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb010VrsoTarfaMeioEntra;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb010VrsoTarfaMeioEntraPK;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb011VersaoServico;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb013PrmtoSrvcoCanal;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb018VrsoMeioEntraSrvco;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb018VrsoMeioEntraSrvcoPK;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb020SrvcoTarfaCanal;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb020SrvcoTarfaCanalPK;
import br.gov.caixa.simtx.util.SimtxConfig;
import br.gov.caixa.simtx.util.XmlValidador;
import br.gov.caixa.simtx.util.exception.SimtxException;

/**
 * Classe responsavel por validar e verificar todos os requisitos para dar
 * continuidade no processo do Core. Ex: MeioEntrada Ativo, Tarefas Ativas etc.
 * 
 * @author rctoscano
 *
 */
public class ValidadorMensagemCanal implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** Logger */
	private static final Logger logger = Logger.getLogger(ValidadorMensagemCanal.class);
	
	/** Fornecedor dados. */
	private FornecedorDados fornecedorDados;
	
	/** Servico solicitado pelo canal */
	private Mtxtb001Servico servicoChamado;
	
	/** VersaoServico solicitado pelo canal */
	private Mtxtb011VersaoServico versaoServicoChamado;
	
	/** Canal */
	private Mtxtb004Canal canal;
	
	/** MeioEntrada */
	private Mtxtb008MeioEntrada meioEntrada;
	
	/** ServicoCanal */
	private Mtxtb005ServicoCanal servicoCanal;
	
	/** SimtxConfig */
	private SimtxConfig simtxConfig;
	
	/** Arquivode propriedades */
	protected Properties properties;
	
	/** Validador de Xml */
    protected XmlValidador xmlValidador;
	
    /** Data Hora */
    private String dataHora;
    
    /** codigo do meio de entrada */
    private Integer numeroMeioEntrada;
    
    /** nome do meio de entrada */
    private String nomeMeioEntrada;
    
	
	/**
	 * Construtor.
	 * 
	 */
	public ValidadorMensagemCanal() {
		this.xmlValidador = new XmlValidador();
	}
	
	/**
	 * Valida a estrutura do xml de requisicao do Canal e demais requisitos.
	 * 
	 * @param mensagem
	 * @return
	 * @throws Exception
	 */
	public boolean validarMensagemEntradaCanalMigrado(String mensagem) throws Exception {
		logger.info("Validando mensagem recebida pelo Canal");
		logger.info("Servico migrado");
		
		File fileXsd = new File(this.simtxConfig.getCaminhoXsd() + this.versaoServicoChamado.getDeXsdRequisicao());
		this.xmlValidador.validarXmlcomXsd(mensagem, fileXsd);
		validarCanal();
		validarServico();
		validarServicoCanal();
		validarParametroDoCanal();
		validarTarefasServico();
		validarMeioEntrada();
		validarMeioEntradaServico();
		validarTarefasMeioEntrada();
		return true;
	}
	
	/**
	 * Valida a estrutura do xml de requisicao do Canal e demais requisitos.
	 * 
	 * @param mensagem
	 * @return
	 * @throws Exception
	 */
	public boolean validarMensagemEntradaCanalNaoMigrado(String mensagem) throws Exception {
		logger.info("Validando mensagem recebida pelo Canal");
		logger.info("Servico nao migrado");
		boolean resposta = true;
		
		File fileXsd = new File(this.simtxConfig.getCaminhoXsd() + this.versaoServicoChamado.getDeXsdRequisicao());
		this.xmlValidador.validarXmlcomXsd(mensagem, fileXsd);
		validarCanal();
		validarServico();
		validarServicoCanal();
		validarParametroDoCanal();
		validarTarefasServico();
		return resposta;
	}
	
	/**
	 * Valida a mensagem recebida do Sibar.
	 * 
	 * @param mensagem
	 * @return
	 * @throws Exception
	 */
	public boolean validarMensagemSaidaSibar(String mensagem) throws Exception {
		if(this.versaoServicoChamado.isMigrado())
			return true;
		
		logger.info("Validando mensagem recebida do Sibar");
		
		if (mensagem == null) {
			throw new SimtxException("MN016");
		}
		
		File fileXsd = new File(this.simtxConfig.getCaminhoXsd() + this.versaoServicoChamado.getDeXsdResposta());
		this.xmlValidador.validarXmlcomXsd(mensagem, fileXsd);
		return true;
	}
	
	
	
	/**
	 * Valida o Servico.
	 * 
	 * @throws Exception
	 */
	public void validarServico() throws Exception {
		logger.info("Validando Servico");
		if(this.servicoChamado.getIcSituacaoServico().compareTo(Constantes.IC_SITUACAO_INATIVO) == 0) {
			throw new SimtxException("MN007");
		}
		else if(this.versaoServicoChamado.getIcStcoVrsoSrvco().compareTo(Constantes.IC_SITUACAO_INATIVO.intValueExact()) == 0) {
			throw new SimtxException("MN008");
		}
	}
	
	/**
	 * Valida o Servico para o Canal.
	 * 
	 * @throws Exception
	 */
	public void validarServicoCanal() throws Exception {
		logger.info("Validando servico para o canal");
		final Mtxtb005ServicoCanalPK servicoCanalPK = new Mtxtb005ServicoCanalPK();
		servicoCanalPK.setNuCanal004(this.canal.getNuCanal());
		servicoCanalPK.setNuServico001(this.servicoChamado.getNuServico());
		this.servicoCanal = this.fornecedorDados.buscarServicoCanalPorPK(servicoCanalPK);
		if (this.servicoCanal != null) {
			if(this.servicoCanal.getIcStcoSrvcoCanal().compareTo(Constantes.IC_SITUACAO_INATIVO) == 0) {
		        throw new SimtxException("MN013");
		    }
		} 
		else {
		    throw new SimtxException("MN013");
		}
	}
	
	/**
	 * Valida o Canal.
	 * 
	 * @throws Exception
	 */
    public void validarCanal() throws Exception {
        logger.info("Validando canal");
        if (this.canal != null && this.canal.getNuCanal() > 3) {
        	if(this.canal.getIcSituacaoCanal().compareTo(Constantes.IC_SITUACAO_INATIVO) == 0) {
                throw new SimtxException("MN011");
            }
        } 
        else {
            throw new SimtxException("MN003");
        }
    }
	
	/**
     * Valida Meio Entrada.
     *
     * @throws Exception
     */
    public void validarMeioEntrada() throws Exception {
    	logger.info("Validando meio de entrada");
    	this.meioEntrada = new Mtxtb008MeioEntrada();
    	if(this.numeroMeioEntrada != null)
    		this.meioEntrada.setNuMeioEntrada(this.numeroMeioEntrada);
    	
    	this.meioEntrada.setNoMeioEntrada(this.nomeMeioEntrada); //this.header.getIDPROCESSO()
    	
    	if(this.numeroMeioEntrada != null)
    		this.meioEntrada = this.fornecedorDados.buscarMeioEntradaPorPK(this.meioEntrada);
    	else
    		this.meioEntrada = this.fornecedorDados.buscarMeioEntradaPorNome(this.meioEntrada);
    	
    	if(this.meioEntrada != null) {
    		if(this.meioEntrada.getIcSituacao().compareTo(Constantes.IC_SITUACAO_INATIVO) == 0) {
    			throw new SimtxException("MN010");
    		}
    	}
    	else {
    		throw new SimtxException("MN009");
    	}
    }
    
    /**
     * Valida Meio Entrada para o Servico.
     * 
     * @throws Exception
     */
    public void validarMeioEntradaServico() throws Exception {
    	logger.info("Validando meio de entrada para o servico");
		Mtxtb018VrsoMeioEntraSrvcoPK meioEntradaServicoPK = new Mtxtb018VrsoMeioEntraSrvcoPK();
		meioEntradaServicoPK.setNuMeioEntrada008(this.meioEntrada.getNuMeioEntrada());
		meioEntradaServicoPK.setNuServico011(this.servicoChamado.getNuServico());
		meioEntradaServicoPK.setNuVersaoServico011(this.versaoServicoChamado.getId().getNuVersaoServico());
		Mtxtb018VrsoMeioEntraSrvco meioEntrada = this.fornecedorDados.buscarVersaoMeioEntraServcoPorPK(meioEntradaServicoPK);

		if (meioEntrada != null) {
			if(meioEntrada.getIcSituacao().compareTo(Constantes.IC_SITUACAO_INATIVO) == 0) {
		        throw new SimtxException("MN015");
		    }
		}
		else {
		    throw new SimtxException("MN014");
		}
	}
    
    /**
     * Valida parametros do canal.
     * 
     * @throws Exception
     */
    public void validarParametroDoCanalHora() throws Exception {
		Mtxtb005ServicoCanalPK servicoCanalPK = new Mtxtb005ServicoCanalPK();
		servicoCanalPK.setNuServico001(this.servicoChamado.getNuServico());
		servicoCanalPK.setNuCanal004(this.canal.getNuCanal());
		Mtxtb013PrmtoSrvcoCanal parametro = this.fornecedorDados.buscarParametroSrvcoCanal(servicoCanalPK);
		
		if(parametro != null) {
			logger.info("Validando parametros do canal para o servico solicitado");
		    SimpleDateFormat formatoData = new SimpleDateFormat("yyyyMMdd");
		    formatoData.setLenient(false);
		
		    SimpleDateFormat sdfConvert = new SimpleDateFormat("HHmmss");
		    sdfConvert.setLenient(false);
		    Date horaRequisicao = sdfConvert.parse(this.dataHora.substring(8, this.dataHora.length()));
		    Date horaReq = sdfConvert.parse(sdfConvert.format(horaRequisicao));
		    Date horaInicio = sdfConvert.parse(sdfConvert.format(parametro.getHhLimiteInicio()));
		    Date horaFim = sdfConvert.parse(sdfConvert.format(parametro.getHhLimiteFim()));
		    if (horaReq.before(horaInicio) || horaReq.after(horaFim)) {
		        throw new SimtxException("MN019");
		    }
		}
		else{
			logger.info("Servico solicitado nao possui parametros cadastrados para o canal");
		}
	}
    
    /**
     * Valida parametros do canal.
     * 
     * @throws Exception
     */
    private void validarParametroDoCanal() {
    	try {
			Mtxtb005ServicoCanalPK servicoCanalPK = new Mtxtb005ServicoCanalPK();
			servicoCanalPK.setNuServico001(this.servicoChamado.getNuServico());
			servicoCanalPK.setNuCanal004(this.canal.getNuCanal());
			Mtxtb013PrmtoSrvcoCanal parametro = this.fornecedorDados.buscarParametroSrvcoCanal(servicoCanalPK);
			
			logger.info("Validando parametros do canal para o servico solicitado");
		    SimpleDateFormat formatoData = new SimpleDateFormat("yyyyMMdd");
		    formatoData.setLenient(false);
		    Date dataRequisicao = formatoData.parse(this.dataHora.substring(0, 8));
		
		    if (dataRequisicao.before(parametro.getDtIncoSlctoSrvco())
		        || dataRequisicao.after(parametro.getDtFimSlctoSrvco())) {
		        throw new SimtxException("MN020");
		    }
		
		    SimpleDateFormat sdfConvert = new SimpleDateFormat("HHmmss");
		    sdfConvert.setLenient(false);
		    Date horaRequisicao = sdfConvert.parse(this.dataHora.substring(8, this.dataHora.length()));
		    Date horaReq = sdfConvert.parse(sdfConvert.format(horaRequisicao));
		    Date horaInicio = sdfConvert.parse(sdfConvert.format(parametro.getHhLimiteInicio()));
		    Date horaFim = sdfConvert.parse(sdfConvert.format(parametro.getHhLimiteFim()));
		    if (horaReq.before(horaInicio) || horaReq.after(horaFim)) {
		        throw new SimtxException("MN019");
		    }
    	} 
    	catch (Exception e) {
    		logger.info("Servico solicitado nao possui parametros cadastrados para o canal");
		}
	}
    
    /**
     * Valida as tarefas de acordo com o Servico e o Canal solicitado.
     * 
     * @param nuVersaoServico
     * @param nuServico
     * @param nuCanal
     * @throws Exception
     */
    public void validarTarefasServico() throws Exception {
    	logger.info("Validando tarefas para o servico");
        Mtxtb020SrvcoTarfaCanalPK servicoTarefaCanalPK = new Mtxtb020SrvcoTarfaCanalPK();
        servicoTarefaCanalPK.setNuServico003(this.servicoChamado.getNuServico());
        servicoTarefaCanalPK.setNuVersaoServico003(this.versaoServicoChamado.getId().getNuVersaoServico());
        servicoTarefaCanalPK.setNuCanal004(this.canal.getNuCanal());
        List<Mtxtb020SrvcoTarfaCanal> listaTarefasPorServicoCanal = this.fornecedorDados.buscarTarefasPorServicoCanal(servicoTarefaCanalPK);
        
        if(listaTarefasPorServicoCanal != null && !listaTarefasPorServicoCanal.isEmpty()) {
	        for (Mtxtb020SrvcoTarfaCanal servicoTarefaCanal : listaTarefasPorServicoCanal) {
	        	if (servicoTarefaCanal.getMtxtb003ServicoTarefa().getIcSituacao().compareTo(Constantes.IC_SITUACAO_INATIVO) == 0) {
					if (servicoTarefaCanal.getMtxtb003ServicoTarefa().getMtxtb012VersaoTarefa().getMtxtb002Tarefa()
							.getIcTipoTarefa().equals(Constantes.IC_TAREFA_MEIOENTRADA)) {
						/**Nao barrar o servico caso tarefa esteja inativa.*/
						logger.info(servicoTarefaCanal.getId().getNuTarefa003() + 
								"[" + servicoTarefaCanal.getMtxtb003ServicoTarefa().getMtxtb012VersaoTarefa()
										.getMtxtb002Tarefa().getNoTarefa() +"] Versao da Tarefa Inativa para o Servico.");
					}
	        		else {
						throw new SimtxException("MN023");
					}
	        	}
				if (servicoTarefaCanal.getMtxtb003ServicoTarefa().getMtxtb012VersaoTarefa().getIcSituacao()
						.compareTo(Constantes.IC_SITUACAO_INATIVO) == 0) {
					if (servicoTarefaCanal.getMtxtb003ServicoTarefa().getMtxtb012VersaoTarefa().getMtxtb002Tarefa()
							.getIcTipoTarefa().equals(Constantes.IC_TAREFA_MEIOENTRADA)) {
						/**Nao barrar o servico caso tarefa esteja inativa.*/
						logger.info(servicoTarefaCanal.getId().getNuTarefa003() + 
								"[" + servicoTarefaCanal.getMtxtb003ServicoTarefa().getMtxtb012VersaoTarefa()
										.getMtxtb002Tarefa().getNoTarefa() +"] Versao da Tarefa Inativa.");
					}
	        		else {
						throw new SimtxException("MN024");
					}
	        	}
	        }
        }
        else {
        	throw new SimtxException("MN025");
        }
    }
	
    /**
     * Valida as tarefas do Meio de Entrada.
     * 
     * @throws Exception
     */
    public void validarTarefasMeioEntrada() throws Exception {
    	logger.info("Validando tarefas para o meio de entrada");
		Mtxtb010VrsoTarfaMeioEntraPK vrsoTarfaMeioEntraPK = new Mtxtb010VrsoTarfaMeioEntraPK();
		vrsoTarfaMeioEntraPK.setNuMeioEntrada008(this.meioEntrada.getNuMeioEntrada());

		List<Mtxtb010VrsoTarfaMeioEntra> listaTarefasExecutarMeioEntrada = this.fornecedorDados
				.buscarTarefasPorMeioEntrada(vrsoTarfaMeioEntraPK);

		if (listaTarefasExecutarMeioEntrada != null && !listaTarefasExecutarMeioEntrada.isEmpty()) {
			for (Mtxtb010VrsoTarfaMeioEntra tarefasExecutar : listaTarefasExecutarMeioEntrada) {
				if (tarefasExecutar.getIcSituacao().compareTo(Constantes.IC_SITUACAO_INATIVO) == 0) {
					if (tarefasExecutar.getMtxtb012VersaoTarefa().getMtxtb002Tarefa()
							.getIcTipoTarefa().equals(Constantes.IC_TAREFA_MEIOENTRADA)) {
						/**Nao barrar o servico caso tarefa esteja inativa.*/
						logger.info(tarefasExecutar.getId().getNuTarefa012() + 
								"[" + tarefasExecutar.getMtxtb012VersaoTarefa()
										.getMtxtb002Tarefa().getNoTarefa() +"] Versao da Tarefa Inativa para o Meio de Entrada.");
					}
	        		else {
						throw new SimtxException("MN026");
					}
				} 
			}
		} 
		else {
			throw new SimtxException("MN025");
		}
	}
    
    /**
     * Verifica se o codigo de retorno do Xml e impeditiva.
     * 
     * @param mensagem
     * @param versaoServico
     * @throws ServicoException
     */
//	public void verificarCodigoImpeditivo(String mensagem, Mtxtb011VersaoServico versaoServico) throws ServicoException {
//    	try {
//    		logger.info("Verificando codigo de retorno");
//    		if(versaoServico.isMigrado()) {
//    			BuscadorTextoXml buscadorRsp = new BuscadorTextoXml(mensagem);
//    			String codRetorno = buscadorRsp.xpathTexto("/*[1]/CONTROLE_NEGOCIAL/COD_RETORNO");
//    			if (AcaoRetorno.IMPEDITIVA.getTipo()
//    					.equals(buscarMensagem(codRetorno).getIcTipoMensagem())) {
//    				logger.error("Codigo de retorno e impeditivo");
//    				throw new ServicoException(Constantes.CODIGO_ERRO_DESCONHECIDO,
//    						this.mensagemServidor.recuperarMensagem(Constantes.TRANSACAO_NEGADA),
//    						this.mensagemServidor.recuperarMensagem("erro.tarefa.impeditiva"), 
//    						Constantes.ORIGEM_SIMTX);
//    			}
//    		}
//		} 
//    	catch (Exception e) {
//    		logger.error(e);
//    		throw new ServicoException(Constantes.CODIGO_ERRO_DESCONHECIDO,
//					this.mensagemServidor.recuperarMensagem(Constantes.TRANSACAO_NEGADA),
//					this.mensagemServidor.recuperarMensagem("erro.encontrar.codigo.retorno"), 
//					Constantes.ORIGEM_SIMTX);
//    	}
//    }
	
	/**
	 * Determina situacao final da Transacao.
	 * 
	 * @return
	 * @throws Exception
	 */
	public BigDecimal situacaoFinalTransacao(Mtxtb011VersaoServico servico) {
		if (servico.getMtxtb001Servico().getIcConfirmaTransacao()
				.compareTo(Constantes.IC_CONFIRMACAO_TRANSACAO) == 0) {
			/**
			 * Servico requer confirmacao/cancelamento para ser
			 * finalizado
			 */
			logger.info(Constantes.SITUACAO_FINAL_TRANSACAO_PENDENTE);
			return Constantes.IC_SERVICO_PENDENTE;
		} 
		else {
			logger.info(Constantes.SITUACAO_FINAL_TRANSACAO_FINALIZADA);
			return Constantes.IC_SERVICO_FINALIZADO;
		}
	}
	
    
    /**
	 * Busca a mensagem na tabela de acordo com o Codigo de Retorno.
	 * 
	 * @param codigo
	 * @return
	 * @throws Exception
	 */
//	public Mtxtb006Mensagem buscarMensagem(String codigo) {
//		return this.fornecedorDados.buscarMensagem(codigo);
//	}
	
	
	public FornecedorDados getFornecedorDados() {
		return fornecedorDados;
	}

	public void setFornecedorDados(FornecedorDados fornecedorDados) {
		this.fornecedorDados = fornecedorDados;
	}

	public Mtxtb001Servico getServicoChamado() {
		return servicoChamado;
	}

	public void setServicoChamado(Mtxtb001Servico servicoChamado) {
		this.servicoChamado = servicoChamado;
	}

	public Mtxtb011VersaoServico getVersaoServicoChamado() {
		return versaoServicoChamado;
	}

	public void setVersaoServicoChamado(Mtxtb011VersaoServico versaoServicoChamado) {
		this.versaoServicoChamado = versaoServicoChamado;
	}

	public Mtxtb004Canal getCanal() {
		return canal;
	}

	public void setCanal(Mtxtb004Canal canal) {
		this.canal = canal;
	}

	public Mtxtb008MeioEntrada getMeioEntrada() {
		return meioEntrada;
	}

	public void setMeioEntrada(Mtxtb008MeioEntrada meioEntrada) {
		this.meioEntrada = meioEntrada;
	}

	public Mtxtb005ServicoCanal getServicoCanal() {
		return servicoCanal;
	}

	public void setServicoCanal(Mtxtb005ServicoCanal servicoCanal) {
		this.servicoCanal = servicoCanal;
	}

	public SimtxConfig getSimtxConfig() {
		return simtxConfig;
	}

	public void setSimtxConfig(SimtxConfig simtxConfig) {
		this.simtxConfig = simtxConfig;
	}

	public Properties getProperties() {
		return properties;
	}

	public void setProperties(Properties properties) {
		this.properties = properties;
	}
	
	public void setDataHora(String dataHora) {
		this.dataHora = dataHora;
	}

	public void setNumeroMeioEntrada(Integer numeroMeioEntrada) {
		this.numeroMeioEntrada = numeroMeioEntrada;
	}

	public void setNomeMeioEntrada(String nomeMeioEntrada) {
		this.nomeMeioEntrada = nomeMeioEntrada;
	}
}
