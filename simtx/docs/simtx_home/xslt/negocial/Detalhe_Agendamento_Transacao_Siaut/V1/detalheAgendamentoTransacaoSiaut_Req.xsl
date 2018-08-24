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

	<xsl:variable name="nuContaSidec"><xsl:value-of select="/BUSDATA/*[2]/CONTA/CONTA_SIDEC/UNIDADE" /></xsl:variable>
	<xsl:variable name="nuContaNsgd"><xsl:value-of select="/BUSDATA/*[2]/CONTA/CONTA_NSGD/UNIDADE" /></xsl:variable>
		
	<xsl:template match="/BUSDATA">
		<multicanal:SERVICO_ENTRADA 
			xmlns:multicanal="http://caixa.gov.br/sibar/multicanal/orquestracao" 
			xmlns:sibar_base="http://caixa.gov.br/sibar" 
			xmlns:detalhe="http://caixa.gov.br/sibar/consulta_transacoes_conta/detalhe" 
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
				<OPERACAO>SERVICO_MIGRADO_DetalheBoleto</OPERACAO>
				<SISTEMA_ORIGEM>
					<xsl:value-of select="*[2]/*[local-name()='HEADER']/CANAL/SIGLA"/>
				</SISTEMA_ORIGEM>
				<xsl:choose>
					<xsl:when test="$nuContaSidec != ''">
						<UNIDADE>
							<xsl:value-of select="substring($nuContaSidec, 1, 4)"/>
						</UNIDADE>
					</xsl:when>
					<xsl:when test="$nuContaNsgd != ''">
						<UNIDADE>
							<xsl:value-of select="substring($nuContaNsgd, 1, 4)"/>
						</UNIDADE>
					</xsl:when>
					<xsl:when test="*[2]/*[local-name()='HEADER']/IDENTIFICADOR_ORIGEM/TERMINAL">
						<UNIDADE>
							<xsl:value-of select="substring(*[2]/*[local-name()='HEADER']/IDENTIFICADOR_ORIGEM/TERMINAL, 1, 4)"/>
						</UNIDADE>
					</xsl:when>
				</xsl:choose>
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
			
			<detalhe:CONSULTA_TRANSACOES_CONTA_ENTRADA>
				<xsl:choose>
					<xsl:when test="*[2]/*[local-name()='CONTA']/CONTA_SIDEC">
						<CONTA>
							<UNIDADE><xsl:value-of select="*[2]/*[local-name()='CONTA']/CONTA_SIDEC/UNIDADE"/></UNIDADE>
							<OPERACAO><xsl:value-of select="*[2]/*[local-name()='CONTA']/CONTA_SIDEC/OPERACAO"/></OPERACAO>
							<NUMERO><xsl:value-of select="*[2]/*[local-name()='CONTA']/CONTA_SIDEC/CONTA"/></NUMERO>
							<DV><xsl:value-of select="*[2]/*[local-name()='CONTA']/CONTA_SIDEC/DV"/></DV>
						</CONTA>
					</xsl:when>
					<xsl:otherwise>
						<CONTA>
							<UNIDADE><xsl:value-of select="*[2]/*[local-name()='CONTA']/CONTA_NSGD/UNIDADE"/></UNIDADE>
							<OPERACAO><xsl:value-of select="*[2]/*[local-name()='CONTA']/CONTA_NSGD/PRODUTO"/></OPERACAO>
							<NUMERO><xsl:value-of select="*[2]/*[local-name()='CONTA']/CONTA_NSGD/CONTA"/></NUMERO>
							<DV><xsl:value-of select="*[2]/*[local-name()='CONTA']/CONTA_NSGD/DV"/></DV>
						</CONTA>
					</xsl:otherwise>
				</xsl:choose>
				<PERIODO>
					<INICIO><xsl:value-of select="*[2]/DETALHE_AGENDAMENTO_TRANSACAO/DATA_EFETIVACAO"/></INICIO>
					<FIM><xsl:value-of select="*[2]/DETALHE_AGENDAMENTO_TRANSACAO/DATA_EFETIVACAO"/></FIM>
				</PERIODO>
				<NSU><xsl:value-of select="*[2]/DETALHE_AGENDAMENTO_TRANSACAO/NSU"/></NSU>
				<ACAO>CONSULTAR</ACAO>
				<SOLICITACOES>
					<SOLICITACAO>BOLETO</SOLICITACAO>
				</SOLICITACOES>
			</detalhe:CONSULTA_TRANSACOES_CONTA_ENTRADA>
		</multicanal:SERVICO_ENTRADA>
	</xsl:template>
</xsl:stylesheet>