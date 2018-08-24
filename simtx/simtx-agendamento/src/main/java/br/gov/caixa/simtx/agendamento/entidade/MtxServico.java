package br.gov.caixa.simtx.agendamento.entidade;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class MtxServico {

	private long nuServico;

	private String noServico;
	
	private int nuVersaoServico;

	public MtxServico(long nuServico) {
		super();
		this.nuServico = nuServico;
	}

	public MtxServico() {
		super();
	}

	public long getNuServico() {
		return nuServico;
	}

	@XmlElement
	public void setNuServico(long nuServico) {
		this.nuServico = nuServico;
	}

	public String getNoServico() {
		return noServico;
	}

	@XmlElement
	public void setNoServico(String noServico) {
		this.noServico = noServico;
	}

	public int getNuVersaoServico() {
		return nuVersaoServico;
	}

	@XmlElement
	public void setNuVersaoServico(int nuVersaoServico) {
		this.nuVersaoServico = nuVersaoServico;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("{\"nuServico\":");
		builder.append(nuServico);
		builder.append(",\"");
		if (noServico != null) {
			builder.append("noServico\":");
			builder.append(noServico);
			builder.append(",\"");
		}
		builder.append("nuVersaoServico\":");
		builder.append(nuVersaoServico);
		builder.append("}");
		return builder.toString();
	}
}
