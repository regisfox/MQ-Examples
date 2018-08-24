package br.gov.caixa.simtx.roteador.api.ws;


import static org.junit.Assert.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;

import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;

import br.gov.caixa.simtx.persistencia.constante.Constantes;
import br.gov.caixa.simtx.persistencia.constante.MensagemRetorno;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb006Mensagem;
import br.gov.caixa.simtx.persistencia.vo.TipoContingenciaEnum;
import br.gov.caixa.simtx.roteador.api.repositorio.FornecedorDadosMock;
import br.gov.caixa.simtx.roteador.api.util.ConstantesRoteadorWeb;
import br.gov.caixa.simtx.roteador.api.util.SimtxConfigMock;
import br.gov.caixa.simtx.roteador.api.ws.matriz.LeitorMatrizConsultaRegrasBoletoMock;
import br.gov.caixa.simtx.roteador.api.ws.xml.Header;
import br.gov.caixa.simtx.roteador.api.ws.xml.consulta.ConsultaRegrasBoletoEntrada;
import br.gov.caixa.simtx.util.TratadorCodigoBarras;
import br.gov.caixa.simtx.util.exception.ServicoException;
import br.gov.caixa.simtx.util.vo.CodigoBarras;
import br.gov.caixa.simtx.util.xml.BuscadorTextoXml;

public class ProcessadorConsultaRegrasBoletoTest {
	
	private ProcessadorConsultaRegrasBoleto consultaRegrasBoleto;
	
	private Header header;
	
	private String xmlWs1 = 
			"<consultaregrasboleto:SERVICO_ENTRADA xmlns:consultaregrasboleto=\"http://caixa.gov.br/simtx/consulta_boleto/regras/v1/ns\" xmlns:sibar_base=\"http://caixa.gov.br/sibar\">\r\n" + 
			"	<sibar_base:HEADER>\r\n" + 
			"		<VERSAO>1.0</VERSAO>\r\n" + 
			"		<USUARIO_SERVICO>smtxsd01</USUARIO_SERVICO>\r\n" + 
			"		<OPERACAO>CONSULTA_REGRAS_BOLETO</OPERACAO>\r\n" + 
			"		<SISTEMA_ORIGEM>SIMAA</SISTEMA_ORIGEM>\r\n" + 
			"		<UNIDADE>625</UNIDADE>\r\n" + 
			"		<DATA_HORA>20180420203302</DATA_HORA>\r\n" + 
			"		<ID_PROCESSO>CONSULTA_REGRAS_BOLETO. PASSO 5: CONSULTA_REGRAS_B</ID_PROCESSO>\r\n" + 
			"	</sibar_base:HEADER>\r\n" + 
			"	<DADOS>\r\n" + 
			"		<NSU_TRANSACAO>1</NSU_TRANSACAO>\r\n" + 
			"		<ULTIMA_PARCELA_VIAVEL>S</ULTIMA_PARCELA_VIAVEL>\r\n" + 
			"		<PAGAMENTO_PARCIAL>S</PAGAMENTO_PARCIAL>\r\n" + 
			"		<QUANTIDADE_PAGAMENTO_PARCIAL_PERMITIDO>0</QUANTIDADE_PAGAMENTO_PARCIAL_PERMITIDO>\r\n" + 
			"		<NUMERO_PARCELA_ATUAL>1</NUMERO_PARCELA_ATUAL>\r\n" + 
			"		<RECEBE_VALOR_DIVERGENTE>SOMENTE_VALOR_MINIMO</RECEBE_VALOR_DIVERGENTE>\r\n" + 
			"		<ESPECIE>SIGCB</ESPECIE>\r\n" + 
			"		<TIPO_CONTINGENCIA>SEM_CONTINGENCIA</TIPO_CONTINGENCIA>\r\n" + 
			"		<NOVA_PLATAFORMA_DE_COBRANCA>S</NOVA_PLATAFORMA_DE_COBRANCA>\r\n" + 
			"		<DATA_CONSULTA>2018-04-20</DATA_CONSULTA>\r\n" + 
			"		<DATA_VENCIMENTO>2018-04-24</DATA_VENCIMENTO>\r\n" + 
			"		<CODIGO_BARRAS>10492772500025000008062480000100040992525250</CODIGO_BARRAS>\r\n" + 
			"	</DADOS>\r\n" + 
			"</consultaregrasboleto:SERVICO_ENTRADA>";
	
	
	@Before
	public void inicializarDados() throws JAXBException {
		this.consultaRegrasBoleto = new ProcessadorConsultaRegrasBoleto();
		FornecedorDadosMock fornecedorDadosMock = new FornecedorDadosMock();
		LeitorMatrizConsultaRegrasBoletoMock regrasBoletoMock = new LeitorMatrizConsultaRegrasBoletoMock();
		regrasBoletoMock.setFornecedorDados(fornecedorDadosMock);
		TratadorCodigoBarras tratadorCodigoBarras = new TratadorCodigoBarras();
		tratadorCodigoBarras.setFornecedorDados(fornecedorDadosMock);
		
		this.consultaRegrasBoleto.setFornecedorDados(fornecedorDadosMock);
		this.consultaRegrasBoleto.setSimtxConfig(new SimtxConfigMock());
		this.consultaRegrasBoleto.setLeitorMatriz(regrasBoletoMock);
		this.consultaRegrasBoleto.setTratadorCodigoBarras(tratadorCodigoBarras);
		
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddhhmmss");
		this.header = new Header();
		this.header.setVersao("1");
		this.header.setOperacao("CONSULTA_REGRAS_BOLETO");
		this.header.setDataHora(format.format(new Date()));
	}
	
