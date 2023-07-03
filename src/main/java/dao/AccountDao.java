package dao;

import model.Account;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface AccountDao extends GenericDao<Account, Long> {
    Optional<Account> findByAddress(String address, String name);
    void deposit(Long id, BigDecimal amount);
    void withdraw(Long id, BigDecimal amount);
    List<Account> findByNameAndAddress(String address, String name);
    List<Account> findByOwner(String name);
    List<Account> findByBalance(BigDecimal min, BigDecimal max);
    List<Account> findWithMostMoney();
    List<Account> findAccountsWithoutOperations();
    List<Account> accountsWithMostOperations();
}
