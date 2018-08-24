package br.gov.caixa.simtx.processador.lote.agendamento.temporizador;

import java.util.Date;
import java.util.List;

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

import br.gov.caixa.simtx.persistencia.agendamento.entidade.Mtxtb034TransacaoAgendamento;
import br.gov.caixa.simtx.persistencia.cache.agendamento.FornecedorDadosAgendamento;
import br.gov.caixa.simtx.persistencia.cache.core.FornecedorDados;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb023Parametro;
import br.gov.caixa.simtx.processador.lote.agendamento.processador.ProcessadorEfetivaAgendamento;

@Singleton
@Startup
public class TemporizadorAgendamento {

	private static final int PAGAMENTO_AGENDADO_PERIODO_INTERVALO_HH_INI_HH_FIM = 3;
	private static final int PAGAMENTO_AGENDADO_PERIODO_INTERVALO_MIN_EXECUCAO = 4;
	private static final int PAGAMENTO_AGENDADO_ULTIMO_PERIODO_HORA = 5;
	private static final int PAGAMENTO_AGENDADO_ULTIMO_PERIODO_MINUTOS = 6;
	private static final int AGENDAMENTO_PERIODO_SEMANA_UTIL = 7;
	private static final int PAGAMENTO_AGENDADO_EXECUCAO_ATIVA = 8;
	private static final String PAGAMENTO_AGENDADO_EXECUCAO_IS_ATIVO = "S";

	private static final Logger logger = Logger.getLogger(TemporizadorAgendamento.class);

	@Resource
	private TimerService timerService;

	@Inject
	private FornecedorDados fornecedorDados;

	@Inject
	private ProcessadorEfetivaAgendamento processadorEfetivaAgendamento;

	@Inject
	private FornecedorDadosAgendamento fornecedorDadosAgendamento;

	public void executar() {
		logger.info("executar");
	}

	@Timeout
	public void timeout(Timer t) {
		Mtxtb023Parametro isAtivo = fornecedorDados.buscarParametroPorPK(PAGAMENTO_AGENDADO_EXECUCAO_ATIVA);
		if (isAtivo != null && PAGAMENTO_AGENDADO_EXECUCAO_IS_ATIVO.equals(isAtivo.getDeConteudoParam())) {
			if (null != t.getInfo()) {
				this.processarExecucaoAgendamentos(t.getInfo().toString());
			}
		} else {
			logger.info(" ======= schedule desativado ======== ");
			cancelarTimers();
		}
	}

	@PostConstruct
	public void initTimer() {
		logger.info("Criando timer Processo Agendamento");
		scheduleCreate();
		logger.info("Timer Processo Agendamento criado");
	}

	private void cancelarTimers() {
		for (Timer timer : timerService.getTimers()) {
			timer.cancel();
		}
	}

	public void setTimerService(TimerService timerService) {
		this.timerService = timerService;
	}

	private void scheduleCreate() {

		Mtxtb023Parametro isAtivo = fornecedorDados.buscarParametroPorPK(PAGAMENTO_AGENDADO_EXECUCAO_ATIVA);

		if (isAtivo != null && PAGAMENTO_AGENDADO_EXECUCAO_IS_ATIVO.equals(isAtivo.getDeConteudoParam())) {
			logger.info("inicio scheduleCreate");
			agendamentoPadrao();
			agendamentoFinal();
			logger.info("termino scheduleCreate");
		} else {
			logger.info(" ======= schedule desativado ======== ");
		}

	}

	/**
	 * Responsavel pela execucao do agendamento via periodo schedule.
	 */
	private void agendamentoPadrao() {
		Mtxtb023Parametro horarioPeriodo = fornecedorDados.buscarParametroPorPK(PAGAMENTO_AGENDADO_PERIODO_INTERVALO_HH_INI_HH_FIM);
		Mtxtb023Parametro intervaloExecucao = fornecedorDados.buscarParametroPorPK(PAGAMENTO_AGENDADO_PERIODO_INTERVALO_MIN_EXECUCAO);
		Mtxtb023Parametro diasExecucao = fornecedorDados.buscarParametroPorPK(AGENDAMENTO_PERIODO_SEMANA_UTIL);

		final TimerConfig agendamentoPadrao = new TimerConfig("agendamentoPadrao", false);

		ScheduleExpression schedule = new ScheduleExpression();

		schedule.dayOfWeek(diasExecucao.getDeConteudoParam()).hour(horarioPeriodo.getDeConteudoParam()).minute(intervaloExecucao.getDeConteudoParam());

		timerService.createCalendarTimer(schedule, agendamentoPadrao);

	}

	/**
	 * Responsavel pelo horario de execucao final
	 */
	private void agendamentoFinal() {
		final TimerConfig agendamentoFinal = new TimerConfig("agendamentoFinal", false);

		ScheduleExpression scheduleFinal = new ScheduleExpression();

		Mtxtb023Parametro horaExecFinal = fornecedorDados.buscarParametroPorPK(PAGAMENTO_AGENDADO_ULTIMO_PERIODO_HORA);
		Mtxtb023Parametro minExecFinal = fornecedorDados.buscarParametroPorPK(PAGAMENTO_AGENDADO_ULTIMO_PERIODO_MINUTOS);
		Mtxtb023Parametro diasExecucao = fornecedorDados.buscarParametroPorPK(AGENDAMENTO_PERIODO_SEMANA_UTIL);

		scheduleFinal.dayOfWeek(diasExecucao.getDeConteudoParam()).hour(horaExecFinal.getDeConteudoParam()).minute(minExecFinal.getDeConteudoParam());

		timerService.createCalendarTimer(scheduleFinal, agendamentoFinal);

	}

	private void processarExecucaoAgendamentos(String tipoAgendamento) {
		logger.info(" ==== Verificando tipo de agendamento ==== ");
		if ("agendamentoPadrao".equals(tipoAgendamento)) {
			logger.info("verificando pagamentos agendados");
			obterInformacoesAgendamentoExecucao(false);
		} else if ("agendamentoFinal".equals(tipoAgendamento)) {
			logger.info("verificando pagamento agendados ultima execucao");
			obterInformacoesAgendamentoExecucao(true);
		}
	}

	private void obterInformacoesAgendamentoExecucao(boolean isFinal) {
		Date date = new Date();
		List<Mtxtb034TransacaoAgendamento> agendamentos = fornecedorDadosAgendamento.buscaTransacoesAgendamentoPorData(date);
		if (null != agendamentos && !agendamentos.isEmpty()) {
			logger.info(" ======= chamando verificarServico ======== ");
			processadorEfetivaAgendamento.verificarServico(agendamentos, isFinal);
		} else {
			logger.info("Nao ha Transacoes Agendadas para o dia");
		}
	}
}