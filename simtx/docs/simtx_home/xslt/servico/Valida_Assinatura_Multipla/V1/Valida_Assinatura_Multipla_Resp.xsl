<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0"
	xmlns:manutencaocheque="http://caixa.gov.br/sibar/manutencao_cheque/contraordem" 
	xmlns:sibar_base="http://caixa.gov.br/sibar"
	xmlns:assinaturamultipla="http://caixa.gov.br/sibar/valida_permissao/assinatura_multipla"
	xmlns:assinaturasimples="http://caixa.gov.br/sibar/valida_permissao/assinatura_simples" 
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" 
	xsi:schemaLocation="http://caixa.gov.br/simtx/multicanal/orquestracao Multicanal_Orquestracao.xsd"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

	<xsl:output omit-xml-declaration="yes"/>
	<xsl:strip-space elements="*" />
	
	<xsl:template match="*" mode="copy">
	  <xsl:element name="{name()}" namespace="{namespace-uri()}">
	    <xsl:apply-templates select="@*|node()" mode="copy" />
	  </xsl:element>
	</xsl:template>
	
	<xsl:template match="@*|text()|comment()" mode="copy">
	  <xsl:copy/>
	</xsl:template>
	
	
	<xsl:param name="codRetorno" />
	<xsl:param name="acaoRetorno" />
	<xsl:param name="origemRetorno" />
	<xsl:param name="mensagemNegocial" />
	<xsl:param name="mensagemTecnica" />
	
	<xsl:param name="erro" />
	<xsl:param name="nsuSimtx" />
	<xsl:param name="numeroConta"><xsl:value-of select="string-length(/BUSDATA/*[local-name()='SERVICO_SAIDA']/*[local-name()='VALIDA_PERMISSAO_SAIDA']/CONTA/NUMERO)"></xsl:value-of></xsl:param>
	<xsl:param name="inibirNegocial" />
	
	
	<xsl:template match="/BUSDATA">
		<ns:ASSINATURA_MULTIPLA_SAIDA  
			xmlns:ns="http://caixa.gov.br/simtx/valida_assinatura_multipla/v1/ns" 
			xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" 
			xsi:schemaLocation="http://caixa.gov.br/simtx/multicanal/orquestracao Multicanal_Orquestracao.xsd">
			<HEADER>
				<SERVICO>
					<CODIGO><xsl:value-of select="*[2]/*[local-name()='HEADER']/SERVICO/CODIGO"/></CODIGO>
					<VERSAO><xsl:value-of select="*[2]/*[local-name()='HEADER']/SERVICO/VERSAO"/></VERSAO>
				</SERVICO>
				<CANAL>
					<SIGLA><xsl:value-of select="*[2]/*[local-name()='HEADER']/CANAL/SIGLA"/></SIGLA>
					<xsl:if test="*[2]/*[local-name()='HEADER']/CANAL/NSU/text() != ''">
						<NSU><xsl:value-of select="*[2]/*[local-name()='HEADER']/CANAL/NSU"/></NSU>
					</xsl:if>
					<DATAHORA><xsl:value-of select="*[2]/*[local-name()='HEADER']/CANAL/DATAHORA"/></DATAHORA>
				</CANAL>
				<MEIOENTRADA><xsl:value-of select="*[2]/*[local-name()='HEADER']/MEIOENTRADA"/></MEIOENTRADA>
				<xsl:if test="*[2]/*[local-name()='HEADER']/IDENTIFICADOR_ORIGEM != ''">
					<xsl:choose>
						<xsl:when test="*[2]/*[local-name()='HEADER']/IDENTIFICADOR_ORIGEM/TERMINAL">
							<UNIDADE>
								<xsl:value-of select="*[2]/*[local-name()='HEADER']/IDENTIFICADOR_ORIGEM/TERMINAL"/>
							</UNIDADE>
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
				</xsl:if>
				<xsl:if test="*[2]/*[local-name()='HEADER']/USUARIO_SERVICO/text() != ''">
					<USUARIO_SERVICO><xsl:value-of select="*[2]/*[local-name()='HEADER']/USUARIO_SERVICO"/></USUARIO_SERVICO>
				</xsl:if>
				<xsl:if test="*[2]/*[local-name()='HEADER']/USUARIO/text() != ''">
					<USUARIO><xsl:value-of select="*[2]/*[local-name()='HEADER']/USUARIO"/></USUARIO>
				</xsl:if>
				<DATA_REFERENCIA><xsl:value-of select="*[2]/*[local-name()='HEADER']/DATA_REFERENCIA"/></DATA_REFERENCIA>
				<NSUMTX><xsl:value-of select="$nsuSimtx"/></NSUMTX>
				
				<COD_RETORNO><xsl:value-of select="$codRetorno"/></COD_RETORNO>
				<ACAO_RETORNO><xsl:value-of select="$acaoRetorno"/></ACAO_RETORNO>
				<ORIGEM_RETORNO><xsl:value-of select="$origemRetorno"/></ORIGEM_RETORNO>
				<MENSAGENS>
					<NEGOCIAL><xsl:value-of select="$mensagemNegocial"/></NEGOCIAL>
					<xsl:if test="$mensagemTecnica != ''">
						<TECNICA><xsl:value-of select="$mensagemTecnica"/></TECNICA>
					</xsl:if>
				</MENSAGENS>
			</HEADER>
			
			<TOKEN>
				<TOKEN_AUTENTICACAO><xsl:value-of select="*[local-name()='SERVICO_SAIDA']/*[local-name()='VALIDA_PERMISSAO_SAIDA']/TOKEN_AUTENTICACAO"/> </TOKEN_AUTENTICACAO>
			</TOKEN>
			
			<xsl:apply-templates />
			
			<xsl:if test="$inibirNegocial != 'true'">
				<CONTRA_ORDEM_CHEQUE>
					<NUMERO_CHEQUE>
						<INICIO><xsl:value-of select="*[2]/*[local-name()='CONTRA_ORDEM_CHEQUE']/NUMERO_CHEQUE/INICIO" /></INICIO>
						<xsl:if test="*[2]/*[local-name()='CONTRA_ORDEM_CHEQUE']/NUMERO_CHEQUE/FIM">
							<FIM><xsl:value-of select="*[2]/*[local-name()='CONTRA_ORDEM_CHEQUE']/NUMERO_CHEQUE/FIM" /></FIM>
						</xsl:if>
					</NUMERO_CHEQUE>
					<CODIGO_MOTIVO><xsl:value-of select="*[2]/*[local-name()='CONTRA_ORDEM_CHEQUE']/CODIGO_MOTIVO" /></CODIGO_MOTIVO>
					<TIPO_COMANDO><xsl:value-of select="*[2]/*[local-name()='CONTRA_ORDEM_CHEQUE']/TIPO_COMANDO" /></TIPO_COMANDO>
					<JUSTIFICATIVA><xsl:value-of select="*[2]/*[local-name()='CONTRA_ORDEM_CHEQUE']/JUSTIFICATIVA" /></JUSTIFICATIVA>
					
					<ACATADO><xsl:value-of select="/BUSDATA/*[local-name()='SERVICO_SAIDA']/*[local-name()='CONTRA_ORDEM_CHEQUE_SAIDA']/CONTRA_ORDEM_CHEQUE/ACATADO" /></ACATADO>
				</CONTRA_ORDEM_CHEQUE>
			</xsl:if>
			
		</ns:ASSINATURA_MULTIPLA_SAIDA>
	</xsl:template>
	
	<xsl:template match="*[local-name()='HEADER']" />
	<xsl:template match="*[local-name()='ASSINATURA_MULTIPLA_ENTRADA']" />
	<xsl:template match="COD_RETORNO" />
	<xsl:template match="ORIGEM_RETORNO" />
	<xsl:template match="MSG_RETORNO" />
	<xsl:template match="BUSTIME" />
	
	<xsl:template match="*[local-name()='VALIDA_PERMISSAO_SAIDA']">
		<ASSINATURA_MULTIPLA>
			<CLASSIFICACAO_ASSINATURA><xsl:value-of select="/BUSDATA/*[local-name()='SERVICO_SAIDA']/*[local-name()='VALIDA_PERMISSAO_SAIDA']/ASSINATURA/CLASSIFICACAO"/></CLASSIFICACAO_ASSINATURA>
			<ASSINANTES>
				<xsl:apply-templates mode="copy" select="/BUSDATA/*[local-name()='SERVICO_SAIDA']/*[local-name()='VALIDA_PERMISSAO_SAIDA']/ASSINANTES/*" />
			</ASSINANTES>
		</ASSINATURA_MULTIPLA>
	</xsl:template>
	
</xsl:stylesheet>