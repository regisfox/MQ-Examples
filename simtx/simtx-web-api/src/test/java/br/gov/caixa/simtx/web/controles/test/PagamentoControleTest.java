package br.gov.caixa.simtx.web.controles.test;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.runners.MockitoJUnitRunner;
import org.xml.sax.SAXException;

import com.google.gson.Gson;

import br.gov.caixa.simtx.persistencia.cache.core.test.FornecedorDadosMock;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb001Servico;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb004Canal;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb035TransacaoConta;
import br.gov.caixa.simtx.util.data.DataUtil;
import br.gov.caixa.simtx.util.xml.BuscadorTextoXml;
import br.gov.caixa.simtx.web.beans.MtxCanal;
import br.gov.caixa.simtx.web.beans.MtxServico;
import br.gov.caixa.simtx.web.beans.MtxTransacaoConta;
import br.gov.caixa.simtx.web.beans.MtxTransacaoContaEntrada;
import io.jsonwebtoken.lang.Assert;

@RunWith(MockitoJUnitRunner.class)
@FixMethodOrder(MethodSorters.JVM)
public class PagamentoControleTest {

	private static final Logger logger = Logger.getLogger(PagamentoControleTest.class);

	private FornecedorDadosMock fornecedorDadosMock;

	private MtxTransacaoConta consultaSemParametros;

	private MtxTransacaoConta consultaMocked;

	@Before
	public void inicializaDados() {
		fornecedorDadosMock = new FornecedorDadosMock();
		consultaMocked = new MtxTransacaoConta();
		consultaMocked.setNumeroUnidade(1234);
		consultaMocked.setNuDvConta(1);
		consultaMocked.setDataReferencia(new Date());
		consultaMocked.setNumeroUnidade(1234);
		consultaMocked.setCanal(new MtxCanal(104));
		consultaMocked.setServico(new MtxServico(123));
		consultaSemParametros = new MtxTransacaoConta();
	}

	@Test
	public void consultaPagamento() throws ParserConfigurationException, SAXException, IOException, IllegalAccessException, InvocationTargetException {
		logger.info("inicio consultaPagamento");
		Assert.notNull(consultaPagamento(null, consultaMocked));
		logger.info("termino consultaPagamento");
		logger.info(" ****** ");

	}

	@Test
	public void consultaPagamentoSemParametros()
			throws IllegalAccessException, InvocationTargetException, ParserConfigurationException, SAXException, IOException {
		logger.info("inicio consultaPagamentoSemParametros");
		Assert.notNull(consultaPagamento(null, consultaSemParametros));
		logger.info("termino consultaPagamentoSemParametros");
		logger.info(" ****** ");
	}

	public List<MtxTransacaoContaEntrada> consultaPagamento(@Context HttpServletRequest httpRequest, MtxTransacaoConta consulta)
			throws ParserConfigurationException, SAXException, IOException, IllegalAccessException, InvocationTargetException {
		List<MtxTransacaoContaEntrada> listaTransacaoConta = new ArrayList<>();

		List<Mtxtb035TransacaoConta> listaTransacaoDb = fornecedorDadosMock.buscaTransacao(preencherMtxtb035TransacaoConta(consulta));

		if (!listaTransacaoDb.isEmpty()) {
			for (Mtxtb035TransacaoConta transacao : listaTransacaoDb) {
				listaTransacaoConta.add(preencherDadosPagamento(transacao));
			}
		}
		Gson gson = new Gson();
		logger.info("Dados Consulta: " + gson.toJson(consulta));
		logger.info("Lista Pagamentos: " + gson.toJson(listaTransacaoConta));

		return listaTransacaoConta;
	}

	private MtxTransacaoContaEntrada preencherDadosPagamento(Mtxtb035TransacaoConta transacao) throws ParserConfigurationException, SAXException, IOException {
		MtxTransacaoContaEntrada mtxTransacao = new MtxTransacaoContaEntrada();
		mtxTransacao.setDataReferencia(DataUtil.formatar(transacao.getDataReferencia(), DataUtil.FORMATO_DATA_PADRAO_BR));
		mtxTransacao.setNuNsuTransacaoRefMtx016(transacao.getNuNsuTransacao());
		mtxTransacao.setNoCanal(transacao.getMtxtb004Canal().getNoCanal());
		mtxTransacao.setNoServico(transacao.getMtxtb001Servico().getNoServico());
		mtxTransacao.setNuCanal(transacao.getNumeroCanal());
		mtxTransacao.setNuServico(transacao.getNumeroServico());
		mtxTransacao.setAgencia(transacao.getNumeroUnidade());
		mtxTransacao.setConta(transacao.getNumeroConta());
		mtxTransacao.setDv(transacao.getNuDvConta());
		mtxTransacao.setIndicadorConta(transacao.getIndicadorConta());

		BuscadorTextoXml buscadorTextoXml = new BuscadorTextoXml(transacao.getMtxtb016IteracaoCanal().getDeRecebimento());

		mtxTransacao.setLinhaDigitavel(
				buscadorTextoXml.xpathTexto("*[local-name()='PAGAMENTO_BOLETO_ENTRADA']/PAGAMENTO_BOLETO/INFORMACOES_BOLETO/LINHA_DIGITAVEL"));
		mtxTransacao.setValor(buscadorTextoXml.xpathTexto("*[local-name()='PAGAMENTO_BOLETO_ENTRADA']/PAGAMENTO_BOLETO/INFORMACOES_BOLETO/VALOR_PAGO"));
		mtxTransacao.setNsuCanal(Long.valueOf(buscadorTextoXml.xpathTexto("*[local-name()='PAGAMENTO_BOLETO_ENTRADA']/HEADER/CANAL/NSU")));
		return mtxTransacao;
	}

	private Mtxtb035TransacaoConta preencherMtxtb035TransacaoConta(MtxTransacaoConta consulta) throws IllegalAccessException, InvocationTargetException {
		Mtxtb035TransacaoConta mtxtb035TransacaoConta = new Mtxtb035TransacaoConta();
		Mtxtb001Servico mtxtb001Servico = new Mtxtb001Servico();
		Mtxtb004Canal mtxtb004Canal = new Mtxtb004Canal();

		BeanUtils.copyProperties(mtxtb035TransacaoConta, consulta);

		if (null != consultaMocked.getServico() && null != consultaMocked.getCanal()) {
			BeanUtils.copyProperties(mtxtb001Servico, consultaMocked.getServico());
			BeanUtils.copyProperties(mtxtb004Canal, consultaMocked.getCanal());
			mtxtb035TransacaoConta.setMtxtb001Servico(mtxtb001Servico);
			mtxtb035TransacaoConta.setMtxtb004Canal(mtxtb004Canal);
		}

		return mtxtb035TransacaoConta;
	}
}
