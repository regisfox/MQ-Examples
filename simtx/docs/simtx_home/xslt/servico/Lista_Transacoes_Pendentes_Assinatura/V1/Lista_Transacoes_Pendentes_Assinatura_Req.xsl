<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

	<xsl:output omit-xml-declaration="yes"/>
	<xsl:strip-space elements="*" />

	<xsl:template match="node()|@*">
		<xsl:copy>
			<xsl:apply-templates select="node()|@*" />
		</xsl:copy>
	</xsl:template>
	
	<xsl:template match="/BUSDATA">
		<multicanal:SERVICO_ENTRADA 
			xmlns:multicanal="http://caixa.gov.br/sibar/multicanal/orquestracao" 
			xmlns:sibar_base="http://caixa.gov.br/sibar" 
			xmlns:lista_transacoes_pendentes="http://caixa.gov.br/sibar/consulta_assinatura_eletronica/lista_transacoes_pendentes"
			xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" 
			xsi:schemaLocation="http://caixa.gov.br/simtx/multicanal/orquestracao Multicanal_Orquestracao.xsd">
			<sibar_base:HEADER>
				<VERSAO>1.0</VERSAO>
				<USUARIO_SERVICO><xsl:value-of select="*[2]/*[local-name()='HEADER']/USUARIO_SERVICO"/></USUARIO_SERVICO>
				<USUARIO><xsl:value-of select="*[2]/*[local-name()='HEADER']/USUARIO"/></USUARIO>
				<OPERACAO>SERVICO_MIGRADO_ListaAssinaturaMultipla</OPERACAO>
				<SISTEMA_ORIGEM><xsl:value-of select="*[2]/*[local-name()='HEADER']/CANAL/SIGLA"/></SISTEMA_ORIGEM>
				<xsl:if test="*[2]/*[local-name()='HEADER']/IDENTIFICADOR_ORIGEM/text() != ''">
					<xsl:choose>
						<xsl:when test="*[2]/*[local-name()='HEADER']/IDENTIFICADOR_ORIGEM/TERMINAL">
							<UNIDADE>
								<xsl:value-of select="substring(*[2]/*[local-name()='HEADER']/IDENTIFICADOR_ORIGEM/TERMINAL, 1, 4)"/>
							</UNIDADE>
							<IDENTIFICADOR_ORIGEM>
								<xsl:value-of select="*[2]/*[local-name()='HEADER']/IDENTIFICADOR_ORIGEM/TERMINAL"/>
							</IDENTIFICADOR_ORIGEM>
						</xsl:when>
						<xsl:when test="*[2]/*[local-name()='HEADER']/IDENTIFICADOR_ORIGEM/ENDERECO_IP">
							<IDENTIFICADOR_ORIGEM>
								<xsl:value-of select="*[2]/*[local-name()='HEADER']/IDENTIFICADOR_ORIGEM/ENDERECO_IP"/>
							</IDENTIFICADOR_ORIGEM>
						</xsl:when>
						<xsl:otherwise>
							<IDENTIFICADOR_ORIGEM>
								<xsl:value-of select="*[2]/*[local-name()='HEADER']/IDENTIFICADOR_ORIGEM/CODIGO_MAQUINA"/>
							</IDENTIFICADOR_ORIGEM>
						</xsl:otherwise>
					</xsl:choose>
				</xsl:if>
				<DATA_HORA>
					<xsl:value-of select="BUSTIME/ANO" />
					<xsl:value-of select="BUSTIME/MES" />
					<xsl:value-of select="BUSTIME/DIA" />
					<xsl:value-of select="BUSTIME/HORA" />
					<xsl:value-of select="BUSTIME/MIN" />
					<xsl:value-of select="BUSTIME/SEG" />
				</DATA_HORA>
			</sibar_base:HEADER>
			
			<lista_transacoes_pendentes:CONSULTA_ASSINATURA_ELETRONICA_ENTRADA>
				<TIPO><xsl:value-of select="*[2]/LISTA_TRANSACOES_PENDENTES_ASSINATURA/TIPO"/></TIPO>
				<CPF><xsl:value-of select="*[2]/LISTA_TRANSACOES_PENDENTES_ASSINATURA/CPF"/></CPF>
				<xsl:choose>
					<xsl:when test="*[2]/*[local-name()='CONTA']/CONTA_SIDEC">
						<CONTA_SIDEC>
							<UNIDADE><xsl:value-of select="*[2]/*[local-name()='CONTA']/CONTA_SIDEC/UNIDADE"/></UNIDADE>
							<PRODUTO><xsl:value-of select="*[2]/*[local-name()='CONTA']/CONTA_SIDEC/OPERACAO"/></PRODUTO>
							<NUMERO><xsl:value-of select="*[2]/*[local-name()='CONTA']/CONTA_SIDEC/CONTA"/></NUMERO>
							<DV><xsl:value-of select="*[2]/*[local-name()='CONTA']/CONTA_SIDEC/DV"/></DV>
						</CONTA_SIDEC>
					</xsl:when>
					<xsl:when test="*[2]/*[local-name()='CONTA']/CONTA_NSGD">
						<CONTA_NSGD>
							<UNIDADE><xsl:value-of select="*[2]/*[local-name()='CONTA']/CONTA_NSGD/UNIDADE"/></UNIDADE>
							<PRODUTO><xsl:value-of select="*[2]/*[local-name()='CONTA']/CONTA_NSGD/PRODUTO"/></PRODUTO>
							<NUMERO><xsl:value-of select="*[2]/*[local-name()='CONTA']/CONTA_NSGD/CONTA"/></NUMERO>
							<DV><xsl:value-of select="*[2]/*[local-name()='CONTA']/CONTA_NSGD/DV"/></DV>
						</CONTA_NSGD>
					</xsl:when>
				</xsl:choose>
				<DATA_INICIO><xsl:value-of select="*[2]/LISTA_TRANSACOES_PENDENTES_ASSINATURA/DATA_INICIO"/></DATA_INICIO>
				<DATA_FIM><xsl:value-of select="*[2]/LISTA_TRANSACOES_PENDENTES_ASSINATURA/DATA_FIM"/></DATA_FIM>
				<TOKEN><xsl:value-of select="*[2]/*[local-name()='TOKEN']/TOKEN" /></TOKEN>
				<SESSION_ID><xsl:value-of select="*[2]/*[local-name()='TOKEN']/ID_SESSAO" /></SESSION_ID>
				<ENDERECO_IP><xsl:value-of select="*[2]/HEADER/IDENTIFICADOR_ORIGEM/ENDERECO_IP"/></ENDERECO_IP>
			</lista_transacoes_pendentes:CONSULTA_ASSINATURA_ELETRONICA_ENTRADA>
		
		</multicanal:SERVICO_ENTRADA>
	</xsl:template>	
</xsl:stylesheet>