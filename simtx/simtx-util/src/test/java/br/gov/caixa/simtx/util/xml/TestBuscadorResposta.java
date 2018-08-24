package br.gov.caixa.simtx.util.xml;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.junit.Test;
import org.xml.sax.SAXException;

import org.junit.Assert;

public class TestBuscadorResposta {

	
	@Test
	public void deveEncontrarOkNaNegocial() throws ParserConfigurationException, SAXException, IOException {
		BuscadorResposta b = new BuscadorResposta();
		Resposta resposta = b.buscarRespostaBarramento(new BuscadorTextoXml(xmlSaidaBarramento), "VALIDA_PERMISSAO");
		
		Assert.assertEquals(resposta.getOrigem(), "SIPER");
		Assert.assertEquals(resposta.getCodigo(), "00");
		Assert.assertEquals(resposta.getMensagemNegocial(), "sucesso");
		Assert.assertEquals(resposta.getMensagemRetorno(), "RETORNO");
		Assert.assertEquals(resposta.getMensagemInstitucional(), "INSTITUCIONAL");
		Assert.assertEquals(resposta.getMensagemInformativa(), "INFORMATIVA");
		Assert.assertEquals(resposta.getMensagemTela(), "TELA");
		Assert.assertEquals(resposta.getMensagemTecnica(), "MENSAGEM");
	}
	
	@Test
	public void deveEncontrarMensagem00() throws ParserConfigurationException, SAXException, IOException {
		BuscadorResposta b = new BuscadorResposta();
		BuscadorTextoXml buscadorXml = new BuscadorTextoXml(xmlSaidaIncompleta);
		Resposta resposta = b.buscarRespostaTarefaBarramento(buscadorXml);
		
		Assert.assertEquals(resposta.getOrigem(), "SIATR");
		Assert.assertEquals(resposta.getCodigo(), "00");
	}
	
	private String xmlSaidaIncompleta = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n" + 
			"<efetivacao:CONTROLE_LIMITE_SAIDA xmlns:efetivacao=\"http://caixa.gov.br/sibar/controle_limite/efetivacao\" xmlns:multicanal=\"http://caixa.gov.br/sibar/multicanal/orquestracao\" xmlns:sibar_base=\"http://caixa.gov.br/sibar\">\r\n" + 
			"	<CONTROLE_NEGOCIAL>\r\n" + 
			"		<ORIGEM_RETORNO>SIATR</ORIGEM_RETORNO>\r\n" + 
			"		<COD_RETORNO>00</COD_RETORNO>\r\n" + 
			"	</CONTROLE_NEGOCIAL>\r\n" + 
			"</efetivacao:CONTROLE_LIMITE_SAIDA>";
	
