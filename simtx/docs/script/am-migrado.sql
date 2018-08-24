--servico
insert into mtx.mtxtb001_servico (nu_servico, no_servico, no_servico_barramento, no_operacao_barramento, ic_situacao_servico, ic_confirmacao_transacao, ic_tipo_servico, dh_atualizacao, qt_sgndo_lmte_resposta, no_conexao, no_fila_requisicao, no_fila_resposta, ic_emissao_comprovante, IC_SERVICO_ORQUESTRADO) 
 values (110030, 'Contra Ordem Cheques', 'manutencao_cheque', 'CONTRA_ORDEM', 1, 0, 6, SYSDATE, 5000, 'java:jboss/jms/SimtxQueueConnectionFactorySIMTX', 'java:jboss/jms/sibar_req_migrado', 'java:jboss/jms/sibar_rsp_migrado', 0, 0);

insert into mtx.mtxtb011_versao_servico (nu_servico_001, nu_versao_servico, de_xsd_requisicao, de_xslt_requisicao, de_xsd_resposta, de_xslt_resposta, dh_atualizacao, ic_stco_vrso_srvco, ic_servico_migrado, co_versao_barramento)
 values (110022, 1, 'validacao_entradas/SIMTX_TYPES/Valida_Assinatura_Multipla/V1/Valida_Assinatura_Multipla.xsd', 'servico/Valida_Assinatura_Multipla/V1/Valida_Assinatura_Multipla_Req.xsl', 'validacao_entradas/SIMTX_TYPES/Valida_Assinatura_Multipla/V1/Valida_Assinatura_Multipla.xsd', 'servico\Valida_Assinatura_Multipla\V1\Valida_Assinatura_Multipla_Resp.xsl', sysdate, 1, 1, '1.0');

insert into mtx.mtxtb001_servico (nu_servico, no_servico, ic_situacao_servico, dh_atualizacao, ic_tipo_servico, ic_confirmacao_transacao, no_servico_barramento, no_operacao_barramento, no_conexao, qt_sgndo_lmte_resposta, no_fila_requisicao, no_fila_resposta, ic_emissao_comprovante, ic_servico_orquestrado, ic_cancelamento_transacao)
 values (110025, 'Cancela Transação com Assinatura Múltipla', 1, sysdate, 6, 0, 'cancela_assinatura', 'CANCELA_ASSINATURA', 'java:jboss/jms/SimtxQueueConnectionFactorySIMTX', 5000, 'java:jboss/jms/req_multicanal', 'java:jboss/jms/rsp_multicanal', 0,	0, 0);

insert into mtx.mtxtb011_versao_servico (nu_servico_001, nu_versao_servico, de_xsd_requisicao, de_xslt_requisicao, de_xsd_resposta, de_xslt_resposta, dh_atualizacao, ic_stco_vrso_srvco, ic_servico_migrado, CO_VERSAO_BARRAMENTO)
 values (110030, 1, 'validacao_entradas/SIMTX_TYPES/Manutencao_Cheque/V1/Contra_Ordem_Cheque.xsd',	'servico/Contra_Ordem_Cheques/V1/Contra_Ordem_Cheque_Req.xsl',	'validacao_entradas/SIMTX_TYPES/Manutencao_Cheque/V1/Contra_Ordem_Cheque.xsd', 'servico/Contra_Ordem_Cheques/V1/Contra_Ordem_Cheque_Resp.xsl', sysdate, 1, 1, '1.0');

insert into mtx.mtxtb011_versao_servico (nu_servico_001, nu_versao_servico, de_xsd_requisicao, de_xslt_requisicao, de_xsd_resposta, de_xslt_resposta, dh_atualizacao, ic_stco_vrso_srvco, ic_servico_migrado, CO_VERSAO_BARRAMENTO)
 values (110023, 1, 'validacao_entradas/SIMTX_TYPES/Lista_Transacoes_Pendentes_Assinatura/V1/Lista_Transacoes_Pendentes_Assinatura.xsd',	'servico/Lista_Transacoes_Pendentes_Assinatura/V1/Lista_Transacoes_Pendentes_Assinatura_Req.xsl',	'servico/Lista_Transacoes_Pendentes_Assinatura/V1/Lista_Transacoes_Pendentes_Assinatura_Req.xsl', 'servico/Lista_Transacoes_Pendentes_Assinatura/V1/Lista_Transacoes_Pendentes_Assinatura_Resp.xsl', sysdate, 1, 1, '1.0');
 
