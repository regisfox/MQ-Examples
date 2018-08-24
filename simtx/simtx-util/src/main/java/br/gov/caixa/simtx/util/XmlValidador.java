/*******************************************************************************
 * Copyright (C)  2017 - CAIXA Econômica Federal 
 * 
 * Todos os direitos reservados
 ******************************************************************************/
package br.gov.caixa.simtx.util;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.apache.log4j.Logger;
import org.xml.sax.SAXException;

/**
 * Classe que valida um xml baseado num arquivo xsd
 * 
 * @author rgrosz@brq.com
 * @since 11/12/2013
 *
 */
public class XmlValidador {

    /** The exception. */
    private Exception exception;

    /** The xsd. */
    private File xsd;
    
    private static final Logger logger = Logger.getLogger(XmlValidador.class);

    
    
    /**
     * Sets the xsd.
     *
     * @param xsd
     *            the new xsd
     */
    public void setXsd(final File xsd) {
        this.xsd = xsd;
    }

    /**
     * Validate.
     *
     * @param xmlData
     *            the xml data
     * @return true, if successful
     */
    public void validate(String xmlData) throws Exception {
        ByteArrayInputStream input = null;
		try {
			input = new ByteArrayInputStream(xmlData.getBytes("UTF-8"));
		} 
		catch (UnsupportedEncodingException e) {
			logger.error("Erro ao ler como UTF-8", e);
			throw e;
		}
        validate(new StreamSource(input));
    }

    /**
     * Validate.
     *
     * @param xml the xml
     * @return true, if successful
     * @throws SAXException 
     * @throws IOException 
     */
    public void validate(Source xml) throws SAXException, IOException {
        try {
            this.exception = null;
            SchemaFactory factory;
            factory = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");
            Schema schema = factory.newSchema(this.xsd);
            Validator validator = schema.newValidator();
            validator.validate(xml);
        }
        catch (SAXException | IOException e) {
        	logger.error("Error Validador: "+e);
            throw e;
        }
        
    }

    /**
     * Gets the exception.
     *
     * @return the exception
     */
    public Exception getException() {
        return this.exception;
    }
    
    
    
    /**
     * Valida a estrutura/dados da mensagem recebida com o xsd do servido solicitado.
     * 
     * @param mensagem
     * @return
     * @throws Exception
     */
    public void validarXmlcomXsd(String mensagem, File fileXsd) throws Exception {
        this.xsd = fileXsd;
        validate(mensagem);
    }
}
