package br.gov.caixa.simtx.util.jms;

import java.util.concurrent.TimeoutException;

import javax.ejb.Stateless;
import javax.jms.JMSException;
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

@Stateless
public class ConexaoHQ {

	private static final Logger logger = Logger.getLogger(ConexaoHQ.class);

	private InitialContext initialContext;

	private String connectionFactoryJndi = "java:jboss/SimtxConnectionFactory";
	private String queueNameJndi = "java:/jms/req.gatilho_processo";
	private int timeout = 5000;

	/**
	 * 
	 * @throws Exception
	 * @throws TimeoutException
	 */
	public void enviarMensagem(String mensagem) {
		QueueConnection queueConnection = null;
		QueueSession queueSession = null;
		QueueSender queueSender = null;
		try {
			this.initialContext = createInitialContext();

			QueueConnectionFactory queueConnectionFactory = (QueueConnectionFactory) lookup(connectionFactoryJndi);
			queueConnection = queueConnectionFactory.createQueueConnection();

			Queue toQueue = (Queue) lookup(queueNameJndi);

			queueSession = queueConnection.createQueueSession(false, Session.CLIENT_ACKNOWLEDGE);
			queueConnection.start();
			queueSender = queueSession.createSender(toQueue);

			TextMessage toMessage = queueSession.createTextMessage();
			toMessage.setJMSExpiration(timeout);
			toMessage.setText(mensagem);

			logger.info("Enviando mensagem: \n" + mensagem);
			logger.info("Enviando na fila:" + toQueue.getQueueName());
			logger.info("JMSExpiration: " + toMessage.getJMSExpiration());

			queueSender.send(toMessage);

		} catch (JMSException e) {
			logger.error("Nao foi possivel se comunicar com o Provedor de Servicos", e);
		} catch (NamingException e) {
			logger.error("Nao foi possivel se comunicar com o Provedor de Servicos");
			logger.error("Fila nao definida", e);
		} finally {
			try {
				if (queueSender != null)
					queueSender.close();
				if (queueSession != null)
					queueSession.close();
				if (queueConnection != null) {
					queueConnection.stop();
					queueConnection.close();
				}
			} catch (JMSException e) {
				logger.info("ERRO - Encerrando Conect MQ - " + e.getMessage());
			}
		}
	}

	/**
	 * Lookup.
	 *
	 * @param name
	 *            the name
	 * @return the object
	 * @throws NamingException
	 *             the naming exception
	 */
	protected Object lookup(String name) throws NamingException {
		return initialContext.lookup(name);
	}

	/**
	 * Creates the initial context.
	 *
	 * @return the initial context
	 * @throws NamingException
	 *             the naming exception
	 */
	protected InitialContext createInitialContext() throws NamingException {
		return new InitialContext();
	}

}
