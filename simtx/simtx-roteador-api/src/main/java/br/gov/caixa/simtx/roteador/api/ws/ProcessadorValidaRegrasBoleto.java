package br.gov.caixa.simtx.roteador.api.ws;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map.Entry;

import javax.inject.Inject;
import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.apache.log4j.Logger;

import br.gov.caixa.simtx.persistencia.cache.core.FornecedorDados;
import br.gov.caixa.simtx.persistencia.constante.Constantes;
import br.gov.caixa.simtx.persistencia.constante.MensagemRetorno;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb006Mensagem;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb040PrmtoPgtoContingencia;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb040PrmtoPgtoContingenciaPK;
import br.gov.caixa.simtx.persistencia.vo.TipoBoletoEnum;
import br.gov.caixa.simtx.persistencia.vo.TipoContingenciaEnum;
import br.gov.caixa.simtx.roteador.api.util.ConstantesRoteadorWeb;
import br.gov.caixa.simtx.roteador.api.ws.matriz.LeitorMatrizValidaRegrasBoleto;
import br.gov.caixa.simtx.roteador.api.ws.matriz.RegrasMatriz;
import br.gov.caixa.simtx.roteador.api.ws.xml.Header;
import br.gov.caixa.simtx.roteador.api.ws.xml.valida.ValidaRegrasBoletoDadosEntrada;
import br.gov.caixa.simtx.roteador.api.ws.xml.valida.ValidaRegrasBoletoDadosSaida;
import br.gov.caixa.simtx.roteador.api.ws.xml.valida.ValidaRegrasBoletoEntrada;
import br.gov.caixa.simtx.roteador.api.ws.xml.valida.ValidaRegrasBoletoSaida;
import br.gov.caixa.simtx.util.SimtxConfig;
import br.gov.caixa.simtx.util.exception.ServicoException;

public class ProcessadorValidaRegrasBoleto {
	
private static final Logger logger = Logger.getLogger(ProcessadorValidaRegrasBoleto.class);

	@Inject
	private FornecedorDados fornecedorDados;
	
	@Inject
	private LeitorMatrizValidaRegrasBoleto leitorMatriz;
	
	@Inject
	private SimtxConfig simtxConfig;
	
	public static final String PATH_XSD = "validacao_entradas/SIMTX_TYPES/Valida_Regras_Boleto/V1/Valida_Regras_Boleto.xsd";
	


	public String processar(String xmlEntrada) {
		logger.info(" ==== Processo Valida Regras Boleto (WS2) Iniciado ==== ");
		logger.info("Mensagem Recebida do SIBAR:\n" + xmlEntrada);
		
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddhhmmss");
		Header header = new Header();
		header.setVersao("1");
		header.setOperacao("VALIDA_REGRAS_BOLETO");
		header.setDataHora(format.format(new Date()));
		
		try {
			ValidaRegrasBoletoEntrada valida = transformarXmlParaJAXB(xmlEntrada);
			
			validarValorPagar(valida.getDados().getValorPagar());
			
			validarPagamentoContingencia(valida.getDados());
			
			validarDataVencimento(valida.getDados().getDataPagamento(), valida.getDados().getDataVencimentoUtil());
			
			Entry<String, String> retornoMatriz = recuperarValorMatriz(valida);
			
			validarSaidaMatriz(retornoMatriz.getKey(), valida.getDados());
			
			return montarResposta(retornoMatriz.getValue(), header);
		} 
		catch (ServicoException se) {
			return tratarExcecao(se, header);
		}
	}

