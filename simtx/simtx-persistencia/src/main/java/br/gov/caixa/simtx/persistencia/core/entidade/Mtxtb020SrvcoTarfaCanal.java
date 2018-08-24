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
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;


@Entity
@Cacheable
@Table(name = "MTXTB020_SRVCO_TARFA_CANAL")
@NamedQueries({
    @NamedQuery(name = "Mtxtb020SrvcoTarfaCanal.findAll", query = "SELECT m FROM Mtxtb020SrvcoTarfaCanal m"),
    @NamedQuery(name = "Mtxtb020SrvcoTarfaCanal.buscarTarefasPorServicoCanal", query = "Select stc FROM Mtxtb020SrvcoTarfaCanal stc join stc.mtxtb003ServicoTarefa st join st.mtxtb012VersaoTarefa vt join vt.mtxtb002Tarefa t join stc.mtxtb004Canal c where stc.id.nuServico003 = :nuServico and stc.id.nuVersaoServico003 = :nuVersaoServico and c.nuCanal = :nuCanal")
    })
public class Mtxtb020SrvcoTarfaCanal implements Serializable {
    
	private static final long serialVersionUID = 1L;

    @EmbeddedId
    private Mtxtb020SrvcoTarfaCanalPK id;
    
    @ManyToOne
    @JoinColumns({
    	@JoinColumn(name = "NU_SERVICO_003", referencedColumnName = "NU_SERVICO_011", insertable = false, updatable = false),
    	@JoinColumn(name = "NU_VERSAO_SERVICO_003", referencedColumnName = "NU_VERSAO_SERVICO_011", insertable = false, updatable = false),
    	@JoinColumn(name = "NU_TAREFA_003", referencedColumnName = "NU_TAREFA_012", insertable = false, updatable = false),
    	@JoinColumn(name = "NU_VERSAO_TAREFA_003", referencedColumnName = "NU_VERSAO_TAREFA_012", insertable = false, updatable = false)
    })
    private Mtxtb003ServicoTarefa mtxtb003ServicoTarefa;
    
    @ManyToOne
    @JoinColumn(name = "NU_CANAL_004", referencedColumnName = "NU_CANAL", insertable = false, updatable = false)
    private Mtxtb004Canal mtxtb004Canal;


    public Mtxtb020SrvcoTarfaCanalPK getId() {
        return this.id;
    }

    public void setId(Mtxtb020SrvcoTarfaCanalPK id) {
        this.id = id;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Mtxtb020SrvcoTarfaCanal [");
        if (id != null) {
            builder.append("id=");
            builder.append(id);
        }
        builder.append("]");
        return builder.toString();
    }

	public Mtxtb003ServicoTarefa getMtxtb003ServicoTarefa() {
		return mtxtb003ServicoTarefa;
	}

	public void setMtxtb003ServicoTarefa(Mtxtb003ServicoTarefa mtxtb003ServicoTarefa) {
		this.mtxtb003ServicoTarefa = mtxtb003ServicoTarefa;
	}

	public Mtxtb004Canal getMtxtb004Canal() {
		return mtxtb004Canal;
	}

	public void setMtxtb004Canal(Mtxtb004Canal mtxtb004Canal) {
		this.mtxtb004Canal = mtxtb004Canal;
	}

}
