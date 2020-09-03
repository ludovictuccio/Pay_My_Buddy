package com.paymybuddy.service;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.paymybuddy.config.Constants;
import com.paymybuddy.model.AppAccount;
import com.paymybuddy.model.PersonalPayment;
import com.paymybuddy.model.User;
import com.paymybuddy.repository.AppAccountRepository;
import com.paymybuddy.repository.PersonalPaymentRepository;
import com.paymybuddy.repository.UserRepository;

/**
 * TransferService class.
 *
 * @author Ludovic Tuccio
 */
@Service
public class TransferService implements ITransferService {

    /**
     * Logger class.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger("TransferService");

    @Autowired
    private PersonalPaymentRepository personalPaymentRepository;

    @Autowired
    private AppAccountRepository appAccountRepository;

    @Autowired
    private UserRepository userRepository;

    /**
     * Method used to check if the bank card informations entered are corrects.
     *
     * @param amount
     * @param cbNumber
     * @param cbExpirationDateMonth
     * @param cbExpirationDateYear
     * @param cbSecurityKey
     * @return isGood boolean, false if bad user entry
     */
    public static boolean checkValidBankCardInformations(final double amount, final String cbNumber,
            final String cbExpirationDateMonth, final String cbExpirationDateYear, final String cbSecurityKey) {
        boolean isGood = true;
        if (amount >= 10000.00 || amount < 1) {
            LOGGER.info("Invalid amount entry. Please entry an amount between 1€ & 9999,99€.");
            isGood = false;
        } else if (!cbNumber.matches(Constants.BANK_CARD_NUMBER_PATTERN)) {
            LOGGER.info("Invalid bank card number. Please entry between 13 & 16 numbers only.");
            isGood = false;
        } else if (!cbExpirationDateMonth.matches(Constants.CARD_EXPIRATION_MONTH_PATTERN)) {
            LOGGER.info("Invalid expiration month entry. Please check the month entered (01-12 allowed only)");
            isGood = false;
        } else if (!cbExpirationDateYear.matches(Constants.CARD_EXPIRATION_YEAR_PATTERN)) {
            LOGGER.info("Invalid expiration year entry. Please check the year entered (20-99 allowed only)");
            isGood = false;
        } else if (!cbSecurityKey.matches(Constants.CARD_SECURITY_KEY_PATTERN)) {
            LOGGER.info("Invalid card security key. Please entry a number between 000 & 999.");
            isGood = false;
        }
        return isGood;
    }

    /**
     * This method service is used to make a personal payment with his bank card
     * informations.
     *
     * @param myEmail
     * @param amount
     * @param cbNumber
     * @param cbExpirationDateMonth
     * @param cbExpirationDateYear
     * @param cbSecurityKey
     * @return isDone boolean, true if personal payment success
     */
    public boolean makePersonalPayment(final String myEmail, final double amount, final String cbNumber,
            final String cbExpirationDateMonth, final String cbExpirationDateYear, final String cbSecurityKey) {
        boolean isDone = false;

        try {
            User user = userRepository.findByEmail(myEmail);
            AppAccount myAppAccount = user.getOwnAppAccount();

            if (myAppAccount != null) {
                boolean isGood = checkValidBankCardInformations(amount, cbNumber, cbExpirationDateMonth,
                        cbExpirationDateYear, cbSecurityKey);
                if (isGood == false) {
                    return isDone;
                }
                BigDecimal amountRounded = new BigDecimal(amount);
                amountRounded.setScale(2, RoundingMode.HALF_UP);
                double amountFinal = amountRounded.doubleValue();

                PersonalPayment myPayment = new PersonalPayment(myAppAccount, amountFinal, cbNumber,
                        cbExpirationDateMonth, cbExpirationDateYear, cbSecurityKey);
                personalPaymentRepository.save(myPayment);

                double beforeBalance = myAppAccount.getBalance();
                myAppAccount.setBalance(beforeBalance + amountFinal);
                appAccountRepository.save(myAppAccount);

                LOGGER.info("Personal payment done ! {}€ will be credited to your account.", amountRounded);
                isDone = true;
                return isDone;
            }
        } catch (NullPointerException np) {
            LOGGER.error("Null Pointer Exception" + np);
        }
        LOGGER.error("Personal payment failed. Please check the informations entered.");
        return isDone;
    }

}
