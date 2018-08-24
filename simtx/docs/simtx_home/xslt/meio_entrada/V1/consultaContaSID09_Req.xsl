<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

	<!-- Gera o arquivo para enviar para o sistema corporativo -->


	<xsl:template match="/">
		<consultacontasid09:SERVICO_ENTRADA 
			xmlns:xsd="http://www.w3.org/2001/XMLSchema"
			xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
			xmlns:sibar_base="http://caixa.gov.br/sibar" 
			xmlns:consultacontasid09="http://caixa.gov.br/sibar/consulta_conta_sid09">
			<sibar_base:HEADER>
				<VERSAO>2.4</VERSAO>
				<OPERACAO>CONSULTA_CONTA</OPERACAO>
				<SISTEMA_ORIGEM>SIMTX</SISTEMA_ORIGEM>
				<DATA_HORA>
					<xsl:value-of select="BUSDATA/BUSTIME/ANO" />
					<xsl:value-of select="BUSDATA/BUSTIME/MES" />
					<xsl:value-of select="BUSDATA/BUSTIME/DIA" />
					<xsl:value-of select="BUSDATA/BUSTIME/HORA" />
					<xsl:value-of select="BUSDATA/BUSTIME/MIN" />
					<xsl:value-of select="BUSDATA/BUSTIME/SEG" />
				</DATA_HORA>
			</sibar_base:HEADER>
			<DADOS>
				<CONSULTA_CONTA>
					<SEGMENTO>2905</SEGMENTO>
					<CONTA>
						<UNIDADE><xsl:value-of select="BUSDATA/RESPOSTA/VALIDA_CARTAO/DADOS/CARTAO_COMERCIAL/CONTA/UNIDADE"/></UNIDADE>
						<OPERACAO><xsl:value-of select="BUSDATA/RESPOSTA/VALIDA_CARTAO/DADOS/CARTAO_COMERCIAL/CONTA/OPERACAO"/></OPERACAO>
						<NUMERO><xsl:value-of select="BUSDATA/RESPOSTA/VALIDA_CARTAO/DADOS/CARTAO_COMERCIAL/CONTA/NUMERO"/></NUMERO>
						<DV><xsl:value-of select="BUSDATA/RESPOSTA/VALIDA_CARTAO/DADOS/CARTAO_COMERCIAL/CONTA/DV"/></DV>
					</CONTA>
					<FLAG_PESQUISA_TITULAR>3</FLAG_PESQUISA_TITULAR>
				</CONSULTA_CONTA>
			</DADOS>
		</consultacontasid09:SERVICO_ENTRADA>
	</xsl:template>
</xsl:stylesheet>









