package br.gov.caixa.simtx.dominio;

import java.util.ArrayList;
import java.util.Random;

import br.gov.caixa.simtx.dominio.adesao.Adesao;
import br.gov.caixa.simtx.dominio.adesao.Telefone;
import br.gov.caixa.simtx.dominio.listaservico.CabecalhoListaServico;
import br.gov.caixa.simtx.dominio.listaservico.ListaServico;
import br.gov.caixa.simtx.layout.AdesaoGenerica;
import br.gov.caixa.simtx.layout.PosicionalLayout2;
import br.gov.caixa.simtx.layout.ServicoEntrada;
import br.gov.caixa.simtx.layout.TransacaoGenerica;
import br.gov.caixa.simtx.utils.Documento;
import br.gov.caixa.simtx.utils.Gerador;

import com.thoughtworks.xstream.XStream;

public class MontaXML {
	//private final String xmlHeader = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n";
	
	public static ListaServico montarMensagemListaServico() {
		ListaServico ls = new ListaServico();
		
		ls.setCabecalho(new CabecalhoListaServico());
		
		return ls;
	}

	public static ListaServico montarMensagemListaServico(String nsu, String versao, String canal) {
		ListaServico ls = new ListaServico();
		
		ls.setCabecalho(new CabecalhoListaServico());
		ls.getCabecalho().setNsu(nsu);
		ls.getCabecalho().setVersao(versao);
		ls.getCabecalho().setCanal(canal);
		
		return ls;
	}

	public static TransacaoGenerica montarMensagemGenericaLayout3() {
		TransacaoGenerica tg = new TransacaoGenerica();
		
		tg.setCabecalho(new Cabecalho());
		tg.setDetalhes(new ArrayList<Mensagem>());
		
		return tg;
	}
	
	public static AdesaoGenerica montarMensagemAdesaoGenerica() {
		AdesaoGenerica ag = new AdesaoGenerica();
		
		Telefone telefone = new Telefone("11", "995933663");
		Adesao adesao = new Adesao();
		adesao.setTransacao("999990220")
		.setServico("20")
		.setTipo_documento("9")
		.setDocumento(Documento.chipras())
		.setHora_inicio("0000")
		.setHora_fim("2359")
		.setValor_minimo("1000000")
		.getTelefones().add(telefone);
		
		ag.getCabecalho().setNsu(Gerador.getNsu(999))
		.setData_hora("02032015105959")
		.setCanal("9705")
		.setVersao("3")
		.setCpf(Documento.novoCPF());
		
		ag.getDetalhes().add(adesao);
		
		return ag; 
	}
	
	public static AdesaoGenerica montarMensagemAdesaoGenericaAlteracao() {
		AdesaoGenerica ag = new AdesaoGenerica();
		
		Telefone telefone = new Telefone("11", "995933663");
		Telefone telefone2 = new Telefone("11", "995933663");
		String conta = Documento.novaContaSidec();
		
		Adesao adesao = new Adesao();
		adesao.setTransacao("999990222")
		.setServico("5")
		.setTipo_documento("7")
		.setDocumento(conta)
		.setHora_inicio("0000")
		.setHora_fim("2359")
		.setValor_minimo("1000000")
		.getTelefones().add(telefone);
		
		Adesao adesao2 = new Adesao();
		adesao2.setTransacao("999990222")
		.setServico("10")
		.setTipo_documento("7")
		.setDocumento(conta)
		.setHora_inicio("0000")
		.setHora_fim("2359")
		.setValor_minimo("1000000")
		.getTelefones().add(telefone2);
		
		ag.getCabecalho().setNsu(Gerador.getNsu(999))
		.setData_hora("02032015105959")
		.setCanal("9705")
		.setVersao("3")
		.setCpf(Documento.novoCPF());
		
		ag.getDetalhes().add(adesao);
		ag.getDetalhes().add(adesao2);
		
		return ag; 
	}
	
	public static AdesaoGenerica montarMensagemAdesaoGenericaFinanceiro() {
		AdesaoGenerica ag = new AdesaoGenerica();
		
		Telefone telefone = new Telefone("11", "995933663");
		Telefone telefone2 = new Telefone("11", "995933663");
		
		Adesao adesao = new Adesao();
		adesao.setTransacao("999990220")
		.setServico("5")
		.setTipo_documento("7")
		.setDocumento(Documento.novaContaSidec())
		.setHora_inicio("0000")
		.setHora_fim("2359")
		.getTelefones().add(telefone);
		adesao.setValor_minimo("100000");
		
		Adesao adesao2 = new Adesao();
		adesao2.setTransacao("999990220")
		.setServico("10")
		.setTipo_documento("7")
		.setDocumento(adesao.getDocumento())
		.setHora_inicio("0000")
		.setHora_fim("2359")
		.setValor_minimo("100000")
		.getTelefones().add(telefone2);
		
		
		ag.getCabecalho().setNsu(Gerador.getNsu(999))
		.setData_hora("02032015105959")
		.setCanal("9705")
		.setVersao("3")
		.setCpf(Documento.novoCPF());
		
		ag.getDetalhes().add(adesao);
		ag.getDetalhes().add(adesao2);
		
		return ag; 
	}
	
	
	
