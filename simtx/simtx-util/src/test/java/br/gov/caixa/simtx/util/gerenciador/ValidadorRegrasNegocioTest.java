package br.gov.caixa.simtx.util.gerenciador;

import static org.junit.Assert.assertNotNull;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;

import br.gov.caixa.simtx.persistencia.constante.AcaoRetorno;
import br.gov.caixa.simtx.persistencia.constante.Constantes;
import br.gov.caixa.simtx.persistencia.constante.MensagemRetorno;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb002Tarefa;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb003ServicoTarefa;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb003ServicoTarefaPK;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb012VersaoTarefa;
import br.gov.caixa.simtx.util.exception.ServicoException;
import br.gov.caixa.simtx.util.mock.MensagemServidorMock;
import br.gov.caixa.simtx.util.mock.FornecedorDadosMock;
import br.gov.caixa.simtx.util.mock.SimtxConfigMock;
import br.gov.caixa.simtx.util.xml.BuscadorResposta;
import br.gov.caixa.simtx.util.xml.BuscadorTextoXml;
import br.gov.caixa.simtx.util.xml.Resposta;

public class ValidadorRegrasNegocioTest {
	
	private ValidadorRegrasNegocio validadorRegrasNegocio;
	
	private String xml = 
			"	<baixaoperacional:MANUTENCAO_COBRANCA_BANCARIA_SAIDA xmlns:baixaoperacional=\"http://caixa.gov.br/sibar/manutencao_cobranca_bancaria/pagamento\">\r\n" + 
			"		<CONTROLE_NEGOCIAL>\r\n" + 
			"			<ORIGEM_RETORNO>SIGCB</ORIGEM_RETORNO>\r\n" + 
			"			<COD_RETORNO>000</COD_RETORNO>\r\n" + 
			"			<MSG_RETORNO>PAGAMENTO REALIZADO COM SUCESSO</MSG_RETORNO>\r\n" + 
			"		</CONTROLE_NEGOCIAL>\r\n" + 
			"		<SITUACAO_CONTINGENCIA>SEM_CONTINGENCIA</SITUACAO_CONTINGENCIA>\r\n" + 
			"	</baixaoperacional:MANUTENCAO_COBRANCA_BANCARIA_SAIDA>\r\n";
	
	@Before
	public void inicializarDados() throws Exception {
		this.validadorRegrasNegocio = new ValidadorRegrasNegocio();
		
		SimtxConfigMock simtxConfigMock = new SimtxConfigMock();
		
		this.validadorRegrasNegocio.setFornecedorDados(new FornecedorDadosMock());
		this.validadorRegrasNegocio.setMensagemServidor(new MensagemServidorMock());
		this.validadorRegrasNegocio.setSimtxConfig(simtxConfigMock);
		
	}
	
	@Test
	public void recuperarMensagemTarefa() throws ServicoException {
		Mtxtb002Tarefa tarefa = new Mtxtb002Tarefa();
		tarefa.setNoTarefa("Nome Teste");
		Mtxtb012VersaoTarefa versaoTarefa = new Mtxtb012VersaoTarefa();
		versaoTarefa.setDeXsltResposta(
				new SimtxConfigMock().getCaminhoXslt() + "negocial/Efetiva_Limite/V1/efetivaLimite_Resp.xsl");
		Mtxtb003ServicoTarefaPK pk = new Mtxtb003ServicoTarefaPK();
		pk.setNuTarefa012(new Long(100001));
		Mtxtb003ServicoTarefa servicoTarefa = new Mtxtb003ServicoTarefa();
		servicoTarefa.setMtxtb012VersaoTarefa(versaoTarefa);
		
		Resposta resposta = new Resposta();
		resposta.setAcao(AcaoRetorno.AUTORIZADORA.getRotulo());
		resposta.setCodigo("00");
		resposta.setIcTipoMensagem(AcaoRetorno.AUTORIZADORA.getTipo());
		resposta.setMensagemNegocial("Mensagem Negocial");
		resposta.setMensagemTecnica("Mensagem Negocial");
		resposta.setMensagemRetorno("Mensagem Retorno");
		resposta.setOrigem(Constantes.ORIGEM_SIMTX);
		
		this.validadorRegrasNegocio.recuperarMensagemTarefa(versaoTarefa, resposta);
	}
	
	@Test
	public void recuperarMensagemSibar() {
		try {
			Resposta resposta = new Resposta();
			resposta.setAcao(AcaoRetorno.AUTORIZADORA.getRotulo());
			resposta.setCodigo("00");
			resposta.setIcTipoMensagem(AcaoRetorno.AUTORIZADORA.getTipo());
			resposta.setMensagemNegocial("Mensagem Negocial");
			resposta.setMensagemRetorno("Mensagem Retorno");
			resposta.setOrigem(Constantes.ORIGEM_SIMTX);
			
			this.validadorRegrasNegocio.recuperarMensagemSibar(resposta);
			
		} 
		catch (ServicoException e) {
			assertNotNull(e);
		}
	}
	
	@Test
	public void recuperarMensagemInvalida() {
		try {
			this.validadorRegrasNegocio.recuperarMensagemInvalida();
		} 
		catch (ServicoException e) {
			Assert.assertEquals(e.getMensagem().getCodigoRetorno(),
					MensagemRetorno.LEIAUTE_MENSAGEM_RESPOSTA_INVALIDO.getCodigo().toString());
		}
	}
	
	@Test
	public void buscarRespostaTarefaBarramento() throws ParserConfigurationException, SAXException, IOException {
		BuscadorResposta buscadorResposta = new BuscadorResposta();
		BuscadorTextoXml buscador = new BuscadorTextoXml(this.xml);
		Resposta resposta = buscadorResposta.buscarRespostaTarefaBarramento(buscador);
		Assert.assertNotNull(resposta);
	}

}
