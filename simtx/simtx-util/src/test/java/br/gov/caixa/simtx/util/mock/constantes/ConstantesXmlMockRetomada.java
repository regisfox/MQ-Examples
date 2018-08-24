package br.gov.caixa.simtx.util.mock.constantes;

public class ConstantesXmlMockRetomada {
	
	public static String XML_TRANSACAO =
			"<ns:RETOMAR_TRANSACAO_ENTRADA xmlns:comuns=\"http://caixa.gov.br/simtx/comuns/comuns\" xmlns:h=\"http://caixa.gov.br/simtx/comuns/header\" xmlns:ns=\"http://caixa.gov.br/simtx/retomadaTransacao/v1/ns\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://caixa.gov.br/simtx/retomadaTransacao/v1/ns retormarTransacao.xsd\">\r\n" + 
			"	<HEADER>\r\n" + 
			"		<SERVICO>\r\n" + 
			"			<CODIGO>110045</CODIGO>\r\n" + 
			"			<VERSAO>1</VERSAO>\r\n" + 
			"		</SERVICO>\r\n" + 
			"		<CANAL>\r\n" + 
			"			<SIGLA>SIMAA</SIGLA>\r\n" + 
			"			<DATAHORA>20180811200459</DATAHORA>\r\n" + 
			"		</CANAL>\r\n" + 
			"		<MEIOENTRADA>0</MEIOENTRADA>\r\n" + 
			"		<DATA_REFERENCIA>20180811</DATA_REFERENCIA>\r\n" + 
			"	</HEADER>\r\n" + 
			"	<NSU_TRANSACAO>226945</NSU_TRANSACAO>\r\n" + 
			"	<SITUACAO_TRANSACAO>1</SITUACAO_TRANSACAO>\r\n" + 
			"	<CODIGO_SERVICO>110031</CODIGO_SERVICO>\r\n" + 
			"	<VERSAO_SERVICO>1</VERSAO_SERVICO>\r\n" + 
			"	<DADOS_MENSAGEM_TRANSACAO_TAREFAS_RESPOSTA_SIBAR>\r\n" + 
			"		<multicanal:SERVICO_SAIDA xmlns:multicanal=\"http://caixa.gov.br/sibar/multicanal/orquestracao\" xmlns:sibar_base=\"http://caixa.gov.br/sibar\">\r\n" + 
			"			<sibar_base:HEADER>\r\n" + 
			"				<VERSAO>1.0</VERSAO>\r\n" + 
			"				<USUARIO_SERVICO>smtxsd01</USUARIO_SERVICO>\r\n" + 
			"				<OPERACAO>SERVICO_MIGRADO</OPERACAO>\r\n" + 
			"				<SISTEMA_ORIGEM>SIMAA</SISTEMA_ORIGEM>\r\n" + 
			"				<UNIDADE>664</UNIDADE>\r\n" + 
			"				<DATA_HORA>20180811200451</DATA_HORA>\r\n" + 
			"			</sibar_base:HEADER>\r\n" + 
			"			<COD_RETORNO>MC006</COD_RETORNO>\r\n" + 
			"			<ORIGEM_RETORNO>ROTINAS_USO_COMUM</ORIGEM_RETORNO>\r\n" + 
			"			<MSG_RETORNO>PASSO ROTINAS_USO_COMUM EXECUTADO COM ERRO DE NEGOCIO.</MSG_RETORNO>\r\n" + 
			"			<calculaboleto:ROTINAS_USO_COMUM_SAIDA xmlns:calculaboleto=\"http://caixa.gov.br/sibar/rotinas_uso_comum/calcula_boleto\">\r\n" + 
			"				<CONTROLE_NEGOCIAL>\r\n" + 
			"					<ORIGEM_RETORNO>BROKER-SPD9MAG3</ORIGEM_RETORNO>\r\n" + 
			"					<COD_RETORNO>X5</COD_RETORNO>\r\n" + 
			"					<MENSAGENS>\r\n" + 
			"						<RETORNO>(BK76) ERRO NA FORMATACAO DA MENSAGEM.</RETORNO>\r\n" + 
			"					</MENSAGENS>\r\n" + 
			"				</CONTROLE_NEGOCIAL>\r\n" + 
			"				<EXCECAO>EXCECAO NO BAR_ROTINAS_USO_COMUM_MQ.MQInput. DETALHES: ParserException(1) - Funcao: ImbRootParser::parseNextItem, Texto Excecao: Exception whilst parsing.ParserException(2) - Funcao: ImbXMLNSCParser::parseLastChild, Texto Excecao: XML Parsing Errors have occurred.ParserException(3) - Funcao: ImbXMLNSCDocHandler::handleParseErrors, Texto Excecao: A schema validation error has occurred while parsing the XML document, Texto de Insercao(1) - 5004, Texto de Insercao(2) - 2, Texto de Insercao(3) - 1, Texto de Insercao(4) - 527, Texto de Insercao(5) - cvc-complex-type.2.4.a: Expecting element with local name \"UNIDADE_PAGAMENTO\" but saw \"DATA_MOVIMENTO\"., Texto de Insercao(6) - /Root/XMLNSC/{http://caixa.gov.br/sibar/rotinas_uso_comum/calcula_boleto}:SERVICO_ENTRADA/DADOS.</EXCECAO>\r\n" + 
			"			</calculaboleto:ROTINAS_USO_COMUM_SAIDA>\r\n" + 
			"		</multicanal:SERVICO_SAIDA>\r\n" + 
			"	</DADOS_MENSAGEM_TRANSACAO_TAREFAS_RESPOSTA_SIBAR>\r\n" + 
			"</ns:RETOMAR_TRANSACAO_ENTRADA>";
	
