/*==============================================================*/
/* Drop Constraints e Tables:                                   */
/*==============================================================*/

alter table MTX.MTXTB003_SERVICO_TAREFA
   drop constraint FK_MTXTB003_MTXTB011;

alter table MTX.MTXTB003_SERVICO_TAREFA
   drop constraint FK_MTXTB003_MTXTB012;

alter table MTX.MTXTB005_SERVICO_CANAL
   drop constraint FK_MTXTB005_MTXTB001;

alter table MTX.MTXTB005_SERVICO_CANAL
   drop constraint FK_MTXTB005_MTXTB004;

alter table MTX.MTXTB007_TAREFA_MENSAGEM
   drop constraint FK_MTXTB007_MTXTB002;

alter table MTX.MTXTB007_TAREFA_MENSAGEM
   drop constraint FK_MTXTB007_MTXTB006;

alter table MTX.MTXTB009_VRSO_MEIO_ENTRADA
   drop constraint FK_MTXTB009_MTXTB008;

alter table MTX.MTXTB010_VRSO_TARFA_MEIO_ENTRA
   drop constraint FK_MTXTB010_MTXTB009;

alter table MTX.MTXTB010_VRSO_TARFA_MEIO_ENTRA
   drop constraint FK_MTXTB010_MTXTB012;

alter table MTX.MTXTB011_VERSAO_SERVICO
   drop constraint FK_MTXTB011_MTXTB001;

alter table MTX.MTXTB012_VERSAO_TAREFA
   drop constraint FK_MTXTB012_MTXTB002;

alter table MTX.MTXTB013_PRMTO_SRVCO_CANAL
   drop constraint FK_MTXTB013_MTXTB005;

alter table MTX.MTXTB015_SRVCO_TRNSO_TARFA
   drop constraint FK_MTXTB015_MTXTB012;

alter table MTX.MTXTB015_SRVCO_TRNSO_TARFA
   drop constraint FK_MTXTB015_MTXTB017;

alter table MTX.MTXTB016_ITERACAO_CANAL
   drop constraint FK_MTXTB016_MTXTB004;

alter table MTX.MTXTB016_ITERACAO_CANAL
   drop constraint FK_MTXTB016_MTXTB014;

alter table MTX.MTXTB017_VERSAO_SRVCO_TRNSO
   drop constraint FK_MTXTB017_MTXTB011;

alter table MTX.MTXTB017_VERSAO_SRVCO_TRNSO
   drop constraint FK_MTXTB017_MTXTB014;

alter table MTX.MTXTB018_VRSO_MEIO_ENTRA_SRVCO
   drop constraint FK_MTXTB018_MTXTB009;

alter table MTX.MTXTB018_VRSO_MEIO_ENTRA_SRVCO
   drop constraint FK_MTXTB018_MTXTB011;

alter table MTX.MTXTB020_SRVCO_TARFA_CANAL
   drop constraint FK_MTXTB020_MTXTB003;

alter table MTX.MTXTB020_SRVCO_TARFA_CANAL
   drop constraint FK_MTXTB020_MTXTB004;

alter table MTX.MTXTB021_USUARIO_CANAL
   drop constraint FK_MTXTB021_MTXTB004;

alter table MTX.MTXTB021_USUARIO_CANAL
   drop constraint FK_MTXTB021_MTXTB019;

drop table MTX.MTXTB001_SERVICO cascade constraints;

drop table MTX.MTXTB002_TAREFA cascade constraints;

drop table MTX.MTXTB003_SERVICO_TAREFA cascade constraints;

drop table MTX.MTXTB004_CANAL cascade constraints;

drop table MTX.MTXTB005_SERVICO_CANAL cascade constraints;

drop table MTX.MTXTB006_MENSAGEM cascade constraints;

drop table MTX.MTXTB007_TAREFA_MENSAGEM cascade constraints;

drop table MTX.MTXTB008_MEIO_ENTRADA cascade constraints;

drop table MTX.MTXTB009_VRSO_MEIO_ENTRADA cascade constraints;

drop table MTX.MTXTB010_VRSO_TARFA_MEIO_ENTRA cascade constraints;

drop table MTX.MTXTB011_VERSAO_SERVICO cascade constraints;

drop table MTX.MTXTB012_VERSAO_TAREFA cascade constraints;

drop table MTX.MTXTB013_PRMTO_SRVCO_CANAL cascade constraints;

drop table MTX.MTXTB014_TRANSACAO cascade constraints;

drop table MTX.MTXTB015_SRVCO_TRNSO_TARFA cascade constraints;

drop table MTX.MTXTB016_ITERACAO_CANAL cascade constraints;

drop table MTX.MTXTB017_VERSAO_SRVCO_TRNSO cascade constraints;

drop table MTX.MTXTB018_VRSO_MEIO_ENTRA_SRVCO cascade constraints;

drop table MTX.MTXTB019_PERFIL_USUARIO cascade constraints;

drop table MTX.MTXTB020_SRVCO_TARFA_CANAL cascade constraints;

drop table MTX.MTXTB021_USUARIO_CANAL cascade constraints;

drop table MTX.MTXTB023_PARAMETRO_SISTEMA cascade constraints;