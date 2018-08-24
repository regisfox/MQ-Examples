package br.gov.caixa.simtx.persistencia.cache.core.test;


import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import br.gov.caixa.simtx.persistencia.cache.core.FornecedorDados;
import br.gov.caixa.simtx.persistencia.constante.AcaoRetorno;
import br.gov.caixa.simtx.persistencia.constante.MensagemRetorno;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb001Servico;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb002Tarefa;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb003ServicoTarefa;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb003ServicoTarefaPK;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb004Canal;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb005ServicoCanal;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb005ServicoCanalPK;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb006Mensagem;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb007TarefaMensagem;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb007TarefaMensagemPK;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb008MeioEntrada;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb010VrsoTarfaMeioEntra;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb010VrsoTarfaMeioEntraPK;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb011VersaoServico;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb011VersaoServicoPK;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb012VersaoTarefa;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb012VersaoTarefaPK;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb013PrmtoSrvcoCanal;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb014Transacao;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb015SrvcoTrnsoTrfa;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb015SrvcoTrnsoTrfaPK;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb016IteracaoCanal;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb016IteracaoCanalPK;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb017VersaoSrvcoTrnso;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb017VersaoSrvcoTrnsoPK;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb018VrsoMeioEntraSrvco;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb018VrsoMeioEntraSrvcoPK;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb020SrvcoTarfaCanal;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb020SrvcoTarfaCanalPK;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb023Parametro;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb024TarefaFila;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb024TarefaFilaPK;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb025RegraProcessamento;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb026ServicoTarefaRegras;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb026ServicoTarefaRegrasPK;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb029SituacaoVersaoServico;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb030SituacaoCanal;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb032MarcaConta;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb035TransacaoConta;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb036TransacaoAuditada;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb038GrupoAcesso;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb040PrmtoPgtoContingencia;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb040PrmtoPgtoContingenciaPK;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb041HistoricoPrmtoPgtoContingencia;
import br.gov.caixa.simtx.persistencia.vo.Header;
import br.gov.caixa.simtx.persistencia.vo.ParametrosFatorVencimento;

public class FornecedorDadosMock implements FornecedorDados {

	public static String pagamentoTeste = "<ns:PAGAMENTO_BOLETO_ENTRADA> 	<HEADER> 		<SERVICO> 			<CODIGO>110039</CODIGO> 			<VERSAO>1</VERSAO> 		</SERVICO> 		<CANAL> 			<SIGLA>SIMAA</SIGLA> 			<DATAHORA>20180101210900</DATAHORA> 			<NSU>12345</NSU> 		</CANAL> 		<MEIOENTRADA>0</MEIOENTRADA> 		<DATA_REFERENCIA>20180510</DATA_REFERENCIA> 	</HEADER> 	<NSUMTX_ORIGEM>17036</NSUMTX_ORIGEM> 	<AGENDAMENTO>NAO</AGENDAMENTO> 	<CONTA> 		<CONTA_SIDEC> 			<UNIDADE>0</UNIDADE> 			<OPERACAO>0</OPERACAO> 			<CONTA>0</CONTA> 			<DV>0</DV> 		</CONTA_SIDEC> 	</CONTA> 	<PAGAMENTO_BOLETO> 		<INFORMACOES_BOLETO> 			<DATA_VENCIMENTO>2001-01-01</DATA_VENCIMENTO> 			<LINHA_DIGITAVEL>23790504004188062212540008109205175660000019900</LINHA_DIGITAVEL> 		</INFORMACOES_BOLETO> 	</PAGAMENTO_BOLETO> </ns:PAGAMENTO_BOLETO_ENTRADA>";

	public Mtxtb015SrvcoTrnsoTrfa salvarTransacaoTarefa(Mtxtb015SrvcoTrnsoTrfa transacaoTarefa) {
		
		return null;
	}

	public Mtxtb017VersaoSrvcoTrnso salvarTransacaoServico(Mtxtb017VersaoSrvcoTrnso transacaoServico) {
		
		return null;
	}

	public Mtxtb014Transacao salvarTransacao(Mtxtb014Transacao transacao) {
		
		return null;
	}

