package br.gov.caixa.simtx.test.simtx;

import org.apache.log4j.Logger;
import org.junit.Test;

import br.gov.caixa.simtx.utils.BaseUtil;
import br.gov.caixa.simtx.xml.TransformadorXsl;

public class TestXMLTransformerXSL extends BaseUtil {

	final static Logger logger = Logger.getLogger(TestXMLTransformerXSL.class);

	@Test
	public void test() {
		try {
			String xmlPath = getPathFile(true, diretorio.listFiles(), "PagamentoBoletoDEV.xml");
			String xslPath = getPathFile(true, diretorio.listFiles(), "detalhe.xsl");
			
//			String xmlPath = getPathFile(true, diretorio.listFiles(), "Detalhe_Agendamento_Boleto.xml");
//			String xslPath = getPathFile(true, diretorio.listFiles(), "pagamentoBoletoNPC_Req.xsl");
//			processarBusdata(xmlPath, xslPath);
			processarRoot(xmlPath, xslPath);
		} catch (Exception e) {
			logger.error(e);
		}

	}

	private void processarBusdata(String xmlPath, String xslPath) throws Exception {

		String xml = TransformadorXsl.recuperarArquivo(xmlPath);

		StringBuffer xmlEstruturaBusData = new StringBuffer();

		xmlEstruturaBusData.append(TransformadorXsl.NAME_MIGRADO_BUSDATA_INI_XML);
		xmlEstruturaBusData.append(xml);
		xmlEstruturaBusData.append(TransformadorXsl.NAME_MIGRADO_BUSDATA_FIM_XML);

		logger.info("--------- XML ENTRADA CANAL ----------\n");
		logger.info(xmlEstruturaBusData);
		logger.info("--------- XML ENTRADA CANAL ----------\n");

		TransformadorXsl.xslTransformer(xmlEstruturaBusData.toString(), xslPath);

	}
	
	private void processarRoot(String xmlPath, String xslPath) throws Exception {

		String xml = TransformadorXsl.recuperarArquivo(xmlPath);

		StringBuffer xmlEstruturaRoot = new StringBuffer();

		xmlEstruturaRoot.append(TransformadorXsl.NAME_MIGRADO_ROOT_INI_XML);
		xmlEstruturaRoot.append(xml);
		xmlEstruturaRoot.append(TransformadorXsl.NAME_MIGRADO_ROOT_FIM_XML);

		logger.info("--------- XML ENTRADA CANAL ----------\n");
		logger.info(xmlEstruturaRoot);
		logger.info("--------- XML ENTRADA CANAL ----------\n");

		TransformadorXsl.xslTransformer(xmlEstruturaRoot.toString(), xslPath);

	}
}
