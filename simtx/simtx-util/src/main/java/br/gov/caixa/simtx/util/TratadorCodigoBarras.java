package br.gov.caixa.simtx.util;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.apache.log4j.Logger;

import br.gov.caixa.simtx.persistencia.cache.core.FornecedorDados;
import br.gov.caixa.simtx.persistencia.constante.Constantes;
import br.gov.caixa.simtx.persistencia.constante.MensagemRetorno;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb006Mensagem;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb023Parametro;
import br.gov.caixa.simtx.persistencia.vo.ParametrosFatorVencimento;
import br.gov.caixa.simtx.util.data.DataUtil;
import br.gov.caixa.simtx.util.exception.ServicoException;
import br.gov.caixa.simtx.util.vo.CodigoBarras;

@Stateless
public class TratadorCodigoBarras implements Serializable {

	private static final long serialVersionUID = 3164425244257654846L;
	
	private static final Logger logger = Logger.getLogger(TratadorCodigoBarras.class);
	
	@Inject
	private FornecedorDados fornecedorDados;
	
	
	
	public CodigoBarras retornarValoresCodigoBarras(String codBarras) throws ServicoException {
		CodigoBarras codigoBarras = quebrarCodigoBarras(codBarras);
		
		ParametrosFatorVencimento parametros = recuperarParametrosFatorVencimento();
		Date dataBase = calcularDataBase(parametros, codigoBarras.getFatorVencimento());
		
		Calendar data = Calendar.getInstance();
		data.setTime(dataBase);
		Date dataVencimento = DataUtil.obterDataFutura(data, codigoBarras.getFatorVencimento());
		logger.info("Data de Vencimento do Boleto: "+dataVencimento);
		codigoBarras.setDataVencimento(dataVencimento);
		
		return codigoBarras;
	}
	
	public CodigoBarras quebrarCodigoBarras(String codBarras) throws ServicoException {
		try {
			logger.info("Quebrando as posicoes do codigo de barras ["+codBarras+"]");
			if(codBarras.length() == 44) {
				CodigoBarras codigoBarras = new CodigoBarras();
				
				String segmento1 = codBarras.substring(CodigoBarras.POSICAO_SEGMENTO1_INICIO, CodigoBarras.POSICAO_SEGMENTO1_FIM);
				String segmento2 = codBarras.substring(CodigoBarras.POSICAO_SEGMENTO2_INICIO, CodigoBarras.POSICAO_SEGMENTO2_FIM);
				String segmento3 = codBarras.substring(CodigoBarras.POSICAO_SEGMENTO3_INICIO, CodigoBarras.POSICAO_SEGMENTO3_FIM);
				String segmento4 = codBarras.substring(CodigoBarras.POSICAO_SEGMENTO4_INICIO, CodigoBarras.POSICAO_SEGMENTO4_FIM);
				String segmento5 = codBarras.substring(CodigoBarras.POSICAO_SEGMENTO5_INICIO, codBarras.length());
				
				String fatorVencimento = codBarras.substring(CodigoBarras.POSICAO_FATOR_VENCIMENTO_INICIO, CodigoBarras.POSICAO_FATOR_VENCIMENTO_FIM);
				String valor = codBarras.substring(CodigoBarras.POSICAO_VALOR_TITULO_INICIO, CodigoBarras.POSICAO_VALOR_TITULO_FIM);
				valor = valor.substring(0, valor.length()-2)+"."+valor.substring(valor.length()-2, valor.length());
				
				BigDecimal valorTitulo = new BigDecimal(valor);
				
				int fator = validarFatorVencimento(fatorVencimento);
				
				codigoBarras.setSegmento1(segmento1);
				codigoBarras.setSegmento2(segmento2);
				codigoBarras.setSegmento3(segmento3);
				codigoBarras.setSegmento4(segmento4);
				codigoBarras.setSegmento5(segmento5);
				codigoBarras.setFatorVencimento(fator);
				codigoBarras.setValorTitulo(valorTitulo);
				return codigoBarras;
			}
			else {
				logger.error("Codigo de barras invalido");
				Mtxtb006Mensagem mensagem = this.fornecedorDados.buscarMensagem(MensagemRetorno.CODIGO_BARRAS_INVALIDO);
				throw new ServicoException(mensagem, Constantes.ORIGEM_SIMTX);
			}
		} 
		catch (ServicoException e) {
			throw e;
		}
		catch (Exception e) {
			logger.error(e);
			Mtxtb006Mensagem mensagem = this.fornecedorDados.buscarMensagem(MensagemRetorno.ERRO_QUEBRAR_CODIGO_BARRAS);
			throw new ServicoException(mensagem, Constantes.ORIGEM_SIMTX);
		}
	}
	
	public int validarFatorVencimento(String fatorVencimento) throws ServicoException {
		try {
			int fator = Integer.parseInt(fatorVencimento);
			if(fator < 1000 || fator > 9999) {
				Mtxtb006Mensagem mensagem = this.fornecedorDados.buscarMensagem(MensagemRetorno.FATOR_VENCIMENTO_INVALIDO);
				throw new ServicoException(mensagem, Constantes.ORIGEM_SIMTX);
			}
			return fator;
		}
		catch (ServicoException e) {
			throw e;
		}
		catch (Exception e) {
			logger.error(e);
			Mtxtb006Mensagem mensagem = this.fornecedorDados.buscarMensagem(MensagemRetorno.FATOR_VENCIMENTO_INVALIDO);
			throw new ServicoException(mensagem, Constantes.ORIGEM_SIMTX);
		}
	}
	
