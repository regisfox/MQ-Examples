package br.gov.caixa.simtx.util.constantes;

public class ConstantesLookup {
	
	private ConstantesLookup() {}
	
	public static final String PROCESSADOR_CORE_JNDI = "java:global/simtx/simtx-core/ProcessadorCore";
	
	public static final String PROCESSADOR_VALIDA_BOLETO_JNDI = "java:global/simtx/simtx-agendamento/ValidaBoletoAgendamento";
	
	public static final String PROCESSADOR_AGENDAMENTO_JNDI = "java:global/simtx/simtx-agendamento/ProcessadorAgendamento";
	
	public static final String PROCESSADOR_OPERACOES_AGENDAMENTO_JNDI = "java:global/simtx/simtx-agendamento/ProcessadorOperacoesAgendamento";
	
	public static final String PROCESSADOR_DESFAZIMENTO_JNDI = "java:global/simtx/simtx-manutencao/ProcessadorDesfazimento";
	
	public static final String PROCESSADOR_RETOMADA_JNDI = "java:global/simtx/simtx-manutencao/ProcessadorRetomadaTransacao";
	
	public static final String PROCESSADOR_COMPROVANTE_JNDI = "java:global/simtx/simtx-comprovante/ProcessadorComprovante";
	
	public static final String LISTA_ASSINATURA_MULTIPLA_JNDI = "java:global/simtx/simtx-assinatura-multipla/ListaAssinaturaMultipla";
	
	public static final String DETALHE_ASSINATURA_MULTIPLA_JNDI = "java:global/simtx/simtx-assinatura-multipla/DetalheAssinaturaMultipla";

}
