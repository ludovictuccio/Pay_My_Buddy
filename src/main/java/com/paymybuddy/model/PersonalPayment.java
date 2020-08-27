package com.paymybuddy.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * PersonalPayment model class.
 *
 * @author Ludovic Tuccio
 */
@Entity
@Table(name = "personal_payment")
public class PersonalPayment {

    @Id
    @GeneratedValue
    private Long id;

    private double amount;

    private String cbNumber;

    private String cbExpirationDateMonth;

    private String cbExpirationDateYear;

    private String cbSecurityKey;

}
