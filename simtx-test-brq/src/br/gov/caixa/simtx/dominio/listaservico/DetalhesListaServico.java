package br.gov.caixa.simtx.dominio.listaservico;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "detalhes", propOrder = { "listaServicos", "codigoResposta", "mensagemResposta" })
public class DetalhesListaServico {
	@XmlElement(name = "servico")
	List<ServicoXml> listaServicos = new ArrayList<ServicoXml>();
	
	@XmlElement(name = "cod_resposta", required=true)
	private Integer codigoResposta;
	
	@XmlElement(name = "msg_resposta", required=true)
	private String mensagemResposta;


	public List<ServicoXml> getListaServicos() {
		return listaServicos;
	}

	public void setListaServicos(List<ServicoXml> listaServicos) {
		this.listaServicos = listaServicos;
	}

	public Integer getCodigoResposta() {
		return codigoResposta;
	}

	public void setCodigoResposta(Integer codigoResposta) {
		this.codigoResposta = codigoResposta;
	}

	public String getMensagemResposta() {
		return mensagemResposta;
	}

	public void setMensagemResposta(String mensagemResposta) {
		this.mensagemResposta = mensagemResposta;
	}
}
