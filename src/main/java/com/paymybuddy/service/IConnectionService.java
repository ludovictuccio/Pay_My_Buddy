package com.paymybuddy.service;

import com.paymybuddy.model.User;

/**
 * IConnectionService interface class.
 *
 * @author Ludovic Tuccio
 */
public interface IConnectionService {

    User addConnection(String email);

    User deleteConnection(String email);

    User getConnection(int id);
}
