drop table MTX.MTXTB020_SRVCO_TARFA_CANAL;
drop table MTX.MTXTB018_VRSO_MEIO_ENTRA_SRVCO;
drop table MTX.MTXTB016_ITERACAO_CANAL;
drop table MTX.MTXTB015_SRVCO_TRNSO_TARFA;
drop table MTX.MTXTB013_PRMTO_SRVCO_CANAL;
drop table MTX.MTXTB010_VRSO_TARFA_MEIO_ENTRA;
drop table MTX.MTXTB008_MEIO_ENTRADA;
drop table MTX.MTXTB007_TAREFA_MENSAGEM;
drop table MTX.MTXTB006_MENSAGEM;
drop table MTX.MTXTB005_SERVICO_CANAL;
drop table MTX.MTXTB004_CANAL;
drop table MTX.MTXTB017_VERSAO_SRVCO_TRNSO;
drop table MTX.MTXTB014_TRANSACAO;
drop table MTX.MTXTB003_SERVICO_TAREFA;
drop table MTX.MTXTB024_TAREFA_FILA;
drop table MTX.MTXTB012_VERSAO_TAREFA;
drop table MTX.MTXTB002_TAREFA;
drop table MTX.MTXTB011_VERSAO_SERVICO;
drop table MTX.MTXTB001_SERVICO;
drop table MTX.MTXTB023_PARAMETRO_SISTEMA;
commit;



/*==============================================================*/
/* Table: MTXTB001_SERVICO                                      */
/*==============================================================*/
create table MTXTB001_SERVICO 
(
   NU_SERVICO                numeric(6)                     not null
      constraint CKC_NU_SERVICO_MTXTB001 check (NU_SERVICO >= 1),
   NO_SERVICO                varchar(100)                   null,
   NO_SERVICO_BARRAMENTO	 varchar(50)		 			not null,
   NO_OPERACAO_BARRAMENTO	 varchar(50)		 			not null,
   IC_SITUACAO_SERVICO       numeric(1)                     not null
      constraint CKC_IC_SITUACAO_SERVI_MTXTB001 check (IC_SITUACAO_SERVICO between 0 and 1 and IC_SITUACAO_SERVICO in (0,1)),
   IC_CONFIRMACAO_TRANSACAO  numeric(1)                     not null,
   IC_TIPO_SERVICO           numeric(1)                     not null,
   DH_ATUALIZACAO            date                           not null,
   QT_SGNDO_LMTE_RESPOSTA    numeric(4)                     not null,
   NO_CONEXAO                varchar(50)                    not null, 
   NO_FILA_REQUISICAO        varchar(50)                    not null, 
   NO_FILA_RESPOSTA          varchar(50)                    not null, 
   constraint PK_MTXTB001 primary key (NU_SERVICO)
);

comment on table MTXTB001_SERVICO is 
'Representa serviços a serem consumidos pelos Canais. Um serviço é composto por um conjunto de tarfeas. 
Exemplo: Para a solicitação de saldo deve-se primeiro solicitar a autenticação e depois a consulta do saldo sendo que cada um deles é considerado como tarefa.';

comment on column MTXTB001_SERVICO.NU_SERVICO is 
'Identificador numérico do serviço executado.';

comment on column MTXTB001_SERVICO.NO_SERVICO is 
'Denominação do Serviço.';

comment on column MTX.MTXTB001_SERVICO.NO_SERVICO_BARRAMENTO is
'Informa o nome do serviço utilizado pelo SIBAR correspondente ao serviço do SIMTX. 
Ex: CONSULTA_CONTRATOS_RENOVACAO; CONSULTA_SALDO.';

comment on column MTX.MTXTB001_SERVICO.NO_OPERACAO_BARRAMENTO is
'Informa o nome da operação negocial utilizada pelo SIBAR correspondente ao serviço do SIMTX. 
Ex: CONSULTA_CONTRATOS_RENOVACAO; CONSULTA_SALDO_NSGD.';

comment on column MTXTB001_SERVICO.IC_SITUACAO_SERVICO is 
'Indica a situção. Pode ser:0=Inativo | 1=Ativo';

comment on column MTXTB001_SERVICO.IC_CONFIRMACAO_TRANSACAO is 
'Indica se o Serviço requer confirmação para a Transção. Pode ser:0=Não | 1=Sim';

comment on column MTXTB001_SERVICO.IC_TIPO_SERVICO is 
'Indica o Tipo do Serviço que foi solicitado para execução pelo Canal. Cada Tipo de Serviço demandará o envio de TAGs específicas no XML para o Barramento. 
Pode ser:0=Pagamento | 1=Confirmação de Pagamento | 2=Cancelamento de Pagamento | 3=Estorno | 4=Confirmação de Estorno | 5=Cancelamento de Estorno | 6=Consulta';

comment on column MTXTB001_SERVICO.DH_ATUALIZACAO is 
'Dia, mês, ano, hora, minuto e segundos da última atualização.';

comment on column MTX.MTXTB024_TAREFA_FILA.QT_SGNDO_LMTE_REQUISICAO is
'Quantidade de milissegundos correspondente ao tempo limite para o sistema executar a requisição (aplicada à Fila-MQ). 
Caso a requisição não tenha sido realizada dentro desse tempo, o sistema irá executar o timeout (interrupção do serviço).';

comment on column MTX.MTXTB001_SERVICO.NO_CONEXAO is
'Nome da conexão contendo as configurações de fila.';

comment on column MTX.MTXTB001_SERVICO.NO_FILA_REQUISICAO is
'Denominação da propriedade da fila MQ de requisição do serviço.';

comment on column MTX.MTXTB001_SERVICO.NO_FILA_RESPOSTA is
'Denominação da propriedade da fila MQ de resposta do serviço.';

/*==============================================================*/
/* Table: MTXTB002_TAREFA                                       */
/*==============================================================*/
create table MTXTB002_TAREFA 
(
   NU_TAREFA            	numeric(6)                     not null
      constraint CKC_NU_TAREFA_MTXTB002 check (NU_TAREFA >= 1),
   NO_TAREFA            	varchar2(50)                   not null,
   NO_SERVICO_BARRAMENTO	varchar2(50)		 		   not null,
   NO_OPERACAO_BARRAMENTO	varchar2(50)				   not null,
   IC_TAREFA_FINANCEIRA 	numeric(1)                     not null,
   IC_TIPO_TAREFA       	numeric(1)                     not null
      constraint CKC_IC_TIPO_TAREFA_MTXTB002 check (IC_TIPO_TAREFA between 1 and 4 and IC_TIPO_TAREFA in (1,2,3,4)),
   DH_ATUALIZACAO           date                           not null,      
   constraint PK_MTXTB002 primary key (NU_TAREFA)
);

comment on table MTXTB002_TAREFA is 
'Representa as tarefas que podem ser utilizadas na composição dos Serviços.';

comment on column MTXTB002_TAREFA.NU_TAREFA is 
'Identificador numérico da tarefa executada.';

comment on column MTXTB002_TAREFA.NO_TAREFA is 
'Denominação da Tarefa.';

comment on column MTXTB002_TAREFA.NO_SERVICO_BARRAMENTO is 
'Informa o nome do serviço utilizado pelo SIBAR correspondente à tarefa do SIMTX. 
Ex: VALIDA_CARTAO; VALIDA_SENHA.';

comment on column MTXTB002_TAREFA.NO_OPERACAO_BARRAMENTO is 
'Informa o nome da operação negocial utilizada pelo SIBAR correspondente à tarefa do SIMTX. 
Ex: CARTAO_COMERCIAL; SENHA_CONTA.';

comment on column MTXTB002_TAREFA.DH_ATUALIZACAO is 
'Dia, mes, ano, hora, minuto e segundos da última atualização.';

comment on column MTXTB002_TAREFA.IC_TIPO_TAREFA is 
'Indicador do tipo de tarefa. Pode ser:1=Transacional | 2=Negocial | 3=Meio de Entrada | 4=Segurança';