	public static String XML_TRANSACAO_VALIDA_AGENDAMENTO =
			"<ns:RETOMAR_TRANSACAO_ENTRADA xmlns:comuns=\"http://caixa.gov.br/simtx/comuns/comuns\" xmlns:h=\"http://caixa.gov.br/simtx/comuns/header\" xmlns:ns=\"http://caixa.gov.br/simtx/retomadaTransacao/v1/ns\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://caixa.gov.br/simtx/retomadaTransacao/v1/ns retormarTransacao.xsd\">\r\n" + 
			"	<HEADER>\r\n" + 
			"		<SERVICO>\r\n" + 
			"			<CODIGO>110045</CODIGO>\r\n" + 
			"			<VERSAO>1</VERSAO>\r\n" + 
			"		</SERVICO>\r\n" + 
			"		<CANAL>\r\n" + 
			"			<SIGLA>SIMAA</SIGLA>\r\n" + 
			"			<DATAHORA>20180811200459</DATAHORA>\r\n" + 
			"		</CANAL>\r\n" + 
			"		<MEIOENTRADA>0</MEIOENTRADA>\r\n" + 
			"		<DATA_REFERENCIA>20180811</DATA_REFERENCIA>\r\n" + 
			"	</HEADER>\r\n" + 
			"	<NSU_TRANSACAO>225349</NSU_TRANSACAO>\r\n" + 
			"	<SITUACAO_TRANSACAO>1</SITUACAO_TRANSACAO>\r\n" + 
			"	<CODIGO_SERVICO>110031</CODIGO_SERVICO>\r\n" + 
			"	<VERSAO_SERVICO>1</VERSAO_SERVICO>\r\n" + 
			"	<DADOS_MENSAGEM_TRANSACAO_TAREFAS_RESPOSTA_SIBAR>\r\n" + 
			"		<multicanal:SERVICO_SAIDA xmlns:multicanal=\"http://caixa.gov.br/sibar/multicanal/orquestracao\" xmlns:sibar_base=\"http://caixa.gov.br/sibar\">\r\n" + 
			"			<sibar_base:HEADER>\r\n" + 
			"				<VERSAO>1.0</VERSAO>\r\n" + 
			"				<USUARIO_SERVICO>smtxsd01</USUARIO_SERVICO>\r\n" + 
			"				<OPERACAO>SERVICO_MIGRADO</OPERACAO>\r\n" + 
			"				<SISTEMA_ORIGEM>SIMAA</SISTEMA_ORIGEM>\r\n" + 
			"				<UNIDADE>664</UNIDADE>\r\n" + 
			"				<DATA_HORA>20180811200451</DATA_HORA>\r\n" + 
			"			</sibar_base:HEADER>\r\n" + 
			"			<COD_RETORNO>MC006</COD_RETORNO>\r\n" + 
			"			<ORIGEM_RETORNO>ROTINAS_USO_COMUM</ORIGEM_RETORNO>\r\n" + 
			"			<MSG_RETORNO>PASSO ROTINAS_USO_COMUM EXECUTADO COM ERRO DE NEGOCIO.</MSG_RETORNO>\r\n" + 
			"			<calculaboleto:ROTINAS_USO_COMUM_SAIDA xmlns:calculaboleto=\"http://caixa.gov.br/sibar/rotinas_uso_comum/calcula_boleto\">\r\n" + 
			"				<CONTROLE_NEGOCIAL>\r\n" + 
			"					<ORIGEM_RETORNO>BROKER-SPD9MAG3</ORIGEM_RETORNO>\r\n" + 
			"					<COD_RETORNO>X5</COD_RETORNO>\r\n" + 
			"					<MENSAGENS>\r\n" + 
			"						<RETORNO>(BK76) ERRO NA FORMATACAO DA MENSAGEM.</RETORNO>\r\n" + 
			"					</MENSAGENS>\r\n" + 
			"				</CONTROLE_NEGOCIAL>\r\n" + 
			"				<EXCECAO>EXCECAO NO BAR_ROTINAS_USO_COMUM_MQ.MQInput. DETALHES: ParserException(1) - Funcao: ImbRootParser::parseNextItem, Texto Excecao: Exception whilst parsing.ParserException(2) - Funcao: ImbXMLNSCParser::parseLastChild, Texto Excecao: XML Parsing Errors have occurred.ParserException(3) - Funcao: ImbXMLNSCDocHandler::handleParseErrors, Texto Excecao: A schema validation error has occurred while parsing the XML document, Texto de Insercao(1) - 5004, Texto de Insercao(2) - 2, Texto de Insercao(3) - 1, Texto de Insercao(4) - 527, Texto de Insercao(5) - cvc-complex-type.2.4.a: Expecting element with local name \"UNIDADE_PAGAMENTO\" but saw \"DATA_MOVIMENTO\"., Texto de Insercao(6) - /Root/XMLNSC/{http://caixa.gov.br/sibar/rotinas_uso_comum/calcula_boleto}:SERVICO_ENTRADA/DADOS.</EXCECAO>\r\n" + 
			"			</calculaboleto:ROTINAS_USO_COMUM_SAIDA>\r\n" + 
			"		</multicanal:SERVICO_SAIDA>\r\n" + 
			"	</DADOS_MENSAGEM_TRANSACAO_TAREFAS_RESPOSTA_SIBAR>\r\n" + 
			"</ns:RETOMAR_TRANSACAO_ENTRADA>";
	
