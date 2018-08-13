package br.gov.caixa.simtx.queue;

public interface CaixaQueue {

	public void callQueue(String ambiente, String nomeFila, String xmlPath) throws Exception;
}
