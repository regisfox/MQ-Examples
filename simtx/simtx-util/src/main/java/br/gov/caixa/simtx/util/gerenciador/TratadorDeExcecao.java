/**
 * 
 */
package br.gov.caixa.simtx.util.gerenciador;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Properties;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.apache.log4j.Logger;

import br.gov.caixa.simtx.persistencia.agendamento.entidade.Mtxtb034TransacaoAgendamento;
import br.gov.caixa.simtx.persistencia.atualizador.dados.AtualizadorDadosTransacao;
import br.gov.caixa.simtx.persistencia.constante.AcaoRetorno;
import br.gov.caixa.simtx.persistencia.constante.Constantes;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb003ServicoTarefa;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb006Mensagem;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb011VersaoServico;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb014Transacao;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb015SrvcoTrnsoTrfa;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb016IteracaoCanal;
import br.gov.caixa.simtx.util.ConstantesAgendamento;
import br.gov.caixa.simtx.util.ParametrosAdicionais;
import br.gov.caixa.simtx.util.SimtxConfig;
import br.gov.caixa.simtx.util.desfazimento.transacao.ProcessadorEnvioDesfazimento;
import br.gov.caixa.simtx.util.exception.ServicoException;
import br.gov.caixa.simtx.util.gerenciador.cache.RepositorioArquivo;
import br.gov.caixa.simtx.util.integrador.sicco.ProcessadorEnvioSicco;
import br.gov.caixa.simtx.util.retomar.transacao.ProcessadorEnvioRetomadaTransacao;
import br.gov.caixa.simtx.util.to.DadosTransacaoComuns;
import br.gov.caixa.simtx.util.xml.DadosBarramento;
import br.gov.caixa.simtx.util.xml.ParametroXsl;


@Stateless
public class TratadorDeExcecao extends GerenciadorTransacao implements Serializable {

	private static final long serialVersionUID = 1L;

	private static Logger logger = Logger.getLogger(TratadorDeExcecao.class);
	
    @Inject
    private SimtxConfig simtxConfig;
    
    protected DadosBarramento dadosBarramento;
	
    @Inject
	private RepositorioArquivo repositorio;
    
    @Inject
	protected ProcessadorEnvioSicco processadorEnvioSicco;
    
    @Inject
    protected ProcessadorEnvioRetomadaTransacao processadorEnvioRetomadaTransacao;
    
    @Inject
    protected AtualizadorDadosTransacao atualizadorDadosTransacao;
    
    @Inject
    protected ProcessadorEnvioDesfazimento processadorEnvioDesfazimento;
    
    private String respostaTratar = "Nao foi possivel montar resposta";
    
    private static final String TRANSACAO_NAO_LOCALIZADA = "Transacao nao localizada";
	

    public TratadorDeExcecao() {}
    
    public TratadorDeExcecao(DadosBarramento dadosBarramento) {
		super();
		this.dadosBarramento = dadosBarramento;
	}
    
    
    /**
     * Trata a excecao e envia a msg para o canal.
     * 
     * @param versaoServico
     * @param transacao
     * @param liSrvcoTrnsoTrfas
     * @param iteracaoCanal
     * @param se
     * @param parametrosAdicionais
     */
	public void tratarExcecaoRetomada(DadosTransacaoComuns dadosTransacaoComuns, ServicoException se,
		ParametrosAdicionais parametrosAdicionais, boolean postaMensagemCanal) {
		logger.info("inicializando tratarExcecaoComRetomada");
		String resposta = null;
		this.dadosBarramento = parametrosAdicionais.getDadosBarramento();
		Mtxtb011VersaoServico versaoServico = dadosTransacaoComuns.getMtxtb011VersaoServico();
		Mtxtb014Transacao transacao = dadosTransacaoComuns.getMtxtb014Transacao();
		
		resposta = retornaMensagem(versaoServico, transacao, this.dadosBarramento, se);
		
		if (postaMensagemCanal) {
			enviarRespostaCanal(resposta, parametrosAdicionais);
		}
		
		if (transacao != null) {
			List<Mtxtb015SrvcoTrnsoTrfa> liSrvcoTrnsoTrfas = dadosTransacaoComuns.getMtxtb015SrvcoTrnsoTrfas();
			Mtxtb016IteracaoCanal iteracaoCanal = dadosTransacaoComuns.getMtxtb016IteracaoCanal();			
			
			logger.info(Constantes.SITUACAO_FINAL_TRANSACAO_NEGADA);
			transacao = atualizaStatusTransacao(transacao, Constantes.IC_SERVICO_NEGADO);
			iteracaoCanal = montarAtualizaIteracaoCanal(iteracaoCanal, transacao, resposta);
			
				boolean sucesso = atualizadorDadosTransacao.atualizarDadosTransacaoTarefasIteracoes(transacao, liSrvcoTrnsoTrfas, montarAtualizaIteracaoCanal(iteracaoCanal, transacao, resposta));
				if (!sucesso) {
					this.processadorEnvioRetomadaTransacao.envioRetomadaTransacao(transacao, versaoServico, parametrosAdicionais.getDadosBarramento(),  parametrosAdicionais.getXmlMensagem());
				}else {
					this.processadorEnvioSicco.processarEnvioOnline(transacao, Constantes.ENVIO_UNICA);	
				}	
		}else {
			logger.error(TRANSACAO_NAO_LOCALIZADA);
		}
		logger.info("finalizando tratarExcecaoComRetomada");
	}
	
