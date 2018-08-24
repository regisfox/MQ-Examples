package br.gov.caixa.simtx.agendamento.processador;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;

import javax.xml.parsers.ParserConfigurationException;

import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;

import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb001Servico;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb004Canal;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb008MeioEntrada;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb011VersaoServico;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb011VersaoServicoPK;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb014Transacao;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb016IteracaoCanal;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb016IteracaoCanalPK;
import br.gov.caixa.simtx.util.exception.ServicoException;
import br.gov.caixa.simtx.util.gerenciador.GerenciadorTarefas;
import br.gov.caixa.simtx.util.gerenciador.TratadorDeExcecao;
import br.gov.caixa.simtx.util.gerenciador.cache.RepositorioArquivo;
import br.gov.caixa.simtx.util.mock.FornecedorDadosMock;
import br.gov.caixa.simtx.util.mock.SimtxConfigMock;
import br.gov.caixa.simtx.util.mock.infra.MensagemServidorMock;
import br.gov.caixa.simtx.util.xml.BuscadorTextoXml;
import br.gov.caixa.simtx.util.xml.DadosBarramento;

public class ProcessadorAgendamentoTest {

	private String XML_PAGAMENTO_BOLETO = "<pagamentoboleto:PAGAMENTO_BOLETO_ENTRADA\r\n" + "  xmlns:comuns=\"http://caixa.gov.br/simtx/comuns/comuns\"\r\n"
			+ "  xmlns:consultacobrancabancaria=\"http://caixa.gov.br/simtx/negocial/consulta_boleto/v1/consulta_boleto\"\r\n"
			+ "  xmlns:h=\"http://caixa.gov.br/simtx/comuns/header\"\r\n" + "  xmlns:pagamentoboleto=\"http://caixa.gov.br/simtx/pagamento_boleto/v1/ns\"\r\n"
			+ "  xmlns:validaassinatura=\"http://caixa.gov.br/simtx/meios_de_entrada/valida_assinatura/v1/valida_assinatura\"\r\n"
			+ "  xmlns:validapermissao=\"http://caixa.gov.br/simtx/meios_de_entrada/valida_permissao/v1/valida_permissao\"\r\n"
			+ "  xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\r\n"
			+ "  xsi:schemaLocation=\"http://caixa.gov.br/simtx/pagamentoboleto Pagamento_Boleto.xsd \">\r\n"
			+ "  <HEADER xsi:type=\"h:HEADER_ENTRADA_Type\">\r\n" + "    <SERVICO>\r\n" + "      <CODIGO>110032</CODIGO>\r\n" + "      <VERSAO>1</VERSAO>\r\n"
			+ "    </SERVICO>\r\n" + "    <CANAL>\r\n" + "      <SIGLA>SIMTX</SIGLA>\r\n" + "      <NSU>123</NSU>\r\n"
			+ "      <DATAHORA>20180110115100</DATAHORA>\r\n" + "    </CANAL>\r\n" + "    <MEIOENTRADA>1</MEIOENTRADA>\r\n" + "    <IDENTIFICADOR_ORIGEM>\r\n"
			+ "      <ENDERECO_IP>127.0.0.1</ENDERECO_IP>\r\n" + "    </IDENTIFICADOR_ORIGEM>\r\n" + "    <DATA_REFERENCIA>20180110</DATA_REFERENCIA>\r\n"
			+ "  </HEADER>\r\n" + "  <NSUMTX_ORIGEM>11832</NSUMTX_ORIGEM>\r\n" + "  <CPF>99999999999</CPF>\r\n" + "  <CONTA>\r\n" + "    <CONTA_SIDEC>\r\n"
			+ "      <UNIDADE>1298</UNIDADE>\r\n" + "      <OPERACAO>001</OPERACAO>\r\n" + "      <CONTA>564123</CONTA>\r\n" + "      <DV>5</DV>\r\n"
			+ "    </CONTA_SIDEC>\r\n" + "  </CONTA>\r\n" + "  <ASSINATURA_SIMPLES>\r\n" + "    <ASSINATURA>ASSINATURA</ASSINATURA>\r\n"
			+ "    <SERVICO_SIPER>123</SERVICO_SIPER>\r\n" + "    <APELIDO>APELIDO</APELIDO>\r\n" + "    <DISPOSITIVO>\r\n"
			+ "      <SISTEMA_OPERACIONAL>ANDROID_PHONE</SISTEMA_OPERACIONAL>\r\n" + "      <CODIGO>CODIGO</CODIGO>\r\n" + "    </DISPOSITIVO>\r\n"
			+ "  </ASSINATURA_SIMPLES>\r\n" + "  <TOKEN>\r\n" + "    <ACAO>1</ACAO>\r\n" + "    <SESSAO>ADASDASDAS</SESSAO>\r\n"
			+ "    <CODIGO_SESSAO>D56AS78D56AS78D5AS78DJGHJG</CODIGO_SESSAO>\r\n" + "  </TOKEN>\r\n" + "  <PAGAMENTO_BOLETO>\r\n"
			+ "    <CODIGO_BARRAS>104465466464654654654665464</CODIGO_BARRAS>\r\n"
			+ "    <LINHA_DIGITAVEL>10442342342342342342342343423423423423423423423</LINHA_DIGITAVEL>\r\n"
			+ "    <DATA_PAGAMENTO>2001-01-01</DATA_PAGAMENTO>\r\n" + "    <DATA_VENCIMENTO>2001-01-01</DATA_VENCIMENTO>\r\n"
			+ "    <IDENTIFICACAO_TRANSACAO>1</IDENTIFICACAO_TRANSACAO>\r\n" + "    <PORTADOR>\r\n" + "      <CPF>99999999999</CPF>\r\n" + "    </PORTADOR>\r\n"
			+ "    <VALOR_PAGAR>505.80</VALOR_PAGAR>\r\n" + "    <INDICADOR_LIDO_DIGITADO>1</INDICADOR_LIDO_DIGITADO>\r\n" + "  </PAGAMENTO_BOLETO>\r\n"
			+ "  <CONSULTA_BOLETO>\r\n" + "    <FLAG_NOVA_COBRANCA>S</FLAG_NOVA_COBRANCA>\r\n"
			+ "    <SITUACAO_CONTINGENCIA>SEM_CONTINGENCIA</SITUACAO_CONTINGENCIA>\r\n" + "    <FLAG_HABITACAO>S</FLAG_HABITACAO>\r\n"
			+ "    <NUMERO_CONTROLE_DDA>31231</NUMERO_CONTROLE_DDA>\r\n" + "    <TIPO_BOLETO>1</TIPO_BOLETO>\r\n" + "    <TITULO>\r\n"
			+ "      <NUMERO_IDENTIFICACAO>0</NUMERO_IDENTIFICACAO>\r\n" + "      <REFERENCIA_ATUAL_CADASTRO>0</REFERENCIA_ATUAL_CADASTRO>\r\n"
			+ "      <SEQUENCIA_ATUALIZACAO_CADASTRO>0</SEQUENCIA_ATUALIZACAO_CADASTRO>\r\n"
			+ "      <DATA_HORA_SITUACAO>2001-12-31T12:00:00</DATA_HORA_SITUACAO>\r\n" + "      <NOSSO_NUMERO>\r\n" + "        <NUMERO>0</NUMERO>\r\n"
			+ "        <DV>0</DV>\r\n" + "      </NOSSO_NUMERO>\r\n" + "      <BENEFICIARIO_ORIGINAL>\r\n" + "        <CPF>99999999999</CPF>\r\n"
			+ "        <NOME>TESTE</NOME>\r\n" + "        <CODIGO>0</CODIGO>\r\n" + "        <DV>0</DV>\r\n" + "        <LOGRADOURO>A</LOGRADOURO>\r\n"
			+ "        <CIDADE>SAOPAULO</CIDADE>\r\n" + "        <UF>SP</UF>\r\n" + "        <CEP>534534</CEP>\r\n" + "      </BENEFICIARIO_ORIGINAL>\r\n"
			+ "      <BENEFICIARIO_FINAL>\r\n" + "        <CPF>99999999999</CPF>\r\n" + "        <NOME>TESTE</NOME>\r\n" + "      </BENEFICIARIO_FINAL>\r\n"
			+ "      <AVALISTA>\r\n" + "        <CPF>99999999999</CPF>\r\n" + "        <NOME>TESTE</NOME>\r\n" + "      </AVALISTA>\r\n" + "      <PAGADOR>\r\n"
			+ "        <CPF>99999999999</CPF>\r\n" + "        <NOME>TESTE</NOME>\r\n" + "      </PAGADOR>\r\n" + "      <CODIGO_MOEDA>AB</CODIGO_MOEDA>\r\n"
			+ "      <CODIGO_BARRAS>104465466464654654654665464</CODIGO_BARRAS>\r\n"
			+ "      <LINHA_DIGITAVEL>10442342342342342342342343423423423423423423423</LINHA_DIGITAVEL>\r\n"
			+ "      <DATA_VENCIMENTO>2001-01-01</DATA_VENCIMENTO>\r\n" + "      <VALOR>505.80</VALOR>\r\n" + "      <ESPECIE>CHEQUE</ESPECIE>\r\n"
			+ "      <VALOR_ABATIMENTO>0.0</VALOR_ABATIMENTO>\r\n" + "      <JUROS>\r\n" + "        <DATA>2001-01-01</DATA>\r\n"
			+ "        <VALOR>0.0</VALOR>\r\n" + "        <TIPO>VALOR_DIA_CORRIDO</TIPO>\r\n" + "      </JUROS>\r\n" + "      <MULTA>\r\n"
			+ "        <DATA>2001-01-01</DATA>\r\n" + "        <VALOR>0.0</VALOR>\r\n" + "        <TIPO>VALOR_FIXO</TIPO>\r\n" + "      </MULTA>\r\n"
			+ "      <DESCONTOS>\r\n" + "        <DESCONTO>\r\n" + "          <DATA>2001-01-01</DATA>\r\n" + "          <VALOR>0.0</VALOR>\r\n"
			+ "          <TIPO>ISENTO</TIPO>\r\n" + "        </DESCONTO>\r\n" + "      </DESCONTOS>\r\n" + "      <MINIMO>\r\n"
			+ "        <VALOR>0.0</VALOR>\r\n" + "      </MINIMO>\r\n" + "      <MAXIMO>\r\n" + "        <VALOR>0.0</VALOR>\r\n" + "      </MAXIMO>\r\n"
			+ "      <PARTICIPANTE_DESTINATARIO>\r\n" + "        <ISPB>ISPB</ISPB>\r\n" + "        <CODIGO_BANCO>0</CODIGO_BANCO>\r\n"
			+ "      </PARTICIPANTE_DESTINATARIO>\r\n" + "    </TITULO>\r\n" + "    <QUANTIDADE_DIAS_PROTESTO>0</QUANTIDADE_DIAS_PROTESTO>\r\n"
			+ "    <DATA_LIMITE_PAGAMENTO>2001-01-01</DATA_LIMITE_PAGAMENTO>\r\n" + "    <RECEBE_VALOR_DIVERGENTE>QUALQUER_VALOR</RECEBE_VALOR_DIVERGENTE>\r\n"
			+ "    <FLAG_RECEBIMENTO_CHEQUE>S</FLAG_RECEBIMENTO_CHEQUE>\r\n" + "    <FLAG_BLOQUEIO_PAGAMENTO>S</FLAG_BLOQUEIO_PAGAMENTO>\r\n"
			+ "    <FLAG_PAGAMENTO_PARCIAL>S</FLAG_PAGAMENTO_PARCIAL>\r\n"
			+ "    <QUANTIDADE_PAGAMENTO_PARCIAL_PERMITIDO>0</QUANTIDADE_PAGAMENTO_PARCIAL_PERMITIDO>\r\n"
			+ "    <QUANTIDADE_PAGAMENTO_PARCIAL_REGISTRADO>0</QUANTIDADE_PAGAMENTO_PARCIAL_REGISTRADO>\r\n"
			+ "    <VALOR_SALDO_TOTAL_ATUAL>0.0</VALOR_SALDO_TOTAL_ATUAL>\r\n" + "    <MODELO_CALCULO>AB</MODELO_CALCULO>\r\n" + "    <CALCULOS>\r\n"
			+ "      <CALCULO_CIP>\r\n" + "        <VALOR_JUROS>0.0</VALOR_JUROS>\r\n" + "        <VALOR_MULTA>0.0</VALOR_MULTA>\r\n"
			+ "        <VALOR_DESCONTO>0.0</VALOR_DESCONTO>\r\n" + "        <VALOR_TOTAL>0.0</VALOR_TOTAL>\r\n"
			+ "        <DATA_VALIDADE_CALCULO>2001-01-01</DATA_VALIDADE_CALCULO>\r\n" + "      </CALCULO_CIP>\r\n" + "      <CALCULO_CAIXA>\r\n"
			+ "        <VALOR_JUROS>0.0</VALOR_JUROS>\r\n" + "        <VALOR_MULTA>0.0</VALOR_MULTA>\r\n" + "        <VALOR_DESCONTO>0.0</VALOR_DESCONTO>\r\n"
			+ "        <VALOR_TOTAL>0.0</VALOR_TOTAL>\r\n" + "        <DATA_VALIDADE_CALCULO>2001-01-01</DATA_VALIDADE_CALCULO>\r\n"
			+ "        <VALOR_IOF>0.0</VALOR_IOF>\r\n" + "        <VALOR_ABATIMENTO>0.0</VALOR_ABATIMENTO>\r\n" + "      </CALCULO_CAIXA>\r\n"
			+ "    </CALCULOS>\r\n" + "    <BAIXAS>\r\n" + "      <BAIXA_OPERACIONAL>\r\n" + "        <NUMERO_IDENTIFICACAO>0</NUMERO_IDENTIFICACAO>\r\n"
			+ "        <NUMERO_REFERENCIA_ATUAL>0</NUMERO_REFERENCIA_ATUAL>\r\n" + "        <NUMERO_SEQUENCIA_ATUALIZACAO>0</NUMERO_SEQUENCIA_ATUALIZACAO>\r\n"
			+ "        <DATA_PROCESSAMENTO>2001-01-01</DATA_PROCESSAMENTO>\r\n"
			+ "        <DATA_HORA_PROCESSAMENTO>2001-12-31T12:00:00</DATA_HORA_PROCESSAMENTO>\r\n"
			+ "        <DATA_HORA_SITUACAO>2001-12-31T12:00:00</DATA_HORA_SITUACAO>\r\n" + "        <VALOR_TITULO>0.0</VALOR_TITULO>\r\n"
			+ "        <CODIGO_BARRAS>CODIGO_BARRAS</CODIGO_BARRAS>\r\n" + "      </BAIXA_OPERACIONAL>\r\n" + "      <BAIXA_EFETIVA>\r\n"
			+ "        <NUMERO_IDENTIFICACAO>0</NUMERO_IDENTIFICACAO>\r\n" + "        <NUMERO_REFERENCIA_ATUAL>0</NUMERO_REFERENCIA_ATUAL>\r\n"
			+ "        <NUMERO_SEQUENCIA_ATUALIZACAO>0</NUMERO_SEQUENCIA_ATUALIZACAO>\r\n" + "        <DATA_PROCESSAMENTO>2001-01-01</DATA_PROCESSAMENTO>\r\n"
			+ "        <DATA_HORA_PROCESSAMENTO>2001-12-31T12:00:00</DATA_HORA_PROCESSAMENTO>\r\n"
			+ "        <DATA_HORA_SITUACAO>2001-12-31T12:00:00</DATA_HORA_SITUACAO>\r\n" + "        <VALOR_TITULO>0.0</VALOR_TITULO>\r\n"
			+ "        <CODIGO_BARRAS>CODIGO_BARRAS</CODIGO_BARRAS>\r\n" + "        <CANAL_PAGAMENTO>AGENCIA</CANAL_PAGAMENTO>\r\n"
			+ "        <MEIO_PAGAMENTO>ESPECIE</MEIO_PAGAMENTO>\r\n" + "      </BAIXA_EFETIVA>\r\n" + "    </BAIXAS>\r\n"
			+ "    <DATA_HORA_DDA>2001-12-31T12:00:00</DATA_HORA_DDA>\r\n" + "    <DATA_MOVIMENTO>2001-01-01</DATA_MOVIMENTO>\r\n" + "  </CONSULTA_BOLETO>\r\n"
			+ "</pagamentoboleto:PAGAMENTO_BOLETO_ENTRADA>";

