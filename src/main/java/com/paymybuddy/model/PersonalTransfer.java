package com.paymybuddy.model;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Positive;

/**
 * PersonalTransfer model class.
 *
 * @author Ludovic Tuccio
 */
@Entity
@Table(name = "personal_transfer")
public class PersonalTransfer implements Serializable {

    private static final long serialVersionUID = 2429213757228328443L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(cascade = { CascadeType.MERGE }, fetch = FetchType.EAGER)
    @JoinColumn(name = "app_account_id", referencedColumnName = "id")
    private AppAccount myAppAccount;

    @Positive
    private BigDecimal amount;

    private String iban;

    private String bic;

    /**
     * Empty class constructor.
     */
    public PersonalTransfer() {
        super();
    }

    /**
     * @param appAccount
     * @param myAmount
     * @param myIban
     * @param myBic
     */
    public PersonalTransfer(final AppAccount appAccount, final BigDecimal myAmount, final String myIban,
            final String myBic) {
        super();
        this.myAppAccount = appAccount;
        this.amount = myAmount;
        this.iban = myIban;
        this.bic = myBic;
    }

    /**
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(final Long id) {
        this.id = id;
    }

    /**
     * @return the myAppAccount
     */
    public AppAccount getMyAppAccount() {
        return myAppAccount;
    }

    /**
     * @param appAccount the myAppAccount to set
     */
    public void setMyAppAccount(final AppAccount appAccount) {
        this.myAppAccount = appAccount;
    }

    /**
     * @return the amount
     */
    public BigDecimal getAmount() {
        return amount;
    }

    /**
     * @param myAmount the amount to set
     */
    public void setAmount(final BigDecimal myAmount) {
        this.amount = myAmount;
    }

    /**
     * @return the iban
     */
    public String getIban() {
        return iban;
    }

    /**
     * @param myIban the iban to set
     */
    public void setIban(final String myIban) {
        this.iban = myIban;
    }

    /**
     * @return the bic
     */
    public String getBic() {
        return bic;
    }

    /**
     * @param myBic the bic to set
     */
    public void setBic(final String myBic) {
        this.bic = myBic;
    }

}
