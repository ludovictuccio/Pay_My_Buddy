package com.paymybuddy.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
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

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @OneToOne(targetEntity = User.class)
    @JoinColumn(name = "user_id")
    private User user;

    private double balance;

    public AppAccount(final User userAccount, final double balanceAccount) {
        this.user = userAccount;
        this.balance = balanceAccount;
    }

    /**
     * @return the user
     */
    public User getUser() {
        return user;
    }

    /**
     * @param userAccount
     */
    public void setUser(final User userAccount) {
        this.user = userAccount;
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

}
