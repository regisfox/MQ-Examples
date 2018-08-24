package br.gov.caixa.simtx.roteador.api.ws.xml.consulta;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import br.gov.caixa.simtx.roteador.api.ws.xml.Header;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SERVICO_ENTRADA_TYPE", propOrder = {"header", "dados"})
public class ConsultaRegrasBoletoEntrada {
	
	@XmlElement(name = "HEADER", namespace = "http://caixa.gov.br/sibar", required = true)
    protected Header header;
	
	@XmlElement(name = "DADOS", required = true)
    protected ConsultaRegrasBoletoDadosEntrada dados;

	
	
	public Header getHeader() {
		return header;
	}

	public void setHeader(Header header) {
		this.header = header;
	}

	public ConsultaRegrasBoletoDadosEntrada getDados() {
		return dados;
	}

	public void setDados(ConsultaRegrasBoletoDadosEntrada dados) {
		this.dados = dados;
	}

}
