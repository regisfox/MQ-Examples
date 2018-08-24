package br.gov.caixa.simtx.roteador.api.ws.xml.consulta;

import java.math.BigDecimal;
import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "dados_saida_negocial_type", propOrder = {
    "formaRecebimento", "valorNominal", "dataVencimento"
})
public class ConsultaRegrasBoletoDadosSaida {

    @XmlElement(name = "FORMA_RECEBIMENTO", required = true)
    @XmlSchemaType(name = "string")
    protected String formaRecebimento;

    @XmlElement(name = "VALOR_NOMINAL")
    protected BigDecimal valorNominal;
    
    @XmlElement(name = "DATA_VENCIMENTO")
    @XmlSchemaType(name = "date")
    protected Date dataVencimento;

    
    
    public String getFormaRecebimento() {
		return formaRecebimento;
	}

	public void setFormaRecebimento(String formaRecebimento) {
		this.formaRecebimento = formaRecebimento;
	}

	public BigDecimal getValorNominal() {
		return valorNominal;
	}

	public void setValorNominal(BigDecimal valorNominal) {
		this.valorNominal = valorNominal;
	}

	public Date getDataVencimento() {
		return dataVencimento;
	}

	public void setDataVencimento(Date dataVencimento) {
		this.dataVencimento = dataVencimento;
	}
	
	
}
