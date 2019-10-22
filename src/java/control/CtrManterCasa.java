package control;

import dao.DaoCasa;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import model.Casa;
import org.hibernate.HibernateException;

@ManagedBean (name="ctrCasa")
@SessionScoped
public class CtrManterCasa {
    DaoCasa daoCasa;
    private Casa casa;
    
    public CtrManterCasa() {
        daoCasa = new DaoCasa();
    }
    
    public String salvar() {
        try {
            daoCasa.gravar(casa);
            return "inc";
        } catch (HibernateException e) {
            e.printStackTrace();
            return "falha";
        }
    }
    
    public List getCasas() {
        try {
            return daoCasa.carregarTudoOrdenado(Casa.class,"nome");
        } catch (HibernateException e) {
                return null;
        }
    }
    
    public String excluir() {
        try {
            daoCasa.excluir(casa);
            return "exc";
        } catch (HibernateException e) {
            e.printStackTrace();
            return "falha";
        }
    }
    
    public String alterar() {
        try {
            daoCasa.alterar(casa);
            return "alt";
        } catch (HibernateException e) {
            e.printStackTrace();
            return "falha";
        }
    }
    
    public Casa getCasa() {
        return casa;
    }

    public void setCasa(Casa casa) {
        this.casa = casa;
    }
    
}
