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
	<xsl:variable name="pCodBarras"><xsl:value-of select="/BUSDATA/*[2]/*[local-name()='CONSULTA_BOLETO']/TITULO/CODIGO_BARRAS"/></xsl:variable>
	<xsl:variable name="pLinhaDigitavel"><xsl:value-of select="/BUSDATA/*[2]/*[local-name()='CONSULTA_BOLETO']/TITULO/LINHA_DIGITAVEL"/></xsl:variable>
	<xsl:variable name="pValor"><xsl:value-of select="/BUSDATA/*[2]/*[local-name()='CONSULTA_BOLETO']/TITULO/VALOR"/></xsl:variable>
	<xsl:variable name="pValorPagar"><xsl:value-of select="/BUSDATA/*[2]/*[local-name()='PAGAMENTO_BOLETO']/VALOR_PAGAR"/></xsl:variable>
	<xsl:variable name="pEspecie"><xsl:value-of select="/BUSDATA/*[2]/*[local-name()='CONSULTA_BOLETO']/TITULO/ESPECIE"/></xsl:variable>
	
	<xsl:template match="/BUSDATA">
		<multicanal:SERVICO_ENTRADA 
			xmlns:multicanal="http://caixa.gov.br/sibar/multicanal/orquestracao" 
			xmlns:sibar_base="http://caixa.gov.br/sibar" 
			xmlns:controlelimite="http://caixa.gov.br/sibar/controle_limite/efetivacao" 
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
			
			<xsl:if test="*[2]/*[local-name()='ASSINATURA_SIMPLES'] != '' 
					and *[2]/*[local-name()='HEADER']/MEIOENTRADA != '0'">
				<assinaturasimples:VALIDA_PERMISSAO_ENTRADA>
					<CPF><xsl:value-of select="*[2]/CPF"/></CPF>
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
					<ASSINATURA><xsl:value-of select="*[2]/*[local-name()='ASSINATURA_SIMPLES']/ASSINATURA"/></ASSINATURA>
					<NSU_ORIGEM><xsl:value-of select="$nsuSimtx"/></NSU_ORIGEM>
					<SERVICO><xsl:value-of select="*[2]/*[local-name()='ASSINATURA_SIMPLES']/SERVICO_SIPER" /></SERVICO>
					<xsl:variable name="data" select="*[2]/*[local-name()='HEADER']/CANAL/DATAHORA"/>
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
				<VALOR_TRANSACAO><xsl:value-of select="*[2]/*[local-name()='PAGAMENTO_BOLETO']/INFORMACOES_BOLETO/VALOR_PAGO"/></VALOR_TRANSACAO>
				<DATA_EFETIVACAO><xsl:value-of select="*[2]/*[local-name()='PAGAMENTO_BOLETO']/INFORMACOES_BOLETO/DATA_EFETIVACAO"/></DATA_EFETIVACAO>
				<TIPO_AUTENTICACAO>AUTENTICACAO_SIMPLES</TIPO_AUTENTICACAO>
			</controlelimite:CONTROLE_LIMITE_ENTRADA>
			
		</multicanal:SERVICO_ENTRADA>
	</xsl:template>
</xsl:stylesheet>