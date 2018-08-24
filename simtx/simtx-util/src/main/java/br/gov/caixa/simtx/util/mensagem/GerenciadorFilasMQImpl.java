/*******************************************************************************
 * Copyright (C)  2017 - CAIXA Econômica Federal 
 * 
 * Todos os direitos reservados
 ******************************************************************************/
package br.gov.caixa.simtx.util.mensagem;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.concurrent.TimeoutException;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.jms.JMSException;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueReceiver;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.log4j.Logger;

import br.gov.caixa.simtx.persistencia.cache.core.FornecedorDados;
import br.gov.caixa.simtx.persistencia.constante.Constantes;
import br.gov.caixa.simtx.persistencia.constante.MensagemRetorno;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb001Servico;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb006Mensagem;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb024TarefaFila;
import br.gov.caixa.simtx.util.exception.ServicoException;

/**
 * Classe responsavel pela comunicacao com as filas MQ.
 * 
 * @author rctoscano
 *
 */
@Stateless
public class GerenciadorFilasMQImpl implements GerenciadorFilasMQ {

    private static final Logger logger = Logger.getLogger(GerenciadorFilasMQImpl.class);
    
    private InitialContext initialContext;
    
    @Inject
    private FornecedorDados fornecedorDados;
    

    public String executar(String mensagem, Mtxtb001Servico servico) throws ServicoException  {
    	PropriedadesMQ propriedadesMQ = new PropriedadesMQ();
    	propriedadesMQ.setTimeout(servico.getTempoLimiteEsperaResposta()*10);
    	propriedadesMQ.setConnectionFactory(servico.getNoConexao());
    	propriedadesMQ.setFilaRequisicao(servico.getNoFilaRequisicao());
    	propriedadesMQ.setFilaResposta(servico.getNoFilaResposta());
    	return executarServico(mensagem, propriedadesMQ);
    }
    
    public String executar(String mensagem, Mtxtb024TarefaFila tarefa) throws ServicoException {
    	PropriedadesMQ propriedadesMQ = new PropriedadesMQ();
    	propriedadesMQ.setTimeout(tarefa.getNuTimeoutRequisicao()*10);
    	propriedadesMQ.setConnectionFactory(tarefa.getNoConnectionFactory());
    	propriedadesMQ.setFilaRequisicao(tarefa.getNoFilaRequisicao());
    	propriedadesMQ.setFilaResposta(tarefa.getNoFilaResposta());
    	return executarServico(mensagem, propriedadesMQ);
    }
    
    public String executarSicco(String mensagem) throws ServicoException {
    	PropriedadesMQ propriedadesMQ = new PropriedadesMQ();
    	propriedadesMQ.setTimeout(Constantes.CORE_TIMEOUT);
    	propriedadesMQ.setConnectionFactory(Constantes.STRING_CONNECTION_FACTORY_SIBAR);
    	propriedadesMQ.setFilaRequisicao(Constantes.STRING_SIBAR_REQ_CCO);
    	propriedadesMQ.setFilaResposta(Constantes.STRING_SIBAR_RSP_CCO);
    	return executarServico(mensagem, propriedadesMQ);
    }
    
