package control;

import dao.DaoPessoa;
import java.util.ArrayList;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
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
        Long temUsuario = daoPessoa.validarUsuario(pessoa.getUsuario(), pessoa.getSenha());
        System.out.println("LOG STATUS | temUsuario: " + temUsuario);
        if (temUsuario != 0) {
            System.out.println("LOG STATUS | Usuário Duplicado");
            pessoa = new Pessoa();
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Usuário já existe!",
                    "Erro na inserção!"));
            return "usuarioDuplicado";
        } else {
            try {
                daoPessoa.gravar(pessoa);
                System.out.println("LOG SYSTEM | Smart House | Pessoa | Insert | Nome: " + pessoa.getUsuario() + " | Sobrenome: " + pessoa.getSobrenome() + " | Usuário: " + pessoa.getUsuario() + " | Senha: " + pessoa.getSenha());
                return "inc";

            } catch (HibernateException e) {
                return "falha";
            }
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
            System.out.println("LOG SYSTEM | Smart House | Pessoa | Update | Nome: " + pessoa.getUsuario() + " | Sobrenome: " + pessoa.getSobrenome() + " | Usuário: " + pessoa.getUsuario() + " | Senha: " + pessoa.getSenha());
            return "alt";
        } catch (HibernateException e) {
            return "falha";
        }
    }
    
    public String excluir() {
        Long casas = daoPessoa.validarProprietario(pessoa.getId());
        System.out.println("LOG STATUS | Quantidade de casas encontradas: " + casas);
        if (casas > 0) {
            pessoa = new Pessoa();
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Você não pode excluir essa pessoa, pois ela é proprietária de " + casas + " casas!",
                    "Erro na Exclusão!"));
            return "usuarioProprietario";
        } else {
            try {
                daoPessoa.excluir(pessoa);
                System.out.println("LOG SYSTEM | Smart House | Pessoa | Delete | Nome: " + pessoa.getUsuario() + " | Sobrenome: " + pessoa.getSobrenome() + " | Usuário: " + pessoa.getUsuario() + " | Senha: " + pessoa.getSenha());
                return "exc";
            } catch (HibernateException e) {
                return "falha";
            }
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
