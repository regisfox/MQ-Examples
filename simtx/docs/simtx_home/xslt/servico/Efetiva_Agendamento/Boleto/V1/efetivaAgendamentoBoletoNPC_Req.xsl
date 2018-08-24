<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

	<xsl:output omit-xml-declaration="yes"/>
	<xsl:strip-space elements="*" />

	<xsl:template match="node()|@*">
		<xsl:copy>
			<xsl:apply-templates select="node()|@*" />
		</xsl:copy>
	</xsl:template>
	
	<xsl:param name="nsuSimtx"/>
	<xsl:param name="isUltimaExecucao"/>
	<xsl:param name="isBoletoCaixa"/>
	<xsl:variable name="dataBustime"		select="/BUSDATA/BUSTIME/ANO"/>
	<xsl:variable name="data" 				select="/BUSDATA/*[2]/*[local-name()='HEADER']/CANAL/DATAHORA"/>
	<xsl:variable name="pCodBarras"><xsl:value-of select="/BUSDATA/*[2]/*[local-name()='PAGAMENTO_BOLETO']/INFORMACOES_BOLETO/CODIGO_BARRAS"/></xsl:variable>
	<xsl:variable name="pLinhaDigitavel"><xsl:value-of select="/BUSDATA/*[2]/*[local-name()='PAGAMENTO_BOLETO']/INFORMACOES_BOLETO/LINHA_DIGITAVEL"/></xsl:variable>
	<xsl:variable name="pValorCalculado"><xsl:value-of select="/BUSDATA/*[2]/*[local-name()='PAGAMENTO_BOLETO']/INFORMACOES_BOLETO/VALOR_CALCULADO"/></xsl:variable>
	<xsl:variable name="pValorPagar"><xsl:value-of select="/BUSDATA/*[2]/*[local-name()='PAGAMENTO_BOLETO']/INFORMACOES_BOLETO/VALOR_PAGO"/></xsl:variable>
	<xsl:variable name="pEspecie"><xsl:value-of select="/BUSDATA/*[2]/*[local-name()='PAGAMENTO_BOLETO']/INFORMACOES_COMPLEMENTARES/ESPECIE"/></xsl:variable>
	<xsl:variable name="pDataEfetiva" 		select="/BUSDATA/*[2]/*[local-name()='PAGAMENTO_BOLETO']/INFORMACOES_BOLETO/DATA_EFETIVACAO"/>
	<xsl:variable name="pDataVencimento" 	select="/BUSDATA/*[2]/*[local-name()='PAGAMENTO_BOLETO']/INFORMACOES_BOLETO/DATA_VENCIMENTO"/>
	<xsl:variable name="pDataEfetivaN" 		select="format-number(translate($pDataEfetiva, '-', ''),0)" /> 
	<xsl:variable name="pDataVencimentoN" 	select="format-number(translate($pDataVencimento, '-', ''),0)" /> 
	
	<xsl:template match="/BUSDATA">
		<multicanal:SERVICO_ENTRADA 
			xmlns:multicanal="http://caixa.gov.br/sibar/multicanal/orquestracao" 
			xmlns:sibar_base="http://caixa.gov.br/sibar" 
			xmlns:solicitacaodebito="http://caixa.gov.br/sibar/solicitacao_debito" 
			xmlns:manutencaocobrancabancaria="http://caixa.gov.br/sibar/manutencao_cobranca_bancaria/pagamento" 
			xmlns:consultacobrancabancaria="http://caixa.gov.br/sibar/consulta_cobranca_bancaria/pagamento"
			xmlns:rotinasusocomum="http://caixa.gov.br/sibar/rotinas_uso_comum/calcula_boleto" 
			xmlns:valida="http://caixa.gov.br/simtx/valida_boleto/regras/v1/ns"
			xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" 
			xsi:schemaLocation="http://caixa.gov.br/simtx/multicanal/orquestracao Multicanal_Orquestracao.xsd">
			<sibar_base:HEADER>
				<VERSAO>1.0</VERSAO>
				<xsl:if test="*[2]/*[local-name()='HEADER']/USUARIO_SERVICO/text() != ''">
					<USUARIO_SERVICO>
						<xsl:value-of select="*[2]/*[local-name()='HEADER']/USUARIO_SERVICO"/>
					</USUARIO_SERVICO>
				</xsl:if>
				<xsl:if test="*[2]/*[local-name()='HEADER']/USUARIO/text() != ''">
					<USUARIO>
						<xsl:value-of select="*[2]/*[local-name()='HEADER']/USUARIO"/>
					</USUARIO>
				</xsl:if>
				<OPERACAO>SERVICO_MIGRADO</OPERACAO>
				<SISTEMA_ORIGEM>
					<xsl:value-of select="*[2]/*[local-name()='HEADER']/CANAL/SIGLA"/>
				</SISTEMA_ORIGEM>
				<UNIDADE>
					<xsl:value-of select="*[2]/PAGAMENTO_BOLETO/INFORMACOES_COMPLEMENTARES/UNIDADE"/>
				</UNIDADE>
				<xsl:if test="*[2]/*[local-name()='HEADER']/IDENTIFICADOR_ORIGEM/text() != ''">
					<xsl:choose>
						<xsl:when test="*[2]/*[local-name()='HEADER']/IDENTIFICADOR_ORIGEM/TERMINAL">
							<IDENTIFICADOR_ORIGEM>
								<xsl:value-of select="*[2]/*[local-name()='HEADER']/IDENTIFICADOR_ORIGEM/TERMINAL"/>
							</IDENTIFICADOR_ORIGEM>
						</xsl:when>
						<xsl:when test="*[2]/*[local-name()='HEADER']/IDENTIFICADOR_ORIGEM/ENDERECO_IP">
							<IDENTIFICADOR_ORIGEM>
								<xsl:value-of select="*[2]/*[local-name()='HEADER']/IDENTIFICADOR_ORIGEM/ENDERECO_IP"/>
							</IDENTIFICADOR_ORIGEM>
						</xsl:when>
						<xsl:otherwise>
							<IDENTIFICADOR_ORIGEM>
								<xsl:value-of select="*[2]/*[local-name()='HEADER']/IDENTIFICADOR_ORIGEM/CODIGO_MAQUINA"/>
							</IDENTIFICADOR_ORIGEM>
						</xsl:otherwise>
					</xsl:choose>
				</xsl:if>
				<DATA_HORA>
					<xsl:value-of select="BUSTIME/ANO" />
					<xsl:value-of select="BUSTIME/MES" />
					<xsl:value-of select="BUSTIME/DIA" />
					<xsl:value-of select="BUSTIME/HORA" />
					<xsl:value-of select="BUSTIME/MIN" />
					<xsl:value-of select="BUSTIME/SEG" />
				</DATA_HORA>
			</sibar_base:HEADER> 
			
			<xsl:if test="($isUltimaExecucao = 'false') or ($isUltimaExecucao = 'true' and isBoletoCaixa = 'false')">
			
				<consultacobrancabancaria:CONSULTA_COBRANCA_BANCARIA_ENTRADA>
					<CONSULTA_BOLETO_PAGAMENTO>
						<xsl:choose>
							<xsl:when test="*[2]/*[local-name()='HEADER']/CANAL/SIGLA = 'SIMAA'">
								<CANAL>MULTICANAL</CANAL>
							</xsl:when>
							<xsl:when test="*[2]/*[local-name()='HEADER']/CANAL/SIGLA = 'SIIBC'">
								<CANAL>INTERNET_BANKING</CANAL>
							</xsl:when>
						</xsl:choose>
						<CODIGO_BARRAS><xsl:value-of select="$pCodBarras"/></CODIGO_BARRAS>
						<DATA_MOVIMENTO>
							<xsl:value-of select="concat(BUSTIME/ANO,'-')"/>
							<xsl:value-of select="concat(BUSTIME/MES,'-')"/>
							<xsl:value-of select="BUSTIME/DIA"/>
						</DATA_MOVIMENTO>
						<LINHA_DIGITAVEL><xsl:value-of select="$pLinhaDigitavel"/></LINHA_DIGITAVEL>
					</CONSULTA_BOLETO_PAGAMENTO>
				</consultacobrancabancaria:CONSULTA_COBRANCA_BANCARIA_ENTRADA>
				
				<valida:VALIDA_REGRAS_BOLETO_ENTRADA>
					<NSU_TRANSACAO><xsl:value-of select="$nsuSimtx"/></NSU_TRANSACAO>
					<NU_CANAL>
						<xsl:choose>
							<xsl:when test="*[2]/*[local-name()='HEADER']/CANAL/SIGLA = 'SIMAA'">106</xsl:when>
							<xsl:otherwise>110</xsl:otherwise>
						</xsl:choose>
					</NU_CANAL>
					<CODIGO_BARRAS><xsl:value-of select="$pCodBarras"/></CODIGO_BARRAS>
					<ULTIMA_PARCELA_VIAVEL>N</ULTIMA_PARCELA_VIAVEL>
					<PAGAMENTO_PARCIAL>N</PAGAMENTO_PARCIAL>
					<QUANTIDADE_PAGAMENTO_PARCIAL_PERMITIDO>1</QUANTIDADE_PAGAMENTO_PARCIAL_PERMITIDO>
					<NUMERO_PARCELA_ATUAL>1</NUMERO_PARCELA_ATUAL>
					<xsl:choose>
						<xsl:when test="*[2]/*[local-name()='PAGAMENTO_BOLETO']/INFORMACOES_COMPLEMENTARES/RECEBE_VALOR_DIVERGENTE">
							<RECEBE_VALOR_DIVERGENTE><xsl:value-of select="*[2]/*[local-name()='PAGAMENTO_BOLETO']/INFORMACOES_COMPLEMENTARES/RECEBE_VALOR_DIVERGENTE" /></RECEBE_VALOR_DIVERGENTE>
						</xsl:when>
						<xsl:otherwise>
							<RECEBE_VALOR_DIVERGENTE>NAO_ACEITAR</RECEBE_VALOR_DIVERGENTE>
						</xsl:otherwise>
					</xsl:choose>
					<xsl:choose>
						<xsl:when test="*[2]/*[local-name()='PAGAMENTO_BOLETO']/INFORMACOES_COMPLEMENTARES/ESPECIE">
							<ESPECIE><xsl:value-of select="*[2]/*[local-name()='PAGAMENTO_BOLETO']/INFORMACOES_COMPLEMENTARES/ESPECIE" /></ESPECIE>
						</xsl:when>
						<xsl:otherwise>
							<ESPECIE>OUTROS</ESPECIE>
						</xsl:otherwise>
					</xsl:choose>
					<xsl:choose>
						<xsl:when test="*[2]/*[local-name()='PAGAMENTO_BOLETO']/INFORMACOES_COMPLEMENTARES/SITUACAO_CONTINGENCIA">
							<TIPO_CONTINGENCIA><xsl:value-of select="*[2]/*[local-name()='PAGAMENTO_BOLETO']/INFORMACOES_COMPLEMENTARES/SITUACAO_CONTINGENCIA" /></TIPO_CONTINGENCIA>
						</xsl:when>
						<xsl:otherwise>
							<TIPO_CONTINGENCIA>SEM_CONTINGENCIA</TIPO_CONTINGENCIA>
						</xsl:otherwise>
					</xsl:choose>
					<NOVA_PLATAFORMA_DE_COBRANCA>N</NOVA_PLATAFORMA_DE_COBRANCA>
					<DATA_PAGAMENTO>
						<xsl:value-of select="concat(BUSTIME/ANO,'-')"/>
						<xsl:value-of select="concat(BUSTIME/MES,'-')"/>
						<xsl:value-of select="BUSTIME/DIA"/>
					</DATA_PAGAMENTO>
					<DATA_VENCIMENTO_UTIL><xsl:value-of select="*[2]/*[local-name()='PAGAMENTO_BOLETO']/INFORMACOES_BOLETO/DATA_VENCIMENTO"/></DATA_VENCIMENTO_UTIL>
					<VALOR_MINIMO_CONSULTA>0.0</VALOR_MINIMO_CONSULTA>
					<VALOR_MAXIMO_CONSULTA>0.0</VALOR_MAXIMO_CONSULTA>
					<xsl:choose>
						<xsl:when test="*[2]/*[local-name()='PAGAMENTO_BOLETO']/INFORMACOES_BOLETO/VALOR_CALCULADO">
							<VALOR_TOTAL_CALCULADO><xsl:value-of select="*[2]/*[local-name()='PAGAMENTO_BOLETO']/INFORMACOES_BOLETO/VALOR_CALCULADO"/></VALOR_TOTAL_CALCULADO>
						</xsl:when>
						<xsl:otherwise>
							<VALOR_TOTAL_CALCULADO>0.0</VALOR_TOTAL_CALCULADO>
						</xsl:otherwise>
					</xsl:choose>
					<VALOR_PAGAR><xsl:value-of select="*[2]/*[local-name()='PAGAMENTO_BOLETO']/INFORMACOES_BOLETO/VALOR_PAGO"/></VALOR_PAGAR>
					<VALOR_NOMINAL>
						<xsl:choose>
							<xsl:when test="*[2]/*[local-name()='PAGAMENTO_BOLETO']/INFORMACOES_BOLETO/VALOR_NOMINAL">
								<xsl:value-of select="*[2]/*[local-name()='PAGAMENTO_BOLETO']/INFORMACOES_BOLETO/VALOR_NOMINAL"/>
							</xsl:when>
							<xsl:otherwise>0.0</xsl:otherwise>
						</xsl:choose>
					</VALOR_NOMINAL>
				</valida:VALIDA_REGRAS_BOLETO_ENTRADA>
				
			</xsl:if>
			
			<solicitacaodebito:SOLICITACAO_DEBITO_ENTRADA>
				<TIPO_SOLICITACAO>SOLICITACAO</TIPO_SOLICITACAO>
				<SIGLA_SERVICO>PBQ</SIGLA_SERVICO>
				<xsl:choose>
					<xsl:when test="*[2]/*[local-name()='CONTA']/CONTA_SIDEC">
						<CONTA>
							<UNIDADE><xsl:value-of select="*[2]/*[local-name()='CONTA']/CONTA_SIDEC/UNIDADE"/></UNIDADE>
							<PRODUTO><xsl:value-of select="*[2]/*[local-name()='CONTA']/CONTA_SIDEC/OPERACAO"/></PRODUTO>
							<NUMERO><xsl:value-of select="*[2]/*[local-name()='CONTA']/CONTA_SIDEC/CONTA"/></NUMERO>
							<DV><xsl:value-of select="*[2]/*[local-name()='CONTA']/CONTA_SIDEC/DV"/></DV>
						</CONTA>
					</xsl:when>
					<xsl:otherwise>
						<CONTA>
							<UNIDADE><xsl:value-of select="*[2]/*[local-name()='CONTA']/CONTA_NSGD/UNIDADE"/></UNIDADE>
							<PRODUTO><xsl:value-of select="*[2]/*[local-name()='CONTA']/CONTA_NSGD/PRODUTO"/></PRODUTO>
							<NUMERO><xsl:value-of select="*[2]/*[local-name()='CONTA']/CONTA_NSGD/CONTA"/></NUMERO>
							<DV><xsl:value-of select="*[2]/*[local-name()='CONTA']/CONTA_NSGD/DV"/></DV>
						</CONTA>
					</xsl:otherwise>
				</xsl:choose>
				<VALOR_TRANSACAO><xsl:value-of select="*[2]/*[local-name()='PAGAMENTO_BOLETO']/INFORMACOES_BOLETO/VALOR_PAGO"/></VALOR_TRANSACAO>
				<DATA_EFETIVACAO><xsl:value-of select="*[2]/*[local-name()='PAGAMENTO_BOLETO']/INFORMACOES_BOLETO/DATA_EFETIVACAO"/></DATA_EFETIVACAO>
				<NSU_CANAL><xsl:value-of select="$nsuSimtx"/></NSU_CANAL>
				<xsl:choose>
					<xsl:when test="substring($pCodBarras,1,3) = 104">
						<SISTEMA_DESTINO>SICOB</SISTEMA_DESTINO>
					</xsl:when>
					<xsl:otherwise>
						<SISTEMA_DESTINO>SICCP</SISTEMA_DESTINO>
					</xsl:otherwise>
				</xsl:choose>
				<TIPO_AUTENTICACAO>AUTENTICACAO_SIMPLES</TIPO_AUTENTICACAO>
				<xsl:if test="*[2]/*[local-name()='HEADER']/IDENTIFICADOR_ORIGEM/TERMINAL/text() != ''">
					<TERMINAL>
						<CODIGO><xsl:value-of select="*[2]/*[local-name()='HEADER']/IDENTIFICADOR_ORIGEM/TERMINAL"/></CODIGO>
					</TERMINAL>
				</xsl:if>
			</solicitacaodebito:SOLICITACAO_DEBITO_ENTRADA>
			
			<manutencaocobrancabancaria:MANUTENCAO_COBRANCA_BANCARIA_ENTRADA>
				<BAIXA_OPERACIONAL>
					<NUMERO_IDENTIFICACAO_TITULO><xsl:value-of select="*[2]/*[local-name()='PAGAMENTO_BOLETO']/INFORMACOES_COMPLEMENTARES/NUMERO_IDENTIFICACAO"/></NUMERO_IDENTIFICACAO_TITULO>
					<BENEFICIARIO><xsl:value-of select="*[2]/*[local-name()='PAGAMENTO_BOLETO']/INFORMACOES_BOLETO/BENEFICIARIOS/ORIGINAL/CODIGO_BENEFICIARIO_ORIGINAL"/></BENEFICIARIO>
					<NOSSO_NUMERO><xsl:value-of select="*[2]/*[local-name()='PAGAMENTO_BOLETO']/INFORMACOES_BOLETO/NOSSO_NUMERO"/></NOSSO_NUMERO>
					<TIPO><xsl:value-of select="*[2]/*[local-name()='PAGAMENTO_BOLETO']/INFORMACOES_COMPLEMENTARES/TIPO_BAIXA"/></TIPO>
					
					<xsl:choose>
						<xsl:when test="*[2]/*[local-name()='PAGAMENTO_BOLETO']/INFORMACOES_COMPLEMENTARES/NUMERO_SEQUENCIA_ATUALIZACAO">
							<NUMERO_REFERENCIA_CADASTRO><xsl:value-of select="*[2]/*[local-name()='PAGAMENTO_BOLETO']/INFORMACOES_COMPLEMENTARES/NUMERO_SEQUENCIA_ATUALIZACAO"/></NUMERO_REFERENCIA_CADASTRO>
						</xsl:when>
						<xsl:otherwise>
							<NUMERO_REFERENCIA_CADASTRO>0</NUMERO_REFERENCIA_CADASTRO>
						</xsl:otherwise>
					</xsl:choose>
					
					<xsl:choose>
						<xsl:when test="*[2]/*[local-name()='PAGAMENTO_BOLETO']/INFORMACOES_COMPLEMENTARES/NUMERO_REFERENCIA_ATUAL">
							<NUMERO_REFERENCIA_ATUAL><xsl:value-of select="*[2]/*[local-name()='PAGAMENTO_BOLETO']/INFORMACOES_COMPLEMENTARES/NUMERO_REFERENCIA_ATUAL"/></NUMERO_REFERENCIA_ATUAL>
						</xsl:when>
						<xsl:otherwise>
							<NUMERO_REFERENCIA_ATUAL>0</NUMERO_REFERENCIA_ATUAL>
						</xsl:otherwise>
					</xsl:choose>
					
					<DATA_HORA_PROCESSAMENTO>
						<xsl:value-of select="concat(BUSTIME/ANO,'-')"/>
						<xsl:value-of select="concat(BUSTIME/MES,'-')"/>
						<xsl:value-of select="BUSTIME/DIA"/>
						<xsl:value-of select="concat('','T')"/>
						<xsl:value-of select="concat(BUSTIME/HORA,':')"/>
						<xsl:value-of select="concat(BUSTIME/MIN,':')"/>
						<xsl:value-of select="BUSTIME/SEG"/>
					</DATA_HORA_PROCESSAMENTO>
					<DATA_PROCESSAMENTO>
						<xsl:value-of select="concat(BUSTIME/ANO,'-')"/>
						<xsl:value-of select="concat(BUSTIME/MES,'-')"/>
						<xsl:value-of select="BUSTIME/DIA"/>
					</DATA_PROCESSAMENTO>
					<VALOR><xsl:value-of select="*[2]/*[local-name()='PAGAMENTO_BOLETO']/INFORMACOES_BOLETO/VALOR_PAGO"/></VALOR>
					<CODIGO_BARRAS><xsl:value-of select="$pCodBarras"/></CODIGO_BARRAS>
					<PORTADOR>
						<xsl:choose>
							<xsl:when test="*[2]/*[local-name()='PAGAMENTO_BOLETO']/INFORMACOES_BOLETO/PAGADORES/FINAL/CPF">
								<CPF><xsl:value-of select="*[2]/*[local-name()='PAGAMENTO_BOLETO']/INFORMACOES_BOLETO/PAGADORES/FINAL/CPF"/></CPF>
							</xsl:when>
							<xsl:otherwise>
								<CNPJ><xsl:value-of select="*[2]/*[local-name()='PAGAMENTO_BOLETO']/INFORMACOES_BOLETO/PAGADORES/FINAL/CNPJ"/></CNPJ>
							</xsl:otherwise>
						</xsl:choose>
					</PORTADOR>
					<xsl:choose>
						<xsl:when test="*[2]/*[local-name()='PAGAMENTO_BOLETO']/INFORMACOES_COMPLEMENTARES/UNIDADE">
							<UNIDADE_RECEBEDORA><xsl:value-of select="*[2]/*[local-name()='PAGAMENTO_BOLETO']/INFORMACOES_COMPLEMENTARES/UNIDADE"/></UNIDADE_RECEBEDORA>
						</xsl:when>
						<xsl:otherwise>
							<xsl:choose>
								<xsl:when test="*[2]/CONTA/CONTA_SIDEC">
									<UNIDADE_RECEBEDORA><xsl:value-of select="*[2]/CONTA/CONTA_SIDEC/UNIDADE"/></UNIDADE_RECEBEDORA>
								</xsl:when>
								<xsl:otherwise>
									<UNIDADE_RECEBEDORA><xsl:value-of select="*[2]/CONTA/CONTA_NSGD/UNIDADE"/></UNIDADE_RECEBEDORA>
								</xsl:otherwise>
							</xsl:choose>
						</xsl:otherwise>
					</xsl:choose>
					<xsl:choose>
						<xsl:when test="*[2]/*[local-name()='HEADER']/CANAL/SIGLA = 'SIMAA'">
							<CANAL>MULTICANAL</CANAL>
							<CANAL_PAGAMENTO>AUTO_ATENDIMENTO</CANAL_PAGAMENTO>
						</xsl:when>
						<xsl:when test="*[2]/*[local-name()='HEADER']/CANAL/SIGLA = 'SIIBC'">
							<CANAL>INTERNET_BANKING</CANAL>
							<CANAL_PAGAMENTO>INTERNET</CANAL_PAGAMENTO>
						</xsl:when>
					</xsl:choose>
					<MEIO_PAGAMENTO>DEBITO_CONTA</MEIO_PAGAMENTO>
					<xsl:choose>
						<xsl:when test="*[2]/*[local-name()='PAGAMENTO_BOLETO']/INFORMACOES_COMPLEMENTARES/SITUACAO_CONTINGENCIA/text() = 'SEM_CONTINGENCIA'">
							<FLAG_OPERACAO_CONTINGENCIA>N</FLAG_OPERACAO_CONTINGENCIA>
						</xsl:when>
						<xsl:when test="*[2]/*[local-name()='PAGAMENTO_BOLETO']/INFORMACOES_COMPLEMENTARES/SITUACAO_CONTINGENCIA/text() = 'CONTINGENCIA_CAIXA'">
							<FLAG_OPERACAO_CONTINGENCIA>N</FLAG_OPERACAO_CONTINGENCIA>
						</xsl:when>
						<xsl:otherwise>
							<FLAG_OPERACAO_CONTINGENCIA>S</FLAG_OPERACAO_CONTINGENCIA>
						</xsl:otherwise>
					</xsl:choose>
					<DATA_MOVIMENTO>
						<xsl:value-of select="concat(BUSTIME/ANO,'-')"/>
						<xsl:value-of select="concat(BUSTIME/MES,'-')"/>
						<xsl:value-of select="BUSTIME/DIA"/>
					</DATA_MOVIMENTO>
					<NSU_ORIGEM><xsl:value-of select="$nsuSimtx"/></NSU_ORIGEM>
					<FLAG_CIP>N</FLAG_CIP>
					<xsl:choose>
						<xsl:when test="*[2]/*[local-name()='PAGAMENTO_BOLETO']/INFORMACOES_COMPLEMENTARES/SITUACAO_CONTINGENCIA/text() = 'SEM_CONTINGENCIA'">
							<FLAG_CONTINGENCIA_CAIXA>N</FLAG_CONTINGENCIA_CAIXA>
						</xsl:when>
						<xsl:when test="*[2]/*[local-name()='PAGAMENTO_BOLETO']/INFORMACOES_COMPLEMENTARES/SITUACAO_CONTINGENCIA/text() = 'CONTINGENCIA_CAIXA'">
							<FLAG_CONTINGENCIA_CAIXA>S</FLAG_CONTINGENCIA_CAIXA>
						</xsl:when>
						<xsl:otherwise>
							<FLAG_CONTINGENCIA_CAIXA>N</FLAG_CONTINGENCIA_CAIXA>
						</xsl:otherwise>
					</xsl:choose>
				</BAIXA_OPERACIONAL>
			</manutencaocobrancabancaria:MANUTENCAO_COBRANCA_BANCARIA_ENTRADA>
					
			
		</multicanal:SERVICO_ENTRADA>
	</xsl:template>
</xsl:stylesheet>