package br.gov.caixa.simtx.util.gerenciador;

import java.io.File;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.apache.log4j.Logger;

import br.gov.caixa.simtx.persistencia.cache.core.FornecedorDados;
import br.gov.caixa.simtx.persistencia.constante.AcaoRetorno;
import br.gov.caixa.simtx.persistencia.constante.Constantes;
import br.gov.caixa.simtx.persistencia.constante.MensagemRetorno;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb001Servico;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb004Canal;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb005ServicoCanal;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb005ServicoCanalPK;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb006Mensagem;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb008MeioEntrada;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb010VrsoTarfaMeioEntra;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb010VrsoTarfaMeioEntraPK;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb011VersaoServico;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb012VersaoTarefa;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb013PrmtoSrvcoCanal;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb014Transacao;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb018VrsoMeioEntraSrvco;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb018VrsoMeioEntraSrvcoPK;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb020SrvcoTarfaCanal;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb020SrvcoTarfaCanalPK;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb023Parametro;
import br.gov.caixa.simtx.persistencia.exception.SemResultadoException;
import br.gov.caixa.simtx.persistencia.vo.TipoContingenciaEnum;
import br.gov.caixa.simtx.util.SimtxConfig;
import br.gov.caixa.simtx.util.XmlValidador;
import br.gov.caixa.simtx.util.enums.ValorServicoEnum;
import br.gov.caixa.simtx.util.exception.ServicoException;
import br.gov.caixa.simtx.util.xml.BuscadorResposta;
import br.gov.caixa.simtx.util.xml.BuscadorTextoXml;
import br.gov.caixa.simtx.util.xml.Resposta;

/**
 * Classe responsavel por validar e verificar todos os requisitos de negocio
 * para dar continuidade no processo de execucao do Servico. 
 * Ex: MeioEntrada Ativo, Tarefas Ativas etc.
 * 
 * @author rctoscano
 *
 */
