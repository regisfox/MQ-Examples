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
	<xsl:param name="numeroConta"><xsl:value-of select="string-length(/BUSDATA/*[local-name()='SERVICO_SAIDA']/*[local-name()='VALIDA_PERMISSAO_SAIDA']/CONTA/NUMERO)"></xsl:value-of></xsl:param>
	
	<xsl:template match="/BUSDATA">
		<ns:DETALHE_AGENDAMENTO_BOLETO_SAIDA
			xmlns:header="http://caixa.gov.br/simtx/comuns/header" 
			xmlns:comuns="http://caixa.gov.br/simtx/comuns/comuns" 
			xmlns:ns="http://caixa.gov.br/simtx/detalhe_agendamento_transacao/boleto/v1/ns"
			xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" 
			xsi:schemaLocation="http://caixa.gov.br/simtx/detalhe_agendamento_transacao/v1/ns Detalhe_Agendamento_Boleto.xsd ">
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
								<xsl:value-of select="substring(*[2]/*[local-name()='HEADER']/IDENTIFICADOR_ORIGEM/TERMINAL, 1, 4)"/>
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
					<xsl:if test="$mensagemTecnica != '' and $mensagemTecnica != '-'">
						<TECNICA><xsl:value-of select="$mensagemTecnica"/></TECNICA>
					</xsl:if>
				</MENSAGENS>
			</HEADER>
						
			<xsl:if test="$erro = ''">
				<xsl:if test="*[local-name()='SERVICO_SAIDA']/*[local-name()='VALIDA_PERMISSAO_SAIDA']/CPF/text() != ''">
					<CPF><xsl:value-of select="*[local-name()='SERVICO_SAIDA']/*[local-name()='VALIDA_PERMISSAO_SAIDA']/CPF"/></CPF>
				</xsl:if>
				
				<xsl:if test="*[local-name()='SERVICO_SAIDA']/*[local-name()='VALIDA_CARTAO_SAIDA'] != ''">
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
				
				<xsl:if test="*[local-name()='SERVICO_SAIDA']/*[local-name()='VALIDA_SENHA_SAIDA'] != ''">
					<SENHA>
						<xsl:if test="*[local-name()='SERVICO_SAIDA']/*[local-name()='VALIDA_SENHA_SAIDA']/INFORMACOES_TELA/text() != ''">
							<INFORMACOES_TELA><xsl:value-of select="*[local-name()='SERVICO_SAIDA']/*[local-name()='VALIDA_SENHA_SAIDA']/INFORMACOES_TELA"/></INFORMACOES_TELA>
						</xsl:if>
						<xsl:if test="*[local-name()='SERVICO_SAIDA']/*[local-name()='VALIDA_SENHA_SAIDA']/FLAG_VALIDACAO/text() != ''">
							<FLAG_VALIDACAO><xsl:value-of select="*[local-name()='SERVICO_SAIDA']/*[local-name()='VALIDA_SENHA_SAIDA']/FLAG_VALIDACAO"/></FLAG_VALIDACAO>
						</xsl:if>
						<xsl:if test="*[local-name()='SERVICO_SAIDA']/*[local-name()='VALIDA_SENHA_SAIDA']/CRIPTOGRAMA/text() != ''">
							<CRIPTOGRAMA><xsl:value-of select="*[local-name()='SERVICO_SAIDA']/*[local-name()='VALIDA_SENHA_SAIDA']/CRIPTOGRAMA"/></CRIPTOGRAMA>
						</xsl:if>
						<xsl:if test="*[local-name()='SERVICO_SAIDA']/*[local-name()='VALIDA_SENHA_SAIDA']/FLAG_ULTIMA_TENTATIVA/text() != ''">
							<FLAG_ULTIMA_TENTATIVA><xsl:value-of select="*[local-name()='SERVICO_SAIDA']/*[local-name()='VALIDA_SENHA_SAIDA']/FLAG_ULTIMA_TENTATIVA"/></FLAG_ULTIMA_TENTATIVA>
						</xsl:if>
						<xsl:if test="*[local-name()='SERVICO_SAIDA']/*[local-name()='VALIDA_SENHA_SAIDA']/TP_IDENTIFICACAO_POSITIVA/text() != ''">
							<TP_IDENTIFICACAO_POSITIVA><xsl:value-of select="*[local-name()='SERVICO_SAIDA']/*[local-name()='VALIDA_SENHA_SAIDA']/TP_IDENTIFICACAO_POSITIVA"/></TP_IDENTIFICACAO_POSITIVA>
						</xsl:if>
					</SENHA>
				</xsl:if>
				
				<xsl:apply-templates mode="copy" select="*[2]/CONTA" disable-output-escaping="yes"/>
				
				<xsl:if test="*[local-name()='SERVICO_SAIDA']/*[local-name()='VALIDA_PERMISSAO_SAIDA'] != ''">
					<PERMISSAO>
						<xsl:if test="*[local-name()='SERVICO_SAIDA']/*[local-name()='VALIDA_PERMISSAO_SAIDA']/OPCAO/text() != ''">
							<OPCAO><xsl:value-of select="*[local-name()='SERVICO_SAIDA']/*[local-name()='VALIDA_PERMISSAO_SAIDA']/OPCAO"/></OPCAO>
						</xsl:if>
						<xsl:if test="*[local-name()='SERVICO_SAIDA']/*[local-name()='VALIDA_PERMISSAO_SAIDA']/OPCAO/text() != ''">
							<SERVICO_SIPER><xsl:value-of select="*[local-name()='SERVICO_SAIDA']/*[local-name()='VALIDA_PERMISSAO_SAIDA']/SERVICO"/></SERVICO_SIPER>
						</xsl:if>
						<xsl:if test="*[local-name()='SERVICO_SAIDA']/*[local-name()='VALIDA_PERMISSAO_SAIDA']/OPCAO/text() != ''">
							<DT_MOVTO_SIPER><xsl:value-of select="*[local-name()='SERVICO_SAIDA']/*[local-name()='VALIDA_PERMISSAO_SAIDA']/DT_MOVTO_SIPER"/></DT_MOVTO_SIPER>
						</xsl:if>
						<xsl:if test="*[local-name()='SERVICO_SAIDA']/*[local-name()='VALIDA_PERMISSAO_SAIDA']/OPCAO/text() != ''">
							<NSU_SIPER><xsl:value-of select="*[local-name()='SERVICO_SAIDA']/*[local-name()='VALIDA_PERMISSAO_SAIDA']/NSU_SIPER"/></NSU_SIPER>
						</xsl:if>
						<xsl:if test="*[local-name()='SERVICO_SAIDA']/*[local-name()='VALIDA_PERMISSAO_SAIDA']/OPCAO/text() != ''">
							<FLAG_TP_LOGIN><xsl:value-of select="*[local-name()='SERVICO_SAIDA']/*[local-name()='VALIDA_PERMISSAO_SAIDA']/FLAG_TP_LOGIN"/></FLAG_TP_LOGIN>
						</xsl:if>
					</PERMISSAO>
					<xsl:if test="*[local-name()='SERVICO_SAIDA']/*[local-name()='VALIDA_PERMISSAO_SAIDA']/TOKEN != ''">
						<TOKEN>
							<xsl:if test="*[local-name()='SERVICO_SAIDA']/*[local-name()='VALIDA_PERMISSAO_SAIDA']/TOKEN/ACAO/text() != ''">
								<ACAO><xsl:value-of select="*[local-name()='SERVICO_SAIDA']/*[local-name()='VALIDA_PERMISSAO_SAIDA']/TOKEN/ACAO"/></ACAO>
							</xsl:if>
							<xsl:if test="*[local-name()='SERVICO_SAIDA']/*[local-name()='VALIDA_PERMISSAO_SAIDA']/TOKEN/ACAO/text() != ''">
								<SESSAO><xsl:value-of select="*[local-name()='SERVICO_SAIDA']/*[local-name()='VALIDA_PERMISSAO_SAIDA']/TOKEN/SESSAO"/></SESSAO>
							</xsl:if>
							<xsl:if test="*[local-name()='SERVICO_SAIDA']/*[local-name()='VALIDA_PERMISSAO_SAIDA']/TOKEN/ACAO/text() != ''">
								<CODIGO_SESSAO><xsl:value-of select="*[local-name()='SERVICO_SAIDA']/*[local-name()='VALIDA_PERMISSAO_SAIDA']/TOKEN/TOKEN_SESSAO"/></CODIGO_SESSAO>
							</xsl:if>
						</TOKEN>
					</xsl:if>
				</xsl:if>

				<xsl:if test="*[2]/DETALHE_AGENDAMENTO_TRANSACAO/SISTEMA_ORIGEM = 'SIAUT' and *[local-name()='SERVICO_SAIDA']/*[local-name()='CONSULTA_TRANSACOES_CONTA_SAIDA'] != ''">
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
				</xsl:if>
				
				<xsl:if test="*[2]/DETALHE_AGENDAMENTO_TRANSACAO/SISTEMA_ORIGEM = 'SIMTX'">
					<INFORMACOES_AGENDAMENTO>
						<xsl:apply-templates mode="copy" select="*[local-name()='PAGAMENTO_BOLETO_ENTRADA']/PAGAMENTO_BOLETO/INFORMACOES_BOLETO/*" disable-output-escaping="yes"/>
					</INFORMACOES_AGENDAMENTO>
				</xsl:if>
			
			</xsl:if>
		</ns:DETALHE_AGENDAMENTO_BOLETO_SAIDA>
	</xsl:template>
</xsl:stylesheet>