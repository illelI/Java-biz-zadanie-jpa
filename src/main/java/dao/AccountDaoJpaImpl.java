package dao;

import jakarta.persistence.TypedQuery;
import model.Account;
import org.hibernate.Session;
import utils.JpaFactory;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public class AccountDaoJpaImpl extends GenericDaoJpaImpl<Account, Long> implements AccountDao {
    public Optional<Account> findByAddress(String address, String name) {
        Account dto;
        try (Session session = JpaFactory.getSessionFactory().openSession()) {
            TypedQuery<Account> query = session.createQuery("SELECT a FROM Account a WHERE a.address = :address AND a.name = :name", Account.class)
                .setParameter("address", address).setParameter("name", name);
        try{
            dto = query.getSingleResult();
        } catch (Exception e) {
            dto = null;
        }
        }
        return Optional.ofNullable(dto);
    }

    public List<Account> findByNameAndAddress(String address, String name) {
        Session session = JpaFactory.getSessionFactory().openSession();
        TypedQuery<Account> query = session.createNamedQuery("Account.findByOwnerAndAddress", Account.class)
                .setParameter(1, address).setParameter(2, name);
        List<Account> list = query.getResultList();
        session.close();
        return list;
    }

    public List<Account> findByOwner(String name) {
        Session session = JpaFactory.getSessionFactory().openSession();
        TypedQuery<Account> query = session.createNamedQuery("Account.findByOwner", Account.class)
                .setParameter("name", name + "%");
        List<Account> list = query.getResultList();
        session.close();
        return list;
    }

    public List<Account> findByBalance(BigDecimal min, BigDecimal max) {
        Session session = JpaFactory.getSessionFactory().openSession();
        TypedQuery<Account> query = session.createNamedQuery("Account.findByBalanceBetween", Account.class)
                .setParameter(1, min).setParameter(2, max);
        List<Account> list = query.getResultList();
        session.close();
        return list;
    }

    public List<Account> findWithMostMoney() {
        Session session = JpaFactory.getSessionFactory().openSession();
        TypedQuery<Account> query = session.createNamedQuery("Account.findWithMostMoney", Account.class);
        List<Account> list = query.getResultList();
        session.close();
        return list;
    }

    @Override
    public List<Account> findAccountsWithoutOperations() {
        Session session = JpaFactory.getSessionFactory().openSession();
        TypedQuery<Account> query = session.createNamedQuery("Account.findAccountsWithoutOperations", Account.class);
        List<Account> list = query.getResultList();
        session.close();
        return list;
    }

    @Override
    public List<Account> accountsWithMostOperations() {
        Session session = JpaFactory.getSessionFactory().openSession();
        TypedQuery<Account> query = session.createNamedQuery("Account.accountsWithMostOperations", Account.class);
        List<Account> list = query.getResultList();
        session.close();
        return list;
    }


    @Override
    public void deposit(Long id, BigDecimal amount) {
        Optional<Account> account = findById(id);
        if (account.isPresent()) {
            account.get().setAmount(amount);
            update(account.get());
        }
    }

    @Override
    public void withdraw(Long id, BigDecimal amount) {
        Optional<Account> account = findById(id);
        if (account.isPresent()) {
            account.get().setAmount(amount.negate());
            update(account.get());
        }
    }



}
