package br.gov.caixa.simtx.modelo;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "GMSTB001_TRNSORJTA")
public class TransacaoRejeitada {

	@Id
	private Long NU_TRNSO_RJTDA;
	private Integer NU_CANAL;
	private Long NU_NSU_TRNSO;
	private Integer IC_MOTIVO_REJEICAO;

	public Long getNU_TRNSO_RJTDA() {
		return NU_TRNSO_RJTDA;
	}

	public void setNU_TRNSO_RJTDA(Long nU_TRNSO_RJTDA) {
		NU_TRNSO_RJTDA = nU_TRNSO_RJTDA;
	}

	public Integer getNU_CANAL() {
		return NU_CANAL;
	}

	public void setNU_CANAL(Integer nU_CANAL) {
		NU_CANAL = nU_CANAL;
	}

	public Long getNU_NSU_TRNSO() {
		return NU_NSU_TRNSO;
	}

	public void setNU_NSU_TRNSO(Long nU_NSU_TRNSO) {
		NU_NSU_TRNSO = nU_NSU_TRNSO;
	}

	public Integer getIC_MOTIVO_REJEICAO() {
		return IC_MOTIVO_REJEICAO;
	}

	public void setIC_MOTIVO_REJEICAO(Integer iC_MOTIVO_REJEICAO) {
		IC_MOTIVO_REJEICAO = iC_MOTIVO_REJEICAO;
	}

}
