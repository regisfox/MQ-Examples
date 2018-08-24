package br.gov.caixa.simtx.manutencao.desfazimento.canal;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.apache.log4j.Logger;

import br.gov.caixa.simtx.persistencia.constante.Constantes;
import br.gov.caixa.simtx.persistencia.constante.MensagemRetorno;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb003ServicoTarefa;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb003ServicoTarefaPK;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb004Canal;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb006Mensagem;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb011VersaoServico;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb012VersaoTarefa;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb014Transacao;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb015SrvcoTrnsoTrfa;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb015SrvcoTrnsoTrfaPK;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb016IteracaoCanal;
import br.gov.caixa.simtx.persistencia.vo.CanalEnum;
import br.gov.caixa.simtx.util.ParametrosAdicionais;
import br.gov.caixa.simtx.util.SimtxConfig;
import br.gov.caixa.simtx.util.data.DataUtil;
import br.gov.caixa.simtx.util.exception.ServicoException;
import br.gov.caixa.simtx.util.gerenciador.GerenciadorTransacao;
import br.gov.caixa.simtx.util.gerenciador.cache.RepositorioArquivo;
import br.gov.caixa.simtx.util.gerenciador.servico.ProcessadorServicos;
import br.gov.caixa.simtx.util.integrador.sicco.ProcessadorEnvioSicco;
import br.gov.caixa.simtx.util.mensagem.GerenciadorFilasMQ;
import br.gov.caixa.simtx.util.xml.BuscadorResposta;
import br.gov.caixa.simtx.util.xml.BuscadorTextoXml;
import br.gov.caixa.simtx.util.xml.DadosBarramento;
import br.gov.caixa.simtx.util.xml.GeradorPassosMigrado;
import br.gov.caixa.simtx.util.xml.ParametroXsl;
import br.gov.caixa.simtx.util.xml.Resposta;
import br.gov.caixa.simtx.util.xml.TransformadorXsl;

@Stateless
public class ProcessadorDesfazimento extends GerenciadorTransacao implements ProcessadorServicos {

	private static final long serialVersionUID = 4456400529065490893L;

	private static final Logger logger = Logger.getLogger(ProcessadorDesfazimento.class);

	private DadosBarramento dadosBarramento;
	
	@Inject
	protected SimtxConfig simtxConfig;
	
	@Inject
	protected GeradorPassosMigrado geradorPassosMigrado;
	
	private Mtxtb004Canal canal;
	
	@Inject
	protected GerenciadorFilasMQ execucaoMq;
	
	@Inject
	protected ProcessadorEnvioSicco processadorEnvioSicco;
	
	@Inject
	private RepositorioArquivo repositorioArquivo;
	


