package control;

import dao.DaoPessoa;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import model.Pessoa;
import org.hibernate.HibernateException;

@ManagedBean (name="ctrPessoa")
@SessionScoped
public class CtrManterPessoa {
    DaoPessoa daoPessoa;
    private Pessoa pessoa;
    
    public CtrManterPessoa() {
        daoPessoa = new DaoPessoa();
    }
    
    public String salvar() {
        try {
            daoPessoa.gravar(pessoa);
            return "incluir";
            
        } catch (HibernateException e) {
            return "falha";
        }
    }
    
    public List getPessoas() {
        try {
            return daoPessoa.carregarTudoOrdenado(Pessoa.class, "id");
        } catch (HibernateException e) {
            return new ArrayList();
        }
    }
    
    public String alterar() {
        try {
            daoPessoa.alterar(pessoa);
            return "alterar";
        } catch (HibernateException e) {
            return "falha";
        }
    }
    
    public String excluir() {
        try {
            daoPessoa.excluir(pessoa);
            return "excluir";
        } catch (HibernateException e) {
            return "falha";
        }
    }
    
    /**
     * @return the pessoa
     */
    public Pessoa getPessoa() {
        return pessoa;
    }

    /**
     * @param pessoa the pessoa to set
     */
    public void setPessoa(Pessoa pessoa) {
        this.pessoa = pessoa;
    }
    
}
