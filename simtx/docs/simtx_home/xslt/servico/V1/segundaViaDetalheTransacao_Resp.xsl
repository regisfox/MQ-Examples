<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:segundaviacomprovante="http://caixa.gov.br/sibar/segunda_via_comprovante/detalhe_transacao" 
	xmlns:validacartao="http://caixa.gov.br/sibar/validacartao" 
	xmlns:sibar_base="http://caixa.gov.br/sibar">


	<xsl:output method="xml" standalone="yes" encoding="utf-8" />

	<xsl:template match="node()|@*">
		<xsl:copy>
			<xsl:apply-templates select="node()|@*" />
		</xsl:copy>
	</xsl:template>

	<xsl:param name="nameSpace1" />
	<xsl:param name="nameSpace2" />
	<xsl:param name="erro" />
	<xsl:param name="codRetorno" />
	<xsl:param name="origemRetorno" />
	<xsl:param name="msgRetorno" />
	
	
	<xsl:template match="/">
		<segundaviacomprovante:SERVICO_SAIDA>
			<sibar_base:HEADER>
				<VERSAO>
					<xsl:value-of select="/BUSDATA/*[local-name()='SERVICO_ENTRADA']/sibar_base:HEADER/VERSAO"/>
				</VERSAO>
				<xsl:choose>
					<xsl:when test="/BUSDATA/*[local-name()='SERVICO_ENTRADA']/sibar_base:HEADER/AUTENTICACAO">
						<AUTENTICACAO>
							<xsl:value-of select="/BUSDATA/*[local-name()='SERVICO_ENTRADA']/sibar_base:HEADER/AUTENTICACAO"/>
						</AUTENTICACAO>
					</xsl:when>
				</xsl:choose>
				<xsl:choose>
					<xsl:when test="/BUSDATA/*[local-name()='SERVICO_ENTRADA']/sibar_base:HEADER/USUARIO_SERVICO">
						<USUARIO_SERVICO>
							<xsl:value-of select="/BUSDATA/*[local-name()='SERVICO_ENTRADA']/sibar_base:HEADER/USUARIO_SERVICO"/>
						</USUARIO_SERVICO>
					</xsl:when>
				</xsl:choose>
				<xsl:choose>
					<xsl:when test="/BUSDATA/*[local-name()='SERVICO_ENTRADA']/sibar_base:HEADER/USUARIO">
						<USUARIO>
							<xsl:value-of select="/BUSDATA/*[local-name()='SERVICO_ENTRADA']/sibar_base:HEADER/USUARIO"/>
						</USUARIO>
					</xsl:when>
				</xsl:choose>
				<OPERACAO>
					<xsl:value-of select="/BUSDATA/*[local-name()='SERVICO_ENTRADA']/sibar_base:HEADER/OPERACAO"/>
				</OPERACAO>
				<xsl:choose>
					<xsl:when test="/BUSDATA/*[local-name()='SERVICO_ENTRADA']/sibar_base:HEADER/INDICE">
						<INDICE>
							<xsl:value-of select="/BUSDATA/*[local-name()='SERVICO_ENTRADA']/sibar_base:HEADER/INDICE"/>
						</INDICE>
					</xsl:when>
				</xsl:choose>
				<xsl:choose>
					<xsl:when test="/BUSDATA/*[local-name()='SERVICO_ENTRADA']/sibar_base:HEADER/SISTEMA_ORIGEM">
						<SISTEMA_ORIGEM>
							<xsl:value-of select="/BUSDATA/*[local-name()='SERVICO_ENTRADA']/sibar_base:HEADER/SISTEMA_ORIGEM"/>
						</SISTEMA_ORIGEM>
					</xsl:when>
				</xsl:choose>
				<xsl:choose>
					<xsl:when test="/BUSDATA/*[local-name()='SERVICO_ENTRADA']/sibar_base:HEADER/UNIDADE">
						<UNIDADE>
							<xsl:value-of select="/BUSDATA/*[local-name()='SERVICO_ENTRADA']/sibar_base:HEADER/UNIDADE"/>
						</UNIDADE>
					</xsl:when>
				</xsl:choose>
				<xsl:choose>
					<xsl:when test="/BUSDATA/*[local-name()='SERVICO_ENTRADA']/sibar_base:HEADER/IDENTIFICADOR_ORIGEM">
						<IDENTIFICADOR_ORIGEM>
							<xsl:value-of select="/BUSDATA/*[local-name()='SERVICO_ENTRADA']/sibar_base:HEADER/IDENTIFICADOR_ORIGEM"/>
						</IDENTIFICADOR_ORIGEM>
					</xsl:when>
				</xsl:choose>
				<DATA_HORA>
					<xsl:value-of select="BUSDATA/BUSTIME/ANO" />
					<xsl:value-of select="BUSDATA/BUSTIME/MES" />
					<xsl:value-of select="BUSDATA/BUSTIME/DIA" />
					<xsl:value-of select="BUSDATA/BUSTIME/HORA" />
					<xsl:value-of select="BUSDATA/BUSTIME/MIN" />
					<xsl:value-of select="BUSDATA/BUSTIME/SEG" />
				</DATA_HORA>
				<xsl:choose>
					<xsl:when test="/BUSDATA/*[local-name()='SERVICO_ENTRADA']/sibar_base:HEADER/ID_PROCESSO">
						<ID_PROCESSO>
							<xsl:value-of select="/BUSDATA/*[local-name()='SERVICO_ENTRADA']/sibar_base:HEADER/ID_PROCESSO"/>
						</ID_PROCESSO>
					</xsl:when>
				</xsl:choose>
			</sibar_base:HEADER>
			<xsl:choose>
				<xsl:when test="$erro = ''">
					<COD_RETORNO>
						<xsl:value-of select="/BUSDATA/RESPOSTA/SEGUNDA_VIA_DETALHE_TRANSACAO/COD_RETORNO"/>
					</COD_RETORNO>
					<ORIGEM_RETORNO>
						<xsl:value-of select="/BUSDATA/RESPOSTA/SEGUNDA_VIA_DETALHE_TRANSACAO/ORIGEM_RETORNO"/>
					</ORIGEM_RETORNO>
					<MSG_RETORNO>
						<xsl:value-of select="/BUSDATA/RESPOSTA/SEGUNDA_VIA_DETALHE_TRANSACAO/MSG_RETORNO"/>
					</MSG_RETORNO>
				</xsl:when>
				<xsl:otherwise>
					<COD_RETORNO>
						<xsl:value-of select="$codRetorno"/>
					</COD_RETORNO>
					<ORIGEM_RETORNO>
						<xsl:value-of select="$origemRetorno"/>
					</ORIGEM_RETORNO>
					<MSG_RETORNO>
						<xsl:value-of select="$msgRetorno"/>
					</MSG_RETORNO>
				</xsl:otherwise>
			</xsl:choose>
			<VALIDA_SENHA>
         		<xsl:copy-of select="/BUSDATA/RESPOSTA/VALIDA_SENHA/DADOS/*"/>
         	</VALIDA_SENHA>
			<DADOS>
				<xsl:copy-of select="/BUSDATA/RESPOSTA/SEGUNDA_VIA_DETALHE_TRANSACAO/DADOS/*"/>
			</DADOS>
		</segundaviacomprovante:SERVICO_SAIDA>
	</xsl:template>
</xsl:stylesheet>