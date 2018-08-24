<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" 
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://caixa.gov.br/simtx/retomadaTransacao/v1/ns retormarTransacao.xsd">
	
	<xsl:output omit-xml-declaration="yes"/>
	<xsl:strip-space elements="*" />
	
	<xsl:template match="*" mode="copy">
		<xsl:element name="{name()}" namespace="{namespace-uri()}">
			<xsl:apply-templates select="@*|node()" mode="copy" />
		</xsl:element>
	</xsl:template>

	<xsl:template match="@*|text()|comment()" mode="copy">
		<xsl:copy />
	</xsl:template>
	
	<xsl:param name="nsu" />
	<xsl:param name="transacaoIcSituacao" />
	<xsl:param name="nsuAgendamento" />
	<xsl:param name="servicoOrigem" />
	<xsl:param name="servicoVersaoOrigem" />
	<xsl:param name="agendamentoFinal" />
	<xsl:param name="siglaCanal" />
	<xsl:param name="meioEntrada" />
	<xsl:param name="usuario" />
	<xsl:param name="ipUsuarioOrigem" />
	<xsl:param name="dadosRespostaTarefasSibar" />
	
	<xsl:template match="/BUSDATA">
		<ns:RETOMAR_TRANSACAO_ENTRADA
				xmlns:comuns="http://caixa.gov.br/simtx/comuns/comuns"
				xmlns:h="http://caixa.gov.br/simtx/comuns/header" 
				xmlns:ns="http://caixa.gov.br/simtx/retomadaTransacao/v1/ns"
				xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
				xsi:schemaLocation="http://caixa.gov.br/simtx/retomadaTransacao/v1/ns retormarTransacao.xsd">
			<HEADER>
				<SERVICO>
					<CODIGO>110045</CODIGO>
					<VERSAO>1</VERSAO>
				</SERVICO>
				<CANAL>
					<SIGLA><xsl:value-of select="$siglaCanal"/></SIGLA>
					<DATAHORA>
						<xsl:value-of select="/BUSDATA/BUSTIME/ANO" />
						<xsl:value-of select="/BUSDATA/BUSTIME/MES" />
						<xsl:value-of select="/BUSDATA/BUSTIME/DIA" />
						<xsl:value-of select="/BUSDATA/BUSTIME/HORA" />
						<xsl:value-of select="/BUSDATA/BUSTIME/MIN" />
						<xsl:value-of select="/BUSDATA/BUSTIME/SEG" />
					</DATAHORA>
				</CANAL>
				<MEIOENTRADA><xsl:value-of select="$meioEntrada"/></MEIOENTRADA>
				<DATA_REFERENCIA>		
					<xsl:value-of select="/BUSDATA/BUSTIME/ANO" />
					<xsl:value-of select="/BUSDATA/BUSTIME/MES" />
					<xsl:value-of select="/BUSDATA/BUSTIME/DIA" />
				</DATA_REFERENCIA>
			</HEADER>
			<NSU_TRANSACAO><xsl:value-of select="$nsu" /></NSU_TRANSACAO>
			<SITUACAO_TRANSACAO><xsl:value-of select="$transacaoIcSituacao" /></SITUACAO_TRANSACAO>
			<NSUMTX_AGENDAMENTO><xsl:value-of select="$nsuAgendamento" /></NSUMTX_AGENDAMENTO>
			<AGENDAMENTO_FINAL><xsl:value-of select="$agendamentoFinal" /></AGENDAMENTO_FINAL>
			<CODIGO_SERVICO><xsl:value-of select="$servicoOrigem" /></CODIGO_SERVICO>
			<VERSAO_SERVICO><xsl:value-of select="$servicoVersaoOrigem" /></VERSAO_SERVICO>
			<CODIGO_USUARIO><xsl:value-of select="$usuario" /></CODIGO_USUARIO>
			<CODIGO_MAQUINA><xsl:value-of select="$ipUsuarioOrigem" /></CODIGO_MAQUINA>
			<DADOS_MENSAGEM_TRANSACAO_TAREFAS_RESPOSTA_SIBAR><xsl:value-of select="$dadosRespostaTarefasSibar" disable-output-escaping="yes" /></DADOS_MENSAGEM_TRANSACAO_TAREFAS_RESPOSTA_SIBAR>

		</ns:RETOMAR_TRANSACAO_ENTRADA>
	</xsl:template>
</xsl:stylesheet>