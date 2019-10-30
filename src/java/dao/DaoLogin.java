package dao;

import java.util.Properties;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import model.Login;

public class DaoLogin extends DaoGenerico {
    public DaoLogin() {
        
    }
    
    private EntityManagerFactory factory = Persistence.createEntityManagerFactory("smart_house");
    private EntityManager em = factory.createEntityManager();
    
    public Login getLogin(String nameUser, String password) {
        try {
            Login login = (Login) em.createQuery("SELECT u FROM Pessoa u WHERE u.usuario = :name AND u.senha = :password")
                    .setParameter("name", nameUser)
                    .setParameter("password", password).getSingleResult();
                    return login;
        } catch (NoResultException e) {
            return null;
        }
    }
    
}
