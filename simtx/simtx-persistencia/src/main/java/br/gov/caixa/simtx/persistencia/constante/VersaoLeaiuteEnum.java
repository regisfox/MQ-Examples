/*******************************************************************************
 * Copyright (C)  2017 - CAIXA Econômica Federal 
 * 
 * Todos os direitos reservados
 ******************************************************************************/
package br.gov.caixa.simtx.persistencia.constante;

/**
 * Classe responsavel por obter a versao do layout do Barramento de acordo com a
 * versao do Simtx.
 * 
 * @author rctoscano
 *
 */
public enum VersaoLeaiuteEnum {
	
	/** Versao 1.0 */
	VERSAO1(1, "1.0"),
	VERSAO1UNICO(1, "1"),
	VERSAO0100(1, "01.00"),
	VERSAO2(2, "1.1"),
	VERSAO_2_0(2, "2.0"),
	VERSAO3(3, "1.2"),
	VERSAO4(4, "1.3"),
	VERSAO5(5, "1.4"),
	VERSAO6(6, "1.5"),
	VERSAO7(7, "1.6");
	
	
	
	/** Versao Servico Simtx */
	private final Integer versaoSimtx;
	
	/** Versao Barramento */
	private final String versaoBarramento;

	
	/**
	 * Construtor.
	 * 
	 * @param versaoSimtx
	 * @param versaoBarramento
	 */
	private VersaoLeaiuteEnum(Integer versaoSimtx, String versaoBarramento) {
	    this.versaoSimtx = versaoSimtx;
		this.versaoBarramento = versaoBarramento;
	}
	
	/**
	 * Obtem a versao.
	 * 
	 * @param versaoServico
	 * @return
	 */
	public static VersaoLeaiuteEnum obterEnum(String versaoBarramento){
		VersaoLeaiuteEnum tabelaEnumSelecionado = null;  
		for(VersaoLeaiuteEnum tabelaEnum : VersaoLeaiuteEnum.values()){
			if(tabelaEnum.getVersaoBarramento().equals(versaoBarramento)){
				tabelaEnumSelecionado = tabelaEnum;
				break;
			}
		}
		return tabelaEnumSelecionado;
	}
	
	
	public Integer getVersaoSimtx() {
		return versaoSimtx;
	}

	public String getVersaoBarramento() {
		return versaoBarramento;
	}
}
