package br.gov.caixa.simtx.persistencia.vo;

public enum CanalEnum {
	
	SICAQ(105, "SICAQ"),
	SIMAA(106, "SIMAA"),
	SIMAW(108, "SIMAW"),
	SIIBC(110, "SIIBC"),
	SISAG(111, "SISAG"),
	SISPL(112, "SISPL"),
	SIMTC(113, "SIMTC"),
	SIMTX(114, "SIMTX");
	
	
	private int codigo;
	
	private String sigla;
	
	private CanalEnum(int codigo, String sigla) {
		this.codigo = codigo;
		this.sigla = sigla;
	}
	
	public static CanalEnum obterPorCodigo(int codigo) {
		CanalEnum retorno = null;
		for (CanalEnum codigoEnum : values()) {
			if (codigoEnum.getCodigo() == codigo) {
				retorno = codigoEnum;
				break;
			}
		}
		return (retorno != null) ? retorno : null;
	}
	
	public static CanalEnum obterPorSigla(String sigla) {
		CanalEnum retorno = null;
		for (CanalEnum codigoEnum : values()) {
			if (codigoEnum.getSigla().equals(sigla)) {
				retorno = codigoEnum;
				break;
			}
		}
		return (retorno != null) ? retorno : null;
	}
	

	public int getCodigo() {
		return codigo;
	}

	public String getSigla() {
		return sigla;
	}


}
