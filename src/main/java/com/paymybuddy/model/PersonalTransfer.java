package com.paymybuddy.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * PersonalTransfer model class.
 *
 * @author Ludovic Tuccio
 */
@Entity
@Table(name = "personal_transfer")
public class PersonalTransfer {

    @Id
    @GeneratedValue
    private long id;

    private double amount;

    private String iban;

    private String bic;

}
