<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" 
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	>

	<!-- ESTE ARQUIVO FOI PROJETADO PARA GRAVAR DADOS NO BUSDATA DO PMAC -->
	<!-- omit-xml-declaration=yes DEVE SER INCLUIDO -->

	<xsl:output method="xml" omit-xml-declaration="yes" />


	<xsl:template match="/*">
		<RESPOSTA name = "VALIDA_SENHA">
			<VALIDA_SENHA>
				<COD_RETORNO>
					<xsl:value-of select="./COD_RETORNO"/>
				</COD_RETORNO>
				<ORIGEM_RETORNO>
					<xsl:value-of select="./ORIGEM_RETORNO"/>
				</ORIGEM_RETORNO>
				<MSG_RETORNO>
					<xsl:value-of select="./MSG_RETORNO"/>
				</MSG_RETORNO>
				<xsl:copy-of select="./DADOS"/>
			</VALIDA_SENHA>
		</RESPOSTA>
	</xsl:template>
</xsl:stylesheet>