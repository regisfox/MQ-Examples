<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:consultaassinaturaeletronica="http://caixa.gov.br/sibar/consulta_assinatura_eletronica/lista_transacoes_pendentes" 
	xmlns:sibar_base="http://caixa.gov.br/sibar">

	<xsl:output method="xml" omit-xml-declaration="yes" />
	<xsl:strip-space elements="*" />

	<xsl:template match="/BUSDATA">
		<consultaassinaturaeletronica:SERVICO_ENTRADA 
			xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
			xmlns:consultaassinaturaeletronica="http://caixa.gov.br/sibar/consulta_assinatura_eletronica/lista_transacoes_pendentes" 
				xmlns:sibar_base="http://caixa.gov.br/sibar">
			<sibar_base:HEADER>
				<VERSAO>
					<xsl:value-of select="*[local-name()='SERVICO_ENTRADA']/*[local-name()='HEADER']/VERSAO"/>
				</VERSAO>
				<OPERACAO>LISTA_TRANSACOES_PENDENTES</OPERACAO>
				<SISTEMA_ORIGEM>
					<xsl:value-of select="SERVICO_ENTRADA/HEADER/SISTEMA_ORIGEM"/>
				</SISTEMA_ORIGEM>
				<DATA_HORA>
					<xsl:value-of select="BUSTIME/ANO" />
					<xsl:value-of select="BUSTIME/MES" />
					<xsl:value-of select="BUSTIME/DIA" />
					<xsl:value-of select="BUSTIME/HORA" />
					<xsl:value-of select="BUSTIME/MIN" />
					<xsl:value-of select="BUSTIME/SEG" />
				</DATA_HORA>
			</sibar_base:HEADER>
			<xsl:copy-of select="*[local-name()='SERVICO_ENTRADA']/DADOS"/>
		</consultaassinaturaeletronica:SERVICO_ENTRADA>
	</xsl:template>
</xsl:stylesheet>