/*==============================================================*/
/* Table: MTXTB032_MARCA_CONTA                                  */
/*==============================================================*/
create table MTX.MTXTB032_MARCA_CONTA 
(
   NU_MARCA_CONTA			NUMBER(3)		not null,
   CO_MARCA_CONTA			VARCHAR2(5)		not null,
   DE_MARCA_CONTA			VARCHAR2(100)	null,
   constraint PK_MTXTB032 primary key (NU_MARCA_CONTA)
);

comment on column MTX.MTXTB032_MARCA_CONTA.NU_MARCA_CONTA is
'Identificador numérico da marca da conta.';

comment on column MTX.MTXTB032_MARCA_CONTA.CO_MARCA_CONTA is
'Código da marca da conta. Pode ser: BI, BJ, BQ, 0006, 0064, 02/02, 02/03';

comment on column MTX.MTXTB032_MARCA_CONTA.DE_MARCA_CONTA is
'Bloco descritor da marca da conta.';

comment on table MTX.MTXTB032_MARCA_CONTA is
'Responsável por armazenar as marcas de contas existentes nos sistemas corporativos.';


/*==============================================================*/
/* Table: MTXTB033_SERVICO_MARCA_CONTA                          */
/*==============================================================*/
create table MTX.MTXTB033_SERVICO_MARCA_CONTA 
(
   NU_MARCA_CONTA_032		NUMBER(3)		not null,
   NU_SERVICO_001			NUMBER(6)		not null,
   constraint PK_MTXTB033 primary key (NU_MARCA_CONTA_032, NU_SERVICO_001)
);

comment on column MTX.MTXTB033_SERVICO_MARCA_CONTA.NU_MARCA_CONTA_032 is
'Identificador numérico da marca da conta.';

comment on column MTX.MTXTB033_SERVICO_MARCA_CONTA.NU_SERVICO_001 is
'Identificador numérico do serviço.';

comment on table MTX.MTXTB033_SERVICO_MARCA_CONTA is
'Relação dos Serviços com as marcas de conta.';


/*==============================================================*/
/* Table: MTXTB023_PARAMETRO                                    */
/*==============================================================*/
create table MTXTB023_PARAMETRO_SISTEMA
(
   NU_PARAMETRO_SISTEMA          numeric(5)       not null,
   DH_ATUALIZACAO_PARAMETRO		 date			  not null,
   NO_PARAMETRO_SISTEMA			 varchar(200)	  not null,
   DE_CONTEUDO_PARAMETRO		 varchar(200)	  not null,

   CONSTRAINT PK_MTXTB023 PRIMARY KEY (NU_PARAMETRO_SISTEMA)
);

comment on column MTXTB023_PARAMETRO_SISTEMA.NU_PARAMETRO_SISTEMA is 
'Codigo numero utilizado para identificar o parametro.';

comment on column MTXTB023_PARAMETRO_SISTEMA.DH_ATUALIZACAO_PARAMETRO is 
'Data, Hora, Minuto, Segundo da atualizacao do parametro do Sistema.';

comment on column MTXTB023_PARAMETRO_SISTEMA.NO_PARAMETRO_SISTEMA is 
'Nome do parametro do Sistema. Descreve o tipo de validacao realizada pelo parametro.';

comment on column MTXTB023_PARAMETRO_SISTEMA.DE_CONTEUDO_PARAMETRO is 
'Descricao do valor do parametro do Sistema.';


/*==============================================================*/
/* Table: MTXTB035_TRANSACAO_CONTA                              */
/*==============================================================*/
CREATE TABLE MTXTB035_TRANSACAO_CONTA
(								
	NU_NSU_TRANSACAO_016 	NUMBER(15)		not null,
	DT_REFERENCIA		 	date			not null,
	IC_CONTA_SOLUCAO		NUMBER(1)		not null,							
	NU_UNIDADE				NUMBER(4)		not null,					
	NU_CONTA 				NUMBER(12)		not null,
	NU_PRODUTO				NUMBER(4)		not null,
	NU_DV_CONTA				NUMBER(1)		not null,					
	NU_CANAL_016			NUMBER(3)		not null,						
	NU_SERVICO_017			NUMBER(6)		not null,						
	NU_VERSAO_SERVICO_017	NUMBER(3)		not null,								
	IC_SITUACAO_TRNSO_CONTA	NUMBER(1)		not null,

	CONSTRAINT PK_MTXTB035 PRIMARY KEY (NU_NSU_TRANSACAO_014)
);
      
