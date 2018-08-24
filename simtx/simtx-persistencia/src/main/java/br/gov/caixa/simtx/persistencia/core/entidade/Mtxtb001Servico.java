/*******************************************************************************
 * Copyright (C)  2017 - CAIXA Econômica Federal 
 * 
 * Todos os direitos reservados
 ******************************************************************************/
package br.gov.caixa.simtx.persistencia.core.entidade;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;


/**
 * The persistent class for the MTXTB001_SERVICO database table.
 * 
 */
@Entity
@Cacheable
@Table(name = "MTXTB001_SERVICO")
@NamedQueries({
	@NamedQuery(name = "Mtxtb001Servico.findAll", query = "SELECT m FROM Mtxtb001Servico m"),
	@NamedQuery(name = "Mtxtb001Servico.buscaPorCancelamento", query = "SELECT m FROM Mtxtb001Servico m WHERE m.icCancelamentoTransacao = :icCancelamento")
	
})
public class Mtxtb001Servico implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "NU_SERVICO", unique = true, nullable = false, precision = 6)
    private long nuServico;

    @Column(name = "DH_ATUALIZACAO", nullable = false)
    private Date dhAtualizacao;

    @Column(name = "IC_CONFIRMACAO_TRANSACAO", nullable = false, precision = 1)
    private BigDecimal icConfirmaTransacao;

    @Column(name = "IC_SITUACAO_SERVICO", nullable = false, precision = 1)
    private BigDecimal icSituacaoServico;

    @Column(name = "NO_SERVICO", length = 100)
    private String noServico;
    
    @Column(name = "IC_TIPO_SERVICO", nullable = false, precision = 1)
    private BigDecimal icTipoServico;
    
    @Column(name = "NO_SERVICO_BARRAMENTO", nullable = false)
    private String noServicoBarramento;
    
    @Column(name = "NO_OPERACAO_BARRAMENTO", nullable = false)
    private String noOperacaoBarramento;
    
    @Column(name = "QT_SGNDO_LMTE_RESPOSTA", nullable = false)
    private Integer tempoLimiteEsperaResposta;
    
    @Column(name = "NO_CONEXAO", nullable = false)
    private String noConexao;
    
    @Column(name = "NO_FILA_REQUISICAO", nullable = false)
    private String noFilaRequisicao;
    
    @Column(name = "NO_FILA_RESPOSTA", nullable = false)
    private String noFilaResposta;
    
    @Column(name = "IC_CANCELAMENTO_TRANSACAO", nullable = false)
    private Integer icCancelamentoTransacao;
    
    @Column(name = "IC_SERVICO_ORQUESTRADO", nullable = false)
    private Integer icServicoOrquestrado;

    @OneToMany(mappedBy = "mtxtb001Servico")
    private List<Mtxtb005ServicoCanal> mtxtb005ServicoCanals;

    @OneToMany(mappedBy = "mtxtb001Servico")
    private List<Mtxtb011VersaoServico> mtxtb011VersaoServicos;
    

    public Mtxtb001Servico(long nuServico) {
		super();
		this.nuServico = nuServico;
	}
    
    public Mtxtb001Servico(long nuServico, String noServico) {
  		super();
  		this.nuServico = nuServico;
  		this.noServico = noServico;
  	}

	public Mtxtb001Servico() {
		super();
	}

	public long getNuServico() {
        return this.nuServico;
    }

    public void setNuServico(long nuServico) {
        this.nuServico = nuServico;
    }

    public Date getDhAtualizacao() {
        return this.dhAtualizacao;
    }

    public void setDhAtualizacao(Date dhAtualizacao) {
        this.dhAtualizacao = dhAtualizacao;
    }

    public BigDecimal getIcConfirmaTransacao() {
        return this.icConfirmaTransacao;
    }

    public void setIcConfirmaTransacao(BigDecimal icConfirmaTransacao) {
        this.icConfirmaTransacao = icConfirmaTransacao;
    }

    public BigDecimal getIcSituacaoServico() {
        return this.icSituacaoServico;
    }

    public void setIcSituacaoServico(BigDecimal icSituacaoServico) {
        this.icSituacaoServico = icSituacaoServico;
    }

    public String getNoServico() {
        return this.noServico;
    }

    public void setNoServico(String noServico) {
        this.noServico = noServico;
    }

    public BigDecimal getIcTipoServico() {
        return icTipoServico;
    }

    public void setIcTipoServico(BigDecimal icTipoServico) {
        this.icTipoServico = icTipoServico;
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
	
	public List<Mtxtb005ServicoCanal> getMtxtb005ServicoCanals() {
        return this.mtxtb005ServicoCanals;
    }

    public void setMtxtb005ServicoCanals(List<Mtxtb005ServicoCanal> mtxtb005ServicoCanals) {
        this.mtxtb005ServicoCanals = mtxtb005ServicoCanals;
    }

    public Integer getIcServicoOrquestrado() {
		return icServicoOrquestrado;
	}

	public void setIcServicoOrquestrado(Integer icServicoOrquestrado) {
		this.icServicoOrquestrado = icServicoOrquestrado;
	}

	public Mtxtb005ServicoCanal addMtxtb005ServicoCanal(Mtxtb005ServicoCanal mtxtb005ServicoCanal) {
        getMtxtb005ServicoCanals().add(mtxtb005ServicoCanal);
        mtxtb005ServicoCanal.setMtxtb001Servico(this);

        return mtxtb005ServicoCanal;
    }

    public Mtxtb005ServicoCanal removeMtxtb005ServicoCanal(Mtxtb005ServicoCanal mtxtb005ServicoCanal) {
        getMtxtb005ServicoCanals().remove(mtxtb005ServicoCanal);
        mtxtb005ServicoCanal.setMtxtb001Servico(null);

        return mtxtb005ServicoCanal;
    }

    public List<Mtxtb011VersaoServico> getMtxtb011VersaoServicos() {
        return this.mtxtb011VersaoServicos;
    }

    public void setMtxtb011VersaoServicos(List<Mtxtb011VersaoServico> mtxtb011VersaoServicos) {
        this.mtxtb011VersaoServicos = mtxtb011VersaoServicos;
    }

    public Mtxtb011VersaoServico addMtxtb011VersaoServico(Mtxtb011VersaoServico mtxtb011VersaoServico) {
        getMtxtb011VersaoServicos().add(mtxtb011VersaoServico);
        mtxtb011VersaoServico.setMtxtb001Servico(this);

        return mtxtb011VersaoServico;
    }

    public Mtxtb011VersaoServico removeMtxtb011VersaoServico(Mtxtb011VersaoServico mtxtb011VersaoServico) {
        getMtxtb011VersaoServicos().remove(mtxtb011VersaoServico);
        mtxtb011VersaoServico.setMtxtb001Servico(null);

        return mtxtb011VersaoServico;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Mtxtb001Servico [nuServico=");
        builder.append(nuServico);
        builder.append(", ");
        if (dhAtualizacao != null) {
            builder.append("dhAtualizacao=");
            builder.append(dhAtualizacao);
            builder.append(", ");
        }
        if (icConfirmaTransacao != null) {
            builder.append("icFinalizarTransacao=");
            builder.append(icConfirmaTransacao);
            builder.append(", ");
        }
        if (icSituacaoServico != null) {
            builder.append("icSituacaoServico=");
            builder.append(icSituacaoServico);
            builder.append(", ");
        }
        if (noServico != null) {
            builder.append("noServico=");
            builder.append(noServico);
            builder.append(", ");
        }
        if (icTipoServico!= null) {
            builder.append("icTipoServico=");
            builder.append(icTipoServico);
            builder.append(", ");
        }
        if (mtxtb005ServicoCanals != null) {
            builder.append("mtxtb005ServicoCanals=");
            builder.append(mtxtb005ServicoCanals);
            builder.append(", ");
        }
        if (mtxtb011VersaoServicos != null) {
            builder.append("mtxtb011VersaoServicos=");
            builder.append(mtxtb011VersaoServicos);
        }
        builder.append("]");
        return builder.toString();
    }

	public Integer getTempoLimiteEsperaResposta() {
		return tempoLimiteEsperaResposta;
	}

	public String getNoConexao() {
		return noConexao;
	}

	public void setNoConexao(String noConexao) {
		this.noConexao = noConexao;
	}

	public String getNoFilaRequisicao() {
		return noFilaRequisicao;
	}

	public void setNoFilaRequisicao(String noFilaRequisicao) {
		this.noFilaRequisicao = noFilaRequisicao;
	}

	public String getNoFilaResposta() {
		return noFilaResposta;
	}

	public void setNoFilaResposta(String noFilaResposta) {
		this.noFilaResposta = noFilaResposta;
	}
	
	public boolean isOrquestrado() {
    	return this.icServicoOrquestrado.equals(1);
    }
	
	public boolean isCancelamento() {
    	return this.icCancelamentoTransacao.equals(1);
    }
    
}
