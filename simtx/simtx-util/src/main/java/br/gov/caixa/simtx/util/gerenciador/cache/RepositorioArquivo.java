/***************************************************************************
 * Copyright (C)  2017 - CAIXA Econômica Federal 
 * 
 * Todos os direitos reservados
 **************************************************************************/
package br.gov.caixa.simtx.util.gerenciador.cache;

import static java.nio.file.Files.readAllBytes;
import static java.nio.file.Paths.get;

import java.io.IOException;

import javax.annotation.PostConstruct;
import javax.cache.Cache;
import javax.cache.CacheManager;
import javax.cache.Caching;
import javax.cache.configuration.MutableConfiguration;
import javax.cache.expiry.CreatedExpiryPolicy;
import javax.cache.expiry.Duration;
import javax.cache.spi.CachingProvider;
import javax.inject.Singleton;

import org.apache.log4j.Logger;

@Singleton
public class RepositorioArquivo {
	private static final Logger logger = Logger.getLogger(RepositorioArquivo.class);

	private Cache<String, String> cache;
	
	@PostConstruct
	public void prepararCache() {
		CachingProvider provider = Caching.getCachingProvider();  
		CacheManager cacheManager = provider.getCacheManager();   
		MutableConfiguration<String, String> configuration =
		    new MutableConfiguration<String, String>()  
		        .setTypes(String.class, String.class)   
		        .setStoreByValue(false)   
		        .setExpiryPolicyFactory(CreatedExpiryPolicy.factoryOf(Duration.THIRTY_MINUTES));
		
		cache = cacheManager.createCache("arquivo", configuration); 
	}
	
	public String recuperarArquivo(String caminho) throws IOException {
		if(cache != null) {
			return recuperarComCache(caminho);
		} else {
			return recuperarDisco(caminho);
		}
	}

	private String recuperarComCache(String caminho) throws IOException {
		String arquivo = cache.get(caminho);
		if(arquivo == null) {
			arquivo = recuperarDisco(caminho);
			cache.put(caminho, arquivo);
		} else {
			logger.info("Acesso arquivo com cache " + caminho);
		}
		return arquivo;
	}

	private String recuperarDisco(String caminho) throws IOException {
		String arquivo;
		logger.info("Acesso arquivo sem cache " + caminho);
		arquivo = new String(readAllBytes(get(caminho)));
		return arquivo;
	}

	public Cache<String, String> getCache() {
		return cache;
	}

	public void setCache(Cache<String, String> cache) {
		this.cache = cache;
	}
}
