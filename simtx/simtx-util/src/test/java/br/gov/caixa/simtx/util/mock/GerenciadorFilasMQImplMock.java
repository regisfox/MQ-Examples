package br.gov.caixa.simtx.util.mock;

import javax.enterprise.inject.Alternative;

import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb001Servico;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb024TarefaFila;
import br.gov.caixa.simtx.util.exception.ServicoException;
import br.gov.caixa.simtx.util.mensagem.GerenciadorFilasMQ;
import br.gov.caixa.simtx.util.mensagem.PropriedadesMQ;

@Alternative
public class GerenciadorFilasMQImplMock implements GerenciadorFilasMQ {
	
	private static final String XML_LISTA_ASSINATURA = "<multicanal:SERVICO_SAIDA xmlns:multicanal=\"http://caixa.gov.br/sibar/multicanal/orquestracao\" xmlns:sibar_base=\"http://caixa.gov.br/sibar\">\r\n" + 
			"	<sibar_base:HEADER>\r\n" + 
			"		<VERSAO>1.0</VERSAO>\r\n" + 
			"		<OPERACAO>LISTA_TRANSACOES_PENDENTES</OPERACAO>\r\n" + 
			"		<SISTEMA_ORIGEM>SIIBC</SISTEMA_ORIGEM>\r\n" + 
			"		<DATA_HORA>20180820123200</DATA_HORA>\r\n" + 
			"	</sibar_base:HEADER>\r\n" + 
			"	<COD_RETORNO>00</COD_RETORNO>	\r\n" + 
			"	<ORIGEM_RETORNO>SIBAR</ORIGEM_RETORNO>\r\n" + 
			"	<MSG_RETORNO>SUCESSO</MSG_RETORNO>\r\n" + 
			"	<consultaassinaturaeletronica:CONSULTA_ASSINATURA_ELETRONICA_SAIDA xmlns:consultaassinaturaeletronica=\"http://caixa.gov.br/sibar/consulta_assinatura_eletronica/lista_transacoes_pendentes\">\r\n" + 
			"		<CONTROLE_NEGOCIAL>\r\n" + 
			"			<ORIGEM_RETORNO>BROKER-SPD9CJI0</ORIGEM_RETORNO>\r\n" + 
			"			<COD_RETORNO>00</COD_RETORNO>\r\n" + 
			"			<MENSAGENS>\r\n" + 
			"				<RETORNO>SUCESSO NA TRANSACAO</RETORNO>\r\n" + 
			"			</MENSAGENS>\r\n" + 
			"		</CONTROLE_NEGOCIAL>\r\n" + 
			"		<TRANSACOES>\r\n" + 
			"			<TRANSACAO>\r\n" + 
			"				<NSU_SIPER>5789789</NSU_SIPER>\r\n" + 
			"				<DATA>2001-01-01</DATA>\r\n" + 
			"				<DATA_PREVISTA_EFETIVACAO>2001-01-01</DATA_PREVISTA_EFETIVACAO>\r\n" + 
			"				<SITUACAO>PENDENTE</SITUACAO>\r\n" + 
			"				<RESUMO>RESUMO</RESUMO>\r\n" + 
			"				<ORIGEM>\r\n" + 
			"					<NSU>45464</NSU>\r\n" + 
			"					<SISTEMA>SIAUT</SISTEMA>\r\n" + 
			"				</ORIGEM>\r\n" + 
			"			</TRANSACAO>\r\n" + 
			"			<TRANSACAO>\r\n" + 
			"				<NSU_SIPER>9209</NSU_SIPER>\r\n" + 
			"				<DATA>2017-08-24</DATA>\r\n" + 
			"				<DATA_PREVISTA_EFETIVACAO>2018-08-24</DATA_PREVISTA_EFETIVACAO>\r\n" + 
			"				<SITUACAO>PENDENTE</SITUACAO>\r\n" + 
			"				<RESUMO>RESUMO</RESUMO>\r\n" + 
			"				<ORIGEM>\r\n" + 
			"					<NSU>9209</NSU>\r\n" + 
			"					<SISTEMA>SIMTX</SISTEMA>\r\n" + 
			"				</ORIGEM>\r\n" + 
			"			</TRANSACAO>\r\n" + 
			"		</TRANSACOES>\r\n" + 
			"	</consultaassinaturaeletronica:CONSULTA_ASSINATURA_ELETRONICA_SAIDA>\r\n" + 
			"</multicanal:SERVICO_SAIDA>";

	@Override
	public String executarServico(String mensagem, PropriedadesMQ propriedadesMQ) throws ServicoException {
		return null;
	}

	@Override
	public String executar(String mensagem, Mtxtb001Servico servico) throws ServicoException {
		if(servico.getNuServico() == 110023L) {
			return XML_LISTA_ASSINATURA;
		}
		return "";
	}

	@Override
	public String executar(String mensagem, Mtxtb024TarefaFila tarefa) throws ServicoException {
		return null;
	}

	@Override
	public String executarSicco(String mensagem) throws ServicoException {
		return "<?xml version=\"1.0\" encoding=\"UTF-8\"?><RESPOSTA><NSUMTX>1</NSUMTX><NSUMTX_ORIGEM/><COD_RETORNO>00</COD_RETORNO><MSG_RETORNO>SUCESSO NO ARMAZENAMENTO DAS INFORMACOES DO SERVICO</MSG_RETORNO></RESPOSTA>\r\n";

	}

}
