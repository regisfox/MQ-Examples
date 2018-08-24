<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" 
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	>

	<!-- ESTE ARQUIVO FOI PROJETADO PARA GRAVAR DADOS NO BUSDATA DO PMAC -->
	<!-- omit-xml-declaration=yes DEVE SER INCLUIDO -->

	<xsl:output method="xml" omit-xml-declaration="yes" />

	<xsl:template match="/">
		<DETALHE_COMPROVANTE_TRANSACAO_SAIDA>
			<CONTROLE_NEGOCIAL>
				<ORIGEM_RETORNO>SICCO</ORIGEM_RETORNO>
				<COD_RETORNO><xsl:value-of select="/BUSDATA/*[local-name()='DETALHE_COMPROVANTE_TRANSACAO_SAIDA']/HEADER/COD_RETORNO"/></COD_RETORNO>
				<MSG_RETORNO><xsl:value-of select="/BUSDATA/*[local-name()='DETALHE_COMPROVANTE_TRANSACAO_SAIDA']/HEADER/MENSAGENS/RETORNO"/></MSG_RETORNO>
			</CONTROLE_NEGOCIAL>
		</DETALHE_COMPROVANTE_TRANSACAO_SAIDA>
	</xsl:template>
</xsl:stylesheet>