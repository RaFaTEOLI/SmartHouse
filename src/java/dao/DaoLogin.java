package dao;

import javax.persistence.Query;
import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class DaoLogin extends DaoGenerico {
    public DaoLogin() {
        
    }
    
    public Long validarLogin(String usuario, String senha) throws HibernateException {
        List l;
        Session session = hibernateConfiguracao.openSession();
        Transaction transaction = session.beginTransaction();
        
        //Query query = (Query) session.createQuery("SELECT COUNT(*) FROM Pessoa WHERE usuario = '" + usuario + "' AND senha = '" + senha + "'");
        org.hibernate.Query query = session.createQuery("SELECT COUNT(*) FROM Pessoa WHERE usuario = '" + usuario + "' AND senha = '" + senha + "'");
        System.out.println("LOG STATUS | Selecionando se usuário existe no banco... ");
        System.out.println("LOG QUERY | " + query);
        System.out.println("LOG RETURN | " + (Long) query.uniqueResult());
        return (Long) query.uniqueResult();
    }
}
