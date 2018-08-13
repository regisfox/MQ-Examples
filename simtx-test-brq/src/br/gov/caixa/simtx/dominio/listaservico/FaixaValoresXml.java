package br.gov.caixa.simtx.dominio.listaservico;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlType;

@XmlType(name = "faixa_valores", propOrder = {"valor"})
public class FaixaValoresXml {
	List<String> valor = new ArrayList<String>();

	public List<String> getValor() {
		return valor;
	}

	public void setValor(List<String> valor) {
		this.valor = valor;
	}
	
}
