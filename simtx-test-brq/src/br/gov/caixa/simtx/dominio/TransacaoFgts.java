package br.gov.caixa.simtx.dominio;

import java.util.Date;

import br.gov.caixa.simtx.layout.Posicional;

public class TransacaoFgts extends Posicional {
	private Long nsu;
	private Integer codigoTransacao;
	private Date dataReferencia;
	private Date dataTransacao;
	private Integer canal;
	private Long nis;
	private String jam;
	private Long valor;
	private String competencia;
	private Long conta;

	public Long getNsu() {
		return nsu;
	}

	public void setNsu(Long nsu) {
		this.nsu = nsu;
	}

	public Integer getCodigoTransacao() {
		return codigoTransacao;
	}

	public void setCodigoTransacao(Integer codigoTransacao) {
		this.codigoTransacao = codigoTransacao;
	}

	public Date getDataReferencia() {
		return dataReferencia;
	}

	public void setDataReferencia(Date dataReferencia) {
		this.dataReferencia = dataReferencia;
	}

	public Date getDataTransacao() {
		return dataTransacao;
	}

	public void setDataTransacao(Date dataTransacao) {
		this.dataTransacao = dataTransacao;
	}

	public Integer getCanal() {
		return canal;
	}

	public void setCanal(Integer canal) {
		this.canal = canal;
	}

	public Long getNis() {
		return nis;
	}

	public void setNis(Long nis) {
		this.nis = nis;
	}

	public String getJam() {
		return jam;
	}

	public void setJam(String jam) {
		this.jam = jam;
	}

	public Long getValor() {
		return valor;
	}

	public void setValor(Long valor) {
		this.valor = valor;
	}

	public String getCompetencia() {
		return competencia;
	}

	public void setCompetencia(String competencia) {
		this.competencia = competencia;
	}

	public Long getConta() {
		return conta;
	}

	public void setConta(Long conta) {
		this.conta = conta;
	}

	public String converter() {
		StringBuilder convertido = new StringBuilder();
		
		convertido
			.append(formata(nsu, 12))
			.append(formata(codigoTransacao, 8))
			.append(formataDataReverso(dataReferencia))
			.append(formataDataReverso(dataTransacao))
			.append(formata(canal, 4))
			.append(formata(nis, 11))
			.append(formata(jam, 7))
			.append(formata(valor, 11))
			.append(formata(competencia, 6))
			.append(formata(conta, 11));
		
		return convertido.toString();
	}
}