insert into mtx.mtxtb011_versao_servico (nu_servico_001, nu_versao_servico, de_xsd_requisicao, de_xslt_requisicao, de_xsd_resposta, de_xslt_resposta, dh_atualizacao, ic_stco_vrso_srvco, ic_servico_migrado, CO_VERSAO_BARRAMENTO)
 values (110024, 1, 'validacao_entradas/SIMTX_TYPES/Detalhe_Transacao_Pendente_Assinatura/V1/Detalhe_Transacao_Pendente_Assinatura.xsd',	'-',	'validacao_entradas\SIMTX_TYPES\Detalhe_Transacao_Pendente_Assinatura\V1\Detalhe_Transacao_Pendente_Assinatura.xsd', 'servico\Detalhe_Transacao_Pendente_Assinatura\V1\Detalhe_Transacao_Pendente_Resp.xsl', sysdate, 1, 1, '1.0');

insert into mtx.MTXTB011_VERSAO_SERVICO (NU_SERVICO_001, NU_VERSAO_SERVICO, DE_XSD_REQUISICAO, DE_XSLT_REQUISICAO, DE_XSD_RESPOSTA, DE_XSLT_RESPOSTA, DH_ATUALIZACAO, IC_STCO_VRSO_SRVCO, IC_SERVICO_MIGRADO, CO_VERSAO_BARRAMENTO)
 values (110025, 1, 'validacao_entradas/SIMTX_TYPES/Cancela_Transacao_Assinatura_Multipla/V1/Cancela_Transacao_Assinatura_Multipla.xsd', 'servico/Cancela_Assinatura/V1/Cancela_Assinatura_Req.xsl', 'Cancela_Transacao_Assinatura_Multipla/V1/Cancela_Transacao_Assinatura_Multipla.xsd', 'servico/Cancela_Assinatura/V1/Cancela_Assinatura_Resp.xsl', sysdate, 1, 0, '1.0');

--update mtx.mtxtb001_servico set no_conexao = 'java:jboss/jms/SimtxQueueConnectionFactorySIMTX', no_fila_requisicao = 'java:jboss/jms/sibar_req_migrado', no_fila_resposta = 'java:jboss/jms/sibar_rsp_migrado' where nu_servico = 110022;

--update mtx.mtxtb011_versao_servico set ic_servico_migrado = 1 where nu_servico_001 = 110022; 

update mtx.mtxtb011_versao_servico set de_xsd_requisicao = 'validacao_entradas/SIMTX_TYPES/Valida_Assinatura_Multipla/V1/Valida_Assinatura_Multipla.xsd' where nu_servico_001 = 110022;
update mtx.mtxtb011_versao_servico set de_xsd_resposta = 'validacao_entradas/SIMTX_TYPES/Valida_Assinatura_Multipla/V1/Valida_Assinatura_Multipla.xsd' where nu_servico_001 = 110022;

update mtx.mtxtb011_versao_servico set de_xslt_requisicao = 'servico/Valida_Assinatura_Multipla/V1/Valida_Assinatura_Multipla_Req.xsl' where nu_servico_001 = 110022;

--update mtx.mtxtb011_versao_servico set de_xslt_requisicao = 'servico/Cancela_Assinatura/V1/Cancela_Assinatura_Req.xsl' where nu_servico_001 = 110025;
--update mtx.mtxtb011_versao_servico set de_xslt_resposta = 'servico/Cancela_Assinatura/V1/Cancela_Assinatura_Resp.xsl' where nu_servico_001 = 110025;

