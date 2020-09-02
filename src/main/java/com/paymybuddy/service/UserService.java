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

            // String password = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
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
                        // password,
                        user.getPhone());
                userRepository.save(newUser);

                AppAccount appAccount = new AppAccount(newUser, 0.00);
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
        // String password = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
        LOGGER.info("Only password or phone number can and will be changed.");
        User userToFind = userRepository.findByEmail(user.getEmail());

        // if exists
        if (userToFind != null) {
            userToFind.setPassword(user.getPassword()
            // password
            );
            userToFind.setPhone(user.getPhone());

            userRepository.save(userToFind);

            LOGGER.info("SUCCESS: Informations changes.");
            isUpdated = true;
            return isUpdated;
        }
        LOGGER.error("ERROR: Only password & phone changes are allowed.");
        return isUpdated;
    }

    /**
     * This method service is used to delete an user.
     *
     * @param email
     * @return isDeleted boolean
     */
    public boolean deleteUser(final String email) {
        boolean isDeleted = false;
        User userToFind = userRepository.findByEmail(email);

        if (userToFind != null) {
            userRepository.deleteUserByEmail(email);
            LOGGER.info("SUCCESS: Deleted user from DB.");
            isDeleted = true;
            return isDeleted;
        }
        return isDeleted;
    }

//    /**
//     * This method service is used to login an user.
//     *
//     * @param email
//     * @param password
//     * @return userToLogin user or null
//     */
//    public User login(final String email, final String password) {
//
//        User userToLogin = userRepository.findByEmail(email);
//
//        if (userToLogin != null && BCrypt.checkpw(password, userToLogin.getPassword())) {
//            LOGGER.info("Hello " + userToLogin.getFirstname() + " !");
//            return userToLogin;
//        } else if (userToLogin != null && !BCrypt.checkpw(password, userToLogin.getPassword())) {
//            LOGGER.info("Invalid password. Please try again.");
//            return null;
//        } else if (userToLogin == null) {
//            LOGGER.info("Invalid email. Please check the email entered.");
//            return null;
//        }
//        return null;
//    }

}
