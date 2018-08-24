package br.gov.caixa.simtx.cancelamento.processador;

import java.io.IOException;
import java.io.Serializable;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.xml.sax.SAXException;

import br.gov.caixa.simtx.cancelamento.beans.MtxTransacaoContaEntrada;
import br.gov.caixa.simtx.cancelamento.beans.MtxTransacaoContaSaida;
import br.gov.caixa.simtx.cancelamento.beans.TransacaoCancelamento;
import br.gov.caixa.simtx.cancelamento.util.ConstantesCancelamento;
import br.gov.caixa.simtx.persistencia.atualizador.dados.AtualizadorDadosTransacao;
import br.gov.caixa.simtx.persistencia.constante.Constantes;
import br.gov.caixa.simtx.persistencia.constante.MensagemRetorno;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb003ServicoTarefa;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb004Canal;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb006Mensagem;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb011VersaoServico;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb014Transacao;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb016IteracaoCanal;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb016IteracaoCanalPK;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb035TransacaoConta;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb036TransacaoAuditada;
import br.gov.caixa.simtx.util.ParametrosAdicionais;
import br.gov.caixa.simtx.util.SimtxConfig;
import br.gov.caixa.simtx.util.data.DataUtil;
import br.gov.caixa.simtx.util.exception.ServicoException;
import br.gov.caixa.simtx.util.gerenciador.GerenciadorTransacao;
import br.gov.caixa.simtx.util.gerenciador.TratadorDeExcecao;
import br.gov.caixa.simtx.util.gerenciador.ValidadorRegrasNegocio;
import br.gov.caixa.simtx.util.gerenciador.cache.RepositorioArquivo;
import br.gov.caixa.simtx.util.mensagem.GerenciadorFilasMQ;
import br.gov.caixa.simtx.util.to.DadosTransacaoComuns;
import br.gov.caixa.simtx.util.to.TarefasServicoResposta;
import br.gov.caixa.simtx.util.xml.DadosBarramento;
import br.gov.caixa.simtx.util.xml.GeradorPassosMigrado;
import br.gov.caixa.simtx.util.xml.ParametroXsl;
import br.gov.caixa.simtx.util.xml.TransformadorXsl;

@Stateless
public class ProcessadorCancelamentoPagamento extends GerenciadorTransacao implements Serializable {	
	
	private static final long serialVersionUID = 1L;
	
	private static final Logger logger = Logger.getLogger(ProcessadorCancelamentoPagamento.class);
	
	@Inject
	private SimtxConfig simtxConfig;
	
	private DadosBarramento dadosBarramento;
	
	@Inject
	private GeradorPassosMigrado geradorPassosMigrado;
	
	@Inject
	private ValidadorRegrasNegocio validadorRegrasNegocio;
			
	@Inject
	private GerenciadorFilasMQ execucaoMq;
	
	@Inject
	private RepositorioArquivo repositorioArquivo;
	
	@Inject
	private AtualizadorDadosTransacao atualizadorDadosTransacao;
	
	@Inject
	private TratadorDeExcecao tratadorDeExcecao;
	
