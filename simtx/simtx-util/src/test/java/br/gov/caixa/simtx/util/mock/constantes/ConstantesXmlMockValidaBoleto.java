package br.gov.caixa.simtx.util.mock.constantes;

import br.gov.caixa.simtx.util.data.DataUtil;

public class ConstantesXmlMockValidaBoleto {
	
	private ConstantesXmlMockValidaBoleto() {}
	
	public static String validaBoleto = "<ns2:VALIDA_BOLETO_ENTRADA xmlns:ns2=\"http://caixa.gov.br/simtx/validaboleto/v1/ns\">\r\n" + 
			"	<HEADER>\r\n" + 
			"		<SERVICO>\r\n" + 
			"			<CODIGO>110031</CODIGO>\r\n" + 
			"			<VERSAO>1</VERSAO>\r\n" + 
			"		</SERVICO>\r\n" + 
			"		<CANAL>\r\n" + 
			"			<SIGLA>SIMAA</SIGLA>\r\n" + 
			"			<NSU>16810030087</NSU>\r\n" + 
			"			<DATAHORA>20180803105038</DATAHORA>\r\n" + 
			"		</CANAL>\r\n" + 
			"		<MEIOENTRADA>0</MEIOENTRADA>\r\n" + 
			"		<IDENTIFICADOR_ORIGEM>\r\n" + 
			"			<TERMINAL>01681003</TERMINAL>\r\n" + 
			"		</IDENTIFICADOR_ORIGEM>\r\n" + 
			"		<USUARIO_SERVICO>smtxsd01</USUARIO_SERVICO>\r\n" + 
			"		<DATA_REFERENCIA>20180803</DATA_REFERENCIA>\r\n" + 
			"	</HEADER>\r\n" + 
			"	<NSUMTX_ORIGEM>225348</NSUMTX_ORIGEM>\r\n" + 
			"	<CONTA>\r\n" + 
			"		<CONTA_NSGD>\r\n" + 
			"			<UNIDADE>664</UNIDADE>\r\n" + 
			"			<PRODUTO>3701</PRODUTO>\r\n" + 
			"			<CONTA>400005142</CONTA>\r\n" + 
			"			<DV>2</DV>\r\n" + 
			"		</CONTA_NSGD>\r\n" + 
			"	</CONTA>\r\n" + 
			"	<VALIDA_BOLETO>\r\n" + 
			"		<DATA_VENCIMENTO>2018-05-29-03:00</DATA_VENCIMENTO>\r\n" + 
			"		<DATA_PAGAMENTO>2018-08-03-03:00</DATA_PAGAMENTO>\r\n" + 
			"		<VALOR_PAGAR>1000.00</VALOR_PAGAR>\r\n" + 
			"	</VALIDA_BOLETO>\r\n" + 
			"</ns2:VALIDA_BOLETO_ENTRADA>";
	
	
	public static String validaBoletoAgendamento = "<ns2:VALIDA_BOLETO_ENTRADA xmlns:ns2=\"http://caixa.gov.br/simtx/validaboleto/v1/ns\">\r\n" + 
			"	<HEADER>\r\n" + 
			"		<SERVICO>\r\n" + 
			"			<CODIGO>110031</CODIGO>\r\n" + 
			"			<VERSAO>1</VERSAO>\r\n" + 
			"		</SERVICO>\r\n" + 
			"		<CANAL>\r\n" + 
			"			<SIGLA>SIMAA</SIGLA>\r\n" + 
			"			<NSU>16810030087</NSU>\r\n" + 
			"			<DATAHORA>20180803105038</DATAHORA>\r\n" + 
			"		</CANAL>\r\n" + 
			"		<MEIOENTRADA>0</MEIOENTRADA>\r\n" + 
			"		<IDENTIFICADOR_ORIGEM>\r\n" + 
			"			<TERMINAL>01681003</TERMINAL>\r\n" + 
			"		</IDENTIFICADOR_ORIGEM>\r\n" + 
			"		<USUARIO_SERVICO>smtxsd01</USUARIO_SERVICO>\r\n" + 
			"		<DATA_REFERENCIA>20180803</DATA_REFERENCIA>\r\n" + 
			"	</HEADER>\r\n" + 
			"	<NSUMTX_ORIGEM>225349</NSUMTX_ORIGEM>\r\n" + 
			"	<CONTA>\r\n" + 
			"		<CONTA_NSGD>\r\n" + 
			"			<UNIDADE>664</UNIDADE>\r\n" + 
			"			<PRODUTO>3701</PRODUTO>\r\n" + 
			"			<CONTA>400005142</CONTA>\r\n" + 
			"			<DV>2</DV>\r\n" + 
			"		</CONTA_NSGD>\r\n" + 
			"	</CONTA>\r\n" + 
			"	<VALIDA_BOLETO>\r\n" + 
			"		<DATA_VENCIMENTO>2018-05-29-03:00</DATA_VENCIMENTO>\r\n" + 
			"		<DATA_PAGAMENTO>" + DataUtil.formatar(DataUtil.obterDataFutura(1),DataUtil.FORMATO_DATA_YYYY_MM_DD_HH_MM) + "</DATA_PAGAMENTO>\r\n" + 
			"		<VALOR_PAGAR>1000.00</VALOR_PAGAR>\r\n" + 
			"	</VALIDA_BOLETO>\r\n" + 
			"</ns2:VALIDA_BOLETO_ENTRADA>";
	
