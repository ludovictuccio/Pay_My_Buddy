package com.paymybuddy.service;

import java.math.BigDecimal;

/**
 * ITransferService interface class.
 *
 * @author Ludovic Tuccio
 */
public interface ITransferService {

    boolean makePersonalPayment(String myEmail, BigDecimal amount, String cbNumber, String cbExpirationDateMonth,
            String cbExpirationDateYear, String cbSecurityKey);

    public boolean makePersonalTransfer(String myEmail, BigDecimal amount, String iban, String bic);

    public boolean makeTransaction(String myEmail, String emailBeneficiary, BigDecimal amount, String description);
}