comment on column MTXTB002_TAREFA.IC_TAREFA_FINANCEIRA is 
'Indicador se a tarefa é do tipo financeira, que sensibiliza o corporativo. Será utilizado para identificar a tarefa que contém a fila que será utilizada no serviço de estorno. 
Pode ser:0=Não | 1=Sim. 
Observação: Este campo foi incluído, pois, uma tarefa pode ser do tipo Negocial mas não é financeira. Ex: Onde um serviço possui duas tarefas Negociais - Pagamento FGTS e Inclui SITAE - e apenas Pagamento FGTS ser financeira.';

/*==============================================================*/
/* Table: MTXTB003_SERVICO_TAREFA                               */
/*==============================================================*/
create table MTXTB003_SERVICO_TAREFA 
(
   NU_SERVICO_011        numeric(6)                     not null
      constraint CKC_NU_SERVICO_011_MTXTB003 check (NU_SERVICO_011 >= 1),
   NU_VERSAO_SERVICO_011 numeric(3)                     not null
      constraint CKC_NU_VERSAO_SERVICO_MTXTB003 check (NU_VERSAO_SERVICO_011 >= 1),
   NU_TAREFA_012         numeric(6)                     not null
      constraint CKC_NU_TAREFA_012_MTXTB003 check (NU_TAREFA_012 >= 1),
   NU_VERSAO_TAREFA_012  numeric(3)                     not null
      constraint CKC_NU_VERSAO_TAREFA__MTXTB003 check (NU_VERSAO_TAREFA_012 >= 1),
   NU_SEQUENCIA_EXECUCAO numeric(5)                     not null
      constraint CKC_NU_SEQUENCIA_EXEC_MTXTB003 check (NU_SEQUENCIA_EXECUCAO >= 1),
   IC_SITUACAO           numeric(1)                     not null
      constraint CKC_IC_SITUACAO_MTXTB003 check (IC_SITUACAO between 0 and 1 and IC_SITUACAO in (0,1)),
   IC_IMPEDIMENTO        numeric(1)                     not null,
   DE_XSLT_NOVA_TAREFA   varchar(200)                   null,
      constraint PK_MTXTB003 primary key (NU_SERVICO_011, NU_VERSAO_SERVICO_011, NU_TAREFA_012, NU_VERSAO_TAREFA_012)
);

comment on table MTXTB003_SERVICO_TAREFA is 
'Relação das Tarefas que compoem o serviço e devem ser executadas.';

comment on column MTXTB003_SERVICO_TAREFA.NU_SERVICO_011 is 
'Identificador numérico do serviço executado.';

comment on column MTXTB003_SERVICO_TAREFA.NU_VERSAO_SERVICO_011 is 
'Identificador numérico da versão do serviço.';

comment on column MTXTB003_SERVICO_TAREFA.NU_TAREFA_012 is 
'Identificador numérico da tarefa executada.';

comment on column MTXTB003_SERVICO_TAREFA.NU_VERSAO_TAREFA_012 is 
'Identificador numérico da versão da tarefa vinculada ao serviço.';

comment on column MTXTB003_SERVICO_TAREFA.NU_SEQUENCIA_EXECUCAO is 
'Identificador numérico da sequencia de execução da tarefa. Se os IDs forem iguais, as tarefas devem ser executadas em paralelo.';

comment on column MTXTB003_SERVICO_TAREFA.IC_SITUACAO is 
'Indica a situação. Pode ser:0=Inativo | 1=Ativo';

comment on column MTXTB003_SERVICO_TAREFA.IC_IMPEDIMENTO is 
'Indicador se a tarefa e Impeditiva ou não. Pode ser:0=Não | 1=Sim';

comment on column MTXTB003_SERVICO_TAREFA.DE_XSLT_NOVA_TAREFA is 
'Bloco de texto descritor do nome do arquivo XSLT que cria uma nova tarefa quando o canal não consegue enviar na requisição.';

/*==============================================================*/
/* Table: MTXTB004_CANAL                                        */
/*==============================================================*/
create table MTXTB004_CANAL 
(
   NU_CANAL                         numeric(3)        not null
      constraint CKC_NU_CANAL_MTXTB004 check (NU_CANAL >= 1),
   NO_CANAL                         varchar(50)       not null,
   SG_CANAL                         varchar(5)        not null,
   IC_SITUACAO_CANAL                numeric(1)        not null
      constraint CKC_IC_SITUACAO_CANAL_MTXTB004 check (IC_SITUACAO_CANAL between 0 and 1 and IC_SITUACAO_CANAL in (0,1)),
   NO_FILA_RQSCO_RSLCO_PNDNA        varchar(60)       null,
   NO_FILA_RSPSA_RSLCO_PNDNA        varchar(60)       null,
   QT_SGNDO_LMTE_RQSCO_RSLCO        numeric(4)        null,
   QT_SGNDO_LMTE_RSPSA_RSLCO        numeric(4)        null,
   NO_FILA_RESPOSTA_CANAL           varchar(50)       default '0' not null,
   NO_CONEXAO_CANAL                 varchar(50)       default '0' not null,
   DH_ATUALIZACAO                   date              null,
       constraint PK_MTXTB004 primary key (NU_CANAL)
);

comment on table MTXTB004_CANAL is 
'Representa um canal que poderá consumir serviços do sistema.';

comment on column MTXTB004_CANAL.NU_CANAL is 
'Identificador numérico do Canal.';

comment on column MTXTB004_CANAL.NO_CANAL is 
'Denominação do Canal.';

comment on column MTXTB004_CANAL.SG_CANAL is 
'Informa a sigla do Canal de Atendimento. Ex: SIMAA; SIIBC; SICAQ.';

comment on column MTXTB004_CANAL.IC_SITUACAO_CANAL is 
'Indica a situação. Pode ser:0=Inativo | 1=Ativo';

comment on column MTXTB004_CANAL.NO_FILA_RQSCO_RSLCO_PNDNA is 
'Nome da fila de requisição do canal para o resolve pendência.';

comment on column MTXTB004_CANAL.NO_FILA_RSPSA_RSLCO_PNDNA is 
'Nome da fila de resposta do canal para o resolve pendência.';

comment on column MTXTB004_CANAL.QT_SGNDO_LMTE_RQSCO_RSLCO is 
'Tempo de timeout para a fila de resposta do canal para o resolve pendência.';

comment on column MTXTB004_CANAL.QT_SGNDO_LMTE_RSPSA_RSLCO is 
'Tempo de timeout para a fila de resposta do canal para o resolve pendência.';

comment on column MTXTB004_CANAL.NO_FILA_RESPOSTA_CANAL is 
'Nome da fila de resposta do canal para os servicoS solicitados.';

comment on column MTXTB004_CANAL.NO_CONEXAO_CANAL is 
'Nome da conexão contendo as configurações de fila.';

comment on column MTXTB004_CANAL.DH_ATUALIZACAO is 
'Dia, mes, ano, hora, minuto e segundos da última atualização.';

/*==============================================================*/
/* Table: MTXTB005_SERVICO_CANAL                                */
/*==============================================================*/
create table MTXTB005_SERVICO_CANAL 
(
   NU_SERVICO_001       numeric(6)                     not null
      constraint CKC_NU_SERVICO_001_MTXTB005 check (NU_SERVICO_001 >= 1),
   NU_CANAL_004         numeric(3)                     not null
      constraint CKC_NU_CANAL_004_MTXTB005 check (NU_CANAL_004 >= 1),
   IC_STCO_SRVCO_CANAL  numeric(1)                     not null
      constraint CKC_IC_STCO_SRVCO_CAN_MTXTB005 check (IC_STCO_SRVCO_CANAL between 0 and 1 and IC_STCO_SRVCO_CANAL in (0,1)),
   constraint PK_MTXTB005 primary key (NU_SERVICO_001, NU_CANAL_004)
);

comment on table MTXTB005_SERVICO_CANAL is 
'serviços que os canais podem solicitar.';

comment on column MTXTB005_SERVICO_CANAL.NU_SERVICO_001 is 
'Identificador numérico do serviço executado.';

comment on column MTXTB005_SERVICO_CANAL.NU_CANAL_004 is 
'Identificador numérico do Canal.';

comment on column MTXTB005_SERVICO_CANAL.IC_STCO_SRVCO_CANAL is 
'Indica a situação do serviço do canal. Pode ser:0=Inativo | 1=Ativo';

