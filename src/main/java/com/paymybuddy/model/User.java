package com.paymybuddy.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * User model class.
 *
 * @author Ludovic Tuccio
 */
@Entity
@Table(name = "user")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstname;

    private String lastname;

    private String email;

    private String password;

    private String phone;

    /**
     * Empty class constructor.
     */
    public User() {

    }

    /**
     * @param personsLastname
     * @param personsFirstname
     * @param personsEmail
     * @param personsPassword
     * @param personsPhoneNumber
     */
    public User(final String personsLastname, final String personsFirstname, final String personsEmail,
            final String personsPassword, final String personsPhoneNumber) {
        super();
        this.firstname = personsFirstname;
        this.lastname = personsLastname;
        this.email = personsEmail;
        this.password = personsPassword;
        this.phone = personsPhoneNumber;
    }

    /**
     * @return the userId
     */
    public Long getId() {
        return id;
    }

    /**
     * @param userId the id to set
     */
    public void setId(final Long userId) {
        this.id = userId;
    }

    /**
     * @return the firstname
     */
    public String getFirstname() {
        return firstname;
    }

    /**
     * @param personsFirstname the firstname to set
     */
    public void setFirstname(final String personsFirstname) {
        this.firstname = personsFirstname;
    }

    /**
     * @return the lastname
     */
    public String getLastname() {
        return lastname;
    }

    /**
     * @param personsLastname the lastname to set
     */
    public void setLastname(final String personsLastname) {
        this.lastname = personsLastname;
    }

    /**
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param personsEmail the email to set
     */
    public void setEmail(final String personsEmail) {
        this.email = personsEmail;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param personsPassword the password to set
     */
    public void setPassword(final String personsPassword) {
        this.password = personsPassword;
    }

    /**
     * @return the phone
     */
    public String getPhone() {
        return phone;
    }

    /**
     * @param personsPhoneNumber the phoneNumber to set
     */
    public void setPhone(final String personsPhoneNumber) {
        this.phone = personsPhoneNumber;
    }

}
