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
	
	<xsl:variable name="numeroConta"><xsl:value-of select="string-length(/BUSDATA/*[local-name()='SERVICO_SAIDA']/*[local-name()='VALIDA_PERMISSAO_SAIDA']/CONTA/NUMERO)"></xsl:value-of></xsl:variable>
	
	<xsl:template match="/BUSDATA">
		<cancelamentoagendamento:CANCELAMENTO_AGENDAMENTO_SAIDA
			xmlns:header="http://caixa.gov.br/simtx/comuns/header" 
			xmlns:validapermissao="http://caixa.gov.br/simtx/meioentrada/valida_permissao"
			xmlns:cancelamentoagendamento="http://caixa.gov.br/simtx/cancelamento_agendamento/v1/ns"
			xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" 
			xsi:schemaLocation="http://caixa.gov.br/simtx/consultaboleto Consulta_Boleto_Pagamento.xsd ">
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
				<USUARIO_SERVICO><xsl:value-of select="*[2]/*[local-name()='HEADER']/USUARIO_SERVICO"/></USUARIO_SERVICO>
				<USUARIO><xsl:value-of select="*[2]/*[local-name()='HEADER']/USUARIO"/></USUARIO>
				<DATA_REFERENCIA><xsl:value-of select="*[2]/*[local-name()='HEADER']/DATA_REFERENCIA"/></DATA_REFERENCIA>
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
			
			<xsl:if test="$erro = ''">
				<CPF><xsl:value-of select="*[local-name()='SERVICO_SAIDA']/*[local-name()='VALIDA_PERMISSAO_SAIDA']/CPF"/></CPF>
				<xsl:apply-templates mode="copy" select="*[2]/CONTA" disable-output-escaping="no" />
				<TOKEN>
					<TOKEN_AUTENTICACAO><xsl:value-of select="*[local-name()='SERVICO_SAIDA']/*[local-name()='VALIDA_PERMISSAO_SAIDA']/TOKEN_AUTENTICACAO"/></TOKEN_AUTENTICACAO>
				</TOKEN>
				
				<xsl:choose>
					<xsl:when test="*[2]/CANCELAMENTO_AGENDAMENTO/SIAUT != '' and *[local-name()='SERVICO_SAIDA']/*[local-name()='CONSULTA_TRANSACOES_CONTA_SAIDA'] != ''">
						<xsl:variable name="saidaSiaut" select="*[local-name()='SERVICO_SAIDA']/*[local-name()='CONSULTA_TRANSACOES_CONTA_SAIDA']/BOLETO/TRANSACOES/TRANSACAO"/>
						
						<INFORMACOES_AGENDAMENTO>
							<NOSSO_NUMERO><xsl:value-of select="$saidaSiaut/NOSSO_NUMERO"/></NOSSO_NUMERO>
							<xsl:if test="$saidaSiaut/DATA_PAGAMENTO != ''">
								<DATA_MOVIMENTO><xsl:value-of select="$saidaSiaut/DATA_PAGAMENTO"/></DATA_MOVIMENTO>
							</xsl:if>
							<xsl:if test="$saidaSiaut/HORA_TRANSACAO != ''">
								<HORA_MOVIMENTO><xsl:value-of select="$saidaSiaut/HORA_TRANSACAO"/></HORA_MOVIMENTO>
							</xsl:if>
							<DATA_VENCIMENTO><xsl:value-of select="$saidaSiaut/DATA_VENCIMENTO"/></DATA_VENCIMENTO>
							<xsl:if test="$saidaSiaut/DATA_EFETIVACAO != ''">
								<DATA_EFETIVACAO><xsl:value-of select="$saidaSiaut/DATA_EFETIVACAO"/></DATA_EFETIVACAO>
							</xsl:if>
							<xsl:apply-templates mode="copy" select="$saidaSiaut/CODIGO_BARRAS" disable-output-escaping="yes"/>
							<LINHA_DIGITAVEL> <!-- VER COM O LEO -->  </LINHA_DIGITAVEL>
							<xsl:apply-templates mode="copy" select="$saidaSiaut/IDENTIFICACAO" disable-output-escaping="yes"/>
							<VALOR_NOMINAL><xsl:value-of select="$saidaSiaut/VALOR"/></VALOR_NOMINAL>
							<xsl:apply-templates mode="copy" select="$saidaSiaut/JUROS" disable-output-escaping="yes"/>
							<xsl:apply-templates mode="copy" select="$saidaSiaut/IOF" disable-output-escaping="yes"/>
							<xsl:apply-templates mode="copy" select="$saidaSiaut/MULTA" disable-output-escaping="yes"/>
							<xsl:apply-templates mode="copy" select="$saidaSiaut/DESCONTO" disable-output-escaping="yes"/>
							<xsl:apply-templates mode="copy" select="$saidaSiaut/ABATIMENTO" disable-output-escaping="yes"/>
							<xsl:if test="$saidaSiaut/VALORES_CALCULADOS != ''">
								<VALOR_CALCULADO><xsl:value-of select="$saidaSiaut/VALORES_CALCULADOS"/></VALOR_CALCULADO>
							</xsl:if>
							<xsl:apply-templates mode="copy" select="$saidaSiaut/VALOR_PAGO" disable-output-escaping="yes"/>
							<xsl:apply-templates mode="copy" select="$saidaSiaut/PARTICIPANTE_DESTINATARIO" disable-output-escaping="yes"/>
							<xsl:if test="$saidaSiaut/BENEFICIARIOS != ''">
								<BENEFICIARIOS>
									<ORIGINAL>
										<xsl:if test="$saidaSiaut/CODIGO_BENEFICIARIO_ORIGINAL != ''">
											<CODIGO_BENEFICIARIO_ORIGINAL><xsl:value-of select="$saidaSiaut/CODIGO_BENEFICIARIO_ORIGINAL"/></CODIGO_BENEFICIARIO_ORIGINAL>
										</xsl:if>
										<xsl:choose>
											<xsl:when test="$saidaSiaut/BENEFICIARIOS/ORIGINAL/CPF != ''">
												<xsl:apply-templates mode="copy" select="$saidaSiaut/BENEFICIARIOS/ORIGINAL/CPF" disable-output-escaping="yes"/>
												<xsl:apply-templates mode="copy" select="$saidaSiaut/BENEFICIARIOS/ORIGINAL/NOME" disable-output-escaping="yes"/>
											</xsl:when>
											<xsl:otherwise>
												<xsl:apply-templates mode="copy" select="$saidaSiaut/BENEFICIARIOS/ORIGINAL/CNPJ" disable-output-escaping="yes"/>
												<xsl:apply-templates mode="copy" select="$saidaSiaut/BENEFICIARIOS/ORIGINAL/RAZAO_SOCIAL" disable-output-escaping="yes"/>
												<xsl:apply-templates mode="copy" select="$saidaSiaut/BENEFICIARIOS/ORIGINAL/NOME_FANTASIA" disable-output-escaping="yes"/>
											</xsl:otherwise>
										</xsl:choose>
									</ORIGINAL>
									<xsl:apply-templates mode="copy" select="$saidaSiaut/BENEFICIARIOS/FINAL" disable-output-escaping="yes"/>
								</BENEFICIARIOS>
							</xsl:if>
							<xsl:apply-templates mode="copy" select="$saidaSiaut/PAGADORES" disable-output-escaping="yes"/>
							<xsl:apply-templates mode="copy" select="$saidaSiaut/SACADOR_AVALISTA" disable-output-escaping="yes"/>
						</INFORMACOES_AGENDAMENTO>
					</xsl:when>
				
					<xsl:otherwise>
						<INFORMACOES_AGENDAMENTO>
							<xsl:apply-templates mode="copy" select="*[local-name()='PAGAMENTO_BOLETO_ENTRADA']/PAGAMENTO_BOLETO/INFORMACOES_BOLETO/*" disable-output-escaping="no" />
						</INFORMACOES_AGENDAMENTO>
					</xsl:otherwise>
				</xsl:choose>
				
			</xsl:if>
		</cancelamentoagendamento:CANCELAMENTO_AGENDAMENTO_SAIDA>
	</xsl:template>
</xsl:stylesheet>