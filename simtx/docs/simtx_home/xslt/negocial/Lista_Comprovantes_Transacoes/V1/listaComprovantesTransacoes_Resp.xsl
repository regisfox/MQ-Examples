<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" 
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	>

	<!-- ESTE ARQUIVO FOI PROJETADO PARA GRAVAR DADOS NO BUSDATA DO PMAC -->
	<!-- omit-xml-declaration=yes DEVE SER INCLUIDO -->

	<xsl:output method="xml" omit-xml-declaration="yes" />

	<xsl:template match="/">
		<LISTA_COMPROVANTES_TRANSACOES_SAIDA>
			<CONTROLE_NEGOCIAL>
				<ORIGEM_RETORNO>SICCO</ORIGEM_RETORNO>
				<COD_RETORNO><xsl:value-of select="/BUSDATA/*[local-name()='LISTA_COMPROVANTES_TRANSACOES_SAIDA']/HEADER/COD_RETORNO"/></COD_RETORNO>
				<MENSAGENS>
					<RETORNO><xsl:value-of select="/BUSDATA/*[local-name()='LISTA_COMPROVANTES_TRANSACOES_SAIDA']/HEADER/MENSAGENS/RETORNO"/></RETORNO>
				</MENSAGENS>
			</CONTROLE_NEGOCIAL>
		</LISTA_COMPROVANTES_TRANSACOES_SAIDA>
	</xsl:template>
</xsl:stylesheet>