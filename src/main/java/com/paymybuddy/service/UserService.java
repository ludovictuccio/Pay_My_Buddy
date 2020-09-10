package com.paymybuddy.service;

import java.math.BigDecimal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

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

    @Autowired
    private AppAccountRepository appAccountRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    /**
     * This method service is used to add a new user.
     *
     * @param user
     * @return newUser or null if email already exists or incorrect informations.
     */
    @Validated
    public User addNewUser(@Validated final User user) {
        try {
//            if (user.getEmail().isEmpty() || user.getEmail() == null || user.getEmail().trim().length() < 10
//                    || !user.getEmail().matches(
//                            "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$")) {
//                LOGGER.info("ERROR: Invalid email.");
//                return null;
//            } else 

            if (user.getLastname() == null || user.getLastname().isEmpty() || user.getFirstname() == null
                    || user.getFirstname().isEmpty() || user.getPassword() == null || user.getPassword().isEmpty()
                    || user.getPhone() == null || user.getPhone().isEmpty() || user.getPhone().length() < 5
                    || user.getPhone().length() > 16) {
                LOGGER.error("Invalid informations. Please check the informations entered.");
                return null;
            } else if (userRepository.findByEmail(user.getEmail()) != null) {
                LOGGER.error("ERROR: An user already exists with the email entered.");
                return null;
            } else {
                User newUser = new User(user.getLastname(), user.getFirstname(), user.getEmail(),
                        bCryptPasswordEncoder.encode(user.getPassword()), user.getPhone());
                userRepository.save(newUser);

                AppAccount appAccount = new AppAccount(newUser, new BigDecimal("0.00"));
                appAccountRepository.save(appAccount);

                LOGGER.info("Succes new user acccount creation");
                return newUser;
            }
        } catch (NullPointerException np) {
            LOGGER.error("ERROR: Please verify email. " + np);
            return null;
        }
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

        if (userToFind == null) {
            LOGGER.error("ERROR: user with this email not found.");
            return isUpdated;
        } else if (userToFind.getLastname() != user.getLastname() || userToFind.getFirstname() != user.getFirstname()) {
            LOGGER.error("ERROR: Only password & phone changes are allowed.");
            return isUpdated;
        }

        userToFind.setPassword(user.getPassword());
        userToFind.setPhone(user.getPhone());

        userRepository.save(userToFind);

        LOGGER.info("SUCCESS: Informations changes.");
        isUpdated = true;
        return isUpdated;
    }

    /**
     * This method service is used to login an user.
     *
     * @param email
     * @param password
     * @return userToLogin User or null
     */
    public User login(final String email, final String password) {
        try {
            User userToLogin = userRepository.findByEmail(email);

            if (userToLogin == null) {
                LOGGER.info("Invalid email. Please check the email entered.");
                return null;
            } else if (BCrypt.checkpw(password, userToLogin.getPassword())) {
                LOGGER.info("Hello " + userToLogin.getFirstname() + " !");
                return userToLogin;
            } else {
                LOGGER.info("Invalid password. Please try again.");
                return null;
            }
        } catch (NullPointerException np) {
            LOGGER.info("Invalid password (Null). Please try again." + np);
            return null;
        }
    }
}
