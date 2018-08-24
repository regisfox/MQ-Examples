package br.gov.caixa.simtx.util.integrador.sicco;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import org.apache.log4j.Logger;

import br.gov.caixa.simtx.persistencia.cache.core.FornecedorDados;
import br.gov.caixa.simtx.persistencia.constante.Constantes;
import br.gov.caixa.simtx.persistencia.constante.MensagemRetorno;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb006Mensagem;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb014Transacao;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb015SrvcoTrnsoTrfa;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb016IteracaoCanal;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb016IteracaoCanalPK;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb017VersaoSrvcoTrnso;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb023Parametro;
import br.gov.caixa.simtx.persistencia.vo.CanalEnum;
import br.gov.caixa.simtx.util.data.DataUtil;
import br.gov.caixa.simtx.util.exception.ServicoException;
import br.gov.caixa.simtx.util.mensagem.GerenciadorFilasMQ;
import br.gov.caixa.simtx.util.xml.BuscadorTextoXml;


@Stateless
public class ProcessadorEnvioSicco implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private static final Logger logger = Logger.getLogger(ProcessadorEnvioSicco.class);
	
	@Inject
	protected FornecedorDados fornecedorDados;
	
	@Inject
	protected GerenciadorFilasMQ execucaoMq;
	
	protected static final String BREAK_PMAC = "<break_pmac/>";
	
	protected static final String BREAK_PMAC_TAREFA = "<break_param_tarefa/>";

	protected static final String BREAK_TAREFA = "<break_tarefa_pmac/>";
	
	
	
	/**
	 * Envia a transacao apos ser processada pelo sistema.
	 * 
	 * @param transacao
	 * @param tipoEnvio
	 */
	public void processarEnvioOnline(Mtxtb014Transacao transacao, String tipoEnvio) {
		logger.info(" ==== Processo Envio SICCO Iniciado ==== ");
		Mtxtb023Parametro pmt = this.fornecedorDados.buscarParametroPorPK(Constantes.FLAG_SICCO);
		if(pmt != null && pmt.getDeConteudoParam() != null 
				&& Integer.parseInt(pmt.getDeConteudoParam()) == 1) {
			logger.info("Status Sistema SICCO: [Ligado]");
		
			if(tipoEnvio.equals(Constantes.ENVIO_UNICA)
					|| tipoEnvio.equals(Constantes.ENVIO_ALTERACAO)) {
				executarEnvioUnico(transacao);
			}
		}
		else{
			logger.info("Mensagem nao enviada. Status Sistema SICCO: [Desligado]");
		}
		logger.info(" ==== Processo Envio SICCO Finalizado ==== ");
	}
	
	/**
	 * Envia todas as transacoes que estao pendentes no sistema.
	 * 
	 */
	@TransactionAttribute(TransactionAttributeType.NEVER)
	public void processarEnvioPendentes() {
		logger.info(" ==== Processo Envio SICCO Iniciado ==== ");

		Mtxtb023Parametro pmt = this.fornecedorDados.buscarParametroPorPK(Constantes.FLAG_SICCO);
		if (pmt != null && pmt.getDeConteudoParam() != null && Integer.parseInt(pmt.getDeConteudoParam()) == 1) {
			logger.info("Status Sistema SICCO: [Ligado]");

			logger.info("Recuperando transacoes pendentes de envio");
			List<Mtxtb014Transacao> lista = this.fornecedorDados.buscarTransacoesParaEnvioSicco(new Date());
			if (!lista.isEmpty()) {
				logger.info("Enviando transacoes para o Sicco - Total [" + lista.size() + "]");
				for (Mtxtb014Transacao transacao : lista) {
					executarEnvioUnico(transacao);
				}
			} 
			else {
				logger.info("Nao ha transacoes pendentes de envio");
			}
		} 
		else {
			logger.info("Mensagens nao enviadas. Status Sistema SICCO: [Desligado]");
		}
		logger.info(" ==== Processo Envio SICCO Finalizado ==== ");
	}
	
	/**
	 * Executa o envio de apenas uma transacao para o Sicco.
	 * 
	 * @param transacao
	 */
	public void executarEnvioUnico(Mtxtb014Transacao transacao) {
		try {
			logger.info("Preparando Transacao Nsu ["+transacao.getNuNsuTransacao()+"] para envio");
			Mtxtb016IteracaoCanalPK iteracaoCanalPK = new Mtxtb016IteracaoCanalPK();
			iteracaoCanalPK.setNuNsuTransacao014(transacao.getNuNsuTransacao());
			Mtxtb016IteracaoCanal iteracaoCanal = new Mtxtb016IteracaoCanal();
			iteracaoCanal.setId(iteracaoCanalPK);
			iteracaoCanal = this.fornecedorDados.buscarIteracaoCanalPorPK(iteracaoCanal);
			
			gravarTentativaDeEnvio(transacao, 0, 0);
			
			StringBuilder sb = new StringBuilder();
			sb.append("<REQUISICAO>");
			sb.append("<OPERACAO>");
			sb.append(Constantes.ENVIO_UNICA);
			sb.append("</OPERACAO>");
			sb.append("</REQUISICAO>");
			sb.append(BREAK_PMAC);
			
			sb.append(iteracaoCanal.getDeRecebimento());
			sb.append(BREAK_PMAC);
			sb.append(iteracaoCanal.getDeRetorno());
			sb = criarMensagem(sb, transacao, iteracaoCanal);
			String mensagem = sb.toString();
			
			String resposta = this.execucaoMq.executarSicco(mensagem);
			gravarTentativaDeEnvio(transacao, 1, 0);
			
			BuscadorTextoXml bt = new BuscadorTextoXml(resposta);
			String codigo = bt.buscarTexto("COD_RETORNO");
			
			if(codigo.equalsIgnoreCase("00")) {
				gravarTentativaDeEnvio(transacao, 1, 1);
			}
			else {
				logger.error("Nao foi possivel gravar a Transacao Nsu ["+transacao.getNuNsuTransacao()+"] no Sicco");
			}
		} 
		catch (ServicoException e) {
			logger.error("Ocorreu um erro na Transacao Nsu ["+transacao.getNuNsuTransacao()+"]");
			logger.error(e.getMensagem().getDeMensagemTecnica());
		}
		catch(Exception e) {
			logger.error("Ocorreu um erro na Transacao Nsu ["+transacao.getNuNsuTransacao()+"]", e);
		}
	}
	
	/**
	 * Grava a tentativa de envio para o Sicco.
	 * 
	 * @param transacao
	 * @param envioCco
	 * @param retornoCco
	 * @throws Exception
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void gravarTentativaDeEnvio(Mtxtb014Transacao transacao, int envioCco, int retornoCco) throws ServicoException {
		try {
			logger.info("Gravando tentativa de envio ["+envioCco+"] e retorno ["+retornoCco+"] da Transacao Nsu ["
					+transacao.getNuNsuTransacao()+"]");
			transacao.setIcEnvio(new BigDecimal(envioCco));
			transacao.setIcRetorno(new BigDecimal(retornoCco));
			transacao.setTsAtualizacao(DataUtil.getDataAtual());
			this.fornecedorDados.alterarTransacao(transacao);
		} 
		catch (Exception e) {
			logger.error(e);
			Mtxtb006Mensagem mensagemErroInterno = this.fornecedorDados.buscarMensagem(MensagemRetorno.ERRO_INTERNO);
			throw new ServicoException(mensagemErroInterno, Constantes.ORIGEM_SIMTX);
		}
	}
	
	/**
	 * Cria a mensagem para o Sicco.
	 * 
	 * @param sb
	 * @param transacao
	 * @return
	 * @throws Exception
	 */
	public StringBuilder criarMensagem(StringBuilder sb, Mtxtb014Transacao transacao,
			Mtxtb016IteracaoCanal iteracaoCanal) throws ServicoException {
		try {
			logger.info("Criando mensagem para envio da Transacao Nsu ["+transacao.getNuNsuTransacao()+"]");
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			SimpleDateFormat dataHora = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	
			Mtxtb017VersaoSrvcoTrnso versaoSrvcoTrnso = new Mtxtb017VersaoSrvcoTrnso();
			versaoSrvcoTrnso.setMtxtb014Transacao(transacao);
			versaoSrvcoTrnso = this.fornecedorDados.buscarSrvcoTransacaoPorNSU(versaoSrvcoTrnso);
			
			String nomeServico = "";
			if(versaoSrvcoTrnso != null) {
				nomeServico = versaoSrvcoTrnso.getMtxtb011VersaoServico().getDeXsdRequisicao();
			    String[] servicoQuebrada = nomeServico.split("/");
			    nomeServico = servicoQuebrada[servicoQuebrada.length-1];
			}
			else {
				nomeServico = "Generico.xsd";
			}
			
			BuscadorTextoXml buscador = new BuscadorTextoXml(iteracaoCanal.getDeRecebimento());
			String sigla = buscador.xpathTexto("/*[1]/HEADER/CANAL/SIGLA");
			int canalOrigem = 0;
			if(!sigla.isEmpty() ) {
				canalOrigem = CanalEnum.obterPorSigla(sigla).getCodigo();
			}
			
		    sb.append(BREAK_PMAC);
		    sb.append("<TRANSACAO>");
		    
		    sb.append("<NSU>");
		    sb.append(transacao.getNuNsuTransacao());
		    sb.append("</NSU>");
		    
		    sb.append("<NSU_ORIGEM>");
		    sb.append(transacao.getNuNsuTransacaoPai());
		    sb.append("</NSU_ORIGEM>");
		    
		    sb.append("<CODIGO_SERVICO>");
		    sb.append(versaoSrvcoTrnso.getId().getNuServico011());
		    sb.append("</CODIGO_SERVICO>");
		    
		    sb.append("<VERSAO_SERVICO>");
		    sb.append(versaoSrvcoTrnso.getId().getNuVersaoServico011());
		    sb.append("</VERSAO_SERVICO>");
		    
		    sb.append("<NOME_SERVICO>");
		    sb.append(nomeServico);
		    sb.append("</NOME_SERVICO>");
	
			sb.append("<CO_CANAL>");
			sb.append(transacao.getCoCanalOrigem());
			sb.append("</CO_CANAL>");
			
			sb.append("<CO_CANAL_ORIGEM>");
			sb.append(canalOrigem);
			sb.append("</CO_CANAL_ORIGEM>");
	
			sb.append("<DT_REFERENCIA>");
			sb.append(sdf.format(transacao.getDtReferencia()));
			sb.append("</DT_REFERENCIA>");
	
			sb.append("<DH_TRANSACAO_CANAL>");
			sb.append(dataHora.format(transacao.getDhTransacaoCanal()));
			sb.append("</DH_TRANSACAO_CANAL>");
	
			sb.append("<DT_CONTABIL>");
			sb.append(transacao.getDtContabil() != null
					? sdf.format(transacao.getDtContabil())
					: "");
			sb.append("</DT_CONTABIL>");
	
			sb.append("<DH_MULTICANAL>");
			sb.append(dataHora.format(transacao.getDhMultiCanal()));
			sb.append("</DH_MULTICANAL>");
	
			sb.append("<IC_SITUACAO>");
			sb.append(transacao.getIcSituacao());
			sb.append("</IC_SITUACAO>");
	
			List<Mtxtb015SrvcoTrnsoTrfa> tarefas = this.fornecedorDados
					.buscarTarefasPorNsu(transacao.getNuNsuTransacao());
	
			sb.append("<TAREFAS>");
			sb.append(tarefas.size());
			sb.append("</TAREFAS>");
	
			sb.append("</TRANSACAO>");
			sb.append(BREAK_PMAC);
			
			for (Mtxtb015SrvcoTrnsoTrfa tarefa : tarefas) {
				sb.append(tarefa.getId().getNuTarefa012());
				sb.append(BREAK_PMAC_TAREFA);
				sb.append(tarefa.getId().getNuVersaoTarefa012());
				sb.append(BREAK_PMAC_TAREFA);
				sb.append(tarefa.getTsExecucaoTransacao());
				sb.append(BREAK_PMAC_TAREFA);
				sb.append(tarefa.getDeXmlRequisicao() == null || "".equals(tarefa.getDeXmlRequisicao()) ? "null" : tarefa.getDeXmlRequisicao());
				sb.append(BREAK_PMAC_TAREFA);
				sb.append(tarefa.getDeXmlResposta() == null || "".equals(tarefa.getDeXmlResposta())? "null" : tarefa.getDeXmlResposta());
				sb.append(BREAK_TAREFA);
			}
			return sb;
		} 
		catch (Exception e) {
			logger.error(e);
			Mtxtb006Mensagem mensagemErroInterno = this.fornecedorDados.buscarMensagem(MensagemRetorno.ERRO_INTERNO);
			throw new ServicoException(mensagemErroInterno, Constantes.ORIGEM_SIMTX);
		}
	}
	
	public void setFornecedorDados(FornecedorDados fornecedorDados) {
		this.fornecedorDados = fornecedorDados;
	}
	
}
