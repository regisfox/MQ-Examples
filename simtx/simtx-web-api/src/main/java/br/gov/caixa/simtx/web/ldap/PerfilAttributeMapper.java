package br.gov.caixa.simtx.web.ldap;

import javax.naming.NamingException;
import javax.naming.directory.Attributes;

import org.springframework.ldap.core.AttributesMapper;

import br.gov.caixa.simtx.web.dominio.Perfil;

public class PerfilAttributeMapper implements AttributesMapper {
	
	public Object mapFromAttributes(Attributes attributes) throws NamingException {
		
		Perfil perfil = new Perfil();

		LdapUtil.setProperty(perfil, "uid", attributes, "uid");
		LdapUtil.setProperty(perfil, "username", attributes, "uid");
		LdapUtil.setProperty(perfil, "noUsuario", attributes, "NO-USUARIO");
		LdapUtil.setProperty(perfil, "givenName", attributes, "givenName");
		LdapUtil.setProperty(perfil, "cn", attributes, "cn");
		
		return perfil;
   }
	
}