DELETE FROM MTX.MTXTB026_SERVICO_TAREFA_REGRA WHERE NU_SERVICO_003 = 110026 AND NU_VERSAO_SERVICO_003 = 1 AND NU_TAREFA_003 = 100029 AND NU_VERSAO_TAREFA_003 = 1;
DELETE FROM MTX.MTXTB026_SERVICO_TAREFA_REGRA WHERE NU_SERVICO_003 = 110027 AND NU_VERSAO_SERVICO_003 = 1 AND NU_TAREFA_003 = 100029 AND NU_VERSAO_TAREFA_003 = 1;

INSERT INTO MTX.MTXTB025_REGRA_PROCESSAMENTO (NU_REGRA,NO_CAMPO_DEPENDENCIA,NO_SERVICO_ORIGEM,NO_OPERACAO_ORIGEM,DE_CAMINHO_INFORMACAO) VALUES (64,'NUMERO_REFERENCIA_ATUAL','consulta_cobranca_bancaria','CONSULTA_BOLETO_PAGAMENTO','CONSULTA_BOLETO_PAGAMENTO.BAIXAS.BAIXA_OPERACIONAL.NUMERO_REFERENCIA_ATUAL');
INSERT INTO MTX.MTXTB025_REGRA_PROCESSAMENTO (NU_REGRA,NO_CAMPO_DEPENDENCIA,NO_SERVICO_ORIGEM,NO_OPERACAO_ORIGEM,DE_CAMINHO_INFORMACAO) VALUES (65,'NUMERO_REFERENCIA_CADASTRO','consulta_cobranca_bancaria','CONSULTA_BOLETO_PAGAMENTO','CONSULTA_BOLETO_PAGAMENTO.TITULO.REFERENCIA_ATUAL_CADASTRO');
INSERT INTO MTX.MTXTB025_REGRA_PROCESSAMENTO (NU_REGRA, NO_CAMPO_DEPENDENCIA, NO_SERVICO_ORIGEM, NO_OPERACAO_ORIGEM, DE_CAMINHO_INFORMACAO) VALUES (66, 'TIPO', 'valida_regras_boleto', 'VALIDA_REGRAS_BOLETO', 'TIPO_BAIXA');
INSERT INTO MTX.MTXTB025_REGRA_PROCESSAMENTO (NU_REGRA, NO_CAMPO_DEPENDENCIA, NO_SERVICO_ORIGEM, NO_OPERACAO_ORIGEM, DE_CAMINHO_INFORMACAO) VALUES ('69', 'CODIGO_BARRAS', 'consulta_cobranca_bancaria', 'CONSULTA_BOLETO_PAGAMENTO', 'CONSULTA_BOLETO_PAGAMENTO.TITULO.CODIGO_BARRAS')


commit;