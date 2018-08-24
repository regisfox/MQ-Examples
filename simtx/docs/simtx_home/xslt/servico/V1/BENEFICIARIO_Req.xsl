<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:consultacobrancabancaria="http://caixa.gov.br/sibar/consulta_cobranca_bancaria/beneficiario"
	xmlns:validapermissao="http://caixa.gov.br/sibar/validapermissao" 
	xmlns:sibar_base="http://caixa.gov.br/sibar">


	<xsl:output omit-xml-declaration="yes"/>
	<xsl:strip-space elements="*" />

	<xsl:template match="node()|@*">
		<xsl:copy>
			<xsl:apply-templates select="node()|@*" />
		</xsl:copy>
	</xsl:template>
	
	<xsl:template match="/">
		<consultacobrancabancaria:SERVICO_ENTRADA>
			<sibar_base:HEADER>
				<xsl:copy-of select="/BUSDATA/*[local-name()='SERVICO_ENTRADA']/sibar_base:HEADER/*"/>
			</sibar_base:HEADER>
			<validapermissao:VALIDA_PERMISSAO_ENTRADA>
				<xsl:copy-of select="/BUSDATA/*[local-name()='SERVICO_ENTRADA']/validapermissao:VALIDA_PERMISSAO_ENTRADA/*"/>
			</validapermissao:VALIDA_PERMISSAO_ENTRADA>
			<DADOS>
				<xsl:copy-of select="/BUSDATA/*[local-name()='SERVICO_ENTRADA']/DADOS/*"/>
			</DADOS>
		</consultacobrancabancaria:SERVICO_ENTRADA>
	</xsl:template>
</xsl:stylesheet>