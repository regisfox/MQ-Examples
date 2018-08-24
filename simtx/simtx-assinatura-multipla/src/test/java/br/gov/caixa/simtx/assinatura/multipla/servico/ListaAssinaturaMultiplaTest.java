package br.gov.caixa.simtx.assinatura.multipla.servico;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import br.gov.caixa.simtx.assinatura.multipla.infra.FornecedorDadosAssinaturaMultiplaMock;
import br.gov.caixa.simtx.persistencia.assinaturamultipla.entidade.Mtxtb027TransacaoAssinaturaMultipla;
import br.gov.caixa.simtx.persistencia.constante.AcaoRetorno;
import br.gov.caixa.simtx.persistencia.constante.Constantes;
import br.gov.caixa.simtx.persistencia.constante.MensagemRetorno;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb001Servico;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb004Canal;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb011VersaoServico;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb011VersaoServicoPK;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb014Transacao;
import br.gov.caixa.simtx.util.ParametrosAdicionais;
import br.gov.caixa.simtx.util.XmlValidador;
import br.gov.caixa.simtx.util.exception.ServicoException;
import br.gov.caixa.simtx.util.gerenciador.TratadorDeExcecao;
import br.gov.caixa.simtx.util.gerenciador.cache.RepositorioArquivo;
import br.gov.caixa.simtx.util.mock.AtualizadorDadosTransacaoMock;
import br.gov.caixa.simtx.util.mock.FornecedorDadosMock;
import br.gov.caixa.simtx.util.mock.GerenciadorFilasMQImplMock;
import br.gov.caixa.simtx.util.mock.GerenciadorTarefasMock;
import br.gov.caixa.simtx.util.mock.MensagemServidorMock;
import br.gov.caixa.simtx.util.mock.ProcessadorEnvioRetomadaTransacaoMock;
import br.gov.caixa.simtx.util.mock.ProcessadorEnvioSiccoMock;
import br.gov.caixa.simtx.util.mock.SimtxConfigMock;
import br.gov.caixa.simtx.util.mock.ValidadorRegrasNegocioMock;
import br.gov.caixa.simtx.util.xml.DadosBarramento;
import br.gov.caixa.simtx.util.xml.GeradorPassosMigrado;
import br.gov.caixa.simtx.util.xml.Resposta;
import br.gov.caixa.simtx.util.xml.TransformadorXsl;

public class ListaAssinaturaMultiplaTest {
	