	/**
	 * Processa os Cancelamentos de Pagamento de Boleto.
	 * 
	 * @param cancelamentos
	 * @return
	 */
	public List<MtxTransacaoContaSaida> realizarCancelamentos(List<MtxTransacaoContaEntrada> cancelamentos) {
		logger.info(" ==== Processo Cancelamento Pagamento Iniciado ==== ");
		Mtxtb014Transacao transacao = new Mtxtb014Transacao();
		ParametrosAdicionais parametrosAdicionais = new ParametrosAdicionais();
		Mtxtb004Canal canal = new Mtxtb004Canal();
		canal.setNuCanal(Constantes.CODIGO_CANAL_SIMTX);
		
		List <MtxTransacaoContaSaida> transacaoContas = new ArrayList<>();
		Mtxtb016IteracaoCanal iteracaoCanal = null;
		Mtxtb011VersaoServico servicoCancelamento = null;
		for (MtxTransacaoContaEntrada mtxTransacaoConta : cancelamentos) {
			try {
				this.dadosBarramento = new DadosBarramento();
				TransacaoCancelamento transacaoCancelamento = obterTransacaoCancelamentoFromXML(mtxTransacaoConta);
				servicoCancelamento = buscarServico(
						ConstantesCancelamento.CODIGO_SERVICO_CANCELAMENTO,
						ConstantesCancelamento.VERSAO_SERVICO_CANCELAMENTO);
				
				Mtxtb016IteracaoCanalPK mtxtb016IteracaoCanalPK = new Mtxtb016IteracaoCanalPK();
				mtxtb016IteracaoCanalPK.setNuNsuTransacao014(transacaoCancelamento.getNsuOrigem());
				iteracaoCanal = new Mtxtb016IteracaoCanal();
				iteracaoCanal.setId(mtxtb016IteracaoCanalPK);
				iteracaoCanal = this.fornecedorDados.buscarIteracaoCanalPorPK(iteracaoCanal);
				this.dadosBarramento.escrever(iteracaoCanal.getDeRecebimento());
				this.dadosBarramento.escrever(entradaParaXml(mtxTransacaoConta));
				
				transacao = salvarTransacao(canal, transacaoCancelamento);
				iteracaoCanal = salvarIteracaoCanal(transacaoCancelamento, transacao, mtxTransacaoConta);
				salvarTransacaoServico(transacao, servicoCancelamento);
				
				this.validadorRegrasNegocio.validarParametrosDoCanal(servicoCancelamento, canal,
						iteracaoCanal.getDeRecebimento());
				
				MtxTransacaoContaSaida saida = processar(transacao, transacaoCancelamento, iteracaoCanal, servicoCancelamento, canal, mtxTransacaoConta, parametrosAdicionais);
				transacaoContas.add(saida);
			} 
			catch (Exception e) {
				logger.error(e.getMessage());
				Mtxtb006Mensagem mensagemErroInterno = this.fornecedorDados.buscarMensagem(MensagemRetorno.ERRO_INTERNO);
				ServicoException se = new ServicoException(mensagemErroInterno, Constantes.ORIGEM_SIMTX);
				parametrosAdicionais.setDadosBarramento(dadosBarramento);
				DadosTransacaoComuns dadosTransacaoComuns = new DadosTransacaoComuns(servicoCancelamento, transacao, null, iteracaoCanal);
				this.tratadorDeExcecao.tratarExcecaoRetomada(dadosTransacaoComuns, se, parametrosAdicionais, false);			
				transacaoContas.add(criarTransacaoContaSaida(mtxTransacaoConta, false));
			}
		}
		logger.info(" ==== Processo Cancelamento Finalizado ==== ");
		return transacaoContas;
	}
	
	private MtxTransacaoContaSaida criarTransacaoContaSaida(MtxTransacaoContaEntrada entrada, boolean estado) {
		MtxTransacaoContaSaida saida = new MtxTransacaoContaSaida();
		
		saida.setAgencia(entrada.getAgencia());
		saida.setCodBarras(entrada.getCodBarras());
		saida.setConta(entrada.getConta());
		saida.setDataReferencia(entrada.getDataReferencia());
		saida.setDescricaoServico(entrada.getDescricaoServico());
		saida.setDv(entrada.getDv());
		saida.setNoCanal(entrada.getNoCanal());
		saida.setNoServico(entrada.getNoServico());
		saida.setNuCanal(entrada.getNuCanal());
		saida.setNuNsuTransacaoRefMtx016(entrada.getNuNsuTransacaoRefMtx016());
		saida.setNuServico(entrada.getNuServico());
		saida.setOpProduto(entrada.getOpProduto());
		saida.setSiglaCanal(entrada.getSiglaCanal());
		saida.setValor(entrada.getValor());
		
		saida.setStatusCancelamento(estado);
		return saida;
	}

