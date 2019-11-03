package control;

import dao.DaoAparelho;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import model.Aparelho;
import org.hibernate.HibernateException;

@ManagedBean (name="ctrAparelho")
@SessionScoped
public class CtrManterAparelho {
    DaoAparelho acessoHibernateAparelho;
    private Aparelho aparelho;
    
    public CtrManterAparelho() {
        acessoHibernateAparelho = new DaoAparelho();
    }
    
    public String salvar() {
        try {
            acessoHibernateAparelho.gravar(aparelho);
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
            return "exc";
        } catch (HibernateException e) {
            e.printStackTrace();
            return "falha";
        }
    }
    
    public String alterar() {
        try {
            acessoHibernateAparelho.alterar(aparelho);
            return "alt";
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
