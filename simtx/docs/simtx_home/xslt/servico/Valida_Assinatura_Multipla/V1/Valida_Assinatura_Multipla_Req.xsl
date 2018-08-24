<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0"
	xmlns:multicanal="http://caixa.gov.br/sibar/multicanal/orquestracao"
	xmlns:manutencaocheque="http://caixa.gov.br/sibar/manutencao_cheque/contraordem" 
	xmlns:sibar_base="http://caixa.gov.br/sibar"
	xmlns:assinaturamultipla="http://caixa.gov.br/sibar/valida_permissao/assinatura_multipla"
	xmlns:assinaturasimples="http://caixa.gov.br/sibar/valida_permissao/assinatura_simples" 
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" 
	xsi:schemaLocation="http://caixa.gov.br/simtx/multicanal/orquestracao Multicanal_Orquestracao.xsd"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	exclude-result-prefixes="multicanal">

	<xsl:output omit-xml-declaration="yes"/>
	<xsl:strip-space elements="*" />
	
	<xsl:template match="/">
		<multicanal:SERVICO_ENTRADA 
			xmlns:multicanal="http://caixa.gov.br/sibar/multicanal/orquestracao" 
			xmlns:assinaturamultipla="http://caixa.gov.br/sibar/valida_permissao/assinatura_multipla" 
			xmlns:sibar_base="http://caixa.gov.br/sibar" 
			xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" 
			xsi:schemaLocation="http://caixa.gov.br/simtx/multicanal/orquestracao Multicanal_Orquestracao.xsd">

			<xsl:apply-templates />  
			
		</multicanal:SERVICO_ENTRADA>
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
	
	<xsl:template match="*[local-name()='ASSINATURA_MULTIPLA']">
		<assinaturamultipla:VALIDA_PERMISSAO_ENTRADA>
			<xsl:apply-templates mode="copy" select="/BUSDATA/*[2]/CPF" />
			<CONTA_SIDEC>
				<UNIDADE><xsl:value-of select="/BUSDATA/*[2]/*[local-name()='CONTA']/CONTA_SIDEC/UNIDADE" /></UNIDADE>
				<OPERACAO><xsl:value-of select="/BUSDATA/*[2]/*[local-name()='CONTA']/CONTA_SIDEC/OPERACAO" /></OPERACAO>
				<NUMERO><xsl:value-of select="/BUSDATA/*[2]/*[local-name()='CONTA']/CONTA_SIDEC/CONTA" /></NUMERO>
				<DV><xsl:value-of select="/BUSDATA/*[2]/*[local-name()='CONTA']/CONTA_SIDEC/DV" /></DV>
			</CONTA_SIDEC>
			<xsl:apply-templates mode="copy" select="/BUSDATA/*[2]/*[local-name()='ASSINATURA_MULTIPLA']/ASSINATURA" />
			<NSU_ORIGEM><xsl:value-of select="/BUSDATA/*[2]/NSU_ORIGEM"/></NSU_ORIGEM>
			
			<xsl:apply-templates mode="copy" select="/BUSDATA/*[2]/*[local-name()='ASSINATURA_MULTIPLA']/DATA_PREVISTA_EFETIVACAO" />
			<SERVICO><xsl:value-of select="/BUSDATA/*[2]/*[local-name()='ASSINATURA_MULTIPLA']/SERVICO_SIPER" /></SERVICO>
			<TRANSCAO>
				<RESUMO><xsl:value-of select="/BUSDATA/*[2]/*[local-name()='ASSINATURA_MULTIPLA']/RESUMO_TRANSACAO" /></RESUMO>
				<xsl:variable name="data" select="/BUSDATA/*[2]/*[local-name()='HEADER']/CANAL/DATAHORA"/>
				<DATA><xsl:value-of select="substring($data, 1, 4)-substring($data, 6, 2)-substring($data, 9, 2)" /></DATA>
			</TRANSCAO>
			<xsl:apply-templates mode="copy" select="/BUSDATA/*[2]/*[local-name()='ASSINATURA_MULTIPLA']/APELIDO" />
			<xsl:apply-templates mode="copy" select="/BUSDATA/*[2]/*[local-name()='HEADER']/IDENTIFICADOR_ORIGEM/ENDERECO_IP" />
			<TOKEN><xsl:value-of select="/BUSDATA/*[2]/*[local-name()='TOKEN']/TOKEN" /></TOKEN>
			<SESSION_ID><xsl:value-of select="/BUSDATA/*[2]/*[local-name()='TOKEN']/ID_SESSAO" /></SESSION_ID>
			<xsl:if test="/BUSDATA/*[2]/*[local-name()='ASSINATURA_MULTIPLA']/DISPOSITIVO/SISTEMA_OPERACIONAL">
				<DISPOSITIVO>
					<SISTEMA_OPERACIONAL><xsl:value-of select="/BUSDATA/*[2]/*[local-name()='ASSINATURA_MULTIPLA']/DISPOSITIVO/SISTEMA_OPERACIONAL" /></SISTEMA_OPERACIONAL>
					<CODIGO><xsl:value-of select="/BUSDATA/*[2]/*[local-name()='ASSINATURA_MULTIPLA']/DISPOSITIVO/CODIGO" /></CODIGO>
				</DISPOSITIVO>
			</xsl:if>
		</assinaturamultipla:VALIDA_PERMISSAO_ENTRADA>
	</xsl:template>
	
	<xsl:template match="*" mode="copy">
		<xsl:element name="{name()}" namespace="{namespace-uri()}">
			<xsl:apply-templates select="@*|node()" mode="copy" />
		</xsl:element>
	</xsl:template>

	<xsl:template match="@*|text()|comment()" mode="copy">
		<xsl:copy />
	</xsl:template>
</xsl:stylesheet>