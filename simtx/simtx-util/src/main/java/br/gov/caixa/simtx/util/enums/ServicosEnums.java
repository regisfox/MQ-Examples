package br.gov.caixa.simtx.util.enums;

import br.gov.caixa.simtx.util.constantes.ConstantesLookup;

public enum ServicosEnums {

	DESFAZIMENTO(110043L, "NAO", ConstantesLookup.PROCESSADOR_DESFAZIMENTO_JNDI),

	RETOMAR_TRANSACAO(110045L, "NAO", ConstantesLookup.PROCESSADOR_RETOMADA_JNDI),
	
	CONTRA_ORDEM_CHEQUES(110030L, "NAO", ConstantesLookup.PROCESSADOR_CORE_JNDI),
	
	CONTRA_ORDEM_CHEQUES_ASSINATURA_MULTIPLA(110030L, "SIM", ""),
	
	CONSULTA_CADASTRO_POSITIVO(110021L, "NAO", ConstantesLookup.PROCESSADOR_CORE_JNDI),
	
	CONSULTA_EXTRATO(110044L, "NAO", ConstantesLookup.PROCESSADOR_CORE_JNDI),
	
	LOGIN_SIMAI(110028L, "NAO", ConstantesLookup.PROCESSADOR_CORE_JNDI),
	
	CONSULTA_BOLETO(110029L, "NAO", ConstantesLookup.PROCESSADOR_CORE_JNDI),
	
	VALIDA_BOLETO_NPC(110031L, "NAO", ConstantesLookup.PROCESSADOR_CORE_JNDI),
	
	VALIDA_BOLETO(110038L, "NAO", ConstantesLookup.PROCESSADOR_CORE_JNDI),

	VALIDA_BOLETO_NPC_AGENDAMENTO(110031L, "SIM", ConstantesLookup.PROCESSADOR_VALIDA_BOLETO_JNDI),
	
	VALIDA_BOLETO_AGENDAMENTO(110038L, "SIM", ConstantesLookup.PROCESSADOR_VALIDA_BOLETO_JNDI),
	
	PAGAMENTO_BOLETO_NPC(110032L, "NAO", ConstantesLookup.PROCESSADOR_CORE_JNDI),
	
	PAGAMENTO_BOLETO(110039L, "NAO", ConstantesLookup.PROCESSADOR_CORE_JNDI),
	
	PAGAMENTO_BOLETO_NPC_AGENDAMENTO(110032L, "SIM", ConstantesLookup.PROCESSADOR_AGENDAMENTO_JNDI),
	
	PAGAMENTO_BOLETO_AGENDAMENTO(110039L, "SIM", ConstantesLookup.PROCESSADOR_AGENDAMENTO_JNDI),
	
	LISTA_AGENDAMENTOS(110036L, "NAO", ConstantesLookup.PROCESSADOR_OPERACOES_AGENDAMENTO_JNDI),
	
	DETALHE_AGENDAMENTO(110041L, "NAO", ConstantesLookup.PROCESSADOR_OPERACOES_AGENDAMENTO_JNDI),
	
	CANCELAMENTO_AGENDAMENTO(110037L, "NAO", ConstantesLookup.PROCESSADOR_OPERACOES_AGENDAMENTO_JNDI),
	
	LISTA_COMPROVANTES(110026L, "NAO", ConstantesLookup.PROCESSADOR_COMPROVANTE_JNDI),
	
	DETALHE_COMPROVANTE(110027L, "NAO", ConstantesLookup.PROCESSADOR_COMPROVANTE_JNDI),
	
	LISTA_ASSINATURA_MULTIPLA(110023L, "NAO", ConstantesLookup.LISTA_ASSINATURA_MULTIPLA_JNDI),
	
	DETALHE_ASSINATURA_MULTIPLA(110024L, "NAO", ConstantesLookup.DETALHE_ASSINATURA_MULTIPLA_JNDI);
	
	
	private Long codigoServico;
	
	private String dataFutura;
	
	private String jndi;

	private ServicosEnums(Long codigoServico, String dataFutura, String jndi) {
		this.codigoServico = codigoServico;
		this.dataFutura = dataFutura;
		this.jndi = jndi;
	}
	
	public static ServicosEnums obterServico(long codServico) {
		ServicosEnums retorno = null;
		for(ServicosEnums servico : values()) {
			if (servico.getCodigoServico() == codServico) {
				retorno = servico;
				break;
			}
		}
		return retorno;
	}
	
	public static ServicosEnums obterServico(long codServico, String dataFurura) {
		ServicosEnums retorno = null;
		for (ServicosEnums servico : values()) {
			if (servico.getCodigoServico() == codServico && servico.getDataFutura().equals(dataFurura)) {
				retorno = servico;
				break;
			}
		}
		return retorno;
	}

	public Long getCodigoServico() {
		return codigoServico;
	}
	
	public String getDataFutura() {
		return dataFutura;
	}

	public String getJndi() {
		return jndi;
	}
	
}
