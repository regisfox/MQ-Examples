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
		<multicanal:SERVICO_ENTRADA 
			xmlns:multicanal="http://caixa.gov.br/sibar/multicanal/orquestracao" 
			xmlns:sibar_base="http://caixa.gov.br/sibar" 
			xmlns:valida_senha="http://caixa.gov.br/sibar/valida_senha/senha_conta"  
			xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
			
			<xsl:apply-templates mode="copy" select="*[local-name()='SERVICO_ENTRADA']/*[local-name()='HEADER']" disable-output-escaping="no" />
			<xsl:apply-templates mode="copy" select="*[local-name()='SERVICO_ENTRADA']/*[local-name()='VALIDA_SENHA_ENTRADA']" disable-output-escaping="no" />
		</multicanal:SERVICO_ENTRADA>
	</xsl:template>
</xsl:stylesheet>