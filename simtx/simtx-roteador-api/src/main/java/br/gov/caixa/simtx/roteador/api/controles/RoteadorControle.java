package br.gov.caixa.simtx.roteador.api.controles;

import java.io.Serializable;
import java.util.List;

import javax.ejb.Asynchronous;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.log4j.Logger;

import br.gov.caixa.simtx.agendamento.entidade.MtxCancelamentoAgendamentoWeb;
import br.gov.caixa.simtx.agendamento.processador.ProcessadorCancelamentoAgendamento;
import br.gov.caixa.simtx.agendamento.servico.EfetivaPagamentoAgendadoBoleto;
import br.gov.caixa.simtx.cancelamento.beans.MtxTransacaoContaEntrada;
import br.gov.caixa.simtx.cancelamento.processador.ProcessadorCancelamentoPagamento;
import br.gov.caixa.simtx.roteador.api.vo.AgendamentoServico;
import br.gov.caixa.simtx.roteador.api.ws.ProcessadorConsultaRegrasBoleto;
import br.gov.caixa.simtx.roteador.api.ws.ProcessadorValidaRegrasBoleto;


@Path("/controle")
public class RoteadorControle implements Serializable {
	
	private static final Logger logger = Logger.getLogger(RoteadorControle.class);
	
	private static final long serialVersionUID = -7419073614570215622L;

	private static final String TESTE_SUCESSO = "Teste realizado com sucesso";

	@Inject
	private ProcessadorConsultaRegrasBoleto consultaRegrasBoletoControle;
	
	@Inject
	private ProcessadorValidaRegrasBoleto processadorValidaRegrasBoleto;
	
	@Inject
	private ProcessadorCancelamentoAgendamento processadorCancelamentoAgendamento;
	
	@Inject
	private ProcessadorCancelamentoPagamento processaCancelamentoPagamento;
	
	@Inject
	private EfetivaPagamentoAgendadoBoleto efetivaPagamentoAgendadoBoleto;
	
	
	@POST
	@Path("/teste")
	public Response testeEndPoint() {
		return Response.ok(TESTE_SUCESSO).build();
	}
	
	@POST
	@Path("/boleto/regras/consulta")
	@Produces(MediaType.TEXT_XML)
	@Consumes(MediaType.TEXT_XML)
	public String processarConsultaRegrasBoleto(String xmlEntrada) {
		return this.consultaRegrasBoletoControle.processar(xmlEntrada);
	}
	
	@POST
	@Path("/boleto/regras/valida")
	@Produces(MediaType.TEXT_XML)
	@Consumes(MediaType.TEXT_XML)
	public String processarValidaRegrasBoleto(String xmlEntrada) {
		return this.processadorValidaRegrasBoleto.processar(xmlEntrada);
	}
	
	@POST
	@Path("/cancelamento/agendamento")
	@Produces(MediaType.APPLICATION_JSON)
	public Response efetuaCancelamentoAgendamento(@Context HttpServletRequest httpRequest, 
			List<MtxCancelamentoAgendamentoWeb> listaCancelamento) {
		return Response.ok().entity(this.processadorCancelamentoAgendamento.processar(listaCancelamento)).build();
	}
	
	@POST
	@Path("/cancelamento/pagamento")
	@Produces(MediaType.APPLICATION_JSON)
	public Response efetuaCancelamentoPagamento(@Context HttpServletRequest httpRequest,
			List<MtxTransacaoContaEntrada> listaCancelamento) {
		return Response.ok().entity(this.processaCancelamentoPagamento.realizarCancelamentos(listaCancelamento)).build();
	}
	
	@POST
	@Path("/efetivaAgendamento/realizaEnvio")
	@Produces(MediaType.APPLICATION_JSON)
	public Response realizaEnvio(AgendamentoServico agendamentoServico) {
		logger.info("realizando envio");
		if (null != agendamentoServico) {
			this.efetivaPagamentoAgendadoBoleto.executarServico(agendamentoServico.getNsuTransacaoOrigem(), agendamentoServico.getNumServicoFinal(),
					agendamentoServico.getNumServicoVersaoFinal(), agendamentoServico.isUltimaExecucao());
		}
		return Response.ok(Status.OK).build();
	}

	@POST
	@Path("/efetivaAgendamento/realizaEnvioAsynchronous")
	@Asynchronous
	public Response realizaEnvioAsynchronous(AgendamentoServico agendamentoServico) {
		if (null != agendamentoServico) {
			this.efetivaPagamentoAgendadoBoleto.executarServico(agendamentoServico.getNsuTransacaoOrigem(), agendamentoServico.getNumServicoFinal(),
					agendamentoServico.getNumServicoVersaoFinal(), agendamentoServico.isUltimaExecucao());
		}
		return Response.ok(Status.OK).build();
	}
	
	@POST
	@Path("/inicioManual")
	@Produces(MediaType.TEXT_HTML)
	public Response agendamentoManual() {
		logger.info("endPointAgendamentoManual");
		this.efetivaPagamentoAgendadoBoleto.iniciar(false);
		return Response.ok(TESTE_SUCESSO).build();
	}
	
	@POST
	@Path("/ultimaExecucaoManual")
	@Produces(MediaType.TEXT_HTML)
	public Response agendamentoFinalManual() {
		logger.info("endPointAgendamentoUltimaExecucaoManual");
		this.efetivaPagamentoAgendadoBoleto.iniciar(true);
		return Response.ok(TESTE_SUCESSO).build();
	}

}
