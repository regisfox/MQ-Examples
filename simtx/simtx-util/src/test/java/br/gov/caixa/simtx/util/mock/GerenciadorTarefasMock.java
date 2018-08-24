package br.gov.caixa.simtx.util.mock;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.inject.Alternative;

import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb002Tarefa;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb003ServicoTarefa;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb003ServicoTarefaPK;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb004Canal;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb008MeioEntrada;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb011VersaoServico;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb012VersaoTarefa;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb012VersaoTarefaPK;
import br.gov.caixa.simtx.util.exception.ServicoException;
import br.gov.caixa.simtx.util.gerenciador.GerenciadorTarefas;

@Alternative
public class GerenciadorTarefasMock extends GerenciadorTarefas {
	private static final long serialVersionUID = 1L;
	
	@Override
	public List<Mtxtb003ServicoTarefa> carregarTarefasServico(Mtxtb011VersaoServico servico, Mtxtb004Canal canal,
			Mtxtb008MeioEntrada meioEntrada) throws ServicoException {
		
		if(servico.getId().getNuServico001() == 110023) {
			
			List<Mtxtb003ServicoTarefa> lista = new ArrayList<>();
			
			Mtxtb003ServicoTarefaPK servicoTarefaPK = new Mtxtb003ServicoTarefaPK();
			servicoTarefaPK.setNuTarefa012(100045);
			servicoTarefaPK.setNuVersaoTarefa012(1);

			Mtxtb002Tarefa tarefa = new Mtxtb002Tarefa();
			tarefa.setNuTarefa(100045);
			tarefa.setNoTarefa("Lista de transacos pendentes de assinatura");
			tarefa.setNoServicoBarramento("consulta_assinatura_eletronica");
			tarefa.setNoOperacaoBarramento("LISTA_TRANSACOES_PENDENTES");
			tarefa.setIcTipoTarefa(1);

			Mtxtb012VersaoTarefaPK versaoTarefaPK = new Mtxtb012VersaoTarefaPK();
			versaoTarefaPK.setNuTarefa002(100045);
			versaoTarefaPK.setNuVersaoTarefa(1);

			Mtxtb012VersaoTarefa versaoTarefa = new Mtxtb012VersaoTarefa();
			versaoTarefa.setIcAssincrono(BigDecimal.ONE);
			versaoTarefa.setIcSituacao(BigDecimal.ONE);
			versaoTarefa.setVersaoBarramento("1");
			versaoTarefa.setDeXsltRequisicao("negocial/Lista_Transacoes_Pendentes_Assinatura/V1/Lista_Transacoes_Pendentes_Assinatura_Req.xsl");
			versaoTarefa.setDeXsltResposta("negocial/Lista_Transacoes_Pendentes_Assinatura/V1/Lista_Transacoes_Pendentes_Assinatura_Resp.xsl");
			versaoTarefa.setId(versaoTarefaPK);
			versaoTarefa.setMtxtb002Tarefa(tarefa);

			Mtxtb003ServicoTarefa servicoTarefa = new Mtxtb003ServicoTarefa();
			servicoTarefa.setIcImpedimento(BigDecimal.ONE);
			servicoTarefa.setId(servicoTarefaPK);
			servicoTarefa.setMtxtb012VersaoTarefa(versaoTarefa);
			lista.add(servicoTarefa);
			
			return lista;
		}
		else {
			return new ArrayList<>();
		}
	}
	
}