	public static String validaBoletoAgendamentoSemErro = "<ns2:VALIDA_BOLETO_ENTRADA xmlns:ns2=\"http://caixa.gov.br/simtx/validaboleto/v1/ns\">\r\n" + 
			"	<HEADER>\r\n" + 
			"		<SERVICO>\r\n" + 
			"			<CODIGO>110031</CODIGO>\r\n" + 
			"			<VERSAO>1</VERSAO>\r\n" + 
			"		</SERVICO>\r\n" + 
			"		<CANAL>\r\n" + 
			"			<SIGLA>SIMAA</SIGLA>\r\n" + 
			"			<NSU>16810030087</NSU>\r\n" + 
			"			<DATAHORA>20180803105038</DATAHORA>\r\n" + 
			"		</CANAL>\r\n" + 
			"		<MEIOENTRADA>0</MEIOENTRADA>\r\n" + 
			"		<IDENTIFICADOR_ORIGEM>\r\n" + 
			"			<TERMINAL>01681003</TERMINAL>\r\n" + 
			"		</IDENTIFICADOR_ORIGEM>\r\n" + 
			"		<USUARIO_SERVICO>smtxsd01</USUARIO_SERVICO>\r\n" + 
			"		<DATA_REFERENCIA>20180803</DATA_REFERENCIA>\r\n" + 
			"	</HEADER>\r\n" + 
			"	<NSUMTX_ORIGEM>225350</NSUMTX_ORIGEM>\r\n" + 
			"	<CONTA>\r\n" + 
			"		<CONTA_NSGD>\r\n" + 
			"			<UNIDADE>664</UNIDADE>\r\n" + 
			"			<PRODUTO>3701</PRODUTO>\r\n" + 
			"			<CONTA>400005142</CONTA>\r\n" + 
			"			<DV>2</DV>\r\n" + 
			"		</CONTA_NSGD>\r\n" + 
			"	</CONTA>\r\n" + 
			"	<VALIDA_BOLETO>\r\n" + 
			"		<DATA_VENCIMENTO>2018-05-29-03:00</DATA_VENCIMENTO>\r\n" + 
			"		<DATA_PAGAMENTO>mmmmmmm/DATA_PAGAMENTO>\r\n" + 
			"		<VALOR_PAGAR>1000.00</VALOR_PAGAR>\r\n" + 
			"	</VALIDA_BOLETO>\r\n" + 
			"</ns2:VALIDA_BOLETO_ENTRADA>";
}
