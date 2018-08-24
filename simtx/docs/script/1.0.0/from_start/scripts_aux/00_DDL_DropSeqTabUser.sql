/*==============================================================*/
/* Drop Sequences, Tablespaces e User:                          */
/*==============================================================*/

drop sequence MTX.MTXSQ014_NU_NSU_TRANSACAO;

drop sequence MTX.MTXSQ016_NU_ITERACAO_CANAL;

drop tablespace MTXTSDT001 including contents cascade constraints;

drop tablespace MTXTSIX001 including contents cascade constraints;

drop user MTX;