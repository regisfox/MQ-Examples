package br.gov.caixa.simtx.persistencia.core.entidade;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "MTXTB040_PARAMETRO_PGMNO_CNTCA")
@NamedQueries({
	@NamedQuery(name = "Mtxtb040PrmtoPgtoContingencia.findAll", query = "SELECT m FROM Mtxtb040PrmtoPgtoContingencia m"),
	@NamedQuery(name = "Mtxtb040PrmtoPgtoContingencia.buscarPorCanaleBoleto", query = "  SELECT m "
																					+ " FROM Mtxtb040PrmtoPgtoContingencia m "
																					+ "where m.id.nuCanal004 = :nuCanal004 "
																					+ "  and m.id.icTipoBoleto = :icTipoBoleto "
																					+ "	 and m.id.icOrigemContingencia = :icTipoContingencia ")
})
public class Mtxtb040PrmtoPgtoContingencia implements Serializable {
	
	private static final long serialVersionUID = 3724158981635252099L;

	@EmbeddedId
	private Mtxtb040PrmtoPgtoContingenciaPK id;
    
    @Column(name = "VR_RECEBIMENTO_MINIMO", nullable = false, precision = 14, scale = 2)
    private BigDecimal valorMinimo;
    
    @Column(name = "VR_RECEBIMENTO_MAXIMO", nullable = false, precision = 14, scale = 2)
    private BigDecimal valorMaximo;
    
    @Column(name = "IC_AUTORIZACAO_PAGAMENTO", nullable = false, precision = 1)
    private int icAutorizacaoContingencia;
    
    @Column(name = "DH_ATUALIZACAO", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date dhAtualizacao;
    
    @ManyToOne
	@JoinColumn(name = "NU_CANAL_004", referencedColumnName = "NU_CANAL", nullable = false, insertable = false, updatable = false)
	private Mtxtb004Canal mtxtb004Canal;

    
    

	public Mtxtb040PrmtoPgtoContingenciaPK getId() {
		return id;
	}

	public void setId(Mtxtb040PrmtoPgtoContingenciaPK id) {
		this.id = id;
	}

	public BigDecimal getValorMinimo() {
		return valorMinimo;
	}

	public void setValorMinimo(BigDecimal valorMinimo) {
		this.valorMinimo = valorMinimo;
	}

	public BigDecimal getValorMaximo() {
		return valorMaximo;
	}

	public void setValorMaximo(BigDecimal valorMaximo) {
		this.valorMaximo = valorMaximo;
	}

	public int getIcAutorizacaoContingencia() {
		return icAutorizacaoContingencia;
	}

	public void setIcAutorizacaoContingencia(int icAutorizacaoContingencia) {
		this.icAutorizacaoContingencia = icAutorizacaoContingencia;
	}

	public Date getDhAtualizacao() {
		return dhAtualizacao;
	}

	public void setDhAtualizacao(Date dhAtualizacao) {
		this.dhAtualizacao = dhAtualizacao;
	}

	public Mtxtb004Canal getMtxtb004Canal() {
		return mtxtb004Canal;
	}

	public void setMtxtb004Canal(Mtxtb004Canal mtxtb004Canal) {
		this.mtxtb004Canal = mtxtb004Canal;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Mtxtb040PrmtoPgtoContingencia [id=");
		builder.append(id);
		builder.append(", valorMinimo=");
		builder.append(valorMinimo);
		builder.append(", valorMaximo=");
		builder.append(valorMaximo);
		builder.append(", icRecebimentoContingencia=");
		builder.append(icAutorizacaoContingencia);
		builder.append(", dhAtualizacao=");
		builder.append(dhAtualizacao);
		builder.append(", mtxtb004Canal=");
		builder.append(mtxtb004Canal);
		builder.append("]");
		return builder.toString();
	}

}
