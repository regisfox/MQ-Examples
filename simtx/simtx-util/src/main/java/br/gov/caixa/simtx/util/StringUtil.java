/*******************************************************************************
 * Copyright (C)  2017 - CAIXA Econômica Federal 
 * 
 * Todos os direitos reservados
 ******************************************************************************/
package br.gov.caixa.simtx.util;

import java.text.Normalizer;

/**
 * The Class StringUtil.
 */
public class StringUtil {
	
	/**
	 * Remove acentuacao da string.
	 * @param stringFonte
	 * @return
	 */
	public static String limpaString(String stringFonte) {
		String passa = stringFonte;

		passa = Normalizer.normalize(passa, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");
		passa = passa.replaceAll("[\\000]*", "");
		passa = passa.replaceAll("[Ãƒâ€šÃƒâ‚¬ÃƒÂ�Ãƒâ€žÃƒÆ’]", "A");
		passa = passa.replaceAll("[ÃƒÂ¢ÃƒÂ£ÃƒÂ ÃƒÂ¡ÃƒÂ¤]", "a");
		passa = passa.replaceAll("[ÃƒÅ ÃƒË†Ãƒâ€°Ãƒâ€¹]", "E");
		passa = passa.replaceAll("[ÃƒÂªÃƒÂ¨ÃƒÂ©ÃƒÂ«]", "e");
		passa = passa.replaceAll("ÃƒÅ½ÃƒÂ�ÃƒÅ’ÃƒÂ�", "I");
		passa = passa.replaceAll("ÃƒÂ®ÃƒÂ­ÃƒÂ¬ÃƒÂ¯", "i");
		passa = passa.replaceAll("[Ãƒâ€�Ãƒâ€¢Ãƒâ€™Ãƒâ€œÃƒâ€“]", "O");
		passa = passa.replaceAll("[ÃƒÂ´ÃƒÂµÃƒÂ²ÃƒÂ³ÃƒÂ¶]", "o");
		passa = passa.replaceAll("[Ãƒâ€ºÃƒâ„¢ÃƒÅ¡ÃƒÅ“]", "U");
		passa = passa.replaceAll("[ÃƒÂ»ÃƒÂºÃƒÂ¹ÃƒÂ¼]", "u");
		passa = passa.replaceAll("Ãƒâ€¡", "C");
		passa = passa.replaceAll("ÃƒÂ§", "c");
		passa = passa.replaceAll("[ÃƒÂ½ÃƒÂ¿]", "y");
		passa = passa.replaceAll("ÃƒÂ�", "Y");
		passa = passa.replaceAll("ÃƒÂ±", "n");
		passa = passa.replaceAll("Ãƒâ€˜", "N");
		return passa;
	}
	
	/**
	 * Remove os namespace das tags que o xslt gera indevidamente ao enviar para
	 * os corporativos. Evita menor processamento de dados nas mensagens.
	 * 
	 * @param mensagem
	 * @return {@link String}
	 */
	public static String limpaNameSpace(String mensagem) {
		String primeira = mensagem.substring(0, mensagem.indexOf("\">")+2);
		mensagem = mensagem.substring(mensagem.indexOf("\">")+2);
		mensagem = mensagem.replaceAll(" xmlns:.*?\">", ">");
		mensagem = primeira.concat(mensagem);
		return mensagem;
	}
	
	/**
	 * Verifica se o valor da String eh null ou vazio.
	 *
	 * @param valor the valor
	 * @return true, if is empty
	 */
	public static boolean isEmpty(String valor){
    	boolean isEmpty = Boolean.FALSE;
    	if(valor == null || valor.trim().length() == 0){
    		isEmpty = Boolean.TRUE;
    	}
    	return isEmpty;
    }
}
