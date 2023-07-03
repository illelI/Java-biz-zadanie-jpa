package model;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import utils.OperationType;

import java.math.BigDecimal;

@Entity
@NamedQueries({
        @NamedQuery(name = "AccountOperation.accountOperationsBetweendDates",
                query = "SELECT ao FROM AccountOperation ao WHERE ao.account.id =?1 AND ao.creationDate BETWEEN ?2 AND ?3"),
       // @NamedQuery(name = "AccountOperation.mostCommonOperationType",
       //         query = "SELECT ao.type FROM AccountOperation ao WHERE ao.account = ?1 GROUP BY ao.type HAVING COUNT (ao.type) = MAX(COUNT (ao.type))")
})
public class AccountOperation extends BasicModel {
    @ManyToOne
    private Account account;
    private BigDecimal amount;
    private OperationType type;

    public AccountOperation(Account account, BigDecimal amount, OperationType type) {
        this.amount = amount;
        this.account = account;
        this.type = type;
    }

    public AccountOperation() {}

    public Account getAccount() {
        return account;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public OperationType getType() {
        return type;
    }

}

