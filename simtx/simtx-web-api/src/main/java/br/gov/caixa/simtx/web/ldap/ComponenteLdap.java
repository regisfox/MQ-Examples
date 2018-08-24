package br.gov.caixa.simtx.web.ldap;

import java.util.Properties;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.InitialLdapContext;

import org.apache.log4j.Logger;

import br.gov.caixa.simtx.web.util.ConstantesWeb;

public class ComponenteLdap {
	
	private static final Logger logger = Logger.getLogger(ComponenteLdap.class);
	
//	protected static String caminhoLdapUrl = System.getProperty(ConstantesWeb.CAMINHO_LDAP_URL);
//	protected static String caminhoLdapInicial = System.getProperty(ConstantesWeb.CAMINHO_LDAP_INICIAL);
//	protected static String caminhoLdapPrincipalDns = System.getProperty(ConstantesWeb.CAMINHO_LDAP_PRINCIPAL_DNS);
	protected static String caminhoLdapUrl = "ldap://10.116.92.130:489";
	protected static String caminhoLdapInicial = "com.sun.jndi.ldap.LdapCtxFactory";
	protected static String caminhoLdapPrincipalDns = "ou=People,o=caixa";

	private static boolean initialised;
	protected Properties properties;
	
	
    public static Attributes processRequest(InitialLdapContext ctx, String filter) {
        
    	SearchControls searchControls = new SearchControls();
        searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);
		NamingEnumeration<SearchResult> searchResults;
		
		try {
			searchResults = ctx.search("cn=simtx,ou=Groups,o=caixa", filter, searchControls);
			SearchResult sr = searchResults.next();

			return sr.getAttributes();

		} catch (NamingException e) {
			logger.error(e.getMessage());
		}
		return null;
	}

    public static InitialLdapContext iniciaContextoLdap(String username, String password) {
        
        Properties properties = new Properties();
        properties.put(Context.INITIAL_CONTEXT_FACTORY, caminhoLdapInicial);
        properties.put(Context.PROVIDER_URL, caminhoLdapUrl);
        properties.put(Context.SECURITY_AUTHENTICATION, "simple");
        properties.put(Context.SECURITY_PRINCIPAL, "uid=" + username + "," + caminhoLdapPrincipalDns);
        properties.put(Context.SECURITY_CREDENTIALS, password);
 
        InitialLdapContext ctx = null;
        try {
            // Create initial context
            ctx = new InitialLdapContext(properties, null);
            initialised = true;
        } catch (NamingException e) {
            initialised = false;
            logger.error(e.getMessage());
        } finally {
            if (initialised) {
                logger.info("Login validado com sucesso no Ldap: "+username);
            } else {
            	logger.error("Falha ao iniciar o login no Ldap: "+username);
            }
        }
        return ctx;
    }

	public boolean validaUser(String matricula, String senha) {
		logger.info("Validano usuario no Ldap: "+matricula);
		InitialLdapContext ctx = iniciaContextoLdap(matricula, senha);
		String filterUser = "(&(uniquemember=uid=" + matricula+ ",ou=People,o=caixa)" + "(objectclass=groupofuniquenames))";
				
		try {
			
			if (ctx != null) {
				processRequest(ctx, filterUser);
				ctx.close();
			}
			
		} catch (NamingException e) {
			logger.error(e.getMessage());
		}
		return initialised;
	}
}