	public Mtxtb014Transacao salvarTransacao(Header header, String mensagem) {
		
		return null;
	}

	public Mtxtb016IteracaoCanal salvarIteracaoCanal(Mtxtb016IteracaoCanal iteracaoCanal) {
		
		return null;
	}

	public Mtxtb016IteracaoCanal salvarIteracaoCanal(String mensagem, Mtxtb014Transacao transacao) {
		
		return null;
	}

	public boolean possuiInformacoesParaEnvioCCO(Date diaAnterior) {
		
		return false;
	}

	public int limparParticoes(String particoes) {
		
		return 0;
	}

	public Mtxtb012VersaoTarefa buscarVersaoTarefaPorPK(Mtxtb012VersaoTarefaPK versaoTarefaPK) {
		
		return null;
	}

	public Mtxtb011VersaoServico buscarVersaoServicoPorNomeOperacao(Mtxtb011VersaoServico versaoServico) {
		
		return null;
	}

	public Mtxtb011VersaoServico buscarVersaoServico(long nuVersaoServico, long nuServico) {
		
		return null;
	}

	public Mtxtb018VrsoMeioEntraSrvco buscarVersaoMeioEntraServcoPorPK(Mtxtb018VrsoMeioEntraSrvcoPK meioEntradaServicoPK) {
		
		return null;
	}

	public List<Mtxtb016IteracaoCanal> buscarTransacoesPendentes(Date data) {
		
		return null;
	}

	public List<Mtxtb016IteracaoCanal> buscarTransacoesParaEnvioCCO(Date diaAnterior) {
		
		return null;
	}

	public List<Mtxtb015SrvcoTrnsoTrfa> buscarTransacaoTarefaPorFiltroCorp(Mtxtb015SrvcoTrnsoTrfa transacaoTarefa) {
		
		return null;
	}

	public Mtxtb015SrvcoTrnsoTrfa buscarTransacaoTarefaPorFiltro(Mtxtb015SrvcoTrnsoTrfa transacaoTarefa) {
		
		return null;
	}

	public Mtxtb014Transacao buscarTransacaoPorPK(Mtxtb014Transacao transacao) {
		
		return null;
	}

	public Mtxtb014Transacao buscarTransacaoPorNSUPai(Mtxtb014Transacao transacao) {
		
		return null;
	}

	public int buscarTimeOutComunicacao() {
		
		return 0;
	}

	public List<Mtxtb020SrvcoTarfaCanal> buscarTarefasPorServicoCanal(Mtxtb020SrvcoTarfaCanalPK servicoTarefaCanalPK) {
		
		return null;
	}

	public List<Mtxtb020SrvcoTarfaCanal> buscarTarefasPorServicoCanal(long nuServico, long nuVersaoServico, long nuCanal) {
		
		return null;
	}

	public List<Mtxtb015SrvcoTrnsoTrfa> buscarTarefasPorNsu(long nsu) {
		List<Mtxtb015SrvcoTrnsoTrfa> lista = new ArrayList<>();
		for (int i = 0; i < 3; i++) {
			Mtxtb015SrvcoTrnsoTrfaPK srvcoTrnsoTrfaPK = new Mtxtb015SrvcoTrnsoTrfaPK();
			srvcoTrnsoTrfaPK.setNuTarefa012(i);
			srvcoTrnsoTrfaPK.setNuVersaoTarefa012(i);
			Mtxtb015SrvcoTrnsoTrfa srvcoTrnsoTrfa = new Mtxtb015SrvcoTrnsoTrfa();
			srvcoTrnsoTrfa.setTsExecucaoTransacao(new java.sql.Timestamp(new Date().getTime()));
			srvcoTrnsoTrfa.setDeXmlRequisicao("<TESTE_ENTRADA>" + i + "</TESTE_ENTRADA>");
			srvcoTrnsoTrfa.setDeXmlResposta("<TESTE_RESPOSTA>" + i + "</TESTE_RESPOSTA>");
			srvcoTrnsoTrfa.setId(srvcoTrnsoTrfaPK);
			lista.add(srvcoTrnsoTrfa);
		}
		return lista;
	}

