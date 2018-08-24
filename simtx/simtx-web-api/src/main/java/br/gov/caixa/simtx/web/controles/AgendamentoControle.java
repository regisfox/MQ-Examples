package br.gov.caixa.simtx.web.controles;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.Logger;

import br.gov.caixa.simtx.persistencia.agendamento.entidade.Mtxtb034TransacaoAgendamento;
import br.gov.caixa.simtx.persistencia.cache.agendamento.FornecedorDadosAgendamento;
import br.gov.caixa.simtx.persistencia.cache.core.FornecedorDados;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb001Servico;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb004Canal;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb016IteracaoCanal;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb023Parametro;
import br.gov.caixa.simtx.util.data.DataUtil;
import br.gov.caixa.simtx.util.xml.BuscadorTextoXml;
import br.gov.caixa.simtx.web.beans.MtxCanal;
import br.gov.caixa.simtx.web.beans.MtxCancelamentoAgendamentoSaida;
import br.gov.caixa.simtx.web.beans.MtxMensagem;
import br.gov.caixa.simtx.web.beans.MtxServico;
import br.gov.caixa.simtx.web.beans.MtxTransacaoAgendamento;
import br.gov.caixa.simtx.web.beans.agendamentos.naoefetivados.MtxAgendamentoNaoEfetivadoSaida;
import br.gov.caixa.simtx.web.configuracao.CancelamentoTransacional;
import br.gov.caixa.simtx.web.configuracao.JWTUtil;
import br.gov.caixa.simtx.web.util.ConstantesWeb;
import br.gov.caixa.simtx.web.util.MatrizAcesso;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;

@Path("/sessao/agendamento")
public class AgendamentoControle {

	private static final Logger logger = Logger.getLogger(AgendamentoControle.class);

	private static final int URL_CANCELAMENTO_AGENDAMENTO_ROTEADOR = 14;
	
	@Inject
	protected FornecedorDadosAgendamento fornecedorDadosAgendamento;
	
	@Inject
	protected FornecedorDados fornecedorDados;
	
	@Inject
	protected MatrizAcesso matriz;

	@POST
	@Path("/consulta")
	@Produces(MediaType.APPLICATION_JSON)
	public Response consultaAgendamento(@Context HttpServletRequest httpRequest, MtxTransacaoAgendamento consulta) {
		
		List<MtxCancelamentoAgendamentoSaida> listaTransacao = new ArrayList<>();		
		String token = httpRequest.getHeader(JWTUtil.TOKEN_HEADER);
		Jws<Claims> jws = JWTUtil.decodificaToken(token);
			if (matriz.verificaPermissao(jws.getBody().getId(), ConstantesWeb.AGENDAMENTO)) {
				
				logger.info("Consulta de agendamento requisitada. Usuario: " + jws.getBody().getSubject());
				List<Mtxtb034TransacaoAgendamento> consultaAgendamentos = fornecedorDadosAgendamento.buscaTransacoesAgendamento(preencherMtxtb034TransacaoAgendamento(consulta));
				if (!consultaAgendamentos.isEmpty()) {
					for (Mtxtb034TransacaoAgendamento transacao: consultaAgendamentos){
						listaTransacao.add(preencherMtxCancelamentoAgendamentoSaida(transacao));
					}	
				}else {
					logger.info("Pesquisa Sem resultados");	
				}
				
			} else {
				logger.warn("Ocorreu uma tentativa de acesso a consulta de agendamento nao autorizada. Usuario: " + jws.getBody().getSubject());
				return Response.status(Response.Status.UNAUTHORIZED).build();
			}
		return  Response.ok().entity(listaTransacao).build();
	}
	
	@POST
	@Path("/naoefetivados")
	@Produces(MediaType.APPLICATION_JSON)
	public Response consultaAgendamentoNaoEfetivados(@Context HttpServletRequest httpRequest,  MtxTransacaoAgendamento consulta)
	{
		final List<MtxAgendamentoNaoEfetivadoSaida> listaTransacao = new ArrayList<>();
		
		final String token = httpRequest.getHeader(JWTUtil.TOKEN_HEADER);
		final Jws<Claims> jws = JWTUtil.decodificaToken(token);

		if (matriz.verificaPermissao(jws.getBody().getId(), ConstantesWeb.AGENDAMENTO)) {
			
			logger.info("Consulta de agendamentos nao efetivados requisitada. Usuario: " + jws.getBody().getSubject());
			
			final List<Mtxtb034TransacaoAgendamento> agendamentosList = fornecedorDadosAgendamento.buscaTransacoesAgendamentoNaoEfetivados(preencherMtxtb034TransacaoAgendamento(consulta));
			if (null != agendamentosList && !agendamentosList.isEmpty()) {
				for (final Mtxtb034TransacaoAgendamento transacao : agendamentosList) {
					listaTransacao.add(preencherMtxAgendamentoNaoEfetivadoSaida(transacao));
				}	
			}else {
				logger.info("Pesquisa Sem resultados");	
			}
		} else {
			logger.warn("Ocorreu uma tentativa de acesso a consulta de agendamentos nao efetivados nao autorizada. Usuario: " + jws.getBody().getSubject());
			return Response.status(Response.Status.UNAUTHORIZED).build();
		}
		return  Response.ok().entity(listaTransacao).build();
	}
	