	private String xmlEntradaCanal = "<ns:LISTA_TRANSACOES_PENDENTES_ASSINATURA_ENTRADA\r\n" + 
			"	xmlns:comuns=\"http://caixa.gov.br/simtx/comuns/comuns\" xmlns:enuns=\"http://caixa.gov.br/simtx/comuns/enuns\"\r\n" + 
			"	xmlns:h=\"http://caixa.gov.br/simtx/comuns/header\"\r\n" + 
			"	xmlns:ns=\"http://caixa.gov.br/simtx/lista_transacoes_pendentes_assinatura/v1/ns\"\r\n" + 
			"	xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\r\n" + 
			"	xsi:schemaLocation=\"http://caixa.gov.br/simtx/lista_transacoes_pendentes_assinatura/v1/ns Lista_Transacoes_Pendentes_Assinatura.xsd \">\r\n" + 
			"	<HEADER>\r\n" + 
			"		<SERVICO>\r\n" + 
			"			<CODIGO>110023</CODIGO>\r\n" + 
			"			<VERSAO>1</VERSAO>\r\n" + 
			"		</SERVICO>\r\n" + 
			"		<CANAL>\r\n" + 
			"			<SIGLA>SIIBC</SIGLA>\r\n" + 
			"			<NSU>16810030005</NSU>\r\n" + 
			"			<DATAHORA>20180815103818</DATAHORA>\r\n" + 
			"		</CANAL>\r\n" + 
			"		<MEIOENTRADA>1</MEIOENTRADA>\r\n" + 
			"		<IDENTIFICADOR_ORIGEM>\r\n" + 
			"			<ENDERECO_IP>127.0.0.1</ENDERECO_IP>\r\n" + 
			"		</IDENTIFICADOR_ORIGEM>\r\n" + 
			"		<USUARIO_SERVICO>smtxsd01</USUARIO_SERVICO>\r\n" + 
			"		<DATA_REFERENCIA>20180815</DATA_REFERENCIA>\r\n" + 
			"	</HEADER>\r\n" + 
			"	<CONTA>\r\n" + 
			"		<CONTA_SIDEC>\r\n" + 
			"			<UNIDADE>1679</UNIDADE>\r\n" + 
			"			<OPERACAO>001</OPERACAO>\r\n" + 
			"			<CONTA>00000008</CONTA>\r\n" + 
			"			<DV>9</DV>\r\n" + 
			"		</CONTA_SIDEC>\r\n" + 
			"		<TITULAR_CONTA>1</TITULAR_CONTA>\r\n" + 
			"	</CONTA>\r\n" + 
			"	<TOKEN>\r\n" + 
			"		<TOKEN>TESTEREDEA09        hN53nsgDQjbkpA2bvWSzQQcuc57Ebkbdn4CJMXxAmmmchpnG7dpiMMCm3cyf</TOKEN>\r\n" + 
			"		<ID_SESSAO>QpgkKgJzKBH7ANxBKADg5XQj.testebr</ID_SESSAO>\r\n" + 
			"	</TOKEN>\r\n" + 
			"	<LISTA_TRANSACOES_PENDENTES_ASSINATURA>\r\n" + 
			"		<TIPO>TODAS</TIPO>\r\n" + 
			"		<CPF>52649647345</CPF>\r\n" + 
			"		<DATA_INICIO>2018-03-07</DATA_INICIO>\r\n" + 
			"		<DATA_FIM>2018-03-09</DATA_FIM>\r\n" + 
			"	</LISTA_TRANSACOES_PENDENTES_ASSINATURA>\r\n" + 
			"</ns:LISTA_TRANSACOES_PENDENTES_ASSINATURA_ENTRADA>";
	
