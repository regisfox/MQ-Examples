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

@MessageDriven(name = "SimtxMDB", activationConfig = {
		@ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue"),
		@ActivationConfigProperty(propertyName = "destination", propertyValue = "java:/jms/req.gatilho_processo"), 
		@ActivationConfigProperty(propertyName = "acknowledgeMode", propertyValue = "auto-acknowledge")})
@ResourceAdapter("hornetq-ra.rar")
@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
public class SimtxMDB implements MessageListener {

	private static final Logger logger = Logger.getLogger(SimtxMDB.class);

	@Override
	public void onMessage(Message message) {
		logger.info("Mensagem recebida do SIMTX");
		TratadorMensagemRecebida tratador = new TratadorMensagemRecebida();
		tratador.tratar(message, "simtx.connection.factory", "simtx.resp");
	}
}
