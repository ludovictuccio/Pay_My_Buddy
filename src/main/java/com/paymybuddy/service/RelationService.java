package com.paymybuddy.service;

import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.paymybuddy.model.AppAccount;
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

    @Autowired
    private UserRepository userRepository;

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

            Set<User> relations = user.getPmbFriends();
            user.addPmbFriends(userToConnect);
            user.setPmbFriends(relations);

            userRepository.save(user);
            LOGGER.info("Successful add connection !");
            isAdded = true;
            return isAdded;
        }
        LOGGER.error("Fail to add connection. Please check the email entered.");
        return isAdded;
    }

    /**
     * This method service is used to delete a connection/relation
     *
     * @param myEmail
     * @param emailToDelete
     * @return boolean isDeleted true if relation's deleted
     */
    public boolean deleteRelation(final String myEmail, final String emailToDelete) {
        boolean isDeleted = false;

        User myAccount = userRepository.findByEmail(myEmail);
        User connectionToDelete = userRepository.findByEmail(emailToDelete);

        if (connectionToDelete != null && myAccount != null) {
            Set<User> allRelations = myAccount.getPmbFriends();
            for (User relation : allRelations) {
                if (relation.getEmail().toString().contentEquals(emailToDelete)) {

                    myAccount.getPmbFriends().remove(relation);

                    userRepository.save(myAccount);
                    LOGGER.info("Successful delete connection !");
                    isDeleted = true;
                    return isDeleted;
                }
            }
        }
        LOGGER.error("Fail to delete connection. Please check the email entered.");
        return isDeleted;
    }

    /**
     * This method service is used to get an user app account connection/relation.
     *
     * @param myEmail
     * @param relationFriendEmail
     * @return connectionToRetrieve an AppAccount object
     */
    public AppAccount getRelationAppAccount(final String myEmail, final String relationFriendEmail) {

        User myAccount = userRepository.findByEmail(myEmail);
        User connectionToRetrieve = userRepository.findByEmail(relationFriendEmail);

        if (connectionToRetrieve != null && myAccount != null) {

            Set<User> allRelations = myAccount.getPmbFriends();
            for (User relation : allRelations) {
                if (relation.getEmail().toString().contentEquals(relationFriendEmail)) {

                    return connectionToRetrieve.getOwnAppAccount();
                }
            }
        }
        LOGGER.error("Fail to retrieve friend's connection. Please check the email entered.");
        return null;
    }

}
