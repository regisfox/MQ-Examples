package br.gov.caixa.simtx.roteador.api.ws.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "HEADER_BARRAMENTO_TYPE", propOrder = { "versao", "autenticacao", "usuarioServico", "usuario",
		"operacao", "indice", "sistemaOrigem", "unidade", "identificadorOrigem", "dataHora", "idProcesso" })
public class Header {

	@XmlElement(name = "VERSAO", required = true)
    protected String versao;
    
    @XmlElement(name = "AUTENTICACAO")
    protected String autenticacao;
    
    @XmlElement(name = "USUARIO_SERVICO")
    protected String usuarioServico;
    
    @XmlElement(name = "USUARIO")
    protected String usuario;
    
    @XmlElement(name = "OPERACAO", required = true)
    protected String operacao;
    
    @XmlElement(name = "INDICE")
    protected Integer indice;

    @XmlElement(name = "SISTEMA_ORIGEM")
    protected String sistemaOrigem;
    
    @XmlElement(name = "UNIDADE")
    protected String unidade;
    
    @XmlElement(name = "IDENTIFICADOR_ORIGEM")
    protected String identificadorOrigem;
    
    @XmlElement(name = "DATA_HORA", required = true)
    protected String dataHora;
    
    @XmlElement(name = "ID_PROCESSO")
    protected String idProcesso;

    
    
    
	public String getVersao() {
		return versao;
	}

	public void setVersao(String versao) {
		this.versao = versao;
	}

	public String getAutenticacao() {
		return autenticacao;
	}

	public void setAutenticacao(String autenticacao) {
		this.autenticacao = autenticacao;
	}

	public String getUsuarioServico() {
		return usuarioServico;
	}

	public void setUsuarioServico(String usuarioServico) {
		this.usuarioServico = usuarioServico;
	}

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public String getOperacao() {
		return operacao;
	}

	public void setOperacao(String operacao) {
		this.operacao = operacao;
	}

	public Integer getIndice() {
		return indice;
	}

	public void setIndice(Integer indice) {
		this.indice = indice;
	}

	public String getSistemaOrigem() {
		return sistemaOrigem;
	}

	public void setSistemaOrigem(String sistemaOrigem) {
		this.sistemaOrigem = sistemaOrigem;
	}

	public String getUnidade() {
		return unidade;
	}

	public void setUnidade(String unidade) {
		this.unidade = unidade;
	}

	public String getIdentificadorOrigem() {
		return identificadorOrigem;
	}

	public void setIdentificadorOrigem(String identificadorOrigem) {
		this.identificadorOrigem = identificadorOrigem;
	}

	public String getDataHora() {
		return dataHora;
	}

	public void setDataHora(String dataHora) {
		this.dataHora = dataHora;
	}

	public String getIdProcesso() {
		return idProcesso;
	}

	public void setIdProcesso(String idProcesso) {
		this.idProcesso = idProcesso;
	}
    
}