	public List<Mtxtb010VrsoTarfaMeioEntra> buscarTarefasPorMeioEntrada(Mtxtb010VrsoTarfaMeioEntraPK versaoServicoPK) {
		
		return null;
	}

	public List<Mtxtb003ServicoTarefa> buscarTarefasNegocialPorServico(long nuServico, long nuVersaoServico, long nuCanal) {
		
		return null;
	}

	public Mtxtb003ServicoTarefa buscarTarefasNegocialPorPK(long nuServico, long nuVersaoServico, long nuTarefa, long nuVersaoTarefa) {
		
		return null;
	}

	public List<Mtxtb024TarefaFila> buscarTarefasFilas(Mtxtb024TarefaFilaPK tarefaPK) {
		List<Mtxtb024TarefaFila> lista = new ArrayList<>();

		Mtxtb024TarefaFila tarefaFila = null;
		Mtxtb024TarefaFilaPK tarefaFilaPK = null;
		for (int i = 1; i < 5; i++) {
			tarefaFilaPK = new Mtxtb024TarefaFilaPK();
			tarefaFilaPK.setNuTarefa012(i);
			tarefaFilaPK.setNuVersaoTarefa012(i);

			tarefaFila = new Mtxtb024TarefaFila();
			tarefaFila.setId(tarefaFilaPK);
			tarefaFila.setNoModoIntegracao("URL");
			tarefaFila.setNoRecurso("http://localhost:8080/rest");
			tarefaFila.setQtdeTempoEspera(10);
			lista.add(tarefaFila);
		}
		return lista;
	}

	public List<Mtxtb003ServicoTarefa> buscarTarefasExecutar(long nuServico, long nuVersaoServico, long nuCanal) {
		List<Mtxtb003ServicoTarefa> lista = new ArrayList<Mtxtb003ServicoTarefa>();

		Mtxtb002Tarefa tarefa = null;
		Mtxtb012VersaoTarefa versaoTarefa = null;
		Mtxtb012VersaoTarefaPK versaoTarefaPK = null;
		Mtxtb003ServicoTarefa servicoTarefa = null;
		Mtxtb003ServicoTarefaPK servicoTarefaPK = null;
		for (int i = 1; i < 5; i++) {
			servicoTarefaPK = new Mtxtb003ServicoTarefaPK();
			servicoTarefaPK.setNuTarefa012(i);
			servicoTarefaPK.setNuVersaoTarefa012(i);

			tarefa = new Mtxtb002Tarefa();
			tarefa.setNoServicoBarramento("consulta_conta_sid09");
			tarefa.setNoOperacaoBarramento("VALIDA_FUNCIONALIDADES_ESPECIAIS_IMPEDITIVAS");
			tarefa.setIcTipoTarefa(1);

			versaoTarefaPK = new Mtxtb012VersaoTarefaPK();
			versaoTarefaPK.setNuTarefa002(i);
			versaoTarefaPK.setNuVersaoTarefa(i);

			versaoTarefa = new Mtxtb012VersaoTarefa();
			versaoTarefa.setIcAssincrono(BigDecimal.ONE);
			versaoTarefa.setId(versaoTarefaPK);
			versaoTarefa.setMtxtb002Tarefa(tarefa);

			servicoTarefa = new Mtxtb003ServicoTarefa();
			servicoTarefa.setIcImpedimento(BigDecimal.ONE);
			servicoTarefa.setId(servicoTarefaPK);
			servicoTarefa.setMtxtb012VersaoTarefa(versaoTarefa);
			lista.add(servicoTarefa);
		}
		return lista;
	}

	public List<Mtxtb003ServicoTarefa> buscarTarefasExecutar(long nuServico, long nuVersaoServico, long nuMeioEntrada, long nuCanal) {
		
		return null;
	}

	public Mtxtb002Tarefa buscarTarefaPorPK(long nuTarefa) {
		final List<Mtxtb002Tarefa> lista = findAllTarefas();
		
		for(final Mtxtb002Tarefa tarefa: lista) {
			if(new Long(tarefa.getNuTarefa()).equals(nuTarefa)) {
				return tarefa;
			}
		}
		
		return null;
	}

