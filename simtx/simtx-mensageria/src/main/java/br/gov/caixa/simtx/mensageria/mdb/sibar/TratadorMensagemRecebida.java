package br.gov.caixa.simtx.mensageria.mdb.sibar;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

import javax.jms.BytesMessage;
import javax.jms.Message;
import javax.jms.TextMessage;
import javax.naming.InitialContext;

import org.apache.log4j.Logger;

import br.gov.caixa.simtx.integrador.IntegradorChamadorNovo;

public class TratadorMensagemRecebida {

    private static final Logger logger = Logger.getLogger(TratadorMensagemRecebida.class);
    
	public void tratar(Message message, String fabricaConexaoJndi, String filaRespostaJndi) {
    	String mensagemCanal = "";
    	String messageId = "";
        try {
        	logger.info("JMSMessageID: "+message.getJMSMessageID());
			logger.info("JMSExpiration: "+message.getJMSExpiration());
			logger.info("JMSTimeStamp: "+message.getJMSTimestamp());
			logger.info("JMS_IBM_PutDate: "+message.getStringProperty("JMS_IBM_PutDate"));
			logger.info("JMS_IBM_PutTime: "+message.getStringProperty("JMS_IBM_PutTime"));
        	
        	messageId = message.getJMSMessageID();
	    	if(message instanceof TextMessage) {
				TextMessage questionMessage = (TextMessage) message;
				mensagemCanal = questionMessage.getText();
			}
			else if(message instanceof BytesMessage) {
				BytesMessage questionMessage = (BytesMessage) message;
				byte[] byteArr = new byte[(int)questionMessage.getBodyLength()];
				questionMessage.readBytes(byteArr);
				mensagemCanal = new String(byteArr, "UTF-8");
			}
        } 
        catch (Exception e) {
            logger.error("Nao foi possivel interpretar a mensagem do canal, tipo invalida", e);
        }
        finally {
        	chamarIntegrador(fabricaConexaoJndi, filaRespostaJndi, mensagemCanal, messageId);
        }
    }

	private void chamarIntegrador(String fabricaConexaoJndi, String filaRespostaJndi, String mensagemCanal,
			String messageId) {
		try {
			final Properties prop = carregarJndi();
			IntegradorChamadorNovo integradorChamador = (IntegradorChamadorNovo) new InitialContext()
					.lookup("java:global/simtx/simtx-integrador/IntegradorChamadorNovo");
			integradorChamador.processar(mensagemCanal, messageId, prop.getProperty(fabricaConexaoJndi),
					prop.getProperty(filaRespostaJndi));
//			integradorChamador.setIdMessage(messageId);
//			integradorChamador.setMensagemCanal(mensagemCanal);
//			integradorChamador.setJndiQueueConnectionFactory(prop.getProperty(fabricaConexaoJndi));
//			integradorChamador.setJndiResponseQueue(prop.getProperty(filaRespostaJndi));
//			integradorChamador.chamar();
		}
		catch(Exception e2) {
			logger.fatal("Nao foi possivel chamar o IntegradorSimtx: "+e2);
		}
	}
    private Properties carregarJndi() {
        final Properties prop = new Properties();
        String dadosProperties = "";
        try {
            String simtxHome = System.getProperty("pasta.simtx.home");
            if (simtxHome == null) {
                logger.info("Variavel pasta.simtx.home nao definida");
            }

            dadosProperties = simtxHome + "/dados.properties";

            final File popFile = new File(dadosProperties);
            final FileInputStream stream = new FileInputStream(popFile);
            prop.load(stream);
            stream.close();
        } 
        catch (Exception e) {
            logger.error("Erro ao Carregar dados.properties", e);
        }
        return prop;
    }
    
}
