package br.gov.caixa.simtx.dominio.adesao;

import java.util.ArrayList;
import java.util.List;

import br.gov.caixa.simtx.dominio.Convertable;

import com.thoughtworks.xstream.XStream;

public class AdesaoGenerica  implements Convertable {
	private static final String XML_CABECALHO = "<?xml version='1.0' encoding='UTF-8'?>";
	private Cabecalho cabecalho;
	private List<Adesao> detalhes;

	public AdesaoGenerica() {
		super();
		this.cabecalho = new Cabecalho();
		this.detalhes = new ArrayList<Adesao>();
	}
	
	public Cabecalho getCabecalho() {
		return cabecalho;
	}
	public AdesaoGenerica setCabecalho(Cabecalho cabecalho) {
		this.cabecalho = cabecalho;
		return this;
	}
	public List<Adesao> getDetalhes() {
		return detalhes;
	}
	public AdesaoGenerica setDetalhes(List<Adesao> detalhes) {
		this.detalhes = detalhes;
		return this;
	}

	public String converter() {
		XStream xs = new XStream();
		xs.alias("adesao_generica", AdesaoGenerica.class);
		xs.alias("adesao", Adesao.class);
		xs.alias("telefone", Telefone.class);
		
		String xmlConvertido = xs.toXML(this).replaceAll("__", "_");
		xmlConvertido = XML_CABECALHO + xmlConvertido;
		return xmlConvertido;
	}
}
