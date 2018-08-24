package br.gov.caixa.simtx.comprovante.enuns;

import java.util.ArrayList;
import java.util.List;

public enum ServicosComprovanteEnum {
	
	LISTA_COMPROVANTE(110026l, "LISTA"),
	
	DETALHE_COMPROVANTE_BOLETO(110027l, "DETALHE");
	
	private long codigoServico;
	
	private String tipoServico;
	
	
	private ServicosComprovanteEnum(long codigoServico, String tipoServico) {
		this.codigoServico = codigoServico;
		this.tipoServico = tipoServico;
	}
	
	public static List<Long> listCodigosServicos() {
		List<Long> list = new ArrayList<>();
		for (ServicosComprovanteEnum codigoEnum : values()) {
			list.add(codigoEnum.getCodigoServico());
		}
		return list;
	}
	
	public static ServicosComprovanteEnum obterServico(long codigoServico) {
		ServicosComprovanteEnum retorno = null;
		if(codigoServico > 0) {
			for(ServicosComprovanteEnum codigoEnum : values()) {
				if (codigoEnum.getCodigoServico() == codigoServico) {
					retorno = codigoEnum;
					break;
				}
			}
		}
		return (retorno != null) ? retorno : null;
	}

	public long getCodigoServico() {
		return codigoServico;
	}

	public String getTipoServico() {
		return tipoServico;
	}

}
