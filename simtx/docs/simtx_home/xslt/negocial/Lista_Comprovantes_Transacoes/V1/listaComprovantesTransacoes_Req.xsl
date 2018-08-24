<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

	<!-- ESTE ARQUIVO FOI PROJETADO PARA GRAVAR DADOS NO BUSDATA DO PMAC -->
	<!-- omit-xml-declaration=yes DEVE SER INCLUIDO -->

	<xsl:output method="xml" omit-xml-declaration="yes" />

	<xsl:template match="*" mode="copy">
		<xsl:element name="{name()}" namespace="{namespace-uri()}">
			<xsl:apply-templates select="@*|node()" mode="copy" />
		</xsl:element>
	</xsl:template>

	<xsl:template match="@*|text()|comment()" mode="copy">
		<xsl:copy />
	</xsl:template>
	
	<xsl:template match="/BUSDATA">
		<ns:LISTA_COMPROVANTES_TRANSACOES_ENTRADA 
			xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
			xmlns:h="http://caixa.gov.br/sicco/comuns/header"
			xmlns:ns="http://caixa.gov.br/sicco/lista_comprovantes_transacoes/v1/ns">
			<HEADER>
				<SERVICO>
					<CODIGO><xsl:value-of select="*[2]/*[local-name()='HEADER']/SERVICO/CODIGO"/></CODIGO>
					<VERSAO><xsl:value-of select="*[2]/*[local-name()='HEADER']/SERVICO/VERSAO"/></VERSAO>
				</SERVICO>
				<CANAL>
					<SIGLA><xsl:value-of select="*[2]/*[local-name()='HEADER']/CANAL/SIGLA"/></SIGLA>
				</CANAL>
				<DATAHORA>
					<xsl:value-of select="BUSTIME/ANO" />
					<xsl:value-of select="BUSTIME/MES" />
					<xsl:value-of select="BUSTIME/DIA" />
					<xsl:value-of select="BUSTIME/HORA" />
					<xsl:value-of select="BUSTIME/MIN" />
					<xsl:value-of select="BUSTIME/SEG" />
				</DATAHORA>
			</HEADER>
			
			<LISTA_COMPROVANTES_TRANSACOES>
				<DATA_INICIO><xsl:value-of select="*[2]/LISTA_COMPROVANTES_TRANSACOES/DATA_INICIO"/></DATA_INICIO>
				<DATA_FIM><xsl:value-of select="*[2]/LISTA_COMPROVANTES_TRANSACOES/DATA_FIM"/></DATA_FIM>
				<xsl:choose>
					<xsl:when test="*[2]/CONTA">
						<xsl:apply-templates mode="copy" select="*[2]/CONTA" disable-output-escaping="yes"/>
					</xsl:when>
					<xsl:otherwise>
						<xsl:choose>
							<xsl:when test="*[local-name()='SERVICO_SAIDA']/*[local-name()='VALIDA_CARTAO_SAIDA']/CARTAO_COMERCIAL/CONTA">
								<CONTA>
									<CONTA_SIDEC>
										<UNIDADE><xsl:value-of select="*[local-name()='SERVICO_SAIDA']/*[local-name()='VALIDA_CARTAO_SAIDA']/CARTAO_COMERCIAL/CONTA/UNIDADE"/></UNIDADE>
										<OPERACAO><xsl:value-of select="*[local-name()='SERVICO_SAIDA']/*[local-name()='VALIDA_CARTAO_SAIDA']/CARTAO_COMERCIAL/CONTA/OPERACAO"/></OPERACAO>
										<CONTA><xsl:value-of select="*[local-name()='SERVICO_SAIDA']/*[local-name()='VALIDA_CARTAO_SAIDA']/CARTAO_COMERCIAL/CONTA/NUMERO"/></CONTA>
										<DV><xsl:value-of select="*[local-name()='SERVICO_SAIDA']/*[local-name()='VALIDA_CARTAO_SAIDA']/CARTAO_COMERCIAL/CONTA/DV"/></DV>
									</CONTA_SIDEC>
								</CONTA>		
							</xsl:when>
							<xsl:otherwise>
								<CONTA>
									<CONTA_NSGD>
										<UNIDADE><xsl:value-of select="*[local-name()='SERVICO_SAIDA']/*[local-name()='VALIDA_CARTAO_SAIDA']/CARTAO_COMERCIAL/CONTA_NSGD/UNIDADE"/></UNIDADE>
										<OPERACAO><xsl:value-of select="*[local-name()='SERVICO_SAIDA']/*[local-name()='VALIDA_CARTAO_SAIDA']/CARTAO_COMERCIAL/CONTA_NSGD/OPERACAO"/></OPERACAO>
										<CONTA><xsl:value-of select="*[local-name()='SERVICO_SAIDA']/*[local-name()='VALIDA_CARTAO_SAIDA']/CARTAO_COMERCIAL/CONTA_NSGD/NUMERO"/></CONTA>
										<DV><xsl:value-of select="*[local-name()='SERVICO_SAIDA']/*[local-name()='VALIDA_CARTAO_SAIDA']/CARTAO_COMERCIAL/CONTA_NSGD/DV"/></DV>
									</CONTA_NSGD>
								</CONTA>
							</xsl:otherwise>
						</xsl:choose>
					</xsl:otherwise>
				</xsl:choose>
			</LISTA_COMPROVANTES_TRANSACOES> 
		</ns:LISTA_COMPROVANTES_TRANSACOES_ENTRADA>
	</xsl:template>
</xsl:stylesheet>