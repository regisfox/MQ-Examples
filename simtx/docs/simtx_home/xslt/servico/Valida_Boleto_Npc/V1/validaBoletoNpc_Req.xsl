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
	
	
	<xsl:param name="nsuSimtx"/>
	<xsl:param name="pRedeTransmissora"/>
	<xsl:param name="funcionalidade"/>
	<xsl:param name="pSegmento"/>
	<xsl:param name="agendamento"/>
	
	<xsl:variable name="nuContaSidec"><xsl:value-of select="/BUSDATA/*[2]/CONTA/CONTA_SIDEC/UNIDADE" /></xsl:variable>
	<xsl:variable name="nuContaNsgd"><xsl:value-of select="/BUSDATA/*[2]/CONTA/CONTA_NSGD/UNIDADE" /></xsl:variable>
	
	<xsl:variable name="transacao_origem" select ="/BUSDATA/TRANSACAO_ORIGEM" />
	<xsl:variable name="data" select="/BUSDATA/*[2]/*[local-name()='HEADER']/CANAL/DATAHORA"/>
	
	
	<xsl:template match="/BUSDATA">
		<multicanal:SERVICO_ENTRADA 
			xmlns:multicanal="http://caixa.gov.br/sibar/multicanal/orquestracao" 
			xmlns:valida_permissao="http://caixa.gov.br/sibar/valida_permissao"
			xmlns:sibar_base="http://caixa.gov.br/sibar"
			xmlns:calculaboleto="http://caixa.gov.br/sibar/rotinas_uso_comum/calcula_boleto" 
			xmlns:valida_funcionalidades_especiais_impeditivas="http://caixa.gov.br/sibar/consulta_conta_sid09/valida_funcionalidades_especiais_impeditivas"
			xmlns:valida_conta="http://caixa.gov.br/sibar/consulta_conta_sid09"
			xmlns:consulta_conta="http://caixa.gov.br/sibar/consulta_conta_sid09"
			xmlns:controlelimite="http://caixa.gov.br/sibar/controle_limite/consulta"
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
				<xsl:choose>
					<xsl:when test="$transacao_origem/*[local-name()='CONSULTA_BOLETO_ENTRADA']/CONSULTA_BOLETO/UNIDADE">
						<UNIDADE>
							<xsl:value-of select="$transacao_origem/*[local-name()='CONSULTA_BOLETO_ENTRADA']/CONSULTA_BOLETO/UNIDADE"/>
						</UNIDADE>
					</xsl:when>
					<xsl:when test="$nuContaSidec != ''">
						<UNIDADE>
							<xsl:value-of select="substring($nuContaSidec, 1, 4)"/>
						</UNIDADE>
					</xsl:when>
					<xsl:when test="$nuContaNsgd != ''">
						<UNIDADE>
							<xsl:value-of select="substring($nuContaNsgd, 1, 4)"/>
						</UNIDADE>
					</xsl:when>
					<xsl:when test="*[2]/*[local-name()='HEADER']/IDENTIFICADOR_ORIGEM/TERMINAL">
						<UNIDADE>
							<xsl:value-of select="substring(*[2]/*[local-name()='HEADER']/IDENTIFICADOR_ORIGEM/TERMINAL, 1, 4)"/>
						</UNIDADE>
					</xsl:when>
				</xsl:choose>
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
			
			<xsl:if test="*[2]/*[local-name()='PERMISSAO'] != '' and *[2]/*[local-name()='HEADER']/MEIOENTRADA/text() != '0'">
				<valida_permissao:VALIDA_PERMISSAO_ENTRADA>
					<OPCAO><xsl:value-of select="*[2]/PERMISSAO/OPCAO" /></OPCAO>
					<CPF><xsl:value-of select="*[2]/CPF" /></CPF>
					<TOKEN>
						<ACAO><xsl:value-of select="*[2]/TOKEN/ACAO" /></ACAO>
						<SESSAO><xsl:value-of select="*[2]/TOKEN/SESSAO" /></SESSAO>
						<TOKEN_SESSAO><xsl:value-of select="*[2]/TOKEN/CODIGO_SESSAO" /></TOKEN_SESSAO>
					</TOKEN>
					<xsl:choose>
						<xsl:when test="*[2]/*[local-name()='CONTA']/CONTA_SIDEC">
							<CONTA>
								<UNIDADE><xsl:value-of select="*[2]/*[local-name()='CONTA']/CONTA_SIDEC/UNIDADE"/></UNIDADE>
								<OPERACAO><xsl:value-of select="*[2]/*[local-name()='CONTA']/CONTA_SIDEC/OPERACAO"/></OPERACAO>
								<NUMERO><xsl:value-of select="*[2]/*[local-name()='CONTA']/CONTA_SIDEC/CONTA"/></NUMERO>
								<DV><xsl:value-of select="*[2]/*[local-name()='CONTA']/CONTA_SIDEC/DV"/></DV>
							</CONTA>
						</xsl:when>
						<xsl:otherwise>
							<CONTA_NSGD>
								<UNIDADE><xsl:value-of select="*[2]/*[local-name()='CONTA']/CONTA_NSGD/UNIDADE"/></UNIDADE>
								<PRODUTO><xsl:value-of select="*[2]/*[local-name()='CONTA']/CONTA_NSGD/PRODUTO"/></PRODUTO>
								<NUMERO><xsl:value-of select="*[2]/*[local-name()='CONTA']/CONTA_NSGD/CONTA"/></NUMERO>
								<DV><xsl:value-of select="*[2]/*[local-name()='CONTA']/CONTA_NSGD/DV"/></DV>
							</CONTA_NSGD>
						</xsl:otherwise>
					</xsl:choose>
					<SERVICO><xsl:value-of select="*[2]/PERMISSAO/SERVICO_SIPER" /></SERVICO>
					<DT_MOVTO_SIPER><xsl:value-of select="*[2]/PERMISSAO/DT_MOVTO_SIPER" /></DT_MOVTO_SIPER>
				</valida_permissao:VALIDA_PERMISSAO_ENTRADA>
			</xsl:if>
			
			<xsl:variable name="entradaBoleto" select="$transacao_origem/*[local-name()='CONSULTA_BOLETO_ENTRADA']/CONSULTA_BOLETO"/>
			<xsl:variable name="saidaBoleto" select="$transacao_origem/*[local-name()='CONSULTA_BOLETO_SAIDA']/CONSULTA_BOLETO"/>
			
			<xsl:if test="$agendamento = 'sim'">
				<calculaboleto:ROTINAS_USO_COMUM_ENTRADA>
					<UNIDADE_PAGAMENTO><xsl:value-of select="$entradaBoleto/UNIDADE" /></UNIDADE_PAGAMENTO>
					<xsl:apply-templates mode="copy" select="$saidaBoleto/MODELO_CALCULO" disable-output-escaping="yes"/>
					<DATA_MOVIMENTO>
						<xsl:value-of select="concat(BUSTIME/ANO,'-')"/>
						<xsl:value-of select="concat(BUSTIME/MES,'-')"/>
						<xsl:value-of select="BUSTIME/DIA"/>
					</DATA_MOVIMENTO>
					<DATA_PAGAMENTO><xsl:value-of select="*[2]/VALIDA_BOLETO/DATA_PAGAMENTO" /></DATA_PAGAMENTO>
					<xsl:choose>
						<xsl:when test="$saidaBoleto/TITULO/DATA_VENCIMENTO">
							<DATA_VENCIMENTO><xsl:value-of select="$saidaBoleto/TITULO/DATA_VENCIMENTO" /></DATA_VENCIMENTO>
						</xsl:when>
						<xsl:otherwise>
							<DATA_VENCIMENTO><xsl:value-of select="*[2]/VALIDA_BOLETO/DATA_VENCIMENTO"/></DATA_VENCIMENTO>
						</xsl:otherwise>
					</xsl:choose>
					<DATA_LIMITE_PAGAMENTO><xsl:value-of select="$saidaBoleto/DATA_LIMITE_PAGAMENTO" /></DATA_LIMITE_PAGAMENTO>
					<JUROS>
						<xsl:apply-templates mode="copy" select="$saidaBoleto/TITULO/JUROS/TIPO" disable-output-escaping="yes"/>
						<xsl:apply-templates mode="copy" select="$saidaBoleto/TITULO/JUROS/DATA" disable-output-escaping="yes"/>
						<xsl:apply-templates mode="copy" select="$saidaBoleto/TITULO/JUROS/VALOR" disable-output-escaping="yes"/>
						<xsl:apply-templates mode="copy" select="$saidaBoleto/TITULO/JUROS/PERCENTUAL" disable-output-escaping="yes"/>
					</JUROS>
					<MULTA>
						<xsl:apply-templates mode="copy" select="$saidaBoleto/TITULO/MULTA/TIPO" disable-output-escaping="yes"/>
						<xsl:apply-templates mode="copy" select="$saidaBoleto/TITULO/MULTA/DATA" disable-output-escaping="yes"/>
						<xsl:apply-templates mode="copy" select="$saidaBoleto/TITULO/MULTA/VALOR" disable-output-escaping="yes"/>
						<xsl:apply-templates mode="copy" select="$saidaBoleto/TITULO/MULTA/PERCENTUAL" disable-output-escaping="yes"/>
					</MULTA>
					<DESCONTOS>
						<DESCONTO>
							<xsl:apply-templates mode="copy" select="$saidaBoleto/TITULO/DESCONTOS/DESCONTO/TIPO" disable-output-escaping="yes"/>
							<xsl:apply-templates mode="copy" select="$saidaBoleto/TITULO/DESCONTOS/DESCONTO/DATA" disable-output-escaping="yes"/>
							<xsl:apply-templates mode="copy" select="$saidaBoleto/TITULO/DESCONTOS/DESCONTO/VALOR" disable-output-escaping="yes"/>
							<xsl:apply-templates mode="copy" select="$saidaBoleto/TITULO/DESCONTOS/DESCONTO/PERCENTUAL" disable-output-escaping="yes"/>
						</DESCONTO>
					</DESCONTOS>
					<xsl:apply-templates mode="copy" select="$saidaBoleto/TITULO/VALOR_ABATIMENTO" disable-output-escaping="yes"/>
					<VALOR_TITULO><xsl:value-of select="$saidaBoleto/TITULO/VALOR" /></VALOR_TITULO>
					<xsl:apply-templates mode="copy" select="$saidaBoleto/FLAG_PAGAMENTO_PARCIAL" disable-output-escaping="yes"/>
					<PERCENTUAL>
						<MINIMO><xsl:value-of select="$saidaBoleto/TITULO/MINIMO/PERCENTUAL" /></MINIMO>
						<MAXIMO><xsl:value-of select="$saidaBoleto/TITULO/MAXIMO/PERCENTUAL" /></MAXIMO>
					</PERCENTUAL>
					<BAIXAS_OPERACIONAIS>
						<BAIXA_OPERACIONAL>
							<xsl:for-each select="$saidaBoleto/BAIXAS/BAIXA_OPERACIONAL">
								<DATA><xsl:value-of select="./DATA_HORA_PROCESSAMENTO" /></DATA>
								<VALOR><xsl:value-of select="./VALOR_TITULO" /></VALOR>
							</xsl:for-each>
						</BAIXA_OPERACIONAL>
					</BAIXAS_OPERACIONAIS>
					<BAIXAS_EFETIVAS>
						<BAIXA_EFETIVA>
							<xsl:for-each select="$saidaBoleto/BAIXAS/BAIXA_EFETIVA">
								<DATA><xsl:value-of select="./DATA_HORA_PROCESSAMENTO" /></DATA>
								<VALOR><xsl:value-of select="./VALOR_TITULO" /></VALOR>
							</xsl:for-each>
						</BAIXA_EFETIVA>
					</BAIXAS_EFETIVAS>
					<CALCULOS>
						<CALCULO>
							<DATA><xsl:value-of select="$saidaBoleto/CALCULOS/CALCULO_CIP/DATA_VALIDADE_CALCULO" /></DATA>
							<VALOR_JUROS><xsl:value-of select="$saidaBoleto/CALCULOS/CALCULO_CIP/VALOR_JUROS" /></VALOR_JUROS>
							<VALOR_MULTA><xsl:value-of select="$saidaBoleto/CALCULOS/CALCULO_CIP/VALOR_MULTA" /></VALOR_MULTA>
							<VALOR_DESCONTO><xsl:value-of select="$saidaBoleto/CALCULOS/CALCULO_CIP/VALOR_DESCONTO" /></VALOR_DESCONTO>
							<VALOR_TOTAL><xsl:value-of select="$saidaBoleto/CALCULOS/CALCULO_CIP/VALOR_TOTAL" /></VALOR_TOTAL>
						</CALCULO>
					</CALCULOS>
					<xsl:choose>
						<xsl:when test="$entradaBoleto/CODIGO_BARRAS">
							<CODIGO_BARRAS><xsl:value-of select="$entradaBoleto/CODIGO_BARRAS" /></CODIGO_BARRAS>
						</xsl:when>
						<xsl:when test="$saidaBoleto/TITULO/CODIGO_BARRAS">
							<CODIGO_BARRAS><xsl:value-of select="$saidaBoleto/TITULO/CODIGO_BARRAS" /></CODIGO_BARRAS>
						</xsl:when>
					</xsl:choose>
					<xsl:choose>
						<xsl:when test="$entradaBoleto/LINHA_DIGITAVEL">
							<LINHA_DIGITAVEL><xsl:value-of select="$entradaBoleto/LINHA_DIGITAVEL" /></LINHA_DIGITAVEL>
						</xsl:when>
						<xsl:when test="$saidaBoleto/TITULO/LINHA_DIGITAVEL">
							<LINHA_DIGITAVEL><xsl:value-of select="$saidaBoleto/TITULO/LINHA_DIGITAVEL" /></LINHA_DIGITAVEL>
						</xsl:when>
					</xsl:choose>
					<xsl:choose>
						<xsl:when test="*[2]/*[local-name()='HEADER']/CANAL/SIGLA = 'SIMAA'">
							<CANAL>MULTICANAL</CANAL>
						</xsl:when>
						<xsl:when test="*[2]/*[local-name()='HEADER']/CANAL/SIGLA = 'SIIBC'">
							<CANAL>INTERNET_BANKING</CANAL>
						</xsl:when>
					</xsl:choose>
				</calculaboleto:ROTINAS_USO_COMUM_ENTRADA>
			</xsl:if>
			
			<valida_conta:CONSULTA_CONTA_SID09_ENTRADA>
				<VALIDA_CONTA>
					<xsl:choose>
						<xsl:when test="*[2]/CONTA/CONTA_SIDEC">
							<TIPO_PESQUISA>2</TIPO_PESQUISA>
						</xsl:when>
						<xsl:otherwise>
							<TIPO_PESQUISA>1</TIPO_PESQUISA>
						</xsl:otherwise>
					</xsl:choose>
					<SEGMENTO><xsl:value-of select="$pSegmento"/></SEGMENTO>
					<DATA_REFERENCIA><xsl:value-of select="*[2]/HEADER/DATA_REFERENCIA"/></DATA_REFERENCIA>
					<xsl:choose>
						<xsl:when test="*[2]/CONTA/CONTA_SIDEC">
							<FLAG_VALIDA_CONTA_SIDEC>S</FLAG_VALIDA_CONTA_SIDEC>
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
						      	<NUMERO><xsl:value-of select="*[2]/CONTA/CONTA_NSGD/CONTA"/></NUMERO>
						      	<DV><xsl:value-of select="*[2]/CONTA/CONTA_NSGD/DV"/></DV>
					      	</CONTA_NSGD>
					    </xsl:otherwise>
					</xsl:choose>
				</VALIDA_CONTA>
			</valida_conta:CONSULTA_CONTA_SID09_ENTRADA>
			
			<valida_funcionalidades_especiais_impeditivas:CONSULTA_CONTA_SID09_ENTRADA>
				<SEGMENTO><xsl:value-of select="$pSegmento" /></SEGMENTO>
				<CONTA>
					<xsl:choose>
						<xsl:when test="*[2]/CONTA/CONTA_SIDEC">
							<UNIDADE><xsl:value-of select="*[2]/CONTA/CONTA_SIDEC/UNIDADE" /></UNIDADE>
							<OPERACAO><xsl:value-of select="*[2]/CONTA/CONTA_SIDEC/OPERACAO" /></OPERACAO>
							<NUMERO><xsl:value-of select="*[2]/CONTA/CONTA_SIDEC/CONTA" /></NUMERO>
							<DV><xsl:value-of select="*[2]/CONTA/CONTA_SIDEC/DV" /></DV>
						</xsl:when>
						<xsl:otherwise>
							<UNIDADE><xsl:value-of select="*[2]/CONTA/CONTA_NSGD/UNIDADE" /></UNIDADE>
							<PRODUTO><xsl:value-of select="*[2]/CONTA/CONTA_NSGD/PRODUTO" /></PRODUTO>
							<NUMERO><xsl:value-of select="*[2]/CONTA/CONTA_NSGD/CONTA" /></NUMERO>
							<DV><xsl:value-of select="*[2]/CONTA/CONTA_NSGD/DV" /></DV>
						</xsl:otherwise>
					</xsl:choose>
				</CONTA>
				
				<FUNCIONALIDADES_ESPECIAIS>
					<xsl:value-of select="$funcionalidade" disable-output-escaping="yes" />
				</FUNCIONALIDADES_ESPECIAIS>
			</valida_funcionalidades_especiais_impeditivas:CONSULTA_CONTA_SID09_ENTRADA>
			
			<consulta_conta:CONSULTA_CONTA_SID09_ENTRADA>
				<CONSULTA_CONTA>
					<SEGMENTO><xsl:value-of select="$pSegmento"/></SEGMENTO>
					<xsl:choose>
						<xsl:when test="*[2]/CONTA/CONTA_SIDEC">
							<CONTA>
						  		<UNIDADE><xsl:value-of select="*[2]/CONTA/CONTA_SIDEC/UNIDADE"/></UNIDADE>
					      		<OPERACAO><xsl:value-of select="*[2]/CONTA/CONTA_SIDEC/OPERACAO"/></OPERACAO>
					      		<NUMERO><xsl:value-of select="*[2]/CONTA/CONTA_SIDEC/CONTA"/></NUMERO>
					      		<DV><xsl:value-of select="*[2]/CONTA/CONTA_SIDEC/DV"/></DV>
					      </CONTA>
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
					<FLAG_PESQUISA_TITULAR>3</FLAG_PESQUISA_TITULAR>
				</CONSULTA_CONTA>
			</consulta_conta:CONSULTA_CONTA_SID09_ENTRADA>
			
			
			<xsl:variable name="entradaBoleto" select="$transacao_origem/*[local-name()='CONSULTA_BOLETO_ENTRADA']/CONSULTA_BOLETO"/>
			<xsl:variable name="saidaBoleto" select="$transacao_origem/*[local-name()='CONSULTA_BOLETO_SAIDA']/CONSULTA_BOLETO"/>
			
			<valida:VALIDA_REGRAS_BOLETO_ENTRADA>
				<NSU_TRANSACAO><xsl:value-of select="$nsuSimtx"/></NSU_TRANSACAO>
				<NU_CANAL>
					<xsl:choose>
						<xsl:when test="*[2]/*[local-name()='HEADER']/CANAL/SIGLA = 'SIMAA'">106</xsl:when>
						<xsl:otherwise>110</xsl:otherwise>
					</xsl:choose>
				</NU_CANAL>
				<xsl:choose>
					<xsl:when test="$saidaBoleto/TITULO/CODIGO_BARRAS">
						<CODIGO_BARRAS><xsl:value-of select="$saidaBoleto/TITULO/CODIGO_BARRAS" /></CODIGO_BARRAS>
					</xsl:when>
					<xsl:otherwise>
						<CODIGO_BARRAS><xsl:value-of select="$entradaBoleto/CODIGO_BARRAS" /></CODIGO_BARRAS>
					</xsl:otherwise>
				</xsl:choose>
				<xsl:choose>
					<xsl:when test="$saidaBoleto/FLAG_ULTIMA_PARCELA_VIAVEL">
						<ULTIMA_PARCELA_VIAVEL><xsl:value-of select="$saidaBoleto/FLAG_ULTIMA_PARCELA_VIAVEL" /></ULTIMA_PARCELA_VIAVEL>
					</xsl:when>
					<xsl:otherwise>
						<ULTIMA_PARCELA_VIAVEL>N</ULTIMA_PARCELA_VIAVEL>
					</xsl:otherwise>
				</xsl:choose>
				<xsl:choose>
					<xsl:when test="$saidaBoleto/FLAG_PAGAMENTO_PARCIAL">
						<PAGAMENTO_PARCIAL><xsl:value-of select="$saidaBoleto/FLAG_PAGAMENTO_PARCIAL" /></PAGAMENTO_PARCIAL>
					</xsl:when>
					<xsl:otherwise>
						<PAGAMENTO_PARCIAL>N</PAGAMENTO_PARCIAL>
					</xsl:otherwise>
				</xsl:choose>
				<xsl:choose>
					<xsl:when test="$saidaBoleto/QUANTIDADE_PAGAMENTO_PARCIAL_PERMITIDO">
						<QUANTIDADE_PAGAMENTO_PARCIAL_PERMITIDO><xsl:value-of select="$saidaBoleto/QUANTIDADE_PAGAMENTO_PARCIAL_PERMITIDO" /></QUANTIDADE_PAGAMENTO_PARCIAL_PERMITIDO>
					</xsl:when>
					<xsl:otherwise>
						<QUANTIDADE_PAGAMENTO_PARCIAL_PERMITIDO>1</QUANTIDADE_PAGAMENTO_PARCIAL_PERMITIDO>
					</xsl:otherwise>
				</xsl:choose>
				<xsl:choose>
					<xsl:when test="$saidaBoleto/NUMERO_PARCELA_ATUAL">
						<NUMERO_PARCELA_ATUAL><xsl:value-of select="$saidaBoleto/NUMERO_PARCELA_ATUAL" /></NUMERO_PARCELA_ATUAL>
					</xsl:when>
					<xsl:otherwise>
						<NUMERO_PARCELA_ATUAL>1</NUMERO_PARCELA_ATUAL>
					</xsl:otherwise>
				</xsl:choose>
				<xsl:choose>
					<xsl:when test="$saidaBoleto/RECEBE_VALOR_DIVERGENTE">
						<RECEBE_VALOR_DIVERGENTE><xsl:value-of select="$saidaBoleto/RECEBE_VALOR_DIVERGENTE" /></RECEBE_VALOR_DIVERGENTE>
					</xsl:when>
					<xsl:otherwise>
						<RECEBE_VALOR_DIVERGENTE>NAO_ACEITAR</RECEBE_VALOR_DIVERGENTE>
					</xsl:otherwise>
				</xsl:choose>
				<xsl:choose>
					<xsl:when test="$saidaBoleto/TITULO/ESPECIE">
						<ESPECIE><xsl:value-of select="$saidaBoleto/TITULO/ESPECIE" /></ESPECIE>
					</xsl:when>
					<xsl:when test="$saidaBoleto/TIPO_BOLETO">
						<ESPECIE><xsl:value-of select="$saidaBoleto/TIPO_BOLETO" /></ESPECIE>
					</xsl:when>
					<xsl:otherwise>
						<ESPECIE>OUTROS</ESPECIE>
					</xsl:otherwise>
				</xsl:choose>
				<xsl:choose>
					<xsl:when test="$transacao_origem/*[local-name()='CONSULTA_BOLETO_SAIDA']/SITUACAO_CONTINGENCIA">
						<TIPO_CONTINGENCIA><xsl:value-of select="$transacao_origem/*[local-name()='CONSULTA_BOLETO_SAIDA']/SITUACAO_CONTINGENCIA" /></TIPO_CONTINGENCIA>
					</xsl:when>
					<xsl:otherwise>
						<TIPO_CONTINGENCIA>SEM_CONTINGENCIA</TIPO_CONTINGENCIA>
					</xsl:otherwise>
				</xsl:choose>
				<xsl:choose>
					<xsl:when test="$transacao_origem/*[local-name()='CONSULTA_COBRANCA_BANCARIA_SAIDA']/CONSULTA_BOLETO_PAGAMENTO/FLAG_NOVA_COBRANCA">
						<NOVA_PLATAFORMA_DE_COBRANCA><xsl:value-of select="$transacao_origem/*[local-name()='CONSULTA_COBRANCA_BANCARIA_SAIDA']/CONSULTA_BOLETO_PAGAMENTO/FLAG_NOVA_COBRANCA" /></NOVA_PLATAFORMA_DE_COBRANCA>
					</xsl:when>
					<xsl:otherwise>
						<NOVA_PLATAFORMA_DE_COBRANCA>N</NOVA_PLATAFORMA_DE_COBRANCA>
					</xsl:otherwise>
				</xsl:choose>
				<DATA_PAGAMENTO><xsl:value-of select="*[2]/VALIDA_BOLETO/DATA_PAGAMENTO"/></DATA_PAGAMENTO>
				<xsl:choose>
					<xsl:when test="$saidaBoleto/CALCULOS/CALCULO_CAIXA/DATA_VENCIMENTO_UTIL">
						<DATA_VENCIMENTO_UTIL><xsl:value-of select="$saidaBoleto/CALCULOS/CALCULO_CAIXA/DATA_VENCIMENTO_UTIL" /></DATA_VENCIMENTO_UTIL>
					</xsl:when>
					<xsl:when test="$saidaBoleto/TITULO/DATA_VENCIMENTO">
						<DATA_VENCIMENTO_UTIL><xsl:value-of select="$saidaBoleto/TITULO/DATA_VENCIMENTO" /></DATA_VENCIMENTO_UTIL>
					</xsl:when>
					<xsl:otherwise>
						<DATA_VENCIMENTO_UTIL><xsl:value-of select="*[2]/VALIDA_BOLETO/DATA_VENCIMENTO"/></DATA_VENCIMENTO_UTIL>
					</xsl:otherwise>
				</xsl:choose>
				<xsl:choose>
					<xsl:when test="$saidaBoleto/TITULO/MINIMO/VALOR">
						<VALOR_MINIMO_CONSULTA><xsl:value-of select="$saidaBoleto/TITULO/MINIMO/VALOR" /></VALOR_MINIMO_CONSULTA>
					</xsl:when>
					<xsl:otherwise>
						<VALOR_MINIMO_CONSULTA>0.0</VALOR_MINIMO_CONSULTA>
					</xsl:otherwise>
				</xsl:choose>
				<xsl:choose>
					<xsl:when test="$saidaBoleto/TITULO/MAXIMO/VALOR">
						<VALOR_MAXIMO_CONSULTA><xsl:value-of select="$saidaBoleto/TITULO/MAXIMO/VALOR" /></VALOR_MAXIMO_CONSULTA>
					</xsl:when>
					<xsl:otherwise>
						<VALOR_MAXIMO_CONSULTA>0.0</VALOR_MAXIMO_CONSULTA>
					</xsl:otherwise>
				</xsl:choose>
				<xsl:choose>
					<xsl:when test="$saidaBoleto/CALCULOS/CALCULO_CAIXA/VALOR_TOTAL">
						<VALOR_TOTAL_CALCULADO><xsl:value-of select="$saidaBoleto/CALCULOS/CALCULO_CAIXA/VALOR_TOTAL" /></VALOR_TOTAL_CALCULADO>
					</xsl:when>
					<xsl:otherwise>
						<VALOR_TOTAL_CALCULADO>0.0</VALOR_TOTAL_CALCULADO>
					</xsl:otherwise>
				</xsl:choose>
				<VALOR_PAGAR><xsl:value-of select="*[2]/VALIDA_BOLETO/VALOR_PAGAR"/></VALOR_PAGAR>
				<xsl:choose>
					<xsl:when test="$transacao_origem/*[local-name()='CONSULTA_BOLETO_SAIDA']/CONSULTA_BOLETO/TITULO/VALOR">
						<VALOR_NOMINAL>
							<xsl:value-of select="$transacao_origem/*[local-name()='CONSULTA_BOLETO_SAIDA']/CONSULTA_BOLETO/TITULO/VALOR"/>
						</VALOR_NOMINAL>
					</xsl:when>
					<xsl:otherwise>
						<VALOR_NOMINAL>0.0</VALOR_NOMINAL>
					</xsl:otherwise>
				</xsl:choose>
			</valida:VALIDA_REGRAS_BOLETO_ENTRADA>
			
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
			    <VALOR_TRANSACAO><xsl:value-of select="*[2]/VALIDA_BOLETO/VALOR_PAGAR"/></VALOR_TRANSACAO>
			    <DATA_EFETIVACAO><xsl:value-of select="*[2]/VALIDA_BOLETO/DATA_PAGAMENTO"/></DATA_EFETIVACAO>
			    <TIPO_AUTENTICACAO>AUTENTICACAO_SIMPLES</TIPO_AUTENTICACAO>
			</controlelimite:CONTROLE_LIMITE_ENTRADA>
			
		</multicanal:SERVICO_ENTRADA>
	</xsl:template>
</xsl:stylesheet>