package br.gov.caixa.simtx.persistencia.constante;

/**
 * @author cvoginski
 *
 */
public enum AcaoRetorno {
	// Pode ser:1=Impeditiva | 2=Informativa | 3=Autorizadora
	IMPEDITIVA(1, "IMPEDITIVA"), INFORMATIVA(2, "INFORMATIVA"), AUTORIZADORA(3, "AUTORIZADORA");

	private Integer tipo;
	private String rotulo;

	private AcaoRetorno(Integer tipo, String rotulo) {
		this.tipo = tipo;
		this.rotulo = rotulo;
	}
	
	public static AcaoRetorno recuperarAcao(Integer tipo) {
		switch (tipo) {
			case 1:
				return AcaoRetorno.IMPEDITIVA;
			case 2:
				return AcaoRetorno.INFORMATIVA;
			case 3:
				return AcaoRetorno.AUTORIZADORA;
			default:
				throw new IllegalArgumentException("Tipo de acao nao existe");
		}
		
	}

	public Integer getTipo() {
		return tipo;
	}

	public String getRotulo() {
		return rotulo;
	}

}
