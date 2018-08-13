package br.gov.caixa.simtx.test.simtx;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.junit.Test;

import com.google.gson.Gson;

import br.gov.caixa.simtx.vo.MtxTransacaoContaEntrada;

public class TestCancelamentoAgendamento {

	@Test
	public void testarWsCancelamentoAgendamento() throws IOException {
//		deletarArquivoSimulador("SERVICO_MIGRADO.xml");
//		alteraArquivoSimulador("SERVICO_MIGRADO_LIMITE_DESFAZIMENTO.xml", "SERVICO_MIGRADO.xml");
		
//		System.out.println("Chamando ws");
//		String contexto = "http://localhost:8080/simtx-web-api";
//		String token = getToken(contexto);
//		System.out.println("token: " + token); 
//		
//		String resp = cancelarPagamento(contexto,token);
	
		
	

		//CONSULTA CANAIS
//		String respCanais = consultaCanais(contexto,token);
//		System.out.println(respCanais);
		
		
		
		
//		String resp = cancelarAgendamento(contexto, token);
//		String resp = montaRequisicao("");
		montaRequisicao2("");

//		System.out.println(resp);
		
		
	}
	
	String JSON_C = "{" + 
			"  \"nsuTransacaoOrigem\": 17627," + 
			"  \"numServicoFinal\": 110039," + 
			"  \"numServicoVersaoFinal\": 1," + 
			"  \"ultimaExecucao\": false" + 
			"}";
	
	private String montaRequisicao(String dadosJson) {
		String resp = null;
		try {
			resp = ClientBuilder.newClient()
			          .target("http://localhost:8080/roteador/restful")
			          .path("/efetivaAgendamento/realizaEnvio")
			          .request(MediaType.APPLICATION_JSON)
			          .post(Entity.json(JSON_C)).readEntity(String.class);
		} catch (Throwable e) {
			System.out.println(e.getMessage());
		}
		
		return resp;
	}
	
	
	private void montaRequisicao2(String dadosJson) {
		try {
			Future<Response> futureResponse = ClientBuilder.newClient().target("http://localhost:8080/roteador/restful/")
					.path("efetivaAgendamento/realizaEnvioAsynchronous").request().async().post(Entity.json(JSON_C));

			Response resp = futureResponse.get();
			
			System.out.println(resp.getStatus());
			
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}

	}
	
	public String cancelarAgendamento(String contexto, String token) {
		String resp = ClientBuilder.newClient()
		          .target(contexto)
		          .path("/web/sessao/cancelamento/agendamento")
		          .request(MediaType.APPLICATION_JSON)
		          .header("Authorization", token)
		          .post(Entity.json(JSON_CANCELAR)).readEntity(String.class);
		
		return resp;
	}
	
	public String consultaCanais(String contexto, String token) {
		String resp = ClientBuilder.newClient()
		          .target(contexto)
		          .path("/web/sessao/canal/consulta")
		          .request(MediaType.APPLICATION_JSON)
		          .header("Authorization", token)
		          .get(String.class);
		
		return resp.replace("},{", "},\n{");
	}
	
	public String getToken(String contexto) {
		String resp = ClientBuilder.newClient()
		          .target(contexto)
		          .path("/web/login")
		          .request(MediaType.APPLICATION_JSON)
		          .post(Entity.json(JSON_LOGIN)).readEntity(String.class);
		
		Usuario u = new Gson().fromJson(resp, Usuario.class);
		return u.getToken();
	}
	
	public String cancelarPagamentoTransacional(String contexto, String token) {
		String resp = ClientBuilder.newClient()
		          .target(contexto)
		          .path("/web/sessao/cancelamento/efetiva")
		          .request(MediaType.APPLICATION_JSON)
		          .header("Authorization", token)
		          .post(Entity.json(JSON_LOGIN)).readEntity(String.class);
		
		return resp;
	}
	