	private String xmlSaidaBarramento = "<mtx:SERVICO_SAIDA\r\n" + 
			"		xmlns:assianturasimples=\"http://caixa.gov.br/sibar/valida_permissao/assinatura_simples\" \r\n" + 
			"		xmlns:sibar_base=\"http://caixa.gov.br/sibar\" xmlns:cartao=\"http://caixa.gov.br/sibar/valida_cartao\"\r\n" + 
			"		xmlns:validapermissao=\"http://caixa.gov.br/sibar/autenticacao_biometrica\"\r\n" + 
			"		xmlns:beneficiario=\"http://caixa.gov.br/sibar/manutencao_beneficiario_inss\"\r\n" + 
			"		xmlns:manutencaocheque=\"http://caixa.gov.br/sibar/manutencao_cheque/contraordem\" \r\n" + 
			"		xmlns:mtx=\"http://caixa.gov.br/simtx/orquestracao\">\r\n" + 
			"	<sibar_base:HEADER>\r\n" + 
			"		<VERSAO>1.0</VERSAO>\r\n" + 
			"		<USUARIO_SERVICO>SMTXS01D</USUARIO_SERVICO>\r\n" + 
			"		<OPERACAO>SERVICO_MIGRADO</OPERACAO>\r\n" + 
			"		<SISTEMA_ORIGEM>SIMAA</SISTEMA_ORIGEM>\r\n" + 
			"		<UNIDADE>7266</UNIDADE>\r\n" + 
			"		<IDENTIFICADOR_ORIGEM>MODELO XML</IDENTIFICADOR_ORIGEM>\r\n" + 
			"		<DATA_HORA>20171124122500</DATA_HORA>\r\n" + 
			"		<ID_PROCESSO>#CARTAO_INSS#BIOMETRIA</ID_PROCESSO>\r\n" + 
			"	</sibar_base:HEADER>\r\n" + 
			"	<COD_RETORNO>00</COD_RETORNO>\r\n" + 
			"	<ORIGEM_RETORNO>BARRAMENTO_MULTICANAL</ORIGEM_RETORNO>\r\n" + 
			"	<MSG_RETORNO>INTEGRACAO REALIZADA COM SUCESSO.</MSG_RETORNO>\r\n" + 
			"\r\n" + 
			"	<assianturasimples:VALIDA_PERMISSAO_SAIDA>\r\n" + 
			"		<CONTROLE_NEGOCIAL>\r\n" + 
			"			<ORIGEM_RETORNO>SIPER</ORIGEM_RETORNO>\r\n" + 
			"			<COD_RETORNO>00</COD_RETORNO>\r\n" + 
			"			<MSG_RETORNO>sucesso</MSG_RETORNO>\r\n" + 
			"			<NSU>123</NSU>\r\n" + 
			"			<MENSAGENS>\r\n" + 
			"				<RETORNO>RETORNO</RETORNO>\r\n" + 
			"				<INSTITUCIONAL>INSTITUCIONAL</INSTITUCIONAL>\r\n" + 
			"				<INFORMATIVA>INFORMATIVA</INFORMATIVA>\r\n" + 
			"				<TELA>TELA</TELA>\r\n" + 
			"				<MENSAGEM>MENSAGEM</MENSAGEM>\r\n" + 
			"			</MENSAGENS>\r\n" + 
			"		</CONTROLE_NEGOCIAL>\r\n" + 
			"		<DATA_TRANSACAO>2018-01-15</DATA_TRANSACAO>\r\n" + 
			"		<ASSINATURA>\r\n" + 
			"			<CLASSIFICACAO>ASSINATURA_FINAL</CLASSIFICACAO>\r\n" + 
			"		</ASSINATURA>\r\n" + 
			"		<ASSINANTES>\r\n" + 
			"			<ASSINANTE>\r\n" + 
			"				<CPF>36199093852</CPF>\r\n" + 
			"			</ASSINANTE>\r\n" + 
			"		</ASSINANTES>\r\n" + 
			"		<TOKEN_AUTENTICACAO>TOKEN_AUTENTICACAO</TOKEN_AUTENTICACAO>\r\n" + 
			"	</assianturasimples:VALIDA_PERMISSAO_SAIDA>\r\n" + 
			"	\r\n" + 
			"	<manutencaocheque:CONTRA_ORDEM_CHEQUE_SAIDA>\r\n" + 
			"		<CONTROLE_NEGOCIAL>\r\n" + 
			"			<ORIGEM_RETORNO>SIPER</ORIGEM_RETORNO>\r\n" + 
			"			<COD_RETORNO>00</COD_RETORNO>\r\n" + 
			"			<MSG_RETORNO>sucesso</MSG_RETORNO>\r\n" + 
			"			<NSU>123</NSU>\r\n" + 
			"			<MENSAGENS>\r\n" + 
			"				<RETORNO>RETORNO</RETORNO>\r\n" + 
			"				<INSTITUCIONAL>INSTITUCIONAL</INSTITUCIONAL>\r\n" + 
			"				<INFORMATIVA>INFORMATIVA</INFORMATIVA>\r\n" + 
			"				<TELA>TELA</TELA>\r\n" + 
			"				<MENSAGEM>MENSAGEM</MENSAGEM>\r\n" + 
			"			</MENSAGENS>\r\n" + 
			"		</CONTROLE_NEGOCIAL>\r\n" + 
			"		<CONTRA_ORDEM_CHEQUE>\r\n" + 
			"			<ACATADO>SIM</ACATADO>\r\n" + 
			"		</CONTRA_ORDEM_CHEQUE>\r\n" + 
			"	</manutencaocheque:CONTRA_ORDEM_CHEQUE_SAIDA>\r\n" + 
			"	\r\n" + 
			"</mtx:SERVICO_SAIDA>";
}
