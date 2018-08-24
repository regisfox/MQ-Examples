<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	
	<xsl:output method="xml" omit-xml-declaration="yes" />
	<xsl:strip-space elements="*" />


	<!-- Gera o arquivo para enviar para o sistema corporativo -->
	<xsl:template match="/">
		<valida_cartao:SERVICO_ENTRADA 
			xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
			xmlns:sibar_base="http://caixa.gov.br/sibar" 
			xmlns:valida_cartao="http://caixa.gov.br/sibar/valida_cartao">
			<sibar_base:HEADER>
				<VERSAO>1.3</VERSAO>
				<OPERACAO>CARTAO_COMERCIAL</OPERACAO>
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
				<xsl:copy-of select="/BUSDATA/*[local-name()='REQUISICAO']/MODOENTRADA/VALIDA_CARTAO/CARTAO_COMERCIAL"/>
			</DADOS>
		</valida_cartao:SERVICO_ENTRADA>
	</xsl:template>
</xsl:stylesheet>