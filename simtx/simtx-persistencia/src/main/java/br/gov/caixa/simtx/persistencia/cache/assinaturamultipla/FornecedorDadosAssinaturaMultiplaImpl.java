/*******************************************************************************
 * Copyright (C)  2017 - CAIXA Econômica Federal 
 * 
 * Todos os direitos reservados
 ******************************************************************************/
package br.gov.caixa.simtx.persistencia.cache.assinaturamultipla;

import java.io.Serializable;
import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import br.gov.caixa.simtx.persistencia.assinaturamultipla.dao.DaoAssinaturaMultipla;
import br.gov.caixa.simtx.persistencia.assinaturamultipla.entidade.Mtxtb027TransacaoAssinaturaMultipla;
import br.gov.caixa.simtx.persistencia.assinaturamultipla.entidade.Mtxtb028ControleAssinaturaMultipla;
import br.gov.caixa.simtx.persistencia.exception.SemResultadoException;

@Stateless
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
public class FornecedorDadosAssinaturaMultiplaImpl implements FornecedorDadosAssinaturaMultipla, Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private DaoAssinaturaMultipla daoAssinaturaMultipla;
	
	public Mtxtb027TransacaoAssinaturaMultipla salvar(Mtxtb027TransacaoAssinaturaMultipla assinaturaMultipla) {
		return daoAssinaturaMultipla.salvar(assinaturaMultipla);
	}

	public Mtxtb028ControleAssinaturaMultipla salvar(Mtxtb028ControleAssinaturaMultipla assinatura) {
		return daoAssinaturaMultipla.salvar(assinatura);
	}
	
	public Mtxtb027TransacaoAssinaturaMultipla buscarAssinaturaMultipla(long nsu) throws SemResultadoException {
		return daoAssinaturaMultipla.buscarAssinaturaMultipla(nsu);
	}
	
	public List<Mtxtb027TransacaoAssinaturaMultipla> buscarAssinaturasMultiplasPendentes(int unidade, int operacao, long numeroConta, int dv, int tipoConta) {
		return daoAssinaturaMultipla.buscarAssinaturasMultiplasPendentes(unidade, operacao, numeroConta, dv, tipoConta);
	}

	public List<Mtxtb027TransacaoAssinaturaMultipla> buscarAssinaturasMultiplasPendentesNaoRelacionadas(int unidade,
			int operacao, long numero, int dv, int indicadorTipoConta, List<Long> nsuListBarramento) {
		return daoAssinaturaMultipla.buscarAssinaturasMultiplasPendentesNaoRelacionadas(unidade, operacao, numero, dv, indicadorTipoConta, nsuListBarramento);
	}
	
}
