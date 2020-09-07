package com.paymybuddy.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Iterator;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.paymybuddy.config.Constants;
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

    @Autowired
    private PersonalTransferRepository personalTransferRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    /**
     * Method used to check that user's amount entry is between 1 & 9999,99
     *
     * @param amount
     * @return isGood boolean
     */
    public static boolean checkValidAmountEntry(final BigDecimal amount) {
        boolean isGood = true;

        BigDecimal minimimAmount = new BigDecimal("1.00");
        BigDecimal maximumAmount = new BigDecimal("9999.99");

        int compareMinimum = amount.compareTo(minimimAmount);
        int compareMaximum = amount.compareTo(maximumAmount);

        // if amount > user's account balance available
        if (compareMinimum == -1 || compareMaximum == 1) {
            LOGGER.info("Invalid amount entry. Please entry an amount between 1€ & 9999,99€.");
            isGood = false;
        }
        return isGood;
    }

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
    public static boolean checkValidBankCardInformations(final BigDecimal amount, final String cbNumber,
            final String cbExpirationDateMonth, final String cbExpirationDateYear, final String cbSecurityKey) {
        boolean isGood = true;
        boolean isGoodAmount = checkValidAmountEntry(amount);

        // if amount > user's account balance available
        if (!isGoodAmount) {
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
    public boolean makePersonalPayment(final String myEmail, BigDecimal amount, final String cbNumber,
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
                amount = amount.setScale(2, RoundingMode.HALF_UP);

                PersonalPayment myPayment = new PersonalPayment(myAppAccount, amount, cbNumber, cbExpirationDateMonth,
                        cbExpirationDateYear, cbSecurityKey);
                personalPaymentRepository.save(myPayment);

                BigDecimal beforeBalance = myAppAccount.getBalance();
                myAppAccount.setBalance(beforeBalance.add(amount));
                appAccountRepository.save(myAppAccount);

                LOGGER.info("Personal payment done ! {}€ will be credited to your account.", amount);
                isDone = true;
                return isDone;
            }
        } catch (NullPointerException np) {
            LOGGER.error("Null Pointer Exception" + np);
        }
        LOGGER.error("Personal payment failed. Please check the informations entered.");
        return isDone;
    }

    /**
     * Method used to check if the bank informations entered are corrects.
     *
     * @param amount
     * @param iban
     * @param bic
     * @return isDone boolean
     */
    public static boolean checkValidBankCardInformations(final BigDecimal amount, final String iban, final String bic) {
        boolean isDone = true;

        boolean isGoodAmount = checkValidAmountEntry(amount);

        // if amount > user's account balance available
        if (!isGoodAmount) {
            LOGGER.info("Invalid amount entry. Please entry an amount between 1€ & 9999,99€.");
            isDone = false;
        } else if (!iban.matches(Constants.EUROPEAN_IBAN_PATTERN)) {
            LOGGER.info("Invalid IBAN. Please check your entry.");
            isDone = false;
        } else if (!bic.matches(Constants.EUROPEAN_BIC_PATTERN)) {
            LOGGER.info("Invalid BIC entry. Please check your entry.");
            isDone = false;
        }
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
    public boolean makePersonalTransfer(final String myEmail, BigDecimal amount, final String iban, final String bic) {
        boolean isDone = false;

        try {
            User user = userRepository.findByEmail(myEmail);
            AppAccount myAppAccount = user.getOwnAppAccount();

            if (myAppAccount != null) {
                boolean isGood = checkValidBankCardInformations(amount, iban, bic);
                if (!isGood) {
                    return isDone;
                }

                amount = amount.setScale(2, RoundingMode.HALF_UP);
                BigDecimal checkAccountAmountAvailable = myAppAccount.getBalance();
                int compare = amount.compareTo(checkAccountAmountAvailable);

                // if amount > user's account balance available
                if (compare == 1) {
                    LOGGER.error("Transfer impossible: your account is not sufficiently funded. Amount available: {}"
                            + checkAccountAmountAvailable);
                    return isDone;
                }

                PersonalTransfer myTransfer = new PersonalTransfer(myAppAccount, amount, iban, bic);
                personalTransferRepository.save(myTransfer);

                BigDecimal beforeBalance = myAppAccount.getBalance();
                myAppAccount.setBalance(beforeBalance.subtract(amount));
                appAccountRepository.save(myAppAccount);

                LOGGER.info("Personal transfer done ! {}€ will be credited to your bank account.", amount);
                isDone = true;
                return isDone;
            }
        } catch (NullPointerException np) {
            LOGGER.error("Null Pointer Exception" + np);
        }
        LOGGER.error("Personal transfer failed. Please check the informations entered.");
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
     * @return isDone boolean, true if transaction success
     */
    public boolean makeTransaction(final String myEmail, final String emailBeneficiary, BigDecimal amount,
            final String description) {
        boolean isDone = false;
        try {
            User user = userRepository.findByEmail(myEmail);
            AppAccount myAppAccount = user.getOwnAppAccount();

            User myFriend = userRepository.findByEmail(emailBeneficiary);
            AppAccount myFriendsAppAccount = myFriend.getOwnAppAccount();

            if (myAppAccount != null && myFriendsAppAccount != null) {
                Set<User> myFriendsList = user.getPmbFriends();

                for (Iterator<User> iter = myFriendsList.iterator(); iter.hasNext();) {
                    User existingFriend = iter.next();
                    if (existingFriend.getEmail().matches(myFriend.getEmail())) {

                        amount = amount.setScale(2, RoundingMode.HALF_UP);
                        BigDecimal checkAccountAmountAvailable = myAppAccount.getBalance();
                        int compare = amount.compareTo(checkAccountAmountAvailable);

                        // if amount > appAccount amount available
                        if (compare == 1) {
                            LOGGER.error(
                                    "Transaction impossible: your account is not sufficiently funded. Amount available: {}"
                                            + checkAccountAmountAvailable);
                            return isDone;
                        }
                        LocalDate date = LocalDate.now();
                        Transaction transaction = new Transaction(myAppAccount, myFriendsAppAccount, amount,
                                description, date);
                        transactionRepository.save(transaction);

                        BigDecimal ownerBeforeBalance = myAppAccount.getBalance();
                        myAppAccount.setBalance(ownerBeforeBalance.subtract(amount));
                        appAccountRepository.save(myAppAccount);

                        BigDecimal friendsBeforeBalance = myFriendsAppAccount.getBalance();
                        myFriendsAppAccount.setBalance(friendsBeforeBalance.add(amount));
                        appAccountRepository.save(myFriendsAppAccount);

                        LOGGER.info("Transaction done ! {}€ will be credited to your friend's account.", amount);
                        isDone = true;
                        return isDone;
                    }
                }
            }
        } catch (NullPointerException np) {
            LOGGER.error("Null Pointer Exception" + np);
        }
        LOGGER.error("Transaction failed. Please check the informations entered.");
        return isDone;
    }

}
