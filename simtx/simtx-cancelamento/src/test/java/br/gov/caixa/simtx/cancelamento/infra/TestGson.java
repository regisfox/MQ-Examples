package br.gov.caixa.simtx.cancelamento.infra;

import org.junit.Test;

import com.google.gson.Gson;

import br.gov.caixa.simtx.cancelamento.beans.MtxTransacaoContaEntrada;

public class TestGson {

	@Test
	public void test() {
		MtxTransacaoContaEntrada mtxTransacaoConta = new MtxTransacaoContaEntrada();
		Gson gson = new Gson();
		String teste = gson.toJson(mtxTransacaoConta);
		System.out.println(teste);
				
	}

}
