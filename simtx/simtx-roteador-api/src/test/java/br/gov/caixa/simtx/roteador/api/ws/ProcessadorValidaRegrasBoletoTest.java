package br.gov.caixa.simtx.roteador.api.ws;


import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map.Entry;

import javax.xml.bind.JAXBException;

import org.junit.Before;
import org.junit.Test;

import br.gov.caixa.simtx.persistencia.constante.Constantes;
import br.gov.caixa.simtx.persistencia.constante.MensagemRetorno;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb006Mensagem;
import br.gov.caixa.simtx.persistencia.vo.TipoBoletoEnum;
import br.gov.caixa.simtx.roteador.api.repositorio.FornecedorDadosMock;
import br.gov.caixa.simtx.roteador.api.util.ConstantesRoteadorWeb;
import br.gov.caixa.simtx.roteador.api.util.SimtxConfigMock;
import br.gov.caixa.simtx.roteador.api.ws.matriz.LeitorMatrizConsultaRegrasBoletoMock;
import br.gov.caixa.simtx.roteador.api.ws.matriz.LeitorMatrizValidaRegrasBoletoMock;
import br.gov.caixa.simtx.roteador.api.ws.xml.Header;
import br.gov.caixa.simtx.roteador.api.ws.xml.valida.ValidaRegrasBoletoEntrada;
import br.gov.caixa.simtx.util.exception.ServicoException;

public class ProcessadorValidaRegrasBoletoTest {
	
	private ProcessadorValidaRegrasBoleto validaRegrasBoleto;
	
	private Header header;
	