	public static String XML_TRANSACAO_VALIDA_AGENDAMENTO_ERRO_ITERACAO_VAZIO =
			"<ns:RETOMAR_TRANSACAO_ENTRADA xmlns:comuns=\"http://caixa.gov.br/simtx/comuns/comuns\" xmlns:h=\"http://caixa.gov.br/simtx/comuns/header\" xmlns:ns=\"http://caixa.gov.br/simtx/retomadaTransacao/v1/ns\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://caixa.gov.br/simtx/retomadaTransacao/v1/ns retormarTransacao.xsd\">\r\n" + 
			"	<HEADER>\r\n" + 
			"		<SERVICO>\r\n" + 
			"			<CODIGO>110045</CODIGO>\r\n" + 
			"			<VERSAO>1</VERSAO>\r\n" + 
			"		</SERVICO>\r\n" + 
			"		<CANAL>\r\n" + 
			"			<SIGLA>SIMAA</SIGLA>\r\n" + 
			"			<DATAHORA>20180811200459</DATAHORA>\r\n" + 
			"		</CANAL>\r\n" + 
			"		<MEIOENTRADA>0</MEIOENTRADA>\r\n" + 
			"		<DATA_REFERENCIA>20180811</DATA_REFERENCIA>\r\n" + 
			"	</HEADER>\r\n" + 
			"	<NSU_TRANSACAO>225351</NSU_TRANSACAO>\r\n" + 
			"	<SITUACAO_TRANSACAO>1</SITUACAO_TRANSACAO>\r\n" + 
			"	<CODIGO_SERVICO>110031</CODIGO_SERVICO>\r\n" + 
			"	<VERSAO_SERVICO>1</VERSAO_SERVICO>\r\n" + 
			"	<DADOS_MENSAGEM_TRANSACAO_TAREFAS_RESPOSTA_SIBAR>\r\n" + 
			"		<multicanal:SERVICO_SAIDA xmlns:multicanal=\"http://caixa.gov.br/sibar/multicanal/orquestracao\" xmlns:sibar_base=\"http://caixa.gov.br/sibar\">\r\n" + 
			"			<sibar_base:HEADER>\r\n" + 
			"				<VERSAO>1.0</VERSAO>\r\n" + 
			"				<USUARIO_SERVICO>smtxsd01</USUARIO_SERVICO>\r\n" + 
			"				<OPERACAO>SERVICO_MIGRADO</OPERACAO>\r\n" + 
			"				<SISTEMA_ORIGEM>SIMAA</SISTEMA_ORIGEM>\r\n" + 
			"				<UNIDADE>664</UNIDADE>\r\n" + 
			"				<DATA_HORA>20180811200451</DATA_HORA>\r\n" + 
			"			</sibar_base:HEADER>\r\n" + 
			"			<COD_RETORNO>MC006</COD_RETORNO>\r\n" + 
			"			<ORIGEM_RETORNO>ROTINAS_USO_COMUM</ORIGEM_RETORNO>\r\n" + 
			"			<MSG_RETORNO>PASSO ROTINAS_USO_COMUM EXECUTADO COM ERRO DE NEGOCIO.</MSG_RETORNO>\r\n" + 
			"			<calculaboleto:ROTINAS_USO_COMUM_SAIDA xmlns:calculaboleto=\"http://caixa.gov.br/sibar/rotinas_uso_comum/calcula_boleto\">\r\n" + 
			"				<CONTROLE_NEGOCIAL>\r\n" + 
			"					<ORIGEM_RETORNO>BROKER-SPD9MAG3</ORIGEM_RETORNO>\r\n" + 
			"					<COD_RETORNO>X5</COD_RETORNO>\r\n" + 
			"					<MENSAGENS>\r\n" + 
			"						<RETORNO>(BK76) ERRO NA FORMATACAO DA MENSAGEM.</RETORNO>\r\n" + 
			"					</MENSAGENS>\r\n" + 
			"				</CONTROLE_NEGOCIAL>\r\n" + 
			"				<EXCECAO>EXCECAO NO BAR_ROTINAS_USO_COMUM_MQ.MQInput. DETALHES: ParserException(1) - Funcao: ImbRootParser::parseNextItem, Texto Excecao: Exception whilst parsing.ParserException(2) - Funcao: ImbXMLNSCParser::parseLastChild, Texto Excecao: XML Parsing Errors have occurred.ParserException(3) - Funcao: ImbXMLNSCDocHandler::handleParseErrors, Texto Excecao: A schema validation error has occurred while parsing the XML document, Texto de Insercao(1) - 5004, Texto de Insercao(2) - 2, Texto de Insercao(3) - 1, Texto de Insercao(4) - 527, Texto de Insercao(5) - cvc-complex-type.2.4.a: Expecting element with local name \"UNIDADE_PAGAMENTO\" but saw \"DATA_MOVIMENTO\"., Texto de Insercao(6) - /Root/XMLNSC/{http://caixa.gov.br/sibar/rotinas_uso_comum/calcula_boleto}:SERVICO_ENTRADA/DADOS.</EXCECAO>\r\n" + 
			"			</calculaboleto:ROTINAS_USO_COMUM_SAIDA>\r\n" + 
			"		</multicanal:SERVICO_SAIDA>\r\n" + 
			"	</DADOS_MENSAGEM_TRANSACAO_TAREFAS_RESPOSTA_SIBAR>\r\n" + 
			"</ns:RETOMAR_TRANSACAO_ENTRADA>";
	
