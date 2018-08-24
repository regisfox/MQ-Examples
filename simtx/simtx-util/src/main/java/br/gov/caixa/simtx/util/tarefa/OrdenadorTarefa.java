package br.gov.caixa.simtx.util.tarefa;

import java.util.Comparator;

import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb003ServicoTarefa;

public class OrdenadorTarefa implements Comparator<Mtxtb003ServicoTarefa> {

	@Override
	public int compare(Mtxtb003ServicoTarefa um, Mtxtb003ServicoTarefa dois) {
		return Integer.compare(um.getNuSequenciaExecucao(), dois.getNuSequenciaExecucao());
	}

}
