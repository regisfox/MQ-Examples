<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" 
	xmlns:cc="http://caixa.gov.br/sibar/consulta_conta_sid09/cc"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	
	<xsl:output omit-xml-declaration="yes" />
	<xsl:strip-space elements="*" />
	
	<xsl:template match="*" mode="copy">
		<xsl:element name="{name()}" namespace="{namespace-uri()}">
			<xsl:choose>
				<xsl:when test="name() = 'IDENTIFICACAO_TRANSACAO'">
					<xsl:value-of select="/BUSDATA/*[2]/VALIDA_BOLETO/IDENTIFICACAO_TRANSACAO"/>
				</xsl:when>
				<xsl:otherwise>
					<xsl:apply-templates select="@*|node()" mode="copy" />
				</xsl:otherwise>
			</xsl:choose>
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
	<xsl:variable name="transacao_origem" select ="/BUSDATA/TRANSACAO_ORIGEM" />

	<xsl:variable name="pTitularConta">
		<xsl:choose>
			<xsl:when test="$transacao_origem/*[local-name()='CONSULTA_BOLETO_SAIDA']/CONTA/TITULAR_CONTA">
				<TITULAR_CONTA><xsl:value-of select="$transacao_origem/*[local-name()='CONSULTA_BOLETO_SAIDA']/CONTA/TITULAR_CONTA"/></TITULAR_CONTA>
			</xsl:when>
			<xsl:otherwise>
				<TITULAR_CONTA><xsl:value-of select="*[2]/CONTA/TITULAR_CONTA"/></TITULAR_CONTA>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:variable>
	
	<xsl:template match="/BUSDATA">
		<validaboleto:VALIDA_BOLETO_SAIDA
			xmlns:header="http://caixa.gov.br/simtx/header" 
			xmlns:validapermissao="http://caixa.gov.br/simtx/meioentrada/valida_permissao"
			xmlns:consultacobrancabancaria="http://caixa.gov.br/simtx/negocial/consulta_cobranca_bancaria/pagamento"
			xmlns:comuns="http://caixa.gov.br/simtx/comuns/comuns"
			xmlns:validaboleto="http://caixa.gov.br/simtx/validaboleto/v2/ns"
			xmlns:consultaconta="http://caixa.gov.br/sibar/consulta_conta_sid09"
			xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
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
				<xsl:if test="*[2]/CPF != ''">
					<CPF><xsl:value-of select="*[2]/CPF"/></CPF>
				</xsl:if>
				<CONTA>
					<xsl:choose>
						<xsl:when test="*[2]/CONTA/CONTA_SIDEC">
							<CONTA_SIDEC>
								<UNIDADE><xsl:value-of select="*[2]/CONTA/CONTA_SIDEC/UNIDADE"/></UNIDADE>
								<OPERACAO><xsl:value-of select="*[2]/CONTA/CONTA_SIDEC/OPERACAO"/></OPERACAO>
								<CONTA><xsl:value-of select="*[2]/CONTA/CONTA_SIDEC/CONTA"/></CONTA>
								<DV><xsl:value-of select="*[2]/CONTA/CONTA_SIDEC/DV"/></DV>
							</CONTA_SIDEC>
						</xsl:when>
						<xsl:otherwise>
							<CONTA_NSGD>
								<UNIDADE><xsl:value-of select="*[2]/CONTA/CONTA_NSGD/UNIDADE"/></UNIDADE>
								<PRODUTO><xsl:value-of select="*[2]/CONTA/CONTA_NSGD/PRODUTO"/></PRODUTO>
								<CONTA><xsl:value-of select="*[2]/CONTA/CONTA_NSGD/CONTA"/></CONTA>
								<DV><xsl:value-of select="*[2]/CONTA/CONTA_NSGD/DV"/></DV>
							</CONTA_NSGD>
						</xsl:otherwise>
					</xsl:choose>
					<xsl:choose>
						<xsl:when test="$transacao_origem/*[local-name()='CONSULTA_BOLETO_SAIDA']/CONTA/TITULAR_CONTA">
							<TITULAR_CONTA><xsl:value-of select="$transacao_origem/*[local-name()='CONSULTA_BOLETO_SAIDA']/CONTA/TITULAR_CONTA"/></TITULAR_CONTA>
						</xsl:when>
						<xsl:otherwise>
							<TITULAR_CONTA><xsl:value-of select="*[2]/CONTA/TITULAR_CONTA"/></TITULAR_CONTA>
						</xsl:otherwise>
					</xsl:choose>
			  	</CONTA>
			  	
			  	<xsl:if test="*[local-name()='SERVICO_SAIDA']/*[local-name()='VALIDA_PERMISSAO_SAIDA'] != ''">
					<PERMISSAO>
						<OPCAO><xsl:value-of select="*[local-name()='SERVICO_SAIDA']/*[local-name()='VALIDA_PERMISSAO_SAIDA']/OPCAO"/></OPCAO>
					    <SERVICO_SIPER><xsl:value-of select="*[local-name()='SERVICO_SAIDA']/*[local-name()='VALIDA_PERMISSAO_SAIDA']/SERVICO"/></SERVICO_SIPER>
					    <DT_MOVTO_SIPER><xsl:value-of select="*[local-name()='SERVICO_SAIDA']/*[local-name()='VALIDA_PERMISSAO_SAIDA']/DT_MOVTO_SIPER"/></DT_MOVTO_SIPER>
					    <FLAG_TP_LOGIN><xsl:value-of select="*[local-name()='SERVICO_SAIDA']/*[local-name()='VALIDA_PERMISSAO_SAIDA']/FLAG_TP_LOGIN"/></FLAG_TP_LOGIN>
					</PERMISSAO>
					<TOKEN>
						<ACAO><xsl:value-of select="*[local-name()='SERVICO_SAIDA']/*[local-name()='VALIDA_PERMISSAO_SAIDA']/TOKEN/ACAO"/></ACAO>
					    <SESSAO><xsl:value-of select="*[local-name()='SERVICO_SAIDA']/*[local-name()='VALIDA_PERMISSAO_SAIDA']/TOKEN/SESSAO"/></SESSAO>
					    <CODIGO_SESSAO><xsl:value-of select="*[local-name()='SERVICO_SAIDA']/*[local-name()='VALIDA_PERMISSAO_SAIDA']/TOKEN/TOKEN_SESSAO"/></CODIGO_SESSAO>
					</TOKEN>
				</xsl:if>
				
				<NSUMTX_ORIGEM><xsl:value-of select="*[2]/NSUMTX_ORIGEM" /></NSUMTX_ORIGEM>

				<xsl:variable name="entradaBoleto" select="$transacao_origem/*[local-name()='CONSULTA_BOLETO_ENTRADA']/CONSULTA_BOLETO"/>
				<xsl:variable name="saidaBoleto" select="$transacao_origem/*[local-name()='CONSULTA_BOLETO_SAIDA']/CONSULTA_BOLETO"/>
			
				<FLAG_NOVA_COBRANCA><xsl:value-of select="$transacao_origem/*[local-name()='CONSULTA_BOLETO_SAIDA']/FLAG_NOVA_COBRANCA" /></FLAG_NOVA_COBRANCA>
				<INFORMACOES_BOLETO>
					<NOSSO_NUMERO>
						<xsl:value-of select="$saidaBoleto/TITULO/NOSSO_NUMERO/NUMERO" />
						<xsl:value-of select="$saidaBoleto/TITULO/NOSSO_NUMERO/DV" />						
					</NOSSO_NUMERO>
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
					<DATA_VENCIMENTO><xsl:value-of select="$saidaBoleto/TITULO/DATA_VENCIMENTO" /></DATA_VENCIMENTO>
					<xsl:choose>
						<xsl:when test="*[local-name()='SERVICO_SAIDA']/*[local-name()='ROTINAS_USO_COMUM_SAIDA']/DATA_CALCULO">
							<DATA_EFETIVACAO><xsl:value-of select="*[local-name()='SERVICO_SAIDA']/*[local-name()='ROTINAS_USO_COMUM_SAIDA']/DATA_CALCULO" /></DATA_EFETIVACAO>
						</xsl:when>
						<xsl:when test="$saidaBoleto/CALCULOS/CALCULO_CAIXA/DATA_CALCULO">
							<DATA_EFETIVACAO><xsl:value-of select="$saidaBoleto/CALCULOS/CALCULO_CAIXA/DATA_CALCULO" /></DATA_EFETIVACAO>
						</xsl:when>
						<xsl:otherwise>
							<DATA_EFETIVACAO><xsl:value-of select="*[2]/VALIDA_BOLETO/DATA_PAGAMENTO" /></DATA_EFETIVACAO>
						</xsl:otherwise>
					</xsl:choose>
					<CODIGO_BARRAS><xsl:value-of select="$saidaBoleto/TITULO/CODIGO_BARRAS" /></CODIGO_BARRAS>
					<LINHA_DIGITAVEL><xsl:value-of select="$saidaBoleto/TITULO/LINHA_DIGITAVEL" /></LINHA_DIGITAVEL>
					<IDENTIFICACAO><xsl:value-of select="*[2]/*[local-name()='VALIDA_BOLETO']/IDENTIFICACAO_TRANSACAO" /></IDENTIFICACAO>
					<VALOR_NOMINAL><xsl:value-of select="$saidaBoleto/TITULO/VALOR" /></VALOR_NOMINAL>
					<xsl:choose>
						<xsl:when test="*[local-name()='SERVICO_SAIDA']/*[local-name()='ROTINAS_USO_COMUM_SAIDA']/VALOR_JUROS">
							<JUROS><xsl:value-of select="*[local-name()='SERVICO_SAIDA']/*[local-name()='ROTINAS_USO_COMUM_SAIDA']/VALOR_JUROS" /></JUROS>
						</xsl:when>
						<xsl:otherwise>
							<JUROS><xsl:value-of select="$saidaBoleto/CALCULOS/CALCULO_CAIXA/VALOR_JUROS" /></JUROS>
						</xsl:otherwise>
					</xsl:choose>
					<IOF><xsl:value-of select="$saidaBoleto/CALCULOS/CALCULO_CAIXA/VALOR_IOF" /></IOF>
					<MULTA><xsl:value-of select="$saidaBoleto/CALCULOS/CALCULO_CAIXA/VALOR_MULTA" /></MULTA>
					<DESCONTO><xsl:value-of select="$saidaBoleto/CALCULOS/CALCULO_CAIXA/VALOR_DESCONTO" /></DESCONTO>
					<ABATIMENTO><xsl:value-of select="$saidaBoleto/CALCULOS/CALCULO_CAIXA/VALOR_ABATIMENTO" /></ABATIMENTO>
					<VALOR_CALCULADO><xsl:value-of select="$saidaBoleto/CALCULOS/CALCULO_CAIXA/VALOR_TOTAL" /></VALOR_CALCULADO>
					<VALOR_PAGO><xsl:value-of select="*[2]/VALIDA_BOLETO/VALOR_PAGAR" /></VALOR_PAGO>
					<PARTICIPANTE_DESTINATARIO>
						<BANCO><xsl:value-of select="$saidaBoleto/TITULO/PARTICIPANTE_DESTINATARIO/CODIGO_BANCO" /></BANCO>
						<ISPB><xsl:value-of select="$saidaBoleto/TITULO/PARTICIPANTE_DESTINATARIO/ISPB" /></ISPB>
						<NOME_BANCO><xsl:value-of select="$saidaBoleto/TITULO/PARTICIPANTE_DESTINATARIO/NOME_BANCO" /></NOME_BANCO>
					</PARTICIPANTE_DESTINATARIO>
					<BENEFICIARIOS>
						<ORIGINAL>
							<CODIGO_BENEFICIARIO_ORIGINAL><xsl:value-of select="$saidaBoleto/TITULO/BENEFICIARIO_ORIGINAL/CODIGO" /></CODIGO_BENEFICIARIO_ORIGINAL>
							<xsl:choose>
								<xsl:when test="$saidaBoleto/TITULO/BENEFICIARIO_ORIGINAL/CPF">
									<CPF><xsl:value-of select="$saidaBoleto/TITULO/BENEFICIARIO_ORIGINAL/CPF" /></CPF>
									<NOME><xsl:value-of select="$saidaBoleto/TITULO/BENEFICIARIO_ORIGINAL/NOME" /></NOME>
								</xsl:when>
								<xsl:otherwise>
									<CNPJ><xsl:value-of select="$saidaBoleto/TITULO/BENEFICIARIO_ORIGINAL/CNPJ" /></CNPJ>
									<RAZAO_SOCIAL><xsl:value-of select="$saidaBoleto/TITULO/BENEFICIARIO_ORIGINAL/RAZAO_SOCIAL" /></RAZAO_SOCIAL>
									<NOME_FANTASIA><xsl:value-of select="$saidaBoleto/TITULO/BENEFICIARIO_ORIGINAL/NOME_FANTASIA" /></NOME_FANTASIA>
								</xsl:otherwise>
							</xsl:choose>
						</ORIGINAL>
						<FINAL>
							<xsl:choose>
								<xsl:when test="$saidaBoleto/TITULO/BENEFICIARIO_FINAL/CPF">
									<CPF><xsl:value-of select="$saidaBoleto/TITULO/BENEFICIARIO_FINAL/CPF" /></CPF>
									<NOME><xsl:value-of select="$saidaBoleto/TITULO/BENEFICIARIO_FINAL/NOME" /></NOME>
								</xsl:when>
								<xsl:otherwise>
									<CNPJ><xsl:value-of select="$saidaBoleto/TITULO/BENEFICIARIO_FINAL/CNPJ" /></CNPJ>
									<RAZAO_SOCIAL><xsl:value-of select="$saidaBoleto/TITULO/BENEFICIARIO_FINAL/RAZAO_SOCIAL" /></RAZAO_SOCIAL>
									<NOME_FANTASIA><xsl:value-of select="$saidaBoleto/TITULO/BENEFICIARIO_FINAL/NOME_FANTASIA" /></NOME_FANTASIA>
								</xsl:otherwise>
							</xsl:choose>
						</FINAL>
					</BENEFICIARIOS>
					<PAGADORES>
						<ORIGINAL>
							<xsl:choose>
								<xsl:when test="$saidaBoleto/TITULO/PAGADOR/CPF">
									<CPF><xsl:value-of select="$saidaBoleto/TITULO/PAGADOR/CPF" /></CPF>
									<NOME><xsl:value-of select="$saidaBoleto/TITULO/PAGADOR/NOME" /></NOME>
								</xsl:when>
								<xsl:otherwise>
									<CNPJ><xsl:value-of select="$saidaBoleto/TITULO/PAGADOR/CNPJ" /></CNPJ>
									<RAZAO_SOCIAL><xsl:value-of select="$saidaBoleto/TITULO/PAGADOR/RAZAO_SOCIAL" /></RAZAO_SOCIAL>
									<NOME_FANTASIA><xsl:value-of select="$saidaBoleto/TITULO/PAGADOR/NOME_FANTASIA" /></NOME_FANTASIA>
								</xsl:otherwise>
							</xsl:choose>
						</ORIGINAL>
						<FINAL>
							<xsl:for-each select="*[local-name()='SERVICO_SAIDA']/*[name()='consultaconta:CONSULTA_CONTA_SID09_SAIDA']/CONSULTA_CONTA/CPF_CNPJ_CONTA">
						 		<xsl:choose>
						 			<xsl:when test="/BUSDATA/*[2]/*[local-name()='HEADER']/MEIOENTRADA = '0'">
								 		<xsl:if test="./TITULARIDADE = $pTitularConta">
											<xsl:if test="./CPF">
												<CPF><xsl:value-of select="./CPF" /></CPF>
											</xsl:if>
											<NOME><xsl:value-of select="./NOME_REDUZIDO" /></NOME>
											<xsl:if test="./CNPJ">
												<CNPJ><xsl:value-of select="./CNPJ" /></CNPJ>
											</xsl:if>
										</xsl:if>
									</xsl:when>
									<xsl:otherwise>
										<xsl:if test="./CPF = /BUSDATA/*[2]/CPF">
											<CPF><xsl:value-of select="./CPF" /></CPF>
											<NOME><xsl:value-of select="./NOME_REDUZIDO" /></NOME>
											<xsl:if test="./CNPJ">
												<CNPJ><xsl:value-of select="./CNPJ" /></CNPJ>
											</xsl:if>
										</xsl:if>
									</xsl:otherwise>
								</xsl:choose>
							</xsl:for-each>						
						</FINAL>
					</PAGADORES>
					<SACADOR_AVALISTA>
						<xsl:choose>
							<xsl:when test="$saidaBoleto/TITULO/AVALISTA/CPF">
								<CPF><xsl:value-of select="$saidaBoleto/TITULO/AVALISTA/CPF" /></CPF>
								<NOME><xsl:value-of select="$saidaBoleto/TITULO/AVALISTA/NOME" /></NOME>
							</xsl:when>
							<xsl:otherwise>
								<CNPJ><xsl:value-of select="$saidaBoleto/TITULO/AVALISTA/CNPJ" /></CNPJ>
								<RAZAO_SOCIAL><xsl:value-of select="$saidaBoleto/TITULO/AVALISTA/RAZAO_SOCIAL" /></RAZAO_SOCIAL>
								<NOME_FANTASIA><xsl:value-of select="$saidaBoleto/TITULO/AVALISTA/NOME_FANTASIA" /></NOME_FANTASIA>
							</xsl:otherwise>
						</xsl:choose>
					</SACADOR_AVALISTA>
				</INFORMACOES_BOLETO>
				<INFORMACOES_COMPLEMENTARES>
					<RECEBE_VALOR_DIVERGENTE><xsl:value-of select="$saidaBoleto/RECEBE_VALOR_DIVERGENTE" /></RECEBE_VALOR_DIVERGENTE>
					<ESPECIE><xsl:value-of select="$saidaBoleto/TITULO/ESPECIE" /></ESPECIE>
					<SITUACAO_CONTINGENCIA><xsl:value-of select="$transacao_origem/*[local-name()='CONSULTA_BOLETO_SAIDA']/SITUACAO_CONTINGENCIA" /></SITUACAO_CONTINGENCIA>
					<NUMERO_IDENTIFICACAO><xsl:value-of select="$saidaBoleto/TITULO/NUMERO_IDENTIFICACAO" /></NUMERO_IDENTIFICACAO>
					<xsl:for-each select="$saidaBoleto/BAIXAS/BAIXA_OPERACIONAL">
						<xsl:if test="position() = last()">
							<NUMERO_REFERENCIA_ATUAL><xsl:value-of select="./NUMERO_REFERENCIA_ATUAL" /></NUMERO_REFERENCIA_ATUAL>
						</xsl:if>
					</xsl:for-each>
					<NUMERO_SEQUENCIA_ATUALIZACAO><xsl:value-of select="$saidaBoleto/TITULO/REFERENCIA_ATUAL_CADASTRO" /></NUMERO_SEQUENCIA_ATUALIZACAO>
					<FORMA_LEITURA><xsl:value-of select="$entradaBoleto/FORMA_LEITURA" /></FORMA_LEITURA>
					<UNIDADE><xsl:value-of select="$entradaBoleto/UNIDADE" /></UNIDADE>
					<TIPO_BAIXA><xsl:value-of select="*[local-name()='SERVICO_SAIDA']/*[local-name()='VALIDA_REGRAS_BOLETO_SAIDA']/TIPO_BAIXA"/></TIPO_BAIXA>
					<NUMERO_PARCELA_ATUAL><xsl:value-of select="$saidaBoleto/NUMERO_PARCELA_ATUAL" /></NUMERO_PARCELA_ATUAL>
					<DATA_HORA_CONSULTA>
						<xsl:choose>
							<xsl:when test="$saidaBoleto/TITULO/DATA_HORA_SITUACAO">
								<xsl:value-of select="$saidaBoleto/TITULO/DATA_HORA_SITUACAO" />
							</xsl:when>
							<xsl:otherwise>
								<xsl:variable name="vDataHoraCanal" select="$transacao_origem/*[local-name()='CONSULTA_BOLETO_ENTRADA']/*[local-name()='HEADER']/CANAL/DATAHORA"/>
								<xsl:value-of select="concat(substring($vDataHoraCanal, 1, 4),'-')"/>
								<xsl:value-of select="concat(substring($vDataHoraCanal, 5, 2),'-')"/>
								<xsl:value-of select="substring($vDataHoraCanal, 7, 2)"/>
								<xsl:value-of select="concat('','T')"/>
								<xsl:value-of select="concat(substring($vDataHoraCanal, 9, 2),':')"/>
								<xsl:value-of select="concat(substring($vDataHoraCanal, 11, 2),':')"/>
								<xsl:value-of select="substring($vDataHoraCanal, 13, 2)"/>
							</xsl:otherwise>
						</xsl:choose>
					</DATA_HORA_CONSULTA>					
				</INFORMACOES_COMPLEMENTARES>
			</xsl:if>
		</validaboleto:VALIDA_BOLETO_SAIDA>
	</xsl:template>
</xsl:stylesheet>