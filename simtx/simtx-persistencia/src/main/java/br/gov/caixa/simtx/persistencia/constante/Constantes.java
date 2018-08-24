/*******************************************************************************
 * Copyright (C)  2017 - CAIXA Econômica Federal 
 * 
 * Todos os direitos reservados
 ******************************************************************************/
package br.gov.caixa.simtx.persistencia.constante;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;


public final class Constantes {
	
	private Constantes(){}
    
	// ****************** TIPO SERVICO **************** //
	public static final BigDecimal SERVICO_PAGAMENTO = new BigDecimal(BigInteger.ZERO);

	public static final BigDecimal SERVICO_CONFIRMACAO = new BigDecimal(BigInteger.ONE);

	public static final BigDecimal SERVICO_CANCELAMENTO = new BigDecimal(2);

	public static final BigDecimal SERVICO_ESTORNO = new BigDecimal(3);

	public static final BigDecimal SERVICO_ESTORNO_CONFIRMACAO = new BigDecimal(4);

	public static final BigDecimal SERVICO_ESTORNO_CANCELAMENTO = new BigDecimal(5);

	public static final BigDecimal SERVICO_CONSULTA = new BigDecimal(6);

	public static final Long SERVICO_DESFAZIMENTO = 110043L;

	public static final Long SERVICO_RETOMAR_TRANSACAO = 110045L;

	
	
	
	// ****************** CODIGO **************** //
	public static final BigDecimal CODIGO_CANAL_INEXISTENTE = new BigDecimal(2);

	public static final BigDecimal NSU_CANAL_NAO_INFORMADO = new BigDecimal(BigInteger.ONE);

	public static final Long CODIGO_CANAL_SIMTX = 114L;
	
	public static final String SG_CANAL_SIMTX = "SIMTX";

	public static final Long CODIGO_SERVICO_CANCELAMENTO_AGENDAMENTO_SIMTX_WEB = 110042L;

	public static final Long CODIGO_VALIDA_BOLETO_NPC = 110031L;

	public static final Long CODIGO_VALIDA_BOLETO = 110038L;

	public static final Long CODIGO_PARAM_BOLETED = 17L;
    
	
    // ****************** TAG'S XML **************** //
	public static final String TAG_NSU_CANAL = "HEADER/NSUCANAL";

	public static final String TAG_SIBAR_COD_RETORNO = "COD_RETORNO";

	public static final String TAG_SIBAR_ORIGEM_RETORNO = "ORIGEM_RETORNO";

	public static final String TAG_SIBAR_MSG_RETORNO = "MSG_RETORNO";

	public static final String TAG_CONTROLE_COD_RETORNO = "DADOS/CONTROLE_NEGOCIAL/COD_RETORNO";

	public static final String TAG_CONTROLE_ORIGEM_RETORNO = "DADOS/CONTROLE_NEGOCIAL/ORIGEM_RETORNO";

	public static final String TAG_CONTROLE_MSG_RETORNO = "DADOS/CONTROLE_NEGOCIAL/MSG_RETORNO";

	public static final String TAG_CONTROLE_MENSAGENS_RETORNO = "DADOS/CONTROLE_NEGOCIAL/MENSAGENS/RETORNO";

	public static final String TAG_CONTROLE_MENSAGENS_INSTITUCIONAL = "DADOS/CONTROLE_NEGOCIAL/MENSAGENS/INSTITUCIONAL";

	public static final String TAG_CONTROLE_MENSAGENS_INFORMATIVA = "DADOS/CONTROLE_NEGOCIAL/MENSAGENS/INFORMATIVA";

	public static final String TAG_CONTROLE_MENSAGENS_TELA = "DADOS/CONTROLE_NEGOCIAL/MENSAGENS/TELA";

	public static final String TAG_CONTROLE_MENSAGENS_MENSAGEM = "DADOS/CONTROLE_NEGOCIAL/MENSAGENS/MENSAGEM";

	public static final String TAG_ID_PROCESSO = "HEADER/ID_PROCESSO";

	public static final String TAG_TRANSACAO_ORIGEM = "<xsl:variable name=\"transacao_origem\"";
	
	public static final String PATH_CONTA_SIDEC = "/*[1]/CONTA/CONTA_SIDEC";
	
    
	
    
    // ****************** ARQUIVOS PROPERTIES **************** //
	public static final String PROP_ARQUIVOS = "/dados.properties";

	public static final String PROP_CANAIS = "/canal.properties";
	
	
	
	// ****************** IC SITUACAO **************** //
	public static final BigDecimal IC_SITUACAO_ATIVO = new BigDecimal(BigInteger.ONE);

