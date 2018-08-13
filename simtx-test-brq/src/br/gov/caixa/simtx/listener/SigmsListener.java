package br.gov.caixa.simtx.listener;



import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

@Deprecated
public class SigmsListener implements MessageListener {
	
	private static final Logger log = Logger.getLogger( LeitorFila.class.getName() );
	private String mqName = "";
	
	public SigmsListener(String mqName) throws SecurityException, IOException {
		Handler handler = new FileHandler("log-listener.%u.txt");
		handler.setFormatter(new SimpleFormatter());
		log.addHandler(handler);
		
		this.mqName = mqName;
	}
	
	public void onMessage(Message msg) {
		TextMessage tm = (TextMessage) msg;
        try {
        	String line = mqName + ": " + tm.getText();
			log.log(Level.INFO, line);
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}
}

