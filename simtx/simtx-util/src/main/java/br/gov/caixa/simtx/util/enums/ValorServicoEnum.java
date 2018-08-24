package br.gov.caixa.simtx.util.enums;

public enum ValorServicoEnum {
	
	VALIDA_BOLETO_NPC(110031, 1, "/*[1]/VALIDA_BOLETO/VALOR_PAGAR"),
	VALIDA_BOLETO(110038, 1, "/*[1]/VALIDA_BOLETO/VALOR_PAGAR");
	
	private long codServico;
	
	private int versaoServico;
	
	private String path;
	
	private ValorServicoEnum(long codServico, int versaoServico, String path) {
		this.codServico = codServico;
		this.versaoServico = versaoServico;
		this.path = path;
	}

	public static ValorServicoEnum obterServico(long codServico, int versaoServico) {
		ValorServicoEnum retorno = null;
		for(ValorServicoEnum servico : values()) {
			if (servico.getCodServico() == codServico
					&& servico.getVersaoServico() == versaoServico) {
				retorno = servico;
				break;
			}
		}
		return retorno;
	}

	public long getCodServico() {
		return codServico;
	}

	public int getVersaoServico() {
		return versaoServico;
	}

	public String getPath() {
		return path;
	}
	
}
