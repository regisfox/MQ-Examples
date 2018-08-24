Insert into MTX.MTXTB001_SERVICO ( NU_SERVICO, NO_SERVICO, IC_SITUACAO_SERVICO, DH_ATUALIZACAO, IC_TIPO_SERVICO, IC_CONFIRMACAO_TRANSACAO, NO_SERVICO_BARRAMENTO, NO_OPERACAO_BARRAMENTO, NO_CONEXAO, QT_SGNDO_LMTE_RESPOSTA, NO_FILA_REQUISICAO, NO_FILA_RESPOSTA, IC_EMISSAO_COMPROVANTE, IC_SERVICO_ORQUESTRADO, IC_CANCELAMENTO_TRANSACAO ) values ( '110024', 'Detalhe de Transa��o Pendente de Assinatura', '1', to_date('08/09/17','DD/MM/RR'), '6', '0', 'consulta_assinatura_eletronica', 'DETALHE_TRANSACAO_PENDENTE', 'java:jboss/jms/SimtxQueueConnectionFactorySIBAR', '5000', 'java:jboss/jms/sibar_req_brq', 'java:jboss/jms/sibar_rsp_brq', '0', '0', '0' ); 
Insert into MTX.MTXTB001_SERVICO ( NU_SERVICO, NO_SERVICO, IC_SITUACAO_SERVICO, DH_ATUALIZACAO, IC_TIPO_SERVICO, IC_CONFIRMACAO_TRANSACAO, NO_SERVICO_BARRAMENTO, NO_OPERACAO_BARRAMENTO, NO_CONEXAO, QT_SGNDO_LMTE_RESPOSTA, NO_FILA_REQUISICAO, NO_FILA_RESPOSTA, IC_EMISSAO_COMPROVANTE, IC_SERVICO_ORQUESTRADO, IC_CANCELAMENTO_TRANSACAO ) values ( '110022', 'Valida Assinatura', '1', to_date('01/02/18','DD/MM/RR'), '6', '0', 'valida_assinatura', 'VALIDA_ASSINATURA', 'java:jboss/jms/SimtxQueueConnectionFactorySIBAR', '5000', 'java:jboss/jms/sibar_req_brq', 'java:jboss/jms/sibar_rsp_brq', '0', '0', '1' );

-- Lista Pendencia assinatura
UPDATE MTX.MTXTB011_VERSAO_SERVICO SET NU_VERSAO_SERVICO = 1, DE_XSD_REQUISICAO = 'validacao_entradas/SIMTX_TYPES/Lista_Transacoes_Pendentes_Assinatura/V1/Lista_Transacoes_Pendentes_Assinatura.xsd', DE_XSLT_REQUISICAO   = 'servico/Lista_Transacoes_Pendentes_Assinatura/V1/Lista_Transacoes_Pendentes_Assinatura_Req.xsl', DE_XSD_RESPOSTA      =  'servico/Lista_Transacoes_Pendentes_Assinatura/V1/Lista_Transacoes_Pendentes_Assinatura_Req.xsl', DE_XSLT_RESPOSTA     = 'servico/Lista_Transacoes_Pendentes_Assinatura/V1/Lista_Transacoes_Pendentes_Assinatura_Resp.xsl', DH_ATUALIZACAO = sysdate, IC_STCO_VRSO_SRVCO  =  1, IC_SERVICO_MIGRADO  = 1, CO_VERSAO_BARRAMENTO =  '1.0' WHERE NU_SERVICO_001 = 110023;

INSERT INTO MTX.MTXTB007_TAREFA_MENSAGEM ( NU_TAREFA_012, NU_VERSAO_TAREFA_012, NU_MENSAGEM_006, NO_CAMPO_RETORNO ) VALUES ( 100045, 1, 1,'CONTROLE_NEGOCIAL.COD_RETORNO' );

INSERT INTO MTX.MTXTB013_PRMTO_SRVCO_CANAL ( NU_PARAMETRO, NU_SERVICO_005, NU_CANAL_005, HH_LIMITE_INICIO, HH_LIMITE_FIM, DT_INCO_SLCTO_SRVCO, DT_FIM_SLCTO_SRVCO, VR_MNMO_SLCTO_SRVCO, VR_MXMO_SLCTO_SRVCO, DH_ATUALIZACAO ) VALUES ( 111, 110023, 110,'01/01/15',	'01/01/20',	'01/01/15',	'01/01/20',	1,	1000, sysdate );