/*==============================================================*/
/* Table: MTXTB006_MENSAGEM                                     */
/*==============================================================*/
create table MTXTB006_MENSAGEM 
(
   NU_NSU_MENSAGEM      numeric(5)                     not null,
   IC_TIPO_MENSAGEM     numeric(1)                     not null
      constraint CKC_IC_TIPO_MENSAGEM_MTXTB006 check (IC_TIPO_MENSAGEM >= 1),
   CO_MENSAGEM          varchar(3)                     not null,
   DE_MENSAGEM_NEGOCIAL varchar(200)                   not null,
   DE_MENSAGEM_TECNICA  varchar(200)                   not null,
   DH_ATUALIZACAO       date                           not null,
   constraint PK_MTXTB006 primary key (NU_NSU_MENSAGEM)
);

comment on table MTXTB006_MENSAGEM is 
'Mensagens parametrizadas para o retorno aos canais.';

comment on column MTXTB006_MENSAGEM.NU_NSU_MENSAGEM is 
'Identificador numérico da Mensagem.';

comment on column MTXTB006_MENSAGEM.IC_TIPO_MENSAGEM is 
'Indicador do tipo de tratamento de mensagem. Pode ser:1=Impeditiva | 2=Informativa | 3=Autorizadora';

comment on column MTXTB006_MENSAGEM.CO_MENSAGEM is 
'Identificador do código do tipo de mensagem retornada pelos sistemas corporativos da CAIXA.';

comment on column MTXTB006_MENSAGEM.DE_MENSAGEM_NEGOCIAL is 
'Bloco de texto descritor da mensagem negocial a ser apresentada ao canal.';

comment on column MTXTB006_MENSAGEM.DE_MENSAGEM_TECNICA is 
'Bloco de texto descritor da mensagem técnica a ser fornecida ao canal.';

comment on column MTXTB006_MENSAGEM.DH_ATUALIZACAO is 
'Dia, mes, ano, hora, minuto e segundos da última atualização.';

/*==============================================================*/
/* Table: MTXTB007_TAREFA_MENSAGEM                              */
/*==============================================================*/
create table MTXTB007_TAREFA_MENSAGEM 
(
   NU_TAREFA_002        numeric(6)                     not null
      constraint CKC_NU_TAREFA_002_MTXTB007 check (NU_TAREFA_002 >= 1),
   NU_MENSAGEM_006      numeric(5)                     not null,
   constraint PK_MTXTB007 primary key (NU_TAREFA_002, NU_MENSAGEM_006)
);

comment on table MTXTB007_TAREFA_MENSAGEM is 
'Mensagens associadas as tarefas de serviços.';

comment on column MTXTB007_TAREFA_MENSAGEM.NU_TAREFA_002 is 
'Identificador numérico da tarefa executada.';

comment on column MTXTB007_TAREFA_MENSAGEM.NU_MENSAGEM_006 is 
'Identificador numérico da Mensagem.';

/*==============================================================*/
/* Table: MTXTB008_MEIO_ENTRADA                                 */
/*==============================================================*/
create table MTXTB008_MEIO_ENTRADA 
(
   NU_MEIO_ENTRADA      numeric(2)                     not null,
   NO_MEIO_ENTRADA      varchar(100)                   not null,
   IC_SITUACAO          numeric(1)                     not null, 
      constraint CKC_IC_SITUACAO_MTXTB008 check (IC_SITUACAO between 0 and 1 and IC_SITUACAO in (0,1)),
   constraint PK_MTXTB008 primary key (NU_MEIO_ENTRADA)
);

comment on table MTXTB008_MEIO_ENTRADA is 
'Meios de entrada disponíveis para validação do Canal. Exemplo: Validação de TOKEN | Validação de Senha';

comment on column MTXTB008_MEIO_ENTRADA.NU_MEIO_ENTRADA is 
'Identificador numérico do Meio de Entrada.';

comment on column MTXTB008_MEIO_ENTRADA.NO_MEIO_ENTRADA is 
'Denominação do Meio de Entrada.';

comment on column MTXTB008_MEIO_ENTRADA.IC_SITUACAO is 
'Indica a situação. Pode ser:0=Inativo | 1=Ativo';


/*==============================================================*/
/* Table: MTXTB010_VRSO_TARFA_MEIO_ENTRA                        */
/*==============================================================*/
create table MTXTB010_VRSO_TARFA_MEIO_ENTRA 
(
   NU_TAREFA_012               numeric(6)                     not null
      constraint CKC_NU_TAREFA_012_MTXTB010 check (NU_TAREFA_012 >= 1),
   NU_VERSAO_TAREFA_012        numeric(3)                     not null
      constraint CKC_NU_VERSAO_TAREFA__MTXTB010 check (NU_VERSAO_TAREFA_012 >= 1),
   NU_MEIO_ENTRADA_008         numeric(2)                     not null,
      constraint CKC_NU_MEIO_EN_MTXTB010 check (NU_MEIO_ENTRADA_008 >= 1),
   IC_SITUACAO                 numeric(1)                     not null
      constraint CKC_IC_SITUACAO_MTXTB010 check (IC_SITUACAO between 0 and 1 and IC_SITUACAO in (0,1)),
   constraint PK_MTXTB010 primary key (NU_MEIO_ENTRADA_008, NU_TAREFA_012, NU_VERSAO_TAREFA_012)
);

comment on table MTXTB010_VRSO_TARFA_MEIO_ENTRA is 
'Tarefa(s) para autenticação dos meios de entrada.';

comment on column MTXTB010_VRSO_TARFA_MEIO_ENTRA.NU_TAREFA_012 is 
'Identificador numérico da tarefa executada.';

comment on column MTXTB010_VRSO_TARFA_MEIO_ENTRA.NU_VERSAO_TAREFA_012 is 
'Identificador numérico da versão da tarefa vinculada ao serviço.';

comment on column MTXTB010_VRSO_TARFA_MEIO_ENTRA.NU_MEIO_ENTRADA_008 is 
'Identificador numérico do Meio de Entrada.';

comment on column MTXTB010_VRSO_TARFA_MEIO_ENTRA.IC_SITUACAO is 
'Indica a situação. Pode ser:0=Inativo | 1=Ativo';


/*==============================================================*/
/* Table: MTXTB011_VERSAO_SERVICO                               */
/*==============================================================*/
create table MTXTB011_VERSAO_SERVICO 
(
   NU_SERVICO_001            numeric(6)            not null
      constraint CKC_NU_SERVICO_001_MTXTB011 check (NU_SERVICO_001 >= 1),
   NU_VERSAO_SERVICO         numeric(3)            not null
      constraint CKC_NU_VERSAO_SERVICO_MTXTB011 check (NU_VERSAO_SERVICO >= 1),
   CO_VERSAO_BARRAMENTO      varchar(10)           not null,
   DE_XSD_REQUISICAO         varchar(200)          not null,
   DE_XSLT_REQUISICAO        varchar(200)          not null,
   DE_XSD_RESPOSTA           varchar(200)          not null,
   DE_XSLT_RESPOSTA          varchar(200)          not null,
   DH_ATUALIZACAO            date                  not null,
   IC_STCO_VRSO_SRVCO        numeric(1)            not null,
   IC_SERVICO_MIGRADO	     numeric(1)            not null
      constraint CKC_IC_STCO_VRSO_SRVC_MTXTB011 check (IC_STCO_VRSO_SRVCO between 0 and 1 and IC_STCO_VRSO_SRVCO in (0,1)),
   constraint PK_MTXTB011 primary key (NU_SERVICO_001, NU_VERSAO_SERVICO)
);

comment on table MTXTB011_VERSAO_SERVICO is 
'Versão dos serviços.';

comment on column MTXTB011_VERSAO_SERVICO.NU_SERVICO_001 is 
'Identificador numérico do serviço executado.';

comment on column MTXTB011_VERSAO_SERVICO.NU_VERSAO_SERVICO is 
'Identificador numérico da versão do serviço.';

comment on column MTXTB011_VERSAO_SERVICO.CO_VERSAO_BARRAMENTO is 
'Código da versão do layout do barramento.';

