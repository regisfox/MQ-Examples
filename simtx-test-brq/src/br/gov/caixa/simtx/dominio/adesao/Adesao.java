package br.gov.caixa.simtx.dominio.adesao;

import java.util.ArrayList;
import java.util.List;

public class Adesao {
	private String transacao;
	private String servico;
	private String tipo_documento;
	private String documento;
	private String hora_inicio;
	private String hora_fim;
	private List<Telefone> telefones;
	private String valor_minimo;

	public Adesao() {
		this.telefones = new ArrayList<Telefone>();
	}
	
	public String getTransacao() {
		return transacao;
	}

	public Adesao setTransacao(String transacao) {
		this.transacao = transacao;
		return this;
	}

	public String getServico() {
		return servico;
	}

	public Adesao setServico(String servico) {
		this.servico = servico;
		return this;
	}

	public String getTipo_documento() {
		return tipo_documento;
	}

	public Adesao setTipo_documento(String tipo_documento) {
		this.tipo_documento = tipo_documento;
		return this;
	}

	public String getDocumento() {
		return documento;
	}

	public Adesao setDocumento(String documento) {
		this.documento = documento;
		return this;
	}

	public String getHora_inicio() {
		return hora_inicio;
	}

	public Adesao setHora_inicio(String hora_inicio) {
		this.hora_inicio = hora_inicio;
		return this;
	}

	public String getHora_fim() {
		return hora_fim;
	}

	public Adesao setHora_fim(String hora_fim) {
		this.hora_fim = hora_fim;
		return this;
	}

	public String getValor_minimo() {
		return valor_minimo;
	}

	public Adesao setValor_minimo(String valor_minimo) {
		this.valor_minimo = valor_minimo;
		return this;
	}

	public List<Telefone> getTelefones() {
		return telefones;
	}

	public Adesao setTelefones(List<Telefone> telefones) {
		this.telefones = telefones;
		return this;
	}

}