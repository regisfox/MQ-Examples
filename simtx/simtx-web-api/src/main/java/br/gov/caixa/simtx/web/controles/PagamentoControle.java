package br.gov.caixa.simtx.web.controles;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.Logger;
import org.xml.sax.SAXException;

import br.gov.caixa.simtx.persistencia.cache.core.FornecedorDados;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb001Servico;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb004Canal;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb023Parametro;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb035TransacaoConta;
import br.gov.caixa.simtx.util.xml.BuscadorTextoXml;
import br.gov.caixa.simtx.web.beans.MtxTransacaoConta;
import br.gov.caixa.simtx.web.beans.MtxTransacaoContaEntrada;
import br.gov.caixa.simtx.web.configuracao.CancelamentoTransacional;
import br.gov.caixa.simtx.web.configuracao.JWTUtil;
import br.gov.caixa.simtx.web.util.ConstantesWeb;
import br.gov.caixa.simtx.web.util.MatrizAcesso;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;

@Path("/sessao/pagamento")
public class PagamentoControle {

	private static final Logger logger = Logger.getLogger(PagamentoControle.class);

	private static final int URL_CANCELAMENTO_PAGAMENTO_ROTEADOR = 13;
	
	@Inject
	protected FornecedorDados fornecedorDados;
	
	@Inject
	protected MatrizAcesso matriz;

	@POST
	@Path("/consulta")
	@Produces(MediaType.APPLICATION_JSON)
	public Response consultaPagamento(@Context HttpServletRequest httpRequest, MtxTransacaoConta consulta) throws ParserConfigurationException, SAXException, IOException, IllegalAccessException, InvocationTargetException {
		
		List<MtxTransacaoContaEntrada> listaTransacaoConta = new ArrayList<>();
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		
		String token = httpRequest.getHeader(JWTUtil.TOKEN_HEADER);
		Jws<Claims> jws = JWTUtil.decodificaToken(token);
		
		if (matriz.verificaPermissao(jws.getBody().getId(), ConstantesWeb.PAGAMENTO)) {
			
			logger.info("Consulta de pagamento requisitada. Usuário: " + jws.getBody().getSubject());
			
			List<Mtxtb035TransacaoConta> listaTransacaoDb = fornecedorDados.buscaTransacao(preencherMtxtb035TransacaoConta(consulta));
		
			if (!listaTransacaoDb.isEmpty()) {

				for (Mtxtb035TransacaoConta transacao : listaTransacaoDb) {
					MtxTransacaoContaEntrada mtxTransacao = new MtxTransacaoContaEntrada();
					mtxTransacao.setDataReferencia(sdf.format(transacao.getDataReferencia()));
					mtxTransacao.setNuNsuTransacaoRefMtx016(transacao.getNuNsuTransacao());
					mtxTransacao.setNoCanal(transacao.getMtxtb004Canal().getNoCanal());
					mtxTransacao.setNoServico(transacao.getMtxtb001Servico().getNoServico());
					mtxTransacao.setNuCanal(transacao.getNumeroCanal());
					mtxTransacao.setNuServico(transacao.getNumeroServico());
					mtxTransacao.setAgencia(transacao.getNumeroUnidade());
					mtxTransacao.setOpProduto(transacao.getOpProduto());
					mtxTransacao.setConta(transacao.getNumeroConta());
					mtxTransacao.setDv(transacao.getNuDvConta());
					mtxTransacao.setIndicadorConta(transacao.getIndicadorConta());
					BuscadorTextoXml buscadorTextoXml = new BuscadorTextoXml(transacao.getMtxtb016IteracaoCanal().getDeRecebimento());
					mtxTransacao.setLinhaDigitavel(buscadorTextoXml
							.xpathTexto("*[local-name()='PAGAMENTO_BOLETO_ENTRADA']/PAGAMENTO_BOLETO/INFORMACOES_BOLETO/LINHA_DIGITAVEL"));
					mtxTransacao.setValor(buscadorTextoXml
							.xpathTexto("*[local-name()='PAGAMENTO_BOLETO_ENTRADA']/PAGAMENTO_BOLETO/INFORMACOES_BOLETO/VALOR_PAGO"));
					
					String nsuCanal = buscadorTextoXml
							.xpathTexto("*[local-name()='PAGAMENTO_BOLETO_ENTRADA']/HEADER/CANAL/NSU");
					if(!nsuCanal.isEmpty())
						mtxTransacao.setNsuCanal(Long.valueOf(nsuCanal));

					listaTransacaoConta.add(mtxTransacao);

				}
			} else {
				return Response.ok().entity(listaTransacaoConta).build();
			}
		} else {
			logger.warn("Ocorreu uma tentativa de acesso a consulta de pagamentos não autorizada. Usuário: " + jws.getBody().getSubject());
			return Response.status(Response.Status.UNAUTHORIZED).build();
		}
		return Response.ok().entity(listaTransacaoConta).build();
	}
	
	@POST
	@Path("/cancelar")
	@Produces(MediaType.APPLICATION_JSON)
	public Response efetuaCancelamentoPagamento(@Context HttpServletRequest httpRequest,
			List<MtxTransacaoContaEntrada> cancelaReq) {
		
		String token = httpRequest.getHeader(JWTUtil.TOKEN_HEADER);
        Jws<Claims> jws = JWTUtil.decodificaToken(token);
        String usuarioLogado = jws.getBody().getSubject();
        
        if (matriz.verificaPermissao(jws.getBody().getId(), ConstantesWeb.CANCELA_PAGAMENTO)) {
        	
        	logger.info("Cancelamento de pagamento solicitado. Usuário: " + usuarioLogado);
        	
    		for (MtxTransacaoContaEntrada canc: cancelaReq){
				canc.setCodigoMaquina(httpRequest.getRemoteAddr());
				canc.setCodigoUsuario(usuarioLogado);
			}
        	
        	CancelamentoTransacional cancelamento = new CancelamentoTransacional();
        	Mtxtb023Parametro parametro = fornecedorDados.buscarParametroPorPK(URL_CANCELAMENTO_PAGAMENTO_ROTEADOR);
        	String retornoJson = cancelamento.envioTransacional(parametro.getDeConteudoParam(), cancelaReq);
        	return Response.ok().entity(retornoJson).build();
        } else {
        	logger.warn("Ocorreu uma tentativa cancelamento de pagamento não autorizada. Usuário: " + usuarioLogado);
			return Response.status(Response.Status.UNAUTHORIZED).build();
        }
	}
	
	private Mtxtb035TransacaoConta preencherMtxtb035TransacaoConta(MtxTransacaoConta consulta) throws IllegalAccessException, InvocationTargetException {
		Mtxtb035TransacaoConta mtxtb035TransacaoConta = new Mtxtb035TransacaoConta();
		Mtxtb001Servico mtxtb001Servico = new Mtxtb001Servico();
		Mtxtb004Canal mtxtb004Canal = new Mtxtb004Canal();

		BeanUtils.copyProperties(mtxtb035TransacaoConta, consulta);

		if (null != consulta.getServico() && null != consulta.getCanal()) {
			BeanUtils.copyProperties(mtxtb001Servico, consulta.getServico());
			BeanUtils.copyProperties(mtxtb004Canal, consulta.getCanal());
			mtxtb035TransacaoConta.setMtxtb001Servico(mtxtb001Servico);
			mtxtb035TransacaoConta.setMtxtb004Canal(mtxtb004Canal);
		}

		return mtxtb035TransacaoConta;
	}
}
