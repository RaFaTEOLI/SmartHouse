package control;

import dao.DaoMorador;
import java.util.ArrayList;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
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
        System.out.println("LOG STATUS | Salvando morador: " + morador.getPessoaId() + " - " + morador.getCasaId());
        Long temMorador = acessoHibernateMorador.validarMorador(morador.getPessoaId(), morador.getCasaId());
        System.out.println("LOG STATUS | temMorador: " + temMorador);
        if (temMorador > 0) {
            System.out.println("LOG STATUS | Morador Duplicado");
            morador = new Morador();
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Morador já existe!",
                    "Erro na inserção!"));
            return "moradorDuplicado";
        } else {
            try {
                acessoHibernateMorador.gravar(morador);
                return "inc";
            } catch (HibernateException e) {
                e.printStackTrace();
                return "falha";
            }
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
        Long temMorador = acessoHibernateMorador.validarMorador(morador.getPessoaId(), morador.getCasaId());
        System.out.println("LOG STATUS | temMorador: " + temMorador);
        if (temMorador > 0) {
            System.out.println("LOG STATUS | Morador Duplicado");
            morador = new Morador();
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Morador já existe!",
                    "Erro na inserção!"));
            return "moradorDuplicado";
        } else {
            try {
                acessoHibernateMorador.alterar(morador);
                return "alt";
            } catch (HibernateException e) {
                e.printStackTrace();
                return "falha";
            }
        }
    }
    
    /**
     * @return the morador
     */
    public Morador getMorador() {
        return morador;
    }

    /**
     * @param morador the morador to set
     */
    public void setMorador(Morador morador) {
        this.morador = morador;
    }
}
