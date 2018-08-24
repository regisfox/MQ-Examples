package br.gov.caixa.simtx.agendamento.servico;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.ejb.Asynchronous;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import org.apache.log4j.Logger;

import br.gov.caixa.simtx.agendamento.enuns.ServicoAgendamentoEnum;
import br.gov.caixa.simtx.agendamento.main.Agendamento;
import br.gov.caixa.simtx.agendamento.util.AgendamentoException;
import br.gov.caixa.simtx.persistencia.agendamento.entidade.Mtxtb034TransacaoAgendamento;
import br.gov.caixa.simtx.persistencia.atualizador.dados.AtualizadorDadosTransacao;
import br.gov.caixa.simtx.persistencia.cache.agendamento.FornecedorDadosAgendamento;
import br.gov.caixa.simtx.persistencia.constante.Constantes;
import br.gov.caixa.simtx.persistencia.constante.MensagemRetorno;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb003ServicoTarefa;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb004Canal;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb006Mensagem;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb011VersaoServico;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb014Transacao;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb016IteracaoCanal;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb035TransacaoConta;
import br.gov.caixa.simtx.util.ConstantesAgendamento;
import br.gov.caixa.simtx.util.ParametrosAdicionais;
import br.gov.caixa.simtx.util.SimtxConfig;
import br.gov.caixa.simtx.util.exception.ServicoException;
import br.gov.caixa.simtx.util.gerenciador.GerenciadorTransacao;
import br.gov.caixa.simtx.util.gerenciador.TratadorDeExcecao;
import br.gov.caixa.simtx.util.gerenciador.ValidadorRegrasNegocio;
import br.gov.caixa.simtx.util.integrador.sicco.ProcessadorEnvioSicco;
import br.gov.caixa.simtx.util.mensagem.GerenciadorFilasMQ;
import br.gov.caixa.simtx.util.to.DadosTransacaoComuns;
import br.gov.caixa.simtx.util.to.TarefasServicoResposta;
import br.gov.caixa.simtx.util.xml.BuscadorTextoXml;
import br.gov.caixa.simtx.util.xml.DadosBarramento;
import br.gov.caixa.simtx.util.xml.GeradorPassosMigrado;
import br.gov.caixa.simtx.util.xml.Resposta;


@Stateless	
public class EfetivaPagamentoAgendadoBoleto extends GerenciadorTransacao implements Serializable {
		
	private static final long serialVersionUID = -7709486899886620960L;

	private static final Logger logger = Logger.getLogger(EfetivaPagamentoAgendadoBoleto.class);
	
	protected DadosBarramento dadosBarramento;
	
	@Inject
	protected Agendamento agendamento;
	
	@Inject
	protected GerenciadorFilasMQ execucaoMq;
	
	@Inject
	protected GeradorPassosMigrado geradorPassosMigrado;
	
	@Inject
	protected ProcessadorEnvioSicco processadorEnvioSicco;
	
	@Inject
	protected TratadorDeExcecao tratadorDeExcecao;
	
	@Inject
	protected ValidadorRegrasNegocio validadorRegrasNegocio;
	
	@Inject
	protected SimtxConfig simtxConfig;
	
	@Inject
	protected FornecedorDadosAgendamento fornecedorDadosAgendamento;
	
	@Inject
	private AtualizadorDadosTransacao atualizadorDadosTransacao;
	
