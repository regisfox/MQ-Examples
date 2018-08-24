package br.gov.caixa.simtx.util.mock.infra;

import javax.enterprise.inject.Alternative;

import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb001Servico;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb024TarefaFila;
import br.gov.caixa.simtx.util.mensagem.GerenciadorFilasMQ;
import br.gov.caixa.simtx.util.mensagem.PropriedadesMQ;

@Alternative
public class GerenciadorFilasMQMock implements GerenciadorFilasMQ {

	@Override
	public String executar(String mensagem, Mtxtb001Servico servico) {
		return executarServico(mensagem);
	}

	@Override
	public String executar(String mensagem, Mtxtb024TarefaFila tarefa) {
		return executarServico(mensagem);
	}

	@Override
	public String executarServico(String mensagem, PropriedadesMQ propriedadesMQ) {
		return executarServico(mensagem);
	}

	@Override
	public String executarSicco(String mensagem) {
		return executarServico(mensagem);
	}

	public String executarServico(String mensagem) {
		return "mensagem ok";
	}
	
}
