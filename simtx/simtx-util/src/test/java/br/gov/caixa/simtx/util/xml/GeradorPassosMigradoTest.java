package br.gov.caixa.simtx.util.xml;

import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.Test;
import org.w3c.dom.Document;

import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb001Servico;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb003ServicoTarefa;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb004Canal;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb011VersaoServico;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb011VersaoServicoPK;
import br.gov.caixa.simtx.util.mock.FornecedorDadosMock;

public class GeradorPassosMigradoTest {
	
	private String XML_PAGAMENTO_BOLETO = 
				      "<ns:CONTRA_ORDEM_CHEQUE_ENTRADA \r\n" + 
				      " xmlns:comuns=\"http://caixa.gov.br/simtx/comuns/comuns\" \r\n" + 
				      " xmlns:enuns_type=\"http://caixa.gov.br/simtx/comuns/enuns_type\" \r\n" + 
				      " xmlns:h=\"http://caixa.gov.br/simtx/comuns/header\" \r\n" + 
				      " xmlns:ns=\"http://caixa.gov.br/simtx/manutencao_cheque/v1/ns\" \r\n" + 
				      " xmlns:valida_assinatura=\"http://caixa.gov.br/simtx/meioentrada/valida_assinatura\" \r\n" + 
				      " xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\r\n" + 
				      " xsi:schemaLocation=\"http://caixa.gov.br/simtx/manutencaocheque Contra_Ordem_Cheques.xsd \">\r\n" + 
				      "  <HEADER>\r\n" + 
				      "    <SERVICO>\r\n" + 
				      "      <CODIGO>110030</CODIGO>\r\n" + 
				      "      <VERSAO>1</VERSAO>\r\n" + 
				      "    </SERVICO>\r\n" + 
				      "    <CANAL>\r\n" + 
				      "      <SIGLA>SIIBC</SIGLA>\r\n" + 
				      "      <DATAHORA>20180115000000</DATAHORA>\r\n" + 
				      "    </CANAL>\r\n" + 
				      "    <MEIOENTRADA>2</MEIOENTRADA>\r\n" + 
				      "    <IDENTIFICADOR_ORIGEM>\r\n" + 
				      "      <CODIGO_MAQUINA>127001</CODIGO_MAQUINA>\r\n" + 
				      "    </IDENTIFICADOR_ORIGEM>\r\n" + 
				      "    <USUARIO_SERVICO>SDMTX01</USUARIO_SERVICO>\r\n" + 
				      "    <USUARIO>JOANADAR</USUARIO>\r\n" + 
				      "    <DATA_REFERENCIA>20180115</DATA_REFERENCIA>\r\n" + 
				      "  </HEADER>\r\n" + 
				      "  <CPF>36199093852</CPF>\r\n" + 
				      "  <CONTA>\r\n" + 
				      "    <CONTA_SIDEC>\r\n" + 
				      "      <UNIDADE>1234</UNIDADE>\r\n" + 
				      "      <OPERACAO>123</OPERACAO>\r\n" + 
				      "      <CONTA>12345678</CONTA>\r\n" + 
				      "      <DV>1</DV>\r\n" + 
				      "    </CONTA_SIDEC>\r\n" + 
				      "  </CONTA>\r\n" + 
				      "  <TOKEN>\r\n" + 
				      "    <TOKEN>TOKEN</TOKEN>\r\n" + 
				      "    <ID_SESSAO>ID_SESSAO</ID_SESSAO>\r\n" + 
				      "  </TOKEN>\r\n" + 
				      "  \r\n" + 
				      "  <ASSINATURA_SIMPLES>\r\n" + 
				      "      <ASSINATURA>A</ASSINATURA>\r\n" + 
				      "      <SERVICO_SIPER>666</SERVICO_SIPER>\r\n" + 
				      "      <APELIDO>APELIDO</APELIDO>\r\n" + 
				      "      <DISPOSITIVO>\r\n" + 
				      "          <SISTEMA_OPERACIONAL>ANDROID_TABLET</SISTEMA_OPERACIONAL>\r\n" + 
				      "          <CODIGO>1</CODIGO>\r\n" + 
				      "      </DISPOSITIVO>\r\n" + 
				      "  </ASSINATURA_SIMPLES>\r\n" + 
				      "  \r\n" + 
				      "  <CONTRA_ORDEM_CHEQUE>\r\n" + 
				      "    <NUMERO_CHEQUE>\r\n" + 
				      "      <INICIO>1</INICIO>\r\n" + 
				      "      <FIM>2</FIM>\r\n" + 
				      "    </NUMERO_CHEQUE>\r\n" + 
				      "    <CODIGO_MOTIVO>01</CODIGO_MOTIVO>\r\n" + 
				      "    <TIPO_COMANDO>INCLUSAO</TIPO_COMANDO>\r\n" + 
				      "    <JUSTIFICATIVA>apenas um teste</JUSTIFICATIVA>\r\n" + 
				      "  </CONTRA_ORDEM_CHEQUE>\r\n" + 
				      "</ns:CONTRA_ORDEM_CHEQUE_ENTRADA>\r\n";
	

	@Test
	public void testGerarPassos() throws Exception {
		
		FornecedorDadosMock fornecedorDadosMock = new FornecedorDadosMock();
		GeradorPassosMigrado geradorPassos = new GeradorPassosMigrado();
		geradorPassos.setFornecedorDados(fornecedorDadosMock);
		
		Mtxtb004Canal canal = new Mtxtb004Canal();
		canal.setNuCanal(104L);
		
		
		Mtxtb001Servico servico = new Mtxtb001Servico();
		servico.setNuServico(110032L);
		
		
		Mtxtb011VersaoServico versaoServico = new Mtxtb011VersaoServico();
		Mtxtb011VersaoServicoPK versaoServicoPK = new Mtxtb011VersaoServicoPK();
		versaoServicoPK.setNuServico001(110032L);
		versaoServicoPK.setNuVersaoServico(1);
		versaoServico.setId(versaoServicoPK);
		versaoServico.setMtxtb001Servico(servico);
		
		
		List<Mtxtb003ServicoTarefa> listaTarefaExecutar = fornecedorDadosMock
				.buscarTarefasExecutar(servico.getNuServico(), versaoServico.getId().getNuVersaoServico(), canal.getNuCanal());
		
		Document doc = geradorPassos.criaTagOrquestracao(versaoServico, listaTarefaExecutar);
		
		assertNotNull(doc);
	}

}
