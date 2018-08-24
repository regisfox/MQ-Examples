package br.gov.caixa.simtx.roteador.api.ws.matriz;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map.Entry;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import br.gov.caixa.simtx.roteador.api.ws.matriz.LeitorMatrizConsultaRegrasBoleto;
import br.gov.caixa.simtx.roteador.api.ws.matriz.LeitorMatrizValidaRegrasBoleto;
import br.gov.caixa.simtx.roteador.api.ws.matriz.RegrasMatriz;
import br.gov.caixa.simtx.roteador.api.ws.xml.consulta.ConsultaRegrasBoletoEntrada;
import br.gov.caixa.simtx.roteador.api.ws.xml.valida.ValidaRegrasBoletoEntrada;
import br.gov.caixa.simtx.util.exception.ServicoException;


public class RegrasMatrizTest {
	
	private RegrasMatriz regrasMatriz;
	
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
			"		<NSUMTX>1</NSUMTX>\r\n" + 
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
			"	</DADOS>\r\n" + 
			"</consultaregrasboleto:SERVICO_ENTRADA>";
	
	private String xmlWs2 = 
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
			"		<NSUMTX>1</NSUMTX>\r\n" + 
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
			"	</DADOS>\r\n" + 
			"</validaregrasboleto:SERVICO_ENTRADA>";
	
	
	
	@Before
	public void inicializarDados() {
		this.regrasMatriz = new RegrasMatriz();
	}
	
	@Test
	public void tratarParametrosEntradaWs1() throws JAXBException, ParseException {
		JAXBContext context = JAXBContext.newInstance(ConsultaRegrasBoletoEntrada.class);
		Unmarshaller unmarshaller = context.createUnmarshaller();

		ConsultaRegrasBoletoEntrada consulta = unmarshaller
				.unmarshal(new StreamSource(new ByteArrayInputStream(this.xmlWs1.getBytes())),
						ConsultaRegrasBoletoEntrada.class).getValue();
		
		String entradaMatriz = this.regrasMatriz.tratarParametrosEntradaWs1(consulta.getDados());
		
		Assert.assertNotNull(entradaMatriz);
		Assert.assertNotEquals("", entradaMatriz);
	}
	
	@Test
	public void tratarParametrosEntradaWs2() throws JAXBException, ParseException {
		JAXBContext context = JAXBContext.newInstance(ValidaRegrasBoletoEntrada.class);
		Unmarshaller unmarshaller = context.createUnmarshaller();

		ValidaRegrasBoletoEntrada valida = unmarshaller
				.unmarshal(new StreamSource(new ByteArrayInputStream(this.xmlWs2.getBytes())),
						ValidaRegrasBoletoEntrada.class).getValue();
		
		String entradaMatriz = this.regrasMatriz.tratarParametrosEntradaWs2(valida.getDados());
		
		Assert.assertNotNull(entradaMatriz);
		Assert.assertNotEquals("", entradaMatriz);
	}
	

	@Test
	public void validaFlagEspecieOutros() {
		String retorno = this.regrasMatriz.validaFlagEspecie("SIGCB");
		Assert.assertEquals("OUTROS", retorno);
	}
	
	@Test
	public void validaFlagEspecieCartaoCredito() {
		String retorno = this.regrasMatriz.validaFlagEspecie("CARTAO_CREDITO");
		Assert.assertEquals("CARTAO_CREDITO", retorno);
	}
	
	@Test
	public void validaFlagEspecieCartaoDeCredito() {
		String retorno = this.regrasMatriz.validaFlagEspecie("CARTAO DE CREDITO");
		Assert.assertEquals("CARTAO_CREDITO", retorno);
	}
	
	@Test
	public void validaFlagEspecieBoletoProposta() {
		String retorno = this.regrasMatriz.validaFlagEspecie("BOLETO_PROPOSTA");
		Assert.assertEquals("BOLETO_PROPOSTA", retorno);
	}
	
	@Test
	public void validaFlagEspecieBoletoPropostaEspaco() {
		String retorno = this.regrasMatriz.validaFlagEspecie("BOLETO PROPOSTA");
		Assert.assertEquals("BOLETO_PROPOSTA", retorno);
	}

	@Test
	public void validaPrimeiraLinhaMatrizConsulta() throws ServicoException {
		LeitorMatrizConsultaRegrasBoleto recuperador = new LeitorMatrizConsultaRegrasBoleto();
		String regra = recuperador.processaRegraBoletoEntrada("S;N;BOLETO_PROPOSTA;QUALQUER_VALOR;VENCIDO;S;S;S;");
		Assert.assertEquals("QUALQUER_VALOR", regra);
	}
	
	@Test
	public void validaPrimeiraLinhaMatrizValida() throws IOException, ServicoException {
		LeitorMatrizValidaRegrasBoleto recuperador = new LeitorMatrizValidaRegrasBoleto();
		recuperador.setConsultaRegrasBoleto(new LeitorMatrizConsultaRegrasBoleto());
		Entry<String, String> regra = recuperador.processaRegraBoletoEntrada("S;N;BOLETO_PROPOSTA;QUALQUER_VALOR;VENCIDO;S;S;S;", "N");
		Assert.assertEquals("BAIXA_INTEGRAL", regra.getValue());
	}
}
