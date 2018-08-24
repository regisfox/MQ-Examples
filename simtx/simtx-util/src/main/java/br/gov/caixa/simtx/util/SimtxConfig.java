/*******************************************************************************
 * Copyright (C)  2017 - CAIXA Econômica Federal 
 * 
 * Todos os direitos reservados
 ******************************************************************************/
package br.gov.caixa.simtx.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.Properties;

import javax.ejb.Singleton;

import br.gov.caixa.simtx.persistencia.constante.Constantes;


@Singleton
public class SimtxConfig implements Serializable {

    private static final long serialVersionUID = 1L;
    
    protected String home;
    
    protected String xsltPath;
    
    protected String xsdPath;

    protected Properties canalRules;


    
    public SimtxConfig() {
        this.home = System.getProperty("pasta.simtx.home");
        if (this.home == null) {
        	throw new NullPointerException("Variavel pasta.simtx.home nao informada");
        }

        this.xsdPath = this.home + "/xsd/";
        this.xsltPath = this.home + "/xslt/";
    }

    public String getHome() {
        return this.home;
    }

    /**
     * Gets the xstl path.
     *
     * @param fileName the file name
     * @return the xstl path
     */
    public String getXstlPath(String fileName) {
        StringBuilder sb = new StringBuilder();
        sb.append(this.xsltPath).append("/").append(fileName);
        return sb.toString();
    }

    /**
     * Gets the prop canal protocolo.
     *
     * @return the prop canal protocolo
     * @throws IOException 
     */
    public Properties getPropCanalProtocolo() throws IOException {
        File popFile = new File(this.home + Constantes.PROP_CANAIS);
        FileInputStream stream = new FileInputStream(popFile);
        Properties prop = new Properties();
        prop.load(stream);
        stream.close();
        return prop;
    }

    /**
     * Gets the prop arquivos.
     *
     * @return the prop arquivos
     * @throws IOException 
     */
    public Properties getPropArquivos() throws IOException {
        File popFile = new File(this.home + Constantes.PROP_ARQUIVOS);
        FileInputStream stream = new FileInputStream(popFile);
        Properties prop = new Properties();
        prop.load(stream);
        stream.close();
        return prop;
    }

    public String getCaminhoXsd() {
        return this.xsdPath;
    }

    public String getCaminhoXslt() {
        return this.xsltPath;
    }

    public String getSimtxHome() {
        return this.home;
    }
}
