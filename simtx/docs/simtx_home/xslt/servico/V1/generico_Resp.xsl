<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
	xmlns:sibar_base="http://caixa.gov.br/sibar" 
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	exclude-result-prefixes="sibar_base xsi xsl">


	<xsl:output omit-xml-declaration="yes" indent="no" />
	<xsl:strip-space elements="*" />

	<xsl:template match="node()|@*">
		<xsl:copy>
			<xsl:apply-templates select="node()|@*" />
		</xsl:copy>
	</xsl:template>
	

	<xsl:param name="codRetorno" />
	<xsl:param name="origemRetorno" />
	<xsl:param name="msgRetorno" />
	<xsl:param name="msgInstitucional" />
	<xsl:param name="msgTela" />
	<xsl:param name="msgInformativa" />
	<xsl:param name="mensagem" />
	<xsl:param name="nsuSimtx" />
	
	
	<xsl:template match="/BUSDATA">
		<naoidentificado:SERVICO_SAIDA
			xmlns:header="http://caixa.gov.br/simtx/comuns/header" 
			xmlns:comuns="http://caixa.gov.br/simtx/comuns/comuns"
			xmlns:naoidentificado="http://caixa.gov.br/simtx/naoidentificado"
			xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
			<HEADER>
				<SERVICO>
					<CODIGO>999999</CODIGO>
					<VERSAO>9</VERSAO>
				</SERVICO>
				<CANAL>
					<SIGLA><xsl:value-of select="*[2]/*[local-name()='HEADER']/CANAL/SIGLA"/></SIGLA>
					<xsl:if test="*[2]/*[local-name()='HEADER']/CANAL/NSU/text() != ''">
						<NSU><xsl:value-of select="*[2]/*[local-name()='HEADER']/CANAL/NSU"/></NSU>
					</xsl:if>
					<DATAHORA><xsl:value-of select="*[2]/*[local-name()='HEADER']/CANAL/DATAHORA"/></DATAHORA>
				</CANAL>
				<MEIOENTRADA>0</MEIOENTRADA>
				<xsl:if test="*[2]/*[local-name()='HEADER']/IDENTIFICADOR_ORIGEM != ''">
					<xsl:choose>
						<xsl:when test="*[2]/*[local-name()='HEADER']/IDENTIFICADOR_ORIGEM/TERMINAL">
							<UNIDADE>
								<xsl:value-of select="substring(*[2]/*[local-name()='HEADER']/IDENTIFICADOR_ORIGEM/TERMINAL, 1, 4)"/>
							</UNIDADE>
							<IDENTIFICADOR_ORIGEM>
								<TERMINAL><xsl:value-of select="*[2]/*[local-name()='HEADER']/IDENTIFICADOR_ORIGEM/TERMINAL"/></TERMINAL>										
							</IDENTIFICADOR_ORIGEM>
						</xsl:when>
						<xsl:when test="*[2]/*[local-name()='HEADER']/IDENTIFICADOR_ORIGEM/ENDERECO_IP">
							<IDENTIFICADOR_ORIGEM>
								<ENDERECO_IP><xsl:value-of select="*[2]/*[local-name()='HEADER']/IDENTIFICADOR_ORIGEM/ENDERECO_IP"/></ENDERECO_IP>
							</IDENTIFICADOR_ORIGEM>
						</xsl:when>
						<xsl:otherwise>
							<IDENTIFICADOR_ORIGEM>
								<CODIGO_MAQUINA><xsl:value-of select="*[2]/*[local-name()='HEADER']/IDENTIFICADOR_ORIGEM/CODIGO_MAQUINA"/></CODIGO_MAQUINA>
							</IDENTIFICADOR_ORIGEM>
						</xsl:otherwise>
					</xsl:choose>
				</xsl:if>
				<xsl:if test="*[2]/*[local-name()='HEADER']/USUARIO_SERVICO/text() != ''">
					<USUARIO_SERVICO><xsl:value-of select="*[2]/*[local-name()='HEADER']/USUARIO_SERVICO"/></USUARIO_SERVICO>
				</xsl:if>
				<xsl:if test="*[2]/*[local-name()='HEADER']/USUARIO/text() != ''">
					<USUARIO><xsl:value-of select="*[2]/*[local-name()='HEADER']/USUARIO"/></USUARIO>
				</xsl:if>
				<DATA_REFERENCIA>
					<xsl:value-of select="BUSTIME/ANO" />
					<xsl:value-of select="BUSTIME/MES" />
					<xsl:value-of select="BUSTIME/DIA" />
				</DATA_REFERENCIA>
				<NSUMTX><xsl:value-of select="$nsuSimtx"/></NSUMTX>
				<COD_RETORNO><xsl:value-of select="$codRetorno"/></COD_RETORNO>
				<ORIGEM_RETORNO><xsl:value-of select="$origemRetorno"/></ORIGEM_RETORNO>
				<MENSAGENS>
					<RETORNO><xsl:value-of select="$msgRetorno"/></RETORNO>
					<xsl:if test="$msgInstitucional != ''">
						<INSTITUCIONAL><xsl:value-of select="$msgInstitucional"/></INSTITUCIONAL>
					</xsl:if>
					<xsl:if test="$msgInformativa != ''">
						<INFORMATIVA><xsl:value-of select="$msgInformativa"/></INFORMATIVA>
					</xsl:if>
					<xsl:if test="$msgTela != ''">
						<TELA><xsl:value-of select="$msgTela"/></TELA>
					</xsl:if>
					<xsl:if test="$mensagem != ''">
						<MENSAGEM><xsl:value-of select="$mensagem"/></MENSAGEM>
					</xsl:if>
				</MENSAGENS>
			</HEADER>
		</naoidentificado:SERVICO_SAIDA>
	</xsl:template>
</xsl:stylesheet>