	public Mtxtb017VersaoSrvcoTrnso buscarSrvcoTransacaoPorNSU(Mtxtb017VersaoSrvcoTrnso transacaoServico) {

		Mtxtb011VersaoServico versaoServico = new Mtxtb011VersaoServico();
		versaoServico = new Mtxtb011VersaoServico();
		Mtxtb011VersaoServicoPK versaoServicoPK = new Mtxtb011VersaoServicoPK();
		versaoServicoPK.setNuServico001(110033L);
		versaoServicoPK.setNuVersaoServico(1);
		versaoServico.setId(versaoServicoPK);
		versaoServico.setDeXsdRequisicao("validacao_entradas/SIMTX_TYPES/V1/Teste.xsd");
		versaoServico.setMigrado(1);

		Mtxtb017VersaoSrvcoTrnsoPK versaoSrvcoTrnsoPK = new Mtxtb017VersaoSrvcoTrnsoPK();
		versaoSrvcoTrnsoPK.setNuServico011(110033L);
		versaoSrvcoTrnsoPK.setNuVersaoServico011(1);
		Mtxtb017VersaoSrvcoTrnso versaoSrvcoTrnso = new Mtxtb017VersaoSrvcoTrnso();
		versaoSrvcoTrnso.setMtxtb011VersaoServico(versaoServico);
		versaoSrvcoTrnso.setId(versaoSrvcoTrnsoPK);

		return versaoSrvcoTrnso;
	}

	public Mtxtb001Servico buscarServicoPorPK(long nuServico) {
		return null;
	}

	public Mtxtb001Servico buscarServicoPorNomeOperacao(Mtxtb001Servico servico) {
		
		return null;
	}

	public Mtxtb005ServicoCanal buscarServicoCanalPorPK(Mtxtb005ServicoCanalPK servicoCanalPK) {
		
		return null;
	}

	public List<Mtxtb026ServicoTarefaRegras> buscarRegrasPorServicoTarefa(Mtxtb026ServicoTarefaRegrasPK regrasPK) {
		List<Mtxtb026ServicoTarefaRegras> lista = new ArrayList<>();

		Mtxtb026ServicoTarefaRegras servicoTarefaRegras = null;
		Mtxtb026ServicoTarefaRegrasPK servicoTarefaRegrasPK = null;
		Mtxtb025RegraProcessamento regraProcessamento = null;

		for (int i = 1; i < 5; i++) {
			servicoTarefaRegrasPK = new Mtxtb026ServicoTarefaRegrasPK();
			servicoTarefaRegrasPK.setNuRegra025(i);
			servicoTarefaRegrasPK.setNuServico003(i);
			servicoTarefaRegrasPK.setNuTarefa003(i);
			servicoTarefaRegrasPK.setNuVersaoServico003(i);
			servicoTarefaRegrasPK.setNuVersaoTarefa003(i);

			regraProcessamento = new Mtxtb025RegraProcessamento();
			regraProcessamento.setDeCaminhoInformacao("CONTA_NSGD.UNIDADE");
			regraProcessamento.setNoCampoDependencia("UNIDADE");
			regraProcessamento.setNoOperacaoOrigem("CARTAO_COMERCIAL");
			regraProcessamento.setNoServicoOrigem("valida_cartao");
			regraProcessamento.setNuRegra(i);

			servicoTarefaRegras = new Mtxtb026ServicoTarefaRegras();
			servicoTarefaRegras.setId(servicoTarefaRegrasPK);
			servicoTarefaRegras.setMtxtb025RegraProcessamento(regraProcessamento);
			lista.add(servicoTarefaRegras);
		}
		return lista;
	}

	public Mtxtb013PrmtoSrvcoCanal buscarParametroSrvcoCanal(Mtxtb005ServicoCanalPK parametro) {
		
		return null;
	}

	public Mtxtb023Parametro buscarParametroPorPK(long code) {
		Mtxtb023Parametro parametro = new Mtxtb023Parametro();
		parametro.setDeConteudoParam("1");
		return parametro;
	}

