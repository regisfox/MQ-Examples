package br.gov.caixa.simtx.util.mock.constantes;

import br.gov.caixa.simtx.util.data.DataUtil;

public class ConstantesXmlMockAgendamento {

	public static final String AGENDAMENTO_SUCESSO_ENTRADA_1 = "<ns:PAGAMENTO_BOLETO_ENTRADA>\r\n" + 
			"	<HEADER>\r\n" + 
			"		<SERVICO>\r\n" + 
			"			<CODIGO>110039</CODIGO>\r\n" + 
			"			<VERSAO>1</VERSAO>\r\n" + 
			"		</SERVICO>\r\n" + 
			"		<CANAL>\r\n" + 
			"			<SIGLA>SIMAA</SIGLA>\r\n" + 
			"			<DATAHORA>20180101210900</DATAHORA>\r\n" + 
			"			<NSU>12345</NSU>\r\n" + 
			"		</CANAL>\r\n" + 
			"		<MEIOENTRADA>0</MEIOENTRADA>\r\n" + 
			"		<DATA_REFERENCIA>"+ DataUtil.formatar(DataUtil.obterDataFutura(1),DataUtil.FORMATO_DATA_YYYY_MM_DD) +"</DATA_REFERENCIA>\r\n" + 
			"	</HEADER>\r\n" + 
			"	<NSUMTX_ORIGEM>17036</NSUMTX_ORIGEM>\r\n" + 
			"	<AGENDAMENTO>SIM</AGENDAMENTO>\r\n" + 
			"	<CONTA>\r\n" + 
			"		<CONTA_SIDEC>\r\n" + 
			"			<UNIDADE>0</UNIDADE>\r\n" + 
			"			<OPERACAO>0</OPERACAO>\r\n" + 
			"			<CONTA>0</CONTA>\r\n" + 
			"			<DV>0</DV>\r\n" + 
			"		</CONTA_SIDEC>\r\n" + 
			"	</CONTA>\r\n" + 
			"	<PAGAMENTO_BOLETO>\r\n" + 
			"		<INFORMACOES_BOLETO>\r\n" + 
			"			<DATA_VENCIMENTO>2001-01-01</DATA_VENCIMENTO>\r\n" + 
			"			<LINHA_DIGITAVEL>23790504004188062212540008109205175660000019900</LINHA_DIGITAVEL>\r\n" + 
			"		</INFORMACOES_BOLETO>\r\n" + 
			"	</PAGAMENTO_BOLETO>\r\n" + 
			"</ns:PAGAMENTO_BOLETO_ENTRADA>\r\n" + 
			"";
	private ConstantesXmlMockAgendamento() {

	}
}
