package br.gov.caixa.simtx.web.util;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import br.gov.caixa.simtx.persistencia.cache.core.FornecedorDados;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb037Funcionalidade;

public class MatrizAcesso {
	
	@Inject
	FornecedorDados fornecedorDados;
	
	public boolean verificaPermissao(String nomeGrupo, String funcionalidade){
		
		List <Mtxtb037Funcionalidade> funcionalidadeAcesso = 
		fornecedorDados.buscaGrupoAcesso(nomeGrupo).getMtxtb037Funcionalidade();
		ArrayList<String> listaFuncionalidades = new ArrayList<>();
		
		for(Mtxtb037Funcionalidade func :funcionalidadeAcesso){
			listaFuncionalidades.add(func.getNoFuncionalidade());
		}
		return listaFuncionalidades.contains(funcionalidade);
	}
}
