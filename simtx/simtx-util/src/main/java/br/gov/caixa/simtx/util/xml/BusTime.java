/*******************************************************************************
 * Copyright (C)  2017 - CAIXA Econômica Federal 
 * 
 * Todos os direitos reservados
 ******************************************************************************/
package br.gov.caixa.simtx.util.xml;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * 
 * Classe responsavel por adicionar valores de tempo ao busdata para poder ser recuperado pelos XSLT
 * como tempo atual do PMAC.
 * 
 * @author rgrosz@brq.com
 *
 */
class BusTime implements Serializable{
    
    /**
     * Insira aqui a descrição do atributo.
     */
    private static final long serialVersionUID = 1L;
    /** The sb. */
    private StringBuilder sb;


    /**
     * Instantiates a new bus time.
     *
     * @param sb the sb
     */
    BusTime(StringBuilder sb) {
        this.setStringBuilder(sb);
    }

    /**
     * Instantiates a new bus time.
     */
    public BusTime() {
    }


    /**
     * Append bus time.
     */
    void appendBusTime() {
        Calendar c = new GregorianCalendar();
        c.setTime(new Date());
        int dia = c.get(Calendar.DAY_OF_MONTH);
        int mes = c.get(Calendar.MONTH) + 1;
        int ano = c.get(Calendar.YEAR);
        int hora = c.get(Calendar.HOUR_OF_DAY);
        int min = c.get(Calendar.MINUTE);
        int seg = c.get(Calendar.SECOND);
        int mili = c.get(Calendar.MILLISECOND);

        sb.append("<BUSTIME>");
        putValue("ANO", ano);
        putValue("MES", mes);
        putValue("DIA", dia);
        putValue("HORA", hora);
        putValue("MIN", min);
        putValue("SEG", seg);
        putValue("MILI", mili);
        sb.append("</BUSTIME>");

    }
    
    public String getBusTime() {
    	StringBuilder retorno = new StringBuilder();
    	
    	Calendar c = new GregorianCalendar();
        c.setTime(new Date());
        int dia = c.get(Calendar.DAY_OF_MONTH);
        int mes = c.get(Calendar.MONTH) + 1;
        int ano = c.get(Calendar.YEAR);
        int hora = c.get(Calendar.HOUR_OF_DAY);
        int min = c.get(Calendar.MINUTE);
        int seg = c.get(Calendar.SECOND);
        int mili = c.get(Calendar.MILLISECOND);

        retorno.append("<BUSTIME>");
        retorno.append(getValue("ANO", ano));
        retorno.append(getValue("MES", mes));
        retorno.append(getValue("DIA", dia));
        retorno.append(getValue("HORA", hora));
        retorno.append(getValue("MIN", min));
        retorno.append(getValue("SEG", seg));
        retorno.append(getValue("MILI", mili));
        retorno.append("</BUSTIME>");
        
        return retorno.toString();
    }

	public String toStringXml() {
		StringBuilder sb2 = new StringBuilder();
		
		Calendar c = new GregorianCalendar();
		c.setTime(new Date());
		int dia = c.get(Calendar.DAY_OF_MONTH);
		int mes = c.get(Calendar.MONTH) + 1;
		int ano = c.get(Calendar.YEAR);
		int hora = c.get(Calendar.HOUR_OF_DAY);
		int min = c.get(Calendar.MINUTE);
		int seg = c.get(Calendar.SECOND);
		int mili = c.get(Calendar.MILLISECOND);

		sb2.append("<BUSTIME>");
		putValue("ANO", ano, sb2);
		putValue("MES", mes, sb2);
		putValue("DIA", dia, sb2);
		putValue("HORA", hora, sb2);
		putValue("MIN", min, sb2);
		putValue("SEG", seg, sb2);
		putValue("MILI", mili, sb2);
		sb2.append("</BUSTIME>");
		
		return sb2.toString();
	}

    /**
     * Put value.
     *
     * @param tag the tag
     * @param value the value
     */
	private void putValue(String tag, int value) {
        sb.append("<").append(tag).append(">");
        sb.append(fmt(value));
        sb.append("</").append(tag).append(">");
    }

	private String getValue(String tag, int value) {
		StringBuilder retorno = new StringBuilder();
		retorno.append("<").append(tag).append(">");
		retorno.append(fmt(value));
		retorno.append("</").append(tag).append(">");
		
		return retorno.toString();
    }
	
	private void putValue(String tag, int value, StringBuilder sb2) {
        sb2.append("<").append(tag).append(">");
        sb2.append(fmt(value));
        sb2.append("</").append(tag).append(">");
    }

    /**
     * Fmt.
     *
     * @param value the value
     * @return the string
     */
    private String fmt(int value) {
        String retorno;
        if (value < 10) {
            retorno = "0" + value;
        }
        else {
            retorno = String.valueOf(value);
        }
        return retorno;
    }

    /**
     * Gets the string builder.
     *
     * @return the string builder
     */
    StringBuilder getStringBuilder() {
        return this.sb;
    }


    /**
     * Sets the string builder.
     *
     * @param sb the new string builder
     */
    void setStringBuilder(StringBuilder sb) {
        if (sb == null)
            throw new NullPointerException("String builder nao pode ser nulo");
        this.sb = sb;
    }
}
