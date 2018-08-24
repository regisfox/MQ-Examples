package br.gov.caixa.simtx.web.controles;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
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
//f776295@10.116.98.23/MULTICANAL/simtx-api.git
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb004Canal;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb030SituacaoCanal;
import br.gov.caixa.simtx.web.beans.MtxWebCanal;
import br.gov.caixa.simtx.web.configuracao.JWTUtil;
import br.gov.caixa.simtx.web.util.ConstantesWeb;
import br.gov.caixa.simtx.web.util.MatrizAcesso;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;

@Path("/sessao/canal")
public class CanalControle {

	private static final Logger logger = Logger.getLogger(CanalControle.class);
	
	@Inject
	protected FornecedorDados fornecedorDados;
	
	@Inject
	protected MatrizAcesso matriz;
	
	@GET
	@Path("/canais-ativos")
	@Produces(MediaType.APPLICATION_JSON)
	public List<MtxWebCanal> consultaCanaisAtivos() {

		List<MtxWebCanal> listaCanais = new ArrayList<>();
		List<Mtxtb004Canal> listaCanaisDb = fornecedorDados.buscarPorSituacao(new BigDecimal("1"));
		
		if(!listaCanaisDb.isEmpty()){
			for (Mtxtb004Canal canal : listaCanaisDb) {
				MtxWebCanal mtxCanal = new MtxWebCanal();
				mtxCanal.setNuCanal(canal.getNuCanal());
				mtxCanal.setNoCanal(canal.getNoCanal());
				listaCanais.add(mtxCanal);
			}
		}
		return listaCanais;
	}
	
	@GET
	@Path("/consulta")
	@Produces(MediaType.APPLICATION_JSON)
	public Response consultaCanais(@Context HttpServletRequest httpRequest) {

		String token = httpRequest.getHeader(JWTUtil.TOKEN_HEADER);
        Jws<Claims> jws = JWTUtil.decodificaToken(token);
        
		List<MtxWebCanal> listaCanais = new ArrayList<>();
			
		if (matriz.verificaPermissao(jws.getBody().getId(), ConstantesWeb.CANAIS)) {
			
			logger.info("Consulta de canais requisitada. Usuário: " + jws.getBody().getSubject());
			
			List<Mtxtb004Canal> listaCanaisDb = fornecedorDados.buscarTodosCanais();

			if (!listaCanaisDb.isEmpty()) {
				for (final Mtxtb004Canal canal : listaCanaisDb) {
					final MtxWebCanal mtxCanal = new MtxWebCanal();
					mtxCanal.setStatus(1);
					mtxCanal.setNuCanal(canal.getNuCanal());
					mtxCanal.setNoCanal(canal.getNoCanal());
					mtxCanal.setIcSituacaoCanal(canal.getIcSituacaoCanal());
					mtxCanal.setIcSituacaoCanalOrigem(canal.getIcSituacaoCanal());
					listaCanais.add(mtxCanal);
				}
			}
			
			return Response.ok().entity(listaCanais).build();
		} else {
			logger.warn("Ocorreu uma tentativa de acesso a consulta de canais não autorizada. Usuário: " + jws.getBody().getSubject());
			return Response.status(Response.Status.UNAUTHORIZED).build();
		}
	}

	@POST
	@Path("/alterar")
	@Produces(MediaType.APPLICATION_JSON)
	public Response alterarCanais(@Context HttpServletRequest httpRequest, List<MtxWebCanal> listaCanais) {

		String token = httpRequest.getHeader(JWTUtil.TOKEN_HEADER);
		Jws<Claims> jws = JWTUtil.decodificaToken(token);
		List<Mtxtb004Canal> listaCanaisBanco = new ArrayList<>();
		String usuarioLogado = jws.getBody().getSubject();

		if (matriz.verificaPermissao(jws.getBody().getId(), ConstantesWeb.ALTERA_CANAIS)) {
			logger.info("Alteração da situação do canal solicitada. Usuário: " + usuarioLogado);
			if (!listaCanais.isEmpty()) {
				
				try {
					
					for (final MtxWebCanal canal : listaCanais) {
						final Mtxtb004Canal canalAux = new Mtxtb004Canal();
						canalAux.setNuCanal(canal.getNuCanal());
						
						final Mtxtb004Canal mtxtb004Canal = this.fornecedorDados.buscarCanalPorPK(canalAux);
						canal.setStatus(1);
						mtxtb004Canal.setNuCanal(canal.getNuCanal());
						mtxtb004Canal.setIcSituacaoCanal(canal.getIcSituacaoCanal());
						listaCanaisBanco.add(mtxtb004Canal);
					}
					
					fornecedorDados.alteraSituacaoCanal(listaCanaisBanco);
					
					for (final MtxWebCanal canal : listaCanais) {
						final Mtxtb030SituacaoCanal situacao = new Mtxtb030SituacaoCanal();
						situacao.setNuCanal(canal.getNuCanal());
						situacao.setDataTsAlteracao(new Date());
						situacao.setCoMaquinaAlteracao(httpRequest.getRemoteAddr());
						situacao.setCoUsuarioAlteracao(usuarioLogado);
						situacao.setIcSituacaoCanal(canal.getIcSituacaoCanal().intValue());
						fornecedorDados.salvaSituacaoCanal(situacao);
					}
					
				} catch (Exception e) {
					logger.error("Erro ao alterar o canal via web");
					return Response.ok().entity(Response.Status.SERVICE_UNAVAILABLE).build();
				}
			}
			return Response.ok().entity(listaCanais).build();
		} else {
			logger.warn("Ocorreu uma tentativa de acesso a alteração de canais não autorizada. Usuário: " + usuarioLogado);
			return Response.status(Response.Status.UNAUTHORIZED).build();
		}
	}
}
