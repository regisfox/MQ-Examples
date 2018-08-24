UPDATE MTX.MTXTB006_MENSAGEM SET DE_MENSAGEM_NEGOCIAL = 'Valor inferior ao valor minimo permitido.', DE_MENSAGEM_TECNICA = 'Nao atende as premissas da transacao.' WHERE NU_NSU_MENSAGEM = 21;
UPDATE MTX.MTXTB006_MENSAGEM SET DE_MENSAGEM_NEGOCIAL = 'Valor superior ao valor maximo permitido.', DE_MENSAGEM_TECNICA = 'Nao atende as premissas da transacao.' WHERE NU_NSU_MENSAGEM = 22;

commit;