	private ProcessadorAgendamento processadorAgendamento;

	private Mtxtb004Canal canal;

	private Mtxtb001Servico servico;

	private Mtxtb011VersaoServico versaoServico;

	private Mtxtb001Servico servicoOrigem;

	private Mtxtb011VersaoServico versaoServicoOrigem;

	private Mtxtb014Transacao transacao;

	private Mtxtb016IteracaoCanal iteracaoCanal;
	
	
	@Before
	public void inicializarDados() {
		DadosBarramento dadosBarramento = new DadosBarramento();
		dadosBarramento.escrever(XML_PAGAMENTO_BOLETO);

		FornecedorDadosMock fornecedorDadosMock = new FornecedorDadosMock();
		MensagemServidorMock mensagemServidorMock = new MensagemServidorMock();
		SimtxConfigMock simtxConfigMock = new SimtxConfigMock();

		GerenciadorTarefas gerenciadorTarefas = new GerenciadorTarefas();
		gerenciadorTarefas.setFornecedorDados(fornecedorDadosMock);
		gerenciadorTarefas.setMensagemServidor(mensagemServidorMock);
		gerenciadorTarefas.setSimtxConfig(simtxConfigMock);

		TratadorDeExcecao tratadorDeExcecao = new TratadorDeExcecao();
		tratadorDeExcecao.setSimtxConfig(simtxConfigMock);

		this.processadorAgendamento = new ProcessadorAgendamento();
		this.processadorAgendamento.setFornecedorDados(fornecedorDadosMock);
		this.processadorAgendamento.setSimtxConfig(simtxConfigMock);
		this.processadorAgendamento.setTratadorDeExcecao(tratadorDeExcecao);
		this.processadorAgendamento.setGerenciadorTarefas(gerenciadorTarefas);
		this.processadorAgendamento.setRepositorioArquivo(new RepositorioArquivo());

		this.processadorAgendamento.setDadosBarramento(dadosBarramento);

		this.canal = new Mtxtb004Canal();
		this.canal.setNuCanal(104L);

		this.servico = new Mtxtb001Servico();
		this.servico.setNuServico(110033L);

		this.versaoServico = new Mtxtb011VersaoServico();
		Mtxtb011VersaoServicoPK versaoServicoPK = new Mtxtb011VersaoServicoPK();
		versaoServicoPK.setNuServico001(110033L);
		versaoServicoPK.setNuVersaoServico(1);
		this.versaoServico.setId(versaoServicoPK);
		this.versaoServico.setMtxtb001Servico(this.servico);
		this.versaoServico.setDeXsltRequisicao("servico/Agendamento/V1/agendamento_Req.xsl");
		this.versaoServico.setDeXsltResposta("servico/Pagamento_Boleto/V1/pagamentoBoleto_Resp.xsl");
		this.versaoServico.setMigrado(1);

		this.servicoOrigem = new Mtxtb001Servico();
		this.servicoOrigem.setNuServico(110032L);

		this.versaoServicoOrigem = new Mtxtb011VersaoServico();
		Mtxtb011VersaoServicoPK versaoServicoOrigemPK = new Mtxtb011VersaoServicoPK();
		versaoServicoOrigemPK.setNuServico001(110032L);
		versaoServicoOrigemPK.setNuVersaoServico(1);
		this.versaoServicoOrigem.setId(versaoServicoOrigemPK);
		this.versaoServicoOrigem.setMtxtb001Servico(this.servicoOrigem);
		this.versaoServicoOrigem.setDeXsltRequisicao("servico/Pagamento_Boleto/V1/pagamentoBoleto_Req.xsl");
		this.versaoServicoOrigem.setDeXsltResposta("servico/Pagamento_Boleto/V1/pagamentoBoleto_Resp.xsl");
		this.versaoServicoOrigem.setMigrado(1);

		this.transacao = new Mtxtb014Transacao();
		this.transacao.setNuNsuTransacao(15L);
		this.transacao.setCoCanalOrigem("104");
		this.transacao.setDtReferencia(new Date());

		this.iteracaoCanal = new Mtxtb016IteracaoCanal();
		Mtxtb016IteracaoCanalPK iteracaoCanalPK = new Mtxtb016IteracaoCanalPK();
//		iteracaoCanalPK.setNuIteracaoCanal(1L);
		iteracaoCanalPK.setNuNsuTransacao014(this.transacao.getNuNsuTransacao());
		this.iteracaoCanal.setId(iteracaoCanalPK);
		this.iteracaoCanal.setMtxtb014Transacao(this.transacao);
	}