    /**
     * Envia e recebe a mensagem.
     * @throws Exception 
     * @throws TimeoutException 
     */
    public String executarServico(String mensagem, PropriedadesMQ propriedadesMQ) throws ServicoException {
    	QueueConnection queueConnection = null;
        QueueSession queueSession = null;
        QueueSender queueSender = null;
        QueueReceiver queueReceiver = null;
        try {
            this.initialContext = createInitialContext();

            QueueConnectionFactory queueConnectionFactory = (QueueConnectionFactory) lookup(propriedadesMQ.getConnectionFactory());
            queueConnection = queueConnectionFactory.createQueueConnection();

            Queue toQueue = (Queue) lookup(propriedadesMQ.getFilaRequisicao());
            
            queueSession = queueConnection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
            queueConnection.start();
            queueSender = queueSession.createSender(toQueue);
            
            TextMessage toMessage = queueSession.createTextMessage();
            toMessage.setStringProperty("JMS_IBM_Character_Set", "819");
            toMessage.setStringProperty("JMS_IBM_Format", "MQSTR");
            toMessage.setText(mensagem);

            logger.info("Enviando mensagem: \n"+mensagem);
            logger.info("Enviando na fila:" + toQueue.getQueueName());
			
            queueSender.setTimeToLive(5000);
            queueSender.send(toMessage);

            return recebeMensagem(propriedadesMQ, queueReceiver, toMessage, queueSession);
        } 
        catch (JMSException e) {
            logger.error("Nao foi possivel se comunicar com o Provedor de Servicos", e);
            Mtxtb006Mensagem mensagemErro = fornecedorDados.buscarMensagem(MensagemRetorno.ERRO_INTERNO);
        	throw new ServicoException(mensagemErro, Constantes.ORIGEM_SIMTX);
        }
        catch (NamingException e) {
        	logger.error("Nao foi possivel se comunicar com o Provedor de Servicos");
        	logger.error("Fila nao definida", e);
        	Mtxtb006Mensagem mensagemErro = fornecedorDados.buscarMensagem(MensagemRetorno.ERRO_INTERNO);
        	throw new ServicoException(mensagemErro, Constantes.ORIGEM_SIMTX);
        }
        catch (UnsupportedEncodingException e) {
        	logger.error("Erro encoding mensagem", e);
        	Mtxtb006Mensagem mensagemErro = fornecedorDados.buscarMensagem(MensagemRetorno.ERRO_INTERNO);
        	throw new ServicoException(mensagemErro, Constantes.ORIGEM_SIMTX);
        }
        finally {
            try {
                if (queueSender != null)
                    queueSender.close();
                if (queueSession != null)
                    queueSession.close();
                if (queueConnection != null) {
                	queueConnection.stop();
                    queueConnection.close();
                }
            }
            catch (JMSException e) {
                logger.info("ERRO - Encerrando Conect MQ - "+e.getMessage());
            }
        }
    }
    
	public String recebeMensagem(PropriedadesMQ propriedadesMQ, QueueReceiver queueReceiver, TextMessage toMessage,
			QueueSession queueSession) throws JMSException, NamingException, UnsupportedEncodingException, ServicoException {
		String mensagemRecebida = null;
		try {
	        
	        Serializable outQueue = propriedadesMQ.getFilaResposta();
	        if(outQueue!=null){
	        	String jndiResp = outQueue.toString();
	        	Queue fromQueue = (Queue) lookup(jndiResp);
	        	long timeout = propriedadesMQ.getTimeout();
	        	            	
	        	String messageSelector = "JMSCorrelationID = '" + toMessage.getJMSMessageID() + "'";
	            queueReceiver = queueSession.createReceiver(fromQueue, messageSelector);
	            
	            logger.info("Aguardando resposta na fila:" + fromQueue.getQueueName());
	            TextMessage receivedMessage = (TextMessage) queueReceiver.receive(timeout);
	            if (receivedMessage != null && receivedMessage.getText() != null) {
	            	mensagemRecebida = new String(receivedMessage.getText().getBytes("UTF-8"));
	            	logger.info("Mensagem recebida: \n"+mensagemRecebida);
	            	mensagemRecebida = mensagemRecebida.replaceAll("<\\?xml.*\\?>", "");
	            }
	            else {
	            	Mtxtb006Mensagem mensagem = fornecedorDados.buscarMensagem(MensagemRetorno.TIMEOUT);
	            	throw new ServicoException(mensagem, Constantes.ORIGEM_SIMTX);
	            }
	        }
		} 
		catch (JMSException | NamingException | UnsupportedEncodingException e) {
			logger.error(e);
			throw e;
		}
        finally {
        	if (queueReceiver != null)
                queueReceiver.close();
		}
		return mensagemRecebida;
    }

    /**
     * Lookup.
     *
     * @param name the name
     * @return the object
     * @throws NamingException the naming exception
     */
    protected Object lookup(String name) throws NamingException {
        return initialContext.lookup(name);
    }

    /**
     * Creates the initial context.
     *
     * @return the initial context
     * @throws NamingException the naming exception
     */
    protected InitialContext createInitialContext() throws NamingException {
        return new InitialContext();
    }

}