	private String xml = 
			"<validaregrasboleto:SERVICO_ENTRADA xmlns:validaregrasboleto=\"http://caixa.gov.br/simtx/valida_boleto/regras/v1/ns\" xmlns:sibar_base=\"http://caixa.gov.br/sibar\">\r\n" + 
			"	<sibar_base:HEADER>\r\n" + 
			"		<VERSAO>1.0</VERSAO>\r\n" + 
			"		<USUARIO_SERVICO>smtxsd01</USUARIO_SERVICO>\r\n" + 
			"		<OPERACAO>VALIDA_REGRAS_BOLETO</OPERACAO>\r\n" + 
			"		<SISTEMA_ORIGEM>SIMAA</SISTEMA_ORIGEM>\r\n" + 
			"		<UNIDADE>625</UNIDADE>\r\n" + 
			"		<DATA_HORA>20180423154715</DATA_HORA>\r\n" + 
			"		<ID_PROCESSO>CONTROLE_LIMITE. PASSO 4: VALIDA_REGRAS_BOLETO</ID_PROCESSO>\r\n" + 
			"	</sibar_base:HEADER>\r\n" + 
			"	<DADOS>\r\n" + 
			"		<NSU_TRANSACAO>1</NSU_TRANSACAO>\r\n" + 
			"		<NU_CANAL>106</NU_CANAL>\r\n" + 
			"		<CODIGO_BARRAS>10498772500001000008062480000100040992479712</CODIGO_BARRAS>\r\n" + 
			"		<ULTIMA_PARCELA_VIAVEL>N</ULTIMA_PARCELA_VIAVEL>\r\n" + 
			"		<PAGAMENTO_PARCIAL>N</PAGAMENTO_PARCIAL>\r\n" + 
			"		<QUANTIDADE_PAGAMENTO_PARCIAL_PERMITIDO>1</QUANTIDADE_PAGAMENTO_PARCIAL_PERMITIDO>\r\n" + 
			"		<NUMERO_PARCELA_ATUAL>1</NUMERO_PARCELA_ATUAL>\r\n" + 
			"		<RECEBE_VALOR_DIVERGENTE>NAO_ACEITAR</RECEBE_VALOR_DIVERGENTE>\r\n" + 
			"		<ESPECIE>BLOQUETO</ESPECIE>\r\n" + 
			"		<TIPO_CONTINGENCIA>SEM_CONTINGENCIA</TIPO_CONTINGENCIA>\r\n" + 
			"		<NOVA_PLATAFORMA_DE_COBRANCA>S</NOVA_PLATAFORMA_DE_COBRANCA>\r\n" + 
			"		<DATA_PAGAMENTO>"+new SimpleDateFormat("yyyy-MM-dd").format(new Date())+"</DATA_PAGAMENTO>\r\n" + 
			"		<DATA_VENCIMENTO_UTIL>"+new SimpleDateFormat("yyyy-MM-dd").format(new Date())+"</DATA_VENCIMENTO_UTIL>\r\n" + 
			"		<VALOR_MINIMO_CONSULTA>1991.73</VALOR_MINIMO_CONSULTA>\r\n" + 
			"		<VALOR_MAXIMO_CONSULTA>1991.73</VALOR_MAXIMO_CONSULTA>\r\n" + 
			"		<VALOR_TOTAL_CALCULADO>1991.73</VALOR_TOTAL_CALCULADO>\r\n" + 
			"		<VALOR_PAGAR>1991.73</VALOR_PAGAR>\r\n" + 
			"		<VALOR_NOMINAL>0.0</VALOR_NOMINAL>\r\n" + 
			"	</DADOS>\r\n" + 
			"</validaregrasboleto:SERVICO_ENTRADA>";
	
	
	@Before
	public void inicializarDados() throws JAXBException {
		this.validaRegrasBoleto = new ProcessadorValidaRegrasBoleto();
		FornecedorDadosMock fornecedorDadosMock = new FornecedorDadosMock();
		SimtxConfigMock simtxConfigMock = new SimtxConfigMock();
		LeitorMatrizValidaRegrasBoletoMock regrasBoletoMock = new LeitorMatrizValidaRegrasBoletoMock();
		
		LeitorMatrizConsultaRegrasBoletoMock boletoMock = new LeitorMatrizConsultaRegrasBoletoMock();
		boletoMock.setFornecedorDados(fornecedorDadosMock);
		regrasBoletoMock.setConsultaRegrasBoleto(boletoMock);
		regrasBoletoMock.setFornecedorDados(fornecedorDadosMock);
		
		this.validaRegrasBoleto.setFornecedorDados(fornecedorDadosMock);
		this.validaRegrasBoleto.setSimtxConfig(simtxConfigMock);
		this.validaRegrasBoleto.setLeitorMatriz(regrasBoletoMock);
		
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddhhmmss");
		this.header = new Header();
		this.header.setVersao("1");
		this.header.setOperacao("VALIDA_REGRAS_BOLETO");
		this.header.setDataHora(format.format(new Date()));
	}
	
	@Test
	public void transformarXmlParaJAXB() throws ServicoException {
		ValidaRegrasBoletoEntrada valida = this.validaRegrasBoleto.transformarXmlParaJAXB(this.xml);
		assertNotNull(valida);
	}
	
	@Test
	public void transformarXmlParaJAXBErro() throws ServicoException {
		try {
			this.validaRegrasBoleto.transformarXmlParaJAXB(this.xml.replace("NSU_TRANSACAO", "NSU"));
			fail();
		} 
		catch (ServicoException e) {
			assertEquals(e.getMensagem().getCodigoRetorno(),
					MensagemRetorno.WS_LAYOUT_INVALIDO.getCodigo().toString());
		}
	}
	
	@Test
	public void validarDataVencimento() throws ServicoException {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String dtAtual = format.format(new Date());
		this.validaRegrasBoleto.validarDataVencimento(dtAtual, dtAtual);
	}
	

	@Test
	public void validarDataVencimentoErro() throws ServicoException {
		try {
			SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
			String dtAtual = format.format(new Date());
			this.validaRegrasBoleto.validarDataVencimento(dtAtual, dtAtual);
			fail();
		} 
		catch (ServicoException e) {
			assertEquals(e.getMensagem().getCodigoRetorno(),
					MensagemRetorno.LEIAUTE_MENSAGEM_INVALIDA.getCodigo().toString());
		}
	}
	
