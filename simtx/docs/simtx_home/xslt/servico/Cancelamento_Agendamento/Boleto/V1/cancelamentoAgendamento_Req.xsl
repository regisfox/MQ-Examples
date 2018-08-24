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
	

	<xsl:template match="/BUSDATA">
		<multicanal:SERVICO_ENTRADA 
			xmlns:multicanal="http://caixa.gov.br/sibar/multicanal/orquestracao" 
			xmlns:sibar_base="http://caixa.gov.br/sibar" 
			xmlns:controlelimite="http://caixa.gov.br/sibar/controle_limite/desfazimento"
			xmlns:detalhe="http://caixa.gov.br/sibar/consulta_transacoes_conta/detalhe"
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
				<xsl:if test="*[2]/*[local-name()='HEADER']/IDENTIFICADOR_ORIGEM/text() != ''">
					<xsl:choose>
						<xsl:when test="*[2]/*[local-name()='HEADER']/IDENTIFICADOR_ORIGEM/TERMINAL">
							<UNIDADE>
								<xsl:value-of select="substring(*[2]/*[local-name()='HEADER']/IDENTIFICADOR_ORIGEM/TERMINAL, 1, 4)"/>
							</UNIDADE>
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

			<xsl:choose>
				<xsl:when test="*[2]/*[local-name()='CANCELAMENTO_AGENDAMENTO']/SIAUT">
					<detalhe:CONSULTA_TRANSACOES_CONTA_ENTRADA>
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
						<PERIODO>
							<INICIO><xsl:value-of select="*[2]/*[local-name()='CANCELAMENTO_AGENDAMENTO']/DATA_EFETIVACAO"/></INICIO>
							<FIM><xsl:value-of select="*[2]/*[local-name()='CANCELAMENTO_AGENDAMENTO']/DATA_EFETIVACAO"/></FIM>
						</PERIODO>
						<NSU><xsl:value-of select="*[2]/*[local-name()='CANCELAMENTO_AGENDAMENTO']/SIAUT/NSU"/></NSU>
						<ACAO>CANCELAR_AGENDAMENTO</ACAO>
						<SOLICITACOES>
							<SOLICITACAO><xsl:value-of select="*[2]/*[local-name()='CANCELAMENTO_AGENDAMENTO']/SERVICO"/></SOLICITACAO>
						</SOLICITACOES>
					</detalhe:CONSULTA_TRANSACOES_CONTA_ENTRADA>
				</xsl:when>
				<xsl:otherwise>
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
						<VALOR_TRANSACAO><xsl:value-of select="*[local-name()='PAGAMENTO_BOLETO_ENTRADA']/*[local-name()='PAGAMENTO_BOLETO']/INFORMACOES_BOLETO/VALOR_PAGO"/></VALOR_TRANSACAO>
						<DATA_EFETIVACAO><xsl:value-of select="*[local-name()='PAGAMENTO_BOLETO_ENTRADA']/*[local-name()='PAGAMENTO_BOLETO']/INFORMACOES_BOLETO/DATA_EFETIVACAO"/></DATA_EFETIVACAO>
						<TIPO_AUTENTICACAO>AUTENTICACAO_SIMPLES</TIPO_AUTENTICACAO>
					</controlelimite:CONTROLE_LIMITE_ENTRADA>
				</xsl:otherwise>
			</xsl:choose>
			
		</multicanal:SERVICO_ENTRADA>
	</xsl:template>
</xsl:stylesheet>