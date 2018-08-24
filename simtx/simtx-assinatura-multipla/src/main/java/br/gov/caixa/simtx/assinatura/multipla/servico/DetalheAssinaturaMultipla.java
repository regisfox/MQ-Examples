package br.gov.caixa.simtx.assinatura.multipla.servico;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.apache.log4j.Logger;

import br.gov.caixa.simtx.persistencia.assinaturamultipla.entidade.Mtxtb027TransacaoAssinaturaMultipla;
import br.gov.caixa.simtx.persistencia.atualizador.dados.AtualizadorDadosTransacao;
import br.gov.caixa.simtx.persistencia.cache.assinaturamultipla.FornecedorDadosAssinaturaMultipla;
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
import br.gov.caixa.simtx.persistencia.exception.SemResultadoException;
import br.gov.caixa.simtx.util.ParametrosAdicionais;
import br.gov.caixa.simtx.util.SimtxConfig;
import br.gov.caixa.simtx.util.exception.ServicoException;
import br.gov.caixa.simtx.util.gerenciador.GerenciadorTransacao;
import br.gov.caixa.simtx.util.gerenciador.TratadorDeExcecao;
import br.gov.caixa.simtx.util.gerenciador.ValidadorRegrasNegocio;
import br.gov.caixa.simtx.util.gerenciador.cache.RepositorioArquivo;
import br.gov.caixa.simtx.util.gerenciador.servico.ProcessadorServicos;
import br.gov.caixa.simtx.util.to.DadosTransacaoComuns;
import br.gov.caixa.simtx.util.to.TarefasServicoResposta;
import br.gov.caixa.simtx.util.xml.BuscadorTextoXml;
import br.gov.caixa.simtx.util.xml.DadosBarramento;
import br.gov.caixa.simtx.util.xml.ParametroXsl;
import br.gov.caixa.simtx.util.xml.Resposta;
import br.gov.caixa.simtx.util.xml.TransformadorXsl;

public class DetalheAssinaturaMultipla extends GerenciadorTransacao implements ProcessadorServicos {

	private static final long serialVersionUID = 6667637926732839089L;
	
	private static final Logger logger = Logger.getLogger(DetalheAssinaturaMultipla.class);
	
	private static final String PROCESSO_FINALIZADO = " ==== Processo Detalhe Assinatura Multipla Finalizado ==== ";
	
	private DadosBarramento dadosBarramento;
	
	@Inject
	private FornecedorDadosAssinaturaMultipla fornecedorDadosAssinaturaMultipla;
	
	@Inject
	private AtualizadorDadosTransacao atualizadorDadosTransacao;
	
	@Inject
	private TratadorDeExcecao tratadorDeExcecao;

	@Inject
	private RepositorioArquivo repositorioArquivo;
	
	@Inject
	private SimtxConfig simtxConfig;
	

