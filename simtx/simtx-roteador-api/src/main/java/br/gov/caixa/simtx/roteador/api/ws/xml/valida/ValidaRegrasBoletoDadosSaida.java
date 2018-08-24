package br.gov.caixa.simtx.roteador.api.ws.xml.valida;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "dados_saida_negocial_type", propOrder = { "tipoBaixa" })
public class ValidaRegrasBoletoDadosSaida {

    @XmlElement(name = "TIPO_BAIXA", required = true)
    @XmlSchemaType(name = "string")
    protected String tipoBaixa;

    
    
	public String getTipoBaixa() {
		return tipoBaixa;
	}

	public void setTipoBaixa(String tipoBaixa) {
		this.tipoBaixa = tipoBaixa;
	}
    
}
