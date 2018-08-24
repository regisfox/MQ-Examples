/*******************************************************************************
 * Copyright (C)  2017 - CAIXA Econômica Federal 
 * 
 * Todos os direitos reservados
 ******************************************************************************/
package br.gov.caixa.simtx.util.exception;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * The Class ExceptionUtil.
 */
public class ExceptionUtil {

	/**
     * Gets the error.
     *
     * @param e the e
     * @return the error
     */
    public static StringWriter getError(Exception e) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        return sw;
    }

    /**
     * Gets the stack trace.
     *
     * @param throwable the throwable
     * @return the stack trace
     */
    public static String getStackTrace(Throwable throwable) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw, true);
        throwable.printStackTrace(pw);
        return sw.getBuffer().toString();
    }

}
