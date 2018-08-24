<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://caixa.gov.br/simtx/multicanal/orquestracao Multicanal_Orquestracao.xsd"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

	<xsl:output omit-xml-declaration="yes"/>
	<xsl:strip-space elements="*" />

	<xsl:template match="node()|@*">
		<xsl:copy>
			<xsl:apply-templates select="node()|@*" />
		</xsl:copy>
	</xsl:template>
	
	<xsl:param name="pRedeTransmissora" />
	<xsl:param name="nsuSimtx"/>
	<xsl:param name="nsuTransacao"/>
	<xsl:param name="dataTransacao"/>
	<xsl:param name="nsuEfetivacao"/>
	
	<xsl:variable name="transacao_origem" select ="/BUSDATA/TRANSACAO_ORIGEM" />
	<xsl:variable name="pCodBarras"><xsl:value-of select="/BUSDATA/*[local-name()='PAGAMENTO_BOLETO_ENTRADA']/PAGAMENTO_BOLETO/INFORMACOES_BOLETO/CODIGO_BARRAS"/></xsl:variable>
	
	<xsl:template match="/BUSDATA">
		<multicanal:SERVICO_ENTRADA 
			xmlns:multicanal="http://caixa.gov.br/sibar/multicanal/orquestracao" 
			xmlns:consultacobrancabancaria="http://caixa.gov.br/sibar/consulta_cobranca_bancaria/pagamento" 
			xmlns:sibar_base="http://caixa.gov.br/sibar" 
			xmlns:controlelimite="http://caixa.gov.br/sibar/controle_limite/efetivacao" 
			xmlns:solicitacaodebito="http://caixa.gov.br/sibar/solicitacao_debito" 
			xmlns:manutencaocobrancabancaria="http://caixa.gov.br/sibar/manutencao_cobranca_bancaria/pagamento" 
			xmlns:assinaturasimples="http://caixa.gov.br/sibar/valida_permissao/assinatura_simples" 
			xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" 
			xsi:schemaLocation="http://caixa.gov.br/simtx/multicanal/orquestracao Multicanal_Orquestracao.xsd">
			<sibar_base:HEADER>
				<VERSAO>1.0</VERSAO>
				<USUARIO_SERVICO>SMTXSD01</USUARIO_SERVICO>
				<USUARIO><xsl:value-of select="mtxTransacaoContaEntrada/codigoUsuario" /></USUARIO>
				<OPERACAO>SERVICO_MIGRADO</OPERACAO>
				<SISTEMA_ORIGEM>SIMTX</SISTEMA_ORIGEM>
				<xsl:if test="*[local-name()='PAGAMENTO_BOLETO_ENTRADA']/*[local-name()='HEADER']/IDENTIFICADOR_ORIGEM/text() != ''">
					<xsl:choose>
						<xsl:when test="*[local-name()='PAGAMENTO_BOLETO_ENTRADA']/*[local-name()='HEADER']/IDENTIFICADOR_ORIGEM/TERMINAL">
							<UNIDADE>
								<xsl:value-of select="substring(*[local-name()='PAGAMENTO_BOLETO_ENTRADA']/*[local-name()='HEADER']/IDENTIFICADOR_ORIGEM/TERMINAL, 1, 4)"/>
							</UNIDADE>
							<IDENTIFICADOR_ORIGEM>
								<xsl:value-of select="*[local-name()='PAGAMENTO_BOLETO_ENTRADA']/*[local-name()='HEADER']/IDENTIFICADOR_ORIGEM/TERMINAL"/>
							</IDENTIFICADOR_ORIGEM>
						</xsl:when>
						<xsl:when test="*[local-name()='PAGAMENTO_BOLETO_ENTRADA']/*[local-name()='HEADER']/IDENTIFICADOR_ORIGEM/ENDERECO_IP">
							<IDENTIFICADOR_ORIGEM>
								<xsl:value-of select="*[local-name()='PAGAMENTO_BOLETO_ENTRADA']/*[local-name()='HEADER']/IDENTIFICADOR_ORIGEM/ENDERECO_IP"/>
							</IDENTIFICADOR_ORIGEM>
						</xsl:when>
						<xsl:otherwise>
							<IDENTIFICADOR_ORIGEM>
								<xsl:value-of select="*[local-name()='PAGAMENTO_BOLETO_ENTRADA']/*[local-name()='HEADER']/IDENTIFICADOR_ORIGEM/CODIGO_MAQUINA"/>
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
			
			<controlelimite:CONTROLE_LIMITE_ENTRADA>
				<TIPO_PAGAMENTO>BOLETO</TIPO_PAGAMENTO>
				<xsl:choose>
					<xsl:when test="*[local-name()='PAGAMENTO_BOLETO_ENTRADA']/*[local-name()='CONTA']/CONTA_SIDEC">
						<CONTA>
							<UNIDADE><xsl:value-of select="*[local-name()='PAGAMENTO_BOLETO_ENTRADA']/*[local-name()='CONTA']/CONTA_SIDEC/UNIDADE"/></UNIDADE>
							<PRODUTO><xsl:value-of select="*[local-name()='PAGAMENTO_BOLETO_ENTRADA']/*[local-name()='CONTA']/CONTA_SIDEC/OPERACAO"/></PRODUTO>
							<NUMERO><xsl:value-of select="*[local-name()='PAGAMENTO_BOLETO_ENTRADA']/*[local-name()='CONTA']/CONTA_SIDEC/CONTA"/></NUMERO>
							<DV><xsl:value-of select="*[local-name()='PAGAMENTO_BOLETO_ENTRADA']/*[local-name()='CONTA']/CONTA_SIDEC/DV"/></DV>
						</CONTA>
					</xsl:when>
					<xsl:otherwise>
						<CONTA>
							<UNIDADE><xsl:value-of select="*[local-name()='PAGAMENTO_BOLETO_ENTRADA']/*[local-name()='CONTA']/CONTA_NSGD/UNIDADE"/></UNIDADE>
							<PRODUTO><xsl:value-of select="*[local-name()='PAGAMENTO_BOLETO_ENTRADA']/*[local-name()='CONTA']/CONTA_NSGD/PRODUTO"/></PRODUTO>
							<NUMERO><xsl:value-of select="*[local-name()='PAGAMENTO_BOLETO_ENTRADA']/*[local-name()='CONTA']/CONTA_NSGD/CONTA"/></NUMERO>
							<DV><xsl:value-of select="*[local-name()='PAGAMENTO_BOLETO_ENTRADA']/*[local-name()='CONTA']/CONTA_NSGD/DV"/></DV>
						</CONTA>
					</xsl:otherwise>
				</xsl:choose>
				<VALOR_TRANSACAO><xsl:value-of select="*[local-name()='PAGAMENTO_BOLETO_ENTRADA']/*[local-name()='PAGAMENTO_BOLETO']/INFORMACOES_BOLETO/VALOR_PAGO"/></VALOR_TRANSACAO>
				<DATA_EFETIVACAO><xsl:value-of select="*[local-name()='PAGAMENTO_BOLETO_ENTRADA']/*[local-name()='PAGAMENTO_BOLETO']/INFORMACOES_BOLETO/DATA_EFETIVACAO"/></DATA_EFETIVACAO>
				<TIPO_AUTENTICACAO>AUTENTICACAO_SIMPLES</TIPO_AUTENTICACAO>
			</controlelimite:CONTROLE_LIMITE_ENTRADA>
			
			<solicitacaodebito:SOLICITACAO_DEBITO_ENTRADA>
				<TIPO_SOLICITACAO>DESFAZIMENTO</TIPO_SOLICITACAO>
				<SIGLA_SERVICO>PBQ</SIGLA_SERVICO>
				<xsl:choose>
					<xsl:when test="*[local-name()='PAGAMENTO_BOLETO_ENTRADA']/*[local-name()='CONTA']/CONTA_SIDEC">
						<CONTA>
							<UNIDADE><xsl:value-of select="*[local-name()='PAGAMENTO_BOLETO_ENTRADA']/*[local-name()='CONTA']/CONTA_SIDEC/UNIDADE"/></UNIDADE>
							<PRODUTO><xsl:value-of select="*[local-name()='PAGAMENTO_BOLETO_ENTRADA']/*[local-name()='CONTA']/CONTA_SIDEC/OPERACAO"/></PRODUTO>
							<NUMERO><xsl:value-of select="*[local-name()='PAGAMENTO_BOLETO_ENTRADA']/*[local-name()='CONTA']/CONTA_SIDEC/CONTA"/></NUMERO>
							<DV><xsl:value-of select="*[local-name()='PAGAMENTO_BOLETO_ENTRADA']/*[local-name()='CONTA']/CONTA_SIDEC/DV"/></DV>
						</CONTA>
					</xsl:when>
					<xsl:otherwise>
						<CONTA>
							<UNIDADE><xsl:value-of select="*[local-name()='PAGAMENTO_BOLETO_ENTRADA']/*[local-name()='CONTA']/CONTA_NSGD/UNIDADE"/></UNIDADE>
							<PRODUTO><xsl:value-of select="*[local-name()='PAGAMENTO_BOLETO_ENTRADA']/*[local-name()='CONTA']/CONTA_NSGD/PRODUTO"/></PRODUTO>
							<NUMERO><xsl:value-of select="*[local-name()='PAGAMENTO_BOLETO_ENTRADA']/*[local-name()='CONTA']/CONTA_NSGD/CONTA"/></NUMERO>
							<DV><xsl:value-of select="*[local-name()='PAGAMENTO_BOLETO_ENTRADA']/*[local-name()='CONTA']/CONTA_NSGD/DV"/></DV>
						</CONTA>
					</xsl:otherwise>
				</xsl:choose>
				<VALOR_TRANSACAO><xsl:value-of select="*[local-name()='PAGAMENTO_BOLETO_ENTRADA']/*[local-name()='PAGAMENTO_BOLETO']/INFORMACOES_BOLETO/VALOR_PAGO"/></VALOR_TRANSACAO>
				<DATA_EFETIVACAO><xsl:value-of select="*[local-name()='PAGAMENTO_BOLETO_ENTRADA']/*[local-name()='PAGAMENTO_BOLETO']/INFORMACOES_BOLETO/DATA_EFETIVACAO"/></DATA_EFETIVACAO>
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
				<xsl:if test="*[local-name()='PAGAMENTO_BOLETO_ENTRADA']/*[local-name()='HEADER']/IDENTIFICADOR_ORIGEM/TERMINAL/text() != ''">
					<TERMINAL>
						<CODIGO><xsl:value-of select="*[local-name()='PAGAMENTO_BOLETO_ENTRADA']/*[local-name()='HEADER']/IDENTIFICADOR_ORIGEM/TERMINAL"/></CODIGO>
					</TERMINAL>
				</xsl:if>
			    <FAVORECIDO>
			    <xsl:choose>
			    <!-- Escolha entre { CPF | CNPJ }  -->
					<xsl:when test="*[local-name()='PAGAMENTO_BOLETO_ENTRADA']/PAGAMENTO_BOLETO/INFORMACOES_BOLETO/BENEFICIARIOS/ORIGINAL/CPF/text() != ' '">
						<CPF><xsl:value-of select="*[local-name()='PAGAMENTO_BOLETO_ENTRADA']/PAGAMENTO_BOLETO/INFORMACOES_BOLETO/BENEFICIARIOS/ORIGINAL/CPF"/></CPF>
					</xsl:when>
					<xsl:otherwise test="*[local-name()='PAGAMENTO_BOLETO_ENTRADA']/PAGAMENTO_BOLETO/INFORMACOES_BOLETO/BENEFICIARIOS/ORIGINAL/CNPJ/text() != ' '">
						<CNPJ><xsl:value-of select="*[local-name()='PAGAMENTO_BOLETO_ENTRADA']/PAGAMENTO_BOLETO/INFORMACOES_BOLETO/BENEFICIARIOS/ORIGINAL/CNPJ"/></CNPJ>
					</xsl:otherwise>
				</xsl:choose>
			    </FAVORECIDO>
			    <DADOS_CONFIRMACAO>
			     	<!-- Informações Tela -->
			     	<NSU_TRANSACAO><xsl:value-of select="$transacao_origem/*[local-name()='SOLICITACAO_DEBITO_SAIDA']/NSU_TRANSACAO"/></NSU_TRANSACAO>			    
			      	<DATA_TRANSACAO>
						<xsl:value-of select="concat(BUSTIME/ANO,'-')"/>
						<xsl:value-of select="concat(BUSTIME/MES,'-')"/>
						<xsl:value-of select="BUSTIME/DIA"/>
					</DATA_TRANSACAO>
			      	<NSU_EFETIVACAO><xsl:value-of select="$transacao_origem/*[local-name()='SOLICITACAO_DEBITO_SAIDA']/NSU_EFETIVACAO"/></NSU_EFETIVACAO>
			    </DADOS_CONFIRMACAO>
			</solicitacaodebito:SOLICITACAO_DEBITO_ENTRADA>
			
			<manutencaocobrancabancaria:MANUTENCAO_COBRANCA_BANCARIA_ENTRADA>
				<CANCELAMENTO_BAIXA_OPERACIONAL>
					<BENEFICIARIO><xsl:value-of select="*[local-name()='PAGAMENTO_BOLETO_ENTRADA']/PAGAMENTO_BOLETO/INFORMACOES_BOLETO/BENEFICIARIOS/ORIGINAL/CODIGO_BENEFICIARIO_ORIGINAL"/></BENEFICIARIO>
					<NOSSO_NUMERO><xsl:value-of select="*[local-name()='PAGAMENTO_BOLETO_ENTRADA']/PAGAMENTO_BOLETO/INFORMACOES_BOLETO/NOSSO_NUMERO"/></NOSSO_NUMERO>
					<CODIGO_BARRAS><xsl:value-of select="$pCodBarras"/></CODIGO_BARRAS>
					<IDENTIFICACAO_BAIXA_OPERACIONAL></IDENTIFICACAO_BAIXA_OPERACIONAL>
					<UNIDADE_RECEBEDORA><xsl:value-of select="*[local-name()='PAGAMENTO_BOLETO_ENTRADA']/PAGAMENTO_BOLETO/INFORMACOES_COMPLEMENTARES/UNIDADE"/></UNIDADE_RECEBEDORA>
					<xsl:choose>
						<xsl:when test="*[local-name()='PAGAMENTO_BOLETO_ENTRADA']/*[local-name()='HEADER']/CANAL/SIGLA = 'SIMAA'">
							<CANAL>MULTICANAL</CANAL>
						</xsl:when>
						<xsl:when test="*[local-name()='PAGAMENTO_BOLETO_ENTRADA']/*[local-name()='HEADER']/CANAL/SIGLA = 'SIIBC'">
							<CANAL>INTERNET_BANKING</CANAL>
						</xsl:when>
					</xsl:choose>
					<DATA_HORA_CANCELAMENTO>
						<xsl:value-of select="concat(BUSTIME/ANO,'-')"/>
						<xsl:value-of select="concat(BUSTIME/MES,'-')"/>
						<xsl:value-of select="BUSTIME/DIA"/>
						<xsl:value-of select="concat('','T')"/>
						<xsl:value-of select="concat(BUSTIME/HORA,':')"/>
						<xsl:value-of select="concat(BUSTIME/MIN,':')"/>
						<xsl:value-of select="BUSTIME/SEG"/>
					</DATA_HORA_CANCELAMENTO>
					<DATA_MOVIMENTO>
						<xsl:value-of select="concat(BUSTIME/ANO,'-')"/>
						<xsl:value-of select="concat(BUSTIME/MES,'-')"/>
						<xsl:value-of select="BUSTIME/DIA"/>
					</DATA_MOVIMENTO>
					<NSU_ORIGEM><xsl:value-of select="mtxTransacaoContaEntrada/nuNsuTransacaoRefMtx016"/></NSU_ORIGEM>			
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
				</CANCELAMENTO_BAIXA_OPERACIONAL>
			</manutencaocobrancabancaria:MANUTENCAO_COBRANCA_BANCARIA_ENTRADA>
		</multicanal:SERVICO_ENTRADA>
	</xsl:template>
</xsl:stylesheet>