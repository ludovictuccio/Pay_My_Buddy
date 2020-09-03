package com.paymybuddy.controller;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.paymybuddy.config.Constants;
import com.paymybuddy.service.ITransferService;

/**
 * TransferController class.
 *
 * @author Ludovic Tuccio
 */
@RestController
public class TransferController {

    /**
     * Logger class.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger("TransferController");

    /**
     * UserService interface variable used to access to service & model classes.
     */
    @Autowired
    private ITransferService transferService;

    /**
     * This method controller is used to do a personal payment (from bank card to
     * app account)
     *
     * @param myEmail
     * @param amount
     * @param cbNumber
     * @param cbExpirationDateMonth
     * @param cbExpirationDateYear
     * @param cbSecurityKey
     * @param response
     * @return isDone boolean
     */
    @GetMapping("user-space/personal-payment")
    public boolean makePersonalPayment(@RequestParam final String myEmail, @RequestParam final double amount,
            @RequestParam final String cbNumber, @RequestParam final String cbExpirationDateMonth,
            @RequestParam final String cbExpirationDateYear, @RequestParam final String cbSecurityKey,
            final HttpServletResponse response) {

        boolean isDone = transferService.makePersonalPayment(myEmail, amount, cbNumber, cbExpirationDateMonth,
                cbExpirationDateYear, cbSecurityKey);

        if (isDone) {
            LOGGER.debug("SUCCESS - login request");
            response.setStatus(Constants.STATUS_OK_200);
            return isDone;
        } else {
            LOGGER.error("FAIL - login request");
            response.setStatus(Constants.ERROR_NOT_FOUND_404);
            return isDone;
        }
    }
}
