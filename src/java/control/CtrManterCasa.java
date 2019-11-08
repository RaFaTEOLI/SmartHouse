package control;

import dao.DaoCasa;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import model.Casa;
import model.Data;
import org.hibernate.HibernateException;

@ManagedBean (name="ctrCasa")
@SessionScoped
public class CtrManterCasa {
    DaoCasa daoCasa;
    private Casa casa;
    Data data = new Data();
    
    public CtrManterCasa() {
        daoCasa = new DaoCasa();
    }
    
    public String salvar() {
        try {
            daoCasa.gravar(casa);
            System.out.println("LOG SYSTEM | " + data.getCurrentTime() + " | Smart House | Casa | Insert | Nome: " + casa.getNome() + " | Endereço: " + casa.getEndereco() + " | Cidade: " + casa.getCidade() + " | CEP: " + casa.getCep() + " | Proprietário: " + casa.getPessoa());
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
            System.out.println("LOG SYSTEM | " + data.getCurrentTime() + " | Smart House | Casa | Delete | Nome: " + casa.getNome() + " | Endereço: " + casa.getEndereco() + " | Cidade: " + casa.getCidade() + " | CEP: " + casa.getCep() + " | Proprietário: " + casa.getPessoa());
            return "exc";
        } catch (HibernateException e) {
            e.printStackTrace();
            return "falha";
        }
    }
    
    public String alterar() {
        try {
            daoCasa.alterar(casa);
            System.out.println("LOG SYSTEM | " + data.getCurrentTime() + " | Smart House | Casa | Update | Nome: " + casa.getNome() + " | Endereço: " + casa.getEndereco() + " | Cidade: " + casa.getCidade() + " | CEP: " + casa.getCep() + " | Proprietário: " + casa.getPessoa());
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
