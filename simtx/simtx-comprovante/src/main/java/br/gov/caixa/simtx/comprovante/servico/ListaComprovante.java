package br.gov.caixa.simtx.comprovante.servico;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import br.gov.caixa.simtx.persistencia.cache.core.FornecedorDados;
import br.gov.caixa.simtx.persistencia.constante.AcaoRetorno;
import br.gov.caixa.simtx.persistencia.constante.Constantes;
import br.gov.caixa.simtx.persistencia.constante.MensagemRetorno;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb003ServicoTarefa;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb004Canal;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb006Mensagem;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb008MeioEntrada;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb011VersaoServico;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb014Transacao;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb024TarefaFila;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb024TarefaFilaPK;
import br.gov.caixa.simtx.util.SimtxConfig;
import br.gov.caixa.simtx.util.exception.ServicoException;
import br.gov.caixa.simtx.util.gerenciador.GerenciadorTarefas;
import br.gov.caixa.simtx.util.gerenciador.cache.RepositorioArquivo;
import br.gov.caixa.simtx.util.mensagem.GerenciadorFilasMQ;
import br.gov.caixa.simtx.util.to.TarefasServicoResposta;
import br.gov.caixa.simtx.util.xml.BuscadorTextoXml;
import br.gov.caixa.simtx.util.xml.DadosBarramento;
import br.gov.caixa.simtx.util.xml.GeradorPassosMigrado;
import br.gov.caixa.simtx.util.xml.ParametroXsl;
import br.gov.caixa.simtx.util.xml.Resposta;
import br.gov.caixa.simtx.util.xml.TransformadorXsl;

@Stateless
public class ListaComprovante implements Serializable {
    
	private static final long serialVersionUID = 1L;
	
	private static final Logger logger = Logger.getLogger(ListaComprovante.class);
	
	protected DadosBarramento dadosBarramento;
	
	@Inject
	protected FornecedorDados fornecedorDados;
	
	@Inject
	protected GerenciadorTarefas gerenciadorTarefas;
	
	@Inject
	protected GeradorPassosMigrado geradorPassosMigrado;
	
	@Inject
	protected GerenciadorFilasMQ execucaoMq;
	
	@Inject
	protected SimtxConfig simtxConfig;
	
	@Inject
	private RepositorioArquivo repositorioArquivo;
	/**
	 * Trata o servico de Lista Comprovante.
	 * 
	 * @param transacao
	 * @param canal
	 * @param servico
	 * @param meioEntrada
	 * @param dadosBarramento
	 * @return
	 * @throws Exception 
	 */
	public TarefasServicoResposta executar(Mtxtb014Transacao transacao, Mtxtb004Canal canal, Mtxtb011VersaoServico servico,
			Mtxtb008MeioEntrada meioEntrada, DadosBarramento dadosBarramento) throws ServicoException {
		
		this.dadosBarramento = dadosBarramento;
		
		List<Mtxtb003ServicoTarefa> listaTarefas = this.gerenciadorTarefas
				.carregarTarefasServico(servico, canal, meioEntrada);
		
		TarefasServicoResposta tarefasServicoRespostaSiaut = processarTarefas(transacao, servico, listaTarefas, canal);
		TarefasServicoResposta tarefasServicoRespostaSicco = processarTarefaSicco(transacao, servico, listaTarefas, canal, tarefasServicoRespostaSiaut);

		try {
			BuscadorTextoXml buscador = new BuscadorTextoXml(dadosBarramento.getDadosLeitura());
			if (temTransacoesSiaut(buscador)
					&& !tarefasServicoRespostaSicco.getResposta().getCodigo().equals(Constantes.MENSAGEM_RETORNO_SUCESSO)) {

				tarefasServicoRespostaSicco.setResposta(tarefasServicoRespostaSiaut.getResposta());
				return tarefasServicoRespostaSicco;
			}
		} catch (ParserConfigurationException | SAXException | IOException e) {
			logger.error("Não foi possível ler resposta do barramento", e);
			Mtxtb006Mensagem mensagemErroInterno = this.fornecedorDados.buscarMensagem(MensagemRetorno.ERRO_INTERNO);
			throw new ServicoException(mensagemErroInterno, Constantes.ORIGEM_SIMTX);
		}
		
		TarefasServicoResposta tarefasServicoResposta = new TarefasServicoResposta();
		
		tarefasServicoResposta.setListaTransacaoTarefas(tarefasServicoRespostaSiaut.getListaTransacaoTarefas());
		tarefasServicoResposta.getListaTransacaoTarefas().addAll(tarefasServicoRespostaSicco.getListaTransacaoTarefas());
		tarefasServicoResposta.setResposta(tarefasServicoRespostaSicco.getResposta());
		
//		logger.info("Preparando xml de resposta para enviar ao Canal");
//		return transformarXml(servico.getDeXsltResposta(), transacao, canal, mensagem);
		return tarefasServicoResposta;
	}
	
