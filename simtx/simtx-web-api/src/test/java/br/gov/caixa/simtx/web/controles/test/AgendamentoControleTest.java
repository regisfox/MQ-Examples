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
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;
import org.xml.sax.SAXException;

import com.google.gson.Gson;

import br.gov.caixa.simtx.persistencia.agendamento.entidade.Mtxtb034TransacaoAgendamento;
import br.gov.caixa.simtx.persistencia.cache.agendamento.test.FornecedorDadosAgendamentoMock;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb001Servico;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb004Canal;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb016IteracaoCanal;
import br.gov.caixa.simtx.util.data.DataUtil;
import br.gov.caixa.simtx.util.xml.BuscadorTextoXml;
import br.gov.caixa.simtx.web.beans.MtxCanal;
import br.gov.caixa.simtx.web.beans.MtxCancelamentoAgendamentoSaida;
import br.gov.caixa.simtx.web.beans.MtxMensagem;
import br.gov.caixa.simtx.web.beans.MtxServico;
import br.gov.caixa.simtx.web.beans.MtxTransacaoAgendamento;
import br.gov.caixa.simtx.web.beans.agendamentos.naoefetivados.MtxAgendamentoNaoEfetivadoSaida;
@RunWith(MockitoJUnitRunner.class)
public class AgendamentoControleTest {

	private static final Logger logger = Logger.getLogger(AgendamentoControleTest.class);

	@InjectMocks
	private MtxTransacaoAgendamento consulta;
	@InjectMocks
	private MtxTransacaoAgendamento consultaAgendamentoNaoEfetivado;

	FornecedorDadosAgendamentoMock fornecedorDadosAgendamentoMock;

	@Before
	public void inicializarDados() {
		fornecedorDadosAgendamentoMock = new FornecedorDadosAgendamentoMock();

		consulta = new MtxTransacaoAgendamento();
		consulta.setNuUnidade(625);
		consulta.setNsuSimtxAgendamento(1L);
		consulta.setDtEfetivacao(new Date());

		consultaAgendamentoNaoEfetivado = new MtxTransacaoAgendamento();
		consultaAgendamentoNaoEfetivado.setNuUnidade(625);

	}

	@Test
	public void consultaAgendamentos() throws ParserConfigurationException, SAXException, IOException, IllegalAccessException, InvocationTargetException {
		logger.info("Inicio consultaAgendamento");
		Assert.assertNotNull(consultaAgendamento(null, consulta));
		logger.info("Termino consultaAgendamento");
	}

	@Test
	public void consultaAgendamentoNaoEfetivados() {
		logger.info("Inicio consultaAgendamentoNaoEfetivados");
		consultaAgendamentoNaoEfetivados(null, consultaAgendamentoNaoEfetivado);
		logger.info("Termino consultaAgendamentoNaoEfetivados");
	}

	public List<MtxCancelamentoAgendamentoSaida> consultaAgendamento(@Context HttpServletRequest httpRequest, MtxTransacaoAgendamento consulta)
			throws ParserConfigurationException, SAXException, IOException, IllegalAccessException, InvocationTargetException {

		List<MtxCancelamentoAgendamentoSaida> listaTransacao = new ArrayList<>();
		for (Mtxtb034TransacaoAgendamento transacao : fornecedorDadosAgendamentoMock
				.buscaTransacoesAgendamento(preencherMtxtb034TransacaoAgendamento(consulta))) {
			listaTransacao.add(preencherMtxCancelamentoAgendamentoSaida(transacao));
		}
		Gson gson = new Gson();
		logger.info("Entrada Consulta: " + consulta.toString());
		logger.info("Saida Consulta Lista Agendamentos: " + gson.toJson(listaTransacao));
		return listaTransacao;
	}

	public void consultaAgendamentoNaoEfetivados(@Context HttpServletRequest httpRequest, MtxTransacaoAgendamento consulta) {
		try {
			final List<MtxAgendamentoNaoEfetivadoSaida> listaTransacao = new ArrayList<>();

			final List<Mtxtb034TransacaoAgendamento> agendamentosList = fornecedorDadosAgendamentoMock
					.buscaTransacoesAgendamentoNaoEfetivados(preencherMtxtb034TransacaoAgendamento(consulta));
			if (null != agendamentosList) {
				for (final Mtxtb034TransacaoAgendamento transacao : agendamentosList) {
					listaTransacao.add(preencherMtxAgendamentoNaoEfetivadoSaida(transacao));
				}
			}
			Gson gson = new Gson();
			logger.info("Dados Consulta: " + consulta.toString());
			logger.info("Lista Não Efetivados: " + gson.toJson(listaTransacao));
		} catch (Exception e) {
			logger.error(e);
		}

	}

