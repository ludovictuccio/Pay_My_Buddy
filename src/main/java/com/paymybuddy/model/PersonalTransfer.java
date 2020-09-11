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
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
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

    @NotNull
    @Positive
    @Column(columnDefinition = "DECIMAL(7,2)")
    private BigDecimal amount;

    @NotNull
    @NotEmpty
    @Pattern(regexp = "^[0-9A-Z]{15,31}$", message = "The european IBAN entered is invalid. Please check it (only letters and numbers, size must be between 15 and 31)")
    private String iban;

    @NotNull
    @NotEmpty
    @Pattern(regexp = "^[0-9A-Z]{8,11}$", message = "The european BIC entered is invalid. Please check it (only letters and numbers, size must be between 8 and 11)")
    private String bic;

    public PersonalTransfer() {
        super();
    }

    public PersonalTransfer(final AppAccount appAccount,
            final BigDecimal myAmount, final String myIban,
            final String myBic) {
        super();
        this.myAppAccount = appAccount;
        this.amount = myAmount;
        this.iban = myIban;
        this.bic = myBic;
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

    public String getIban() {
        return iban;
    }

    public void setIban(final String myIban) {
        this.iban = myIban;
    }

    public String getBic() {
        return bic;
    }

    public void setBic(final String myBic) {
        this.bic = myBic;
    }

}