	private String xmlSaidaSibar = "<multicanal:SERVICO_SAIDA xmlns:multicanal=\"http://caixa.gov.br/sibar/multicanal/orquestracao\" xmlns:sibar_base=\"http://caixa.gov.br/sibar\">\r\n" + 
			"	<sibar_base:HEADER>\r\n" + 
			"		<VERSAO>1.0</VERSAO>\r\n" + 
			"		<OPERACAO>LISTA_TRANSACOES_PENDENTES</OPERACAO>\r\n" + 
			"		<SISTEMA_ORIGEM>SIIBC</SISTEMA_ORIGEM>\r\n" + 
			"		<DATA_HORA>20180820123200</DATA_HORA>\r\n" + 
			"	</sibar_base:HEADER>\r\n" + 
			"	<COD_RETORNO>00</COD_RETORNO>	\r\n" + 
			"	<ORIGEM_RETORNO>SIBAR</ORIGEM_RETORNO>\r\n" + 
			"	<MSG_RETORNO>SUCESSO</MSG_RETORNO>\r\n" + 
			"	<consultaassinaturaeletronica:CONSULTA_ASSINATURA_ELETRONICA_SAIDA xmlns:consultaassinaturaeletronica=\"http://caixa.gov.br/sibar/consulta_assinatura_eletronica/lista_transacoes_pendentes\">\r\n" + 
			"		<CONTROLE_NEGOCIAL>\r\n" + 
			"			<ORIGEM_RETORNO>BROKER-SPD9CJI0</ORIGEM_RETORNO>\r\n" + 
			"			<COD_RETORNO>00</COD_RETORNO>\r\n" + 
			"			<MENSAGENS>\r\n" + 
			"				<RETORNO>SUCESSO NA TRANSACAO</RETORNO>\r\n" + 
			"			</MENSAGENS>\r\n" + 
			"		</CONTROLE_NEGOCIAL>\r\n" + 
			"		<TRANSACOES>\r\n" + 
			"			<TRANSACAO>\r\n" + 
			"				<NSU_SIPER>5789789</NSU_SIPER>\r\n" + 
			"				<DATA>2001-01-01</DATA>\r\n" + 
			"				<DATA_PREVISTA_EFETIVACAO>2001-01-01</DATA_PREVISTA_EFETIVACAO>\r\n" + 
			"				<SITUACAO>PENDENTE</SITUACAO>\r\n" + 
			"				<RESUMO>RESUMO</RESUMO>\r\n" + 
			"				<ORIGEM>\r\n" + 
			"					<NSU>45464</NSU>\r\n" + 
			"					<SISTEMA>SIAUT</SISTEMA>\r\n" + 
			"				</ORIGEM>\r\n" + 
			"			</TRANSACAO>\r\n" + 
			"			<TRANSACAO>\r\n" + 
			"				<NSU_SIPER>9209</NSU_SIPER>\r\n" + 
			"				<DATA>2017-08-24</DATA>\r\n" + 
			"				<DATA_PREVISTA_EFETIVACAO>2018-08-24</DATA_PREVISTA_EFETIVACAO>\r\n" + 
			"				<SITUACAO>PENDENTE</SITUACAO>\r\n" + 
			"				<RESUMO>RESUMO</RESUMO>\r\n" + 
			"				<ORIGEM>\r\n" + 
			"					<NSU>9209</NSU>\r\n" + 
			"					<SISTEMA>SIAUT</SISTEMA>\r\n" + 
			"				</ORIGEM>\r\n" + 
			"			</TRANSACAO>\r\n" + 
			"		</TRANSACOES>\r\n" + 
			"	</consultaassinaturaeletronica:CONSULTA_ASSINATURA_ELETRONICA_SAIDA>\r\n" + 
			"</multicanal:SERVICO_SAIDA>";
	
	private ListaAssinaturaMultipla listaAssinaturaMultipla;
	
	private Mtxtb011VersaoServico versaoServico;
	
	private Mtxtb014Transacao transacao;
	
	private Mtxtb004Canal canal;
	
