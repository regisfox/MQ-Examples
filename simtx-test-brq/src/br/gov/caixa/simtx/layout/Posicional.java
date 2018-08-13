package br.gov.caixa.simtx.layout;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;

import br.gov.caixa.simtx.dominio.Convertable;

public abstract class Posicional implements Convertable {
	
	SimpleDateFormat dateFormat = new SimpleDateFormat("ddMMyyyy");
	SimpleDateFormat dateFormatReverse = new SimpleDateFormat("yyyyMMdd");
	
	public String padNumero(String valor, int tamanho) {
		return StringUtils.leftPad(valor, tamanho, '0');
	}
	
	public String padTexto(String valor, int tamanho) {
		return StringUtils.leftPad(valor, tamanho, 'x');
	}
	
	public String formata(String valor, int tamanho) {
		return StringUtils.leftPad(valor, tamanho, '0') ;
	}
	
	public String formataData(Date valor) {
		return dateFormat.format(valor);
	}
	
	public String formataDataReverso(Date valor) {
		return dateFormatReverse.format(valor);
	}
	
	public String formata(Integer valor, int tamanho) {
		return padNumero(String.valueOf(valor), tamanho); 
	}
	
	public String formata(Long valor, int tamanho) {
		return padNumero(String.valueOf(valor), tamanho);
	}
	
	
}
