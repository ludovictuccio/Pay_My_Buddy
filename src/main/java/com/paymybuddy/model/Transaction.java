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
import javax.validation.constraints.Digits;

/**
 * Transaction model class.
 *
 * @author Ludovic Tuccio
 */
@Entity
@Table(name = "transaction")
public class Transaction implements Serializable {

    private static final long serialVersionUID = 7547368168259612613L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @DecimalMax(value = "9999.99")
    @DecimalMin(value = "0.10", inclusive = false)
    @Digits(fraction = 2, integer = 4, message = "Must be a number between 0.10 and 10,000.00, with 2 fractional digits max")
    @Column(columnDefinition = "DECIMAL(8,2)")
    private BigDecimal amount;

    private String description;

    @Column(name = "transaction_date")
    private LocalDate transactionDate;

    @ManyToOne
    @JoinColumn(name = "app_account_sender_id")
    private AppAccount appAccountSender;

    @ManyToOne
    @JoinColumn(name = "app_account_beneficiary_id")
    private AppAccount appAccountBeneficiary;

    /**
     * Empty class constructor.
     */
    public Transaction() {
        super();
    }

    /**
     * @param transactionAmount
     * @param transactionDescription
     * @param transDate
     * @param transAppAccount
     */
    public Transaction(final AppAccount senderAccount, final AppAccount beneficiary,
            @DecimalMax("9999.99") @DecimalMin(value = "0.10", inclusive = false) @Digits(fraction = 2, integer = 4, message = "Must be a number between 0.10 and 10,000.00, with 2 fractional digits max") final BigDecimal transactionAmount,
            final String transactionDescription, final LocalDate transDate) {
        super();
        this.amount = transactionAmount;
        this.description = transactionDescription;
        this.transactionDate = transDate;
        this.appAccountBeneficiary = beneficiary;
        this.appAccountSender = senderAccount;
    }

    /**
     * @return the appAccountSender
     */
    public AppAccount getAppAccountSender() {
        return appAccountSender;
    }

    /**
     * @param senderAccount the appAccountSender to set
     */
    public void setAppAccountSender(final AppAccount senderAccount) {
        this.appAccountSender = senderAccount;
    }

    /**
     * @return the appAccountBeneficiary
     */
    public AppAccount getAppAccountBeneficiary() {
        return appAccountBeneficiary;
    }

    /**
     * @param beneficiaryAccount the appAccountBeneficiary to set
     */
    public void setAppAccountBeneficiary(final AppAccount beneficiaryAccount) {
        this.appAccountBeneficiary = beneficiaryAccount;
    }

    /**
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * @param idTransaction the id to set
     */
    public void setId(final Long idTransaction) {
        this.id = idTransaction;
    }

    /**
     * @return the amount
     */
    public BigDecimal getAmount() {
        return amount;
    }

    /**
     * @param transactionAmount the amount to set
     */
    public void setAmount(final BigDecimal transactionAmount) {
        this.amount = transactionAmount;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param transactionDescription the description to set
     */
    public void setDescription(final String transactionDescription) {
        this.description = transactionDescription;
    }

    /**
     * @return the transactionDate
     */
    public LocalDate getTransactionDate() {
        return transactionDate;
    }

    /**
     * @param transDate the transactionDate to set
     */
    public void setTransactionDate(final LocalDate transDate) {
        this.transactionDate = transDate;
    }

}