	@Before
	public void inicializarDados() {
		FornecedorDadosMock fornecedorDadosMock = new FornecedorDadosMock();
		MensagemServidorMock mensagemServidorMock = new MensagemServidorMock();
		SimtxConfigMock simtxConfigMock = new SimtxConfigMock();

		GerenciadorTarefasMock gerenciadorTarefas = new GerenciadorTarefasMock();
		gerenciadorTarefas.setFornecedorDados(fornecedorDadosMock);
		gerenciadorTarefas.setMensagemServidor(mensagemServidorMock);
		gerenciadorTarefas.setSimtxConfig(simtxConfigMock);

		TratadorDeExcecao tratadorDeExcecao = new TratadorDeExcecao();
		tratadorDeExcecao.setSimtxConfig(simtxConfigMock);
		
		GeradorPassosMigrado geradorPassosMigrado = new GeradorPassosMigrado();
		geradorPassosMigrado.setFornecedorDados(fornecedorDadosMock);
		
		this.listaAssinaturaMultipla = new ListaAssinaturaMultipla();
		this.listaAssinaturaMultipla.setFornecedorDados(fornecedorDadosMock);
		this.listaAssinaturaMultipla.setFornecedorDadosAssinaturaMultipla(new FornecedorDadosAssinaturaMultiplaMock());
		this.listaAssinaturaMultipla.setSimtxConfig(simtxConfigMock);
		this.listaAssinaturaMultipla.setTratadorDeExcecao(tratadorDeExcecao);
		this.listaAssinaturaMultipla.setGerenciadorTarefas(gerenciadorTarefas);
		this.listaAssinaturaMultipla.setGeradorPassosMigrado(geradorPassosMigrado);
		this.listaAssinaturaMultipla.setAtualizadorDadosTransacao(new AtualizadorDadosTransacaoMock());
		this.listaAssinaturaMultipla.setRepositorioArquivo(new RepositorioArquivo());
		this.listaAssinaturaMultipla.setExecucaoMq(new GerenciadorFilasMQImplMock());
		this.listaAssinaturaMultipla.setProcessadorEnvioRetomadaTransacao(new ProcessadorEnvioRetomadaTransacaoMock());
		this.listaAssinaturaMultipla.setProcessadorEnvioSicco(new ProcessadorEnvioSiccoMock());
		this.listaAssinaturaMultipla.setValidadorRegrasNegocio(new ValidadorRegrasNegocioMock());
		
		DadosBarramento dadosBarramento = new DadosBarramento();
		dadosBarramento.escrever(this.xmlEntradaCanal);
		this.listaAssinaturaMultipla.setDadosBarramento(dadosBarramento);
		
		Mtxtb001Servico servico = new Mtxtb001Servico();
		servico.setNuServico(110023L);

		this.versaoServico = new Mtxtb011VersaoServico();
		Mtxtb011VersaoServicoPK versaoServicoPK = new Mtxtb011VersaoServicoPK();
		versaoServicoPK.setNuServico001(110023L);
		versaoServicoPK.setNuVersaoServico(1);
		this.versaoServico.setId(versaoServicoPK);
		this.versaoServico.setMtxtb001Servico(servico);
		this.versaoServico.setDeXsltRequisicao("servico/Lista_Transacoes_Pendentes_Assinatura/V1/Lista_Transacoes_Pendentes_Assinatura_Req.xsl");
		this.versaoServico.setDeXsltResposta("servico/Lista_Transacoes_Pendentes_Assinatura/V1/Lista_Transacoes_Pendentes_Assinatura_Resp.xsl");
		this.versaoServico.setMigrado(1);
		
		this.transacao = new Mtxtb014Transacao();
		this.transacao.setNuNsuTransacao(15L);
		this.transacao.setCoCanalOrigem("104");
		this.transacao.setDtReferencia(new Date());
		
		this.canal = new Mtxtb004Canal();
		this.canal.setNuCanal(104L);
		
	}
	
	@Test
	public void buscarTransacoesSimtx() {
		List<Mtxtb027TransacaoAssinaturaMultipla> lista = this.listaAssinaturaMultipla
				.buscarTransacoesSimtx(this.xmlEntradaCanal);
		assertFalse(lista.isEmpty());
	}
	
	@Test
	public void buscarTransacoesSimtxListaVazia() {
		List<Mtxtb027TransacaoAssinaturaMultipla> lista = this.listaAssinaturaMultipla
				.buscarTransacoesSimtx(this.xmlEntradaCanal.replaceAll("1679", "1678"));
		assertTrue(lista.isEmpty());
	}
	
	@Test
	public void buscarTransacoesContaNsgd() {
		List<Mtxtb027TransacaoAssinaturaMultipla> lista = this.listaAssinaturaMultipla
				.buscarTransacoesSimtx(this.xmlEntradaCanal.replaceAll("CONTA_SIDEC", "CONTA_NSGD").replaceAll("OPERACAO", "PRODUTO"));
		assertFalse(lista.isEmpty());
	}
	
	@Test
	public void buscarTransacoesSimtxErro() {
		List<Mtxtb027TransacaoAssinaturaMultipla> lista = this.listaAssinaturaMultipla
				.buscarTransacoesSimtx(this.xmlEntradaCanal.replaceAll("1679", ""));
		assertTrue(lista.isEmpty());
	}
	
