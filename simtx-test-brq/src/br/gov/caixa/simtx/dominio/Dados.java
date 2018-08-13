package br.gov.caixa.simtx.dominio;

public class Dados {

	private String NSU;
	private String DATA;
	private String HORA;
	private Integer TP_DOC;
	private String DOC;
	private Integer DDD;
	private String TELEFONE;
	private PMTList PMTLST;

	// ---seguranca
	private String TP_PESSOA;
	private String CPFCNPJ;
	private String COD_SEG;
	private String COD_AGENCIA;
	private String COD_TIPO_CONTA;
	private String NUM_CONTA;

	public String getNSU() {
		return NSU;
	}

	public Dados setNSU(String nSU) {
		NSU = nSU;
		return this;
	}

	public String getDATA() {
		return DATA;
	}

	public Dados setDATA(String dATA) {
		DATA = dATA;
		return this;
	}

	public String getHORA() {
		return HORA;
	}

	public Dados setHORA(String hORA) {
		HORA = hORA;
		return this;
	}

	public Integer getTP_DOC() {
		return TP_DOC;
	}

	public Dados setTP_DOC(Integer tP_DOC) {
		TP_DOC = tP_DOC;
		return this;
	}

	public String getDOC() {
		return DOC;
	}

	public Dados setDOC(String dOC) {
		DOC = dOC;
		return this;
	}

	public Integer getDDD() {
		return DDD;
	}

	public Dados setDDD(Integer dDD) {
		DDD = dDD;
		return this;
	}

	public String getTELEFONE() {
		return TELEFONE;
	}

	public Dados setTELEFONE(String tELEFONE) {
		TELEFONE = tELEFONE;
		return this;
	}

	public PMTList getPMTLST() {
		return PMTLST;
	}

	public Dados setPMTLST(PMTList pMTLST) {
		PMTLST = pMTLST;
		return this;
	}

	public String getTP_PESSOA() {
		return TP_PESSOA;
	}

	public Dados setTP_PESSOA(String tP_PESSOA) {
		TP_PESSOA = tP_PESSOA;
		return this;
	}

	public String getCPFCNPJ() {
		return CPFCNPJ;
	}

	public Dados setCPFCNPJ(String cPFCNPJ) {
		CPFCNPJ = cPFCNPJ;
		return this;
	}

	public String getCOD_SEG() {
		return COD_SEG;
	}

	public Dados setCOD_SEG(String cOD_SEG) {
		COD_SEG = cOD_SEG;
		return this;
	}

	public String getCOD_AGENCIA() {
		return COD_AGENCIA;
	}

	public Dados setCOD_AGENCIA(String cOD_AGENCIA) {
		COD_AGENCIA = cOD_AGENCIA;
		return this;
	}

	public String getCOD_TIPO_CONTA() {
		return COD_TIPO_CONTA;
	}

	public Dados setCOD_TIPO_CONTA(String cOD_TIPO_CONTA) {
		COD_TIPO_CONTA = cOD_TIPO_CONTA;
		return this;
	}

	public String getNUM_CONTA() {
		return NUM_CONTA;
	}

	public Dados setNUM_CONTA(String nUM_CONTA) {
		NUM_CONTA = nUM_CONTA;
		return this;
	}

}
