package br.gov.caixa.simtx.util.mock;

import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb014Transacao;
import br.gov.caixa.simtx.util.integrador.sicco.ProcessadorEnvioSicco;

public class ProcessadorEnvioSiccoMock extends ProcessadorEnvioSicco {

	private static final long serialVersionUID = 5185052563456665255L;

	@Override
	public void processarEnvioOnline(Mtxtb014Transacao transacao, String tipoEnvio) {
		/**
		 * ProcessadorEnvioSicco ainda nao feito testes Junit.
		 */
	}
	
	@Override
	public void processarEnvioPendentes() {
		/**
		 * ProcessadorEnvioSicco ainda nao feito testes Junit.
		 */
	}
	
}