	/**
	 * Inicio do processo para Efetivacao do Agendamento de Boleto. 
	 * 
	 * @param transacaoAgendamento
	 * @param servicoAgendamentoEnum
	 * @param isUltimaExecucao
	 */
	@Asynchronous
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void executarServico(Long nsuTransacaoOrigem, Long numServicoFinal, Integer numServicoVersaoFinal, boolean isUltimaExecucao) {
		
		Mtxtb034TransacaoAgendamento transacaoAgendamento = new Mtxtb034TransacaoAgendamento();
		transacaoAgendamento.setNuNsuTransacaoAgendamento(nsuTransacaoOrigem);
		transacaoAgendamento = fornecedorDadosAgendamento.buscaTransacaoAgendamentoPorPK(transacaoAgendamento);
		
		ParametrosAdicionais parametrosAdicionais = new ParametrosAdicionais();
		
		if (null != transacaoAgendamento && ConstantesAgendamento.SITUACAO_AGENDADO.intValue() == transacaoAgendamento.getIcSituacao()) {
			
			logger.info("Executando Agendamento Nsu ["+transacaoAgendamento.getNuNsuTransacaoAgendamento()+"]");
			
			Mtxtb014Transacao transacao = null;
			Mtxtb016IteracaoCanal iteracaoCanal = null;
			Mtxtb011VersaoServico servicoOrigem = null;
			Mtxtb004Canal canal = null;
			
			try {
				servicoOrigem = buscarServico(numServicoFinal, numServicoVersaoFinal);
				
				this.dadosBarramento = new DadosBarramento();
				this.dadosBarramento.escrever(transacaoAgendamento.getDeXmlAgendamento());
				BuscadorTextoXml buscador = new BuscadorTextoXml(transacaoAgendamento.getDeXmlAgendamento());
				montarDadosXmlInformacoesAgendamento(transacaoAgendamento, isUltimaExecucao);
				canal = buscarCanalSIMTX();
				transacao = this.agendamento.salvarTransacao(transacaoAgendamento);
				iteracaoCanal = salvarIteracaoCanal(buscador, transacaoAgendamento.getDeXmlAgendamento(), transacao);
				salvarTransacaoServico(transacao, servicoOrigem);
				
				processarEntradaSaida(canal, servicoOrigem, transacao, iteracaoCanal, transacaoAgendamento, isUltimaExecucao);
			
			} catch (ServicoException se) {
				logger.error(se.getMensagem());
				parametrosAdicionais.setDadosBarramento(dadosBarramento);
				DadosTransacaoComuns dadosTransacaoComuns = new DadosTransacaoComuns(servicoOrigem, transacao,iteracaoCanal);
				this.tratadorDeExcecao.tratarExcecaoRetomada(dadosTransacaoComuns, se, parametrosAdicionais, true);		
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
				Mtxtb006Mensagem mensagemErroInterno = this.fornecedorDados.buscarMensagem(MensagemRetorno.ERRO_INTERNO);
				ServicoException se = new ServicoException(mensagemErroInterno, Constantes.ORIGEM_SIMTX);
				parametrosAdicionais.setDadosBarramento(dadosBarramento);
				DadosTransacaoComuns dadosTransacaoComuns = new DadosTransacaoComuns(servicoOrigem, transacao,iteracaoCanal);
				this.tratadorDeExcecao.tratarExcecaoRetomada(dadosTransacaoComuns, se, parametrosAdicionais, true);		
			} finally {
				logger.info(" ==== finalizando processamento da mensagem ==== ");
				logger.info(" ==== Processo Core Finalizado ==== ");
			}
			
		} else {
				logger.info("Agendamento Nsu ["+ nsuTransacaoOrigem + "] sem resultado");	
		}
	}
	
	private void processarEntradaSaida(Mtxtb004Canal canal, Mtxtb011VersaoServico servicoOrigem, Mtxtb014Transacao transacao, Mtxtb016IteracaoCanal iteracaoCanal, Mtxtb034TransacaoAgendamento transacaoAgendamento, boolean isUltimaExecucao) {
		String xmlSaidaSibar = null;
		try {
			List<Mtxtb003ServicoTarefa> listaTarefas = this.gerenciadorTarefas.carregarTarefasServico(servicoOrigem, canal, null);
			
			logger.info("Preparando xml para enviar ao Sibar");
			
			String xmlEntradaSibar = this.gerenciadorTarefas.transformarTransacaoAgendamentoXml(servicoOrigem.getDeXsltRequisicao(), this.dadosBarramento, transacao, isUltimaExecucao, false);
			xmlEntradaSibar = this.geradorPassosMigrado.gerarPassos(xmlEntradaSibar, servicoOrigem, listaTarefas);
			
			this.dadosBarramento.escrever(xmlEntradaSibar);
			
			xmlSaidaSibar = this.execucaoMq.executar(xmlEntradaSibar, servicoOrigem.getMtxtb001Servico());
			this.dadosBarramento.escrever(xmlSaidaSibar);
			
			TarefasServicoResposta tarefasServicoResposta = new TarefasServicoResposta();
			
			if(isUltimaExecucao) {
				tarefasServicoResposta = this.gerenciadorTarefas.processarUltimaExecucao(servicoOrigem, transacaoAgendamento, transacao, listaTarefas, dadosBarramento);
			}
			else {
				this.gerenciadorTarefas.montarTarefasResposta(listaTarefas, servicoOrigem, transacao, this.dadosBarramento, xmlSaidaSibar);
			}
			
			String xmlSaidaCanal = this.agendamento.transformarXml(servicoOrigem.getDeXsltResposta(), this.dadosBarramento, transacaoAgendamento, canal, tarefasServicoResposta.getResposta());
			Resposta resposta = gerenciadorTarefas.validarCodigoImpeditivoAgendamento(transacao, listaTarefas, this.dadosBarramento, xmlSaidaSibar, isUltimaExecucao);
			
			if (null!=resposta) {
				tarefasServicoResposta.setResposta(resposta);
			}
			
			BigDecimal situacaoTransacao = this.validadorRegrasNegocio.situacaoFinalTransacao(servicoOrigem);
			transacao = atualizaStatusTransacao(transacao, situacaoTransacao);
			iteracaoCanal = montaMtxtb016IteracaoCanalTansacao(iteracaoCanal, transacao, xmlSaidaCanal);
			Mtxtb035TransacaoConta transacaoConta = montarDadosTransacaoConta(situacaoTransacao, transacao, iteracaoCanal, canal, servicoOrigem, xmlSaidaCanal);
			transacaoAgendamento.setIcSituacao(ConstantesAgendamento.SITUACAO_EFETIVADO);
			boolean statusAtualizacaoTransacao = atualizadorDadosTransacao.atualizarDadosTransacaoEfetivaPagamentoAgendado(transacao, tarefasServicoResposta.getListaTransacaoTarefas(), iteracaoCanal, transacaoAgendamento, transacaoConta);
		
			direcionaProcessadorMensagemTransacao(true, statusAtualizacaoTransacao, transacao, servicoOrigem, this.dadosBarramento, xmlSaidaSibar);
			
		}
		catch (ServicoException se) {
			logger.error(se.getMensagem());
			DadosTransacaoComuns dadosTransacaoComuns = new DadosTransacaoComuns(servicoOrigem, transacao, iteracaoCanal);
			ParametrosAdicionais parametrosAdicionais = new ParametrosAdicionais(this.dadosBarramento, xmlSaidaSibar);
			tratadorDeExcecao.tratarExcecaoEfetivaAgendamentoPagamentoRetomadaDesfazimento(dadosTransacaoComuns, transacaoAgendamento, se, parametrosAdicionais, isUltimaExecucao, false);
		}
		catch (Exception e) {
			logger.error(e);
			Mtxtb006Mensagem mensagemErroInterno = this.fornecedorDados.buscarMensagem(MensagemRetorno.ERRO_INTERNO);
			ServicoException se = new ServicoException(mensagemErroInterno, Constantes.ORIGEM_SIMTX);
			DadosTransacaoComuns dadosTransacaoComuns = new DadosTransacaoComuns(servicoOrigem, transacao, iteracaoCanal);
			ParametrosAdicionais parametrosAdicionais = new ParametrosAdicionais(this.dadosBarramento, xmlSaidaSibar);
			tratadorDeExcecao.tratarExcecaoEfetivaAgendamentoPagamentoRetomadaDesfazimento(dadosTransacaoComuns, transacaoAgendamento, se, parametrosAdicionais, isUltimaExecucao, false);
		}
	}
	
