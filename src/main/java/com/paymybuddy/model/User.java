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
import javax.validation.Valid;
import javax.validation.constraints.Email;

/**
 * User model class.
 *
 * @author Ludovic Tuccio
 */
@Entity
@Table(name = "user")
public class User implements Serializable {

    private static final long serialVersionUID = 5727638570064420003L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    private String firstname;

    private String lastname;

    // @Column(unique = true, nullable = false)
    // @Valid
    // @NotNull
    // @NotEmpty
    // @NotBlank
    // @Length(min = 10, max = 80)
    @Email(regexp = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$", message = "Invalid email entry.")
    private String email;

    @Valid
    // @Length(min = 1, max = 60, message = "Invalid password entry")
    // @ColumnTransformer(forColumn = "password", read = "AES_DECRYPT(password,
    // 'password')", write = "AES_ENCRYPT(?, 'password')")
    private String password;

    // @Pattern(regexp = "^[0-9]{5-16}", message = "Invalid phone number entry")
    private String phone;

    @OneToOne(cascade = CascadeType.REMOVE, mappedBy = "userId", fetch = FetchType.EAGER)
    private AppAccount ownAppAccount;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "relation", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "user_to_connect_id"))
    private Set<User> pmbFriends = new HashSet<>();

    public void addPmbFriends(final User user) {
        this.pmbFriends.add(user);
    }

    public Set<User> getPmbFriends() {
        return pmbFriends;
    }

    public void setPmbFriends(final Set<User> userRelations) {
        this.pmbFriends = userRelations;
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
    public User(final String personsLastname, final String personsFirstname,
            @Email(regexp = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$", message = "Invalid email entry.") final String personsEmail,
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
