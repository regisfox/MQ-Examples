/*******************************************************************************
 * Copyright (C)  2017 - CAIXA Econômica Federal 
 * 
 * Todos os direitos reservados
 ******************************************************************************/
package br.gov.caixa.simtx.persistencia.assinaturamultipla.entidade;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import br.gov.caixa.simtx.persistencia.assinaturamultipla.vo.SituacaoAssinaturaMultipla;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb011VersaoServico;

@Entity
@Table(name = "MTXTB027_TRNSO_ASNTA_MLTPA")
@NamedQueries({
		@NamedQuery(name = "Mtxtb027TransacaoAssinaturaMultipla.buscarAssinaturasMultiplasPendentes", query = "SELECT am FROM Mtxtb027TransacaoAssinaturaMultipla am where am.unidade = :unidade and am.produto = :produto and am.conta = :numeroConta and am.dvConta = :dv and am.indicadorTipoConta = :tipoConta and am.situacao = :situacao"),
		@NamedQuery(name = "Mtxtb027TransacaoAssinaturaMultipla.buscarAssinaturasMultiplasPendentesNaoRelacionadas", query = "SELECT am FROM Mtxtb027TransacaoAssinaturaMultipla am where am.unidade = :unidade and am.produto = :produto and am.conta = :numeroConta and am.dvConta = :dv and am.indicadorTipoConta = :tipoConta and am.situacao = :situacao and am.nsuAssinaturaMultipla not in( :nsuList)")})
public class Mtxtb027TransacaoAssinaturaMultipla implements Serializable {

    private static final long serialVersionUID = 4781668645781789286L;

    public static final int INDICADOR_TIPO_CONTA_SIDEC = 1;
    public static final int INDICADOR_TIPO_CONTA_NSGD = 2;

    @Id
    @Column(name = "NU_NSU_TRNSO_ASNTA_MLTPA", unique = true, nullable = false, length = 15)
    private long nsuAssinaturaMultipla;

    @Column(name = "NU_NSU_PERMISSAO", nullable = false, length = 9)
    private long nsuPermissao;

    @Column(name = "DT_PERMISSAO", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date dataPermissao;

    @Column(name = "NU_UNIDADE", nullable = false, length = 4)
    private int unidade;

    @Column(name = "NU_PRODUTO", nullable = false, length = 4)
    private int produto;

    @Column(name = "NU_CONTA", nullable = false, length = 12)
    private long conta;

    @Column(name = "NU_DV_CONTA", nullable = false, length = 1)
    private int dvConta;

    @Column(name = "IC_CONTA_SOLUCAO", nullable = false, length = 1)
    private int indicadorTipoConta;

    @Column(name = "IC_STCO_TRNSO_ASNTA_MLTPA", nullable = false, length = 2)
    private String situacao;

    @Column(name = "VR_TRANSACAO", nullable = false, length = 14, precision = 2)
    private BigDecimal valor;

    @Lob
    @Column(name = "DE_XML_NEGOCIAL", nullable = false)
    private String xmlNegocial;

    @Column(name = "DT_LIMITE_EFETIVACAO", nullable = true)
    @Temporal(TemporalType.DATE)
    private Date dataEfetivacao;

    @Column(name = "NU_SERVICO", nullable = false, length = 6)
    private long servico;

    @Column(name = "NU_VERSAO_SERVICO", nullable = false, length = 3)
    private long versaoServico;

    @Column(name = "NU_SERVICO_PERMISSAO", nullable = false, length = 3)
    private int servicoPermissao;

    @OneToOne
    @JoinColumns({
            @JoinColumn(name = "NU_SERVICO", referencedColumnName = "NU_SERVICO_001", nullable = false,
                    insertable = false, updatable = false),
            @JoinColumn(name = "NU_VERSAO_SERVICO", referencedColumnName = "NU_VERSAO_SERVICO", nullable = false,
                    insertable = false, updatable = false) })
    private Mtxtb011VersaoServico mtxtb011VersaoServico;

    public long getNsuAssinaturaMultipla() {
        return nsuAssinaturaMultipla;
    }

    public void setNsuAssinaturaMultipla(long nsuAssinaturaMultipla) {
        this.nsuAssinaturaMultipla = nsuAssinaturaMultipla;
    }

    public long getNsuPermissao() {
        return nsuPermissao;
    }

    public void setNsuPermissao(long nsuPermissao) {
        this.nsuPermissao = nsuPermissao;
    }

    public Date getDataPermissao() {
        return dataPermissao;
    }

    public void setDataPermissao(Date dataPermissao) {
        this.dataPermissao = dataPermissao;
    }

    public long getConta() {
        return conta;
    }

    public void setConta(long conta) {
        this.conta = conta;
    }

    public SituacaoAssinaturaMultipla getSituacao() {
        if (this.situacao.equals(SituacaoAssinaturaMultipla.ASSINADA.getRotulo())) {
            return SituacaoAssinaturaMultipla.ASSINADA;
        } else if (this.situacao.equals(SituacaoAssinaturaMultipla.CANCELADA.getRotulo())) {
            return SituacaoAssinaturaMultipla.CANCELADA;
        } else if (this.situacao.equals(SituacaoAssinaturaMultipla.EFETIVADA.getRotulo())) {
            return SituacaoAssinaturaMultipla.EFETIVADA;
        } else if (this.situacao.equals(SituacaoAssinaturaMultipla.PENDENTE_ASSINATURA.getRotulo())) {
            return SituacaoAssinaturaMultipla.PENDENTE_ASSINATURA;
        }

        return null;
    }

    public void setSituacao(SituacaoAssinaturaMultipla situacao) {
        this.situacao = situacao.getRotulo();
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public String getXmlNegocial() {
        return xmlNegocial;
    }

    public void setXmlNegocial(String xmlNegocial) {
        this.xmlNegocial = xmlNegocial;
    }

    public Date getDataEfetivacao() {
        return dataEfetivacao;
    }

    public void setDataEfetivacao(Date dataEfetivacao) {
        this.dataEfetivacao = dataEfetivacao;
    }

    public long getServico() {
        return servico;
    }

    public void setServico(long servico) {
        this.servico = servico;
    }

    public int getProduto() {
        return produto;
    }

    public void setProduto(int produto) {
        this.produto = produto;
    }

    public int getDvConta() {
        return dvConta;
    }

    public void setDvConta(int dvConta) {
        this.dvConta = dvConta;
    }

    public Mtxtb011VersaoServico getMtxtb011VersaoServico() {
        return mtxtb011VersaoServico;
    }

    public void setMtxtb011VersaoServico(Mtxtb011VersaoServico mtxtb011VersaoServico) {
        this.mtxtb011VersaoServico = mtxtb011VersaoServico;
    }

    public int getUnidade() {
        return unidade;
    }

    public void setUnidade(int unidade) {
        this.unidade = unidade;
    }

    public int getIndicadorTipoConta() {
        return indicadorTipoConta;
    }

    public void setIndicadorTipoConta(int indicadorTipoConta) {
        this.indicadorTipoConta = indicadorTipoConta;
    }

    public long getVersaoServico() {
        return versaoServico;
    }

    public void setVersaoServico(long versaoServico) {
        this.versaoServico = versaoServico;
    }

    public int getServicoPermissao() {
        return servicoPermissao;
    }

    public void setServicoPermissao(int servicoPermissao) {
        this.servicoPermissao = servicoPermissao;
    }

    public void setSituacao(String situacao) {
        this.situacao = situacao;
    }
}
