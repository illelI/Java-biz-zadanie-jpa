import dao.AccountDaoJpaImpl;
import dao.AccountOperationDaoJpaImpl;
import model.Account;
import model.AccountOperation;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

public class BankTest {
    @Test
    void createAccountTest() {
        BankImpl bank = new BankImpl();
        Long id1 = bank.createAccount("a", "a");
        Long id2 = bank.createAccount("b", "b");
        Long id3 = bank.createAccount("c", "c");
        Long id4 = bank.createAccount("a", "a");
        assert id1.compareTo(bank.createAccount("a", "a")) == 0 ;
        assert id2.compareTo(bank.createAccount("b", "b")) == 0;
        assert id3.compareTo(bank.createAccount("c", "c")) == 0;
        assert id1.compareTo(id4) == 0;
    }
    @Test
    void findAccountTest() {
        BankImpl bank = new BankImpl();
        bank.createAccount("a", "a");
        assert bank.findAccount("a", "a") != null;
        assert bank.findAccount("b", "b") == null;
    }
    @Test
    void depositTest() {
        BankImpl bank = new BankImpl();
        Long id = bank.createAccount("a", "a");
        bank.deposit(id, BigDecimal.valueOf(10));
        List<AccountOperation> list = bank.getAccountOperations(id);
        assert list.size() == 1;
        //assert bank.accounts.get(0).getAmount().equals(BigDecimal.valueOf(10));
        assert bank.getBalance(id).compareTo(BigDecimal.valueOf(10)) == 0;
        Assertions.assertThrows(Bank.AccountIdException.class, () -> bank.deposit(100L, BigDecimal.valueOf(10)));
    }
    @Test
    void getBalanceTest() {
        BankImpl bank = new BankImpl();
        bank.createAccount("z", "z");
        Long accId = bank.findAccount("z", "z");
        bank.deposit(accId, BigDecimal.valueOf(10));
        assert bank.getBalance(accId).compareTo(BigDecimal.valueOf(10)) == 0;
        Assertions.assertThrows(Bank.AccountIdException.class, () -> bank.getBalance(100L));
    }
    @Test
    void withdrawTest() {
        BankImpl bank = new BankImpl();
        bank.createAccount("f", "f");
        Long id = bank.findAccount("f", "f");
        bank.deposit(id, BigDecimal.valueOf(10));
        bank.withdraw(id, BigDecimal.valueOf(5));
        assert bank.getBalance(id).compareTo(BigDecimal.valueOf(5)) == 0;
        Assertions.assertThrows(Bank.AccountIdException.class, () -> bank.getBalance(200L));
        Assertions.assertThrows(Bank.InsufficientFundsException.class, () -> bank.withdraw(id, BigDecimal.valueOf(10)));
    }
    @Test
    void transferTest() {
        BankImpl bank = new BankImpl();
        Long yId = bank.createAccount("y", "y");
        bank.deposit(yId, BigDecimal.valueOf(10));
        Long xId = bank.createAccount("x", "x");
        bank.transfer(yId, xId, BigDecimal.valueOf(5));
        assert bank.getBalance(yId).compareTo(BigDecimal.valueOf(5)) == 0;
        assert bank.getBalance(xId).compareTo(BigDecimal.valueOf(5)) == 0;
        Assertions.assertThrows(Bank.InsufficientFundsException.class, () -> bank.transfer(yId, xId, BigDecimal.valueOf(10)));
        Assertions.assertThrows(Bank.AccountIdException.class, () -> bank.transfer(100L, yId, BigDecimal.valueOf(5)));
        Assertions.assertThrows(Bank.AccountIdException.class, () -> bank.transfer(yId, 100L, BigDecimal.valueOf(5)));
    }

    @Test
    void accountDaoJpqlTest() {
        AccountDaoJpaImpl dao = new AccountDaoJpaImpl();
        BankImpl bank = new BankImpl();
        dao.save(new Account("qw", "qw"));
        assert dao.findByNameAndAddress("qw", "qw").size() == 1;
        dao.save(new Account("q", "q"));
        assert dao.findByOwner("q").size() == 2;
        dao.save(new Account("zz", "zz"));
        bank.deposit(bank.findAccount("q", "q"), BigDecimal.valueOf(10));
        bank.deposit(bank.findAccount("qw", "qw"), BigDecimal.valueOf(15));
        assert dao.findByBalance(BigDecimal.valueOf(5), BigDecimal.valueOf(20)).size() == 2;
        assert dao.findWithMostMoney().get(0).getAmount().compareTo(BigDecimal.valueOf(15)) == 0;
        assert dao.findAccountsWithoutOperations().get(0).getName().equals("zz");
        assert dao.findAccountsWithoutOperations().size() == 1;
        bank.deposit(bank.findAccount("qw", "qw"), BigDecimal.valueOf(5));
        //List<Account> accounts = dao.accountsWithMostOperations();
        //for (Account account : accounts) {
       //     System.out.println(account.getName());
        //}
        //assert dao.accountsWithMostOperations().get(0).getName().equals("qw");
    }


    @BeforeEach
    void clean() {
        AccountDaoJpaImpl dao = new AccountDaoJpaImpl();
        AccountOperationDaoJpaImpl operationDao = new AccountOperationDaoJpaImpl();
        List<AccountOperation> operations = operationDao.findAll();
        for(AccountOperation operation : operations) {
            operationDao.delete(operation);
        }
        List<Account> accounts = dao.findAll();
        for(Account acc : accounts) {
            dao.delete(acc);
        }
    }

}
