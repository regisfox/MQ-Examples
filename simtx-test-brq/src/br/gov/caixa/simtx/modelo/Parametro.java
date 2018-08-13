package br.gov.caixa.simtx.modelo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "pmt")
public class Parametro {
	
	@Id
	private long pmtcod;
	private String pmtdes;
	private String pmtval;
	private Integer pmttip;

	public long getPmtcod() {
		return pmtcod;
	}

	public void setPmtcod(long pmtcod) {
		this.pmtcod = pmtcod;
	}

	public String getPmtdes() {
		return pmtdes;
	}

	public void setPmtdes(String pmtdes) {
		this.pmtdes = pmtdes;
	}

	public String getPmtval() {
		return pmtval;
	}

	public void setPmtval(String pmtval) {
		this.pmtval = pmtval;
	}
	
	public List<String> getValorComoLista() {
		String[] valores = this.pmtval.split(";");
		return Arrays.asList(valores);
	}
	
	public List<Integer> getValorComoListaDeInteiros() {
		String[] valores = this.pmtval.split(";");
		List<Integer> retorno = new ArrayList<Integer>();
		for(String v : valores) {
			retorno.add(Integer.valueOf(v));
		}
		
		return retorno;
	}
	
	public List<Long> getValorComoListaDeLongs() {
		String[] valores = this.pmtval.split(";");
		List<Long> retorno = new ArrayList<Long>();
		for(String v : valores) {
			retorno.add(Long.valueOf(v));
		}
		
		return retorno;
	}
	
	public Integer getValorComoInteiro() {
		return Integer.valueOf(this.pmtval);
	}

	public Integer getPmttip() {
		return pmttip;
	}

	public void setPmttip(Integer pmttip) {
		this.pmttip = pmttip;
	}
	
	

}
