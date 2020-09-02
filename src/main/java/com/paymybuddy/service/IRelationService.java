package com.paymybuddy.service;

/**
 * IConnectionService interface class.
 *
 * @author Ludovic Tuccio
 */
public interface IRelationService {

    boolean addRelation(String myEmail, String email);

    boolean deleteRelation(String myEmail, String emailToConnect);

//    User getRelationById(Long id);
}