	public String buscarNomeServico(Long nuServico) {
		
		return null;
	}

	public List<Mtxtb007TarefaMensagem> buscarAutorizadorasPorTarefa(Mtxtb007TarefaMensagemPK tarefaMensagemPK) {
		List<Mtxtb007TarefaMensagem> lista = new ArrayList<>();
		for (int i = 0; i < 2; i++) {
			Mtxtb007TarefaMensagemPK tarefaMensagemPK2 = new Mtxtb007TarefaMensagemPK();
			tarefaMensagemPK2.setNuMensagem006(1L);
			tarefaMensagemPK2.setNuTarefa012(1L);
			tarefaMensagemPK2.setNuVersaoTarefa012(1L);

			Mtxtb006Mensagem mensagem = new Mtxtb006Mensagem();
			mensagem.setCoMensagem("0" + i);
			mensagem.setDeMensagemNegocial("SUCESSO");

			Mtxtb007TarefaMensagem tarefaMensagem = new Mtxtb007TarefaMensagem();
			tarefaMensagem.setId(tarefaMensagemPK2);
			tarefaMensagem.setMtxtb006Mensagem(mensagem);
			lista.add(tarefaMensagem);
		}
		return lista;
	}

	public Mtxtb006Mensagem buscarMensagem(String codigoMensagem) {
		
		return null;
	}

	public Mtxtb008MeioEntrada buscarMeioEntradaPorPK(Mtxtb008MeioEntrada meioEntrada) {
		
		return null;
	}

	public Mtxtb008MeioEntrada buscarMeioEntradaPorNome(Mtxtb008MeioEntrada meioEntrada) {
		
		return null;
	}

	public Mtxtb008MeioEntrada buscarMeioEntrada(long codModoEntrada) {
		
		return null;
	}

	public Mtxtb016IteracaoCanal buscarIteracaoCanalPorPK(Mtxtb016IteracaoCanal iteracaoCanalPK) {
		Mtxtb014Transacao transacao = new Mtxtb014Transacao();
		transacao.setNuNsuTransacao(15L);
		transacao.setIcSituacao(BigDecimal.ONE);
		transacao.setCoCanalOrigem("104");
		transacao.setDtReferencia(new Date());
		transacao.setDhTransacaoCanal(new Date());
		transacao.setDhMultiCanal(new Date());

		Mtxtb016IteracaoCanal iteracaoCanal = new Mtxtb016IteracaoCanal();
		Mtxtb016IteracaoCanalPK pk = new Mtxtb016IteracaoCanalPK();
		pk.setNuNsuTransacao014(transacao.getNuNsuTransacao());
		iteracaoCanal.setDeRecebimento("<ENTRADA>A</ENTRADA>");
		iteracaoCanal.setDeRetorno("<SAIDA>B</SAIDA>");
		iteracaoCanal.setId(pk);
		iteracaoCanal.setMtxtb014Transacao(transacao);

		return iteracaoCanal;
	}

	public List<Mtxtb016IteracaoCanal> buscarIteracao(long nsu) {
		
		return null;
	}

	public List<Mtxtb014Transacao> buscarInformacoesParaLimpeza(Date diaAnterior) {
		
		return null;
	}

	public Mtxtb004Canal buscarCanalPorSigla(Mtxtb004Canal canal) {
		
		return null;
	}

	public Mtxtb004Canal buscarCanalPorPK(Mtxtb004Canal mtxtb004Canal) {
		
		return null;
	}

	public List<Mtxtb032MarcaConta> buscaTarefaMarcas(long nuTarefa) {
		
		return null;
	}

	public List<Mtxtb032MarcaConta> buscarMarcasPorServico(long nuServico) {
		
		return null;
	}

	public Mtxtb015SrvcoTrnsoTrfa atualizarTransacaoTarefa(Mtxtb015SrvcoTrnsoTrfa transacaoTarefa) {
		
		return null;
	}

	public void alterarTransacao(Mtxtb014Transacao transacao) {
		

	}

	public void alterarIteracaoCanal(Mtxtb016IteracaoCanal iteracaoCanal) {
		

	}

