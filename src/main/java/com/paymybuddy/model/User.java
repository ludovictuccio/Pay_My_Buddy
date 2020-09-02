package com.paymybuddy.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * User model class.
 *
 * @author Ludovic Tuccio
 */
@Entity
@Table(name = "user")
public class User implements Serializable {

    private static final long serialVersionUID = 3445487792037720759L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    private String firstname;

    private String lastname;

    @Column(unique = true, nullable = false)
    private String email;

    private String password;

    private String phone;

    @OneToOne(cascade = CascadeType.REMOVE, mappedBy = "userId", fetch = FetchType.EAGER)
    private AppAccount ownAppAccount;

    // @OneToMany(mappedBy = "userToConnect", cascade = CascadeType.ALL)

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "relation", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "user_to_connect_id"))
    private Set<User> relations = new HashSet<>();

    public void addRelation(final User user) {
        this.relations.add(user);
    }

    public Set<User> getRelations() {
        return relations;
    }

    public void setRelations(final Set<User> userRelations) {
        this.relations = userRelations;
    }

    /**
     * Empty class constructor.
     */
    public User() {
        super();
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
     * @return the ownAppAccount
     */
    public AppAccount getOwnAppAccount() {
        return ownAppAccount;
    }

    /**
     * @param account the ownAppAccount to set
     */
    public void setOwnAppAccount(final AppAccount account) {
        this.ownAppAccount = account;
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
