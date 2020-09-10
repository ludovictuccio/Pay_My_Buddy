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
 * PersonalPayment model class, used to transfer money to their app account with
 * their bank card.
 *
 * @author Ludovic Tuccio
 */
@Entity
@Table(name = "personal_payment")
public class PersonalPayment implements Serializable {

    private static final long serialVersionUID = 1267461539173079092L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(cascade = { CascadeType.MERGE }, fetch = FetchType.EAGER)
    @JoinColumn(name = "app_account_id", referencedColumnName = "id")
    private AppAccount myAppAccount;

    @Positive
    private BigDecimal amount;

    private String cbNumber;

    private String cbExpirationDateMonth;

    private String cbExpirationDateYear;

    private String cbSecurityKey;

    /**
     * Empty class constructor.
     */
    public PersonalPayment() {
        super();
    }

    /**
     * @param myAmount
     * @param cardNumber
     * @param cbExpirationMonth
     * @param cbExpirationYear
     * @param cbSecuKey
     */
    public PersonalPayment(final AppAccount appAccount, final BigDecimal myAmount, final String cardNumber,
            final String cbExpirationMonth, final String cbExpirationYear, final String cbSecuKey) {
        super();
        this.myAppAccount = appAccount;
        this.amount = myAmount;
        this.cbNumber = cardNumber;
        this.cbExpirationDateMonth = cbExpirationMonth;
        this.cbExpirationDateYear = cbExpirationYear;
        this.cbSecurityKey = cbSecuKey;
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
     * @return the cbNumber
     */
    public String getCbNumber() {
        return cbNumber;
    }

    /**
     * @param cardNumber the cbNumber to set
     */
    public void setCbNumber(final String cardNumber) {
        this.cbNumber = cardNumber;
    }

    /**
     * @return the cbExpirationDateMonth
     */
    public String getCbExpirationDateMonth() {
        return cbExpirationDateMonth;
    }

    /**
     * @param cbExpirationMonth the cbExpirationDateMonth to set
     */
    public void setCbExpirationDateMonth(final String cbExpirationMonth) {
        this.cbExpirationDateMonth = cbExpirationMonth;
    }

    /**
     * @return the cbExpirationDateYear
     */
    public String getCbExpirationDateYear() {
        return cbExpirationDateYear;
    }

    /**
     * @param cbExpirationDateYear the cbExpirationDateYear to set
     */
    public void setCbExpirationDateYear(final String cbExpirationYear) {
        this.cbExpirationDateYear = cbExpirationYear;
    }

    /**
     * @return the cbSecurityKey
     */
    public String getCbSecurityKey() {
        return cbSecurityKey;
    }

    /**
     * @param cbSecuKey the cbSecurityKey to set
     */
    public void setCbSecurityKey(final String cbSecuKey) {
        this.cbSecurityKey = cbSecuKey;
    }

}