	@Override
	public void processar(ParametrosAdicionais parametrosAdicionais) {
		Mtxtb014Transacao transacao = null;
		Mtxtb016IteracaoCanal iteracaoCanal = null;
		Mtxtb011VersaoServico servico = null;
		Mtxtb004Canal canal = null;
		
		TarefasServicoResposta tarefasServicoResposta = new TarefasServicoResposta();
		List<Mtxtb003ServicoTarefa> listaTarefas = null;
		
		try {
			logger.info(" ==== Processo Lista Assinatura Multipla Iniciado ==== ");
			
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
			meioEntrada = this.fornecedorDados.buscarMeioEntrada(nuMeioEntrada);

			this.validadorRegrasNegocio.validarRegrasMigrado(parametrosAdicionais.getXmlMensagem(), canal, meioEntrada, servico);
			
			Mtxtb027TransacaoAssinaturaMultipla assinaturaMultipla = buscarTransacaoAssinaturaMultipla();
			
			logger.info("Preparando xml de resposta para enviar ao Canal");
			String xmlSaidaCanal = transformarXml(servico, transacao, assinaturaMultipla);
			logger.info("Enviando resposta para Canal");
			enviarRespostaCanal(xmlSaidaCanal, parametrosAdicionais);
			
			
			logger.info("Atualizando informacoes das tarefas e transacao");
			BigDecimal situacaoTransacao = this.validadorRegrasNegocio.situacaoFinalTransacao(servico);
			transacao = atualizaStatusTransacao(transacao, situacaoTransacao);
			iteracaoCanal = montaMtxtb016IteracaoCanalTansacao(iteracaoCanal, transacao, xmlSaidaCanal);
			
			boolean statusAtualizacaoTransacao = this.atualizadorDadosTransacao.atualizarDadosTransacaoTarefasIteracoes(transacao, tarefasServicoResposta.getListaTransacaoTarefas(), iteracaoCanal);
			
			direcionaProcessadorMensagemTransacao(true, statusAtualizacaoTransacao, transacao, servico, this.dadosBarramento, "");
			
			logger.info(PROCESSO_FINALIZADO);
		} 
		catch (ServicoException se) {
			logger.error(se.getMensagem());
			parametrosAdicionais.setDadosBarramento(this.dadosBarramento);
			parametrosAdicionais.setXmlMensagem("");
			DadosTransacaoComuns dadosTransacaoComuns = new DadosTransacaoComuns(listaTarefas, servico, transacao,tarefasServicoResposta.getListaTransacaoTarefas(), iteracaoCanal);
			this.tratadorDeExcecao.tratarExcecaoRetomada(dadosTransacaoComuns, se, parametrosAdicionais, true);
			logger.info(PROCESSO_FINALIZADO);
		} 
		catch (Exception e) {
			logger.error(e);
			Mtxtb006Mensagem mensagemErroInterno = this.fornecedorDados.buscarMensagem(MensagemRetorno.ERRO_INTERNO);
			ServicoException se = new ServicoException(mensagemErroInterno, Constantes.ORIGEM_SIMTX);
			parametrosAdicionais.setDadosBarramento(dadosBarramento);
			parametrosAdicionais.setXmlMensagem("");
			DadosTransacaoComuns dadosTransacaoComuns = new DadosTransacaoComuns(listaTarefas, servico, transacao,tarefasServicoResposta.getListaTransacaoTarefas(), iteracaoCanal);
			this.tratadorDeExcecao.tratarExcecaoRetomada(dadosTransacaoComuns, se, parametrosAdicionais, true);
			logger.info(PROCESSO_FINALIZADO);
		}
	}
	
	public Mtxtb027TransacaoAssinaturaMultipla buscarTransacaoAssinaturaMultipla() throws ServicoException {
		long nsuTransacaoSimtx = 0l;
		try {
			nsuTransacaoSimtx = Long.parseLong(this.dadosBarramento.xpathTexto("/BUSDATA/*[2]/NSU_MTX"));
			
			Mtxtb027TransacaoAssinaturaMultipla assinaturaMultipla = this.fornecedorDadosAssinaturaMultipla
					.buscarAssinaturaMultipla(nsuTransacaoSimtx);
			
			this.dadosBarramento.escrever(assinaturaMultipla.getXmlNegocial());
			
			return assinaturaMultipla;
		} 
		catch (SemResultadoException e) {
			logger.error("Nenhuma transacao de assinatura multipla encontrada para o Nsu ["+nsuTransacaoSimtx+"]");
			Mtxtb006Mensagem mensagemErroInterno = this.fornecedorDados
					.buscarMensagem(MensagemRetorno.ERRO_RECUPERAR_DETALHE_ASSINATURA_MULTIPLA);
			throw new ServicoException(mensagemErroInterno, Constantes.ORIGEM_SIMTX);
		}
		catch (Exception e) {
			logger.error(e);
			Mtxtb006Mensagem mensagemErroInterno = this.fornecedorDados.buscarMensagem(MensagemRetorno.ERRO_INTERNO);
			throw new ServicoException(mensagemErroInterno, Constantes.ORIGEM_SIMTX);
		}
	}
	
	private Resposta criarRespostaSucesso() {
		Resposta resposta = new Resposta();
		Mtxtb006Mensagem mensagem = this.fornecedorDados.buscarMensagem(MensagemRetorno.SUCESSO);
		resposta.setAcao(AcaoRetorno.AUTORIZADORA.getRotulo());
		resposta.setCodigo(mensagem.getCodigoRetorno());
		resposta.setMensagemNegocial(mensagem.getDeMensagemNegocial());
		resposta.setMensagemTecnica(mensagem.getDeMensagemTecnica());
		return resposta;
	}

