package br.gov.caixa.simtx.persistencia.core.entidade;

import java.io.Serializable;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * The persistent class for the MTXTB032_MARCA_CONTA database table.
 * 
 */
@Entity
@Cacheable
@Table(name = "MTXTB032_MARCA_CONTA")
@NamedQuery(name = "Mtxtb032MarcaConta.findAll", query = "SELECT m FROM Mtxtb032MarcaConta m")
public class Mtxtb032MarcaConta implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
    @Column(name = "NU_MARCA_CONTA", unique = true, nullable = false, precision = 3)
    private long nuMarcaConta;

    @Column(name = "CO_MARCA_CONTA")
    private String coMarcaConta;
	
    @Column(name = "DE_MARCA_CONTA")
    private String deMarcaConta;

	public long getNuMarcaConta() {
		return nuMarcaConta;
	}

	public void setNuMarcaConta(long nuMarcaConta) {
		this.nuMarcaConta = nuMarcaConta;
	}

	public String getCoMarcaConta() {
		return coMarcaConta;
	}

	public void setCoMarcaConta(String coMarcaConta) {
		this.coMarcaConta = coMarcaConta;
	}

	public String getDeMarcaConta() {
		return deMarcaConta;
	}

	public void setDeMarcaConta(String deMarcaConta) {
		this.deMarcaConta = deMarcaConta;
	}
}
