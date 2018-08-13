package br.gov.caixa.simtx.queue;

import org.apache.log4j.Logger;
import org.junit.Assert;
import org.w3c.dom.Document;

import br.gov.caixa.simtx.constants.Constants;
import br.gov.caixa.simtx.test.infra.TestMQ;
import br.gov.caixa.simtx.vo.Fila;
import br.gov.caixa.simtx.xml.TransformadorXsl;

public class CaixaQueueImpl extends TestMQ implements CaixaQueue {

	final Logger logger = Logger.getLogger(Fila.class);
	
	protected Document doc;

	private String ambienteLocal = null;


	public void callQueue(String ambiente, String nomeFilaProperties, String xmlPathFile) throws Exception {
		ambienteLocal = ambiente;
		callSendMessage(nomeFilaProperties, xmlPathFile);
	}

	private void callSendMessage(String nomeFila, String xmlPath) throws Exception {
		String xml = TransformadorXsl.recuperarArquivo(xmlPath);
		sendMessage(nomeFila, xml);
	}

	private void sendMessage(String nomeFila, String xml) throws Exception {
		logger.info("---------sendMessage----------");

		if (Constants.LOC.equals(ambienteLocal)) {
			setUserBrq();
		} else {
			setUserMtx();
		}

		Fila fila = Fila.getFilaRequisicao(nomeFila);
		String id = enviarGetId(fila, xml);

		logger.info("Enviado " + id);

		String resp = ler(Fila.getFilaResposta(nomeFila), id);

		Assert.assertNotNull("Nao teve resposta", resp);
		Assert.assertNotSame("Nao teve resposta", resp, "");

		logger.info("---------sendMessage----------");
	}
}
