package com.paymybuddy.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.paymybuddy.model.AppAccount;
import com.paymybuddy.model.User;
import com.paymybuddy.repository.AppAccountRepository;
import com.paymybuddy.repository.UserRepository;

@Service
public class UserService implements IUserService {

    /**
     * Logger class.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger("UserService");

    private AppAccount appAccount;

    @Autowired
    private AppAccountRepository appAccountRepository;

    @Autowired
    private UserRepository userRepository;

    /**
     * This method service is used to add a new user.
     *
     * @param user
     * @return newUser or null if email already exists or incorrect
     */
    public User addNewUser(final User user) {

        try {
            User userToCreate = userRepository.findByEmail(user.getEmail());

            if (userToCreate != null) {
                LOGGER.info("ERROR: An user already exists with the email entered.");
                return null;
            } else if (user.getEmail().isEmpty() || user.getEmail() == null || user.getEmail().trim().length() < 10
                    || !user.getEmail().matches(
                            "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$")) {
                LOGGER.info("ERROR: Invalid email.");
                return null;
            } else {
                User newUser = new User(user.getLastname(), user.getFirstname(), user.getEmail(), user.getPassword(),
                        user.getPhone());
                userRepository.save(newUser);

                appAccount = new AppAccount(newUser, 0.00);
                appAccountRepository.save(appAccount);

                LOGGER.info("Succes new user acccount creation");
                return newUser;
            }
        } catch (NullPointerException np) {
            LOGGER.error("ERROR: Please verify email." + np);
        }
        return null;
    }
}
