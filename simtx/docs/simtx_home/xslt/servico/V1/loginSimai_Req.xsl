<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" 
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:consultasaldo="http://caixa.gov.br/sibar/consulta_saldo"
	xmlns:sibar_base="http://caixa.gov.br/sibar" 
	xmlns:valida_cartao="http://caixa.gov.br/sibar/valida_cartao/comercial"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">

	<xsl:output omit-xml-declaration="yes"/>
 	<xsl:strip-space elements="*"/>

	<xsl:template match="/">
		<consultasaldo:SERVICO_ENTRADA>
			<sibar_base:HEADER>
				<xsl:copy-of select="/BUSDATA/*[local-name()='SERVICO_ENTRADA']/*[local-name()='HEADER']/*" disable-output-escaping="yes"/>
			</sibar_base:HEADER>
			<valida_cartao:VALIDA_CARTAO_ENTRADA>
				<xsl:copy-of select="/BUSDATA/*[local-name()='SERVICO_ENTRADA']/*[local-name()='VALIDA_CARTAO_ENTRADA']/*" disable-output-escaping="yes"/>
			</valida_cartao:VALIDA_CARTAO_ENTRADA>
			<DADOS>
				<xsl:copy-of select="/BUSDATA/*[local-name()='SERVICO_ENTRADA']/DADOS/*" disable-output-escaping="yes"/>
			</DADOS>
		</consultasaldo:SERVICO_ENTRADA>
	</xsl:template>
</xsl:stylesheet>