package com.paymybuddy.service;

import com.paymybuddy.model.User;

/**
 * IUserService interface class.
 *
 * @author Ludovic Tuccio
 */
public interface IUserService {

    User addNewUser(User user);

    boolean updateUserInfos(User user);

    User login(String email, String password);

}
