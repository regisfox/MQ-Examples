<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" 
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:validafuncionalidadesespeciaisimpeditivas="http://caixa.gov.br/sibar/consulta_conta_sid09/valida_funcionalidades_especiais_impeditivas">

	<!-- ESTE ARQUIVO FOI PROJETADO PARA GRAVAR DADOS NO BUSDATA DO PMAC -->
	<!-- omit-xml-declaration=yes DEVE SER INCLUIDO -->

	<xsl:output method="xml" omit-xml-declaration="yes" />

	<xsl:template match="/">
		<xsl:copy-of select="/BUSDATA/*[local-name()='SERVICO_SAIDA']/validafuncionalidadesespeciaisimpeditivas:CONSULTA_CONTA_SID09_SAIDA"/>
	</xsl:template>
</xsl:stylesheet>