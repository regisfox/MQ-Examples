package br.gov.caixa.simtx.roteador.api.ws.matriz;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;

import br.gov.caixa.simtx.roteador.api.util.ConstantesRoteadorWeb;
import br.gov.caixa.simtx.roteador.api.ws.xml.consulta.ConsultaRegrasBoletoDadosEntrada;
import br.gov.caixa.simtx.roteador.api.ws.xml.valida.ValidaRegrasBoletoDadosEntrada;

/**
 * Classe responsavel por gerenciar todas as regras dos pagamentos recebidos via webservice
 * 
 * @author vnRibeiro
 *
 */
public class RegrasMatriz {
	
	private static final Logger logger = Logger.getLogger(RegrasMatriz.class);
	
	private static final String RESULTADO = "Resultado: ";
	
	public String tratarParametrosEntradaWs1(ConsultaRegrasBoletoDadosEntrada dados) throws ParseException {
		MatrizVO matrizVO = new MatrizVO();
		matrizVO.setFlagNovaPlataformaCobranca(dados.getNovaPlataformaCobranca());
		matrizVO.setFlagContingencia(dados.getTipoContingencia());
		matrizVO.setEspecie(dados.getEspecie());
		matrizVO.setRecebeValorDivergente(dados.getRecebeValorDivergente());
		matrizVO.setDataVencimento(dados.getDataVencimento());
		matrizVO.setDataConsulta(dados.getDataConsulta());
		matrizVO.setFlagPagamentoParcial(dados.getPagamentoParcial());
		matrizVO.setNumeroParcelaAtual(dados.getNuParcelaAtual());
		matrizVO.setQtdePagamentoParcial(dados.getQtdePagtoParcialPermitido());
		matrizVO.setUltimaParcelaViavel(dados.getUltimaParcelaViavel());
		return tratarParametrosEntradaMatriz(matrizVO);
	}
	
	public String tratarParametrosEntradaWs2(ValidaRegrasBoletoDadosEntrada dados) throws ParseException {
		MatrizVO matrizVO = new MatrizVO();
		matrizVO.setFlagNovaPlataformaCobranca(dados.getNovaPlataformaCobranca());
		matrizVO.setFlagContingencia(dados.getTipoContingencia());
		matrizVO.setEspecie(dados.getEspecie());
		matrizVO.setRecebeValorDivergente(dados.getRecebeValorDivergente());
		matrizVO.setDataVencimento(dados.getDataVencimentoUtil());
		matrizVO.setDataConsulta(dados.getDataPagamento());
		matrizVO.setFlagPagamentoParcial(dados.getPagamentoParcial());
		matrizVO.setNumeroParcelaAtual(dados.getNuParcelaAtual());
		matrizVO.setQtdePagamentoParcial(dados.getQtdePagtoParcialPermitido());
		matrizVO.setUltimaParcelaViavel(dados.getUltimaParcelaViavel());
		return tratarParametrosEntradaMatriz(matrizVO);
	}
	
	public String tratarParametrosEntradaMatriz(MatrizVO dados) throws ParseException {
		logger.info("Tratando parametros de entrada da matriz");
		StringBuilder regras = new StringBuilder();
		regras.append(dados.getFlagNovaPlataformaCobranca() + ";");
		regras.append(validaContingencia(dados.getFlagContingencia()) + ";");
		regras.append(validaFlagEspecie(dados.getEspecie()) + ";");
		regras.append(dados.getRecebeValorDivergente() + ";");
		regras.append(validaDataVencimento(dados.getDataConsulta(), dados.getDataVencimento()) + ";");
		regras.append(dados.getFlagPagamentoParcial() + ";");
		regras.append(validaUltimaParcela(dados.getNumeroParcelaAtual(), dados.getQtdePagamentoParcial()) + ";");
		regras.append(dados.getUltimaParcelaViavel() + ";");
		return regras.toString();
	}

	/**
	 * Verificacao entre a data da consulta e do vencimento. Informando um
	 * valor fixo para comparacao no arquivo csv.
	 * 
	 * @param dataConsulta
	 * @param dataVencimento
	 * 
	 * @return Constante 
	 **/
	public String validaDataVencimento(String dataConsulta, String dataVencimento) throws ParseException {
		logger.info("Parametro data vencimento");
		String verificaData=""; 
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date dataConsultaDt = sdf.parse(dataConsulta);
		Date dataVencimentoUtilDt = sdf.parse(dataVencimento);

		if (dataConsultaDt.before(dataVencimentoUtilDt)) {
			verificaData = ConstantesRoteadorWeb.DATA_VENCER;

		} else if (dataConsultaDt.after(dataVencimentoUtilDt)) {
			verificaData = ConstantesRoteadorWeb.DATA_VENCIDO;

		} else if (dataConsultaDt.equals(dataVencimentoUtilDt)) {
			verificaData = ConstantesRoteadorWeb.DATA_VENCER;
		}
		logger.info(RESULTADO+ verificaData);
		return verificaData;
	}

	/**
	 * Calcula a ultima parcela mediante ao numero de parcelas e quantidade do pagamento registrado. 
	 * Informando um valor fixo para comparacao no arquivo csv.
	 * 
	 * @param numeroParcela
	 * @param quantidadePagamentoRegistrado
	 * 
	 * @return Constante 
	 **/
	public String validaUltimaParcela(int numeroParcela, int quantidadePagamentoRegistrado) {
		logger.info("Parametro Ultima Parcela");
		String ultimaParcela = "";
		if (numeroParcela > 1 && quantidadePagamentoRegistrado == numeroParcela) {
			ultimaParcela = ConstantesRoteadorWeb.SIM;
		} else {
			ultimaParcela = ConstantesRoteadorWeb.NAO;
		}
		logger.info(RESULTADO+ultimaParcela);
		return ultimaParcela;
	}

	/**
	 * Verifica a flag informada no campo especie recebido no xml de entrada.
	 * Todos que sao diferentes de CARTAO_DE_CREDITO e BOLETO_PROPOSTA devem 
	 * ser tratados como OUTROS.
	 * 
	 * @param flagEspecie
	 * @return flagEspecie 
	 **/
	public String validaFlagEspecie(String flagEspecie) {
		logger.info("Parametro Especie");
		String especie = "";
		if (flagEspecie.equals(ConstantesRoteadorWeb.CARTAO_DE_CREDITO)
				|| flagEspecie.equals(ConstantesRoteadorWeb.CARTAO_CREDITO)) {
			especie = "CARTAO_CREDITO";
		}
		else if (flagEspecie.equals(ConstantesRoteadorWeb.BOLETO_PROPOSTA_1)
				|| flagEspecie.equals(ConstantesRoteadorWeb.BOLETO_PROPOSTA)) {
			especie = "BOLETO_PROPOSTA";
		}
		else {
			especie = "OUTROS";
		}
		logger.info(RESULTADO+especie);
		return especie;
	}
	
	/**
	 * Retorna S ou N de acordo com a contigencia
	 * 
	 * @param contingencia
	 * @return
	 */
	public String validaContingencia(String contingencia) {
		logger.info("Parametro Contingencia");
		String resultado = "";
		if (contingencia.equals(ConstantesRoteadorWeb.CONTINGENCIA_CAIXA)
				|| contingencia.equals(ConstantesRoteadorWeb.SEM_CONTINGENCIA)) {
			resultado = ConstantesRoteadorWeb.NAO;
		}
		else {
			resultado = ConstantesRoteadorWeb.SIM;
		}
		logger.info(RESULTADO+resultado);
		return resultado;
	}
	
}
