package br.gov.caixa.simtx.util.mock;

import javax.enterprise.inject.Alternative;

import org.apache.log4j.Logger;

import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb011VersaoServico;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb014Transacao;
import br.gov.caixa.simtx.util.gerenciador.cache.RepositorioArquivo;
import br.gov.caixa.simtx.util.retomar.transacao.ProcessadorEnvioRetomadaTransacao;
import br.gov.caixa.simtx.util.xml.DadosBarramento;

@Alternative
public class ProcessadorEnvioRetomadaTransacaoMock extends ProcessadorEnvioRetomadaTransacao {

	private static final Logger logger = Logger.getLogger(ProcessadorEnvioRetomadaTransacaoMock.class);

	@Override
	public void setRepositorio(RepositorioArquivo repositorio) {
		super.setRepositorio(repositorio);
	}

	@Override
	public void envioRetomadaTransacao(Mtxtb014Transacao transacaoCorrente, Mtxtb011VersaoServico servicoOrigem, DadosBarramento dadosBarramento,
			String xmlResposta) {
		try {
			super.envioRetomadaTransacao(transacaoCorrente, servicoOrigem, dadosBarramento, xmlResposta);
			logger.info("Mensagem Enviada");
		} catch (Exception e) {
			logger.error("Não foi possível chamar serviço de Retomada de Transacao");
		}
	}
}