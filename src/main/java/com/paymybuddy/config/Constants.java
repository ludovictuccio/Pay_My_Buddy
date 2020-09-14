package com.paymybuddy.config;

import java.math.BigDecimal;

/**
 * Constants class.
 *
 * @author Ludovic Tuccio
 */
public class Constants {

    /**
     * BigDecimal used to initialize the initial amount of the account.
     */
    public static final BigDecimal INITIAL_ACCOUNT_AMOUNT = new BigDecimal(
            "0.00");

    /**
     * Email regexp used to check valid email before the encryption.
     */
    public static final String EMAIL_REGEX = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
}
