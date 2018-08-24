package br.gov.caixa.simtx.web.controles;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
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

import org.apache.log4j.Logger;

import br.gov.caixa.simtx.persistencia.cache.core.FornecedorDados;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb001Servico;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb011VersaoServico;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb029SituacaoVersaoServico;
import br.gov.caixa.simtx.web.beans.MtxServico;
import br.gov.caixa.simtx.web.beans.MtxVersaoServico;
import br.gov.caixa.simtx.web.configuracao.JWTUtil;
import br.gov.caixa.simtx.web.util.ConstantesWeb;
import br.gov.caixa.simtx.web.util.MatrizAcesso;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;

@Path("/sessao/servico")
public class ServicoControle {

	private static final Logger logger = Logger.getLogger(ServicoControle.class);
	
	@Inject
	protected FornecedorDados fornecedorDados;
	
	@Inject
	protected MatrizAcesso matriz;
	
	@GET
	@Path("/servicos-ativos")
	@Produces(MediaType.APPLICATION_JSON)
	public List<MtxServico> consultaServicosAtivos() {
		final List<Mtxtb001Servico> listaServicosDb = fornecedorDados.buscarPorServicoCancelamento(1);
		
		return preencheMtxServico(listaServicosDb);
	}
	
	@GET
	@Path("/consulta-servico-todos")
	@Produces(MediaType.APPLICATION_JSON)
	public List<MtxServico> consultaServicosTodos() {
		final List<Mtxtb001Servico> listaServicosDb = fornecedorDados.buscarTodosServicos();
		
		return preencheMtxServico(listaServicosDb);
	}
	
	@GET
	@Path("/versaoservico-ativos")
	@Produces(MediaType.APPLICATION_JSON)
	public List<MtxVersaoServico> getVersaoServiceAtivos() {
		final List<MtxVersaoServico> result = new ArrayList<>();
		
		try {
			final List<Mtxtb011VersaoServico> listaVersaoServicoDb = fornecedorDados.buscarTodosVersaoServico();
			
			for(final Mtxtb011VersaoServico versaoServico: listaVersaoServicoDb) {
				final MtxVersaoServico obj = new MtxVersaoServico();
				
				obj.setIcSituacaoServico(versaoServico.getIcStcoVrsoSrvco());
				obj.setIcSituacaoServicoOrigem(versaoServico.getIcStcoVrsoSrvco());
				obj.setIcVersaoServico(versaoServico.getId().getNuVersaoServico());
				obj.setNoServico(versaoServico.getMtxtb001Servico().getNoServico());
				obj.setNuServico(versaoServico.getMtxtb001Servico().getNuServico());
				
				result.add(obj);
			}
			
			return result;
			
		} catch (Exception e) {
			logger.error("Erro ao consultar versão serviços via web");
			
			return null;
		}
	}
	
	@POST
	@Path("/consulta-versaoservico-parametros")
	@Produces(MediaType.APPLICATION_JSON)
	public Response consultaVersaoServicoPorParametros(@Context HttpServletRequest httpRequest, MtxVersaoServico servico) {
		final String servicoRest;
		
		if(servico.getNoServico() != null) {
			servicoRest = "/consulta-versaoservico-nome";
			
		} else if(servico.getIcSituacaoServico() != null) {
			servicoRest = "/consulta-versaoservico-situacao";
			
		} else {
			servicoRest = null;
		}
		
		return getVersaoServicos(httpRequest, servico, servicoRest);
	}
	
	/*
	@POST
	@Path("/consulta-versaoservico-nome")
	@Produces(MediaType.APPLICATION_JSON)
	@Deprecated
	public Response consultaVersaoServicoPorNome(@Context HttpServletRequest httpRequest, MtxVersaoServico servico) {
		final String servicoRest;
		
		//if(servico.getServico() != null && servico.getServico().getNoServico() != null) {
		if(servico.getNoServico() != null) {
			servicoRest = "/consulta-versaoservico-nome";
		} else {
			servicoRest = null;
		}
		
		return getVersaoServicos(httpRequest, servico, servicoRest);
	}
	*/
	
