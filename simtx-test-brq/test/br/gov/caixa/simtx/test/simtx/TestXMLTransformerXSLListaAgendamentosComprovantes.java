package br.gov.caixa.simtx.test.simtx;

import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import br.gov.caixa.simtx.utils.BaseUtil;
import br.gov.caixa.simtx.xml.BuscadorTextoXml;
import br.gov.caixa.simtx.xml.TransformadorXsl;

public class TestXMLTransformerXSLListaAgendamentosComprovantes extends BaseUtil {

	private String fileName = "listaAgendamentosTransacoes_Resp.xsl";

	@Test
	public void test() {
		try {
			processarListaTransacoes();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void processarListaTransacoes() throws Exception {
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder;
		dBuilder = dbFactory.newDocumentBuilder();

		Document doc = dBuilder.newDocument();
		Element root = doc.createElement("TRANSACOES");
		doc.adoptNode(root);
		doc.appendChild(root);
		String xml = TransformadorXsl.recuperarArquivo(pathServicoMigradoFile);
		BuscadorTextoXml buscadorSaidaSibar = new BuscadorTextoXml(xml);

		Node nodeTransacoes = buscadorSaidaSibar.xpath("/*[1]/CONSULTA_TRANSACOES_CONTA_SAIDA/BOLETO/TRANSACOES");
		if (null != nodeTransacoes) {
			for (int i = 0; i < nodeTransacoes.getChildNodes().getLength(); i++) {
				Node node = nodeTransacoes.getChildNodes().item(i);
				if (node.getNodeType() == Node.ELEMENT_NODE) {
					String linha = node.getTextContent();
					if (linha.trim().contains("Agendado")) {
						montarTagTransacaoSiaut(node, doc, root, 1234L);
					}
				}
			}	
		}
		

		montarTagTransacaoSimtx(doc, root, 123L);

		StringBuffer xmlEstruturaBusData = new StringBuffer();
		xmlEstruturaBusData.append(TransformadorXsl.NAME_MIGRADO_BUSDATA_INI_XML);
		xmlEstruturaBusData.append(processamentoDoc(doc));
		xmlEstruturaBusData.append(TransformadorXsl.NAME_MIGRADO_BUSDATA_FIM_XML);
		System.out.println("--------- XML ENTRADA CANAL ----------");
		System.out.print(xmlEstruturaBusData);
		System.out.println("--------- XML ENTRADA CANAL ----------");

		TransformadorXsl.xslTransformer(xmlEstruturaBusData.toString(), getPathFile(true, diretorio.listFiles(), fileName));

	}

	private String processamentoDoc(Document doc) throws Exception {
		TransformerFactory tf = TransformerFactory.newInstance();
		Transformer transformer = tf.newTransformer();
		transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
		StringWriter writer = new StringWriter();
		transformer.transform(new DOMSource(doc), new StreamResult(writer));
		String output = writer.getBuffer().toString().replaceAll("\n|\r", "");
		return output;
	}

	private void montarTagTransacaoSiaut(Node nodeTransao, Document doc, Element root, Long codigoDetalhe) {
		try {

			Element transacaoTag = doc.createElement("TRANSACAO");

			Element sistemaOrigem = doc.createElement("SISTEMA_ORIGEM");
			Element nsu = doc.createElement("NSU");
			Element dataAgendamento = doc.createElement("DATA_AGENDAMENTO");
			Element dataEfetivacao = doc.createElement("DATA_EFETIVACAO");
			Element valor = doc.createElement("VALOR");
			Element identificador = doc.createElement("IDENTIFICADOR");
			Element identificadorCancelamento = doc.createElement("IDENTIFICADOR_CANCELAMENTO");
			Element cdDetalheLista = doc.createElement("CODIGO_SERVICO_DETALHE");

			sistemaOrigem.appendChild(doc.createTextNode("SIAUT"));
			nsu.appendChild(doc.createTextNode(BuscadorTextoXml.xpath("/TRANSACAO/NSU", nodeTransao)));
			dataAgendamento
					.appendChild(doc.createTextNode(BuscadorTextoXml.xpath("/TRANSACAO/DATA_PAGAMENTO", nodeTransao)));
			dataEfetivacao
					.appendChild(doc.createTextNode(BuscadorTextoXml.xpath("/TRANSACAO/DATA_EFETIVACAO", nodeTransao)));
			valor.appendChild(doc.createTextNode(BuscadorTextoXml.xpath("/TRANSACAO/VALOR", nodeTransao)));
			identificador
					.appendChild(doc.createTextNode(BuscadorTextoXml.xpath("/TRANSACAO/IDENTIFICADOR", nodeTransao)));
			identificadorCancelamento
					.appendChild(doc.createTextNode(BuscadorTextoXml.xpath("/TRANSACAO/SITUACAO", nodeTransao)));
			cdDetalheLista.appendChild(doc.createTextNode(codigoDetalhe.toString()));

			transacaoTag.appendChild(sistemaOrigem);
			transacaoTag.appendChild(nsu);
			transacaoTag.appendChild(dataAgendamento);
			transacaoTag.appendChild(dataEfetivacao);
			transacaoTag.appendChild(identificador);
			transacaoTag.appendChild(identificadorCancelamento);
			transacaoTag.appendChild(cdDetalheLista);

			root.appendChild(transacaoTag);

		} catch (Exception e) {
		}
	}

	private void montarTagTransacaoSimtx(Document doc, Element root, Long codigoDetalhe) {
		try {

			Element transacaoTag = doc.createElement("TRANSACAO");

			Element sistemaOrigem = doc.createElement("SISTEMA_ORIGEM");
			Element nsu = doc.createElement("NSU_SIMTX");
			Element dataAgendamento = doc.createElement("DATA_AGENDAMENTO");
			Element dataEfetivacao = doc.createElement("DATA_EFETIVACAO");
			Element valor = doc.createElement("VALOR");
			Element identificador = doc.createElement("IDENTIFICADOR");
			Element identificadorCancelamento = doc.createElement("IDENTIFICADOR_CANCELAMENTO");
			Element cdDetalheLista = doc.createElement("CODIGO_SERVICO_DETALHE");

			sistemaOrigem.appendChild(doc.createTextNode("SIMTX"));
			nsu.appendChild(doc.createTextNode(""));
			dataAgendamento.appendChild(doc.createTextNode(""));
			dataEfetivacao.appendChild(doc.createTextNode(""));
			valor.appendChild(doc.createTextNode(""));
			identificador.appendChild(doc.createTextNode(""));
			identificadorCancelamento.appendChild(doc.createTextNode(""));
			cdDetalheLista.appendChild(doc.createTextNode(""));

			transacaoTag.appendChild(sistemaOrigem);
			transacaoTag.appendChild(nsu);
			transacaoTag.appendChild(dataAgendamento);
			transacaoTag.appendChild(dataEfetivacao);
			transacaoTag.appendChild(identificador);
			transacaoTag.appendChild(identificadorCancelamento);
			transacaoTag.appendChild(cdDetalheLista);

			root.appendChild(transacaoTag);

		} catch (Exception e) {
		}
	}
}
