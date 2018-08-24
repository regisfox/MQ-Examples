/*******************************************************************************
 * Copyright (C)  2017 - CAIXA Econômica Federal 
 * 
 * Todos os direitos reservados
 ******************************************************************************/
package br.gov.caixa.simtx.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.transform.Result;
import javax.xml.transform.stream.StreamResult;

import org.apache.log4j.Logger;


/**
 * Classe e permite usar String como Stream e tambem como {@link Result} ao mesmo tempo</br>
 * 
 * @author rgrosz@brq.com
 * @since 11/dez/2013
 *
 */
public class StringGravavel {

    Result result;
    ByteArrayOutputStream byteOutputStream;
    private static final Logger logger = Logger.getLogger(StringGravavel.class);

    public StringGravavel() {
        byteOutputStream = new ByteArrayOutputStream();
        result = new StreamResult(byteOutputStream);
    }


    public void load(InputStream ios) {
        try {
            int bite;
            while ((bite = ios.read()) >= 0) {
                byteOutputStream.write(bite);
            }
        }
        catch (IOException e) {
            logger.error(e);
            throw new RuntimeException(e);
        }
    }


    public Result getResult() {
        return result;
    }

    public ByteArrayOutputStream getStream() {
        return byteOutputStream;
    }

    public byte[] getBytes() {
        return byteOutputStream.toByteArray();
    }

    public String toString() {
    	try {
    		return byteOutputStream.toString("UTF-8");
		} 
    	catch (Exception e) {
    		return byteOutputStream.toString();
		}
    }
}
