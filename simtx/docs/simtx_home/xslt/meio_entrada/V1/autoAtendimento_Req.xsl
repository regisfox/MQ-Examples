<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

	<!-- Gera o arquivo para enviar para o sistema corporativo -->


	<xsl:template match="/">
		<SERVICO_ENTRADA xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
			xmlns:sibar_base="http://caixa.gov.br/sibar"
			xmlns:controleautoatendimento="http://caixa.gov.br/sibar/controle_auto_atendimento">

			<sibar_base:HEADER>
				<VERSAO>1.0</VERSAO>
				<OPERACAO>CONTROLE_AUTO_ATENDIMENTO</OPERACAO>
				<SISTEMA_ORIGEM>SIMTX</SISTEMA_ORIGEM>
				<NSUMTX>
					<xsl:value-of select="/BUSDATA/*[local-name()='REQUISICAO']/HEADER/NSUMTX" />
				</NSUMTX>
				<DATA_HORA>
					<xsl:value-of select="BUSDATA/BUSTIME/ANO" />
					<xsl:text>-</xsl:text>
					<xsl:value-of select="BUSDATA/BUSTIME/MES" />
					<xsl:text>-</xsl:text>
					<xsl:value-of select="BUSDATA/BUSTIME/DIA" />
				</DATA_HORA>
			</sibar_base:HEADER>

			<xsl:for-each
				select="/BUSDATA/*[local-name()='REQUISICAO']/MODOENTRADA/CONTROLE_AUTOATENDIMENTO">
				<controleautoatendimento:DADOS
					xmlns:controleautoatendimento="http://caixa.gov.br/sibar/controle_auto_atendimento">
					<xsl:for-each select="./*">
						<xsl:copy-of select="." />
					</xsl:for-each>
				</controleautoatendimento:DADOS>
			</xsl:for-each>
		</SERVICO_ENTRADA>

	</xsl:template>
</xsl:stylesheet>