	public static String XML_TRANSACAO_VALIDA_AGENDAMENTO_ERRO =
			"<ns:RETOMAR_TRANSACAO_ENTRADA xmlns:comuns=\"http://caixa.gov.br/simtx/comuns/comuns\" xmlns:h=\"http://caixa.gov.br/simtx/comuns/header\" xmlns:ns=\"http://caixa.gov.br/simtx/retomadaTransacao/v1/ns\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://caixa.gov.br/simtx/retomadaTransacao/v1/ns retormarTransacao.xsd\">\r\n" + 
			"	<HEADER>\r\n" + 
			"		<SERVICO>\r\n" + 
			"			<CODIGO>110045</CODIGO>\r\n" + 
			"			<VERSAO>1</VERSAO>\r\n" + 
			"		</SERVICO>\r\n" + 
			"		<CANAL>\r\n" + 
			"			<SIGLA>SIMAA</SIGLA>\r\n" + 
			"			<DATAHORA>20180811200459</DATAHORA>\r\n" + 
			"		</CANAL>\r\n" + 
			"		<MEIOENTRADA>0</MEIOENTRADA>\r\n" + 
			"		<DATA_REFERENCIA>20180811</DATA_REFERENCIA>\r\n" + 
			"	</HEADER>\r\n" + 
			"	<NSU_TRANSACAO>225350</NSU_TRANSACAO>\r\n" + 
			"	<SITUACAO_TRANSACAO>1</SITUACAO_TRANSACAO>\r\n" + 
			"	<CODIGO_SERVICO>110031</CODIGO_SERVICO>\r\n" + 
			"	<VERSAO_SERVICO>1</VERSAO_SERVICO>\r\n" + 
			"	<DADOS_MENSAGEM_TRANSACAO_TAREFAS_RESPOSTA_SIBAR>\r\n" + 
			"		<multicanal:SERVICO_SAIDA xmlns:multicanal=\"http://caixa.gov.br/sibar/multicanal/orquestracao\" xmlns:sibar_base=\"http://caixa.gov.br/sibar\">\r\n" + 
			"			<sibar_base:HEADER>\r\n" + 
			"				<VERSAO>1.0</VERSAO>\r\n" + 
			"				<USUARIO_SERVICO>smtxsd01</USUARIO_SERVICO>\r\n" + 
			"				<OPERACAO>SERVICO_MIGRADO</OPERACAO>\r\n" + 
			"				<SISTEMA_ORIGEM>SIMAA</SISTEMA_ORIGEM>\r\n" + 
			"				<UNIDADE>664</UNIDADE>\r\n" + 
			"				<DATA_HORA>20180811200451</DATA_HORA>\r\n" + 
			"			</sibar_base:HEADER>\r\n" + 
			"			<COD_RETORNO>MC006</COD_RETORNO>\r\n" + 
			"			<ORIGEM_RETORNO>ROTINAS_USO_COMUM</ORIGEM_RETORNO>\r\n" + 
			"			<MSG_RETORNO>PASSO ROTINAS_USO_COMUM EXECUTADO COM ERRO DE NEGOCIO.</MSG_RETORNO>\r\n" + 
			"			<calculaboleto:ROTINAS_USO_COMUM_SAIDA xmlns:calculaboleto=\"http://caixa.gov.br/sibar/rotinas_uso_comum/calcula_boleto\">\r\n" + 
			"				<CONTROLE_NEGOCIAL>\r\n" + 
			"					<ORIGEM_RETORNO>BROKER-SPD9MAG3</ORIGEM_RETORNO>\r\n" + 
			"					<COD_RETORNO>X5</COD_RETORNO>\r\n" + 
			"					<MENSAGENS>\r\n" + 
			"						<RETORNO>(BK76) ERRO NA FORMATACAO DA MENSAGEM.</RETORNO>\r\n" + 
			"					</MENSAGENS>\r\n" + 
			"				</CONTROLE_NEGOCIAL>\r\n" + 
			"				<EXCECAO>EXCECAO NO BAR_ROTINAS_USO_COMUM_MQ.MQInput. DETALHES: ParserException(1) - Funcao: ImbRootParser::parseNextItem, Texto Excecao: Exception whilst parsing.ParserException(2) - Funcao: ImbXMLNSCParser::parseLastChild, Texto Excecao: XML Parsing Errors have occurred.ParserException(3) - Funcao: ImbXMLNSCDocHandler::handleParseErrors, Texto Excecao: A schema validation error has occurred while parsing the XML document, Texto de Insercao(1) - 5004, Texto de Insercao(2) - 2, Texto de Insercao(3) - 1, Texto de Insercao(4) - 527, Texto de Insercao(5) - cvc-complex-type.2.4.a: Expecting element with local name \"UNIDADE_PAGAMENTO\" but saw \"DATA_MOVIMENTO\"., Texto de Insercao(6) - /Root/XMLNSC/{http://caixa.gov.br/sibar/rotinas_uso_comum/calcula_boleto}:SERVICO_ENTRADA/DADOS.</EXCECAO>\r\n" + 
			"			</calculaboleto:ROTINAS_USO_COMUM_SAIDA>\r\n" + 
			"		</multicanal:SERVICO_SAIDA>\r\n" + 
			"	</DADOS_MENSAGEM_TRANSACAO_TAREFAS_RESPOSTA_SIBAR>\r\n" + 
			"</ns:RETOMAR_TRANSACAO_ENTRADA>";
	
