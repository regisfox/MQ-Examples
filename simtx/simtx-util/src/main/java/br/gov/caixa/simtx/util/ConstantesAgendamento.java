package br.gov.caixa.simtx.util;

public class ConstantesAgendamento {

	public static final long CODIGO_SERVICO_AGENDAMENTO = 110033L;

	public static final int VERSAO_SERVICO_AGENDAMENTO = 1;

	public static final long CODIGO_SERVICO_PAGAMENTO_BOLETO_NPC = 110032L;

	public static final long CODIGO_SERVICO_PAGAMENTO_BOLETO = 110039L;

	public static final long CODIGO_TAREFA_CONSULTA_BOLETO = 100050L;
	
	public static final long CODIGO_TAREFA_VALIDA_REGRAS_BOLETO = 100068L;

	public static final long VERSAO_TAREFA_CONSULTA_BOLETO = 1L;

	public static final Integer SITUACAO_AGENDADO = 1;

	public static final Integer SITUACAO_EFETIVADO = 2;

	public static final Integer SITUACAO_CANCELADO = 3;
	
	public static final Integer SITUACAO_NEGADO = 4;

	public static final String SITUACAO_FINAL_AGENDAMENTO_EFETIVADO = "Situacao final do Agendamento: [2] Efetivado";

	public static final String SITUACAO_FINAL_AGENDAMENTO_CANCELADO = "Situacao final do Agendamento: [3] Cancelado";
	
	public static final String SITUACAO_FINAL_AGENDAMENTO_NEGADO = "Situacao final do Agendamento: [4] Negado";

	public static final String SITUACAO_IMPEDITIVA = "IMPEDITIVA";

	public static final String ULTIMA_EXECUCAO = "Ultima tentativa de execucao do dia, portanto, transacao sera negada";
	
	public static final String MODELO_CALCULO = "03";

	private ConstantesAgendamento() {
	}

}
