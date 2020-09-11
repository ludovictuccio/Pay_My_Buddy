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
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
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

    @NotNull
    @Positive
    @DecimalMax(value = "9999.99")
    @DecimalMin(value = "0.10")
    @Column(columnDefinition = "DECIMAL(7,2)")
    private BigDecimal amount;

    @NotNull
    @NotEmpty
    @Pattern(regexp = "^(?:(?<visa>4[0-9]{12}(?:[0-9]{3})?)|"
            + "(?<mastercard>5[1-5][0-9]{14})|"
            + "(?<discover>6(?:011|5[0-9]{2})[0-9]{12})|"
            + "(?<amex>3[47][0-9]{13})|"
            + "(?<diners>3(?:0[0-5]|[68][0-9])?[0-9]{11})|"
            + "(?<jcb>(?:2131|1800|35[0-9]{3})[0-9]{11}))$", message = "The bank card number is incorrect.")
    private String cbNumber;

    @NotNull
    @NotEmpty
    @Pattern(regexp = "(^[1-9]$)|(^0[1-9]|1[0-2]$)", message = "The card expiration month must be a number between 01 & 12.")
    private String cbExpirationDateMonth;

    @NotNull
    @NotEmpty
    @Pattern(regexp = "([2-9][0-9])|([1-9])", message = "The card expiration year must be a number between 20 & 99.")
    private String cbExpirationDateYear;

    @NotNull
    @NotEmpty
    @Pattern(regexp = "([0-9][0-9][0-9])|([1-9])", message = "The card security key must be a number between 000 & 999.")
    private String cbSecurityKey;

    public PersonalPayment() {
        super();
    }

    public PersonalPayment(final AppAccount appAccount,
            final BigDecimal myAmount, final String cardNumber,
            final String cbExpirationMonth, final String cbExpirationYear,
            final String cbSecuKey) {
        super();
        this.myAppAccount = appAccount;
        this.amount = myAmount;
        this.cbNumber = cardNumber;
        this.cbExpirationDateMonth = cbExpirationMonth;
        this.cbExpirationDateYear = cbExpirationYear;
        this.cbSecurityKey = cbSecuKey;
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long pId) {
        this.id = pId;
    }

    public AppAccount getMyAppAccount() {
        return myAppAccount;
    }

    public void setMyAppAccount(final AppAccount appAccount) {
        this.myAppAccount = appAccount;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(final BigDecimal myAmount) {
        this.amount = myAmount;
    }

    public String getCbNumber() {
        return cbNumber;
    }

    public void setCbNumber(final String cardNumber) {
        this.cbNumber = cardNumber;
    }

    public String getCbExpirationDateMonth() {
        return cbExpirationDateMonth;
    }

    public void setCbExpirationDateMonth(final String cbExpirationMonth) {
        this.cbExpirationDateMonth = cbExpirationMonth;
    }

    public String getCbExpirationDateYear() {
        return cbExpirationDateYear;
    }

    public void setCbExpirationDateYear(final String cbExpirationYear) {
        this.cbExpirationDateYear = cbExpirationYear;
    }

    public String getCbSecurityKey() {
        return cbSecurityKey;
    }

    public void setCbSecurityKey(final String cbSecuKey) {
        this.cbSecurityKey = cbSecuKey;
    }

}
