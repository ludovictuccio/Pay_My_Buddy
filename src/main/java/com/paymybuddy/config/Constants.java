package com.paymybuddy.config;

/**
 * Constants class.
 *
 * @author Ludovic Tuccio
 */
public class Constants {

    /**
     * Date pattern constant (Day, Month, Year)).
     */
    public static final String DATE_PATTERN = "dd/MM/yyyy";

    /**
     * Final static String for double pattern, used for the amount to send (between
     * 0 & 9999,99).
     */
    public static final String AMOUNT_PATTERN = "^[+]?(\\\\d{0,4})+(\\\\.{0,1}(\\\\d{0,2}))?$";

    /**
     * Final static String for IBAN pattern (letters & numbers, size: between 15 &
     * 31)
     */
    public static final String EUROPEAN_IBAN_PATTERN = "^[0-9A-Z]{15,31}$";

    /**
     * Final static String for BIC pattern (letters & numbers, size: between 8 & 11)
     */
    public static final String EUROPEAN_BIC_PATTERN = "^[0-9A-Z]{8,11}$";

    /**
     * Final static String for bank card number (between 13 for visa & 16 numbers
     * for mastercard allowed)
     */
    public static final String BANK_CARD_NUMBER_PATTERN = "^(?:(?<visa>4[0-9]{12}(?:[0-9]{3})?)|"
            + "(?<mastercard>5[1-5][0-9]{14})|" + "(?<discover>6(?:011|5[0-9]{2})[0-9]{12})|"
            + "(?<amex>3[47][0-9]{13})|" + "(?<diners>3(?:0[0-5]|[68][0-9])?[0-9]{11})|"
            + "(?<jcb>(?:2131|1800|35[0-9]{3})[0-9]{11}))$";

    /**
     * Final static String for bank card expiration month (between 01 & 12 allowed)
     */
    public static final String CARD_EXPIRATION_MONTH_PATTERN = "(^[1-9]$)|(^0[1-9]|1[0-2]$)";

    /**
     * Final static String for bank card expiration year (between 20 & 99 allowed)
     */
    public static final String CARD_EXPIRATION_YEAR_PATTERN = "([2-9][0-9])|([1-9])";

    /**
     * Final static String for bank card security key (between 000 & 999 allowed)
     */
    public static final String CARD_SECURITY_KEY_PATTERN = "([0-9][0-9][0-9])|([1-9])";

    /**
     * Final static variable for calculate the application commission rate.
     */
    public static final double PMB_COMMISSION_RATE = 0.05;

    /**
     * Final static variable for success status 200.
     */
    public static final int STATUS_OK_200 = 200;
    /**
     * Final static variable for created status 201.
     */
    public static final int STATUS_CREATED_201 = 201;
    /**
     * Final static variable for error status 404.
     */
    public static final int ERROR_NOT_FOUND_404 = 404;
    /**
     * Final static variable for conflict status 409.
     */
    public static final int ERROR_CONFLICT_409 = 409;

}
