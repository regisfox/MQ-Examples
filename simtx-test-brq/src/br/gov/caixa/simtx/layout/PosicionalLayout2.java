package br.gov.caixa.simtx.layout;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

public class PosicionalLayout2 extends Posicional {

	private Long ttrcod;
	private Integer nsu;
	private Integer agenciaConta;
	private Integer tipoConta;
	private Integer numeroConta;
	private String cpf;
	private Integer ddd;
	private Integer tel;
	private Integer quantidadeServico;
	private Integer horaInicio;
	private Integer horaFinal;
	private List<ServicoEValorMinimo> servicosEValorMinimo;
	private Integer codigoResposta;
	private String resposta;
	private Integer canal;
	private Integer versao;

	public PosicionalLayout2() {
		servicosEValorMinimo = new ArrayList<ServicoEValorMinimo>();
	}
	
	public Long getTtrcod() {
		return ttrcod;
	}

	public void setTtrcod(Long ttrcod) {
		this.ttrcod = ttrcod;
	}

	public Integer getNsu() {
		return nsu;
	}

	public void setNsu(Integer nsu) {
		this.nsu = nsu;
	}

	public Integer getAgenciaConta() {
		return agenciaConta;
	}

	public void setAgenciaConta(Integer agenciaConta) {
		this.agenciaConta = agenciaConta;
	}

	public Integer getTipoConta() {
		return tipoConta;
	}

	public void setTipoConta(Integer tipoConta) {
		this.tipoConta = tipoConta;
	}

	public Integer getNumeroConta() {
		return numeroConta;
	}

	public void setNumeroConta(Integer numeroConta) {
		this.numeroConta = numeroConta;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = StringUtils.leftPad(cpf, 11, '0');
	}

	public Integer getDdd() {
		return ddd;
	}

	public void setDdd(Integer ddd) {
		this.ddd = ddd;
	}

	public Integer getTel() {
		return tel;
	}

	public void setTel(Integer tel) {
		this.tel = tel;
	}

	public Integer getQuantidadeServico() {
		return quantidadeServico;
	}

	public void setQuantidadeServico(Integer quantidadeServico) {
		this.quantidadeServico = quantidadeServico;
	}

	public Integer getHoraInicio() {
		return horaInicio;
	}

	public void setHoraInicio(Integer horaInicio) {
		this.horaInicio = horaInicio;
	}

	public Integer getHoraFinal() {
		return horaFinal;
	}

	public void setHoraFinal(Integer horaFinal) {
		this.horaFinal = horaFinal;
	}

	public List<ServicoEValorMinimo> getServicosEValorMinimo() {
		return servicosEValorMinimo;
	}

	/*
	 * Se não saber oque esta fazendo favor utilizar o metodo addServicosEValorMinimo
	 */
	public void setServicosEValorMinimo(
			List<ServicoEValorMinimo> servicosEValorMinimo) {
		this.servicosEValorMinimo = servicosEValorMinimo;
	}

	public void addServicosEValorMinimo(Long codigo, Long valorMinimo) {
		this.servicosEValorMinimo.add(new ServicoEValorMinimo(codigo, valorMinimo));
	}

	public Integer getCodigoResposta() {
		return codigoResposta;
	}

	public void setCodigoResposta(Integer codigoResposta) {
		this.codigoResposta = codigoResposta;
	}

	public String getResposta() {
		return resposta;
	}

	public void setResposta(String resposta) {
		this.resposta = resposta;
	}

	public Integer getCanal() {
		return canal;
	}

	public void setCanal(Integer canal) {
		this.canal = canal;
	}

	public Integer getVersao() {
		return versao;
	}

	public void setVersao(Integer versao) {
		this.versao = versao;
	}

	public String converter() {
		StringBuilder convertido = new StringBuilder();
		
		convertido
			.append(formata(ttrcod, 10))
			.append(formata(nsu, 9))
			.append(formata(agenciaConta, 4))
			.append(formata(tipoConta, 3))
			.append(formata(numeroConta, 9))
			.append(formata(cpf, 14))
			.append(formata(ddd, 2))
			.append(formata(tel, 9))
			.append(formata(quantidadeServico, 2))
			.append(formata(horaInicio, 4))
			.append(formata(horaFinal, 4));
		
		for(ServicoEValorMinimo servicoEValorMinimo : servicosEValorMinimo) {
			convertido.append(formata(servicoEValorMinimo.getCodigo(), 10));//validar estes tamanhos
			convertido.append(formata(servicoEValorMinimo.getValorMinimo(), 11));
		}
			//mudar para tamanho dinamico
			convertido.append("000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000");
			convertido
				.append(formata(codigoResposta, 2))
				.append(formata(resposta, 120))
				.append(formata(canal, 4))
				.append(formata(versao, 3));
			
		return convertido.toString();
	}
}

class ServicoEValorMinimo {
	private Long codigo;
	private Long valorMinimo;
	
	
	public ServicoEValorMinimo(Long codigo, Long valorMinimo) {
		super();
		this.codigo = codigo;
		this.valorMinimo = valorMinimo;
	}
	
	public Long getCodigo() {
		return codigo;
	}
	public void setCodigo(Long codigo) {
		this.codigo = codigo;
	}
	public Long getValorMinimo() {
		return valorMinimo;
	}
	public void setValorMinimo(Long valorMinimo) {
		this.valorMinimo = valorMinimo;
	}
	
	
}
