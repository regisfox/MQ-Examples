package br.gov.caixa.simtx.core.canal;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.apache.log4j.Logger;

import br.gov.caixa.simtx.persistencia.atualizador.dados.AtualizadorDadosTransacao;
import br.gov.caixa.simtx.persistencia.cache.agendamento.FornecedorDadosAgendamento;
import br.gov.caixa.simtx.persistencia.constante.AcaoRetorno;
import br.gov.caixa.simtx.persistencia.constante.Constantes;
import br.gov.caixa.simtx.persistencia.constante.MensagemRetorno;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb003ServicoTarefa;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb004Canal;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb006Mensagem;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb008MeioEntrada;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb011VersaoServico;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb014Transacao;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb016IteracaoCanal;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb032MarcaConta;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb035TransacaoConta;
import br.gov.caixa.simtx.util.ParametrosAdicionais;
import br.gov.caixa.simtx.util.SimtxConfig;
import br.gov.caixa.simtx.util.exception.ServicoException;
import br.gov.caixa.simtx.util.gerenciador.GerenciadorTransacao;
import br.gov.caixa.simtx.util.gerenciador.TratadorDeExcecao;
import br.gov.caixa.simtx.util.gerenciador.cache.RepositorioArquivo;
import br.gov.caixa.simtx.util.gerenciador.servico.ProcessadorServicos;
import br.gov.caixa.simtx.util.mensagem.GerenciadorFilasMQ;
import br.gov.caixa.simtx.util.to.DadosTransacaoComuns;
import br.gov.caixa.simtx.util.to.TarefasServicoResposta;
import br.gov.caixa.simtx.util.xml.BuscadorTextoXml;
import br.gov.caixa.simtx.util.xml.DadosBarramento;
import br.gov.caixa.simtx.util.xml.GeradorPassosMigrado;
import br.gov.caixa.simtx.util.xml.ParametroXsl;
import br.gov.caixa.simtx.util.xml.Resposta;
import br.gov.caixa.simtx.util.xml.TransformadorXsl;

/**
 * Realiza o processamento de mensagem e integracoes basicas do fluxo core
 * 
 * @author rsfagundes
 *
 */
@Stateless
public class ProcessadorCore extends GerenciadorTransacao implements ProcessadorServicos {

	private static final long serialVersionUID = 5943938350651731183L;

	private static final Logger logger = Logger.getLogger(ProcessadorCore.class);

	protected DadosBarramento dadosBarramento;

	@Inject
	protected FornecedorDadosAgendamento fornecedorDadosAgendamento;

	@Inject
	protected GerenciadorFilasMQ execucaoMq;

	@Inject
	protected GeradorPassosMigrado geradorPassosMigrado;

	@Inject
	protected TratadorDeExcecao tratadorDeExcecao;

	@Inject
	protected SimtxConfig simtxConfig;

	@Inject
	private RepositorioArquivo repositorioArquivo;

