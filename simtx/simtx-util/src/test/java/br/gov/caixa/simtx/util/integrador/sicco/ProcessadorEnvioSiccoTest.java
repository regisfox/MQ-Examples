package br.gov.caixa.simtx.util.integrador.sicco;

import java.math.BigDecimal;
import java.util.Date;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb014Transacao;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb016IteracaoCanal;
import br.gov.caixa.simtx.util.exception.ServicoException;
import br.gov.caixa.simtx.util.mock.FornecedorDadosMock;

public class ProcessadorEnvioSiccoTest {
	
	private ProcessadorEnvioSicco processadorEnvioSicco = new ProcessadorEnvioSicco();
	
	private Mtxtb014Transacao transacao;
	
	private Mtxtb016IteracaoCanal iteracaoCanal;
	
	private String xml = "<ns2:CONSULTA_BOLETO_ENTRADA xmlns:ns2=\"http://caixa.gov.br/simtx/consulta_boleto_pagamento/v1/ns\">\r\n" + 
			"	<HEADER>\r\n" + 
			"		<SERVICO>\r\n" + 
			"			<CODIGO>110029</CODIGO>\r\n" + 
			"			<VERSAO>1</VERSAO>\r\n" + 
			"		</SERVICO>\r\n" + 
			"		<CANAL>\r\n" + 
			"			<SIGLA>SIMAA</SIGLA>\r\n" + 
			"			<NSU>120250170046</NSU>\r\n" + 
			"			<DATAHORA>20180328154426</DATAHORA>\r\n" + 
			"		</CANAL>\r\n" + 
			"		<MEIOENTRADA>4</MEIOENTRADA>\r\n" + 
			"		<IDENTIFICADOR_ORIGEM>\r\n" + 
			"			<TERMINAL>12025017</TERMINAL>\r\n" + 
			"		</IDENTIFICADOR_ORIGEM>\r\n" + 
			"		<USUARIO_SERVICO>smtxsd01</USUARIO_SERVICO>\r\n" + 
			"		<DATA_REFERENCIA>20180515</DATA_REFERENCIA>\r\n" + 
			"	</HEADER>\r\n" + 
			"	<CONTA>\r\n" + 
			"		<CONTA_NSGD>\r\n" + 
			"			<UNIDADE>682</UNIDADE>\r\n" + 
			"			<PRODUTO>1288</PRODUTO>\r\n" + 
			"			<CONTA>990011127</CONTA>\r\n" + 
			"			<DV>9</DV>\r\n" + 
			"		</CONTA_NSGD>\r\n" + 
			"	</CONTA>\r\n" + 
			"	<CARTAO>\r\n" + 
			"		<TRILHA>6277809990222884=20076208321906901040</TRILHA>\r\n" + 
			"		<NUMERO_SEQUENCIAL>0</NUMERO_SEQUENCIAL>\r\n" + 
			"	</CARTAO>\r\n" + 
			"	<SENHA>\r\n" + 
			"		<SENHA>M0A3E4F066B6A</SENHA>\r\n" + 
			"		<FLAG_CONSULTA_IP>S</FLAG_CONSULTA_IP>\r\n" + 
			"		<VERSAO>3</VERSAO>\r\n" + 
			"		<ACAO>17</ACAO>\r\n" + 
			"	</SENHA>\r\n" + 
			"	<CONSULTA_BOLETO>\r\n" + 
			"		<DATA_MOVIMENTO>2018-03-28-03:00</DATA_MOVIMENTO>\r\n" + 
			"		<FORMA_LEITURA>2</FORMA_LEITURA>\r\n" + 
			"		<LINHA_DIGITAVEL>10492511903200010004320180200436475150000290000</LINHA_DIGITAVEL>\r\n" + 
			"		<UNIDADE>682</UNIDADE>\r\n" + 
			"	</CONSULTA_BOLETO>\r\n" + 
			"</ns2:CONSULTA_BOLETO_ENTRADA>\r\n";
	
	
	@Before
	public void inicializarDados() {
		FornecedorDadosMock fornecedorDadosMock = new FornecedorDadosMock();
		
		this.processadorEnvioSicco = new ProcessadorEnvioSicco();
		this.processadorEnvioSicco.setFornecedorDados(fornecedorDadosMock);
		
		this.transacao = new Mtxtb014Transacao();
		this.transacao.setNuNsuTransacao(15L);
		this.transacao.setIcSituacao(BigDecimal.ONE);
		this.transacao.setCoCanalOrigem("104");
		this.transacao.setDtReferencia(new Date());
		this.transacao.setDhTransacaoCanal(new Date());
		this.transacao.setDhMultiCanal(new Date());
		
		this.iteracaoCanal = new Mtxtb016IteracaoCanal();
		this.iteracaoCanal.setDeRecebimento(this.xml);
	}

	@Test
	public void criarMensagem() throws ServicoException {
		this.transacao.setDtContabil(null);
		this.iteracaoCanal.setDeRecebimento("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\r\n" + 
				"<mtxCancelamentoAgendamentoWeb>\r\n" + 
				"    <canal>\r\n" + 
				"        <nsuCanal>0</nsuCanal>\r\n" + 
				"        <nuCanal>114</nuCanal>\r\n" + 
				"    </canal>\r\n" + 
				"    <codigoMaquina>127.0.0.1</codigoMaquina>\r\n" + 
				"    <codigoUsuario>c988069</codigoUsuario>\r\n" + 
				"    <dataAgendamento>2018-06-27</dataAgendamento>\r\n" + 
				"    <dataEfetivacao>2018-06-29</dataEfetivacao>\r\n" + 
				"    <dvConta>7</dvConta>\r\n" + 
				"    <icTipoConta>2</icTipoConta>\r\n" + 
				"    <linhaDigitavel>10498062338200010004809924877062277250000000000</linhaDigitavel>\r\n" + 
				"    <nuConta>985967243</nuConta>\r\n" + 
				"    <nuNsuTransacaoAgendamento>216964</nuNsuTransacaoAgendamento>\r\n" + 
				"    <nuProduto>1288</nuProduto>\r\n" + 
				"    <nuUnidade>682</nuUnidade>\r\n" + 
				"    <servico>\r\n" + 
				"        <nuServico>110042</nuServico>\r\n" + 
				"        <nuVersaoServico>0</nuVersaoServico>\r\n" + 
				"    </servico>\r\n" + 
				"    <valorTransacao>2</valorTransacao>\r\n" + 
				"</mtxCancelamentoAgendamentoWeb>\r\n");
		
		StringBuilder resposta = this.processadorEnvioSicco.criarMensagem(new StringBuilder(), this.transacao,
				this.iteracaoCanal);
		Assert.assertNotNull(resposta);
		Assert.assertNotEquals("", resposta);
	}
	
	@Test
	public void gravarTentativaDeEnvio() throws ServicoException {
		this.processadorEnvioSicco.gravarTentativaDeEnvio(this.transacao, 1, 1);
	}
	
	@Test
	public void executarEnvioUnico() {
		this.processadorEnvioSicco.executarEnvioUnico(this.transacao);
	}

}
