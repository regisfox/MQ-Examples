package br.gov.caixa.simtx.persistencia.vo;

public enum TipoContingenciaEnum {
	
	CIP(1, "CONTINGENCIA_CIP"),
	CAIXA(2, "CONTINGENCIA_CAIXA");
	
	private int codigo;
	
	private String descricao;
	
	private TipoContingenciaEnum(int codigo, String descricao) {
		this.codigo = codigo;
		this.descricao = descricao;
	}
	
	public static TipoContingenciaEnum obterCodigoPorDescricao(String descricao) {
		for(TipoContingenciaEnum servico : values()) {
			if (servico.getDescricao().equals(descricao)) {
				return servico;
			}
		}
		return null;
	}
	
	public static TipoContingenciaEnum obterTipoPorCodigo(Integer codigo) {
		if(codigo != null) {
			for(final TipoContingenciaEnum servico : values()) {
				if (codigo.equals(servico.getCodigo())) {
					return servico;
				}
			}
		}
		
		return null;
	}

	public int getCodigo() {
		return codigo;
	}

	public String getDescricao() {
		return descricao;
	}

}