comment on column MTXTB035_TRANSACAO_CONTA.NU_NSU_TRANSACAO_016 is 
'Numero sequencial unico da transacao.';

comment on column MTXTB035_TRANSACAO_CONTA.DT_REFERENCIA is 
'Data de referencia da transacao informada pelo Canal de Atendimento. Usada para criacao da particao.';

comment on column MTXTB035_TRANSACAO_CONTA.IC_CONTA_SOLUCAO is 
'Indicador da situacao de conta SIDEC ou NSGD
Pode ser: 1 - SIDEC; 1 - NSGD."';

comment on column MTXTB035_TRANSACAO_CONTA.NU_UNIDADE is 
'Numero da unidade da transacao que ocorreu o debito.';

comment on column MTXTB035_TRANSACAO_CONTA.NU_CONTA is 
'Numero da conta que ocorreu o debito da transacao.';

comment on column MTXTB035_TRANSACAO_CONTA.NU_PRODUTO is 
'Produto da Conta que ocorreu o debito da transacao';

comment on column MTXTB035_TRANSACAO_CONTA.NU_DV_CONTA is 
'Numero do digito da conta que ocorreu o debito da transacao.';

comment on column MTXTB035_TRANSACAO_CONTA.NU_CANAL_016 is 
'Identificador numerico do Canal.';

comment on column MTXTB035_TRANSACAO_CONTA.NU_SERVICO_017 is 
'Identificador numerico do servico executado.';

comment on column MTXTB035_TRANSACAO_CONTA.NU_VERSAO_SERVICO_017 is 
'Identificador numerico da versao do servico.';

comment on column MTXTB035_TRANSACAO_CONTA.IC_SITUACAO_TRNSO_CONTA is 
'"Esta situacao indicara as transacoes passiveis de cancelamento foram realizadas com sucesso e nao foram canceladas (finalizada)
Ao realizar o cancelamento a situacao devera ser atualizada para cancelada
Valores possiveis: 1. Finalizada 2. Cancelada"';
      

/*==============================================================*/
/* Table: MTXTB034_TRANSACAO_AGENDAMENTO                        */
/*==============================================================*/
create table MTX.MTXTB034_TRANSACAO_AGENDAMENTO 
(
   NU_NSU_TRANSACAO_AGENDAMENTO		NUMBER(15)		not null,
   DT_REFERENCIA					DATE			not null,
   DT_EFETIVACAO					DATE			not null,
   DE_IDENTIFICACAO_TRANSACAO		VARCHAR2(25)	null,
   IC_SITUACAO_AGENDAMENTO			NUMBER(1)		not null,
   NU_SERVICO						NUMBER(6)		not null,
   NU_VERSAO_SERVICO				NUMBER(3)		not null,
   NU_CANAL							NUMBER(3)		not null,
   VR_TRANSACAO						NUMBER(14,2)	not null,
   NU_UNIDADE						NUMBER(4)		not null,
   NU_PRODUTO						NUMBER(4)		not null,
   NU_CONTA							NUMBER(12)		not null,
   NU_DV_CONTA						NUMBER(1)		not null,
   IC_CONTA_SOLUCAO					NUMBER(1)		not null,
   DE_XML_AGENDAMENTO				CLOB			not null,
   constraint PK_MTXTB034 primary key (NU_NSU_TRANSACAO_AGENDAMENTO)
);

comment on column MTX.MTXTB034_TRANSACAO_AGENDAMENTO.NU_NSU_TRANSACAO_AGENDAMENTO is
'Número sequencia único da transação.';

