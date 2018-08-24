package br.gov.caixa.simtx.util.mock;

import java.io.File;
import java.io.Serializable;

import javax.enterprise.inject.Alternative;

import br.gov.caixa.simtx.util.SimtxConfig;

@Alternative
public class SimtxConfigMock extends SimtxConfig implements Serializable {

	public SimtxConfigMock() {
        super();
        iniciarSimtxConfig();
    }

	private static final long serialVersionUID = 1L;
	
	private SimtxConfig iniciarSimtxConfig() {

		File currentDir = new File("");
		String dirFiles = currentDir.getAbsolutePath().replace("\\simtx-manutencao", "\\docs\\simtx_home");
		SimtxConfig simtxConfig;
		
		try {
			simtxConfig = new SimtxConfig();

		} catch (Exception e) {
			System.setProperty("pasta.simtx.home", dirFiles);
			simtxConfig = new SimtxConfig();
		}

		return simtxConfig;
	}

}
