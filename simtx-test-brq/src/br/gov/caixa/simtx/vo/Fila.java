package br.gov.caixa.simtx.vo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.jms.JMSException;
import javax.jms.Session;

import org.apache.log4j.Logger;

import com.ibm.jms.JMSTextMessage;
import com.ibm.mq.jms.MQQueue;
import com.ibm.mq.jms.MQQueueConnection;
import com.ibm.mq.jms.MQQueueConnectionFactory;
import com.ibm.mq.jms.MQQueueSender;
import com.ibm.mq.jms.MQQueueSession;

import br.gov.caixa.simtx.config.Reader;

public class Fila {
	final static Logger logger = Logger.getLogger(Fila.class);

	private String host;
	private String port;
	private String manager;
	private String channel;
	private String queue;
	private static List<Fila> poolFilasAbertas = new ArrayList<Fila>();

	private Properties config;
	private Properties props = Reader.getProps();

	MQQueueConnectionFactory cf = new MQQueueConnectionFactory();
	MQQueueSender sender;
	MQQueueSession session;
	MQQueueConnection connection;

	private Fila(String host, String port, String manager, String channel, String queue)
			throws JMSException, IOException {
		super();
		this.host = host;
		this.port = port;
		this.manager = manager;
		this.channel = channel;
		this.queue = queue;
	}

	private Fila create() throws NumberFormatException, JMSException, IOException {

		for (Fila f : poolFilasAbertas) {
			if (f.equals(this))
				return f;
		}

		config = Reader.getProps();
		cf.setHostName(host);
		cf.setPort(Integer.valueOf(port));
		cf.setQueueManager(manager);
		cf.setTransportType(new Integer(config.getProperty("sigms.mq.tranport_type")));
		cf.setChannel(channel);

		poolFilasAbertas.add(this);
		return this;
	}

	public void abrir() throws JMSException {
		if (this.sender == null) {
			this.connection = (MQQueueConnection) cf.createQueueConnection();
			this.session = (MQQueueSession) connection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
			MQQueue queue = (MQQueue) session.createQueue(this.queue);
			this.sender = (MQQueueSender) session.createSender(queue);
			logger.info("Fila foi aberta: " + this.queue + " **********");
		}
	}

	public String publicar(String msg) throws JMSException {
		if (this.sender == null || this.session == null)
			throw new RuntimeException("Usar o metodo 'abrir' para abrir uma conexão");

		JMSTextMessage message = (JMSTextMessage) session.createTextMessage(msg);
		sender.send(message);
		return message.getJMSMessageID();
	}

	public void fechar() throws JMSException {
		this.sender.close();
		this.session.close();
		this.connection.close();
		logger.info("Fila foi fechada: " + this.queue + " **********");
	}

	public static void fecharFilas() throws JMSException {
		for (Fila f : poolFilasAbertas) {
			if (f.sender != null)
				f.fechar();
		}
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public String getManager() {
		return manager;
	}

	public void setManager(String manager) {
		this.manager = manager;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public String getQueue() {
		return queue;
	}

	public void setQueue(String queue) {
		this.queue = queue;
	}

	public String getConf(String key) {
		return props.getProperty(key);
	}

	public static Fila getFilaRequisicao(String nomeFila) {
		try {
			return new Fila(Reader.getPropertiesFilaRequisicao(nomeFila, Reader.HOST),
					Reader.getPropertiesFilaRequisicao(nomeFila, Reader.PORT),
					Reader.getPropertiesFilaRequisicao(nomeFila, Reader.QM),
					Reader.getPropertiesFilaRequisicao(nomeFila, Reader.CHANNEL),
					Reader.getPropertiesFilaRequisicao(nomeFila, Reader.DEST)).create();
		} catch (Exception e) {
			logger.error(e);
		}
		return null;
	}

	public static Fila getFilaResposta(String nomeFila) {
		try {
			return new Fila(Reader.getPropertiesFilaResposta(nomeFila, Reader.HOST),
					Reader.getPropertiesFilaResposta(nomeFila, Reader.PORT),
					Reader.getPropertiesFilaResposta(nomeFila, Reader.QM),
					Reader.getPropertiesFilaResposta(nomeFila, Reader.CHANNEL),
					Reader.getPropertiesFilaResposta(nomeFila, Reader.DEST)).create();
		} catch (Exception e) {
			logger.error(e);
		}
		return null;
	}

	private static Fila getFilaExterna(String nomeFila) {
		try {
			return new Fila(Reader.getPropertiesFilaExterna(nomeFila, Reader.HOST),
					Reader.getPropertiesFilaExterna(nomeFila, Reader.PORT),
					Reader.getPropertiesFilaExterna(nomeFila, Reader.QM),
					Reader.getPropertiesFilaExterna(nomeFila, Reader.CHANNEL),
					Reader.getPropertiesFilaExterna(nomeFila, Reader.DEST)).create();
		} catch (Exception e) {
			logger.error(e);
		}
		return null;
	}

	public static Fila getFilaExternaEnviaSmsBroker() {
		return getFilaExterna("enviasmsbroker");
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((channel == null) ? 0 : channel.hashCode());
		result = prime * result + ((host == null) ? 0 : host.hashCode());
		result = prime * result + ((manager == null) ? 0 : manager.hashCode());
		result = prime * result + ((port == null) ? 0 : port.hashCode());
		result = prime * result + ((queue == null) ? 0 : queue.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Fila other = (Fila) obj;
		if (channel == null) {
			if (other.channel != null)
				return false;
		} else if (!channel.equals(other.channel))
			return false;
		if (host == null) {
			if (other.host != null)
				return false;
		} else if (!host.equals(other.host))
			return false;
		if (manager == null) {
			if (other.manager != null)
				return false;
		} else if (!manager.equals(other.manager))
			return false;
		if (port == null) {
			if (other.port != null)
				return false;
		} else if (!port.equals(other.port))
			return false;
		if (queue == null) {
			if (other.queue != null)
				return false;
		} else if (!queue.equals(other.queue))
			return false;
		return true;
	}
}
