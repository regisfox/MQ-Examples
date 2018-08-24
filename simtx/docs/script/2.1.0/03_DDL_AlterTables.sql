/*==============================================================*/
/* Table: MTXTB001_SERVICO                      */
/*==============================================================*/
ALTER TABLE MTX.MTXTB001_SERVICO ADD IC_CANCELAMENTO_TRANSACAO NUMBER(1) NOT NULL;

comment on column MTX.MTXTB001_SERVICO.IC_CANCELAMENTO_TRANSACAO is
'Indicador que demonstra se o servi�o � pass�vel de cancelamento. 
Por exemplo, pagamento de boleto, ted, doc, e tev. Valores poss�veis: 0 - N�o, 1 - Sim';
