<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

	<xsl:output method="xml" omit-xml-declaration="yes" />
	<xsl:strip-space elements="*" />
	
	
	<!-- Gera o arquivo para enviar para o sistema corporativo -->
	<xsl:template match="/">
		<validasenha:SERVICO_ENTRADA 
			xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
			xmlns:sibar_base="http://caixa.gov.br/sibar"
			xmlns:validasenha="http://caixa.gov.br/sibar/valida_senha">
			<sibar_base:HEADER>
				<VERSAO>3.5</VERSAO>
				<OPERACAO>SENHA_CIDADAO</OPERACAO>
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
				<xsl:copy-of select="/BUSDATA/*[local-name()='REQUISICAO']/MODOENTRADA/VALIDA_SENHA/SENHA_CIDADAO"/>
			</DADOS>
		</validasenha:SERVICO_ENTRADA>
	</xsl:template>
</xsl:stylesheet>