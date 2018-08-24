package br.gov.caixa.simtx.util.to;

import java.util.List;

import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb003ServicoTarefa;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb004Canal;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb008MeioEntrada;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb011VersaoServico;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb014Transacao;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb015SrvcoTrnsoTrfa;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb016IteracaoCanal;
import br.gov.caixa.simtx.util.ParametrosAdicionais;

public class DadosTransacaoComuns {

	private List<Mtxtb003ServicoTarefa> mtxtb003ServicoTarefas;

	private Mtxtb004Canal mtxtb004Canal;

	private Mtxtb008MeioEntrada mtxtb008MeioEntrada;

	private Mtxtb011VersaoServico mtxtb011VersaoServico;

	private Mtxtb014Transacao mtxtb014Transacao;

	private List<Mtxtb015SrvcoTrnsoTrfa> mtxtb015SrvcoTrnsoTrfas;

	private Mtxtb016IteracaoCanal mtxtb016IteracaoCanal;
	
	private ParametrosAdicionais parametrosAdicionais;
	

	public DadosTransacaoComuns(Mtxtb004Canal mtxtb004Canal, Mtxtb008MeioEntrada mtxtb008MeioEntrada, Mtxtb011VersaoServico mtxtb011VersaoServico,
			Mtxtb014Transacao mtxtb014Transacao, Mtxtb016IteracaoCanal mtxtb016IteracaoCanal) {
		super();
		this.mtxtb004Canal = mtxtb004Canal;
		this.mtxtb008MeioEntrada = mtxtb008MeioEntrada;
		this.mtxtb011VersaoServico = mtxtb011VersaoServico;
		this.mtxtb014Transacao = mtxtb014Transacao;
		this.mtxtb016IteracaoCanal = mtxtb016IteracaoCanal;
	}

	public DadosTransacaoComuns(Mtxtb011VersaoServico mtxtb011VersaoServico, Mtxtb014Transacao mtxtb014Transacao, Mtxtb016IteracaoCanal mtxtb016IteracaoCanal) {
		super();
		this.mtxtb011VersaoServico = mtxtb011VersaoServico;
		this.mtxtb014Transacao = mtxtb014Transacao;
		this.mtxtb016IteracaoCanal = mtxtb016IteracaoCanal;
	}

	public DadosTransacaoComuns(Mtxtb011VersaoServico mtxtb011VersaoServico, Mtxtb014Transacao mtxtb014Transacao,
			List<Mtxtb015SrvcoTrnsoTrfa> mtxtb015SrvcoTrnsoTrfas, Mtxtb016IteracaoCanal mtxtb016IteracaoCanal) {
		super();
		this.mtxtb011VersaoServico = mtxtb011VersaoServico;
		this.mtxtb014Transacao = mtxtb014Transacao;
		this.mtxtb015SrvcoTrnsoTrfas = mtxtb015SrvcoTrnsoTrfas;
		this.mtxtb016IteracaoCanal = mtxtb016IteracaoCanal;
	}

	public DadosTransacaoComuns(List<Mtxtb003ServicoTarefa> mtxtb003ServicoTarefas, Mtxtb011VersaoServico mtxtb011VersaoServico,
			Mtxtb014Transacao mtxtb014Transacao, List<Mtxtb015SrvcoTrnsoTrfa> mtxtb015SrvcoTrnsoTrfas, Mtxtb016IteracaoCanal mtxtb016IteracaoCanal) {
		super();
		this.mtxtb011VersaoServico = mtxtb011VersaoServico;
		this.mtxtb014Transacao = mtxtb014Transacao;
		this.mtxtb015SrvcoTrnsoTrfas = mtxtb015SrvcoTrnsoTrfas;
		this.mtxtb016IteracaoCanal = mtxtb016IteracaoCanal;
		this.mtxtb003ServicoTarefas = mtxtb003ServicoTarefas;
	}
	
	public DadosTransacaoComuns(Mtxtb014Transacao mtxtb014Transacao, Mtxtb016IteracaoCanal mtxtb016IteracaoCanal,
			Mtxtb004Canal mtxtb004Canal, Mtxtb011VersaoServico mtxtb011VersaoServico,
			Mtxtb008MeioEntrada mtxtb008MeioEntrada, ParametrosAdicionais parametrosAdicionais) {
		super();
		this.mtxtb004Canal = mtxtb004Canal;
		this.mtxtb008MeioEntrada = mtxtb008MeioEntrada;
		this.mtxtb011VersaoServico = mtxtb011VersaoServico;
		this.mtxtb014Transacao = mtxtb014Transacao;
		this.mtxtb016IteracaoCanal = mtxtb016IteracaoCanal;
		this.parametrosAdicionais = parametrosAdicionais;
	}
	

	public List<Mtxtb003ServicoTarefa> getMtxtb003ServicoTarefas() {
		return mtxtb003ServicoTarefas;
	}

	public Mtxtb004Canal getMtxtb004Canal() {
		return mtxtb004Canal;
	}

	public Mtxtb008MeioEntrada getMtxtb008MeioEntrada() {
		return mtxtb008MeioEntrada;
	}

	public Mtxtb011VersaoServico getMtxtb011VersaoServico() {
		return mtxtb011VersaoServico;
	}

	public Mtxtb014Transacao getMtxtb014Transacao() {
		return mtxtb014Transacao;
	}

	public List<Mtxtb015SrvcoTrnsoTrfa> getMtxtb015SrvcoTrnsoTrfas() {
		return mtxtb015SrvcoTrnsoTrfas;
	}

	public Mtxtb016IteracaoCanal getMtxtb016IteracaoCanal() {
		return mtxtb016IteracaoCanal;
	}

	public ParametrosAdicionais getParametrosAdicionais() {
		return parametrosAdicionais;
	}
	
}
