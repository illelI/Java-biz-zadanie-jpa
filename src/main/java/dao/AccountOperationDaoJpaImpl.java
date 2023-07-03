package dao;

import jakarta.persistence.TypedQuery;
import model.Account;
import model.AccountOperation;
import org.hibernate.Session;
import utils.JpaFactory;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class AccountOperationDaoJpaImpl extends GenericDaoJpaImpl<AccountOperation, Long> implements AccountOperationDao {
    @Override
    public List<AccountOperation> findByAccount(Account account) {
        List<AccountOperation> list;
        try (Session session = JpaFactory.getSessionFactory().openSession()) {
            TypedQuery<AccountOperation> query = session.createQuery("SELECT a FROM AccountOperation a WHERE a.account = :account", AccountOperation.class)
                    .setParameter("account", account);
            list = query.getResultList();
        }
        return list;
    }

    @Override
    public List<AccountOperation> accountOperationsBetweendDates(Account account, LocalDateTime start, LocalDateTime end) {
        Session session = JpaFactory.getSessionFactory().openSession();
        TypedQuery<AccountOperation> query = session.createNamedQuery("AccountOperation.accountOperationsBetweendDates", AccountOperation.class)
                .setParameter(1, account).setParameter(2, start).setParameter(3, end);
        List<AccountOperation> list = query.getResultList();
        session.close();
        return list;
    }

    @Override
    public List<AccountOperation> mostCommonOperationType(Account account) {
        Session session = JpaFactory.getSessionFactory().openSession();
        TypedQuery<AccountOperation> query = session.createNamedQuery("AccountOperation.mostCommonOperationType", AccountOperation.class)
                .setParameter(1, account);
        List<AccountOperation> list = query.getResultList();
        session.close();
        return list;
    }

}
