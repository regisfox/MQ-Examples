//package br.gov.caixa.simtx.integrador.chamador;
//
//import static org.junit.Assert.assertTrue;
//
//import java.rmi.RemoteException;
//import java.text.SimpleDateFormat;
//import java.util.Calendar;
//
//import br.gov.caixa.simtx.agendamento.processador.ProcessadorOperacoesAgendamento;
//import br.gov.caixa.simtx.core.canal.ProcessadorMensagemMQCanal;
//import br.gov.caixa.simtx.integrador.IntegradorChamador;
//
//public class TestChamador {
//
////	@Test
//	public void deveChamarProcessadorAgendamentoQuandoValidaBoletoEhAgendamento() throws RemoteException {
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//		Calendar c = Calendar.getInstance();
//		c.add(Calendar.DAY_OF_MONTH, 1);
//		
//		String mensagem = XML_VALIDA_AGENDAMENTO.replaceAll("\\{\\{codigo_servico\\}\\}", "110031");
//		mensagem = mensagem.replaceAll("\\{\\{data_pagamento\\}\\}", sdf.format(c.getTime()));
//
//		ProcessadorOperacoesAgendamentoMock processador = new ProcessadorOperacoesAgendamentoMock();
//		IntegradorChamador integrador = new IntegradorChamador(mensagem, processador);
////     	integrador.setProcessadorOperacoesAgendamento(processador);
////		integrador.setMensagemCanal(mensagem);
//		integrador.chamar();
//		
//		assertTrue(processador.isChamado());
//	}
//	
////	@Test
////	public void deveChamarProcessadorCoreQuandoValidaBoletoEhDataDeAtual() throws Exception {
////		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
////		
////		String mensagem = XML_VALIDA_AGENDAMENTO.replaceAll("\\{\\{codigo_servico\\}\\}", "110031");
////		mensagem = mensagem.replaceAll("\\{\\{data_pagamento\\}\\}", sdf.format(new Date()));
////		
////		ProcessadorMensagemMQCanalMock processador = new ProcessadorMensagemMQCanalMock();
////		IntegradorChamador integrador = new IntegradorChamador();
////		integrador.setFornecedorDados(new FornecedorDadosMock());
////     	integrador.setProcessadorMsgCanal(processador);
////		integrador.setMensagemCanal(mensagem);
////		integrador.setValidadorRegrasNegocio(new ValidadorRegrasNegocio() {
////			public void validarRegrasMigrado(String xml, Mtxtb004Canal canal, Mtxtb008MeioEntrada meioEntrada,
////					Mtxtb011VersaoServico versaoServico) throws ServicoException {}
////		});
////		integrador.chamar();
////		
////		assertTrue(processador.isChamado());
////	}
//	
//	
//	String XML_VALIDA_AGENDAMENTO = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\r\n" + 
//			"<ns2:VALIDA_BOLETO_ENTRADA xmlns:ns2=\"http://caixa.gov.br/simtx/validaboleto/v1/ns\">\r\n" + 
//			"	<HEADER>\r\n" + 
//			"		<SERVICO>\r\n" + 
//			"			<CODIGO>{{codigo_servico}}</CODIGO>\r\n" + 
//			"			<VERSAO>1</VERSAO>\r\n" + 
//			"		</SERVICO>\r\n" + 
//			"		<CANAL>\r\n" + 
//			"			<SIGLA>SIMAA</SIGLA>\r\n" + 
//			"			<NSU>218515060025</NSU>\r\n" + 
//			"			<DATAHORA>20180619095913</DATAHORA>\r\n" + 
//			"		</CANAL>\r\n" + 
//			"		<MEIOENTRADA>0</MEIOENTRADA>\r\n" + 
//			"		<IDENTIFICADOR_ORIGEM>\r\n" + 
//			"			<TERMINAL>21851506</TERMINAL>\r\n" + 
//			"		</IDENTIFICADOR_ORIGEM>\r\n" + 
//			"		<USUARIO_SERVICO>smtxsd01</USUARIO_SERVICO>\r\n" + 
//			"		<DATA_REFERENCIA>20180619</DATA_REFERENCIA>\r\n" + 
//			"	</HEADER>\r\n" + 
//			"	<NSUMTX_ORIGEM>184346</NSUMTX_ORIGEM>\r\n" + 
//			"	<CONTA>\r\n" + 
//			"		<CONTA_NSGD>\r\n" + 
//			"			<UNIDADE>682</UNIDADE>\r\n" + 
//			"			<PRODUTO>1288</PRODUTO>\r\n" + 
//			"			<CONTA>985967243</CONTA>\r\n" + 
//			"			<DV>7</DV>\r\n" + 
//			"		</CONTA_NSGD>\r\n" + 
//			"	</CONTA>\r\n" + 
//			"	<VALIDA_BOLETO>\r\n" + 
//			"		<DATA_VENCIMENTO>2018-12-01-02:00</DATA_VENCIMENTO>\r\n" + 
//			"		<DATA_PAGAMENTO>{{data_pagamento}}</DATA_PAGAMENTO>\r\n" + 
//			"		<VALOR_PAGAR>500.00</VALOR_PAGAR>\r\n" + 
//			"	</VALIDA_BOLETO>\r\n" + 
//			"</ns2:VALIDA_BOLETO_ENTRADA>";
//}
//
//class ProcessadorOperacoesAgendamentoMock extends ProcessadorOperacoesAgendamento {
//	/**
//	 * 
//	 */
//	private static final long serialVersionUID = 8566545974415323481L;
//	boolean chamado = false;
//	
//	public void processarMensagem(String idMessage, String xml, String jndiQueueConnectionFactory,
//			String jndiResponseQueue, boolean converterRespostaParaJson) {
//		chamado = true;
//	}
//
//	public boolean isChamado() {
//		return chamado;
//	}
//}
//
//class ProcessadorMensagemMQCanalMock extends ProcessadorMensagemMQCanal {
//
//	private static final long serialVersionUID = 1L;
//	boolean chamado = false;
//	
//	public void processarMensagemMQ() {
//		chamado = true;
//	}
//
//	public boolean isChamado() {
//		return chamado;
//	}
//	
//	public ProcessadorMensagemMQCanalMock() throws Exception {
//		super();
//	}
//}