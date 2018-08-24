package br.gov.caixa.simtx.web.beans;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class MtxCancelamentoAgendamentoWeb {

	private Long nuNsuTransacaoAgendamento;

	private Long nuNsuTransacaoSimtx;

	private String dataAgendamento;

	private String dataEfetivacao;

	private int nuUnidade;

	private int nuProduto;

	private long nuConta;

	private int dvConta;

	private int icTipoConta;

	private String valorTransacao;

	private String codigoBarras;

	private String linhaDigitavel;

	private MtxCanal canal;

	private MtxServico servico;
	
	private String codigoMaquina;
	
	private String codigoUsuario;

	public Long getNuNsuTransacaoAgendamento() {
		return nuNsuTransacaoAgendamento;
	}

	public void setNuNsuTransacaoAgendamento(Long nuNsuTransacaoAgendamento) {
		this.nuNsuTransacaoAgendamento = nuNsuTransacaoAgendamento;
	}

	public Long getNuNsuTransacaoSimtx() {
		return nuNsuTransacaoSimtx;
	}

	public void setNuNsuTransacaoSimtx(Long nuNsuTransacaoSimtx) {
		this.nuNsuTransacaoSimtx = nuNsuTransacaoSimtx;
	}

	public String getDataAgendamento() {
		return dataAgendamento;
	}

	public void setDataAgendamento(String dataAgendamento) {
		this.dataAgendamento = dataAgendamento;
	}

	public String getDataEfetivacao() {
		return dataEfetivacao;
	}

	public void setDataEfetivacao(String dataEfetivacao) {
		this.dataEfetivacao = dataEfetivacao;
	}

	public int getNuUnidade() {
		return nuUnidade;
	}

	public void setNuUnidade(int nuUnidade) {
		this.nuUnidade = nuUnidade;
	}

	public int getNuProduto() {
		return nuProduto;
	}

	public void setNuProduto(int nuProduto) {
		this.nuProduto = nuProduto;
	}

	public long getNuConta() {
		return nuConta;
	}

	public void setNuConta(long nuConta) {
		this.nuConta = nuConta;
	}

	public int getDvConta() {
		return dvConta;
	}

	public void setDvConta(int dvConta) {
		this.dvConta = dvConta;
	}

	public int getIcTipoConta() {
		return icTipoConta;
	}

	public void setIcTipoConta(int icTipoConta) {
		this.icTipoConta = icTipoConta;
	}

	public String getValorTransacao() {
		return valorTransacao;
	}

	public void setValorTransacao(String valorTransacao) {
		this.valorTransacao = valorTransacao;
	}

	public String getCodigoBarras() {
		return codigoBarras;
	}

	public void setCodigoBarras(String codigoBarras) {
		this.codigoBarras = codigoBarras;
	}

	public String getLinhaDigitavel() {
		return linhaDigitavel;
	}

	public void setLinhaDigitavel(String linhaDigitavel) {
		this.linhaDigitavel = linhaDigitavel;
	}

	public MtxCanal getCanal() {
		return canal;
	}

	public void setCanal(MtxCanal canal) {
		this.canal = canal;
	}

	public MtxServico getServico() {
		return servico;
	}

	public void setServico(MtxServico servico) {
		this.servico = servico;
	}
	
	public String getCodigoMaquina() {
		return codigoMaquina;
	}

	public void setCodigoMaquina(String codigoMaquina) {
		this.codigoMaquina = codigoMaquina;
	}

	public String getCodigoUsuario() {
		return codigoUsuario;
	}

	public void setCodigoUsuario(String codigoUsuario) {
		this.codigoUsuario = codigoUsuario;
	}

}