	public void tratarExcecaoRetomadaDesfazimentoTransacao(DadosTransacaoComuns dadosTransacaoComuns, ServicoException se,
			ParametrosAdicionais parametrosAdicionais, boolean postaMensagemCanal) {
			logger.info("inicializando tratarExcecaoRetomadaDesfazimentoTransacao");
			
			String resposta = null;
			this.dadosBarramento = parametrosAdicionais.getDadosBarramento();
			Mtxtb011VersaoServico versaoServico = dadosTransacaoComuns.getMtxtb011VersaoServico();
			Mtxtb014Transacao transacao = dadosTransacaoComuns.getMtxtb014Transacao();
			List<Mtxtb015SrvcoTrnsoTrfa> liSrvcoTrnsoTrfas = dadosTransacaoComuns.getMtxtb015SrvcoTrnsoTrfas();
			Mtxtb016IteracaoCanal iteracaoCanal = dadosTransacaoComuns.getMtxtb016IteracaoCanal();
			List<Mtxtb003ServicoTarefa> listaTarefas = dadosTransacaoComuns.getMtxtb003ServicoTarefas();
			
			resposta = retornaMensagem(dadosTransacaoComuns.getMtxtb011VersaoServico(), dadosTransacaoComuns.getMtxtb014Transacao(), this.dadosBarramento, se);
			if (postaMensagemCanal) {
				enviarRespostaCanal(resposta, parametrosAdicionais);
			}
			if (transacao != null) {
				logger.info(Constantes.SITUACAO_FINAL_TRANSACAO_NEGADA);
				transacao = atualizaStatusTransacao(transacao, Constantes.IC_SERVICO_NEGADO);
				iteracaoCanal = montarAtualizaIteracaoCanal(iteracaoCanal, transacao, resposta);
				
				boolean statusAtualizacao = atualizadorDadosTransacao.atualizarDadosTransacaoTarefasIteracoes(transacao, liSrvcoTrnsoTrfas, montarAtualizaIteracaoCanal(iteracaoCanal, transacao, resposta));
				
				if (!statusAtualizacao) {
					this.processadorEnvioRetomadaTransacao.envioRetomadaTransacao(transacao, versaoServico, parametrosAdicionais.getDadosBarramento(),  parametrosAdicionais.getXmlMensagem());
				}else {
					this.processadorEnvioDesfazimento.enviarMsgDesfazimento(listaTarefas, transacao, dadosBarramento);
					this.processadorEnvioSicco.processarEnvioOnline(transacao, Constantes.ENVIO_UNICA);
				}
			}else {
				logger.error(TRANSACAO_NAO_LOCALIZADA);
			}
			
			logger.info("finalizando tratarExcecaoRetomadaDesfazimentoTransacao");
	}
	
