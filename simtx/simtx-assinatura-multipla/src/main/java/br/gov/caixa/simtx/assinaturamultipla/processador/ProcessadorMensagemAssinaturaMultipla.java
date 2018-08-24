/*******************************************************************************
 * Copyright (C)  2017 - CAIXA Econômica Federal 
 * 
 * Todos os direitos reservados
 ******************************************************************************/
package br.gov.caixa.simtx.assinaturamultipla.processador;

import java.io.Serializable;
import java.util.Properties;

import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb001Servico;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb004Canal;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb011VersaoServico;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb014Transacao;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb016IteracaoCanal;
import br.gov.caixa.simtx.util.SimtxConfig;
import br.gov.caixa.simtx.util.xml.TransformaMensagemCanal;

public interface ProcessadorMensagemAssinaturaMultipla extends Serializable {

	/**
	 * Metodo responsavel por processar mensagens de assinatura multipla dos
	 * canais via Fila MQ.
	 * 
	 * @param mensagem
	 */
	public void processarMensagemMQ();

	public String getMensagemCanal();

	public void setMensagemCanal(String mensagemCanal);

	public Mtxtb011VersaoServico getVersaoServicoChamado();

	public void setVersaoServicoChamado(Mtxtb011VersaoServico versaoServicoChamado);

	public TransformaMensagemCanal getTransformaMensagemCanal();

	public void setTransformaMensagemCanal(TransformaMensagemCanal transformaMensagemCanal);

	public Mtxtb014Transacao getTransacao();

	public void setTransacao(Mtxtb014Transacao transacao);

	public Mtxtb001Servico getServicoChamado();

	public void setServicoChamado(Mtxtb001Servico servicoChamado);

	public SimtxConfig getSimtxConfig();

	public void setSimtxConfig(SimtxConfig simtxConfig);

	public Properties getProperties();

	public void setProperties(Properties properties);

	public Mtxtb004Canal getCanal();

	public void setCanal(Mtxtb004Canal canal);
	
	public String getIdMensagem();

	public void setIdMensagem(String idMensagem);

	public void setIteracaoCanal(Mtxtb016IteracaoCanal iteracaoCanal);

}
