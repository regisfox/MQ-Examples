UPDATE MTX.MTXTB012_VERSAO_TAREFA SET DE_XSLT_REQUISICAO = 'negocial/Lista_Transacoes_Pendentes_Assinatura/V1/Lista_Transacoes_Pendentes_Assinatura_Req.xsl', DE_XSLT_RESPOSTA = 'negocial/Lista_Transacoes_Pendentes_Assinatura/V1/Lista_Transacoes_Pendentes_Assinatura_Resp.xsl' WHERE NU_TAREFA_002 = 100045 AND NU_VERSAO_TAREFA = 1;


commit;
