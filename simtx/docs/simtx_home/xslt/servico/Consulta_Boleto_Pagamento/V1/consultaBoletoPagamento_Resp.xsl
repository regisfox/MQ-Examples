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
	<xsl:param name="codRetorno" />
	<xsl:param name="acaoRetorno" />
	<xsl:param name="origemRetorno" />
	<xsl:param name="mensagemNegocial" />
	<xsl:param name="mensagemTecnica" />
	
	<xsl:variable name="pCodBarras" select="/BUSDATA/*[local-name()='SERVICO_SAIDA']/*[local-name()='CONSULTA_COBRANCA_BANCARIA_SAIDA']/CONSULTA_BOLETO_PAGAMENTO/TITULO/CODIGO_BARRAS"/>
	
	<xsl:template match="/BUSDATA">
		<consultaboleto:CONSULTA_BOLETO_SAIDA
			xmlns:comuns="http://caixa.gov.br/simtx/comuns/comuns" 
			xmlns:consultaboleto="http://caixa.gov.br/simtx/consulta_boleto_pagamento/v1/ns"
			xmlns:h="http://caixa.gov.br/simtx/comuns/header" 
			xmlns:validacartao="http://caixa.gov.br/simtx/meios_de_entrada/valida_cartao/v1/valida_cartao"
			xmlns:validasenha="http://caixa.gov.br/simtx/meios_de_entrada/valida_senha/v1/valida_senha"
			xmlns:validapermissao="http://caixa.gov.br/simtx/meios_de_entrada/valida_permissao/v1/valida_permissao"
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
			
			<xsl:if test="$erro = ''">
				<CPF><xsl:value-of select="*[2]/CPF"/></CPF>
				
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
						<VENCIMENTO><xsl:value-of select="*[local-name()='SERVICO_SAIDA']/*[local-name()='VALIDA_CARTAO_SAIDA']/CARTAO_COMERCIAL/CARTAO/VENCIMENTO"/></VENCIMENTO>
						<TIPO><xsl:value-of select="*[local-name()='SERVICO_SAIDA']/*[local-name()='VALIDA_CARTAO_SAIDA']/CARTAO_COMERCIAL/CARTAO/TIPO"/></TIPO>
						<SITUACAO><xsl:value-of select="*[local-name()='SERVICO_SAIDA']/*[local-name()='VALIDA_CARTAO_SAIDA']/CARTAO_COMERCIAL/CARTAO/SITUACAO"/></SITUACAO>
						<FLAG_ULTIMA_VIA><xsl:value-of select="*[local-name()='SERVICO_SAIDA']/*[local-name()='VALIDA_CARTAO_SAIDA']/CARTAO_COMERCIAL/CARTAO/FLAG_ULTIMA_VIA"/></FLAG_ULTIMA_VIA>
						<QUANTIDADE_UR><xsl:value-of select="*[local-name()='SERVICO_SAIDA']/*[local-name()='VALIDA_CARTAO_SAIDA']/CARTAO_COMERCIAL/CARTAO/QUANTIDADE_UR"/></QUANTIDADE_UR>
						<FLAG_CHIP><xsl:value-of select="*[local-name()='SERVICO_SAIDA']/*[local-name()='VALIDA_CARTAO_SAIDA']/CARTAO_COMERCIAL/CARTAO/FLAG_CHIP"/></FLAG_CHIP>
						<FLAG_CVV2><xsl:value-of select="*[local-name()='SERVICO_SAIDA']/*[local-name()='VALIDA_CARTAO_SAIDA']/CARTAO_COMERCIAL/CARTAO/FLAG_CVV2"/></FLAG_CVV2>
						<BANDEIRA><xsl:value-of select="*[local-name()='SERVICO_SAIDA']/*[local-name()='VALIDA_CARTAO_SAIDA']/CARTAO_COMERCIAL/CARTAO/BANDEIRA"/></BANDEIRA>
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
				
				<xsl:if test="*[local-name()='SERVICO_SAIDA']/*[local-name()='VALIDA_PERMISSAO_SAIDA'] != ''">
					<CONTA>
						<xsl:choose>
							<xsl:when test="*[local-name()='SERVICO_SAIDA']/*[local-name()='VALIDA_PERMISSAO_SAIDA']/CONTA">
								<CONTA_SIDEC>
									<UNIDADE><xsl:value-of select="*[local-name()='SERVICO_SAIDA']/*[local-name()='VALIDA_PERMISSAO_SAIDA']/CONTA/UNIDADE"/></UNIDADE>
									<OPERACAO><xsl:value-of select="*[local-name()='SERVICO_SAIDA']/*[local-name()='VALIDA_PERMISSAO_SAIDA']/CONTA/OPERACAO"/></OPERACAO>
									<CONTA><xsl:value-of select="*[local-name()='SERVICO_SAIDA']/*[local-name()='VALIDA_PERMISSAO_SAIDA']/CONTA/NUMERO"/></CONTA>
									<DV><xsl:value-of select="*[local-name()='SERVICO_SAIDA']/*[local-name()='VALIDA_PERMISSAO_SAIDA']/CONTA/DV"/></DV>
								</CONTA_SIDEC>
							</xsl:when>
							<xsl:otherwise>
								<CONTA_NSGD>
									<UNIDADE><xsl:value-of select="*[local-name()='SERVICO_SAIDA']/*[local-name()='VALIDA_PERMISSAO_SAIDA']/CONTA_NSGD/UNIDADE"/></UNIDADE>
									<PRODUTO><xsl:value-of select="*[local-name()='SERVICO_SAIDA']/*[local-name()='VALIDA_PERMISSAO_SAIDA']/CONTA_NSGD/PRODUTO"/></PRODUTO>
									<CONTA><xsl:value-of select="*[local-name()='SERVICO_SAIDA']/*[local-name()='VALIDA_PERMISSAO_SAIDA']/CONTA_NSGD/NUMERO"/></CONTA>
									<DV><xsl:value-of select="*[local-name()='SERVICO_SAIDA']/*[local-name()='VALIDA_PERMISSAO_SAIDA']/CONTA_NSGD/DV"/></DV>
								</CONTA_NSGD>
							</xsl:otherwise>
						</xsl:choose>
						<TITULAR_CONTA/>
					</CONTA>
					<PERMISSAO>
						<OPCAO><xsl:value-of select="*[local-name()='SERVICO_SAIDA']/*[local-name()='VALIDA_PERMISSAO_SAIDA']/OPCAO"/></OPCAO>
						<SERVICO_SIPER><xsl:value-of select="*[local-name()='SERVICO_SAIDA']/*[local-name()='VALIDA_PERMISSAO_SAIDA']/SERVICO"/></SERVICO_SIPER>
						<DT_MOVTO_SIPER><xsl:value-of select="*[local-name()='SERVICO_SAIDA']/*[local-name()='VALIDA_PERMISSAO_SAIDA']/DT_MOVTO_SIPER"/></DT_MOVTO_SIPER>
						<NSU_SIPER><xsl:value-of select="*[local-name()='SERVICO_SAIDA']/*[local-name()='VALIDA_PERMISSAO_SAIDA']/NSU_SIPER"/></NSU_SIPER>
						<FLAG_TP_LOGIN><xsl:value-of select="*[local-name()='SERVICO_SAIDA']/*[local-name()='VALIDA_PERMISSAO_SAIDA']/FLAG_TP_LOGIN"/></FLAG_TP_LOGIN>
					</PERMISSAO>
					<xsl:if test="*[local-name()='SERVICO_SAIDA']/*[local-name()='VALIDA_PERMISSAO_SAIDA']/TOKEN != ''">
						<TOKEN>
							<ACAO><xsl:value-of select="*[local-name()='SERVICO_SAIDA']/*[local-name()='VALIDA_PERMISSAO_SAIDA']/TOKEN/ACAO"/></ACAO>
							<SESSAO><xsl:value-of select="*[local-name()='SERVICO_SAIDA']/*[local-name()='VALIDA_PERMISSAO_SAIDA']/TOKEN/SESSAO"/></SESSAO>
							<CODIGO_SESSAO><xsl:value-of select="*[local-name()='SERVICO_SAIDA']/*[local-name()='VALIDA_PERMISSAO_SAIDA']/TOKEN/TOKEN_SESSAO"/></CODIGO_SESSAO>
						</TOKEN>
					</xsl:if>
				</xsl:if>
				
				
				<xsl:variable name="saidaBoleto" select="*[local-name()='SERVICO_SAIDA']/*[local-name()='CONSULTA_COBRANCA_BANCARIA_SAIDA']/CONSULTA_BOLETO_PAGAMENTO"/>
			
			
				<xsl:apply-templates mode="copy" select="*[local-name()='SERVICO_SAIDA']/*[local-name()='CONSULTA_REGRAS_BOLETO_SAIDA']/FORMA_RECEBIMENTO" disable-output-escaping="no" />
				<FLAG_NOVA_COBRANCA>
					<xsl:choose>
						<xsl:when test="($saidaBoleto/FLAG_NOVA_COBRANCA != '' and (substring($pCodBarras,1,3) = 104)) 
										or ($saidaBoleto/FLAG_NOVA_COBRANCA = 'S')">S</xsl:when>
						<xsl:otherwise>N</xsl:otherwise>
					</xsl:choose>
				</FLAG_NOVA_COBRANCA>
				<SITUACAO_CONTINGENCIA>
					<xsl:choose>
						<xsl:when test="$saidaBoleto/SITUACAO_CONTINGENCIA"><xsl:value-of select="$saidaBoleto/SITUACAO_CONTINGENCIA"/></xsl:when>
						<xsl:otherwise>SEM_CONTINGENCIA</xsl:otherwise>
					</xsl:choose>
				</SITUACAO_CONTINGENCIA>
				<CONSULTA_BOLETO>
					<xsl:apply-templates mode="copy" select="$saidaBoleto/FLAG_HABITACAO" disable-output-escaping="no" />
					<xsl:apply-templates mode="copy" select="$saidaBoleto/NUMERO_CONTROLE_DDA" disable-output-escaping="no" />
					<xsl:apply-templates mode="copy" select="$saidaBoleto/TIPO_BOLETO" disable-output-escaping="no" />
					<TITULO>
						<xsl:apply-templates mode="copy" select="$saidaBoleto/TITULO/NUMERO_IDENTIFICACAO" disable-output-escaping="no" />
						<xsl:apply-templates mode="copy" select="$saidaBoleto/TITULO/REFERENCIA_ATUAL_CADASTRO" disable-output-escaping="no" />
						<xsl:apply-templates mode="copy" select="$saidaBoleto/TITULO/SEQUENCIA_ATUALIZACAO_CADASTRO" disable-output-escaping="no" />
						<xsl:apply-templates mode="copy" select="$saidaBoleto/TITULO/DATA_HORA_SITUACAO" disable-output-escaping="no" />
						<xsl:apply-templates mode="copy" select="$saidaBoleto/TITULO/NOSSO_NUMERO" disable-output-escaping="no" />
						<xsl:apply-templates mode="copy" select="$saidaBoleto/TITULO/BENEFICIARIO_ORIGINAL" disable-output-escaping="no" />
						<xsl:apply-templates mode="copy" select="$saidaBoleto/TITULO/BENEFICIARIO_FINAL" disable-output-escaping="no" />
						<xsl:apply-templates mode="copy" select="$saidaBoleto/TITULO/AVALISTA" disable-output-escaping="no" />
						<xsl:apply-templates mode="copy" select="$saidaBoleto/TITULO/PAGADOR" disable-output-escaping="no" />
						<xsl:apply-templates mode="copy" select="$saidaBoleto/TITULO/CODIGO_MOEDA" disable-output-escaping="no" />
						<xsl:apply-templates mode="copy" select="$saidaBoleto/TITULO/CODIGO_BARRAS" disable-output-escaping="no" />
						<xsl:apply-templates mode="copy" select="$saidaBoleto/TITULO/LINHA_DIGITAVEL" disable-output-escaping="no" />
						<DATA_VENCIMENTO>
							<xsl:choose>
								<xsl:when test="$saidaBoleto/TITULO/DATA_VENCIMENTO"><xsl:value-of select="$saidaBoleto/TITULO/DATA_VENCIMENTO"/></xsl:when>
								<xsl:otherwise><xsl:value-of select="*[local-name()='SERVICO_SAIDA']/*[local-name()='CONSULTA_REGRAS_BOLETO_SAIDA']/DATA_VENCIMENTO"/></xsl:otherwise>
							</xsl:choose>
						</DATA_VENCIMENTO>
						<VALOR>
							<xsl:choose>
								<xsl:when test="$saidaBoleto/TITULO/VALOR"><xsl:value-of select="$saidaBoleto/TITULO/VALOR"/></xsl:when>
								<xsl:otherwise><xsl:value-of select="*[local-name()='SERVICO_SAIDA']/*[local-name()='CONSULTA_REGRAS_BOLETO_SAIDA']/VALOR_NOMINAL"/></xsl:otherwise>
							</xsl:choose>
						</VALOR>
						<xsl:apply-templates mode="copy" select="$saidaBoleto/TITULO/ESPECIE" disable-output-escaping="no" />
						<xsl:apply-templates mode="copy" select="$saidaBoleto/TITULO/VALOR_ABATIMENTO" disable-output-escaping="no" />
						<xsl:apply-templates mode="copy" select="$saidaBoleto/TITULO/JUROS" disable-output-escaping="no" />
						<xsl:apply-templates mode="copy" select="$saidaBoleto/TITULO/MULTA" disable-output-escaping="no" />
						<xsl:apply-templates mode="copy" select="$saidaBoleto/TITULO/DESCONTOS" disable-output-escaping="no" />
						<MINIMO>
							<xsl:choose>
								<xsl:when test="$saidaBoleto/TITULO/MINIMO/VALOR">
									<VALOR><xsl:value-of select="$saidaBoleto/TITULO/MINIMO/VALOR"/></VALOR>
								</xsl:when>
								<xsl:otherwise>
									<VALOR><xsl:value-of select="$saidaBoleto/CALCULOS/CALCULO_CAIXA/VALOR_MINIMO"/></VALOR>
								</xsl:otherwise>
							</xsl:choose>
						</MINIMO>
						<MAXIMO>
							<xsl:choose>
								<xsl:when test="$saidaBoleto/TITULO/MAXIMO/VALOR">
									<VALOR><xsl:value-of select="$saidaBoleto/TITULO/MAXIMO/VALOR"/></VALOR>
								</xsl:when>
								<xsl:otherwise>
									<VALOR><xsl:value-of select="$saidaBoleto/CALCULOS/CALCULO_CAIXA/VALOR_MAXIMO"/></VALOR>
								</xsl:otherwise>
							</xsl:choose>
						</MAXIMO>
						<PARTICIPANTE_DESTINATARIO>
							<ISPB><xsl:value-of select="*[local-name()='SERVICO_SAIDA']/*[local-name()='BANCO_AGENCIA_SAIDA']/BANCO/IDENTIFICACAO_SPB_IF"/></ISPB>
							<CODIGO_BANCO><xsl:value-of select="*[local-name()='SERVICO_SAIDA']/*[local-name()='BANCO_AGENCIA_SAIDA']/BANCO/NUMERO"/></CODIGO_BANCO>
							<NOME_BANCO><xsl:value-of select="*[local-name()='SERVICO_SAIDA']/*[local-name()='BANCO_AGENCIA_SAIDA']/BANCO/NOME"/></NOME_BANCO>
						</PARTICIPANTE_DESTINATARIO>
					</TITULO>
					<xsl:apply-templates mode="copy" select="$saidaBoleto/QUANTIDADE_DIAS_PROTESTO" disable-output-escaping="no" />
					<xsl:apply-templates mode="copy" select="$saidaBoleto/DATA_LIMITE_PAGAMENTO" disable-output-escaping="no" />
					<xsl:apply-templates mode="copy" select="$saidaBoleto/RECEBE_VALOR_DIVERGENTE" disable-output-escaping="no" />
					<xsl:apply-templates mode="copy" select="$saidaBoleto/FLAG_RECEBIMENTO_CHEQUE" disable-output-escaping="no" />
					<xsl:apply-templates mode="copy" select="$saidaBoleto/FLAG_BLOQUEIO_PAGAMENTO" disable-output-escaping="no" />
					<xsl:apply-templates mode="copy" select="$saidaBoleto/FLAG_PAGAMENTO_PARCIAL" disable-output-escaping="no" />
					<xsl:apply-templates mode="copy" select="$saidaBoleto/QUANTIDADE_PAGAMENTO_PARCIAL_PERMITIDO" disable-output-escaping="no" />
					<xsl:apply-templates mode="copy" select="$saidaBoleto/QUANTIDADE_PAGAMENTO_PARCIAL_REGISTRADO" disable-output-escaping="no" />
					<xsl:apply-templates mode="copy" select="$saidaBoleto/VALOR_SALDO_TOTAL_ATUAL" disable-output-escaping="no" />
					<xsl:apply-templates mode="copy" select="$saidaBoleto/MODELO_CALCULO" disable-output-escaping="no" />
					<xsl:apply-templates mode="copy" select="$saidaBoleto/CALCULOS" disable-output-escaping="no" />
					<xsl:apply-templates mode="copy" select="$saidaBoleto/BAIXAS" disable-output-escaping="no" />
					<xsl:apply-templates mode="copy" select="$saidaBoleto/DATA_HORA_DDA" disable-output-escaping="no" />
					<xsl:apply-templates mode="copy" select="$saidaBoleto/DATA_MOVIMENTO" disable-output-escaping="no" />
					<xsl:apply-templates mode="copy" select="$saidaBoleto/FLAG_ULTIMA_PARCELA_VIAVEL" disable-output-escaping="no" />
					<xsl:apply-templates mode="copy" select="$saidaBoleto/FLAG_RECEBER_QUALQUER_VALOR" disable-output-escaping="no" />
					<xsl:apply-templates mode="copy" select="$saidaBoleto/NUMERO_PARCELA_ATUAL" disable-output-escaping="no" />
				</CONSULTA_BOLETO>

			</xsl:if>
		</consultaboleto:CONSULTA_BOLETO_SAIDA>
	</xsl:template>
</xsl:stylesheet>