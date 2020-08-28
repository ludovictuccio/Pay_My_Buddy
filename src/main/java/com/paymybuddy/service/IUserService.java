package com.paymybuddy.service;

import com.paymybuddy.model.User;

/**
 * IUserService interface class.
 *
 * @author Ludovic Tuccio
 */
public interface IUserService {

    User addNewUser(final User user);

    boolean updateUserInfos(final User user);

    boolean deleteUser(final String email);

}
