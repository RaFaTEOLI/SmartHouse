package model;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
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
    
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
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
//    public String getDataHora() {
//        //SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
//        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
//        String dateFormatada = df.format(dataHora);
//        //return formato.format(dataHora);
//        return dateFormatada;
//    }
    
    public Date getDataHora() {
        return dataHora;
    }

//    public void setDataHora(String dataHora) {
//        Date date1 = null;
//        try {
//            date1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dataHora);
//        } catch (ParseException ex) {
//            Logger.getLogger(Rotina.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        System.out.println("DATA: " + date1);
//        this.dataHora = date1;
//    }
    public void setDataHora(Date dataHora) {
        this.dataHora = dataHora;
    }
    
    public String getSeveridade() {
        String severidade;
        if (getAcao().equals("Ligar")) {
            severidade = "warning";
        } else {
            severidade = "danger";
        }
        return severidade;
    }

    /**
     * @return the acao
     */
    /*public Boolean getAcao() {
        return acao;
    }*/
    
    public String getAcao() {
        String sAcao = "";
        if (acao == true) {
            sAcao = "Ligar";
        } else {
            sAcao = "Desligar";
        }
        return sAcao;
    }

    /**
     * @param acao the acao to set
     */
    /*public void setAcao(Boolean acao) {
        this.acao = acao;
    }*/
    
    public void setAcao(String sAcao) {
        if ("Ligar".equals(sAcao)) {
            acao = true;
        } else if ("Desligar".equals(sAcao)) {
            acao = false;
        }
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
