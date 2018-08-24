<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

	<xsl:output omit-xml-declaration="yes"/>
	<xsl:strip-space elements="*" />

	<xsl:template match="node()|@*">
		<xsl:copy>
			<xsl:apply-templates select="node()|@*" />
		</xsl:copy>
	</xsl:template>
	
	<xsl:param name="pRedeTransmissora"/>
	
	<xsl:template match="/BUSDATA">
		<multicanal:SERVICO_ENTRADA 
			xmlns:multicanal="http://caixa.gov.br/sibar/multicanal/orquestracao" 
			xmlns:sibar_base="http://caixa.gov.br/sibar" 
			xmlns:valida_cartao="http://caixa.gov.br/sibar/valida_cartao" 
			xmlns:valida_senha="http://caixa.gov.br/sibar/valida_senha/senha_conta" 
			xmlns:valida_permissao="http://caixa.gov.br/sibar/valida_permissao" 
			xmlns:resumo="http://caixa.gov.br/sibar/consulta_transacoes_conta/resumo"
			xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" 
			xsi:schemaLocation="http://caixa.gov.br/simtx/multicanal/orquestracao Multicanal_Orquestracao.xsd">
			<sibar_base:HEADER>
				<VERSAO>1.0</VERSAO>
				<USUARIO_SERVICO><xsl:value-of select="*[2]/*[local-name()='HEADER']/USUARIO_SERVICO"/></USUARIO_SERVICO>
				<USUARIO><xsl:value-of select="*[2]/*[local-name()='HEADER']/USUARIO"/></USUARIO>
				<OPERACAO>SERVICO_MIGRADO</OPERACAO>
				<SISTEMA_ORIGEM><xsl:value-of select="*[2]/*[local-name()='HEADER']/CANAL/SIGLA"/></SISTEMA_ORIGEM>
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

			<xsl:if test="*[2]/*[local-name()='CARTAO'] != ''">
				<valida_cartao:VALIDA_CARTAO_ENTRADA>
					<CARTAO_COMERCIAL>
						<CARTAO>
							<TRILHA><xsl:value-of select="*[2]/*[local-name()='CARTAO']/TRILHA"/></TRILHA>
							<TP_ENTRADA><xsl:value-of select="*[2]/*[local-name()='CARTAO']/TP_ENTRADA"/></TP_ENTRADA>
							<NUMERO_SEQUENCIAL><xsl:value-of select="*[2]/*[local-name()='CARTAO']/NUMERO_SEQUENCIAL"/></NUMERO_SEQUENCIAL>
							<INFORMACOES_CHIP><xsl:value-of select="*[2]/*[local-name()='CARTAO']/INFORMACOES_CHIP"/></INFORMACOES_CHIP>
						</CARTAO>
						<TERMINAL><xsl:value-of select="*[2]/*[local-name()='HEADER']/IDENTIFICADOR_ORIGEM/TERMINAL"/></TERMINAL>
						<REDE_TRANSMISSORA><xsl:value-of select="$pRedeTransmissora"/></REDE_TRANSMISSORA>
					</CARTAO_COMERCIAL>
				</valida_cartao:VALIDA_CARTAO_ENTRADA>
			
				<valida_senha:VALIDA_SENHA_ENTRADA>
					<FLAG_CONSULTA_IP><xsl:value-of select="*[2]/*[local-name()='SENHA']/FLAG_CONSULTA_IP"/></FLAG_CONSULTA_IP>
					<VERSAO><xsl:value-of select="*[2]/*[local-name()='SENHA']/VERSAO"/></VERSAO>
					<ACAO><xsl:value-of select="*[2]/*[local-name()='SENHA']/ACAO"/></ACAO>
					<REDE_TRANSMISSORA><xsl:value-of select="$pRedeTransmissora"/></REDE_TRANSMISSORA>
					<xsl:choose>
						<xsl:when test="*[2]/*[local-name()='CONTA']/CONTA_SIDEC">
							<CONTA>
								<UNIDADE><xsl:value-of select="*[2]/*[local-name()='CONTA']/CONTA_SIDEC/UNIDADE"/></UNIDADE>
								<OPERACAO><xsl:value-of select="*[2]/*[local-name()='CONTA']/CONTA_SIDEC/OPERACAO"/></OPERACAO>
								<NUMERO><xsl:value-of select="*[2]/*[local-name()='CONTA']/CONTA_SIDEC/CONTA"/></NUMERO>
								<DV><xsl:value-of select="*[2]/*[local-name()='CONTA']/CONTA_SIDEC/DV"/></DV>
							</CONTA>
						</xsl:when>
						<xsl:when test="*[2]/*[local-name()='CONTA']/CONTA_NSGD">
							<CONTA_NSGD>
								<UNIDADE><xsl:value-of select="*[2]/*[local-name()='CONTA']/CONTA_NSGD/UNIDADE"/></UNIDADE>
								<PRODUTO><xsl:value-of select="*[2]/*[local-name()='CONTA']/CONTA_NSGD/PRODUTO"/></PRODUTO>
								<NUMERO><xsl:value-of select="*[2]/*[local-name()='CONTA']/CONTA_NSGD/CONTA"/></NUMERO>
								<DV><xsl:value-of select="*[2]/*[local-name()='CONTA']/CONTA_NSGD/DV"/></DV>
							</CONTA_NSGD>
						</xsl:when>
						<xsl:otherwise>
							<CONTA>
								<UNIDADE>0</UNIDADE>
								<OPERACAO>0</OPERACAO>
								<NUMERO>0</NUMERO>
								<DV>0</DV>
							</CONTA>
							<CONTA_NSGD>
								<UNIDADE>0</UNIDADE>
								<PRODUTO>0</PRODUTO>
								<NUMERO>0</NUMERO>
								<DV>0</DV>
							</CONTA_NSGD>
						</xsl:otherwise>
					</xsl:choose>
					<TITULARIDADE>
						<xsl:choose>
							<xsl:when test="*[2]/*[local-name()='CONTA']/TITULAR_CONTA != ''">
								<xsl:value-of select="*[2]/*[local-name()='CONTA']/TITULAR_CONTA"/>
							</xsl:when>
							<xsl:otherwise>1</xsl:otherwise>
						</xsl:choose>
					</TITULARIDADE>
					<xsl:choose>
						<xsl:when test="*[2]/*[local-name()='CARTAO']/TRILHA/text() != ''">
							<FLAG_TRILHA>S</FLAG_TRILHA>
							<TRILHA><xsl:value-of select="*[2]/*[local-name()='CARTAO']/TRILHA"/></TRILHA>
						</xsl:when>
						<xsl:otherwise>
							<FLAG_TRILHA>N</FLAG_TRILHA>
						</xsl:otherwise>
					</xsl:choose>
					<SENHA><xsl:value-of select="*[2]/*[local-name()='SENHA']/SENHA"/></SENHA>
					<INFORMACOES_CHIP><xsl:value-of select="*[2]/*[local-name()='CARTAO']/INFORMACOES_CHIP"/></INFORMACOES_CHIP>
					<CODIGO_SERVICO>AUT001</CODIGO_SERVICO>
					<CARTEIRA><xsl:value-of select="*[2]/*[local-name()='SENHA']/CARTEIRA"/></CARTEIRA>
					<INFORMACOES_TELA><xsl:value-of select="*[2]/*[local-name()='SENHA']/INFORMACOES_TELA"/></INFORMACOES_TELA>
				</valida_senha:VALIDA_SENHA_ENTRADA>
			</xsl:if>
			
			<xsl:if test="*[2]/*[local-name()='PERMISSAO'] != ''">
				<valida_permissao:VALIDA_PERMISSAO_ENTRADA>
					<OPCAO><xsl:value-of select="*[2]/*[local-name()='PERMISSAO']/OPCAO"/></OPCAO>
					<CPF><xsl:value-of select="*[2]/CPF"/></CPF>
					<TOKEN>
						<ACAO><xsl:value-of select="*[2]/*[local-name()='TOKEN']/ACAO"/></ACAO>
						<SESSAO><xsl:value-of select="*[2]/*[local-name()='TOKEN']/SESSAO"/></SESSAO>
						<ENDERECO_IP><xsl:value-of select="*[2]/*[local-name()='HEADER']/IDENTIFICADOR_ORIGEM/ENDERECO_IP"/></ENDERECO_IP>
						<TOKEN_SESSAO><xsl:value-of select="*[2]/*[local-name()='TOKEN']/CODIGO_SESSAO"/></TOKEN_SESSAO>
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
					<SERVICO><xsl:value-of select="*[2]/*[local-name()='PERMISSAO']/SERVICO_SIPER"/></SERVICO>
					<DT_MOVTO_SIPER><xsl:value-of select="*[2]/*[local-name()='PERMISSAO']/DT_MOVTO_SIPER"/></DT_MOVTO_SIPER>
				</valida_permissao:VALIDA_PERMISSAO_ENTRADA>
			</xsl:if>

			<resumo:CONSULTA_TRANSACOES_CONTA_ENTRADA>
				<CONTA>
					<xsl:choose>
						<xsl:when test="*[2]/CONTA/CONTA_SIDEC">
							<UNIDADE><xsl:value-of select="*[2]/CONTA/CONTA_SIDEC/UNIDADE" /></UNIDADE>
							<PRODUTO><xsl:value-of select="*[2]/CONTA/CONTA_SIDEC/OPERACAO" /></PRODUTO>
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
				<PERIODO>
					<INICIO><xsl:value-of select="*[2]/LISTA_COMPROVANTES_TRANSACOES/DATA_INICIO" /></INICIO>
					<FIM><xsl:value-of select="*[2]/LISTA_COMPROVANTES_TRANSACOES/DATA_FIM" /></FIM>
				</PERIODO>
				<SOLICITACOES>
					<SOLICITACAO>BOLETO</SOLICITACAO>
				</SOLICITACOES>
			</resumo:CONSULTA_TRANSACOES_CONTA_ENTRADA>
			
		</multicanal:SERVICO_ENTRADA>
	</xsl:template>
</xsl:stylesheet>