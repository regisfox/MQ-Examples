package br.gov.caixa.simtx.dominio;

import java.util.ArrayList;
import java.util.List;

import com.thoughtworks.xstream.XStream;

public class TransacaoGenerica implements Convertable {

	private Cabecalho cabecalho;
	private String tipo;
	private Integer qnt_transacoes;
	private Integer sequencial_arquivo;
	private List<Mensagem> detalhes = new ArrayList<Mensagem>();

	public Cabecalho getCabecalho() {
		return cabecalho;
	}

	public void setCabecalho(Cabecalho cabecalho) {
		this.cabecalho = cabecalho;
	}

	public List<Mensagem> getDetalhes() {
		return detalhes;
	}

	public void setDetalhes(List<Mensagem> detalhes) {
		this.detalhes = detalhes;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public int getQnt_transacoes() {
		return qnt_transacoes;
	}

	public void setQnt_transacoes(int qnt_transacoes) {
		this.qnt_transacoes = qnt_transacoes;
	}

	public int getSequencial_arquivo() {
		return sequencial_arquivo;
	}

	public void setSequencial_arquivo(int sequencial_arquivo) {
		this.sequencial_arquivo = sequencial_arquivo;
	}

	public String converter() {
		XStream xs = new XStream();
		xs.alias("transacao_generica", TransacaoGenerica.class);
		xs.alias("mensagem", Mensagem.class);
		xs.alias("parametro", String.class);
		
		xs.useAttributeFor(TransacaoGenerica.class, "qnt_transacoes");
		xs.useAttributeFor(TransacaoGenerica.class, "sequencial_arquivo");
		xs.useAttributeFor(TransacaoGenerica.class, "tipo");
		
		return xs.toXML(this).replaceAll("__", "_");
	}

}
