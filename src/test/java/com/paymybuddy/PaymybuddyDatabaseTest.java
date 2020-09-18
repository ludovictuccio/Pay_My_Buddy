package com.paymybuddy;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;

import com.paymybuddy.model.Transaction;
import com.paymybuddy.model.User;
import com.paymybuddy.repository.AppAccountRepository;
import com.paymybuddy.repository.TransactionRepository;
import com.paymybuddy.repository.UserRepository;
import com.paymybuddy.service.IBillingService;
import com.paymybuddy.service.IRelationService;
import com.paymybuddy.service.ITransferService;
import com.paymybuddy.service.IUserService;

@SpringBootTest
@TestPropertySource(locations = "classpath:application.properties")
@Sql(scripts = "classpath:dropAndCreate.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@Sql(scripts = {
        "classpath:dbTest.sql" }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class PaymybuddyDatabaseTest {

    @Autowired
    public IBillingService billingService;

    @Autowired
    public ITransferService transferService;

    @Autowired
    public IUserService userService;

    @Autowired
    public IRelationService relationService;

    @Autowired
    private AppAccountRepository appAccountRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private UserRepository userRepository;

    private static String date_10_07_20 = "10/07/2020";

    private static DateTimeFormatter formatter = DateTimeFormatter
            .ofPattern("dd/MM/yyyy");

    private static LocalDate localdate_10_07_20;

    @BeforeEach
    public void setUp() {
        localdate_10_07_20 = LocalDate.parse(date_10_07_20, formatter);
    }

    /**
     * Used to mark a half-second breakTime between two methods.
     *
     * @throws InterruptedException
     */
    private synchronized void halfSecondBreak() throws InterruptedException {
        final int HALF_SECOND = 500;
        Thread.sleep(HALF_SECOND);
    }

    @Test
    @Tag("USER")
    @DisplayName("Add & update")
    public void givenOneUser_whenAddAndUpdate_thenReturnCorrectValues()
            throws InterruptedException {
        // GIVEN
        User user = new User("Floyd", "Georges", "georges.floyd@gmail.com",
                "before-password", "0611111111");
        User userUpdated = new User("Floyd", "Georges",
                "georges.floyd@gmail.com", "after-password", "0699999999");

        // WHEN
        User result = userService.addNewUser(user);
        halfSecondBreak();
        boolean isUpdated = userService.updateUserInfos(userUpdated);

        // THEN
        assertThat(result).isNotNull();
        assertThat(isUpdated).isTrue();
        assertThat(result.getFirstname()).isEqualTo("Georges");
        assertThat(result.getLastname()).isEqualTo("Floyd");
        assertThat(userRepository.findByEmail("georges.floyd@gmail.com"))
                .isNotNull();

        assertThat(BCrypt.checkpw(userUpdated.getPassword(), userRepository
                .findByEmail("georges.floyd@gmail.com").getPassword()))
                        .isTrue();

        assertThat(result.getPhone()).isEqualTo("0611111111");
        assertThat(userRepository.findByEmail("georges.floyd@gmail.com")
                .getPhone()).isEqualTo("0699999999");
    }

    @Test
    @Tag("USER")
    @DisplayName("Add & login")
    public void givenOneUser_whenAddAndLogin_thenReturnCorrectValues()
            throws InterruptedException {
        // GIVEN
        User user = new User("New", "User", "new-user@gmail.com", "password",
                "0611111111");

        // WHEN
        User result = userService.addNewUser(user);
        halfSecondBreak();
        User userLoged = userService.login("new-user@gmail.com", "password");

        // THEN
        assertThat(result).isNotNull();
        assertThat(result.getFirstname()).isEqualTo("User");
        assertThat(userRepository.findByEmail("new-user@gmail.com"))
                .isNotNull();
        assertThat(userLoged).isNotNull();
    }

    @Test
    @Tag("TRANSFER")
    @DisplayName("Transaction & billing")
    public void givenOneTransaction_whenBillingService_thenReturnOK()
            throws InterruptedException {
        // GIVEN
        // "donald.trump@gmail.com" has relation with "kim.jong@gmail.com" in
        // dbTest

        // WHEN
        Transaction transaction = transferService.makeTransaction(
                "donald.trump@gmail.com", "kim.jong@gmail.com",
                new BigDecimal("10.00"), "transac");
        transaction.setTransactionDate(localdate_10_07_20);
        transactionRepository.save(transaction);

        halfSecondBreak();

        BigDecimal result = billingService.getInvoiceOfUsersTransactions(
                "donald.trump@gmail.com", "01/07/2020", "01/08/2020");

        // THEN
        assertThat(transaction).isNotNull();
        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(new BigDecimal("0.05"));
        assertThat(appAccountRepository.findById(1L).get().getBalance())
                .isEqualTo(new BigDecimal("490.00")); // was 500
        assertThat(appAccountRepository.findById(5L).get().getBalance())
                .isEqualTo(new BigDecimal("20.00"));// was 10
        assertThat(transactionRepository.count()).isEqualTo(1);
        assertThat(userRepository.findById(1L).get().getOwnAppAccount()
                .getSenderTransactions().size()).isEqualTo(1);
        assertThat(userRepository.findById(1L).get().getOwnAppAccount()
                .getBeneficiaryTransactions().size()).isEqualTo(0);
        assertThat(userRepository.findById(5L).get().getOwnAppAccount()
                .getSenderTransactions().size()).isEqualTo(0);
        assertThat(userRepository.findById(5L).get().getOwnAppAccount()
                .getBeneficiaryTransactions().size()).isEqualTo(1);
    }

    @Test
    @Tag("TRANSFER")
    @DisplayName("Personal payment & personal transfer")
    public void givenAnUser_whenPersonalPaymentAndTransfer_thenReturnTrue()
            throws InterruptedException {
        // GIVEN

        // WHEN
        // trump appaccount : was 500, now 1000
        boolean personalPayment = transferService.makePersonalPayment(
                "donald.trump@gmail.com", new BigDecimal("500.00"),
                "5136987895652036", "09", "23", "762");
        halfSecondBreak();
        boolean personalTransfer = transferService.makePersonalTransfer(
                "donald.trump@gmail.com", new BigDecimal("1000.00"),
                "CG57126645GSDHGFS", "08HDFGD5");

        // THEN
        assertThat(personalPayment).isTrue();
        assertThat(personalTransfer).isTrue();
        assertThat(appAccountRepository.findById(1L).get().getBalance())
                .isEqualTo(new BigDecimal("0.00")); // was 1000

    }

    @Test
    @Tag("RELATION")
    @DisplayName("Add & delete relation")
    public void givenAnyRelation_whenDeleteIt_thenReturnDeleted()
            throws InterruptedException {
        // GIVEN
        // user: relation with "kim.jong@gmail.com"
        User user = userRepository.findByEmail("donald.trump@gmail.com");
        User friend = userRepository.findByEmail("vlad.poutine@gmail.com");

        // WHEN
        boolean isAdded = relationService.addRelation("donald.trump@gmail.com",
                "vlad.poutine@gmail.com");
        assertThat(userRepository.findByEmail("donald.trump@gmail.com")
                .getPmbFriends().size()).isEqualTo(2);
        halfSecondBreak();
        boolean isDeleted = relationService.deleteRelation(
                "donald.trump@gmail.com", "vlad.poutine@gmail.com");

        // THEN
        assertThat(isAdded).isTrue();
        assertThat(isDeleted).isTrue();
        assertThat(user.getPmbFriends().size()).isEqualTo(1);
        assertThat(userRepository.findByEmail("donald.trump@gmail.com")
                .getPmbFriends().size()).isEqualTo(1);
        assertThat(friend.getPmbFriends().size()).isEqualTo(1);
        assertThat(userRepository.findByEmail("vlad.poutine@gmail.com")
                .getPmbFriends().size()).isEqualTo(1);
    }

}
