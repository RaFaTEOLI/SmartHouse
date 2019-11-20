package model;

import java.io.Serializable;
import java.util.Date;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;

@ManagedBean (name="rotina")
@SessionScoped
@Entity
public class Rotina implements Serializable {
   
    @Id
    @Column(unique=true)
    @GeneratedValue (strategy=GenerationType.IDENTITY)
    private Integer rotinaId;
    
    @ManyToOne
    @JoinColumn(name="aparelhoId")
    private Aparelho aparelhoId;
    
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date dataHora;
    
    private boolean acao;
    private String descricao;
    
    public Rotina() {
    }
    
    /**
     * @return the rotinaId
     */
    public Integer getRotinaId() {
        return rotinaId;
    }

    /**
     * @param rotinaId the rotinaId to set
     */
    public void setRotinaId(Integer rotinaId) {
        this.rotinaId = rotinaId;
    }

    /**
     * @return the aparelhoId
     */
    public Aparelho getAparelhoId() {
        return aparelhoId;
    }

    /**
     * @param aparelhoId the aparelhoId to set
     */
    public void setAparelhoId(Aparelho aparelhoId) {
        this.aparelhoId = aparelhoId;
    }

    /**
     * @return the dataHora
     */
    public Date getDataHora() {
        return dataHora;
    }

    /**
     * @param dataHora the dataHora to set
     */
    public void setDataHora(Date dataHora) {
        this.dataHora = dataHora;
    }

    /**
     * @return the acao
     */
    public Boolean getAcao() {
        return acao;
    }

    /**
     * @param acao the acao to set
     */
    public void setAcao(Boolean acao) {
        this.acao = acao;
    }

    /**
     * @return the descricao
     */
    public String getDescricao() {
        return descricao;
    }

    /**
     * @param descricao the descricao to set
     */
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
    
    @Override
    public String toString() {
        return this.getRotinaId() + " - " + this.getDescricao();
    }
    
    @Override
    public boolean equals(Object obj) {
        boolean result = false;
        if (this.getRotinaId().equals(((Rotina)obj).getRotinaId())) {
            result = true;
        }
        return result;
    }
}
