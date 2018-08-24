package br.gov.caixa.simtx.assinatura.multipla.infra;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.gov.caixa.simtx.persistencia.assinaturamultipla.entidade.Mtxtb027TransacaoAssinaturaMultipla;
import br.gov.caixa.simtx.persistencia.assinaturamultipla.entidade.Mtxtb028ControleAssinaturaMultipla;
import br.gov.caixa.simtx.persistencia.assinaturamultipla.vo.SituacaoAssinaturaMultipla;
import br.gov.caixa.simtx.persistencia.cache.assinaturamultipla.FornecedorDadosAssinaturaMultipla;

public class FornecedorDadosAssinaturaMultiplaMock implements FornecedorDadosAssinaturaMultipla {

	@Override
	public Mtxtb027TransacaoAssinaturaMultipla salvar(Mtxtb027TransacaoAssinaturaMultipla assinaturaMultipla) {
		return null;
	}

	@Override
	public Mtxtb028ControleAssinaturaMultipla salvar(Mtxtb028ControleAssinaturaMultipla assinatura) {
		return null;
	}

	@Override
	public Mtxtb027TransacaoAssinaturaMultipla buscarAssinaturaMultipla(long nsu) {
		Mtxtb027TransacaoAssinaturaMultipla entidade = new Mtxtb027TransacaoAssinaturaMultipla();
		entidade.setUnidade(1234);
		entidade.setProduto(123);
		entidade.setConta(12345678);
		entidade.setDvConta(1);
		entidade.setIndicadorTipoConta(1);
		entidade.setNsuAssinaturaMultipla(nsu);
		entidade.setNsuPermissao(nsu+1);
		entidade.setDataEfetivacao(new Date());
		entidade.setDataPermissao(new Date());
		entidade.setServico(110030);
		entidade.setVersaoServico(1);
		entidade.setServicoPermissao(1);
		entidade.setSituacao(SituacaoAssinaturaMultipla.PENDENTE_ASSINATURA.getRotulo());
		entidade.setXmlNegocial("<ns:CONTRA_ORDEM_CHEQUE_ENTRADA 	xmlns:comuns=\"http://caixa.gov.br/simtx/comuns/comuns\" 	xmlns:enuns_type=\"http://caixa.gov.br/simtx/comuns/enuns_type\" 	xmlns:h=\"http://caixa.gov.br/simtx/comuns/header\" 	xmlns:ns=\"http://caixa.gov.br/simtx/manutencao_cheque/v1/ns\" 	xmlns:valida_assinatura=\"http://caixa.gov.br/simtx/meioentrada/valida_assinatura\" 	xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"	xsi:schemaLocation=\"http://caixa.gov.br/simtx/manutencaocheque Contra_Ordem_Cheques.xsd \"><HEADER><SERVICO><CODIGO>110030</CODIGO><VERSAO>1</VERSAO></SERVICO><CANAL><SIGLA>SIIBC</SIGLA><DATAHORA>20171228162300</DATAHORA></CANAL><MEIOENTRADA>5</MEIOENTRADA><IDENTIFICADOR_ORIGEM><ENDERECO_IP>127.0.0.1</ENDERECO_IP> </IDENTIFICADOR_ORIGEM><USUARIO_SERVICO>SDMTX01</USUARIO_SERVICO><USUARIO>JOANADAR</USUARIO><DATA_REFERENCIA>20171228</DATA_REFERENCIA></HEADER><CPF>36199093852</CPF><CONTA><CONTA_SIDEC><UNIDADE>1234</UNIDADE><OPERACAO>123</OPERACAO><CONTA>12345678</CONTA><DV>1</DV></CONTA_SIDEC></CONTA><TOKEN><TOKEN>TOKEN</TOKEN><ID_SESSAO>ID_SESSAO</ID_SESSAO></TOKEN><ASSINATURA_MULTIPLA><ASSINATURA>A</ASSINATURA><SERVICO_SIPER>666</SERVICO_SIPER><APELIDO>APELIDO</APELIDO><DISPOSITIVO><SISTEMA_OPERACIONAL>ANDROID_TABLET</SISTEMA_OPERACIONAL><CODIGO>1</CODIGO></DISPOSITIVO><DATA_PREVISTA_EFETIVACAO>2018-01-15</DATA_PREVISTA_EFETIVACAO><RESUMO_TRANSACAO>resumindo</RESUMO_TRANSACAO></ASSINATURA_MULTIPLA>	<CONTRA_ORDEM_CHEQUE><NUMERO_CHEQUE><INICIO>1</INICIO><FIM>2</FIM></NUMERO_CHEQUE><CODIGO_MOTIVO>01</CODIGO_MOTIVO><TIPO_COMANDO>INCLUSAO</TIPO_COMANDO><JUSTIFICATIVA>apenas um teste</JUSTIFICATIVA></CONTRA_ORDEM_CHEQUE></ns:CONTRA_ORDEM_CHEQUE_ENTRADA>");
		return entidade;
	}

	@Override
	public List<Mtxtb027TransacaoAssinaturaMultipla> buscarAssinaturasMultiplasPendentes(int unidade, int operacao,
			long numeroConta, int dv, int tipoConta) {
		
		List<Mtxtb027TransacaoAssinaturaMultipla> lista = new ArrayList<>();
		
		if(unidade == 1679) {
			for(int i = 0; i < 5; i++) {
				Mtxtb027TransacaoAssinaturaMultipla entidade = new Mtxtb027TransacaoAssinaturaMultipla();
				entidade.setUnidade(unidade);
				entidade.setProduto(operacao);
				entidade.setConta(numeroConta);
				entidade.setDvConta(dv);
				entidade.setIndicadorTipoConta(tipoConta);
				entidade.setNsuAssinaturaMultipla(i);
				entidade.setNsuPermissao(123+i);
				entidade.setDataEfetivacao(new Date());
				entidade.setDataPermissao(new Date());
				entidade.setServico(110030);
				entidade.setVersaoServico(1);
				entidade.setServicoPermissao(1);
				entidade.setSituacao(SituacaoAssinaturaMultipla.PENDENTE_ASSINATURA.getRotulo());
				lista.add(entidade);
			}
		}
		
		return lista;
	}

	@Override
	public List<Mtxtb027TransacaoAssinaturaMultipla> buscarAssinaturasMultiplasPendentesNaoRelacionadas(int unidade,
			int operacao, long numero, int dv, int indicadorTipoConta, List<Long> nsuListBarramento) {
		return null;
	}

}
