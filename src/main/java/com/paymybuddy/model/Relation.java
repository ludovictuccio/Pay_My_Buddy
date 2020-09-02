//package com.paymybuddy.model;
//
//import java.io.Serializable;
//
//import javax.persistence.CascadeType;
//import javax.persistence.Column;
//import javax.persistence.Entity;
//import javax.persistence.GeneratedValue;
//import javax.persistence.GenerationType;
//import javax.persistence.Id;
//import javax.persistence.JoinColumn;
//import javax.persistence.ManyToOne;
//import javax.persistence.Table;
//
///**
// * Relation model class.
// *
// * @author Ludovic Tuccio
// */
//@Entity
//@Table(name = "relation")
//public class Relation implements Serializable {
//
//    private static final long serialVersionUID = -403306651940604556L;
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "id")
//    private Long id;
//
//    @Column(name = "user_id")
//    private User userId;
//
//    @ManyToOne(cascade = CascadeType.ALL)
//    @JoinColumn(name = "user_to_connect_id")
//    private User userToConnect;
//
//    /**
//     * Empty class constructor.
//     */
//    public Relation() {
//        super();
//    }
//
//    /**
//     * @param user
//     * @param userToConnectId
//     */
//    public Relation(final User user, final User userToConnectId) {
//        super();
//        this.userId = user;
//        this.userToConnect = userToConnectId;
//    }
//
//    /**
//     * @return the id
//     */
//    public Long getId() {
//        return id;
//    }
//
//    /**
//     * @param id the id to set
//     */
//    public void setId(Long id) {
//        this.id = id;
//    }
//
//    /**
//     * @return the userId
//     */
//    public User getUserId() {
//        return userId;
//    }
//
//    /**
//     * @param userId the userId to set
//     */
//    public void setUserId(User userId) {
//        this.userId = userId;
//    }
//
//    /**
//     * @return the userToConnect
//     */
//    public User getUserToConnect() {
//        return userToConnect;
//    }
//
//    /**
//     * @param userToConnect the userToConnect to set
//     */
//    public void setUserToConnect(User userToConnect) {
//        this.userToConnect = userToConnect;
//    }
//
//}