	public static final BigDecimal IC_SITUACAO_INATIVO = new BigDecimal(BigInteger.ZERO);

	public static final BigDecimal IC_CONFIRMACAO_TRANSACAO = new BigDecimal(BigInteger.ONE);

	public static final BigDecimal IC_CONFIRMAR_TRANSACAO = new BigDecimal(BigInteger.ONE);

	public static final BigDecimal IC_CANCELAR_TRANSACAO = new BigDecimal(BigInteger.ONE);

	public static final BigDecimal IC_SOLICITACAO_ESTORNO = new BigDecimal(BigInteger.ONE);

	public static final BigDecimal IC_CONFIRMAR_ESTORNO = new BigDecimal(BigInteger.ONE);

	public static final BigDecimal IC_CANCELAR_ESTORNO = new BigDecimal(BigInteger.ONE);
    
	
    
    // ****************** SITUACAO SERVICO **************** //
	public static final BigDecimal IC_SERVICO_EM_ANDAMENTO = new BigDecimal(BigInteger.ZERO);

	public static final BigDecimal IC_SERVICO_FINALIZADO = new BigDecimal(BigInteger.ONE);
    
    public static final BigDecimal IC_SERVICO_NEGADO = new BigDecimal(2);
    
    public static final BigDecimal IC_SERVICO_PENDENTE = new BigDecimal(3);
    
    public static final BigDecimal IC_SERVICO_CANCELADO = new BigDecimal(4);
    
    public static final BigDecimal IC_SERVICO_ESTORNADO = new BigDecimal(5);
    
    public static final BigDecimal IC_SERVICO_FINALIZADO_PARCIAL = new BigDecimal(9);
    
    public static final BigDecimal IC_SERVICO_FINALIZADO_PENDENTE_ASSINATURA = new BigDecimal(8);
    
    public static final String SITUACAO_FINAL_TRANSACAO_FINALIZADA = "Situacao final da transacao: [1] Finalizada";
    
    public static final String SITUACAO_FINAL_TRANSACAO_NEGADA = "Situacao final da transacao: [2] Negada";
    
    public static final String SITUACAO_FINAL_TRANSACAO_PENDENTE = "Situacao final da transacao: [3] Pendente";
    
    public static final String SITUACAO_FINAL_TRANSACAO_CANCELADA = "Situacao final da transacao: [4] Cancelada";
    
    public static final String SITUACAO_FINAL_TRANSACAO_ESTORNADA = "Situacao final da transacao: [5] Estornada";
    
    public static final String SITUACAO_FINAL_PARCIAL_TRANSACAO = "Situacao final da transacao: [9] Finalizada Parcial";
    
    
    
    // ****************** TAREFA **************** //
	public static final Integer IC_TAREFA_TRANSACIONAL = 1;
	
	public static final Integer IC_TAREFA_NEGOCIAL = 2;
	
	public static final Integer IC_TAREFA_MEIOENTRADA = 3;

	public static final Integer IC_FINANCEIRA = 1;
	
	public static final Long NU_TAREFA_CALCULA_BOLETO = 100066L;

	// ************** VARIAVEIS ****************** //
	public static final Integer CORE_TIMEOUT = 5000;

	public static final String STRING_TIMEOUT = "timeOut";

	public static final String STRING_CONNECTION_FACTORY = "connectionFactory";

	public static final String STRING_CONNECTION_FACTORY_SIBAR = "java:jboss/jms/SimtxQueueConnectionFactorySICCO";

	public static final String STRING_QUEUE_REQ = "queueReq";

	public static final String STRING_QUEUE_RESP = "queueResp";

	public static final String STRING_SIBAR_REQ_CCO = "java:jboss/jms/req_recebe_informacao";

	public static final String STRING_SIBAR_RSP_CCO = "java:jboss/jms/rsp_recebe_informacao";

	public static final String CODIGO_ERRO_DESCONHECIDO = "X5";

	public static final String MENSAGEM_ERRO_DESCONHECIDO = "Erro interno";

	public static final String ORIGEM_SIMTX = "SIMTX";

	public static final String ORIGEM_SIBAR = "SIBAR";

	public static final String TRANSACAO_NEGADA = "erro.transacao.negada";

	public static final String ERRO_BANCO_DADOS = "erro.banco.dados";

	public static final String ERRO_LEIAUTE_INVALIDO = "erro.leiaute.invalido";

	public static final int TIMEOUT_MUX = 510;

	public static final int FLAG_SICCO = 1;

	public static final int FLAG_LIM_PAGAMENTO = 2;

