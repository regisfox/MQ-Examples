/*******************************************************************************
 * Copyright (C)  2017 - CAIXA Econômica Federal 
 * 
 * Todos os direitos reservados
 ******************************************************************************/
package br.gov.caixa.simtx.persistencia.assinaturamultipla.vo;

public enum SituacaoAssinaturaMultipla {
	
	PENDENTE_ASSINATURA("TP", "PENDENTE"), 
	ASSINADA("TA", "ASSINADA"), 
	EFETIVADA("TE", ""), 
	CANCELADA("TC", ""), 
	NAO_EFETUADA("NE", "NAO_ASSINADA");
	
	private String rotulo;
	
	private String descricaoXsd;
	
	private SituacaoAssinaturaMultipla(String rotulo, String descricaoXsd) {
		this.rotulo = rotulo;
		this.descricaoXsd = descricaoXsd;
	}

	public String getRotulo() {
		return rotulo;
	}

	public String getDescricaoXsd() {
		return descricaoXsd;
	}
	
}
