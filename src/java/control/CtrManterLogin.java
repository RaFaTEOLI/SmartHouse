package control;

import dao.DaoLogin;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import model.Login;
import org.hibernate.HibernateException;

@ManagedBean (name="ctrLogin")
@SessionScoped
public class CtrManterLogin {
    DaoLogin daoLogin;
    private Login login;
    
    public CtrManterLogin() {
        daoLogin = new DaoLogin();
    }
    
    public String logar() {
        try {
            daoLogin.login(login);
            return "logar";
            
        } catch (HibernateException e) {
            return "falha";
        }
    }
    
}
