//package br.gov.caixa.simtx.util.gerenciador;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import org.junit.Assert;
//import org.junit.Test;
//
//import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb012VersaoTarefa;
//import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb012VersaoTarefaPK;
//import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb015SrvcoTrnsoTrfa;
//import br.gov.caixa.simtx.util.infra.FornecedorDadosMock;
//
//public class GerenciadorTarefasTest {
//
//	@Test
//	public void deveRetornarZeroTarefasComErro() {
//		List<Mtxtb015SrvcoTrnsoTrfa> tts = new ArrayList<>();
//		tts.add(criarTarefaTransacao("00"));
//		tts.add(criarTarefaTransacao("00"));
//		tts.add(criarTarefaTransacao("00"));
//
//		GerenciadorTarefas gt = new GerenciadorTarefas();
//		gt.setFornecedorDados(new FornecedorDadosMock());
//		Assert.assertEquals("Deve retornar zero tarefas com zero", 0, gt.quantidadeTarefasComErro(tts));
//	}
//	
//	private Mtxtb015SrvcoTrnsoTrfa criarTarefaTransacao(String codigoRetorno) {
//		String xmlRetorno = "<controle_limite:CONTROLE_LIMITE_SAIDA xmlns:controle_limite=\"http://caixa.gov.br/sibar/controle_limite\" xmlns:multicanal=\"http://caixa.gov.br/sibar/multicanal/orquestracao\" xmlns:manutencao_cobranca_bancaria=\"http://caixa.gov.br/sibar/manutencao_cobranca_bancaria\" xmlns:sibar=\"http://caixa.gov.br/sibar\" xmlns:cartao=\"http://caixa.gov.br/sibar/valida_cartao\" xmlns:valida_senha=\"http://caixa.gov.br/sibar/valida_senha\" xmlns:solicitacao_debito=\"http://caixa.gov.br/sibar/solicitacao_debito\" xmlns:assinaturaimples=\"http://caixa.gov.br/sibar/valida_permissao/assinatura_simples\">\r\n" + 
//				"		<ORIGEM_RETORNO>SIPER</ORIGEM_RETORNO>\r\n" + 
//				"		<CONTROLE_NEGOCIAL>\r\n" + 
//				"			<COD_RETORNO>%codigoRetorno%</COD_RETORNO>\r\n" + 
//				"			<MSG_RETORNO>sucesso</MSG_RETORNO>\r\n" + 
//				"			<NSU>123</NSU>\r\n" + 
//				"			<MENSAGENS>\r\n" + 
//				"				<RETORNO>RETORNO</RETORNO>\r\n" + 
//				"				<INSTITUCIONAL>INSTITUCIONAL</INSTITUCIONAL>\r\n" + 
//				"				<INFORMATIVA>INFORMATIVA</INFORMATIVA>\r\n" + 
//				"				<TELA>TELA</TELA>\r\n" + 
//				"				<MENSAGEM>MENSAGEM</MENSAGEM>\r\n" + 
//				"			</MENSAGENS>\r\n" + 
//				"		</CONTROLE_NEGOCIAL>\r\n" + 
//				"		<DATA_TRANSACAO>2018-01-15</DATA_TRANSACAO>\r\n" + 
//				"		<TOKEN_AUTENTICACAO>2A5S45SAASDFF545115S34511534587S#15</TOKEN_AUTENTICACAO>\r\n" + 
//				"	</controle_limite:CONTROLE_LIMITE_SAIDA>";
//		xmlRetorno = xmlRetorno.replaceAll("\\%codigoRetorno\\%", codigoRetorno);
//		Mtxtb015SrvcoTrnsoTrfa transacaoTarefa = new Mtxtb015SrvcoTrnsoTrfa();
//		transacaoTarefa.setDeXmlResposta(xmlRetorno);
//		Mtxtb012VersaoTarefa vt = new Mtxtb012VersaoTarefa();
//		transacaoTarefa.setMtxtb012VersaoTarefa(vt);
//		
//		Mtxtb012VersaoTarefaPK pk = new Mtxtb012VersaoTarefaPK();
//		vt.setId(pk);
//		
//		pk.setNuTarefa002(1L);
//		pk.setNuVersaoTarefa(1);
//		
//		return transacaoTarefa;
//	}
//}
