package br.gov.caixa.simtx.roteador.api.ws.matriz;

public class MatrizVO {
	
	private String flagNovaPlataformaCobranca;
	
	private String flagContingencia;
	
	private String especie;
	
	private String recebeValorDivergente;
	
	private String dataVencimento;
	
	private String dataConsulta;
	
	private String flagPagamentoParcial;
	
	private int qtdePagamentoParcial;
	
	private int numeroParcelaAtual;
	
	private String ultimaParcelaViavel;
	

	public String getFlagNovaPlataformaCobranca() {
		return flagNovaPlataformaCobranca;
	}

	public void setFlagNovaPlataformaCobranca(String flagNovaPlataformaCobranca) {
		this.flagNovaPlataformaCobranca = flagNovaPlataformaCobranca;
	}

	public String getFlagContingencia() {
		return flagContingencia;
	}

	public void setFlagContingencia(String flagContingencia) {
		this.flagContingencia = flagContingencia;
	}

	public String getEspecie() {
		return especie;
	}

	public void setEspecie(String especie) {
		this.especie = especie;
	}

	public String getRecebeValorDivergente() {
		return recebeValorDivergente;
	}

	public void setRecebeValorDivergente(String recebeValorDivergente) {
		this.recebeValorDivergente = recebeValorDivergente;
	}

	public String getDataVencimento() {
		return dataVencimento;
	}

	public void setDataVencimento(String dataVencimento) {
		this.dataVencimento = dataVencimento;
	}
	
	public String getDataConsulta() {
		return dataConsulta;
	}

	public void setDataConsulta(String dataConsulta) {
		this.dataConsulta = dataConsulta;
	}

	public String getFlagPagamentoParcial() {
		return flagPagamentoParcial;
	}

	public void setFlagPagamentoParcial(String flagPagamentoParcial) {
		this.flagPagamentoParcial = flagPagamentoParcial;
	}

	public int getQtdePagamentoParcial() {
		return qtdePagamentoParcial;
	}

	public void setQtdePagamentoParcial(int qtdePagamentoParcial) {
		this.qtdePagamentoParcial = qtdePagamentoParcial;
	}

	public int getNumeroParcelaAtual() {
		return numeroParcelaAtual;
	}

	public void setNumeroParcelaAtual(int numeroParcelaAtual) {
		this.numeroParcelaAtual = numeroParcelaAtual;
	}

	public String getUltimaParcelaViavel() {
		return ultimaParcelaViavel;
	}

	public void setUltimaParcelaViavel(String ultimaParcelaViavel) {
		this.ultimaParcelaViavel = ultimaParcelaViavel;
	}

}
