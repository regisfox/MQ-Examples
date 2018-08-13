/*******************************************************************************
 * Copyright (C)  2017 - CAIXA Econômica Federal 
 * 
 * Todos os direitos reservados
 ******************************************************************************/
package br.gov.caixa.simtx.xml;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.log4j.Logger;

public class TransformadorXsl {

	final static Logger logger = Logger.getLogger(TransformadorXsl.class);

	private TransformerFactory transformadorFactory;

	public static final String NAME_MIGRADO_BUSDATA_INI_XML = "<BUSDATA><BUSTIME></BUSTIME>\r\n";
	public static final String NAME_MIGRADO_BUSDATA_FIM_XML = "</BUSDATA> \r\n";
	public static final String NAME_MIGRADO_ROOT_INI_XML = "<root>\r\n";
	public static final String NAME_MIGRADO_ROOT_FIM_XML = "</root> \r\n";
	
	private static BufferedReader br;

	public TransformadorXsl() {
		this.transformadorFactory = TransformerFactory.newInstance();
	}

	public String transformar(String xml, String xsl) throws TransformerException {
		StringWriter saidaEscritor = new StringWriter();
		Result saida = new StreamResult(saidaEscritor);

		Transformer transformador = transformadorFactory.newTransformer(new StreamSource(new StringReader(xsl)));
		transformador.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
		transformador.setOutputProperty(OutputKeys.ENCODING, "UTF-8");

		transformador.transform(new StreamSource(new StringReader(xml)), saida);

		StringBuffer sb = saidaEscritor.getBuffer();
		return sb.toString();
	}

	public static String recuperarArquivo(String caminho) throws IOException {
		StringBuffer arquivoConteudo = new StringBuffer();
		br = new BufferedReader(new FileReader(caminho));
		while (br.ready()) {
			arquivoConteudo.append(br.readLine());
		}
		return arquivoConteudo.toString();
	}

//	public static void callXmlTransformerXls(String xmlPathFile, String xlsPath) throws Exception {
//		String xslPathFile = dirPathXslt + xlsPath;
//		callXslTransformer(xmlPathFile, xslPathFile);
//	}

	public static void callXslTransformer(String xmlPath, String xlsPath) throws IOException, TransformerException {
		StringBuffer xmlEstruturaBusData = new StringBuffer();
		xmlEstruturaBusData.append(NAME_MIGRADO_BUSDATA_INI_XML);
		String xml = TransformadorXsl.recuperarArquivo(xmlPath);
		xmlEstruturaBusData.append(xml);
		xmlEstruturaBusData.append(NAME_MIGRADO_BUSDATA_FIM_XML);
		xslTransformer(xmlEstruturaBusData.toString(), xlsPath);
	}

	public static void xslTransformer(String xmlContent, String xlsPath) throws IOException, TransformerException {
		logger.info("---------xslTransformer----------\n");
		TransformadorXsl transformadorXsl = new TransformadorXsl();
		String xsl = TransformadorXsl.recuperarArquivo(xlsPath);
		logger.info(transformadorXsl.transformar(xmlContent, xsl.trim()) + "\n");
		logger.info("---------xslTransformer----------\n");
	}
}
