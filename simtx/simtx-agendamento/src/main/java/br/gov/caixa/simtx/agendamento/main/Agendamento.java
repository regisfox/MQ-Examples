package br.gov.caixa.simtx.agendamento.main;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.apache.log4j.Logger;

import br.gov.caixa.simtx.agendamento.util.AgendamentoException;
import br.gov.caixa.simtx.persistencia.agendamento.entidade.Mtxtb034TransacaoAgendamento;
import br.gov.caixa.simtx.persistencia.cache.agendamento.FornecedorDadosAgendamento;
import br.gov.caixa.simtx.persistencia.cache.core.FornecedorDados;
import br.gov.caixa.simtx.persistencia.constante.AcaoRetorno;
import br.gov.caixa.simtx.persistencia.constante.Constantes;
import br.gov.caixa.simtx.persistencia.constante.MensagemRetorno;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb003ServicoTarefa;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb004Canal;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb006Mensagem;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb011VersaoServico;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb014Transacao;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb016IteracaoCanal;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb016IteracaoCanalPK;
import br.gov.caixa.simtx.util.SimtxConfig;
import br.gov.caixa.simtx.util.data.DataUtil;
import br.gov.caixa.simtx.util.enums.CodigosRetornosEnum;
import br.gov.caixa.simtx.util.exception.ServicoException;
import br.gov.caixa.simtx.util.gerenciador.GerenciadorTarefas;
import br.gov.caixa.simtx.util.gerenciador.Mensagem;
import br.gov.caixa.simtx.util.gerenciador.ValidadorRegrasNegocio;
import br.gov.caixa.simtx.util.gerenciador.cache.RepositorioArquivo;
import br.gov.caixa.simtx.util.xml.BuscadorTextoXml;
import br.gov.caixa.simtx.util.xml.DadosBarramento;
import br.gov.caixa.simtx.util.xml.ParametroXsl;
import br.gov.caixa.simtx.util.xml.Resposta;
import br.gov.caixa.simtx.util.xml.TransformadorXsl;


