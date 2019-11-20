package control;

import dao.DaoRotina;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import model.Data;
import model.Rotina;
import org.hibernate.HibernateException;

@ManagedBean (name="ctrRotina")
@SessionScoped
public class CtrManterRotina {
    DaoRotina acessoHibernateRotina;
    private Rotina rotina;
    Data data = new Data();
    
    public CtrManterRotina() {
        acessoHibernateRotina = new DaoRotina();
    }
    
    public String salvar() {
        try {
            acessoHibernateRotina.gravar(rotina);
            System.out.println("LOG SYSTEM | " + data.getCurrentTime() + " | Smart House | Rotina | Insert | Aparelho: " + rotina.getAparelhoId() + " | Data Hora: " + rotina.getDataHora() + " | Ação: " + rotina.getAcao());
            return "inc";
        } catch (HibernateException e) {
            e.printStackTrace();
            return "falha";
        }
    }
    
    public List getRotinas() {
        try {
            return acessoHibernateRotina.carregarTudoOrdenado(Rotina.class,"rotinaId");
        } catch (HibernateException e) {
                return new ArrayList();
        }
    }
    
    public String excluir() {
        try {
            acessoHibernateRotina.excluir(rotina);
            System.out.println("LOG SYSTEM | " + data.getCurrentTime() + " | Smart House | Rotina | Delete | Aparelho: " + rotina.getAparelhoId() + " | Data Hora: " + rotina.getDataHora() + " | Ação: " + rotina.getAcao());
            return "exc";
        } catch (HibernateException e) {
            e.printStackTrace();
            return "falha";
        }
    }
    
    public String alterar() {
        try {
            acessoHibernateRotina.alterar(rotina);
            System.out.println("LOG SYSTEM | " + data.getCurrentTime() + " | Smart House | Aparelho | Update | Aparelho: " + rotina.getAparelhoId() + " | Data Hora: " + rotina.getDataHora() + " | Ação: " + rotina.getAcao());
            return "alt";
        } catch (HibernateException e) {
            e.printStackTrace();
            return "falha";
        }
    }
    
    /**
     * @return the aparelho
     */
    public Rotina getRotina() {
        return rotina;
    }

    /**
     * @param pessoa the pessoa to set
     */
    public void setRotina(Rotina rotina) {
        this.rotina = rotina;
    }
    
}