	@Inject
	private AtualizadorDadosTransacao atualizadorDadosTransacao;

	
	@Override
	public void processar(ParametrosAdicionais parametrosAdicionais) {
		logger.info(" ==== Processo Core Iniciado ==== ");

		Mtxtb014Transacao transacao = null;
		Mtxtb016IteracaoCanal iteracaoCanal = null;
		Mtxtb011VersaoServico servico = null;
		Mtxtb004Canal canal = null;

		try {
			this.dadosBarramento = new DadosBarramento();
			this.dadosBarramento.escrever(parametrosAdicionais.getXmlMensagem());

			BuscadorTextoXml buscador = new BuscadorTextoXml(parametrosAdicionais.getXmlMensagem());
			Long codigoServico = Long.parseLong(buscador.xpathTexto("/*[1]/HEADER/SERVICO/CODIGO"));
			Integer versaoServico = Integer.parseInt(buscador.xpathTexto("/*[1]/HEADER/SERVICO/VERSAO"));
			servico = buscarServico(codigoServico, versaoServico);
			canal = buscarCanal(buscador);

			transacao = salvarTransacao(buscador, canal);
			iteracaoCanal = salvarIteracaoCanal(buscador, parametrosAdicionais.getXmlMensagem(), transacao);
			salvarTransacaoServico(transacao, servico);

			int nuMeioEntrada = Integer.parseInt(buscador.xpathTexto("/*[1]/HEADER/MEIOENTRADA"));
			Mtxtb008MeioEntrada meioEntrada = new Mtxtb008MeioEntrada();
			meioEntrada.setNuMeioEntrada(nuMeioEntrada);

			this.validadorRegrasNegocio.validarRegras(parametrosAdicionais.getXmlMensagem(), canal, meioEntrada, servico);

			meioEntrada = this.fornecedorDados.buscarMeioEntrada(nuMeioEntrada);
			//TODO:TESTE RETOMADA RETIRAR
			//  Mtxtb006Mensagem mensagemErroInterno = this.fornecedorDados.buscarMensagem(MensagemRetorno.ERRO_INTERNO);
			//	incluir para teste ServicoException	throw new ServicoException(mensagemErroInterno, Constantes.ORIGEM_SIMTX); 
			processarEntradaSaida(transacao, iteracaoCanal, canal, servico, meioEntrada, parametrosAdicionais);

		} catch (ServicoException se) {
			logger.error(se.getMensagem());
			parametrosAdicionais.setDadosBarramento(dadosBarramento);
			DadosTransacaoComuns dadosTransacaoComuns = new DadosTransacaoComuns(servico, transacao, iteracaoCanal);
			this.tratadorDeExcecao.tratarExcecaoRetomada(dadosTransacaoComuns, se, parametrosAdicionais, true);		
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			Mtxtb006Mensagem mensagemErroInterno = this.fornecedorDados.buscarMensagem(MensagemRetorno.ERRO_INTERNO);
			ServicoException se = new ServicoException(mensagemErroInterno, Constantes.ORIGEM_SIMTX);
			parametrosAdicionais.setDadosBarramento(dadosBarramento);
			DadosTransacaoComuns dadosTransacaoComuns = new DadosTransacaoComuns(servico, transacao, iteracaoCanal);
			this.tratadorDeExcecao.tratarExcecaoRetomada(dadosTransacaoComuns, se, parametrosAdicionais, true);		
		}
			logger.info(" ==== Processo Core Finalizado ==== ");
	}
	/**
	 * 
	 * @param transacao
	 * @param iteracaoCanal
	 * @param canal
	 * @param servicoOrigem
	 * @param servicoAgendamento
	 * @param dadosBarramento
	 */
	private void processarEntradaSaida(Mtxtb014Transacao transacao, Mtxtb016IteracaoCanal iteracaoCanal, Mtxtb004Canal canal,
			Mtxtb011VersaoServico servicoOrigem, Mtxtb008MeioEntrada meioEntrada, ParametrosAdicionais parametrosAdicionais) {

		String xmlEntradaSibar = null;
		String xmlSaidaSibar = null;

		TarefasServicoResposta tarefasServicoResposta = new TarefasServicoResposta();
		List<Mtxtb003ServicoTarefa> listaTarefas = null;
		
		try {
			listaTarefas = this.gerenciadorTarefas.carregarTarefasServico(servicoOrigem, canal, meioEntrada);

			logger.info("Preparando xml de requisicao para enviar ao Sibar");
			xmlEntradaSibar = transformarXml(servicoOrigem, transacao, canal);
			xmlEntradaSibar = this.geradorPassosMigrado.gerarPassos(xmlEntradaSibar, servicoOrigem, listaTarefas);
			this.dadosBarramento.escrever(xmlEntradaSibar);

			xmlSaidaSibar = this.execucaoMq.executar(xmlEntradaSibar, servicoOrigem.getMtxtb001Servico());
			this.dadosBarramento.escrever(xmlSaidaSibar);

			logger.info("Preparando xml de resposta para enviar ao Canal");
			tarefasServicoResposta = this.gerenciadorTarefas.montarTarefasResposta(listaTarefas, servicoOrigem, transacao, this.dadosBarramento, xmlSaidaSibar);
			String xmlSaidaCanal = transformarXml(servicoOrigem.getDeXsltResposta(), transacao, tarefasServicoResposta.getResposta());
			logger.info("Enviando resposta para Canal");
			enviarRespostaCanal(xmlSaidaCanal, parametrosAdicionais);
			
			this.gerenciadorTarefas.validarTarefasServicoResposta(tarefasServicoResposta);
			
		

			logger.info("Atualizando informacoes das tarefas e transacao");
			BigDecimal situacaoTransacao = this.validadorRegrasNegocio.situacaoFinalTransacao(servicoOrigem);
			transacao = atualizaStatusTransacao(transacao, situacaoTransacao);
			iteracaoCanal = montaMtxtb016IteracaoCanalTansacao(iteracaoCanal, transacao, xmlSaidaCanal);
			Mtxtb035TransacaoConta transacaoConta = montarDadosTransacaoConta(situacaoTransacao, transacao, iteracaoCanal, canal, servicoOrigem, xmlSaidaCanal);
			
			boolean statusAtualizacaoTransacao = this.atualizadorDadosTransacao.atualizarDadosTransacaoCore(transacao, tarefasServicoResposta.getListaTransacaoTarefas(), iteracaoCanal, transacaoConta);
			
			direcionaProcessadorMensagemTransacao(true, statusAtualizacaoTransacao, transacao, servicoOrigem, dadosBarramento, xmlSaidaSibar);

		} catch (ServicoException se) {
			logger.error(se.getMensagem());
			parametrosAdicionais.setDadosBarramento(dadosBarramento);
			parametrosAdicionais.setXmlMensagem(xmlSaidaSibar);
			DadosTransacaoComuns dadosTransacaoComuns = new DadosTransacaoComuns(listaTarefas, servicoOrigem, transacao,tarefasServicoResposta.getListaTransacaoTarefas(), iteracaoCanal);
			this.tratadorDeExcecao.tratarExcecaoRetomadaDesfazimentoTransacao(dadosTransacaoComuns, se, parametrosAdicionais, true);		
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			Mtxtb006Mensagem mensagemErroInterno = this.fornecedorDados.buscarMensagem(MensagemRetorno.ERRO_INTERNO);
			ServicoException se = new ServicoException(mensagemErroInterno, Constantes.ORIGEM_SIMTX);
			parametrosAdicionais.setDadosBarramento(dadosBarramento);
			parametrosAdicionais.setXmlMensagem(xmlSaidaSibar);
			DadosTransacaoComuns dadosTransacaoComuns = new DadosTransacaoComuns(listaTarefas, servicoOrigem, transacao,tarefasServicoResposta.getListaTransacaoTarefas(), iteracaoCanal);
			this.tratadorDeExcecao.tratarExcecaoRetomadaDesfazimentoTransacao(dadosTransacaoComuns, se, parametrosAdicionais, true);
		}
	}

