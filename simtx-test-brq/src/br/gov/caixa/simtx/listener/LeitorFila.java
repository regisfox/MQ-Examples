package br.gov.caixa.simtx.listener;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.jms.TextMessage;

import br.gov.caixa.simtx.vo.Fila;

import com.ibm.mq.jms.MQDestination;
import com.ibm.mq.jms.MQQueue;
import com.ibm.mq.jms.MQQueueConnection;
import com.ibm.mq.jms.MQQueueConnectionFactory;
import com.ibm.mq.jms.MQQueueSession;

public class LeitorFila {
	private MQQueueConnection connection;
	private MQQueueSession session;
	private MQDestination queue;
	MessageConsumer consumer;

	public LeitorFila(Fila fila, String id) throws Exception {
		MQQueueConnectionFactory cf = new MQQueueConnectionFactory();
		//Properties config = Reader.getProps();

		cf.setHostName(fila.getHost());
		cf.setPort(new Integer(fila.getPort()));
		cf.setQueueManager(fila.getManager());
		cf.setTransportType(new Integer("1"));
		cf.setChannel(fila.getChannel());
		
		System.out.println("host: " + fila.getHost());

//		System.setProperty("user.name", "brqmq");
//		System.setProperty("user.name", "SMTXSD01");

		this.connection = (MQQueueConnection) cf.createQueueConnection();
		this.session = (MQQueueSession) connection.createQueueSession(false,
				Session.AUTO_ACKNOWLEDGE);
		this.queue = (MQQueue) session.createQueue(fila.getQueue());
		
		if(id == null) {
			this.consumer = session.createConsumer(queue);
		}
		else {
			String messageSelector = "JMSCorrelationID = '" + id + "'";
			this.consumer = session.createConsumer(queue, messageSelector);
		}
	}

	public String tentarLerMensagem(Long tempoEspera) throws Exception {
		connection.start();
		
		Message msg = consumer.receive(tempoEspera);
		connection.stop();
		connection.close();

		if (msg == null)
			return null;

		return ((TextMessage) msg).getText();
	}

	public String tentarLerMensagemSemFechar(Long tempoEspera) throws Exception {
		connection.start();
		Message msg = consumer.receive(tempoEspera);
		connection.stop();

		if (msg == null)
			return null;

		return ((TextMessage) msg).getText();
	}

	public void fechar() throws JMSException {
		connection.close();
	}

	public Message ler() throws Exception {
		MessageConsumer consumer = session.createConsumer(queue);

		connection.start();
		Message msg = consumer.receive();
		connection.close();

		return msg;
	}

	public void destory() {
		try {
			if (connection != null)
				connection.close();
		} catch (Exception e) {
		}

	}
}