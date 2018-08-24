<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

	<xsl:output method="xml" omit-xml-declaration="yes" />
	<xsl:strip-space elements="*" />
	
	
	<!-- Gera o arquivo para enviar para o sistema corporativo -->
	<xsl:template match="/">
		<autenticacaobiometrica:SERVICO_ENTRADA
			xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
			xmlns:sibar_base="http://caixa.gov.br/sibar"
			xmlns:autenticacaobiometrica="http://caixa.gov.br/sibar/autenticacao_biometrica">
			<sibar_base:HEADER>
				<VERSAO>1.1</VERSAO>
				<OPERACAO>AUTENTICACAO_BIOMETRIA</OPERACAO>
				<SISTEMA_ORIGEM>SIMTX</SISTEMA_ORIGEM>
				<NSUMTX>
					<xsl:value-of select="/BUSDATA/*[local-name()='REQUISICAO']/HEADER/NSUMTX" />
				</NSUMTX>
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
				<xsl:copy-of select="/BUSDATA/*[local-name()='REQUISICAO']/MODOENTRADA/AUTENTICACAO_BIOMETRIA"/>
			</DADOS>
		</autenticacaobiometrica:SERVICO_ENTRADA>
	</xsl:template>
</xsl:stylesheet>