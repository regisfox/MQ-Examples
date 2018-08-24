package br.gov.caixa.simtx.persistencia.vo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ParametrosFatorVencimento {
	
	public static final long PMT_DATA_BASE_ATUAL = 19;
	
	public static final long PMT_DATA_BASE_NOVA = 20;
	
	public static final long PMT_RANGE_VENCIDO = 21;
	
	public static final long PMT_RANGE_A_VENCER = 22;

	private static final String DATA_BASE = "07/10/1997";
	
	private static final String DATA_IMPLANTACAO = "12/03/2014";
	
	private Date dataImplantacao;
	
	private Date dataBase;
	
	private Date dataBaseAtual;
	
	private Date dataBaseNova;
	
	private int rangeVencido;
	
	private int rangeAVencer;
	
	
	public ParametrosFatorVencimento() throws ParseException {
		this.dataBase = new SimpleDateFormat("dd/MM/yyyy").parse(DATA_BASE);
		this.dataImplantacao = new SimpleDateFormat("dd/MM/yyyy").parse(DATA_IMPLANTACAO);
	}

	public Date getDataImplantacao() {
		return dataImplantacao;
	}

	public void setDataImplantacao(Date dataImplantacao) {
		this.dataImplantacao = dataImplantacao;
	}

	public Date getDataBase() {
		return dataBase;
	}

	public void setDataBase(Date dataBase) {
		this.dataBase = dataBase;
	}

	public Date getDataBaseAtual() {
		return dataBaseAtual;
	}

	public void setDataBaseAtual(Date dataBaseAtual) {
		this.dataBaseAtual = dataBaseAtual;
	}

	public Date getDataBaseNova() {
		return dataBaseNova;
	}

	public void setDataBaseNova(Date dataBaseNova) {
		this.dataBaseNova = dataBaseNova;
	}

	public int getRangeVencido() {
		return rangeVencido;
	}

	public void setRangeVencido(int rangeVencido) {
		this.rangeVencido = rangeVencido;
	}

	public int getRangeAVencer() {
		return rangeAVencer;
	}

	public void setRangeAVencer(int rangeAVencer) {
		this.rangeAVencer = rangeAVencer;
	}
	
}