comment on column MTX.MTXTB034_TRANSACAO_AGENDAMENTO.DT_REFERENCIA is
'Dia, mês e ano de referência da transação, informada pelo Canal de Atendimento.';

comment on column MTX.MTXTB034_TRANSACAO_AGENDAMENTO.DT_EFETIVACAO is
'Dia, mês e ano que ocorrerá a efetivação do pagamento';

comment on column MTX.MTXTB034_TRANSACAO_AGENDAMENTO.DE_IDENT_TRANSACAO is
'Descrição da identificação realizada pelo cliente no canal. Por exemplo: mensalidade escolar';

comment on column MTX.MTXTB034_TRANSACAO_AGENDAMENTO.IC_SITUACAO_AGENDAMENTO is
'Indica a situação do agendamento. Pode ser: 0-Cancelado | 1-Agendado | 2-Efetivado';

comment on column MTX.MTXTB034_TRANSACAO_AGENDAMENTO.NU_SERVICO is
'Identificador numérico do serviço executado.';

comment on column MTX.MTXTB034_TRANSACAO_AGENDAMENTO.NU_VERSAO_SERVICO is
'Identificador numérico da versão do serviço.';

comment on column MTX.MTXTB034_TRANSACAO_AGENDAMENTO.NU_CANAL is
'Identificador numérico do Canal.';

comment on column MTX.MTXTB034_TRANSACAO_AGENDAMENTO.VR_TRANSACAO is
'Importância monetária da Transação.';

comment on column MTX.MTXTB034_TRANSACAO_AGENDAMENTO.NU_UNIDADE is
'Identificador da Unidade de vinculação. Agência da Conta de crédito vinculada a Conta e Produto.';

comment on column MTX.MTXTB034_TRANSACAO_AGENDAMENTO.NU_PRODUTO is
'Identificador do Componente da Conta NSGD / Produto. Identificação da Operação que identifica o Produto da Conta';

comment on column MTX.MTXTB034_TRANSACAO_AGENDAMENTO.NU_CONTA is
'Identificador do número da conta para movimentação bancária.';

comment on column MTX.MTXTB034_TRANSACAO_AGENDAMENTO.NU_DV_CONTA is
'Identificador do Dígito Verificador do número da conta para movimentação bancária.';

comment on column MTX.MTXTB034_TRANSACAO_AGENDAMENTO.IC_CONTA_SOLUCAO is
'Indica se a conta é do tipo SIDEC ou NSGD. 
Valores Válidos:
1 = SIDEC (Sistema Depósito e Contabilidade)
2 = NSGD (Nova Solução de Gestão de Depósito) ';

comment on column MTX.MTXTB034_TRANSACAO_AGENDAMENTO.DE_XML_AGENDAMENTO is
'Bloco de texto descritor do conteúdo XML recebido do canal.';

comment on table MTX.MTXTB034_TRANSACAO_AGENDAMENTO is
'Responsável pelo armazenamento das informações de transações a serem executadas em data futura.';


/*==============================================================*/
/* Table: MTXTB0XX_PRMTO_PGTO_CNTNGNCIA                        */
/*==============================================================*/
create table MTX.MTXTB0XX_PRMTO_PGTO_CNTNGNCIA
(
   NU_PRMTO_PGTO_CNTNGNCIA		NUMBER(3)		not null,
   NU_CANAL_004 				NUMBER(3) 		not null,
   IC_TIPO_BOLETO				NUMBER(1)		not null,
   VR_VALOR_MINIMO				NUMBER(14,2)	not null,
   VR_VALOR_MAXIMO				NUMBER(14,2)	not null,
   IC_RECEBIMENTO_CONTINGENCIA	NUMBER(1)		not null,
   IC_TIPO_CONTINGENCIA			NUMBER(1)		not null,
   DH_ATUALIZACAO            	DATE			not null,
   constraint PK_MTXTB0XX primary key (NU_PRMTO_PGTO_CNTNGNCIA)
);

comment on column MTX.MTXTB0XX_PRMTO_PGTO_CNTNGNCIA.NU_PRMTO_PGTO_CNTNGNCIA is
'Número sequencial único do parametro.';

