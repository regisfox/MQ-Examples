package br.gov.caixa.simtx.roteador.api.ws.xml.valida;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import br.gov.caixa.simtx.roteador.api.ws.xml.Header;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SERVICO_ENTRADA_TYPE", propOrder = {"header", "dados"})
public class ValidaRegrasBoletoEntrada {
	
	@XmlElement(name = "HEADER", namespace = "http://caixa.gov.br/sibar", required = true)
    protected Header header;
	
	@XmlElement(name = "DADOS", required = true)
    protected ValidaRegrasBoletoDadosEntrada dados;

	
	
	public Header getHeader() {
		return header;
	}

	public void setHeader(Header header) {
		this.header = header;
	}

	public ValidaRegrasBoletoDadosEntrada getDados() {
		return dados;
	}

	public void setDados(ValidaRegrasBoletoDadosEntrada dados) {
		this.dados = dados;
	}

}
