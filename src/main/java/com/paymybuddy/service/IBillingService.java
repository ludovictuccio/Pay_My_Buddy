package com.paymybuddy.service;

import java.math.BigDecimal;

public interface IBillingService {

    BigDecimal getInvoiceOfUsersTransactions(String email, String beginDate, String endDate);
}