	@Override
	public List<Mtxtb004Canal> buscarPorSituacao(BigDecimal situacao) {
		
		return null;
	}

	@Override
	public List<Mtxtb001Servico> buscarPorServicoCancelamento(int cancelamento) {
		
		return null;
	}

	@Override
	public List<Mtxtb035TransacaoConta> buscaTransacao(Mtxtb035TransacaoConta transacaoConta) {
		return mockListaTransacaoConta(transacaoConta);
	}

	@Override
	public List<Mtxtb003ServicoTarefa> buscarTarefasMeioEntrada(long nuMeioEntrada, long nuServico, long nuVersaoServico) {
		
		return null;
	}

	@Override
	public Mtxtb035TransacaoConta salvarTransacaoConta(Mtxtb035TransacaoConta transacaoConta) {
		
		return null;
	}

	@Override
	public Mtxtb036TransacaoAuditada salvarMtxtb036TransacaoAuditada(Mtxtb036TransacaoAuditada transacao) {
		
		return null;
	}

	@Override
	public List<Mtxtb004Canal> buscarTodosCanais() {
		
		return null;
	}

	@Override
	public Mtxtb006Mensagem buscarMensagem(MensagemRetorno mensagem) {
		
		return null;
	}

	@Override
	public Mtxtb007TarefaMensagem buscarMensagemPorTarefaCodRetorno(String codigoMensagem,
			Mtxtb012VersaoTarefa versaoTarefa) {
		Mtxtb007TarefaMensagem msgTarefa = new Mtxtb007TarefaMensagem();
		
		Mtxtb006Mensagem msg = new Mtxtb006Mensagem();
		msg.setCodigoRetorno(codigoMensagem);
		msg.setDeMensagemNegocial("TRANSACAO NEGADA");
		msg.setDeMensagemTecnica("MENSAGEM TECNICA");
		
		if(codigoMensagem.equals("00"))
			msg.setIcTipoMensagem(AcaoRetorno.AUTORIZADORA.getTipo());
		else
			msg.setIcTipoMensagem(AcaoRetorno.IMPEDITIVA.getTipo());
		
		msgTarefa.setMtxtb006Mensagem(msg);
		return msgTarefa;
	}

	@Override
	public Mtxtb006Mensagem buscarMensagemPorCodigoMensagem(String codigoMensagem) {
		
		return null;
	}


	@Override
	public Mtxtb014Transacao buscarTransacaoOrigem(Long nsu) {
		return null;
	}

	@Override
	public Mtxtb011VersaoServico buscarVersaoServico(int nuVersaoServico, long nuServico) {
		return null;
	}

	@Override
	public List<Mtxtb014Transacao> buscarTransacoesParaEnvioSicco(Date dataAtual) {
		
		return null;
	}

	@Override
	public Mtxtb038GrupoAcesso buscaGrupoAcesso(String grupo) {
		
		return null;
	}

	@Override
	public void alteraSituacaoCanal(List<Mtxtb004Canal> listaCanais) {
		

	}

	@Override
	public List<Mtxtb001Servico> buscarTodosServicos() {
		
		return null;
	}
	
	@Override
	public void salvaSituacaoServico(Mtxtb029SituacaoVersaoServico situacao) {
		

	}

	@Override
	public void salvaSituacaoCanal(Mtxtb030SituacaoCanal situacao) {
		

	}

	@Override
	public void alteraSituacaoServico(List<Mtxtb011VersaoServico> listaServicos) {
		

	}

	private List<Mtxtb035TransacaoConta> mockListaTransacaoConta(Mtxtb035TransacaoConta consulta) {
		List<Mtxtb035TransacaoConta> lista = new ArrayList<>();

		if (consulta.getNumeroUnidade() == 1234) {
			for (int i = 0; i < 2; i++) {
				Mtxtb035TransacaoConta transacao = new Mtxtb035TransacaoConta();
				transacao.setNuNsuTransacao(1000 + i);
				transacao.setDataReferencia(new Date());
				Mtxtb016IteracaoCanal mtxtb016IteracaoCanal = new Mtxtb016IteracaoCanal();
				mtxtb016IteracaoCanal.setDeRecebimento(pagamentoTeste);
				transacao.setMtxtb016IteracaoCanal(mtxtb016IteracaoCanal);
				transacao.setMtxtb004Canal(new Mtxtb004Canal(1235L));
				transacao.setMtxtb001Servico(new Mtxtb001Servico(1L));
				lista.add(transacao);
			}
		}

		return lista;

	}

