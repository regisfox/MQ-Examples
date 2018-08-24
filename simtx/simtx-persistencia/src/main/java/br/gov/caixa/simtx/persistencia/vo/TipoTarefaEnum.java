/*******************************************************************************
 * Copyright (C)  2017 - CAIXA Econômica Federal 
 * 
 * Todos os direitos reservados
 ******************************************************************************/
package br.gov.caixa.simtx.persistencia.vo;

/**
 * The Enum TipoTarefaEnum.
 */
public enum TipoTarefaEnum {

	// Pode ser:1=Transacional | 2=Negocial | 3=Meio de Entrada | 4=Seguranca

	TRANSACIONAL(1), NEGOCIAL(2), MEIO_DE_ENTRADA(3), SEGURANCA(4);

	final Integer codigo;

	/**
	 * Instantiates a new tipo tarefa enum.
	 *
	 * @param codigo
	 *            the codigo
	 */
	private TipoTarefaEnum(Integer codigo) {
		this.codigo = codigo;
	}

	/**
	 * Gets the codigo.
	 *
	 * @return the codigo
	 */
	public Integer getCodigo() {
		return this.codigo;
	}
}
