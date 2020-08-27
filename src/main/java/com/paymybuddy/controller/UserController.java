package com.paymybuddy.controller;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.paymybuddy.config.Constants;
import com.paymybuddy.model.User;
import com.paymybuddy.service.IUserService;

/**
 * UserController class.
 *
 * @author Ludovic Tuccio
 */
@RestController
public class UserController {

    /**
     * Logger class.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger("UserController");

    /**
     * UserService interface variable used to access to service & model classes.
     */
    @Autowired
    private IUserService userService;

    /**
     * This method controller is used to create a new User with the service method.
     *
     * @param user
     */
    @PostMapping("/registration")
    public User createNewUser(@RequestBody final User user, final HttpServletResponse response) {

        User userCreated = userService.addNewUser(user);

        if (user.getEmail() == null) {
            LOGGER.debug("FAIL: null email");
            response.setStatus(Constants.ERROR_CONFLICT_409);
            return null;
        } else if (userCreated != null) {
            LOGGER.debug("SUCCESS - New user created for email: {}", userCreated.getEmail());
            response.setStatus(Constants.STATUS_CREATED_201);
            return userCreated;
        } else {
            LOGGER.debug("FAIL: post request to add new user");
            response.setStatus(Constants.ERROR_CONFLICT_409);
        }
        return null;
    }

}
