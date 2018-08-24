package br.gov.caixa.simtx.processador.lote.agendamento.temporizador;

public class TemporizadorParametros {

	private String hora;
	private String minuto;
	private String intervaloPeriodo;
	private String mes;
	private String dia;
	private String ano;
	private String periodoDias;

	public String getHora() {
		return hora;
	}

	public void setHora(String hora) {
		this.hora = hora;
	}

	public String getMinuto() {
		return minuto;
	}

	public void setMinuto(String minuto) {
		this.minuto = minuto;
	}

	public String getIntervaloPeriodo() {
		return intervaloPeriodo;
	}

	public void setIntervaloPeriodo(String intervaloPeriodo) {
		this.intervaloPeriodo = intervaloPeriodo;
	}

	public String getMes() {
		return mes;
	}

	public void setMes(String mes) {
		this.mes = mes;
	}

	public String getDia() {
		return dia;
	}

	public void setDia(String dia) {
		this.dia = dia;
	}

	public String getAno() {
		return ano;
	}

	public void setAno(String ano) {
		this.ano = ano;
	}

	public String getPeriodoDias() {
		return periodoDias;
	}

	public void setPeriodoDias(String periodoDias) {
		this.periodoDias = periodoDias;
	}

}
