/*******************************************************************************
 * Copyright (C)  2017 - CAIXA Econômica Federal 
 * 
 * Todos os direitos reservados
 ******************************************************************************/
package br.gov.caixa.simtx.util.mensagem;

import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.log4j.Logger;

import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb004Canal;
import br.gov.caixa.simtx.util.xml.JsonConversor;

public class FilaCanal {

    private static final Logger logger = Logger.getLogger(FilaCanal.class);

    protected QueueConnection qCon = null;

    protected QueueSession qSession = null;

    protected String txtResponse = "";

    protected String msg = "";

    protected String jndiQueueConnectionFactory;

    protected String jndiResponseQueue;

    protected String connectionFactory;

    protected String jndiRspCanal;

    protected String messageID = "";

    public void postarMensagem(String idMessage, String mensagem, Mtxtb004Canal canal) {
    	postarMensagem(idMessage, mensagem, canal, false);
    }

    /**
     * Posta a mensagem na fila do Canal.
     * 
     * @param mensagem
     * @param canal
     */
    public void postarMensagem(String idMessage, String mensagem, Mtxtb004Canal canal, boolean converterParaJson) {
		mensagem = mensagem.trim();
	
		if (converterParaJson) {
		    mensagem = JsonConversor.toJSON(mensagem);
		    mensagem = mensagem.trim();
		}
	
		logger.info("Enviando resposta para o canal: \n" + mensagem);
		QueueSender qSender = null;
		String fila = "";
	
		try {
		    this.messageID = idMessage;
		    InitialContext context = createInitialContext();
		    Queue toQueue = (Queue) context.lookup(canal.getNoFilaRspCanal());
		    fila = toQueue.getQueueName();
	
		    QueueConnectionFactory qFactory;
		    qFactory = (QueueConnectionFactory) context.lookup(canal.getNoConexaoCanal());
		    this.qCon = qFactory.createQueueConnection();
		    this.qSession = this.qCon.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
		    this.qCon.start();
	
		    qSender = this.qSession.createSender(toQueue);
		    TextMessage txtMessage = this.qSession.createTextMessage();
		    txtMessage.setStringProperty("JMS_IBM_Character_Set", "819");
		    txtMessage.setText(mensagem);
		    txtMessage.setJMSCorrelationID(this.messageID);
		    
		    logger.info("Enviando na fila " + toQueue.getQueueName());
		    logger.info("JMSMessageID: "+txtMessage.getJMSMessageID());
		    
		    qSender.setTimeToLive(5000);
		    qSender.send(txtMessage);
		} 
		catch (Exception e) {
		    logger.error("Erro ao tentar enviar resposta para o canal na fila " + fila + ": \n" + e);
		    logger.error("Causa: " + e.getCause());
		} 
		finally {
		    try {
			if (this.qSession != null)
			    this.qSession.close();
			if (this.qCon != null)
			    this.qCon.close();
			if (qSender != null)
			    qSender.close();
		    } 
		    catch (Exception e2) {
		    	logger.error(e2);
		    }
		}
    }

    /**
     * InitialContext.
     * 
     * @return
     * @throws NamingException
     */
    protected InitialContext createInitialContext() throws NamingException {
    	return new InitialContext();
    }

}
