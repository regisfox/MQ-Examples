UPDATE MTX.MTXTB002_TAREFA SET NO_TAREFA = 'Desfaz Debito Transacional' WHERE NU_TAREFA = 100072;
UPDATE MTX.MTXTB002_TAREFA SET NO_TAREFA = 'Desfaz Limite Transacional' WHERE NU_TAREFA = 100079;

commit;