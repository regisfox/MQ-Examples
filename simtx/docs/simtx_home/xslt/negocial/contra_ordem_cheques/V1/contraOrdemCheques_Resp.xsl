<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	version="2.0">
	<!-- ESTE ARQUIVO FOI PROJETADO PARA GRAVAR DADOS NO BUSDATA DO PMAC -->
	<!-- omit-xml-declaration=yes DEVE SER INCLUIDO -->
	<xsl:output omit-xml-declaration="yes" method="xml" />
	<xsl:template match="/">
		<xsl:copy-of select="/BUSDATA/*[local-name()='SERVICO_SAIDA']/*[local-name()='MANUTENCAO_CHEQUE_SAIDA']" />
	</xsl:template>
</xsl:stylesheet>