	@Override
	public Mtxtb006Mensagem buscarMensagemErroInterno() {
		
		return null;
	}

	@Override
	public Mtxtb040PrmtoPgtoContingencia buscarPrmtoPgtoCntngnciaPorCanal(
			Mtxtb040PrmtoPgtoContingenciaPK prmtoPgtoCntngncia) {
		
		return null;
	}


	@Override
	public List<Mtxtb040PrmtoPgtoContingencia> buscarTodosParametrosBoletosContingencia() {
		
		return null;
	}

	@Override
	public void updateParametrosBoletosContingencia(List<Mtxtb040PrmtoPgtoContingencia> parametros) {
		
	}

	@Override
	public ParametrosFatorVencimento buscarParametrosFatorVencimento() throws ParseException {
		return null;
	}

	@Override
	public void alterarParametro(Mtxtb023Parametro mtxtb023Parametro) {
		
	}

	@Override
	public void atualizaStatusPagamento(Long nsuOrigem, Long icSituacao) {
		
	}

	@Override
	public Mtxtb035TransacaoConta buscarTransacaoContaPorNsu(Long nsuTransacaoConta) {
		return null;
	}
	
	@Override
	public void salvaHistoricoParametroBoleto(Mtxtb041HistoricoPrmtoPgtoContingencia parametroBoletoContingencia) {
		
	}

	@Override
	public List<Mtxtb011VersaoServico> buscarVersaoServicoPorNome(String nomeServico) {
		return null;
	}

	@Override
	public List<Mtxtb011VersaoServico> buscarVersaoServicoPorSituacao(int situacaoVersaoServico) {
		return null;
	}

	@Override
	public List<Mtxtb011VersaoServico> buscarTodosVersaoServico() {
		return null;
	}

	@Override
	public List<Mtxtb002Tarefa> findAllTarefas() {
		final List<Mtxtb002Tarefa> result = new ArrayList<>();
		
		final Mtxtb002Tarefa tar01 = new Mtxtb002Tarefa();
		tar01.setDhAtualizacao(Calendar.getInstance().getTime());
		tar01.setIcFinanceira(BigDecimal.valueOf(0));
		tar01.setIcTipoTarefa(2);
		tar01.setNoOperacaoBarramento("CONSULTA_SALDO");
		tar01.setNoServicoBarramento("consulta_saldo");
		tar01.setNoTarefa("Consulta Saldo");
		tar01.setNuTarefa(100014);
		
		result.add(tar01);
		
		final Mtxtb002Tarefa tar02 = new Mtxtb002Tarefa();
		tar02.setDhAtualizacao(Calendar.getInstance().getTime());
		tar02.setIcFinanceira(BigDecimal.valueOf(0));
		tar02.setIcTipoTarefa(2);
		tar02.setNoOperacaoBarramento("CALCULA_BOLETO");
		tar02.setNoServicoBarramento("rotinas_uso_comum");
		tar02.setNoTarefa("Calcula Boleto");
		tar02.setNuTarefa(100066);
		
		result.add(tar02);
		
		final Mtxtb002Tarefa tar03 = new Mtxtb002Tarefa();
		tar03.setDhAtualizacao(Calendar.getInstance().getTime());
		tar03.setIcFinanceira(BigDecimal.valueOf(1));
		tar03.setIcTipoTarefa(2);
		tar03.setNoOperacaoBarramento("BAIXA_OPERACIONAL");
		tar03.setNoServicoBarramento("manutencao_cobranca_bancaria");
		tar03.setNoTarefa("Baixa Operacional");
		tar03.setNuTarefa(100059);
		
		result.add(tar03);
		
		
		return result;
	}
	
