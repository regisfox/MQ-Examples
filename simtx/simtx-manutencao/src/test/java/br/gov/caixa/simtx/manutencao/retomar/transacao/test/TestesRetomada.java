package br.gov.caixa.simtx.manutencao.retomar.transacao.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ ProcessadorRetomadaFluxoCoreTransacaoTest.class, ProcessadorRetomadaFluxoAgendamentoTest.class})
public class TestesRetomada {

	private TestesRetomada() {}
}
