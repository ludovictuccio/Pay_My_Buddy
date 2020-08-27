package com.paymybuddy.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.paymybuddy.model.AppAccount;
import com.paymybuddy.model.User;
import com.paymybuddy.repository.AppAccountRepository;
import com.paymybuddy.repository.UserRepository;

@Service
@Transactional(rollbackFor = Exception.class)
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

    /**
     * This method service is used to update user informations (password or phone).
     *
     * @param user entity
     * @return isUpdated boolean
     */
    public boolean updateUserInfos(final User user) {
        boolean isUpdated = false;
        LOGGER.info("Only password or phone number can and will be changed.");
        User userToFind = userRepository.findByEmail(user.getEmail());

        // if exists
        if (userToFind != null) {
            userToFind.setPassword(user.getPassword());
            userToFind.setPhone(user.getPhone());

            LOGGER.info("SUCCESS: Informations changes.");
            isUpdated = true;
            return isUpdated;
        }
        LOGGER.error("ERROR: Only password & phone changes are allowed.");
        return isUpdated;
    }
}
