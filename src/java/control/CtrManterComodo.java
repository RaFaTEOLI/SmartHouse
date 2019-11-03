package control;

import dao.DaoComodo;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import model.Comodo;
import org.hibernate.HibernateException;

@ManagedBean (name="ctrComodo")
@SessionScoped
public class CtrManterComodo {
    DaoComodo acessoHibernateQuarto;
    private Comodo comodo;
    
    public CtrManterComodo() {
        acessoHibernateQuarto = new DaoComodo();
    }
    
    public String salvar() {
        try {
            acessoHibernateQuarto.gravar(comodo);
            return "inc";
        } catch (HibernateException e) {
            e.printStackTrace();
            return "falha";
        }
    }
    
    public List getComodos() {
        try {
            return acessoHibernateQuarto.carregarTudoOrdenado(Comodo.class,
"nome");
        } catch (HibernateException e) {
            return new ArrayList();
        }
    }
    
    public String excluir() {
        try {
            acessoHibernateQuarto.excluir(comodo);
            return "exc";
        } catch (HibernateException e) {
            e.printStackTrace();
            return "falha";
        }
    }
    
    public String alterar() {
        try {
            acessoHibernateQuarto.alterar(comodo);
            return "alt";
        } catch (HibernateException e) {
            e.printStackTrace();
            return "falha";
        }
    }
    
    /**
     * @return the comodo
     */
    public Comodo getComodo() {
        return comodo;
    }

    /**
     * @param comodo the comodo to set
     */
    public void setComodo(Comodo comodo) {
        this.comodo = comodo;
    }
    
}
