<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:consultacadastropositivo="http://caixa.gov.br/sibar/consulta_cadastro_positivo" 
	xmlns:sibar_base="http://caixa.gov.br/sibar">

	<xsl:output method="xml" omit-xml-declaration="yes" />

	<xsl:template match="/">
		<consultacadastropositivo:SERVICO_ENTRADA 
			xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
			xmlns:consultacadastropositivo="http://caixa.gov.br/sibar/consulta_cadastro_positivo" 
			xmlns:sibar_base="http://caixa.gov.br/sibar">
			<sibar_base:HEADER>
				<VERSAO>
					<xsl:value-of select="/BUSDATA/*[local-name()='SERVICO_ENTRADA']/sibar_base:HEADER/VERSAO"/>
				</VERSAO>
				<OPERACAO>
					<xsl:value-of select="/BUSDATA/*[local-name()='SERVICO_ENTRADA']/sibar_base:HEADER/OPERACAO"/>
				</OPERACAO>
				<SISTEMA_ORIGEM>
					<xsl:value-of select="/BUSDATA/*[local-name()='SERVICO_ENTRADA']/sibar_base:HEADER/SISTEMA_ORIGEM"/>
				</SISTEMA_ORIGEM>
				<DATA_HORA>
					<xsl:value-of select="BUSDATA/BUSTIME/ANO" />
					<xsl:value-of select="BUSDATA/BUSTIME/MES" />
					<xsl:value-of select="BUSDATA/BUSTIME/DIA" />
					<xsl:value-of select="BUSDATA/BUSTIME/HORA" />
					<xsl:value-of select="BUSDATA/BUSTIME/MIN" />
					<xsl:value-of select="BUSDATA/BUSTIME/SEG" />
				</DATA_HORA>
			</sibar_base:HEADER>
			<xsl:copy-of select="/BUSDATA/*[local-name()='SERVICO_ENTRADA']/DADOS"/>
		</consultacadastropositivo:SERVICO_ENTRADA>
	</xsl:template>
</xsl:stylesheet>