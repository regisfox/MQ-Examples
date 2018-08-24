package br.gov.caixa.simtx.persistencia.core.dao;

import java.util.List;

import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb032MarcaConta;

public interface DaoServicoMarcas {

	public List<Mtxtb032MarcaConta> buscaServicos(long nuServico);
}