comment on column MTXTB011_VERSAO_SERVICO.DE_XSD_REQUISICAO is 
'Bloco de texto descritor do nome do arquivo XSD de requisição.';

comment on column MTXTB011_VERSAO_SERVICO.DE_XSLT_REQUISICAO is 
'Bloco de texto descritor do nome do arquivo XSLT de requisição.';

comment on column MTXTB011_VERSAO_SERVICO.DE_XSD_RESPOSTA is 
'Bloco de texto descritor do nome do arquivo XSD de resposta.';

comment on column MTXTB011_VERSAO_SERVICO.DE_XSLT_RESPOSTA is 
'Bloco de texto descritor do nome do arquivo XSLT de resposta.';

comment on column MTXTB011_VERSAO_SERVICO.DH_ATUALIZACAO is 
'Dia, mes, ano, hora, minuto e segundos da última atualização.';

comment on column MTXTB011_VERSAO_SERVICO.IC_STCO_VRSO_SRVCO is 
'Indica a situação. Pode ser:0=Inativo | 1=Ativo';


/*==============================================================*/
/* Table: MTXTB012_VERSAO_TAREFA                                */
/*==============================================================*/
create table MTXTB012_VERSAO_TAREFA 
(
   NU_TAREFA_002              numeric(6)         not null
      constraint CKC_NU_TAREFA_002_MTXTB012 check (NU_TAREFA_002 >= 1),
   NU_VERSAO_TAREFA           numeric(3)         not null
      constraint CKC_NU_VERSAO_TAREFA_MTXTB012 check (NU_VERSAO_TAREFA >= 1),
   IC_SITUACAO                numeric(1)         not null
      constraint CKC_IC_SITUACAO_MTXTB012 check (IC_SITUACAO between 0 and 1 and IC_SITUACAO in (0,1)),
   IC_DESFAZIMENTO            numeric(1)         not null,
   IC_PARALELISMO             numeric(1)         not null,
   IC_ASSINCRONO              numeric(1)         not null,
   DE_XSD_REQUISICAO          varchar(200)       not null,
   DE_XSLT_REQUISICAO         varchar(200)       not null,
   DE_XSD_RESPOSTA            varchar(200)       null,
   DE_XSLT_RESPOSTA           varchar(200)       null,
   DE_XSLT_PARAMETRO          varchar(200)       null,
   DH_ATUALIZACAO             date               not null,
   constraint PK_MTXTB012 primary key (NU_TAREFA_002, NU_VERSAO_TAREFA)
);

comment on table MTXTB012_VERSAO_TAREFA is 
'Versão das tarefas.';

comment on column MTXTB012_VERSAO_TAREFA.NU_TAREFA_002 is 
'Identificador numérico da tarefa executada.';

comment on column MTXTB012_VERSAO_TAREFA.NU_VERSAO_TAREFA is 
'Identificador numérico da versão da tarefa vinculada ao serviço.';

comment on column MTXTB012_VERSAO_TAREFA.IC_SITUACAO is 
'Indica a situação. Pode ser:0=Inativo | 1=Ativo';

comment on column MTXTB012_VERSAO_TAREFA.IC_DESFAZIMENTO is 
'Indica se a tarefa requer Desfazimento em caso de falha. Pode ser:0=Não | 1=Sim';

comment on column MTXTB012_VERSAO_TAREFA.IC_PARALELISMO is 
'Indicar se a tarefa pode ser executada em Paralelo. Pode ser:0=Não | 1=Sim';

comment on column MTXTB012_VERSAO_TAREFA.IC_ASSINCRONO is 
'Indicador se a tarefa é do tipo Assincrona ou Sincrona. Pode ser:0=Assincrona | 1=Sincrona';

comment on column MTXTB012_VERSAO_TAREFA.DE_XSD_REQUISICAO is 
'Bloco de texto descritor do nome do arquivo XSD de requisição.';

comment on column MTXTB012_VERSAO_TAREFA.DE_XSLT_REQUISICAO is 
'Bloco de texto descritor do nome do arquivo XSLT de requisição.';

comment on column MTXTB012_VERSAO_TAREFA.DE_XSLT_PARAMETRO is
'Bloco de texto descritor do nome do arquivo XSLT de tratamento dos parametros.';

comment on column MTXTB012_VERSAO_TAREFA.DE_XSD_RESPOSTA is 
'Bloco de texto descritor do nome do arquivo XSD de resposta.';

comment on column MTXTB012_VERSAO_TAREFA.DE_XSLT_RESPOSTA is 
'Bloco de texto descritor do nome do arquivo XSLT de resposta.';

comment on column MTXTB012_VERSAO_TAREFA.DH_ATUALIZACAO is 
'Dia, mes, ano, hora, minuto e segundos da última atualização.';


/*==============================================================*/
/* Table: MTXTB013_PRMTO_SRVCO_CANAL                            */
/*==============================================================*/
create table MTXTB013_PRMTO_SRVCO_CANAL 
(
   NU_PARAMETRO         numeric(9)                     not null,
   NU_SERVICO_005       numeric(6)                     not null
      constraint CKC_NU_SERVICO_005_MTXTB013 check (NU_SERVICO_005 >= 1),
   NU_CANAL_005         numeric(3)                     not null
      constraint CKC_NU_CANAL_005_MTXTB013 check (NU_CANAL_005 >= 1),
   HH_LIMITE_INICIO     date                           null,
   HH_LIMITE_FIM        date                           null,
   DT_INCO_SLCTO_SRVCO  date                           null,
   DT_FIM_SLCTO_SRVCO   date                           null,
   VR_MNMO_SLCTO_SRVCO  numeric(10,2)                  null,
   VR_MXMO_SLCTO_SRVCO  numeric(10,2)                  null,
   DH_ATUALIZACAO       date                           not null,
   constraint PK_MTXTB013 primary key (NU_PARAMETRO)
);

comment on table MTXTB013_PRMTO_SRVCO_CANAL is 
'Mantem os parametros do serviço de canal.';

comment on column MTXTB013_PRMTO_SRVCO_CANAL.NU_PARAMETRO is 
'Identificador numérico do parametro do sistema SI';

comment on column MTXTB013_PRMTO_SRVCO_CANAL.NU_SERVICO_005 is 
'Identificador numérico do serviço executado.';

comment on column MTXTB013_PRMTO_SRVCO_CANAL.NU_CANAL_005 is 
'Identificador numérico do Canal.';

comment on column MTXTB013_PRMTO_SRVCO_CANAL.HH_LIMITE_INICIO is 
'Hora e minuto inicial para solicitação do serviço. Se preenchido a Hora de Limite de Inicio deve ser fornecido.';

comment on column MTXTB013_PRMTO_SRVCO_CANAL.HH_LIMITE_FIM is 
'Hora e minuto final para solicitação do serviço. Se preenchido a Hora de Limite Final deve ser fornecido.';

comment on column MTXTB013_PRMTO_SRVCO_CANAL.DT_INCO_SLCTO_SRVCO is 
'Dia, mes e ano inicial para solicitação do serviço. Caso seja preenchido a Data de Limite de Inicio deve ser fornecido.';

comment on column MTXTB013_PRMTO_SRVCO_CANAL.DT_FIM_SLCTO_SRVCO is 
'Dia, mes e ano final para solicitação do serviço. Caso seja preenchido a Data de Limite de Inicio deve ser fornecido.';

comment on column MTXTB013_PRMTO_SRVCO_CANAL.VR_MNMO_SLCTO_SRVCO is 
'Importancia monetaria do limite minimo para a solicitação do serviço.';

comment on column MTXTB013_PRMTO_SRVCO_CANAL.VR_MXMO_SLCTO_SRVCO is 
'Importancia monetaria do limite maximo para a solicitação do serviço.';

comment on column MTXTB013_PRMTO_SRVCO_CANAL.DH_ATUALIZACAO is 
'Dia, mes, ano, hora, minuto e segundos da última atualização.';


