package com.paymybuddy.service;

/**
 * ITransferService interface class.
 *
 * @author Ludovic Tuccio
 */
public interface ITransferService {

    boolean makePersonalPayment(String myEmail, double amount, String cbNumber, String cbExpirationDateMonth,
            String cbExpirationDateYear, String cbSecurityKey);

}
