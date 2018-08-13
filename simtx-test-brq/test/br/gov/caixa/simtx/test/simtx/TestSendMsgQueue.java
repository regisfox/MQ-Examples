package br.gov.caixa.simtx.test.simtx;

import org.apache.log4j.Logger;
import org.junit.Test;

import br.gov.caixa.simtx.constants.Constants;
import br.gov.caixa.simtx.queue.CaixaQueueImpl;
import br.gov.caixa.simtx.utils.BaseUtil;

public class TestSendMsgQueue extends BaseUtil {

	final static Logger logger = Logger.getLogger(TestSendMsgQueue.class);
//	private String fileName = "Detalhe_Agendamento_Transacao.xml";
//	private String fileName = "Lista_Comprovantes_SIAUT_NSGD.xml";
//	private String fileName = "PagamentoBoletoDEV.xml";
//	private String fileName = "PagamentoBoletoEntrada.xml";
//	private String fileName = "Lista transacoes agendadas.xml";
//  private String fileName = "Detalhe_Agendamento_Transacao.xml";
//	private String fileName = "consulta-extrato.xml";
//	private String fileName = "Detalhe_Agendamento_Boleto.xml";
//	private String fileName = "Consulta_Boleto.xml";
//	private String fileName = "Cancelamento_Agendamento.xml";
    private String fileName = "Valida Boleto caixa.xml";

	
	@Test
	public void test() {
		try {

			CaixaQueueImpl caixaQueueImpl = new CaixaQueueImpl();

//			caixaQueueImpl.callQueue(Constants.DEV, internetBankingDevQueue, getPathFile(true, diretorio.listFiles(), fileName));
			caixaQueueImpl.callQueue(Constants.LOC, localSimtxQueue, getPathFile(true, diretorioProjeto.listFiles(), fileName));

		} catch (Exception e) {
			logger.error(e);
		}
	}

}
