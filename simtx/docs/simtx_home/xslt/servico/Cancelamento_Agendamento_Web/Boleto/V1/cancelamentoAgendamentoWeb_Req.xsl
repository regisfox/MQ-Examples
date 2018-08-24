<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

	<xsl:output omit-xml-declaration="yes"/>
	<xsl:strip-space elements="*" />
	
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
				<USUARIO_SERVICO>SMTXSD01</USUARIO_SERVICO>
<!-- 				<USUARIO><xsl:value-of select="cancelamentoAgendamentoWeb/codigoUsuario" /></USUARIO> -->
				<OPERACAO>SERVICO_MIGRADO</OPERACAO>
				<SISTEMA_ORIGEM>SIMTX</SISTEMA_ORIGEM>
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
				<CONTA>
					<UNIDADE>
						<xsl:value-of select="mtxCancelamentoAgendamentoWeb/nuUnidade" />
					</UNIDADE>
					<PRODUTO>
						<xsl:value-of select="mtxCancelamentoAgendamentoWeb/nuProduto" />
					</PRODUTO>
					<NUMERO>
						<xsl:value-of select="mtxCancelamentoAgendamentoWeb/nuConta" />
					</NUMERO>
					<DV>
						<xsl:value-of select="mtxCancelamentoAgendamentoWeb/dvConta" />
					</DV>
				</CONTA>
				<VALOR_TRANSACAO>
					<xsl:value-of select="mtxCancelamentoAgendamentoWeb/valorTransacao" />
				</VALOR_TRANSACAO>
				<DATA_EFETIVACAO>
					<xsl:value-of select="mtxCancelamentoAgendamentoWeb/dataEfetivacao" />
				</DATA_EFETIVACAO>
				<TIPO_AUTENTICACAO>AUTENTICACAO_SIMPLES</TIPO_AUTENTICACAO>
			</controlelimite:CONTROLE_LIMITE_ENTRADA>
		</multicanal:SERVICO_ENTRADA>
	</xsl:template>
</xsl:stylesheet>