	public static String XML_TRANSACAO_SEM_CODIGO_SERVICO =
			"<ns:RETOMAR_TRANSACAO_ENTRADA xmlns:comuns=\"http://caixa.gov.br/simtx/comuns/comuns\" xmlns:h=\"http://caixa.gov.br/simtx/comuns/header\" xmlns:ns=\"http://caixa.gov.br/simtx/retomadaTransacao/v1/ns\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://caixa.gov.br/simtx/retomadaTransacao/v1/ns retormarTransacao.xsd\">\r\n" + 
			"	<HEADER>\r\n" + 
			"		<SERVICO>\r\n" + 
			"			<CODIGO>110045</CODIGO>\r\n" + 
			"			<VERSAO>1</VERSAO>\r\n" + 
			"		</SERVICO>\r\n" + 
			"		<CANAL>\r\n" + 
			"			<SIGLA>SIMAA</SIGLA>\r\n" + 
			"			<DATAHORA>20180811200459</DATAHORA>\r\n" + 
			"		</CANAL>\r\n" + 
			"		<MEIOENTRADA>0</MEIOENTRADA>\r\n" + 
			"		<DATA_REFERENCIA>20180811</DATA_REFERENCIA>\r\n" + 
			"	</HEADER>\r\n" + 
			"	<NSU_TRANSACAO>226945</NSU_TRANSACAO>\r\n" + 
			"	<SITUACAO_TRANSACAO>1</SITUACAO_TRANSACAO>\r\n" + 
			"	<CODIGO_SERVICO></CODIGO_SERVICO>\r\n" + 
			"	<VERSAO_SERVICO>1</VERSAO_SERVICO>\r\n" + 
			"	<DADOS_MENSAGEM_TRANSACAO_TAREFAS_RESPOSTA_SIBAR>\r\n" + 
			"		<multicanal:SERVICO_SAIDA xmlns:multicanal=\"http://caixa.gov.br/sibar/multicanal/orquestracao\" xmlns:sibar_base=\"http://caixa.gov.br/sibar\">\r\n" + 
			"			<sibar_base:HEADER>\r\n" + 
			"				<VERSAO>1.0</VERSAO>\r\n" + 
			"				<USUARIO_SERVICO>smtxsd01</USUARIO_SERVICO>\r\n" + 
			"				<OPERACAO>SERVICO_MIGRADO</OPERACAO>\r\n" + 
			"				<SISTEMA_ORIGEM>SIMAA</SISTEMA_ORIGEM>\r\n" + 
			"				<UNIDADE>664</UNIDADE>\r\n" + 
			"				<DATA_HORA>20180811200451</DATA_HORA>\r\n" + 
			"			</sibar_base:HEADER>\r\n" + 
			"			<COD_RETORNO>MC006</COD_RETORNO>\r\n" + 
			"			<ORIGEM_RETORNO>ROTINAS_USO_COMUM</ORIGEM_RETORNO>\r\n" + 
			"			<MSG_RETORNO>PASSO ROTINAS_USO_COMUM EXECUTADO COM ERRO DE NEGOCIO.</MSG_RETORNO>\r\n" + 
			"			<calculaboleto:ROTINAS_USO_COMUM_SAIDA xmlns:calculaboleto=\"http://caixa.gov.br/sibar/rotinas_uso_comum/calcula_boleto\">\r\n" + 
			"				<CONTROLE_NEGOCIAL>\r\n" + 
			"					<ORIGEM_RETORNO>BROKER-SPD9MAG3</ORIGEM_RETORNO>\r\n" + 
			"					<COD_RETORNO>X5</COD_RETORNO>\r\n" + 
			"					<MENSAGENS>\r\n" + 
			"						<RETORNO>(BK76) ERRO NA FORMATACAO DA MENSAGEM.</RETORNO>\r\n" + 
			"					</MENSAGENS>\r\n" + 
			"				</CONTROLE_NEGOCIAL>\r\n" + 
			"				<EXCECAO>EXCECAO NO BAR_ROTINAS_USO_COMUM_MQ.MQInput. DETALHES: ParserException(1) - Funcao: ImbRootParser::parseNextItem, Texto Excecao: Exception whilst parsing.ParserException(2) - Funcao: ImbXMLNSCParser::parseLastChild, Texto Excecao: XML Parsing Errors have occurred.ParserException(3) - Funcao: ImbXMLNSCDocHandler::handleParseErrors, Texto Excecao: A schema validation error has occurred while parsing the XML document, Texto de Insercao(1) - 5004, Texto de Insercao(2) - 2, Texto de Insercao(3) - 1, Texto de Insercao(4) - 527, Texto de Insercao(5) - cvc-complex-type.2.4.a: Expecting element with local name \"UNIDADE_PAGAMENTO\" but saw \"DATA_MOVIMENTO\"., Texto de Insercao(6) - /Root/XMLNSC/{http://caixa.gov.br/sibar/rotinas_uso_comum/calcula_boleto}:SERVICO_ENTRADA/DADOS.</EXCECAO>\r\n" + 
			"			</calculaboleto:ROTINAS_USO_COMUM_SAIDA>\r\n" + 
			"		</multicanal:SERVICO_SAIDA>\r\n" + 
			"	</DADOS_MENSAGEM_TRANSACAO_TAREFAS_RESPOSTA_SIBAR>\r\n" + 
			"</ns:RETOMAR_TRANSACAO_ENTRADA>";
	
	public static String XML_TRANSACAO_SEM_CODIGO_USUARIO =
			"<ns:RETOMAR_TRANSACAO_ENTRADA xmlns:comuns=\"http://caixa.gov.br/simtx/comuns/comuns\" xmlns:h=\"http://caixa.gov.br/simtx/comuns/header\" xmlns:ns=\"http://caixa.gov.br/simtx/retomadaTransacao/v1/ns\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://caixa.gov.br/simtx/retomadaTransacao/v1/ns retormarTransacao.xsd\">\r\n" + 
			"	<HEADER>\r\n" + 
			"		<SERVICO>\r\n" + 
			"			<CODIGO>110045</CODIGO>\r\n" + 
			"			<VERSAO>1</VERSAO>\r\n" + 
			"		</SERVICO>\r\n" + 
			"		<CANAL>\r\n" + 
			"			<SIGLA>SIMAA</SIGLA>\r\n" + 
			"			<DATAHORA>20180811200459</DATAHORA>\r\n" + 
			"		</CANAL>\r\n" + 
			"		<MEIOENTRADA>0</MEIOENTRADA>\r\n" + 
			"		<DATA_REFERENCIA>20180811</DATA_REFERENCIA>\r\n" + 
			"	</HEADER>\r\n" + 
			"	<NSU_TRANSACAO>226945</NSU_TRANSACAO>\r\n" + 
			"	<SITUACAO_TRANSACAO>1</SITUACAO_TRANSACAO>\r\n" + 
			"	<CODIGO_SERVICO>1</CODIGO_SERVICO>\r\n" + 
			"	<VERSAO_SERVICO>1</VERSAO_SERVICO>\r\n" + 
			"	<CODIGO_USUARIO>1</CODIGO_USUARIO>\r\n" + 
			"	<CODIGO_MAQUINA>1</CODIGO_MAQUINA>\r\n" + 
			"	<DADOS_MENSAGEM_TRANSACAO_TAREFAS_RESPOSTA_SIBAR>\r\n" + 
			"		<multicanal:SERVICO_SAIDA xmlns:multicanal=\"http://caixa.gov.br/sibar/multicanal/orquestracao\" xmlns:sibar_base=\"http://caixa.gov.br/sibar\">\r\n" + 
			"			<sibar_base:HEADER>\r\n" + 
			"				<VERSAO>1.0</VERSAO>\r\n" + 
			"				<USUARIO_SERVICO>smtxsd01</USUARIO_SERVICO>\r\n" + 
			"				<OPERACAO>SERVICO_MIGRADO</OPERACAO>\r\n" + 
			"				<SISTEMA_ORIGEM>SIMAA</SISTEMA_ORIGEM>\r\n" + 
			"				<UNIDADE>664</UNIDADE>\r\n" + 
			"				<DATA_HORA>20180811200451</DATA_HORA>\r\n" + 
			"			</sibar_base:HEADER>\r\n" + 
			"			<COD_RETORNO>MC006</COD_RETORNO>\r\n" + 
			"			<ORIGEM_RETORNO>ROTINAS_USO_COMUM</ORIGEM_RETORNO>\r\n" + 
			"			<MSG_RETORNO>PASSO ROTINAS_USO_COMUM EXECUTADO COM ERRO DE NEGOCIO.</MSG_RETORNO>\r\n" + 
			"			<calculaboleto:ROTINAS_USO_COMUM_SAIDA xmlns:calculaboleto=\"http://caixa.gov.br/sibar/rotinas_uso_comum/calcula_boleto\">\r\n" + 
			"				<CONTROLE_NEGOCIAL>\r\n" + 
			"					<ORIGEM_RETORNO>BROKER-SPD9MAG3</ORIGEM_RETORNO>\r\n" + 
			"					<COD_RETORNO>X5</COD_RETORNO>\r\n" + 
			"					<MENSAGENS>\r\n" + 
			"						<RETORNO>(BK76) ERRO NA FORMATACAO DA MENSAGEM.</RETORNO>\r\n" + 
			"					</MENSAGENS>\r\n" + 
			"				</CONTROLE_NEGOCIAL>\r\n" + 
			"				<EXCECAO>EXCECAO NO BAR_ROTINAS_USO_COMUM_MQ.MQInput. DETALHES: ParserException(1) - Funcao: ImbRootParser::parseNextItem, Texto Excecao: Exception whilst parsing.ParserException(2) - Funcao: ImbXMLNSCParser::parseLastChild, Texto Excecao: XML Parsing Errors have occurred.ParserException(3) - Funcao: ImbXMLNSCDocHandler::handleParseErrors, Texto Excecao: A schema validation error has occurred while parsing the XML document, Texto de Insercao(1) - 5004, Texto de Insercao(2) - 2, Texto de Insercao(3) - 1, Texto de Insercao(4) - 527, Texto de Insercao(5) - cvc-complex-type.2.4.a: Expecting element with local name \"UNIDADE_PAGAMENTO\" but saw \"DATA_MOVIMENTO\"., Texto de Insercao(6) - /Root/XMLNSC/{http://caixa.gov.br/sibar/rotinas_uso_comum/calcula_boleto}:SERVICO_ENTRADA/DADOS.</EXCECAO>\r\n" + 
			"			</calculaboleto:ROTINAS_USO_COMUM_SAIDA>\r\n" + 
			"		</multicanal:SERVICO_SAIDA>\r\n" + 
			"	</DADOS_MENSAGEM_TRANSACAO_TAREFAS_RESPOSTA_SIBAR>\r\n" + 
			"</ns:RETOMAR_TRANSACAO_ENTRADA>";
	
