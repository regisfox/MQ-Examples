package br.gov.caixa.simtx.util.gerenciador.servico;

import javax.ejb.Local;

import br.gov.caixa.simtx.util.ParametrosAdicionais;

@Local
public interface ProcessadorServicos {
	
	public void processar(ParametrosAdicionais parametrosAdicionais);

}
