package com.paymybuddy.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.DirtiesContext;

import com.paymybuddy.model.AppAccount;
import com.paymybuddy.model.User;
import com.paymybuddy.repository.AppAccountRepository;
import com.paymybuddy.repository.UserRepository;

@SpringBootTest
//@TestInstance(Lifecycle.PER_CLASS)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
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
        User donaldTrump = new User("New", "User", "new-user@gmail.com", "love", "000111222");
        AppAccount donaldAccount = new AppAccount(donaldTrump, new BigDecimal("0.00"));
        donaldAccount.setUserId(donaldTrump);

        when(userRepository.save(donaldTrump)).thenReturn(donaldTrump);
        when(appAccountRepository.save(donaldAccount)).thenReturn(donaldAccount);
        // WHEN
        User result = userService.addNewUser(donaldTrump);

        // THEN
        assertThat(result).isNotNull();
        assertThat(result.getLastname()).isEqualTo("New");
        assertThat(result.getFirstname()).isEqualTo("User");
        assertThat(result.getEmail()).isEqualTo("new-user@gmail.com");
        assertThat(result.getPassword()).isEqualTo("love");
        assertThat(result.getPhone()).isEqualTo("000111222");
        assertThat(result.getPmbFriends()).isEmpty();
        assertThat(donaldAccount.getBalance()).isEqualTo(new BigDecimal("0.00"));
        assertThat(donaldAccount.getAppAccountId()).isEqualTo(donaldTrump.getId());
    }

    @Test
    @Tag("CREATE")
    @DisplayName("Create new user - ERROR - Email already exist")
    public void givenAlreadyExistingEmailPersonInDb_whenCreation_thenReturnPersonNotSavedInDb() {
        // GIVEN
        User donaldTrump = new User("Trump", "Donald", "donald.trump@gmail.com", "love", "000111222");
        AppAccount donaldAccount = new AppAccount(donaldTrump, new BigDecimal("0.00"));
        donaldAccount.setUserId(donaldTrump);
        when(userRepository.save(donaldTrump)).thenReturn(donaldTrump);
        when(appAccountRepository.save(donaldAccount)).thenReturn(donaldAccount);

        User existingEmailInDb = new User("Trumpidou", "Donaldidou", "donald.trump@gmail.com", "lovidou", "000111000");
        when(userRepository.findByEmail("donald.trump@gmail.com")).thenReturn(donaldTrump);
        // WHEN
        User result = userService.addNewUser(existingEmailInDb);

        // THEN
        assertThat(result).isNull();
        verify(userRepository, times(1)).findByEmail(anyString());
        verify(userRepository, never()).save(existingEmailInDb);
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
        verify(userRepository, never()).findByEmail(anyString());
        verify(userRepository, never()).save(donaldTrump);
    }

    @Test
    @Tag("CREATE")
    @DisplayName("Create new user - ERROR - Invalid email insered")
    public void givenInvalidEmailAddressEntry_whenCreation_thenReturnNull() {
        // GIVEN
        User donaldTrump = new User("Trump", "Donald", "donald", "love-usa", "000111222");

        // WHEN
        User result = userService.addNewUser(donaldTrump);

        // THEN
        assertThat(result).isNull();
        verify(userRepository, never()).findByEmail(anyString());
        verify(userRepository, never()).save(donaldTrump);
    }

    @Test
    @Tag("CREATE")
    @DisplayName("Create new user - ERROR - Null firstanme insered")
    public void givenNullFirstnameEntry_whenCreation_thenReturnNull() {
        // GIVEN
        User donaldTrump = new User("Trump", "", "donald@gmail.com", "love-usa", "000111222");

        // WHEN
        User result = userService.addNewUser(donaldTrump);

        // THEN
        assertThat(result).isNull();
        verify(userRepository, never()).findByEmail(anyString());
        verify(userRepository, never()).save(donaldTrump);
    }

    @Test
    @Tag("CREATE")
    @DisplayName("Create new user - ERROR - Null lastname insered")
    public void givenNullFLastnameEntry_whenCreation_thenReturnNull() {
        // GIVEN
        User donaldTrump = new User("", "Donald", "donald@gmail.com", "love-usa", "000111222");

        // WHEN
        User result = userService.addNewUser(donaldTrump);

        // THEN
        assertThat(result).isNull();
        verify(userRepository, never()).findByEmail(anyString());
        verify(userRepository, never()).save(donaldTrump);
    }

    @Test
    @Tag("CREATE")
    @DisplayName("Create new user - ERROR - Null lastname insered")
    public void givenNullPasswordEntry_whenCreation_thenReturnNull() {
        // GIVEN
        User donaldTrump = new User("Trump", "Donald", "donald@gmail.com", "", "000111222");

        // WHEN
        User result = userService.addNewUser(donaldTrump);

        // THEN
        assertThat(result).isNull();
        verify(userRepository, never()).findByEmail(anyString());
        verify(userRepository, never()).save(donaldTrump);
    }

    @Test
    @Tag("CREATE")
    @DisplayName("Create new user - ERROR - Null phone insered")
    public void givenNullPhoneEntry_whenCreation_thenReturnNull() {
        // GIVEN
        User donaldTrump = new User("Trump", "Donald", "donald@gmail.com", "lovit", "");

        // WHEN
        User result = userService.addNewUser(donaldTrump);

        // THEN
        assertThat(result).isNull();
        verify(userRepository, never()).findByEmail(anyString());
        verify(userRepository, never()).save(donaldTrump);
    }

    @Test
    @Tag("CREATE")
    @DisplayName("Create new user - ERROR - Invalid minimum size phone insered")
    public void givenInvalidPhoneEntry_whenCreation_thenReturnNull() {
        // GIVEN
        User donaldTrump = new User("Trump", "Donald", "donald@gmail.com", "lovit", "023");

        // WHEN
        User result = userService.addNewUser(donaldTrump);

        // THEN
        assertThat(result).isNull();
        verify(userRepository, never()).findByEmail(anyString());
        verify(userRepository, never()).save(donaldTrump);
    }

    @Test
    @Tag("CREATE")
    @DisplayName("Create new user - ERROR - Invalid maximum size phone insered (17)")
    public void givenInvalidMaximumPhoneEntry_whenCreation_thenReturnNull() {
        // GIVEN
        User donaldTrump = new User("Trump", "Donald", "donald@gmail.com", "lovit", "12345678123456789");

        // WHEN
        User result = userService.addNewUser(donaldTrump);

        // THEN
        assertThat(result).isNull();
        verify(userRepository, never()).findByEmail(anyString());
        verify(userRepository, never()).save(donaldTrump);
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
        verify(userRepository, times(1)).findByEmail(anyString());
        assertThat(userRepository.findByEmail("donald@gmail.com").getPassword()).isEqualTo("other");
    }

    @Test
    @Tag("UPDATE")
    @DisplayName("Update user - ERROR - Infos not allowed changes (name)")
    public void givenExistingUser_whenUpdateBadValues_thenReturnFalse() {
        // GIVEN
        User user = new User("Trump", "Donald", "donald@gmail.com", "love-usa", "000111222");
        userRepository.save(user);
        User userToUpdate = new User("Georges", "Bush", "donald@gmail.com", "love-usa", "000111222");

        // WHEN
        when(userRepository.findByEmail("donald@gmail.com")).thenReturn(user);
        boolean isUpdated = userService.updateUserInfos(userToUpdate);

        // THEN
        assertThat(isUpdated).isFalse();
        assertThat(userRepository.findByEmail("donald@gmail.com").getFirstname()).isEqualTo("Donald");
        assertThat(userRepository.findByEmail("donald@gmail.com").getLastname()).isEqualTo("Trump");
    }

    @Test
    @Tag("UPDATE")
    @DisplayName("Update user - ERROR - Unknow user in DB")
    public void givenUnexistingUser_whenUpdateBadValues_thenReturnFalse() {
        // GIVEN
        User user = new User("Trump", "Donald", "donald@gmail.com", "love-usa", "000111222");
        userRepository.save(user);
        User userToUpdate = new User("Georges", "Bush", "georgybushi@gmail.com", "other", "9999");

        // WHEN
        when(userRepository.findByEmail("georgybushi@gmail.com")).thenReturn(null);
        boolean isUpdated = userService.updateUserInfos(userToUpdate);

        // THEN
        assertThat(isUpdated).isFalse();
    }

