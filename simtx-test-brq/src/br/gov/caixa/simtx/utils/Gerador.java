package br.gov.caixa.simtx.utils;

import java.util.Random;

public class Gerador {
	
	private Gerador(){}
	
	public static Long getNsu(long start){
		return new Random().nextInt(999999999) + start;
	}
	
	public static Integer getNsuInteiro(int start){
		return new Random().nextInt(999999999) + start;
	}

	public static String getValor() {
		Integer valor1 = new Random().nextInt(50) + 50;
		Integer valor2 = new Random().nextInt(89) + 10;
		
		String valor = valor1 + "" + valor2;
		
		return valor;
	}
}
