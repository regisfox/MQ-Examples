/*******************************************************************************
 * Copyright (C)  2018 - CAIXA Econômica Federal 
 * 
 * Todos os direitos reservados
 ******************************************************************************/
package br.gov.caixa.simtx.agendamento.processador;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.apache.log4j.Logger;
import org.w3c.dom.Node;

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
import br.gov.caixa.simtx.persistencia.vo.TipoContingenciaEnum;
import br.gov.caixa.simtx.util.ConstantesAgendamento;
import br.gov.caixa.simtx.util.ParametrosAdicionais;
import br.gov.caixa.simtx.util.SimtxConfig;
import br.gov.caixa.simtx.util.exception.ServicoException;
import br.gov.caixa.simtx.util.exception.SimtxException;
import br.gov.caixa.simtx.util.gerenciador.GerenciadorTransacao;
import br.gov.caixa.simtx.util.gerenciador.TratadorDeExcecao;
import br.gov.caixa.simtx.util.gerenciador.ValidadorRegrasNegocio;
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
 * Classe responsavel por tratar e executar todos os servicos passiveis de
 * Agendamento. Ex: Pagamento Boleto.
 * 
 * @author rctoscano
 *
 */
@Stateless
public class ProcessadorAgendamento extends GerenciadorTransacao implements ProcessadorServicos, Serializable {
    
	private static final long serialVersionUID = 1L;
	
	private static final Logger logger = Logger.getLogger(ProcessadorAgendamento.class);

	
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
		logger.info(" ==== Processo Agendamento Iniciado ==== ");
		
