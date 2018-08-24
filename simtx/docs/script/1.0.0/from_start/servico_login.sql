INSERT INTO MTXTB001_SERVICO VALUES (110028, 'Login SIMAI',	'login_simai', 'LOGIN_SIMAI', 1, 0, 6, sysdate,	5000,	'java:jboss/jms/SimtxQueueConnectionFactorySIBAR',	'java:jboss/jms/sibar_req_brq',	'java:jboss/jms/sibar_rsp_brq',	1);
INSERT INTO MTXTB011_VERSAO_SERVICO VALUES (110028, 1,	'validacao_entradas/SIMTX_TYPES/V1/Login_Simai.xsd',	'requisicao_simtx.xsl',	'validacao_entradas/SIMTX_TYPES/V1/Login_Simai.xsd',	'servico/V1/loginSimai_Resp.xsl',	sysdate, 1,	0,	'F', null	);
 
INSERT INTO MTX.MTXTB002_TAREFA VALUES (100014, 'Consulta Saldo', 'consulta_saldo', 'CONSULTA_SALDO', 0, 2, sysdate);
INSERT INTO MTX.MTXTB002_TAREFA VALUES (100025, 'Valida Cartao Comercial', 'valida_cartao', 'CARTAO_COMERCIAL', 0, 3, sysdate);

INSERT INTO MTX.MTXTB012_VERSAO_TAREFA (NU_TAREFA_002, NU_VERSAO_TAREFA, IC_SITUACAO, IC_DESFAZIMENTO, IC_PARALELISMO, IC_ASSINCRONO, DE_XSD_REQUISICAO, DE_XSLT_REQUISICAO,DE_XSD_RESPOSTA, DE_XSLT_RESPOSTA, DE_XSLT_PARAMETRO, DH_ATUALIZACAO) VALUES
(100025, 1, 1, 0, 0, 0, 'validacao_entradas/SIBAR/MEIOS_DE_ENTRADA/V1/VALIDA_CARTAO/Valida_Cartao.xsd', 'meio_entrada/V1/validaCartaoComercial_Req.xsl', 'validacao_entradas/SIBAR/MEIOS_DE_ENTRADA/V1/VALIDA_CARTAO/Valida_Cartao.xsd', 'meio_entrada/V1/validaCartaoComercial_Resp.xsl', null, SYSDATE);

INSERT INTO MTX.MTXTB012_VERSAO_TAREFA (NU_TAREFA_002, NU_VERSAO_TAREFA, IC_SITUACAO, IC_DESFAZIMENTO, IC_PARALELISMO, IC_ASSINCRONO, DE_XSD_REQUISICAO, DE_XSLT_REQUISICAO,DE_XSD_RESPOSTA, DE_XSLT_RESPOSTA, DE_XSLT_PARAMETRO, DH_ATUALIZACAO) VALUES
(100014, 1, 1, 0, 0, 0,	'validacao_entradas/SIBAR/NEGOCIAL/V1/Consulta_Saldo.xsd',	'negocial/V1/consultaSaldo_Req.xsl', 'validacao_entradas/SIBAR/NEGOCIAL/V1/Consulta_Saldo.xsd',	'negocial/V1/consultaSaldo_Resp.xsl', sysdate);


INSERT INTO MTX.MTXTB024_TAREFA_FILA (NU_TAREFA_012, NU_VERSAO_TAREFA_012, SG_SISTEMA_CORPORATIVO, NO_FILA_REQUISICAO, QT_SGNDO_LMTE_REQUISICAO, NO_FILA_RESPOSTA, QT_SGNDO_LMTE_RESPOSTA, NO_MODO_INTEGRACAO, NO_RECURSO, QT_TEMPO_ESPERA,  NO_CONEXAO) 
 VALUES (100025, 1, 'SIB24', 'java:jboss/jms/sibar_req_brq', 5000, 'java:jboss/jms/sibar_rsp_brq', 5000, 'java:jboss/jms/SimtxQueueConnectionFactorySIBAR', 'MQ', '-', '-', 0);

INSERT INTO MTX.MTXTB024_TAREFA_FILA (NU_TAREFA_012, NU_VERSAO_TAREFA_012, SG_SISTEMA_CORPORATIVO, NO_FILA_REQUISICAO, QT_SGNDO_LMTE_REQUISICAO, NO_FILA_RESPOSTA, QT_SGNDO_LMTE_RESPOSTA, NO_MODO_INTEGRACAO, NO_RECURSO, QT_TEMPO_ESPERA,  NO_CONEXAO)  
 VALUES (100014, 1, 'SIBAR', 'java:jboss/jms/sibar_req_brq', 5000, 'java:jboss/jms/sibar_rsp_brq', 5000, 'java:jboss/jms/SimtxQueueConnectionFactorySIBAR', '-', '-', 0);

INSERT INTO MTX.MTXTB003_SERVICO_TAREFA (NU_SERVICO_011, NU_VERSAO_SERVICO_011, NU_TAREFA_012, NU_VERSAO_TAREFA_012, NU_SEQUENCIA_EXECUCAO, IC_SITUACAO, IC_IMPEDIMENTO, DE_XSLT_NOVA_TAREFA)
 VALUES(110028,1,100025,1,1,1,1, null);
 
INSERT INTO MTX.MTXTB003_SERVICO_TAREFA (NU_SERVICO_011, NU_VERSAO_SERVICO_011, NU_TAREFA_012, NU_VERSAO_TAREFA_012, NU_SEQUENCIA_EXECUCAO, IC_SITUACAO, IC_IMPEDIMENTO, DE_XSLT_NOVA_TAREFA)
 VALUES(110028, 1, 100014, 1, 1, 1, 1, null);

INSERT INTO MTX.MTXTB005_SERVICO_CANAL (NU_SERVICO_001, NU_CANAL_004, IC_STCO_SRVCO_CANAL) VALUES (110028, 113, 1);
INSERT INTO MTXTB020_SRVCO_TARFA_CANAL VALUES (110028,	1, 100025, 1, 113);


--meio de entrada
INSERT INTO MTX.MTXTB008_MEIO_ENTRADA (NU_MEIO_ENTRADA, NO_MEIO_ENTRADA, IC_SITUACAO) VALUES (3, '#CARTAO', 1);
INSERT INTO MTX.MTXTB010_VRSO_TARFA_MEIO_ENTRA (NU_TAREFA_012, NU_VERSAO_TAREFA_012, NU_MEIO_ENTRADA_008, IC_SITUACAO) VALUES (100014, 1, 3, 1);
INSERT INTO mtx.MTXTB018_VRSO_MEIO_ENTRA_SRVCO(NU_MEIO_ENTRADA_008, NU_SERVICO_011, NU_VERSAO_SERVICO_011, IC_SITUACAO, DH_ATUALIZACAO) VALUES (3, 110028, 1,	1, sysdate);