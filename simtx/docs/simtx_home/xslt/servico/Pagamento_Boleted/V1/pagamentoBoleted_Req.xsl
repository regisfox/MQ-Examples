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
	
	<xsl:param name="pRedeTransmissora"/>
	<xsl:param name="nsuSimtx"/>
	<xsl:variable name="pCodBarras" 		select="/BUSDATA/*[2]/*[local-name()='PAGAMENTO_BOLETO']/INFORMACOES_BOLETO/CODIGO_BARRAS"/>
	<xsl:variable name="pValorCalculado" 	select="/BUSDATA/*[2]/*[local-name()='PAGAMENTO_BOLETO']/INFORMACOES_BOLETO/VALOR_CALCULADO"/>
	<xsl:variable name="pValorPagar" 		select="/BUSDATA/*[2]/*[local-name()='PAGAMENTO_BOLETO']/INFORMACOES_BOLETO/VALOR_PAGO"/>
	<xsl:variable name="pEspecie" 			select="/BUSDATA/*[2]/*[local-name()='PAGAMENTO_BOLETO']/INFORMACOES_COMPLEMENTARES/ESPECIE"/>
	<xsl:variable name="data" 				select="/BUSDATA/*[2]/*[local-name()='HEADER']/CANAL/DATAHORA"/>
	<xsl:variable name="pDataEfetiva" 		select="/BUSDATA/*[2]/*[local-name()='PAGAMENTO_BOLETO']/INFORMACOES_BOLETO/DATA_EFETIVACAO"/>
	<xsl:variable name="pDataVencimento" 	select="/BUSDATA/*[2]/*[local-name()='PAGAMENTO_BOLETO']/INFORMACOES_BOLETO/DATA_VENCIMENTO"/>
	<xsl:variable name="pDataEfetivaN" 		select="format-number(translate($pDataEfetiva, '-', ''),0)" /> 
	<xsl:variable name="pDataVencimentoN" 	select="format-number(translate($pDataVencimento, '-', ''),0)" /> 
	
	
	<xsl:template match="/BUSDATA">
		<multicanal:SERVICO_ENTRADA 
			xmlns:multicanal="http://caixa.gov.br/sibar/multicanal/orquestracao" 
			xmlns:consultacobrancabancaria="http://caixa.gov.br/sibar/consulta_cobranca_bancaria/pagamento" 
			xmlns:sibar_base="http://caixa.gov.br/sibar" 
			xmlns:controlelimite="http://caixa.gov.br/sibar/controle_limite/efetivacao" 
			xmlns:solicitacaodebito="http://caixa.gov.br/sibar/solicitacao_debito"
			xmlns:efetivated="http://caixa.gov.br/sibar/ted/manutencao_ted"  
			xmlns:manutencaocobrancabancaria="http://caixa.gov.br/sibar/manutencao_cobranca_bancaria/pagamento" 
			xmlns:assinaturasimples="http://caixa.gov.br/sibar/valida_permissao/assinatura_simples" 
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

			<xsl:if test="*[2]/*[local-name()='ASSINATURA_SIMPLES'] != '' and *[2]/*[local-name()='HEADER']/MEIOENTRADA != '0'">
				<assinaturasimples:VALIDA_PERMISSAO_ENTRADA>
					<CPF><xsl:value-of select="*[2]/CPF"/></CPF>
					<xsl:choose>
						<xsl:when test="*[2]/CONTA/CONTA_SIDEC">
							<CONTA_SIDEC>
						  		<UNIDADE><xsl:value-of select="*[2]/CONTA/CONTA_SIDEC/UNIDADE"/></UNIDADE>
					      		<OPERACAO><xsl:value-of select="*[2]/CONTA/CONTA_SIDEC/OPERACAO"/></OPERACAO>
					      		<NUMERO><xsl:value-of select="*[2]/CONTA/CONTA_SIDEC/CONTA"/></NUMERO>
					      		<DV><xsl:value-of select="*[2]/CONTA/CONTA_SIDEC/DV"/></DV>
					      </CONTA_SIDEC>
						</xsl:when>
						<xsl:otherwise>
							<CONTA_NSGD>
					  	  		<UNIDADE><xsl:value-of select="*[2]/CONTA/CONTA_NSGD/UNIDADE"/></UNIDADE>
					      		<PRODUTO><xsl:value-of select="*[2]/CONTA/CONTA_NSGD/PRODUTO"/></PRODUTO>
					      		<NUMERO><xsl:value-of select="*[2]/CONTA/CONTA_NSGD/CONTA"/></NUMERO>
					      		<DV><xsl:value-of select="*[2]/CONTA/CONTA_NSGD/DV"/></DV>
					      </CONTA_NSGD>
					    </xsl:otherwise>
					</xsl:choose>
					<ASSINATURA><xsl:value-of select="*[2]/*[local-name()='ASSINATURA_SIMPLES']/ASSINATURA"/></ASSINATURA>
					<NSU_ORIGEM><xsl:value-of select="$nsuSimtx"/></NSU_ORIGEM>
					<SERVICO><xsl:value-of select="*[2]/*[local-name()='ASSINATURA_SIMPLES']/SERVICO_SIPER" /></SERVICO>
					<DATA_TRANSACAO>
						<xsl:value-of select="concat(substring($data, 1, 4),'-')"/>
						<xsl:value-of select="concat(substring($data, 5, 2),'-')"/>
						<xsl:value-of select="substring($data, 7, 2)"/>
					</DATA_TRANSACAO>
					<APELIDO><xsl:value-of select="*[2]/*[local-name()='ASSINATURA_SIMPLES']/APELIDO"/></APELIDO>
					<ENDERECO_IP><xsl:value-of select="*[2]/*[local-name()='HEADER']/IDENTIFICADOR_ORIGEM/ENDERECO_IP"/></ENDERECO_IP>
					<TOKEN><xsl:value-of select="*[2]/*[local-name()='TOKEN']/CODIGO_SESSAO" /></TOKEN>
					<SESSION_ID><xsl:value-of select="*[2]/*[local-name()='TOKEN']/SESSAO" /></SESSION_ID>
					<DISPOSITIVO>
						<SISTEMA_OPERACIONAL><xsl:value-of select="*[2]/*[local-name()='ASSINATURA_SIMPLES']/DISPOSITIVO/SISTEMA_OPERACIONAL" /></SISTEMA_OPERACIONAL>
						<CODIGO><xsl:value-of select="*[2]/*[local-name()='ASSINATURA_SIMPLES']/DISPOSITIVO/CODIGO" /></CODIGO>
					</DISPOSITIVO>
				</assinaturasimples:VALIDA_PERMISSAO_ENTRADA>
			</xsl:if>
			
			<controlelimite:CONTROLE_LIMITE_ENTRADA>
				<TIPO_PAGAMENTO>BOLETO</TIPO_PAGAMENTO>
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
				<VALOR_TRANSACAO><xsl:value-of select="$pValorPagar"/></VALOR_TRANSACAO>
				<DATA_EFETIVACAO><xsl:value-of select="$pDataEfetiva"/></DATA_EFETIVACAO>
				<TIPO_AUTENTICACAO>AUTENTICACAO_SIMPLES</TIPO_AUTENTICACAO>
			</controlelimite:CONTROLE_LIMITE_ENTRADA>
			
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
				<VALOR_TRANSACAO><xsl:value-of select="$pValorPagar"/></VALOR_TRANSACAO>
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
				<TERMINAL>
					<CODIGO><xsl:value-of select="*[2]/*[local-name()='HEADER']/IDENTIFICADOR_ORIGEM/TERMINAL"/></CODIGO>
				</TERMINAL>
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
					<VALOR><xsl:value-of select="$pValorPagar"/></VALOR>
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
					<xsl:choose>
						<xsl:when test="*[2]/*[local-name()='PAGAMENTO_BOLETO']/INFORMACOES_BOLETO/DATA_MOVIMENTO">
							<DATA_MOVIMENTO>
								<xsl:value-of select="*[2]/*[local-name()='PAGAMENTO_BOLETO']/INFORMACOES_BOLETO/DATA_MOVIMENTO"/>
							</DATA_MOVIMENTO>
						</xsl:when>
						<xsl:otherwise>
							<DATA_MOVIMENTO>
								<xsl:value-of select="concat(substring($data, 1, 4),'-')"/>
								<xsl:value-of select="concat(substring($data, 5, 2),'-')"/>
								<xsl:value-of select="substring($data, 7, 2)"/>
							</DATA_MOVIMENTO>
						</xsl:otherwise>
					</xsl:choose>
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
			
			<efetivated:EFETIVA_TED>
				<COMUM>
					<SISTEMA_DESTINO></SISTEMA_DESTINO>
					<NSU_ORIGEM><xsl:value-of select="$nsuSimtx"/></NSU_ORIGEM>
					<DATA_TRANSACAO></DATA_TRANSACAO>
					<REDE_TRANSMISSORA></REDE_TRANSMISSORA>
					<NUMERO_PV_ORIGEM></NUMERO_PV_ORIGEM>
					<FORMA_RECEBIMENTO></FORMA_RECEBIMENTO>
					<CODIGO_MENSAGEM></CODIGO_MENSAGEM>
				</COMUM>
				<DADOS_TRC>
					<DADOS_SACADO>
						<DADOS_BANCARIOS>
							<CODIGO_COMPE_INSTITUICAO_FINANCEIRA></CODIGO_COMPE_INSTITUICAO_FINANCEIRA>
							<CODIGO_ISPB_INSTITUICAO_FINANCEIRA></CODIGO_ISPB_INSTITUICAO_FINANCEIRA>
							<AGENCIA></AGENCIA>
						</DADOS_BANCARIOS>
						<DADOS_PESSOA>
							<TIPO></TIPO>
							<NUMERO_DOCUMENTO></NUMERO_DOCUMENTO>
						</DADOS_PESSOA>
					</DADOS_SACADO>
					<DADOS_CEDENTE>
						<DADOS_BANCARIOS>
							<CODIGO_COMPE_INSTITUICAO_FINANCEIRA></CODIGO_COMPE_INSTITUICAO_FINANCEIRA>
							<CODIGO_ISPB_INSTITUICAO_FINANCEIRA></CODIGO_ISPB_INSTITUICAO_FINANCEIRA>
							<AGENCIA></AGENCIA>
						</DADOS_BANCARIOS>
						<DADOS_PESSOA>
							<TIPO></TIPO>
							<NUMERO_DOCUMENTO></NUMERO_DOCUMENTO>
						</DADOS_PESSOA>
					</DADOS_CEDENTE>
					<CODIGO_BARRAS>
						<TIPO></TIPO>
						<NUMERO></NUMERO>
					</CODIGO_BARRAS>
					<VALORES>
						<LANCAMENTO></LANCAMENTO>
						<DOCUMENTO></DOCUMENTO>
						<DESCONTO_ABATIMENTO></DESCONTO_ABATIMENTO>
						<JUROS></JUROS>
						<MULTA></MULTA>
						<OUTROS_ACRESCIMOS></OUTROS_ACRESCIMOS>
					</VALORES>
					<CANAL_PAGAMENTO></CANAL_PAGAMENTO>
					<CODIGO_IDENTIFICADOR_TRANSFERENCIA></CODIGO_IDENTIFICADOR_TRANSFERENCIA>
					<HISTORICO></HISTORICO>
					<DATA_MOVIMENTO></DATA_MOVIMENTO>
				</DADOS_TRC>
			</efetivated:EFETIVA_TED>
		</multicanal:SERVICO_ENTRADA>
	</xsl:template>
</xsl:stylesheet>