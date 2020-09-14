package com.paymybuddy.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import org.hibernate.validator.constraints.Length;

/**
 * Transaction model class.
 *
 * @author Ludovic Tuccio
 */
@Entity
@Table(name = "transaction")
public class Transaction implements Serializable {

    private static final long serialVersionUID = 7547368168259612613L;

    private static final int DESCRIPTION_MAX_SIZE = 80;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @NotNull
    @Positive
    @DecimalMax(value = "9999.99")
    @DecimalMin(value = "1.00")
    @Column(columnDefinition = "DECIMAL(8,2)")
    private BigDecimal amount;

    @NotNull
    @Length(min = 1, max = DESCRIPTION_MAX_SIZE, message = "The description size must be less than 80 characters")
    private String description;

    @Column(name = "transaction_date")
    private LocalDate transactionDate;

    @ManyToOne
    @JoinColumn(name = "app_account_sender_id")
    private AppAccount appAccountSender;

    @ManyToOne
    @JoinColumn(name = "app_account_beneficiary_id")
    private AppAccount appAccountBeneficiary;

    public Transaction() {
        super();
    }

    public Transaction(final AppAccount senderAccount,
            final AppAccount beneficiary, final BigDecimal transactionAmount,
            final String transactionDescription, final LocalDate transDate) {
        super();
        this.amount = transactionAmount;
        this.description = transactionDescription;
        this.transactionDate = transDate;
        this.appAccountBeneficiary = beneficiary;
        this.appAccountSender = senderAccount;
    }

    public AppAccount getAppAccountSender() {
        return appAccountSender;
    }

    public void setAppAccountSender(final AppAccount senderAccount) {
        this.appAccountSender = senderAccount;
    }

    public AppAccount getAppAccountBeneficiary() {
        return appAccountBeneficiary;
    }

    public void setAppAccountBeneficiary(final AppAccount beneficiaryAccount) {
        this.appAccountBeneficiary = beneficiaryAccount;
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long idTransaction) {
        this.id = idTransaction;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(final BigDecimal transactionAmount) {
        this.amount = transactionAmount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String transactionDescription) {
        this.description = transactionDescription;
    }

    public LocalDate getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(final LocalDate transDate) {
        this.transactionDate = transDate;
    }

}
