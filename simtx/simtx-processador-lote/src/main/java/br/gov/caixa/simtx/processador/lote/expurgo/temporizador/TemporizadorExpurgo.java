package br.gov.caixa.simtx.processador.lote.expurgo.temporizador;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.ScheduleExpression;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.Timeout;
import javax.ejb.Timer;
import javax.ejb.TimerConfig;
import javax.ejb.TimerService;
import javax.inject.Inject;

import org.apache.log4j.Logger;

import br.gov.caixa.simtx.persistencia.cache.core.FornecedorDados;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb023Parametro;
import br.gov.caixa.simtx.processador.lote.expurgo.processador.ProcessadorExpurgo;


@Singleton
@Startup
public class TemporizadorExpurgo implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private static final Logger logger = Logger.getLogger(TemporizadorExpurgo.class);
	
	private static final int INTERVALO_EXECUCAO_HORA = 11;

	private static final int INTERVALO_EXECUCAO_MINUTO = 12;
	
	@Resource
	private transient TimerService timerService;

	@Inject
	private FornecedorDados fornecedorDados;
	
	@Inject
	private ProcessadorExpurgo processadorExpurgo;
	
	

	@PostConstruct
	public void initTimer() {
		logger.info("Criando timer Processo Expurgo");
		
		Mtxtb023Parametro execucaoHora = this.fornecedorDados.buscarParametroPorPK(INTERVALO_EXECUCAO_HORA);
		Mtxtb023Parametro execucaoMin = this.fornecedorDados.buscarParametroPorPK(INTERVALO_EXECUCAO_MINUTO);

		if(execucaoHora != null) {
			final TimerConfig processoExpurgo = new TimerConfig("timerExpurgo", false);
			ScheduleExpression schedule = new ScheduleExpression();
			schedule.hour(execucaoHora.getDeConteudoParam()).minute(execucaoMin.getDeConteudoParam());
			this.timerService.createCalendarTimer(schedule, processoExpurgo);
			logger.info("Timer Processo Expurgo criado");
		}
		else {
			logger.info("Timer Processo Expurgo nao foi criado. Script nao definido");
		}
	}
	
	@Timeout
	public void timeout(Timer t) {
		if(t.getInfo() != null) {
			logger.info(" ==== Timer Envio Sicco Pendentes Iniciado ====");
			this.processadorExpurgo.processarExpurgoTemporizador();
		}
	}

}
