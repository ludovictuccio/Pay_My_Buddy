package com.paymybuddy.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import java.math.BigDecimal;
import java.time.LocalDate;

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
import com.paymybuddy.repository.UserRepository;

@SpringBootTest
@TestPropertySource(locations = "classpath:application.properties")
@Sql(scripts = "classpath:dropAndCreate.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@Sql(scripts = { "classpath:dbTest.sql" }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class TransferServiceTest {

    @Autowired
    public ITransferService transferService;

    @Autowired
    private AppAccountRepository appAccountRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    @Tag("PersonalPayment")
    @DisplayName("Personal Payment - Ok - Email valid")
    public void givenValidEmail_whenPersonalPayment_thenReturnTrue() {
        // GIVEN
        BigDecimal amount = new BigDecimal("10.00");

        // WHEN
        boolean result = transferService.makePersonalPayment("donald.trump@gmail.com", amount, "5136483000003333", "09",
                "23", "799");
        // THEN
        assertThat(result).isTrue();
        assertThat(appAccountRepository.findById(1L).get().getBalance()).isEqualTo(new BigDecimal("510.00")); // was 500
    }

    @Test
    @Tag("PersonalPayment")
    @DisplayName("Personal Payment - ERROR - Invalid email")
    public void givenInvalidEmail_whenPersonalPayment_thenReturnFalse() {
        // GIVEN
        BigDecimal amount = new BigDecimal("10.00");

        // WHEN
        boolean result = transferService.makePersonalPayment("UNKNOW-EMAIL@gmail.com", amount, "5136483000003333", "09",
                "23", "799");
        // THEN
        assertThat(result).isFalse();
        assertThat(appAccountRepository.findById(1L).get().getBalance()).isEqualTo(new BigDecimal("500.00")); // unchanged
    }

    @Test
    @Tag("PersonalPayment")
    @DisplayName("Personal Payment - OK - AMOUNT")
    public void givenCorrectAmount_whenPayment_thenReturnTrue() {
        // GIVEN
        BigDecimal amount = new BigDecimal("950.80");

        // WHEN
        boolean result = transferService.makePersonalPayment("donald.trump@gmail.com", amount, "5136483000003333", "09",
                "23", "799");
        // THEN
        assertThat(result).isTrue();
        assertThat(appAccountRepository.findById(1L).get().getBalance()).isEqualTo(new BigDecimal("1450.80")); // was
                                                                                                               // 500
    }

    @Test
    @Tag("PersonalPayment")
    @DisplayName("Personal Payment - ERROR - AMOUNT")
    public void givenIncorrectAmount_whenPayment_thenReturnFalse() {
        // GIVEN
        BigDecimal amount = new BigDecimal("19950.80");

        // WHEN
        boolean result = transferService.makePersonalPayment("donald.trump@gmail.com", amount, "5136483000003333", "09",
                "23", "799");
        // THEN
        assertThat(result).isFalse();
        assertThat(appAccountRepository.findById(1L).get().getBalance()).isEqualTo(new BigDecimal("500.00")); // unchanged
    }

    @Test
    @Tag("PersonalPayment")
    @DisplayName("Personal Payment - OK - arround AMOUNT")
    public void givenIncorrectDecimalAmount_whenPayment_thenReturnTrue() {
        // GIVEN
        BigDecimal amount = new BigDecimal("950.808");

        // WHEN
        boolean result = transferService.makePersonalPayment("manu.macron@gmail.com", amount, "5136483000003333", "09",
                "23", "799");
        // THEN
        assertThat(result).isTrue();
        assertThat(appAccountRepository.findById(2L).get().getBalance()).isEqualTo(new BigDecimal("1950.81")); // was
                                                                                                               // 1000
    }

    @Test
    @Tag("PersonalPayment")
    @DisplayName("Personal Payment - OK - arround AMOUNT")
    public void givenIncorrectDecimalAmount_whenArroundPayment_thenReturnTrue() {
        // GIVEN
        BigDecimal amount = new BigDecimal("950.804");

        // WHEN
        boolean result = transferService.makePersonalPayment("manu.macron@gmail.com", amount, "5136483000003333", "09",
                "23", "799");
        // THEN
        assertThat(result).isTrue();
        assertThat(appAccountRepository.findById(2L).get().getBalance()).isEqualTo(new BigDecimal("1950.80")); // was
                                                                                                               // 1000
    }

    @Test
    @Tag("PersonalPayment")
    @DisplayName("Personal Payment - ERROR - negative AMOUNT")
    public void givenIncorrectNegativeAmount_whenPayment_thenReturnFalse() {
        // GIVEN
        BigDecimal amount = new BigDecimal("-300.00");

        // WHEN
        boolean result = transferService.makePersonalPayment("donald.trump@gmail.com", amount, "5136483000003333", "09",
                "23", "799");
        // THEN
        assertThat(result).isFalse();
        assertThat(appAccountRepository.findById(1L).get().getBalance()).isEqualTo(new BigDecimal("500.00")); // unchanged
    }

    @Test
    @Tag("PersonalPayment")
    @DisplayName("Personal Payment - OK - CARD NUMBER")
    public void givenGoodCardNumber_whenPersonalPayment_thenReturnTrue() {
        // GIVEN
        BigDecimal amount = new BigDecimal("50.00");

        // WHEN
        boolean result = transferService.makePersonalPayment("donald.trump@gmail.com", amount, "5136483000003333", "09",
                "23", "799");
        // THEN
        assertThat(result).isTrue();
        assertThat(appAccountRepository.findById(1L).get().getBalance()).isEqualTo(new BigDecimal("550.00"));
    }

    @Test
    @Tag("PersonalPayment")
    @DisplayName("Personal Payment - ERROR - CARD NUMBER")
    public void givenInvalidCardNumber_whenPersonalPayment_thenReturnFalse() {
        // GIVEN
        BigDecimal amount = new BigDecimal("50.00");

        // WHEN
        boolean result = transferService.makePersonalPayment("donald.trump@gmail.com", amount, "6987", "09", "23",
                "799");
        // THEN
        assertThat(result).isFalse();
        assertThat(appAccountRepository.findById(1L).get().getBalance()).isEqualTo(new BigDecimal("500.00"));
    }

    @Test
    @Tag("PersonalPayment")
    @DisplayName("Personal Payment - OK - Expiration MONTH ")
    public void givenValidMonthEntry_whenPersonalPayment_thenReturnTrue() {
        // GIVEN
        BigDecimal amount = new BigDecimal("50.00");

        // WHEN
        boolean result = transferService.makePersonalPayment("donald.trump@gmail.com", amount, "5136483000003333", "12",
                "23", "799");
        // THEN
        assertThat(result).isTrue();
        assertThat(appAccountRepository.findById(1L).get().getBalance()).isEqualTo(new BigDecimal("550.00"));// was 500
    }

    @Test
    @Tag("PersonalPayment")
    @DisplayName("Personal Payment - ERROR - Expiration MONTH ")
    public void givenInvalidMonthEntry_whenPersonalPayment_thenReturnFalse() {
        // GIVEN
        BigDecimal amount = new BigDecimal("50.00");

        // WHEN
        boolean result = transferService.makePersonalPayment("donald.trump@gmail.com", amount, "5136483000003333", "13",
                "23", "799");
        // THEN
        assertThat(result).isFalse();
        assertThat(appAccountRepository.findById(1L).get().getBalance()).isEqualTo(new BigDecimal("500.00"));// unchanged
    }

    @Test
    @Tag("PersonalPayment")
    @DisplayName("Personal Payment - ERROR - Expiration MONTH ")
    public void givenInvalidMonthEntry_whenPersonalPayment_thenReturnFalse2() {
        // GIVEN
        BigDecimal amount = new BigDecimal("50.00");

        // WHEN
        boolean result = transferService.makePersonalPayment("donald.trump@gmail.com", amount, "5136483000003333", "0",
                "23", "799");
        // THEN
        assertThat(result).isFalse();
        assertThat(appAccountRepository.findById(1L).get().getBalance()).isEqualTo(new BigDecimal("500.00"));// unchanged
    }

    @Test
    @Tag("PersonalPayment")
    @DisplayName("Personal Payment - ERROR - Expiration MONTH ")
    public void givenInvalidMonthEntry_whenPersonalPayment_thenReturnFalse3() {
        // GIVEN
        BigDecimal amount = new BigDecimal("50.00");

        // WHEN
        boolean result = transferService.makePersonalPayment("donald.trump@gmail.com", amount, "5136483000003333", "-2",
                "23", "799");
        // THEN
        assertThat(result).isFalse();
        assertThat(appAccountRepository.findById(1L).get().getBalance()).isEqualTo(new BigDecimal("500.00"));// unchanged
    }

    @Test
    @Tag("PersonalPayment")
    @DisplayName("Personal Payment - OK - Expiration YEAR ")
    public void givenValidYear_whenPayment_thenReturnTrue() {
        // GIVEN
        BigDecimal amount = new BigDecimal("50.00");

        // WHEN
        boolean result = transferService.makePersonalPayment("donald.trump@gmail.com", amount, "5136483000003333", "10",
                "25", "799");
        // THEN
        assertThat(result).isTrue();
        assertThat(appAccountRepository.findById(1L).get().getBalance()).isEqualTo(new BigDecimal("550.00"));// was 500
    }

    @Test
    @Tag("PersonalPayment")
    @DisplayName("Personal Payment - ERROR - Expiration YEAR ")
    public void givenInvalidYear_whenPayment_thenReturnFalse() {
        // GIVEN
        BigDecimal amount = new BigDecimal("50.00");

        // WHEN
        boolean result = transferService.makePersonalPayment("donald.trump@gmail.com", amount, "5136483000003333", "5",
                "19", "799");
        // THEN
        assertThat(result).isFalse();
        assertThat(appAccountRepository.findById(1L).get().getBalance()).isEqualTo(new BigDecimal("500.00"));// unchanged
    }

    @Test
    @Tag("PersonalPayment")
    @DisplayName("Personal Payment - ERROR - Expiration YEAR ")
    public void givenInvalidYear_whenPayment_thenReturnFalse2() {
        // GIVEN
        BigDecimal amount = new BigDecimal("50.00");

        // WHEN
        boolean result = transferService.makePersonalPayment("donald.trump@gmail.com", amount, "5136483000003333", "10",
                "-1", "799");
        // THEN
        assertThat(result).isFalse();
        assertThat(appAccountRepository.findById(1L).get().getBalance()).isEqualTo(new BigDecimal("500.00"));// unchanged
    }

    @Test
    @Tag("PersonalPayment")
    @DisplayName("Personal Payment - ERROR - Expiration YEAR ")
    public void givenInvalidYear_whenPayment_thenReturnFalse3() {
        // GIVEN
        BigDecimal amount = new BigDecimal("50.00");

        // WHEN
        boolean result = transferService.makePersonalPayment("donald.trump@gmail.com", amount, "5136483000003333", "5",
                "109", "799");
        // THEN
        assertThat(result).isFalse();
        assertThat(appAccountRepository.findById(1L).get().getBalance()).isEqualTo(new BigDecimal("500.00"));// unchanged
    }

    @Test
    @Tag("PersonalPayment")
    @DisplayName("Personal Payment - OK - Security key ")
    public void givenValidKey_whenPayment_thenReturnTrue() {
        // GIVEN
        BigDecimal amount = new BigDecimal("50.00");

        // WHEN
        boolean result = transferService.makePersonalPayment("donald.trump@gmail.com", amount, "5136483000003333", "4",
                "25", "001");
        // THEN
        assertThat(result).isTrue();
        assertThat(appAccountRepository.findById(1L).get().getBalance()).isEqualTo(new BigDecimal("550.00"));// was 500
    }

    @Test
    @Tag("PersonalPayment")
    @DisplayName("Personal Payment - ERROR - Security key ")
    public void givenInvalidKey_whenPayment_thenReturnFalse() {
        // GIVEN
        BigDecimal amount = new BigDecimal("50.00");

        // WHEN
        boolean result = transferService.makePersonalPayment("donald.trump@gmail.com", amount, "5136483000003333", "4",
                "26", "02");
        // THEN
        assertThat(result).isFalse();
        assertThat(appAccountRepository.findById(1L).get().getBalance()).isEqualTo(new BigDecimal("500.00"));// unchanged
    }

    @Test
    @Tag("PersonalPayment")
    @DisplayName("Personal Payment - ERROR - Security key ")
    public void givenInvalidKey_whenPayment_thenReturnFalse2() {
        // GIVEN
        BigDecimal amount = new BigDecimal("50.00");

        // WHEN
        boolean result = transferService.makePersonalPayment("donald.trump@gmail.com", amount, "5136483000003333", "11",
                "29", "1009");
        // THEN
        assertThat(result).isFalse();
        assertThat(appAccountRepository.findById(1L).get().getBalance()).isEqualTo(new BigDecimal("500.00"));// unchanged
    }

    @Test
    @Tag("PersonalTransfer")
    @DisplayName("Personal Transfer - Ok - Email valid")
    public void givenValidEmail_whenTransfer_thenReturnTrue() {
        // GIVEN
        BigDecimal amount = new BigDecimal("50.00");
        // WHEN
        boolean result = transferService.makePersonalTransfer("donald.trump@gmail.com", amount,
                "FR01010101010101010101010101FR", "BIC010101");
        // THEN
        assertThat(result).isTrue();
        assertThat(appAccountRepository.findById(1L).get().getBalance()).isEqualTo(new BigDecimal("450.00")); // was 500
    }

    @Test
    @Tag("PersonalTransfer")
    @DisplayName("Personal Transfer - ERROR - Email invalid")
    public void givenInvalidEmail_whenTransfer_thenReturnFalse() {
        // GIVEN
        BigDecimal amount = new BigDecimal("50.00");

        // WHEN
        boolean result = transferService.makePersonalTransfer("UNKNOW@gmail.com", amount,
                "FR01010101010101010101010101FR", "BIC010101");
        // THEN
        assertThat(result).isFalse();
        assertThat(appAccountRepository.findById(1L).get().getBalance()).isEqualTo(new BigDecimal("500.00")); // unchanged
    }

    @Test
    @Tag("PersonalTransfer")
    @DisplayName("Personal Transfer - Ok - Amount")
    public void givenCorrectAmount_whenTransfer_thenReturnTrue() {
        // GIVEN
        BigDecimal amount = new BigDecimal("400.80");

        // WHEN
        boolean result = transferService.makePersonalTransfer("donald.trump@gmail.com", amount,
                "FR01010101010101010101010101FR", "BIC010101");
        // THEN
        assertThat(result).isTrue();
        assertThat(appAccountRepository.findById(1L).get().getBalance()).isEqualTo(new BigDecimal("99.20")); // was 500
    }

    @Test
    @Tag("PersonalTransfer")
    @DisplayName("Personal Transfer - Ok - Amount, arround ok")
    public void givenIncorrectAmount_whenTransfer_thenArroundAndReturnTrue() {
        // GIVEN
        BigDecimal amount = new BigDecimal("200.809");

        // WHEN
        boolean result = transferService.makePersonalTransfer("donald.trump@gmail.com", amount,
                "FR01010101010101010101010101FR", "BIC010101");
        // THEN
        assertThat(result).isTrue();
        assertThat(appAccountRepository.findById(1L).get().getBalance()).isEqualTo(new BigDecimal("299.19")); // was 500
    }

    @Test
    @Tag("PersonalTransfer")
    @DisplayName("Personal Transfer - Ok - Amount, arround ok")
    public void givenCorrectAmount_whenTransferAll_thenArroundAndReturnTrue() {
        // GIVEN
        BigDecimal amount = new BigDecimal("500.00");

        // WHEN
        boolean result = transferService.makePersonalTransfer("donald.trump@gmail.com", amount,
                "FR01010101010101010101010101FR", "BIC010101");
        // THEN
        assertThat(result).isTrue();
        assertThat(appAccountRepository.findById(1L).get().getBalance()).isEqualTo(new BigDecimal("0.00")); // was 500
    }

    @Test
    @Tag("PersonalTransfer")
    @DisplayName("Personal Transfer - ERROR - Amount, -0.01")
    public void givenIncorrectAmount_whenTransferAll_thenArroundAndReturnTrue() {
        // GIVEN
        BigDecimal amount = new BigDecimal("500.01");

        // WHEN
        boolean result = transferService.makePersonalTransfer("donald.trump@gmail.com", amount,
                "FR01010101010101010101010101FR", "BIC010101");
        // THEN
        assertThat(result).isFalse();
        assertThat(appAccountRepository.findById(1L).get().getBalance()).isEqualTo(new BigDecimal("500.00")); // was 500
    }

    @Test
    @Tag("PersonalTransfer")
    @DisplayName("Personal Transfer - ERROR - Amount")
    public void givenIncorrectAmount_whenTransfer_thenReturnFalse() {
        // GIVEN
        BigDecimal amount = new BigDecimal("50500.80");

        // WHEN
        boolean result = transferService.makePersonalTransfer("donald.trump@gmail.com", amount,
                "FR01010101010101010101010101FR", "BIC010101");
        // THEN
        assertThat(result).isFalse();
        assertThat(appAccountRepository.findById(1L).get().getBalance()).isEqualTo(new BigDecimal("500.00")); // unchanged
    }

    @Test
    @Tag("PersonalTransfer")
    @DisplayName("Personal Transfer - ERROR - Amount")
    public void givenIncorrectNegativeAmount_whenTransfer_thenReturnFalse() {
        // GIVEN
        BigDecimal amount = new BigDecimal("-50.00");

        // WHEN
        boolean result = transferService.makePersonalTransfer("donald.trump@gmail.com", amount,
                "FR01010101010101010101010101FR", "BIC010101");
        // THEN
        assertThat(result).isFalse();
        assertThat(appAccountRepository.findById(1L).get().getBalance()).isEqualTo(new BigDecimal("500.00")); // unchanged
    }

    @Test
    @Tag("PersonalTransfer")
    @DisplayName("Personal Transfer - Ok - Iban, size maximum 31")
    public void givenIbanSizeMaximum_whenTransfer_thenReturnTrue() {
        // GIVEN
        BigDecimal amount = new BigDecimal("50.00");

        // WHEN
        boolean result = transferService.makePersonalTransfer("donald.trump@gmail.com", amount,
                "FR11111111111111111111111111111", "BIC010101");
        // THEN
        assertThat(result).isTrue();
        assertThat(appAccountRepository.findById(1L).get().getBalance()).isEqualTo(new BigDecimal("450.00")); // was 500
    }

    @Test
    @Tag("PersonalTransfer")
    @DisplayName("Personal Transfer - Ok - Iban, minimum size 15")
    public void givenIbanSizeMinimum_whenTransfer_thenReturnTrue() {
        // GIVEN
        BigDecimal amount = new BigDecimal("50.00");

        // WHEN
        boolean result = transferService.makePersonalTransfer("donald.trump@gmail.com", amount, "FR1111111111111",
                "BIC010101");
        // THEN
        assertThat(result).isTrue();
        assertThat(appAccountRepository.findById(1L).get().getBalance()).isEqualTo(new BigDecimal("450.00")); // was 500
    }

    @Test
    @Tag("PersonalTransfer")
    @DisplayName("Personal Transfer - ERROR - Iban size")
    public void givenIbanSizeInvalid_whenTransfer_thenReturnFalse() {
        // GIVEN
        BigDecimal amount = new BigDecimal("50.00");

        // WHEN
        boolean result = transferService.makePersonalTransfer("donald.trump@gmail.com", amount, "11", "BIC010101");
        // THEN
        assertThat(result).isFalse();
        assertThat(appAccountRepository.findById(1L).get().getBalance()).isEqualTo(new BigDecimal("500.00")); // unchanged
    }

    @Test
    @Tag("PersonalTransfer")
    @DisplayName("Personal Transfer - Ok - BIC, minimum size 8")
    public void givenMiniSizeBic_whenTransfer_thenReturnTrue() {
        // GIVEN
        BigDecimal amount = new BigDecimal("50.00");

        // WHEN
        boolean result = transferService.makePersonalTransfer("donald.trump@gmail.com", amount, "FR1111111111111",
                "BIC01234");
        // THEN
        assertThat(result).isTrue();
        assertThat(appAccountRepository.findById(1L).get().getBalance()).isEqualTo(new BigDecimal("450.00")); // was 500
    }

    @Test
    @Tag("PersonalTransfer")
    @DisplayName("Personal Transfer - Ok - BIC, maximum size 11")
    public void givenMaxiSizeBic_whenTransfer_thenReturnTrue() {
        // GIVEN
        BigDecimal amount = new BigDecimal("50.00");

        // WHEN
        boolean result = transferService.makePersonalTransfer("donald.trump@gmail.com", amount, "FR1111111111111",
                "BIC01234987");
        // THEN
        assertThat(result).isTrue();
        assertThat(appAccountRepository.findById(1L).get().getBalance()).isEqualTo(new BigDecimal("450.00")); // was 500
    }

    @Test
    @Tag("PersonalTransfer")
    @DisplayName("Personal Transfer - ERROR - BIC size")
    public void givenInvalidBic_whenTransfer_thenReturnTrue() {
        // GIVEN
        BigDecimal amount = new BigDecimal("50.00");

        // WHEN
        boolean result = transferService.makePersonalTransfer("donald.trump@gmail.com", amount, "FR1111111111111",
                "BIC0123498754584562");
        // THEN
        assertThat(result).isFalse();
        assertThat(appAccountRepository.findById(1L).get().getBalance()).isEqualTo(new BigDecimal("500.00")); // unchanged
    }

    @Test
    @Tag("Transaction")
    @DisplayName("Transaction - OK - Friends")
    public void givenTwoFriends_whenTransaction_thenReturnTrue() {
        // GIVEN
        User user = userRepository.findByEmail("donald.trump@gmail.com");
        User myFriend = userRepository.findByEmail("vlad.poutine@gmail.com");
        BigDecimal amount = new BigDecimal(100.05);
        user.addPmbFriends(myFriend);
        userRepository.save(user);

        // WHEN
        Transaction result = transferService.makeTransaction(user.getEmail(), myFriend.getEmail(), amount,
                "For my best friend");
        // THEN
        assertThat(result).isNotNull();
        assertThat(result.getTransactionDate()).isEqualTo(LocalDate.now());
        assertThat(result.getAmount()).isEqualTo(new BigDecimal("100.05"));
        assertThat(result.getDescription()).isEqualTo("For my best friend");
        assertThat(result.getId()).isNotNull();
        assertThat(appAccountRepository.findById(1L).get().getBalance()).isEqualTo(new BigDecimal("399.95")); // was 500
        assertThat(appAccountRepository.findById(4L).get().getBalance()).isEqualTo(new BigDecimal("2100.05"));// was
                                                                                                              // 2000
        assertThat(appAccountRepository.findById(1L).get().getSenderTransactions().size()).isEqualTo(1);// trump
        assertThat(appAccountRepository.findById(1L).get().getBeneficiaryTransactions().size()).isEqualTo(0);
        assertThat(appAccountRepository.findById(2L).get().getSenderTransactions().size()).isEqualTo(0);
        assertThat(appAccountRepository.findById(3L).get().getSenderTransactions().size()).isEqualTo(0);
        assertThat(appAccountRepository.findById(4L).get().getBeneficiaryTransactions().size()).isEqualTo(1);// poutine
        assertThat(appAccountRepository.findById(4L).get().getSenderTransactions().size()).isEqualTo(0);
        assertThat(appAccountRepository.findById(5L).get().getSenderTransactions().size()).isEqualTo(0);
    }

    @Test
    @Tag("Transaction")
    @DisplayName("Transaction - ERROR - No friends")
    public void givenTwoUserNoFriends_whenTryTransaction_thenReturnFalse() {
        // GIVEN
        User user = userRepository.findByEmail("donald.trump@gmail.com");
        User myFriend = userRepository.findByEmail("vlad.poutine@gmail.com");
        BigDecimal amount = new BigDecimal(100.05);

        // WHEN
        Transaction result = transferService.makeTransaction(user.getEmail(), myFriend.getEmail(), amount,
                "For my best friend");
        // THEN
        assertThat(result).isNull();
        assertThat(appAccountRepository.findById(1L).get().getBalance()).isEqualTo(new BigDecimal("500.00")); // unchanged
        assertThat(appAccountRepository.findById(4L).get().getBalance()).isEqualTo(new BigDecimal("2000.00"));
        assertThat(appAccountRepository.findById(1L).get().getSenderTransactions().size()).isEqualTo(0);
        assertThat(appAccountRepository.findById(1L).get().getBeneficiaryTransactions().size()).isEqualTo(0);
        assertThat(appAccountRepository.findById(4L).get().getSenderTransactions().size()).isEqualTo(0);
        assertThat(appAccountRepository.findById(4L).get().getBeneficiaryTransactions().size()).isEqualTo(0);
    }

    @Test
    @Tag("Transaction")
    @DisplayName("Transaction - Error - Amount negative")
    public void givenTwoFriends_whenTransactionWithNegativeAmount_thenReturnNull() {
        // GIVEN
        User user = userRepository.findByEmail("donald.trump@gmail.com");
        User myFriend = userRepository.findByEmail("vlad.poutine@gmail.com");
        BigDecimal amount = new BigDecimal("-100.05");
        user.addPmbFriends(myFriend);
        userRepository.save(user);

        // WHEN
        Transaction result = transferService.makeTransaction(user.getEmail(), myFriend.getEmail(), amount,
                "For my best friend");
        // THEN
        assertThat(result).isNull();
        assertThat(appAccountRepository.findById(1L).get().getBalance()).isEqualTo(new BigDecimal("500.00")); // unchanged
        assertThat(appAccountRepository.findById(4L).get().getBalance()).isEqualTo(new BigDecimal("2000.00"));
        assertThat(appAccountRepository.findById(1L).get().getSenderTransactions().size()).isEqualTo(0);
        assertThat(appAccountRepository.findById(1L).get().getBeneficiaryTransactions().size()).isEqualTo(0);
        assertThat(appAccountRepository.findById(4L).get().getSenderTransactions().size()).isEqualTo(0);
        assertThat(appAccountRepository.findById(4L).get().getBeneficiaryTransactions().size()).isEqualTo(0);
    }

    @Test
    @Tag("Transaction")
    @DisplayName("Transaction - Error - Null Amount")
    public void givenTwoFriends_whenTransactionWithNullAmount_thenReturnNull() {
        // GIVEN
        User user = userRepository.findByEmail("donald.trump@gmail.com");
        User myFriend = userRepository.findByEmail("vlad.poutine@gmail.com");
        user.addPmbFriends(myFriend);
        userRepository.save(user);

        // WHEN
        Transaction result = transferService.makeTransaction(user.getEmail(), myFriend.getEmail(), null,
                "For my best friend");
        // THEN
        assertThat(result).isNull();
        assertThat(appAccountRepository.findById(1L).get().getBalance()).isEqualTo(new BigDecimal("500.00")); // unchanged
        assertThat(appAccountRepository.findById(4L).get().getBalance()).isEqualTo(new BigDecimal("2000.00"));
        assertThat(appAccountRepository.findById(1L).get().getSenderTransactions().size()).isEqualTo(0);
        assertThat(appAccountRepository.findById(1L).get().getBeneficiaryTransactions().size()).isEqualTo(0);
        assertThat(appAccountRepository.findById(4L).get().getSenderTransactions().size()).isEqualTo(0);
        assertThat(appAccountRepository.findById(4L).get().getBeneficiaryTransactions().size()).isEqualTo(0);
    }

    @Test
    @Tag("Transaction")
    @DisplayName("Transaction - Error - Empty Amount")
    public void givenTwoFriends_whenTransactionWithEmptyAmount_thenReturnNull() {
        // GIVEN
        User user = userRepository.findByEmail("donald.trump@gmail.com");
        User myFriend = userRepository.findByEmail("vlad.poutine@gmail.com");
        user.addPmbFriends(myFriend);
        userRepository.save(user);

        // WHEN
        assertThatExceptionOfType(NumberFormatException.class).isThrownBy(() -> {
            transferService.makeTransaction(user.getEmail(), myFriend.getEmail(), new BigDecimal(""),
                    "For my best friend");
        });
        // THEN
        assertThat(appAccountRepository.findById(1L).get().getBalance()).isEqualTo(new BigDecimal("500.00")); // unchanged
        assertThat(appAccountRepository.findById(4L).get().getBalance()).isEqualTo(new BigDecimal("2000.00"));
        assertThat(appAccountRepository.findById(1L).get().getSenderTransactions().size()).isEqualTo(0);
        assertThat(appAccountRepository.findById(1L).get().getBeneficiaryTransactions().size()).isEqualTo(0);
        assertThat(appAccountRepository.findById(4L).get().getSenderTransactions().size()).isEqualTo(0);
        assertThat(appAccountRepository.findById(4L).get().getBeneficiaryTransactions().size()).isEqualTo(0);
    }

    @Test
    @Tag("Transaction")
    @DisplayName("Transaction - Error - Less than 1 euros")
    public void givenTwoFriends_whenTransactionWithAmountLessThanMinSize_thenReturnNull() {
        // GIVEN
        User user = userRepository.findByEmail("donald.trump@gmail.com");
        User myFriend = userRepository.findByEmail("vlad.poutine@gmail.com");
        BigDecimal amount = new BigDecimal("0.12");
        user.addPmbFriends(myFriend);
        userRepository.save(user);

        // WHEN
        Transaction result = transferService.makeTransaction(user.getEmail(), myFriend.getEmail(), amount,
                "For my best friend");
        // THEN
        assertThat(result).isNull();
        assertThat(appAccountRepository.findById(1L).get().getBalance()).isEqualTo(new BigDecimal("500.00")); // unchanged
        assertThat(appAccountRepository.findById(4L).get().getBalance()).isEqualTo(new BigDecimal("2000.00"));
        assertThat(appAccountRepository.findById(1L).get().getSenderTransactions().size()).isEqualTo(0);
        assertThat(appAccountRepository.findById(1L).get().getBeneficiaryTransactions().size()).isEqualTo(0);
        assertThat(appAccountRepository.findById(4L).get().getSenderTransactions().size()).isEqualTo(0);
        assertThat(appAccountRepository.findById(4L).get().getBeneficiaryTransactions().size()).isEqualTo(0);
    }

    @Test
    @Tag("Transaction")
    @DisplayName("Transaction - Error - 0 euros to tranfer")
    public void givenTwoFriends_whenTransactionWithAmountZero_thenReturnNull() {
        // GIVEN
        User user = userRepository.findByEmail("donald.trump@gmail.com");
        User myFriend = userRepository.findByEmail("vlad.poutine@gmail.com");
        BigDecimal amount = new BigDecimal("0.00");
        user.addPmbFriends(myFriend);
        userRepository.save(user);

        // WHEN
        Transaction result = transferService.makeTransaction(user.getEmail(), myFriend.getEmail(), amount,
                "For my best friend");
        // THEN
        assertThat(result).isNull();
        assertThat(appAccountRepository.findById(1L).get().getBalance()).isEqualTo(new BigDecimal("500.00")); // unchanged
        assertThat(appAccountRepository.findById(4L).get().getBalance()).isEqualTo(new BigDecimal("2000.00"));
        assertThat(appAccountRepository.findById(1L).get().getSenderTransactions().size()).isEqualTo(0);
        assertThat(appAccountRepository.findById(1L).get().getBeneficiaryTransactions().size()).isEqualTo(0);
        assertThat(appAccountRepository.findById(4L).get().getSenderTransactions().size()).isEqualTo(0);
        assertThat(appAccountRepository.findById(4L).get().getBeneficiaryTransactions().size()).isEqualTo(0);
    }

    @Test
    @Tag("Transaction")
    @DisplayName("Transaction - Error - Empty description ")
    public void givenTwoFriends_whenTransactionWithEmptyDescription_thenReturnNull() {
        // GIVEN
        User user = userRepository.findByEmail("donald.trump@gmail.com");
        User myFriend = userRepository.findByEmail("vlad.poutine@gmail.com");
        BigDecimal amount = new BigDecimal("2.00");
        user.addPmbFriends(myFriend);
        userRepository.save(user);

        // WHEN
        Transaction result = transferService.makeTransaction(user.getEmail(), myFriend.getEmail(), amount, "");
        // THEN
        assertThat(result).isNull();
        assertThat(appAccountRepository.findById(1L).get().getBalance()).isEqualTo(new BigDecimal("500.00")); // unchanged
        assertThat(appAccountRepository.findById(4L).get().getBalance()).isEqualTo(new BigDecimal("2000.00"));
        assertThat(appAccountRepository.findById(1L).get().getSenderTransactions().size()).isEqualTo(0);
        assertThat(appAccountRepository.findById(1L).get().getBeneficiaryTransactions().size()).isEqualTo(0);
        assertThat(appAccountRepository.findById(4L).get().getSenderTransactions().size()).isEqualTo(0);
        assertThat(appAccountRepository.findById(4L).get().getBeneficiaryTransactions().size()).isEqualTo(0);
    }

    @Test
    @Tag("Transaction")
    @DisplayName("Transaction - Error - Null description ")
    public void givenTwoFriends_whenTransactionWithNullDescription_thenReturnNull() {
        // GIVEN
        User user = userRepository.findByEmail("donald.trump@gmail.com");
        User myFriend = userRepository.findByEmail("vlad.poutine@gmail.com");
        BigDecimal amount = new BigDecimal("2.00");
        user.addPmbFriends(myFriend);
        userRepository.save(user);

        // WHEN
        Transaction result = transferService.makeTransaction(user.getEmail(), myFriend.getEmail(), amount, null);
        // THEN
        assertThat(result).isNull();
        assertThat(appAccountRepository.findById(1L).get().getBalance()).isEqualTo(new BigDecimal("500.00")); // unchanged
        assertThat(appAccountRepository.findById(4L).get().getBalance()).isEqualTo(new BigDecimal("2000.00"));
        assertThat(appAccountRepository.findById(1L).get().getSenderTransactions().size()).isEqualTo(0);
        assertThat(appAccountRepository.findById(1L).get().getBeneficiaryTransactions().size()).isEqualTo(0);
        assertThat(appAccountRepository.findById(4L).get().getSenderTransactions().size()).isEqualTo(0);
        assertThat(appAccountRepository.findById(4L).get().getBeneficiaryTransactions().size()).isEqualTo(0);
    }

    @Test
    @Tag("Transaction")
    @DisplayName("Transaction - Error - No own AppAccount")
    public void givenNoOwnerAppAccount_whenTransaction_thenReturnNull() {
        // GIVEN
        User user = userRepository.findByEmail("donald.trump@gmail.com");
        appAccountRepository.findById(1L).get().setUserId(null); // set null own app account
        User myFriend = userRepository.findByEmail("vlad.poutine@gmail.com");
        BigDecimal amount = new BigDecimal(100.05);
        userRepository.save(user);

        // WHEN
        Transaction result = transferService.makeTransaction(user.getEmail(), myFriend.getEmail(), amount,
                "For my best friend");
        // THEN
        assertThat(result).isNull();
        assertThat(appAccountRepository.findById(1L).get().getBalance()).isEqualTo(new BigDecimal("500.00"));
        assertThat(appAccountRepository.findById(4L).get().getBalance()).isEqualTo(new BigDecimal("2000.00"));
        assertThat(appAccountRepository.findById(1L).get().getSenderTransactions().size()).isEqualTo(0);
        assertThat(appAccountRepository.findById(1L).get().getBeneficiaryTransactions().size()).isEqualTo(0);
        assertThat(appAccountRepository.findById(2L).get().getSenderTransactions().size()).isEqualTo(0);
        assertThat(appAccountRepository.findById(3L).get().getSenderTransactions().size()).isEqualTo(0);
        assertThat(appAccountRepository.findById(4L).get().getBeneficiaryTransactions().size()).isEqualTo(0);
        assertThat(appAccountRepository.findById(4L).get().getSenderTransactions().size()).isEqualTo(0);
        assertThat(appAccountRepository.findById(5L).get().getSenderTransactions().size()).isEqualTo(0);
    }

    @Test
    @Tag("Transaction")
    @DisplayName("Transaction - Error - No friend AppAccount")
    public void givenNoFriendsAppAccount_whenTransaction_thenReturnNull() {
        // GIVEN
        User user = userRepository.findByEmail("donald.trump@gmail.com");
        User myFriend = userRepository.findByEmail("vlad.poutine@gmail.com");
        appAccountRepository.findById(4L).get().setUserId(null); // set null friends app account

        BigDecimal amount = new BigDecimal(100.05);
        userRepository.save(user);
        userRepository.save(myFriend);

        // WHEN
        Transaction result = transferService.makeTransaction(user.getEmail(), myFriend.getEmail(), amount,
                "For my best friend");

        // THEN
        assertThat(result).isNull();
        assertThat(appAccountRepository.findById(1L).get().getBalance()).isEqualTo(new BigDecimal("500.00"));
        assertThat(appAccountRepository.findById(4L).get().getBalance()).isEqualTo(new BigDecimal("2000.00"));
        assertThat(appAccountRepository.findById(1L).get().getSenderTransactions().size()).isEqualTo(0);
        assertThat(appAccountRepository.findById(1L).get().getBeneficiaryTransactions().size()).isEqualTo(0);
        assertThat(appAccountRepository.findById(2L).get().getSenderTransactions().size()).isEqualTo(0);
        assertThat(appAccountRepository.findById(3L).get().getSenderTransactions().size()).isEqualTo(0);
        assertThat(appAccountRepository.findById(4L).get().getBeneficiaryTransactions().size()).isEqualTo(0);
        assertThat(appAccountRepository.findById(4L).get().getSenderTransactions().size()).isEqualTo(0);
        assertThat(appAccountRepository.findById(5L).get().getSenderTransactions().size()).isEqualTo(0);
    }

    @Test
    @Tag("Transaction")
    @DisplayName("Transaction - ERROR - Insufficient account amount")
    public void givenATransaction_whenTryToSendAmountSuperiorTantMyAccountAmountAvailable_thenReturnFalse() {
        // GIVEN
        User user = userRepository.findByEmail("donald.trump@gmail.com");
        User myFriend = userRepository.findByEmail("vlad.poutine@gmail.com");
        BigDecimal amount = new BigDecimal(500.05);
        user.addPmbFriends(myFriend);
        userRepository.save(user);

        // WHEN
        Transaction result = transferService.makeTransaction(user.getEmail(), myFriend.getEmail(), amount,
                "For my best friend");
        // THEN
        assertThat(result).isNull();
        assertThat(appAccountRepository.findById(1L).get().getBalance()).isEqualTo(new BigDecimal("500.00")); // unchanged
        assertThat(appAccountRepository.findById(4L).get().getBalance()).isEqualTo(new BigDecimal("2000.00"));// unchanged
        assertThat(appAccountRepository.findById(1L).get().getSenderTransactions().size()).isEqualTo(0);// trump
        assertThat(appAccountRepository.findById(1L).get().getBeneficiaryTransactions().size()).isEqualTo(0);
        assertThat(appAccountRepository.findById(2L).get().getSenderTransactions().size()).isEqualTo(0);
        assertThat(appAccountRepository.findById(3L).get().getSenderTransactions().size()).isEqualTo(0);
        assertThat(appAccountRepository.findById(4L).get().getBeneficiaryTransactions().size()).isEqualTo(0);// poutine
        assertThat(appAccountRepository.findById(4L).get().getSenderTransactions().size()).isEqualTo(0);
        assertThat(appAccountRepository.findById(5L).get().getSenderTransactions().size()).isEqualTo(0);
    }

}
