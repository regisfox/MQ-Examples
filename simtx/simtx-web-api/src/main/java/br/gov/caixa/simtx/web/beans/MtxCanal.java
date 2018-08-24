package br.gov.caixa.simtx.web.beans;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class MtxCanal {

	private long nsuCanal;

	private long nuCanal;

	private String noCanal;

	public long getNuCanal() {
		return nuCanal;
	}

	public MtxCanal() {
		super();
	}

	public MtxCanal(long nuCanal) {
		super();
		this.nuCanal = nuCanal;
	}

	@XmlElement
	public void setNuCanal(long nuCanal) {
		this.nuCanal = nuCanal;
	}

	public String getNoCanal() {
		return noCanal;
	}

	@XmlElement
	public void setNoCanal(String noCanal) {
		this.noCanal = noCanal;
	}

	public long getNsuCanal() {
		return nsuCanal;
	}

	@XmlElement
	public void setNsuCanal(long nsuCanal) {
		this.nsuCanal = nsuCanal;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("{nsuCanal:\"");
		builder.append(nsuCanal);
		builder.append(",\"");
		builder.append("\"nuCanal:\"");
		builder.append(nuCanal);
		builder.append(",\"");
		if (noCanal != null) {
			builder.append("noCanal:\"");
			builder.append(noCanal);
		}
		builder.append("}");
		return builder.toString();
	}
}
