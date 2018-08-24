/*******************************************************************************
 * Copyright (C)  2017 - CAIXA Econômica Federal 
 * 
 * Todos os direitos reservados
 ******************************************************************************/
package br.gov.caixa.simtx.util.exception;

import java.util.Properties;

import org.xml.sax.SAXParseException;

import br.gov.caixa.simtx.persistencia.constante.Constantes;
import br.gov.caixa.simtx.util.data.DataUtil;


/**
 * The Class SimtxException.
 */
public class SimtxException extends RuntimeException {
    
    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;
    
    /** The mensagens. */
    protected static Properties mensagens;
    
    /** The error code. */
    protected String errorCode;
    
    /** The message. */
    protected String message;
    
    /** The mensagem xslt. */
    protected String mensagemXslt;
    
    /** The s header. */
    protected String sHeader;
    
    /** The nsu requisicao. */
    protected String nsuRequisicao;
    
    /** The cause. */
    protected Throwable cause;
    

    
    /**
     * Instantiates a new simtx exception.
     *
     * @param errorCode the error code
     */
    public SimtxException(String errorCode) {
    	this.errorCode = errorCode;
    	if (errorCode == null) {
        	this.errorCode = "X5";
            this.message = "Erro SIMTX - Codigo nao especificado";
        }
        this.message = mensagens.getProperty(errorCode);
        if (this.message == null) {
            this.message = Constantes.ERRO_SIMTX_DATA + DataUtil.getDataAtual() + "] " + errorCode + "]";
        }
    }
    
    /**
     * Instantiates a new simtx exception.
     *
     * @param errorCode the error code
     * @param message the message
     */
    public SimtxException(String errorCode, String message) {
        this.mensagemXslt = message;
    }

    /**
     * Instantiates a new simtx exception.
     *
     * @param e the e
     */
    public SimtxException(Exception e) {
        this.errorCode = "X5";
        this.message = Constantes.ERRO_SIMTX_DATA + DataUtil.getDataAtual() + "] " + e.toString();
    }

    /**
     * Instantiates a new simtx exception.
     *
     * @param e the e
     */
    public SimtxException(SAXParseException e) {
        this.errorCode = "X5";
        this.message = Constantes.ERRO_SIMTX_DATA + DataUtil.getDataAtual() + "] "  + e.toString();
    }

    /**
     * Instantiates a new simtx exception.
     */
    public SimtxException() {
        this.errorCode = "X5";
        this.message = "Erro SIMTX - Codigo nao Definido";
    }

