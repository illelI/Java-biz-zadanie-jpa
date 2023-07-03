
import dao.AccountDao;
import dao.AccountDaoJpaImpl;
import dao.AccountOperationDao;
import dao.AccountOperationDaoJpaImpl;
import model.Account;
import model.AccountOperation;
import utils.OperationType;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class BankImpl implements Bank {

    //ArrayList<Account> accounts;

    private AccountDao accDao;
    private AccountOperationDaoJpaImpl operationDao;

    public BankImpl() {
        //accounts = new ArrayList<>();
        accDao = new AccountDaoJpaImpl();
        operationDao = new AccountOperationDaoJpaImpl();
    }

    public List<AccountOperation> getAccountOperations(Long id) {
        try {
            if(accDao.findById(id).isEmpty())
                throw new AccountIdException();
            return operationDao.findByAccount(accDao.findById(id).get());
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    @Override
    public Long createAccount(String name, String address) {
        /*
        for(Account acc : accounts) {
            if(acc.equals(new Account(name, address, (long) accounts.size()))) {
                return acc.getId();
            }
        }
        accounts.add(new Account(name, address, (long) accounts.size()));
        return accounts.get(accounts.size() - 1).getId();
         */
        Optional<Account> account = accDao.findByAddress(address, name);
        if(account.isPresent()) {
            return account.get().getId();
        }
        accDao.save(new Account(name, address));
        account = accDao.findByAddress(address, name);
        return account.get().getId();
    }

    @Override
    public Long findAccount(String name, String address) {
        /*
        for(Account a : accounts) {
            if(a.getName().equals(name) && a.getAddress().equals(address))
                return a.getId();
        }
        return null;
         */
        Optional<Account> account = accDao.findByAddress(address, name);
        if(account.isPresent()) {
            return account.get().getId();
        }
        return null;
    }

    @Override
    public void deposit(Long id, BigDecimal amount) {
        /*
        try {
            accounts.get(id.intValue()).setAmount(amount);
        } catch (Exception e) {
            throw new AccountIdException();
        }
         */
        if(accDao.findById(id).isEmpty())
           throw new AccountIdException();
        accDao.deposit(id, amount);
        operationDao.save(new AccountOperation(accDao.findById(id).get(), amount, OperationType.DEPOSIT));
    }

    @Override
    public BigDecimal getBalance(Long id) {
        /*
        Account acc;
        try {
            acc = accounts.get(id.intValue());
        } catch (Exception e) {
            throw new AccountIdException();
        }
        return acc.getAmount();
         */
        Optional<Account> account = accDao.findById(id);
        if (account.isEmpty())
            throw new AccountIdException();
        return account.get().getAmount();

    }

    @Override
    public void withdraw(Long id, BigDecimal amount) {
        /*
        try {
            if(accounts.size() < id.intValue()) {
                throw new AccountIdException();
            }
            if(accounts.get(id.intValue()).getAmount().compareTo(amount) == -1) {
                throw new InsufficientFundsException();
            }
            else accounts.get(id.intValue()).setAmount(amount.negate());
        }
        catch (InsufficientFundsException e) {
            throw new InsufficientFundsException();
        }
        catch (Exception e) {
            throw new AccountIdException();
        }
         */
        Optional<Account> account = accDao.findById(id);
        if(account.isEmpty())
            throw new AccountIdException();
        if(account.get().getAmount().compareTo(amount) < 0)
            throw new InsufficientFundsException();
        accDao.withdraw(account.get().getId(), amount);
        operationDao.save(new AccountOperation(accDao.findById(id).get(), amount, OperationType.WITHDRAW));
    }

    @Override
    public void transfer(Long idSource, Long idDestination, BigDecimal amount) {
        /*
        try {
            if(accounts.size() < idDestination && accounts.size() < idSource) {
                throw new AccountIdException();
            }
            withdraw(idSource, amount);
            deposit(idDestination, amount);
        }
        catch (AccountIdException e) {
            throw new AccountIdException();
        }
        catch (InsufficientFundsException e) {
            throw new InsufficientFundsException();
        }

         */
        if (accDao.findById(idSource).isEmpty() || accDao.findById(idDestination).isEmpty())
            throw new AccountIdException();
        if (accDao.findById(idSource).get().getAmount().compareTo(amount) < 0)
            throw new InsufficientFundsException();
        accDao.withdraw(idSource, amount);
        accDao.deposit(idDestination, amount);
        operationDao.save(new AccountOperation(accDao.findById(idSource).get(), amount, OperationType.TRANSFER));
    }
}
