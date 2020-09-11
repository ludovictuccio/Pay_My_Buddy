package com.paymybuddy.service;

import java.math.BigDecimal;

import com.paymybuddy.model.Transaction;

/**
 * ITransferService interface class.
 *
 * @author Ludovic Tuccio
 */
public interface ITransferService {

    boolean makePersonalPayment(String myEmail, BigDecimal amount,
            String cbNumber, String cbExpirationDateMonth,
            String cbExpirationDateYear, String cbSecurityKey);

    boolean makePersonalTransfer(String myEmail, BigDecimal amount, String iban,
            String bic);

    Transaction makeTransaction(String myEmail, String emailBeneficiary,
            BigDecimal amount, String description);
}
