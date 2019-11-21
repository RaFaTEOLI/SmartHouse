package control;

import com.sun.faces.action.RequestMapping;
import dao.DaoLogin;
import java.io.IOException;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
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
    
    @RequestMapping("send")
    public String send(HttpSession session) {
        Long rLogin = daoLogin.validarLogin(login.getUsuario(), login.getSenha());
        if (rLogin == 0) {
            login = new Login();
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Usuário não encontrado!",
                    "Erro Login!"));
            return "falha";
        } else {
            session.setAttribute("usuarioLogado", login.getUsuario());
            return "login";
        }
    }
    
    @RequestMapping("logout")
    public String logout(HttpSession session) {
        if (session != null) {
            session.removeAttribute("usuarioLogado");
        }
        return "logout";
    }
    
    public Login getLogin() {
        return login;
    }

    public void setLogin(Login login) {
        this.login = login;
    }
    
}
