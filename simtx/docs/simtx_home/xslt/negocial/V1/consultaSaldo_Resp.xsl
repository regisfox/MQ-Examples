<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:consultasaldo="http://caixa.gov.br/sibar/consulta_saldo" 
	xmlns:sibar_base="http://caixa.gov.br/sibar">

	<xsl:output method="xml" omit-xml-declaration="yes" />

	<xsl:template match="/*">	
		<RESPOSTA name="CONSULTA_SALDO">
			<CONSULTA_SALDO>
				<COD_RETORNO>
					<xsl:value-of select="/BUSDATA/*[local-name()='SERVICO_SAIDA']/COD_RETORNO"/>
				</COD_RETORNO>
				<ORIGEM_RETORNO>
					<xsl:value-of select="/BUSDATA/*[local-name()='SERVICO_SAIDA']/ORIGEM_RETORNO"/>
				</ORIGEM_RETORNO>
				<MSG_RETORNO>
					<xsl:value-of select="/BUSDATA/*[local-name()='SERVICO_SAIDA']/MSG_RETORNO"/>
				</MSG_RETORNO>
					<xsl:copy-of select="/BUSDATA/*[local-name()='SERVICO_SAIDA']/DADOS"/>
			</CONSULTA_SALDO>
		</RESPOSTA>
	</xsl:template>
</xsl:stylesheet>