	/**
	 * Trata o Cancelamento com Assinatura Simples.
	 * 
	 * @param transacao
	 * @param iteracaoCanal
	 * @param canal
	 * @param servicoOrigem
	 * @param servicoCancelamento
	 * @param dadosBarramento
	 */
	private MtxTransacaoContaSaida processar(Mtxtb014Transacao transacao, TransacaoCancelamento transacaoCancelamento,
			Mtxtb016IteracaoCanal iteracaoCanal, Mtxtb011VersaoServico servicoCancelamento, Mtxtb004Canal canal,
			MtxTransacaoContaEntrada mtxTransacaoConta, ParametrosAdicionais parametrosAdicionais) {
		TarefasServicoResposta tarefasServicoResposta = null;
		String xmlSaidaSibar = null;
		MtxTransacaoContaSaida saida = null;
		try {
			List<Mtxtb003ServicoTarefa> listaTarefas = this.gerenciadorTarefas
					.carregarTarefasServico(servicoCancelamento, canal, null);
			
			logger.info("Preparando xml de requisicao para enviar ao Sibar");
			String xmlEntradaSibar = transformarXml(servicoCancelamento.getDeXsltRequisicao(), transacao, transacaoCancelamento);
			xmlEntradaSibar = this.geradorPassosMigrado.gerarPassos(xmlEntradaSibar, servicoCancelamento, listaTarefas);
			
			xmlSaidaSibar = this.execucaoMq.executar(xmlEntradaSibar, servicoCancelamento.getMtxtb001Servico());
			this.dadosBarramento.escrever(xmlSaidaSibar);
			logger.info("inicializando montagem de tarefas");
			tarefasServicoResposta = this.gerenciadorTarefas.montarTarefasResposta(listaTarefas, servicoCancelamento, transacao, dadosBarramento, xmlSaidaSibar);
			this.gerenciadorTarefas.validarTarefasServicoResposta(tarefasServicoResposta);
			logger.info("Atualizando informacoes das tarefas e transacao");
			BigDecimal situacaoTransacao = this.validadorRegrasNegocio.situacaoFinalTransacao(servicoCancelamento);
			transacao = atualizaStatusTransacao(transacao, situacaoTransacao);
			Mtxtb016IteracaoCanal mtxtb016IteracaoCanal = montaMtxtb016IteracaoCanalTansacao(iteracaoCanal, transacao, xmlSaidaSibar);
			Mtxtb035TransacaoConta mtxtb035TransacaoConta = buscarMtxtb035TransacaoConta(mtxTransacaoConta.getNuNsuTransacaoRefMtx016());
			Mtxtb036TransacaoAuditada mtxtb036TransacaoAuditada = montarTransacaoAuditada(mtxTransacaoConta, transacao.getNuNsuTransacao(), servicoCancelamento);
			mtxtb035TransacaoConta.setIcSituacao(2L);
			boolean statusAtualizacaoTransacao = this.atualizadorDadosTransacao.atualizarDadosTransacaoCancelamento(transacao, tarefasServicoResposta.getListaTransacaoTarefas(), mtxtb016IteracaoCanal, mtxtb035TransacaoConta, mtxtb036TransacaoAuditada);
			
			direcionaProcessadorMensagemTransacao(false, statusAtualizacaoTransacao, transacao, servicoCancelamento, dadosBarramento, xmlSaidaSibar);
			saida = criarTransacaoContaSaida(mtxTransacaoConta, true);
		
		}	catch (Exception e) {
			logger.error(e.getMessage());
			Mtxtb006Mensagem mensagemErroInterno = this.fornecedorDados.buscarMensagem(MensagemRetorno.ERRO_INTERNO);
			ServicoException se = new ServicoException(mensagemErroInterno, Constantes.ORIGEM_SIMTX);
			parametrosAdicionais.setDadosBarramento(dadosBarramento);
			DadosTransacaoComuns dadosTransacaoComuns = new DadosTransacaoComuns(servicoCancelamento, transacao, null, iteracaoCanal);
			this.tratadorDeExcecao.tratarExcecaoRetomada(dadosTransacaoComuns, se, parametrosAdicionais, false);				
			return criarTransacaoContaSaida(mtxTransacaoConta, false);
		}
		return saida;
	}
	
	/**
	 * Salva a entidade Transacao.
	 * 
	 * @param buscador
	 * @param canal
	 * @return {@link Mtxtb014Transacao}
	 * @throws Exception
	 */
	private Mtxtb014Transacao salvarTransacao(Mtxtb004Canal canal, TransacaoCancelamento transacaoCancelamento) throws ServicoException {
		try {
			logger.info("Gravando transacao");
			
			Mtxtb014Transacao transacao = new Mtxtb014Transacao();
			transacao.setCoCanalOrigem(String.valueOf(canal.getNuCanal()));
			transacao.setNuNsuTransacaoPai(transacaoCancelamento.getNsuOrigem());
			transacao.setIcSituacao(Constantes.IC_SERVICO_EM_ANDAMENTO);
			transacao.setDhMultiCanal(DataUtil.getDataAtual());
			transacao.setDtReferencia(transacaoCancelamento.getDataReferencia()); 
			transacao.setDhTransacaoCanal(transacaoCancelamento.getDataHoraCanal());
			transacao.setIcEnvio(BigDecimal.ZERO);
			transacao.setIcRetorno(BigDecimal.ZERO);
			transacao.setTsAtualizacao(new Date());
			transacao = this.fornecedorDados.salvarTransacao(transacao);
			logger.info("NSUMTX gerado: " + transacao.getNuNsuTransacao());
			return transacao;
		} 
		catch (Exception e) {
			logger.error("Erro ao gravar a transação: " + e.getMessage());
			Mtxtb006Mensagem mensagemErroInterno = this.fornecedorDados.buscarMensagem(MensagemRetorno.NSU_NAO_GERADO);
			throw new ServicoException(mensagemErroInterno,Constantes.ORIGEM_SIMTX);
		}
	}
	
