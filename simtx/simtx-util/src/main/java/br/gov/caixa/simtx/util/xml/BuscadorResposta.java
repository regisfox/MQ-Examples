package br.gov.caixa.simtx.util.xml;

import org.w3c.dom.Node;

import br.gov.caixa.simtx.util.StringUtil;

public class BuscadorResposta {

	public Resposta buscarRespostaBarramento(BuscadorTextoXml buscador, String nomeServico) {
		nomeServico = nomeServico.toUpperCase();
		String xpath = "/SERVICO_SAIDA/" + nomeServico + "_SAIDA/CONTROLE_NEGOCIAL";
		
		Node nodeControle = buscador.xpath(xpath);
		if(nodeControle == null) return null;
		
		
		String origem = buscador.xpathTexto(xpath + "/ORIGEM_RETORNO/text()");
		String codigo = buscador.xpathTexto(xpath + "/COD_RETORNO/text()");
		String mensagemRetorno = buscador.xpathTexto(xpath + "/MSG_RETORNO/text()");
		String retorno = buscador.xpathTexto(xpath + "/MENSAGENS/RETORNO/text()");
		String institucional = buscador.xpathTexto(xpath + "/MENSAGENS/INSTITUCIONAL/text()");
		String informativa = buscador.xpathTexto(xpath + "/MENSAGENS/INFORMATIVA/text()");
		String tela = buscador.xpathTexto(xpath + "/MENSAGENS/TELA/text()");
		String mensagem = buscador.xpathTexto(xpath + "/MENSAGENS/MENSAGEM/text()");

		Resposta resposta = new Resposta();
		resposta.setOrigem(origem);
		resposta.setCodigo(codigo);
		resposta.setMensagemNegocial(mensagemRetorno);
		resposta.setMensagemRetorno(retorno);
		resposta.setMensagemInstitucional(institucional);
		resposta.setMensagemInformativa(informativa);
		resposta.setMensagemTela(tela);
		resposta.setMensagemTecnica(mensagem);

		return resposta;
	}
	
	public Resposta buscarRespostaTarefaBarramento(BuscadorTextoXml buscador) {
		String xpath = "/*[1]/CONTROLE_NEGOCIAL";
		
		Node nodeControle = buscador.xpath(xpath);
		if(nodeControle == null) return null;
		
		
		String origem = buscador.xpathTexto(xpath + "/ORIGEM_RETORNO/text()");
		String codigo = buscador.xpathTexto(xpath + "/COD_RETORNO/text()");
		String mensagemRetorno = buscador.xpathTexto(xpath + "/MSG_RETORNO/text()");
		String retorno = buscador.xpathTexto(xpath + "/MENSAGENS/RETORNO/text()");
		String institucional = buscador.xpathTexto(xpath + "/MENSAGENS/INSTITUCIONAL/text()");
		String informativa = buscador.xpathTexto(xpath + "/MENSAGENS/INFORMATIVA/text()");
		String tela = buscador.xpathTexto(xpath + "/MENSAGENS/TELA/text()");
		
		
		Resposta resposta = new Resposta();
		resposta.setOrigem(origem);
		resposta.setCodigo(codigo);
		resposta.setMensagemNegocial(retorno);
		resposta.setMensagemTecnica(retorno);
		if(retorno.trim().isEmpty()) {
			resposta.setMensagemNegocial(mensagemRetorno);
			resposta.setMensagemTecnica(mensagemRetorno);
		}
		resposta.setMensagemRetorno(retorno);
		resposta.setMensagemInstitucional(institucional);
		resposta.setMensagemInformativa(informativa);
		resposta.setMensagemTela(tela);

		return resposta;
	}
	
	public Resposta buscarRespostaBarramento(BuscadorTextoXml buscador) {
		String origem = buscador.xpathTexto("/SERVICO_SAIDA/ORIGEM_RETORNO/text()");
		String codigo = buscador.xpathTexto("/SERVICO_SAIDA/COD_RETORNO/text()");
		String mensagemRetorno = buscador.xpathTexto("/SERVICO_SAIDA/MSG_RETORNO/text()");
		
		if(StringUtil.isEmpty(origem)) return null;
		
		Resposta resposta = new Resposta();
		resposta.setOrigem(origem);
		resposta.setCodigo(codigo);
		resposta.setMensagemNegocial(mensagemRetorno);
		resposta.setMensagemRetorno(mensagemRetorno);
		resposta.setMensagemInstitucional("");
		resposta.setMensagemInformativa("");
		resposta.setMensagemTela("");
		resposta.setMensagemTecnica("");

		return resposta;
	}
}
