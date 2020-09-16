package com.paymybuddy.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.paymybuddy.config.Constants;
import com.paymybuddy.model.AppAccount;
import com.paymybuddy.model.Transaction;
import com.paymybuddy.model.User;
import com.paymybuddy.repository.UserRepository;

@Service
public class BillingService implements IBillingService {

    private static final Logger LOGGER = LoggerFactory
            .getLogger("BillingService");

    @Autowired
    private UserRepository userRepository;

    @Value("${application.fee.rate}")
    private BigDecimal percentForFacturation;

    /**
     * Method used to retrieve all user's transaction between two dates
     * includes, and calculate the invoices (only for sender's transactions).
     *
     * @param email
     * @param beginDateString
     * @param endDateString
     * @return totalForFacturation a BigDecimal
     */
    public BigDecimal getInvoiceOfUsersTransactions(final String email,
            final String beginDateString, final String endDateString)
            throws NullPointerException {

        User user = userRepository.findByEmail(email);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        try {
            LocalDate beginDate = LocalDate.parse(beginDateString, formatter);
            LocalDate endDate = LocalDate.parse(endDateString, formatter);

            if (user != null) {
                AppAccount userAppAccount = user.getOwnAppAccount();
                List<Transaction> allTransactions = userAppAccount
                        .getSenderTransactions();

                if (allTransactions.isEmpty()) {
                    LOGGER.debug("No transaction found");
                    return Constants.INITIAL_ACCOUNT_AMOUNT;
                } else {
                    List<BigDecimal> allInvoices = new ArrayList<>();

                    for (Transaction transaction : allTransactions) {

                        if (transaction.getTransactionDate()
                                .isAfter(beginDate.minusDays(1))
                                && transaction.getTransactionDate()
                                        .isBefore(endDate.plusDays(1))) {

                            BigDecimal amountTransaction = transaction
                                    .getAmount();
                            BigDecimal percentToInvoice = amountTransaction
                                    .multiply(percentForFacturation);
                            allInvoices.add(percentToInvoice);
                        }
                    }
                    BigDecimal totalForFacturation = allInvoices.stream()
                            .reduce((x, y) -> x.add(y))
                            .orElse(Constants.INITIAL_ACCOUNT_AMOUNT);

                    LOGGER.info("Success billing transactions for user: "
                            + user.getEmail());
                    totalForFacturation = totalForFacturation.setScale(2,
                            RoundingMode.HALF_UP);
                    return totalForFacturation;
                }
            }
        } catch (Exception e) {
            LOGGER.error(
                    "Billing service impossible. Please check the entered dates, with format dd/MM/yyyy"
                            + e);
            return null;
        }
        LOGGER.error("FAIL billing transactions for user: {} , unknow email !",
                email);
        return null;
    }

}
