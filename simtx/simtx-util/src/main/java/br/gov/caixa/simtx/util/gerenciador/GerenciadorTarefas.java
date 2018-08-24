package br.gov.caixa.simtx.util.gerenciador;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.xml.sax.SAXException;

import br.gov.caixa.simtx.persistencia.agendamento.entidade.Mtxtb034TransacaoAgendamento;
import br.gov.caixa.simtx.persistencia.cache.core.FornecedorDados;
import br.gov.caixa.simtx.persistencia.constante.AcaoRetorno;
import br.gov.caixa.simtx.persistencia.constante.Constantes;
import br.gov.caixa.simtx.persistencia.constante.MensagemRetorno;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb003ServicoTarefa;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb003ServicoTarefaPK;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb004Canal;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb006Mensagem;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb008MeioEntrada;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb011VersaoServico;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb012VersaoTarefa;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb012VersaoTarefaPK;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb014Transacao;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb015SrvcoTrnsoTrfa;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb015SrvcoTrnsoTrfaPK;
import br.gov.caixa.simtx.util.SimtxConfig;
import br.gov.caixa.simtx.util.StringUtil;
import br.gov.caixa.simtx.util.data.DataUtil;
import br.gov.caixa.simtx.util.enums.CodigosRetornosEnum;
import br.gov.caixa.simtx.util.exception.ServicoException;
import br.gov.caixa.simtx.util.gerenciador.cache.RepositorioArquivo;
import br.gov.caixa.simtx.util.tarefa.OrdenadorTarefa;
import br.gov.caixa.simtx.util.to.TarefasServicoResposta;
import br.gov.caixa.simtx.util.xml.BuscadorResposta;
import br.gov.caixa.simtx.util.xml.BuscadorTextoXml;
import br.gov.caixa.simtx.util.xml.DadosBarramento;
import br.gov.caixa.simtx.util.xml.ParametroXsl;
import br.gov.caixa.simtx.util.xml.Resposta;
import br.gov.caixa.simtx.util.xml.TransformadorXsl;

/**
 * Responsavel por metodos comuns de tarefas
 *
 */
