<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

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
	
	<xsl:template match="/BUSDATA">
		<extrato:CONSULTA_EXTRATO_SAIDA
			xmlns:header="http://caixa.gov.br/simtx/comuns/header" 
			xmlns:comuns="http://caixa.gov.br/simtx/comuns/comuns"
			xmlns:extrato="http://caixa.gov.br/simtx/consulta_extrato/v1/ns"
			xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" 
			xsi:schemaLocation="http://caixa.gov.br/simtx/consultaExtrato Consulta_Extrato.xsd ">
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
			
			<xsl:if test="$erro = ''">		
				<xsl:if test="*[local-name()='SERVICO_SAIDA']/*[local-name()='VALIDA_CARTAO_SAIDA'] != ''">
					<CONTA>
						<xsl:choose>
							<xsl:when test="*[local-name()='SERVICO_SAIDA']/*[local-name()='VALIDA_CARTAO_SAIDA']/CARTAO_COMERCIAL/CONTA and *[local-name()='SERVICO_SAIDA']/*[local-name()='VALIDA_CARTAO_SAIDA']/CARTAO_COMERCIAL/CONTA_NSGD">
								<CONTA_NSGD>
									<UNIDADE><xsl:value-of select="*[local-name()='SERVICO_SAIDA']/*[local-name()='VALIDA_CARTAO_SAIDA']/CARTAO_COMERCIAL/CONTA_NSGD/UNIDADE"/></UNIDADE>
									<PRODUTO><xsl:value-of select="*[local-name()='SERVICO_SAIDA']/*[local-name()='VALIDA_CARTAO_SAIDA']/CARTAO_COMERCIAL/CONTA_NSGD/PRODUTO"/></PRODUTO>
									<CONTA><xsl:value-of select="*[local-name()='SERVICO_SAIDA']/*[local-name()='VALIDA_CARTAO_SAIDA']/CARTAO_COMERCIAL/CONTA_NSGD/NUMERO"/></CONTA>
									<DV><xsl:value-of select="*[local-name()='SERVICO_SAIDA']/*[local-name()='VALIDA_CARTAO_SAIDA']/CARTAO_COMERCIAL/CONTA_NSGD/DV"/></DV>
								</CONTA_NSGD>
							</xsl:when>
							<xsl:when test="*[local-name()='SERVICO_SAIDA']/*[local-name()='VALIDA_CARTAO_SAIDA']/CARTAO_COMERCIAL/CONTA">
								<CONTA_SIDEC>
									<UNIDADE><xsl:value-of select="*[local-name()='SERVICO_SAIDA']/*[local-name()='VALIDA_CARTAO_SAIDA']/CARTAO_COMERCIAL/CONTA/UNIDADE"/></UNIDADE>
									<OPERACAO><xsl:value-of select="*[local-name()='SERVICO_SAIDA']/*[local-name()='VALIDA_CARTAO_SAIDA']/CARTAO_COMERCIAL/CONTA/OPERACAO"/></OPERACAO>
									<CONTA><xsl:value-of select="*[local-name()='SERVICO_SAIDA']/*[local-name()='VALIDA_CARTAO_SAIDA']/CARTAO_COMERCIAL/CONTA/NUMERO"/></CONTA>
									<DV><xsl:value-of select="*[local-name()='SERVICO_SAIDA']/*[local-name()='VALIDA_CARTAO_SAIDA']/CARTAO_COMERCIAL/CONTA/DV"/></DV>
								</CONTA_SIDEC>
							</xsl:when>
							<xsl:otherwise>
								<CONTA_NSGD>
									<UNIDADE><xsl:value-of select="*[local-name()='SERVICO_SAIDA']/*[local-name()='VALIDA_CARTAO_SAIDA']/CARTAO_COMERCIAL/CONTA_NSGD/UNIDADE"/></UNIDADE>
									<PRODUTO><xsl:value-of select="*[local-name()='SERVICO_SAIDA']/*[local-name()='VALIDA_CARTAO_SAIDA']/CARTAO_COMERCIAL/CONTA_NSGD/PRODUTO"/></PRODUTO>
									<CONTA><xsl:value-of select="*[local-name()='SERVICO_SAIDA']/*[local-name()='VALIDA_CARTAO_SAIDA']/CARTAO_COMERCIAL/CONTA_NSGD/NUMERO"/></CONTA>
									<DV><xsl:value-of select="*[local-name()='SERVICO_SAIDA']/*[local-name()='VALIDA_CARTAO_SAIDA']/CARTAO_COMERCIAL/CONTA_NSGD/DV"/></DV>
								</CONTA_NSGD>
							</xsl:otherwise>
						</xsl:choose>
						<TITULAR_CONTA><xsl:value-of select="*[local-name()='SERVICO_SAIDA']/*[local-name()='VALIDA_CARTAO_SAIDA']/CARTAO_COMERCIAL/CONTA/TITULAR"/></TITULAR_CONTA>
					</CONTA>
					<CARTAO>
						<xsl:if test="*[local-name()='SERVICO_SAIDA']/*[local-name()='VALIDA_CARTAO_SAIDA']/CARTAO_COMERCIAL/CARTAO/VENCIMENTO/text() != ''">
							<VENCIMENTO><xsl:value-of select="*[local-name()='SERVICO_SAIDA']/*[local-name()='VALIDA_CARTAO_SAIDA']/CARTAO_COMERCIAL/CARTAO/VENCIMENTO"/></VENCIMENTO>
						</xsl:if>
						<xsl:if test="*[local-name()='SERVICO_SAIDA']/*[local-name()='VALIDA_CARTAO_SAIDA']/CARTAO_COMERCIAL/CARTAO/TIPO/text() != ''">
							<TIPO><xsl:value-of select="*[local-name()='SERVICO_SAIDA']/*[local-name()='VALIDA_CARTAO_SAIDA']/CARTAO_COMERCIAL/CARTAO/TIPO"/></TIPO>
						</xsl:if>
						<xsl:if test="*[local-name()='SERVICO_SAIDA']/*[local-name()='VALIDA_CARTAO_SAIDA']/CARTAO_COMERCIAL/CARTAO/SITUACAO/text() != ''">
							<SITUACAO><xsl:value-of select="*[local-name()='SERVICO_SAIDA']/*[local-name()='VALIDA_CARTAO_SAIDA']/CARTAO_COMERCIAL/CARTAO/SITUACAO"/></SITUACAO>
						</xsl:if>
						<xsl:if test="*[local-name()='SERVICO_SAIDA']/*[local-name()='VALIDA_CARTAO_SAIDA']/CARTAO_COMERCIAL/CARTAO/FLAG_ULTIMA_VIA/text() != ''">
							<FLAG_ULTIMA_VIA><xsl:value-of select="*[local-name()='SERVICO_SAIDA']/*[local-name()='VALIDA_CARTAO_SAIDA']/CARTAO_COMERCIAL/CARTAO/FLAG_ULTIMA_VIA"/></FLAG_ULTIMA_VIA>
						</xsl:if>
						<xsl:if test="*[local-name()='SERVICO_SAIDA']/*[local-name()='VALIDA_CARTAO_SAIDA']/CARTAO_COMERCIAL/CARTAO/QUANTIDADE_UR/text() != ''">
							<QUANTIDADE_UR><xsl:value-of select="*[local-name()='SERVICO_SAIDA']/*[local-name()='VALIDA_CARTAO_SAIDA']/CARTAO_COMERCIAL/CARTAO/QUANTIDADE_UR"/></QUANTIDADE_UR>
						</xsl:if>
						<xsl:if test="*[local-name()='SERVICO_SAIDA']/*[local-name()='VALIDA_CARTAO_SAIDA']/CARTAO_COMERCIAL/CARTAO/FLAG_CHIP/text() != ''">
							<FLAG_CHIP><xsl:value-of select="*[local-name()='SERVICO_SAIDA']/*[local-name()='VALIDA_CARTAO_SAIDA']/CARTAO_COMERCIAL/CARTAO/FLAG_CHIP"/></FLAG_CHIP>
						</xsl:if>
						<xsl:if test="*[local-name()='SERVICO_SAIDA']/*[local-name()='VALIDA_CARTAO_SAIDA']/CARTAO_COMERCIAL/CARTAO/FLAG_CVV2/text() != ''">
							<FLAG_CVV2><xsl:value-of select="*[local-name()='SERVICO_SAIDA']/*[local-name()='VALIDA_CARTAO_SAIDA']/CARTAO_COMERCIAL/CARTAO/FLAG_CVV2"/></FLAG_CVV2>
						</xsl:if>
						<xsl:if test="*[local-name()='SERVICO_SAIDA']/*[local-name()='VALIDA_CARTAO_SAIDA']/CARTAO_COMERCIAL/CARTAO/BANDEIRA/text() != ''">
							<BANDEIRA><xsl:value-of select="*[local-name()='SERVICO_SAIDA']/*[local-name()='VALIDA_CARTAO_SAIDA']/CARTAO_COMERCIAL/CARTAO/BANDEIRA"/></BANDEIRA>
						</xsl:if>
					</CARTAO>
				</xsl:if>
				
				<CONSULTA_EXTRATO>
					<xsl:apply-templates mode="copy" select="*[local-name()='SERVICO_SAIDA']/*[local-name()='CONSULTA_EXTRATO_SAIDA']/DADOS_CADASTRAIS" disable-output-escaping="yes"/>
					<xsl:apply-templates mode="copy" select="*[local-name()='SERVICO_SAIDA']/*[local-name()='CONSULTA_EXTRATO_SAIDA']/LANCAMENTOS" disable-output-escaping="yes"/>
					<xsl:apply-templates mode="copy" select="*[local-name()='SERVICO_SAIDA']/*[local-name()='CONSULTA_EXTRATO_SAIDA']/SALDOS" disable-output-escaping="yes"/>
					<xsl:apply-templates mode="copy" select="*[local-name()='SERVICO_SAIDA']/*[local-name()='CONSULTA_EXTRATO_SAIDA']/COMPRAS" disable-output-escaping="yes"/>
					<xsl:apply-templates mode="copy" select="*[local-name()='SERVICO_SAIDA']/*[local-name()='CONSULTA_EXTRATO_SAIDA']/AGENDAMENTOS" disable-output-escaping="yes"/>
					<xsl:apply-templates mode="copy" select="*[local-name()='SERVICO_SAIDA']/*[local-name()='CONSULTA_EXTRATO_SAIDA']/VALORES_BLOQUEADOS" disable-output-escaping="yes"/>
					<xsl:apply-templates mode="copy" select="*[local-name()='SERVICO_SAIDA']/*[local-name()='CONSULTA_EXTRATO_SAIDA']/SALDO_DATA_LIMITE" disable-output-escaping="yes"/>
					<xsl:apply-templates mode="copy" select="*[local-name()='SERVICO_SAIDA']/*[local-name()='CONSULTA_EXTRATO_SAIDA']/LANCAMENTOS_DIA" disable-output-escaping="yes"/>
				</CONSULTA_EXTRATO>
				
			</xsl:if>
		</extrato:CONSULTA_EXTRATO_SAIDA>
	</xsl:template>
</xsl:stylesheet>