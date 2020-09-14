package com.paymybuddy.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.paymybuddy.model.AppAccount;
import com.paymybuddy.model.PersonalPayment;
import com.paymybuddy.model.PersonalTransfer;
import com.paymybuddy.model.Transaction;
import com.paymybuddy.model.User;
import com.paymybuddy.repository.AppAccountRepository;
import com.paymybuddy.repository.PersonalPaymentRepository;
import com.paymybuddy.repository.PersonalTransferRepository;
import com.paymybuddy.repository.TransactionRepository;
import com.paymybuddy.repository.UserRepository;

/**
 * TransferService class.
 *
 * @author Ludovic Tuccio
 */
@Service
public class TransferService implements ITransferService {

    private static final Logger LOGGER = LoggerFactory
            .getLogger("TransferService");

    @Autowired
    private IRelationService relationService;

    @Autowired
    private PersonalPaymentRepository personalPaymentRepository;

    @Autowired
    private AppAccountRepository appAccountRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PersonalTransferRepository personalTransferRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    /**
     * Validator used to validate javax constraints in model classes.
     */
    private Validator validator;

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
    @Transactional
    public boolean makePersonalPayment(final String myEmail, BigDecimal amount,
            final String cbNumber, final String cbExpirationDateMonth,
            final String cbExpirationDateYear, final String cbSecurityKey) {
        boolean isDone = false;

        try {
            User user = userRepository.findByEmail(myEmail);
            AppAccount myAppAccount = user.getOwnAppAccount();

            if (myAppAccount != null) {

                amount = amount.setScale(2, RoundingMode.HALF_UP);

                PersonalPayment myPayment = new PersonalPayment(myAppAccount,
                        amount, cbNumber, cbExpirationDateMonth,
                        cbExpirationDateYear, cbSecurityKey);

                // Check constraints violations
                ValidatorFactory factory = Validation
                        .buildDefaultValidatorFactory();
                validator = factory.getValidator();
                Set<ConstraintViolation<PersonalPayment>> constraintViolations = validator
                        .validate(myPayment);
                if (constraintViolations.size() > 0) {
                    LOGGER.error(
                            "ERROR: a constraint was violated. Please check the informations entered.");
                    return isDone;
                }
                personalPaymentRepository.save(myPayment);

                BigDecimal beforeBalance = myAppAccount.getBalance();
                myAppAccount.setBalance(beforeBalance.add(amount));
                appAccountRepository.save(myAppAccount);

                LOGGER.info(
                        "Personal payment done ! {}€ will be credited to your account.",
                        amount);
                isDone = true;
                return isDone;
            }
        } catch (NullPointerException np) {
            LOGGER.error("Null Pointer Exception" + np);
            return isDone;
        }
        LOGGER.error(
                "Personal payment failed. Please check the informations entered.");
        return isDone;
    }

    /**
     * This method service is used to make a personal transfer with his bank
     * informations (iban & bic).
     *
     * @param myEmail
     * @param amount
     * @param iban
     * @param bic
     * @return isDone boolean, true if personal transfer success
     */
    @Transactional
    public boolean makePersonalTransfer(final String myEmail, BigDecimal amount,
            final String iban, final String bic) {
        boolean isDone = false;

        try {
            User user = userRepository.findByEmail(myEmail);
            AppAccount myAppAccount = user.getOwnAppAccount();

            amount = amount.setScale(2, RoundingMode.HALF_UP);
            BigDecimal checkAccountAmountAvailable = myAppAccount.getBalance();
            int compare = amount.compareTo(checkAccountAmountAvailable);

            // if amount > user's account balance available
            if (compare > 0) {
                LOGGER.error(
                        "Transfer impossible: your account is not sufficiently funded. Amount available: {}"
                                + checkAccountAmountAvailable);
                return isDone;
            }

            PersonalTransfer myTransfer = new PersonalTransfer(myAppAccount,
                    amount, iban, bic);

            // Check constraints violations
            ValidatorFactory factory = Validation
                    .buildDefaultValidatorFactory();
            validator = factory.getValidator();
            Set<ConstraintViolation<PersonalTransfer>> constraintViolations = validator
                    .validate(myTransfer);
            if (constraintViolations.size() > 0) {
                LOGGER.error(
                        "ERROR: a constraint was violated. Please check the informations entered.");
                return isDone;
            }
            personalTransferRepository.save(myTransfer);

            BigDecimal beforeBalance = myAppAccount.getBalance();
            myAppAccount.setBalance(beforeBalance.subtract(amount));
            appAccountRepository.save(myAppAccount);

            LOGGER.info(
                    "Personal transfer done ! {}€ will be credited to your bank account.",
                    amount);
            isDone = true;
            return isDone;

        } catch (NullPointerException np) {
            LOGGER.error("Null Pointer Exception" + np);
        }
        LOGGER.error(
                "Personal transfer failed. Please check the informations entered.");
        return isDone;
    }

    /**
     * This method service is used to make transaction between two user's app
     * accounts.
     *
     * @param myEmail
     * @param emailBeneficiary
     * @param amount
     * @param description
     * @param transactionDate
     * @return transaction a Transaction object
     */
    @Transactional
    public Transaction makeTransaction(final String myEmail,
            final String emailBeneficiary, BigDecimal amount,
            final String description) throws NumberFormatException {
        try {
            User user = userRepository.findByEmail(myEmail);
            AppAccount myAppAccount = user.getOwnAppAccount();

            AppAccount myFriendsAppAccount = relationService
                    .getRelationAppAccount(myEmail, emailBeneficiary);

            if (myAppAccount != null && myFriendsAppAccount != null) {

                amount = amount.setScale(2, RoundingMode.HALF_UP);
                BigDecimal checkAccountAmountAvailable = myAppAccount
                        .getBalance();
                int compare = amount.compareTo(checkAccountAmountAvailable);

                // if amount > appAccount amount available
                if (compare > 0) {
                    LOGGER.error(
                            "Transaction impossible: your account is not sufficiently funded. Amount available: {}"
                                    + checkAccountAmountAvailable);
                    return null;
                }
                Transaction transaction = new Transaction(myAppAccount,
                        myFriendsAppAccount, amount, description,
                        LocalDate.now());

                // Check constraints violations
                ValidatorFactory factory = Validation
                        .buildDefaultValidatorFactory();
                validator = factory.getValidator();
                Set<ConstraintViolation<Transaction>> constraintViolations = validator
                        .validate(transaction);
                if (constraintViolations.size() > 0) {
                    LOGGER.error(
                            "ERROR: a constraint was violated. Please check the informations entered.");
                    return null;
                }
                transactionRepository.save(transaction);

                BigDecimal ownerBeforeBalance = myAppAccount.getBalance();
                myAppAccount.setBalance(ownerBeforeBalance.subtract(amount));
                appAccountRepository.save(myAppAccount);

                BigDecimal friendsBeforeBalance = myFriendsAppAccount
                        .getBalance();
                myFriendsAppAccount
                        .setBalance(friendsBeforeBalance.add(amount));
                appAccountRepository.save(myFriendsAppAccount);

                LOGGER.info(
                        "Transaction done ! {}€ will be credited to your friend's account.",
                        amount);
                return transaction;
            }
        } catch (NullPointerException np) {
            LOGGER.error("Null Pointer Exception" + np);
            return null;
        }
        LOGGER.error(
                "Transaction failed. Please check the informations entered.");
        return null;
    }

}
