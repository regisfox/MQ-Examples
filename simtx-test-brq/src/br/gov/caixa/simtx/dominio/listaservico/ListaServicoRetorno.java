package br.gov.caixa.simtx.dominio.listaservico;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "lista_servico")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "lista_servico", propOrder = { "cabecalho", "detalhe" })
public class ListaServicoRetorno {
	@XmlElement(required = true)
	CabecalhoListaServico cabecalho;
	DetalhesListaServico detalhe;
	public CabecalhoListaServico getCabecalho() {
		return cabecalho;
	}
	public void setCabecalho(CabecalhoListaServico cabecalho) {
		this.cabecalho = cabecalho;
	}
	public DetalhesListaServico getDetalhe() {
		return detalhe;
	}
	public void setDetalhe(DetalhesListaServico detalhe) {
		this.detalhe = detalhe;
	}
	
	
}
