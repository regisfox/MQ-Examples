package br.gov.caixa.simtx.manutencao.retomar.transacao.test;

import java.io.File;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.runners.MockitoJUnitRunner;

import br.gov.caixa.simtx.manutencao.retomar.transacao.ProcessadorRetomadaTransacao;
import br.gov.caixa.simtx.util.ParametrosAdicionais;
import br.gov.caixa.simtx.util.SimtxConfig;
import br.gov.caixa.simtx.util.gerenciador.ValidadorRegrasNegocio;
import br.gov.caixa.simtx.util.gerenciador.cache.RepositorioArquivo;
import br.gov.caixa.simtx.util.mock.AtualizadorDadosTransacaoMock;
import br.gov.caixa.simtx.util.mock.FornecedorDadosAgendamentoMock;
import br.gov.caixa.simtx.util.mock.FornecedorDadosMock;
import br.gov.caixa.simtx.util.mock.GerenciadorTarefasMock;
import br.gov.caixa.simtx.util.mock.ProcessadorEnvioRetomadaTransacaoMock;
import br.gov.caixa.simtx.util.mock.TratadorDeExcecaoMock;
import br.gov.caixa.simtx.util.mock.constantes.ConstantesXmlMockRetomada;


@RunWith(MockitoJUnitRunner.class)
@FixMethodOrder(MethodSorters.JVM)
public class ProcessadorRetomadaFluxoCoreTransacaoTest {
	
	private static final Logger logger = Logger.getLogger(ProcessadorRetomadaFluxoCoreTransacaoTest.class);
	
	private ProcessadorRetomadaTransacao processadorRetomadaTransacaoNew;
	private ProcessadorRetomadaTransacao processadorRetomadaTransacao;

	@Before
	public void iniciarDados() {

		SimtxConfig simtxConfig = iniciarSimtxConfig();
		RepositorioArquivo repositorioArquivo = new RepositorioArquivo();
		processadorRetomadaTransacaoNew = new ProcessadorRetomadaTransacao();
		processadorRetomadaTransacao = new ProcessadorRetomadaTransacao(new AtualizadorDadosTransacaoMock(), new FornecedorDadosAgendamentoMock(), new TratadorDeExcecaoMock(), new ValidadorRegrasNegocio(), simtxConfig, repositorioArquivo);
		FornecedorDadosMock fornecedorDadosMock = new FornecedorDadosMock();
		processadorRetomadaTransacao.setFornecedorDados(fornecedorDadosMock);
		GerenciadorTarefasMock gerenciadorTarefasMock = new GerenciadorTarefasMock();
		gerenciadorTarefasMock.setFornecedorDados(fornecedorDadosMock);
		processadorRetomadaTransacao.setGerenciadorTarefas(gerenciadorTarefasMock);
		ValidadorRegrasNegocio validadorRegrasNegocio = new ValidadorRegrasNegocio();
		validadorRegrasNegocio.setFornecedorDados(fornecedorDadosMock);
		validadorRegrasNegocio.setSimtxConfig(simtxConfig);
		processadorRetomadaTransacao.setValidadorRegrasNegocio(validadorRegrasNegocio);
		ProcessadorEnvioRetomadaTransacaoMock envioRetomadaTransacaoMock = new ProcessadorEnvioRetomadaTransacaoMock();
		envioRetomadaTransacaoMock.setRepositorio(repositorioArquivo);
		processadorRetomadaTransacao.setProcessadorEnvioRetomadaTransacao(envioRetomadaTransacaoMock);
	}
	
	private SimtxConfig iniciarSimtxConfig() {

		File currentDir = new File("");
		String dirFiles = currentDir.getAbsolutePath().replace("\\simtx-manutencao", "\\docs\\simtx_home");
		SimtxConfig simtxConfig;

		try {

			simtxConfig = new SimtxConfig();

		} catch (Exception e) {
			logger.error(e);
			logger.info("Inicializando variavel pasta.simtx.home para o diretorio corrente " + dirFiles);
			System.setProperty("pasta.simtx.home", dirFiles);
			simtxConfig = new SimtxConfig();
		}

		return simtxConfig;
	}
	
	@Test
	public void deveProcessarMensagemInvalida() {
		ParametrosAdicionais parametrosAdicionais = new ParametrosAdicionais();
		parametrosAdicionais.setXmlMensagem("ENVIO MENSAGEM INCORRETA");
		processadorRetomadaTransacaoNew.processar(parametrosAdicionais);
	}
	
