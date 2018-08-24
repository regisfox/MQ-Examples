package br.gov.caixa.simtx.web.ldap;

import javax.naming.NamingException;
import javax.naming.directory.Attributes;

import org.springframework.ldap.core.AttributesMapper;

import br.gov.caixa.simtx.web.dominio.Grupo;

public class GrupoAttributeMapper implements AttributesMapper {

	public Object mapFromAttributes(Attributes attributes) throws NamingException {

		Grupo grupo = new Grupo();
  		LdapUtil.setProperty(grupo, "cn", attributes, "cn");

		return grupo;
	}
}