	@Override
	public void processar(ParametrosAdicionais parametrosAdicionais) {
		Mtxtb014Transacao transacao = null;
		Mtxtb016IteracaoCanal iteracao = null;
		
		if (null != parametrosAdicionais.getXmlMensagem() && !parametrosAdicionais.getXmlMensagem().isEmpty()) {
		try {
			this.dadosBarramento = new DadosBarramento();
			BuscadorTextoXml buscador = new BuscadorTextoXml(parametrosAdicionais.getXmlMensagem());
			canal = new Mtxtb004Canal();
			canal.setNuCanal(Constantes.CODIGO_CANAL_SIMTX);

			validaCodigoServico(buscador);
			
			Integer versaoServicoXml = Integer.parseInt(buscador.xpathTexto("/DESFAZIMENTO_ENTRADA/HEADER/SERVICO/VERSAO"));
			
			Mtxtb011VersaoServico versaoServicoDesfazimento = this.fornecedorDados.buscarVersaoServico(versaoServicoXml, Constantes.SERVICO_DESFAZIMENTO);
			
			this.validadorRegrasNegocio.validarXmlComXsd(parametrosAdicionais.getXmlMensagem(), versaoServicoDesfazimento);
			
			this.dadosBarramento.escrever(parametrosAdicionais.getXmlMensagem());
			
			transacao = salvarTransacao(buscador, canal);
			iteracao = salvarIteracaoCanal(buscador, parametrosAdicionais.getXmlMensagem(), transacao);
			salvarTransacaoServico(transacao, versaoServicoDesfazimento);
			
			Mtxtb014Transacao transacaoOrigem = this.fornecedorDados.buscarTransacaoOrigem(transacao.getNuNsuTransacaoPai());
			List<Mtxtb015SrvcoTrnsoTrfa> tarefasExecutadas = this.fornecedorDados.buscarTarefasPorNsu(transacaoOrigem.getNuNsuTransacao());
			
			List<Mtxtb015SrvcoTrnsoTrfa> tarefasDesfazer = buscarTarefasComDesfazimento(tarefasExecutadas);
			
			if(tarefasDesfazer.isEmpty()) {
				logger.warn("Nenhuma tarefa para ser desfeita transacao: " + transacaoOrigem.getNuNsuTransacao());
				return;
			}
			
			List<Mtxtb003ServicoTarefa> tarefasDeDesfazimento = buscarServicoTarefasDesfazimento(tarefasDesfazer, versaoServicoDesfazimento);

			subirXmlDesfazimento(tarefasDesfazer);
			
			String xmlSaidaSibar = enviarDesfazimentoBarramento(versaoServicoDesfazimento, transacao, transacaoOrigem,
					tarefasDeDesfazimento);
			this.dadosBarramento.escrever(xmlSaidaSibar);
			
			List<Mtxtb015SrvcoTrnsoTrfa> listaTransacaoTarefa = salvarTarefasServicoDesfazimento(tarefasDeDesfazimento, 
					versaoServicoDesfazimento, transacao, this.dadosBarramento);
			
			int quantidadeErros = this.gerenciadorTarefas.quantidadeTarefasComErro(listaTransacaoTarefa);
			logger.info("Quantidade tarefas com erros: " + quantidadeErros);
			
			BigDecimal situacaoTransacao = situacaoFinalTransacao(quantidadeErros, listaTransacaoTarefa);
			atualizaTransacao(transacao, situacaoTransacao);
			atualizaIteracaoCanal(iteracao, transacao, "");
			
			this.processadorEnvioSicco.processarEnvioOnline(transacao, Constantes.ENVIO_UNICA);
		}catch (ServicoException e) {
			logger.error(e.getMessage(), e);
			tratarExcecao(transacao, iteracao);
			this.processadorEnvioSicco.processarEnvioOnline(transacao, Constantes.ENVIO_UNICA);
		}catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		}else {
			logger.info("Mensagem vazia, não processada");
		}
	}

	private BigDecimal situacaoFinalTransacao(int quantidadeErros, List<Mtxtb015SrvcoTrnsoTrfa> lista) {
		if (quantidadeErros == lista.size()) {
			logger.info(Constantes.SITUACAO_FINAL_TRANSACAO_NEGADA);
			return Constantes.IC_SERVICO_NEGADO;
		} 
		else if(quantidadeErros > 0) {
			logger.info(Constantes.SITUACAO_FINAL_PARCIAL_TRANSACAO);
			return Constantes.IC_SERVICO_FINALIZADO_PARCIAL;
		}
		else {
			logger.info(Constantes.SITUACAO_FINAL_TRANSACAO_FINALIZADA);
			return Constantes.IC_SERVICO_FINALIZADO;
		}
	}
	