/*==============================================================*/
/* Table: MTXTB014_TRANSACAO                                    */
/*==============================================================*/
create table MTXTB014_TRANSACAO 
(
   NU_NSU_TRANSACAO          numeric(15)         not null,
   IC_SITUACAO               numeric(1)          not null
      constraint CKC_IC_SITUACAO_MTXTB014 check (IC_SITUACAO in (0,1,2,3,4,5)),
   CO_CANAL_ORIGEM           varchar(20)         not null,
   IC_ENVIO                  numeric(1)          not null,
   IC_RETORNO                numeric(1)          not null,
   TS_ATUALIZACAO            timestamp           not null,
   NU_NSU_TRANSACAO_ORIGEM   numeric(15)         not null,
   DT_REFERENCIA			 date                not null,
   DH_TRANSACAO_CANAL        date                not null,
   DT_CONTABIL				 date                null,
   DH_MULTICANAL             date                not null,
   constraint PK_MTXTB014 primary key (NU_NSU_TRANSACAO)
) partition by range(DT_REFERENCIA) 
interval(NUMTODSINTERVAL(1,'day'))
store in (users)
(
  partition p1 values less than (to_date('01/01/1900','dd/mm/yyyy'))
);

comment on table MTXTB014_TRANSACAO is 
'Mantem os registros das transações solicitadas pelo Canal. Deve manter as informações para envio ao coodenador corporativo, que manterá as informações para Batimento, BI, Relatórios, etc.';

comment on column MTXTB014_TRANSACAO.NU_NSU_TRANSACAO is 
'Identificador Sequencial Unico da transação do SI';

comment on column MTXTB014_TRANSACAO.IC_SITUACAO is 
'Indica a situação. 
Pode ser: 0=Em Andamento | 1=Finalizado | 2=Negado | 3=Pendente | 4=Cancelado | 5=Estornado';

comment on column MTXTB014_TRANSACAO.CO_CANAL_ORIGEM is 
'Identificador Sequencial Unico do Canal (enviado pelo Canal). Cada canal pode ter seu formato especifico de de NSU.';

comment on column MTXTB014_TRANSACAO.IC_ENVIO is 
'Indicador de envio das informações da Transação para o sistema SICCO.
Pode ser:0=Não Enviado | 1=Enviado';

comment on column MTXTB014_TRANSACAO.IC_RETORNO is 
'Indicador de retorno da confirmação de recebimento das informações da Transação pelo sistema SICCO. 
Pode ser:0=Não recebido | 1=Recebido';

comment on column MTXTB014_TRANSACAO.TS_ATUALIZACAO is 
'Dia, mês, ano, hora, minuto e segundos da última alteração.';

comment on column MTXTB014_TRANSACAO.NU_NSU_TRANSACAO_ORIGEM is 
'Identificador numérico sequencial unico da transação de origem. 
Ex.:
. NSU do Serviço de Pagamento (1a. transação) cadastrado na transação de confirmação ou cancelamento do serviço.
. Na transação de Serviço de Pagamento (1a. transação), essa coluna não terá valor (nulo).';

comment on column MTXTB014_TRANSACAO.DT_REFERENCIA is 
'Data de referência da transação informada pelo Canal de Atendimento. Usada para criação da partição.';

comment on column MTXTB014_TRANSACAO.DH_TRANSACAO_CANAL is 
'Data, hora, segundos do início da Transação no Canal de Atendimento.';

comment on column MTXTB014_TRANSACAO.DT_CONTABIL is 
'Data contábil da transação informada pelo Canal de Atendimento.';

comment on column MTXTB014_TRANSACAO.DH_MULTICANAL is 
'Data, hora, segundos do recebimento da transação no sistema.';


/*==============================================================*/
/* Table: MTXTB015_SRVCO_TRNSO_TARFA                        */
/*==============================================================*/
create table MTXTB015_SRVCO_TRNSO_TARFA 
(
   NU_NSU_TRANSACAO_017   numeric(15)           not null,
   NU_SERVICO_017         numeric(6)            not null
      constraint CKC_NU_SERVICO_017_MTXTB015 check (NU_SERVICO_017 >= 1),
   NU_VERSAO_SERVICO_017  numeric(3)            not null
      constraint CKC_NU_VERSAO_SERVICO_MTXTB015 check (NU_VERSAO_SERVICO_017 >= 1),
   NU_TAREFA_012          numeric(6)            not null
      constraint CKC_NU_TAREFA_012_MTXTB015 check (NU_TAREFA_012 >= 1),
   NU_VERSAO_TAREFA_012   numeric(3)            not null
      constraint CKC_NU_VERSAO_TAREFA__MTXTB015 check (NU_VERSAO_TAREFA_012 >= 1),
   DE_XML_REQUISICAO      CLOB                  null,
   DE_XML_RESPOSTA        CLOB                  null,
   TS_EXECUCAO_TRANSACAO  timestamp             null,
   NU_NSU_TRANSACAO_CRPRO numeric(15)           null,
   DT_REFERENCIA			  date					not null, 
   constraint PK_MTXTB015 primary key (NU_NSU_TRANSACAO_017, NU_SERVICO_017, NU_VERSAO_SERVICO_017, NU_TAREFA_012, NU_VERSAO_TAREFA_012)
) partition by range(DT_REFERENCIA) 
interval(NUMTODSINTERVAL(1,'day'))
store in (users)
(
  partition p1 values less than (to_date('01/01/1900','dd/mm/yyyy'))
);

comment on table MTXTB015_SRVCO_TRNSO_TARFA is 
'Mantem o registro das informações enviadas e recebidas do barramento (Fila MQ).';

comment on column MTXTB015_SRVCO_TRNSO_TARFA.NU_NSU_TRANSACAO_017 is 
'Identificador Sequencial Unico da transação do SI';

comment on column MTXTB015_SRVCO_TRNSO_TARFA.NU_SERVICO_017 is 
'Identificador numérico do serviço executado.';

comment on column MTXTB015_SRVCO_TRNSO_TARFA.NU_VERSAO_SERVICO_017 is 
'Identificador numérico da versão do serviço.';

comment on column MTXTB015_SRVCO_TRNSO_TARFA.NU_TAREFA_012 is 
'Identificador numérico da tarefa executada.';

comment on column MTXTB015_SRVCO_TRNSO_TARFA.NU_VERSAO_TAREFA_012 is 
'Identificador numérico da versão da tarefa vinculada ao serviço.';

comment on column MTXTB015_SRVCO_TRNSO_TARFA.DE_XML_REQUISICAO is 
'Bloco de texto descritor do conteudo XML enviado ao barramento.';

comment on column MTXTB015_SRVCO_TRNSO_TARFA.DE_XML_RESPOSTA is 
'Bloco de texto descritor do conteudo XML recebido do barramento.';

comment on column MTXTB015_SRVCO_TRNSO_TARFA.TS_EXECUCAO_TRANSACAO is 
'Data, hora, segundos, nanosegundos da última atualização da transação.';

comment on column MTXTB015_SRVCO_TRNSO_TARFA.DT_REFERENCIA is 
'Data da transação. Usada para criação da partição.';

comment on column MTXTB015_SRVCO_TRNSO_TARFA.NU_NSU_TRANSACAO_CRPRO is
'Identificador Sequencial Unico da transacão do host(corporativos).';


/*==============================================================*/
/* Table: MTXTB016_ITERACAO_CANAL                               */
/*==============================================================*/
create table MTXTB016_ITERACAO_CANAL 
(
   NU_ITERACAO_CANAL           numeric(15)        not null,
   NU_NSU_TRANSACAO_014        numeric(15)        not null,
   NU_CANAL_004                numeric(3)         not null
      constraint CKC_NU_CANAL_004_MTXTB016 check (NU_CANAL_004 >= 0),
   DE_RECEBIMENTO              CLOB               not null,
   TS_RECEBIMENTO_SOLICITACAO  timestamp          not null,
   DE_RETORNO                  CLOB               null,
   TS_RETORNO_SOLICITACAO      timestamp          null,
   DT_REFERENCIA			   date               not null,
   CO_TERMINAL                 varchar(15)        null,
   constraint PK_MTXTB016 primary key (NU_ITERACAO_CANAL, NU_NSU_TRANSACAO_014)
) partition by range(DT_REFERENCIA) 
interval(NUMTODSINTERVAL(1,'day'))
store in (users)
(
  partition p1 values less than (to_date('01/01/1900','dd/mm/yyyy'))
);

