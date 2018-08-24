<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	
	<xsl:output omit-xml-declaration="yes"/>
	<xsl:strip-space elements="*" />
	
	<xsl:template match="*" mode="copy">
		<xsl:element name="{name()}" namespace="{namespace-uri()}">
			<xsl:apply-templates select="@*|node()" mode="copy" />
		</xsl:element>
	</xsl:template>

	<xsl:template match="@*|text()|comment()" mode="copy">
		<xsl:copy />
	</xsl:template> 
	
	<xsl:template match="/root">
		<xsl:variable name="entrada" select="*[local-name()='SERVICO_ENTRADA']/*[local-name()='CONTROLE_LIMITE_ENTRADA']"/>
		<xsl:variable name="saida" select="*[local-name()='CONTROLE_LIMITE_SAIDA']"/>
			
		<controlelimite:CONTROLE_LIMITE_ENTRADA xmlns:controlelimite="http://caixa.gov.br/sibar/controle_limite/desfazimento">
			<xsl:apply-templates mode="copy" select="$entrada/TIPO_PAGAMENTO" disable-output-escaping="yes"/>
			<xsl:apply-templates mode="copy" select="$entrada/CONTA" disable-output-escaping="yes"/>
			<xsl:apply-templates mode="copy" select="$entrada/VALOR_TRANSACAO" disable-output-escaping="yes"/>
			<xsl:apply-templates mode="copy" select="$entrada/DATA_EFETIVACAO" disable-output-escaping="yes"/>
			<xsl:apply-templates mode="copy" select="$entrada/TIPO_AUTENTICACAO" disable-output-escaping="yes"/>
		</controlelimite:CONTROLE_LIMITE_ENTRADA>
		
	</xsl:template>
	
	<xsl:template match="/BUSDATA">
		<xsl:apply-templates mode="copy" select="*[local-name()='SERVICO_ENTRADA']/*[local-name()='CONTROLE_LIMITE_ENTRADA']" disable-output-escaping="yes"/>
	</xsl:template>
</xsl:stylesheet>