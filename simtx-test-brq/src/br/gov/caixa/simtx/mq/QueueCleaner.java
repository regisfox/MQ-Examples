package br.gov.caixa.simtx.mq;

import org.apache.log4j.Logger;

import br.gov.caixa.simtx.listener.LeitorFila;
import br.gov.caixa.simtx.vo.Fila;

public class QueueCleaner {
	final static Logger logger = Logger.getLogger(QueueCleaner.class);
	public static void clean(Fila fila) throws Exception {
		if(fila == null) return;
		
		LeitorFila leitor = new LeitorFila(fila, null);
		while(leitor.tentarLerMensagemSemFechar(500L) != null){
			logger.info("Mensagem apagada");
		}
		leitor.fechar();
	}
}
