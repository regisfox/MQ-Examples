///*******************************************************************************
// * Copyright (C)  2017 - CAIXA Econômica Federal 
// * 
// * Todos os direitos reservados
// ******************************************************************************/
//package br.gov.caixa.simtx.mensageria.mdb.sibar;
//
//import java.io.File;
//import java.io.FileInputStream;
//import java.util.Properties;
//
//import javax.ejb.ActivationConfigProperty;
//import javax.ejb.MessageDriven;
//import javax.ejb.TransactionAttribute;
//import javax.ejb.TransactionAttributeType;
//import javax.jms.BytesMessage;
//import javax.jms.Message;
//import javax.jms.MessageListener;
//import javax.jms.TextMessage;
//import javax.naming.InitialContext;
//
//import org.apache.log4j.Logger;
//import org.jboss.ejb3.annotation.ResourceAdapter;
//
//import br.gov.caixa.simtx.integrador.IntegradorChamador;
//
///**
// * The Class GenericaMDB.
// */
//@MessageDriven(name = "AutoAtendimentoWebMDB", activationConfig = {
//		@ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue"),
//		@ActivationConfigProperty(propertyName = "useJNDI", propertyValue = "false"),
//
//		@ActivationConfigProperty(propertyName = "hostName", propertyValue = "${simtx.qsda.hostname}"),
//		@ActivationConfigProperty(propertyName = "username", propertyValue = "${simtx.qsda.username}"),
//		@ActivationConfigProperty(propertyName = "port", propertyValue = "${simtx.qsda.port}"),
//		@ActivationConfigProperty(propertyName = "channel", propertyValue = "${simtx.qsda.channel}"),
//		@ActivationConfigProperty(propertyName = "queueManager", propertyValue = "${simtx.qsda.queueManager}"),
//		@ActivationConfigProperty(propertyName = "destination", propertyValue = "${simtx.autoatendimentoweb.req.destination}"),
//
//		@ActivationConfigProperty(propertyName = "transportType", propertyValue = "CLIENT") })
//
//@ResourceAdapter(value = "${sibar.resource_adapter}")
//@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
//public class AutoAtendimentoWebMDB implements MessageListener {
//
//	private static final Logger logger = Logger.getLogger(AutoAtendimentoWebMDB.class);
//
//	@Override
//	public void onMessage(Message message) {
//		logger.info("Mensagem recebida do canal AutoAtendimentoWeb");
//		TratadorMensagemRecebida tratador = new TratadorMensagemRecebida();
//		tratador.tratar(message, "autoatendimentoweb.connection.factory", "autoatendimentoweb.resp");
//	}
//}
