package br.gov.caixa.simtx.core.main;

import java.io.InputStream;
import java.util.Properties;

import javax.ejb.Stateless;

/**
 * Classe responsavel por recuperar mensagens do arquivo 'mensagens.properties'
 * 
 * @author cvoginski
 *
 */
@Stateless
public class MensagemCore {
	/** Nome do Arquivo que contem as mensagens */
	private static final String NOME_ARQUIVO_DE_MENSAGENS = "mensagens.properties";
	private Properties props;
	
	public MensagemCore() {
		String resourceName = NOME_ARQUIVO_DE_MENSAGENS;
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		props = new Properties();
		try (InputStream resourceStream = loader.getResourceAsStream(resourceName)) {
			
			props.load(resourceStream);
		} catch (Exception e) {

		}
	}
	
	public String recuperarMensagem(String chaveMensagem) {
		return (String) props.get(chaveMensagem);
	}
	
}
