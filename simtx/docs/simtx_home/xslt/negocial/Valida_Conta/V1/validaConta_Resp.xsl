<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" 
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:validaconta="http://caixa.gov.br/sibar/consulta_conta_sid09">

	<!-- ESTE ARQUIVO FOI PROJETADO PARA GRAVAR DADOS NO BUSDATA DO PMAC -->
	<!-- omit-xml-declaration=yes DEVE SER INCLUIDO -->

	<xsl:output method="xml" omit-xml-declaration="yes" />

	<xsl:template match="*" mode="copy">
		<xsl:element name="{name()}" namespace="{namespace-uri()}">
			<xsl:apply-templates select="@*|node()" mode="copy" />
		</xsl:element>
	</xsl:template>

	<xsl:template match="@*|text()|comment()" mode="copy">
		<xsl:copy />
	</xsl:template>
	
	<xsl:template match="/BUSDATA">
		<xsl:apply-templates mode="copy" select="*[local-name()='SERVICO_SAIDA']/*[name() = 'validaconta:CONSULTA_CONTA_SID09_SAIDA']" disable-output-escaping="yes" />
	</xsl:template>
</xsl:stylesheet>