	/*
	@POST
	@Path("/consulta-versaoservico-situacao")
	@Produces(MediaType.APPLICATION_JSON)
	@Deprecated
	public Response consultaVersaoServicoPorSituacao(@Context HttpServletRequest httpRequest, MtxVersaoServico servico) {
		final String servicoRest;
		
		if(servico.getIcSituacaoServico() != null) {
			servicoRest = "/consulta-versaoservico-situacao";
		} else {
			servicoRest = null;
		}
		
		return getVersaoServicos(httpRequest, servico, servicoRest);
	}
	*/
	
	private Response getVersaoServicos(final HttpServletRequest httpRequest, final MtxVersaoServico servico, final String servicoRest) {
		final List<MtxVersaoServico> result = new ArrayList<>();
		
		final String token = httpRequest.getHeader(JWTUtil.TOKEN_HEADER);
        final Jws<Claims> jws = JWTUtil.decodificaToken(token);
			
		if (matriz.verificaPermissao(jws.getBody().getId(), ConstantesWeb.SERVICOS)) {
			
			logger.info("Consulta de versão serviços por nome requisitada. Usuário: " + jws.getBody().getSubject());
			
			try {
				final List<Mtxtb011VersaoServico> listaServicosDb;
				
				if(servicoRest == null) {
					listaServicosDb = fornecedorDados.buscarTodosVersaoServico();
					
				} else if(servicoRest.equals("/consulta-versaoservico-nome")) {
					listaServicosDb = fornecedorDados.buscarVersaoServicoPorNome(servico.getNoServico());
					
				} else if (servicoRest.equals("/consulta-versaoservico-situacao")) {
					listaServicosDb = fornecedorDados.buscarVersaoServicoPorSituacao(servico.getIcSituacaoServico().intValue());
					
				} else {
					listaServicosDb = fornecedorDados.buscarTodosVersaoServico();
				}
				
				if (listaServicosDb != null && !listaServicosDb.isEmpty()) {
					for (final Mtxtb011VersaoServico servicoAux : listaServicosDb) {
						final MtxVersaoServico versaoServico = preencheMtxVersaoServico(servicoAux);
						
						result.add(versaoServico);
					}
					
					Collections.sort(result, new MtxVersaoServicoComparator());
				}
				
			} catch (Exception e) {
				logger.error("Erro ao consultar versão serviços por nome via web");
				return Response.ok().entity(Response.Status.SERVICE_UNAVAILABLE).build();
			}
			
		} else {
			logger.warn("Ocorreu uma tentativa de acesso a consulta de versão serviço por nome não autorizada. Usuário: " + jws.getBody().getSubject());
			return Response.status(Response.Status.UNAUTHORIZED).build();
		}
		
		return Response.ok().entity(result).build();
	}
	
