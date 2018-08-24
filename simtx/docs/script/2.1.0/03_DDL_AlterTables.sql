/*==============================================================*/
/* Table: MTXTB001_SERVICO                      */
/*==============================================================*/
ALTER TABLE MTX.MTXTB001_SERVICO ADD IC_CANCELAMENTO_TRANSACAO NUMBER(1) NOT NULL;

comment on column MTX.MTXTB001_SERVICO.IC_CANCELAMENTO_TRANSACAO is
'Indicador que demonstra se o serviço é passível de cancelamento. 
Por exemplo, pagamento de boleto, ted, doc, e tev. Valores possíveis: 0 - Não, 1 - Sim';
