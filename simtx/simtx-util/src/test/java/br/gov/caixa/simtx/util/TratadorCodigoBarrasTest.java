package br.gov.caixa.simtx.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.util.Calendar;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;

import br.gov.caixa.simtx.persistencia.constante.MensagemRetorno;
import br.gov.caixa.simtx.persistencia.vo.ParametrosFatorVencimento;
import br.gov.caixa.simtx.util.exception.ServicoException;
import br.gov.caixa.simtx.util.mock.FornecedorDadosMock;
import br.gov.caixa.simtx.util.vo.CodigoBarras;

public class TratadorCodigoBarrasTest {

	private TratadorCodigoBarras tratadorCodigobarras;
	
	private final String codigoBarras = "10492772500025000008062480000100040992525250";
	
	
	@Before
	public void inicializarDados() {
		this.tratadorCodigobarras = new TratadorCodigoBarras();
		this.tratadorCodigobarras.setFornecedorDados(new FornecedorDadosMock());
	}
	
	@Test
	public void quebrarCodigoBarras() throws ServicoException {
		CodigoBarras codigoBarras = this.tratadorCodigobarras.quebrarCodigoBarras(this.codigoBarras);
		assertNotNull(codigoBarras);
	}
	
	@Test
	public void quebrarCodigoBarrasErroTamanho() {
		try {
			this.tratadorCodigobarras.quebrarCodigoBarras("123");
			fail();
		} 
		catch (ServicoException e) {
			assertEquals(e.getMensagem().getCodigoRetorno(),
					MensagemRetorno.CODIGO_BARRAS_INVALIDO.getCodigo().toString());
		}
	}
	
	@Test
	public void quebrarCodigoBarrasErro() {
		try {
			this.tratadorCodigobarras.quebrarCodigoBarras(null);
			fail();
		} 
		catch (ServicoException e) {
			assertEquals(e.getMensagem().getCodigoRetorno(),
					MensagemRetorno.ERRO_QUEBRAR_CODIGO_BARRAS.getCodigo().toString());
		}
	}
	
	@Test
	public void validarFatorVencimento() throws ServicoException {
		int fatorVencimento = this.tratadorCodigobarras.validarFatorVencimento("5000");
		assertNotEquals(0, fatorVencimento);
	}
	
	@Test
	public void validarFatorVencimentoErro() {
		try {
			this.tratadorCodigobarras.validarFatorVencimento("");
			fail();
		} 
		catch (ServicoException e) {
			assertEquals(e.getMensagem().getCodigoRetorno(),
					MensagemRetorno.FATOR_VENCIMENTO_INVALIDO.getCodigo().toString());
		}
	}
	
	@Test
	public void validarFatorVencimentoMenor1000() {
		try {
			this.tratadorCodigobarras.validarFatorVencimento("999");
			fail();
		} 
		catch (ServicoException e) {
			assertEquals(e.getMensagem().getCodigoRetorno(),
					MensagemRetorno.FATOR_VENCIMENTO_INVALIDO.getCodigo().toString());
		}
	}
	
	@Test
	public void validarFatorVencimentoMaior9999() {
		try {
			this.tratadorCodigobarras.validarFatorVencimento("10000");
		} 
		catch (ServicoException e) {
			assertEquals(e.getMensagem().getCodigoRetorno(),
					MensagemRetorno.FATOR_VENCIMENTO_INVALIDO.getCodigo().toString());
		}
	}
	
	@Test
	public void recuperarParametrosFatorVencimento() throws ServicoException {
		ParametrosFatorVencimento parametros = this.tratadorCodigobarras.recuperarParametrosFatorVencimento();
		assertNotNull(parametros);
	}
	
	@Test
	public void recuperarParametrosFatorVencimentoErro() throws ServicoException {
		ParametrosFatorVencimento parametros = this.tratadorCodigobarras.recuperarParametrosFatorVencimento();
		assertNotNull(parametros);
	}
	
	@Test
	public void atualizarDataBaseAtual() throws ServicoException {
		this.tratadorCodigobarras.atualizarDataBaseAtual(new Date());
	}
	
	@Test
	public void atualizarDataBaseAtualErro() throws ServicoException {
		try {
			this.tratadorCodigobarras.atualizarDataBaseAtual(null);
		} 
		catch (ServicoException e) {
			assertEquals(e.getMensagem().getCodigoRetorno(),
					MensagemRetorno.ERRO_ATUALIZA_DATA_BASE_ATUAL.getCodigo().toString());
		}
	}
	
	@Test
	public void atualizarDataBaseNova() throws ServicoException {
		this.tratadorCodigobarras.atualizarDataBaseNova(new Date());
	}
	
	@Test
	public void atualizarDataBaseNovaErro() throws ServicoException {
		try {
			this.tratadorCodigobarras.atualizarDataBaseNova(null);
		} 
		catch (ServicoException e) {
			assertEquals(e.getMensagem().getCodigoRetorno(),
					MensagemRetorno.ERRO_ATUALIZA_DATA_BASE_NOVA.getCodigo().toString());
		}
	}
	
	@Test
	public void calcularDataBase() throws ServicoException {
		ParametrosFatorVencimento parametros = this.tratadorCodigobarras.recuperarParametrosFatorVencimento();
		assertNotNull(parametros);
		Date data = this.tratadorCodigobarras.calcularDataBase(parametros, 7725);
		assertEquals(data, parametros.getDataBase());
	}
	
	@Test
	public void calcularDataBaseNovaData() throws ServicoException {
		ParametrosFatorVencimento parametros = this.tratadorCodigobarras.recuperarParametrosFatorVencimento();
		assertNotNull(parametros);
		Date data = this.tratadorCodigobarras.calcularDataBase(parametros, 2000);
		assertEquals(data, parametros.getDataBaseNova());
	}
	
	@Test
	public void calcularDataBaseRangeSeguranca() throws ServicoException {
		try {
			ParametrosFatorVencimento parametros = this.tratadorCodigobarras.recuperarParametrosFatorVencimento();
			assertNotNull(parametros);
			Calendar dataBase = Calendar.getInstance();
			dataBase.setTime(parametros.getDataBase());
			
			Date data = this.tratadorCodigobarras.calcularDataBase(parametros, 4500);
			assertNotNull(data);
		} 
		catch (ServicoException e) {
			assertEquals(e.getMensagem().getCodigoRetorno(),
					MensagemRetorno.ERRO_VERIFICAR_DATA_BASE_NOVA.getCodigo().toString());
		}
	}
	
	@Test
	public void calcularDataBaseErro() throws ServicoException {
		try {
			this.tratadorCodigobarras.calcularDataBase(null, 0);
		} 
		catch (ServicoException e) {
			assertEquals(e.getMensagem().getCodigoRetorno(),
					MensagemRetorno.ERRO_VERIFICAR_DATA_BASE_NOVA.getCodigo().toString());
		}
	}
	
	@Test
	public void retornarValoresCodigoBarras() throws ServicoException {
		this.tratadorCodigobarras.retornarValoresCodigoBarras(this.codigoBarras);
	}

}