update mtx.mtxtb011_versao_servico set de_xsd_requisicao = 'validacao_entradas/SIMTX_TYPES/Cancela_Transacao_Assinatura_Multipla/V1/Cancela_Transacao_Assinatura_Multipla.xsd' where nu_servico_001 = 110025;
update mtx.mtxtb011_versao_servico set de_xsd_resposta = 'Cancela_Transacao_Assinatura_Multipla/V1/Cancela_Transacao_Assinatura_Multipla.xsd' where nu_servico_001 = 110025;

update mtx.mtxtb012_versao_tarefa set de_xslt_resposta = 'negocial/Cancela_Assinatura_Multipla/V1/cancelaAssinaturaMultipla_Resp.xsl' where nu_tarefa_002 = 100047;
update mtx.mtxtb012_versao_tarefa set de_xslt_requisicao = '-' where nu_tarefa_002 = 100047;

update mtx.mtxtb012_versao_tarefa set de_xslt_requisicao = 'meio_entrada/assinatura_multipla/valida_assinatura/V1/assinaturaMultipla_Req.xsl' where nu_tarefa_002 = 100056;
update mtx.mtxtb012_versao_tarefa set de_xslt_resposta = 'meio_entrada/assinatura_multipla/valida_assinatura/V1/validaAssinaturaMultipla_Resp.xsl' where nu_tarefa_002 = 100056;

update mtx.mtxtb011_versao_servico set de_xslt_requisicao = '-' where nu_servico_001 = 110024;
update mtx.mtxtb011_versao_servico set de_xslt_resposta = 'servico/Detalhe_Transacao_Pendente_Assinatura/V1/Detalhe_Transacao_Pendente_Resp.xsl' where nu_servico_001 = 110024;
update mtx.mtxtb012_versao_tarefa set de_xslt_resposta = 'negocial/Detalhe_Transacao_Pendente_Assinatura/V1/Detalhe_Transacao_Pendente_Resp.xsl' where nu_tarefa_002 = 100046;
update mtx.mtxtb012_versao_tarefa set de_xslt_requisicao = '-' where nu_tarefa_002 = 100046;

insert into mtx.MTXTB002_TAREFA
(nu_tarefa, no_tarefa, dh_atualizacao, ic_tipo_tarefa, ic_tarefa_financeira, no_servico_barramento, no_operacao_barramento)
values
(100046, 'Detalhe de Transação Pendente de Assinatura', sysdate, 2, 0, 'consulta_assinatura_eletronica', 'DETALHE_TRANSACAO_PENDENTE');

insert into mtx.mtxtb012_versao_tarefa
(nu_tarefa_002, nu_versao_tarefa, ic_situacao, ic_desfazimento, ic_paralelismo, ic_assincrono, de_xsd_requisicao, de_xslt_requisicao, de_xsd_resposta, de_xslt_resposta, dh_atualizacao, de_xslt_parametro, co_versao_barramento)
values 
(100046, 1, 1, 0, 0, 1, '-', '-', '-', 'negocial/Detalhe_Transacao_Pendente_Assinatura/V1/Detalhe_Transacao_Pendente_Resp.xsl', sysdate, NULL, '1.0');

--meio entrada

--meio entrada x servico 
insert into mtx.mtxtb018_vrso_meio_entra_srvco (nu_meio_entrada_008, nu_servico_011, nu_versao_servico_011, ic_situacao, dh_atualizacao)
 values (2, 110030, 1, 1, sysdate);
 
insert into mtx.mtxtb018_vrso_meio_entra_srvco (nu_meio_entrada_008, nu_servico_011, nu_versao_servico_011, ic_situacao, dh_atualizacao)
 values (5, 110030, 1, 1, sysdate);
 
insert into mtx.mtxtb018_vrso_meio_entra_srvco (nu_meio_entrada_008, nu_servico_011, nu_versao_servico_011, ic_situacao, dh_atualizacao)
 values (5, 110023, 1, 1, sysdate);

