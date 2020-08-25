package com.paymybuddy.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
import com.paymybuddy.service.IUserService;

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
        userRepository.save(donaldTrump);
        AppAccount appAccount = new AppAccount(donaldTrump, 0.0);
        appAccountRepository.save(appAccount);

        User existingEmailInDb = new User("Trumpidou", "Donaldidou", "donald.trump@gmail.com", "lovidou", "000111000");

        when(userRepository.findByEmail("donald.trump@gmail.com")).thenReturn(donaldTrump);
        // WHEN
        userService.addNewUser(existingEmailInDb);

        // THEN
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
        assertThat(result.getEmail()).isNull();
        assertThat(userRepository.count()).isEqualTo(0);
        assertThat(userRepository.findByEmail(donaldTrump.getEmail())).isNull();
    }

}