	public ParametrosFatorVencimento recuperarParametrosFatorVencimento() throws ServicoException {
		try {
			logger.info("Recuperando parametros do fator de vencimento");
			Mtxtb023Parametro pmt = this.fornecedorDados.buscarParametroPorPK(ParametrosFatorVencimento.PMT_DATA_BASE_NOVA);
			Date dataBaseNova = new SimpleDateFormat(DataUtil.FORMATO_DATA_PADRAO_BR).parse(pmt.getDeConteudoParam());
			
			Date dataAtual = new Date();
			if(dataAtual.after(dataBaseNova)) {
				
				Calendar novaData = Calendar.getInstance();
				novaData.setTime(dataBaseNova);
				novaData.add(Calendar.DAY_OF_MONTH, 9999);
				
				atualizarDataBaseAtual(dataBaseNova);

				atualizarDataBaseNova(novaData.getTime());
				
				return this.fornecedorDados.buscarParametrosFatorVencimento();
			}
			else {
				return this.fornecedorDados.buscarParametrosFatorVencimento();
			}
		} 
		catch (Exception e) {
			logger.error(e);
			Mtxtb006Mensagem mensagem = this.fornecedorDados.buscarMensagem(MensagemRetorno.ERRO_VERIFICAR_DATA_BASE_NOVA);
			throw new ServicoException(mensagem, Constantes.ORIGEM_SIMTX);
		}
	}
	
	public void atualizarDataBaseAtual(Date dataBaseAtual) throws ServicoException {
		try {
			logger.info("Atualizando data base atual");
			Mtxtb023Parametro parametro = this.fornecedorDados.buscarParametroPorPK(ParametrosFatorVencimento.PMT_DATA_BASE_ATUAL);
			parametro.setDhAtualizacaoParam(new Date());
			parametro.setDeConteudoParam(new SimpleDateFormat(DataUtil.FORMATO_DATA_PADRAO_BR).format(dataBaseAtual));
			this.fornecedorDados.alterarParametro(parametro);
		} 
		catch (Exception e) {
			logger.error(e);
			Mtxtb006Mensagem mensagem = this.fornecedorDados.buscarMensagem(MensagemRetorno.ERRO_ATUALIZA_DATA_BASE_ATUAL);
			throw new ServicoException(mensagem, Constantes.ORIGEM_SIMTX);
		}
	}
	
	public void atualizarDataBaseNova(Date dataBaseNova) throws ServicoException {
		try {
			logger.info("Atualizando data base nova");
			Mtxtb023Parametro parametro = this.fornecedorDados.buscarParametroPorPK(ParametrosFatorVencimento.PMT_DATA_BASE_NOVA);
			parametro.setDhAtualizacaoParam(new Date());
			parametro.setDeConteudoParam(new SimpleDateFormat(DataUtil.FORMATO_DATA_PADRAO_BR).format(dataBaseNova));
			this.fornecedorDados.alterarParametro(parametro);
		} 
		catch (Exception e) {
			logger.error(e);
			Mtxtb006Mensagem mensagem = this.fornecedorDados.buscarMensagem(MensagemRetorno.ERRO_ATUALIZA_DATA_BASE_NOVA);
			throw new ServicoException(mensagem, Constantes.ORIGEM_SIMTX);
		}
	}
	
	public Date calcularDataBase(ParametrosFatorVencimento parametros, int fatorVencimento) throws ServicoException {
		try {
			logger.info("Calculando data base vencimento");
			Date dataAtual = new Date();
			
			long fatorDataAtual = TimeUnit.DAYS.convert(dataAtual.getTime() - parametros.getDataBase().getTime(), TimeUnit.MILLISECONDS);
			long fatorDataImplementacao = TimeUnit.DAYS.convert(parametros.getDataImplantacao().getTime() - parametros.getDataBase().getTime(), TimeUnit.MILLISECONDS);
			long fatorN = fatorDataAtual - fatorDataImplementacao;
	
			if (fatorVencimento >= (fatorN + parametros.getRangeVencido())) {
	
				logger.info("Data Base Vencimento: "+parametros.getDataBaseAtual());
				return parametros.getDataBaseAtual();
			} 
			else if (fatorVencimento <= (parametros.getRangeAVencer() - parametros.getRangeVencido() + fatorN)) {
	
				logger.info("Data Base Vencimento: "+parametros.getDataBaseNova());
				return parametros.getDataBaseNova();
			} 
			else {
				logger.error("Fator de Vencimento dentro do Range de Seguranca.");
				Mtxtb006Mensagem mensagem = this.fornecedorDados.buscarMensagem(MensagemRetorno.ERRO_VERIFICAR_DATA_BASE_NOVA);
				throw new ServicoException(mensagem, Constantes.ORIGEM_SIMTX);
			}
		} 
		catch (ServicoException e) {
			throw e;
		}
		catch (Exception e) {
			logger.error(e);
			Mtxtb006Mensagem mensagem = this.fornecedorDados.buscarMensagem(MensagemRetorno.ERRO_VERIFICAR_DATA_BASE_NOVA);
			throw new ServicoException(mensagem, Constantes.ORIGEM_SIMTX);
		}
	}


	public void setFornecedorDados(FornecedorDados fornecedorDados) {
		this.fornecedorDados = fornecedorDados;
	}

}