	public static final String ENVIO_UNICA = "UNICA";

	public static final String ENVIO_ALTERACAO = "ALTERACAO";

	/** Envia a transacao de Origem+Corrente(Atual). */
	public static final String ENVIO_ORIGEM_E_ATUAL = "ORIGEM_ATUAL";

	/** Envia a transacao de Origem+Estorno. */
	public static final String ENVIO_ESTORNO = "ESTORNO";

	/** Envia a transacao de Origem+Estorno+Cancelamento. */
	public static final String ENVIO_ESTORNO_CANCELAMENTO = "ESTORNO_CANCELAMENTO";
	
	public static final String MODELO_CALCULO = "03";

    
	//************** MENSAGENS ****************** //
    public static final String MENSAGE_ERRO_TECNICO = "[Técnica] Banco de dados do MTX Temporariamente Indisponivel";
    
    public static final String ERRO_SIMTX_DATA = "Erro SIMTX DATA= [";
    
    public static final String ERRO_RETOMADA_DATA = "Erro RETOMADA DATA= [";

 
	public static final String ENCODE_PADRAO = "UTF-8";
	
	// ************** PARAMETROS ****************** //

	public static final String CAMINHO_HOME_SIMTX = "pasta.simtx.home";

	public static final BigDecimal SIM = new BigDecimal(BigInteger.ONE);
	
	public static final String MENSAGEM_RETORNO_SUCESSO = "00";

	public static final String SISTEMA_SICCO = "SICCO";

	public static final String CODIGO_EXCECAO = "MN001";
	
	public static final long CODIGO_SERVICO_CANCELAMENTO = 110034L;
	
	public static final int VERSAO_SERVICO_CANCELAMENTO = 1;
	
	public static final long CODIGO_TAREFA_DEBITO = 100057L;
	
	public static final Integer AGENDAMENTO_IC_AGENDADO = 1; 
	
	public static final Integer AGENDAMENTO_IC_EFETIVADO = 2;
	
	public static final Integer AGENDAMENTO_IC_CANCELADO = 3;
	
	public static final String AGENDAMENTO_CODIGOMSGIMPEDITIVO = "Codigo de retorno impeditivo para Agendamento";
	
	public static final String MSG_AGENDAMENTO_NAO_IMPEDITIVO = "Codigo de retorno impeditivo no Sistema, porem, nao impeditivo para Agendamento";



	public static List<Long> getServicosAssinaturaMultipla() {
		List<Long> servicosAssinaturaMultipla = new ArrayList<>();
		servicosAssinaturaMultipla.add(110024L);// Detalhe
		servicosAssinaturaMultipla.add(110025L);// Cancela
		servicosAssinaturaMultipla.add(110023L);// Lista
		servicosAssinaturaMultipla.add(110022L);// Valida
		return servicosAssinaturaMultipla;
	}

	public static List<Long> getServicosCore() {
		List<Long> servicos = new ArrayList<>();
		servicos.add(110021L);
		servicos.add(110028L);
		servicos.add(110029L);
		servicos.add(110030L);
		servicos.add(110031L);
		servicos.add(110032L);
		servicos.add(110038L);
		servicos.add(110039L);
		servicos.add(110044L);
		return servicos;
	}
	public static List<Long> getServicosComprovantes() {
		List<Long> servicos = new ArrayList<>();
		servicos.add(110026L);
		servicos.add(110027L);
		return servicos;
	}
	
	public static List<Long> getServicosAgendamentos() {
		List<Long> servicos = new ArrayList<>();
		servicos.add(110033L);
		return servicos;
	}
	
	public static List<Long> getServicosAgendamentosPagamentos() {
		List<Long> servicos = new ArrayList<>();
		servicos.add(110040L);
		return servicos;
	}
	
	public static List<Long> getServicosAgendamentosCancelamentoCanal() {
		List<Long> servicos = new ArrayList<>();
		servicos.add(110037L);		
		return servicos;
	}
	
	public static List<Long> getServicosAgendamentosListaDetalhe() {
		List<Long> servicos = new ArrayList<>();
		servicos.add(110036L);
		servicos.add(110041L);
		return servicos;
	}
	
	public static List<Long> getServicosCancelamento() {
		List<Long> servicos = new ArrayList<>();
		servicos.add(110034L);
		return servicos;
	}
	
	public static List<Long> getServicosCancelamentoWeb() {
		List<Long> servicos = new ArrayList<>();
		servicos.add(CODIGO_SERVICO_CANCELAMENTO_AGENDAMENTO_SIMTX_WEB);
		return servicos;
	}
	
}