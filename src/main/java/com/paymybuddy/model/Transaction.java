package com.paymybuddy.model;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Transaction model class.
 *
 * @author Ludovic Tuccio
 */
@Entity
@Table(name = "transaction")
public class Transaction {

    @Id
    @GeneratedValue
    private long id;

    private double amount;

    private String description;

    private LocalDate transactionDate;
}