	@Test
	public void transformarXmlParaJAXB() throws ServicoException {
		ConsultaRegrasBoletoEntrada consulta = this.consultaRegrasBoleto.transformarXmlParaJAXB(this.xmlWs1);
		assertNotNull(consulta);
	}
	
	@Test
	public void transformarXmlParaJAXBErro() {
		try {
			this.consultaRegrasBoleto.transformarXmlParaJAXB(null);
			fail();
		} 
		catch (ServicoException e) {
			assertEquals(e.getMensagem().getCodigoRetorno(), MensagemRetorno.WS_LAYOUT_INVALIDO.getCodigo().toString());
		}
	}
	
	@Test
	public void recuperarValorMatriz() throws ServicoException {
		ConsultaRegrasBoletoEntrada consulta = this.consultaRegrasBoleto.transformarXmlParaJAXB(this.xmlWs1);
		String valorMatriz = this.consultaRegrasBoleto.recuperarValorMatriz(consulta);
		assertNotEquals("", valorMatriz);
	}
	
	@Test
	public void recuperarValorMatrizErro() throws ServicoException {
		try {
			this.consultaRegrasBoleto.recuperarValorMatriz(null);
			fail();
		}
		catch (ServicoException e) {
			assertEquals(e.getMensagem().getCodigoRetorno(),
					MensagemRetorno.WS_NAO_RECUPEROU_DADOS_MATRIZ.getCodigo().toString());
		}
	}
	
	@Test
	public void tratarExcecao() {
		Mtxtb006Mensagem mensagem = new Mtxtb006Mensagem();
		mensagem.setCodigoRetorno(ConstantesRoteadorWeb.ERRO_CONSULTA);
		mensagem.setDeMensagemNegocial("TESTE TRATAR EXCECAO");
		ServicoException e = new ServicoException(mensagem, Constantes.ORIGEM_SIMTX);
		String consulta = this.consultaRegrasBoleto.tratarExcecao(e, this.header);
		assertNotEquals(ConstantesRoteadorWeb.MSG_GENERICA, consulta);
	}
	
	@Test
	public void tratarExcecaoErro() {
		String consulta = this.consultaRegrasBoleto.tratarExcecao(null, null);
		assertEquals(ConstantesRoteadorWeb.MSG_GENERICA, consulta);
	}
	
	@Test
	public void montarResposta()
			throws ServicoException, JAXBException, ParserConfigurationException, SAXException, IOException {
		String retornoMatriz = "NAO_RECEBER";
		String consulta = this.consultaRegrasBoleto.montarResposta(retornoMatriz, this.header, null);
		assertNotEquals("", consulta);
		assertNotEquals(ConstantesRoteadorWeb.MSG_GENERICA, consulta);
		
		String saida = new BuscadorTextoXml(consulta).xpathTexto("/*[1]/DADOS/VALOR_NOMINAL");
		assertEquals("", saida);
		
		saida = new BuscadorTextoXml(consulta).xpathTexto("/*[1]/DADOS/DATA_VENCIMENTO");
		assertEquals("", saida);
	}
	
	@Test
	public void montarRespostaCodigoBarras()
			throws ServicoException, JAXBException, ParserConfigurationException, SAXException, IOException {
		String retornoMatriz = "VALOR_BARRA";
		CodigoBarras codigoBarras = new CodigoBarras();
		codigoBarras.setValorTitulo(BigDecimal.valueOf(1.00));
		
		Calendar data = Calendar.getInstance();
		data.add(Calendar.DAY_OF_MONTH, 1);
		codigoBarras.setDataVencimento(data.getTime());
		
		String consulta = this.consultaRegrasBoleto.montarResposta(retornoMatriz, this.header, codigoBarras);
		assertNotEquals("", consulta);
		assertNotEquals(ConstantesRoteadorWeb.MSG_GENERICA, consulta);
		
		String saida = new BuscadorTextoXml(consulta).xpathTexto("/*[1]/DADOS/VALOR_NOMINAL");
		assertNotEquals("", saida);
		assertNotEquals(0.0, saida);
		
		saida = new BuscadorTextoXml(consulta).xpathTexto("/*[1]/DADOS/DATA_VENCIMENTO");
		assertNotEquals("", saida);
	}
	