	public void tratarExcecaoEfetivaAgendamentoPagamentoRetomadaDesfazimento(DadosTransacaoComuns dadosTransacaoComuns, Mtxtb034TransacaoAgendamento transacaoAgendamento, ServicoException se, ParametrosAdicionais parametrosAdicionais, boolean isUltimaExecucao, boolean postaMensagemCanal) {
			logger.info("inicializando tratarExcecaoEfetivaAgendamentoPagamentoRetomadaDesfazimento");		
			
			boolean validaRegraAgendamento = ((null != se.getMessage() && !se.getMensagem().getDeMensagemTecnica().equals(Constantes.MSG_AGENDAMENTO_NAO_IMPEDITIVO)) || isUltimaExecucao);
			
			String resposta = null;
			this.dadosBarramento = parametrosAdicionais.getDadosBarramento();
			Mtxtb011VersaoServico versaoServico = dadosTransacaoComuns.getMtxtb011VersaoServico();
			Mtxtb014Transacao transacao = dadosTransacaoComuns.getMtxtb014Transacao();
			List<Mtxtb015SrvcoTrnsoTrfa> liSrvcoTrnsoTrfas = dadosTransacaoComuns.getMtxtb015SrvcoTrnsoTrfas();
			Mtxtb016IteracaoCanal iteracaoCanal = dadosTransacaoComuns.getMtxtb016IteracaoCanal();
			List<Mtxtb003ServicoTarefa> listaTarefas = dadosTransacaoComuns.getMtxtb003ServicoTarefas();
			
			resposta = retornaMensagem(dadosTransacaoComuns.getMtxtb011VersaoServico(), dadosTransacaoComuns.getMtxtb014Transacao(), this.dadosBarramento, se);

			if (postaMensagemCanal) {
				enviarRespostaCanal(resposta, parametrosAdicionais);
			}
			if (transacao != null) {

				if(validaRegraAgendamento) {
						logger.info(ConstantesAgendamento.ULTIMA_EXECUCAO);
						transacaoAgendamento.setIcSituacao(ConstantesAgendamento.SITUACAO_NEGADO);
						logger.info(ConstantesAgendamento.SITUACAO_FINAL_AGENDAMENTO_NEGADO);
				}
				else {
					logger.info("Agendamento Nsu ["+transacaoAgendamento.getNuNsuTransacaoAgendamento()+"] " + "Nao foi Efetivado. Sera realizado novas tentativas no dia.");
				}
				
				logger.info(Constantes.SITUACAO_FINAL_TRANSACAO_NEGADA);
				transacao = atualizaStatusTransacao(transacao, Constantes.IC_SERVICO_NEGADO);
				iteracaoCanal = montarAtualizaIteracaoCanal(iteracaoCanal, transacao, resposta);
				
				boolean statusAtualizacao = atualizadorDadosTransacao.atualizarDadosTransacaoAgendamento(transacao, liSrvcoTrnsoTrfas, montarAtualizaIteracaoCanal(iteracaoCanal, transacao, resposta), transacaoAgendamento);
				
				if (!statusAtualizacao) {
					this.processadorEnvioRetomadaTransacao.envioRetomadaTransacao(transacao, versaoServico, parametrosAdicionais.getDadosBarramento(),  parametrosAdicionais.getXmlMensagem());
				}else {
					if (validaRegraAgendamento) {
						this.processadorEnvioDesfazimento.enviarMsgDesfazimento(listaTarefas, transacao, dadosBarramento);	
					}
					this.processadorEnvioSicco.processarEnvioOnline(transacao, Constantes.ENVIO_UNICA);
				}
				
			}else {
				logger.error(TRANSACAO_NAO_LOCALIZADA);
			}
			
			logger.info("finalizando tratarExcecaoEfetivaAgendamentoPagamentoRetomadaDesfazimento");
	}
    
    /**
     * Retorna mensagem de resposta pronta para o canal.
     *
     * @return the string
     */
    public String retornaMensagem(Long nsuSimtx, String xslServico, Mtxtb006Mensagem mensagem, String origemRetorno) {
        try {
        	return this.dadosBarramento.ler(xslServico,
    				new ParametroXsl("erro", "sim"),
    				new ParametroXsl("codRetorno", mensagem.getCodigoRetorno()),
    				new ParametroXsl("acaoRetorno", AcaoRetorno.recuperarAcao(mensagem.getIcTipoMensagem()).getRotulo()),
    				new ParametroXsl("origemRetorno", origemRetorno),
    				new ParametroXsl("mensagemNegocial", mensagem.getDeMensagemNegocial()),
    				new ParametroXsl("mensagemTecnica", mensagem.getDeMensagemTecnica()),
    				new ParametroXsl("nsuSimtx", String.valueOf(nsuSimtx)));
        }
		catch (Exception e) {
			logger.fatal("Erro no processamento de envio da resposta para o canal", e);
		}
        return null;
	}
    
    /**
     * Monta a mensagem de resposta.
     * 
     * @param versaoServico
     * @param transacao
     * @param dadosBarramento
     * @param se
     * @return String
     */
	public String retornaMensagem(Mtxtb011VersaoServico versaoServico, Mtxtb014Transacao transacao,
			DadosBarramento dadosBarramento, ServicoException se) {
		String mensagem = respostaTratar;
        try {
        	Properties prop = carregaArquivoPropriedade();
			
			String xslServico = this.simtxConfig.getCaminhoXslt() + prop.getProperty("path.generico.xslt");
        	if(versaoServico != null) {
        		xslServico = this.simtxConfig.getCaminhoXslt() + versaoServico.getDeXsltResposta();
        	}
        	
        	String arquivoXsl = this.repositorio.recuperarArquivo(xslServico);
        	String nsuSimtx = transacao != null ? String.valueOf(transacao.getNuNsuTransacao()) : "";
        	
        	mensagem = dadosBarramento.ler(arquivoXsl,
    				new ParametroXsl("erro", "sim"),
    				new ParametroXsl("codRetorno", se.getMensagem().getCodigoRetorno()),
    				new ParametroXsl("acaoRetorno", AcaoRetorno.recuperarAcao(se.getMensagem().getIcTipoMensagem()).getRotulo()),
    				new ParametroXsl("origemRetorno", se.getSistemaOrigem()),
    				new ParametroXsl("mensagemNegocial", se.getMensagem().getDeMensagemNegocial()),
    				new ParametroXsl("mensagemTecnica", se.getMensagem().getDeMensagemTecnica()),
    				new ParametroXsl("nsuSimtx", nsuSimtx));
        }
		catch (Exception e) {
			logger.error(e);
			return mensagem;
		}
        return mensagem;
	}
	
