package br.gov.caixa.simtx.util.tarefa;

import java.util.ArrayList;
import java.util.List;

import br.gov.caixa.simtx.persistencia.constante.Constantes;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb002Tarefa;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb003ServicoTarefa;

public class TarefaUtils {

	private TarefaUtils() {
		//nao deve existir instancia
	}
	
	public static List<Mtxtb002Tarefa> filtrarTarefaNegocial(List<Mtxtb002Tarefa> tarefas) {
		List<Mtxtb002Tarefa> tarefasNegociais = new ArrayList<>();
		
		for(Mtxtb002Tarefa tarefa : tarefas) {
			if(Constantes.IC_TAREFA_NEGOCIAL.equals(tarefa.getIcTipoTarefa())) {
				tarefasNegociais.add(tarefa);
			}
		}
		
		return tarefasNegociais;
	}

	public static List<Mtxtb003ServicoTarefa> filtrarServicoTarefaNegocial(List<Mtxtb003ServicoTarefa> tarefas) {
		List<Mtxtb003ServicoTarefa> tarefasNegociais = new ArrayList<>();
		
		for(Mtxtb003ServicoTarefa tarefa : tarefas) {
			if(tarefa.isNegocial()) {
				tarefasNegociais.add(tarefa);
			}
		}
		
		return tarefasNegociais;
	}
}