	public static String XML_TRANSACAO_AGENDAMENTO =
			"<ns:RETOMAR_TRANSACAO_ENTRADA xmlns:comuns=\"http://caixa.gov.br/simtx/comuns/comuns\" xmlns:h=\"http://caixa.gov.br/simtx/comuns/header\" xmlns:ns=\"http://caixa.gov.br/simtx/retomadaTransacao/v1/ns\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://caixa.gov.br/simtx/retomadaTransacao/v1/ns retormarTransacao.xsd\">\r\n" + 
			"	<HEADER>\r\n" + 
			"		<SERVICO>\r\n" + 
			"			<CODIGO>110045</CODIGO>\r\n" + 
			"			<VERSAO>1</VERSAO>\r\n" + 
			"		</SERVICO>\r\n" + 
			"		<CANAL>\r\n" + 
			"			<SIGLA>SIMAA</SIGLA>\r\n" + 
			"			<DATAHORA>20180811200459</DATAHORA>\r\n" + 
			"		</CANAL>\r\n" + 
			"		<MEIOENTRADA>0</MEIOENTRADA>\r\n" + 
			"		<DATA_REFERENCIA>20180811</DATA_REFERENCIA>\r\n" + 
			"	</HEADER>\r\n" + 
			"	<NSU_TRANSACAO>226945</NSU_TRANSACAO>\r\n" + 
			"	<SITUACAO_TRANSACAO>1</SITUACAO_TRANSACAO>\r\n" + 
			"	<CODIGO_SERVICO>1</CODIGO_SERVICO>\r\n" + 
			"	<VERSAO_SERVICO>1</VERSAO_SERVICO>\r\n" + 
			"	<CODIGO_USUARIO>1</CODIGO_USUARIO>\r\n" + 
			"	<CODIGO_MAQUINA>1</CODIGO_MAQUINA>\r\n" + 
			"	<NSUMTX_AGENDAMENTO>1</NSUMTX_AGENDAMENTO>\r\n" + 
			"	<DADOS_MENSAGEM_TRANSACAO_TAREFAS_RESPOSTA_SIBAR>\r\n" + 
			"		<multicanal:SERVICO_SAIDA xmlns:multicanal=\"http://caixa.gov.br/sibar/multicanal/orquestracao\" xmlns:sibar_base=\"http://caixa.gov.br/sibar\">\r\n" + 
			"			<sibar_base:HEADER>\r\n" + 
			"				<VERSAO>1.0</VERSAO>\r\n" + 
			"				<USUARIO_SERVICO>smtxsd01</USUARIO_SERVICO>\r\n" + 
			"				<OPERACAO>SERVICO_MIGRADO</OPERACAO>\r\n" + 
			"				<SISTEMA_ORIGEM>SIMAA</SISTEMA_ORIGEM>\r\n" + 
			"				<UNIDADE>664</UNIDADE>\r\n" + 
			"				<DATA_HORA>20180811200451</DATA_HORA>\r\n" + 
			"			</sibar_base:HEADER>\r\n" + 
			"			<COD_RETORNO>MC006</COD_RETORNO>\r\n" + 
			"			<ORIGEM_RETORNO>ROTINAS_USO_COMUM</ORIGEM_RETORNO>\r\n" + 
			"			<MSG_RETORNO>PASSO ROTINAS_USO_COMUM EXECUTADO COM ERRO DE NEGOCIO.</MSG_RETORNO>\r\n" + 
			"			<calculaboleto:ROTINAS_USO_COMUM_SAIDA xmlns:calculaboleto=\"http://caixa.gov.br/sibar/rotinas_uso_comum/calcula_boleto\">\r\n" + 
			"				<CONTROLE_NEGOCIAL>\r\n" + 
			"					<ORIGEM_RETORNO>BROKER-SPD9MAG3</ORIGEM_RETORNO>\r\n" + 
			"					<COD_RETORNO>X5</COD_RETORNO>\r\n" + 
			"					<MENSAGENS>\r\n" + 
			"						<RETORNO>(BK76) ERRO NA FORMATACAO DA MENSAGEM.</RETORNO>\r\n" + 
			"					</MENSAGENS>\r\n" + 
			"				</CONTROLE_NEGOCIAL>\r\n" + 
			"				<EXCECAO>EXCECAO NO BAR_ROTINAS_USO_COMUM_MQ.MQInput. DETALHES: ParserException(1) - Funcao: ImbRootParser::parseNextItem, Texto Excecao: Exception whilst parsing.ParserException(2) - Funcao: ImbXMLNSCParser::parseLastChild, Texto Excecao: XML Parsing Errors have occurred.ParserException(3) - Funcao: ImbXMLNSCDocHandler::handleParseErrors, Texto Excecao: A schema validation error has occurred while parsing the XML document, Texto de Insercao(1) - 5004, Texto de Insercao(2) - 2, Texto de Insercao(3) - 1, Texto de Insercao(4) - 527, Texto de Insercao(5) - cvc-complex-type.2.4.a: Expecting element with local name \"UNIDADE_PAGAMENTO\" but saw \"DATA_MOVIMENTO\"., Texto de Insercao(6) - /Root/XMLNSC/{http://caixa.gov.br/sibar/rotinas_uso_comum/calcula_boleto}:SERVICO_ENTRADA/DADOS.</EXCECAO>\r\n" + 
			"			</calculaboleto:ROTINAS_USO_COMUM_SAIDA>\r\n" + 
			"		</multicanal:SERVICO_SAIDA>\r\n" + 
			"	</DADOS_MENSAGEM_TRANSACAO_TAREFAS_RESPOSTA_SIBAR>\r\n" + 
			"</ns:RETOMAR_TRANSACAO_ENTRADA>";
	
