package com.paymybuddy.service;

import com.paymybuddy.model.AppAccount;

/**
 * IConnectionService interface class.
 *
 * @author Ludovic Tuccio
 */
public interface IRelationService {

    boolean addRelation(String myEmail, String email);

    boolean deleteRelation(String myEmail, String emailToConnect);

    AppAccount getRelationAppAccount(String myEmail,
            String relationFriendEmail);

}