	@Test
	public void montarRespostaErro() throws ServicoException {
		try {
			this.consultaRegrasBoleto.montarResposta(null, null, null);
			fail();
		} 
		catch (ServicoException e) {
			assertEquals(ConstantesRoteadorWeb.ERRO_CONSULTA, e.getMensagem().getCodigoRetorno());
		}
	}
	
	@Test
	public void validarSaidaMatrizNaoReceber() {
		try {
			String retornoMatriz = "NAO_RECEBER";
			this.consultaRegrasBoleto.validarSaidaMatriz(retornoMatriz);
			fail();
		} 
		catch (ServicoException e) {
			assertEquals(e.getMensagem().getCodigoRetorno(),
					MensagemRetorno.WS1_NAO_RECEBER_BOLETO.getCodigo().toString());
		}
	}
	
	@Test
	public void validarSaidaMatriz() throws ServicoException {
		String retornoMatriz = "DIFERENTE";
		this.consultaRegrasBoleto.validarSaidaMatriz(retornoMatriz);
	}
	
	@Test
	public void atualizarDadosEntrada() throws ServicoException {
		TratadorCodigoBarras tratadorCodigobarras = new TratadorCodigoBarras();
		tratadorCodigobarras.setFornecedorDados(new FornecedorDadosMock());
		CodigoBarras codigoBarras = tratadorCodigobarras.retornarValoresCodigoBarras("10492772500025000008062480000100040992525250");
		
		ConsultaRegrasBoletoEntrada consulta = this.consultaRegrasBoleto.transformarXmlParaJAXB(this.xmlWs1);
		assertNotNull(consulta);
		ConsultaRegrasBoletoEntrada retorno = this.consultaRegrasBoleto.atualizarDadosEntrada(consulta, codigoBarras);
		assertNotNull(retorno);
	}
	
	@Test
	public void naoAtualizarDadosEntrada() throws ServicoException {
		this.consultaRegrasBoleto.atualizarDadosEntrada(null, null);
	}
	
	@Test
	public void retornarValoresCodigoBarras() throws ServicoException {
		ConsultaRegrasBoletoEntrada consulta = this.consultaRegrasBoleto.transformarXmlParaJAXB(this.xmlWs1);
		CodigoBarras codigoBarras = this.consultaRegrasBoleto.retornarValoresCodigoBarras(consulta.getDados());
		assertNull(codigoBarras);
	}
	
	@Test
	public void retornarValoresCodigoBarrasContingencia() throws ServicoException {
		ConsultaRegrasBoletoEntrada consulta = this.consultaRegrasBoleto.transformarXmlParaJAXB(this.xmlWs1);
		consulta.getDados().setTipoContingencia(TipoContingenciaEnum.CIP.getDescricao());
		CodigoBarras codigoBarras = this.consultaRegrasBoleto.retornarValoresCodigoBarras(consulta.getDados());
		assertNotNull(codigoBarras);
		assertNotEquals(0.0, codigoBarras.getValorTitulo());
		assertNotNull(codigoBarras.getDataVencimento());
	}
	
	@Test
	public void retornarValoresCodigoBarrasNPCNao() throws ServicoException {
		ConsultaRegrasBoletoEntrada consulta = this.consultaRegrasBoleto.transformarXmlParaJAXB(this.xmlWs1);
		consulta.getDados().setNovaPlataformaCobranca(ConstantesRoteadorWeb.NAO);
		CodigoBarras codigoBarras = this.consultaRegrasBoleto.retornarValoresCodigoBarras(consulta.getDados());
		assertNotNull(codigoBarras);
		assertNotEquals(0.0, codigoBarras.getValorTitulo());
		assertNotNull(codigoBarras.getDataVencimento());
	}
	
	@Test
	public void retornarValoresCodigoBarrasErro() throws ServicoException {
		CodigoBarras codigoBarras = this.consultaRegrasBoleto.retornarValoresCodigoBarras(null);
		assertNull(codigoBarras);
	}
	
	@Test
	public void processar() {
		String resposta = this.consultaRegrasBoleto.processar(this.xmlWs1);
		assertNotEquals("", resposta);
		assertNotEquals(ConstantesRoteadorWeb.MSG_GENERICA, resposta);
	}
	
	@Test
	public void processarErro() {
		String resposta = this.consultaRegrasBoleto.processar(null);
		assertNotEquals("", resposta);
		assertNotEquals(ConstantesRoteadorWeb.MSG_GENERICA, resposta);
	}


}