comment on table MTXTB016_ITERACAO_CANAL is 
'Mantem as informações sobre iteração (ciclo) com o canal, como dados de recebimento e envio ao canal.';

comment on column MTXTB016_ITERACAO_CANAL.NU_ITERACAO_CANAL is 
'Identificador numérico da iteração com o Canal.';

comment on column MTXTB016_ITERACAO_CANAL.NU_NSU_TRANSACAO_014 is 
'Identificador Sequencial Unico da transação do SI';

comment on column MTXTB016_ITERACAO_CANAL.NU_CANAL_004 is 
'Identificador num�rico do Canal.';

comment on column MTXTB016_ITERACAO_CANAL.DE_RECEBIMENTO is 
'Bloco de texto descritor do conteudo (XML | Json) recebido do Canal.';

comment on column MTXTB016_ITERACAO_CANAL.TS_RECEBIMENTO_SOLICITACAO is 
'Data, hora, segundos, nanosegundos do recebimento da informação do Canal solicitante.';

comment on column MTXTB016_ITERACAO_CANAL.DE_RETORNO is 
'Bloco de texto descritor do conteudo (XML | Json) retornado para o canal.';

comment on column MTXTB016_ITERACAO_CANAL.TS_RETORNO_SOLICITACAO is 
'Data, hora, segundos, nanosegundos do retorno da informação do Canal solicitante.';

comment on column MTXTB016_ITERACAO_CANAL.DT_REFERENCIA is 
'Data da transação. Usada para criação da partição.'; 

comment on column MTXTB016_ITERACAO_CANAL.CO_TERMINAl is 
'Código do terminal recebido pelo canal.'; 

/*==============================================================*/
/* Table: MTXTB017_VERSAO_SRVCO_TRNSO                           */
/*==============================================================*/
create table MTXTB017_VERSAO_SRVCO_TRNSO 
(
   NU_NSU_TRANSACAO_014 numeric(15)                    not null,
   NU_SERVICO_011       numeric(6)                     not null
      constraint CKC_NU_SERVICO_011_MTXTB017 check (NU_SERVICO_011 >= 1),
   NU_VERSAO_SERVICO_011 numeric(3)                     not null
      constraint CKC_NU_VERSAO_SERVICO_MTXTB017 check (NU_VERSAO_SERVICO_011 >= 1),
   TS_SOLICITACAO       timestamp                      not null,
   DT_REFERENCIA			date						   not null,
   constraint PK_MTXTB017 primary key (NU_SERVICO_011, NU_VERSAO_SERVICO_011, NU_NSU_TRANSACAO_014)
) partition by range(DT_REFERENCIA) 
interval(NUMTODSINTERVAL(1,'day'))
store in (users)
(
  partition p1 values less than (to_date('01/01/1900','dd/mm/yyyy'))
);

comment on table MTXTB017_VERSAO_SRVCO_TRNSO is 
'serviço solicitado para a transação.';

comment on column MTXTB017_VERSAO_SRVCO_TRNSO.NU_NSU_TRANSACAO_014 is 
'Identificador Sequencial Unico da transação do SI';

comment on column MTXTB017_VERSAO_SRVCO_TRNSO.NU_SERVICO_011 is 
'Identificador numérico do serviço executado.';

comment on column MTXTB017_VERSAO_SRVCO_TRNSO.NU_VERSAO_SERVICO_011 is 
'Identificador numérico da versão do serviço.';

comment on column MTXTB017_VERSAO_SRVCO_TRNSO.TS_SOLICITACAO is 
'Data, hora, segundos, nanosegundos da solicitação do serviço.';

comment on column MTXTB017_VERSAO_SRVCO_TRNSO.DT_REFERENCIA is 
'Data da transação. Usada para criação da partição.'; 

/*==============================================================*/
/* Table: MTXTB018_VRSO_MEIO_ENTRA_SRVCO                        */
/*==============================================================*/
create table MTXTB018_VRSO_MEIO_ENTRA_SRVCO 
(
   NU_MEIO_ENTRADA_008         numeric(2)                     not null,
   NU_SERVICO_011              numeric(6)                     not null
      constraint CKC_NU_SERVICO_011_MTXTB018 check (NU_SERVICO_011 >= 1),
   NU_VERSAO_SERVICO_011       numeric(3)                     not null
      constraint CKC_NU_VERSAO_SERVICO_MTXTB018 check (NU_VERSAO_SERVICO_011 >= 1),
   IC_SITUACAO                 numeric(1)                     not null
      constraint CKC_IC_SITUACAO_MTXTB018 check (IC_SITUACAO between 0 and 1 and IC_SITUACAO in (0,1)),
   DH_ATUALIZACAO              date                           not null,
   constraint PK_MTXTB018 primary key (NU_MEIO_ENTRADA_008, NU_SERVICO_011, NU_VERSAO_SERVICO_011)
);

comment on table MTXTB018_VRSO_MEIO_ENTRA_SRVCO is 
'Meios de entrada disponiveis para o serviço.';

comment on column MTXTB018_VRSO_MEIO_ENTRA_SRVCO.NU_MEIO_ENTRADA_008 is 
'Identificador numérico do Meio de Entrada.';

comment on column MTXTB018_VRSO_MEIO_ENTRA_SRVCO.NU_SERVICO_011 is 
'Identificador numérico do serviço executado.';

comment on column MTXTB018_VRSO_MEIO_ENTRA_SRVCO.NU_VERSAO_SERVICO_011 is 
'Identificador numérico da versão do serviço.';

comment on column MTXTB018_VRSO_MEIO_ENTRA_SRVCO.IC_SITUACAO is 
'Indica a situação. Pode ser:0=Inativo | 1=Ativo';

comment on column MTXTB018_VRSO_MEIO_ENTRA_SRVCO.DH_ATUALIZACAO is 
'Dia, mes, ano, hora, minuto e segundos da última atualização.';


/*==============================================================*/
/* Table: MTXTB020_SRVCO_TARFA_CANAL                            */
/*==============================================================*/
create table MTXTB020_SRVCO_TARFA_CANAL 
(
   NU_SERVICO_003       numeric(6)                     not null
      constraint CKC_NU_SERVICO_003_MTXTB020 check (NU_SERVICO_003 >= 1),
   NU_VERSAO_SERVICO_003 numeric(3)                     not null
      constraint CKC_NU_VERSAO_SERVICO_MTXTB020 check (NU_VERSAO_SERVICO_003 >= 1),
   NU_TAREFA_003        numeric(6)                     not null
      constraint CKC_NU_TAREFA_003_MTXTB020 check (NU_TAREFA_003 >= 1),
   NU_VERSAO_TAREFA_003 numeric(3)                     not null
      constraint CKC_NU_VERSAO_TAREFA__MTXTB020 check (NU_VERSAO_TAREFA_003 >= 1),
   NU_CANAL_004         numeric(3)                     not null
      constraint CKC_NU_CANAL_004_MTXTB020 check (NU_CANAL_004 >= 1),
   constraint PK_MTXTB020 primary key (NU_SERVICO_003, NU_VERSAO_SERVICO_003, NU_TAREFA_003, NU_VERSAO_TAREFA_003, NU_CANAL_004)
);

comment on table MTXTB020_SRVCO_TARFA_CANAL is 
'Associação entre as entidades serviço Tarefa e Canal.';

comment on column MTXTB020_SRVCO_TARFA_CANAL.NU_SERVICO_003 is 
'Identificador numérico do serviço executado.';

comment on column MTXTB020_SRVCO_TARFA_CANAL.NU_VERSAO_SERVICO_003 is 
'Identificador numérico da versão do serviço.';

comment on column MTXTB020_SRVCO_TARFA_CANAL.NU_TAREFA_003 is 
'Identificador numérico da tarefa executada.';

comment on column MTXTB020_SRVCO_TARFA_CANAL.NU_VERSAO_TAREFA_003 is 
'Identificador numérico da versão da tarefa vinculada ao serviço.';

comment on column MTXTB020_SRVCO_TARFA_CANAL.NU_CANAL_004 is 
'Identificador numérico do Canal.';


