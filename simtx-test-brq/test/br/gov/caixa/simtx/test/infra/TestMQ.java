package br.gov.caixa.simtx.test.infra;

import java.io.StringReader;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.jms.JMSException;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.lang3.StringUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;

import br.gov.caixa.simtx.config.Reader;
import br.gov.caixa.simtx.dominio.Convertable;
import br.gov.caixa.simtx.listener.LeitorFila;
import br.gov.caixa.simtx.mq.QueueCleaner;
import br.gov.caixa.simtx.vo.Fila;

public class TestMQ extends TestSigms {
	private static final String LIMPARFILAS = "sigms.rft.limparfilas";
	private static final long TEMPO_ESPERA_LEITURA_FILA = Reader.getPropertiesTimeoutLeituraFila();
	public static final String RETORNO = "ret";
	public static final String ENVIO = "env";

	@BeforeClass
	public static void limparFilas() {
		String limpar = (String) Reader.getProp(LIMPARFILAS);
		if(limpar.equals("false"))
			return;
		
		if(!limpar.equals("true"))
			return;
		
		logger.info("Limpando filas");
		Method[] metodos = Fila.class.getMethods();
		List<Method> metodosGetFila = new ArrayList<Method>();

		for (Method metodo : metodos) {
			String nome = metodo.getName();
			if (nome.contains("getFila")) {
				metodosGetFila.add(metodo);
			}
		}

		for (Method getFila : metodosGetFila) {
			try {
				Fila fila = (Fila) getFila.invoke(null, (Object)null);
				limparFila(fila);
				logger.info(fila.getQueue() + " foi limpado");
			} catch (Exception e) {
				logger.info("Erro ao invocar metodo: " + getFila);
				logger.info(e.getMessage());
			}
		}

	}

	private static void limparFila(Fila fila) {
		try {
			QueueCleaner.clean(fila);
		} catch (Exception e) {
			logger.info("Algo aconteceu ao limpar fila: " + e.getMessage());
		}
	}

	@AfterClass
	public static void fecharFilas() throws JMSException {
		Fila.fecharFilas();
	}

	public Date enviar(Fila fila, String msg) throws JMSException {
		fila.abrir();
		logger.info("enviando msg: ");
		logger.info(msg);
		fila.publicar(msg);
		return new Date();
	}
	
	public String enviarGetId(Fila fila, String msg) throws JMSException {
		fila.abrir();
		logger.info("enviando msg: ");
		logger.info(msg);
		return fila.publicar(msg);
	}
	
	public Date enviar(Fila fila, Convertable msg) throws JMSException {
		fila.abrir();
		String msgStr = msg.converter();
		logger.info("enviando msg: ");
		logger.info(msgStr);
		fila.publicar(msgStr);
		return new Date();
	}

	public String ler(Fila fila, String id) throws Exception {
		return ler(TEMPO_ESPERA_LEITURA_FILA, fila, id);
	}

	public <T> T lerXml(T clazz, Fila fila, String id) throws Exception {
		return lerXml(TEMPO_ESPERA_LEITURA_FILA, clazz, fila);
	}

	public String ler(Long tempoEspera, Fila fila, String id) throws Exception {
		logger.info("Aguartando retorno de mensagem na fila");
		String ret = new LeitorFila(fila, id).tentarLerMensagem(tempoEspera);
		if (StringUtils.isEmpty(ret)) {
			logger.info("Mensagem nula retornada.");
			ret = "";
		} else {
			logger.info("Mensagem retornada: ");
			logger.info(ret);

		}

		return ret;
	}
	
	public String ler(Long tempoEspera, Fila fila) throws Exception {
		logger.info("Aguartando retorno de mensagem na fila");
		String ret = new LeitorFila(fila, null).tentarLerMensagem(tempoEspera);
		if (StringUtils.isEmpty(ret)) {
			logger.info("Mensagem nula retornada.");
			ret = "";
		} else {
			logger.info("Mensagem retornada: " + ret);
		}

		return ret;
	}

	@SuppressWarnings("unchecked")
    public <T> T lerXml(Long tempoEspera, T clazz, Fila fila) throws Exception {
		logger.info("Aguartando retorno de mensagem na fila");
		String retorno = new LeitorFila(fila, null).tentarLerMensagem(tempoEspera);

		if (StringUtils.isEmpty(retorno)) {
			logger.info("Mensagem nula retornada.");
			return null;
		} else {
			JAXBContext jaxbContext = JAXBContext.newInstance(clazz.getClass());
			Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
			logger.info("Mensagem retornada: ");
			logger.info(retorno);
			return (T) unmarshaller.unmarshal(new StringReader(retorno));
		}
	}

	public void aguardar(Long time) {
		try {
			Thread.sleep(time);
		} catch (Exception e) {

		}
	}
	
	public void naoLimparFilas() {
		
	}
}
