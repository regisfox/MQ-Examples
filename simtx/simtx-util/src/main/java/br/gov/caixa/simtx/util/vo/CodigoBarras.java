package br.gov.caixa.simtx.util.vo;

import java.math.BigDecimal;
import java.util.Date;

public class CodigoBarras {
	
	public static final int POSICAO_SEGMENTO1_INICIO = 0;
	public static final int POSICAO_SEGMENTO1_FIM = 10;
	
	public static final int POSICAO_SEGMENTO2_INICIO = 10;
	public static final int POSICAO_SEGMENTO2_FIM = 21;
	
	public static final int POSICAO_SEGMENTO3_INICIO = 21;
	public static final int POSICAO_SEGMENTO3_FIM = 32;
	
	public static final int POSICAO_SEGMENTO4_INICIO = 32;
	public static final int POSICAO_SEGMENTO4_FIM = 33;
	
	public static final int POSICAO_SEGMENTO5_INICIO = 33;
	
	public static final int POSICAO_FATOR_VENCIMENTO_INICIO = 5;
	public static final int POSICAO_FATOR_VENCIMENTO_FIM = 9;

	public static final int POSICAO_VALOR_TITULO_INICIO = 9;
	public static final int POSICAO_VALOR_TITULO_FIM = 19;
	
	private String segmento1;
	
	private String segmento2;
	
	private String segmento3;
	
	private String segmento4;
	
	private String segmento5;
	
	private int fatorVencimento;
	
	private BigDecimal valorTitulo;
	
	private Date dataVencimento;

	
	
	public String getSegmento1() {
		return segmento1;
	}

	public void setSegmento1(String segmento1) {
		this.segmento1 = segmento1;
	}

	public String getSegmento2() {
		return segmento2;
	}

	public void setSegmento2(String segmento2) {
		this.segmento2 = segmento2;
	}

	public String getSegmento3() {
		return segmento3;
	}

	public void setSegmento3(String segmento3) {
		this.segmento3 = segmento3;
	}

	public String getSegmento4() {
		return segmento4;
	}

	public void setSegmento4(String segmento4) {
		this.segmento4 = segmento4;
	}

	public String getSegmento5() {
		return segmento5;
	}

	public void setSegmento5(String segmento5) {
		this.segmento5 = segmento5;
	}

	public int getFatorVencimento() {
		return fatorVencimento;
	}

	public void setFatorVencimento(int fatorVencimento) {
		this.fatorVencimento = fatorVencimento;
	}

	public BigDecimal getValorTitulo() {
		return valorTitulo;
	}

	public void setValorTitulo(BigDecimal valorTitulo) {
		this.valorTitulo = valorTitulo;
	}

	public Date getDataVencimento() {
		return dataVencimento;
	}

	public void setDataVencimento(Date dataVencimento) {
		this.dataVencimento = dataVencimento;
	}
	

}
