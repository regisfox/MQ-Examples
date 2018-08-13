package br.gov.caixa.simtx.dominio;

import com.thoughtworks.xstream.XStream;


public class ServicoEntrada implements Convertable {
	
	private Operacao OPERACAO;
	private Dados DADOS;

	public Operacao getOPERACAO() {
		return OPERACAO;
	}
	public void setOPERACAO(Operacao oPERACAO) {
		OPERACAO = oPERACAO;
	}
	public Dados getDADOS() {
		return DADOS;
	}
	public void setDADOS(Dados dADOS) {
		DADOS = dADOS;
	}
	
	public String converter() {
		XStream xs = new XStream();
		xs.alias("SERVICO_ENTRADA", ServicoEntrada.class);
		xs.alias("DADOS", Dados.class);
		xs.alias("PMTLST", PMTList.class);
		xs.alias("OPERACAO", Operacao.class);
		
		return xs.toXML(this).replaceAll("__", "_");
	}
}
