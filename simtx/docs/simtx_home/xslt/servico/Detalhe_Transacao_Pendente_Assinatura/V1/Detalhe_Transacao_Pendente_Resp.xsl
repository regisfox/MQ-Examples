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
	
	<xsl:param name="erro" />
	<xsl:param name="nsuSimtx" />
	<xsl:param name="nsuSiper" />
	<xsl:param name="dataTransacao" />
	<xsl:param name="dataPrevistaEfetivacao" />
	
	<xsl:param name="codRetorno" />
	<xsl:param name="acaoRetorno" />
	<xsl:param name="origemRetorno" />
	<xsl:param name="mensagemNegocial" />
	<xsl:param name="mensagemTecnica" />
	
	<xsl:template match="/BUSDATA">
	
		<ns:DETALHE_TRANSACAO_PENDENTE_ASSINATURA_SAIDA
			xmlns:ns="http://caixa.gov.br/simtx/detalhe_transacao_pendente_assinatura/v1/ns" 
			xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
			<HEADER>
				<SERVICO>
					<CODIGO><xsl:value-of select="*[2]/*[local-name()='HEADER']/SERVICO/CODIGO"/></CODIGO>
					<VERSAO><xsl:value-of select="*[2]/*[local-name()='HEADER']/SERVICO/VERSAO"/></VERSAO>
				</SERVICO>
				<CANAL>
					<SIGLA><xsl:value-of select="*[2]/*[local-name()='HEADER']/CANAL/SIGLA"/></SIGLA>
					<NSU><xsl:value-of select="*[2]/*[local-name()='HEADER']/CANAL/NSU"/></NSU>
					<DATAHORA><xsl:value-of select="*[2]/*[local-name()='HEADER']/CANAL/DATAHORA"/></DATAHORA>
				</CANAL>
				<MEIOENTRADA><xsl:value-of select="*[2]/*[local-name()='HEADER']/MEIOENTRADA"/></MEIOENTRADA>
				<xsl:choose>
					<xsl:when test="*[2]/*[local-name()='HEADER']/IDENTIFICADOR_ORIGEM/TERMINAL">
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
				<USUARIO_SERVICO><xsl:value-of select="*[2]/*[local-name()='HEADER']/USUARIO_SERVICO"/></USUARIO_SERVICO>
				<USUARIO><xsl:value-of select="*[2]/*[local-name()='HEADER']/USUARIO"/></USUARIO>
				<DATA_REFERENCIA><xsl:value-of select="*[2]/*[local-name()='HEADER']/DATA_REFERENCIA"/></DATA_REFERENCIA>
				<NSUMTX><xsl:value-of select="$nsuSimtx"/></NSUMTX>
				<COD_RETORNO><xsl:value-of select="$codRetorno"/></COD_RETORNO>
				<ACAO_RETORNO><xsl:value-of select="$acaoRetorno"/></ACAO_RETORNO>
				<ORIGEM_RETORNO><xsl:value-of select="$origemRetorno"/></ORIGEM_RETORNO>
				<MENSAGENS>
					<NEGOCIAL><xsl:value-of select="$mensagemNegocial"/></NEGOCIAL>
					<TECNICA><xsl:value-of select="$mensagemTecnica"/></TECNICA>
				</MENSAGENS>
			</HEADER>

			<xsl:if test="$erro != ''">
				<DETALHE_TRANSACA_PENDENTE_ASSINATURA>
					<NSU_MTX><xsl:value-of select="$nsuSimtx" /></NSU_MTX>	
					<NSU_SIPER><xsl:value-of select="$nsuSiper" /></NSU_SIPER>
					<DATA_TRANSACAO><xsl:value-of select="$dataTransacao" /></DATA_TRANSACAO>
					<DATA_PREVISTA_EFETIVACAO><xsl:value-of select="$dataPrevistaEfetivacao" /></DATA_PREVISTA_EFETIVACAO>
				</DETALHE_TRANSACA_PENDENTE_ASSINATURA>
			
			
				<xsl:if test="*[local-name()='CONTRA_ORDEM_CHEQUE_ENTRADA']">
					<xsl:apply-templates mode="copy" select="*[local-name()='CONTRA_ORDEM_CHEQUE_ENTRADA']/CONTRA_ORDEM_CHEQUE" />
				</xsl:if>
			
			</xsl:if>
			
		</ns:DETALHE_TRANSACAO_PENDENTE_ASSINATURA_SAIDA>
	</xsl:template>
</xsl:stylesheet>