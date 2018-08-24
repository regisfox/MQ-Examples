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
	
	<xsl:param name="pRedeTransmissora"/>
	<xsl:param name="erro" />
	<xsl:param name="nsuSimtx" />
	<xsl:param name="numeroConta"><xsl:value-of select="string-length(/BUSDATA/*[local-name()='SERVICO_SAIDA']/*[local-name()='VALIDA_PERMISSAO_SAIDA']/CONTA/NUMERO)"></xsl:value-of></xsl:param>
	
	
	<xsl:template match="/BUSDATA">
		<manutencaocobrancabancaria:MANUTENCAO_COBRANCA_BANCARIA_SAIDA>
			xmlns:header="http://caixa.gov.br/simtx/comuns/header" 
			xmlns:validapermissao="http://caixa.gov.br/simtx/meioentrada/valida_permissao"
			xmlns:consultacobrancabancaria="http://caixa.gov.br/simtx/negocial/consulta_cobranca_bancaria/pagamento"
			xmlns:comuns="http://caixa.gov.br/simtx/comuns/comuns"
			xmlns:pagamentoboleto="http://caixa.gov.br/simtx/pagamentoboleto"
			xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" 
			xsi:schemaLocation="http://caixa.gov.br/simtx/consultaboleto Consulta_Boleto_Pagamento.xsd ">
			<HEADER>
				<SERVICO>
					<CODIGO><xsl:value-of select="*[local-name()='PAGAMENTO_BOLETO_ENTRADA']/*[local-name()='HEADER']/SERVICO/CODIGO"/></CODIGO>
					<VERSAO><xsl:value-of select="*[local-name()='PAGAMENTO_BOLETO_ENTRADA']/*[local-name()='HEADER']/SERVICO/VERSAO"/></VERSAO>
				</SERVICO>
				<CANAL>
					<SIGLA><xsl:value-of select="*[local-name()='PAGAMENTO_BOLETO_ENTRADA']/*[local-name()='HEADER']/CANAL/SIGLA"/></SIGLA>
					<xsl:if test="*[local-name()='PAGAMENTO_BOLETO_ENTRADA']/*[local-name()='HEADER']/CANAL/NSU/text() != ''">
						<NSU><xsl:value-of select="*[local-name()='PAGAMENTO_BOLETO_ENTRADA']/*[local-name()='HEADER']/CANAL/NSU"/></NSU>
					</xsl:if>
					<DATAHORA><xsl:value-of select="*[local-name()='PAGAMENTO_BOLETO_ENTRADA']/*[local-name()='HEADER']/CANAL/DATAHORA"/></DATAHORA>
				</CANAL>
				<MEIOENTRADA><xsl:value-of select="*[local-name()='PAGAMENTO_BOLETO_ENTRADA']/*[local-name()='HEADER']/MEIOENTRADA"/></MEIOENTRADA>
				<xsl:if test="*[local-name()='PAGAMENTO_BOLETO_ENTRADA']/*[local-name()='HEADER']/IDENTIFICADOR_ORIGEM != ''">
					<xsl:choose>
						<xsl:when test="*[local-name()='PAGAMENTO_BOLETO_ENTRADA']/*[local-name()='HEADER']/IDENTIFICADOR_ORIGEM/TERMINAL">
							<UNIDADE>
								<xsl:value-of select="substring(*[local-name()='PAGAMENTO_BOLETO_ENTRADA']/*[local-name()='HEADER']/IDENTIFICADOR_ORIGEM/TERMINAL, 1, 4)"/>
							</UNIDADE>
							<IDENTIFICADOR_ORIGEM>
								<TERMINAL><xsl:value-of select="*[local-name()='PAGAMENTO_BOLETO_ENTRADA']/*[local-name()='HEADER']/IDENTIFICADOR_ORIGEM/TERMINAL"/></TERMINAL>										
							</IDENTIFICADOR_ORIGEM>
						</xsl:when>
						<xsl:when test="*[local-name()='PAGAMENTO_BOLETO_ENTRADA']/*[local-name()='HEADER']/IDENTIFICADOR_ORIGEM/ENDERECO_IP">
							<IDENTIFICADOR_ORIGEM>
								<ENDERECO_IP><xsl:value-of select="*[local-name()='PAGAMENTO_BOLETO_ENTRADA']/*[local-name()='HEADER']/IDENTIFICADOR_ORIGEM/ENDERECO_IP"/></ENDERECO_IP>
							</IDENTIFICADOR_ORIGEM>
						</xsl:when>
						<xsl:otherwise>
							<IDENTIFICADOR_ORIGEM>
								<CODIGO_MAQUINA><xsl:value-of select="*[local-name()='PAGAMENTO_BOLETO_ENTRADA']/*[local-name()='HEADER']/IDENTIFICADOR_ORIGEM/CODIGO_MAQUINA"/></CODIGO_MAQUINA>
							</IDENTIFICADOR_ORIGEM>
						</xsl:otherwise>
					</xsl:choose>
				</xsl:if>
				<xsl:if test="*[local-name()='PAGAMENTO_BOLETO_ENTRADA']/*[local-name()='HEADER']/USUARIO_SERVICO/text() != ''">
					<USUARIO_SERVICO><xsl:value-of select="*[local-name()='PAGAMENTO_BOLETO_ENTRADA']/*[local-name()='HEADER']/USUARIO_SERVICO"/></USUARIO_SERVICO>
				</xsl:if>
				<xsl:if test="*[local-name()='PAGAMENTO_BOLETO_ENTRADA']/*[local-name()='HEADER']/USUARIO/text() != ''">
					<USUARIO><xsl:value-of select="*[local-name()='PAGAMENTO_BOLETO_ENTRADA']/*[local-name()='HEADER']/USUARIO"/></USUARIO>
				</xsl:if>
				<DATA_REFERENCIA><xsl:value-of select="*[local-name()='PAGAMENTO_BOLETO_ENTRADA']/*[local-name()='HEADER']/DATA_REFERENCIA"/></DATA_REFERENCIA>
				<NSUMTX><xsl:value-of select="$nsuSimtx"/></NSUMTX>
				
				<COD_RETORNO><xsl:value-of select="$codRetorno"/></COD_RETORNO>
				<ACAO_RETORNO><xsl:value-of select="$acaoRetorno"/></ACAO_RETORNO>
				<ORIGEM_RETORNO><xsl:value-of select="$origemRetorno"/></ORIGEM_RETORNO>
				<MENSAGENS>
					<NEGOCIAL><xsl:value-of select="$mensagemNegocial"/></NEGOCIAL>
					<xsl:if test="$mensagemTecnica != '' and $mensagemTecnica != '-'">
						<TECNICA><xsl:value-of select="$mensagemTecnica"/></TECNICA>
					</xsl:if>
				</MENSAGENS>
			</HEADER>
			<CANCELAMENTO_BAIXA_OPERACIONAL>
				<xsl:choose>
					<xsl:when test="$erro = ''">
						<CANCELADO>S</CANCELADO>
					</xsl:when>
					<xsl:otherwise>
						<CANCELADO>N</CANCELADO>
					</xsl:otherwise>
				</xsl:choose>
			</CANCELAMENTO_BAIXA_OPERACIONAL>
		</manutencaocobrancabancaria:MANUTENCAO_COBRANCA_BANCARIA_SAIDA>
	</xsl:template>
</xsl:stylesheet>