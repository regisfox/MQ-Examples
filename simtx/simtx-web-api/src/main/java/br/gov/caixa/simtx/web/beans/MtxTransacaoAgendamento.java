
package br.gov.caixa.simtx.web.beans;

import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;

import br.gov.caixa.simtx.util.data.DataUtil;

@XmlRootElement
public class MtxTransacaoAgendamento {

	private long nsuSimtxAgendamento;

	private long nsuSimtx;

	private Date dtReferencia;

	private Date dtEfetivacao;

	private int nuUnidade;

	private int nuProduto;

	private long nuConta;

	private int dvConta;

	private int icTipoConta;

	private MtxCanal canal;

	private MtxServico servico;

	public long getNsuSimtxAgendamento() {
		return nsuSimtxAgendamento;
	}

	public void setNsuSimtxAgendamento(long nsuSimtxAgendamento) {
		this.nsuSimtxAgendamento = nsuSimtxAgendamento;
	}

	public long getNsuSimtx() {
		return nsuSimtx;
	}

	public void setNsuSimtx(long nsuSimtx) {
		this.nsuSimtx = nsuSimtx;
	}

	public Date getDtReferencia() {
		return dtReferencia;
	}

	public void setDtReferencia(Date dtReferencia) {
		this.dtReferencia = dtReferencia;
	}

	public Date getDtEfetivacao() {
		return dtEfetivacao;
	}

	public void setDtEfetivacao(Date dtEfetivacao) {
		this.dtEfetivacao = dtEfetivacao;
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

	public String toString() {
		return "{\"nsuSimtxAgendamento\":" + nsuSimtxAgendamento + ",\"nsuSimtx\":" + nsuSimtx + ",\""
				+ (dtReferencia != null ? "dtReferencia\":\"" + DataUtil.formatar(dtReferencia, DataUtil.FORMATO_DATA_YYYY_MM_DD_HIFEN_JSON) + "\",\"" : "")
				+ (dtEfetivacao != null ? "dtEfetivacao\":\"" + DataUtil.formatar(dtEfetivacao, DataUtil.FORMATO_DATA_YYYY_MM_DD_HIFEN_JSON) + "\",\"" : "") + "nuUnidade\":" + nuUnidade + ",\"nuProduto\":" + nuProduto + ",\"nuConta\":"
				+ nuConta + ",\"dvConta\":" + dvConta + ",\"icTipoConta\":" + icTipoConta + ",\"" + (canal != null ? "canal\":" + canal + ",\"" : "")
				+ (servico != null ? "servico\":" + servico : "") + "}";
	}

}
