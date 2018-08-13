package br.gov.caixa.simtx.dominio;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author cvoginski
 *
 */
@XmlRootElement(name = "mensagem")
@XmlAccessorType (XmlAccessType.FIELD)
public class Mensagem {
	private long nsu;
	private String transacao;
	private String datahora_referencia;
	private String datahora_transacao;
	private String tipo_documento;
	private String documento;
	private String ddd;
	private String telefone;
	private String cpf;
	private String cnpj;
	private String valor;
	private List<String> parametros;

	public String getDatahora_referencia() {
		return datahora_referencia;
	}

	public Mensagem setDatahora_referencia(String datahora_referencia) {
		this.datahora_referencia = datahora_referencia;
		return this;
	}

	public String getDatahora_transacao() {
		return datahora_transacao;
	}

	public Mensagem setDatahora_transacao(String datahora_transacao) {
		this.datahora_transacao = datahora_transacao;
		return this;
	}

	public String getDocumento() {
		return documento;
	}

	public Mensagem setDocumento(String documento) {
		this.documento = documento;
		return this;
	}

	public String getCpf() {
		return cpf;
	}

	public Mensagem setCpf(String cpf) {
		this.cpf = cpf;
		return this;
	}

	public String getValor() {
		return valor;
	}

	public Mensagem setValor(String valor) {
		this.valor = valor;
		return this;
	}

	public long getNsu() {
		return nsu;
	}

	public Mensagem setNsu(long nsu) {
		this.nsu = nsu;
		return this;
	}

	public String getTransacao() {
		return transacao;
	}

	public Mensagem setTransacao(String transacao) {
		this.transacao = transacao;
		return this;
	}

	public String getTipo_documento() {
		return tipo_documento;
	}

	public Mensagem setTipo_documento(String tipo_documento) {
		this.tipo_documento = tipo_documento;
		return this;
	}

	public List<String> getParametros() {
		return parametros;
	}

	public void setParametros(List<String> parametros) {
		this.parametros = parametros;
	}

	public String getDdd() {
		return ddd;
	}

	public Mensagem setDdd(String ddd) {
		this.ddd = ddd;
		return this;
	}

	public String getTelefone() {
		return telefone;
	}

	public Mensagem setTelefone(String telefone) {
		this.telefone = telefone;
		return this;
	}

	public String getCnpj() {
		return cnpj;
	}

	public Mensagem setCnpj(String cnpj) {
		this.cnpj = cnpj;
		return this;
	}
	
	
}
