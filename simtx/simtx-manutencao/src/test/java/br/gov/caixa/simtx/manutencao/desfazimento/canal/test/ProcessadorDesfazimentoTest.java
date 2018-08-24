package br.gov.caixa.simtx.manutencao.desfazimento.canal.test;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import br.gov.caixa.simtx.manutencao.desfazimento.canal.ProcessadorDesfazimento;
import br.gov.caixa.simtx.util.ParametrosAdicionais;
@RunWith(MockitoJUnitRunner.class)
@FixMethodOrder(MethodSorters.JVM)
public class ProcessadorDesfazimentoTest {
	
	@InjectMocks
	ProcessadorDesfazimento processadorDesfazimento = Mockito.mock(ProcessadorDesfazimento.class); 

	@Before
	public void iniciarDados() {
		processadorDesfazimento = new ProcessadorDesfazimento();
	}
	@Test
	public void test() {
		ParametrosAdicionais parametrosAdicionais = new ParametrosAdicionais();
		parametrosAdicionais.setXmlMensagem("");
		processadorDesfazimento.processar(parametrosAdicionais);
	}

}
