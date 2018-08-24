<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:consultacadastropositivo="http://caixa.gov.br/sibar/consulta_cadastro_positivo" 
	xmlns:sibar_base="http://caixa.gov.br/sibar">

	<!-- omit-xml-declaration=yes DEVE SER INCLUIDO -->

	<xsl:output method="xml" omit-xml-declaration="yes" />

	<xsl:template match="/*">	
		<RESPOSTA name="CONSULTA_CADASTRO_POSITIVO">
			<CONSULTA_CADASTRO_POSITIVO>
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
			</CONSULTA_CADASTRO_POSITIVO>
		</RESPOSTA>
	</xsl:template>
</xsl:stylesheet>