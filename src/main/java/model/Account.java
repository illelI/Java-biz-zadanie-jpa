package model;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@NamedQueries({
        @NamedQuery(name = "Account.findByOwnerAndAddress",
                query = "SELECT a FROM Account a WHERE a.address = ?1 AND a.name = ?2"),
        @NamedQuery(name = "Account.findByOwner",
                query = "SELECT a FROM Account a WHERE a.name LIKE :name"),
        @NamedQuery(name = "Account.findByBalanceBetween",
                query = "SELECT a FROM Account a WHERE a.amount BETWEEN ?1 AND ?2"),
        @NamedQuery(name = "Account.findWithMostMoney",
                query = "SELECT a FROM Account a WHERE a.amount = (SELECT MAX(a.amount) FROM Account a)"),
        @NamedQuery(name = "Account.findAccountsWithoutOperations",
                query = "SELECT a FROM Account a WHERE a.id NOT IN (SELECT ao.account.id FROM AccountOperation ao)"),
       // @NamedQuery(name = "Account.accountsWithMostOperations",
       //         query = "SELECT a FROM Account a GROUP BY a HAVING COUNT(a) = MAX(COUNT(a))")
})
@Table(name = "account")
public class Account extends BasicModel{

    private String name;
    @Column(unique = true)
    private String address;
    BigDecimal amount;

    public Account(String name, String address, Long id) {
        this.name = name;
        this.address = address;
        amount = new BigDecimal("0");
        this.id = id;
    }
    public Account(String name, String address) {
        this.name = name;
        this.address = address;
        amount = new BigDecimal("0");
    }

    public Account() {
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }
    public Long getId() { return id; }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = this.amount.add(amount);
    }

    @Override
    public boolean equals(Object o) {
        Account tmp = (Account) o;
        return this.name.equals(tmp.getName()) && this.address.equals(tmp.getAddress());
    }

}