	@Test
	public void atualizaIteracaoCanal() throws ServicoException {
		this.processadorAgendamento.atualizaIteracaoCanal(this.iteracaoCanal, this.transacao, XML_PAGAMENTO_BOLETO);
	}

	@Test
	public void atualizaTransacao() throws ServicoException {
		this.processadorAgendamento.atualizaTransacao(this.transacao, BigDecimal.ONE);
	}
	
	@Test
	public void transformarXmlRequisicao() throws ServicoException {
		String requisicao = this.processadorAgendamento.transformarXml(this.versaoServico.getDeXsltRequisicao(), this.transacao, this.canal, null);
		assertNotEquals("", requisicao);
		assertNotNull(requisicao);
	}

	@Test
	public void transformarXmlResposta() throws ServicoException {
		String resposta = this.processadorAgendamento.transformarXml(this.versaoServico.getDeXsltResposta(), this.transacao, this.canal, null);
		assertNotEquals("", resposta);
		assertNotNull(resposta);
	}

	@Test
	public void transformarXmlNull() {
		try {
			this.processadorAgendamento.transformarXml(null, null, null, null);
		} catch (ServicoException e) {
			return;
		}
		fail();
	}

	@Test
	public void salvarTransacaoServico() throws ServicoException {
		this.processadorAgendamento.salvarTransacaoServico(this.transacao, this.versaoServico);
	}

