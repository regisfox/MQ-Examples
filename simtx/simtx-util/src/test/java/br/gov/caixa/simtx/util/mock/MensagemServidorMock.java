package br.gov.caixa.simtx.util.mock;

import java.io.Serializable;

import javax.enterprise.inject.Alternative;

import br.gov.caixa.simtx.util.gerenciador.Mensagem;

@Alternative
public class MensagemServidorMock extends Mensagem implements Serializable  {

	private static final long serialVersionUID = 1L;
	
}