	/**
	 * Transforma o xml em outro a partir do xslt.
	 * 
	 * @param xslt
	 * @param transacao
	 * @param canal
	 * @param mensagem
	 * @return
	 * @throws ServicoException
	 */
	private String transformarXml(Mtxtb011VersaoServico versaoServico, Mtxtb014Transacao transacao, Mtxtb004Canal canal) throws ServicoException {
		logger.info("inicializando tranformacao xml");
		try {
			List<ParametroXsl> parametrosNovos = new ArrayList<>();
			parametrosNovos.add(new ParametroXsl(ParametroXsl.XSL_PARAM_NSU_SIMTX, String.valueOf(transacao.getNuNsuTransacao())));
			parametrosNovos.add(new ParametroXsl(ParametroXsl.XSL_PARAM_REDE_TRANSMISSORA, String.valueOf(canal.getNuRedeTransmissora())));
			parametrosNovos.add(new ParametroXsl(ParametroXsl.XSL_PARAM_SEGMENTO, String.valueOf(canal.getNuSegmento())));

			StringBuilder funcionalidades = new StringBuilder();
			List<Mtxtb032MarcaConta> listaServico = this.fornecedorDados
					.buscarMarcasPorServico(versaoServico.getId().getNuServico001());
			if (!listaServico.isEmpty()) {
				for (Mtxtb032MarcaConta servico : listaServico) {
					funcionalidades.append(
							"<FUNCIONALIDADE_ESPECIAL>" + servico.getCoMarcaConta() + "</FUNCIONALIDADE_ESPECIAL>");
				}
				parametrosNovos.add(new ParametroXsl(ParametroXsl.XSL_PARAM_FUNCIONALIDADE, funcionalidades.toString()));
			}
			
			ParametroXsl[] pArr = new ParametroXsl[parametrosNovos.size()];

			String caminhoXls = this.simtxConfig.getCaminhoXslt() + versaoServico.getDeXsltRequisicao();
			String arquivoXsl = this.repositorioArquivo.recuperarArquivo(caminhoXls);
			
			if(arquivoXsl.contains(Constantes.TAG_TRANSACAO_ORIGEM) && transacao.getNuNsuTransacaoPai() != 0) {
				this.dadosBarramento.escrever(recuperarTransacaoOrigemNoDadosBarramento(transacao.getNuNsuTransacaoPai()));
			}
			
			return new TransformadorXsl().transformar(this.dadosBarramento.getDadosLeitura(), arquivoXsl, parametrosNovos.toArray(pArr));
		} 
		catch (Exception e) {
			logger.error(e);
			Mtxtb006Mensagem mensagemErroInterno = this.fornecedorDados.buscarMensagem(MensagemRetorno.ERRO_INTERNO);
			throw new ServicoException(mensagemErroInterno, Constantes.ORIGEM_SIMTX);
		}
	}
	