	@POST
	@Path("/alterar-versaoservico")
	@Produces(MediaType.APPLICATION_JSON)
	public Response alterarVersaoService(@Context HttpServletRequest httpRequest, List<MtxVersaoServico> listaServico){
		final String token = httpRequest.getHeader(JWTUtil.TOKEN_HEADER);
		final Jws<Claims> jws = JWTUtil.decodificaToken(token);
		final List<Mtxtb011VersaoServico> listaServicosBanco = new ArrayList<>();
		final String usuarioLogado = jws.getBody().getSubject();
		
		if (matriz.verificaPermissao(jws.getBody().getId(), ConstantesWeb.ALTERA_SERVICO)) {
			logger.info("Alteração da situação do serviço solicitada. Usuário: " + usuarioLogado);
			
			if (!listaServico.isEmpty()) {
				
				try {
					for (final MtxVersaoServico servico : listaServico) {
						final Mtxtb011VersaoServico mtxtb011VersaoServico = this.fornecedorDados.buscarVersaoServico(servico.getIcVersaoServico(), servico.getNuServico());
						
						mtxtb011VersaoServico.setIcStcoVrsoSrvco(servico.getIcSituacaoServico());
						mtxtb011VersaoServico.setDhAtualizacao(Calendar.getInstance().getTime());
						
						listaServicosBanco.add(mtxtb011VersaoServico);
					}
					
					fornecedorDados.alteraSituacaoServico(listaServicosBanco);
					
					for (final MtxVersaoServico servico : listaServico) {
						final Mtxtb029SituacaoVersaoServico situacao = new Mtxtb029SituacaoVersaoServico();
						situacao.setIcSituacaoVersaoServico(servico.getIcSituacaoServico().intValue());
						situacao.setNuServico(servico.getNuServico());
						situacao.setNuVersaoServico(servico.getIcVersaoServico());
						situacao.setDataTsAlteracao(Calendar.getInstance().getTime());
						situacao.setCoMaquinaAlteracao(httpRequest.getRemoteAddr());
						situacao.setCoUsuarioAlteracao(usuarioLogado);
						fornecedorDados.salvaSituacaoServico(situacao);
					}
					
				} catch (Exception e) {
					logger.error("Erro ao alterar o servico via web");
					return Response.ok().entity(Response.Status.SERVICE_UNAVAILABLE).build();
				}
			}
			return Response.ok().entity(listaServico).build();
		} else {
			logger.warn("Ocorreu uma tentativa cancelamento de agendamento não autorizada. Usuário: " + usuarioLogado);
			return Response.status(Response.Status.UNAUTHORIZED).build();
        }
	}
	
	private MtxVersaoServico preencheMtxVersaoServico(final Mtxtb011VersaoServico servicoAux) {
		final MtxVersaoServico versaoServico = new MtxVersaoServico();
		//versaoServico.setServico(new MtxServico());
		//versaoServico.getServico().setNuServico(servicoAux.getId().getNuServico001());
		//versaoServico.getServico().setNoServico(servicoAux.getMtxtb001Servico().getNoServico());
		versaoServico.setIcSituacaoServico(servicoAux.getIcStcoVrsoSrvco());
		versaoServico.setIcSituacaoServicoOrigem(servicoAux.getIcStcoVrsoSrvco());
		versaoServico.setIcVersaoServico(servicoAux.getId().getNuVersaoServico());
		versaoServico.setNuServico(servicoAux.getId().getNuServico001());
		versaoServico.setNoServico(servicoAux.getMtxtb001Servico().getNoServico());
		
		return versaoServico;
	}
	
	private List<MtxServico> preencheMtxServico(final List<Mtxtb001Servico> listaServicosDb) {
		final List<MtxServico> result = new ArrayList<>();
		
		if(!listaServicosDb.isEmpty()){
			for (final Mtxtb001Servico servico : listaServicosDb) {
				final MtxServico servicoBean = new MtxServico();
				servicoBean.setNuServico(servico.getNuServico());
				servicoBean.setNoServico(servico.getNoServico());
				
				result.add(servicoBean);
			}
			
			Collections.sort(result, new MtxServicoComparator());
		}
		
		return result;
	}
	
	private static class MtxServicoComparator implements Comparator<MtxServico> {

		@Override
		public int compare(MtxServico o1, MtxServico o2) {
			return o1.getNoServico().compareTo(o2.getNoServico());
		}
	}
	
	private static class MtxVersaoServicoComparator implements Comparator<MtxVersaoServico> {

		@Override
		public int compare(MtxVersaoServico o1, MtxVersaoServico o2) {
			int result = o1.getNoServico().compareTo(o2.getNoServico());
			
			if(result == 0) {
				result = new Integer(o1.getIcVersaoServico()).compareTo(o2.getIcSituacaoServico()) * (-1);
			}
			
			return result;
		}
	}
}
