<?xml version="1.0" encoding="UTF-8"?>

<!--
  ///////////////////////////////////////////////////////////////////////////////////////////
  
  Criador: Renato Alves Grosz
  Empresa: BRQ
  Contato: rgrosz@brq.com

  Arquivo utilizado no pmac para copiar dados do xml origem  
  ///////////////////////////////////////////////////////////////////////////////////////////

-->

<xsl:stylesheet version="2.0" 
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:consulta_rnv_consignacao_simtx="http://caixa.gov.br/simtx/consulta_rnv_consignacao_simtx"
	xmlns:simtx_base="http://caixa.gov.br/simtx/Mensagens_simtx"
	xmlns:header_simtx="http://caixa.gov.br/simtx/Header_simtx"
	xmlns:autenticacaobiometrica_simtx="http://caixa.gov.br/simtx/autenticacao_biometrica"
	xmlns:valida_cartao_simtx="http://caixa.gov.br/simtx/valida_cartao"
	xmlns:consultacontasid09_simtx="http://caixa.gov.br/simtx/consulta_conta_sid09"
	xmlns:controlecorrespondente_simtx="http://caixa.gov.br/simtx/controle_correspondente"
	xmlns:validapermissao_simtx="http://caixa.gov.br/simtx/validapermissao"
	xmlns:valida_senha_simtx="http://caixa.gov.br/simtx/valida_senha"
	xmlns:consulta_doc_nis_simtx="http://caixa.gov.br/simtx/consulta_documento_nis"
	xmlns:ns6="http://caixa.gov.br/simtx/consulta_rnv_consignacao_simtx"
	exclude-result-prefixes="consulta_rnv_consignacao_simtx valida_cartao_simtx header_simtx simtx_base autenticacaobiometrica_simtx consultacontasid09_simtx controlecorrespondente_simtx validapermissao_simtx valida_senha_simtx consulta_doc_nis_simtx ns6">

	<xsl:output omit-xml-declaration="yes"/>
 	<xsl:strip-space elements="*"/>

	<!-- template to copy elements -->
	<xsl:template match="*">
        <xsl:element name="{local-name()}">
            <xsl:apply-templates select="@* | node()"/>
        </xsl:element>
    </xsl:template>
 	
 	<xsl:template match="ns6:*">
		<xsl:element name="{local-name()}">
			<xsl:copy-of select="ns6:*[not(. = namespace-uri(..))]" />
			<xsl:apply-templates select="node()|@*" />
		</xsl:element>
	</xsl:template>

	<xsl:template match="/">
		<xsl:copy-of select="/" disable-output-escaping="yes"/>
	</xsl:template>
</xsl:stylesheet>