/*==============================================================*/
/* Table: MTXTB023_PARAMETRO                                    */
/*==============================================================*/
create table MTXTB023_PARAMETRO_SISTEMA
(
   NU_PARAMETRO_SISTEMA          numeric(5)       not null,
   DE_PARAMETRO_SISTEMA          varchar(60)      not null,
   IC_PARAMETRO_SISTEMA	         numeric(1)       not null,
   CONSTRAINT PK_MTXTB023 PRIMARY KEY (NU_PARAMETRO_SISTEMA)
);

comment on column MTXTB023_PARAMETRO_SISTEMA.NU_PARAMETRO_SISTEMA is 
'Indentificador numérico do parametro.';

comment on column MTXTB023_PARAMETRO_SISTEMA.DE_PARAMETRO_SISTEMA is 
'Descrição do parametro.';

comment on column MTXTB023_PARAMETRO_SISTEMA.IC_PARAMETRO_SISTEMA is 
'Indicador de Ativação do sistema. Pod ser: 0=Inativo | 1=Ativo';


/*==============================================================*/
/* Table: MTXTB024_TAREFA_FILA                                  */
/*==============================================================*/
create table MTX.MTXTB024_TAREFA_FILA
(
   NU_TAREFA_012             NUMBER(6)       not null
     constraint CKC_NU_TAREFA_012_MTXTB024 check (NU_TAREFA_012 >= 1),
   NU_VERSAO_TAREFA_012      NUMBER(3)       not null
     constraint CKC_NU_VERSAO_TAREFA__MTXTB024 check (NU_VERSAO_TAREFA_012 >= 1),
   SG_SISTEMA_CORPORATIVO    VARCHAR2(5)     not null,
   NO_FILA_REQUISICAO        VARCHAR2(50)    not null,
   QT_SGNDO_LMTE_REQUISICAO  NUMBER(4)       not null,
   NO_FILA_RESPOSTA          VARCHAR2(50)    null,
   QT_SGNDO_LMTE_RESPOSTA    NUMBER(4)       null,
   NO_CONEXAO                VARCHAR2(50)    not null,
   NO_MODO_INTEGRACAO		 VARCHAR2(50)    not null,
   NO_RECURSO		         VARCHAR2(50)    not null,
   QT_TEMPO_ESPERA           NUMBER(3)       not null, 
     constraint PK_MTXTB024 primary key (NU_TAREFA_012, NU_VERSAO_TAREFA_012, SG_SISTEMA_CORPORATIVO)
);

comment on table MTX.MTXTB024_TAREFA_FILA is
'Identificação das filas para envio do XML da tarefa. 
Cada fila corresponde a um sistema corporativo (CAIXA) que receberá a tarefa a ser consumida (processada)..';

comment on column MTX.MTXTB024_TAREFA_FILA.NU_TAREFA_012 is
'Identificador numérico da tarefa executada.';

comment on column MTX.MTXTB024_TAREFA_FILA.NU_VERSAO_TAREFA_012 is
'Identificador numérico da versão da tarefa vinculada ao serviço.';

comment on column MTX.MTXTB024_TAREFA_FILA.SG_SISTEMA_CORPORATIVO is
'Abreviatura do Sistema Corporativo (CAIXA) que receberá a tarefa (parte de um serviço) a ser consumida (processada). 
Exemplo: SIB24, SIFGS, SID00, SIPIS, etc...';

comment on column MTX.MTXTB024_TAREFA_FILA.NO_FILA_REQUISICAO is
'Denominação da propriedade da fila MQ de requisição da tarefa.';

comment on column MTX.MTXTB024_TAREFA_FILA.QT_SGNDO_LMTE_REQUISICAO is
'Quantidade de milissegundos correspondente ao tempo limite para o sistema executar a requisição (aplicada à Fila-MQ). 
Caso a requisição não tenha sido realizada dentro desse tempo, o sistema irá executar o timeout (interrupção da tarefa).';

comment on column MTX.MTXTB024_TAREFA_FILA.NO_FILA_RESPOSTA is
'Denominação da propriedade da fila MQ de resposta da tarefa.';

comment on column MTX.MTXTB024_TAREFA_FILA.QT_SGNDO_LMTE_RESPOSTA is
'Quantidade de milissegundos correspondente ao tempo limite para o sistema executar a resposta (aplicada à Fila-MQ). 
Caso a resposta não tenha sido realizada dentro desse tempo, o sistema irá executar o timeout (interrupção da tarefa).';

comment on column MTX.MTXTB024_TAREFA_FILA.NO_CONEXAO is
'Nome da conexão contendo as configurações de fila.';

comment on column MTX.MTXTB024_TAREFA_FILA.NO_MODO_INTEGRACAO is
'Informa o nome do modo de integração a ser utilizado pelo SIBAR para processamento da operação. 
Ex: MQ; WS; REST; TCP.';

comment on column MTX.MTXTB024_TAREFA_FILA.NO_RECURSO is
'Informa o nome do recurso a ser utilizado pelo SIBAR para processamento da operação. 
Pode informar o nome da fila MQ, a URL do Web Service, entre outras informações. 
Ex: SIB24.REQ.VALIDA_CARTAO; SIRAN.RSP.VALIDA_SENHA.';

comment on column MTX.MTXTB024_TAREFA_FILA.QT_TEMPO_ESPERA is
'Quantidade de segundos de tempo de espera (timeout) a ser aplicado para a operação.';


/*==============================================================*/
/* Table: MTXTB025_REGRA_PROCESSAMENTO                          */
/*==============================================================*/
create table MTX.MTXTB025_REGRA_PROCESSAMENTO 
(
   NU_REGRA 				NUMBER(6)            not null,
   NO_CAMPO_DEPENDENCIA 	VARCHAR2(50)         not null,
   NO_SERVICO_ORIGEM 		VARCHAR2(50)         not null,
   NO_OPERACAO_ORIGEM 		VARCHAR2(50)         not null,
   DE_CAMINHO_INFORMACAO 	VARCHAR2(100)        not null, 
   constraint PK_MTXTB025 primary key (NU_REGRA)
);

comment on column MTX.MTXTB025_REGRA_PROCESSAMENTO.NU_REGRA is
'Identificador numérico da regra para processamento de transações no SIBAR.';

comment on column MTX.MTXTB025_REGRA_PROCESSAMENTO.NO_CAMPO_DEPENDENCIA is
'Nome do campo da operação que possui dependência de informação presente em campo de outra operação. 
Ex: CPF_CLIENTE; NUMERO_CONTA.';

comment on column MTX.MTXTB025_REGRA_PROCESSAMENTO.NO_SERVICO_ORIGEM is
'Nome do serviço de origem do SIBAR onde a informação será obtida para preenchimento do campo da operação que possui dependência. 
Ex: VALIDA_CARTAO; VALIDA_SENHA.';

comment on column MTX.MTXTB025_REGRA_PROCESSAMENTO.NO_OPERACAO_ORIGEM is
'Nome da operação de origem do SIBAR onde a informação será obtida para preenchimento do campo da operação que possui dependência. 
Ex: CARTAO_CIDADAO; SENHA_CIDADAO.';

comment on column MTX.MTXTB025_REGRA_PROCESSAMENTO.DE_CAMINHO_INFORMACAO is
'Descrição do caminho onde se encontra a informação na operação de origem do SIBAR. 
Ex: SAIDA/CPF_CNPJ_CONTA/CPF';

comment on table MTX.MTXTB025_REGRA_PROCESSAMENTO is
'Responsável por armazenar as regras para processamento das transações no SIBAR.';


/*==============================================================*/
/* Table: MTXTBX26_SERVICO_TAREFA_REGRA                         */
/*==============================================================*/
create table MTX.MTXTBX26_SERVICO_TAREFA_REGRA 
(
   NU_REGRA_025				NUMBER(6)            not null,
   NU_SERVICO_003		 	NUMBER(6)	         not null,
   NU_VERSAO_SERVICO_003 	NUMBER(6)	         not null,
   NU_TAREFA_003		 	NUMBER(6)	         not null,
   NU_VERSAO_TAREFA_003	 	NUMBER(6)	         not null,
   constraint PK_MTXTB026 primary key (NU_REGRA_025, NU_SERVICO_003, NU_VERSAO_SERVICO_003, NU_TAREFA_003, NU_VERSAO_TAREFA_003)
);