comment on column MTX.MTXTB0XX_PRMTO_PGTO_CNTNGNCIA.NU_CANAL_004 is
'Identificador numérico do Canal. Referência com a tabela MTXTB004_CANAL.';

comment on column MTX.MTXTB0XX_PRMTO_PGTO_CNTNGNCIA.IC_TIPO_BOLETO is
'Indicador do tipo de boleto. Informa se o boleto é banco CAIXA, Outros Bancos ou Boleted.
Pode ser: 1 - Banco Caixa | 2 - Outros Bancos | 3 - Boleted';

comment on column MTX.MTXTB0XX_PRMTO_PGTO_CNTNGNCIA.VR_VALOR_MINIMO is
'Valor mínimo permitido para pagamento do boleto em situação de contingência.';

comment on column MTX.MTXTB0XX_PRMTO_PGTO_CNTNGNCIA.VR_VALOR_MAXIMO is
'Valor máximo permitido para pagamento do boleto em situação de contingência.';

comment on column MTX.MTXTB0XX_PRMTO_PGTO_CNTNGNCIA.IC_RECEBIMENTO_CONTINGENCIA is
'Indicador de contingência. Informa se o boleto para aquele canal, para aquele banco pode ser pago quando está em contingência.
Pode ser: 1 - Sim, aceita pagamento em contingência | 2 - Não aceita pagamento em contingência';

comment on column MTX.MTXTB0XX_PRMTO_PGTO_CNTNGNCIA.IC_TIPO_CONTINGENCIA is
'Indicador do tipo de contingência. 
Pode ser: 1 - Contingência CIP | 2 - Contingência Caixa';

comment on column MTX.MTXTB0XX_PRMTO_PGTO_CNTNGNCIA.DH_ATUALIZACAO is
'Data e Hora da atualização do parametro.';

comment on table MTX.MTXTB0XX_PRMTO_PGTO_CNTNGNCIA is
'Responsável por armazenar os parametros de valores para cada canal, banco caixa e outros bancos, para o serviço de pagamento que o simtx executa. Cada canal para cada banco terá um valor mínimo e máximo permitido quando estiver em situação de contingência.
Exemplo: Aceita em Contingência, Canal SIMAA, Banco Caixa: Pode pagar entre R$100 e R$1.000,00.';
      

/*==============================================================*/
alter table MTX.MTXTB033_SERVICO_MARCA_CONTA
   add constraint FK_MTXTB033_MTXTB001 foreign key (NU_SERVICO_001)
      references MTX.MTXTB001_SERVICO (NU_SERVICO);
      
alter table MTX.MTXTB033_SERVICO_MARCA_CONTA
   add constraint FK_MTXTB033_MTXTB032 foreign key (NU_MARCA_CONTA_032)
      references MTX.MTXTB032_MARCA_CONTA (NU_MARCA_CONTA);

alter table MTX.MTXTB0XX_PRMTO_PGTO_CNTNGNCIA
   add constraint FK_MTXTB0XX_MTXTB004 foreign key (NU_CANAL_004)
      references MTX.MTXTB004_CANAL (NU_CANAL);
      
      
CREATE TABLE MTXTB037_GRUPO_ACESSO (
  NU_GRUPO_ACESSO NUMBER(5) 			NOT NULL, 
  NO_GRUPO_ACESSO VARCHAR2(50) 			NOT NULL , 
  CONSTRAINT MTXTB037_GRUPO_ACESSO_PK 	PRIMARY KEY 
  (NU_GRUPO_ACESSO)
  ENABLE);

CREATE TABLE MTXTB038_FUNCIONALIDADE_ACESSO (
  NU_FUNCIONALIDADE_ACESSO NUMBER(5) NOT NULL,
  NO_FUNCIONALIDADE_ACESSO VARCHAR2(50) NOT NULL,
  CONSTRAINT MTXTB038_FUNCIONALIDADE_AC_PK PRIMARY KEY 
  ( NU_FUNCIONALIDADE_ACESSO)
  ENABLE);

