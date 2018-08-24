package br.gov.caixa.simtx.util.mock;

import java.util.List;

import javax.enterprise.inject.Alternative;
import javax.inject.Named;

import br.gov.caixa.simtx.persistencia.atualizador.dados.AtualizadorDadosTransacao;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb014Transacao;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb015SrvcoTrnsoTrfa;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb016IteracaoCanal;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb035TransacaoConta;

@Alternative
public class AtualizadorDadosTransacaoMock extends AtualizadorDadosTransacao {
	private static final long serialVersionUID = 4092539186751656469L;
	@Override
	public boolean atualizarDadosTransacaoCore(Mtxtb014Transacao transacao, List<Mtxtb015SrvcoTrnsoTrfa> listaTarefas, Mtxtb016IteracaoCanal iteracaoCanal,
		Mtxtb035TransacaoConta transacaoConta) {
		boolean statusAtualizacao = false;
		if (transacao.getNuNsuTransacao()==226945) {
			statusAtualizacao = true;	
		}else if(transacao.getNuNsuTransacao()==226946) {
			statusAtualizacao = false;
		}
		return statusAtualizacao;
	}	
}
