package br.gov.caixa.simtx.roteador.api.ws.xml.valida;

import java.math.BigDecimal;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "dados_entrada_negocial_type", propOrder = { "nsuTransacao", "nuCanal", "codBarras", "ultimaParcelaViavel",
		"pagamentoParcial", "qtdePagtoParcialPermitido", "nuParcelaAtual", "recebeValorDivergente", "especie",
		"tipoContingencia", "novaPlataformaCobranca", "dataPagamento", "dataVencimentoUtil", "valorMinimoConsulta",
		"valorMaximoConsulta", "valorTotalCalculado", "valorPagar", "valorNominal" })
public class ValidaRegrasBoletoDadosEntrada {

	@XmlElement(name = "NSU_TRANSACAO", required = true)
    protected Long nsuTransacao;
	
	@XmlElement(name = "NU_CANAL", required = true)
	protected int nuCanal;
	
	@XmlElement(name = "CODIGO_BARRAS", required = true)
	protected String codBarras;
	
	@XmlElement(name = "ULTIMA_PARCELA_VIAVEL", required = true)
	protected String ultimaParcelaViavel;
    
    @XmlElement(name = "PAGAMENTO_PARCIAL", required = true)
    protected String pagamentoParcial;
    
    @XmlElement(name = "QUANTIDADE_PAGAMENTO_PARCIAL_PERMITIDO", required = true)
    protected int qtdePagtoParcialPermitido;
    
    @XmlElement(name = "NUMERO_PARCELA_ATUAL", required = true)
    protected int nuParcelaAtual;
    
    @XmlElement(name = "RECEBE_VALOR_DIVERGENTE", required = true)
    protected String recebeValorDivergente;
    
    @XmlElement(name = "ESPECIE", required = true)
    protected String especie;
    
    @XmlElement(name = "TIPO_CONTINGENCIA", required = true)
    protected String tipoContingencia;
    
    @XmlElement(name = "NOVA_PLATAFORMA_DE_COBRANCA", required = true)
    protected String novaPlataformaCobranca;
    
    @XmlElement(name = "DATA_PAGAMENTO", required = true)
    @XmlSchemaType(name = "date")
    protected String dataPagamento;
    
    @XmlElement(name = "DATA_VENCIMENTO_UTIL", required = true)
    @XmlSchemaType(name = "date")
    protected String dataVencimentoUtil;
    
    @XmlElement(name = "VALOR_MINIMO_CONSULTA", required = true)
    protected BigDecimal valorMinimoConsulta;
    
    @XmlElement(name = "VALOR_MAXIMO_CONSULTA", required = true)
    protected BigDecimal valorMaximoConsulta;
    
    @XmlElement(name = "VALOR_TOTAL_CALCULADO", required = true)
    protected BigDecimal valorTotalCalculado;
    
    @XmlElement(name = "VALOR_PAGAR", required = true)
    protected BigDecimal valorPagar;
    
    @XmlElement(name = "VALOR_NOMINAL", required = true)
    protected BigDecimal valorNominal;

    
    
    
	public Long getNsuTransacao() {
		return nsuTransacao;
	}

	public void setNsuTransacao(Long nsuTransacao) {
		this.nsuTransacao = nsuTransacao;
	}
	
	public int getNuCanal() {
		return nuCanal;
	}

	public void setNuCanal(int nuCanal) {
		this.nuCanal = nuCanal;
	}
	
	public String getCodBarras() {
		return codBarras;
	}

	public void setCodBarras(String codBarras) {
		this.codBarras = codBarras;
	}

	public String getUltimaParcelaViavel() {
		return ultimaParcelaViavel;
	}

	public void setUltimaParcelaViavel(String ultimaParcelaViavel) {
		this.ultimaParcelaViavel = ultimaParcelaViavel;
	}

	public String getPagamentoParcial() {
		return pagamentoParcial;
	}

	public void setPagamentoParcial(String pagamentoParcial) {
		this.pagamentoParcial = pagamentoParcial;
	}

	public int getQtdePagtoParcialPermitido() {
		return qtdePagtoParcialPermitido;
	}

	public void setQtdePagtoParcialPermitido(int qtdePagtoParcialPermitido) {
		this.qtdePagtoParcialPermitido = qtdePagtoParcialPermitido;
	}

	public int getNuParcelaAtual() {
		return nuParcelaAtual;
	}

	public void setNuParcelaAtual(int nuParcelaAtual) {
		this.nuParcelaAtual = nuParcelaAtual;
	}

	public String getRecebeValorDivergente() {
		return recebeValorDivergente;
	}

	public void setRecebeValorDivergente(String recebeValorDivergente) {
		this.recebeValorDivergente = recebeValorDivergente;
	}

	public String getEspecie() {
		return especie;
	}

	public void setEspecie(String especie) {
		this.especie = especie;
	}

	public String getTipoContingencia() {
		return tipoContingencia;
	}

	public void setTipoContingencia(String tipoContingencia) {
		this.tipoContingencia = tipoContingencia;
	}

	public String getNovaPlataformaCobranca() {
		return novaPlataformaCobranca;
	}

	public void setNovaPlataformaCobranca(String novaPlataformaCobranca) {
		this.novaPlataformaCobranca = novaPlataformaCobranca;
	}

	public String getDataPagamento() {
		return dataPagamento;
	}

	public void setDataPagamento(String dataPagamento) {
		this.dataPagamento = dataPagamento;
	}

	public String getDataVencimentoUtil() {
		return dataVencimentoUtil;
	}

	public void setDataVencimentoUtil(String dataVencimentoUtil) {
		this.dataVencimentoUtil = dataVencimentoUtil;
	}

	public BigDecimal getValorMinimoConsulta() {
		return valorMinimoConsulta;
	}

	public void setValorMinimoConsulta(BigDecimal valorMinimoConsulta) {
		this.valorMinimoConsulta = valorMinimoConsulta;
	}

	public BigDecimal getValorMaximoConsulta() {
		return valorMaximoConsulta;
	}

	public void setValorMaximoConsulta(BigDecimal valorMaximoConsulta) {
		this.valorMaximoConsulta = valorMaximoConsulta;
	}

	public BigDecimal getValorTotalCalculado() {
		return valorTotalCalculado;
	}

	public void setValorTotalCalculado(BigDecimal valorTotalCalculado) {
		this.valorTotalCalculado = valorTotalCalculado;
	}

	public BigDecimal getValorPagar() {
		return valorPagar;
	}

	public void setValorPagar(BigDecimal valorPagar) {
		this.valorPagar = valorPagar;
	}

	public BigDecimal getValorNominal() {
		return valorNominal;
	}

	public void setValorNominal(BigDecimal valorNominal) {
		this.valorNominal = valorNominal;
	}
	
}
