package control;

import dao.DaoAparelho;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import model.Aparelho;
import model.Data;
import org.hibernate.HibernateException;

@ManagedBean (name="ctrAparelho")
@SessionScoped
public class CtrManterAparelho {
    DaoAparelho acessoHibernateAparelho;
    private Aparelho aparelho;
    Data data = new Data();
    
    public CtrManterAparelho() {
        acessoHibernateAparelho = new DaoAparelho();
    }
    
    public String salvar() {
        try {
            acessoHibernateAparelho.gravar(aparelho);
            System.out.println("LOG SYSTEM | " + data.getCurrentTime() + " | Smart House | Aparelho | Insert | Nome: " + aparelho.getNome() + " | Descrição: " + aparelho.getDescricao() + " | Cômodo: " + aparelho.getComodoId());
            return "inc";
        } catch (HibernateException e) {
            e.printStackTrace();
            return "falha";
        }
    }
    
    public List getAparelhos() {
        try {
            return acessoHibernateAparelho.carregarTudoOrdenado(Aparelho.class,"aparelhoId");
        } catch (HibernateException e) {
                return new ArrayList();
        }
    }
    
    public String excluir() {
        try {
            acessoHibernateAparelho.excluir(aparelho);
            System.out.println("LOG SYSTEM | " + data.getCurrentTime() + " | Smart House | Aparelho | Delete | Nome: " + aparelho.getNome() + " | Descrição: " + aparelho.getDescricao() + " | Cômodo: " + aparelho.getComodoId());
            return "exc";
        } catch (HibernateException e) {
            e.printStackTrace();
            return "falha";
        }
    }
    
    public String alterar() {
        try {
            acessoHibernateAparelho.alterar(aparelho);
            System.out.println("LOG SYSTEM | " + data.getCurrentTime() + " | Smart House | Aparelho | Update | Nome: " + aparelho.getNome() + " | Descrição: " + aparelho.getDescricao() + " | Cômodo: " + aparelho.getComodoId());
            return "alt";
        } catch (HibernateException e) {
            e.printStackTrace();
            return "falha";
        }
    }
    
    public String acionar() {
        try {
            aparelho.setStatus("Ligado");
            acessoHibernateAparelho.alterar(aparelho);
            System.out.println("LOG SYSTEM | " + data.getCurrentTime() + " | Smart House | Aparelho | Update | Nome: " + aparelho.getNome() + " | Descrição: " + aparelho.getDescricao() + " | Cômodo: " + aparelho.getComodoId() + " | Status: " + aparelho.getStatus());
            return "aparelho";
        } catch (HibernateException e) {
            e.printStackTrace();
            return "falha";
        }
    }
    
    /**
     * @return the aparelho
     */
    public Aparelho getAparelho() {
        return aparelho;
    }

    /**
     * @param pessoa the pessoa to set
     */
    public void setAparelho(Aparelho aparelho) {
        this.aparelho = aparelho;
    }
    
}
