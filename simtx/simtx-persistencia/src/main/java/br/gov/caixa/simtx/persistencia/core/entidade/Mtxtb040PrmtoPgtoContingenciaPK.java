package br.gov.caixa.simtx.persistencia.core.entidade;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Mtxtb040PrmtoPgtoContingenciaPK implements Serializable {
	
	private static final long serialVersionUID = -1846444576354534194L;

    
    @Column(name = "NU_CANAL_004", nullable = false, precision = 3)
    private int nuCanal004;
    
    @Column(name = "IC_TIPO_BOLETO", nullable = false, precision = 1)
    private int icTipoBoleto;
    
    @Column(name = "IC_ORIGEM_CONTINGENCIA", nullable = false, precision = 1)
    private int icOrigemContingencia;

    

	public int getNuCanal004() {
		return nuCanal004;
	}

	public void setNuCanal004(int nuCanal004) {
		this.nuCanal004 = nuCanal004;
	}

	public int getIcTipoBoleto() {
		return icTipoBoleto;
	}

	public void setIcTipoBoleto(int icTipoBoleto) {
		this.icTipoBoleto = icTipoBoleto;
	}

	public int getIcOrigemContingencia() {
		return icOrigemContingencia;
	}

	public void setIcOrigemContingencia(int icOrigemContingencia) {
		this.icOrigemContingencia = icOrigemContingencia;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + icOrigemContingencia;
		result = prime * result + icTipoBoleto;
		result = prime * result + nuCanal004;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Mtxtb040PrmtoPgtoContingenciaPK other = (Mtxtb040PrmtoPgtoContingenciaPK) obj;
		if (icOrigemContingencia != other.icOrigemContingencia)
			return false;
		if (icTipoBoleto != other.icTipoBoleto)
			return false;
		if (nuCanal004 != other.nuCanal004)
			return false;
		return true;
	}

	
}
