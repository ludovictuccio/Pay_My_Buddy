package com.paymybuddy.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;

import com.paymybuddy.model.Transaction;
import com.paymybuddy.model.User;
import com.paymybuddy.repository.AppAccountRepository;
import com.paymybuddy.repository.TransactionRepository;
import com.paymybuddy.repository.UserRepository;

@SpringBootTest
@TestPropertySource(locations = "classpath:application.properties")
@Sql(scripts = "classpath:dropAndCreate.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@Sql(scripts = { "classpath:dbTest.sql" }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class BillingServiceTest {

    @Autowired
    public IBillingService billingService;

    @Autowired
    public ITransferService transferService;

    @Autowired
    private AppAccountRepository appAccountRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private UserRepository userRepository;

    private static String date_10_07_20 = "10/07/2020";
    private static String date_05_06_20 = "05/06/2020";
    private static String date_31_07_20 = "10/07/2020";

    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private static LocalDate localdate_10_07_20, localdate_05_06_20, localdate_31_07_20;

    @BeforeEach
    public void setUp() {
        localdate_10_07_20 = LocalDate.parse(date_10_07_20, formatter);
        localdate_05_06_20 = LocalDate.parse(date_05_06_20, formatter);
        localdate_31_07_20 = LocalDate.parse(date_31_07_20, formatter);
    }

    @Test
    @Tag("DATE")
    @DisplayName("ERROR - Bad day entry")
    public void givenOneTransaction_whenDadDayEntry_thenReturnNull() {
        // GIVEN
        User user = userRepository.findByEmail("donald.trump@gmail.com");// relation with "kim.jong@gmail.com"

        Transaction transaction1 = transferService.makeTransaction(user.getEmail(), "kim.jong@gmail.com",
                new BigDecimal("10.00"), "transac 1");
        transaction1.setTransactionDate(localdate_05_06_20);
        transactionRepository.save(transaction1);

        // WHEN
        BigDecimal result = billingService.getInvoiceOfUsersTransactions(user.getEmail(), "41/07/2020", "01/08/2020");
        // THEN
        assertThat(result).isNull();
        assertThat(appAccountRepository.findById(1L).get().getBalance()).isEqualTo(new BigDecimal("490.00")); // was 500
        assertThat(appAccountRepository.findById(5L).get().getBalance()).isEqualTo(new BigDecimal("20.00"));// was 10
        assertThat(transactionRepository.count()).isEqualTo(1);
        assertThat(userRepository.findById(1L).get().getOwnAppAccount().getSenderTransactions().size()).isEqualTo(1);
        assertThat(userRepository.findById(1L).get().getOwnAppAccount().getBeneficiaryTransactions().size())
                .isEqualTo(0);
        assertThat(userRepository.findById(5L).get().getOwnAppAccount().getSenderTransactions().size()).isEqualTo(0);
        assertThat(userRepository.findById(5L).get().getOwnAppAccount().getBeneficiaryTransactions().size())
                .isEqualTo(1);
    }

    @Test
    @Tag("DATE")
    @DisplayName("ERROR - Bad Month entry")
    public void givenOneTransaction_whenDadMonthEntry_thenReturnNull() {
        // GIVEN
        User user = userRepository.findByEmail("donald.trump@gmail.com");// relation with "kim.jong@gmail.com"

        Transaction transaction1 = transferService.makeTransaction(user.getEmail(), "kim.jong@gmail.com",
                new BigDecimal("10.00"), "transac 1");
        transaction1.setTransactionDate(localdate_05_06_20);
        transactionRepository.save(transaction1);

        // WHEN
        BigDecimal result = billingService.getInvoiceOfUsersTransactions(user.getEmail(), "01/13/2020", "01/08/2020");
        // THEN
        assertThat(result).isNull();
        assertThat(appAccountRepository.findById(1L).get().getBalance()).isEqualTo(new BigDecimal("490.00")); // was 500
        assertThat(appAccountRepository.findById(5L).get().getBalance()).isEqualTo(new BigDecimal("20.00"));// was 10
        assertThat(transactionRepository.count()).isEqualTo(1);
        assertThat(userRepository.findById(1L).get().getOwnAppAccount().getSenderTransactions().size()).isEqualTo(1);
        assertThat(userRepository.findById(1L).get().getOwnAppAccount().getBeneficiaryTransactions().size())
                .isEqualTo(0);
        assertThat(userRepository.findById(5L).get().getOwnAppAccount().getSenderTransactions().size()).isEqualTo(0);
        assertThat(userRepository.findById(5L).get().getOwnAppAccount().getBeneficiaryTransactions().size())
                .isEqualTo(1);
    }

    @Test
    @Tag("DATE")
    @DisplayName("ERROR - Bad year entry")
    public void givenOneTransaction_whenDadYearEntry_thenReturnNull() {
        // GIVEN
        User user = userRepository.findByEmail("donald.trump@gmail.com");// relation with "kim.jong@gmail.com"

        Transaction transaction1 = transferService.makeTransaction(user.getEmail(), "kim.jong@gmail.com",
                new BigDecimal("10.00"), "transac 1");
        transaction1.setTransactionDate(localdate_05_06_20);
        transactionRepository.save(transaction1);

        // WHEN
        BigDecimal result = billingService.getInvoiceOfUsersTransactions(user.getEmail(), "01/07/20", "01/08/2020");

        // THEN
        assertThat(result).isNull();
        assertThat(appAccountRepository.findById(1L).get().getBalance()).isEqualTo(new BigDecimal("490.00")); // was 500
        assertThat(appAccountRepository.findById(5L).get().getBalance()).isEqualTo(new BigDecimal("20.00"));// was 10
        assertThat(transactionRepository.count()).isEqualTo(1);
        assertThat(userRepository.findById(1L).get().getOwnAppAccount().getSenderTransactions().size()).isEqualTo(1);
        assertThat(userRepository.findById(1L).get().getOwnAppAccount().getBeneficiaryTransactions().size())
                .isEqualTo(0);
        assertThat(userRepository.findById(5L).get().getOwnAppAccount().getSenderTransactions().size()).isEqualTo(0);
        assertThat(userRepository.findById(5L).get().getOwnAppAccount().getBeneficiaryTransactions().size())
                .isEqualTo(1);
    }

    @Test
    @Tag("DATE")
    @DisplayName("ERROR - Bad email entry")
    public void givenOneTransaction_whenBadEmailEntry_thenReturnNullPointerException() {

        User user = userRepository.findByEmail("bad-email@gmail.com");

        assertThatNullPointerException().isThrownBy(() -> {
            billingService.getInvoiceOfUsersTransactions(user.getEmail(), "01/07/20", "01/08/2020");
        });
    }

    @Test
    @Tag("DATE")
    @DisplayName("OK - 5 transaction - 3 between dates")
    public void givenFiveTransactions_whenTryToRetrieveThreeTransactions_thenReturnCorrectValues() {
        // GIVEN
        User user = userRepository.findByEmail("donald.trump@gmail.com");// relation with "kim.jong@gmail.com"

        Transaction transaction1 = transferService.makeTransaction(user.getEmail(), "kim.jong@gmail.com",
                new BigDecimal("10.00"), "transac 1");
        transaction1.setTransactionDate(localdate_05_06_20);
        transactionRepository.save(transaction1);

        Transaction transaction2 = transferService.makeTransaction(user.getEmail(), "kim.jong@gmail.com",
                new BigDecimal("18.00"), "transac 2");
        transaction2.setTransactionDate(localdate_10_07_20);
        transactionRepository.save(transaction2);

        Transaction transaction3 = transferService.makeTransaction(user.getEmail(), "kim.jong@gmail.com",
                new BigDecimal("100.00"), "transac 3");
        transaction3.setTransactionDate(localdate_31_07_20);
        transactionRepository.save(transaction3);

        Transaction transaction4 = transferService.makeTransaction(user.getEmail(), "kim.jong@gmail.com",
                new BigDecimal("8.50"), "transac 4");
        transaction4.setTransactionDate(localdate_05_06_20);
        transactionRepository.save(transaction4);

        Transaction transaction5 = transferService.makeTransaction(user.getEmail(), "kim.jong@gmail.com",
                new BigDecimal("55.60"), "transac 5");
        transaction5.setTransactionDate(localdate_31_07_20);
        transactionRepository.save(transaction5);
        // total for dates: 173.60 & invoices 0.868 around 0.87

        // WHEN
        BigDecimal result = billingService.getInvoiceOfUsersTransactions(user.getEmail(), "01/07/2020", "01/08/2020");
        // THEN
        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(new BigDecimal("0.87"));
        assertThat(appAccountRepository.findById(1L).get().getBalance()).isEqualTo(new BigDecimal("307.90")); // was 500
        assertThat(appAccountRepository.findById(5L).get().getBalance()).isEqualTo(new BigDecimal("202.10"));// was 10
        assertThat(transactionRepository.count()).isEqualTo(5);
        assertThat(userRepository.findById(1L).get().getOwnAppAccount().getSenderTransactions().size()).isEqualTo(5);
        assertThat(userRepository.findById(1L).get().getOwnAppAccount().getBeneficiaryTransactions().size())
                .isEqualTo(0);
        assertThat(userRepository.findById(5L).get().getOwnAppAccount().getSenderTransactions().size()).isEqualTo(0);
        assertThat(userRepository.findById(5L).get().getOwnAppAccount().getBeneficiaryTransactions().size())
                .isEqualTo(5);
    }

    @Test
    @Tag("DATE")
    @DisplayName("OK - 2 transaction - 0 between dates")
    public void givenTwoTransactions_whenTryToRetrieveWithZeroInGoodDates_thenReturnCorrectValue() {
        // GIVEN
        User user = userRepository.findByEmail("donald.trump@gmail.com");// relation with "kim.jong@gmail.com"

        Transaction transaction1 = transferService.makeTransaction(user.getEmail(), "kim.jong@gmail.com",
                new BigDecimal("10.00"), "transac 1");
        transaction1.setTransactionDate(localdate_05_06_20);
        transactionRepository.save(transaction1);

        Transaction transaction2 = transferService.makeTransaction(user.getEmail(), "kim.jong@gmail.com",
                new BigDecimal("10.00"), "transac 2");
        transaction2.setTransactionDate(localdate_05_06_20);
        transactionRepository.save(transaction2);
        // all total: 20.00 && invoice: 0.10

        // WHEN
        BigDecimal result = billingService.getInvoiceOfUsersTransactions(user.getEmail(), "01/05/2020", "01/06/2020");
        // THEN
        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(new BigDecimal("0.00"));
        assertThat(appAccountRepository.findById(1L).get().getBalance()).isEqualTo(new BigDecimal("480.00")); // was 500
        assertThat(appAccountRepository.findById(5L).get().getBalance()).isEqualTo(new BigDecimal("30.00"));// was 10
        assertThat(transactionRepository.count()).isEqualTo(2);
        assertThat(userRepository.findById(1L).get().getOwnAppAccount().getSenderTransactions().size()).isEqualTo(2);
        assertThat(userRepository.findById(1L).get().getOwnAppAccount().getBeneficiaryTransactions().size())
                .isEqualTo(0);
        assertThat(userRepository.findById(5L).get().getOwnAppAccount().getSenderTransactions().size()).isEqualTo(0);
        assertThat(userRepository.findById(5L).get().getOwnAppAccount().getBeneficiaryTransactions().size())
                .isEqualTo(2);
    }

    @Test
    @DisplayName("OK - One transaction")
    public void givenTwoFriends_whenOneTransaction_thenReturnCorrectValue() {
        // GIVEN
        User user = userRepository.findByEmail("donald.trump@gmail.com");// relation with "kim.jong@gmail.com"

        Transaction transaction1 = transferService.makeTransaction(user.getEmail(), "kim.jong@gmail.com",
                new BigDecimal("10.00"), "transac 1");
        transaction1.setTransactionDate(localdate_10_07_20);
        transactionRepository.save(transaction1);

        // WHEN
        BigDecimal result = billingService.getInvoiceOfUsersTransactions(user.getEmail(), "01/07/2020", "01/08/2020");
        // THEN
        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(new BigDecimal("0.05"));
        assertThat(appAccountRepository.findById(1L).get().getBalance()).isEqualTo(new BigDecimal("490.00")); // was 500
        assertThat(appAccountRepository.findById(5L).get().getBalance()).isEqualTo(new BigDecimal("20.00"));// was 10
        assertThat(transactionRepository.count()).isEqualTo(1);
        assertThat(userRepository.findById(1L).get().getOwnAppAccount().getSenderTransactions().size()).isEqualTo(1);
        assertThat(userRepository.findById(1L).get().getOwnAppAccount().getBeneficiaryTransactions().size())
                .isEqualTo(0);
        assertThat(userRepository.findById(5L).get().getOwnAppAccount().getSenderTransactions().size()).isEqualTo(0);
        assertThat(userRepository.findById(5L).get().getOwnAppAccount().getBeneficiaryTransactions().size())
                .isEqualTo(1);
    }

    @Test
    @DisplayName("OK - 5 transaction")
    public void givenTwoFriends_whenFiveTransactions_thenReturnCorrectValue() {
        // GIVEN
        User user = userRepository.findByEmail("donald.trump@gmail.com");// relation with "kim.jong@gmail.com"

        Transaction transaction1 = transferService.makeTransaction(user.getEmail(), "kim.jong@gmail.com",
                new BigDecimal("10.00"), "transac 1");
        transaction1.setTransactionDate(localdate_10_07_20);
        transactionRepository.save(transaction1);

        Transaction transaction2 = transferService.makeTransaction(user.getEmail(), "kim.jong@gmail.com",
                new BigDecimal("18.00"), "transac 2");
        transaction2.setTransactionDate(localdate_10_07_20);
        transactionRepository.save(transaction2);

        Transaction transaction3 = transferService.makeTransaction(user.getEmail(), "kim.jong@gmail.com",
                new BigDecimal("100.00"), "transac 3");
        transaction3.setTransactionDate(localdate_10_07_20);
        transactionRepository.save(transaction3);

        Transaction transaction4 = transferService.makeTransaction(user.getEmail(), "kim.jong@gmail.com",
                new BigDecimal("8.50"), "transac 4");
        transaction4.setTransactionDate(localdate_10_07_20);
        transactionRepository.save(transaction4);

        Transaction transaction5 = transferService.makeTransaction(user.getEmail(), "kim.jong@gmail.com",
                new BigDecimal("55.60"), "transac 5");
        transaction5.setTransactionDate(localdate_10_07_20);
        transactionRepository.save(transaction5);
        // all total: 192.10 && invoice: 0.9605 , around to 0.96

        // WHEN
        BigDecimal result = billingService.getInvoiceOfUsersTransactions(user.getEmail(), "01/07/2020", "01/08/2020");
        // THEN
        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(new BigDecimal("0.96"));
        assertThat(appAccountRepository.findById(1L).get().getBalance()).isEqualTo(new BigDecimal("307.90")); // was 500
        assertThat(appAccountRepository.findById(5L).get().getBalance()).isEqualTo(new BigDecimal("202.10"));// was 10
        assertThat(transactionRepository.count()).isEqualTo(5);
        assertThat(userRepository.findById(1L).get().getOwnAppAccount().getSenderTransactions().size()).isEqualTo(5);
        assertThat(userRepository.findById(1L).get().getOwnAppAccount().getBeneficiaryTransactions().size())
                .isEqualTo(0);
        assertThat(userRepository.findById(5L).get().getOwnAppAccount().getSenderTransactions().size()).isEqualTo(0);
        assertThat(userRepository.findById(5L).get().getOwnAppAccount().getBeneficiaryTransactions().size())
                .isEqualTo(5);
    }

    @Test
    @DisplayName("OK - 0 transaction - return 0 to pay")
    public void givenUserWithoutSenderTransactions_whenFacturation_thenReturnZero() {
        // GIVEN
        User user = userRepository.findByEmail("donald.trump@gmail.com");// relation with "kim.jong@gmail.com"

        // WHEN
        BigDecimal result = billingService.getInvoiceOfUsersTransactions(user.getEmail(), "01/07/2020", "01/08/2020");
        // THEN
        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(new BigDecimal("0.00"));
        assertThat(appAccountRepository.findById(1L).get().getBalance()).isEqualTo(new BigDecimal("500.00"));// was 500
        assertThat(transactionRepository.count()).isEqualTo(0);
        assertThat(userRepository.findById(1L).get().getOwnAppAccount().getSenderTransactions().size()).isEqualTo(0);
        assertThat(userRepository.findById(1L).get().getOwnAppAccount().getBeneficiaryTransactions().size())
                .isEqualTo(0);
    }

}
