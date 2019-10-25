package control;

import dao.DaoMorador;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import model.Morador;
import org.hibernate.HibernateException;

@ManagedBean (name="ctrMorador")
@SessionScoped
public class CtrManterMorador {
    DaoMorador acessoHibernateMorador;
    private Morador morador;
    
    public CtrManterMorador() {
        acessoHibernateMorador = new DaoMorador();
    }
    
    public String salvar() {
        try {
            acessoHibernateMorador.gravar(morador);
            return "inc";
        } catch (HibernateException e) {
            e.printStackTrace();
            return "falha";
        }
    }
    
    public List getMoradores() {
        try {
            return acessoHibernateMorador.carregarTudoOrdenado(Morador.class, "moradorId");
        } catch (HibernateException e) {
            return new ArrayList();
        }
    }
    
    public String excluir() {
        try {
            acessoHibernateMorador.excluir(morador);
            return "exc";
        } catch (HibernateException e) {
            e.printStackTrace();
            return "falha";
        }
    }
    
    public String alterar() {
        try {
            acessoHibernateMorador.alterar(morador);
            return "alt";
        } catch (HibernateException e) {
            e.printStackTrace();
            return "falha";
        }
    }
}