	@Test
	public void validarBoletoVencido() {
		try {
			Calendar data = Calendar.getInstance();
			data.add(Calendar.DAY_OF_MONTH, 1);
			
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			String dtFutura = format.format(data.getTime());
			
			data.add(Calendar.DAY_OF_MONTH, -2);
			String dtVencimento = format.format(data.getTime());
			
			this.validaRegrasBoleto.validarDataVencimento(dtFutura, dtVencimento);
			fail();
		} 
		catch (ServicoException e) {
			assertEquals(e.getMensagem().getCodigoRetorno(),
					MensagemRetorno.WS2_BOLETO_VENCIDO.getCodigo().toString());
		}
	}
	
	@Test
	public void validarDataPagamentoPosterior() {
		try {
			Calendar data = Calendar.getInstance();
			data.add(Calendar.DAY_OF_MONTH, 1);
			
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			String dtPagamento = format.format(data.getTime());
			
			String dtVencimento = format.format(new Date());
			
			this.validaRegrasBoleto.validarDataVencimento(dtPagamento, dtVencimento);
			fail();
		} 
		catch (ServicoException e) {
			assertEquals(e.getMensagem().getCodigoRetorno(),
					MensagemRetorno.WS2_DATA_POSTERIOR_VENCIMENTO.getCodigo().toString());
		}
	}
	
	@Test
	public void validarDataPagamentoIgualDataVencimento() {
		try {
			Calendar data = Calendar.getInstance();
			data.add(Calendar.DAY_OF_MONTH, 2);
			
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			String dtPagamento = format.format(data.getTime());
			
			data.add(Calendar.DAY_OF_MONTH, -1);
			String dtVencimento = format.format(data.getTime());
			
			this.validaRegrasBoleto.validarDataVencimento(dtPagamento, dtVencimento);
			fail();
		} 
		catch (ServicoException e) {
			assertEquals(e.getMensagem().getCodigoRetorno(),
					MensagemRetorno.WS2_DATA_POSTERIOR_VENCIMENTO.getCodigo().toString());
		}
	}
	
	@Test
	public void validarDataPagamentoAnterior() {
		try {
			Calendar data = Calendar.getInstance();
			data.add(Calendar.DAY_OF_MONTH, -1);
			
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			String dtPagamento = format.format(data.getTime());
			
			String dtVencimento = format.format(new Date());
			
			this.validaRegrasBoleto.validarDataVencimento(dtPagamento, dtVencimento);
			fail();
		} 
		catch (ServicoException e) {
			assertEquals(e.getMensagem().getCodigoRetorno(),
					MensagemRetorno.WS2_DATA_PAGAMENTO_ANTERIOR_ATUAL.getCodigo().toString());
		}
	}
	
	@Test
	public void validarSaidaMatriz() throws ServicoException {
		ValidaRegrasBoletoEntrada valida = this.validaRegrasBoleto.transformarXmlParaJAXB(this.xml);
		assertNotNull(valida);
		this.validaRegrasBoleto.validarSaidaMatriz(ConstantesRoteadorWeb.QUALQUER_VALOR, valida.getDados());
	}
	
	@Test
	public void validarSaidaMatrizValorCalculado() throws ServicoException {
		ValidaRegrasBoletoEntrada valida = this.validaRegrasBoleto.transformarXmlParaJAXB(this.xml);
		assertNotNull(valida);
		this.validaRegrasBoleto.validarSaidaMatriz(ConstantesRoteadorWeb.VALOR_CALCULADO, valida.getDados());
	}
	
	@Test
	public void validarSaidaMatrizMinimoMaximo() throws ServicoException {
		ValidaRegrasBoletoEntrada valida = this.validaRegrasBoleto.transformarXmlParaJAXB(this.xml);
		assertNotNull(valida);
		this.validaRegrasBoleto.validarSaidaMatriz(ConstantesRoteadorWeb.MINIMO_E_MAXIMO, valida.getDados());
	}
	
	@Test
	public void validarSaidaMatrizMinimo() throws ServicoException {
		ValidaRegrasBoletoEntrada valida = this.validaRegrasBoleto.transformarXmlParaJAXB(this.xml);
		assertNotNull(valida);
		this.validaRegrasBoleto.validarSaidaMatriz(ConstantesRoteadorWeb.SOMENTE_MINIMO, valida.getDados());
	}
	
