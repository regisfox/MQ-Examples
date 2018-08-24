package br.gov.caixa.simtx.util.mock;

import java.math.BigDecimal;

import br.gov.caixa.simtx.persistencia.constante.Constantes;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb004Canal;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb008MeioEntrada;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb011VersaoServico;
import br.gov.caixa.simtx.util.exception.ServicoException;
import br.gov.caixa.simtx.util.gerenciador.ValidadorRegrasNegocio;

public class ValidadorRegrasNegocioMock extends ValidadorRegrasNegocio {

	private static final long serialVersionUID = 1L;
	
	@Override
	public BigDecimal situacaoFinalTransacao(Mtxtb011VersaoServico servico) {
		return Constantes.IC_SERVICO_FINALIZADO;
	}
	
	@Override
	public void validarRegrasMigrado(String xml, Mtxtb004Canal canal, Mtxtb008MeioEntrada meioEntrada,
			Mtxtb011VersaoServico versaoServico) throws ServicoException {
		/**
		 * ValidadorRegrasNegocio ainda nao foi feito testes JUnit.
		 */
	}
	

}