//    @Test
//    @Tag("DELETE")
//    @DisplayName("Delete user - OK - Existing email")
//    public void givenExistingUser_whenDeleteValidEmail_thenReturnTrue() {
//        // GIVEN
//        User user = new User("Trump", "Donald", "donald@gmail.com", "love-usa", "000111222");
//
//        when(userService.addNewUser(user)).thenReturn(user);
//        when(userRepository.findByEmail("donald@gmail.com")).thenReturn(user);
//
//        // WHEN
//        boolean isDeleted = userService.deleteUser(user.getEmail());
//
//        // THEN
//        assertThat(isDeleted).isTrue();
//        verify(userRepository, times(1)).deleteUserByEmail(anyString());
//    }
//
//    @Test
//    @Tag("DELETE")
//    @DisplayName("Delete user - ERROR - Unexisting email")
//    public void givenUnxistingUser_whenDeleteInvalidEmail_thenReturnFalse() {
//        // GIVEN
//        when(userRepository.findByEmail("non-existantemail@gmail.fr")).thenReturn(null);
//
//        // WHEN
//        boolean isDeleted = userService.deleteUser("non-existantemail@gmail.fr");
//
//        // THEN
//        assertThat(isDeleted).isFalse();
//        verify(userRepository, never()).deleteUserByEmail(anyString());
//    }
}