	@Test
	public void montarTransacoesSimtx() {
		List<Mtxtb027TransacaoAssinaturaMultipla> lista = this.listaAssinaturaMultipla
				.buscarTransacoesSimtx(this.xmlEntradaCanal);
		assertFalse(lista.isEmpty());
		
		this.listaAssinaturaMultipla.montarTransacoesSimtx(lista);
		String tagTransacoes = this.listaAssinaturaMultipla.getDadosBarramento().xpathTexto("/BUSDATA/TRANSACOES");
		assertNotEquals("", tagTransacoes);
	}
	
	@Test
	public void montarTransacoesSimtxErro() {
		this.listaAssinaturaMultipla.montarTransacoesSimtx(null);
	}
	
	@Test
	public void verificarSePossuiTransacoes() throws ServicoException {
		List<Mtxtb027TransacaoAssinaturaMultipla> lista = this.listaAssinaturaMultipla
				.buscarTransacoesSimtx(this.xmlEntradaCanal);
		this.listaAssinaturaMultipla.getDadosBarramento().escrever(this.xmlSaidaSibar);
		this.listaAssinaturaMultipla.verificarSePossuiTransacoes(lista);
	}
	
	@Test
	public void verificarSePossuiTransacoesErro() {
		try {
			this.listaAssinaturaMultipla.verificarSePossuiTransacoes(null);
			fail();
		} 
		catch (ServicoException e) {
			assertEquals(e.getMensagem().getCodigoRetorno(),
					MensagemRetorno.ERRO_INTERNO.getCodigo().toString());
		}
	}
	
	@Test
	public void verificarSePossuiTransacoesApenasSimtx() throws ServicoException {
		List<Mtxtb027TransacaoAssinaturaMultipla> lista = this.listaAssinaturaMultipla
				.buscarTransacoesSimtx(this.xmlEntradaCanal);
		this.listaAssinaturaMultipla.verificarSePossuiTransacoes(lista);
	}
	
	@Test
	public void verificarSePossuiTransacoesApenasSiaut() throws ServicoException {
		this.listaAssinaturaMultipla.getDadosBarramento().escrever(this.xmlSaidaSibar);
		this.listaAssinaturaMultipla.verificarSePossuiTransacoes(new ArrayList<Mtxtb027TransacaoAssinaturaMultipla>());
	}
	
	@Test
	public void verificarSePossuiTransacoesSemTransacoes() {
		try {
			this.listaAssinaturaMultipla.verificarSePossuiTransacoes(new ArrayList<Mtxtb027TransacaoAssinaturaMultipla>());
			fail();
		} 
		catch (ServicoException e) {
			assertEquals(e.getMensagem().getCodigoRetorno(),
					MensagemRetorno.NAO_ENCONTROU_RESULTADOS.getCodigo().toString());
		}
	}
	
	@Test
	public void transformarXml() throws Exception {
		String xml = this.listaAssinaturaMultipla.transformarXml(this.versaoServico, this.transacao, this.canal);
		assertNotEquals("", xml);
		assertTrue(xml.contains("<multicanal:SERVICO_ENTRADA"));
		assertTrue(xml.contains("<lista_transacoes_pendentes:CONSULTA_ASSINATURA_ELETRONICA_ENTRADA"));
		
		this.listaAssinaturaMultipla.getDadosBarramento().escrever(xml);
		
		SimtxConfigMock simtxConfigMock = new SimtxConfigMock();
		
		String caminhoXls = simtxConfigMock.getCaminhoXslt() + "negocial/Lista_Transacoes_Pendentes_Assinatura/V1/Lista_Transacoes_Pendentes_Assinatura_Req.xsl";
		String arquivoXsl = new RepositorioArquivo().recuperarArquivo(caminhoXls);
		
		String xmlTarefa = new TransformadorXsl().transformar(this.listaAssinaturaMultipla.getDadosBarramento().getDadosLeitura(), arquivoXsl);
		assertNotEquals("", xmlTarefa);
		assertTrue(xml.contains("<multicanal:SERVICO_ENTRADA"));
		assertTrue(xml.contains("<lista_transacoes_pendentes:CONSULTA_ASSINATURA_ELETRONICA_ENTRADA"));
		
		xmlTarefa = xmlTarefa
				.replaceAll("<lista_transacoes_pendentes:CONSULTA_ASSINATURA_ELETRONICA_ENTRADA>", "<DADOS>")
				.replaceAll("</lista_transacoes_pendentes:CONSULTA_ASSINATURA_ELETRONICA_ENTRADA>", "</DADOS>")
				.replaceAll("<multicanal", "<lista_transacoes_pendentes")
				.replaceAll("</multicanal", "</lista_transacoes_pendentes");
		
		File fileXsd = new File(simtxConfigMock.getCaminhoXsd() + "validacao_entradas/SIBAR/NEGOCIAL/V1/Consulta_Assinatura_Eletronica_Lista_Transacoes_Pendentes.xsd");
		XmlValidador xmlValidador = new XmlValidador();
		xmlValidador.validarXmlcomXsd(xmlTarefa, fileXsd);
	}
	