	private void tratarExcecao(Mtxtb014Transacao transacao, Mtxtb016IteracaoCanal iteracao) {
		try {
			if (transacao != null)
				atualizaTransacao(transacao, Constantes.IC_SERVICO_NEGADO);
			if(iteracao != null)
				atualizaIteracaoCanal(iteracao, transacao, "");
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	public List<Mtxtb015SrvcoTrnsoTrfa> salvarTarefasServicoDesfazimento(List<Mtxtb003ServicoTarefa> listaTarefas,
			Mtxtb011VersaoServico servico, Mtxtb014Transacao transacao, DadosBarramento dadosBarramento)
			throws ServicoException {
		try {
			logger.info("Salvando tarefas do servico");
			List<Mtxtb015SrvcoTrnsoTrfa> listaTransacaoTarefas = new ArrayList<>();
			for(Mtxtb003ServicoTarefa servicoTarefa : listaTarefas) {
				try {
					logger.info("Salvando Tarefa ["+servicoTarefa.getId().getNuTarefa012()+"] "
							+ servicoTarefa.getMtxtb012VersaoTarefa().getMtxtb002Tarefa().getNoTarefa()+"");
					
					String mensagemSaida = transformarXml(servicoTarefa.getMtxtb012VersaoTarefa().getDeXsltResposta(), dadosBarramento);
					
					Mtxtb015SrvcoTrnsoTrfa transacaoTarefa = new Mtxtb015SrvcoTrnsoTrfa();
			        transacaoTarefa.setId(new Mtxtb015SrvcoTrnsoTrfaPK());
			        transacaoTarefa.getId().setNuNsuTransacao017(transacao.getNuNsuTransacao());
			        transacaoTarefa.getId().setNuTarefa012(servicoTarefa.getMtxtb012VersaoTarefa().getId().getNuTarefa002());
			        transacaoTarefa.getId().setNuVersaoTarefa012(servicoTarefa.getMtxtb012VersaoTarefa().getId().getNuVersaoTarefa());
			        transacaoTarefa.getId().setNuServico017(servico.getId().getNuServico001());
			        transacaoTarefa.getId().setNuVersaoServico017(servico.getId().getNuVersaoServico());
			        transacaoTarefa.setTsExecucaoTransacao(DataUtil.getDataAtual());
			        transacaoTarefa.setDtReferencia(transacao.getDtReferencia());
			        transacaoTarefa.setDeXmlRequisicao("");
			        transacaoTarefa.setDeXmlResposta(mensagemSaida);
			        transacaoTarefa.setMtxtb012VersaoTarefa(servicoTarefa.getMtxtb012VersaoTarefa());
			        transacaoTarefa.setNsuCorp(0L);
			        this.fornecedorDados.salvarTransacaoTarefa(transacaoTarefa);
			        listaTransacaoTarefas.add(transacaoTarefa);
			        
				} catch (Exception e) {
	        		logger.error("Erro ao receber resposta da tarefa " + servicoTarefa.getId().getNuTarefa012());
	        	}
			}
			
			return listaTransacaoTarefas;
		} 
		catch (Exception e) {
			logger.error(this.mensagemServidor.recuperarMensagem("erro.salvar.tarefas"), e);
			Mtxtb006Mensagem mensagemErroInterno = this.fornecedorDados.buscarMensagem(MensagemRetorno.ERRO_INTERNO);
			throw new ServicoException(mensagemErroInterno, Constantes.ORIGEM_SIMTX);
		}
	}

	private String transformarXml(String xslt, DadosBarramento dadosBarramento) throws ServicoException {
		try {
			String caminhoXls = this.simtxConfig.getCaminhoXslt() + xslt;
			String arquivoXsl = this.repositorioArquivo.recuperarArquivo(caminhoXls);
			return new TransformadorXsl().transformar(dadosBarramento.getDadosLeitura(), arquivoXsl);
		} 
		catch (Exception e) {
			logger.error(this.mensagemServidor.recuperarMensagem("erro.transformar.xml"));
			logger.error(e);
			
			Mtxtb006Mensagem mensagemErroInterno = this.fornecedorDados.buscarMensagem(MensagemRetorno.ERRO_INTERNO);
			throw new ServicoException(mensagemErroInterno, Constantes.ORIGEM_SIMTX);
		}
	}
	
	private String enviarDesfazimentoBarramento(Mtxtb011VersaoServico versaoServicoDesfazimento,
			Mtxtb014Transacao transacao, Mtxtb014Transacao transacaoOrigem, List<Mtxtb003ServicoTarefa> tarefasDeDesfazimento) throws ServicoException {
		logger.info("Preparando xml de requisicao para enviar ao Sibar");
		String xmlEntradaSibar = transformarXml(versaoServicoDesfazimento.getDeXsltRequisicao(), transacao, 
				CanalEnum.obterPorCodigo(Integer.valueOf(transacaoOrigem.getCoCanalOrigem())).getSigla());
		
		xmlEntradaSibar = this.geradorPassosMigrado.gerarPassos(xmlEntradaSibar, versaoServicoDesfazimento, tarefasDeDesfazimento);
		this.dadosBarramento.escrever(xmlEntradaSibar);
		return this.execucaoMq.executar(xmlEntradaSibar, versaoServicoDesfazimento.getMtxtb001Servico());
	}
	
	private List<Mtxtb003ServicoTarefa> buscarServicoTarefasDesfazimento(List<Mtxtb015SrvcoTrnsoTrfa> tarefasDesfazer, Mtxtb011VersaoServico versaoServicoDesfazimento) {
		List<Mtxtb004Canal> canais = new ArrayList<>();
		canais.add(this.canal);
		
		List<Mtxtb003ServicoTarefa> servicosTarefas = new ArrayList<>();
		
		for(Mtxtb015SrvcoTrnsoTrfa tarefaDesfazer : tarefasDesfazer) {
			Mtxtb003ServicoTarefa st = new Mtxtb003ServicoTarefa();
			st.setMtxtb011VersaoServico(versaoServicoDesfazimento);
			st.setMtxtb012VersaoTarefa(tarefaDesfazer.getMtxtb012VersaoTarefa().getVersaoTarefaDesfazimento());
			st.setMtxtb004Canals(canais);
			Mtxtb003ServicoTarefaPK stPk = new Mtxtb003ServicoTarefaPK();
			
			stPk.setNuServico011(versaoServicoDesfazimento.getId().getNuServico001());
			stPk.setNuTarefa012(tarefaDesfazer.getMtxtb012VersaoTarefa().getId().getNuTarefa002());
			stPk.setNuVersaoServico011(versaoServicoDesfazimento.getId().getNuVersaoServico());
			stPk.setNuVersaoTarefa012(tarefaDesfazer.getMtxtb012VersaoTarefa().getId().getNuVersaoTarefa());
			st.setId(stPk);
			servicosTarefas.add(st);
		}
		
		return servicosTarefas;
	}

	public String transformarXml(String xslt, Mtxtb014Transacao transacao, String canalOriginal) throws ServicoException {
		try {
			String caminhoXls = this.simtxConfig.getCaminhoXslt() + xslt;
			String arquivoXsl = this.repositorioArquivo.recuperarArquivo(caminhoXls);
			return new TransformadorXsl().transformar(this.dadosBarramento.getDadosLeitura(), arquivoXsl,
					new ParametroXsl("nsuSimtx", String.valueOf(transacao.getNuNsuTransacao())),
					new ParametroXsl("canalOriginal", canalOriginal));
		} 
		catch (Exception e) {
			logger.error(e);
			Mtxtb006Mensagem mensagemErroInterno = this.fornecedorDados.buscarMensagem(MensagemRetorno.ERRO_INTERNO);
			throw new ServicoException(mensagemErroInterno,Constantes.ORIGEM_SIMTX);
		}
	}

	private List<Mtxtb015SrvcoTrnsoTrfa> buscarTarefasComDesfazimento(List<Mtxtb015SrvcoTrnsoTrfa> tarefasExecutadas) {
		List<Mtxtb015SrvcoTrnsoTrfa> tarefasDesfazer = new ArrayList<>();
		
		for(Mtxtb015SrvcoTrnsoTrfa tarefaExecutada : tarefasExecutadas) {
			Mtxtb012VersaoTarefa versaoTarefaDesfazimento = 
					tarefaExecutada.getMtxtb012VersaoTarefa().getVersaoTarefaDesfazimento();
			if(versaoTarefaDesfazimento != null) {
				if(tarefaExecutadaComSucesso(tarefaExecutada)) {
					tarefasDesfazer.add(tarefaExecutada);
				} 
			}
		}
		
		return tarefasDesfazer;
	}

	private void subirXmlDesfazimento(List<Mtxtb015SrvcoTrnsoTrfa> tarefasDesfazer) {
		StringBuilder xmlTarefasDesfazimento = new StringBuilder("<DESFAZIMENTO>");
		TransformadorXsl transformador = new TransformadorXsl();
		for(Mtxtb015SrvcoTrnsoTrfa tarefaDesfazer : tarefasDesfazer) {
			transformarTarefaDesfazimento(xmlTarefasDesfazimento, transformador, tarefaDesfazer);
		}
		
		String desfazimentoXml = xmlTarefasDesfazimento.append("</DESFAZIMENTO>").toString();
		desfazimentoXml = desfazimentoXml.replaceAll("\\<\\?xml(.+?)\\?\\>", "").trim();
		this.dadosBarramento.escrever(desfazimentoXml);
	}

	private void transformarTarefaDesfazimento(StringBuilder xmlTarefasDesfazimento, TransformadorXsl transformador,
			Mtxtb015SrvcoTrnsoTrfa tarefaDesfazer) {
		try {
			String arquivoXsl = this.repositorioArquivo.recuperarArquivo(this.simtxConfig.getCaminhoXslt()
					+ tarefaDesfazer.getMtxtb012VersaoTarefa().getVersaoTarefaDesfazimento().getDeXsltRequisicao());
			
			String xmlTarefas = "<root>"+tarefaDesfazer.getDeXmlRequisicao()+tarefaDesfazer.getDeXmlResposta()+"</root>";
			
			String xmlDesfazimento = transformador.transformar(xmlTarefas.replaceAll("\\<\\?xml(.+?)\\?\\>", "").trim(), arquivoXsl);
			
			xmlTarefasDesfazimento.append(xmlDesfazimento);
		}
		catch (Exception e) {
			logger.error(e.getMessage(), e);
			logger.warn("fluxo de desfazimento continua");
		}
	}

	private boolean tarefaExecutadaComSucesso(Mtxtb015SrvcoTrnsoTrfa tarefaExecutada) {
		if(tarefaExecutada.getDeXmlResposta() == null || tarefaExecutada.getDeXmlResposta().equals("")) {
			return false;
		}
		
		try {
			BuscadorTextoXml buscador = new BuscadorTextoXml(tarefaExecutada.getDeXmlResposta());
			Resposta respostaBarramentoXml = new BuscadorResposta().buscarRespostaTarefaBarramento(buscador);
			String resposta = respostaBarramentoXml.getCodigo();
			Mtxtb006Mensagem mensagemRetorno = this.fornecedorDados
					.buscarMensagemPorTarefaCodRetorno(resposta, tarefaExecutada.getMtxtb012VersaoTarefa())
					.getMtxtb006Mensagem();
			if(mensagemRetorno == null) { 
				return false;
			} 
			
			return !mensagemRetorno.isImpeditiva();
			
		} catch (Exception e) {
			return false;
		}
	}

	private void validaCodigoServico(BuscadorTextoXml buscador) throws ServicoException {
		if(!Constantes.SERVICO_DESFAZIMENTO.toString().equals(buscador.xpathTexto("/DESFAZIMENTO_ENTRADA/HEADER/SERVICO/CODIGO"))){
			Mtxtb006Mensagem mensagemErro = this.fornecedorDados.buscarMensagem(MensagemRetorno.LEIAUTE_MENSAGEM_INVALIDA);
			throw new ServicoException(mensagemErro, Constantes.ORIGEM_SIMTX);
		}
	}

}