insert into mtx.mtxtb018_vrso_meio_entra_srvco (nu_meio_entrada_008, nu_servico_011, nu_versao_servico_011, ic_situacao, dh_atualizacao)
 values (5, 110022, 1, 1, sysdate);
 
insert into mtx.mtxtb018_vrso_meio_entra_srvco (nu_meio_entrada_008, nu_servico_011, nu_versao_servico_011, ic_situacao, dh_atualizacao)
 values (5, 110024, 1, 1, sysdate);

insert into mtx.mtxtb018_vrso_meio_entra_srvco (nu_meio_entrada_008, nu_servico_011, nu_versao_servico_011, ic_situacao, dh_atualizacao)
 values (5, 110025, 1, 1, sysdate);
 

--meio entrada x tarefa
insert into mtx.mtxtb010_vrso_tarfa_meio_entra(nu_tarefa_012, nu_versao_tarefa_012, nu_meio_entrada_008, ic_situacao) values (100043, 1, 2, 1);

insert into mtx.mtxtb010_vrso_tarfa_meio_entra(nu_tarefa_012, nu_versao_tarefa_012, nu_meio_entrada_008, ic_situacao) values (100043, 1, 5, 1);

insert into mtx.mtxtb010_vrso_tarfa_meio_entra(nu_tarefa_012, nu_versao_tarefa_012, nu_meio_entrada_008, ic_situacao) values (100044, 1, 5, 1);

insert into mtx.mtxtb010_vrso_tarfa_meio_entra(nu_tarefa_012, nu_versao_tarefa_012, nu_meio_entrada_008, ic_situacao) values (100046, 1, 5, 1);
--tarefas
--tarefa valida assinatura
--update mtx.mtxtb012_versao_tarefa set de_xsd_requisicao = 'validacao_entradas/SIMTX_TYPES/Valida_Assinatura_Multipla/V1/Valida_Assinatura_Multipla.xsd' where nu_tarefa_002 = 100022;
--update mtx.mtxtb012_versao_tarefa set de_xsd_resposta = 'validacao_entradas/SIMTX_TYPES/Valida_Assinatura_Multipla/V1/Valida_Assinatura_Multipla.xsd' where nu_tarefa_002 = 100022;

--tarefa ass. simples
update mtx.mtxtb002_tarefa set no_servico_barramento = 'valida_permissao', no_operacao_barramento = 'ASSINATURA_SIMPLES' where nu_tarefa = 100043;
update mtx.mtxtb002_tarefa set no_tarefa = 'Valida Assinatura Simples (Meio Entrada)' where nu_tarefa = 100043;
update mtx.mtxtb012_versao_tarefa set ic_assincrono  = 1 where nu_tarefa_002 = 100043;
update mtx.mtxtb012_versao_tarefa set de_xslt_resposta = 'meio_entrada/assinatura_simples/V1/validaAssinaturaSimples_Resp.xsl' where nu_tarefa_002 = 100043;
update mtx.mtxtb012_versao_tarefa set de_xslt_requisicao = '-' where nu_tarefa_002 = 100043;

update mtx.mtxtb012_versao_tarefa set de_xslt_requisicao = 'meio_entrada/assinatura_multipla/valida_assinatura/V1/assinaturaMultipla_Req.xsl' where nu_tarefa_002 = 100056;

insert into mtx.mtxtb002_tarefa (nu_tarefa, no_tarefa, no_servico_barramento, no_operacao_barramento, ic_tarefa_financeira, ic_tipo_tarefa, dh_atualizacao) values 
(100056, 'Valida Assinatura Múltipla (Meio de Entrada)', 'assinatura_multipla', 'VALIDA_ASSINATURA_MULTIPLA', 0, 3, sysdate);

