/*******************************************************************************
 * Copyright (C)  2017 - CAIXA Econômica Federal 
 * 
 * Todos os direitos reservados
 ******************************************************************************/
package br.gov.caixa.simtx.persistencia.core.entidade;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;


@Entity
@Cacheable
@Table(name = "MTXTB008_MEIO_ENTRADA")
@NamedQueries({
	@NamedQuery(name = "Mtxtb008MeioEntrada.findAll", query = "SELECT m FROM Mtxtb008MeioEntrada m"),
	@NamedQuery(name = "Mtxtb008MeioEntrada.buscarPorNome", query =  "SELECT m FROM Mtxtb008MeioEntrada m where m.noMeioEntrada = :noMeioEntrada")
})
public class Mtxtb008MeioEntrada implements Serializable {
    
	private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "NU_MEIO_ENTRADA", unique = true, nullable = false, precision = 2)
    private long nuMeioEntrada;

    @Column(name = "IC_SITUACAO", nullable = false, precision = 1)
    private BigDecimal icSituacao;

    @Column(name = "NO_MEIO_ENTRADA", nullable = false, length = 100)
    private String noMeioEntrada;



    public long getNuMeioEntrada() {
        return this.nuMeioEntrada;
    }

    public void setNuMeioEntrada(long nuMeioEntrada) {
        this.nuMeioEntrada = nuMeioEntrada;
    }

    public BigDecimal getIcSituacao() {
        return this.icSituacao;
    }

    public void setIcSituacao(BigDecimal icSituacao) {
        this.icSituacao = icSituacao;
    }

    public String getNoMeioEntrada() {
        return this.noMeioEntrada;
    }

    public void setNoMeioEntrada(String noMeioEntrada) {
        this.noMeioEntrada = noMeioEntrada;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Mtxtb008MeioEntrada [nuMeioEntrada=");
        builder.append(nuMeioEntrada);
        builder.append(", ");
        if (icSituacao != null) {
            builder.append("icSituacao=");
            builder.append(icSituacao);
            builder.append(", ");
        }
        if (noMeioEntrada != null) {
            builder.append("noMeioEntrada=");
            builder.append(noMeioEntrada);
            builder.append(", ");
        }
        builder.append("]");
        return builder.toString();
    }


}
