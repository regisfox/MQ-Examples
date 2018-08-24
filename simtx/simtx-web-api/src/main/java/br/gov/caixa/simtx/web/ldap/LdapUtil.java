package br.gov.caixa.simtx.web.ldap;

import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.Logger;

public class LdapUtil {

	private static final Logger logger = Logger.getLogger(LdapUtil.class);
	
	public static void setProperty(Object bean, String beanProperty, Attributes attributes, String ldapProperty) throws NamingException {
		
		Attribute attribute = attributes.get(ldapProperty);
		
		if (attribute != null) {
			Object value = attribute.get();
			try {
				BeanUtils.setProperty(bean, beanProperty, value);
			} catch (Exception e) {
				logger.error("Erro ao mapear as propriedades do LDAP!" + e.getMessage());
			}
		
		}
		
	}

	private LdapUtil(){}
}
