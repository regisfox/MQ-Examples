/*******************************************************************************
 * Copyright (C)  2017 - CAIXA Econômica Federal 
 * 
 * Todos os direitos reservados
 ******************************************************************************/
package br.gov.caixa.simtx.persistencia.core.entidade;

import java.io.Serializable;

import javax.persistence.Cacheable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Cacheable
@Table(name = "MTXTB026_SERVICO_TAREFA_REGRA")
@NamedQueries({
        @NamedQuery(name = "Mtxtb026ServicoTarefaRegras.findAll",
                query = "SELECT s FROM Mtxtb026ServicoTarefaRegras s"),
        @NamedQuery(name = "Mtxtb026ServicoTarefaRegras.buscarPorServicoTarefa",
                query = "SELECT m " + "  FROM Mtxtb026ServicoTarefaRegras m " + "  join m.mtxtb025RegraProcessamento r "
                        + " WHERE m.id.nuServico003 = :nuServico "
                        + "   AND m.id.nuVersaoServico003 = :nuVersaoServico " + "   AND m.id.nuTarefa003 = :nuTarefa "
                        + "   AND m.id.nuVersaoTarefa003 = :nuVersaoTarefa") })
public class Mtxtb026ServicoTarefaRegras implements Serializable {

    private static final long serialVersionUID = 1L;

    @EmbeddedId
    private Mtxtb026ServicoTarefaRegrasPK id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(name = "NU_SERVICO_003", referencedColumnName = "NU_SERVICO_011", insertable = false,
                    updatable = false),
            @JoinColumn(name = "NU_VERSAO_SERVICO_003", referencedColumnName = "NU_VERSAO_SERVICO_011",
                    insertable = false, updatable = false),
            @JoinColumn(name = "NU_TAREFA_003", referencedColumnName = "NU_TAREFA_012", insertable = false,
                    updatable = false),
            @JoinColumn(name = "NU_VERSAO_TAREFA_003", referencedColumnName = "NU_VERSAO_TAREFA_012",
                    insertable = false, updatable = false) })
    private Mtxtb003ServicoTarefa mtxtb003ServicoTarefa;

    @ManyToOne
    @JoinColumn(name = "NU_REGRA_025", referencedColumnName = "NU_REGRA", insertable = false, updatable = false)
    private Mtxtb025RegraProcessamento mtxtb025RegraProcessamento;

    public Mtxtb026ServicoTarefaRegrasPK getId() {
        return id;
    }

    public void setId(Mtxtb026ServicoTarefaRegrasPK id) {
        this.id = id;
    }

    public Mtxtb003ServicoTarefa getMtxtb003ServicoTarefa() {
        return mtxtb003ServicoTarefa;
    }

    public void setMtxtb003ServicoTarefa(Mtxtb003ServicoTarefa mtxtb003ServicoTarefa) {
        this.mtxtb003ServicoTarefa = mtxtb003ServicoTarefa;
    }

    public Mtxtb025RegraProcessamento getMtxtb025RegraProcessamento() {
        return mtxtb025RegraProcessamento;
    }

    public void setMtxtb025RegraProcessamento(Mtxtb025RegraProcessamento mtxtb025RegraProcessamento) {
        this.mtxtb025RegraProcessamento = mtxtb025RegraProcessamento;
    }
}