	/**
	 * Envia para o sibar as tarefas durante as tentativas do dia.
	 * 
	 * @param servicoEfetivaBoleto
	 * @param transacaoCorrente
	 * @param listaTarefas
	 * @param isUltimaExecucao
	 * @param isBoletoCaixa
	 * @throws ServicoException
	 * @throws AgendamentoException
	 */
	public TarefasServicoResposta processarTarefas(Mtxtb011VersaoServico servicoEfetivaBoleto, Mtxtb014Transacao transacaoCorrente, List<Mtxtb003ServicoTarefa> listaTarefas, String xmlSaidaSibar) {
		return this.gerenciadorTarefas.montarTarefasResposta(listaTarefas, servicoEfetivaBoleto, transacaoCorrente, this.dadosBarramento, xmlSaidaSibar);
	}
	
	public void iniciar(boolean isUltimaExecucao) {
		Date date = new Date();
		
		List<Mtxtb034TransacaoAgendamento> listaAgendamentos = this.fornecedorDadosAgendamento
				.buscaTransacoesAgendamentoPorData(date);
		if (null != listaAgendamentos && !listaAgendamentos.isEmpty()) {

			for (Mtxtb034TransacaoAgendamento transacaoAgendamento : listaAgendamentos) {
				ServicoAgendamentoEnum servicoAgendamentoEnum = ServicoAgendamentoEnum
						.obterServicoFinal(transacaoAgendamento.getNuServico(), transacaoAgendamento.getNuVersaoServico());

				if (servicoAgendamentoEnum.getServicoOrigem() == ConstantesAgendamento.CODIGO_SERVICO_PAGAMENTO_BOLETO
						|| servicoAgendamentoEnum.getServicoOrigem() == ConstantesAgendamento.CODIGO_SERVICO_PAGAMENTO_BOLETO_NPC) {
					logger.info("Executando Servico Pagamento Boleto Agendado");

					executarServico(transacaoAgendamento.getNuNsuTransacaoAgendamento(),
							servicoAgendamentoEnum.getServicoFinal(), servicoAgendamentoEnum.getVersaoServicoFinal(),
							isUltimaExecucao);
				}
			}
		} 
		else {
			logger.info("Nao ha Transacoes Agendadas para o dia");
		}
	}
	
	private void montarDadosXmlInformacoesAgendamento(Mtxtb034TransacaoAgendamento transacaoAgendamento, boolean isUltimaExecucao) {
		this.dadosBarramento.escrever("<NSU_AGENDAMENTO>"+transacaoAgendamento.getNuNsuTransacaoAgendamento()+"</NSU_AGENDAMENTO>");
		String agendamentoFinal = isUltimaExecucao?"S":"N";
		this.dadosBarramento.escrever("<AGENDAMENTO_FINAL>"+agendamentoFinal+"</AGENDAMENTO_FINAL>");
	}
}
