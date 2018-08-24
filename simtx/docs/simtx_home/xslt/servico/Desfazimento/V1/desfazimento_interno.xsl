<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" 
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://caixa.gov.br/simtx/desfazimento/v1/ns Desfazimento.xsd">
	
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
	
	<xsl:template match="/BUSDATA">
		<ns:DESFAZIMENTO_ENTRADA xmlns:comuns="http://caixa.gov.br/simtx/comuns/comuns"
				xmlns:h="http://caixa.gov.br/simtx/comuns/header" xmlns:ns="http://caixa.gov.br/simtx/desfazimento/v1/ns"
				xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
				xsi:schemaLocation="http://caixa.gov.br/simtx/desfazimento/v1/ns Desfazimento.xsd">
			<HEADER>
				<SERVICO>
					<CODIGO>110043</CODIGO>
					<VERSAO>1</VERSAO>
				</SERVICO>
				<CANAL>
					<SIGLA>SIMTX</SIGLA>
					<DATAHORA>
						<xsl:value-of select="/BUSDATA/BUSTIME/ANO" />
						<xsl:value-of select="/BUSDATA/BUSTIME/MES" />
						<xsl:value-of select="/BUSDATA/BUSTIME/DIA" />
						<xsl:value-of select="/BUSDATA/BUSTIME/HORA" />
						<xsl:value-of select="/BUSDATA/BUSTIME/MIN" />
						<xsl:value-of select="/BUSDATA/BUSTIME/SEG" />
					</DATAHORA>
				</CANAL>
				<MEIOENTRADA>0</MEIOENTRADA>
				<DATA_REFERENCIA>		
					<xsl:value-of select="/BUSDATA/BUSTIME/ANO" />
					<xsl:value-of select="/BUSDATA/BUSTIME/MES" />
					<xsl:value-of select="/BUSDATA/BUSTIME/DIA" />
				</DATA_REFERENCIA>
			</HEADER>
			<NSUMTX_ORIGEM><xsl:value-of select="$nsu" /></NSUMTX_ORIGEM>
		</ns:DESFAZIMENTO_ENTRADA>
	</xsl:template>
</xsl:stylesheet>