	@Override
	public List<Mtxtb007TarefaMensagem> buscarTarefaMensagensPorNumeroTarefa(long nuTarefa) {
		final List<Mtxtb007TarefaMensagem> result = new ArrayList<>();
		
		for(final Mtxtb007TarefaMensagem tarefaMensagem: findAllTarefaMensagem()) {
			if(new Long(tarefaMensagem.getMtxtb012VersaoTarefa().getMtxtb002Tarefa().getNuTarefa()).equals(nuTarefa)) {
				result.add(tarefaMensagem);
			}
		}
		
		return result;
	}
	
	private List<Mtxtb007TarefaMensagem> findAllTarefaMensagem() {
		final List<Mtxtb007TarefaMensagem> result = new ArrayList<>();
		
		final Mtxtb007TarefaMensagem tm01 = new Mtxtb007TarefaMensagem();
		tm01.setId(new Mtxtb007TarefaMensagemPK());
		tm01.getId().setNuMensagem006(100068);
		tm01.getId().setNuTarefa012(1);
		tm01.getId().setNuVersaoTarefa012(1);
		
		tm01.setMtxtb006Mensagem(new Mtxtb006Mensagem());
		tm01.getMtxtb006Mensagem().setNuNsuMensagem(1);
		tm01.getMtxtb006Mensagem().setCodigoRetorno("00");
		tm01.getMtxtb006Mensagem().setCoMensagem("00");
		tm01.getMtxtb006Mensagem().setDeMensagemNegocial("Transacao efetivada com sucesso.");
		tm01.getMtxtb006Mensagem().setDeMensagemTecnica("Transacao efetivada com sucesso.");
		tm01.getMtxtb006Mensagem().setDhAtualizacao(Calendar.getInstance().getTime());
		tm01.getMtxtb006Mensagem().setIcTipoMensagem(3);
		
		tm01.setMtxtb012VersaoTarefa(new Mtxtb012VersaoTarefa());
		tm01.getMtxtb012VersaoTarefa().setId(new Mtxtb012VersaoTarefaPK());
		tm01.getMtxtb012VersaoTarefa().getId().setNuTarefa002(1);
		tm01.getMtxtb012VersaoTarefa().getId().setNuVersaoTarefa(1);
		
		tm01.setNoCampoRetorno("CONTROLE_NEGOCIAL.COD_RETORNO");
		
		result.add(tm01);
		
		
		final Mtxtb007TarefaMensagem tm02 = new Mtxtb007TarefaMensagem();
		tm02.setId(new Mtxtb007TarefaMensagemPK());
		tm02.getId().setNuMensagem006(165);
		tm02.getId().setNuTarefa012(100050);
		tm02.getId().setNuVersaoTarefa012(1);
		
		tm02.setMtxtb006Mensagem(new Mtxtb006Mensagem());
		tm02.getMtxtb006Mensagem().setNuNsuMensagem(165);
		tm02.getMtxtb006Mensagem().setCodigoRetorno("X5");
		tm02.getMtxtb006Mensagem().setCoMensagem("XX5");
		tm02.getMtxtb006Mensagem().setDeMensagemNegocial("Transacao indisponivel. Tente mais tarde.");
		tm02.getMtxtb006Mensagem().setDeMensagemTecnica("-");
		tm02.getMtxtb006Mensagem().setDhAtualizacao(Calendar.getInstance().getTime());
		tm02.getMtxtb006Mensagem().setIcTipoMensagem(1);
		
		tm02.setMtxtb012VersaoTarefa(new Mtxtb012VersaoTarefa());
		tm02.getMtxtb012VersaoTarefa().setId(new Mtxtb012VersaoTarefaPK());
		tm02.getMtxtb012VersaoTarefa().getId().setNuTarefa002(100050);
		tm02.getMtxtb012VersaoTarefa().getId().setNuVersaoTarefa(1);
		
		tm02.setNoCampoRetorno("CONTROLE_NEGOCIAL.COD_RETORNO");
		
		result.add(tm02);
		
		return result;
	}

	@Override
	public void salvaMensagem(Mtxtb006Mensagem mensagem) {
		
	}
}
