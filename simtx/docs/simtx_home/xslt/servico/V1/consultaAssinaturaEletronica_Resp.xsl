<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:consultaassinaturaeletronica="http://caixa.gov.br/sibar/consulta_assinatura_eletronica/lista_transacoes_pendentes" 
	xmlns:sibar_base="http://caixa.gov.br/sibar">

	<xsl:output omit-xml-declaration="yes" indent="yes" />

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
	<xsl:param name="param1" />

	<xsl:template match="/BUSDATA">
		<consultaassinaturaeletronica:SERVICO_SAIDA>
			<sibar_base:HEADER>
				<VERSAO>
					<xsl:value-of select="*[local-name()='SERVICO_ENTRADA']/*[local-name()='HEADER']/VERSAO" />
				</VERSAO>
				<OPERACAO>
					<xsl:value-of select="*[local-name()='SERVICO_ENTRADA']/*[local-name()='HEADER']/OPERACAO" />
				</OPERACAO>
				<SISTEMA_ORIGEM>
					<xsl:value-of select="*[local-name()='SERVICO_ENTRADA']/*[local-name()='HEADER']/SISTEMA_ORIGEM" />
				</SISTEMA_ORIGEM>
				<DATA_HORA>
					<xsl:value-of select="BUSTIME/ANO" />
					<xsl:value-of select="BUSTIME/MES" />
					<xsl:value-of select="BUSTIME/DIA" />
					<xsl:value-of select="BUSTIME/HORA" />
					<xsl:value-of select="BUSTIME/MIN" />
					<xsl:value-of select="BUSTIME/SEG" />
				</DATA_HORA>
			</sibar_base:HEADER>
			<COD_RETORNO>
				<xsl:value-of select="$codRetorno" />
			</COD_RETORNO>
			<ORIGEM_RETORNO>
				<xsl:value-of select="$origemRetorno" />
			</ORIGEM_RETORNO>
			<MSG_RETORNO>
				<xsl:value-of select="$msgRetorno" />
			</MSG_RETORNO>
			<xsl:choose>
				<xsl:when test="RESPOSTA/CONSULTA_ASSINATURA_ELETRONICA/DADOS">
					<DADOS>
						<xsl:copy-of select="RESPOSTA/CONSULTA_ASSINATURA_ELETRONICA/DADOS/CONTROLE_NEGOCIAL" />
						<xsl:choose>
							<xsl:when test="RESPOSTA/CONSULTA_ASSINATURA_ELETRONICA/DADOS/TRANSACOES">
								<TRANSACOES>
									<xsl:copy-of select="SIAUT_ASSINATURAS/*" />
									<xsl:copy-of select="SIMTX_ASSINATURAS/*" />
								</TRANSACOES>
							</xsl:when>
						</xsl:choose>
					</DADOS>
				</xsl:when>
			</xsl:choose>
		</consultaassinaturaeletronica:SERVICO_SAIDA>
	</xsl:template>

</xsl:stylesheet>