	@Test
	public void deveProcessarMensagemInvalidaXML() {
		ParametrosAdicionais parametrosAdicionais = new ParametrosAdicionais();
		parametrosAdicionais.setXmlMensagem("<xml></xml>");
		processadorRetomadaTransacaoNew.processar(parametrosAdicionais);
	}
	
	@Test
	public void deveProcessarMensagemNull() {
		ParametrosAdicionais parametrosAdicionais = new ParametrosAdicionais();
		parametrosAdicionais.setXmlMensagem("");
		processadorRetomadaTransacaoNew.processar(parametrosAdicionais);
	}
	
	@Test
	public void deveProcessarMensagemComErroSIBARGravarAMensagem() {
		ParametrosAdicionais parametrosAdicionais = new ParametrosAdicionais();
		parametrosAdicionais.setXmlMensagem(ConstantesXmlMockRetomada.XML_TRANSACAO);
		processadorRetomadaTransacao.processar(parametrosAdicionais);
	}
	
	@Test
	public void deveProcessarMensagemComErroSIBARSemCodigoServico() {
		ParametrosAdicionais parametrosAdicionais = new ParametrosAdicionais();
		parametrosAdicionais.setXmlMensagem(ConstantesXmlMockRetomada.XML_TRANSACAO_SEM_CODIGO_SERVICO);
		processadorRetomadaTransacao.processar(parametrosAdicionais);
	}
	
	@Test
	public void deveProcessarMensagemComErroSIBARSemCodigoUsuario() {
		ParametrosAdicionais parametrosAdicionais = new ParametrosAdicionais();
		parametrosAdicionais.setXmlMensagem(ConstantesXmlMockRetomada.XML_TRANSACAO_SEM_CODIGO_USUARIO);
		processadorRetomadaTransacao.processar(parametrosAdicionais);
	}
	
	@Test
	public void deveProcessarMensagemComEGravarAMensagem() {
		ParametrosAdicionais parametrosAdicionais = new ParametrosAdicionais();
		parametrosAdicionais.setXmlMensagem(ConstantesXmlMockRetomada.XML_ERRO_PREENCHIMENTO_TRANSACAO);
		processadorRetomadaTransacao.processar(parametrosAdicionais);
	}
	
	@Test
	public void deveProcessarMensagemAgendamento() {
		ParametrosAdicionais parametrosAdicionais = new ParametrosAdicionais();
		parametrosAdicionais.setXmlMensagem(ConstantesXmlMockRetomada.XML_TRANSACAO_AGENDAMENTO);
		processadorRetomadaTransacao.processar(parametrosAdicionais);
	}
	
	@Test
	public void deveProcessarMensagemErroTagDados() {
		ParametrosAdicionais parametrosAdicionais = new ParametrosAdicionais();
		parametrosAdicionais.setXmlMensagem(ConstantesXmlMockRetomada.XML_ERRO_TAG_PREENCHIMENTO_TRANSACAO);
		processadorRetomadaTransacao.processar(parametrosAdicionais);
	}
	
	
	@Test
	public void deveProcessarMensagemValidaBoletoAgendamento() {
		ParametrosAdicionais parametrosAdicionais = new ParametrosAdicionais();
		parametrosAdicionais.setXmlMensagem(ConstantesXmlMockRetomada.XML_TRANSACAO_VALIDA_AGENDAMENTO);
		processadorRetomadaTransacao.processar(parametrosAdicionais);
	}
	
	@Test
	public void deveProcessarMensagemValidaBoletoAgendamentoErroData() {
		ParametrosAdicionais parametrosAdicionais = new ParametrosAdicionais();
		parametrosAdicionais.setXmlMensagem(ConstantesXmlMockRetomada.XML_TRANSACAO_VALIDA_AGENDAMENTO_ERRO);
		processadorRetomadaTransacao.processar(parametrosAdicionais);
	}
	
	@Test
	public void deveProcessarMensagemValidaBoletoAgendamentoErroIteracaoVazio() {
		ParametrosAdicionais parametrosAdicionais = new ParametrosAdicionais();
		parametrosAdicionais.setXmlMensagem(ConstantesXmlMockRetomada.XML_TRANSACAO_VALIDA_AGENDAMENTO_ERRO_ITERACAO_VAZIO);
		processadorRetomadaTransacao.processar(parametrosAdicionais);
	}
	
	
}
