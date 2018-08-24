<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

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
	

	<xsl:template match="/BUSDATA">
		<ns:DETALHE_COMPROVANTE_TRANSACAO_ENTRADA 
			xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
			xmlns:h="http://caixa.gov.br/sicco/comuns/header"
			xmlns:ns="http://caixa.gov.br/sicco/detalhe_comprovante_transacao/v1/ns">
			<HEADER>
				<SERVICO>
					<CODIGO><xsl:value-of select="*[2]/*[local-name()='HEADER']/SERVICO/CODIGO"/></CODIGO>
					<VERSAO><xsl:value-of select="*[2]/*[local-name()='HEADER']/SERVICO/VERSAO"/></VERSAO>
				</SERVICO>
				<CANAL>
					<SIGLA><xsl:value-of select="*[2]/*[local-name()='HEADER']/CANAL/SIGLA"/></SIGLA>
				</CANAL>
				<DATAHORA>
					<xsl:value-of select="BUSTIME/ANO" />
					<xsl:value-of select="BUSTIME/MES" />
					<xsl:value-of select="BUSTIME/DIA" />
					<xsl:value-of select="BUSTIME/HORA" />
					<xsl:value-of select="BUSTIME/MIN" />
					<xsl:value-of select="BUSTIME/SEG" />
				</DATAHORA>
			</HEADER>
			<NSU_SIMTX><xsl:value-of select="*[2]/DETALHE_COMPROVANTE_TRANSACAO/NSU"/></NSU_SIMTX>
		</ns:DETALHE_COMPROVANTE_TRANSACAO_ENTRADA>
	</xsl:template>
</xsl:stylesheet>