	/**
	 * Salva a entidade a Transação Auditada.
	 * 
	 */
	public void salvarTransacaoAuditada(MtxTransacaoContaEntrada mtxTransacaoConta, Long nsuTransacaoAuditada,
			Mtxtb011VersaoServico servicoCancelamento) {
		try {
			logger.info("Salvando transacao de Cancelamento");
			this.fornecedorDados.salvarMtxtb036TransacaoAuditada(montarTransacaoAuditada(mtxTransacaoConta, nsuTransacaoAuditada, servicoCancelamento));
		} 
		catch (Exception e) {
			logger.error("Nao foi possivel gravar na tabela TransacaoAuditada", e);
		}
	}
	
	/**
	 * monta informações para a entidade a Transação Auditada.
	 * 
	 */
	public Mtxtb036TransacaoAuditada montarTransacaoAuditada(MtxTransacaoContaEntrada mtxTransacaoConta, Long nsuTransacaoAuditada,
			Mtxtb011VersaoServico servicoCancelamento) {
		Mtxtb036TransacaoAuditada transacaoCancelamento = null;
		try {
			logger.info("inicio montarTransacaoAuditada");
			
			transacaoCancelamento = new Mtxtb036TransacaoAuditada();
			transacaoCancelamento.setNuTransacaoAuditada(nsuTransacaoAuditada);
			transacaoCancelamento.setNuServico(servicoCancelamento.getId().getNuServico001());
			transacaoCancelamento.setNuVersaoServico(servicoCancelamento.getId().getNuVersaoServico());
			
			transacaoCancelamento.setNuTransacaoOrigem(mtxTransacaoConta.getNuNsuTransacaoRefMtx016());
			transacaoCancelamento.setNuCanalOrigem(mtxTransacaoConta.getNuCanal());
			transacaoCancelamento.setNuServicoOrigem(mtxTransacaoConta.getNuServico());
			transacaoCancelamento.setNuVersaoServicoOrigem(servicoCancelamento.getId().getNuVersaoServico());
			
			transacaoCancelamento.setCoUsuario(mtxTransacaoConta.getCodigoUsuario());
			transacaoCancelamento.setCoMaquinaInclusao(mtxTransacaoConta.getCodigoMaquina());
			transacaoCancelamento.setTsInclusao(DataUtil.getDataAtual());
			
			logger.info("fim montarTransacaoAuditada");


		} 
		catch (Exception e) {
			logger.error("Nao foi possivel gravar na tabela TransacaoAuditada", e);
		}
		return transacaoCancelamento;
	}
	
	/**
	 * Salva entidade IteracaoCanal.
	 * 
	 * @param buscador
	 * @param mensagem
	 * @param transacao
	 * @return {@link Mtxtb016IteracaoCanal}
	 * @throws Exception
	 */
	private Mtxtb016IteracaoCanal salvarIteracaoCanal(TransacaoCancelamento transacaoCancelamento, Mtxtb014Transacao transacao, MtxTransacaoContaEntrada mtxTransacaoConta) throws ServicoException {
		try {
			logger.info("Gravando IteracaoCanal");

			Mtxtb016IteracaoCanal iteracao = new Mtxtb016IteracaoCanal();
			iteracao.setMtxtb004Canal(new Mtxtb004Canal());
			iteracao.getMtxtb004Canal().setNuCanal(Long.parseLong(transacao.getCoCanalOrigem()));
			iteracao.setMtxtb014Transacao(transacao);
			iteracao.setTsRecebimentoSolicitacao(DataUtil.getDataAtual());
			iteracao.setTsRetornoSolicitacao(DataUtil.getDataAtual()); 
			iteracao.setId(preencherMtxtb016IteracaoCanalPK(mtxTransacaoConta));
			iteracao.getId().setNuNsuTransacao014(transacao.getNuNsuTransacao());
			iteracao.setDeRecebimento(entradaParaXml(mtxTransacaoConta));
			iteracao.setDtReferencia(transacao.getDtReferencia());
			iteracao.setCodTerminal(transacaoCancelamento.getTerminal());
			return this.fornecedorDados.salvarIteracaoCanal(iteracao);
		} 
		catch (Exception e) {
			logger.error(e);
			Mtxtb006Mensagem mensagemErroInterno = this.fornecedorDados.buscarMensagem(MensagemRetorno.ERRO_GRAVACAO_TRANSACAO);
			throw new ServicoException(mensagemErroInterno,Constantes.ORIGEM_SIMTX);
		}
	}
	