	/**
	 * Transforma o Xml recebido em classes JAXB.
	 * 
	 * @param entrada
	 * @return
	 * @throws ServicoException
	 */
	public ValidaRegrasBoletoEntrada transformarXmlParaJAXB(String entrada) throws ServicoException {
		logger.info("Transformando xml recebido para classe JAXB");
		try {
			SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI); 
			Schema schema = sf.newSchema(new File(this.simtxConfig.getCaminhoXsd() + PATH_XSD));

			JAXBContext context = JAXBContext.newInstance(ValidaRegrasBoletoEntrada.class);
			Unmarshaller unmarshaller = context.createUnmarshaller();
			unmarshaller.setSchema(schema);
			
			return unmarshaller.unmarshal(new StreamSource(new ByteArrayInputStream(entrada.getBytes())),
					ValidaRegrasBoletoEntrada.class).getValue();
		} 
		catch (Exception e) {
			logger.error(e);
			Mtxtb006Mensagem mensagem = this.fornecedorDados.buscarMensagem(MensagemRetorno.WS_LAYOUT_INVALIDO);
			throw new ServicoException(mensagem, Constantes.ORIGEM_SIMTX);
		} 
	}
	
	/**
	 * Verifica se o valor a pagar e maior que zero.
	 * 
	 * @param valorPagar
	 * @throws ServicoException
	 */
	public void validarValorPagar(BigDecimal valorPagar) throws ServicoException {
		logger.info("Validando valor a pagar");
        if (valorPagar.compareTo(BigDecimal.ZERO) <= 0) {
        	logger.error("Valor a pagar menor/igual a zero");
			Mtxtb006Mensagem mensagem = this.fornecedorDados
					.buscarMensagem(MensagemRetorno.WS2_VALOR_PAGAR_ZERADO);
			throw new ServicoException(mensagem, Constantes.ORIGEM_SIMTX);
        } 
    }
	
	/**
	 * Verifica se esta em contingencia e se esta autorizado a pagar.
	 * 
	 * @param dados
	 * @throws ServicoException
	 */
	public void validarPagamentoContingencia(ValidaRegrasBoletoDadosEntrada dados) throws ServicoException {
		try {
			String tipoContingencia = dados.getTipoContingencia();
			logger.info("Flag Contingencia: "+tipoContingencia);
			
			if(!tipoContingencia.equals(ConstantesRoteadorWeb.SEM_CONTINGENCIA)) {
				logger.info("Validando Pagamento em Contingencia");
				
				int nuCanal = dados.getNuCanal();
				BigDecimal valorPagar = dados.getValorPagar();
				String codBarras = dados.getCodBarras();
				int tipoBoleto = verificarTipoBoleto(valorPagar, codBarras);
				
				Mtxtb040PrmtoPgtoContingenciaPK parametroPK = new Mtxtb040PrmtoPgtoContingenciaPK();
				parametroPK.setNuCanal004(nuCanal);
				parametroPK.setIcTipoBoleto(tipoBoleto);
				parametroPK.setIcOrigemContingencia(TipoContingenciaEnum.obterCodigoPorDescricao(tipoContingencia).getCodigo());
				Mtxtb040PrmtoPgtoContingencia parametro = this.fornecedorDados.buscarPrmtoPgtoCntngnciaPorCanal(parametroPK);
				
				if(parametro != null) {
					if (parametro.getIcAutorizacaoContingencia() == ConstantesRoteadorWeb.ACEITA_PAGAMENTO_CONTINGENCIA) {
						logger.info("Pode aceitar em situacao de Contingencia");
						verificarValorMinimoMaximo(valorPagar, parametro.getValorMinimo(), parametro.getValorMaximo());
					}
					else {
						logger.error("Nao deve aceitar pagamento em situacao de contingencia");
						Mtxtb006Mensagem mensagem = this.fornecedorDados
								.buscarMensagem(MensagemRetorno.WS2_NAO_ACEITA_PAGAMENTO_CONTINGENCIA);
						throw new ServicoException(mensagem, Constantes.ORIGEM_SIMTX);
					}
				}
				else {
					logger.error("Nenhum parametro de pagamento encontrado. Nao deve aceitar pagamento em situacao de contingencia");
					Mtxtb006Mensagem mensagem = this.fornecedorDados
							.buscarMensagem(MensagemRetorno.WS2_NAO_ACEITA_PAGAMENTO_CONTINGENCIA);
					throw new ServicoException(mensagem, Constantes.ORIGEM_SIMTX);
				}
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
	 * Verifica o tipo de boleto.
	 * 
	 * @param valorPagar
	 * @param codBarras
	 * @return
	 */
	public int verificarTipoBoleto(BigDecimal valorPagar, String codBarras) {
		logger.info("Verificando o tipo do boleto");
		if(valorPagar.compareTo(ConstantesRoteadorWeb.VALOR_BOLETED) >= 0) {
			logger.info("Boleted");
			return TipoBoletoEnum.BOLETED.getCodigo();
		}
		else {
			codBarras = codBarras.substring(0, 3);
			if(codBarras.equals("104")) {
				logger.info("Boleto Caixa");
				return TipoBoletoEnum.CAIXA.getCodigo();
			}
			else {
				logger.info("Boleto Outros Bancos");
				return TipoBoletoEnum.OUTROS_BANCOS.getCodigo();
			}
		}
	}
	
	/**
	 * Verifica o valor para pagamento em contingencia.
	 * 
	 * @param valorPagar
	 * @param valorMinimo
	 * @param valorMaximo
	 * @throws ServicoException
	 */
	public void verificarValorMinimoMaximo(BigDecimal valorPagar, BigDecimal valorMinimo, BigDecimal valorMaximo)
			throws ServicoException {
		logger.info("Verificando valor ["+valorPagar+"] para pagamento em contingencia");
		if(valorPagar.compareTo(valorMinimo) < 0
				|| valorPagar.compareTo(valorMaximo) > 0) {
			logger.error("Valor de pagamento em contingencia nao esta entre o minimo e maximo permitido");
			Mtxtb006Mensagem mensagem = this.fornecedorDados
					.buscarMensagem(MensagemRetorno.WS2_VALOR_PAGTO_CONTINGENCIA_NAO_ATENDE_PREMISSAS);
			throw new ServicoException(mensagem, Constantes.ORIGEM_SIMTX);
		}
		else {
			logger.info("Valor de pagamento em contingencia permitido");
		}
	}
	
	/**
	 * Valida a Data de Vencimento  do boleto.
	 * 
	 * @param dados
	 * @throws ServicoException
	 */
	public void validarDataVencimento(String dtPagamento, String dtVencimentoUtil) throws ServicoException {
		logger.info("Validando data de vencimento");
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Date dataAtual = sdf.parse(sdf.format(new Date()));
			
			Date dataPagamento = sdf.parse(dtPagamento);
	        Date dataVencimentoUtil= sdf.parse(dtVencimentoUtil);
			
			if (dataPagamento.after(dataAtual)) {
				if (dataVencimentoUtil.before(dataAtual)) {
					Mtxtb006Mensagem mensagem = this.fornecedorDados
							.buscarMensagem(MensagemRetorno.WS2_BOLETO_VENCIDO);
					throw new ServicoException(mensagem, Constantes.ORIGEM_SIMTX);
				} 
				else {
					if(dataPagamento.after(dataVencimentoUtil)) {
						Mtxtb006Mensagem mensagem = this.fornecedorDados
								.buscarMensagem(MensagemRetorno.WS2_DATA_POSTERIOR_VENCIMENTO);
						throw new ServicoException(mensagem, Constantes.ORIGEM_SIMTX);
					}
				}
			}
			else if (dataPagamento.before(dataAtual)) {
				Mtxtb006Mensagem mensagem = this.fornecedorDados
						.buscarMensagem(MensagemRetorno.WS2_DATA_PAGAMENTO_ANTERIOR_ATUAL);
				throw new ServicoException(mensagem, Constantes.ORIGEM_SIMTX);
			}
		} 
		catch (ServicoException e) {
			throw e;
		}
		catch (Exception e) {
			logger.error(e);
			Mtxtb006Mensagem mensagem = this.fornecedorDados.buscarMensagem(MensagemRetorno.LEIAUTE_MENSAGEM_INVALIDA);
			throw new ServicoException(mensagem, Constantes.ORIGEM_SIMTX);
		}
    }
	
	/**
	 * Recupera o valor da matriz.
	 * 
	 * @param dados
	 * @return
	 * @throws ServicoException
	 */
	public Entry<String, String> recuperarValorMatriz(ValidaRegrasBoletoEntrada dados) throws ServicoException {
		logger.info("Recuperando valor da matriz");
		try {
			RegrasMatriz regrasMatriz = new RegrasMatriz();
			String entradaMatriz = regrasMatriz.tratarParametrosEntradaWs2(dados.getDados());
	
	    	int comparadorValor = dados.getDados().getValorPagar().compareTo(dados.getDados().getValorTotalCalculado());
	    	String valorPagoMaiorValorCalculado = comparadorValor >= 0 ? "S" : "N"; 
	    	logger.info("Entrada para matriz: "+entradaMatriz + " - " + valorPagoMaiorValorCalculado);
	
	    	Entry<String, String> retornoRegrasBoletoMatriz = this.leitorMatriz.processaRegraBoletoEntrada(entradaMatriz, valorPagoMaiorValorCalculado);
	    	logger.info("Saida da matriz: " + retornoRegrasBoletoMatriz.getKey() + " - " +retornoRegrasBoletoMatriz.getValue());
	    	
	    	return retornoRegrasBoletoMatriz;
		} 
		catch (ServicoException e) {
			throw e;
		}
		catch (Exception e) {
			logger.error(e);
			Mtxtb006Mensagem mensagem = this.fornecedorDados.buscarMensagem(MensagemRetorno.LEIAUTE_MENSAGEM_INVALIDA);
			throw new ServicoException(mensagem, Constantes.ORIGEM_SIMTX);
		} 
	}
	
	/**
	 * Valida a saida da matriz.
	 * 
	 * @param saidaMatrizWs1
	 * @param dados
	 * @throws ServicoException
	 */
	public void validarSaidaMatriz(String saidaMatrizWs1, ValidaRegrasBoletoDadosEntrada dados)
			throws ServicoException {
		logger.info("Validando saida da matriz: "+saidaMatrizWs1);
		if (saidaMatrizWs1.equals(ConstantesRoteadorWeb.VALOR_CALCULADO)) {
			validarValorCalculado(dados.getValorTotalCalculado(), dados.getValorPagar());
		} 
		else if (saidaMatrizWs1.equals(ConstantesRoteadorWeb.MINIMO_E_MAXIMO)) {
			validarMinimoMaximo(dados.getValorPagar(), dados.getValorMinimoConsulta(), dados.getValorMaximoConsulta());
		}
		else if (saidaMatrizWs1.equals(ConstantesRoteadorWeb.SOMENTE_MINIMO)) {
			validarSomenteMinimo(dados.getValorPagar(), dados.getValorMinimoConsulta());
		}
		else if (saidaMatrizWs1.equals(ConstantesRoteadorWeb.NAO_RECEBER)) {
			Mtxtb006Mensagem mensagem = this.fornecedorDados.buscarMensagem(MensagemRetorno.WS1_NAO_RECEBER_BOLETO);
			throw new ServicoException(mensagem, Constantes.ORIGEM_SIMTX);
		}
		else if (saidaMatrizWs1.equals(ConstantesRoteadorWeb.VALOR_BARRA)) {
			validarValorNominal(dados.getValorPagar(), dados.getValorNominal(), dados);
		}
	}
	
	/**
	 * Valida o valor a pagar com o valor calculado.
	 * 
	 * @param valorTotalCalculado
	 * @param valorPagar
	 * @throws ServicoException
	 */
	public void validarValorCalculado(BigDecimal valorTotalCalculado, BigDecimal valorPagar) throws ServicoException {
		if(valorTotalCalculado.compareTo(valorPagar) != 0) {
			logger.error("Valor a pagar diferente do valor calculado");
			Mtxtb006Mensagem mensagem = this.fornecedorDados
					.buscarMensagem(MensagemRetorno.WS2_VALOR_PAGO_DIF_VALOR_CALCULADO);
			throw new ServicoException(mensagem, Constantes.ORIGEM_SIMTX);
		}
	}
	
	/**
	 * Valida o valor a pagar com o valor minimo e maximo.
	 * 
	 * @param valorPagar
	 * @param valorMinimo
	 * @param valorMaximo
	 * @throws ServicoException
	 */
	public void validarMinimoMaximo(BigDecimal valorPagar, BigDecimal valorMinimo, BigDecimal valorMaximo)
			throws ServicoException {
		if (valorPagar.compareTo(valorMinimo) < 0 || valorPagar.compareTo(valorMaximo) > 0) {
			logger.error("Valor a pagar nao esta entre o minimo e maximo");
			Mtxtb006Mensagem mensagem = this.fornecedorDados
					.buscarMensagem(MensagemRetorno.WS2_VALOR_NAO_ESTA_MINIMO_MAXIMO);
			throw new ServicoException(mensagem, Constantes.ORIGEM_SIMTX);
		}
	}
	
	/**
	 * Valida o valor a pagar com o valor minimo.
	 * 
	 * @param valorPagar
	 * @param valorMinimo
	 * @throws ServicoException
	 */
	public void validarSomenteMinimo(BigDecimal valorPagar, BigDecimal valorMinimo) throws ServicoException {
		if(valorPagar.compareTo(valorMinimo) < 0) {
			logger.error("Valor a pagar menor que o minimo");
			Mtxtb006Mensagem mensagem = this.fornecedorDados
					.buscarMensagem(MensagemRetorno.WS2_VALOR_PAGO_MENOR_MINIMO);
			throw new ServicoException(mensagem, Constantes.ORIGEM_SIMTX);
		}
	}
	
	/**
	 * Valida o valor a pagar com o valor nominal.
	 * 
	 * @param valorPagar
	 * @param valorNominal
	 * @throws ServicoException
	 */
	public void validarValorNominal(BigDecimal valorPagar, BigDecimal valorNominal,
			ValidaRegrasBoletoDadosEntrada dados) throws ServicoException {
		if ((dados.getTipoContingencia().equals(ConstantesRoteadorWeb.CONTINGENCIA_CIP)
				|| dados.getNovaPlataformaCobranca().equals(ConstantesRoteadorWeb.NAO))
				&& (valorNominal.compareTo(BigDecimal.valueOf(0.00)) > 0 && valorPagar.compareTo(valorNominal) != 0)) {
			logger.error("Valor a pagar diferente do valor nominal");
			Mtxtb006Mensagem mensagem = this.fornecedorDados
					.buscarMensagem(MensagemRetorno.WS2_VALOR_PAGAR_MAIOR_VALOR_BARRA);
			throw new ServicoException(mensagem, Constantes.ORIGEM_SIMTX);
		}
	}
	
	/**
	 * Monta a resposta para o SIBAR.
	 * 
	 * @param retornoMatriz
	 * @param e
	 * @return
	 */
	public String montarResposta(String retornoMatriz, Header header) throws ServicoException {
		logger.info("Montando o xml de resposta");
		try {
			ValidaRegrasBoletoSaida saida = new ValidaRegrasBoletoSaida();
			
			saida.setHeader(header);
			saida.setOrigemRetorno(ConstantesRoteadorWeb.SISTEMA_ORIGEM);
			
			ValidaRegrasBoletoDadosSaida dadosSaida = new ValidaRegrasBoletoDadosSaida();
			dadosSaida.setTipoBaixa(retornoMatriz);
			saida.setDados(dadosSaida);
			
			Mtxtb006Mensagem mensagem = this.fornecedorDados.buscarMensagem(MensagemRetorno.SUCESSO);
			saida.setCodRetorno(mensagem.getCodigoRetorno());
			saida.setMsgRetorno(mensagem.getDeMensagemNegocial());
			
			SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI); 
			Schema schema = sf.newSchema(new File(this.simtxConfig.getCaminhoXsd() + PATH_XSD));

			JAXBContext context = JAXBContext.newInstance(ValidaRegrasBoletoSaida.class);
			Marshaller marshaller = context.createMarshaller();
			marshaller.setSchema(schema);
			
			ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();
			StreamResult result = new StreamResult(byteOutputStream);
			marshaller.marshal(saida, result);
			
			String resposta = byteOutputStream.toString();
			
			logger.info("Enviando resposta para o SIBAR:\n"+resposta);
			logger.info(" ==== Processo Valida Regras Boleto (WS2) Finalizado ==== ");
			return resposta;
		}
		catch (Exception e) {
			logger.error(e);
			Mtxtb006Mensagem mensagem = new Mtxtb006Mensagem();
			mensagem.setCodigoRetorno(ConstantesRoteadorWeb.ERRO_CONSULTA);
			mensagem.setDeMensagemNegocial(ConstantesRoteadorWeb.MSG_GENERICA);
			throw new ServicoException(mensagem, Constantes.ORIGEM_SIMTX);
		}
	}
	
	/**
	 * Monta a mensagem de excecao para enviar ao SIBAR.
	 * 
	 * @param e
	 * @param header
	 * @return
	 */
	public String tratarExcecao(ServicoException e, Header header) {
		logger.info("Tratando excecao");
		try {
			ValidaRegrasBoletoSaida saida = new ValidaRegrasBoletoSaida();
			
			saida.setHeader(header);
			saida.setDados(new ValidaRegrasBoletoDadosSaida());
			saida.setCodRetorno(e.getMensagem().getCoMensagem());
			saida.setMsgRetorno(e.getMensagem().getDeMensagemTecnica());
			saida.setOrigemRetorno(e.getSistemaOrigem());
			
			JAXBContext context = JAXBContext.newInstance(ValidaRegrasBoletoSaida.class);
			Marshaller marshaller = context.createMarshaller();
			
			ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();
			StreamResult result = new StreamResult(byteOutputStream);
			marshaller.marshal(saida, result);
			
			String resposta = byteOutputStream.toString();
			logger.info("Enviando resposta para o SIBAR:\n"+resposta);
			logger.info(" ==== Processo Valida Regras Boleto (WS2) Finalizado ==== ");
			return resposta;
		} 
		catch (Exception e2) {
			logger.error(e2);
			return ConstantesRoteadorWeb.MSG_GENERICA;
		}
	}
	
	
	public void setFornecedorDados(FornecedorDados fornecedorDados) {
		this.fornecedorDados = fornecedorDados;
	}

	public void setLeitorMatriz(LeitorMatrizValidaRegrasBoleto leitorMatriz) {
		this.leitorMatriz = leitorMatriz;
	}

	public void setSimtxConfig(SimtxConfig simtxConfig) {
		this.simtxConfig = simtxConfig;
	}
	
}