insert into mtx.mtxtb012_versao_tarefa (nu_tarefa_002, nu_versao_tarefa, ic_situacao, ic_desfazimento, ic_paralelismo, ic_assincrono, de_xsd_requisicao, de_xslt_requisicao,de_xsd_resposta, de_xslt_resposta, de_xslt_parametro, dh_atualizacao) values
(100056, 1, 1, 0, 0, 1, 'validacao_entradas/SIBAR/MEIOS_DE_ENTRADA/VALIDA_ASSINATURA_MULTIPLA/V1/Valida_Assinatura_Multipla.xsd', 'meio_entrada/V1/assinaturaMultipla_Req.xsl', 'validacao_entradas/SIBAR/MEIOS_DE_ENTRADA/VALIDA_ASSINATURA_MULTIPLA/V1/Valida_Assinatura_Multipla.xsd', 'meio_entrada/V1/assinaturaMultipla_Resp.xsl', null, SYSDATE);

insert into mtx.mtxtb024_tarefa_fila (nu_tarefa_012, nu_versao_tarefa_012, sg_sistema_corporativo, no_fila_requisicao, qt_sgndo_lmte_requisicao, no_fila_resposta, qt_sgndo_lmte_resposta, no_conexao, no_modo_integracao, no_recurso, qt_tempo_espera) 
 values (100056, 1, 'SIPER', 'java:jboss/jms/req_consulta_assinatura_eletronica', 5000, 'java:jboss/jms/rsp_consulta_assinatura_eletronica', 5000, 'java:jboss/jms/SimtxQueueConnectionFactorySIMTX', 'MQ', 'SIBAR.REQ.VALIDA_PERMISSAO', 3);
 
update mtx.mtxtb024_tarefa_fila set no_modo_integracao = 'MQ', no_recurso = 'SIBAR.REQ.VALIDA_PERMISSAO', qt_tempo_espera = 3  where nu_tarefa_012 = 100044;
update mtx.mtxtb024_tarefa_fila set no_modo_integracao = 'MQ' where nu_tarefa_012 = 100044;
update mtx.mtxtb012_versao_tarefa set ic_assincrono = 1 where nu_tarefa_002 = 100044;
 
--tarefa contra ordem
insert into mtx.mtxtb002_tarefa (nu_tarefa, no_tarefa, no_servico_barramento, no_operacao_barramento, ic_tarefa_financeira, ic_tipo_tarefa, dh_atualizacao) values 
(100055, 'Contra Ordem Cheques', 'manutencao_cheque', 'CONTRA_ORDEM', 0, 2, sysdate);

insert into mtx.mtxtb012_versao_tarefa (nu_tarefa_002, nu_versao_tarefa, ic_situacao, ic_desfazimento, ic_paralelismo, ic_assincrono, de_xsd_requisicao, de_xslt_requisicao,de_xsd_resposta, de_xslt_resposta, de_xslt_parametro, dh_atualizacao) values
(100055, 1, 1, 0, 0, 1, 'validacao_entradas/SIBAR/MEIOS_DE_ENTRADA/CONTRA_ORDEM_CHEQUES/V1/Contra_Ordem_Cheque.xsd', '-', 'validacao_entradas/SIBAR/MEIOS_DE_ENTRADA/CONTRA_ORDEM_CHEQUES/V1/Contra_Ordem_Cheque.xsd', 'negocial/contra_ordem_cheques/V1/contraOrdemCheques_Resp.xsl', null, sysdate);

insert into mtx.mtxtb024_tarefa_fila (nu_tarefa_012, nu_versao_tarefa_012, sg_sistema_corporativo, no_fila_requisicao, qt_sgndo_lmte_requisicao, no_fila_resposta, qt_sgndo_lmte_resposta, no_conexao, no_modo_integracao, no_recurso, qt_tempo_espera) 
 VALUES (100055, 1, 'SINAU', 'java:jboss/jms/req_contra_ordem_cheques', 5000, 'java:jboss/jms/rsp_contra_ordem_cheques', 5000, 'java:jboss/jms/SimtxQueueConnectionFactorySIMTX', 'MQ', 'SIBAR.REQ.CONTRA_ORDEM_CHEQUES', 3);