@Stateless
public class Agendamento implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private static final Logger logger = Logger.getLogger(Agendamento.class);
	
	@Inject
	protected FornecedorDados fornecedorDados;
	
	@Inject
	protected FornecedorDadosAgendamento fornecedorDadosAgendamento;
	
	@Inject
	protected Mensagem mensagemServidor;
	
	@Inject
	protected SimtxConfig simtxConfig;
	
	@Inject
	protected GerenciadorTarefas gerenciadorTarefas;
	
	@Inject
	protected ValidadorRegrasNegocio validadorRegrasNegocio;
	
	@Inject 
	private RepositorioArquivo repositorioArquivo;
	
	private static final String CODIGOMSGIMPEDITIVO = "Codigo de retorno impeditivo para Agendamento";
	

	
	/**
	 * Busca o servico e a versao na entidade Mtxtb011VersaoServico.
	 * 
	 * @param buscador
	 * @return {@link Mtxtb034TransacaoAgendamento }
	 * @throws SemResultadoException
	 */
	public List<Mtxtb034TransacaoAgendamento> buscarAgendadamentosPorData(Date date) throws ServicoException {
		try {
			logger.info("Buscando os agendamentos de  ["+date+"] no banco");
			return this.fornecedorDadosAgendamento.buscaTransacoesAgendamentoPorData(date);
		} 
		catch (Exception e) {
			logger.error(e);
			Mtxtb006Mensagem mensagem = this.fornecedorDados.buscarMensagem(MensagemRetorno.ERRO_INTERNO);
			throw new ServicoException(mensagem, Constantes.ORIGEM_SIMTX);
		}
	}
	
	/**
	 * Buscar a entidade Canal.
	 * 
	 * @return {@link Mtxtb004Canal}
	 * @throws Exception
	 */
	public Mtxtb004Canal buscarCanalSIMTX() {
		Mtxtb004Canal canal = new Mtxtb004Canal();
		try {
			logger.info("Buscando buscarCanalSIMTX");
			canal.setNuCanal(Constantes.CODIGO_CANAL_SIMTX);
			return this.fornecedorDados.buscarCanalPorPK(canal);
		} 
		catch (Exception e) {
			logger.error("Canal nao encontrado", e);
			canal.setNuCanal(Constantes.CODIGO_CANAL_INEXISTENTE.longValue());
			return canal;
		}
	}
	
	/**
	 * Salva a entidade Transacao.
	 * 
	 * @param transacaoAgendamento
	 * @param canal
	 * @return {@link Mtxtb014Transacao}
	 * @throws Exception
	 */
	public Mtxtb014Transacao salvarTransacao(Mtxtb034TransacaoAgendamento transacaoAgendamento)
			throws ServicoException {
		try {
			logger.info("Gravando transacao");
			Mtxtb014Transacao transacao = new Mtxtb014Transacao();
			transacao.setCoCanalOrigem(String.valueOf(Constantes.CODIGO_CANAL_SIMTX));
			transacao.setNuNsuTransacaoPai(transacaoAgendamento.getNuNsuTransacaoAgendamento());
			transacao.setIcSituacao(Constantes.IC_SERVICO_EM_ANDAMENTO);
			transacao.setDhMultiCanal(new Date());
			transacao.setDtReferencia(new Date());
			transacao.setDhTransacaoCanal(new Date());
			transacao.setIcEnvio(BigDecimal.ZERO);
			transacao.setIcRetorno(BigDecimal.ZERO);
			transacao.setTsAtualizacao(new Date());
			transacao = this.fornecedorDados.salvarTransacao(transacao);
			logger.info("NSUMTX gerado: " + transacao.getNuNsuTransacao());
			return transacao;
		} 
		catch (Exception e) {
			logger.error(e);
			Mtxtb006Mensagem mensagem = this.fornecedorDados.buscarMensagem(MensagemRetorno.NSU_NAO_GERADO);
			throw new ServicoException(mensagem, Constantes.ORIGEM_SIMTX);
		}
	}
	
	/**
	 * Atualiza a Transacao com a nova situacao.
	 * 
	 * @param situacao
	 * @throws ServicoException
	 */
	public void atualizaTransacaoAgendamento(Mtxtb034TransacaoAgendamento transacaoAgendamento, int situacao)
			throws ServicoException {
		try {
			logger.info("Atualizando Agendamento Nsu ["+transacaoAgendamento.getNuNsuTransacaoAgendamento()+"]");
			transacaoAgendamento.setIcSituacao(situacao);
			this.fornecedorDadosAgendamento.alterarTransacao(transacaoAgendamento);
		} 
		catch (Exception e) {
			logger.error(e);
			Mtxtb006Mensagem mensagem = this.fornecedorDados.buscarMensagem(MensagemRetorno.ERRO_GRAVACAO_TRANSACAO);
			throw new ServicoException(mensagem, Constantes.ORIGEM_SIMTX);
		}
	}
	
	/**
	 * Salva entidade IteracaoCanal.
	 * 
	 * @param transacao
	 * @param xmlEntrada
	 * @param xmlSaida
	 * @return {@link Mtxtb016IteracaoCanal}
	 * @throws ServicoException
	 */
	public Mtxtb016IteracaoCanal salvarIteracaoCanal(Mtxtb014Transacao transacao, String xmlEntrada, String xmlSaida)
			throws ServicoException {
		try {
			logger.info("Gravando IteracaoCanal - Transacao Nsu "+transacao.getNuNsuTransacao()+"]");
			String terminal = "";

			Mtxtb016IteracaoCanal iteracao = new Mtxtb016IteracaoCanal();
			iteracao.setMtxtb004Canal(new Mtxtb004Canal());
			iteracao.getMtxtb004Canal().setNuCanal(Long.parseLong(transacao.getCoCanalOrigem()));
			iteracao.setMtxtb014Transacao(transacao);
			iteracao.setTsRecebimentoSolicitacao(DataUtil.getDataAtual());
			iteracao.setTsRetornoSolicitacao(DataUtil.getDataAtual());
			iteracao.setDeRetorno(xmlSaida);
			iteracao.setDeRecebimento(xmlEntrada);
			iteracao.setId(new Mtxtb016IteracaoCanalPK());
			iteracao.getId().setNuNsuTransacao014(transacao.getNuNsuTransacao());
			iteracao.setDtReferencia(transacao.getDtReferencia());
			iteracao.setCodTerminal(terminal);
			return this.fornecedorDados.salvarIteracaoCanal(iteracao);
		} 
		catch (Exception e) {
			logger.error(e);
			Mtxtb006Mensagem mensagem = this.fornecedorDados.buscarMensagem(MensagemRetorno.ERRO_GRAVACAO_TRANSACAO);
			throw new ServicoException(mensagem, Constantes.ORIGEM_SIMTX);
		}
	}
	
	/**
	 * Valida os codigos de retorno de acordo com as Regras do Agendamento.
	 * 
	 * @param listaTarefas
	 * @param versaoServico
	 * @param isUltimaExecucao
	 * @throws ServicoException
	 * @throws AgendamentoException
	 */
	public Resposta verificarCodigoImpeditivo(String xmlRespostaTarefa, Mtxtb003ServicoTarefa servicoTarefa,
			String xmlSaidaSibar, boolean isUltimaExecucao)
			throws ServicoException, AgendamentoException {

		Resposta resposta = null;
		try {
			resposta = this.validadorRegrasNegocio.verificarCodigoImpeditivo(
					new BuscadorTextoXml(xmlRespostaTarefa), new BuscadorTextoXml(xmlSaidaSibar),
					servicoTarefa.getMtxtb012VersaoTarefa());
		} 
		catch (ServicoException e) {
			validarCodigoParaAgendamento(e, servicoTarefa, isUltimaExecucao);
		} 
		catch (Exception e) {
			if(isUltimaExecucao) {
				Mtxtb006Mensagem msg = this.fornecedorDados.buscarMensagem(MensagemRetorno.ERRO_INTERNO);
				throw new AgendamentoException(msg, CODIGOMSGIMPEDITIVO);
			}
			throw new ServicoException(this.fornecedorDados.buscarMensagem(MensagemRetorno.ERRO_INTERNO),
					Constantes.ORIGEM_SIMTX);
		}
		return resposta;
    }
	
	/**
	 * Valida se o codigo impeditivo recebido da tabela 06(Mesangem) deve cancelar o
	 * Agendamento.
	 * Caso nao seja impeditivo para Agendamento, sera realizado novas tentativas 
	 * no dia.
	 * 
	 * @param e
	 * @param trnsoTrfa
	 * @param isUltimaExecucao
	 * @throws ServicoException
	 * @throws AgendamentoException
	 */
	public void validarCodigoParaAgendamento(ServicoException e, Mtxtb003ServicoTarefa servicoTarefa,
			boolean isUltimaExecucao) throws ServicoException, AgendamentoException {
		
		if(isUltimaExecucao) {
			throw new AgendamentoException(e.getMensagem(), CODIGOMSGIMPEDITIVO);
		}
		else if(!Constantes.MENSAGEM_ERRO_DESCONHECIDO.equals(e.getMensagem().getDeMensagemNegocial())) {
			CodigosRetornosEnum codigoEnum = CodigosRetornosEnum.obterCodigo(e.getMensagem().getCoMensagem(),
					servicoTarefa.getMtxtb012VersaoTarefa().getId().getNuTarefa002());
			
			if(codigoEnum != null && AcaoRetorno.IMPEDITIVA.getTipo().equals(codigoEnum.getTipoCodigo())) {
				throw new AgendamentoException(e.getMensagem(), CODIGOMSGIMPEDITIVO);
			}
			else {
				Mtxtb006Mensagem mensagem = e.getMensagem();
				mensagem.setDeMensagemTecnica(
						Constantes.MSG_AGENDAMENTO_NAO_IMPEDITIVO);
				throw new ServicoException(mensagem, Constantes.ORIGEM_SIMTX);
			}
		}
		else {
			throw e;
		}
	}
	
	/**
	 * Gera o xml a partir do xslt.
	 * 
	 * @param xslt
	 * @param dadosBarramento
	 * @param transacao
	 * @param canal
	 * @param mensagem
	 * @return
	 * @throws ServicoException
	 */
	public String transformarXml(String xslt, DadosBarramento dadosBarramento, Mtxtb034TransacaoAgendamento transacao,
			Mtxtb004Canal canal, Resposta mensagem) throws ServicoException {
		try {
			List<ParametroXsl> parametrosNovos = new ArrayList<>();
			parametrosNovos.add(new ParametroXsl(ParametroXsl.XSL_PARAM_NSU_SIMTX, String.valueOf(transacao.getNuNsuTransacaoAgendamento())));
			parametrosNovos.add(new ParametroXsl(ParametroXsl.XSL_PARAM_REDE_TRANSMISSORA, String.valueOf(canal.getNuRedeTransmissora())));
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
			return new TransformadorXsl().transformar(dadosBarramento.getDadosLeitura(), arquivoXsl,
					parametrosNovos.toArray(pArr));
		} 
		catch (Exception e) {
			logger.error(e);
			Mtxtb006Mensagem mensagemErroInterno = this.fornecedorDados.buscarMensagem(MensagemRetorno.ERRO_INTERNO);
			throw new ServicoException(mensagemErroInterno,Constantes.ORIGEM_SIMTX);
		}
	}
	
	/**
	 * Gera o xml a partir do xslt.
	 * 
	 * @param xslt
	 * @param dadosBarramento
	 * @param transacao
	 * @param isUltimaExecucao
	 * @param isBoletoCaixa
	 * @return
	 * @throws ServicoException
	 */
	public String transformarTransacaoAgendamentoXml(String xslt, DadosBarramento dadosBarramento,
			Mtxtb014Transacao transacao, boolean isUltimaExecucao, boolean isBoletoCaixa)
			throws ServicoException {
		try {
			String caminhoXls = this.simtxConfig.getCaminhoXslt() + xslt;
			String arquivoXsl = this.repositorioArquivo.recuperarArquivo(caminhoXls);
			return new TransformadorXsl().transformar(dadosBarramento.getDadosLeitura(), arquivoXsl,
					new ParametroXsl("nsuSimtx", String.valueOf(transacao.getNuNsuTransacao())),
					new ParametroXsl("isUltimaExecucao", String.valueOf(isUltimaExecucao)),
					new ParametroXsl("isBoletoCaixa", String.valueOf(isBoletoCaixa)));
		} 
		catch (Exception e) {
			logger.error(this.mensagemServidor.recuperarMensagem("erro.transformar.xml"), e);
			Mtxtb006Mensagem mensagemBanco = this.fornecedorDados.buscarMensagem(MensagemRetorno.ERRO_INTERNO);
			throw new ServicoException(mensagemBanco, Constantes.ORIGEM_SIMTX);
		}
	}
	
	/**
     * Retorna mensagem de resposta pronta para o canal.
     *
     * @return the string
     */
	public String retornaMensagem(Mtxtb011VersaoServico versaoServico, Mtxtb014Transacao transacao,
			DadosBarramento dadosBarramento, ServicoException se) {
        try {
        	Properties prop = carregaArquivoPropriedade();
			
			String xslServico = this.simtxConfig.getCaminhoXslt() + prop.getProperty("path.generico.xslt");
        	if(versaoServico != null)
        		xslServico = this.simtxConfig.getCaminhoXslt() + versaoServico.getDeXsltResposta();
			
        	String arquivoXsl = this.repositorioArquivo.recuperarArquivo(xslServico);
        	String nsuSimtx = transacao != null ? String.valueOf(transacao.getNuNsuTransacao()) : "";
        	
        	return dadosBarramento.ler(arquivoXsl,
    				new ParametroXsl("erro", "sim"),
    				new ParametroXsl("codRetorno", se.getMensagem().getCoMensagem()),
    				new ParametroXsl("acaoRetorno", AcaoRetorno.recuperarAcao(se.getMensagem().getIcTipoMensagem()).getRotulo()),
    				new ParametroXsl("origemRetorno", se.getSistemaOrigem()),
    				new ParametroXsl("mensagemNegocial", se.getMensagem().getDeMensagemNegocial()),
    				new ParametroXsl("mensagemTecnica", se.getMensagem().getDeMensagemTecnica()),
    				new ParametroXsl("nsuSimtx", nsuSimtx));
        }
		catch (Exception e) {
			logger.fatal("Erro no processamento de envio da resposta para o canal", e);
		}
        return null;
	}
	
    /**
	 * Carrega o arquivo de propriedades Dados.
     * @throws IOException 
     * @throws Exception
	 */
	public Properties carregaArquivoPropriedade() throws IOException {
		Properties prop = new Properties();
		String arquivo = this.simtxConfig.getHome() + "/dados.properties";
		File popFile = new File(arquivo);
		FileInputStream stream = new FileInputStream(popFile);
		prop.load(stream);
		stream.close();
		return prop;
	}
	
	
	
	public void setFornecedorDados(FornecedorDados fornecedorDados) {
		this.fornecedorDados = fornecedorDados;
	}
	
	public void setMensagemServidor(Mensagem mensagemServidor) {
		this.mensagemServidor = mensagemServidor;
	}

	public void setSimtxConfig(SimtxConfig simtxConfig) {
		this.simtxConfig = simtxConfig;
	}

	public void setGerenciadorTarefas(GerenciadorTarefas gerenciadorTarefas) {
		this.gerenciadorTarefas = gerenciadorTarefas;
	}

	public void setFornecedorDadosAgendamento(FornecedorDadosAgendamento fornecedorDadosAgendamento) {
		this.fornecedorDadosAgendamento = fornecedorDadosAgendamento;
	}

	public void setValidadorRegrasNegocio(ValidadorRegrasNegocio validadorRegrasNegocio) {
		this.validadorRegrasNegocio = validadorRegrasNegocio;
	}

}
