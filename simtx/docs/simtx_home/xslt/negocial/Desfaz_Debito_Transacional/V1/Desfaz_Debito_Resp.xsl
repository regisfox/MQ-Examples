<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" 
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

	<xsl:output method="xml" omit-xml-declaration="yes" />

	<xsl:template match="/">
		<xsl:copy-of select="/BUSDATA/*[local-name()='SERVICO_SAIDA']/*[local-name()='SOLICITACAO_DEBITO_SAIDA']"/>
	</xsl:template>
</xsl:stylesheet>