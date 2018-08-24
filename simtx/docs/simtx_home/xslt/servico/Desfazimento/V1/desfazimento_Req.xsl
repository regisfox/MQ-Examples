<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" 
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:multicanal="http://caixa.gov.br/sibar/multicanal/orquestracao"
	xmlns:sibar_base="http://caixa.gov.br/sibar"
	xmlns:controlelimite="http://caixa.gov.br/sibar/controle_limite/desfazimento" 
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://caixa.gov.br/simtx/multicanal/orquestracao Multicanal_Orquestracao.xsd">
	
	<xsl:output omit-xml-declaration="yes"/>
	<xsl:strip-space elements="*" />
	
	<xsl:param name="canalOriginal" />
	
	<xsl:template match="*" mode="copy">
		<xsl:element name="{name()}" namespace="{namespace-uri()}">
			<xsl:apply-templates select="@*|node()" mode="copy" />
		</xsl:element>
	</xsl:template>

	<xsl:template match="@*|text()|comment()" mode="copy">
		<xsl:copy />
	</xsl:template>
	
	<xsl:template match="/BUSDATA">
		<multicanal:SERVICO_ENTRADA 
			xmlns:multicanal="http://caixa.gov.br/sibar/multicanal/orquestracao" 
			xmlns:controlelimite="http://caixa.gov.br/sibar/controle_limite/desfazimento" 
			xmlns:solicitacaodebito="http://caixa.gov.br/sibar/solicitacao_debito" 
			xmlns:sibar_base="http://caixa.gov.br/sibar" 
			xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" 
			xsi:schemaLocation="http://caixa.gov.br/simtx/multicanal/orquestracao Multicanal_Orquestracao.xsd">
			<sibar_base:HEADER>
				<VERSAO>1.0</VERSAO>
				<xsl:choose>
					<xsl:when test="/BUSDATA/*[2]/HEADER/USUARIO_SERVICO">
						<USUARIO_SERVICO>
							<xsl:value-of select="/BUSDATA/*[2]/HEADER/USUARIO_SERVICO"/>
						</USUARIO_SERVICO>
					</xsl:when>
				</xsl:choose>
				<xsl:choose>
					<xsl:when test="/BUSDATA/*[2]/HEADER/USUARIO">
						<USUARIO>
							<xsl:value-of select="/BUSDATA/*[2]/HEADER/USUARIO"/>
						</USUARIO>
					</xsl:when>
				</xsl:choose>
				<OPERACAO>SERVICO_MIGRADO</OPERACAO>
				<SISTEMA_ORIGEM>
					<xsl:value-of select="$canalOriginal"/>
				</SISTEMA_ORIGEM>
				<xsl:choose>
					<xsl:when test="/BUSDATA/*[2]/HEADER/IDENTIFICADOR_ORIGEM">
						<xsl:choose>
							<xsl:when test="/BUSDATA/*[2]/HEADER/IDENTIFICADOR_ORIGEM/TERMINAL">
								<UNIDADE>
									<xsl:value-of select="/BUSDATA/*[2]/HEADER/IDENTIFICADOR_ORIGEM/TERMINAL"/>
								</UNIDADE>
								<IDENTIFICADOR_ORIGEM>
									<xsl:value-of select="/BUSDATA/*[2]/HEADER/IDENTIFICADOR_ORIGEM/TERMINAL"/>
								</IDENTIFICADOR_ORIGEM>
							</xsl:when>
							<xsl:when test="/BUSDATA/*[2]/HEADER/IDENTIFICADOR_ORIGEM/ENDERECO_IP">
								<IDENTIFICADOR_ORIGEM>
									<xsl:value-of select="/BUSDATA/*[2]/HEADER/IDENTIFICADOR_ORIGEM/ENDERECO_IP"/>
								</IDENTIFICADOR_ORIGEM>
							</xsl:when>
							<xsl:otherwise>
								<IDENTIFICADOR_ORIGEM>
									<xsl:value-of select="/BUSDATA/*[2]/HEADER/IDENTIFICADOR_ORIGEM/CODIGO_MAQUINA"/>
								</IDENTIFICADOR_ORIGEM>
							</xsl:otherwise>
						</xsl:choose>
					</xsl:when>
				</xsl:choose>
				<DATA_HORA>
					<xsl:value-of select="/BUSDATA/BUSTIME/ANO" />
					<xsl:value-of select="/BUSDATA/BUSTIME/MES" />
					<xsl:value-of select="/BUSDATA/BUSTIME/DIA" />
					<xsl:value-of select="/BUSDATA/BUSTIME/HORA" />
					<xsl:value-of select="/BUSDATA/BUSTIME/MIN" />
					<xsl:value-of select="/BUSDATA/BUSTIME/SEG" />
				</DATA_HORA>
			</sibar_base:HEADER>
			
			<xsl:apply-templates mode="copy" select="DESFAZIMENTO/*" />
			
		</multicanal:SERVICO_ENTRADA>
	</xsl:template>
</xsl:stylesheet>