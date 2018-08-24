package br.gov.caixa.simtx.persistencia.core.dao.impl;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import br.gov.caixa.simtx.persistencia.core.dao.DaoMarcasConta;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb032MarcaConta;

@Stateless
public class DaoMarcasContaImpl implements DaoMarcasConta {

    @PersistenceContext(name = "simtx_dao", unitName = "simtx_dao")
    private EntityManager entityManager;

    public List<Mtxtb032MarcaConta> buscarMarcasContas(Mtxtb032MarcaConta mtxtb032MarcasConta){
        return this.entityManager.createNamedQuery("Mtxtb032MarcaConta.findAll", Mtxtb032MarcaConta.class)
            .setParameter("nuMarcaConta", mtxtb032MarcasConta.getNuMarcaConta()).getResultList();
    }

}
