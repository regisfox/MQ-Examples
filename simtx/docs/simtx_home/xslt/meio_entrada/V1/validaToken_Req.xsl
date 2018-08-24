<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" 
xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

	<!-- Gera o arquivo para enviar para o sistema corporativo -->


	<xsl:template match="/">
		<validapermissao:SERVICO_ENTRADA
			xmlns:validapermissao="http://caixa.gov.br/sibar/validapermissao"
			xmlns:sibar_base="http://caixa.gov.br/sibar" 
			xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
			<sibar_base:HEADER>
				<VERSAO>2.0</VERSAO>
				<OPERACAO>VALIDA_PERMISSAO</OPERACAO>
				<SISTEMA_ORIGEM>SIMTX</SISTEMA_ORIGEM>
				<IDENTIFICADOR_ORIGEM>200.201.162.188</IDENTIFICADOR_ORIGEM>
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
				<xsl:copy-of select="/BUSDATA/*[local-name()='REQUISICAO']/MODOENTRADA/VALIDA_PERMISSAO/*"/>
			</DADOS>
		</validapermissao:SERVICO_ENTRADA>
	</xsl:template>
</xsl:stylesheet>