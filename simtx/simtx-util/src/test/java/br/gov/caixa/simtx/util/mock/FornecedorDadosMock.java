package br.gov.caixa.simtx.util.mock;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.enterprise.inject.Alternative;

import br.gov.caixa.simtx.persistencia.cache.core.FornecedorDados;
import br.gov.caixa.simtx.persistencia.constante.AcaoRetorno;
import br.gov.caixa.simtx.persistencia.constante.Constantes;
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
import br.gov.caixa.simtx.persistencia.vo.TipoBoletoEnum;
import br.gov.caixa.simtx.persistencia.vo.TipoContingenciaEnum;
import br.gov.caixa.simtx.util.data.DataUtil;
import br.gov.caixa.simtx.util.mock.constantes.ConstantesXmlMockPagamentoBoleto;
import br.gov.caixa.simtx.util.mock.constantes.ConstantesXmlMockValidaBoleto;

@Alternative
public class FornecedorDadosMock implements FornecedorDados, Serializable {
	
	private static final long serialVersionUID = -7951244287396720166L;


	public Mtxtb015SrvcoTrnsoTrfa salvarTransacaoTarefa(Mtxtb015SrvcoTrnsoTrfa transacaoTarefa) {
		return new Mtxtb015SrvcoTrnsoTrfa();
	}

	
	public Mtxtb017VersaoSrvcoTrnso salvarTransacaoServico(Mtxtb017VersaoSrvcoTrnso transacaoServico) {
		return new Mtxtb017VersaoSrvcoTrnso();
	}

	
	public Mtxtb014Transacao salvarTransacao(Mtxtb014Transacao transacao) {
		transacao.setNuNsuTransacao(15L);
		return transacao;
	}

	
	public Mtxtb014Transacao salvarTransacao(Header header, String mensagem) {
		return new Mtxtb014Transacao();
	}

	
	public Mtxtb016IteracaoCanal salvarIteracaoCanal(Mtxtb016IteracaoCanal iteracaoCanal) {
		return new Mtxtb016IteracaoCanal();
	}

	
	public Mtxtb016IteracaoCanal salvarIteracaoCanal(String mensagem, Mtxtb014Transacao transacao) {
		return new Mtxtb016IteracaoCanal();
	}

	
	public boolean possuiInformacoesParaEnvioCCO(Date diaAnterior) {
		
		return false;
	}

	
	public int limparParticoes(String particoes) {
		return 0;
	}

	
	public Mtxtb012VersaoTarefa buscarVersaoTarefaPorPK(Mtxtb012VersaoTarefaPK versaoTarefaPK) {
		Mtxtb012VersaoTarefa mtxtb012VersaoTarefa = new Mtxtb012VersaoTarefa();
		Mtxtb002Tarefa mtxtb002Tarefa = new Mtxtb002Tarefa();
		mtxtb002Tarefa.setNuTarefa(100066L);
		mtxtb002Tarefa.setNoTarefa("Calcula Boleto");
		Mtxtb012VersaoTarefaPK pk = new Mtxtb012VersaoTarefaPK();
		pk.setNuTarefa002(100066L);
		pk.setNuVersaoTarefa(1L);
		mtxtb012VersaoTarefa.setId(pk);
		mtxtb012VersaoTarefa.setMtxtb002Tarefa(mtxtb002Tarefa);
		
		return mtxtb012VersaoTarefa;
	}

	
	public Mtxtb011VersaoServico buscarVersaoServicoPorNomeOperacao(Mtxtb011VersaoServico versaoServico) {
		return new Mtxtb011VersaoServico();
	}

	
	public Mtxtb011VersaoServico buscarVersaoServico(long nuVersaoServico, long nuServico) {
		return null;
	}

	
	public Mtxtb018VrsoMeioEntraSrvco buscarVersaoMeioEntraServcoPorPK(
			Mtxtb018VrsoMeioEntraSrvcoPK meioEntradaServicoPK) {
		
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
		Mtxtb014Transacao mocked = new Mtxtb014Transacao();
		if (null != transacao) {
			mocked.setNuNsuTransacao(transacao.getNuNsuTransacao());	
		}
		return mocked;
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
			srvcoTrnsoTrfa.setTsExecucaoTransacao(DataUtil.getDataAtual());
			srvcoTrnsoTrfa.setDeXmlRequisicao("<TESTE_ENTRADA>"+i+"</TESTE_ENTRADA>");
			srvcoTrnsoTrfa.setDeXmlResposta("<TESTE_RESPOSTA>"+i+"</TESTE_RESPOSTA>");
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

		
		if(tarefaPK.getNuTarefa012() == 100045) {
			Mtxtb024TarefaFilaPK tarefaFilaPK = new Mtxtb024TarefaFilaPK();
			tarefaFilaPK.setNuTarefa012(100045);
			tarefaFilaPK.setNuVersaoTarefa012(1);

			Mtxtb024TarefaFila tarefaFila = new Mtxtb024TarefaFila();
			tarefaFila.setId(tarefaFilaPK);
			tarefaFila.setNoModoIntegracao("MQ");
			tarefaFila.setNoRecurso("SIBAR.REQ.CONSULTA_ASSINATURA_ELETRONICA");
			tarefaFila.setQtdeTempoEspera(10);
			lista.add(tarefaFila);
			
		}
		else {
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
		
		return buscarTarefasExecutar(nuServico, nuVersaoServico, nuCanal);
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
		
		if(regrasPK.getNuServico003() == 110023) {
			return new ArrayList<>();
		}
		else {
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
	}

	
	public Mtxtb013PrmtoSrvcoCanal buscarParametroSrvcoCanal(Mtxtb005ServicoCanalPK parametro) {
		
		return null;
	}

	
	public Mtxtb023Parametro buscarParametroPorPK(long code) {
		Mtxtb023Parametro parametro = new Mtxtb023Parametro();
		if(code == ParametrosFatorVencimento.PMT_DATA_BASE_NOVA) {
			parametro.setDeConteudoParam("29/05/2022");
		}
		else if(code == ParametrosFatorVencimento.PMT_DATA_BASE_ATUAL) {
			parametro.setDeConteudoParam("07/10/1997");
		}
		else {
			parametro.setDeConteudoParam("1");
		}
		
		return parametro;
	}

	
	public String buscarNomeServico(Long nuServico) {
		
		return null;
	}

	
	public List<Mtxtb007TarefaMensagem> buscarAutorizadorasPorTarefa(Mtxtb007TarefaMensagemPK tarefaMensagemPK) {
		List<Mtxtb007TarefaMensagem> lista = new ArrayList<>();
		for(int i = 0; i < 2; i++) {
			Mtxtb007TarefaMensagemPK tarefaMensagemPK2 = new Mtxtb007TarefaMensagemPK();
			tarefaMensagemPK2.setNuMensagem006(1L);
			tarefaMensagemPK2.setNuTarefa012(1L);
			tarefaMensagemPK2.setNuVersaoTarefa012(1L);
			
			Mtxtb006Mensagem mensagem = new Mtxtb006Mensagem();
			mensagem.setCoMensagem("0"+i);
			mensagem.setDeMensagemNegocial("SUCESSO");
			
			Mtxtb007TarefaMensagem tarefaMensagem = new Mtxtb007TarefaMensagem();
			tarefaMensagem.setId(tarefaMensagemPK2);
			tarefaMensagem.setMtxtb006Mensagem(mensagem);
			tarefaMensagem.setNoCampoRetorno("CONTROLE_NEGOCIAL.COD_RETORNO");
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
		Mtxtb008MeioEntrada meioEntrada = new Mtxtb008MeioEntrada();
		meioEntrada.setNuMeioEntrada(codModoEntrada);
		meioEntrada.setIcSituacao(BigDecimal.ONE);
		meioEntrada.setNoMeioEntrada("MEIO_ENTRADA_MOCK");
		return meioEntrada;
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
		String deRecebimento = null;
		if (nsu==225349) {
			deRecebimento = ConstantesXmlMockValidaBoleto.validaBoletoAgendamento;
		}
		else if (nsu==225350) {
			deRecebimento = ConstantesXmlMockValidaBoleto.validaBoletoAgendamentoSemErro;	
		}
		else if (nsu==225351) {
			deRecebimento = "";
		}
		else {
			deRecebimento = ConstantesXmlMockValidaBoleto.validaBoleto;
		}
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
		iteracaoCanal.setDeRecebimento(deRecebimento);
		iteracaoCanal.setDeRetorno("<SAIDA>B</SAIDA>");
		iteracaoCanal.setId(pk);
		iteracaoCanal.setMtxtb014Transacao(transacao);
		List<Mtxtb016IteracaoCanal> list = new ArrayList<>();
		list.add(iteracaoCanal);
		return list;
	}

	public List<Mtxtb014Transacao> buscarInformacoesParaLimpeza(Date diaAnterior) {
		
		return null;
	}

	public Mtxtb004Canal buscarCanalPorSigla(Mtxtb004Canal canal) {
		canal.setNuCanal(Constantes.CODIGO_CANAL_SIMTX);
		canal.setIcSituacaoCanal(new BigDecimal(1L));
		return canal;
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
		Mtxtb006Mensagem msg = new Mtxtb006Mensagem();
		if(mensagem.getCodigo() == 1) {
			msg.setCodigoRetorno(mensagem.getCodigo().toString());
			msg.setDeMensagemNegocial("TRANSACAO EFETIVADA COM SUCESSO");
			msg.setDeMensagemTecnica("MENSAGEM TECNICA");
			msg.setIcTipoMensagem(AcaoRetorno.AUTORIZADORA.getTipo());
		}
		else {
			msg.setCodigoRetorno(mensagem.getCodigo().toString());
			msg.setDeMensagemNegocial("TRANSACAO NEGADA");
			msg.setDeMensagemTecnica("MENSAGEM TECNICA");
			msg.setIcTipoMensagem(AcaoRetorno.INFORMATIVA.getTipo());
		}
		return msg;
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
		String xml = "<consultaboleto:CONSULTA_BOLETO_SAIDA xmlns:comuns=\"http://caixa.gov.br/simtx/comuns/comuns\" xmlns:consultaboleto=\"http://caixa.gov.br/simtx/consulta_boleto_pagamento/v1/ns\" xmlns:h=\"http://caixa.gov.br/simtx/comuns/header\" xmlns:validacartao=\"http://caixa.gov.br/simtx/meios_de_entrada/valida_cartao/v1/valida_cartao\" xmlns:validapermissao=\"http://caixa.gov.br/simtx/meios_de_entrada/valida_permissao/v1/valida_permissao\" xmlns:validasenha=\"http://caixa.gov.br/simtx/meios_de_entrada/valida_senha/v1/valida_senha\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><HEADER><SERVICO><CODIGO>110029</CODIGO><VERSAO>1</VERSAO></SERVICO><CANAL><SIGLA>SIMAA</SIGLA><NSU>16810050030</NSU><DATAHORA>20180606173714</DATAHORA></CANAL><MEIOENTRADA>4</MEIOENTRADA><IDENTIFICADOR_ORIGEM><TERMINAL>01681005</TERMINAL></IDENTIFICADOR_ORIGEM><USUARIO_SERVICO>smtxsd01</USUARIO_SERVICO><DATA_REFERENCIA>20180606</DATA_REFERENCIA><NSUMTX>183000</NSUMTX><COD_RETORNO>00</COD_RETORNO><ACAO_RETORNO>AUTORIZADORA</ACAO_RETORNO><ORIGEM_RETORNO>SIMTX</ORIGEM_RETORNO><MENSAGENS><NEGOCIAL>TRANSACAO EFETIVADA COM SUCESSO</NEGOCIAL><TECNICA>TRANSACAO EFETIVADA COM SUCESSO</TECNICA></MENSAGENS></HEADER><CONTA><CONTA_NSGD><UNIDADE>0612</UNIDADE><PRODUTO>1288</PRODUTO><CONTA>000999061201</CONTA><DV>9</DV></CONTA_NSGD><TITULAR_CONTA>1</TITULAR_CONTA></CONTA><CARTAO><VENCIMENTO>2012</VENCIMENTO><TIPO>10</TIPO><SITUACAO>0</SITUACAO><FLAG_ULTIMA_VIA>0</FLAG_ULTIMA_VIA><QUANTIDADE_UR>90</QUANTIDADE_UR><FLAG_CHIP>0</FLAG_CHIP><FLAG_CVV2>N</FLAG_CVV2><BANDEIRA>V</BANDEIRA></CARTAO><SENHA><INFORMACOES_TELA>10ESCOLHA A OPCAO QUE CONFERE COM A SUA PALAVRA SECRETA.                              EUROPEU   ORADOR    JANEIRO   AMIDALAS  GELO      BANCARIO  PATO      UNIVERSO  0999999999999999999999999999999999999999999999999999999999999999999999999   000000000IDPOSI    001</INFORMACOES_TELA><FLAG_VALIDACAO>S</FLAG_VALIDACAO><CRIPTOGRAMA>00010000                  256D827062F90CA9076A6F32EC95264A                                </CRIPTOGRAMA><FLAG_ULTIMA_TENTATIVA>N</FLAG_ULTIMA_TENTATIVA><TP_IDENTIFICACAO_POSITIVA>2</TP_IDENTIFICACAO_POSITIVA></SENHA><FORMA_RECEBIMENTO>VALOR_CALCULADO</FORMA_RECEBIMENTO><FLAG_NOVA_COBRANCA>S</FLAG_NOVA_COBRANCA><SITUACAO_CONTINGENCIA>SEM_CONTINGENCIA</SITUACAO_CONTINGENCIA><CONSULTA_BOLETO><FLAG_HABITACAO>N</FLAG_HABITACAO><TIPO_BOLETO>SIGCB</TIPO_BOLETO><TITULO><NOSSO_NUMERO><NUMERO>14000000099247877</NUMERO><DV>0</DV></NOSSO_NUMERO><BENEFICIARIO_ORIGINAL><CNPJ>360305000104</CNPJ><RAZAO_SOCIAL>TESTE GCB TIPO 99 SEM INDICE</RAZAO_SOCIAL><NOME_FANTASIA>TESTE GCB TIPO 99 SEM INDICE</NOME_FANTASIA><CODIGO>806248</CODIGO><DV>0</DV></BENEFICIARIO_ORIGINAL><AVALISTA><CNPJ>0</CNPJ></AVALISTA><PAGADOR><CPF>57962014849</CPF><NOME>TESTE PAGADOR</NOME></PAGADOR><CODIGO_BARRAS>10491772500001000008062480000100040992478775</CODIGO_BARRAS><LINHA_DIGITAVEL>10498062418000010004209924787758177250000100000</LINHA_DIGITAVEL><DATA_VENCIMENTO>2018-12-01</DATA_VENCIMENTO><VALOR>1000.00</VALOR><MINIMO><VALOR>500.00</VALOR></MINIMO><MAXIMO><VALOR>1500.00</VALOR></MAXIMO><PARTICIPANTE_DESTINATARIO><ISPB>00360305</ISPB><CODIGO_BANCO>104</CODIGO_BANCO><NOME_BANCO>CAIXA ECONOMICA FEDERAL</NOME_BANCO></PARTICIPANTE_DESTINATARIO></TITULO><RECEBE_VALOR_DIVERGENTE>ENTRE_MINIMO_MAXIMO</RECEBE_VALOR_DIVERGENTE><FLAG_RECEBIMENTO_CHEQUE>S</FLAG_RECEBIMENTO_CHEQUE><FLAG_PAGAMENTO_PARCIAL>S</FLAG_PAGAMENTO_PARCIAL><QUANTIDADE_PAGAMENTO_PARCIAL_PERMITIDO>15</QUANTIDADE_PAGAMENTO_PARCIAL_PERMITIDO><QUANTIDADE_PAGAMENTO_PARCIAL_REGISTRADO>2</QUANTIDADE_PAGAMENTO_PARCIAL_REGISTRADO><MODELO_CALCULO>04</MODELO_CALCULO><CALCULOS><CALCULO_CAIXA><VALOR_JUROS>0.00</VALOR_JUROS><VALOR_MULTA>0.00</VALOR_MULTA><VALOR_DESCONTO>0.00</VALOR_DESCONTO><VALOR_TOTAL>0.00</VALOR_TOTAL><DATA_VALIDADE_CALCULO>2018-06-06</DATA_VALIDADE_CALCULO><VALOR_IOF>0.00</VALOR_IOF><VALOR_ABATIMENTO>0.00</VALOR_ABATIMENTO><DATA_VENCIMENTO_UTIL>2018-12-03</DATA_VENCIMENTO_UTIL><DATA_CALCULO>2018-06-06</DATA_CALCULO></CALCULO_CAIXA></CALCULOS><FLAG_ULTIMA_PARCELA_VIAVEL>S</FLAG_ULTIMA_PARCELA_VIAVEL><NUMERO_PARCELA_ATUAL>3</NUMERO_PARCELA_ATUAL></CONSULTA_BOLETO></consultaboleto:CONSULTA_BOLETO_SAIDA>";

		Mtxtb014Transacao transacao = new Mtxtb014Transacao();
		transacao.setNuNsuTransacao(nsu);
		
		Mtxtb016IteracaoCanal iteracaoCanal = new Mtxtb016IteracaoCanal();
		Mtxtb016IteracaoCanalPK iteracaoCanalPK = new Mtxtb016IteracaoCanalPK();
		iteracaoCanalPK.setNuNsuTransacao014(nsu);
		iteracaoCanal.setId(iteracaoCanalPK);
		iteracaoCanal.setMtxtb014Transacao(transacao);
		iteracaoCanal.setDeRetorno(xml);
		
		List<Mtxtb016IteracaoCanal> lista = new ArrayList<>();
		lista.add(iteracaoCanal);
		
		transacao.setMtxtb016IteracaoCanals(lista);
		return transacao;
	}

	@Override
	public Mtxtb011VersaoServico buscarVersaoServico(int nuVersaoServico, long nuServico) {
		Mtxtb011VersaoServico mtxtb011VersaoServico = new Mtxtb011VersaoServico();
		Mtxtb001Servico servico = new Mtxtb001Servico();

		if(nuServico == 110031L && nuVersaoServico == 1) {
			mtxtb011VersaoServico.setMigrado(1);
			Mtxtb011VersaoServicoPK pk = new Mtxtb011VersaoServicoPK();
			pk.setNuServico001(nuServico);
			pk.setNuVersaoServico(nuVersaoServico);
			mtxtb011VersaoServico.setId(pk);
			mtxtb011VersaoServico.setMtxtb001Servico(new Mtxtb001Servico(nuServico, "Valida"));
			mtxtb011VersaoServico.setDeXsdRequisicao("validacao_entradas/SIMTX_TYPES/Valida_Boleto/V1/Valida_Boleto.xsd");
			mtxtb011VersaoServico.setDeXsdResposta("validacao_entradas/SIMTX_TYPES/Valida_Boleto/V1/Valida_Boleto.xsd");
			mtxtb011VersaoServico.setDeXsltRequisicao("servico/Valida_Boleto_Npc/V1/validaBoletoNpc_Req.xsl");
			mtxtb011VersaoServico.setDeXsltResposta("servico/Valida_Boleto_Npc/V1/validaBoletoNpc_Resp.xsl");
			Mtxtb001Servico mtxtb001Servico = new Mtxtb001Servico();
			mtxtb001Servico.setNoServico("Valida Boleto NPC");
			mtxtb001Servico.setIcConfirmaTransacao(new BigDecimal(1));
			mtxtb011VersaoServico.setMtxtb001Servico(mtxtb001Servico);	

		}else if (nuServico == 110023) {
			servico = new Mtxtb001Servico();
			servico.setNuServico(110023L);

			mtxtb011VersaoServico = new Mtxtb011VersaoServico();
			Mtxtb011VersaoServicoPK versaoServicoPK = new Mtxtb011VersaoServicoPK();
			versaoServicoPK.setNuServico001(110023L);
			versaoServicoPK.setNuVersaoServico(1);
			mtxtb011VersaoServico.setId(versaoServicoPK);
			mtxtb011VersaoServico.setMtxtb001Servico(servico);
			mtxtb011VersaoServico.setDeXsltRequisicao("servico/Lista_Transacoes_Pendentes_Assinatura/V1/Lista_Transacoes_Pendentes_Assinatura_Req.xsl");
			mtxtb011VersaoServico.setDeXsltResposta("servico/Lista_Transacoes_Pendentes_Assinatura/V1/Lista_Transacoes_Pendentes_Assinatura_Resp.xsl");
			mtxtb011VersaoServico.setMigrado(1);
			
		}
		else if (nuServico == 110045) {
			mtxtb011VersaoServico.setMigrado(1);
			Mtxtb011VersaoServicoPK pk = new Mtxtb011VersaoServicoPK();
			pk.setNuServico001(nuServico);
			pk.setNuVersaoServico(nuVersaoServico);
			mtxtb011VersaoServico.setId(pk);
			mtxtb011VersaoServico.setMtxtb001Servico(new Mtxtb001Servico(nuServico, "Valida"));
			mtxtb011VersaoServico.setDeXsdRequisicao("validacao_entradas/SIMTX_TYPES/Valida_Boleto/V1/Valida_Boleto.xsd");
			mtxtb011VersaoServico.setDeXsdResposta("validacao_entradas/SIMTX_TYPES/Valida_Boleto/V1/Valida_Boleto.xsd");
			mtxtb011VersaoServico.setDeXsltRequisicao("servico/Valida_Boleto_Npc/V1/validaBoletoNpc_Req.xsl");
			mtxtb011VersaoServico.setDeXsltResposta("servico/Valida_Boleto_Npc/V1/validaBoletoNpc_Resp.xsl");
			Mtxtb001Servico mtxtb001Servico = new Mtxtb001Servico();
			mtxtb001Servico.setNoServico("Valida Boleto NPC");
			mtxtb001Servico.setIcConfirmaTransacao(new BigDecimal(1));
			mtxtb011VersaoServico.setMtxtb001Servico(mtxtb001Servico);	
		}
		else if(nuVersaoServico == 0 || nuServico == 0L) {
			mtxtb011VersaoServico = null;
		}
		else {
			mtxtb011VersaoServico.setMigrado(1);
			Mtxtb011VersaoServicoPK pk = new Mtxtb011VersaoServicoPK();
			pk.setNuServico001(nuServico);
			pk.setNuVersaoServico(nuVersaoServico);
			mtxtb011VersaoServico.setId(pk);
			mtxtb011VersaoServico.setMtxtb001Servico(new Mtxtb001Servico(nuServico, "Retomada Transacao"));
			mtxtb011VersaoServico.setDeXsdRequisicao("/validacao_entradas/SIMTX_TYPES/RetomadaTransacao/V1/retomarTransacao.xsd");
			mtxtb011VersaoServico.setDeXsdResposta("/validacao_entradas/SIMTX_TYPES/RetomadaTransacao/V1/retomarTransacao.xsd");
			mtxtb011VersaoServico.setDeXsltRequisicao("servico/Retomada_Transacao/V1/retomarTransacao.xsl");
			mtxtb011VersaoServico.setDeXsltResposta("servico/Retomada_Transacao/V1/retomarTransacao.xsl");
			Mtxtb001Servico mtxtb001Servico = new Mtxtb001Servico();
			mtxtb001Servico.setNoServico("Retomada Transacao");
			mtxtb001Servico.setIcConfirmaTransacao(new BigDecimal(1));
			mtxtb011VersaoServico.setMtxtb001Servico(mtxtb001Servico);	
		}
		
		return mtxtb011VersaoServico;
	}

	@Override
	public List<Mtxtb014Transacao> buscarTransacoesParaEnvioSicco(Date dataAtual) {
		return new ArrayList<>();
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


	@Override
	public Mtxtb040PrmtoPgtoContingencia buscarPrmtoPgtoCntngnciaPorCanal(Mtxtb040PrmtoPgtoContingenciaPK prmtoPgtoCntngncia) {
		if(prmtoPgtoCntngncia.getNuCanal004() == 0) {
			return null;
		}
		else if(prmtoPgtoCntngncia.getNuCanal004() == 1) {
			Mtxtb040PrmtoPgtoContingenciaPK contingenciaPK = new Mtxtb040PrmtoPgtoContingenciaPK();
			contingenciaPK.setIcTipoBoleto(TipoBoletoEnum.CAIXA.getCodigo());
			contingenciaPK.setIcOrigemContingencia(TipoContingenciaEnum.CAIXA.getCodigo());
			contingenciaPK.setNuCanal004(106);
			
			Mtxtb040PrmtoPgtoContingencia cntngncia = new Mtxtb040PrmtoPgtoContingencia();
			cntngncia.setId(contingenciaPK);
			cntngncia.setIcAutorizacaoContingencia(0);
			cntngncia.setValorMaximo(BigDecimal.valueOf(100000.00));
			cntngncia.setValorMinimo(BigDecimal.ZERO);
			cntngncia.setDhAtualizacao(new Date());
			return cntngncia;
		}
		else {
			Mtxtb040PrmtoPgtoContingenciaPK contingenciaPK = new Mtxtb040PrmtoPgtoContingenciaPK();
			contingenciaPK.setIcTipoBoleto(TipoBoletoEnum.CAIXA.getCodigo());
			contingenciaPK.setIcOrigemContingencia(TipoContingenciaEnum.CAIXA.getCodigo());
			contingenciaPK.setNuCanal004(106);
			
			Mtxtb040PrmtoPgtoContingencia cntngncia = new Mtxtb040PrmtoPgtoContingencia();
			cntngncia.setId(contingenciaPK);
			cntngncia.setIcAutorizacaoContingencia(1);
			cntngncia.setValorMaximo(BigDecimal.valueOf(100000.00));
			cntngncia.setValorMinimo(BigDecimal.ZERO);
			cntngncia.setDhAtualizacao(new Date());
			return cntngncia;
		}
	}


	@Override
	public Mtxtb006Mensagem buscarMensagemErroInterno() {
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
		String dataBaseAtual = "07/10/1997";
    	String dataBaseNova = "29/05/2022";
    	String rangeVencido = "3000";
    	String rangeAVencer = "5500";

    	SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
    	
    	ParametrosFatorVencimento parametros = new ParametrosFatorVencimento();
    	parametros.setDataBaseAtual(format.parse(dataBaseAtual));
    	parametros.setDataBaseNova(format.parse(dataBaseNova));
    	parametros.setRangeVencido(Integer.parseInt(rangeVencido));
    	parametros.setRangeAVencer(Integer.parseInt(rangeAVencer));
    	return parametros;
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
	
	
	private List<Mtxtb035TransacaoConta> mockListaTransacaoConta(Mtxtb035TransacaoConta consulta) {
		List<Mtxtb035TransacaoConta> lista = new ArrayList<>();

		if (consulta.getNumeroUnidade() == 1234) {
			for (int i = 0; i < 2; i++) {
				Mtxtb035TransacaoConta transacao = new Mtxtb035TransacaoConta();
				transacao.setNuNsuTransacao(1000 + i);
				transacao.setDataReferencia(new Date());
				Mtxtb016IteracaoCanal mtxtb016IteracaoCanal = new Mtxtb016IteracaoCanal();
				mtxtb016IteracaoCanal.setDeRecebimento(ConstantesXmlMockPagamentoBoleto.PAGAMENTO_SUCESSO_ENTRADA_1);
				transacao.setMtxtb016IteracaoCanal(mtxtb016IteracaoCanal);
				transacao.setMtxtb004Canal(new Mtxtb004Canal(1235L));
				transacao.setMtxtb001Servico(new Mtxtb001Servico(1L));
				lista.add(transacao);
			}
		}

		return lista;

	}
}