@Stateless
public class GerenciadorTarefas implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private static final Logger logger = Logger.getLogger(GerenciadorTarefas.class);
	
	private static final String SALVANDO_TAREFAS = "Salvando tarefas do servico";
	
	private static final String ERRO_SALVAR_TAREFAS = "erro.salvar.tarefas";
	
	@Inject
	private FornecedorDados fornecedorDados;
	
	@Inject
	private Mensagem mensagemServidor;
	
	@Inject
	protected SimtxConfig simtxConfig;
	
	@Inject
	private ValidadorRegrasNegocio validadorRegrasNegocio;
	
	@Inject 
	private RepositorioArquivo repositorioArquivo;

	/**
	 * Carrega as tarefas do Serviço (Migrado ou Nao Migrado).
	 * 
	 * @param servico
	 * @param canal
	 * @param meioEntrada
	 * @return
	 * @throws ServicoException
	 */
	public List<Mtxtb003ServicoTarefa> carregarTarefasServico(Mtxtb011VersaoServico servico, Mtxtb004Canal canal,
			Mtxtb008MeioEntrada meioEntrada) throws ServicoException {
		try {
			logger.info("Carregando tarefas do servico");
			long numeroMeioEntrada = meioEntrada != null ? meioEntrada.getNuMeioEntrada() : 0L;
			
			if(servico.isMigrado()) {
				return this.fornecedorDados.buscarTarefasExecutar(servico.getId().getNuServico001(),
						servico.getId().getNuVersaoServico(), numeroMeioEntrada, canal.getNuCanal());
			}
			else {
				return this.fornecedorDados.buscarTarefasExecutar(servico.getId().getNuServico001(),
						servico.getId().getNuVersaoServico(), canal.getNuCanal());
			}
		} 
		catch (Exception e) {
			logger.error(this.mensagemServidor.recuperarMensagem("erro.carregar.tarefas"), e);
			Mtxtb006Mensagem mensagemErroInterno = this.fornecedorDados.buscarMensagem(MensagemRetorno.ERRO_INTERNO);
			throw new ServicoException(mensagemErroInterno, Constantes.ORIGEM_SIMTX);
		}
	}
	
	/**
	 * Salva as tarefas do Servico na entidade {@link Mtxtb015SrvcoTrnsoTrfa}
	 * 
	 * @param listaTarefas
	 * @param servico
	 * @param transacao
	 */
	public Resposta salvarTarefasServico(List<Mtxtb003ServicoTarefa> listaTarefas, Mtxtb011VersaoServico servico,
			Mtxtb014Transacao transacao, DadosBarramento dadosBarramento, String mensagemServicoSaida) throws ServicoException {
		try {
			logger.info(SALVANDO_TAREFAS);
			List<Mtxtb015SrvcoTrnsoTrfa> listaTransacaoTarefas = new ArrayList<>();
			Resposta resposta = null;
			for(Mtxtb003ServicoTarefa servicoTarefa : listaTarefas) {
				logger.info("Salvando Tarefa ["+servicoTarefa.getId().getNuTarefa012()+"] "
						+ servicoTarefa.getMtxtb012VersaoTarefa().getMtxtb002Tarefa().getNoTarefa()+"");
				
				String mensagemEntrada = "";
				if(!servicoTarefa.getMtxtb012VersaoTarefa().getDeXsltRequisicao().equals("-"))
					mensagemEntrada = transformarXml(servicoTarefa.getMtxtb012VersaoTarefa().getDeXsltRequisicao(), dadosBarramento);
				
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
		        transacaoTarefa.setDeXmlRequisicao(mensagemEntrada);
		        transacaoTarefa.setDeXmlResposta(mensagemSaida);
		        transacaoTarefa.setMtxtb012VersaoTarefa(servicoTarefa.getMtxtb012VersaoTarefa());
		        transacaoTarefa.setNsuCorp(0L);
		        this.fornecedorDados.salvarTransacaoTarefa(transacaoTarefa);
		        listaTransacaoTarefas.add(transacaoTarefa);
		        
		        if(servicoTarefa.isImpeditiva())
		        	resposta = validarTransacaoTarefa(transacaoTarefa, mensagemServicoSaida);
			}
			return resposta;
		} 
		catch (ServicoException se) {
			throw se;
		}
		catch (Exception e) {
			logger.error(this.mensagemServidor.recuperarMensagem(ERRO_SALVAR_TAREFAS), e);
			Mtxtb006Mensagem mensagemErroInterno = this.fornecedorDados.buscarMensagem(MensagemRetorno.ERRO_INTERNO);
			throw new ServicoException(mensagemErroInterno, Constantes.ORIGEM_SIMTX);
		}
	}
	
	/**
	 * valida se tarefa possui impedimento
	 * @param tarefasServicoResposta
	 * @throws ServicoException
	 */
	public void validarTarefasServicoResposta(TarefasServicoResposta tarefasServicoResposta) throws ServicoException {
		logger.info("validando Tarefas");
		try {
			if(tarefasServicoResposta.isPossuiTarefaImpeditiva()) {
				logger.info("Possui Tarefa com impedimento");
				Mtxtb006Mensagem mensagemErroInterno = this.fornecedorDados.buscarMensagem(MensagemRetorno.ERRO_INTERNO);
				throw new ServicoException(mensagemErroInterno, Constantes.ORIGEM_SIMTX);
			}
		} 
		catch (ServicoException se) {
			throw se;
		}
		catch (Exception e) {
			logger.error(this.mensagemServidor.recuperarMensagem(ERRO_SALVAR_TAREFAS), e);
			Mtxtb006Mensagem mensagemErroInterno = this.fornecedorDados.buscarMensagem(MensagemRetorno.ERRO_INTERNO);
			throw new ServicoException(mensagemErroInterno, Constantes.ORIGEM_SIMTX);
		}
	}
	
	/**
	 * 
	 * @param listaTarefas
	 * @param servico
	 * @param transacao
	 * @throws Exception 
	 */
	public TarefasServicoResposta montarTarefasResposta(List<Mtxtb003ServicoTarefa> listaTarefas, Mtxtb011VersaoServico servico,
			Mtxtb014Transacao transacao, DadosBarramento dadosBarramento, String xmlSaidaSibar) {
		logger.info("inicianlizando tarefas do servico");
		
		TarefasServicoResposta tarefasServicoResposta = new TarefasServicoResposta();
		List<Mtxtb015SrvcoTrnsoTrfa> listaTransacaoTarefas = new ArrayList<>();
		Resposta resposta = null;
		try {
			for(Mtxtb003ServicoTarefa servicoTarefa : listaTarefas) {
				logger.info("Montando Tarefas ["+servicoTarefa.getId().getNuTarefa012()+"] "
						+ servicoTarefa.getMtxtb012VersaoTarefa().getMtxtb002Tarefa().getNoTarefa()+"");
				
				String mensagemEntrada = "";
				if(!servicoTarefa.getMtxtb012VersaoTarefa().getDeXsltRequisicao().equals("-")) {
					mensagemEntrada = transformarXml(servicoTarefa.getMtxtb012VersaoTarefa().getDeXsltRequisicao(), dadosBarramento);
				}
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
		        transacaoTarefa.setDeXmlRequisicao(mensagemEntrada);
		        transacaoTarefa.setDeXmlResposta(mensagemSaida);
		        transacaoTarefa.setMtxtb012VersaoTarefa(servicoTarefa.getMtxtb012VersaoTarefa());
		        transacaoTarefa.setNsuCorp(0L);
		        listaTransacaoTarefas.add(transacaoTarefa);
		        
		        if(servicoTarefa.isImpeditiva()) {
		        	resposta = validarTransacaoTarefa(transacaoTarefa, xmlSaidaSibar);
		        }
			}
		}
		catch (ServicoException e) {
			resposta = criarResposta(e);
		}
		catch (Exception e) {
			logger.error(e);
			tarefasServicoResposta.setPossuiTarefaImpeditiva(true);
		}
		
		tarefasServicoResposta.setResposta(resposta);
		tarefasServicoResposta.setListaTransacaoTarefas(listaTransacaoTarefas);
		tarefasServicoResposta.setXmlSaidaSibar(xmlSaidaSibar);
		logger.info("finalizando tarefas do servico");
		return tarefasServicoResposta;
	}
	
	private Resposta criarResposta(ServicoException e) {
		Resposta resposta = new Resposta();
		resposta.setAcao(AcaoRetorno.IMPEDITIVA.getRotulo());
		resposta.setCodigo(e.getMensagem().getCodigoRetorno());
		resposta.setIcTipoMensagem(AcaoRetorno.AUTORIZADORA.getTipo());
		resposta.setMensagemNegocial(e.getMensagem().getDeMensagemNegocial());
		resposta.setMensagemTecnica(e.getMensagem().getDeMensagemTecnica());
		resposta.setOrigem(e.getSistemaOrigem());
		return resposta;
	}
	
	/**
	 * 
	 * @param listaTarefas
	 * @param servico
	 * @param transacao
	 * @throws Exception 
	 */
	public TarefasServicoResposta montarTarefasRespostaSemValidacao(List<Mtxtb003ServicoTarefa> listaTarefas, Mtxtb011VersaoServico servico,
			Mtxtb014Transacao transacao, DadosBarramento dadosBarramento) {
			logger.info("iniciando tarefas sem validacao");
			
			TarefasServicoResposta tarefasServicoResposta = new TarefasServicoResposta();
			List<Mtxtb015SrvcoTrnsoTrfa> listaTransacaoTarefas = new ArrayList<>();
			try {
				logger.info("Montando tarefas do servico");
				for(Mtxtb003ServicoTarefa servicoTarefa : listaTarefas) {
					logger.info("Montando Tarefas ["+servicoTarefa.getId().getNuTarefa012()+"] "
							+ servicoTarefa.getMtxtb012VersaoTarefa().getMtxtb002Tarefa().getNoTarefa()+"");
					
					String mensagemEntrada = "";
					if(!servicoTarefa.getMtxtb012VersaoTarefa().getDeXsltRequisicao().equals("-")) {
						mensagemEntrada = transformarXml(servicoTarefa.getMtxtb012VersaoTarefa().getDeXsltRequisicao(), dadosBarramento);
					}
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
			        transacaoTarefa.setDeXmlRequisicao(mensagemEntrada);
			        transacaoTarefa.setDeXmlResposta(mensagemSaida);
			        transacaoTarefa.setMtxtb012VersaoTarefa(servicoTarefa.getMtxtb012VersaoTarefa());
			        transacaoTarefa.setNsuCorp(0L);
			        listaTransacaoTarefas.add(transacaoTarefa);
				}
			}
			catch (Exception e) {
				logger.error(e);
			}
			tarefasServicoResposta.setListaTransacaoTarefas(listaTransacaoTarefas);
			logger.info("finalizando tarefas sem validacao");
		return tarefasServicoResposta;
	}
	
	public Resposta validarTransacaoTarefa(Mtxtb015SrvcoTrnsoTrfa srvTrnsoTrfa, String mensagemServicoSaida)
			throws ServicoException, ParserConfigurationException, SAXException, IOException {
		
			Mtxtb012VersaoTarefa versaoTarefa = null;
			BuscadorTextoXml buscaXmlResp = null;
			BuscadorTextoXml buscaXmlSaida = null;
			
			if(!"".equals(srvTrnsoTrfa.getDeXmlResposta())){
				buscaXmlResp = new BuscadorTextoXml(srvTrnsoTrfa.getDeXmlResposta());	
			}else{
				buscaXmlResp = new BuscadorTextoXml("<vazio></vazio>");
			}
			
			if(!"".equals(mensagemServicoSaida)){
				buscaXmlSaida = new BuscadorTextoXml(mensagemServicoSaida);
			}
			
			if (null != srvTrnsoTrfa.getMtxtb012VersaoTarefa()) {
				versaoTarefa = srvTrnsoTrfa.getMtxtb012VersaoTarefa();	
			}
			
			return this.validadorRegrasNegocio.verificarCodigoImpeditivo(buscaXmlResp,
					buscaXmlSaida, versaoTarefa);
	}
	
	public int quantidadeTarefasComErro(List<Mtxtb015SrvcoTrnsoTrfa> listaTransacaoTarefa) {
		int erros = 0;
		
		for(Mtxtb015SrvcoTrnsoTrfa tt : listaTransacaoTarefa) {
			try {
				
				if(StringUtil.isEmpty(tt.getDeXmlResposta())){
					erros++;
				} else {
					BuscadorTextoXml buscador = new BuscadorTextoXml(tt.getDeXmlResposta());
					Resposta respostaTarefaXml = new BuscadorResposta().buscarRespostaTarefaBarramento(buscador);
					Mtxtb006Mensagem mensagem = this.fornecedorDados.buscarMensagemPorTarefaCodRetorno(
							respostaTarefaXml.getCodigo(), tt.getMtxtb012VersaoTarefa()).getMtxtb006Mensagem();
					
					if(mensagem.isImpeditiva()) erros++;
				}
			} catch (Exception e) {
				erros++;
			}
		}
		
		return erros;
	}

	/**
	 * Salva as tarefas do Servico na entidade {@link Mtxtb015SrvcoTrnsoTrfa}
	 * 
	 * @param listaTarefas
	 * @param servico
	 * @param transacao
	 */
	public List<Mtxtb015SrvcoTrnsoTrfa> salvarTarefasServicoSemValidar(List<Mtxtb003ServicoTarefa> listaTarefas, Mtxtb011VersaoServico servico,
			Mtxtb014Transacao transacao, DadosBarramento dadosBarramento) throws ServicoException {
		List<Mtxtb015SrvcoTrnsoTrfa> listaTransacaoTarefas = new ArrayList<>();
		try {
			logger.info(SALVANDO_TAREFAS);
			for(Mtxtb003ServicoTarefa servicoTarefa : listaTarefas) {
				Mtxtb015SrvcoTrnsoTrfa transacaoTarefa = salvarTarefaServicoSemValidar(servicoTarefa, servico, transacao, dadosBarramento);
				listaTransacaoTarefas.add(transacaoTarefa);
			}
			return listaTransacaoTarefas;
		} 
		catch (ServicoException se) {
			throw se;
		}
		catch (Exception e) {
			logger.error(this.mensagemServidor.recuperarMensagem(ERRO_SALVAR_TAREFAS), e);
			Mtxtb006Mensagem mensagemErroInterno = this.fornecedorDados.buscarMensagem(MensagemRetorno.ERRO_INTERNO);
			throw new ServicoException(mensagemErroInterno, Constantes.ORIGEM_SIMTX);
		}
	}
	
	/**
	 * Salva as tarefas do Servico na entidade {@link Mtxtb015SrvcoTrnsoTrfa}
	 * 
	 * @param listaTarefas
	 * @param servico
	 * @param transacao
	 */
	public Mtxtb015SrvcoTrnsoTrfa salvarTarefaServicoSemValidar(Mtxtb003ServicoTarefa tarefa, Mtxtb011VersaoServico servico,
			Mtxtb014Transacao transacao, DadosBarramento dadosBarramento) throws ServicoException {
		try {
			logger.info("Salvando tarefa do servico");
			logger.info("Salvando Tarefa ["+tarefa.getId().getNuTarefa012()+"] "
					+ tarefa.getMtxtb012VersaoTarefa().getMtxtb002Tarefa().getNoTarefa()+"");
			
			String mensagemEntrada = "";
			if(!tarefa.getMtxtb012VersaoTarefa().getDeXsltRequisicao().equals("-"))
				mensagemEntrada = transformarXml(tarefa.getMtxtb012VersaoTarefa().getDeXsltRequisicao(), dadosBarramento);
			
			String mensagemSaida = transformarXml(tarefa.getMtxtb012VersaoTarefa().getDeXsltResposta(), dadosBarramento);
			
			Mtxtb015SrvcoTrnsoTrfa transacaoTarefa = new Mtxtb015SrvcoTrnsoTrfa();
	        transacaoTarefa.setId(new Mtxtb015SrvcoTrnsoTrfaPK());
	        transacaoTarefa.getId().setNuNsuTransacao017(transacao.getNuNsuTransacao());
	        transacaoTarefa.getId().setNuTarefa012(tarefa.getMtxtb012VersaoTarefa().getId().getNuTarefa002());
	        transacaoTarefa.getId().setNuVersaoTarefa012(tarefa.getMtxtb012VersaoTarefa().getId().getNuVersaoTarefa());
	        transacaoTarefa.getId().setNuServico017(servico.getId().getNuServico001());
	        transacaoTarefa.getId().setNuVersaoServico017(servico.getId().getNuVersaoServico());
	        transacaoTarefa.setTsExecucaoTransacao(DataUtil.getDataAtual());
	        transacaoTarefa.setDtReferencia(transacao.getDtReferencia());
	        transacaoTarefa.setDeXmlRequisicao(mensagemEntrada);
	        transacaoTarefa.setDeXmlResposta(mensagemSaida);
	        transacaoTarefa.setNsuCorp(0L);
	        transacaoTarefa.setMtxtb012VersaoTarefa(tarefa.getMtxtb012VersaoTarefa());
	        this.fornecedorDados.salvarTransacaoTarefa(transacaoTarefa);
	        
	        return transacaoTarefa;
		} 
		catch (Exception e) {
			logger.error(this.mensagemServidor.recuperarMensagem(ERRO_SALVAR_TAREFAS), e);
			Mtxtb006Mensagem mensagemErroInterno = this.fornecedorDados.buscarMensagem(MensagemRetorno.ERRO_INTERNO);
			throw new ServicoException(mensagemErroInterno, Constantes.ORIGEM_SIMTX);
		}
	}
	
	/**
	 * Gera o xml das tarefas do Servico.
	 * 
	 * @param listaTarefas
	 * @param servico
	 */
	public List<Mtxtb015SrvcoTrnsoTrfa> gerarXmlTarefas(List<Mtxtb003ServicoTarefa> listaTarefas,
			Mtxtb011VersaoServico servico, DadosBarramento dadosBarramento) throws ServicoException {
		List<Mtxtb015SrvcoTrnsoTrfa> listaTransacaoTarefas = new ArrayList<>();
		try {
			logger.info("Gerando xml das tarefas");
			for(Mtxtb003ServicoTarefa servicoTarefa : listaTarefas) {
				String mensagemEntrada = "";
				if(!servicoTarefa.getMtxtb012VersaoTarefa().getDeXsltRequisicao().equals("-"))
					mensagemEntrada = transformarXml(servicoTarefa.getMtxtb012VersaoTarefa().getDeXsltRequisicao(), dadosBarramento);
				
				String mensagemSaida = transformarXml(servicoTarefa.getMtxtb012VersaoTarefa().getDeXsltResposta(), dadosBarramento);
				
				Mtxtb015SrvcoTrnsoTrfa transacaoTarefa = new Mtxtb015SrvcoTrnsoTrfa();
		        transacaoTarefa.setId(new Mtxtb015SrvcoTrnsoTrfaPK());
		        transacaoTarefa.getId().setNuTarefa012(servicoTarefa.getMtxtb012VersaoTarefa().getId().getNuTarefa002());
		        transacaoTarefa.getId().setNuVersaoTarefa012(servicoTarefa.getMtxtb012VersaoTarefa().getId().getNuVersaoTarefa());
		        transacaoTarefa.getId().setNuServico017(servico.getId().getNuServico001());
		        transacaoTarefa.getId().setNuVersaoServico017(servico.getId().getNuVersaoServico());
		        transacaoTarefa.setTsExecucaoTransacao(DataUtil.getDataAtual());
		        transacaoTarefa.setDeXmlRequisicao(mensagemEntrada);
		        transacaoTarefa.setDeXmlResposta(mensagemSaida);
		        transacaoTarefa.setNsuCorp(0L);
		        transacaoTarefa.setMtxtb012VersaoTarefa(servicoTarefa.getMtxtb012VersaoTarefa());
		        listaTransacaoTarefas.add(transacaoTarefa);
			}
		} 
		catch (Exception e) {
			logger.error("Erro ao gerar xml das tarefas", e);
			Mtxtb006Mensagem mensagemErroInterno = this.fornecedorDados.buscarMensagem(MensagemRetorno.ERRO_INTERNO);
			throw new ServicoException(mensagemErroInterno, Constantes.ORIGEM_SIMTX);
		}
		return listaTransacaoTarefas;
	}
	
	/**
	 * Salva as tarefas do Servico na entidade {@link Mtxtb015SrvcoTrnsoTrfa}
	 * 
	 * @param listaTarefas
	 * @param servico
	 * @param transacao
	 * @throws Exception 
	 */
	public void salvarSrvcoTrnsoTarefas(List<Mtxtb015SrvcoTrnsoTrfa> listaTarefas) throws ServicoException {
		try {
			logger.info(SALVANDO_TAREFAS);
			for (Mtxtb015SrvcoTrnsoTrfa mtxtb015SrvcoTrnsoTrfa : listaTarefas) {
			    this.fornecedorDados.salvarTransacaoTarefa(mtxtb015SrvcoTrnsoTrfa);
			}
		} 
		catch (Exception se) {
			Mtxtb006Mensagem mensagemErroInterno = this.fornecedorDados.buscarMensagem(MensagemRetorno.ERRO_INTERNO);
			throw new ServicoException(mensagemErroInterno, Constantes.ORIGEM_SIMTX);
		}
	}
	
	/**
	 * Gera o xml de resposta da Tarefa para verificar se eh codigo Impeditivo. 
	 * Caso seja impeditivo, verifica se eh Impeditivo para Agendamento na classe
	 * CodigosRetornosEnum.
	 * 
	 * @param servicoEfetivaBoleto
	 * @param transacao
	 * @param listaTarefas
	 * @param xmlSaidaSibar
	 * @throws ServicoException
	 * @throws AgendamentoException
	 */
	public Resposta validarCodigoImpeditivoAgendamento(Mtxtb014Transacao transacaoCorrente, List<Mtxtb003ServicoTarefa> listaTarefas, DadosBarramento dadosBarramento, String xmlSaidaSibar,
			boolean isUltimaExecucao) throws ServicoException {
		
		Resposta resposta = null;
		for (Mtxtb003ServicoTarefa servicoTarefa : listaTarefas) {
			if(servicoTarefa.isImpeditiva()) {
				String xmlRespostaTarefa = transformarTransacaoAgendamentoXml(
						servicoTarefa.getMtxtb012VersaoTarefa().getDeXsltResposta(), dadosBarramento, transacaoCorrente,
						false, false);
				
				resposta = verificarCodigoImpeditivoAgendamento(xmlRespostaTarefa, servicoTarefa, xmlSaidaSibar, isUltimaExecucao);
			}
		}
		return resposta;
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
	public Resposta verificarCodigoImpeditivoAgendamento(String xmlRespostaTarefa, Mtxtb003ServicoTarefa servicoTarefa,
			String xmlSaidaSibar, boolean isUltimaExecucao)
			throws ServicoException {

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
				throw new ServicoException(msg, Constantes.AGENDAMENTO_CODIGOMSGIMPEDITIVO);
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
			boolean isUltimaExecucao) throws ServicoException {
		
		if(isUltimaExecucao) {
			throw new ServicoException(e.getMensagem(), Constantes.AGENDAMENTO_CODIGOMSGIMPEDITIVO);
		}
		else if(!Constantes.MENSAGEM_ERRO_DESCONHECIDO.equals(e.getMensagem().getDeMensagemNegocial())) {
			CodigosRetornosEnum codigoEnum = CodigosRetornosEnum.obterCodigo(e.getMensagem().getCoMensagem(),
					servicoTarefa.getMtxtb012VersaoTarefa().getId().getNuTarefa002());
			
			if(codigoEnum != null && AcaoRetorno.IMPEDITIVA.getTipo().equals(codigoEnum.getTipoCodigo())) {
				throw new ServicoException(e.getMensagem(), Constantes.AGENDAMENTO_CODIGOMSGIMPEDITIVO);
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
	 * Envia para o sibar as tarefas durante as tentativas do dia.
	 * 
	 * @param servicoEfetivaBoleto
	 * @param transacaoCorrente
	 * @param listaTarefas
	 * @param isUltimaExecucao
	 * @param isBoletoCaixa
	 * @throws ServicoException
	 * @throws AgendamentoException
	 */
	public TarefasServicoResposta validarCodigoImpeditivoAgendamento(Mtxtb014Transacao transacao, List<Mtxtb003ServicoTarefa> listaTarefas, 
				DadosBarramento dadosBarramento, String xmlSaidaSibar, boolean isUltimaExecucao, TarefasServicoResposta tarefasServicoResposta) throws ServicoException {
		if (tarefasServicoResposta == null ) {
			tarefasServicoResposta = new TarefasServicoResposta();		
		}
		Resposta resposta = validarCodigoImpeditivoAgendamento(transacao, listaTarefas, dadosBarramento, xmlSaidaSibar, isUltimaExecucao);
		if (null!=resposta) {
			tarefasServicoResposta.setResposta(resposta);
		}	
		return tarefasServicoResposta;
	}
	
	/**
	 * Envia tarefas diferente na ultima tentativa de execucao do dia.
	 * 
	 * @param servicoEfetivaBoleto
	 * @param transacaoAgendamento
	 * @param transacaoCorrente
	 * @param canal
	 * @param listaTarefas
	 * @param isBoletoCaixa
	 * @throws ServicoException
	 */
	public TarefasServicoResposta processarUltimaExecucao(Mtxtb011VersaoServico servico, 
			Mtxtb034TransacaoAgendamento transacaoAgendamento, Mtxtb014Transacao transacao,
			List<Mtxtb003ServicoTarefa> listaTarefas, DadosBarramento dadosBarramento) throws ServicoException {
		
		boolean isBoletoCaixa = verificarBoletoCaixa(transacaoAgendamento);
		
		/**
		 * Executa apenas as tarefas Debito e Baixa Operacional.
		 */
		if (isBoletoCaixa && !listaTarefas.isEmpty() && listaTarefas.size() > 2) {
			listaTarefas = listaTarefas.subList(2, listaTarefas.size());
		}
		
		return montarTarefasRespostaSemValidacao(listaTarefas, servico, transacao, dadosBarramento);
	}
	
	/**
	 * Verifica se eh Boleto Caixa.
	 * 
	 * @param transacaoAgendamento
	 * @return
	 * @throws ServicoException
	 */
	public boolean verificarBoletoCaixa(Mtxtb034TransacaoAgendamento transacaoAgendamento) throws ServicoException {
		try {
			BuscadorTextoXml buscador = new BuscadorTextoXml(transacaoAgendamento.getDeXmlAgendamento());
			String boleto = buscador.xpathTexto("/*[1]/PAGAMENTO_BOLETO/INFORMACOES_BOLETO/CODIGO_BARRAS");
			boleto = boleto.substring(0, 3);
			if(boleto.equals("104"))
				return true;
		} 
		catch (Exception e) {
			logger.error(e);
			Mtxtb006Mensagem mensagemErroInterno = this.fornecedorDados.buscarMensagem(MensagemRetorno.ERRO_INTERNO);
			throw new ServicoException(mensagemErroInterno, Constantes.ORIGEM_SIMTX);
		}
		return false;
	}
	
	public List<Mtxtb003ServicoTarefa> adicionarTarefaCalculaBoletoParaListaTarefas(Mtxtb011VersaoServico servico, List<Mtxtb003ServicoTarefa> listaTarefasBanco) {
		if (listaTarefasBanco == null) {
			listaTarefasBanco = new ArrayList<>();
		}
		Mtxtb012VersaoTarefaPK pk = new Mtxtb012VersaoTarefaPK();
		pk.setNuTarefa002(Constantes.NU_TAREFA_CALCULA_BOLETO);
		pk.setNuVersaoTarefa(1);

		Mtxtb012VersaoTarefa tarefaCalculaBoleto = this.fornecedorDados.buscarVersaoTarefaPorPK(pk);
		
		List<Mtxtb003ServicoTarefa> listaTarefas = new ArrayList<>();
		listaTarefas.add(criarServicoTarefaCalculaBoleto(servico, tarefaCalculaBoleto));
		listaTarefas.addAll(listaTarefasBanco);
		
		Collections.sort(listaTarefas, new OrdenadorTarefa());
		
		return listaTarefas;
	}
	
	
	public List<Mtxtb003ServicoTarefa> buscarTarefasValidaAgendamento(Mtxtb011VersaoServico servico, Mtxtb004Canal canal,
			Mtxtb008MeioEntrada meioEntrada) throws ServicoException {
		
		if(servico.getId().getNuServico001() == 110038l) {
			return this.carregarTarefasServico(servico, canal, meioEntrada);
		}
		else {
			Mtxtb012VersaoTarefaPK pk = new Mtxtb012VersaoTarefaPK();
			pk.setNuTarefa002(Constantes.NU_TAREFA_CALCULA_BOLETO);
			pk.setNuVersaoTarefa(1);
			
			Mtxtb012VersaoTarefa tarefaCalculaBoleto = this.fornecedorDados.buscarVersaoTarefaPorPK(pk);
			
			List<Mtxtb003ServicoTarefa> listaTarefasBanco = this.carregarTarefasServico(servico, canal,
					meioEntrada);
			
			List<Mtxtb003ServicoTarefa> listaTarefas = new ArrayList<>();
			listaTarefas.add(criarServicoTarefaCalculaBoleto(servico, tarefaCalculaBoleto));
			listaTarefas.addAll(listaTarefasBanco);
			
			Collections.sort(listaTarefas, new OrdenadorTarefa());
			
			return listaTarefas;
		}
	}
	
	public Mtxtb003ServicoTarefa criarServicoTarefaCalculaBoleto(Mtxtb011VersaoServico servico, Mtxtb012VersaoTarefa versaoTarefa) {
		Mtxtb003ServicoTarefa servicoTarefaCalculaBoleto = new Mtxtb003ServicoTarefa();
		servicoTarefaCalculaBoleto.setIcImpedimento(Constantes.SIM);
		servicoTarefaCalculaBoleto.setIcSituacao(Constantes.SIM);
		Mtxtb003ServicoTarefaPK id = new Mtxtb003ServicoTarefaPK();
		
		id.setNuTarefa012(versaoTarefa.getId().getNuTarefa002());
		id.setNuVersaoTarefa012(versaoTarefa.getId().getNuVersaoTarefa());
		
		servicoTarefaCalculaBoleto.setId(id);
		servicoTarefaCalculaBoleto.setMtxtb012VersaoTarefa(versaoTarefa);
		servicoTarefaCalculaBoleto.setMtxtb011VersaoServico(servico);
		servicoTarefaCalculaBoleto.setNuSequenciaExecucao(2);

		return servicoTarefaCalculaBoleto;
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
	 * Transforma o xml em outro a partir do xslt.
	 * 
	 * @param xslt
	 * @param dadosBarramento
	 * @return
	 * @throws ServicoException
	 */
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

	public void setFornecedorDados(FornecedorDados fornecedorDados) {
		this.fornecedorDados = fornecedorDados;
	}
	
	public void setMensagemServidor(Mensagem mensagemServidor) {
		this.mensagemServidor = mensagemServidor;
	}

	public void setSimtxConfig(SimtxConfig simtxConfig) {
		this.simtxConfig = simtxConfig;
	}
	
}