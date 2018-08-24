<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	
	<xsl:output method="xml" omit-xml-declaration="yes" />
	<xsl:strip-space elements="*" />


	<!-- Gera o arquivo para enviar para o sistema corporativo -->
	<xsl:template match="/">
		<consulta_doc:SERVICO_ENTRADA 			
			xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
			xmlns:sibar_base="http://caixa.gov.br/sibar"
			xmlns:consulta_doc="http://caixa.gov.br/siiso/consulta_documento">
			<sibar_base:HEADER>
				<VERSAO>1.0</VERSAO>
				<OPERACAO>CONSULTA_DOCUMENTO</OPERACAO>
				<SISTEMA_ORIGEM>SIMTX</SISTEMA_ORIGEM>
				<DATA_HORA>
					<xsl:value-of select="BUSDATA/BUSTIME/ANO" />
					<xsl:value-of select="BUSDATA/BUSTIME/MES" />
					<xsl:value-of select="BUSDATA/BUSTIME/DIA" />
					<xsl:value-of select="BUSDATA/BUSTIME/HORA" />
					<xsl:value-of select="BUSDATA/BUSTIME/MIN" />
					<xsl:value-of select="BUSDATA/BUSTIME/SEG" />
				</DATA_HORA>
			</sibar_base:HEADER>
			<DADOS>
				<xsl:copy-of select="/BUSDATA/*[local-name()='REQUISICAO']/MODOENTRADA/CONSULTA_DOCUMENTO"/>
			</DADOS>
		</consulta_doc:SERVICO_ENTRADA>
	</xsl:template>
</xsl:stylesheet>