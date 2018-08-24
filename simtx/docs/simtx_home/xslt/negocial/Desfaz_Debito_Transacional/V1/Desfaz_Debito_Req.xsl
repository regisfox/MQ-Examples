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
		<xsl:variable name="entrada" select="*[local-name()='SERVICO_ENTRADA']/*[local-name()='SOLICITACAO_DEBITO_ENTRADA']"/>
		<xsl:variable name="saida" select="*[local-name()='SOLICITACAO_DEBITO_SAIDA']"/>
	
		<solicitacaodebito:SOLICITACAO_DEBITO_ENTRADA xmlns:solicitacaodebito="http://caixa.gov.br/sibar/solicitacao_debito" >
			<TIPO_SOLICITACAO>DESFAZIMENTO</TIPO_SOLICITACAO>
			<xsl:apply-templates mode="copy" select="$entrada/SIGLA_SERVICO" disable-output-escaping="yes"/>
			<xsl:apply-templates mode="copy" select="$entrada/CONTA" disable-output-escaping="yes"/>
			<xsl:apply-templates mode="copy" select="$entrada/VALOR_TRANSACAO" disable-output-escaping="yes"/>
			<xsl:apply-templates mode="copy" select="$entrada/DATA_EFETIVACAO" disable-output-escaping="yes"/>
			<xsl:apply-templates mode="copy" select="$entrada/NSU_CANAL" disable-output-escaping="yes"/>
			<xsl:apply-templates mode="copy" select="$entrada/SISTEMA_DESTINO" disable-output-escaping="yes"/>
			<xsl:apply-templates mode="copy" select="$entrada/TIPO_AUTENTICACAO" disable-output-escaping="yes"/>
			<xsl:apply-templates mode="copy" select="$entrada/TERMINAL" disable-output-escaping="yes"/>
			<xsl:apply-templates mode="copy" select="$entrada/FAVORECIDO" disable-output-escaping="yes"/>
		    <DADOS_CONFIRMACAO>
		     	<NSU_TRANSACAO><xsl:value-of select="$saida/NSU_TRANSACAO"/></NSU_TRANSACAO>			    
		      	<DATA_TRANSACAO><xsl:value-of select="$entrada/DATA_EFETIVACAO"/></DATA_TRANSACAO>
		      	<NSU_EFETIVACAO><xsl:value-of select="$saida/NSU_EFETIVACAO"/></NSU_EFETIVACAO>
		    </DADOS_CONFIRMACAO>
		</solicitacaodebito:SOLICITACAO_DEBITO_ENTRADA>
	</xsl:template>
</xsl:stylesheet>