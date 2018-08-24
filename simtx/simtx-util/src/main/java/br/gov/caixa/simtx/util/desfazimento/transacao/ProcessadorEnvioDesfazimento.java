package br.gov.caixa.simtx.util.desfazimento.transacao;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import javax.ejb.Asynchronous;
import javax.ejb.Stateless;
import javax.inject.Inject;

import org.apache.log4j.Logger;

import br.gov.caixa.simtx.persistencia.constante.Constantes;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb003ServicoTarefa;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb014Transacao;
import br.gov.caixa.simtx.util.gerenciador.cache.RepositorioArquivo;
import br.gov.caixa.simtx.util.jms.ConexaoHQ;
import br.gov.caixa.simtx.util.servico.DesfazimentoUtils;
import br.gov.caixa.simtx.util.xml.DadosBarramento;
import br.gov.caixa.simtx.util.xml.ParametroXsl;
import br.gov.caixa.simtx.util.xml.TransformadorXsl;

@Stateless
public class ProcessadorEnvioDesfazimento implements Serializable {

	private static final long serialVersionUID = 1L;

	private static final Logger logger = Logger.getLogger(ProcessadorEnvioDesfazimento.class);

	@Inject
	private RepositorioArquivo repositorio;
	
	@Inject
	private ConexaoHQ conexaoHQ;

	@Asynchronous
	public void enviarMsgDesfazimento(List<Mtxtb003ServicoTarefa> listaTarefas, Mtxtb014Transacao transacaoCorrente, DadosBarramento dadosBarramento) {
		try {
			if (transacaoCorrente == null) {
				return;
			}

			boolean temTarefaParaDesfazer = false;
			if (transacaoCorrente.getIcSituacao() == null || transacaoCorrente.getIcSituacao().compareTo(Constantes.IC_SERVICO_NEGADO) == 0
					|| transacaoCorrente.getIcSituacao().compareTo(new BigDecimal(0)) == 0) {
				for (Mtxtb003ServicoTarefa st : listaTarefas) {
					if (st.getMtxtb012VersaoTarefa().getVersaoTarefaDesfazimento() != null) {
						temTarefaParaDesfazer = true;
						break;
					}
				}
			}

			if (temTarefaParaDesfazer) {
				String nsu = String.valueOf(transacaoCorrente.getNuNsuTransacao());
				String xmlEntrada = new TransformadorXsl().transformar(dadosBarramento.getDadosLeitura(), DesfazimentoUtils.getXslConteudo(this.repositorio),
						new ParametroXsl("nsu", nsu));

				logger.info("Enviando XML para desfazimento");
				logger.info(xmlEntrada);
				conexaoHQ.enviarMensagem(xmlEntrada);
			}
		} catch (Exception e) {
			logger.error("Não foi possível chamar serviço de desfazimento");
		}
	}
}
