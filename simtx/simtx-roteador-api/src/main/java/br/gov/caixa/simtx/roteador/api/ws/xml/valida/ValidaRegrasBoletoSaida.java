package br.gov.caixa.simtx.roteador.api.ws.xml.valida;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import br.gov.caixa.simtx.roteador.api.ws.xml.Header;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SERVICO_SAIDA_TYPE", propOrder = { "header", "codRetorno", "origemRetorno", "msgRetorno", "dados" })
@XmlRootElement(name = "SERVICO_SAIDA", namespace = "http://caixa.gov.br/simtx/valida_boleto/regras/v1/ns")
public class ValidaRegrasBoletoSaida {

	@XmlElement(name = "HEADER", namespace = "http://caixa.gov.br/sibar", required = true)
	protected Header header;

	@XmlElement(name = "COD_RETORNO", required = true)
	protected String codRetorno;

	@XmlElement(name = "ORIGEM_RETORNO", required = true)
	protected String origemRetorno;

	@XmlElement(name = "MSG_RETORNO", required = true)
	protected String msgRetorno;

	@XmlElement(name = "DADOS", required = true)
	protected ValidaRegrasBoletoDadosSaida dados;

	
	
	
	public Header getHeader() {
		return header;
	}

	public void setHeader(Header header) {
		this.header = header;
	}

	public String getCodRetorno() {
		return codRetorno;
	}

	public void setCodRetorno(String codRetorno) {
		this.codRetorno = codRetorno;
	}

	public String getOrigemRetorno() {
		return origemRetorno;
	}

	public void setOrigemRetorno(String origemRetorno) {
		this.origemRetorno = origemRetorno;
	}

	public String getMsgRetorno() {
		return msgRetorno;
	}

	public void setMsgRetorno(String msgRetorno) {
		this.msgRetorno = msgRetorno;
	}

	public ValidaRegrasBoletoDadosSaida getDados() {
		return dados;
	}

	public void setDados(ValidaRegrasBoletoDadosSaida dados) {
		this.dados = dados;
	}

}
