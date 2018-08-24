package br.gov.caixa.simtx.agendamento.servico;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.apache.log4j.Logger;

import br.gov.caixa.simtx.persistencia.agendamento.entidade.Mtxtb034TransacaoAgendamento;
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
import br.gov.caixa.simtx.util.ParametrosAdicionais;
import br.gov.caixa.simtx.util.SimtxConfig;
import br.gov.caixa.simtx.util.exception.ServicoException;
import br.gov.caixa.simtx.util.gerenciador.GerenciadorTransacao;
import br.gov.caixa.simtx.util.gerenciador.TratadorDeExcecao;
import br.gov.caixa.simtx.util.gerenciador.cache.RepositorioArquivo;
import br.gov.caixa.simtx.util.integrador.sicco.ProcessadorEnvioSicco;
import br.gov.caixa.simtx.util.mensagem.GerenciadorFilasMQ;
import br.gov.caixa.simtx.util.to.DadosTransacaoComuns;
import br.gov.caixa.simtx.util.to.TarefasServicoResposta;
import br.gov.caixa.simtx.util.xml.DadosBarramento;
import br.gov.caixa.simtx.util.xml.GeradorPassosMigrado;
import br.gov.caixa.simtx.util.xml.ParametroXsl;
import br.gov.caixa.simtx.util.xml.Resposta;
import br.gov.caixa.simtx.util.xml.TransformadorXsl;

@Stateless
public class DetalheAgendamento extends GerenciadorTransacao implements Serializable {

	private static final long serialVersionUID = 1L;

	private static final Logger logger = Logger.getLogger(DetalheAgendamento.class);
	
	protected DadosBarramento dadosBarramento;

	@Inject
	protected FornecedorDadosAgendamento fornecedorDadosAgendamento;

	@Inject
	protected GeradorPassosMigrado geradorPassosMigrado;

	@Inject
	protected GerenciadorFilasMQ execucaoMq;

	@Inject
	protected SimtxConfig simtxConfig;

	@Inject
	protected ProcessadorEnvioSicco processadorEnvioSicco;
	
	@Inject 
	private RepositorioArquivo repositorioArquivo;
	
	@Inject
	private TratadorDeExcecao tratadorDeExcecao;
	
	@Inject
	private AtualizadorDadosTransacao atualizadorDadosTransacao; 
	
	
	public void processar(Mtxtb014Transacao transacao, Mtxtb004Canal canal, Mtxtb011VersaoServico servico,
			Mtxtb008MeioEntrada meioEntrada, DadosBarramento dadosBarramento, Mtxtb016IteracaoCanal iteracaoCanal,
			ParametrosAdicionais parametrosAdicionais) throws ServicoException {
		TarefasServicoResposta tarefasServicoResposta = new TarefasServicoResposta();
		try {
			logger.info(" ==== Processo Detalhe Agendamento Iniciado ==== ");
			
			this.dadosBarramento = dadosBarramento;

			Mtxtb034TransacaoAgendamento transacaoAgendamento = null;
			List<Mtxtb003ServicoTarefa> listaTarefas = this.fornecedorDados.buscarTarefasMeioEntrada(
					meioEntrada.getNuMeioEntrada(), servico.getId().getNuServico001(),
					servico.getId().getNuVersaoServico());
	
			Resposta mensagem = null;
			String sistema = this.dadosBarramento.xpathTexto("/BUSDATA/*[2]/DETALHE_AGENDAMENTO_TRANSACAO/SISTEMA_ORIGEM");
			if (sistema.equals(Constantes.ORIGEM_SIMTX)) {
				logger.info("Tag Simtx encontrada");

				String nsu = this.dadosBarramento.xpathTexto("/BUSDATA/*[2]/DETALHE_AGENDAMENTO_TRANSACAO/NSU");
				logger.info("Buscando Agendamento Nsu ["+nsu+"]");
				
				transacaoAgendamento = new Mtxtb034TransacaoAgendamento();
				transacaoAgendamento.setNuNsuTransacaoAgendamento(Long.valueOf(nsu));
				transacaoAgendamento = this.fornecedorDadosAgendamento.buscaTransacaoAgendamentoPorPK(transacaoAgendamento);
	
				if (transacaoAgendamento == null) {
					logger.error("Agendamento Nsu ["+nsu+"] nao encontrado no banco de dados");
					Mtxtb006Mensagem mensagemErroInterno = this.fornecedorDados
							.buscarMensagem(MensagemRetorno.AGENDAMENTO_NAO_ENCONTRADO);
					throw new ServicoException(mensagemErroInterno, Constantes.ORIGEM_SIMTX);
				}
				else {
					Mtxtb006Mensagem mensagemSucesso = this.fornecedorDados .buscarMensagem(MensagemRetorno.SUCESSO);
					mensagem = new Resposta();
					mensagem.setIcTipoMensagem(mensagemSucesso.getIcTipoMensagem());
					mensagem.setCodigo(mensagemSucesso.getCodigoRetorno());
					mensagem.setMensagemNegocial(mensagemSucesso.getDeMensagemNegocial());
					mensagem.setMensagemTecnica(mensagemSucesso.getDeMensagemTecnica());
					mensagem.setOrigem(Constantes.ORIGEM_SIMTX);
				}
				this.dadosBarramento.escrever(transacaoAgendamento.getDeXmlAgendamento());
			} 
			else {
				logger.info("Tag Siaut encontrada");
				List<Mtxtb003ServicoTarefa> listaTarefasNegociais = this.gerenciadorTarefas.carregarTarefasServico(servico, canal, null);
				listaTarefas.addAll(listaTarefasNegociais);
			}
	
			if(!listaTarefas.isEmpty()) {
				 tarefasServicoResposta = executarTarefas(listaTarefas, transacao, servico);
			}else {
				tarefasServicoResposta.setResposta(mensagem);
			}
			
			logger.info("Preparando xml de resposta para enviar ao Canal");
			String xmlSaidaCanal = transformarXml(servico.getDeXsltResposta(), transacao, mensagem);
			enviarRespostaCanal(xmlSaidaCanal,parametrosAdicionais);

			logger.info("Preparando informações das tarefas e transacao");
			BigDecimal situacaoTransacao = this.validadorRegrasNegocio.situacaoFinalTransacao(servico);
			transacao = atualizaStatusTransacao(transacao, situacaoTransacao);
			iteracaoCanal = montaMtxtb016IteracaoCanalTansacao(iteracaoCanal, transacao, xmlSaidaCanal);
			
			boolean statusAtualizacaoTransacao = this.atualizadorDadosTransacao.atualizarDadosTransacaoTarefasIteracoes(transacao, tarefasServicoResposta.getListaTransacaoTarefas(), iteracaoCanal);
			
			direcionaProcessadorMensagemTransacao(false, statusAtualizacaoTransacao, transacao, servico, dadosBarramento, tarefasServicoResposta.getXmlSaidaSibar());
		} 
		catch (ServicoException se) {
			logger.error(se.getMensagem());
			DadosTransacaoComuns dadosTransacaoComuns = new DadosTransacaoComuns(servico, transacao,tarefasServicoResposta.getListaTransacaoTarefas(), iteracaoCanal);
			parametrosAdicionais.setDadosBarramento(this.dadosBarramento);
			this.tratadorDeExcecao.tratarExcecaoRetomada(dadosTransacaoComuns, se, parametrosAdicionais, true);		
		} 
		catch (Exception e) {
			logger.error(e);
			Mtxtb006Mensagem mensagemErroInterno = this.fornecedorDados.buscarMensagem(MensagemRetorno.ERRO_INTERNO);
			ServicoException se = new ServicoException(mensagemErroInterno, Constantes.ORIGEM_SIMTX);
			DadosTransacaoComuns dadosTransacaoComuns = new DadosTransacaoComuns(servico, transacao,tarefasServicoResposta.getListaTransacaoTarefas(), iteracaoCanal);
			parametrosAdicionais.setDadosBarramento(this.dadosBarramento);
			this.tratadorDeExcecao.tratarExcecaoRetomada(dadosTransacaoComuns, se, parametrosAdicionais, true);		
		}
	}
	