	/**
	 * Executa as tarefas de Meio de Entrada e do SIUAT.
	 * 
	 * @param transacao
	 * @param servico
	 * @param listaTarefas
	 * @throws Exception 
	 */
	private TarefasServicoResposta processarTarefas(Mtxtb014Transacao transacao, Mtxtb011VersaoServico servico,
			List<Mtxtb003ServicoTarefa> listaTarefas, Mtxtb004Canal canal) throws ServicoException {
		logger.info("Executando tarefas de Meio de Entrada e do SIAUT");
		logger.info("Preparando xml de requisicao para enviar ao Sibar");
		String xmlEntradaSibar = transformarXml(servico.getDeXsltRequisicao(), transacao, canal, null);
		int posPenultimaTarefa = listaTarefas.size() - 1;
		xmlEntradaSibar = this.geradorPassosMigrado.gerarPassos(xmlEntradaSibar, servico,
				listaTarefas.subList(0, (listaTarefas.size() - 1)));
		this.dadosBarramento.escrever(xmlEntradaSibar);
		
		String xmlSaidaSibar = this.execucaoMq.executar(xmlEntradaSibar, servico.getMtxtb001Servico());
		this.dadosBarramento.escrever(xmlSaidaSibar);
		
		return this.gerenciadorTarefas.montarTarefasResposta(listaTarefas.subList(0,posPenultimaTarefa), servico, transacao, dadosBarramento, xmlSaidaSibar);
//		try {
//			this.gerenciadorTarefas.salvarTarefasServico(listaTarefas.subList(0, (listaTarefas.size() - 1)),
//					servico, transacao, this.dadosBarramento, xmlSaidaSibar);
//		} 
//		catch (ServicoException e) {
//			this.gerenciadorTarefas.salvarTarefasServicoSemValidar(
//					listaTarefas.subList((listaTarefas.size() - 1), listaTarefas.size()), servico, transacao,
//					this.dadosBarramento);
//			
//			throw e;
//		}
	}
	
