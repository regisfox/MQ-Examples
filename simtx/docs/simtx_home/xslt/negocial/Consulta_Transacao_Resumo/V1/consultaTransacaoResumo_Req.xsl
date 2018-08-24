<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
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
			xmlns:resumo="http://caixa.gov.br/sibar/consulta_transacoes_conta/resumo"
			xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" 
			xsi:schemaLocation="http://caixa.gov.br/simtx/multicanal/orquestracao Multicanal_Orquestracao.xsd">
			<sibar_base:HEADER>
				<VERSAO>1.0</VERSAO>
				<xsl:if test="*[2]/*[local-name()='HEADER']/USUARIO_SERVICO/text() != ''">
					<USUARIO_SERVICO>
						<xsl:value-of select="*[2]/*[local-name()='HEADER']/USUARIO_SERVICO"/>
					</USUARIO_SERVICO>
				</xsl:if>
				<xsl:if test="*[2]/*[local-name()='HEADER']/USUARIO/text() != ''">
					<USUARIO>
						<xsl:value-of select="*[2]/*[local-name()='HEADER']/USUARIO"/>
					</USUARIO>
				</xsl:if>
				<OPERACAO>SERVICO_MIGRADO</OPERACAO>
				<SISTEMA_ORIGEM>
					<xsl:value-of select="*[2]/*[local-name()='HEADER']/CANAL/SIGLA"/>
				</SISTEMA_ORIGEM>
				<xsl:if test="*[2]/*[local-name()='HEADER']/IDENTIFICADOR_ORIGEM/text() != ''">
					<xsl:choose>
						<xsl:when test="*[2]/*[local-name()='HEADER']/IDENTIFICADOR_ORIGEM/TERMINAL">
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
			<resumo:CONSULTA_TRANSACOES_CONTA_ENTRADA>
				<CONTA>
					<xsl:choose>
						<xsl:when test="*[2]/CONTA/CONTA_SIDEC">
							<UNIDADE><xsl:value-of select="*[2]/CONTA/CONTA_SIDEC/UNIDADE" /></UNIDADE>
							<PRODUTO><xsl:value-of	select="*[2]/CONTA/CONTA_SIDEC/OPERACAO" /></PRODUTO>
							<NUMERO><xsl:value-of select="*[2]/CONTA/CONTA_SIDEC/CONTA" /></NUMERO>
							<DV><xsl:value-of select="*[2]/CONTA/CONTA_SIDEC/DV" /></DV>
						</xsl:when>
						<xsl:otherwise>
							<UNIDADE><xsl:value-of select="*[2]/CONTA/CONTA_NSGD/UNIDADE" /></UNIDADE>
							<PRODUTO><xsl:value-of select="*[2]/CONTA/CONTA_NSGD/PRODUTO" /></PRODUTO>
							<NUMERO><xsl:value-of select="*[2]/CONTA/CONTA_NSGD/CONTA" /></NUMERO>
							<DV><xsl:value-of select="*[2]/CONTA/CONTA_NSGD/DV" /></DV>
						</xsl:otherwise>
					</xsl:choose>
				</CONTA>
			    <PERIODO>
			      <INICIO><xsl:value-of select="*[2]/LISTA_COMPROVANTES_TRANSACOES/DATA_INICIO" /></INICIO>
			      <FIM><xsl:value-of select="*[2]/LISTA_COMPROVANTES_TRANSACOES/DATA_FIM" /></FIM>
			    </PERIODO>
			    <SOLICITACOES>
			      <SOLICITACAO>BOLETO</SOLICITACAO>
			    </SOLICITACOES>
			</resumo:CONSULTA_TRANSACOES_CONTA_ENTRADA>
		</multicanal:SERVICO_ENTRADA>
	</xsl:template>
</xsl:stylesheet>