	/**
	 * Envia as tarefas/passos para o sibar.
	 * 
	 * @param listaTarefas
	 * @param transacao
	 * @param servico
	 * @return
	 * @throws ServicoException
	 */
	public TarefasServicoResposta executarTarefas(List<Mtxtb003ServicoTarefa> listaTarefas, Mtxtb014Transacao transacao,
			Mtxtb011VersaoServico servico) throws ServicoException {

		logger.info("Preparando xml de requisicao para enviar ao Sibar");
		String xmlEntradaSibar = transformarXml(servico.getDeXsltRequisicao(), transacao, null);
		xmlEntradaSibar = this.geradorPassosMigrado.gerarPassos(xmlEntradaSibar, servico, listaTarefas);
		this.dadosBarramento.escrever(xmlEntradaSibar);
		
		String xmlSaidaSibar = this.execucaoMq.executar(xmlEntradaSibar, servico.getMtxtb001Servico());
		this.dadosBarramento.escrever(xmlSaidaSibar);

		return this.gerenciadorTarefas.montarTarefasResposta(listaTarefas, servico, transacao, this.dadosBarramento, xmlSaidaSibar);
	}

	/**
	 * Transforma o xml em outro a partir do xslt.
	 * 
	 * @param xslt
	 * @param dadosBarramento
	 * @return
	 * @throws ServicoException
	 */
	public String transformarXml(String xslt, Mtxtb014Transacao transacao, Resposta mensagem) throws ServicoException {
		try {
			List<ParametroXsl> parametrosNovos = new ArrayList<>();
			parametrosNovos.add(new ParametroXsl("nsuSimtx", String.valueOf(transacao.getNuNsuTransacao())));
			if (mensagem != null) {
				parametrosNovos.add(new ParametroXsl(ParametroXsl.XSL_PARAM_COD_RETORNO, mensagem.getCodigo()));
				parametrosNovos.add(new ParametroXsl(ParametroXsl.XSL_PARAM_ACAO_RETORNO, AcaoRetorno.recuperarAcao(mensagem.getIcTipoMensagem()).getRotulo()));
				parametrosNovos.add(new ParametroXsl(ParametroXsl.XSL_PARAM_ORIGEM_RETORNO, mensagem.getOrigem()));
				parametrosNovos.add(new ParametroXsl(ParametroXsl.XSL_PARAM_MENSAGEM_NEGOCIAL, mensagem.getMensagemNegocial()));
				parametrosNovos.add(new ParametroXsl(ParametroXsl.XSL_PARAM_MENSAGEM_TECNICA, mensagem.getMensagemTecnica()));
			}
			ParametroXsl[] pArr = new ParametroXsl[parametrosNovos.size()];

			String caminhoXls = this.simtxConfig.getCaminhoXslt() + xslt;
			String arquivoXsl = this.repositorioArquivo.recuperarArquivo(caminhoXls);
			return new TransformadorXsl().transformar(this.dadosBarramento.getDadosLeitura(), arquivoXsl,
					parametrosNovos.toArray(pArr));
		} catch (Exception e) {
			logger.error(e);
			Mtxtb006Mensagem mensagemErroInterno = this.fornecedorDados.buscarMensagem(MensagemRetorno.ERRO_INTERNO);
			throw new ServicoException(mensagemErroInterno, Constantes.ORIGEM_SIMTX);
		}
	}

}
