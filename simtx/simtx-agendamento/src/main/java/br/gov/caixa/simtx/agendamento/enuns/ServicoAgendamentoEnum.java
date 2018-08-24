package br.gov.caixa.simtx.agendamento.enuns;

import java.util.ArrayList;
import java.util.List;

public enum ServicoAgendamentoEnum {
	
	EFETIVA_AGENDAMENTO_BOLETO_NPC(110032l, 1, 110035L, 1),

	PAGAMENTO_AGENDAMENTO_BOLETO(110039l, 1, 110040L, 1),
	
	CONSULTA_LISTA_AGENDAMENTO_BOLETO(110036L, 1, 110036L, 1),
	
	CANCELAMENTO_AGENDAMENTO_BOLETO(110037L, 1, 110037L, 1),
	
	CONSULTA_DETALHE_AGENDAMENTO_BOLETO(110041L, 1, 110041L, 1);
	
	private long servicoOrigem;
	
	private long servicoFinal;
	
	private long versaoServicoOrigem;
	
	private int versaoServicoFinal;
	
	
	private ServicoAgendamentoEnum(long servicoOrigem, int versaoServicoOrigem, long servicoFinal,
			int versaoServicoFinal) {
		this.servicoOrigem = servicoOrigem;
		this.versaoServicoOrigem = versaoServicoOrigem;
		this.servicoFinal = servicoFinal;
		this.versaoServicoFinal = versaoServicoFinal;
	}

	public static ServicoAgendamentoEnum obterServicoFinal(long servicoOrigem, long versaoServicoOrigem) {
		ServicoAgendamentoEnum retorno = null;
		if(servicoOrigem > 0) {
			for(ServicoAgendamentoEnum servico : values()) {
				if (servico.getServicoOrigem() == servicoOrigem
						&& servico.getVersaoServicoOrigem() == versaoServicoOrigem) {
					retorno = servico;
					break;
				}
			}
		}
		return (retorno != null) ? retorno : null;
	}
	
	public static List<Long> listCodigosServicos() {
		List<Long> list = new ArrayList<>();
		for (ServicoAgendamentoEnum codigoEnum : values()) {
			list.add(codigoEnum.getServicoFinal());
		}
		return list;
	}
	
	public long getServicoOrigem() {
		return servicoOrigem;
	}

	public long getServicoFinal() {
		return servicoFinal;
	}

	public long getVersaoServicoOrigem() {
		return versaoServicoOrigem;
	}

	public int getVersaoServicoFinal() {
		return versaoServicoFinal;
	}
	
}
