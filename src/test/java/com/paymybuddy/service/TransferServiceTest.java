package com.paymybuddy.service;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;

import com.paymybuddy.repository.AppAccountRepository;
import com.paymybuddy.repository.PersonalPaymentRepository;

@SpringBootTest
@TestPropertySource(locations = "classpath:application.properties")
@Sql(scripts = "classpath:dropAndCreate.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@Sql(scripts = { "classpath:dbTest.sql" }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class TransferServiceTest {

    @Autowired
    public ITransferService transferService;

    @Autowired
    private PersonalPaymentRepository personalPaymentRepository;

    @Autowired
    private AppAccountRepository appAccountRepository;

    @Test
    @Tag("PersonalPayment")
    @DisplayName("Personal Payment - Ok - Email valid")
    public void givenValidEmail_whenPersonalPayment_thenReturnTrue() {
        // GIVEN

        // WHEN
        boolean result = transferService.makePersonalPayment("donald.trump@gmail.com", 10, "5136483000003333", "09",
                "23", "799");
        // THEN
        assertThat(result).isTrue();
        assertThat(appAccountRepository.findById(1L).get().getBalance()).isEqualTo(510); // was 500
    }

    @Test
    @Tag("PersonalPayment")
    @DisplayName("Personal Payment - ERROR - Invalid email")
    public void givenInvalidEmail_whenPersonalPayment_thenReturnFalse() {
        // GIVEN

        // WHEN
        boolean result = transferService.makePersonalPayment("UNKNOW-EMAIL@gmail.com", 10, "5136483000003333", "09",
                "23", "799");
        // THEN
        assertThat(result).isFalse();
        assertThat(appAccountRepository.findById(1L).get().getBalance()).isEqualTo(500); // unchanged
    }

    @Test
    @Tag("PersonalPayment")
    @DisplayName("Personal Payment - OK - AMOUNT")
    public void givenCorrectAmount_whenPayment_thenReturnTrue() {
        // GIVEN

        // WHEN
        boolean result = transferService.makePersonalPayment("donald.trump@gmail.com", 950.80, "5136483000003333", "09",
                "23", "799");
        // THEN
        assertThat(result).isTrue();
        assertThat(appAccountRepository.findById(1L).get().getBalance()).isEqualTo(1450.80); // was 500
    }

    @Test
    @Tag("PersonalPayment")
    @DisplayName("Personal Payment - ERROR - AMOUNT")
    public void givenIncorrectAmount_whenPayment_thenReturnFalse() {
        // GIVEN

        // WHEN
        boolean result = transferService.makePersonalPayment("donald.trump@gmail.com", 19950.80, "5136483000003333",
                "09", "23", "799");
        // THEN
        assertThat(result).isFalse();
        assertThat(appAccountRepository.findById(1L).get().getBalance()).isEqualTo(500); // unchanged
    }

    @Test
    @Tag("PersonalPayment")
    @DisplayName("Personal Payment - OK - arround AMOUNT")
    public void givenIncorrectDecimalAmount_whenPayment_thenReturnTrue() {
        // GIVEN

        // WHEN
        boolean result = transferService.makePersonalPayment("manu.macron@gmail.com", 950.808, "5136483000003333", "09",
                "23", "799");
        // THEN
        assertThat(result).isTrue();
        assertThat(appAccountRepository.findById(2L).get().getBalance()).isEqualTo(1950.81); // was 1000
    }

    @Test
    @Tag("PersonalPayment")
    @DisplayName("Personal Payment - OK - arround AMOUNT")
    public void givenIncorrectDecimalAmount_whenArroundPayment_thenReturnTrue() {
        // GIVEN

        // WHEN
        boolean result = transferService.makePersonalPayment("manu.macron@gmail.com", 950.804, "5136483000003333", "09",
                "23", "799");
        // THEN
        assertThat(result).isTrue();
        assertThat(appAccountRepository.findById(2L).get().getBalance()).isEqualTo(1950.80); // was 1000
    }

    @Test
    @Tag("PersonalPayment")
    @DisplayName("Personal Payment - ERROR - negative AMOUNT")
    public void givenIncorrectNegativeAmount_whenPayment_thenReturnFalse() {
        // GIVEN

        // WHEN
        boolean result = transferService.makePersonalPayment("donald.trump@gmail.com", -300, "5136483000003333", "09",
                "23", "799");
        // THEN
        assertThat(result).isFalse();
        assertThat(appAccountRepository.findById(1L).get().getBalance()).isEqualTo(500); // unchanged
    }

    @Test
    @Tag("PersonalPayment")
    @DisplayName("Personal Payment - OK - CARD NUMBER")
    public void givenGoodCardNumber_whenPersonalPayment_thenReturnTrue() {
        // GIVEN

        // WHEN
        boolean result = transferService.makePersonalPayment("donald.trump@gmail.com", 50, "5136483000003333", "09",
                "23", "799");
        // THEN
        assertThat(result).isTrue();
        assertThat(appAccountRepository.findById(1L).get().getBalance()).isEqualTo(550);
    }

    @Test
    @Tag("PersonalPayment")
    @DisplayName("Personal Payment - ERROR - CARD NUMBER")
    public void givenInvalidCardNumber_whenPersonalPayment_thenReturnFalse() {
        // GIVEN

        // WHEN
        boolean result = transferService.makePersonalPayment("donald.trump@gmail.com", 50, "6987", "09", "23", "799");
        // THEN
        assertThat(result).isFalse();
        assertThat(appAccountRepository.findById(1L).get().getBalance()).isEqualTo(500);
    }

    @Test
    @Tag("PersonalPayment")
    @DisplayName("Personal Payment - OK - Expiration MONTH ")
    public void givenValidMonthEntry_whenPersonalPayment_thenReturnTrue() {
        // GIVEN

        // WHEN
        boolean result = transferService.makePersonalPayment("donald.trump@gmail.com", 50, "5136483000003333", "12",
                "23", "799");
        // THEN
        assertThat(result).isTrue();
        assertThat(appAccountRepository.findById(1L).get().getBalance()).isEqualTo(550);// was 500
    }

    @Test
    @Tag("PersonalPayment")
    @DisplayName("Personal Payment - ERROR - Expiration MONTH ")
    public void givenInvalidMonthEntry_whenPersonalPayment_thenReturnFalse() {
        // GIVEN

        // WHEN
        boolean result = transferService.makePersonalPayment("donald.trump@gmail.com", 50, "5136483000003333", "13",
                "23", "799");
        // THEN
        assertThat(result).isFalse();
        assertThat(appAccountRepository.findById(1L).get().getBalance()).isEqualTo(500);// unchanged
    }

    @Test
    @Tag("PersonalPayment")
    @DisplayName("Personal Payment - ERROR - Expiration MONTH ")
    public void givenInvalidMonthEntry_whenPersonalPayment_thenReturnFalse2() {
        // GIVEN

        // WHEN
        boolean result = transferService.makePersonalPayment("donald.trump@gmail.com", 50, "5136483000003333", "0",
                "23", "799");
        // THEN
        assertThat(result).isFalse();
        assertThat(appAccountRepository.findById(1L).get().getBalance()).isEqualTo(500);// unchanged
    }

    @Test
    @Tag("PersonalPayment")
    @DisplayName("Personal Payment - ERROR - Expiration MONTH ")
    public void givenInvalidMonthEntry_whenPersonalPayment_thenReturnFalse3() {
        // GIVEN

        // WHEN
        boolean result = transferService.makePersonalPayment("donald.trump@gmail.com", 50, "5136483000003333", "-2",
                "23", "799");
        // THEN
        assertThat(result).isFalse();
        assertThat(appAccountRepository.findById(1L).get().getBalance()).isEqualTo(500);// unchanged
    }

    @Test
    @Tag("PersonalPayment")
    @DisplayName("Personal Payment - OK - Expiration YEAR ")
    public void givenValidYear_whenPayment_thenReturnTrue() {
        // GIVEN

        // WHEN
        boolean result = transferService.makePersonalPayment("donald.trump@gmail.com", 50, "5136483000003333", "10",
                "25", "799");
        // THEN
        assertThat(result).isTrue();
        assertThat(appAccountRepository.findById(1L).get().getBalance()).isEqualTo(550);// was 500
    }

    @Test
    @Tag("PersonalPayment")
    @DisplayName("Personal Payment - ERROR - Expiration YEAR ")
    public void givenInvalidYear_whenPayment_thenReturnFalse() {
        // GIVEN

        // WHEN
        boolean result = transferService.makePersonalPayment("donald.trump@gmail.com", 50, "5136483000003333", "5",
                "19", "799");
        // THEN
        assertThat(result).isFalse();
        assertThat(appAccountRepository.findById(1L).get().getBalance()).isEqualTo(500);// unchanged
    }

    @Test
    @Tag("PersonalPayment")
    @DisplayName("Personal Payment - ERROR - Expiration YEAR ")
    public void givenInvalidYear_whenPayment_thenReturnFalse2() {
        // GIVEN

        // WHEN
        boolean result = transferService.makePersonalPayment("donald.trump@gmail.com", 50, "5136483000003333", "10",
                "-1", "799");
        // THEN
        assertThat(result).isFalse();
        assertThat(appAccountRepository.findById(1L).get().getBalance()).isEqualTo(500);// unchanged
    }

    @Test
    @Tag("PersonalPayment")
    @DisplayName("Personal Payment - ERROR - Expiration YEAR ")
    public void givenInvalidYear_whenPayment_thenReturnFalse3() {
        // GIVEN

        // WHEN
        boolean result = transferService.makePersonalPayment("donald.trump@gmail.com", 50, "5136483000003333", "5",
                "109", "799");
        // THEN
        assertThat(result).isFalse();
        assertThat(appAccountRepository.findById(1L).get().getBalance()).isEqualTo(500);// unchanged
    }

    @Test
    @Tag("PersonalPayment")
    @DisplayName("Personal Payment - OK - Security key ")
    public void givenValidKey_whenPayment_thenReturnTrue() {
        // GIVEN

        // WHEN
        boolean result = transferService.makePersonalPayment("donald.trump@gmail.com", 50, "5136483000003333", "4",
                "25", "001");
        // THEN
        assertThat(result).isTrue();
        assertThat(appAccountRepository.findById(1L).get().getBalance()).isEqualTo(550);// was 500
    }

    @Test
    @Tag("PersonalPayment")
    @DisplayName("Personal Payment - ERROR - Security key ")
    public void givenInvalidKey_whenPayment_thenReturnFalse() {
        // GIVEN

        // WHEN
        boolean result = transferService.makePersonalPayment("donald.trump@gmail.com", 50, "5136483000003333", "4",
                "26", "02");
        // THEN
        assertThat(result).isFalse();
        assertThat(appAccountRepository.findById(1L).get().getBalance()).isEqualTo(500);// unchanged
    }

    @Test
    @Tag("PersonalPayment")
    @DisplayName("Personal Payment - ERROR - Security key ")
    public void givenInvalidKey_whenPayment_thenReturnFalse2() {
        // GIVEN

        // WHEN
        boolean result = transferService.makePersonalPayment("donald.trump@gmail.com", 50, "5136483000003333", "11",
                "29", "1009");
        // THEN
        assertThat(result).isFalse();
        assertThat(appAccountRepository.findById(1L).get().getBalance()).isEqualTo(500);// unchanged
    }
}
