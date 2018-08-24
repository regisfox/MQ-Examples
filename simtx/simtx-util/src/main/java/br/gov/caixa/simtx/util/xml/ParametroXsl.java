/*******************************************************************************
 * Copyright (C)  2017 - CAIXA Econômica Federal 
 * 
 * Todos os direitos reservados
 ******************************************************************************/
package br.gov.caixa.simtx.util.xml;

public class ParametroXsl {

	public static final String XSL_PARAM_COD_RETORNO = "codRetorno";
	public static final String XSL_PARAM_ACAO_RETORNO = "acaoRetorno";
	public static final String XSL_PARAM_ORIGEM_RETORNO = "origemRetorno";
	public static final String XSL_PARAM_MENSAGEM_NEGOCIAL = "mensagemNegocial";
	public static final String XSL_PARAM_MENSAGEM_TECNICA = "mensagemTecnica";
	public static final String XSL_PARAM_NSU_SIMTX = "nsuSimtx";
	public static final String XSL_PARAM_REDE_TRANSMISSORA = "pRedeTransmissora";
	public static final String XSL_PARAM_SEGMENTO = "pSegmento";
	public static final String XSL_PARAM_FUNCIONALIDADE = "funcionalidade";
	public static final String XSL_PARAM_ERRO = "erro";
	
	private String chave;
	private String valor;

	public ParametroXsl(String chave, String valor) {
		super();
		this.chave = chave;
		this.valor = valor;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((chave == null) ? 0 : chave.hashCode());
		result = prime * result + ((valor == null) ? 0 : valor.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ParametroXsl other = (ParametroXsl) obj;
		if (chave == null) {
			if (other.chave != null)
				return false;
		} else if (!chave.equals(other.chave))
			return false;
		if (valor == null) {
			if (other.valor != null)
				return false;
		} else if (!valor.equals(other.valor))
			return false;
		return true;
	}

	public String getChave() {
		return chave;
	}

	public void setChave(String chave) {
		this.chave = chave;
	}

	public String getValor() {
		return valor;
	}

	public void setValor(String valor) {
		this.valor = valor;
	}

}
