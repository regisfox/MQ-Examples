<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0"
	xmlns:mtx="http://caixa.gov.br/sibar/multicanal/orquesracao" 
	xmlns:sibar_base="http://caixa.gov.br/sibar"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" 
	xsi:schemaLocation="http://caixa.gov.br/simtx/multicanal/orquestracao Multicanal_Orquestracao.xsd"
	xmlns:validapermissao="http://caixa.gov.br/sibar/valida_permissao/assinatura_multipla"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	exclude-result-prefixes="mtx">

	<xsl:output omit-xml-declaration="yes"/>
	<xsl:strip-space elements="*" />
	<xsl:param name="nsu" />
	
	<xsl:template match="/">
		<mtx:SERVICO_ENTRADA 
			xmlns:mtx="http://caixa.gov.br/sibar/multicanal/orquestracao" 
			xmlns:sibar_base="http://caixa.gov.br/sibar" 
			xmlns:validapermissao="http://caixa.gov.br/sibar/valida_permissao/assinatura_multipla" 
			xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" 
			xsi:schemaLocation="http://caixa.gov.br/simtx/multicanal/orquestracao Multicanal_Orquestracao.xsd">

			<xsl:apply-templates />  
			
		</mtx:SERVICO_ENTRADA>
	</xsl:template>
	
	<xsl:template match="HEADER">
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
				<xsl:value-of select="/BUSDATA/*[2]/HEADER/CANAL/SIGLA"/>
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
	</xsl:template>
	
	<xsl:template match="CPF" />
	<xsl:template match="BUSTIME" />
	<xsl:template match="CONTA" />
	<xsl:template match="TOKEN" />
	<xsl:template match="*[local-name()='ASSINATURA_SIMPLES']" />
	<xsl:template match="*[local-name()='ASSINATURA_MULTIPLA']" />
	
	<xsl:template match="*[local-name()='CONTRA_ORDEM_CHEQUE']">
		<validapermissao:VALIDA_PERMISSAO_ENTRADA>
			<CPF><xsl:value-of select="/BUSDATA/*[2]/CPF" /></CPF>
			<CONTA_SIDEC>
				<UNIDADE><xsl:value-of select="/BUSDATA/*[2]/*[local-name()='CONTA']/CONTA_SIDEC/UNIDADE" /></UNIDADE>
				<OPERACAO><xsl:value-of select="/BUSDATA/*[2]/*[local-name()='CONTA']/CONTA_SIDEC/OPERACAO" /></OPERACAO>
				<NUMERO><xsl:value-of select="/BUSDATA/*[2]/*[local-name()='CONTA']/CONTA_SIDEC/CONTA" /></NUMERO>
				<DV><xsl:value-of select="/BUSDATA/*[2]/*[local-name()='CONTA']/CONTA_SIDEC/DV" /></DV>
			</CONTA_SIDEC>
			<ASSINATURA><xsl:value-of select="/BUSDATA/*[2]/*[local-name()='ASSINATURA_MULTIPLA']/ASSINATURA" /></ASSINATURA>
			<NSU_ORIGEM><xsl:value-of select="$nsu" /></NSU_ORIGEM>
			<DATA_PREVISTA_EFETIVACAO><xsl:value-of select="/BUSDATA/*[2]/*[local-name()='ASSINATURA_MULTIPLA']/DATA_PREVISTA_EFETIVACAO" /></DATA_PREVISTA_EFETIVACAO>
			<SERVICO><xsl:value-of select="/BUSDATA/*[2]/*[local-name()='ASSINATURA_MULTIPLA']/SERVICO_SIPER" /></SERVICO>
			<TRANSACAO>
				<xsl:variable name="data" select="/BUSDATA/*[2]/*[local-name()='HEADER']/CANAL/DATAHORA"/>
				<DATA>
					<xsl:value-of select="concat(substring($data, 1, 4),'-')"/>
					<xsl:value-of select="concat(substring($data, 5, 2),'-')"/>
					<xsl:value-of select="substring($data, 7, 2)"/>
				</DATA> 
			</TRANSACAO>
			<APELIDO><xsl:value-of select="/BUSDATA/*[2]/*[local-name()='ASSINATURA_MULTIPLA']/APELIDO" /></APELIDO>
			<ENDERECO_IP><xsl:value-of select="/BUSDATA/*[2]/HEADER/IDENTIFICADOR_ORIGEM/ENDERECO_IP" /></ENDERECO_IP>
			<TOKEN><xsl:value-of select="/BUSDATA/*[2]/*[local-name()='TOKEN']/TOKEN" /></TOKEN>
			<SESSION_ID><xsl:value-of select="/BUSDATA/*[2]/*[local-name()='TOKEN']/ID_SESSAO" /></SESSION_ID>
			<xsl:if test="/BUSDATA/*[2]/*[local-name()='ASSINATURA_MULTIPLA']/DISPOSITIVO">
				<DISPOSITIVO>
					<SISTEMA_OPERACIONAL><xsl:value-of select="/BUSDATA/*[2]/*[local-name()='ASSINATURA_MULTIPLA']/DISPOSITIVO/SISTEMA_OPERACIONAL" /></SISTEMA_OPERACIONAL>
					<CODIGO><xsl:value-of select="/BUSDATA/*[2]/*[local-name()='ASSINATURA_MULTIPLA']/DISPOSITIVO/CODIGO" /></CODIGO>
				</DISPOSITIVO>
			</xsl:if>
		</validapermissao:VALIDA_PERMISSAO_ENTRADA>
	</xsl:template>
	
	<xsl:template match="*" mode="copy">
	  <xsl:element name="{name()}" namespace="{namespace-uri()}">
	    <xsl:apply-templates select="@*|node()" mode="copy" />
	  </xsl:element>
	</xsl:template>
	
	<xsl:template match="@*|text()|comment()" mode="copy">
	  <xsl:copy/>
	</xsl:template>
</xsl:stylesheet>