	public static ServicoEntrada montarMensagemGenericaLayout2(String telefone, String cpf, String servico, String ttr, String versao) {
		Dados dados = new Dados();
		Random r = new Random(System.currentTimeMillis()); 
		dados.setNSU(r.nextInt(9999999) + "");
		
		dados.setDATA("01122014");//TODO gerar
		dados.setHORA("142724");//TODO gerar
		dados.setTP_DOC(1);
		
		String cpfDefinido = (cpf != null) ? cpf : Documento.novoCPF(); 
		dados.setDOC(cpfDefinido);
		dados.setDDD(11);
		String telDefinido = (telefone != null) ? telefone : "985903483"; 
		dados.setTELEFONE(telDefinido);
		
		PMTList pmtLst = new PMTList();
		pmtLst.setPmt01("	 TESTE SMS D");
		pmtLst.setPmt02("01/12/2014");
		pmtLst.setPmt03("R$666,00");
		pmtLst.setPmt04("200605 NN11900000000118260");

		dados.setPMTLST(pmtLst);
		
		String svcDefinido = (servico != null) ? servico : "9730";
		String ttrDefinido = (ttr != null) ? ttr : "0000000019";
		String versaoDefinido = (versao != null) ? versao : "002";
		
		Operacao operacao = new Operacao("9730", svcDefinido, ttrDefinido, versaoDefinido);
		
		ServicoEntrada se = new ServicoEntrada();
		
		se.setDADOS(dados);
		se.setOPERACAO(operacao);
		return se;
	}
	
	public static ServicoEntrada montarMensagemSeguranca() {
		Dados dados = new Dados();
		Random r = new Random(System.currentTimeMillis()); 
		dados.setNSU(r.nextInt(9999999) + "")
			.setDATA("01122014")
			.setHORA("142724")
			.setTP_PESSOA("1")
			.setCPFCNPJ("4405603308")
			.setDDD(11)
			.setTELEFONE("995933663")
			.setCOD_SEG("99999")
			.setCOD_AGENCIA("1679")
			.setCOD_TIPO_CONTA("001")
			.setNUM_CONTA("000000918");
		
		
		PMTList pmtLst = new PMTList();
		pmtLst.setPmt01("	 TESTE SMS D");
		pmtLst.setPmt02("01/12/2014");
		pmtLst.setPmt03("R$666,00");
		pmtLst.setPmt04("200605 NN11900000000118260");

		dados.setPMTLST(pmtLst);
		
		Operacao operacao = new Operacao("9605", "0000000016", "999990350", "001");
		
		ServicoEntrada se = new ServicoEntrada();
		
		se.setDADOS(dados);
		se.setOPERACAO(operacao);
		return se;
	}
	
	public String montarMensagemlayout3(int cnl, int svc, long trn) {
		XStream xs = new XStream();
		xs.alias("transacao_generica", TransacaoGenerica.class);
		xs.alias("mensagem", Mensagem.class);
		xs.alias("parametro", Parametro.class);
		
		xs.useAttributeFor(TransacaoGenerica.class, "tipo");
		xs.useAttributeFor(TransacaoGenerica.class, "qnt_transacoes");
		xs.useAttributeFor(TransacaoGenerica.class, "sequencial_arquivo");
		Cabecalho cabecalho = new Cabecalho();
//		cabecalho.setCanal(cnl);
//		cabecalho.setServico(svc);
		cabecalho.setVersao("003");
		
		Mensagem msg = new Mensagem();
		msg.setNsu(Gerador.getNsu(1000000));
//		msg.setTransacao(trn);
		msg.setDatahora_referencia("09122014");
		msg.setDatahora_transacao("09122014235959");
//		msg.setTipo_documento(7);
		msg.setDocumento("173001000051225");
		msg.setCpf(Documento.novoCPF());
		msg.setValor(Gerador.getValor());
//		
//		String parametro = "01";
//		String parametro2 = "4232";
//		
//		Parametro parametro = new Parametro();
//		parametro.setCod_parametro("01");
//		parametro.setValor(Generator.getValor());
//		
//		msg.getParametros().add(parametro);
		
		TransacaoGenerica tg = new TransacaoGenerica();
		tg.setCabecalho(cabecalho);
		tg.getDetalhes().add(msg);
		tg.setTipo("online");
		tg.setQnt_transacoes(1);
		tg.setSequencial_arquivo(2);
		
		return xs.toXML(tg).replaceAll("__", "_");
	}
	
