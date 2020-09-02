package com.paymybuddy.controller;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.paymybuddy.config.Constants;
import com.paymybuddy.model.AppAccount;
import com.paymybuddy.service.IRelationService;

/**
 * RelationController class.
 *
 * @author Ludovic Tuccio
 */
@RestController
public class RelationController {

    /**
     * Logger class.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger("RelationController");

    /**
     * UserService interface variable used to access to service & model classes.
     */
    @Autowired
    private IRelationService relationService;

    /**
     * This method controller is used to add a new relation/connection with the
     * service method.
     *
     * @param myEmail
     * @param emailToConnect
     */
    @PostMapping("/connection")
    public void addRelation(@RequestParam final String myEmail, @RequestParam final String emailToConnect,
            final HttpServletResponse response) {

        boolean isAdded = relationService.addRelation(myEmail, emailToConnect);

        if (isAdded) {
            LOGGER.debug("SUCCESS - New connection added with: {}", emailToConnect);
            response.setStatus(Constants.STATUS_CREATED_201);
        } else {
            LOGGER.error("FAIL: unable to establish new connection.");
            response.setStatus(Constants.ERROR_CONFLICT_409);
        }
    }

    /**
     * This method controller is used to delete a relation/connection with the
     * service method.
     *
     * @param myEmail
     * @param emailToDelete
     */
    @DeleteMapping("/connection")
    public void deleteRelation(@RequestParam final String myEmail, @RequestParam final String emailToDelete,
            final HttpServletResponse response) {

        boolean isDeleted = relationService.deleteRelation(myEmail, emailToDelete);

        if (isDeleted) {
            LOGGER.debug("SUCCESS - Deleted connection with: {}", emailToDelete);
            response.setStatus(Constants.STATUS_OK_200);
        } else {
            LOGGER.error("FAIL: unable to delete the connection with email: {}", emailToDelete);
            response.setStatus(Constants.ERROR_CONFLICT_409);
        }
    }

    /**
     * This method service is used to get an user app account connection/relation
     * with the service method.
     *
     * @param myEmail
     * @param relationFriendEmail
     */
    @GetMapping("/connection")
    public AppAccount getRelationAppAccount(@RequestParam final String myEmail,
            @RequestParam final String relationFriendEmail, final HttpServletResponse response) {

        AppAccount result = relationService.getRelationAppAccount(myEmail, relationFriendEmail);

        if (result != null) {
            LOGGER.info("SUCCESS - Get relation app account with: {}", relationFriendEmail);
            response.setStatus(Constants.STATUS_OK_200);
        } else {
            LOGGER.error("FAIL: unable to get a connection with user:: {}", relationFriendEmail);
            response.setStatus(Constants.ERROR_CONFLICT_409);
        }
        return null;
    }

}
