package br.gov.caixa.simtx.persistencia.vo;

/**
 * Enum criado para definir os tipos de autorizacao de pagamento.
 *  
 * @author joseoliveirajunior
 */
public enum TipoAutorizacaoPagamentoEnum {
	
	DESABILITADO(0, "Não"),
	HABILITADO(1, "Sim");
	
	private int codigo;
	
	private String descricao;
	
	private TipoAutorizacaoPagamentoEnum(int codigo, String descricao) {
		this.codigo = codigo;
		this.descricao = descricao;
	}

	public int getCodigo() {
		return codigo;
	}

	public String getDescricao() {
		return descricao;
	}
	
	public static TipoAutorizacaoPagamentoEnum obterTipoPorCodigo(Integer codigo) {
		if(codigo != null) {
			for(final TipoAutorizacaoPagamentoEnum servico : values()) {
				if (codigo.equals(servico.getCodigo())) {
					return servico;
				}
			}
		}
		
		return null;
	}
}
