package dao;

import model.Account;
import model.AccountOperation;

import java.time.LocalDateTime;
import java.util.List;

public interface AccountOperationDao extends GenericDao<AccountOperation, Long>{
    List<AccountOperation> findByAccount(Account account);
    List<AccountOperation> accountOperationsBetweendDates(Account account, LocalDateTime start, LocalDateTime end);
    List<AccountOperation> mostCommonOperationType(Account account);

}