	@Test
	public void salvarTransacaoServicoErro() {
		try {
			this.processadorAgendamento.salvarTransacaoServico(this.transacao, null);
		} catch (ServicoException e) {
			return;
		}
		fail();
	}
	
	@Test
	public void salvarIteracaoCanal() throws ParserConfigurationException, SAXException, IOException, ServicoException {
		BuscadorTextoXml buscador = new BuscadorTextoXml(XML_PAGAMENTO_BOLETO);
		Mtxtb016IteracaoCanal iteracaoCanal = this.processadorAgendamento.salvarIteracaoCanal(buscador, XML_PAGAMENTO_BOLETO, this.transacao);
		assertNotNull(iteracaoCanal);
	}
	
	@Test
	public void salvarIteracaoCanalErro() {
		try {
			BuscadorTextoXml buscador = new BuscadorTextoXml(XML_PAGAMENTO_BOLETO);
			Mtxtb016IteracaoCanal iteracaoCanal = this.processadorAgendamento.salvarIteracaoCanal(buscador, XML_PAGAMENTO_BOLETO, null);
			assertNotNull(iteracaoCanal);
		} catch (Exception e) {
			return;
		}
		fail();
	}

	@Test
	public void salvarTransacao() throws ParserConfigurationException, SAXException, IOException, ServicoException {
		BuscadorTextoXml buscador = new BuscadorTextoXml(XML_PAGAMENTO_BOLETO);
		Mtxtb014Transacao transacao = this.processadorAgendamento.salvarTransacao(buscador, this.canal);
		assertNotNull(transacao);
	}

