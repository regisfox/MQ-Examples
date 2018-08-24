package br.gov.caixa.simtx.util.enums;

import br.gov.caixa.simtx.util.ConstantesAgendamento;

public enum CodigosRetornosEnum {
	
	CODIGO_01_CONSULTA_BOLETO("01", ConstantesAgendamento.CODIGO_TAREFA_CONSULTA_BOLETO, 1, ConstantesAgendamento.SITUACAO_IMPEDITIVA),
	CODIGO_02_CONSULTA_BOLETO("02", ConstantesAgendamento.CODIGO_TAREFA_CONSULTA_BOLETO, 1, ConstantesAgendamento.SITUACAO_IMPEDITIVA),
	CODIGO_03_CONSULTA_BOLETO("03", ConstantesAgendamento.CODIGO_TAREFA_CONSULTA_BOLETO, 1, ConstantesAgendamento.SITUACAO_IMPEDITIVA),
	CODIGO_06_CONSULTA_BOLETO("06", ConstantesAgendamento.CODIGO_TAREFA_CONSULTA_BOLETO, 1, ConstantesAgendamento.SITUACAO_IMPEDITIVA),
	CODIGO_07_CONSULTA_BOLETO("07", ConstantesAgendamento.CODIGO_TAREFA_CONSULTA_BOLETO, 1, ConstantesAgendamento.SITUACAO_IMPEDITIVA),
	CODIGO_08_CONSULTA_BOLETO("08", ConstantesAgendamento.CODIGO_TAREFA_CONSULTA_BOLETO, 1, ConstantesAgendamento.SITUACAO_IMPEDITIVA),
	CODIGO_09_CONSULTA_BOLETO("09", ConstantesAgendamento.CODIGO_TAREFA_CONSULTA_BOLETO, 1, ConstantesAgendamento.SITUACAO_IMPEDITIVA),
	CODIGO_005_CONSULTA_BOLETO("005", ConstantesAgendamento.CODIGO_TAREFA_CONSULTA_BOLETO, 1, ConstantesAgendamento.SITUACAO_IMPEDITIVA),
	CODIGO_NC_CONSULTA_BOLETO("NC", ConstantesAgendamento.CODIGO_TAREFA_CONSULTA_BOLETO, 1, ConstantesAgendamento.SITUACAO_IMPEDITIVA),
	CODIGO_05_WS2("05", ConstantesAgendamento.CODIGO_TAREFA_VALIDA_REGRAS_BOLETO, 1, ConstantesAgendamento.SITUACAO_IMPEDITIVA),
	CODIGO_06_WS2("06", ConstantesAgendamento.CODIGO_TAREFA_VALIDA_REGRAS_BOLETO, 1, ConstantesAgendamento.SITUACAO_IMPEDITIVA),
	CODIGO_07_WS2("07", ConstantesAgendamento.CODIGO_TAREFA_VALIDA_REGRAS_BOLETO, 1, ConstantesAgendamento.SITUACAO_IMPEDITIVA),
	CODIGO_08_WS2("08", ConstantesAgendamento.CODIGO_TAREFA_VALIDA_REGRAS_BOLETO, 1, ConstantesAgendamento.SITUACAO_IMPEDITIVA),
	CODIGO_09_WS2("09", ConstantesAgendamento.CODIGO_TAREFA_VALIDA_REGRAS_BOLETO, 1, ConstantesAgendamento.SITUACAO_IMPEDITIVA),
	CODIGO_10_WS2("10", ConstantesAgendamento.CODIGO_TAREFA_VALIDA_REGRAS_BOLETO, 1, ConstantesAgendamento.SITUACAO_IMPEDITIVA),
	CODIGO_11_WS2("11", ConstantesAgendamento.CODIGO_TAREFA_VALIDA_REGRAS_BOLETO, 1, ConstantesAgendamento.SITUACAO_IMPEDITIVA),
	CODIGO_12_WS2("12", ConstantesAgendamento.CODIGO_TAREFA_VALIDA_REGRAS_BOLETO, 1, ConstantesAgendamento.SITUACAO_IMPEDITIVA),
	CODIGO_13_WS2("13", ConstantesAgendamento.CODIGO_TAREFA_VALIDA_REGRAS_BOLETO, 1, ConstantesAgendamento.SITUACAO_IMPEDITIVA);

	private String codigo;
	
	private long codigoTarefa;
	
	private int tipoCodigo;
	
	private String descCodigo;
	
	private CodigosRetornosEnum(String codigo, long codigoTarefa, int tipoCodigo, String descCodigo) {
		this.codigo = codigo;
		this.codigoTarefa = codigoTarefa;
		this.tipoCodigo = tipoCodigo;
		this.descCodigo = descCodigo;
	}

	public static CodigosRetornosEnum obterCodigo(String codigoRetorno, long codigoTarefa) {
		CodigosRetornosEnum retorno = null;
		if (!codigoRetorno.isEmpty()) {
			for (CodigosRetornosEnum codigoEnum : values()) {
				if (codigoEnum.getCodigo().equals(codigoRetorno) && codigoEnum.getCodigoTarefa() == codigoTarefa) {
					retorno = codigoEnum;
					break;
				}
			}
		}
		return (retorno != null) ? retorno : null;
	}
	
	
	public String getCodigo() {
		return codigo;
	}
	
	public long getCodigoTarefa() {
		return codigoTarefa;
	}

	public int getTipoCodigo() {
		return tipoCodigo;
	}

	public String getDescCodigo() {
		return descCodigo;
	}

}
