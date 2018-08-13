package br.gov.caixa.simtx.dominio.listaservico;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "servico", propOrder = {
	    "codigoServico",
	    "nomeServico",
	    "valorMinimo",
	    "faixaValores"
})
public class ServicoXml {
    @XmlElement(name = "cod_servico", required = true)
	private Long codigoServico;
    
    @XmlElement(name = "nome_servico", required = true)
	private String nomeServico;
    
    @XmlElement(name = "valor_minimo", required = true)
	private String valorMinimo;
    
    @XmlElement(name = "faixa_valores", required = true)
	private FaixaValoresXml faixaValores;

	public Long getCodigoServico() {
		return codigoServico;
	}

	public void setCodigoServico(Long codigoServico) {
		this.codigoServico = codigoServico;
	}

	public String getNomeServico() {
		return nomeServico;
	}

	public void setNomeServico(String nomeServico) {
		this.nomeServico = nomeServico;
	}

	public String getValorMinimo() {
		return valorMinimo;
	}

	public void setValorMinimo(String valorMinimo) {
		this.valorMinimo = valorMinimo;
	}

	public FaixaValoresXml getFaixaValores() {
		return faixaValores;
	}

	public void setFaixaValores(FaixaValoresXml faixaValores) {
		this.faixaValores = faixaValores;
	}
}