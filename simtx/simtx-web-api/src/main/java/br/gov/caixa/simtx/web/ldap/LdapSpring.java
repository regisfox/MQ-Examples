package br.gov.caixa.simtx.web.ldap;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.LdapContextSource;
import org.springframework.ldap.pool.factory.PoolingContextSource;

import br.gov.caixa.simtx.web.dominio.Grupo;
import br.gov.caixa.simtx.web.dominio.Perfil;
import br.gov.caixa.simtx.web.dominio.Usuario;
import br.gov.caixa.simtx.web.util.ConstantesWeb;

public class LdapSpring {

	private static final Logger logger = Logger.getLogger(LdapSpring.class);
	protected static String caminhoLdapUrl = System.getProperty(ConstantesWeb.CAMINHO_LDAP_URL);
	protected static String caminhoLdapPrincipalDns = System.getProperty(ConstantesWeb.CAMINHO_LDAP_PRINCIPAL_DNS);
	
	private LdapTemplate ldapTemplate;
    
	@SuppressWarnings("unchecked")
	private String getNivelAcesso(String matricula) {
		logger.info("Buscando grupos de acesso do usuario: "+matricula);
		StringBuilder nivelAcesso = new StringBuilder();
		boolean isFirst = true;
		String filterUser = "(&(uniquemember=uid=" + matricula+ "," + caminhoLdapPrincipalDns + ")" + "(objectclass=groupofuniquenames))";
		List<Grupo> roles = this.ldapTemplate.search("cn=simtx,ou=Groups", filterUser, new GrupoAttributeMapper());

		if (matricula != null) {
			for (Grupo g : roles) {
				
				if (isFirst) {
					isFirst = false;
				} else {
					nivelAcesso.append(",");
				}

				nivelAcesso.append(g.getCn());
			}
			// Sem permissão para operar o sistema
			if (isFirst) {
				nivelAcesso.append("");
			}
		}

		return nivelAcesso.toString();
	}

	private String obterNomeUsuarioLdap(String userName) {
		logger.info("Obtendo nome do usuario: "+userName);
		LdapContextSource contextSource = new LdapContextSource();
		contextSource.setUrl(caminhoLdapUrl);
		contextSource.setBase("o=caixa");
		
		contextSource.setAnonymousReadOnly(true);
		PoolingContextSource poolingContextSource = new PoolingContextSource();
		poolingContextSource.setContextSource(contextSource);

		try {
			contextSource.afterPropertiesSet();
		} catch (Exception e) {
			logger.error("Erro ao conectar" + e.getMessage());
		}
		
		ldapTemplate = new LdapTemplate(poolingContextSource);
		Perfil perfil = consultarPerfil(userName);
		String nome;
		if (perfil.getNoUsuario() != null) {
			nome = perfil.getNoUsuario();
		} else {
			if (perfil.getGivenName() != null) {
				nome = perfil.getGivenName();
			} else {
				nome = perfil.getCn();
			}
		}

		if (nome.indexOf(' ') != -1) {
			nome = nome.substring(0, nome.indexOf(' '));
		}
		return nome;
	}

	@SuppressWarnings("rawtypes")
	public Perfil consultarPerfil(String username) {
		String filterUser = "(&(objectclass=" + "cefusuario" + ")(uid=" + username + "))";
		List list = this.ldapTemplate.search("ou=People", filterUser, new PerfilAttributeMapper());
		return (list != null && !list.isEmpty()) ? (Perfil) list.get(0) : null;

	}

	public Usuario carregarDadoUsuario(String request) {
		logger.info("Carregando dados do usuario: "+request);
		String codigoUsuarioCaixa = request;
		codigoUsuarioCaixa = obterNomeUsuarioLdap(codigoUsuarioCaixa);
		if ("".equals(codigoUsuarioCaixa))
			codigoUsuarioCaixa = request;

		String nome = codigoUsuarioCaixa;
		String grupoAcesso = getNivelAcesso(request);
		String login = request;
		return new Usuario(nome, grupoAcesso, login);
	}
}
