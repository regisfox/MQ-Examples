package br.gov.caixa.simtx.util.servico;

import java.io.IOException;

import br.gov.caixa.simtx.util.SimtxConfig;
import br.gov.caixa.simtx.util.gerenciador.cache.RepositorioArquivo;

public class RetomadaTransacaoUtils {

	private static final String CAMINHO_XSL_ENTRADA = "servico/Retomada_Transacao/V1/retomarTransacao.xsl";
	
	private RetomadaTransacaoUtils() {}
	
	public static String getXslConteudo(RepositorioArquivo repositorio) throws IOException {
		return repositorio.recuperarArquivo(new SimtxConfig().getCaminhoXslt() + CAMINHO_XSL_ENTRADA);
	}
}