	/**
	 * Transforma o xml em outro a partir do xslt.
	 * 
	 * @param versaoServico
	 * @param transacao
	 * @param mensagem
	 * @return
	 * @throws ServicoException
	 */
	public String transformarXml(Mtxtb011VersaoServico versaoServico, Mtxtb014Transacao transacao,
			Mtxtb027TransacaoAssinaturaMultipla assinaturaMultipla) throws ServicoException {
		logger.info("inicializando tranformacao xml");
		try {
			Resposta mensagem = criarRespostaSucesso();
			
			List<ParametroXsl> parametrosNovos = new ArrayList<>();
			parametrosNovos.add(new ParametroXsl(ParametroXsl.XSL_PARAM_NSU_SIMTX, String.valueOf(transacao.getNuNsuTransacao())));
			parametrosNovos.add(new ParametroXsl(ParametroXsl.XSL_PARAM_COD_RETORNO, mensagem.getCodigo()));
			parametrosNovos.add(new ParametroXsl(ParametroXsl.XSL_PARAM_ACAO_RETORNO, AcaoRetorno.recuperarAcao(mensagem.getIcTipoMensagem()).getRotulo()));
			parametrosNovos.add(new ParametroXsl(ParametroXsl.XSL_PARAM_ORIGEM_RETORNO, mensagem.getOrigem()));
			parametrosNovos.add(new ParametroXsl(ParametroXsl.XSL_PARAM_MENSAGEM_NEGOCIAL, mensagem.getMensagemNegocial()));
			parametrosNovos.add(new ParametroXsl(ParametroXsl.XSL_PARAM_MENSAGEM_TECNICA, mensagem.getMensagemTecnica()));
			
			if(assinaturaMultipla != null) {
				parametrosNovos.add(new ParametroXsl("nsuSiper", Long.toString(assinaturaMultipla.getNsuPermissao())));
				parametrosNovos.add(new ParametroXsl("dataTransacao", assinaturaMultipla.getDataPermissao().toString()));
				parametrosNovos.add(new ParametroXsl("dataPrevistaEfetivacao", assinaturaMultipla.getDataEfetivacao().toString()));
				parametrosNovos.add(new ParametroXsl("nsuAssinaturaMultipla", Long.toString(assinaturaMultipla.getNsuAssinaturaMultipla())));
			}
			
			ParametroXsl[] pArr = new ParametroXsl[parametrosNovos.size()];
			
			String caminhoXls = this.simtxConfig.getCaminhoXslt() + versaoServico.getDeXsltResposta();
			String arquivoXsl = this.repositorioArquivo.recuperarArquivo(caminhoXls);
			return new TransformadorXsl().transformar(this.dadosBarramento.getDadosLeitura(), arquivoXsl, parametrosNovos.toArray(pArr));
		} 
		catch (Exception e) {
			logger.error(e);
			Mtxtb006Mensagem mensagemErroInterno = this.fornecedorDados.buscarMensagem(MensagemRetorno.ERRO_INTERNO);
			throw new ServicoException(mensagemErroInterno, Constantes.ORIGEM_SIMTX);
		}
	}
	
	
	public void setFornecedorDadosAssinaturaMultipla(FornecedorDadosAssinaturaMultipla fornecedorDadosAssinaturaMultipla) {
		this.fornecedorDadosAssinaturaMultipla = fornecedorDadosAssinaturaMultipla;
	}

	public void setTratadorDeExcecao(TratadorDeExcecao tratadorDeExcecao) {
		this.tratadorDeExcecao = tratadorDeExcecao;
	}

	public void setValidadorRegrasNegocio(ValidadorRegrasNegocio validadorRegrasNegocio) {
		this.validadorRegrasNegocio = validadorRegrasNegocio;
	}

	public void setAtualizadorDadosTransacao(AtualizadorDadosTransacao atualizadorDadosTransacao) {
		this.atualizadorDadosTransacao = atualizadorDadosTransacao;
	}

	public void setRepositorioArquivo(RepositorioArquivo repositorioArquivo) {
		this.repositorioArquivo = repositorioArquivo;
	}

	public void setSimtxConfig(SimtxConfig simtxConfig) {
		this.simtxConfig = simtxConfig;
	}
	
	public void setDadosBarramento(DadosBarramento dadosBarramento) {
		this.dadosBarramento = dadosBarramento;
	}

	public DadosBarramento getDadosBarramento() {
		return dadosBarramento;
	}
	
}