	private MtxAgendamentoNaoEfetivadoSaida preencherMtxAgendamentoNaoEfetivadoSaida(Mtxtb034TransacaoAgendamento transacao)
			throws IllegalAccessException, InvocationTargetException, ParserConfigurationException, SAXException, IOException {
		MtxAgendamentoNaoEfetivadoSaida dados = new MtxAgendamentoNaoEfetivadoSaida();
		BeanUtils.copyProperties(dados, transacao);
		dados.setValorTransacao(transacao.getValorTransacao() == null ? "" : transacao.getValorTransacao().toString());
		dados.setDataAgendamento(DataUtil.formatar(transacao.getDtReferencia(), DataUtil.FORMATO_DATA_PADRAO_BR));
		dados.setDataEfetivacao(DataUtil.formatar(transacao.getDtEfetivacao(), DataUtil.FORMATO_DATA_PADRAO_BR));
		dados.setCanal(new MtxCanal());
		dados.setServico(new MtxServico());

		if (null != transacao.getMtxtb004Canal() && null != transacao.getMtxtb001Servico()) {
			BeanUtils.copyProperties(dados.getCanal(), transacao.getMtxtb004Canal());
			BeanUtils.copyProperties(dados.getServico(), transacao.getMtxtb001Servico());
		}

		if (null != transacao.getDeXmlAgendamento()) {
			BuscadorTextoXml buscadorTextoXml = new BuscadorTextoXml(transacao.getDeXmlAgendamento());
			dados.setLinhaDigitavel(
					buscadorTextoXml.xpathTexto("*[local-name()='PAGAMENTO_BOLETO_ENTRADA']/PAGAMENTO_BOLETO/INFORMACOES_BOLETO/LINHA_DIGITAVEL"));
			dados.getCanal().setNsuCanal(Long.valueOf(buscadorTextoXml.xpathTexto("*[local-name()='PAGAMENTO_BOLETO_ENTRADA']/HEADER/CANAL/NSU")));
		}

		Mtxtb016IteracaoCanal iteracaoCanal = fornecedorDadosAgendamentoMock.buscaUltimoMtxtb016IteracaoCanal(dados.getNuNsuTransacaoAgendamento());

		if (null != iteracaoCanal && null != iteracaoCanal.getDeRetorno()) {
			BuscadorTextoXml buscadorTextoXml = new BuscadorTextoXml(iteracaoCanal.getDeRetorno());
			dados.setMensagem(new MtxMensagem());
			dados.getMensagem().setCodigoMensagem(buscadorTextoXml.xpathTexto("*[local-name()='PAGAMENTO_BOLETO_SAIDA']/HEADER/COD_RETORNO"));
			dados.getMensagem().setMensagemNegocial(buscadorTextoXml.xpathTexto("*[local-name()='PAGAMENTO_BOLETO_SAIDA']/HEADER/MENSAGENS/NEGOCIAL"));
			dados.setNuNsuTransacaoSimtx(iteracaoCanal.getMtxtb014Transacao().getNuNsuTransacao());
		}

		return dados;
	}

	private MtxCancelamentoAgendamentoSaida preencherMtxCancelamentoAgendamentoSaida(Mtxtb034TransacaoAgendamento transacao)
			throws ParserConfigurationException, SAXException, IOException, IllegalAccessException, InvocationTargetException {
		MtxCancelamentoAgendamentoSaida dados = new MtxCancelamentoAgendamentoSaida();
		BeanUtils.copyProperties(dados, transacao);
		dados.setValorTransacao(transacao.getValorTransacao() == null ? "" : transacao.getValorTransacao().toString());
		dados.setDataAgendamento(DataUtil.formatar(transacao.getDtReferencia(), DataUtil.FORMATO_DATA_PADRAO_BR));
		dados.setDataEfetivacao(DataUtil.formatar(transacao.getDtEfetivacao(), DataUtil.FORMATO_DATA_PADRAO_BR));

		dados.setCanal(new MtxCanal());
		dados.getCanal().setNuCanal(transacao.getMtxtb004Canal().getNuCanal());
		dados.getCanal().setNoCanal(transacao.getMtxtb004Canal().getNoCanal());

		dados.setServico(new MtxServico());
		dados.getServico().setNuServico(transacao.getMtxtb001Servico().getNuServico());
		dados.getServico().setNoServico(transacao.getMtxtb001Servico().getNoServico());

		if (null != transacao.getDeXmlAgendamento()) {
			BuscadorTextoXml buscadorTextoXml = new BuscadorTextoXml(transacao.getDeXmlAgendamento());
			dados.setLinhaDigitavel(
					buscadorTextoXml.xpathTexto("*[local-name()='PAGAMENTO_BOLETO_ENTRADA']/PAGAMENTO_BOLETO/INFORMACOES_BOLETO/LINHA_DIGITAVEL"));
			dados.getCanal().setNsuCanal(Long.valueOf(buscadorTextoXml.xpathTexto("*[local-name()='PAGAMENTO_BOLETO_ENTRADA']/HEADER/CANAL/NSU")));
		}
		return dados;
	}

	private Mtxtb034TransacaoAgendamento preencherMtxtb034TransacaoAgendamento(MtxTransacaoAgendamento consulta)
			throws IllegalAccessException, InvocationTargetException {
		Mtxtb034TransacaoAgendamento mtxtb034TransacaoAgendamento = new Mtxtb034TransacaoAgendamento();
		Mtxtb001Servico mtxtb001Servico = new Mtxtb001Servico();
		Mtxtb004Canal mtxtb004Canal = new Mtxtb004Canal();

		BeanUtils.copyProperties(mtxtb034TransacaoAgendamento, consulta);

		if (null != consulta.getServico() && null != consulta.getCanal()) {
			BeanUtils.copyProperties(mtxtb001Servico, consulta.getServico());
			BeanUtils.copyProperties(mtxtb004Canal, consulta.getCanal());
			mtxtb034TransacaoAgendamento.setMtxtb001Servico(mtxtb001Servico);
			mtxtb034TransacaoAgendamento.setMtxtb004Canal(mtxtb004Canal);
		}

		return mtxtb034TransacaoAgendamento;
	}
}