	public static String XML_ERRO_PREENCHIMENTO_TRANSACAO = 
			"<ns:RETOMAR_TRANSACAO_ENTRADA xmlns:comuns=\"http://caixa.gov.br/simtx/comuns/comuns\" xmlns:h=\"http://caixa.gov.br/simtx/comuns/header\" xmlns:ns=\"http://caixa.gov.br/simtx/retomadaTransacao/v1/ns\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://caixa.gov.br/simtx/retomadaTransacao/v1/ns retormarTransacao.xsd\">\r\n" + 
			"	<HEADER>\r\n" + 
			"		<SERVICO>\r\n" + 
			"			<CODIGO>110045</CODIGO>\r\n" + 
			"			<VERSAO>1</VERSAO>\r\n" + 
			"		</SERVICO>\r\n" + 
			"		<CANAL>\r\n" + 
			"			<SIGLA>SIMAA</SIGLA>\r\n" + 
			"			<DATAHORA>20180811200459</DATAHORA>\r\n" + 
			"		</CANAL>\r\n" + 
			"		<MEIOENTRADA>0</MEIOENTRADA>\r\n" + 
			"		<DATA_REFERENCIA>20180811</DATA_REFERENCIA>\r\n" + 
			"	</HEADER>\r\n" + 
			"	<NSU_TRANSACAO>226946</NSU_TRANSACAO>\r\n" + 
			"	<SITUACAO_TRANSACAO>1</SITUACAO_TRANSACAO>\r\n" + 
			"	<CODIGO_SERVICO>110031</CODIGO_SERVICO>\r\n" + 
			"	<VERSAO_SERVICO>1</VERSAO_SERVICO>\r\n" + 
			"	<DADOS_MENSAGEM_TRANSACAO_TAREFAS_RESPOSTA_SIBAR>\r\n" + 
			"		<multicanal:SERVICO_SAIDA xmlns:multicanal=\"http://caixa.gov.br/sibar/multicanal/orquestracao\" xmlns:sibar_base=\"http://caixa.gov.br/sibar\">\r\n" + 
			"			<sibar_base:HEADER>\r\n" + 
			"				<VERSAO>1.0</VERSAO>\r\n" + 
			"				<USUARIO_SERVICO>smtxsd01</USUARIO_SERVICO>\r\n" + 
			"				<OPERACAO>SERVICO_MIGRADO</OPERACAO>\r\n" + 
			"				<SISTEMA_ORIGEM>SIMAA</SISTEMA_ORIGEM>\r\n" + 
			"				<UNIDADE>664</UNIDADE>\r\n" + 
			"				<DATA_HORA>20180811200451</DATA_HORA>\r\n" + 
			"			</sibar_base:HEADER>\r\n" + 
			"			<COD_RETORNO>MC006</COD_RETORNO>\r\n" + 
			"			<ORIGEM_RETORNO>ROTINAS_USO_COMUM</ORIGEM_RETORNO>\r\n" + 
			"			<MSG_RETORNO>PASSO ROTINAS_USO_COMUM EXECUTADO COM ERRO DE NEGOCIO.</MSG_RETORNO>\r\n" + 
			"			<calculaboleto:ROTINAS_USO_COMUM_SAIDA xmlns:calculaboleto=\"http://caixa.gov.br/sibar/rotinas_uso_comum/calcula_boleto\">\r\n" + 
			"				<CONTROLE_NEGOCIAL>\r\n" + 
			"					<ORIGEM_RETORNO>BROKER-SPD9MAG3</ORIGEM_RETORNO>\r\n" + 
			"					<COD_RETORNO>X5</COD_RETORNO>\r\n" + 
			"					<MENSAGENS>\r\n" + 
			"						<RETORNO>(BK76) ERRO NA FORMATACAO DA MENSAGEM.</RETORNO>\r\n" + 
			"					</MENSAGENS>\r\n" + 
			"				</CONTROLE_NEGOCIAL>\r\n" + 
			"				<EXCECAO>EXCECAO NO BAR_ROTINAS_USO_COMUM_MQ.MQInput. DETALHES: ParserException(1) - Funcao: ImbRootParser::parseNextItem, Texto Excecao: Exception whilst parsing.ParserException(2) - Funcao: ImbXMLNSCParser::parseLastChild, Texto Excecao: XML Parsing Errors have occurred.ParserException(3) - Funcao: ImbXMLNSCDocHandler::handleParseErrors, Texto Excecao: A schema validation error has occurred while parsing the XML document, Texto de Insercao(1) - 5004, Texto de Insercao(2) - 2, Texto de Insercao(3) - 1, Texto de Insercao(4) - 527, Texto de Insercao(5) - cvc-complex-type.2.4.a: Expecting element with local name \"UNIDADE_PAGAMENTO\" but saw \"DATA_MOVIMENTO\"., Texto de Insercao(6) - /Root/XMLNSC/{http://caixa.gov.br/sibar/rotinas_uso_comum/calcula_boleto}:SERVICO_ENTRADA/DADOS.</EXCECAO>\r\n" + 
			"			</calculaboleto:ROTINAS_USO_COMUM_SAIDA>\r\n" + 
			"		</multicanal:SERVICO_SAIDA>\r\n" + 
			"	</DADOS_MENSAGEM_TRANSACAO_TAREFAS_RESPOSTA_SIBARsadsad>\r\n" + 
			"</ns:RETOMAR_TRANSACAO_ENTRADA>";
	
