package com.paymybuddy.model;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * AppAccount model class.
 *
 * @author Ludovic Tuccio
 */
@Entity
@Table(name = "app_account")
public class AppAccount implements Serializable {

    private static final long serialVersionUID = -822275697470290777L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @OneToOne(cascade = { CascadeType.ALL, CascadeType.REMOVE }, fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User userId;

    private double balance;

    /**
     * Empty class constructor.
     */
    public AppAccount() {
        super();
    }

    public AppAccount(final User userAccount, final double balanceAccount) {
        this.userId = userAccount;
        this.balance = balanceAccount;
    }

    /**
     * @return the user
     */
    public User getUserId() {
        return userId;
    }

    /**
     * @param userAccount
     */
    public void setUserId(final User userAccount) {
        this.userId = userAccount;
    }

    /**
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * @param accountId the id to set
     */
    public void setId(final Long accountId) {
        this.id = accountId;
    }

    /**
     * @return the balance
     */
    public double getBalance() {
        return balance;
    }

    /**
     * @param accountBalance the balance to set
     */
    public void setBalance(final double accountBalance) {
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
    public boolean equals(Object obj) {
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
