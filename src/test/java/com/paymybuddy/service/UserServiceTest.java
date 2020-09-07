package com.paymybuddy.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.paymybuddy.model.AppAccount;
import com.paymybuddy.model.User;
import com.paymybuddy.repository.AppAccountRepository;
import com.paymybuddy.repository.UserRepository;

@SpringBootTest
@TestInstance(Lifecycle.PER_CLASS)
public class UserServiceTest {

    @Autowired
    public IUserService userService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private AppAccountRepository appAccountRepository;

    @Test
    @Tag("CREATE")
    @DisplayName("Create new user - OK")
    public void givenNewPerson_whenCreation_thenReturnPersonSavedInDb() {

        // GIVEN
        User donaldTrump = new User("Trump", "Donald", "donald@gmail.com", "love-usa", "000111222");

        // WHEN
        User result = userService.addNewUser(donaldTrump);

        // THEN
        verify(userRepository).save(any(User.class));
        verify(appAccountRepository).save(any(AppAccount.class));
        assertThat(result).isNotNull();
    }

    @Test
    @Tag("CREATE")
    @DisplayName("Create new user - ERROR - Email already exist")
    public void givenAlreadyExistingEmailPersonInDb_whenCreation_thenReturnPersonNotSavedInDb() {

        // GIVEN
        User donaldTrump = new User("Trump", "Donald", "donald.trump@gmail.com", "love-usa", "000111222");
        when(userRepository.save(donaldTrump)).thenReturn(donaldTrump);

        BigDecimal initialAmountBalance = new BigDecimal("0.00");
        AppAccount appAccount = new AppAccount(donaldTrump, initialAmountBalance);
        when(appAccountRepository.save(appAccount)).thenReturn(appAccount);

        User existingEmailInDb = new User("Trumpidou", "Donaldidou", "donald.trump@gmail.com", "lovidou", "000111000");

        when(userRepository.findByEmail("donald.trump@gmail.com")).thenReturn(donaldTrump);
        // WHEN
        // User result = userService.addNewUser(existingEmailInDb);

        // THEN
        assertThatNullPointerException().isThrownBy(() -> {
            userService.addNewUser(existingEmailInDb);
        });

        // assertThat(result.getId()).isNull();
        assertThat(userRepository.findByEmail("unknow.email@gmail.com")).isNull();
        assertThat(userRepository.findByEmail("donald.trump@gmail.com")).isNotNull();
    }

    @Test
    @Tag("CREATE")
    @DisplayName("Create new user - ERROR - Unexisting email insered")
    public void givenNullEmailAddressEntry_whenCreation_thenReturnNull() {

        // GIVEN
        User donaldTrump = new User("Trump", "Donald", null, "love-usa", "000111222");

        // WHEN
        User result = userService.addNewUser(donaldTrump);

        // THEN
        assertThat(result).isNull();
        assertThat(userRepository.count()).isEqualTo(0);
        assertThat(userRepository.findByEmail(donaldTrump.getEmail())).isNull();
    }

    @Test
    @Tag("UPDATE")
    @DisplayName("Update user - OK - Phone and password changes")
    public void givenExistingUser_whenUpdateCorrectValues_thenReturnTrue() {

        // GIVEN
        User user = new User("Trump", "Donald", "donald@gmail.com", "love-usa", "000111222");
        userRepository.save(user);
        User userToUpdate = new User("Trump", "Donald", "donald@gmail.com", "other", "9999");

        // WHEN
        when(userRepository.findByEmail("donald@gmail.com")).thenReturn(user);
        boolean isUpdated = userService.updateUserInfos(userToUpdate);

        // THEN
        assertThat(isUpdated).isTrue();
    }

    @Test
    @Tag("UPDATE")
    @DisplayName("Update user - ERROR - Infos not allowed changes (name & email)")
    public void givenExistingUser_whenUpdateBadValues_thenReturnFalse() {
        // GIVEN
        User user = new User("Trump", "Donald", "donald@gmail.com", "love-usa", "000111222");
        userRepository.save(user);
        User userToUpdate = new User("Georges", "Bush", "georgybushi@gmail.com", "other", "9999");

        // WHEN
        when(userRepository.findByEmail("donald@gmail.com")).thenReturn(user);
        boolean isUpdated = userService.updateUserInfos(userToUpdate);

        // THEN
        assertThat(isUpdated).isFalse();
    }

    @Test
    @Tag("DELETE")
    @DisplayName("Delete user - OK - Existing email")
    public void givenExistingUser_whenDeleteValidEmail_thenReturnTrue() {
        // GIVEN
        User user = new User("Trump", "Donald", "donald@gmail.com", "love-usa", "000111222");
        userRepository.save(user);
        BigDecimal initialAmountBalance = new BigDecimal("0.00");
        AppAccount appAccount = new AppAccount(user, initialAmountBalance);
        appAccountRepository.save(appAccount);

        // WHEN
        when(userRepository.findByEmail("donald@gmail.com")).thenReturn(user);
        boolean isDeleted = userService.deleteUser(user.getEmail());

        // THEN
        assertThat(isDeleted).isTrue();
    }

    @Test
    @Tag("DELETE")
    @DisplayName("Delete user - ERROR - Unexisting email")
    public void givenUnxistingUser_whenDeleteInvalidEmail_thenReturnFalse() {
        // GIVEN

        // WHEN
        boolean isDeleted = userService.deleteUser("non-existantemail@gmail.fr");

        // THEN
        assertThat(isDeleted).isFalse();
    }
}
