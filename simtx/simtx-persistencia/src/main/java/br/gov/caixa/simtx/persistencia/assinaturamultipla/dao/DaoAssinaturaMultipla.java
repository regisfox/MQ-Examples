/*******************************************************************************
 * Copyright (C)  2017 - CAIXA Econômica Federal 
 * 
 * Todos os direitos reservados
 ******************************************************************************/
package br.gov.caixa.simtx.persistencia.assinaturamultipla.dao;

import java.util.List;

import br.gov.caixa.simtx.persistencia.assinaturamultipla.entidade.Mtxtb027TransacaoAssinaturaMultipla;
import br.gov.caixa.simtx.persistencia.assinaturamultipla.entidade.Mtxtb028ControleAssinaturaMultipla;
import br.gov.caixa.simtx.persistencia.exception.SemResultadoException;

public interface DaoAssinaturaMultipla {
	public Mtxtb027TransacaoAssinaturaMultipla salvar(Mtxtb027TransacaoAssinaturaMultipla assinaturaMultipla);

	public Mtxtb028ControleAssinaturaMultipla salvar(Mtxtb028ControleAssinaturaMultipla assinatura);

	public Mtxtb027TransacaoAssinaturaMultipla buscarAssinaturaMultipla(long nsu) throws SemResultadoException;

	public List<Mtxtb027TransacaoAssinaturaMultipla> buscarAssinaturasMultiplasPendentes(int unidade, int operacao,
			long numeroConta, int dv, int tipoConta);
	
	public List<Mtxtb027TransacaoAssinaturaMultipla> buscarAssinaturasMultiplasPendentesNaoRelacionadas(int unidade,
			int operacao, long numero, int dv, int indicadorTipoConta, List<Long> nsuListBarramento);
	
}