	/**
	 * Trata a excecao e envia resposta ao canal e processador sicco
	 * 
	 * @param versaoServico
	 * @param transacao
	 * @param iteracaoCanal
	 * @param se
	 */
	public void tratarExcecao(Mtxtb011VersaoServico versaoServico, Mtxtb014Transacao transacao,
			Mtxtb016IteracaoCanal iteracaoCanal, ServicoException se, ParametrosAdicionais parametrosAdicionais) {
		String resposta = null;
		try {
			logger.info("Tratando excecao");
			resposta = retornaMensagem(versaoServico, transacao, this.dadosBarramento, se);
			if (resposta == null || resposta.isEmpty()) {
				resposta = respostaTratar;
			}
			logger.info(Constantes.SITUACAO_FINAL_TRANSACAO_NEGADA);
			atualizaTransacao(transacao, new BigDecimal(2));
			atualizaIteracaoCanal(iteracaoCanal, transacao, resposta);
		} 
		catch (Exception e) {
			logger.warn("Nao foi possivel gravar a mensagem de resposta no banco de dados", e);
		} 
		finally {
			enviarRespostaCanal(resposta, parametrosAdicionais);
			this.processadorEnvioSicco.processarEnvioOnline(transacao, Constantes.ENVIO_UNICA);
		}
	}
	
	/**
	 * Trata a excecao.
	 * 
	 * @param transacao
	 * @param iteracaoCanal
	 * @param transacaoAgendamento
	 * @param ServicoException
	 * @param AgendamentoException
	 */
	public void tratarExcecaoEfetivaAgendamentoPagamento(Mtxtb014Transacao transacaoCorrente, Mtxtb016IteracaoCanal iteracaoCanal,
			Mtxtb034TransacaoAgendamento transacaoAgendamento, Mtxtb011VersaoServico versaoServico, ServicoException se,
			boolean isUltimaExecucao) {

		String resposta = "";
		
		try {
			logger.info("Tratando excecao");
			resposta = retornaMensagem(versaoServico, transacaoCorrente, this.dadosBarramento, se);
			if (resposta == null || resposta.isEmpty()) {
				resposta = respostaTratar;
			}
			logger.info(Constantes.SITUACAO_FINAL_TRANSACAO_NEGADA);
			atualizaTransacao(transacaoCorrente, new BigDecimal(2));
			atualizaIteracaoCanal(iteracaoCanal, transacaoCorrente, resposta);

			if(se.getSistemaOrigem().equals(Constantes.AGENDAMENTO_CODIGOMSGIMPEDITIVO) || isUltimaExecucao) {
				if(isUltimaExecucao) {
					logger.info(ConstantesAgendamento.ULTIMA_EXECUCAO);
						transacaoAgendamento.setIcSituacao(ConstantesAgendamento.SITUACAO_NEGADO);
					logger.info(ConstantesAgendamento.SITUACAO_FINAL_AGENDAMENTO_NEGADO);
				}
			}
			else {
				logger.info("Agendamento Nsu ["+transacaoAgendamento.getNuNsuTransacaoAgendamento()+"] " + "Nao foi Efetivado. Sera realizado novas tentativas no dia.");
			}
		} 
		catch (Exception e) {
			logger.warn("Nao foi possivel gravar a mensagem de resposta no banco de dados", e);
		}
		finally {
			this.processadorEnvioSicco.processarEnvioOnline(transacaoCorrente, Constantes.ENVIO_UNICA);
		}
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

	/**
	 * Sets the simtx config.
	 *
	 * @param simtxConfig the new simtx config
	 */
	public void setSimtxConfig(SimtxConfig simtxConfig) {
		this.simtxConfig = simtxConfig;
	}

	public void setRepositorio(RepositorioArquivo repositorio) {
		this.repositorio = repositorio;
	}

}