	public String cancelarPagamento(String contexto, String token) {
		String resp = ClientBuilder.newClient()
		          .target(contexto)
		          .path("/web/sessao/cancelamento/pagamento")
		          .request(MediaType.APPLICATION_JSON)
		          .header("Authorization", token)
		          .post(Entity.json(cancelamentoPagamento())).readEntity(String.class);
		
		return resp;
	}
	

	
	private String cancelamentoPagamento(){
		Gson gson = new Gson();
		List<MtxTransacaoContaEntrada> mtxTransacaoContaEntradas = new ArrayList<>();
		MtxTransacaoContaEntrada mtxTransacaoContaEntrada = new MtxTransacaoContaEntrada();
		mtxTransacaoContaEntrada.setNuNsuTransacaoRefMtx016(19051L);
		mtxTransacaoContaEntrada.setNuServico(110034L);
		mtxTransacaoContaEntrada.setNuCanal(106L);
		mtxTransacaoContaEntrada.setCodigoUsuario("c988065");
		mtxTransacaoContaEntrada.setCodigoMaquina("SIMTX001");
		
		MtxTransacaoContaEntrada mtxTransacaoContaEntrada2 = new MtxTransacaoContaEntrada();
		mtxTransacaoContaEntrada2.setNuNsuTransacaoRefMtx016(19011L);
		mtxTransacaoContaEntrada2.setNuServico(110034L);
		mtxTransacaoContaEntrada2.setNuCanal(106L);
		mtxTransacaoContaEntrada2.setCodigoUsuario("c988065");
		mtxTransacaoContaEntrada2.setCodigoMaquina("SIMTX001");
		
		mtxTransacaoContaEntradas.add(mtxTransacaoContaEntrada);
		mtxTransacaoContaEntradas.add(mtxTransacaoContaEntrada2);

		String produtoJson = gson.toJson(mtxTransacaoContaEntradas);
		
		System.out.println(produtoJson);
		return produtoJson;
	}

	String JSON_LOGIN = "{\r\n" + 
			"  \"nomeUsuario\": \"c988065\",\r\n" + 
			"  \"senha\": \"c988065\"\r\n" + 
			"}";
	
	String JSON_CANCELAR = " [\r\n" + 
			"	{\r\n" + 
			"		\"nsuAgendamento\": 17627,\r\n" + 
			"		\"codigoBarras\": \"789134789234587902534897534127890253\",\r\n" + 
			"		\"dataAgendamento\": \"2018-04-06\",\r\n" + 
			"		\"dataEfetivacao\": \"2018-06-04\",\r\n" + 
			"		\"valorTransacao\": \"150.00\",\r\n" + 
			"		\"codigoCanal\": 106,\r\n" + 
			"		\"codigoServico\": 110039,\r\n" + 
			"		\"versaoServico\": 1,\r\n" + 
			"		\"agencia\": 612,\r\n" + 
			"		\"operacao\": 1288,\r\n" + 
			"		\"conta\": 990010354,\r\n" + 
			"		\"dv\": 3,\r\n" + 
			"		\"tipoConta\": \"NSGD\"\r\n" + 
			"	}\r\n" + 
			"]\r\n" + 
			"";
	
	String JSON_CANCELAR_PAGAMENTO ="";
	
	class Usuario {
		public Usuario(String nome){
			this.nome = nome;
		}

		public Usuario(String nome, String grupoAcesso, String login, String token, List<String> funcionalidades){
			this.nome = nome;
			this.grupoAcesso = grupoAcesso;
			this.login = login;
		}
		
		private String nome;
		
		private String grupoAcesso;
		
		private String login;
		
		private String token;
		
		private List<String> funcionalidades;

		
		public String getNome() {
			return nome;
		}

		public void setNome(String nome) {
			this.nome = nome;
		}

	    public String getGrupoAcesso() {
	        return grupoAcesso;
	    }

	    public void setGrupoAcesso(String grupoAcesso) {
	        this.grupoAcesso = grupoAcesso;
	    }

		public String getLogin() {
			return login;
		}

		public void setLogin(String login) {
			this.login = login;
		}

		public String getToken() {
			return token;
		}

		public void setToken(String token) {
			this.token = token;
		}
		

		public List<String> getFuncionalidades() {
			return funcionalidades;
		}

		public void setFuncionalidades(List<String> funcionalidades) {
			this.funcionalidades = funcionalidades;
		}	
	}
	

	private void deletarArquivoSimulador(String arquivo) {
		try {
			new File("C:\\simtx-simulacao\\simulacao\\" + arquivo).delete();
		} catch (Throwable e) {
			System.out.println(
					"Nao foi possivel apagar arquivo " + arquivo + " no simulador (talvez ja nao estivesse la)");
		}
	}
	
	private void alteraArquivoSimulador(String source, String dest) throws IOException {
		InputStream is = null;
		OutputStream os = null;
		try {
			is = new FileInputStream("C:\\simtx-simulacao\\simulacao\\" + source);
			os = new FileOutputStream("C:\\simtx-simulacao\\simulacao\\" + dest);
			byte[] buffer = new byte[1024];
			int length;
			while ((length = is.read(buffer)) > 0) {
				os.write(buffer, 0, length);
			}
		} finally {
			is.close();
			os.close();
		}
	}

}
