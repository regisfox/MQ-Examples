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
	
	
	<xsl:template match="/BUSDATA">
		<pagamentoboleto:PAGAMENTO_BOLETO_SAIDA
			xmlns:header="http://caixa.gov.br/simtx/comuns/header" 
			xmlns:validapermissao="http://caixa.gov.br/simtx/meioentrada/valida_permissao"
			xmlns:consultacobrancabancaria="http://caixa.gov.br/simtx/negocial/consulta_cobranca_bancaria/pagamento"
			xmlns:comuns="http://caixa.gov.br/simtx/comuns/comuns"
			xmlns:pagamentoboleto="http://caixa.gov.br/simtx/pagamento_boleto/v1/ns"
			xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" 
			xsi:schemaLocation="http://caixa.gov.br/simtx/consultaboleto Consulta_Boleto_Pagamento.xsd ">
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
				<AGENDAMENTO><xsl:value-of select="*[2]/AGENDAMENTO"/></AGENDAMENTO>
				<CPF><xsl:value-of select="*[2]/CPF"/></CPF>
				<xsl:apply-templates mode="copy" select="*[2]/CONTA" disable-output-escaping="no" />
				<TOKEN>
					<TOKEN_AUTENTICACAO><xsl:value-of select="*[local-name()='SERVICO_SAIDA']/*[local-name()='VALIDA_PERMISSAO_SAIDA']/TOKEN_AUTENTICACAO"/></TOKEN_AUTENTICACAO>
				</TOKEN>
								
				<xsl:variable name="infBoleto" select="*[2]/*[local-name()='PAGAMENTO_BOLETO']/INFORMACOES_BOLETO"/>
				<xsl:variable name="saidaBoleto" select="*[local-name()='SERVICO_SAIDA']/*[local-name()='CONSULTA_COBRANCA_BANCARIA_SAIDA']/CONSULTA_BOLETO_PAGAMENTO"/>
			
				<INFORMACOES_BOLETO>
					<DATA_MOVIMENTO>
						<xsl:value-of select="concat(BUSTIME/ANO,'-')"/>
						<xsl:value-of select="concat(BUSTIME/MES,'-')"/>
						<xsl:value-of select="BUSTIME/DIA"/>
					</DATA_MOVIMENTO>
					<HORA_MOVIMENTO>
						<xsl:value-of select="concat(BUSTIME/HORA,':')"/>
						<xsl:value-of select="concat(BUSTIME/MIN,':')"/>
						<xsl:value-of select="BUSTIME/SEG"/>
					</HORA_MOVIMENTO>
					<xsl:apply-templates mode="copy" select="$infBoleto/DATA_VENCIMENTO" disable-output-escaping="no" />
					<xsl:apply-templates mode="copy" select="$infBoleto/DATA_PAGAMENTO" disable-output-escaping="no" />
					<xsl:apply-templates mode="copy" select="$infBoleto/CODIGO_BARRAS" disable-output-escaping="no" />
					<xsl:apply-templates mode="copy" select="$infBoleto/LINHA_DIGITAVEL" disable-output-escaping="no" />
					<xsl:apply-templates mode="copy" select="$infBoleto/IDENTIFICACAO" disable-output-escaping="no" />
					<xsl:apply-templates mode="copy" select="$infBoleto/VALOR_NOMINAL" disable-output-escaping="no" />
					<xsl:apply-templates mode="copy" select="$infBoleto/VALOR_PAGO" disable-output-escaping="no" />
					<xsl:apply-templates mode="copy" select="$infBoleto/PARTICIPANTE_DESTINATARIO" disable-output-escaping="no" />
					<xsl:apply-templates mode="copy" select="$infBoleto/PAGADORES" disable-output-escaping="no" />
				</INFORMACOES_BOLETO>
			
			</xsl:if>
		</pagamentoboleto:PAGAMENTO_BOLETO_SAIDA>
	</xsl:template>
</xsl:stylesheet>