	@POST
	@Path("/cancelar")
	@Produces(MediaType.APPLICATION_JSON)
	public Response efetuaCancelamentoAgendamento(@Context HttpServletRequest httpRequest, 
			List<MtxCancelamentoAgendamentoSaida> listaCancelamento) {
		
		String token = httpRequest.getHeader(JWTUtil.TOKEN_HEADER);
        Jws<Claims> jws = JWTUtil.decodificaToken(token);
        String usuarioLogado = jws.getBody().getSubject();
		
        if (matriz.verificaPermissao(jws.getBody().getId(), ConstantesWeb.CANCELA_AGENDAMENTO)) {
        	
        	logger.info("Cancelamento de agendamento solicitado. Usuario: " + usuarioLogado);
        	
			for (MtxCancelamentoAgendamentoSaida canc: listaCancelamento){
				canc.setCodigoMaquina(httpRequest.getRemoteAddr());
				canc.setCodigoUsuario(usuarioLogado);
			}
			
			CancelamentoTransacional cancelamento = new CancelamentoTransacional();
			Mtxtb023Parametro parametro = fornecedorDados.buscarParametroPorPK(URL_CANCELAMENTO_AGENDAMENTO_ROTEADOR);
			String retornoJson = cancelamento.envioTransacional(parametro.getDeConteudoParam(), listaCancelamento);

			return Response.ok().entity(retornoJson).build();
        }else{
			logger.warn("Ocorreu uma tentativa cancelamento de agendamento nao autorizada. Usuario: " + usuarioLogado);
			return Response.status(Response.Status.UNAUTHORIZED).build();
        }
	}

