package br.gov.caixa.simtx.util.mock;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.enterprise.inject.Alternative;

import br.gov.caixa.simtx.persistencia.agendamento.entidade.Mtxtb034TransacaoAgendamento;
import br.gov.caixa.simtx.persistencia.cache.agendamento.FornecedorDadosAgendamento;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb001Servico;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb004Canal;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb016IteracaoCanal;

@Alternative
public class FornecedorDadosAgendamentoMock implements FornecedorDadosAgendamento, Serializable {

	private static final long serialVersionUID = 1L;

	public static String pagamentoTeste = "<ns:PAGAMENTO_BOLETO_ENTRADA> 	<HEADER> 		<SERVICO> 			<CODIGO>110039</CODIGO> 			<VERSAO>1</VERSAO> 		</SERVICO> 		<CANAL> 			<SIGLA>SIMAA</SIGLA> 			<DATAHORA>20180101210900</DATAHORA> 			<NSU>12345</NSU> 		</CANAL> 		<MEIOENTRADA>0</MEIOENTRADA> 		<DATA_REFERENCIA>20180510</DATA_REFERENCIA> 	</HEADER> 	<NSUMTX_ORIGEM>17036</NSUMTX_ORIGEM> 	<AGENDAMENTO>NAO</AGENDAMENTO> 	<CONTA> 		<CONTA_SIDEC> 			<UNIDADE>0</UNIDADE> 			<OPERACAO>0</OPERACAO> 			<CONTA>0</CONTA> 			<DV>0</DV> 		</CONTA_SIDEC> 	</CONTA> 	<PAGAMENTO_BOLETO> 		<INFORMACOES_BOLETO> 			<DATA_VENCIMENTO>2001-01-01</DATA_VENCIMENTO> 			<LINHA_DIGITAVEL>23790504004188062212540008109205175660000019900</LINHA_DIGITAVEL> 		</INFORMACOES_BOLETO> 	</PAGAMENTO_BOLETO> </ns:PAGAMENTO_BOLETO_ENTRADA>";

	/**
	 * {@inheritDoc}
	 */
	public Mtxtb034TransacaoAgendamento salvarTransacaoAgendamento(Mtxtb034TransacaoAgendamento transacaoAgendamento) {
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	public List<Mtxtb034TransacaoAgendamento> buscaTransacoesAgendamento(Mtxtb034TransacaoAgendamento transacaoAgendamento) {
		return this.listaAgendamentos(transacaoAgendamento);
	}

	public List<Mtxtb034TransacaoAgendamento> buscaTransacoesAgendamentoPorData(Date date) {
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	public Mtxtb034TransacaoAgendamento buscaTransacaoAgendamentoPorPK(Mtxtb034TransacaoAgendamento transacaoAgendamento) {
		return obterMtxtb034TransacaoAgendamento(transacaoAgendamento.getNuNsuTransacaoAgendamento());
	}

	/**
	 * {@inheritDoc}
	 */
	public void alterarTransacao(Mtxtb034TransacaoAgendamento transacaoAgendamento) {
	}

	@Override
	public List<Mtxtb034TransacaoAgendamento> buscaTransacoesAgendamentoPeriodo(Mtxtb034TransacaoAgendamento transacaoAgendamento, Date periodoInicio,
			Date periodoFinal) {
		return null;
	}

	@Override
	public List<Mtxtb034TransacaoAgendamento> buscaTransacoesAgendamentoNaoEfetivados(Mtxtb034TransacaoAgendamento transacaoAgendamento) {
		return listaAgendamentos(transacaoAgendamento);
	}

	private List<Mtxtb034TransacaoAgendamento> listaAgendamentos(Mtxtb034TransacaoAgendamento consulta) {
		List<Mtxtb034TransacaoAgendamento> agendamentosCancelados = new ArrayList<>();
		if (consulta.getNuUnidade() == 625) {
			for (int i = 0; i < 2; i++) {
				agendamentosCancelados.add(obterMtxtb034TransacaoAgendamento(Long.valueOf(i)));
			}
		}

		return agendamentosCancelados;

	}
	
	
	private Mtxtb034TransacaoAgendamento obterMtxtb034TransacaoAgendamento(Long nsu) {
		Mtxtb034TransacaoAgendamento mtxtb034TransacaoAgendamento = new Mtxtb034TransacaoAgendamento();
		mtxtb034TransacaoAgendamento.setNuNsuTransacaoAgendamento(nsu);
		mtxtb034TransacaoAgendamento.setDtReferencia(new Date());
		mtxtb034TransacaoAgendamento.setDtEfetivacao(new Date());
		mtxtb034TransacaoAgendamento.setNuServico(123);
		mtxtb034TransacaoAgendamento.setNuCanal(1);
		mtxtb034TransacaoAgendamento.setValorTransacao(new BigDecimal(123));
		mtxtb034TransacaoAgendamento.setNuUnidade(625);
		mtxtb034TransacaoAgendamento.setNuProduto(123456);
		mtxtb034TransacaoAgendamento.setNuConta(001520);
		mtxtb034TransacaoAgendamento.setDvConta(0121);
		mtxtb034TransacaoAgendamento.setDeXmlAgendamento(pagamentoTeste);
		mtxtb034TransacaoAgendamento.setMtxtb004Canal(new Mtxtb004Canal(106L));
		mtxtb034TransacaoAgendamento.setMtxtb001Servico(new Mtxtb001Servico(110032L));
		return mtxtb034TransacaoAgendamento;
	}

	@Override
	public Mtxtb016IteracaoCanal buscaUltimoMtxtb016IteracaoCanal(long nsu) {
		// TODO Auto-generated method stub
		return null;
	}
}