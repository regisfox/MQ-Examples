/*******************************************************************************
 * Copyright (C)  2017 - CAIXA Econômica Federal 
 * 
 * Todos os direitos reservados
 ******************************************************************************/
package br.gov.caixa.simtx.persistencia.core.dao;

import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb023Parametro;

public interface DaoParammetro {

    public Mtxtb023Parametro loadParam(long code);

    public void alterar(Mtxtb023Parametro parametro);

}