	/**
	 * Cria a mensagem para gravação na tabela IteracaoCanal.
	 * 
	 * @param MensagemCancelamento
	 * @return String Mensagem
	 * @throws IOException 
	 * @throws SAXException 
	 * @throws ParserConfigurationException 
	 * @throws ParseException 
	 */
	private TransacaoCancelamento obterTransacaoCancelamentoFromXML(MtxTransacaoContaEntrada mtxTransacaoConta) {
		
		TransacaoCancelamento transacaoCancelamento = new TransacaoCancelamento();
		transacaoCancelamento.setDataReferencia(DataUtil.getDataAtual());
		transacaoCancelamento.setDataHoraCanal(DataUtil.getDataAtual());
		transacaoCancelamento.setNsuOrigem(mtxTransacaoConta.getNuNsuTransacaoRefMtx016());
		transacaoCancelamento.setSiglaCanal(mtxTransacaoConta.getSiglaCanal());
		transacaoCancelamento.setCodBarras(mtxTransacaoConta.getCodBarras());
		transacaoCancelamento.setCodCanal(mtxTransacaoConta.getNuCanal());
		transacaoCancelamento.setCodServico(mtxTransacaoConta.getNuServico());
		transacaoCancelamento.setValor(mtxTransacaoConta.getValor());
		transacaoCancelamento.setAgencia(mtxTransacaoConta.getAgencia());
		transacaoCancelamento.setOpProduto(mtxTransacaoConta.getOpProduto());
		transacaoCancelamento.setNuConta(mtxTransacaoConta.getConta());
		transacaoCancelamento.setNuDV(mtxTransacaoConta.getDv());
		
		transacaoCancelamento.setIpMaquina(mtxTransacaoConta.getCodigoMaquina());
		transacaoCancelamento.setUsuario(mtxTransacaoConta.getCodigoUsuario());
		return transacaoCancelamento;
	}
	
	private Mtxtb016IteracaoCanalPK preencherMtxtb016IteracaoCanalPK(MtxTransacaoContaEntrada mtxTransacaoConta){
		Mtxtb016IteracaoCanalPK mtxtb016IteracaoCanalPK = new Mtxtb016IteracaoCanalPK();
		mtxtb016IteracaoCanalPK.setNuNsuTransacao014(mtxTransacaoConta.getNuNsuTransacaoRefMtx016());
		return mtxtb016IteracaoCanalPK;
	}
	
	private String entradaParaXml(MtxTransacaoContaEntrada entrada) throws JAXBException {
		JAXBContext jaxbContext = JAXBContext.newInstance(MtxTransacaoContaEntrada.class);
		Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

		jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

		StringWriter w = new StringWriter();
		jaxbMarshaller.marshal(entrada, w);
		return w.toString();
	}

	/**
	 * Transforma o xml em outro a partir do xslt.
	 * 
	 * @param xslt
	 * @param dadosBarramento
	 * @return
	 * @throws ServicoException
	 */
	private String transformarXml(String xslt, Mtxtb014Transacao transacao, TransacaoCancelamento transacaoCancelamento) throws ServicoException {
		try {
			String caminhoXls = this.simtxConfig.getCaminhoXslt() + xslt;
			String arquivoXsl = this.repositorioArquivo.recuperarArquivo(caminhoXls);
			
			if(arquivoXsl.contains(Constantes.TAG_TRANSACAO_ORIGEM) && transacao.getNuNsuTransacaoPai() != 0) {
				this.dadosBarramento.escrever(recuperarTransacaoOrigemNoDadosBarramento(transacaoCancelamento.getNsuOrigem()));
			}
			
			return new TransformadorXsl().transformar(this.dadosBarramento.getDadosLeitura(), arquivoXsl,
					new ParametroXsl(ParametroXsl.XSL_PARAM_NSU_SIMTX, String.valueOf(transacao.getNuNsuTransacao())));
		} 
		catch (Exception e) {
			logger.error(this.mensagemServidor.recuperarMensagem("erro.transformar.xml"), e);
			Mtxtb006Mensagem mensagemErroInterno = this.fornecedorDados.buscarMensagem(MensagemRetorno.ERRO_INTERNO);
			throw new ServicoException(mensagemErroInterno, Constantes.ORIGEM_SIMTX);
		}
	}
	
}