	@Test
	public void transformarXmlErro() {
		try {
			this.listaAssinaturaMultipla.transformarXml(this.versaoServico, null, this.canal);
		} 
		catch (ServicoException e) {
			assertEquals(e.getMensagem().getCodigoRetorno(),
					MensagemRetorno.ERRO_INTERNO.getCodigo().toString());
		}
	}
	
	@Test
	public void transformarXmlResposta() throws Exception {
		List<Mtxtb027TransacaoAssinaturaMultipla> lista = this.listaAssinaturaMultipla
				.buscarTransacoesSimtx(this.xmlEntradaCanal);
		assertFalse(lista.isEmpty());
		
		Resposta resposta = new Resposta();
		resposta.setAcao(AcaoRetorno.AUTORIZADORA.getRotulo());
		resposta.setCodigo("00");
		resposta.setIcTipoMensagem(AcaoRetorno.AUTORIZADORA.getTipo());
		resposta.setMensagemNegocial("Transacao com sucesso");
		resposta.setMensagemTecnica("Transacao com sucesso");
		resposta.setOrigem(Constantes.ORIGEM_SIMTX);
		
		String xmlResposta = this.listaAssinaturaMultipla.transformarXml(this.versaoServico, this.transacao, resposta);
		assertNotEquals("", xmlResposta);
		assertTrue(xmlResposta.contains("<ns:LISTA_TRANSACOES_PENDENTES_ASSINATURA_SAIDA"));
//		assertTrue(xmlResposta.contains("<LISTA_TRANSACOES_PENDENTES_ASSINATURA>"));
//		assertTrue(xmlResposta.contains("<TRANSACOES>"));
//		assertTrue(xmlResposta.contains("<TRANSACAO>"));
		
		File fileXsd = new File(new SimtxConfigMock().getCaminhoXsd() + "validacao_entradas/SIMTX_TYPES/Lista_Transacoes_Pendentes_Assinatura/V1/Lista_Transacoes_Pendentes_Assinatura.xsd");
		XmlValidador xmlValidador = new XmlValidador();
		xmlValidador.validarXmlcomXsd(xmlResposta, fileXsd);
	}
	
	@Test
	public void transformarXmlRespostaErro() {
		try {
			this.listaAssinaturaMultipla.transformarXml(this.versaoServico, this.transacao, new Resposta());
		} 
		catch (ServicoException e) {
			assertEquals(e.getMensagem().getCodigoRetorno(),
					MensagemRetorno.ERRO_INTERNO.getCodigo().toString());
		}
	}
	
	@Test
	public void processar() {
		ParametrosAdicionais parametrosAdicionais = new ParametrosAdicionais(this.xmlEntradaCanal, "51654",
				"a", "b", false);
		this.listaAssinaturaMultipla.processar(parametrosAdicionais);
	}

}