-- servico x tarefa
insert into mtx.mtxtb003_servico_tarefa (nu_servico_011, nu_versao_servico_011, nu_tarefa_012, nu_versao_tarefa_012, nu_sequencia_execucao, ic_situacao, ic_impedimento, de_xslt_nova_tarefa)
 VALUES(110030, 1, 100043, 1, 1, 1, 1, null);

INSERT INTO MTX.MTXTB003_SERVICO_TAREFA (NU_SERVICO_011, NU_VERSAO_SERVICO_011, NU_TAREFA_012, NU_VERSAO_TAREFA_012, NU_SEQUENCIA_EXECUCAO, IC_SITUACAO, IC_IMPEDIMENTO, DE_XSLT_NOVA_TAREFA)
 VALUES(110030, 1, 100055, 1, 2, 1, 1, null);
 
INSERT INTO MTX.MTXTB003_SERVICO_TAREFA (NU_SERVICO_011, NU_VERSAO_SERVICO_011, NU_TAREFA_012, NU_VERSAO_TAREFA_012, NU_SEQUENCIA_EXECUCAO, IC_SITUACAO, IC_IMPEDIMENTO, DE_XSLT_NOVA_TAREFA)
 VALUES(110030, 1, 100056, 1, 1, 1, 1, null);
 
--canal x servico x tarefa
insert into mtx.mtxtb005_servico_canal (nu_servico_001, nu_canal_004, ic_stco_srvco_canal) VALUES (110030, 110, 1);

insert into mtx.mtxtb020_srvco_tarfa_canal (nu_servico_003, nu_versao_servico_003, nu_tarefa_003, nu_versao_tarefa_003, nu_canal_004) values (110030, 1, 100043, 1, 110);
insert into mtx.mtxtb020_srvco_tarfa_canal (nu_servico_003, nu_versao_servico_003, nu_tarefa_003, nu_versao_tarefa_003, nu_canal_004) values (110030, 1, 100055, 1, 110);
insert into mtx.mtxtb020_srvco_tarfa_canal (nu_servico_003, nu_versao_servico_003, nu_tarefa_003, nu_versao_tarefa_003, nu_canal_004) values (110030, 1, 100056, 1, 110);

--mensagens
insert into mtx.mtxtb007_tarefa_mensagem (nu_tarefa_012, nu_mensagem_006, no_campo_retorno, nu_versao_tarefa_012) values (100043, 1, 'CONTROLE_NEGOCIAL.COD_RETORNO', 1);
insert into mtx.mtxtb007_tarefa_mensagem (nu_tarefa_012, nu_mensagem_006, no_campo_retorno, nu_versao_tarefa_012) values (100044, 1, 'CONTROLE_NEGOCIAL.COD_RETORNO', 1);
insert into mtx.mtxtb007_tarefa_mensagem (nu_tarefa_012, nu_mensagem_006, no_campo_retorno, nu_versao_tarefa_012) values (100045, 1, 'CONTROLE_NEGOCIAL.COD_RETORNO', 1);
insert into mtx.mtxtb007_tarefa_mensagem (nu_tarefa_012, nu_mensagem_006, no_campo_retorno, nu_versao_tarefa_012) values (100046, 1, 'CONTROLE_NEGOCIAL.COD_RETORNO', 1);
insert into mtx.mtxtb007_tarefa_mensagem (nu_tarefa_012, nu_mensagem_006, no_campo_retorno, nu_versao_tarefa_012) values (100047, 1, 'CONTROLE_NEGOCIAL.COD_RETORNO', 1);
insert into mtx.mtxtb007_tarefa_mensagem (nu_tarefa_012, nu_mensagem_006, no_campo_retorno, nu_versao_tarefa_012) values (100055, 1, 'CONTROLE_NEGOCIAL.COD_RETORNO', 1);
insert into mtx.mtxtb007_tarefa_mensagem (nu_tarefa_012, nu_mensagem_006, no_campo_retorno, nu_versao_tarefa_012) values (100056, 1, 'CONTROLE_NEGOCIAL.COD_RETORNO', 1);

