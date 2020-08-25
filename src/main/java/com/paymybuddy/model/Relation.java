package com.paymybuddy.model;

import java.io.Serializable;

import javax.persistence.Entity;

/**
 * Relation model class.
 *
 * @author Ludovic Tuccio
 */
@Entity
public class Relation implements Serializable {

    private static final long serialVersionUID = 1L;

    private User user;

    private User userToConnect;

}
