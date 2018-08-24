/*******************************************************************************
 * Copyright (C)  2017 - CAIXA Econômica Federal 
 * 
 * Todos os direitos reservados
 ******************************************************************************/
package br.gov.caixa.simtx.util;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamSource;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;


/**
 * Classe que faz a transformacao de um xml baseado num xslt
 * 
 * @author rgrosz@brq.com
 * @since 11/12/2013
 *
 */
public class XmlProcessador {
    private Source xml;
    private Result output;
    private Exception exception;
    private Transformer transformer;
    private static Logger logger = Logger.getLogger(XmlProcessador.class);

    public void transforme() {
        try {
        	transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            transformer.transform(xml, output);
        }
        catch (TransformerException trnException) {
            throw new RuntimeException(trnException);
        }
    }

    public void prepararTransformacao(String xmlStringSource, StringGravavel output) {
        ByteArrayInputStream stream = new ByteArrayInputStream(xmlStringSource.getBytes());
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder;
        try {
            dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(stream);
            doc.getDocumentElement().normalize();
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            setOutput(output.result);
            this.xml = new DOMSource(doc);
            this.transformer = transformerFactory.newTransformer();
        }
        catch (Exception e) {
            logger.warn(e);
        }
    }

    public Transformer getTranformer() {
        return this.transformer;
    }

    public void setXml(StringGravavel busData) {
        InputStream streamInput = new ByteArrayInputStream(busData.getBytes());
        StreamSource input = new StreamSource(streamInput);
        setXml(input);
    }

    public void setXml(String xmlData) {
        ByteArrayInputStream input;
        try {
        	input = new ByteArrayInputStream(xmlData.getBytes("UTF-8"));
		} 
        catch (Exception e) {
			input = new ByteArrayInputStream(xmlData.getBytes());
		}
        setXml(new StreamSource(input));
    }

    public void setXml(Source xml) {
        this.xml = xml;
    }

    public void setXslt(File arquivo) {
        StreamSource xslt = new StreamSource(arquivo);
        setXslt(xslt);
    }

    public void setXslt(Source xslt) {
        try {
            TransformerFactory tFactory = TransformerFactory.newInstance();
            transformer = tFactory.newTransformer(xslt);
        }
        catch (Exception e) {
            this.exception = e;
        }
    }

    public Exception getException() {
        return this.exception;
    }

    public void setOutput(Result output) {
        this.output = output;
    }

    public void setOutput(StringGravavel output) {
        setOutput(output.getResult());
    }
}