	public static PosicionalLayout2 montarLayout2InclusaoPadrao() {
		PosicionalLayout2 msg = new PosicionalLayout2();
		String[] conta = Documento.novaContaSidecArray();
		
		msg.setTtrcod(999990211L);
		msg.setNsu(Gerador.getNsuInteiro(100));
		msg.setAgenciaConta(Integer.valueOf(conta[0]));
		msg.setTipoConta(Integer.valueOf(conta[1]));
		msg.setNumeroConta(Integer.valueOf(conta[2]));
		msg.setCpf(Documento.novoCNPJ());
		msg.setDdd(11);
		msg.setTel(986470360);
		msg.setHoraInicio(9);
		msg.setHoraFinal(23);
		msg.setQuantidadeServico(2);
		msg.addServicosEValorMinimo(10L, 100000L);
		msg.addServicosEValorMinimo(5L, 100000L);
		msg.setCodigoResposta(99);
		msg.setResposta("testando");
		msg.setCanal(9602);
		msg.setVersao(2);
		
		return msg;
	}
	
	public static PosicionalLayout2 montarLayout2InclusaoOperacaoInvalida() {
		PosicionalLayout2 msg = new PosicionalLayout2();
		
		msg.setTtrcod(999990211L);
        msg.setNsu(Gerador.getNsuInteiro(100));
        msg.setAgenciaConta(0123);
        msg.setTipoConta(023); // operacao invalida
        msg.setNumeroConta(12345678);
        msg.setCpf(Documento.novoCNPJ());
        msg.setDdd(11);
        msg.setTel(986470360);
        msg.setHoraInicio(9);
        msg.setHoraFinal(23);
        msg.setQuantidadeServico(2);
        msg.addServicosEValorMinimo(10L, 100000L);
        msg.addServicosEValorMinimo(5L, 100000L);
        msg.setCodigoResposta(99);
        msg.setResposta("testando");
        msg.setCanal(9602);
        msg.setVersao(2);
		
		return msg;
	}
	
	public static PosicionalLayout2 montarLayout2InclusaoTipoDocumentoInvalido() {
        PosicionalLayout2 msg = new PosicionalLayout2();
        
        msg.setAgenciaConta(13);
        msg.setCanal(9602);
        msg.setCpf(Documento.novoCNPJ());
        msg.setCodigoResposta(99);
        msg.setDdd(11);
        msg.setHoraFinal(23);
        msg.setHoraInicio(9);
        msg.setNsu(Gerador.getNsuInteiro(100));
        msg.setNumeroConta(1234567);        
        msg.setQuantidadeServico(2);
        msg.setResposta("testando");
        msg.addServicosEValorMinimo(10L, 100000L);
        msg.addServicosEValorMinimo(5L, 100000L);
        msg.setTel(986470360);
        msg.setTipoConta(7); // SIDEC
        msg.setTtrcod(999990211L);
        msg.setVersao(2);
        
        return msg;
    }
	
	
	public static PosicionalLayout2 montarLayout2AlteracaoPadrao() {
		PosicionalLayout2 msg = montarLayout2InclusaoPadrao();
		msg.setTtrcod(999990212L);
		return msg;
	}
	
	public static PosicionalLayout2 montarLayout2ConsultaPadrao() {
		PosicionalLayout2 msg = new PosicionalLayout2();
		String[] conta = Documento.novaContaSidecArray();
		
		msg.setTtrcod(999990210L);
		msg.setNsu(Gerador.getNsuInteiro(100));
		msg.setAgenciaConta(Integer.valueOf(conta[0]));
		msg.setTipoConta(Integer.valueOf(conta[1]));
		msg.setNumeroConta(Integer.valueOf(conta[2]));
		msg.setCpf(Documento.novoCPF());
		msg.setDdd(11);
		msg.setTel(986470360);
		msg.setQuantidadeServico(2);
		msg.addServicosEValorMinimo(5L, 2000L);
		msg.addServicosEValorMinimo(10L, 2000L);
		msg.setCodigoResposta(99);
		msg.setResposta("testando");
		msg.setCanal(9602);
		msg.setVersao(2);
		
		return msg;
	}
	
	public static String montarLayout1() {
		String msg = "0999990211278528308conta000740730309651188556644020000235900000000100000001000000000000050000000500000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000099                                                                                                                        ";
		String conta = Documento.novaContaSidec();
		msg = msg.replace("conta", conta);
		return msg;
	}
	
	public static TransacaoGenerica montarTransacaoGenericaLayout3Padrao() {
		Long nsu = Gerador.getNsu(0);
		TransacaoGenerica tg = MontaXML.montarMensagemGenericaLayout3();
		
		tg.getCabecalho().setCanal("9730").setServico("5").setVersao("3");
		
		tg.setTipo("online");
		tg.setQnt_transacoes(1);
		tg.setSequencial_arquivo(2);
		
		Mensagem msg = new Mensagem()
				.setNsu(nsu)
				.setTransacao("530")
				.setDatahora_referencia("07052015")
				.setDatahora_transacao("07052015235959")
				.setTipo_documento("7")
				.setDocumento("239013000004903")
				.setCpf("23045007813123")
				.setValor("100000");
		
		tg.getDetalhes().add(msg);
		
		msg.setParametros(new ArrayList<String>());
		msg.getParametros().add("CASAS BAHIA");
		msg.getParametros().add("1234");
		msg.getParametros().add("VISA");
		msg.getParametros().add("3581-8818");

		return tg;
	}
	
}
