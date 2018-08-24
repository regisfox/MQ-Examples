/*==============================================================*/
/* Create Sequences                                                    */
/*==============================================================*/
create sequence MTX.MTXSQ014_NU_NSU_TRANSACAO
increment by 1
start with 1
nocycle
 nocache;

create sequence MTX.MTXSQ016_NU_ITERACAO_CANAL
increment by 1
start with 1
 nomaxvalue
nocycle
 cache 20;