    static {

        mensagens = new Properties();
        mensagens.put("MN001", "Leiaute/Formato/Conteúdo de identificação da mensagem inválido.");
        mensagens.put("MN002", "Leiaute/Formato/Conteúdo negocial da mensagem inválido.");
        mensagens.put("MN003", "Canal de Atendimento inexistente.");
        mensagens.put("MN004", "Serviço inexistente.");
        mensagens.put("MN005", "Versão do serviço inexistente.");
        mensagens.put("MN006", "Não foi possível gerar NSU para controle da transação.");
        mensagens.put("MN007", "Serviço invativo.");
        mensagens.put("MN008", "Versão do serviço inativa.");
        mensagens.put("MN009", "Meio de entrada inexistente.");
        mensagens.put("MN010", "Meio de entrada inativo.");
        mensagens.put("MN011", "Canal de Atendimento inativo.");
        mensagens.put("MN012", "Serviço inexistente para o Canal de Atendimento.");
        mensagens.put("MN013", "Serviço inativo para o Canal de Atendimento.");
        mensagens.put("MN014", "Meio de entrada inexistente para o serviço.");
        mensagens.put("MN015", "Meio de entrada inativo para o serviço.");
        mensagens.put("MN016", "Timeout na solicitação ao Provedor de Serviços.");
        mensagens.put("MN017", "Leiaute/Formato/Conteúdo da resposta da mensagem inválido.");
        mensagens.put("MN018", "Não foi possível gravar as informações de controle da transação.");
        mensagens.put("MN019", "[Técnica] Nao atende às premissas da transação. [Negocial] Horário inválido para realização da transação.");
        mensagens.put("MN020", "[Técnica] Nao atende às premissas da transação. [Negocial] Data inválida para realização da transação.");
        mensagens.put("MN021", "[Técnica] Nao atende às premissas da transação. [Negocial] Valor inferior ao valor mínimo para realização da transação.");
        mensagens.put("MN022", "[Técnica] Nao atende às premissas da transação. [Negocial] Valor superior ao valor máximo para realização da transação.");
        mensagens.put("MN023", "Tarefa para o serviço inativo.");
        mensagens.put("MN024", "[Técnica] Não foi possível recuperar as tarefas. [Negocial] Versão da tarefa inativa.");
        mensagens.put("MN025", "Não foi possível recuperar as tarefas.");
        mensagens.put("MN026", "Tarefa para o meio de entrada inativa.");
        mensagens.put("MN027", "Não foi possível se comunicar com o provedor de serviços.");
        
        
        mensagens.put("MN000002", "MN000002 - [Tecnica] Leiaute/Formato/Conteudo mensagem inválido");
        mensagens.put("MN0000022", "MN0000022 - [Tecnica] Leiaute/Formato/Conteudo mensagem inválido");
        mensagens.put("MN000003", "MN000003 - [Tecnica] Leiaute/Formato/Conteudo mensagem inválido");
        mensagens.put("MN000004", "MN000004 - [Tecnica] Dados Obrigatórios Nao identificados");
        mensagens.put("MN000006", "MN000006  -[Tecnica] Time out na solicitação ao provedor de Servicos");
        mensagens.put("MN000010", "MN000010 - [Tecnica] Servico Inativo");
        mensagens.put("MN000011", "MN000011 - [Tecnica] Servico Inexistente");
        mensagens.put("MN000012", "MN000012 - [Tecnica] Canal Inativo");
        mensagens.put("MN000013", "MN000013 - [Tecnica] Canal Inexistente");
        mensagens.put("MN000014", "MN000014 - [Tecnica] Nao Atende ás premissas da transação [Negocial] Servico Para o Canal Invalido");
        mensagens.put("MN000015", "MN000015 - [Tecnica] Nao Atende ás premissas da transação [Negocial] Servico Para o Canal Inexistente");
        mensagens.put("MN000016", "MN000016 - [Tecnica] VERSAO MEIO ENTRADA NAO INFORMADO NO ARQUIVO XML");
        mensagens.put("MN000017", "MN000017 - [Tecnica] Versão Servico Inativo");
        mensagens.put("MN000018", "MN000018 - [Tecnica] Versão Servico Inexistente! ");
        mensagens.put("MN000019", "MN000019 - [Tecnica] Modo de Entrada Inativo");
        mensagens.put("MN000020", "MN000020 - [Tecnica] Modo de Entrada Inexistente");
        mensagens.put("MN000021", "MN000021 - [Tecnica] Modo de Entrada Para o Servico Inativo");
        mensagens.put("MN000022", "MN000022 - [Tecnica] Modo de Entrada Para o Servico Inexistente");
        mensagens.put("MN000023", "MN000023 - [Tecnica] Versão Meio de Entrada Inativo");
        mensagens.put("MN000024", "MN000024 - [Tecnica] Versão Meio de Entrada Inexistente");
        mensagens.put("MN000025", "MN000025 - [Tecnica] Tarefa Meio de Entrada Inativo");
        mensagens.put("MN000026", "MN000026 - [Tecnica] Tarefa Meio de Entrada Inexistente");
        mensagens.put("MN000027", "MN000027 - [Tecnica] Nao Atende ás premissas da transação [Negocial] Valor Excede o Limite do Servico Por Canal");
        mensagens.put("MN000028", "MN000028 - [Tecnica] Nao Atende ás premissas da transação [Negocial] Valor Inferior Permitido do Servico por Canal");
        mensagens.put("MN000029", "MN000029 - [Tecnica] Nao Atende ás premissas da transação [Negocial] Data Indisponivel Para o Período Solicitado");
        mensagens.put("MN000030", "MN000030 - [Tecnica] Nao foi Possivel Recuperar as Tarefas");
        mensagens.put("MN000031", "MN000031 - [Tecnica] Nao foi Possivel Recuperar as Tarefas");
        mensagens.put("MN000032", "MN000032 - [Tecnica] Nao foi Possivel Recuperar as Tarefas [Negocial] Versão Tarefa Inativo! ");
        mensagens.put("MN000033", "MN000033 - [Tecnica] Nao foi Possivel Recuperar as Tarefas [Negocial] Versão Tarefa Inexistente! ");
        mensagens.put("MN000034", "MN000034 - [Tecnica] Nao foi Possivel Criar o Parametro! ");
        mensagens.put("MN000035", "MN000035 - [Tecnica] NSU MTX Nao Cadastrado! ");
        mensagens.put("MN000036", "MN000036 - [Tecnica] Nao foi Possivel Converter o NSU MTX para numérico! ");
        mensagens.put("MN000037", "MN000037 - [Tecnica] Quantidade de Caracteres ultrapassa o limite permitido (4000)! ");
        mensagens.put("MN000038", "MN000038 - [Tecnica] Nao Foi Possivel Alterar a Iteração com o Canal! ");
        mensagens.put("MN000039", "MN000039 - [Tecnica] Nao Foi Possivel Alterar a Transação! ");
        mensagens.put("MN000041", "MN000041 - [Tecnica] XML de Resposta Nao Valida com XSD Respota do Servico! ");
        mensagens.put("MN000042", "MN000042 - [Tecnica] Protocolo nao aceito pelo canal! ");
        mensagens.put("MN000043", "MN000043 - [Tecnica] Nao Atende ás premissas da transação [Negocial] Hora Indisponivel Para o Período Solicitado");
        mensagens.put("MN000044", "MN000044 - [Negocial] Tarefa para o Servico Inativo");
        mensagens.put("MN000045", "MN000045 - [Tecnica] Nao há Sequencia de Tarefas a ser Executada no Modo de Entrada [Negocial] Tarefas para o Meio de Entrada Inexistente");
        mensagens.put("MN000046", "MN000046 - [Tecnica] Leiaute da Mensagem de Saida Invalido");
        mensagens.put("MN000047", "MN000047 - [Tecnica] Sem Comunicação com o Canal");
        mensagens.put("MN000048", "MN000048 - [Tecnica] Resposta da Tarefa com Formato Invalido");
        mensagens.put("MN000049", "MN000049 - [Tecnica] Arquivo Nao Encontrado");
        mensagens.put("MN000050", "MN000050 - [Tecnica] Tarefa teve Resposta Impeditiva");
        mensagens.put("MN000051", "MN000051 - [Tecnica] Nao Foi Possivel se comunicar com o provedor de Servicos");
        mensagens.put("MN000052", "MN000052 - [Tecnica] Nao foi Possivel gerar NSU para controle dos Servicos");
        mensagens.put("MN000053", "MN000053 - [Tecnica] Nao foi Possivel gravar as informações de controle dos Servicos");
        mensagens.put("MN000054", "MN000054 - [Tecnica] Impedimento de Efetivacao / Cancelamento de Servico - (Transacao nao esta em estado pendente.");
        mensagens.put("MN000055", "MN000055 - [Tecnica] Nao há sequência de tarefas a ser executada no modo de entrada");
        mensagens.put("MN000056", "MN000056 - [Tecnica] Nao há sequência de tarefas a ser executada no negocial");
        mensagens.put("MN000057", "MN000057 - [Tecnica] Sem Comunicacao com o canal Solicitante");
        mensagens.put("MN000058", "MN000058 - [Tecnica] Nao foi possivel enviar resposta ao canal");
        mensagens.put("MN000059", "MN000059 - [Tecnica] Nao Foi Possivel se comunicar com o canal");
        mensagens.put("MN000060", "MN000060 - [Tecnica] Nao Atende ás premissas da transação");
        mensagens.put("MN000061", "MN000061 - [Negocial] Transacao origem diferente de finalizada.");
        mensagens.put("MN000062", "MN000062 - [Negocial] Nao ha tarefa financeira para o servico ser Estornado.");
        mensagens.put("MN000063", "MN000063 - [Negocial] Tarefa de Confirmação ou Cancelamento Inexistente.");
        mensagens.put("MN000064", "MN000064 - [Negocial] Serviço de Confirmação ou Cancelamento Inexistente.");
        mensagens.put("MN000065", "MN000065 - [Negocial] Data de requisicao diferente da Data atual.");
        mensagens.put("MN000066", "MN000066 - [Tecnica] Código de Retorno não cadastrado no SIMTX.");
        mensagens.put("MN000067", "MN000067 - [Tecnica] Não foi possível recuperar a fila de resposta do canal.");
        mensagens.put("MN000068", "MN000068 - [Negocial] Mensagem TOKEN não contem tag <CODIGO>");
    }
    
