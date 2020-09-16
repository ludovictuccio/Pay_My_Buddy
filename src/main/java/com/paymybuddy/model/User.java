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
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;

/**
 * User model class.
 *
 * @author Ludovic Tuccio
 */
@Entity
@Table(name = "user")
public class User implements Serializable {

    private static final long serialVersionUID = 5727638570064420003L;

    private static final int FIRSTNAME_MAX_SIZE = 50;

    private static final int LASTNAME_MAX_SIZE = 50;

    private static final int EMAIL_MIN_SIZE = 8;

    private static final int EMAIL_MAX_SIZE = 80;

    private static final int PASSWORD_MIN_SIZE = 4;

    private static final int PASSWORD_MAX_SIZE = 60;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @NotNull
    @NotEmpty
    @Length(min = 2, max = FIRSTNAME_MAX_SIZE)
    private String firstname;

    @NotNull
    @NotEmpty
    @Length(min = 2, max = LASTNAME_MAX_SIZE)
    private String lastname;

    @NotNull
    @NotEmpty
    @Length(min = EMAIL_MIN_SIZE, max = EMAIL_MAX_SIZE, message = "Invalid email entry. The size must be betwwen 8 and 80.")
    @Pattern(regexp = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$", message = " Invalid entry email.")
    private String email;

    @NotNull
    @NotEmpty
    @Length(min = PASSWORD_MIN_SIZE, max = PASSWORD_MAX_SIZE, message = "Invalid password entry. The size must be betwwen 4 and 60.")
    private String password;

    @NotNull
    @NotEmpty
    @Pattern(regexp = "^(\\+\\d{1,3}( )?)?((\\(\\d{3}\\))|\\d{3})[- .]?\\d{3}[- .]?\\d{4}$", message = " Invalid entry phone number.")
    private String phone;

    @OneToOne(cascade = CascadeType.REMOVE, mappedBy = "userId", fetch = FetchType.EAGER)
    private AppAccount ownAppAccount;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "relation", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "user_to_connect_id"))
    private Set<User> pmbFriends = new HashSet<>();

    public User() {
        super();
    }

    public User(final String personsLastname, final String personsFirstname,
            final String personsEmail, final String personsPassword,
            final String personsPhoneNumber) {
        super();
        this.firstname = personsFirstname;
        this.lastname = personsLastname;
        this.email = personsEmail;
        this.password = personsPassword;
        this.phone = personsPhoneNumber;
    }

    public void addPmbFriends(final User user) {
        this.pmbFriends.add(user);
    }

    public Set<User> getPmbFriends() {
        return pmbFriends;
    }

    public void setPmbFriends(final Set<User> userRelations) {
        this.pmbFriends = userRelations;
    }

    public AppAccount getOwnAppAccount() {
        return ownAppAccount;
    }

    public void setOwnAppAccount(final AppAccount account) {
        this.ownAppAccount = account;
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long userId) {
        this.id = userId;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(final String personsFirstname) {
        this.firstname = personsFirstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(final String personsLastname) {
        this.lastname = personsLastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(final String personsEmail) {
        this.email = personsEmail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(final String personsPassword) {
        this.password = personsPassword;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(final String personsPhoneNumber) {
        this.phone = personsPhoneNumber;
    }

}
