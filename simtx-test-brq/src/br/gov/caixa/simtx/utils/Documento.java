package br.gov.caixa.simtx.utils;

import java.util.Random;

import org.apache.commons.lang3.StringUtils;


public class Documento {
	
	private Documento(){}
	
	public static String novoCPF() {
		String iniciais = "";
		Integer numero;
		for (int i = 0; i < 9; i++) {
			numero = new Integer((int) (Math.random() * 10));
			iniciais += numero.toString();
		}
		return iniciais + calcDigVerif(iniciais);
	}

	private static String calcDigVerif(String num) {
		Integer primDig, segDig;
		int soma = 0, peso = 10;
		for (int i = 0; i < num.length(); i++)
			soma += Integer.parseInt(num.substring(i, i + 1)) * peso--;

		if (soma % 11 == 0 | soma % 11 == 1)
			primDig = new Integer(0);
		else
			primDig = new Integer(11 - (soma % 11));

		soma = 0;
		peso = 11;
		for (int i = 0; i < num.length(); i++)
			soma += Integer.parseInt(num.substring(i, i + 1)) * peso--;

		soma += primDig.intValue() * 2;
		if (soma % 11 == 0 | soma % 11 == 1)
			segDig = new Integer(0);
		else
			segDig = new Integer(11 - (soma % 11));

		return primDig.toString() + segDig.toString();
	}

	public static boolean validaCPF(String cpf) {
		if (cpf.length() != 11)
			return false;

		String numDig = cpf.substring(0, 9);
		return calcDigVerif(numDig).equals(cpf.substring(9, 11));
	}
	
	
	public static String novaContaSidec() {
		Random r = new Random(System.currentTimeMillis());
		Integer age = 1679;//r.nextInt(9999);
		Integer prod = 1;//r.nextInt(999);
		Integer num = r.nextInt(99999);
		
		//String a = StringUtils.leftPad(age.toString(), 4, '0');
		String p = StringUtils.leftPad(prod.toString(), 3, '0');
		//p = "013";
		String n = StringUtils.leftPad(num.toString(), 9, '0');
		
		return age.toString() + p + n; 
	}
	
	public static String[] novaContaSidecArray() {
		Random r = new Random(System.currentTimeMillis());
		Integer age = 1679;
		Integer prod = 1;
		Integer num = r.nextInt(99999);
		
		String p = StringUtils.leftPad(prod.toString(), 3, '0');
		String n = StringUtils.leftPad(num.toString(), 9, '0');
		
		return new String[]{age.toString(), p, n};
	}
	
	public static String novoCNPJ() {
		int n = 9;
		int n1 = randomiza(n);
		int n2 = randomiza(n);
		int n3 = randomiza(n);
		int n4 = randomiza(n);
		int n5 = randomiza(n);
		int n6 = randomiza(n);
		int n7 = randomiza(n);
		int n8 = randomiza(n);
		int n9 = 0; //randomiza(n);
		int n10 = 0; //randomiza(n);
		int n11 = 0; //randomiza(n);
		int n12 = 1; //randomiza(n);
		int d1 = n12 * 2 + n11 * 3 + n10 * 4 + n9 * 5 + n8 * 6 + n7 * 7 + n6 * 8 + n5 * 9 + n4 * 2 + n3 * 3 + n2 * 4 + n1 * 5;

		d1 = 11 - (mod(d1, 11));

		if (d1 >= 10)
			d1 = 0;

		int d2 = d1 * 2 + n12 * 3 + n11 * 4 + n10 * 5 + n9 * 6 + n8 * 7 + n7 * 8 + n6 * 9 + n5 * 2 + n4 * 3 + n3 * 4 + n2 * 5 + n1 * 6;

		d2 = 11 - (mod(d2, 11));

		if (d2 >= 10)
			d2 = 0;

		String retorno = null;

		retorno = "" + n1 + n2 + n3 + n4 + n5 + n6 + n7 + n8 + n9 + n10 + n11 + n12 + d1 + d2;

		return retorno;
	}
	
	private static int mod(int dividendo, int divisor) {
		return (int) Math.round(dividendo - (Math.floor(dividendo / divisor) * divisor));
	}
	
	private static int randomiza(int n) {
		int ranNum = (int) (Math.random() * n);
		return ranNum;
	}
	
	public static String chipras() {
		return "SIPCS" + 
				StringUtils.leftPad(Integer.toString(new Random(System.currentTimeMillis()).nextInt(9999999)), 7, '0');   
	}
}
