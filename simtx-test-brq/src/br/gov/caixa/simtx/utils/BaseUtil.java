package br.gov.caixa.simtx.utils;

import java.io.File;

import org.apache.log4j.Logger;

public class BaseUtil {

	final static Logger logger = Logger.getLogger(BaseUtil.class);

	protected String localSimtxQueue = "local-simtx";
	protected String internetBankingDevQueue = "internetbanking-dev";
	protected String internetBankingSibarQueue = "internetbanking-sibar";
	protected String enviasmsbroker = "enviasmsbroker-sibar";

	protected String pathServicoMigradoFile = "C:\\simulacao\\simulacao\\SERVICO_MIGRADO.xml";
	
	protected File diretorioProjeto = new File("C:\\Users\\" + System.getProperty("user.name") + "\\git\\simtx");


	protected File diretorio = new File("C:\\Users\\" + System.getProperty("user.name") + "\\git\\");

	private String fileNamePath = "";

	public String getPathFile(boolean executeRecursive, File[] list, String fileName) {
		if (executeRecursive) {
			for (File f : list) {
				if (f.isDirectory()) {
					if (executeRecursive) {
						getPathFile(executeRecursive, f.listFiles(), fileName);
					} else {
						break;
					}
				} else {
					if (fileName.equals(f.getName())) {
						executeRecursive = false;
						logger.info("Caminho Arquivo Teste: \n" + f.getAbsolutePath().toString());
						fileNamePath = f.getAbsolutePath().toString();
					}
				}
			}
		}
		return fileNamePath;
	}
}