    /**
     * Gets the error code.
     *
     * @return the error code
     */
    public String getErrorCode() {
        return errorCode;
    }

    /**
     * Sets the error code.
     *
     * @param errorCode the new error code
     */
    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }
    
    /**
     * Insira aqui a descrição do método.
     * @see java.lang.Throwable#getMessage()
     */
    @Override
    public String getMessage() {
        return message;
    }

    /**
     * Sets the message.
     *
     * @param message the new message
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Gets the s header.
     *
     * @return the s header
     */
    public String getsHeader() {
        return sHeader;
    }

    /**
     * Sets the s header.
     *
     * @param sHeader the new s header
     */
    public void setsHeader(String sHeader) {
        this.sHeader = sHeader;
    }
    
    /**
     * Gets the nsu requisicao.
     *
     * @return the nsu requisicao
     */
    public String getNsuRequisicao() {
		return nsuRequisicao;
	}

	/**
	 * Sets the nsu requisicao.
	 *
	 * @param nsuRequisicao the new nsu requisicao
	 */
	public void setNsuRequisicao(String nsuRequisicao) {
		this.nsuRequisicao = nsuRequisicao;
	}
	
	/**
	 * Insira aqui a descrição do método.
	 * @see java.lang.Throwable#getCause()
	 */
	@Override
	public Throwable getCause() {
		return cause;
	}

	/**
	 * Sets the cause.
	 *
	 * @param cause the new cause
	 */
	public void setCause(Throwable cause) {
		this.cause = cause;
	}
	
	/**
	 * Insira aqui a descrição do método.
	 * @see java.lang.Throwable#toString()
	 */
	@Override
	public String toString() {
		if(this.mensagemXslt != null && !this.mensagemXslt.trim().isEmpty()) {
			return this.mensagemXslt;
		}
		else {
			return this.message;
		}
    }
}