	public static String XML_ERRO_TAG_PREENCHIMENTO_TRANSACAO = 
			"<ns:RETOMAR_TRANSACAO_ENTRADA xmlns:comuns=\"http://caixa.gov.br/simtx/comuns/comuns\" xmlns:h=\"http://caixa.gov.br/simtx/comuns/header\" xmlns:ns=\"http://caixa.gov.br/simtx/retomadaTransacao/v1/ns\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://caixa.gov.br/simtx/retomadaTransacao/v1/ns retormarTransacao.xsd\">\r\n" + 
			"	<HEADER>\r\n" + 
			"		<SERVICO>\r\n" + 
			"			<CODIGO>110045</CODIGO>\r\n" + 
			"			<VERSAO>1</VERSAO>\r\n" + 
			"		</SERVICO>\r\n" + 
			"		<CANAL>\r\n" + 
			"			<SIGLA>SIMAA</SIGLA>\r\n" + 
			"			<DATAHORA>20180811200459</DATAHORA>\r\n" + 
			"		</CANAL>\r\n" + 
			"		<MEIOENTRADA>0</MEIOENTRADA>\r\n" + 
			"		<DATA_REFERENCIA>20180811</DATA_REFERENCIA>\r\n" + 
			"	</HEADER>\r\n" + 
			"	<NSU_TRANSACAO>226946</NSU_TRANSACAO>\r\n" + 
			"	<SITUACAO_TRANSACAO>1</SITUACAO_TRANSACAO>\r\n" + 
			"	<CODIGO_SERVICO>110031</CODIGO_SERVICO>\r\n" + 
			"	<VERSAO_SERVICO>1</VERSAO_SERVICO>\r\n" + 
			"	<DADOS>\r\n" + 
			"		<multicanal:SERVICO_SAIDA xmlns:multicanal=\"http://caixa.gov.br/sibar/multicanal/orquestracao\" xmlns:sibar_base=\"http://caixa.gov.br/sibar\">\r\n" + 
			"			<sibar_base:HEADER>\r\n" + 
			"				<VERSAO>1.0</VERSAO>\r\n" + 
			"				<USUARIO_SERVICO>smtxsd01</USUARIO_SERVICO>\r\n" + 
			"				<OPERACAO>SERVICO_MIGRADO</OPERACAO>\r\n" + 
			"				<SISTEMA_ORIGEM>SIMAA</SISTEMA_ORIGEM>\r\n" + 
			"				<UNIDADE>664</UNIDADE>\r\n" + 
			"				<DATA_HORA>20180811200451</DATA_HORA>\r\n" + 
			"			</sibar_base:HEADER>\r\n" + 
			"			<COD_RETORNO>MC006</COD_RETORNO>\r\n" + 
			"			<ORIGEM_RETORNO>ROTINAS_USO_COMUM</ORIGEM_RETORNO>\r\n" + 
			"			<MSG_RETORNO>PASSO ROTINAS_USO_COMUM EXECUTADO COM ERRO DE NEGOCIO.</MSG_RETORNO>\r\n" + 
			"			<calculaboleto:ROTINAS_USO_COMUM_SAIDA xmlns:calculaboleto=\"http://caixa.gov.br/sibar/rotinas_uso_comum/calcula_boleto\">\r\n" + 
			"				<CONTROLE_NEGOCIAL>\r\n" + 
			"					<ORIGEM_RETORNO>BROKER-SPD9MAG3</ORIGEM_RETORNO>\r\n" + 
			"					<COD_RETORNO>X5</COD_RETORNO>\r\n" + 
			"					<MENSAGENS>\r\n" + 
			"						<RETORNO>(BK76) ERRO NA FORMATACAO DA MENSAGEM.</RETORNO>\r\n" + 
			"					</MENSAGENS>\r\n" + 
			"				</CONTROLE_NEGOCIAL>\r\n" + 
			"				<EXCECAO>EXCECAO NO BAR_ROTINAS_USO_COMUM_MQ.MQInput. DETALHES: ParserException(1) - Funcao: ImbRootParser::parseNextItem, Texto Excecao: Exception whilst parsing.ParserException(2) - Funcao: ImbXMLNSCParser::parseLastChild, Texto Excecao: XML Parsing Errors have occurred.ParserException(3) - Funcao: ImbXMLNSCDocHandler::handleParseErrors, Texto Excecao: A schema validation error has occurred while parsing the XML document, Texto de Insercao(1) - 5004, Texto de Insercao(2) - 2, Texto de Insercao(3) - 1, Texto de Insercao(4) - 527, Texto de Insercao(5) - cvc-complex-type.2.4.a: Expecting element with local name \"UNIDADE_PAGAMENTO\" but saw \"DATA_MOVIMENTO\"., Texto de Insercao(6) - /Root/XMLNSC/{http://caixa.gov.br/sibar/rotinas_uso_comum/calcula_boleto}:SERVICO_ENTRADA/DADOS.</EXCECAO>\r\n" + 
			"			</calculaboleto:ROTINAS_USO_COMUM_SAIDA>\r\n" + 
			"		</multicanal:SERVICO_SAIDA>\r\n" + 
			"	</DADOS>\r\n" + 
			"</ns:RETOMAR_TRANSACAO_ENTRADA>";
}