		Mtxtb014Transacao transacao = null;
		Mtxtb016IteracaoCanal iteracaoCanal = null;
		Mtxtb011VersaoServico servicoOrigem = null;
		Mtxtb004Canal canal = null;
		try {
			this.dadosBarramento = new DadosBarramento();
			this.dadosBarramento.escrever(parametrosAdicionais.getXmlMensagem());

			BuscadorTextoXml buscador = new BuscadorTextoXml(parametrosAdicionais.getXmlMensagem());
			Long codigoServico = Long.parseLong(buscador.xpathTexto("/*[1]/HEADER/SERVICO/CODIGO"));
			Integer versaoServico = Integer.parseInt(buscador.xpathTexto("/*[1]/HEADER/SERVICO/VERSAO"));
			servicoOrigem = buscarServico(codigoServico, versaoServico);
			canal = buscarCanal(buscador);
			
			Mtxtb011VersaoServico servicoAgendamento = buscarServico(ConstantesAgendamento.CODIGO_SERVICO_AGENDAMENTO, versaoServico);
			
			transacao = salvarTransacao(buscador, canal);
			iteracaoCanal = salvarIteracaoCanal(buscador, parametrosAdicionais.getXmlMensagem(), transacao);
			salvarTransacaoServico(transacao, servicoAgendamento);
			
			int nuMeioEntrada = Integer.parseInt(buscador.xpathTexto("/*[1]/HEADER/MEIOENTRADA"));
			Mtxtb008MeioEntrada meioEntrada = new Mtxtb008MeioEntrada();
			meioEntrada.setNuMeioEntrada(nuMeioEntrada);
			
			this.validadorRegrasNegocio.validarRegrasMigradoSemParametros(parametrosAdicionais.getXmlMensagem(), canal, meioEntrada, servicoOrigem);
			this.validadorRegrasNegocio.validarParametrosDoCanal(servicoAgendamento, canal, parametrosAdicionais.getXmlMensagem());
			
			validarRegrasAgendamento(transacao);
			
			meioEntrada = this.fornecedorDados.buscarMeioEntrada(nuMeioEntrada);

			if (buscador.xpathTexto("/*[1]/ASSINATURA_MULTIPLA") != null
					&& !buscador.xpathTexto("/*[1]/ASSINATURA_MULTIPLA").isEmpty()) {
				tratarAssinaturaMultipla(transacao, iteracaoCanal, canal, servicoOrigem, servicoAgendamento);
			}
			else {
				processarEntradaSaida(transacao, iteracaoCanal, canal, servicoOrigem, servicoAgendamento, meioEntrada, parametrosAdicionais);
			}
		} 
		catch (ServicoException se) {
			logger.error(se.getMensagem());
			DadosTransacaoComuns dadosTransacaoComuns = new DadosTransacaoComuns(servicoOrigem, transacao, iteracaoCanal);
			parametrosAdicionais.setDadosBarramento(dadosBarramento);
			this.tratadorDeExcecao.tratarExcecaoRetomada(dadosTransacaoComuns, se, parametrosAdicionais, true);
		}
		catch (Exception e) {
			logger.error(e.getMessage(), e);
			Mtxtb006Mensagem mensagemErroInterno = this.fornecedorDados.buscarMensagem(MensagemRetorno.ERRO_INTERNO);
			ServicoException se = new ServicoException(mensagemErroInterno,Constantes.ORIGEM_SIMTX);
			DadosTransacaoComuns dadosTransacaoComuns = new DadosTransacaoComuns(servicoOrigem, transacao, iteracaoCanal);
			parametrosAdicionais.setDadosBarramento(dadosBarramento);
			this.tratadorDeExcecao.tratarExcecaoRetomada(dadosTransacaoComuns, se, parametrosAdicionais, true);
		} 
		finally {
			logger.info(" ==== Processo Agendamento Finalizado ==== ");
		}
	}
	
	/**
	 * Trata o Agendamento com Assinatura Simples.
	 * 
	 * @param transacao
	 * @param iteracaoCanal
	 * @param canal
	 * @param servicoOrigem
	 * @param servicoAgendamento
	 * @param dadosBarramento
	 */
	private void processarEntradaSaida(Mtxtb014Transacao transacao, Mtxtb016IteracaoCanal iteracaoCanal, Mtxtb004Canal canal,
			Mtxtb011VersaoServico servicoOrigem, Mtxtb011VersaoServico servicoAgendamento,
			Mtxtb008MeioEntrada meioEntrada, ParametrosAdicionais parametrosAdicionais) {
		
		TarefasServicoResposta tarefasServicoResposta = new TarefasServicoResposta();
		String xmlSaidaSibar = null;
		List<Mtxtb003ServicoTarefa> listaTarefas = null;
		try {
			Mtxtb004Canal canalMtx = buscarCanalSIMTX();
			listaTarefas = this.gerenciadorTarefas.carregarTarefasServico(servicoAgendamento, canalMtx, meioEntrada);
			
			logger.info("Preparando xml de requisicao para enviar ao Sibar");
			String xmlEntradaSibar = transformarXml(servicoAgendamento.getDeXsltRequisicao(), transacao, canal, null);
			xmlEntradaSibar = this.geradorPassosMigrado.gerarPassos(xmlEntradaSibar, servicoAgendamento, listaTarefas);
			this.dadosBarramento.escrever(xmlEntradaSibar);
			
			xmlSaidaSibar = this.execucaoMq.executar(xmlEntradaSibar, servicoAgendamento.getMtxtb001Servico());
			this.dadosBarramento.escrever(xmlSaidaSibar);
			
			tarefasServicoResposta = this.gerenciadorTarefas.montarTarefasResposta(listaTarefas, servicoAgendamento,
					transacao, this.dadosBarramento, xmlSaidaSibar);
			
			logger.info("Preparando xml de resposta para enviar ao Canal");
			String xmlSaidaCanal = transformarXml(servicoOrigem.getDeXsltResposta(), transacao, canal, tarefasServicoResposta.getResposta());
			logger.info("Enviando resposta para Canal");
			enviarRespostaCanal(xmlSaidaCanal, parametrosAdicionais);
			
			logger.info("Atualizando informacoes das tarefas e transacao");
			BigDecimal situacaoTransacao = this.validadorRegrasNegocio.situacaoFinalTransacao(servicoAgendamento);
			transacao = atualizaStatusTransacao(transacao, situacaoTransacao);
			Mtxtb016IteracaoCanal mtxtb016IteracaoCanal = montaMtxtb016IteracaoCanalTansacao(iteracaoCanal, transacao, xmlSaidaCanal);
			Mtxtb034TransacaoAgendamento mtxtb034TransacaoAgendamento = montarTransacaoAgendamento(transacao, servicoOrigem, canal, iteracaoCanal);
			
			boolean statusAtualizacaoTransacao = this.atualizadorDadosTransacao.atualizarDadosTransacaoAgendamento(transacao, tarefasServicoResposta.getListaTransacaoTarefas(), mtxtb016IteracaoCanal, mtxtb034TransacaoAgendamento);
			
			direcionaProcessadorMensagemTransacao(false, statusAtualizacaoTransacao, transacao, servicoAgendamento, dadosBarramento, xmlSaidaSibar);
			
		} catch (ServicoException se) {
			logger.error(se.getMensagem());
			DadosTransacaoComuns dadosTransacaoComuns = new DadosTransacaoComuns(listaTarefas, servicoAgendamento, transacao, tarefasServicoResposta.getListaTransacaoTarefas(), iteracaoCanal);
			parametrosAdicionais.setDadosBarramento(dadosBarramento);
			parametrosAdicionais.setXmlMensagem(xmlSaidaSibar);
			this.tratadorDeExcecao.tratarExcecaoRetomada(dadosTransacaoComuns, se, parametrosAdicionais, true);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			Mtxtb006Mensagem mensagemErroInterno = this.fornecedorDados.buscarMensagem(MensagemRetorno.ERRO_INTERNO);
			ServicoException se = new ServicoException(mensagemErroInterno, Constantes.ORIGEM_SIMTX);
			parametrosAdicionais.setDadosBarramento(dadosBarramento);
			parametrosAdicionais.setXmlMensagem(xmlSaidaSibar);
			DadosTransacaoComuns dadosTransacaoComuns = new DadosTransacaoComuns(listaTarefas, servicoAgendamento, transacao, tarefasServicoResposta.getListaTransacaoTarefas(), iteracaoCanal);
			this.tratadorDeExcecao.tratarExcecaoRetomada(dadosTransacaoComuns, se, parametrosAdicionais, true);
		}
	}
		
	/**
	 * Trata o Agendamento com Assinatura Multipla. Deve gravar no banco como
	 * Pendente de Assinatura e mandar para o modulo de Assinatura Multipla
	 * tratar.
	 * 
	 * @param transacao
	 * @param iteracaoCanal
	 * @param canal
	 * @param servicoOrigem
	 * @param servicoAgendamento
	 * @param dadosBarramento
	 */
	public void tratarAssinaturaMultipla(Mtxtb014Transacao transacao, Mtxtb016IteracaoCanal iteracaoCanal,
			Mtxtb004Canal canal, Mtxtb011VersaoServico servicoOrigem, Mtxtb011VersaoServico servicoAgendamento) {
		/**
		 * 
		 * 
		 */
	}
	
	/**
	 * Salva a entidade MtxtbxxxTransacaoAgendamento.
	 * 
	 * @param transacao
	 * @param servicoAgendamento
	 * @param canal
	 * @param iteracaoCanal
	 * @param dadosBarramento
	 * @throws ServicoException
	 */
	@Override
	public Mtxtb034TransacaoAgendamento montarTransacaoAgendamento(Mtxtb014Transacao transacao, Mtxtb011VersaoServico servicoOrigem,
			Mtxtb004Canal canal, Mtxtb016IteracaoCanal iteracaoCanal)
			throws ServicoException {
		try {
			logger.info("Salvando transacao de agendamento");
			SimpleDateFormat formataData = new SimpleDateFormat("yyyy-MM-dd");
			
			Node tag = this.dadosBarramento.xpath("/BUSDATA/*[2]/CONTA/CONTA_SIDEC");
			int tipoConta = 1;
			String tipoContaTexto = "/BUSDATA/*[2]/CONTA/CONTA_SIDEC";
			String operacao = "/OPERACAO";
			if (tag == null) {
				tipoConta = 2;
				tipoContaTexto = "/BUSDATA/*[2]/CONTA/CONTA_NSGD";
				operacao = "/PRODUTO";
			}
			int nuUnidade = Integer.parseInt(this.dadosBarramento.xpathTexto(tipoContaTexto+"/UNIDADE"));
			int nuProduto = Integer.parseInt(this.dadosBarramento.xpathTexto(tipoContaTexto+operacao));
			long nuConta = Long.parseLong(this.dadosBarramento.xpathTexto(tipoContaTexto+"/CONTA"));
			short dvConta = Short.parseShort(this.dadosBarramento.xpathTexto(tipoContaTexto+"/DV"));
			
		
			String dtEfetivacao = this.dadosBarramento.xpathTexto("/BUSDATA/*[2]/PAGAMENTO_BOLETO/INFORMACOES_BOLETO/DATA_EFETIVACAO");
			String identfTransacao = this.dadosBarramento.xpathTexto("/BUSDATA/*[2]/PAGAMENTO_BOLETO/INFORMACOES_BOLETO/IDENTIFICACAO");
			String valorPagamento = this.dadosBarramento.xpathTexto("/BUSDATA/*[2]/PAGAMENTO_BOLETO/INFORMACOES_BOLETO/VALOR_PAGO");
			if (null == dtEfetivacao || dtEfetivacao.isEmpty()) {
				throw new SimtxException("DATA_EFETIVACAO Campo Requerido");
			}
			if (null == valorPagamento || dtEfetivacao.isEmpty()) {
				throw new SimtxException("VALOR_PAGO Campo Requerido");
			}
			
			BigDecimal	valorTransacao = new BigDecimal(valorPagamento);
			
			Mtxtb034TransacaoAgendamento transacaoAgendamento = new Mtxtb034TransacaoAgendamento();
			transacaoAgendamento.setNuNsuTransacaoAgendamento(transacao.getNuNsuTransacao());
			transacaoAgendamento.setDtEfetivacao(formataData.parse(dtEfetivacao));
			transacaoAgendamento.setDtReferencia(transacao.getDtReferencia());
			transacaoAgendamento.setDeXmlAgendamento(iteracaoCanal.getDeRecebimento());
			transacaoAgendamento.setDeIdentificacaoTransacao(identfTransacao);
			transacaoAgendamento.setNuCanal(canal.getNuCanal());
			transacaoAgendamento.setNuServico(servicoOrigem.getId().getNuServico001());
			transacaoAgendamento.setNuVersaoServico(servicoOrigem.getId().getNuVersaoServico());
			transacaoAgendamento.setIcSituacao(ConstantesAgendamento.SITUACAO_AGENDADO);
			transacaoAgendamento.setNuUnidade(nuUnidade);
			transacaoAgendamento.setNuProduto(nuProduto);
			transacaoAgendamento.setNuConta(nuConta);
			transacaoAgendamento.setDvConta(dvConta);
			transacaoAgendamento.setIcTipoConta(tipoConta);
			transacaoAgendamento.setValorTransacao(valorTransacao);
			return transacaoAgendamento;
		} 
		catch (Exception e) {
			logger.error(e);
			Mtxtb006Mensagem mensagemErroInterno = this.fornecedorDados.buscarMensagem(MensagemRetorno.ERRO_GRAVACAO_TRANSACAO);
			throw new ServicoException(mensagemErroInterno,Constantes.ORIGEM_SIMTX);
		}
	}
	
	/**
	 * Valida as regras para verificar se pode agendar.
	 * 
	 * @param transacao
	 * @throws ServicoException
	 */
	public void validarRegrasAgendamento(Mtxtb014Transacao transacao) throws ServicoException {
		logger.info("Validando regras para agendamento");
		try {
			Mtxtb014Transacao transacaoOrigem = this.fornecedorDados
					.buscarTransacaoOrigem(transacao.getNuNsuTransacaoPai());
			
			if(transacaoOrigem == null) {
				String erro = "NSU Origem não encontrado: " + transacao.getNuNsuTransacaoPai();
				logger.error(erro);
				throw new IllegalArgumentException(erro);
			}
			
			BuscadorTextoXml buscador = new BuscadorTextoXml(
					transacaoOrigem.getMtxtb016IteracaoCanals().get(0).getDeRetorno());

			String contingencia = buscador.xpathTexto("/*[1]/SITUACAO_CONTINGENCIA");
			String modeloCalculo = buscador.xpathTexto("/*[1]/CONSULTA_BOLETO/MODELO_CALCULO");
			
			if(!contingencia.isEmpty() && TipoContingenciaEnum.CIP.getDescricao().equals(contingencia)) {
				logger.warn("Nao permitido agendamento em situacao de contingencia: "
						+ TipoContingenciaEnum.CIP.getDescricao());
				Mtxtb006Mensagem mensagem = this.fornecedorDados
						.buscarMensagem(MensagemRetorno.NAO_PERMITE_AGENDAMENTO_CONTINGENCIA_CIP);
				throw new ServicoException(mensagem, Constantes.ORIGEM_SIMTX);
			}
			else if(!modeloCalculo.isEmpty() && modeloCalculo.equals(ConstantesAgendamento.MODELO_CALCULO)) {
				logger.warn("Nao permitido agendamento para modelo de calculo: "+ConstantesAgendamento.MODELO_CALCULO);
				Mtxtb006Mensagem mensagem = this.fornecedorDados
						.buscarMensagem(MensagemRetorno.NAO_PERMITE_AGENDAMENTO_MODELO_CALCULO_3);
				throw new ServicoException(mensagem, Constantes.ORIGEM_SIMTX);
			}
		} 
		catch (ServicoException e) {
			throw e;
		}
		catch (Exception e) {
			logger.error("Erro ao validar para agendamento", e);
			Mtxtb006Mensagem mensagem = this.fornecedorDados.buscarMensagem(MensagemRetorno.ERRO_INTERNO);
			throw new ServicoException(mensagem, Constantes.ORIGEM_SIMTX);
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
	public String transformarXml(String xslt, Mtxtb014Transacao transacao, Mtxtb004Canal canal, Resposta mensagem)
			throws ServicoException {
		try {
			List<ParametroXsl> parametrosNovos = new ArrayList<>();
			parametrosNovos.add(new ParametroXsl(ParametroXsl.XSL_PARAM_NSU_SIMTX, String.valueOf(transacao.getNuNsuTransacao())));
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
			return new TransformadorXsl().transformar(this.dadosBarramento.getDadosLeitura(), arquivoXsl, parametrosNovos.toArray(pArr));
		} 
		catch (Exception e) {
			logger.error(e);
			Mtxtb006Mensagem mensagemErroInterno = this.fornecedorDados.buscarMensagem(MensagemRetorno.ERRO_INTERNO);
			throw new ServicoException(mensagemErroInterno,Constantes.ORIGEM_SIMTX);
		}
	}

	

	public void setSimtxConfig(SimtxConfig simtxConfig) {
		this.simtxConfig = simtxConfig;
	}

	public void setFornecedorDadosAgendamento(FornecedorDadosAgendamento fornecedorDadosAgendamento) {
		this.fornecedorDadosAgendamento = fornecedorDadosAgendamento;
	}

	public void setValidadorRegrasNegocio(ValidadorRegrasNegocio validadorRegrasNegocio) {
		this.validadorRegrasNegocio = validadorRegrasNegocio;
	}
	
	public void setTratadorDeExcecao(TratadorDeExcecao tratadorDeExcecao) {
		this.tratadorDeExcecao = tratadorDeExcecao;
	}
	
	public void setDadosBarramento(DadosBarramento dadosBarramento) {
		this.dadosBarramento = dadosBarramento;
	}
	
	public void setRepositorioArquivo(RepositorioArquivo repositorioArquivo) {
		this.repositorioArquivo = repositorioArquivo;
	}

}
