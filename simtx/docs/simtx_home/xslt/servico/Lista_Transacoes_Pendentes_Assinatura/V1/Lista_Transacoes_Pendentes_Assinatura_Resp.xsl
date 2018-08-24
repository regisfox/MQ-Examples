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
	
	<xsl:param name="codRetorno" />
	<xsl:param name="acaoRetorno" />
	<xsl:param name="origemRetorno" />
	<xsl:param name="mensagemNegocial" />
	<xsl:param name="mensagemTecnica" />
	
	<xsl:param name="erro" />
	<xsl:param name="nsuSimtx" />
	
	<xsl:template match="/BUSDATA">
	
		<ns:LISTA_TRANSACOES_PENDENTES_ASSINATURA_SAIDA 
			xmlns:ns="http://caixa.gov.br/simtx/lista_transacoes_pendentes_assinatura/v1/ns" 
			xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" 
			xsi:schemaLocation="http://caixa.gov.br/simtx/lista_transacoes_pendentes_assinatura/v1/ns Lista_Transacoes_Pendentes_Assinatura.xsd ">
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
			
			<xsl:if test="$erro = ''">
				<LISTA_TRANSACOES_PENDENTES_ASSINATURA>
					<TRANSACOES>
						<!-- TRANSACOES SIAUT -->
						<xsl:for-each select="*[local-name()='SERVICO_SAIDA']/*[local-name()='CONSULTA_ASSINATURA_ELETRONICA_SAIDA']/TRANSACOES/*">
							<xsl:if test="./ORIGEM/SISTEMA = 'SIAUT'">
								<TRANSACAO>
									<NSU_SIPER><xsl:value-of select="./NSU_SIPER"/></NSU_SIPER>
									<DATA_TRANSACAO><xsl:value-of select="./DATA"/></DATA_TRANSACAO>
									<DATA_PREVISTA_EFETIVACAO><xsl:value-of select="./DATA_PREVISTA_EFETIVACAO"/></DATA_PREVISTA_EFETIVACAO>
									<SITUACAO>PENDENTE</SITUACAO>
									<SISTEMA_ORIGEM>
										<SIAUT>
											<NSU><xsl:value-of select="./ORIGEM/NSU"/></NSU>
											<RESUMO><xsl:value-of select="./RESUMO"/></RESUMO>
										</SIAUT>
									</SISTEMA_ORIGEM>
								</TRANSACAO>
							</xsl:if>
						</xsl:for-each>
						<!-- TRANSACOES SIMTX -->
						<xsl:apply-templates mode="copy" select="*[local-name()='TRANSACOES']/*" disable-output-escaping="no" />
					</TRANSACOES>
				</LISTA_TRANSACOES_PENDENTES_ASSINATURA>
			</xsl:if>	
						
		</ns:LISTA_TRANSACOES_PENDENTES_ASSINATURA_SAIDA>
	</xsl:template>
	
</xsl:stylesheet>