	@Test
	public void validarSaidaMatrizValorBarra() throws ServicoException {
		ValidaRegrasBoletoEntrada valida = this.validaRegrasBoleto.transformarXmlParaJAXB(this.xml);
		assertNotNull(valida);
		this.validaRegrasBoleto.validarSaidaMatriz(ConstantesRoteadorWeb.VALOR_BARRA, valida.getDados());
	}
	
	@Test
	public void validarSaidaMatrizNaoReceber() throws ServicoException {
		try {
			ValidaRegrasBoletoEntrada valida = this.validaRegrasBoleto.transformarXmlParaJAXB(this.xml);
			assertNotNull(valida);
			this.validaRegrasBoleto.validarSaidaMatriz(ConstantesRoteadorWeb.NAO_RECEBER, valida.getDados());
			fail();
		} 
		catch (ServicoException e) {
			assertEquals(e.getMensagem().getCodigoRetorno(),
					MensagemRetorno.WS1_NAO_RECEBER_BOLETO.getCodigo().toString());
		}
	}
	
	@Test
	public void validarValorPagar() {
		try {
			this.validaRegrasBoleto.validarValorPagar(BigDecimal.ZERO);
			fail();
		} 
		catch (ServicoException e) {
			assertEquals(e.getMensagem().getCodigoRetorno(),
					MensagemRetorno.WS2_VALOR_PAGAR_ZERADO.getCodigo().toString());
		}
	}
	
	@Test
	public void validarValorCalculado() {
		try {
			this.validaRegrasBoleto.validarValorCalculado(BigDecimal.ONE, BigDecimal.ZERO);
			fail();
		}
		catch (ServicoException e) {
			assertEquals(e.getMensagem().getCodigoRetorno(),
					MensagemRetorno.WS2_VALOR_PAGO_DIF_VALOR_CALCULADO.getCodigo().toString());
		}
	}

	@Test
	public void validarMinimoMaximoMenor() {
		try {
			this.validaRegrasBoleto.validarMinimoMaximo(BigDecimal.ZERO, BigDecimal.ONE, BigDecimal.ONE);
			fail();
		}
		catch (ServicoException e) {
			assertEquals(e.getMensagem().getCodigoRetorno(),
					MensagemRetorno.WS2_VALOR_NAO_ESTA_MINIMO_MAXIMO.getCodigo().toString());
		}
	}
	
	@Test
	public void validarMinimoMaximoMaior() {
		try {
			this.validaRegrasBoleto.validarMinimoMaximo(BigDecimal.valueOf(2), BigDecimal.ONE, BigDecimal.ONE);
			fail();
		}
		catch (ServicoException e) {
			assertEquals(e.getMensagem().getCodigoRetorno(),
					MensagemRetorno.WS2_VALOR_NAO_ESTA_MINIMO_MAXIMO.getCodigo().toString());
		}
	}
	
	@Test
	public void validarSomenteMinimo() {
		try {
			this.validaRegrasBoleto.validarSomenteMinimo(BigDecimal.ZERO, BigDecimal.ONE);
			fail();
		}
		catch (ServicoException e) {
			assertEquals(e.getMensagem().getCodigoRetorno(),
					MensagemRetorno.WS2_VALOR_PAGO_MENOR_MINIMO.getCodigo().toString());
		}
	}
	
	@Test
	public void montarResposta() throws ServicoException {
		String tipoBaixa = "BAIXA_INTEGRAL";
		String valida = this.validaRegrasBoleto.montarResposta(tipoBaixa, this.header);
		assertNotEquals("", valida);
	}
	
	@Test
	public void montarRespostaErro() throws ServicoException {
		try {
			this.validaRegrasBoleto.montarResposta(null, null);
			fail();
		} 
		catch (ServicoException e) {
			assertEquals(ConstantesRoteadorWeb.ERRO_CONSULTA, e.getMensagem().getCodigoRetorno());
		}
	}
	
