<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	version="2.0">
	<!-- ESTE ARQUIVO FOI PROJETADO PARA GRAVAR DADOS NO BUSDATA DO PMAC -->
	<!-- omit-xml-declaration=yes DEVE SER INCLUIDO -->
	<xsl:output omit-xml-declaration="yes" method="xml" />
	<xsl:template match="/">
		<DETALHE_TRANSACA_PENDENTE_ASSINATURA>
			   <CONTROLE_NEGOCIAL>
			      <ORIGEM_RETORNO>SIMTX</ORIGEM_RETORNO>
			      <COD_RETORNO>00</COD_RETORNO>
			      <MENSAGENS>
			        <RETORNO>SUCESSO</RETORNO>
			      </MENSAGENS>
			    </CONTROLE_NEGOCIAL>
		</DETALHE_TRANSACA_PENDENTE_ASSINATURA>
	</xsl:template>
</xsl:stylesheet>