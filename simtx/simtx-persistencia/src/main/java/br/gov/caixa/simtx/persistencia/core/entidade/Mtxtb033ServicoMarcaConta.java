package br.gov.caixa.simtx.persistencia.core.entidade;

import java.io.Serializable;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Cacheable
@Table(name = "MTXTB033_SERVICO_MARCA_CONTA")
@NamedQueries({
@NamedQuery(name = "Mtxtb033ServicoMarcaConta.findAll", query = "SELECT m FROM Mtxtb033ServicoMarcaConta m"),
@NamedQuery(name = "Mtxtb033ServicoMarcaConta.buscaServicoMarca", query = "SELECT mc FROM Mtxtb033ServicoMarcaConta m join m.mtxtb032MarcaConta mc where m.nuServico = :numeroServico")
})

public class Mtxtb033ServicoMarcaConta implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Id
    @Column(name = "NU_MARCA_CONTA_032", unique = true, nullable = false, precision = 5)
    private long nuMarcaConta032;
 
    @Column(name = "NU_SERVICO_001")
    private long nuServico;
    
    @ManyToOne
    @JoinColumn(name = "NU_MARCA_CONTA_032", referencedColumnName = "NU_MARCA_CONTA", insertable = false, updatable = false)
    private Mtxtb032MarcaConta mtxtb032MarcaConta;

	public long getNuMarcaConta032() {
		return nuMarcaConta032;
	}

	public void setNuMarcaConta032(long nuMarcaConta032) {
		this.nuMarcaConta032 = nuMarcaConta032;
	}

	public long getNuServico() {
		return nuServico;
	}

	public void setNuServico(long nuServico) {
		this.nuServico = nuServico;
	}

}
