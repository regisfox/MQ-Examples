<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" 
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:validapermissao="http://caixa.gov.br/sibar/validapermissao" 
	xmlns:sibar_base="http://caixa.gov.br/sibar">

	<!-- omit-xml-declaration=yes DEVE SER INCLUIDO -->

	<xsl:output method="xml" omit-xml-declaration="yes" />

	<xsl:template match="/*">	
		<RESPOSTA name="VALIDA_PERMISSAO">
			<VALIDA_PERMISSAO>
				<COD_RETORNO>
					<xsl:value-of select="/BUSDATA/*[local-name()='SERVICO_SAIDA']/COD_RETORNO"/>
				</COD_RETORNO>
				<ORIGEM_RETORNO>
					<xsl:value-of select="/BUSDATA/*[local-name()='SERVICO_SAIDA']/ORIGEM_RETORNO"/>
				</ORIGEM_RETORNO>
				<MSG_RETORNO>
					<xsl:value-of select="/BUSDATA/*[local-name()='SERVICO_SAIDA']/MSG_RETORNO"/>
				</MSG_RETORNO>
				<DADOS>
					<xsl:copy-of select="/BUSDATA/*[local-name()='SERVICO_SAIDA']/validapermissao:VALIDA_PERMISSAO_SAIDA/*"/>
				</DADOS>
			</VALIDA_PERMISSAO>
		</RESPOSTA>
	</xsl:template>
</xsl:stylesheet>