package com.paymybuddy.service;

import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.paymybuddy.model.User;
import com.paymybuddy.repository.UserRepository;

/**
 * UserService class.
 *
 * @author Ludovic Tuccio
 */
@Service
public class RelationService implements IRelationService {

    /**
     * Logger class.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger("RelationService");

//    @Autowired
//    private RelationRepository relationRepository;

    @Autowired
    private UserRepository userRepository;

    /**
     * This method service is used to add a connection/relation between two users
     * adding the email of the user's friend.
     *
     * @param myEmail
     * @param emailToConnect
     * @return relation saved in relationRepository or null
     */
//    public Relation addRelation(final String myEmail, final String emailToConnect) {
//
//        User user = userRepository.findByEmail(myEmail);
//        User userToConnect = userRepository.findByEmail(emailToConnect);
//
//        if (userToConnect != null) {
//            Relation relation = new Relation(user, userToConnect);
//            LOGGER.info("Successful add connection !");
//            return relationRepository.save(relation);
//        }
//        LOGGER.error("Fail to add connection. Please check the email entered.");
//        return null;
//    }

    /**
     * This method service is used to add a connection/relation between two users
     * adding the email of the user's friend.
     *
     * @param myEmail
     * @param emailToConnect
     * @return boolean isAdded true if relation's saved
     */
    public boolean addRelation(final String myEmail, final String emailToConnect) {
        boolean isAdded = false;
        User user = userRepository.findByEmail(myEmail);
        User userToConnect = userRepository.findByEmail(emailToConnect);

        if (userToConnect != null && user != null) {

            Set<User> relations = user.getRelations();
            user.addRelation(userToConnect);
            user.setRelations(relations);

            userRepository.save(user);
            LOGGER.info("Successful add connection !");
            isAdded = true;
            return isAdded;
        }
        LOGGER.error("Fail to add connection. Please check the email entered.");
        return isAdded;
    }

//    public Relation deleteRelation(final String email) {
//
//        return null;
//    }
//
//    public Relation getRelationById(final Long id) {
//
//        return null;
//    }

}