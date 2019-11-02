package dao;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class DaoPessoa extends DaoGenerico {
    public DaoPessoa() {
        
    }
    public Long validarUsuario(String usuario, String senha) throws HibernateException {
        Session session = hibernateConfiguracao.openSession();
        Transaction transaction = session.beginTransaction();
        
        org.hibernate.Query query = session.createQuery("SELECT COUNT(*) FROM Pessoa WHERE usuario = '" + usuario + "' AND senha = '" + senha + "'");
        System.out.println("LOG STATUS | Selecionando se usuário existe no banco... ");
        System.out.println("LOG QUERY | " + query);
        System.out.println("LOG RETURN | " + (Long) query.uniqueResult());
        return (Long) query.uniqueResult();
    }
    
    public Long validarProprietario(Integer proprietarioId) throws HibernateException {
        Session session = hibernateConfiguracao.openSession();
        Transaction transaction = session.beginTransaction();
        
        org.hibernate.Query query = session.createQuery("SELECT COUNT(*) FROM Casa WHERE proprietarioId = '" + proprietarioId + "'");
        System.out.println("LOG STATUS | Selecionando se a Pessoa: " + proprietarioId + " é dono de alguma casa... ");
        System.out.println("LOG QUERY | " + query);
        System.out.println("LOG RETURN | " + (Long) query.uniqueResult());
        return (Long) query.uniqueResult();
    }
}
