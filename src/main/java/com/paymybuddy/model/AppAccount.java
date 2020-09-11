package com.paymybuddy.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.PositiveOrZero;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

/**
 * AppAccount model class.
 *
 * @author Ludovic Tuccio
 */
@Entity
@Table(name = "app_account")
public class AppAccount implements Serializable {

    private static final long serialVersionUID = 1641958077792413640L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long appAccountId;

    @OneToOne(cascade = { CascadeType.ALL,
            CascadeType.REMOVE }, fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User userId;

    @Column(columnDefinition = "DECIMAL(8,2)")
    @PositiveOrZero
    private BigDecimal balance;

    @OneToMany(mappedBy = "appAccountSender")
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<Transaction> senderTransactions = new ArrayList<>();

    @OneToMany(mappedBy = "appAccountBeneficiary")
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<Transaction> beneficiaryTransactions = new ArrayList<>();

    public AppAccount() {
        super();
    }

    public AppAccount(final User userAccount, final BigDecimal balanceAccount) {
        this.userId = userAccount;
        this.balance = balanceAccount;
    }

    public User getUserId() {
        return userId;
    }

    public void addTransactionForSender(final Transaction transaction) {
        this.senderTransactions.add(transaction);
    }

    public List<Transaction> getSenderTransactions() {
        return senderTransactions;
    }

    public void setSenderTransactions(final List<Transaction> senderTransac) {
        this.senderTransactions = senderTransac;
    }

    public List<Transaction> getBeneficiaryTransactions() {
        return beneficiaryTransactions;
    }

    public void setBeneficiaryTransactions(
            final List<Transaction> beneficiaryTransac) {
        this.beneficiaryTransactions = beneficiaryTransac;
    }

    public void setUserId(final User userAccount) {
        this.userId = userAccount;
    }

    public Long getAppAccountId() {
        return appAccountId;
    }

    public void setAppAccountId(final Long accountId) {
        this.appAccountId = accountId;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(final BigDecimal accountBalance) {
        this.balance = accountBalance;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((userId == null) ? 0 : userId.hashCode());
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        AppAccount other = (AppAccount) obj;
        if (userId == null) {
            if (other.userId != null)
                return false;
        } else if (!userId.equals(other.userId))
            return false;
        return true;
    }

}
