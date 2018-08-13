package br.gov.caixa.simtx.dominio.listaservico;

import br.gov.caixa.simtx.dominio.Convertable;

import com.thoughtworks.xstream.XStream;


public class ListaServico implements Convertable {
	private CabecalhoListaServico cabecalho;

	public CabecalhoListaServico getCabecalho() {
		return cabecalho;
	}

	public ListaServico setCabecalho(CabecalhoListaServico cabecalho) {
		this.cabecalho = cabecalho;
		return this;
	}

	public String converter() {
		XStream xs = new XStream();
		xs.alias("lista_servico", ListaServico.class);
		
		return xs.toXML(this).replaceAll("__", "_");
	}

}
