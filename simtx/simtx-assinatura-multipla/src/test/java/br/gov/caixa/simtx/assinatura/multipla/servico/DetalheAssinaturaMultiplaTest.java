package br.gov.caixa.simtx.assinatura.multipla.servico;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;

import br.gov.caixa.simtx.assinatura.multipla.infra.FornecedorDadosAssinaturaMultiplaMock;
import br.gov.caixa.simtx.persistencia.assinaturamultipla.entidade.Mtxtb027TransacaoAssinaturaMultipla;
import br.gov.caixa.simtx.util.exception.ServicoException;
import br.gov.caixa.simtx.util.gerenciador.TratadorDeExcecao;
import br.gov.caixa.simtx.util.gerenciador.cache.RepositorioArquivo;
import br.gov.caixa.simtx.util.mock.AtualizadorDadosTransacaoMock;
import br.gov.caixa.simtx.util.mock.FornecedorDadosMock;
import br.gov.caixa.simtx.util.mock.ProcessadorEnvioRetomadaTransacaoMock;
import br.gov.caixa.simtx.util.mock.ProcessadorEnvioSiccoMock;
import br.gov.caixa.simtx.util.mock.SimtxConfigMock;
import br.gov.caixa.simtx.util.mock.ValidadorRegrasNegocioMock;
import br.gov.caixa.simtx.util.xml.DadosBarramento;
import br.gov.caixa.simtx.util.xml.GeradorPassosMigrado;

public class DetalheAssinaturaMultiplaTest {
	
	private String xmlEntradaCanal = 
			"<ns:DETALHE_TRANSACAO_PENDENTE_ASSINATURA_ENTRADA\r\n" + 
			"	xmlns:comuns=\"http://caixa.gov.br/simtx/comuns/comuns\" \r\n" + 
			"	xmlns:enuns=\"http://caixa.gov.br/simtx/comuns/enuns\"\r\n" + 
			"	xmlns:h=\"http://caixa.gov.br/simtx/comuns/header\"\r\n" + 
			"	xmlns:ns=\"http://caixa.gov.br/simtx/detalhe_transacao_pendente_assinatura/v1/ns\"\r\n" + 
			"	xmlns:ns1=\"http://caixa.gov.br/simtx/negocial/manutencao_cheque/v1/ns\"\r\n" + 
			"	xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\r\n" + 
			"	xsi:schemaLocation=\"http://caixa.gov.br/simtx/detalhe_transacao_pendente_assinatura/v1/ns Detalhe_Transacao_Pendente_Assinatura.xsd \">\r\n" + 
			"	<HEADER>\r\n" + 
			"		<SERVICO>\r\n" + 
			"			<CODIGO>110024</CODIGO>\r\n" + 
			"			<VERSAO>1</VERSAO>\r\n" + 
			"		</SERVICO>\r\n" + 
			"		<CANAL>\r\n" + 
			"			<SIGLA>SIIBC</SIGLA>\r\n" + 
			"			<NSU>16810030005</NSU>\r\n" + 
			"			<DATAHORA>20180815103818</DATAHORA>\r\n" + 
			"		</CANAL>\r\n" + 
			"		<MEIOENTRADA>0</MEIOENTRADA>\r\n" + 
			"		<IDENTIFICADOR_ORIGEM>\r\n" + 
			"			<ENDERECO_IP>127.0.0.1</ENDERECO_IP>\r\n" + 
			"		</IDENTIFICADOR_ORIGEM>\r\n" + 
			"		<USUARIO_SERVICO>smtxsd01</USUARIO_SERVICO>\r\n" + 
			"		<DATA_REFERENCIA>20180822</DATA_REFERENCIA>\r\n" + 
			"	</HEADER>\r\n" + 
			"	<NSU_MTX>13616</NSU_MTX>\r\n" + 
			"</ns:DETALHE_TRANSACAO_PENDENTE_ASSINATURA_ENTRADA>\r\n" ;
	
	private DetalheAssinaturaMultipla detalheAssinaturaMultipla;
	
	
	@Before
	public void inicializarDados() {
		FornecedorDadosMock fornecedorDadosMock = new FornecedorDadosMock();
		SimtxConfigMock simtxConfigMock = new SimtxConfigMock();

		TratadorDeExcecao tratadorDeExcecao = new TratadorDeExcecao();
		tratadorDeExcecao.setSimtxConfig(simtxConfigMock);
		
		GeradorPassosMigrado geradorPassosMigrado = new GeradorPassosMigrado();
		geradorPassosMigrado.setFornecedorDados(fornecedorDadosMock);
		
		this.detalheAssinaturaMultipla = new DetalheAssinaturaMultipla();
		this.detalheAssinaturaMultipla.setFornecedorDados(fornecedorDadosMock);
		this.detalheAssinaturaMultipla.setFornecedorDadosAssinaturaMultipla(new FornecedorDadosAssinaturaMultiplaMock());
		this.detalheAssinaturaMultipla.setSimtxConfig(simtxConfigMock);
		this.detalheAssinaturaMultipla.setTratadorDeExcecao(tratadorDeExcecao);
		this.detalheAssinaturaMultipla.setAtualizadorDadosTransacao(new AtualizadorDadosTransacaoMock());
		this.detalheAssinaturaMultipla.setRepositorioArquivo(new RepositorioArquivo());
		this.detalheAssinaturaMultipla.setProcessadorEnvioRetomadaTransacao(new ProcessadorEnvioRetomadaTransacaoMock());
		this.detalheAssinaturaMultipla.setProcessadorEnvioSicco(new ProcessadorEnvioSiccoMock());
		this.detalheAssinaturaMultipla.setValidadorRegrasNegocio(new ValidadorRegrasNegocioMock());
		
		DadosBarramento dadosBarramento = new DadosBarramento();
		dadosBarramento.escrever(this.xmlEntradaCanal);
		this.detalheAssinaturaMultipla.setDadosBarramento(dadosBarramento);
	}
	
	@Test
	public void buscarTransacaoAssinaturaMultipla() throws ServicoException {
		Mtxtb027TransacaoAssinaturaMultipla assinaturaMultipla = this.detalheAssinaturaMultipla
				.buscarTransacaoAssinaturaMultipla();
		assertNotNull(assinaturaMultipla);
		assertFalse(assinaturaMultipla.getXmlNegocial().isEmpty());
	}

}
