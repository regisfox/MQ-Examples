package br.gov.caixa.simtx.integrador;

import java.io.Serializable;
import java.util.Date;

import javax.ejb.Stateless;
import javax.naming.InitialContext;

import org.apache.log4j.Logger;

import br.gov.caixa.simtx.persistencia.constante.Constantes;
import br.gov.caixa.simtx.persistencia.constante.MensagemRetorno;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb006Mensagem;
import br.gov.caixa.simtx.util.ParametrosAdicionais;
import br.gov.caixa.simtx.util.StringUtil;
import br.gov.caixa.simtx.util.data.DataUtil;
import br.gov.caixa.simtx.util.enums.ServicosEnums;
import br.gov.caixa.simtx.util.exception.ServicoException;
import br.gov.caixa.simtx.util.gerenciador.GerenciadorTransacao;
import br.gov.caixa.simtx.util.gerenciador.servico.ProcessadorServicos;
import br.gov.caixa.simtx.util.xml.BuscadorTextoXml;
import br.gov.caixa.simtx.util.xml.JsonConversor;
import br.gov.caixa.simtx.util.xml.XmlUtils;

@Stateless
public class IntegradorChamadorNovo extends GerenciadorTransacao implements Serializable {
	
	private static final long serialVersionUID = 4769517708969083738L;

	private static final Logger logger = Logger.getLogger(IntegradorChamadorNovo.class);
	
	
	
	public void processar(String mensagemCanal, String idMessage, String jndiQueueConnectionFactory, String jndiResponseQueue) {
		try {
			logger.info(" ==== Processo Integrador Iniciado ==== ");
			logger.info("Mensagem recebida:\n" + mensagemCanal.trim());
			
			boolean converterRespostaParaJson = false;
			if(!XmlUtils.isXml(mensagemCanal)) {
				converterJsonParaXml(mensagemCanal);
				converterRespostaParaJson = true;
			}
			
			mensagemCanal = mensagemCanal.replaceAll("<\\?xml.*\\?>", "");
			ParametrosAdicionais parametrosAdicionais = new ParametrosAdicionais(mensagemCanal, idMessage,
					jndiQueueConnectionFactory, jndiResponseQueue, converterRespostaParaJson);
			
			BuscadorTextoXml buscador = new BuscadorTextoXml(parametrosAdicionais.getXmlMensagem());
			Long codigoServico = Long.parseLong(buscador.xpathTexto("/*[1]/HEADER/SERVICO/CODIGO"));
			Integer versaoServico = Integer.parseInt(buscador.xpathTexto("/*[1]/HEADER/SERVICO/VERSAO"));
			String dataFutura = isDataFutura(buscador);
			
			ServicosEnums servicosEnums = buscarServicoEnum(codigoServico, dataFutura);
			if(servicosEnums != null) {
				ProcessadorServicos processadorServicos = (ProcessadorServicos) new InitialContext().lookup(servicosEnums.getJndi());
				processadorServicos.processar(parametrosAdicionais);
			}
			else {
				
				/**
				 * Servico sem validacao. Vai direto pro SIBAR.
				 */
			}
			
			logger.info(" ==== Processo Integrador Finalizado ==== ");
		} 
		catch (Exception e) {
			logger.error(e);
			logger.info(" ==== Processo Integrador Finalizado ==== ");
		}
	}
	
	public String converterJsonParaXml(String mensagem) throws ServicoException {
		try {
			logger.info("Convertendo mensagem JSON para XML");
			mensagem = JsonConversor.toXML(mensagem);
			logger.info("Mensagem JSON convertida para XML:\n"+mensagem);
			return mensagem;
		}
		catch (Exception e) {
			logger.error(e);
			Mtxtb006Mensagem mensagemErroInterno = this.fornecedorDados.buscarMensagem(MensagemRetorno.ERRO_INTERNO);
			throw new ServicoException(mensagemErroInterno, Constantes.ORIGEM_SIMTX);
		}
	}
	
	public String isDataFutura(BuscadorTextoXml buscador) {
		String resposta = "NAO";
		try {
			String dataXml = buscador.xpathTexto("/*[1]/VALIDA_BOLETO/DATA_PAGAMENTO");
			String servicoProcessarAgendamento = buscador.xpathTexto("/*[1]/AGENDAMENTO");
			
			if(dataXml != null && !StringUtil.isEmpty(dataXml)) {
				Date dataAgendamento = DataUtil.simpleDateFormat(DataUtil.FORMATO_DATA_XML).parse(dataXml);
				
				if(DataUtil.dataEntradaEhDepoisDeDataHoje(dataAgendamento)) {
					resposta = "SIM";
				}
			}
			else if(!servicoProcessarAgendamento.isEmpty() && servicoProcessarAgendamento.equals("SIM")) {
				resposta = "SIM";
			}
		} 
		catch (Exception e) {
			resposta = "NAO";
		}
		return resposta;
	}
	
	public ServicosEnums buscarServicoEnum(Long codigoServico, String dataFutura) {
		try {
			logger.info("Buscando processador responsavel no ServicoEnums");
			return ServicosEnums.obterServico(codigoServico, dataFutura);
		} 
		catch (Exception e) {
			logger.error("Nenhum processador encontrado no ServicosEnums. Prosseguindo com o fluxo.");
			return null;
		}
	}

}
