package br.gov.caixa.simtx.agendamento.processador;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.cache.annotation.CacheKey;
import javax.xml.transform.TransformerException;

import br.gov.caixa.simtx.persistencia.cache.core.FornecedorDadosImpl;
import br.gov.caixa.simtx.persistencia.constante.Constantes;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb003ServicoTarefa;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb004Canal;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb008MeioEntrada;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb011VersaoServico;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb012VersaoTarefa;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb012VersaoTarefaPK;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb014Transacao;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb016IteracaoCanal;
import br.gov.caixa.simtx.util.exception.ServicoException;
import br.gov.caixa.simtx.util.gerenciador.GerenciadorTarefas;
import br.gov.caixa.simtx.util.gerenciador.ValidadorRegrasNegocio;
import br.gov.caixa.simtx.util.gerenciador.cache.RepositorioArquivo;
import br.gov.caixa.simtx.util.integrador.sicco.ProcessadorEnvioSicco;
import br.gov.caixa.simtx.util.xml.DadosBarramento;
import br.gov.caixa.simtx.util.xml.GeradorPassosMigrado;
import br.gov.caixa.simtx.util.xml.ParametroXsl;
import br.gov.caixa.simtx.util.xml.Resposta;
import br.gov.caixa.simtx.util.xml.TransformadorXsl;

public class ValidaBoletoAgendamentoTest {

}

class FornecedorDadosValidaBoletoMock extends FornecedorDadosImpl {

	private static final long serialVersionUID = 1L;

	@Override
	public Mtxtb012VersaoTarefa buscarVersaoTarefaPorPK(Mtxtb012VersaoTarefaPK versaoTarefaPK) {
		return new Mtxtb012VersaoTarefa();
	}
	
    public void alterarTransacao(Mtxtb014Transacao transacao) {
    }
	
    public void alterarIteracaoCanal(Mtxtb016IteracaoCanal iteracaoCanal) {}
}

class GerenciadorTarefasMock extends GerenciadorTarefas {
	private static final long serialVersionUID = 1L;
	
	@Override
	public List<Mtxtb003ServicoTarefa> carregarTarefasServico(Mtxtb011VersaoServico servico, Mtxtb004Canal canal,
			Mtxtb008MeioEntrada meioEntrada) throws ServicoException {
		return new ArrayList<>();
	}
	
	@Override
	public Resposta salvarTarefasServico(List<Mtxtb003ServicoTarefa> listaTarefas, Mtxtb011VersaoServico servico,
			Mtxtb014Transacao transacao, DadosBarramento dadosBarramento, String mensagemServicoSaida) throws ServicoException {
		return null;
	}
}

class RepositorioArquivoMock extends RepositorioArquivo {

	public String recuperarArquivo(@CacheKey String caminho) throws IOException {
		return "";
	}
	
	public void invalidarCache() {}
}

class TransformadorXslMock extends TransformadorXsl {
	private static final long serialVersionUID = 1L;

	public String transformar(String xml, String xsl, ParametroXsl... parametrosXls) throws TransformerException {
		return "";
	}
}

class GeradorPassosMigradoMock extends GeradorPassosMigrado {
	public String gerarPassos(String xml, Mtxtb011VersaoServico versaoServicoChamado,
			List<Mtxtb003ServicoTarefa> listaTarefaExecutar) throws ServicoException {
		return "";
	}
}

class ValidadorRegrasNegocioMock extends ValidadorRegrasNegocio {
	private static final long serialVersionUID = 1L;

	public BigDecimal situacaoFinalTransacao(Mtxtb011VersaoServico servico) {
		return Constantes.IC_SERVICO_FINALIZADO;
	}
}

class ProcessadorEnvioSiccoMock extends ProcessadorEnvioSicco {
	private static final long serialVersionUID = 1L;
	private boolean chamado = false;
	
	public void processarEnvioOnline(Mtxtb014Transacao transacao, String tipoEnvio) {
		this.chamado = true;
	}

	public boolean isChamado() {
		return chamado;
	}
		
}