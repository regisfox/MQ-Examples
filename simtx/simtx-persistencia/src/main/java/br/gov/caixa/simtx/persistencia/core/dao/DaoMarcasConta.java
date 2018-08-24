package br.gov.caixa.simtx.persistencia.core.dao;

import java.util.List;

import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb032MarcaConta;

public interface DaoMarcasConta {

	 public List<Mtxtb032MarcaConta> buscarMarcasContas(Mtxtb032MarcaConta mtxtb032MarcasConta);
}
