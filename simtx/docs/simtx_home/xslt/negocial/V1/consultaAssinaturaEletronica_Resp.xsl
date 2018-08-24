<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:consultaassinaturaeletronica="http://caixa.gov.br/sibar/consulta_assinatura_eletronica/lista_transacoes_pendentes" 
	xmlns:sibar_base="http://caixa.gov.br/sibar">

	<xsl:output method="xml" omit-xml-declaration="yes" indent="yes" />
	
	<xsl:template match="/">
		<RESPOSTA name="CONSULTA_ASSINATURA_ELETRONICA">
			<CONSULTA_ASSINATURA_ELETRONICA>
				<xsl:apply-templates />
			</CONSULTA_ASSINATURA_ELETRONICA>
		</RESPOSTA>
	</xsl:template>

	<xsl:template match="consultaassinaturaeletronica:SERVICO_SAIDA">
		<xsl:copy-of select="COD_RETORNO" />
		<xsl:copy-of select="ORIGEM_RETORNO" />
		<xsl:copy-of select="MSG_RETORNO" />
		<xsl:copy-of select="DADOS" />
		<TRANSACOES>
			<xsl:apply-templates select="DADOS/TRANSACOES" />
		</TRANSACOES>
	</xsl:template>
	
	<xsl:template match="TRANSACOES">
		<TRANSACOES_SIAUT>
			 <xsl:for-each select="TRANSACAO/ORIGEM[SISTEMA='SIAUT']">
			 	<xsl:copy-of select="./../." />
			 </xsl:for-each>
		</TRANSACOES_SIAUT>
		
		<TRANSACOES_SIMTX>
			 <xsl:for-each select="TRANSACAO/ORIGEM[SISTEMA='SIMTX']">
			 	<xsl:copy-of select="./../." />
			 </xsl:for-each>
		</TRANSACOES_SIMTX>
	</xsl:template>
	
	
</xsl:stylesheet>