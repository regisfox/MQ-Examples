package br.gov.caixa.simtx.dominio;

public interface Convertable {
	public static final String XML_CABECALHO = "<?xml version='1.0' encoding='UTF-8'?>";
	public abstract String converter();
}