	@GET
	@Path("/")
	@Produces(MediaType.APPLICATION_JSON)
	public Response testeConsulta(){
		return Response.ok().build();
	}
	
	
	private MtxAgendamentoNaoEfetivadoSaida preencherMtxAgendamentoNaoEfetivadoSaida(
			Mtxtb034TransacaoAgendamento transacao) {

		MtxAgendamentoNaoEfetivadoSaida dados = new MtxAgendamentoNaoEfetivadoSaida();
		try {
		BeanUtils.copyProperties(dados, transacao);
		dados.setValorTransacao(transacao.getValorTransacao() == null ? "" : transacao.getValorTransacao().toString());
		dados.setDataAgendamento(DataUtil.formatar(transacao.getDtReferencia(), DataUtil.FORMATO_DATA_PADRAO_BR));
		dados.setDataEfetivacao(DataUtil.formatar(transacao.getDtEfetivacao(), DataUtil.FORMATO_DATA_PADRAO_BR));
		dados.setCanal(new MtxCanal());
		dados.setServico(new MtxServico());
		
		if (null != transacao.getMtxtb004Canal() && null != transacao.getMtxtb001Servico()) {
			BeanUtils.copyProperties(dados.getCanal(), transacao.getMtxtb004Canal());
			BeanUtils.copyProperties(dados.getServico(), transacao.getMtxtb001Servico());
			dados.getServico().setNuVersaoServico(transacao.getNuVersaoServico());
		}
		
		if (null != transacao.getDeXmlAgendamento()) {
			BuscadorTextoXml buscadorTextoXml = new BuscadorTextoXml(transacao.getDeXmlAgendamento());
			dados.setLinhaDigitavel(
					buscadorTextoXml.xpathTexto("*[local-name()='PAGAMENTO_BOLETO_ENTRADA']/PAGAMENTO_BOLETO/INFORMACOES_BOLETO/LINHA_DIGITAVEL"));
			dados.getCanal().setNsuCanal(Long.valueOf(buscadorTextoXml.xpathTexto("*[local-name()='PAGAMENTO_BOLETO_ENTRADA']/HEADER/CANAL/NSU")));					
		}
		
		Mtxtb016IteracaoCanal iteracaoCanal = fornecedorDadosAgendamento.buscaUltimoMtxtb016IteracaoCanal(dados.getNuNsuTransacaoAgendamento());
		
		if (null != iteracaoCanal && null != iteracaoCanal.getDeRetorno()) {
			BuscadorTextoXml buscadorTextoXml = new BuscadorTextoXml(iteracaoCanal.getDeRetorno());
			dados.setMensagem(new MtxMensagem());
			dados.getMensagem().setCodigoMensagem(
					buscadorTextoXml.xpathTexto("*[local-name()='PAGAMENTO_BOLETO_SAIDA']/HEADER/COD_RETORNO"));
			dados.getMensagem().setMensagemNegocial(buscadorTextoXml.xpathTexto("*[local-name()='PAGAMENTO_BOLETO_SAIDA']/HEADER/MENSAGENS/NEGOCIAL"));
			dados.setNuNsuTransacaoSimtx(iteracaoCanal.getMtxtb014Transacao().getNuNsuTransacao());
		}
		}catch (Exception e) {
			logger.error(e);
		}
		
		return dados;
	}
	private MtxCancelamentoAgendamentoSaida preencherMtxCancelamentoAgendamentoSaida(Mtxtb034TransacaoAgendamento transacao) {
		MtxCancelamentoAgendamentoSaida dados = null;
		
		try {
		
		dados = new MtxCancelamentoAgendamentoSaida();
		BeanUtils.copyProperties(dados, transacao);
		dados.setValorTransacao(transacao.getValorTransacao() == null ? "" : transacao.getValorTransacao().toString());
		dados.setDataAgendamento(DataUtil.formatar(transacao.getDtReferencia(), DataUtil.FORMATO_DATA_PADRAO_BR));
		dados.setDataEfetivacao(DataUtil.formatar(transacao.getDtEfetivacao(), DataUtil.FORMATO_DATA_PADRAO_BR));
		
		if (null != transacao.getMtxtb004Canal() && null != transacao.getMtxtb001Servico()) {
			dados.setCanal(new MtxCanal());
			dados.setServico(new MtxServico());
			BeanUtils.copyProperties(dados.getCanal(), transacao.getMtxtb004Canal());
			BeanUtils.copyProperties(dados.getServico(), transacao.getMtxtb001Servico());
			dados.getServico().setNuVersaoServico(transacao.getNuVersaoServico());
		}
		
		if (null != transacao.getDeXmlAgendamento()) {
			BuscadorTextoXml buscadorTextoXml = new BuscadorTextoXml(transacao.getDeXmlAgendamento());
			dados.setLinhaDigitavel(
					buscadorTextoXml.xpathTexto("*[local-name()='PAGAMENTO_BOLETO_ENTRADA']/PAGAMENTO_BOLETO/INFORMACOES_BOLETO/LINHA_DIGITAVEL"));
			dados.getCanal().setNsuCanal(Long.valueOf(buscadorTextoXml.xpathTexto("*[local-name()='PAGAMENTO_BOLETO_ENTRADA']/HEADER/CANAL/NSU")));					
		}
		}catch (Exception e) {
			logger.error(e);
		}
		return dados;
	}
	
	private Mtxtb034TransacaoAgendamento preencherMtxtb034TransacaoAgendamento(MtxTransacaoAgendamento consulta) {
		Mtxtb034TransacaoAgendamento mtxtb034TransacaoAgendamento = new Mtxtb034TransacaoAgendamento();
		Mtxtb001Servico mtxtb001Servico = new Mtxtb001Servico();
		Mtxtb004Canal mtxtb004Canal = new Mtxtb004Canal();
		
		try {
			
		BeanUtils.copyProperties(mtxtb034TransacaoAgendamento, consulta);

		if (null != consulta.getServico() && null != consulta.getCanal()) {
			BeanUtils.copyProperties(mtxtb001Servico, consulta.getServico());
			BeanUtils.copyProperties(mtxtb004Canal, consulta.getCanal());
			mtxtb034TransacaoAgendamento.setMtxtb001Servico(mtxtb001Servico);
			mtxtb034TransacaoAgendamento.setMtxtb004Canal(mtxtb004Canal);
		}
		}catch (Exception e) {
			logger.error(e);
		}
		return mtxtb034TransacaoAgendamento;
	}
}
