/*******************************************************************************
 * Copyright (C)  2017 - CAIXA Econômica Federal 
 * 
 * Todos os direitos reservados
 ******************************************************************************/
package br.gov.caixa.simtx.util.data;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import org.apache.log4j.Logger;


public class DataUtil {
	
	private static final Logger logger = Logger.getLogger(DataUtil.class);

	public static final String FORMATO_DATA_XML = "yyyy-MM-dd";
	public static final String FORMATO_DATA_PADRAO_BR = "dd/MM/yyyy";
	public static final String FORMATO_DATA_YYYY_MM_DD_HIFEN_JSON = "yyyy-MM-dd'T'HH:mm:ss.SSSX";
	public static final String FORMATO_DATA_YYYY_MM_DD_HH_MM = "yyyy-MM-dd HH:mm";
	public static final String FORMATO_DATA_YYYY_MM_DD_CANAL = "yyyy-MM-dd'T'HH:mm:ss.SSSX";
	public static final String FORMATO_DATA_YYYY_MM_DD_HH_MM_SS = "yyyyMMddHHmmss";
	public static final String FORMATO_DATA_YYYY_MM_DD = "yyyyMMdd";
	
	static int test = 34;
	
	public static void main(String args[]) {
		System.out.println(DataUtil.formatar(DataUtil.obterDataFutura(1),DataUtil.FORMATO_DATA_YYYY_MM_DD_HH_MM));
	}
	

	private DataUtil() throws IllegalAccessException {
		throw new IllegalAccessException("Class DataUtil");
	}
	
	public static Calendar getHojeCalendar() {
		Calendar hoje = Calendar.getInstance(TimeZone.getDefault());
		hoje.set(Calendar.HOUR_OF_DAY, 23);
		hoje.set(Calendar.MINUTE, 59);
		hoje.set(Calendar.SECOND, 59);
		return hoje;
	}


	public static SimpleDateFormat simpleDateFormat(String format) {
		return new SimpleDateFormat(format);
	}

	/**
	 * Retorna a data atual.
	 * 
	 * @return {@link Timestamp} Data atual.
	 */
	public static Timestamp getDataAtual() {
		return new Timestamp(System.currentTimeMillis());
	}

	/**
	 * Retorna a data anterior à data de referência informada.
	 * 
	 * @param dataReferencia
	 *            {@link Date} Data de referência.
	 * @return {@link Timestamp} Data anterior.
	 */
	public static Timestamp getDataAnterior(Date dataReferencia) {

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(dataReferencia);
		calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) - 1);
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		calendar.set(Calendar.MILLISECOND, 999);

		return new Timestamp(calendar.getTimeInMillis());
	}

	/**
	 * Retorna a data posterior à data de referência informada.
	 * 
	 * @param dataReferencia
	 *            {@link Date} Data de referência.
	 * @return {@link Timestamp} Data posterior.
	 */
	public static Timestamp getDataPosterior(Date dataReferencia) {

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(dataReferencia);
		calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) + 1);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 000);

		return new Timestamp(calendar.getTimeInMillis());
	}

	/**
	 * Retorna a da formatada no padrão brasileiro.
	 * 
	 * @param data
	 *            {@link Date} Data.
	 * @return {@link String} Data formatada.
	 */
	public static String getDataFormatadaPadraoBR(Date data) {

		return formatar(data, FORMATO_DATA_PADRAO_BR);
	}

	/**
	 * Formata a data para o formato informado.
	 * 
	 * @param data
	 *            {@link Date} Data.
	 * @param formato
	 *            {@link String} Formato.
	 * @return {@link String} Data formatada.
	 */
	public static String formatar(Date data, String formato) {

		DateFormat df = new SimpleDateFormat(formato);

		return df.format(data);
	}

	public static String obterDataFormatada(Date data, String pattern) {

		String dataFormatada = null;

		if (data != null) {
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
			dataFormatada = simpleDateFormat.format(data);
		}

		return dataFormatada;
	}

	public static Date obterDataFutura(int qtdDias) {
		Calendar dataFutura = Calendar.getInstance();
		return obterDataFutura(dataFutura, qtdDias);
	}
	
	public static Date obterDataFutura(Calendar dataBase, int qtdDias) {
		dataBase.add(Calendar.DAY_OF_MONTH, qtdDias);
		return dataBase.getTime();
	}

	public static Date formatStringData(String dateXml, String pattern) throws ParseException {
		Date result = null;
		if (null != dateXml) {
			DateFormat df = new SimpleDateFormat(pattern);
			result = df.parse(dateXml);
		}
		return result;
	}
	
	public static String obterFormatacaoDataSTRSIBAR(String dataOrigem) {
		String retornoData = "";
		try {
			SimpleDateFormat dataOrigemformat = new SimpleDateFormat(FORMATO_DATA_PADRAO_BR);
			SimpleDateFormat dataSibarfomat = new SimpleDateFormat(FORMATO_DATA_XML);

			Date dataformatada = dataOrigemformat.parse(dataOrigem);
			retornoData = dataSibarfomat.format(dataformatada);
		} catch (ParseException e) {
			
			logger.error("Erro ao formatar a data de entrada - Msg: "+ e.getMessage());
		}
		return retornoData;
	}
	
	public static boolean dataEntradaEhDepoisDeDataHoje(Date dataEntrada) {
		boolean retorno = false;
		Calendar data = Calendar.getInstance(TimeZone.getDefault());
		data.setTime(dataEntrada);
		
		retorno = data.after(getHojeCalendar());
		return retorno;
	}

}
