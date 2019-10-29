package dao;

import java.util.List;
import javax.persistence.EntityManager;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

public class DaoLogin extends DaoGenerico {
    public DaoLogin() {
        
    }
    
    public Long login(Object obj) throws HibernateException {
        long i = 1;
        List l;
        Session session = hibernateConfiguracao.openSession();
        Transaction transaction = session.beginTransaction();
        System.out.println(obj);
        //Query query = session.createQuery("SELECT COUNT(*) FROM Pessoa WHERE usuario = '" + usuario + "' AND senha = '" + senha + "'");
        //System.out.println("LOG STATUS | Selecionando se usuário existe no banco... ");
        //System.out.println("LOG QUERY | " + query);
        //System.out.println("LOG RETURN | " + (Long) query.uniqueResult());
        return i;
        //return (Long) query.uniqueResult();
    }
}
