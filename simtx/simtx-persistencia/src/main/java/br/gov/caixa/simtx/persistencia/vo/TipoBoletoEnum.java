package br.gov.caixa.simtx.persistencia.vo;

public enum TipoBoletoEnum {
	
	CAIXA(1, "Boleto Caixa"),
	OUTROS_BANCOS(2, "Boleto Outros Bancos"),
	BOLETED(3, "Boleto Boleted");
	
	
	private int codigo;
	
	private String descricao;
	
	private TipoBoletoEnum(int codigo, String descricao) {
		this.codigo = codigo;
		this.descricao = descricao;
	}

	public int getCodigo() {
		return codigo;
	}

	public String getDescricao() {
		return descricao;
	}
	
	public static TipoBoletoEnum obterTipoPorCodigo(Integer codigo) {
		if(codigo != null) {
			for(final TipoBoletoEnum servico : values()) {
				if (codigo.equals(servico.getCodigo())) {
					return servico;
				}
			}
		}
		
		return null;
	}
}