	@Test
	public void tratarExcecaoOK() {
		Mtxtb006Mensagem mensagem = new Mtxtb006Mensagem();
		mensagem.setCoMensagem(MensagemRetorno.WS2_VALOR_PAGO_MENOR_MINIMO.getCodigo().toString());
		mensagem.setDeMensagemNegocial("TESTE TRATAR EXCECAO");
		ServicoException e = new ServicoException(mensagem, Constantes.ORIGEM_SIMTX);
		
		String consulta = this.validaRegrasBoleto.tratarExcecao(e, this.header);
		assertNotEquals(ConstantesRoteadorWeb.MSG_GENERICA, consulta);
	}
	
	@Test
	public void tratarExcecaoErro() {
		String consulta = this.validaRegrasBoleto.tratarExcecao(null, this.header);
		assertEquals(ConstantesRoteadorWeb.MSG_GENERICA, consulta);
	}
	
	@Test
	public void recuperarValorMatriz() throws ServicoException {
		ValidaRegrasBoletoEntrada valida = this.validaRegrasBoleto.transformarXmlParaJAXB(this.xml);
		Entry<String, String> saidaMatriz = this.validaRegrasBoleto.recuperarValorMatriz(valida);
		assertNotNull(saidaMatriz);
	}
	
	@Test
	public void recuperarValorMatrizValorMaior() throws ServicoException {
		ValidaRegrasBoletoEntrada valida = this.validaRegrasBoleto.transformarXmlParaJAXB(this.xml);
		valida.getDados().setValorPagar(BigDecimal.valueOf(1000.00));
		Entry<String, String> saidaMatriz = this.validaRegrasBoleto.recuperarValorMatriz(valida);
		assertNotNull(saidaMatriz);
	}
	
	@Test
	public void recuperarValorMatrizErro() throws ServicoException {
		try {
			this.validaRegrasBoleto.recuperarValorMatriz(null);
			fail();
		} 
		catch (ServicoException e) {
			assertEquals(e.getMensagem().getCodigoRetorno(),
					MensagemRetorno.LEIAUTE_MENSAGEM_INVALIDA.getCodigo().toString());
		}
	}
	
	@Test
	public void recuperarValorMatrizInexistente() throws ServicoException {
		try {
			ValidaRegrasBoletoEntrada valida = this.validaRegrasBoleto.transformarXmlParaJAXB(this.xml);
			valida.getDados().setNovaPlataformaCobranca("N");
			valida.getDados().setTipoContingencia(ConstantesRoteadorWeb.CONTINGENCIA_CIP);;
			this.validaRegrasBoleto.recuperarValorMatriz(valida);
			fail();
		} 
		catch (ServicoException e) {
			assertEquals(e.getMensagem().getCodigoRetorno(),
					MensagemRetorno.WS_NAO_RECUPEROU_DADOS_MATRIZ.getCodigo().toString());
		}
	}
	
	@Test
	public void verificarTipoBoletoCaixa() {
		String codBarras = "10498772500001000008062480000100040992479712";
		int tipoBoleto = this.validaRegrasBoleto.verificarTipoBoleto(BigDecimal.ONE, codBarras);
		assertNotNull(tipoBoleto);
		assertEquals(TipoBoletoEnum.CAIXA.getCodigo(), tipoBoleto);
	}
	
	@Test
	public void verificarTipoBoletoOutrosBancos() {
		String codBarras = "03398772500001000008062480000100040992479712";
		int tipoBoleto = this.validaRegrasBoleto.verificarTipoBoleto(BigDecimal.ONE, codBarras);
		assertNotNull(tipoBoleto);
		assertEquals(TipoBoletoEnum.OUTROS_BANCOS.getCodigo(), tipoBoleto);
	}
	
	@Test
	public void verificarTipoBoletoOutrosBoleted() {
		String codBarras = "03398772500001000008062480000100040992479712";
		int tipoBoleto = this.validaRegrasBoleto.verificarTipoBoleto(BigDecimal.valueOf(300000.000), codBarras);
		assertNotNull(tipoBoleto);
		assertEquals(TipoBoletoEnum.BOLETED.getCodigo(), tipoBoleto);
	}
	
