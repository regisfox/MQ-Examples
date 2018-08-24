/*******************************************************************************
 * Copyright (C)  2017 - CAIXA Econômica Federal 
 * 
 * Todos os direitos reservados
 ******************************************************************************/
package br.gov.caixa.simtx.mensageria.mdb.sibar;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.jms.Message;
import javax.jms.MessageListener;

import org.apache.log4j.Logger;
import org.jboss.ejb3.annotation.ResourceAdapter;


@MessageDriven(name = "InternetBankingMDB", activationConfig = {
		@ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue"),
		@ActivationConfigProperty(propertyName = "useJNDI", propertyValue = "false"),

		@ActivationConfigProperty(propertyName = "hostName", propertyValue = "${simtx.qsda.hostname}"),
		@ActivationConfigProperty(propertyName = "username", propertyValue = "${simtx.qsda.username}"),
		@ActivationConfigProperty(propertyName = "port", propertyValue = "${simtx.qsda.port}"),
		@ActivationConfigProperty(propertyName = "channel", propertyValue = "${simtx.qsda.channel}"),
		@ActivationConfigProperty(propertyName = "queueManager", propertyValue = "${simtx.qsda.queueManager}"),
		@ActivationConfigProperty(propertyName = "destination", propertyValue = "${simtx.internetbanking.req.destination}"),

		@ActivationConfigProperty(propertyName = "transportType", propertyValue = "CLIENT") })

@ResourceAdapter(value = "${sibar.resource_adapter}")
@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
public class InternetBankingMDB implements MessageListener {

	private static final Logger logger = Logger.getLogger(InternetBankingMDB.class);

	@Override
	public void onMessage(Message message) {
		logger.info("Mensagem recebida do canal InternetBanking");
		TratadorMensagemRecebida tratador = new TratadorMensagemRecebida();
		tratador.tratar(message, "internetbanking.connection.factory", "internetbanking.resp");
	}
}
