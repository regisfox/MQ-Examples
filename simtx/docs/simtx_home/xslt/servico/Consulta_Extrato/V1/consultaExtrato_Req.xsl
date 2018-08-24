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
	
	<xsl:param name="pRedeTransmissora" />
	
	<xsl:template match="/BUSDATA">
		<multicanal:SERVICO_ENTRADA
			xmlns:multicanal="http://caixa.gov.br/sibar/multicanal/orquestracao"
			xmlns:sibar_base="http://caixa.gov.br/sibar" 
			xmlns:valida_cartao="http://caixa.gov.br/sibar/valida_cartao"
			xmlns:consulta="http://caixa.gov.br/sibar/extrato/consulta" 
			xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
			xsi:schemaLocation="http://caixa.gov.br/simtx/multicanal/orquestracao Multicanal_Orquestracao.xsd">
			<sibar_base:HEADER>
				<VERSAO>1.0</VERSAO>
				<xsl:if test="*[2]/*[local-name()='HEADER']/USUARIO_SERVICO/text() != ''">
					<USUARIO_SERVICO><xsl:value-of select="*[2]/*[local-name()='HEADER']/USUARIO_SERVICO" /></USUARIO_SERVICO>
				</xsl:if>
				<xsl:if test="*[2]/*[local-name()='HEADER']/USUARIO/text() != ''">
					<USUARIO><xsl:value-of select="*[2]/*[local-name()='HEADER']/USUARIO" /></USUARIO>
				</xsl:if>
				<OPERACAO>SERVICO_MIGRADO</OPERACAO>
				<SISTEMA_ORIGEM><xsl:value-of select="*[2]/*[local-name()='HEADER']/CANAL/SIGLA" /></SISTEMA_ORIGEM>
				<UNIDADE><xsl:value-of select="substring(*[2]/*[local-name()='HEADER']/IDENTIFICADOR_ORIGEM/TERMINAL, 1, 4)"/></UNIDADE>
				<xsl:if test="*[2]/*[local-name()='HEADER']/IDENTIFICADOR_ORIGEM/text() != ''">
					<xsl:choose>
						<xsl:when test="*[2]/*[local-name()='HEADER']/IDENTIFICADOR_ORIGEM/TERMINAL">
							<IDENTIFICADOR_ORIGEM><xsl:value-of select="*[2]/*[local-name()='HEADER']/IDENTIFICADOR_ORIGEM/TERMINAL" /></IDENTIFICADOR_ORIGEM>
						</xsl:when>
						<xsl:when test="*[2]/*[local-name()='HEADER']/IDENTIFICADOR_ORIGEM/ENDERECO_IP">
							<IDENTIFICADOR_ORIGEM><xsl:value-of select="*[2]/*[local-name()='HEADER']/IDENTIFICADOR_ORIGEM/ENDERECO_IP" /></IDENTIFICADOR_ORIGEM>
						</xsl:when>
						<xsl:otherwise>
							<IDENTIFICADOR_ORIGEM><xsl:value-of select="*[2]/*[local-name()='HEADER']/IDENTIFICADOR_ORIGEM/CODIGO_MAQUINA" /></IDENTIFICADOR_ORIGEM>
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
			
			<valida_cartao:VALIDA_CARTAO_ENTRADA>
				<CARTAO_COMERCIAL>
					<CARTAO>
						<TRILHA><xsl:value-of select="*[2]/*[local-name()='CARTAO']/TRILHA" /></TRILHA>
						<TP_ENTRADA><xsl:value-of select="*[2]/*[local-name()='CARTAO']/TP_ENTRADA" /></TP_ENTRADA>
						<NUMERO_SEQUENCIAL><xsl:value-of select="*[2]/*[local-name()='CARTAO']/NUMERO_SEQUENCIAL" /></NUMERO_SEQUENCIAL>
						<INFORMACOES_CHIP><xsl:value-of select="*[2]/*[local-name()='CARTAO']/INFORMACOES_CHIP" /></INFORMACOES_CHIP>
					</CARTAO>
					<xsl:if test="*[2]/*[local-name()='HEADER']/IDENTIFICADOR_ORIGEM/TERMINAL/text() != ''">
						<TERMINAL><xsl:value-of select="*[2]/*[local-name()='HEADER']/IDENTIFICADOR_ORIGEM/TERMINAL" /></TERMINAL>
					</xsl:if>
					<REDE_TRANSMISSORA><xsl:value-of select="$pRedeTransmissora" /></REDE_TRANSMISSORA>
				</CARTAO_COMERCIAL>
			</valida_cartao:VALIDA_CARTAO_ENTRADA>

			<consulta:EXTRATO_ENTRADA>
				<REDE_TRANSMISSORA><xsl:value-of select="$pRedeTransmissora" /></REDE_TRANSMISSORA>
				<CONTA>
					<xsl:choose>
						<xsl:when test="*[2]/*[local-name()='CONTA']/CONTA_SIDEC">
							<UNIDADE><xsl:value-of select="*[2]/*[local-name()='CONTA']/CONTA_SIDEC/UNIDADE"/></UNIDADE>
							<PRODUTO><xsl:value-of select="*[2]/*[local-name()='CONTA']/CONTA_SIDEC/OPERACAO"/></PRODUTO>
							<NUMERO><xsl:value-of select="*[2]/*[local-name()='CONTA']/CONTA_SIDEC/CONTA"/></NUMERO>
							<DV><xsl:value-of select="*[2]/*[local-name()='CONTA']/CONTA_SIDEC/DV"/></DV>
						</xsl:when>
						<xsl:when test="*[2]/*[local-name()='CONTA']/CONTA_NSGD">
							<UNIDADE><xsl:value-of select="*[2]/*[local-name()='CONTA']/CONTA_NSGD/UNIDADE"/></UNIDADE>
							<PRODUTO><xsl:value-of select="*[2]/*[local-name()='CONTA']/CONTA_NSGD/PRODUTO"/></PRODUTO>
							<NUMERO><xsl:value-of select="*[2]/*[local-name()='CONTA']/CONTA_NSGD/CONTA"/></NUMERO>
							<DV><xsl:value-of select="*[2]/*[local-name()='CONTA']/CONTA_NSGD/DV"/></DV>
						</xsl:when>
						<xsl:otherwise>
							<UNIDADE>0</UNIDADE>
							<PRODUTO>0</PRODUTO>
							<NUMERO>0</NUMERO>
							<DV>0</DV>
						</xsl:otherwise>
					</xsl:choose>
				</CONTA>
				<PERIODO_CONSULTA>
					<DATA_INICIO><xsl:value-of select="*[2]/*[local-name()='CONSULTA_EXTRATO']/PERIODO_CONSULTA/DATA_INICIO"/></DATA_INICIO>
					<DATA_FIM><xsl:value-of select="*[2]/*[local-name()='CONSULTA_EXTRATO']/PERIODO_CONSULTA/DATA_FIM"/></DATA_FIM>
				</PERIODO_CONSULTA>
				<TIPO_CONSULTA>
						<FLAG_DADOS_CADASTRAIS><xsl:value-of select="*[2]/*[local-name()='CONSULTA_EXTRATO']/TIPO_CONSULTA/FLAG_DADOS_CADASTRAIS"/></FLAG_DADOS_CADASTRAIS>										
						<FLAG_LANCAMENTOS><xsl:value-of select="*[2]/*[local-name()='CONSULTA_EXTRATO']/TIPO_CONSULTA/FLAG_LANCAMENTOS"/></FLAG_LANCAMENTOS>
						<FLAG_SALDOS><xsl:value-of select="*[2]/*[local-name()='CONSULTA_EXTRATO']/TIPO_CONSULTA/FLAG_SALDOS"/></FLAG_SALDOS>
						<FLAG_COMPRAS><xsl:value-of select="*[2]/*[local-name()='CONSULTA_EXTRATO']/TIPO_CONSULTA/FLAG_COMPRAS"/></FLAG_COMPRAS>
						<FLAG_AGENDAMENTO><xsl:value-of select="*[2]/*[local-name()='CONSULTA_EXTRATO']/TIPO_CONSULTA/FLAG_AGENDAMENTO"/></FLAG_AGENDAMENTO>
						<FLAG_VALORES_BLOQUEADOS><xsl:value-of select="*[2]/*[local-name()='CONSULTA_EXTRATO']/TIPO_CONSULTA/FLAG_VALORES_BLOQUEADOS"/></FLAG_VALORES_BLOQUEADOS>
						<FLAG_SALDO_DATA_LIMITE><xsl:value-of select="*[2]/*[local-name()='CONSULTA_EXTRATO']/TIPO_CONSULTA/FLAG_SALDO_DATA_LIMITE"/></FLAG_SALDO_DATA_LIMITE>
						<FLAG_LANCAMENTOS_DIA><xsl:value-of select="*[2]/*[local-name()='CONSULTA_EXTRATO']/TIPO_CONSULTA/FLAG_LANCAMENTOS_DIA"/></FLAG_LANCAMENTOS_DIA>
						<FLAG_SALDO_POUPANCA_VINCULADA><xsl:value-of select="*[2]/*[local-name()='CONSULTA_EXTRATO']/TIPO_CONSULTA/FLAG_SALDO_POUPANCA_VINCULADA"/></FLAG_SALDO_POUPANCA_VINCULADA>
						<FLAG_LANCAMENTO_POUPANCA_VINCULADA><xsl:value-of select="*[2]/*[local-name()='CONSULTA_EXTRATO']/TIPO_CONSULTA/FLAG_LANCAMENTO_POUPANCA_VINCULADA"/></FLAG_LANCAMENTO_POUPANCA_VINCULADA>
						<FLAG_SALDO_POUPANCA_INTEGRADA><xsl:value-of select="*[2]/*[local-name()='CONSULTA_EXTRATO']/TIPO_CONSULTA/FLAG_SALDO_POUPANCA_INTEGRADA"/></FLAG_SALDO_POUPANCA_INTEGRADA>
						<FLAG_LANCAMENTO_POUPANCA_INTEGRADA><xsl:value-of select="*[2]/*[local-name()='CONSULTA_EXTRATO']/TIPO_CONSULTA/FLAG_LANCAMENTO_POUPANCA_INTEGRADA"/></FLAG_LANCAMENTO_POUPANCA_INTEGRADA>
				</TIPO_CONSULTA>
			</consulta:EXTRATO_ENTRADA>
		</multicanal:SERVICO_ENTRADA>
	</xsl:template>
</xsl:stylesheet>