	/**
	 * Executa a tarefa do SICCO.
	 * 
	 * @param transacao
	 * @param servico
	 * @param listaTarefas
	 * @param tarefasServicoRespostaSiaut 
	 * @throws Exception 
	 */
	private TarefasServicoResposta processarTarefaSicco(Mtxtb014Transacao transacao, Mtxtb011VersaoServico servico,
			List<Mtxtb003ServicoTarefa> listaTarefas, Mtxtb004Canal canal, TarefasServicoResposta tarefasServicoRespostaSiaut) throws ServicoException {
		logger.info("Executando tarefa do Sicco");
		
		listaTarefas = listaTarefas.subList((listaTarefas.size() - 1), listaTarefas.size());
		
		Mtxtb024TarefaFilaPK filaTarefaPk = new Mtxtb024TarefaFilaPK();
		filaTarefaPk.setNuTarefa012(listaTarefas.get(0).getMtxtb012VersaoTarefa().getId().getNuTarefa002());
		filaTarefaPk.setNuVersaoTarefa012(listaTarefas.get(0).getMtxtb012VersaoTarefa().getId().getNuVersaoTarefa());

		List<Mtxtb024TarefaFila> tarefaFilas = this.fornecedorDados.buscarTarefasFilas(filaTarefaPk);

		logger.info("Preparando xml de requisicao para enviar ao Sibar");
		String xmlEntradaSibar = transformarXml(listaTarefas.get(0).getMtxtb012VersaoTarefa().getDeXsltRequisicao(),
				transacao, canal, null);
		this.dadosBarramento.escrever(xmlEntradaSibar);
		
		String xmlSaidaSibar = this.execucaoMq.executar(xmlEntradaSibar, tarefaFilas.get(0));
		this.dadosBarramento.escrever(xmlSaidaSibar);
		
		return this.gerenciadorTarefas.montarTarefasResposta(listaTarefas, servico, transacao, dadosBarramento, xmlSaidaSibar);
//		return this.gerenciadorTarefas.salvarTarefasServico(listaTarefas, servico, transacao, this.dadosBarramento,	xmlSaidaSibar);
	}
	
	
	/**
	 * Gera um xml a partir do xslt.
	 * 
	 * @param xslt
	 * @param transacao
	 * @param canal
	 * @param mensagem
	 * @return
	 * @throws ServicoException
	 */
	public String transformarXml(String xslt, Mtxtb014Transacao transacao, Mtxtb004Canal canal, Resposta mensagem)
			throws ServicoException {
		try {
			List<ParametroXsl> parametrosNovos = new ArrayList<>();
			parametrosNovos.add(new ParametroXsl("nsuSimtx", String.valueOf(transacao.getNuNsuTransacao())));
			parametrosNovos.add(new ParametroXsl("pRedeTransmissora", String.valueOf(canal.getNuRedeTransmissora())));
			if(mensagem != null) {
				parametrosNovos.add(new ParametroXsl(ParametroXsl.XSL_PARAM_COD_RETORNO, mensagem.getCodigo()));
		        parametrosNovos.add(new ParametroXsl(ParametroXsl.XSL_PARAM_ACAO_RETORNO, AcaoRetorno.recuperarAcao(mensagem.getIcTipoMensagem()).getRotulo()));
		        parametrosNovos.add(new ParametroXsl(ParametroXsl.XSL_PARAM_ORIGEM_RETORNO, mensagem.getOrigem()));
		        parametrosNovos.add(new ParametroXsl(ParametroXsl.XSL_PARAM_MENSAGEM_NEGOCIAL,mensagem.getMensagemNegocial()));
		        parametrosNovos.add(new ParametroXsl(ParametroXsl.XSL_PARAM_MENSAGEM_TECNICA, mensagem.getMensagemTecnica()));
			}
			ParametroXsl[] pArr = new ParametroXsl[parametrosNovos.size()];
			
			String caminhoXls = this.simtxConfig.getCaminhoXslt() + xslt;
			String arquivoXsl = this.repositorioArquivo.recuperarArquivo(caminhoXls);
			return new TransformadorXsl().transformar(this.dadosBarramento.getDadosLeitura(), arquivoXsl, parametrosNovos.toArray(pArr));
		} 
		catch (Exception e) {
			logger.error(e);
			Mtxtb006Mensagem mensagemErroInterno = this.fornecedorDados.buscarMensagem(MensagemRetorno.ERRO_INTERNO);
			throw new ServicoException(mensagemErroInterno,Constantes.ORIGEM_SIMTX);
		}
	}
	
	private boolean temTransacoesSiaut(BuscadorTextoXml buscador) {
		Node ns = buscador.xpath("/BUSDATA/*[local-name()='SERVICO_SAIDA']/*[local-name()='CONSULTA_TRANSACOES_CONTA_SAIDA']/BOLETO/TRANSACOES/*");
		if(ns == null) return false;
		
		return ns.getChildNodes().getLength() > 0;
	}

}