	@Test
	public void verificarValorMinimoMaximoOK() throws ServicoException {
		this.validaRegrasBoleto.verificarValorMinimoMaximo(BigDecimal.ONE, BigDecimal.ZERO, BigDecimal.ONE);
	}
	
	@Test
	public void verificarValorMinimoMaximoErroMaior() {
		try {
			this.validaRegrasBoleto.verificarValorMinimoMaximo(BigDecimal.valueOf(10), BigDecimal.ZERO, BigDecimal.ONE);
			fail();
		} 
		catch (ServicoException e) {
			assertEquals(e.getMensagem().getCodigoRetorno(),
					MensagemRetorno.WS2_VALOR_PAGTO_CONTINGENCIA_NAO_ATENDE_PREMISSAS.getCodigo().toString());
		}
	}
	
	@Test
	public void verificarValorMinimoMaximoErroMenor() {
		try {
			this.validaRegrasBoleto.verificarValorMinimoMaximo(BigDecimal.ZERO, BigDecimal.ONE, BigDecimal.ONE);
			fail();
		} 
		catch (ServicoException e) {
			assertEquals(e.getMensagem().getCodigoRetorno(),
					MensagemRetorno.WS2_VALOR_PAGTO_CONTINGENCIA_NAO_ATENDE_PREMISSAS.getCodigo().toString());
		}
	}
	
	@Test
	public void validarPagamentoContingenciaSem() throws ServicoException {
		ValidaRegrasBoletoEntrada valida = this.validaRegrasBoleto.transformarXmlParaJAXB(this.xml);
		this.validaRegrasBoleto.validarPagamentoContingencia(valida.getDados());
	}
	
	@Test
	public void validarPagamentoContingencia() throws ServicoException {
		ValidaRegrasBoletoEntrada valida = this.validaRegrasBoleto
				.transformarXmlParaJAXB(this.xml.replace("SEM_CONTINGENCIA", "CONTINGENCIA_CAIXA"));
		this.validaRegrasBoleto.validarPagamentoContingencia(valida.getDados());
	}
	
	@Test
	public void validarPagamentoContingenciaNaoExiste() throws ServicoException {
		try {
			ValidaRegrasBoletoEntrada valida = this.validaRegrasBoleto
					.transformarXmlParaJAXB(this.xml.replace("106", "0").replace("SEM_CONTINGENCIA", "CONTINGENCIA_CAIXA"));
			this.validaRegrasBoleto.validarPagamentoContingencia(valida.getDados());
			fail();
		} 
		catch (ServicoException e) {
			assertEquals(e.getMensagem().getCodigoRetorno(),
					MensagemRetorno.WS2_NAO_ACEITA_PAGAMENTO_CONTINGENCIA.getCodigo().toString());
		}
	}
	
	@Test
	public void validarPagamentoContingenciaNaoAutorizado() throws ServicoException {
		try {
			ValidaRegrasBoletoEntrada valida = this.validaRegrasBoleto
					.transformarXmlParaJAXB(this.xml.replace("106", "1").replace("SEM_CONTINGENCIA", "CONTINGENCIA_CAIXA"));
			this.validaRegrasBoleto.validarPagamentoContingencia(valida.getDados());
			fail();
		} 
		catch (ServicoException e) {
			assertEquals(e.getMensagem().getCodigoRetorno(),
					MensagemRetorno.WS2_NAO_ACEITA_PAGAMENTO_CONTINGENCIA.getCodigo().toString());
		}
	}
	
	@Test
	public void validarPagamentoContingenciaErro() throws ServicoException {
		try {
			this.validaRegrasBoleto.validarPagamentoContingencia(null);
			fail();
		} 
		catch (ServicoException e) {
			assertEquals(e.getMensagem().getCodigoRetorno(),
					MensagemRetorno.ERRO_INTERNO.getCodigo().toString());
		}
	}
	
