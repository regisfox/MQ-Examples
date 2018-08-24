package br.gov.caixa.simtx.roteador.api.ws.xml.consulta;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "dados_entrada_negocial_type", propOrder = { "nsuTransacao", "ultimaParcelaViavel", "pagamentoParcial",
		"qtdePagtoParcialPermitido", "nuParcelaAtual", "recebeValorDivergente", "especie", "tipoContingencia",
		"novaPlataformaCobranca", "dataConsulta", "dataVencimento", "codigoBarras" })
public class ConsultaRegrasBoletoDadosEntrada {

	@XmlElement(name = "NSUTRANSACAO", required = true)
    protected Long nsuTransacao;
	
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
    
    @XmlElement(name = "DATA_CONSULTA", required = true)
    protected String dataConsulta;
    
    @XmlElement(name = "DATA_VENCIMENTO", required = true)
    @XmlSchemaType(name = "date")
    protected String dataVencimento;
    
    @XmlElement(name = "CODIGO_BARRAS", required = true)
	protected String codigoBarras;

    
    
    
    public Long getNsuTransacao() {
		return nsuTransacao;
	}

	public void setNsuTransacao(Long nsuTransacao) {
		this.nsuTransacao = nsuTransacao;
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

	public String getDataConsulta() {
		return dataConsulta;
	}

	public void setDataConsulta(String dataConsulta) {
		this.dataConsulta = dataConsulta;
	}

	public String getDataVencimento() {
		return dataVencimento;
	}

	public void setDataVencimento(String dataVencimento) {
		this.dataVencimento = dataVencimento;
	}

	public String getCodigoBarras() {
		return codigoBarras;
	}

	public void setCodigoBarras(String codigoBarras) {
		this.codigoBarras = codigoBarras;
	}

}
