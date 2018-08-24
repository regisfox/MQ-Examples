package br.gov.caixa.simtx.util.servico;

import java.io.IOException;

import br.gov.caixa.simtx.util.SimtxConfig;
import br.gov.caixa.simtx.util.gerenciador.cache.RepositorioArquivo;

public class DesfazimentoUtils {

	private static final String CAMINHO_XSL_DESFAZIMENTO = "servico/Desfazimento/V1/desfazimento_interno.xsl";
	
	private DesfazimentoUtils() {}
	
	public static String getXslConteudo(RepositorioArquivo repositorio) throws IOException {
		return repositorio.recuperarArquivo(new SimtxConfig().getCaminhoXslt() + CAMINHO_XSL_DESFAZIMENTO);
	}
}
