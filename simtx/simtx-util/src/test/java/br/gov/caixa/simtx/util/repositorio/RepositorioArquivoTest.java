package br.gov.caixa.simtx.util.repositorio;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;

import br.gov.caixa.simtx.util.gerenciador.cache.RepositorioArquivo;

public class RepositorioArquivoTest {

//	@Test
	public void cacheDeveRetornarMesmaInstanciaDeArquivo() throws IOException {
		RepositorioArquivo repo = new RepositorioArquivo();
		String tmp1 = repo.recuperarArquivo("C:\\Users\\cvoginski\\Documents\\temp.xml");
		String tmp2 = repo.recuperarArquivo("C:\\Users\\cvoginski\\Documents\\temp.xml");
		
		Assert.assertTrue(tmp1 == tmp2);
	}
}
