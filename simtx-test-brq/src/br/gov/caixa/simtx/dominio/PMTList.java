package br.gov.caixa.simtx.dominio;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("PMTLIST")
public class PMTList {
	private String pmt01;
	private String pmt02;
	private String pmt03;
	private String pmt04;
	
	public String getPmt01() {
		return pmt01;
	}
	public void setPmt01(String pmt01) {
		this.pmt01 = pmt01;
	}
	public String getPmt02() {
		return pmt02;
	}
	public void setPmt02(String pmt02) {
		this.pmt02 = pmt02;
	}
	public String getPmt03() {
		return pmt03;
	}
	public void setPmt03(String pmt03) {
		this.pmt03 = pmt03;
	}
	public String getPmt04() {
		return pmt04;
	}
	public void setPmt04(String pmt04) {
		this.pmt04 = pmt04;
	}
}
