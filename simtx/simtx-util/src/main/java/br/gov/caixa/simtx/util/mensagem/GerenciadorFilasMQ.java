package br.gov.caixa.simtx.util.mensagem;

import javax.ejb.Local;

import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb001Servico;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb024TarefaFila;
import br.gov.caixa.simtx.util.exception.ServicoException;

@Local
public interface GerenciadorFilasMQ {
	
	
	/**
	 * Envia mensagens na FilaMQ e aguarda a resposta.
	 * De acordo com as Propriedades.
	 * 
	 * @param mensagem
	 * @param PropriedadesMQ
	 * @return
	 * @throws Exception 
	 */
	public String executarServico(String mensagem, PropriedadesMQ propriedadesMQ) throws ServicoException;
	
	/**
	 * Envia mensagens na FilaMQ e aguarda a resposta.
	 * De acordo com o Servico.
	 * 
	 * @param mensagem
	 * @param Mtxtb001Servico
	 * @return
	 * @throws Exception 
	 */
	public String executar(String mensagem, Mtxtb001Servico servico) throws ServicoException;
	
	/**
	 * Envia mensagens na FilaMQ e aguarda a resposta.
	 * De acordo com a Tarefa.
	 * 
	 * @param mensagem
	 * @param Mtxtb024TarefaFila
	 * @return
	 * @throws Exception 
	 */
	public String executar(String mensagem, Mtxtb024TarefaFila tarefa) throws ServicoException;
	
	/**
	 * Envia mensagens na FilaMQ e aguarda a resposta.
	 * De acordo com a Tarefa.
	 * 
	 * @param mensagem
	 * @return
	 * @throws Exception 
	 */
	public String executarSicco(String mensagem) throws ServicoException;

}
