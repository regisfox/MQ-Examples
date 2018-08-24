/*******************************************************************************
 * Copyright (C)  2017 - CAIXA Econômica Federal 
 * 
 * Todos os direitos reservados
 ******************************************************************************/
package br.gov.caixa.simtx.persistencia.core.entidade;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;


/**
 * The persistent class for the MTXTB002_TAREFA database table.
 * 
 */
@Entity
@Cacheable
@Table(name = "MTXTB002_TAREFA")
@NamedQuery(name = "Mtxtb002Tarefa.findAll", query = "SELECT m FROM Mtxtb002Tarefa m")
public class Mtxtb002Tarefa implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "NU_TAREFA", unique = true, nullable = false, precision = 6)
    private long nuTarefa;

    @Column(name = "NO_TAREFA", nullable = false, length = 50)
    private String noTarefa;
    
    @Column(name = "NO_SERVICO_BARRAMENTO", nullable = false, length = 50)
    private String noServicoBarramento;
    
    @Column(name = "NO_OPERACAO_BARRAMENTO", nullable = false, length = 50)
    private String noOperacaoBarramento;

    @Column(name = "DH_ATUALIZACAO", nullable = false)
    private Date dhAtualizacao;

    @Column(name = "IC_TIPO_TAREFA", nullable = false, precision = 1)
    private Integer icTipoTarefa;
    
    @Column(name = "IC_TAREFA_FINANCEIRA", nullable = false, precision = 1)
    private BigDecimal icFinanceira;

    
    public long getNuTarefa() {
        return this.nuTarefa;
    }

    public void setNuTarefa(long nuTarefa) {
        this.nuTarefa = nuTarefa;
    }

    public Date getDhAtualizacao() {
        return this.dhAtualizacao;
    }

    public void setDhAtualizacao(Date dhAtualizacao) {
        this.dhAtualizacao = dhAtualizacao;
    }

    public Integer getIcTipoTarefa() {
        return this.icTipoTarefa;
    }

    public void setIcTipoTarefa(Integer icTipoTarefa) {
        this.icTipoTarefa = icTipoTarefa;
    }

    public String getNoTarefa() {
        return this.noTarefa;
    }

    public void setNoTarefa(String noTarefa) {
        this.noTarefa = noTarefa;
    } 
    
    public String getNoServicoBarramento() {
		return noServicoBarramento;
	}

	public void setNoServicoBarramento(String noServicoBarramento) {
		this.noServicoBarramento = noServicoBarramento;
	}

	public String getNoOperacaoBarramento() {
		return noOperacaoBarramento;
	}

	public void setNoOperacaoBarramento(String noOperacaoBarramento) {
		this.noOperacaoBarramento = noOperacaoBarramento;
	}

	public BigDecimal getIcFinanceira() {
        return icFinanceira;
    }

    public void setIcFinanceira(BigDecimal icFinanceira) {
        this.icFinanceira = icFinanceira;
    }
    
    
    

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Mtxtb002Tarefa [nuTarefa=");
        builder.append(nuTarefa);
        builder.append(", ");
        if (dhAtualizacao != null) {
            builder.append("dhAtualizacao=");
            builder.append(dhAtualizacao);
            builder.append(", ");
        }
        if (icTipoTarefa != null) {
            builder.append("icTipoTarefa=");
            builder.append(icTipoTarefa);
            builder.append(", ");
        }
        if (noTarefa != null) {
            builder.append("noTarefa=");
            builder.append(noTarefa);
            builder.append(", ");
        }
        if (noServicoBarramento != null) {
            builder.append("noServicoBarramento=");
            builder.append(noServicoBarramento);
            builder.append(", ");
        }
        if (noOperacaoBarramento != null) {
            builder.append("noOperacaoBarramento=");
            builder.append(noOperacaoBarramento);
            builder.append(", ");
        }
        if (icFinanceira != null) {
            builder.append("icFinanceira=");
            builder.append(icFinanceira);
            builder.append(", ");
        }
        builder.append("]");
        return builder.toString();
    }
}