comment on column MTX.MTXTBX26_SERVICO_TAREFA_REGRA.NU_REGRA_025 is
'Identificador numérico da regra para processamento de transações no SIBAR.';

comment on column MTX.MTXTBX26_SERVICO_TAREFA_REGRA.NU_SERVICO_003 is
'Identificador numérico do serviço.';

comment on column MTX.MTXTBX26_SERVICO_TAREFA_REGRA.NU_VERSAO_SERVICO_003 is
'Identificador numérico da versão do serviço.';

comment on column MTX.MTXTBX26_SERVICO_TAREFA_REGRA.NU_TAREFA_003 is
'Identificador numérico da tarefa.';

comment on column MTX.MTXTBX26_SERVICO_TAREFA_REGRA.NU_VERSAO_TAREFA_003 is
'Identificador numérico da versão da tarefa.';

comment on table MTX.MTXTBX26_SERVICO_TAREFA_REGRA is
'Responsável por armazenar as regras para processamento das transações no SIBAR.';



/*==============================================================*/
alter table MTXTB003_SERVICO_TAREFA 
   add constraint FK_MTXTB003_MTXTB011 
   foreign key (NU_SERVICO_011, NU_VERSAO_SERVICO_011) references MTXTB011_VERSAO_SERVICO (NU_SERVICO_001, NU_VERSAO_SERVICO);

alter table MTXTB003_SERVICO_TAREFA
   add constraint FK_MTXTB003_MTXTB012 foreign key (NU_TAREFA_012, NU_VERSAO_TAREFA_012)
      references MTXTB012_VERSAO_TAREFA (NU_TAREFA_002, NU_VERSAO_TAREFA);

alter table MTXTB005_SERVICO_CANAL
   add constraint FK_MTXTB005_MTXTB001 foreign key (NU_SERVICO_001)
      references MTXTB001_SERVICO (NU_SERVICO);

alter table MTXTB005_SERVICO_CANAL
   add constraint FK_MTXTB005_MTXTB004 foreign key (NU_CANAL_004)
      references MTXTB004_CANAL (NU_CANAL);

alter table MTXTB007_TAREFA_MENSAGEM
   add constraint FK_MTXTB007_MTXTB002 foreign key (NU_TAREFA_002)
      references MTXTB002_TAREFA (NU_TAREFA);

alter table MTXTB007_TAREFA_MENSAGEM
   add constraint FK_MTXTB007_MTXTB006 foreign key (NU_MENSAGEM_006)
      references MTXTB006_MENSAGEM (NU_NSU_MENSAGEM);

alter table MTXTB010_VRSO_TARFA_MEIO_ENTRA
   add constraint FK_MTXTB010_MTXTB008 foreign key (NU_MEIO_ENTRADA_008)
      references MTXTB008_MEIO_ENTRADA (NU_MEIO_ENTRADA);

alter table MTXTB010_VRSO_TARFA_MEIO_ENTRA
   add constraint FK_MTXTB010_MTXTB012 foreign key (NU_TAREFA_012, NU_VERSAO_TAREFA_012)
      references MTXTB012_VERSAO_TAREFA (NU_TAREFA_002, NU_VERSAO_TAREFA);

alter table MTXTB011_VERSAO_SERVICO
   add constraint FK_MTXTB011_MTXTB001 foreign key (NU_SERVICO_001)
      references MTXTB001_SERVICO (NU_SERVICO);

alter table MTXTB012_VERSAO_TAREFA
   add constraint FK_MTXTB012_MTXTB002 foreign key (NU_TAREFA_002)
      references MTXTB002_TAREFA (NU_TAREFA);

alter table MTXTB013_PRMTO_SRVCO_CANAL
   add constraint FK_MTXTB013_MTXTB005 foreign key (NU_SERVICO_005, NU_CANAL_005)
      references MTXTB005_SERVICO_CANAL (NU_SERVICO_001, NU_CANAL_004);

alter table MTXTB015_SRVCO_TRNSO_TARFA
   add constraint FK_MTXTB015_MTXTB012 foreign key (NU_TAREFA_012, NU_VERSAO_TAREFA_012)
      references MTXTB012_VERSAO_TAREFA (NU_TAREFA_002, NU_VERSAO_TAREFA);

alter table MTXTB015_SRVCO_TRNSO_TARFA
   add constraint FK_MTXTB015_MTXTB017 foreign key (NU_SERVICO_017, NU_VERSAO_SERVICO_017, NU_NSU_TRANSACAO_017)
      references MTXTB017_VERSAO_SRVCO_TRNSO (NU_SERVICO_011, NU_VERSAO_SERVICO_011, NU_NSU_TRANSACAO_014);

alter table MTXTB016_ITERACAO_CANAL
   add constraint FK_MTXTB016_MTXTB004 foreign key (NU_CANAL_004)
      references MTXTB004_CANAL (NU_CANAL);

alter table MTXTB016_ITERACAO_CANAL
   add constraint FK_MTXTB016_MTXTB014 foreign key (NU_NSU_TRANSACAO_014)
      references MTXTB014_TRANSACAO (NU_NSU_TRANSACAO);
      
alter table MTXTB017_VERSAO_SRVCO_TRNSO
   add constraint FK_MTXTB017_MTXTB011 foreign key (NU_SERVICO_011, NU_VERSAO_SERVICO_011)
      references MTXTB011_VERSAO_SERVICO (NU_SERVICO_001, NU_VERSAO_SERVICO);

alter table MTXTB017_VERSAO_SRVCO_TRNSO
   add constraint FK_MTXTB017_MTXTB014 foreign key (NU_NSU_TRANSACAO_014)
      references MTXTB014_TRANSACAO (NU_NSU_TRANSACAO);

alter table MTXTB018_VRSO_MEIO_ENTRA_SRVCO
   add constraint FK_MTXTB018_MTXTB008 foreign key (NU_MEIO_ENTRADA_008)
      references MTXTB008_MEIO_ENTRADA (NU_MEIO_ENTRADA);

alter table MTXTB018_VRSO_MEIO_ENTRA_SRVCO
   add constraint FK_MTXTB018_MTXTB011 foreign key (NU_SERVICO_011, NU_VERSAO_SERVICO_011)
      references MTXTB011_VERSAO_SERVICO (NU_SERVICO_001, NU_VERSAO_SERVICO);

alter table MTXTB020_SRVCO_TARFA_CANAL
   add constraint FK_MTXTB020_MTXTB003 foreign key (NU_SERVICO_003, NU_VERSAO_SERVICO_003, NU_TAREFA_003, NU_VERSAO_TAREFA_003)
      references MTXTB003_SERVICO_TAREFA (NU_SERVICO_011, NU_VERSAO_SERVICO_011, NU_TAREFA_012, NU_VERSAO_TAREFA_012);

alter table MTXTB020_SRVCO_TARFA_CANAL
   add constraint FK_MTXTB020_MTXTB004 foreign key (NU_CANAL_004)
      references MTXTB004_CANAL (NU_CANAL);
      
alter table MTX.MTXTB024_TAREFA_FILA
   add constraint FK_MTXTB024_MTXTB012 foreign key (NU_TAREFA_012, NU_VERSAO_TAREFA_012)
      references MTX.MTXTB012_VERSAO_TAREFA (NU_TAREFA_002, NU_VERSAO_TAREFA);
      
alter table MTX.MTXTBX26_SERVICO_TAREFA_REGRA
   add constraint FK_MTXTB026_MTXTB025 foreign key (NU_REGRA_025)
      references MTX.MTXTB025_REGRA_PROCESSAMENTO (NU_REGRA);

alter table MTX.MTXTBX26_SERVICO_TAREFA_REGRA
   add constraint FK_MTXTB026_MTXTB003 foreign key (NU_SERVICO_003, NU_VERSAO_SERVICO_003, NU_TAREFA_003, NU_VERSAO_TAREFA_003)
      references MTX.MTXTB003_SERVICO_TAREFA (NU_SERVICO_011, NU_VERSAO_SERVICO_011, NU_TAREFA_012, NU_VERSAO_TAREFA_012);