	@Test
	public void salvarTransacaoErro() {
		try {
			BuscadorTextoXml buscador = new BuscadorTextoXml(XML_PAGAMENTO_BOLETO);
			Mtxtb014Transacao transacao = this.processadorAgendamento.salvarTransacao(buscador, null);
			assertNotNull(transacao);
		} catch (ServicoException | ParserConfigurationException | SAXException | IOException e) {
			return;
		}
		fail();
	}

	@Test
	public void buscarCanal() throws ParserConfigurationException, SAXException, IOException {
		BuscadorTextoXml buscador = new BuscadorTextoXml(XML_PAGAMENTO_BOLETO);
		Mtxtb004Canal canal = this.processadorAgendamento.buscarCanal(buscador);
		assertNotNull(canal);
	}

	@Test
	public void buscarCanalErro() {
		Mtxtb004Canal canal = this.processadorAgendamento.buscarCanal(null);
		assertNotEquals(canal.getNuCanal(), "");
	}

	@Test
	public void buscarServico() throws ParserConfigurationException, SAXException, IOException, ServicoException {
		BuscadorTextoXml buscador = new BuscadorTextoXml(XML_PAGAMENTO_BOLETO);
		Long codigoServico = Long.parseLong(buscador.xpathTexto("/*[1]/HEADER/SERVICO/CODIGO"));
		Integer versaoServico = Integer.parseInt(buscador.xpathTexto("/*[1]/HEADER/SERVICO/VERSAO"));
		Mtxtb011VersaoServico versaoServico2 = this.processadorAgendamento.buscarServico(codigoServico, versaoServico);
		assertNotNull(versaoServico2);
	}

	@Test
	public void buscarServicoErro() {
		try {
			this.processadorAgendamento.buscarServico(0L, 0);
		} catch (ServicoException e) {
			return;
		}
		fail();
	}

	public void tratarAssinaturaSimples() {
		Mtxtb008MeioEntrada meioEntrada = new Mtxtb008MeioEntrada();
		meioEntrada.setNuMeioEntrada(1L);
	}

	@Test
	public void validarRegrasAgendamentoOK() throws ServicoException {
		this.processadorAgendamento.validarRegrasAgendamento(this.transacao);
	}

}
