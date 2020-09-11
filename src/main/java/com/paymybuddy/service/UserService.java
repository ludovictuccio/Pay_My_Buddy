package com.paymybuddy.service;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.paymybuddy.config.Constants;
import com.paymybuddy.model.AppAccount;
import com.paymybuddy.model.User;
import com.paymybuddy.repository.AppAccountRepository;
import com.paymybuddy.repository.UserRepository;

/**
 * UserService class.
 *
 * @author Ludovic Tuccio
 */
@Service
public class UserService implements IUserService {

    private static final Logger LOGGER = LoggerFactory.getLogger("UserService");

    @Autowired
    private AppAccountRepository appAccountRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    /**
     * Validator used to validate javax constraints in model classes.
     */
    private Validator validator;

    /**
     * This method service is used to add a new user. Email and password are
     * encrypted.
     *
     * @param user
     * @return newUser or null if email already exists or invalid informations.
     */
    public User addNewUser(final User user) {
        try {

            if (userRepository.findByEmail(user.getEmail()) != null) {
                LOGGER.error(
                        "ERROR: An user already exists with the email entered.");
                return null;
            }

            ValidatorFactory factory = Validation
                    .buildDefaultValidatorFactory();
            validator = factory.getValidator();

            Set<ConstraintViolation<User>> constraintViolations = validator
                    .validate(user);
            if (constraintViolations.size() > 0) {
                LOGGER.error(
                        "ERROR: a constraint was violated. Please check the informations entered.");
                return null;
            }

            User newUser = new User(user.getLastname(), user.getFirstname(),
                    bCryptPasswordEncoder.encode(user.getEmail()),
                    bCryptPasswordEncoder.encode(user.getPassword()),
                    user.getPhone());
            userRepository.save(newUser);

            AppAccount appAccount = new AppAccount(newUser,
                    Constants.INITIAL_ACCOUNT_AMOUNT);
            appAccountRepository.save(appAccount);

            LOGGER.info("Succes new user acccount creation");
            return newUser;

        } catch (NullPointerException np) {
            LOGGER.error("ERROR: Please verify email. " + np);
            return null;
        }
    }

    /**
     * This method service is used to update user informations (password or
     * phone).
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
        } else if (!userToFind.getLastname().equals(user.getLastname())
                || !userToFind.getFirstname().equals(user.getFirstname())) {
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
                LOGGER.debug("Login success");
                LOGGER.info("Hello " + userToLogin.getFirstname() + " !");
                return userToLogin;
            } else {
                LOGGER.info("Invalid password. Please try again.");
                return null;
            }
        } catch (NullPointerException np) {
            LOGGER.info("Invalid password (Null). Please try again. " + np);
            return null;
        }
    }
}
