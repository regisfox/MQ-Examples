<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0"
	xmlns:mtx="http://caixa.gov.br/sibar/multicanal/orquesracao" 
	xmlns:manutencaoassinaturaeletronica="http://caixa.gov.br/sibar/manutencao_assinatura_eletronica/cancela_assinatura_multipla" 
	xmlns:sibar_base="http://caixa.gov.br/sibar"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" 
	xsi:schemaLocation="http://caixa.gov.br/simtx/multicanal/orquestracao Multicanal_Orquestracao.xsd"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	exclude-result-prefixes="mtx">

	<xsl:output omit-xml-declaration="yes"/>
	<xsl:strip-space elements="*" />
	
	<xsl:template match="/BUSDATA">
	
		<mtx:SERVICO_ENTRADA 
			xmlns:mtx="http://caixa.gov.br/sibar/multicanal/orquestracao" 
			xmlns:manutencaoassinaturaeletronica="http://caixa.gov.br/sibar/manutencao_assinatura_eletronica/cancela_assinatura_multipla" 
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
		
		<manutencaoassinaturaeletronica:MANUTENCAO_ASSINATURA_ELETRONICA_ENTRADA>
			<CPF><xsl:value-of select="/BUSDATA/*[2]/*[local-name()='CPF']"/></CPF>
			<!-- NSU_SIPER --><copy-of select="/BUSDATA/*[2]/*[local-name()='CANCELA_TRANSACAO_ASSINATURA_MULTIPLA']/NSU_SIPER" />
			<!-- NSU_TRANSACAO_ORIGEM--><copy-of select="/BUSDATA/*[2]/*[local-name()='NSU_ORIGEM']" />
			<xsl:variable name="data" select="/BUSDATA/*[2]/*[local-name()='HEADER']/CANAL/DATAHORA"/>
			<DATA_TRANSACAO><xsl:value-of select="/BUSDATA/*[2]/*[local-name()='CANCELA_TRANSACAO_ASSINATURA_MULTIPLA']/DATA_TRANSACAO"/></DATA_TRANSACAO>
	      	<CONTA_SIDEC>
				<UNIDADE><xsl:value-of select="/BUSDATA/*[2]/*[local-name()='CONTA']/CONTA_SIDEC/UNIDADE" /></UNIDADE>
				<OPERACAO><xsl:value-of select="/BUSDATA/*[2]/*[local-name()='CONTA']/CONTA_SIDEC/OPERACAO" /></OPERACAO>
				<NUMERO><xsl:value-of select="/BUSDATA/*[2]/*[local-name()='CONTA']/CONTA_SIDEC/CONTA" /></NUMERO>
				<DV><xsl:value-of select="/BUSDATA/*[2]/*[local-name()='CONTA']/CONTA_SIDEC/DV" /></DV>
			</CONTA_SIDEC>
			<ASSINATURA><xsl:value-of select="/BUSDATA/*[2]/*[local-name()='CANCELA_TRANSACAO_ASSINATURA_MULTIPLA']/ASSINATURA"/></ASSINATURA>
			<APELIDO><xsl:value-of select="/BUSDATA/*[2]/*[local-name()='CANCELA_TRANSACAO_ASSINATURA_MULTIPLA']/APELIDO"/></APELIDO>
			<!-- ENDERECO_IP --><xsl:copy-of select="/BUSDATA/*[2]/HEADER/IDENTIFICADOR_ORIGEM/ENDERECO_IP" />
			<TOKEN><xsl:value-of select="/BUSDATA/*[2]/*[local-name()='TOKEN/TOKEN']"/></TOKEN>
			<SESSION_ID><xsl:value-of select="/BUSDATA/*[2]/*[local-name()='TOKEN/ID_SESSAO']"/></SESSION_ID>
			<!-- DISPOSITIVO --><xsl:copy-of select="/BUSDATA/*[2]/*[local-name()='CANCELA_TRANSACAO_ASSINATURA_MULTIPLA']/DISPOSITIVO" /><!-- DISPOSITIVO -->
		</manutencaoassinaturaeletronica:MANUTENCAO_ASSINATURA_ELETRONICA_ENTRADA>
	</mtx:SERVICO_ENTRADA>
</xsl:template>
</xsl:stylesheet>