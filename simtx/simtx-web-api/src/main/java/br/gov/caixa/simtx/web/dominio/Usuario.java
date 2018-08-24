package br.gov.caixa.simtx.web.dominio;

import java.io.Serializable;
import java.util.List;

/**
 * The Class Usuario.
 */
public class Usuario implements Serializable {
	
	/**
	 * Instantiates a new usuario.
	 */
	public Usuario(){
	    
	    /****/
	}

	public Usuario(String nome){
		this.nome = nome;
	}
	
	public Usuario(String nome, String grupoAcesso, String login){
		this.nome = nome;
		this.grupoAcesso = grupoAcesso;
		this.login = login;
	}
	
	private static final long serialVersionUID = 1L;

	private String nome;
	private String grupoAcesso;
	private String login;
	private String token;
	private List<String> funcionalidades;
	
	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getGrupoAcesso() {
		return grupoAcesso;
	}

	public void setGrupoAcesso(String grupoAcesso) {
		this.grupoAcesso = grupoAcesso;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public List<String> getFuncionalidades() {
		return funcionalidades;
	}

	public void setFuncionalidades(List<String> funcionalidades) {
		this.funcionalidades = funcionalidades;
	}
}