@Stateless
public class ValidadorRegrasNegocio implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private static final Logger logger = Logger.getLogger(ValidadorRegrasNegocio.class);
	
	@Inject
	private FornecedorDados fornecedorDados;
	
	@Inject
	private Mensagem mensagemServidor;
	
	@Inject
	private SimtxConfig simtxConfig;
	
	
	
	/**
	 * Verifica se o servico e migrado ou nao para chamar o metodo correspondente.
	 * 
	 * @param xml
	 * @param canal
	 * @param meioEntrada
	 * @param versaoServico
	 * @throws ServicoException
	 */
	public void validarRegras(String xml, Mtxtb004Canal canal, Mtxtb008MeioEntrada meioEntrada,
			Mtxtb011VersaoServico versaoServico) throws ServicoException {
		if(versaoServico.isMigrado()) {
			validarRegrasMigrado(xml, canal, meioEntrada, versaoServico);
		}
		else {
			validarRegrasNaoMigrado(xml, canal, versaoServico);
		}
	}
	
	/**
	 * Valida todas as regras de negocio para o Servico Migrado.
	 * 
	 * @param xml
	 * @param canal
	 * @param meioEntrada
	 * @param versaoServico
	 * @throws ServicoException
	 */
	public void validarRegrasMigrado(String xml, Mtxtb004Canal canal, Mtxtb008MeioEntrada meioEntrada,
			Mtxtb011VersaoServico versaoServico) throws ServicoException {
		validarXmlComXsd(xml, versaoServico);
		validarDatasHeader(xml);
		validarCanal(canal);
		validarServicoEversao(versaoServico.getMtxtb001Servico(), versaoServico);
		validarServicoCanal(versaoServico.getMtxtb001Servico(), canal);
		validarParametrosDoCanal(versaoServico, canal, xml);
		validarTarefasServico(versaoServico, canal);
		validarMeioEntrada(meioEntrada);
		validarMeioEntradaServico(meioEntrada, versaoServico);
		validarTarefasMeioEntrada(meioEntrada);
		validarDataAgendamento(xml, versaoServico);
	}
	
	/**
	 * Valida todas as regras de negocio para o Servico Migrado.
	 * 
	 * @param xml
	 * @param canal
	 * @param meioEntrada
	 * @param versaoServico
	 * @throws ServicoException
	 */
	public void validarRegrasMigradoSemParametros(String xml, Mtxtb004Canal canal, Mtxtb008MeioEntrada meioEntrada,
			Mtxtb011VersaoServico versaoServico) throws ServicoException {
		logger.info("Validando regras para o servico migrado");
		validarXmlComXsd(xml, versaoServico);
		validarDatasHeader(xml);
		validarCanal(canal);
		validarServicoEversao(versaoServico.getMtxtb001Servico(), versaoServico);
		validarServicoCanal(versaoServico.getMtxtb001Servico(), canal);
		validarTarefasServico(versaoServico, canal);
		validarMeioEntrada(meioEntrada);
		validarMeioEntradaServico(meioEntrada, versaoServico);
		validarTarefasMeioEntrada(meioEntrada);
		validarDataAgendamento(xml, versaoServico);
	}
	
	/**
	 * Valida todas as regras de negocio para o Servico que nao e Migrado.
	 * 
	 * @param xml
	 * @param canal
	 * @param versaoServico
	 * @throws ServicoException
	 */
	public void validarRegrasNaoMigrado(String xml, Mtxtb004Canal canal, Mtxtb011VersaoServico versaoServico)
			throws ServicoException {
		validarXmlComXsd(xml, versaoServico);
		validarCanal(canal);
		validarServicoEversao(versaoServico.getMtxtb001Servico(), versaoServico);
		validarServicoCanal(versaoServico.getMtxtb001Servico(), canal);
		validarParametrosDoCanal(versaoServico, canal, xml);
		validarTarefasServico(versaoServico, canal);
	}
	
	/**
	 * Valida a estrutura da mensagem com o Xsd do Servico.
	 * 
	 * @param xml
	 * @param versaoServico
	 * @throws ServicoException
	 */
	public void validarXmlComXsd(String xml, Mtxtb011VersaoServico versaoServico) throws ServicoException {
		try {
			logger.info("Validando xml com xsd");
			File fileXsd = new File(this.simtxConfig.getCaminhoXsd() + versaoServico.getDeXsdRequisicao());
			XmlValidador xmlValidador = new XmlValidador();
			xmlValidador.validarXmlcomXsd(xml, fileXsd);
		} 
		catch (Exception e) {
			logger.error(e);

			Mtxtb006Mensagem mensagem = this.fornecedorDados.buscarMensagem(MensagemRetorno.LEIAUTE_MENSAGEM_INVALIDA);
			throw new ServicoException(mensagem, Constantes.ORIGEM_SIMTX);
		}
	}
	
	/**
	 * Valida se as Datas do Header sao futuras.
	 * 
	 * @param xml
	 * @throws ServicoException
	 */
	public void validarDatasHeader(String xml) throws ServicoException {
		try {
			logger.info("Validando header");
			SimpleDateFormat formatoData = new SimpleDateFormat("yyyyMMdd");
			formatoData.setLenient(false);
			
			BuscadorTextoXml buscador = new BuscadorTextoXml(xml);
			Date dataReferencia = formatoData.parse(buscador.xpathTexto("/*[1]/HEADER/DATA_REFERENCIA"));
			Date dataAtual = formatoData.parse(formatoData.format(new Date()));
			
			if(dataReferencia.after(dataAtual) || dataReferencia.before(dataAtual)) {
				Mtxtb006Mensagem mensagem = this.fornecedorDados
						.buscarMensagem(MensagemRetorno.DATA_REFERENCIA_INVALIDA);
				
				throw new ServicoException(mensagem, Constantes.ORIGEM_SIMTX);
			}
		} 
		catch (ServicoException se) {
			throw se;
		}
		catch (Exception e) {
			logger.error(e);
			Mtxtb006Mensagem mensagem = this.fornecedorDados.buscarMensagem(MensagemRetorno.LEIAUTE_MENSAGEM_INVALIDA);
			throw new ServicoException(mensagem, Constantes.ORIGEM_SIMTX);
		}
	}
	
	/**
	 * Valida a estrutura da mensagem com o Xsd do Servico.
	 * 
	 * @param xml
	 * @param versaoTarefa
	 * @throws ServicoException
	 */
	public void validarXmlComXsd(String xml, Mtxtb012VersaoTarefa versaoTarefa) throws ServicoException {
		try {
			File fileXsd = new File(this.simtxConfig.getCaminhoXsd() + versaoTarefa.getDeXsdRequisicao());
			XmlValidador xmlValidador = new XmlValidador();
			xmlValidador.validarXmlcomXsd(xml, fileXsd);
		} 
		catch (Exception e) {
			logger.error(e);
			
			Mtxtb006Mensagem mensagem = this.fornecedorDados.buscarMensagem(MensagemRetorno.LEIAUTE_MENSAGEM_INVALIDA);
			throw new ServicoException(mensagem, Constantes.ORIGEM_SIMTX);
		}
	}
	
	/**
	 * Valida a estrutura da mensagem com o Xsd informado.
	 * 
	 * @param xml
	 * @param xsd
	 * @throws ServicoException
	 */
	public void validarXmlComXsd(String xml, File xsd) throws ServicoException {
		try {
			File fileXsd = new File(this.simtxConfig.getCaminhoXsd() + xsd);
			XmlValidador xmlValidador = new XmlValidador();
			xmlValidador.validarXmlcomXsd(xml, fileXsd);
		} 
		catch (Exception e) {
			logger.error(e);
			
			Mtxtb006Mensagem mensagem = this.fornecedorDados.buscarMensagem(MensagemRetorno.LEIAUTE_MENSAGEM_INVALIDA);
			throw new ServicoException(mensagem, Constantes.ORIGEM_SIMTX);
		}
	}
	
	/**
	 * Valida se o Canal existe e se esta ativo.
	 * 
	 * @param canal
	 * @throws ServicoException
	 */
	public void validarCanal(Mtxtb004Canal canal) throws ServicoException {
        logger.info("Validando canal");
        if (canal != null && canal.getNuCanal() > 3) {
        	if(canal.getIcSituacaoCanal().compareTo(Constantes.IC_SITUACAO_INATIVO) == 0) {
    			Mtxtb006Mensagem mensagem = this.fornecedorDados.buscarMensagem(MensagemRetorno.CANAL_ATENDIMENTO_INATIVO);
    			throw new ServicoException(mensagem, Constantes.ORIGEM_SIMTX);
            }
        } 
        else {
        	Mtxtb006Mensagem mensagem = this.fornecedorDados.buscarMensagem(MensagemRetorno.CANAL_INEXISTENTE);
			throw new ServicoException(mensagem, Constantes.ORIGEM_SIMTX);
        }
    }
	
	/**
	 * Valida se o Servico e a Versao do Servico estao ativas.
	 * 
	 * @param servico
	 * @param versaoServico
	 * @throws Exception
	 */
	public void validarServicoEversao(Mtxtb001Servico servico, Mtxtb011VersaoServico versaoServico) throws ServicoException {
		logger.info("Validando Servico");
		if(servico.getIcSituacaoServico().compareTo(Constantes.IC_SITUACAO_INATIVO) == 0) {
			Mtxtb006Mensagem mensagem = this.fornecedorDados.buscarMensagem(MensagemRetorno.SERVICO_INATIVO);
			throw new ServicoException(mensagem, Constantes.ORIGEM_SIMTX);
		}
		else if(versaoServico.getIcStcoVrsoSrvco().compareTo(Constantes.IC_SITUACAO_INATIVO.intValueExact()) == 0) {
			Mtxtb006Mensagem mensagem = this.fornecedorDados.buscarMensagem(MensagemRetorno.VERSAO_SERVICO_INATIVA);
			throw new ServicoException(mensagem, Constantes.ORIGEM_SIMTX);
		}
	}
	
	/**
	 * Valida se o Servico esta associado ao canal e se esta ativo.
	 * 
	 * @param servico
	 * @param canal
	 * @throws Exception
	 */
	public void validarServicoCanal(Mtxtb001Servico servico, Mtxtb004Canal canal) throws ServicoException {
		try {
			logger.info("Validando servico para o canal");
			final Mtxtb005ServicoCanalPK servicoCanalPK = new Mtxtb005ServicoCanalPK();
			servicoCanalPK.setNuCanal004(canal.getNuCanal());
			servicoCanalPK.setNuServico001(servico.getNuServico());
			Mtxtb005ServicoCanal servicoCanal = this.fornecedorDados.buscarServicoCanalPorPK(servicoCanalPK);
			if(servicoCanal.getIcStcoSrvcoCanal().compareTo(Constantes.IC_SITUACAO_INATIVO) == 0) {
		        Mtxtb006Mensagem mensagem = this.fornecedorDados.buscarMensagem(MensagemRetorno.SERVICO_INATIVO_CANAL);
    			throw new ServicoException(mensagem, Constantes.ORIGEM_SIMTX);
		    }
		} 
		catch (Exception e) {
			Mtxtb006Mensagem mensagem = this.fornecedorDados.buscarMensagem(MensagemRetorno.SERVICO_INEXISTENTE_PARA_CANAL);
			throw new ServicoException(mensagem, Constantes.ORIGEM_SIMTX);
		}
	}
	
	/**
	 * Valida os parametros restritivos do Servico para o Canal.
	 * {@link Mtxtb013PrmtoSrvcoCanal}
	 * 
	 * @param versaoServico
	 * @param canal
	 * @throws ServicoException
	 */
	public void validarParametrosDoCanal(Mtxtb011VersaoServico versaoServico, Mtxtb004Canal canal, String xml) throws ServicoException {
		try {
			Date data = new Date();
			
			Mtxtb005ServicoCanalPK servicoCanalPK = new Mtxtb005ServicoCanalPK();
			servicoCanalPK.setNuServico001(versaoServico.getId().getNuServico001());
			servicoCanalPK.setNuCanal004(canal.getNuCanal());
			Mtxtb013PrmtoSrvcoCanal parametro = this.fornecedorDados.buscarParametroSrvcoCanal(servicoCanalPK);
			
			logger.info("Validando parametros do canal para o servico solicitado");
		    
		    if(parametro.getDtIncoSlctoSrvco() != null && parametro.getDtFimSlctoSrvco() != null) {
				validarDataParametrosDoCanal(data, parametro.getDtIncoSlctoSrvco(), parametro.getDtFimSlctoSrvco());
		    }
		
		    if(parametro.getHhLimiteInicio() != null && parametro.getHhLimiteFim() != null) {
				validarHorarioParametrosDoCanal(data, parametro.getHhLimiteInicio(), parametro.getHhLimiteFim());
		    }
		    
		    if(parametro.getVrMnmoSlctoSrvco() != null || parametro.getVrMxmoSlctoSrvco() != null) {
				ValorServicoEnum valorServicoEnum = ValorServicoEnum.obterServico(
						versaoServico.getId().getNuServico001(), versaoServico.getId().getNuVersaoServico());
				
				BuscadorTextoXml buscador = new BuscadorTextoXml(xml);
				BigDecimal valorPagar = new BigDecimal(buscador.xpathTexto(valorServicoEnum.getPath()));
				
				validarValorParametrosDoCanal(valorPagar, parametro.getVrMnmoSlctoSrvco(),
						parametro.getVrMxmoSlctoSrvco());
			}
		} 
		catch (ServicoException e) {
			throw e;
		}
		catch (Exception e) {
			logger.info("Servico solicitado nao possui parametros cadastrados para o canal");
		}
	}

	/**
	 * Valida o Horario permitida entre Servico e Canal.
	 * 
	 * @param dataAtual
	 * @param horaInicio
	 * @param horaFim
	 * @throws ServicoException
	 */
	private void validarHorarioParametrosDoCanal(Date dataAtual, Date horaInicio, Date horaFim) throws ServicoException {
		try {
			SimpleDateFormat formatoHorario = new SimpleDateFormat("HHmmss");
		    formatoHorario.setLenient(false);
		    Date horarioAtual = formatoHorario.parse(formatoHorario.format(dataAtual));
			
			horaInicio = formatoHorario.parse(formatoHorario.format(horaInicio));
			horaFim = formatoHorario.parse(formatoHorario.format(horaFim));
			
			if (horarioAtual.before(horaInicio) || horarioAtual.after(horaFim)) {
				Mtxtb006Mensagem mensagem = this.fornecedorDados
						.buscarMensagem(MensagemRetorno.HORARIO_INVALIDO_TRANSACAO);
				throw new ServicoException(mensagem, Constantes.ORIGEM_SIMTX);
			}
		} 
		catch (ServicoException e) {
			throw e;
		}
		catch (Exception e) {
			logger.error(e);
			Mtxtb006Mensagem mensagem = this.fornecedorDados.buscarMensagem(MensagemRetorno.ERRO_INTERNO);
			throw new ServicoException(mensagem, Constantes.ORIGEM_SIMTX);
		}
	}

	/**
	 * Valida o Data permitida entre Servico e Canal.
	 * 
	 * @param dataAtual
	 * @param dtInicio
	 * @param dtFim
	 * @throws ServicoException
	 */
	private void validarDataParametrosDoCanal(Date dataAtual, Date dtInicio, Date dtFim) throws ServicoException {
		try {
			SimpleDateFormat formatoData = new SimpleDateFormat("yyyyMMdd");
		    formatoData.setLenient(false);
		    dataAtual = formatoData.parse(formatoData.format(dataAtual));
			
			Date dataInicio = formatoData.parse(formatoData.format((dtInicio)));
			Date dataFim = formatoData.parse(formatoData.format((dtFim)));
			
			if (dataAtual.before(dataInicio) || dataAtual.after(dataFim)) {
				Mtxtb006Mensagem mensagem = this.fornecedorDados
						.buscarMensagem(MensagemRetorno.DATA_INVALIDA_TRANSACAO);
				throw new ServicoException(mensagem, Constantes.ORIGEM_SIMTX);
			}
		} 
		catch (ServicoException e) {
			throw e;
		}
		catch (Exception e) {
			logger.error(e);
			Mtxtb006Mensagem mensagem = this.fornecedorDados.buscarMensagem(MensagemRetorno.ERRO_INTERNO);
			throw new ServicoException(mensagem, Constantes.ORIGEM_SIMTX);
		}
	}

	/**
	 * Valida o Valor permitido entre Servico e Canal.
	 * 
	 * @param valorPagar
	 * @param valorMinimo
	 * @param valorMaximo
	 * @throws ServicoException
	 */
	private void validarValorParametrosDoCanal(BigDecimal valorPagar, BigDecimal valorMinimo, BigDecimal valorMaximo)
			throws ServicoException {
		
		if (valorMinimo != null && valorPagar.compareTo(valorMinimo) < 0) {
			logger.error("Valor de pagamento [" + valorPagar + "] abaixo do permitido de acordo com o Servico-Canal");
			Mtxtb006Mensagem mensagem = this.fornecedorDados
					.buscarMensagem(MensagemRetorno.VALOR_INFERIOR_VALOR_MINIMO);
			throw new ServicoException(mensagem, Constantes.ORIGEM_SIMTX);
		} 
		
		if (valorMaximo != null && valorPagar.compareTo(valorMaximo) > 0) {
			logger.error("Valor de pagamento [" + valorPagar + "] acima do permitido de acordo com o Servico-Canal");
			Mtxtb006Mensagem mensagem = this.fornecedorDados
					.buscarMensagem(MensagemRetorno.VALOR_SUPERIOR_VALOR_MAXIMO);
			throw new ServicoException(mensagem, Constantes.ORIGEM_SIMTX);
		}
	}
	
	/**
	 * Valida se as Tarefas estao ativas.
	 * 
	 * @param versaoServico
	 * @param canal
	 * @throws ServicoException
	 */
	public void validarTarefasServico(Mtxtb011VersaoServico versaoServico, Mtxtb004Canal canal)
			throws ServicoException {
    	logger.info("Validando tarefas para o servico");
        Mtxtb020SrvcoTarfaCanalPK servicoTarefaCanalPK = new Mtxtb020SrvcoTarfaCanalPK();
        servicoTarefaCanalPK.setNuServico003(versaoServico.getId().getNuServico001());
        servicoTarefaCanalPK.setNuVersaoServico003(versaoServico.getId().getNuVersaoServico());
        servicoTarefaCanalPK.setNuCanal004(canal.getNuCanal());
		List<Mtxtb020SrvcoTarfaCanal> listaTarefasPorServicoCanal = this.fornecedorDados
				.buscarTarefasPorServicoCanal(servicoTarefaCanalPK);
        
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
	        			logger.error(this.mensagemServidor.recuperarMensagem("erro.carregar.tarefas"));
	        			Mtxtb006Mensagem mensagem = this.fornecedorDados.buscarMensagem(MensagemRetorno.ERRO_INTERNO);
	        			throw new ServicoException(mensagem, Constantes.ORIGEM_SIMTX);
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
	        			Mtxtb006Mensagem mensagem = this.fornecedorDados.buscarMensagem(MensagemRetorno.ERRO_INTERNO);
	        			throw new ServicoException(mensagem, Constantes.ORIGEM_SIMTX);
					}
	        	}
	        }
        }
        else {
        	logger.error(this.mensagemServidor.recuperarMensagem("erro.carregar.tarefas"));
        	Mtxtb006Mensagem mensagem = this.fornecedorDados.buscarMensagem(MensagemRetorno.ERRO_INTERNO);
			throw new ServicoException(mensagem, Constantes.ORIGEM_SIMTX);
        }
    }
	
	/**
	 * Valida se o MeioEntrada existe e se esta ativo.
	 * 
	 * @param meioEntrada
	 * @throws ServicoException
	 */
	public void validarMeioEntrada(Mtxtb008MeioEntrada meioEntrada) throws ServicoException {
		try {
			logger.info("Validando meio de entrada");
			if(meioEntrada.getNuMeioEntrada() == 0L) return;
			meioEntrada = this.fornecedorDados.buscarMeioEntrada(meioEntrada.getNuMeioEntrada());
			if(meioEntrada.getIcSituacao().compareTo(Constantes.IC_SITUACAO_INATIVO) == 0) {
				Mtxtb006Mensagem mensagem = this.fornecedorDados.buscarMensagem(MensagemRetorno.MEIO_ENTRADA_INATIVO);
    			throw new ServicoException(mensagem, Constantes.ORIGEM_SIMTX);
			}
		} 
		catch (ServicoException e) {
			throw e;
		}
		catch (Exception e) {
			Mtxtb006Mensagem mensagem = this.fornecedorDados.buscarMensagem(MensagemRetorno.MEIO_ENTRADA_INEXISTENTE);
			throw new ServicoException(mensagem, Constantes.ORIGEM_SIMTX);
		}
    }
	
	/**
	 * Valida se o MeioEntrada existe e se esta ativo para o Servico.
	 * 
	 * @param meioEntrada
	 * @param versaoServico
	 * @throws ServicoException
	 */
	public void validarMeioEntradaServico(Mtxtb008MeioEntrada meioEntrada, Mtxtb011VersaoServico versaoServico)
			throws ServicoException {
		try {
	    	logger.info("Validando meio de entrada para o Servico");
			Mtxtb018VrsoMeioEntraSrvcoPK meioEntradaServicoPK = new Mtxtb018VrsoMeioEntraSrvcoPK();
			meioEntradaServicoPK.setNuMeioEntrada008(meioEntrada.getNuMeioEntrada());
			meioEntradaServicoPK.setNuServico011(versaoServico.getId().getNuServico001());
			meioEntradaServicoPK.setNuVersaoServico011(versaoServico.getId().getNuVersaoServico());
			Mtxtb018VrsoMeioEntraSrvco meioEntraSrvco = this.fornecedorDados
					.buscarVersaoMeioEntraServcoPorPK(meioEntradaServicoPK);
	
			if(meioEntraSrvco.getIcSituacao().compareTo(Constantes.IC_SITUACAO_INATIVO) == 0) {
				logger.error(this.mensagemServidor.recuperarMensagem("erro.meio.entrada.inativo.servico"));
				Mtxtb006Mensagem mensagem = this.fornecedorDados.buscarMensagem(MensagemRetorno.MEIO_ENTRADA_INATIVO_SERVICO);
				throw new ServicoException(mensagem, Constantes.ORIGEM_SIMTX);
		    }
		} 
		catch (ServicoException e) {
			throw e;
		}
		catch (Exception e) {
			logger.error(this.mensagemServidor.recuperarMensagem("erro.meio.entrada.inexistente.servico"));
			Mtxtb006Mensagem mensagem = this.fornecedorDados.buscarMensagem(MensagemRetorno.MEIO_ENTRADA_INEXISTENTE_SERVICO);
			throw new ServicoException(mensagem, Constantes.ORIGEM_SIMTX);
		}
	}
	
	/**
	 * Valida se a Tarefa esta ativa para o MeioEntrada.
	 * 
	 * @param meioEntrada
	 * @throws ServicoException
	 */
    public void validarTarefasMeioEntrada(Mtxtb008MeioEntrada meioEntrada) throws ServicoException {
    	try {
	    	logger.info("Validando tarefas para o meio de entrada");
			Mtxtb010VrsoTarfaMeioEntraPK vrsoTarfaMeioEntraPK = new Mtxtb010VrsoTarfaMeioEntraPK();
			vrsoTarfaMeioEntraPK.setNuMeioEntrada008(meioEntrada.getNuMeioEntrada());

			List<Mtxtb010VrsoTarfaMeioEntra> listaTarefasExecutarMeioEntrada = this.fornecedorDados
					.buscarTarefasPorMeioEntrada(vrsoTarfaMeioEntraPK);
	
			for (Mtxtb010VrsoTarfaMeioEntra tarefasExecutar : listaTarefasExecutarMeioEntrada) {
				if (tarefasExecutar.getIcSituacao().compareTo(Constantes.IC_SITUACAO_INATIVO) == 0) {
					if (tarefasExecutar.getMtxtb012VersaoTarefa().getMtxtb002Tarefa()
							.getIcTipoTarefa().compareTo(Constantes.IC_TAREFA_MEIOENTRADA) == 0) {
						/**Nao barrar o servico caso tarefa esteja inativa.*/
						logger.info(tarefasExecutar.getId().getNuTarefa012() + 
								"[" + tarefasExecutar.getMtxtb012VersaoTarefa()
										.getMtxtb002Tarefa().getNoTarefa() +"] Versao da Tarefa Inativa para o Meio de Entrada.");
					}
	        		else {
	        			Mtxtb006Mensagem mensagem = this.fornecedorDados.buscarMensagem(MensagemRetorno.MEIO_ENTRADA_INATIVO);
	    				throw new ServicoException(mensagem, Constantes.ORIGEM_SIMTX);
					}
				} 
			}
    	} 
    	catch (ServicoException e) {
			throw e;
		}
    	catch (Exception e) {
    		logger.error(e.getMessage(), e);
    		Mtxtb006Mensagem mensagem = this.fornecedorDados.buscarMensagem(MensagemRetorno.ERRO_RECUPERAR_TAREFAS);
			throw new ServicoException(mensagem, Constantes.ORIGEM_SIMTX);
		}
	}
    
    /**
	 * Verifica se a Data de Pagamento nao e maior que o permitido.
	 * 
	 * @param xml
	 * @throws ServicoException
	 */
	public void validarDataAgendamento(String xml, Mtxtb011VersaoServico versaoServico) throws ServicoException {
		try {
			if (versaoServico.getId().getNuServico001() == Constantes.CODIGO_VALIDA_BOLETO_NPC
					|| versaoServico.getId().getNuServico001() == Constantes.CODIGO_VALIDA_BOLETO) {
				logger.info("Verificando Periodo Maximo para Agendamento de Transacao");
				SimpleDateFormat formatoData = new SimpleDateFormat("yyyy-MM-dd");
				formatoData.setLenient(false);
				
				BuscadorTextoXml buscador = new BuscadorTextoXml(xml);
				Date dataPagamento = formatoData.parse(buscador.xpathTexto("/*[1]/VALIDA_BOLETO/DATA_PAGAMENTO"));

				Mtxtb023Parametro parametro = this.fornecedorDados.buscarParametroPorPK(Constantes.FLAG_LIM_PAGAMENTO);
				
				Calendar dataFutura = Calendar.getInstance();
				dataFutura.add(Calendar.DAY_OF_MONTH, Integer.parseInt(parametro.getDeConteudoParam()));
				
				if(dataPagamento.after(formatoData.parse(formatoData.format(dataFutura.getTime())))) {
					Mtxtb006Mensagem mensagem = this.fornecedorDados
							.buscarMensagem(MensagemRetorno.DATA_AGENDAMENTO_MAIOR_PERMITIDO);
					throw new ServicoException(mensagem, Constantes.ORIGEM_SIMTX);
				}
			}
		} 
		catch (ServicoException se) {
			throw se;
		}
		catch (Exception e) {
			logger.error(e);
			Mtxtb006Mensagem mensagem = this.fornecedorDados.buscarMensagem(MensagemRetorno.LEIAUTE_MENSAGEM_INVALIDA);
			throw new ServicoException(mensagem, Constantes.ORIGEM_SIMTX);
		}
	}
	
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
     * Verifica se o codigo de retorno do Xml e impeditiva.
     * 
     * @param mensagem - Mensagem da TAREFA
	 * @return 
     * @throws ServicoException
     */
	public Resposta verificarCodigoImpeditivo(BuscadorTextoXml buscadorTarefa, BuscadorTextoXml buscadorSaida,
			Mtxtb012VersaoTarefa vTarefa) throws ServicoException {
		
		logger.info("Verificando codigo de retorno da tarefa ["+vTarefa.getId().getNuTarefa002()+"] "
				+ vTarefa.getMtxtb002Tarefa().getNoTarefa());

		BuscadorResposta buscadorResposta = new BuscadorResposta();

		Resposta respostaBarramentoXml = buscadorResposta.buscarRespostaBarramento(buscadorSaida);
		Resposta respostaTarefaXml = buscadorResposta.buscarRespostaTarefaBarramento(buscadorTarefa);

		if (respostaTarefaXml == null && respostaBarramentoXml == null) {
			recuperarMensagemInvalida();
		} 
		else if (respostaTarefaXml == null) {
			recuperarMensagemSibar(respostaBarramentoXml);
		} 
		
		return recuperarMensagemTarefa(vTarefa, respostaTarefaXml);
	}

	public Resposta recuperarMensagemTarefa(Mtxtb012VersaoTarefa vTarefa, Resposta respostaTarefaXml)
			throws ServicoException {
		logger.info("Buscando Codigo de Retorno ["+respostaTarefaXml.getCodigo()+"] no banco de dados");
		try {
			Mtxtb006Mensagem mensagem = this.fornecedorDados
					.buscarMensagemPorTarefaCodRetorno(respostaTarefaXml.getCodigo(), vTarefa).getMtxtb006Mensagem();
			
			Mtxtb006Mensagem mensagemRetorno = new Mtxtb006Mensagem();
			
			if(mensagem.getDeMensagemTecnica() != null) {
				respostaTarefaXml.setMensagemTecnica(mensagem.getDeMensagemTecnica());
				mensagemRetorno.setDeMensagemTecnica(mensagem.getDeMensagemTecnica());
			}
			else {
				mensagemRetorno.setDeMensagemTecnica(respostaTarefaXml.getMensagemTecnica());
			}
			
			if(mensagem.getDeMensagemNegocial() != null) {
				respostaTarefaXml.setMensagemNegocial(mensagem.getDeMensagemNegocial());
				mensagemRetorno.setDeMensagemNegocial(mensagem.getDeMensagemNegocial());
			}
			else {
				mensagemRetorno.setDeMensagemNegocial(respostaTarefaXml.getMensagemNegocial());
			}
			
			if (mensagem.isImpeditiva()) {
				logger.error("Codigo de retorno ["+respostaTarefaXml.getCodigo()+"] impeditivo");
				mensagemRetorno.setCodigoRetorno(mensagem.getCodigoRetorno());
				mensagemRetorno.setCoMensagem(mensagem.getCoMensagem());
				mensagemRetorno.setIcTipoMensagem(mensagem.getIcTipoMensagem());
				
				throw new ServicoException(mensagemRetorno, respostaTarefaXml.getOrigem());
			}
			respostaTarefaXml.setIcTipoMensagem(mensagem.getIcTipoMensagem());
			respostaTarefaXml.setCodigo(mensagem.getCodigoRetorno());
			
			return respostaTarefaXml;
		} 
		catch (ServicoException e) {
			throw e;
		}
		catch (SemResultadoException e) {
			logger.error("Codigo de Retorno [" + respostaTarefaXml.getCodigo() + "] nao encontrado para a Tarefa ["
					+ vTarefa.getId().getNuTarefa002() + "] " + vTarefa.getMtxtb002Tarefa().getNoTarefa(), e);
			throw new ServicoException(criarMensagem(respostaTarefaXml), respostaTarefaXml.getOrigem());
		}
	}

	public void recuperarMensagemSibar(Resposta respostaBarramentoXml) throws ServicoException {
		try {
			logger.error("Resposta das Tarefas nao localizada");
			Mtxtb006Mensagem mensagem = this.fornecedorDados
					.buscarMensagemPorCodigoMensagem(respostaBarramentoXml.getCodigo());
			throw new ServicoException(mensagem, Constantes.ORIGEM_SIBAR);
		} 
		catch (SemResultadoException e) {
			logger.error("Codigo de retorno [" + respostaBarramentoXml.getCodigo() + "] "
					+ "do Sibar nao encontrado ou retornou mais de um resultado do banco de dados", e);
			throw new ServicoException(criarMensagem(respostaBarramentoXml), Constantes.ORIGEM_SIBAR);
		}
	}

	public void recuperarMensagemInvalida() throws ServicoException {
		logger.error("Resposta das Tarefas e do Barramento vazias");
		Mtxtb006Mensagem mensagem = this.fornecedorDados
				.buscarMensagem(MensagemRetorno.LEIAUTE_MENSAGEM_RESPOSTA_INVALIDO);
		throw new ServicoException(mensagem, Constantes.ORIGEM_SIMTX);
	}

	private Mtxtb006Mensagem criarMensagem(Resposta resposta) {
		Mtxtb006Mensagem mensagem = new Mtxtb006Mensagem();
		mensagem.setCodigoRetorno(resposta.getCodigo());
		mensagem.setCoMensagem(resposta.getCodigo());
		mensagem.setDeMensagemNegocial(resposta.getMensagemRetorno());
		mensagem.setDeMensagemTecnica(resposta.getMensagemRetorno());
		mensagem.setIcTipoMensagem(AcaoRetorno.IMPEDITIVA.getTipo());
		return mensagem;
	}

	/**
	 * Valida as regras para verificar se pode agendar.
	 * 
	 * @param transacao
	 * @throws ServicoException
	 */
	public void validarRegrasAgendamento(Mtxtb014Transacao transacao) throws ServicoException {
		logger.info("Validando regras para agendamento");
		try {
			Mtxtb014Transacao transacaoOrigem = this.fornecedorDados
					.buscarTransacaoOrigem(transacao.getNuNsuTransacaoPai());
			BuscadorTextoXml buscador = new BuscadorTextoXml(
					transacaoOrigem.getMtxtb016IteracaoCanals().get(0).getDeRetorno());

			String contingencia = buscador.xpathTexto("/*[1]/SITUACAO_CONTINGENCIA");
			String modeloCalculo = buscador.xpathTexto("/*[1]/CONSULTA_BOLETO/MODELO_CALCULO");
			
			if(!contingencia.isEmpty() && TipoContingenciaEnum.CIP.getDescricao().equals(contingencia)) {
				logger.error("Nao permitido agendamento em situacao de contingencia: "
						+ TipoContingenciaEnum.CIP.getDescricao());
				Mtxtb006Mensagem mensagem = this.fornecedorDados
						.buscarMensagem(MensagemRetorno.NAO_PERMITE_AGENDAMENTO_CONTINGENCIA_CIP);
				throw new ServicoException(mensagem, Constantes.ORIGEM_SIMTX);
			}
			else if(!modeloCalculo.isEmpty() && modeloCalculo.equals(Constantes.MODELO_CALCULO)) {
				logger.error("Nao permitido agendamento para modelo de calculo: "+Constantes.MODELO_CALCULO);
				Mtxtb006Mensagem mensagem = this.fornecedorDados
						.buscarMensagem(MensagemRetorno.NAO_PERMITE_AGENDAMENTO_MODELO_CALCULO_3);
				throw new ServicoException(mensagem, Constantes.ORIGEM_SIMTX);
			}
		} 
		catch (ServicoException e) {
			throw e;
		}
		catch (Exception e) {
			logger.error(e);
			Mtxtb006Mensagem mensagem = this.fornecedorDados.buscarMensagem(MensagemRetorno.ERRO_INTERNO);
			throw new ServicoException(mensagem, Constantes.ORIGEM_SIMTX);
		}
	}
	
	
	
	public void setFornecedorDados(FornecedorDados fornecedorDados) {
		this.fornecedorDados = fornecedorDados;
	}

	public void setMensagemServidor(Mensagem mensagemServidor) {
		this.mensagemServidor = mensagemServidor;
	}

	public void setSimtxConfig(SimtxConfig simtxConfig) {
		this.simtxConfig = simtxConfig;
	}
}
