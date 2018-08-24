package br.gov.caixa.simtx.persistencia.core.entidade;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * The persistent class for the MTXTB041_HSTRO_PRMTO_CNTCA database table.
 * 
 * @author joseoliveirajunior
 * 
 */
@Entity
@Table(name = "MTXTB041_HSTRO_PRMTO_CNTCA")
public class Mtxtb041HistoricoPrmtoPgtoContingencia implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@EmbeddedId
	private Mtxtb041HistoricoPrmtoPgtoContingenciaPK id;
	
	@Column(name = "TS_ALTERACAO")
	private Date dataTsAlteracao;
	
	@Column(name = "VR_RECEBIMENTO_MINIMO", nullable = false, precision = 16, scale = 2)
    private BigDecimal valorMinimo;
    
    @Column(name = "VR_RECEBIMENTO_MAXIMO", nullable = false, precision = 16, scale = 2)
    private BigDecimal valorMaximo;
    
    @Column(name = "IC_AUTORIZACAO_PAGAMENTO", nullable = false, precision = 1)
    private int icAutorizacaoContingencia;
	
	@Column(name = "CO_USUARIO_ALTERACAO")
	private String coUsuarioAlteracao;
	
	@Column(name = "CO_MAQUINA_ALTERACAO")
	private String coMAquinaAlteracao;

	public Mtxtb041HistoricoPrmtoPgtoContingenciaPK getId() {
		return id;
	}

	public void setId(Mtxtb041HistoricoPrmtoPgtoContingenciaPK id) {
		this.id = id;
	}

	public Date getDataTsAlteracao() {
		return dataTsAlteracao;
	}

	public void setDataTsAlteracao(Date dataTsAlteracao) {
		this.dataTsAlteracao = dataTsAlteracao;
	}

	public String getCoUsuarioAlteracao() {
		return coUsuarioAlteracao;
	}

	public void setCoUsuarioAlteracao(String coUsuarioAlteracao) {
		this.coUsuarioAlteracao = coUsuarioAlteracao;
	}

	public String getCoMAquinaAlteracao() {
		return coMAquinaAlteracao;
	}

	public void setCoMAquinaAlteracao(String coMAquinaAlteracao) {
		this.coMAquinaAlteracao = coMAquinaAlteracao;
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

	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder(400);
		builder.append("Mtxtb041HistoricoPrmtoPgtoContingencia [id=");
		builder.append(id);
		builder.append(", dataTsAlteracao=");
		builder.append(dataTsAlteracao);
		builder.append(", valorMinimo=");
		builder.append(valorMinimo);
		builder.append(", valorMaximo=");
		builder.append(valorMaximo);
		builder.append(", icAutorizacaoContingencia=");
		builder.append(icAutorizacaoContingencia);
		builder.append(", coUsuarioAlteracao=");
		builder.append(coUsuarioAlteracao);
		builder.append(", coMAquinaAlteracao=");
		builder.append(coMAquinaAlteracao);
		builder.append("]");
		return builder.toString();
	}
}