-- parametros
INSERT INTO MTX.MTXTB013_PRMTO_SRVCO_CANAL (NU_PARAMETRO, NU_SERVICO_005, NU_CANAL_005, HH_LIMITE_INICIO, HH_LIMITE_FIM, DT_INCO_SLCTO_SRVCO, DT_FIM_SLCTO_SRVCO, VR_MNMO_SLCTO_SRVCO, VR_MXMO_SLCTO_SRVCO, DH_ATUALIZACAO) VALUES (110, 110030, 110, TO_DATE('01/01/2015 08:02:44', 'dd/mm/yyyy hh24:mi:ss'), TO_DATE('01/01/2020 21:02:44', 'dd/mm/yyyy hh24:mi:ss'), TO_DATE('01/01/2015 08:02:44', 'dd/mm/yyyy hh24:mi:ss'), TO_DATE('01/01/2020 08:02:44', 'dd/mm/yyyy hh24:mi:ss'), 1, 1000, SYSDATE);
INSERT INTO MTX.MTXTB013_PRMTO_SRVCO_CANAL (NU_PARAMETRO, NU_SERVICO_005, NU_CANAL_005, HH_LIMITE_INICIO, HH_LIMITE_FIM, DT_INCO_SLCTO_SRVCO, DT_FIM_SLCTO_SRVCO, VR_MNMO_SLCTO_SRVCO, VR_MXMO_SLCTO_SRVCO, DH_ATUALIZACAO) VALUES (111, 110023, 110, TO_DATE('01/01/2015 08:02:44', 'dd/mm/yyyy hh24:mi:ss'), TO_DATE('01/01/2020 21:02:44', 'dd/mm/yyyy hh24:mi:ss'), TO_DATE('01/01/2015 08:02:44', 'dd/mm/yyyy hh24:mi:ss'), TO_DATE('01/01/2020 08:02:44', 'dd/mm/yyyy hh24:mi:ss'), 1, 1000, SYSDATE);
INSERT INTO MTX.MTXTB013_PRMTO_SRVCO_CANAL (NU_PARAMETRO, NU_SERVICO_005, NU_CANAL_005, HH_LIMITE_INICIO, HH_LIMITE_FIM, DT_INCO_SLCTO_SRVCO, DT_FIM_SLCTO_SRVCO, VR_MNMO_SLCTO_SRVCO, VR_MXMO_SLCTO_SRVCO, DH_ATUALIZACAO) VALUES (112, 110024, 110, TO_DATE('01/01/2015 08:02:44', 'dd/mm/yyyy hh24:mi:ss'), TO_DATE('01/01/2020 21:02:44', 'dd/mm/yyyy hh24:mi:ss'), TO_DATE('01/01/2015 08:02:44', 'dd/mm/yyyy hh24:mi:ss'), TO_DATE('01/01/2020 08:02:44', 'dd/mm/yyyy hh24:mi:ss'), 1, 1000, SYSDATE);
INSERT INTO MTX.MTXTB013_PRMTO_SRVCO_CANAL (NU_PARAMETRO, NU_SERVICO_005, NU_CANAL_005, HH_LIMITE_INICIO, HH_LIMITE_FIM, DT_INCO_SLCTO_SRVCO, DT_FIM_SLCTO_SRVCO, VR_MNMO_SLCTO_SRVCO, VR_MXMO_SLCTO_SRVCO, DH_ATUALIZACAO) VALUES (113, 110025, 110, TO_DATE('01/01/2015 08:02:44', 'dd/mm/yyyy hh24:mi:ss'), TO_DATE('01/01/2020 21:02:44', 'dd/mm/yyyy hh24:mi:ss'), TO_DATE('01/01/2015 08:02:44', 'dd/mm/yyyy hh24:mi:ss'), TO_DATE('01/01/2020 08:02:44', 'dd/mm/yyyy hh24:mi:ss'), 1, 1000, SYSDATE);