	@Test
	public void validarValorNominal() throws ServicoException {
		ValidaRegrasBoletoEntrada valida = this.validaRegrasBoleto.transformarXmlParaJAXB(this.xml);
		this.validaRegrasBoleto.validarValorNominal(BigDecimal.ONE, BigDecimal.ONE, valida.getDados());
	}
	
	@Test
	public void validarValorNominalZerado() throws ServicoException {
		ValidaRegrasBoletoEntrada valida = this.validaRegrasBoleto
				.transformarXmlParaJAXB(this.xml.replace("SEM_CONTINGENCIA", "CONTINGENCIA_CIP"));
		this.validaRegrasBoleto.validarValorNominal(BigDecimal.ONE, BigDecimal.ZERO, valida.getDados());
	}
	
	@Test
	public void validarValorNominalMenor() throws ServicoException {
		try {
			ValidaRegrasBoletoEntrada valida = this.validaRegrasBoleto
					.transformarXmlParaJAXB(this.xml.replace("SEM_CONTINGENCIA", "CONTINGENCIA_CIP"));
			this.validaRegrasBoleto.validarValorNominal(BigDecimal.ONE, BigDecimal.valueOf(10), valida.getDados());
			fail();
		} 
		catch (ServicoException e) {
			assertEquals(e.getMensagem().getCodigoRetorno(),
					MensagemRetorno.WS2_VALOR_PAGAR_MAIOR_VALOR_BARRA.getCodigo().toString());
		}
	}
	
	@Test
	public void validarValorNominalContingenciaCip() {
		try {
			ValidaRegrasBoletoEntrada valida = this.validaRegrasBoleto
					.transformarXmlParaJAXB(this.xml.replace("SEM_CONTINGENCIA", "CONTINGENCIA_CIP"));
			this.validaRegrasBoleto.validarValorNominal(BigDecimal.valueOf(10), BigDecimal.valueOf(2), valida.getDados());
			fail();
		} 
		catch (ServicoException e) {
			assertEquals(e.getMensagem().getCodigoRetorno(),
					MensagemRetorno.WS2_VALOR_PAGAR_MAIOR_VALOR_BARRA.getCodigo().toString());
		}
	}
	
	@Test
	public void validarValorNominalContingenciaCip2() {
		try {
			ValidaRegrasBoletoEntrada valida = this.validaRegrasBoleto
					.transformarXmlParaJAXB(this.xml.replace("SEM_CONTINGENCIA", "CONTINGENCIA_CIP"));
			this.validaRegrasBoleto.validarValorNominal(BigDecimal.valueOf(1), BigDecimal.valueOf(2), valida.getDados());
			fail();
		} 
		catch (ServicoException e) {
			assertEquals(e.getMensagem().getCodigoRetorno(),
					MensagemRetorno.WS2_VALOR_PAGAR_MAIOR_VALOR_BARRA.getCodigo().toString());
		}
	}
	
	@Test
	public void validarValorNominalFlagNovaCobranca() {
		try {
			ValidaRegrasBoletoEntrada valida = this.validaRegrasBoleto.transformarXmlParaJAXB(this.xml);
			valida.getDados().setNovaPlataformaCobranca("N");
			this.validaRegrasBoleto.validarValorNominal(BigDecimal.valueOf(1), BigDecimal.valueOf(2), valida.getDados());
			fail();
		} 
		catch (ServicoException e) {
			assertEquals(e.getMensagem().getCodigoRetorno(),
					MensagemRetorno.WS2_VALOR_PAGAR_MAIOR_VALOR_BARRA.getCodigo().toString());
		}
	}
	
	@Test
	public void processar() {
		String resposta = this.validaRegrasBoleto.processar(this.xml);
		assertNotEquals(ConstantesRoteadorWeb.MSG_GENERICA, resposta);
	}
	
	@Test
	public void processarErro() {
		String resposta = this.validaRegrasBoleto.processar(this.xml.replace("NSU_TRANSACAO", "NSU"));
		assertNotEquals(ConstantesRoteadorWeb.MSG_GENERICA, resposta);
	}

}
