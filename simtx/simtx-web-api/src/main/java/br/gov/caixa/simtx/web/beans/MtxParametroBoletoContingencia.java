package br.gov.caixa.simtx.web.beans;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Bean criado para conter dados da tabela de parametros de boletos contingencia.
 * 
 * Usado para mapear consulta e atualizacao.
 * 
 * PK (canal, icTipoBoletoCodigo, icOrigemContingenciaDescricao).
 *
 *
 * @author joseoliveirajunior
 */
@XmlRootElement
public class MtxParametroBoletoContingencia {

	/**
	 * Canal - Faz parte da PK.
	 */
	private MtxCanal canal;
	
	/**
	 * Tipo de Boleto - Codigo - Faz parte da PK. 
	 */
	private int icTipoBoletoCodigo;
	
	/**
	 * Tipo de Boleto - Descricao.
	 */
	private String icTipoBoletoDescricao;
	
	/**
	 * Origem Contingencia - Codigo - Faz parte da PK. 
	 */
	private int icOrigemContingenciaCodigo;
	
	/**
	 * Origem Contingencia - Descricao. 
	 */
	private String icOrigemContingenciaDescricao;
	
	/**
	 * Valor de Recebimento Minimo. 
	 */
	private String valorRecebimentoMinimo;
	
	/**
	 * Valor de Recebimento Minimo Origem. 
	 */
	private String valorRecebimentoMinimoOrigem;
	
	/**
	 * Valor de Recebimento Maximo. 
	 */
	private String valorRecebimentoMaximo;
	
	/**
	 * Valor de Recebimento Maximo Origem. 
	 */
	private String valorRecebimentoMaximoOrigem;
	
	/**
	 * Autorizacao de Pagamento - Codigo.
	 */ 
	private int icAutorizacaoPagamentoCodigo;
	
	/**
	 * Autorizacao de Pagamento - Codigo Origem.
	 */ 
	private int icAutorizacaoPagamentoCodigoOrigem;
	
	/**
	 * Autorizacao de Pagamento - Descricao.
	 */
	private String icAutorizacaoPagamentoDescricao;
	
	/**
	 * Data da atualizacao. 
	 */
	private String dataAtualizacao;
	
	/**
	 * Status. 
	 */
	private String status;

	public MtxCanal getCanal() {
		return canal;
	}

	@XmlElement
	public void setCanal(MtxCanal canal) {
		this.canal = canal;
	}

	public int getIcTipoBoletoCodigo() {
		return icTipoBoletoCodigo;
	}

	@XmlElement
	public void setIcTipoBoletoCodigo(int icTipoBoletoCodigo) {
		this.icTipoBoletoCodigo = icTipoBoletoCodigo;
	}

	public String getIcTipoBoletoDescricao() {
		return icTipoBoletoDescricao;
	}

	@XmlElement
	public void setIcTipoBoletoDescricao(String icTipoBoletoDescricao) {
		this.icTipoBoletoDescricao = icTipoBoletoDescricao;
	}

	public int getIcOrigemContingenciaCodigo() {
		return icOrigemContingenciaCodigo;
	}

	@XmlElement
	public void setIcOrigemContingenciaCodigo(int icOrigemContingenciaCodigo) {
		this.icOrigemContingenciaCodigo = icOrigemContingenciaCodigo;
	}

	public String getIcOrigemContingenciaDescricao() {
		return icOrigemContingenciaDescricao;
	}

	@XmlElement
	public void setIcOrigemContingenciaDescricao(String icOrigemContingenciaDescricao) {
		this.icOrigemContingenciaDescricao = icOrigemContingenciaDescricao;
	}

	public String getValorRecebimentoMinimo() {
		return valorRecebimentoMinimo;
	}

	@XmlElement
	public void setValorRecebimentoMinimo(String valorRecebimentoMinimo) {
		this.valorRecebimentoMinimo = valorRecebimentoMinimo;
	}

	public String getValorRecebimentoMaximo() {
		return valorRecebimentoMaximo;
	}

	@XmlElement
	public void setValorRecebimentoMaximo(String valorRecebimentoMaximo) {
		this.valorRecebimentoMaximo = valorRecebimentoMaximo;
	}

	public int getIcAutorizacaoPagamentoCodigo() {
		return icAutorizacaoPagamentoCodigo;
	}

	@XmlElement
	public void setIcAutorizacaoPagamentoCodigo(int icAutorizacaoPagamentoCodigo) {
		this.icAutorizacaoPagamentoCodigo = icAutorizacaoPagamentoCodigo;
	}

	public String getIcAutorizacaoPagamentoDescricao() {
		return icAutorizacaoPagamentoDescricao;
	}

	@XmlElement
	public void setIcAutorizacaoPagamentoDescricao(String icAutorizacaoPagamentoDescricao) {
		this.icAutorizacaoPagamentoDescricao = icAutorizacaoPagamentoDescricao;
	}

	public String getDataAtualizacao() {
		return dataAtualizacao;
	}

	@XmlElement
	public void setDataAtualizacao(String dataAtualizacao) {
		this.dataAtualizacao = dataAtualizacao;
	}

	public String getStatus() {
		return status;
	}

	@XmlElement
	public void setStatus(String status) {
		this.status = status;
	}

	public String getValorRecebimentoMinimoOrigem() {
		return valorRecebimentoMinimoOrigem;
	}

	@XmlElement
	public void setValorRecebimentoMinimoOrigem(String valorRecebimentoMinimoOrigem) {
		this.valorRecebimentoMinimoOrigem = valorRecebimentoMinimoOrigem;
	}

	public String getValorRecebimentoMaximoOrigem() {
		return valorRecebimentoMaximoOrigem;
	}

	@XmlElement
	public void setValorRecebimentoMaximoOrigem(String valorRecebimentoMaximoOrigem) {
		this.valorRecebimentoMaximoOrigem = valorRecebimentoMaximoOrigem;
	}

	public int getIcAutorizacaoPagamentoCodigoOrigem() {
		return icAutorizacaoPagamentoCodigoOrigem;
	}

	@XmlElement
	public void setIcAutorizacaoPagamentoCodigoOrigem(int icAutorizacaoPagamentoCodigoOrigem) {
		this.icAutorizacaoPagamentoCodigoOrigem = icAutorizacaoPagamentoCodigoOrigem;
	}
}
