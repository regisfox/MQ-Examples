package br.gov.caixa.simtx.web.controles;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
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

import br.gov.caixa.simtx.persistencia.cache.core.FornecedorDados;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb040PrmtoPgtoContingencia;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb040PrmtoPgtoContingenciaPK;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb041HistoricoPrmtoPgtoContingencia;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb041HistoricoPrmtoPgtoContingenciaPK;
import br.gov.caixa.simtx.persistencia.vo.TipoAutorizacaoPagamentoEnum;
import br.gov.caixa.simtx.persistencia.vo.TipoBoletoEnum;
import br.gov.caixa.simtx.persistencia.vo.TipoContingenciaEnum;
import br.gov.caixa.simtx.util.data.DataUtil;
import br.gov.caixa.simtx.web.beans.MtxCanal;
import br.gov.caixa.simtx.web.beans.MtxParametroBoletoContingencia;
import br.gov.caixa.simtx.web.configuracao.JWTUtil;
import br.gov.caixa.simtx.web.util.ConstantesWeb;
import br.gov.caixa.simtx.web.util.MatrizAcesso;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;

/**
 *  Controle criado para definir servicos Rest
 *  para consulta e alteracao de parametros de boleto contingencia.
 *  
 */
@Path("/sessao/parametroboletocontingencia")
public class ParametroBoletoContingenciaControle {
	
	private static final Logger logger = Logger.getLogger(ParametroBoletoContingenciaControle.class);
	
	@Inject
	protected FornecedorDados fornecedorDados;
	
	@Inject
	protected MatrizAcesso matriz;
	
	@GET
	@Path("/consulta")
	@Produces(MediaType.APPLICATION_JSON)
	public Response consultaParametrosBoletoContingencia(@Context HttpServletRequest httpRequest) throws IllegalAccessException, InvocationTargetException {
		final List<MtxParametroBoletoContingencia> listaTransacao = new ArrayList<>();
		
		final String token = httpRequest.getHeader(JWTUtil.TOKEN_HEADER);
		final Jws<Claims> jws = JWTUtil.decodificaToken(token);

		if (matriz.verificaPermissao(jws.getBody().getId(), ConstantesWeb.PARAMETROS_BOLETOS_CONTINGENCIA)) {
			
			logger.info("Consulta de parametros de boletos contingencia. Usuário: " + jws.getBody().getSubject());
			
			final List<Mtxtb040PrmtoPgtoContingencia> parametrosBoletoContingenciaList = fornecedorDados.buscarTodosParametrosBoletosContingencia();
			
			if (null != parametrosBoletoContingenciaList && !parametrosBoletoContingenciaList.isEmpty()) {
				for (final Mtxtb040PrmtoPgtoContingencia transacao : parametrosBoletoContingenciaList) {
					listaTransacao.add(preencheMtxParametroBoletoContingencia(transacao));
				}	
			}else {
				logger.info("Pesquisa Sem resultados");	
			}
		} else {
			logger.warn("Ocorreu uma tentativa de acesso a consulta de parametros boletos contingencia não autorizada. Usuário: " + jws.getBody().getSubject());
			return Response.status(Response.Status.UNAUTHORIZED).build();
		}
		
		return  Response.ok().entity(listaTransacao).build();
	}
	
	@POST
	@Path("/alterar")
	@Produces(MediaType.APPLICATION_JSON)
	public Response alterarParametrosBoletoContingencia(@Context HttpServletRequest httpRequest,  List<MtxParametroBoletoContingencia> listaParametros) {
		final String token = httpRequest.getHeader(JWTUtil.TOKEN_HEADER);
		final Jws<Claims> jws = JWTUtil.decodificaToken(token);
		final String usuarioLogado = jws.getBody().getSubject();

		if (matriz.verificaPermissao(jws.getBody().getId(), ConstantesWeb.ALTERA_PARAMETROS_BOLETOS_CONTINGENCIA)) {
			logger.info("Alteração do parametro boleto contingencia solicitada. Usuário: " + usuarioLogado);
			if (!listaParametros.isEmpty()) {
				final List<Mtxtb040PrmtoPgtoContingencia> parametrosList = new ArrayList<>();
				
				for (final MtxParametroBoletoContingencia dados : listaParametros) {
					parametrosList.add(preencheMtxtb040PrmtoPgtoContingencia(dados));
				}
				
				try {
					
					fornecedorDados.updateParametrosBoletosContingencia(parametrosList);
					
					updateHistoricoParametroBoleto(usuarioLogado, httpRequest.getRemoteAddr(), parametrosList);
					
				} catch (Exception e) {
					logger.error("Erro ao alterar o parametro de boleto contingencia via web");
					return Response.ok().entity(Response.Status.SERVICE_UNAVAILABLE).build();
				}
			}
			
			return Response.ok().entity(listaParametros).build();
			
		} else {
			logger.warn("Ocorreu uma tentativa de acesso a alteração de parametro de boleto contingencia não autorizada. Usuário: " + usuarioLogado);
			return Response.status(Response.Status.UNAUTHORIZED).build();
		}
	}
	
