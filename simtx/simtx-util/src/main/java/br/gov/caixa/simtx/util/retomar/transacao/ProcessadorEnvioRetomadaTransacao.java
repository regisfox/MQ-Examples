package br.gov.caixa.simtx.util.retomar.transacao;

import java.io.IOException;

import javax.ejb.Asynchronous;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.apache.log4j.Logger;
import org.xml.sax.SAXException;

import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb011VersaoServico;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb014Transacao;
import br.gov.caixa.simtx.util.gerenciador.cache.RepositorioArquivo;
import br.gov.caixa.simtx.util.jms.ConexaoHQ;
import br.gov.caixa.simtx.util.servico.RetomadaTransacaoUtils;
import br.gov.caixa.simtx.util.xml.BuscadorTextoXml;
import br.gov.caixa.simtx.util.xml.DadosBarramento;
import br.gov.caixa.simtx.util.xml.ParametroXsl;
import br.gov.caixa.simtx.util.xml.TransformadorXsl;

@Stateless
public class ProcessadorEnvioRetomadaTransacao {

	private static final Logger logger = Logger.getLogger(ProcessadorEnvioRetomadaTransacao.class);

	@Inject
	private RepositorioArquivo repositorio;
	
	@Inject
	private ConexaoHQ conexaoHQ;

	@Asynchronous
	public void envioRetomadaTransacao(Mtxtb014Transacao transacaoCorrente, Mtxtb011VersaoServico servicoOrigem, DadosBarramento dadosBarramento, String xmlResposta) {
		
		logger.info("inicializando envioRetomadaTransacao");
		
		try {
			String xmlEntrada = obterXmlEntradaEnvio(transacaoCorrente, servicoOrigem, dadosBarramento, xmlResposta);
			if (null!=xmlEntrada && null!=conexaoHQ) {
				conexaoHQ.enviarMensagem(xmlEntrada);
			}
		} catch (Exception e) {
			logger.error("Não foi possível chamar serviço de Retomada de Transacao");
		}
		logger.info("finalizando envioRetomadaTransacao");
	}
	
	public String obterXmlEntradaEnvio(Mtxtb014Transacao transacaoCorrente, Mtxtb011VersaoServico servicoOrigem, DadosBarramento dadosBarramento, String xmlResposta) throws ParserConfigurationException, SAXException, IOException, TransformerException {
		String xmlEntrada = null;
		if (transacaoCorrente != null) {
			BuscadorTextoXml buscadorTextoXml = new BuscadorTextoXml(dadosBarramento.getDadosLeitura());
			String nuMeioEntrada = buscadorTextoXml.xpathTexto("BUSDATA/*[2]/HEADER/MEIOENTRADA");
			String siglaCanal = buscadorTextoXml.xpathTexto("BUSDATA/*[2]/HEADER/CANAL/SIGLA");
			String nsu = String.valueOf(transacaoCorrente.getNuNsuTransacao());
			String usuario = buscadorTextoXml.xpathTexto("BUSDATA/mtxCancelamentoAgendamentoWeb/codigoUsuario");
			String ipUsuarioOrigem = buscadorTextoXml.xpathTexto("BUSDATA/mtxCancelamentoAgendamentoWeb/codigoMaquina");
			String nsuAgendamento = buscadorTextoXml.xpathTexto("BUSDATA/mtxCancelamentoAgendamentoWeb/nuNsuTransacaoAgendamento");
			if (nsuAgendamento.isEmpty()) {
				nsuAgendamento = buscadorTextoXml.xpathTexto("/BUSDATA/*[2]/DETALHE_AGENDAMENTO_TRANSACAO/NSU");	
			}
			if (nsuAgendamento.isEmpty()) {
				nsuAgendamento = buscadorTextoXml.xpathTexto("/BUSDATA/NSU_AGENDAMENTO");	
			}
			
			String agendamendoFinal = buscadorTextoXml.xpathTexto("/BUSDATA/AGENDAMENTO_FINAL");
			
			xmlEntrada = new TransformadorXsl().transformar(dadosBarramento.getDadosLeitura(), RetomadaTransacaoUtils.getXslConteudo(this.repositorio),
					new ParametroXsl("meioEntrada", nuMeioEntrada),
					new ParametroXsl("siglaCanal", siglaCanal), 
					new ParametroXsl("nsu", nsu),
					new ParametroXsl("transacaoIcSituacao", String.valueOf(transacaoCorrente.getIcSituacao())),
					new ParametroXsl("nsuAgendamento", nsuAgendamento),
					new ParametroXsl("agendamentoFinal", agendamendoFinal),
					new ParametroXsl("servicoOrigem", String.valueOf(servicoOrigem.getId().getNuServico001())),
					new ParametroXsl("servicoVersaoOrigem", String.valueOf(servicoOrigem.getId().getNuVersaoServico())),
					new ParametroXsl("dadosRespostaTarefasSibar", TransformadorXsl.xmlRemoveTagCabecalho(xmlResposta)),
					new ParametroXsl("usuario", usuario == null ? "" : String.valueOf(usuario)),
					new ParametroXsl("ipUsuarioOrigem", ipUsuarioOrigem == null ? "" : String.valueOf(ipUsuarioOrigem)));

			logger.info("Enviando XML para Retomada de Transacao");
			logger.info(xmlEntrada);
		}
		
		return xmlEntrada;
	}

	public void setRepositorio(RepositorioArquivo repositorio) {
		this.repositorio = repositorio;
	}
	
	
}
