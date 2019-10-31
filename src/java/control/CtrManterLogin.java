package control;

import dao.DaoLogin;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import model.Login;

@ManagedBean (name="ctrLogin")
@SessionScoped
public class CtrManterLogin {
    /*private DaoLogin daoLogin = new DaoLogin();
    private Login login = new Login();*/
    
    DaoLogin daoLogin;
    private Login login;
    
    public CtrManterLogin() {
        daoLogin = new DaoLogin();
    }
    
    public String send() {
        Long rLogin = daoLogin.validarLogin(login.getUsuario(), login.getSenha());
        if (rLogin == 0) {
            login = new Login();
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Usuário não encontrado!",
                    "Erro Login!"));
            return "falha";
        } else {
            return "login";
        }
    }
    
    public Login getLogin() {
        return login;
    }

    public void setLogin(Login login) {
        this.login = login;
    }
    
}
