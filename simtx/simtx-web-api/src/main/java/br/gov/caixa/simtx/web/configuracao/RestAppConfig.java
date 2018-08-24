package br.gov.caixa.simtx.web.configuracao;

import java.util.Set;

import javax.ws.rs.core.Application;

@javax.ws.rs.ApplicationPath("web")
public class RestAppConfig extends Application {
	// Obtém um conjunto de classes de recursos e provedores da raiz
	@Override
	public Set<Class<?>> getClasses() {
		final Set<Class<?>> resources = new java.util.HashSet<>();
		resources.add(br.gov.caixa.simtx.web.controles.LoginControle.class);
		resources.add(br.gov.caixa.simtx.web.controles.PagamentoControle.class);
		resources.add(br.gov.caixa.simtx.web.controles.CanalControle.class);
		resources.add(br.gov.caixa.simtx.web.controles.AgendamentoControle.class);
		resources.add(br.gov.caixa.simtx.web.controles.ServicoControle.class);
		resources.add(br.gov.caixa.simtx.web.controles.ParametroBoletoContingenciaControle.class);
		return resources;
	}
}