	/**
	 * Transforma o xml em outro a partir do xslt.
	 * 
	 * @param xslt
	 * @param transacao
	 * @param canal
	 * @param mensagem
	 * @return
	 * @throws ServicoException
	 */
	private String transformarXml(String xslt, Mtxtb014Transacao transacao, Resposta mensagem) throws ServicoException {
		logger.info("inicializando tranformacao xml");
		try {
			List<ParametroXsl> parametrosNovos = new ArrayList<>();
			parametrosNovos.add(new ParametroXsl(ParametroXsl.XSL_PARAM_NSU_SIMTX, String.valueOf(transacao.getNuNsuTransacao())));
			parametrosNovos.add(new ParametroXsl(ParametroXsl.XSL_PARAM_COD_RETORNO, mensagem.getCodigo()));
			parametrosNovos.add(new ParametroXsl(ParametroXsl.XSL_PARAM_ACAO_RETORNO, AcaoRetorno.recuperarAcao(mensagem.getIcTipoMensagem()).getRotulo()));
			parametrosNovos.add(new ParametroXsl(ParametroXsl.XSL_PARAM_ORIGEM_RETORNO, mensagem.getOrigem()));
			parametrosNovos.add(new ParametroXsl(ParametroXsl.XSL_PARAM_MENSAGEM_NEGOCIAL, mensagem.getMensagemNegocial()));
			parametrosNovos.add(new ParametroXsl(ParametroXsl.XSL_PARAM_MENSAGEM_TECNICA, mensagem.getMensagemTecnica()));
			ParametroXsl[] pArr = new ParametroXsl[parametrosNovos.size()];
			
			String caminhoXls = this.simtxConfig.getCaminhoXslt() + xslt;
			String arquivoXsl = this.repositorioArquivo.recuperarArquivo(caminhoXls);
			return new TransformadorXsl().transformar(this.dadosBarramento.getDadosLeitura(), arquivoXsl, parametrosNovos.toArray(pArr));
		} 
		catch (Exception e) {
			logger.error(e);
			Mtxtb006Mensagem mensagemErroInterno = this.fornecedorDados.buscarMensagem(MensagemRetorno.ERRO_INTERNO);
			throw new ServicoException(mensagemErroInterno, Constantes.ORIGEM_SIMTX);
		}
	}


}