	private void updateHistoricoParametroBoleto(final String usuario, final String maquina, final List<Mtxtb040PrmtoPgtoContingencia> parametrosList) throws Exception {
		if(parametrosList != null) {
			for (final Mtxtb040PrmtoPgtoContingencia parametro : parametrosList) {
				
				final Mtxtb041HistoricoPrmtoPgtoContingencia historico = new Mtxtb041HistoricoPrmtoPgtoContingencia();
				historico.setId(new Mtxtb041HistoricoPrmtoPgtoContingenciaPK());
				
				// ID
				historico.getId().setNuCanal004(parametro.getId().getNuCanal004());
				historico.getId().setIcTipoBoleto(parametro.getId().getIcTipoBoleto());
				historico.getId().setIcOrigemContingencia(parametro.getId().getIcOrigemContingencia());
				
				// DATA
				historico.setDataTsAlteracao(Calendar.getInstance().getTime());
				
				// DADOS
				historico.setValorMinimo(parametro.getValorMinimo());
				historico.setValorMaximo(parametro.getValorMaximo());
				historico.setIcAutorizacaoContingencia(parametro.getIcAutorizacaoContingencia());
				
				// REGISTRO SISTEMA
				historico.setCoUsuarioAlteracao(usuario);
				historico.setCoMAquinaAlteracao(maquina);
				
				fornecedorDados.salvaHistoricoParametroBoleto(historico);
			}
		}
	}
	
	private MtxParametroBoletoContingencia preencheMtxParametroBoletoContingencia(final Mtxtb040PrmtoPgtoContingencia dados) throws IllegalAccessException, InvocationTargetException {
		MtxParametroBoletoContingencia result = null;
		
		if(dados != null) {
			result = new MtxParametroBoletoContingencia();
			
			// Canal.
			if(dados.getMtxtb004Canal() != null) {
				result.setCanal(new MtxCanal());
				BeanUtils.copyProperties(result.getCanal(), dados.getMtxtb004Canal());
			}
			
			// Tipo Origem Contingencia.
			final TipoContingenciaEnum tipoContingenciaEnum = TipoContingenciaEnum.obterTipoPorCodigo(dados.getId().getIcOrigemContingencia());
			result.setIcOrigemContingenciaCodigo(tipoContingenciaEnum.getCodigo());
			result.setIcOrigemContingenciaDescricao(tipoContingenciaEnum.getDescricao());
			
			// Tipo Boleto.
			final TipoBoletoEnum tipoBoletoEnum = TipoBoletoEnum.obterTipoPorCodigo(dados.getId().getIcTipoBoleto());
			result.setIcTipoBoletoCodigo(tipoBoletoEnum.getCodigo());
			result.setIcTipoBoletoDescricao(tipoBoletoEnum.getDescricao());
			
			// Data de atualizacao.
			result.setDataAtualizacao(DataUtil.formatar(dados.getDhAtualizacao(), DataUtil.FORMATO_DATA_PADRAO_BR));

			// Tipo Autorizacao Pagamento.
			final TipoAutorizacaoPagamentoEnum tipoAutorizacaoPagamentoEnum = TipoAutorizacaoPagamentoEnum.obterTipoPorCodigo(dados.getIcAutorizacaoContingencia());
			
			result.setIcAutorizacaoPagamentoCodigo(tipoAutorizacaoPagamentoEnum.getCodigo());
			result.setIcAutorizacaoPagamentoDescricao(tipoAutorizacaoPagamentoEnum.getDescricao());
			
			// Tipo Autorizacao Pagamento - Codigo Origem
			result.setIcAutorizacaoPagamentoCodigoOrigem(tipoAutorizacaoPagamentoEnum.getCodigo());
			
			// Valor Minimo e Valor Maximo.
			result.setValorRecebimentoMinimo(dados.getValorMinimo().toString());
			result.setValorRecebimentoMaximo(dados.getValorMaximo().toString());
			
			// Valor Minimo Origem e Valor Maximo Origem.
			result.setValorRecebimentoMinimoOrigem(dados.getValorMinimo().toString());
			result.setValorRecebimentoMaximoOrigem(dados.getValorMaximo().toString());
		}
		
		return result;
	}
	
	private Mtxtb040PrmtoPgtoContingencia preencheMtxtb040PrmtoPgtoContingencia(final MtxParametroBoletoContingencia dados) {
		Mtxtb040PrmtoPgtoContingencia result = null;
		
		if(dados != null) {
			// Criando a PK (Canal, Tipo Boleto, Origem Contingencia).
			final Mtxtb040PrmtoPgtoContingenciaPK id = new Mtxtb040PrmtoPgtoContingenciaPK();
			
			// PK Canal.
			id.setNuCanal004((int) dados.getCanal().getNuCanal());
						
			// PK Tipo Boleto.
			id.setIcTipoBoleto(dados.getIcTipoBoletoCodigo());
						
			// PK Origem Contingencia.
			id.setIcOrigemContingencia(dados.getIcOrigemContingenciaCodigo());
			
			// Busca por PK.
			result = this.fornecedorDados.buscarPrmtoPgtoCntngnciaPorCanal(id);
			
			result.setDhAtualizacao(Calendar.getInstance().getTime());
			
			// Autorizacao Pagamento Habilitado / Desabilitado.
			result.setIcAutorizacaoContingencia(dados.getIcAutorizacaoPagamentoCodigo());
			
			// Valor limite.
			// TODO Confirmar se eh valor maximo mesmo.
			result.setValorMaximo(new BigDecimal(dados.getValorRecebimentoMaximo()));
		}
		
		return result;
	}
}
