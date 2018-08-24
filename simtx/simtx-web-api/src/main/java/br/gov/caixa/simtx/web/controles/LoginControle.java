package br.gov.caixa.simtx.web.controles;


import java.util.ArrayList;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;

import br.gov.caixa.simtx.persistencia.cache.core.FornecedorDados;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb037Funcionalidade;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb038GrupoAcesso;
import br.gov.caixa.simtx.web.configuracao.JWTUtil;
import br.gov.caixa.simtx.web.dominio.Credencial;
import br.gov.caixa.simtx.web.dominio.Usuario;
import br.gov.caixa.simtx.web.ldap.ComponenteLdap;
import br.gov.caixa.simtx.web.ldap.LdapSpring;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;

@Path("/")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class LoginControle {
	
	private static final Logger logger = Logger.getLogger(LoginControle.class);
	
	@Inject
	FornecedorDados fornecedorDados;
	
    @GET
    @Path("home")
    public Usuario me(@Context HttpServletRequest httpRequest) {
        String token = httpRequest.getHeader(JWTUtil.TOKEN_HEADER);
        Jws<Claims> jws = JWTUtil.decodificaToken(token);
        Usuario me = new Usuario();
        me.setNome(jws.getBody().getSubject());
        me.setGrupoAcesso(jws.getBody().getId());
        return null;
    }

    @POST
    @Path("login")
    public Response login(Credencial credencial) {
    	logger.info(" ==== Processo Login Iniciado ==== ");
    	try {
			ComponenteLdap comp = new ComponenteLdap();
	
			if (comp.validaUser(credencial.getNomeUsuario(), credencial.getSenha())) {
				LdapSpring ldap = new LdapSpring();
				Usuario usuario = ldap.carregarDadoUsuario(credencial.getNomeUsuario());
				Mtxtb038GrupoAcesso grupo = fornecedorDados.buscaGrupoAcesso(usuario.getGrupoAcesso());
				ArrayList<String> listaFuncionalidades = new ArrayList<>();
				
				if (grupo != null) {
					for(Mtxtb037Funcionalidade funcionalidade :grupo.getMtxtb037Funcionalidade()){
						listaFuncionalidades.add(funcionalidade.getNoFuncionalidade());
					}
					usuario.setToken(JWTUtil.geraToken(credencial.getNomeUsuario(), usuario.getGrupoAcesso()));
					usuario.setFuncionalidades(listaFuncionalidades);
					logger.info("Login efetuado com sucesso. NomeUsuario:" + usuario.getNome() + " | GrupoAcesso:"
							+ usuario.getGrupoAcesso() + " | Matricula:" + credencial.getNomeUsuario());
					return Response.ok().entity(usuario).build();
				} 
				else {
					logger.error("Tentativa de acesso ao sistema NEGADO. Grupo de Acesso nao encontrado: "
							+ usuario.getGrupoAcesso() + ". Usuario: " + credencial.getNomeUsuario());
					return Response.status(Response.Status.UNAUTHORIZED).build();
				}
	
			} 
			else {
				logger.error("Tentativa de acesso ao sistema NEGADO. Usuario informado: " + credencial.getNomeUsuario());
				return Response.status(Response.Status.UNAUTHORIZED).build();
			}
    	} 
    	catch (Exception e) {
    		logger.error("Erro inesperado na validacao do usuario: " + credencial.getNomeUsuario(), e);
			return Response.status(Response.Status.UNAUTHORIZED).build();
		}
    	finally {
    		logger.info(" ==== Processo Login Finalizado ==== ");
		}
	}
}
