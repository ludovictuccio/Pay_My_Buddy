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
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.annotation.DirtiesContext;

import com.paymybuddy.model.AppAccount;
import com.paymybuddy.model.User;
import com.paymybuddy.repository.AppAccountRepository;
import com.paymybuddy.repository.UserRepository;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
public class UserServiceTest {

    @Autowired
    public IUserService userService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private AppAccountRepository appAccountRepository;

    @MockBean
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    private static String encryptedPassword = "encrypted_password";
    private static String encryptedEmail = "encrypted_email";

    @Test
    @Tag("CREATE")
    @DisplayName("Create new user - OK")
    public void givenNewPerson_whenCreation_thenReturnPersonSavedInDb() {
        // GIVEN
        User user = new User("New", "User", "new-user@gmail.com", "love5", "0676990578");
        AppAccount userAccount = new AppAccount(user, new BigDecimal("0.00"));
        userAccount.setUserId(user);

        when(userRepository.save(user)).thenReturn(user);
        when(appAccountRepository.save(userAccount)).thenReturn(userAccount);
        when(bCryptPasswordEncoder.encode(user.getEmail())).thenReturn(encryptedEmail);
        when(bCryptPasswordEncoder.encode(user.getPassword())).thenReturn(encryptedPassword);

        // WHEN
        User result = userService.addNewUser(user);

        // THEN
        assertThat(result).isNotNull();
        assertThat(result.getLastname()).isEqualTo("New");
        assertThat(result.getFirstname()).isEqualTo("User");
        assertThat(result.getPassword().equals("love")).isFalse();// encrypted
        assertThat(result.getPassword()).isEqualTo(encryptedPassword);
        assertThat(result.getEmail().equals("new-user@gmail.com")).isFalse();// encrypted
        assertThat(result.getEmail()).isEqualTo(encryptedEmail);
        assertThat(result.getPhone()).isEqualTo("0676990578");
        assertThat(result.getPmbFriends()).isEmpty();
        assertThat(userAccount.getBalance()).isEqualTo(new BigDecimal("0.00"));
        assertThat(userAccount.getAppAccountId()).isEqualTo(user.getId());
    }

    @Test
    @Tag("CREATE")
    @DisplayName("Create new user - Ok - + insered")
    public void givenValidPhoneEntry_whenCreationWithSymbol_thenReturnNull() {
        // GIVEN
        User donaldTrump = new User("Trump", "Donald", "donald@gmail.com", "lovit", "+33679608456");
        AppAccount donaldAccount = new AppAccount(donaldTrump, new BigDecimal("0.00"));
        donaldAccount.setUserId(donaldTrump);

        when(userRepository.save(donaldTrump)).thenReturn(donaldTrump);
        when(appAccountRepository.save(donaldAccount)).thenReturn(donaldAccount);
        when(bCryptPasswordEncoder.encode(donaldTrump.getEmail())).thenReturn(encryptedEmail);
        when(bCryptPasswordEncoder.encode(donaldTrump.getPassword())).thenReturn(encryptedPassword);

        // WHEN
        User result = userService.addNewUser(donaldTrump);

        // THEN
        assertThat(result).isNotNull();
        assertThat(result.getLastname()).isEqualTo("Trump");
        assertThat(result.getFirstname()).isEqualTo("Donald");
        assertThat(result.getEmail().equals("new-user@gmail.com")).isFalse();// encrypted
        assertThat(result.getEmail()).isEqualTo(encryptedEmail);
        assertThat(result.getPassword().equals("love")).isFalse();// encrypted
        assertThat(result.getPassword()).isEqualTo(encryptedPassword);
        assertThat(result.getPhone()).isEqualTo("+33679608456");
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
        verify(userRepository, times(1)).findByEmail("donald.trump@gmail.com");
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
        verify(userRepository, times(1)).findByEmail(null);
        verify(userRepository, never()).save(donaldTrump);
    }

    @Test
    @Tag("CREATE")
    @DisplayName("Create new user - ERROR - Invalid email insered")
    public void givenInvalidEmailAddressEntry_whenCreation_thenReturnNull() {
        // GIVEN
        User donaldTrump = new User("Trump", "Donald", "dodo", "love-usa", "000111222");

        // WHEN
        User result = userService.addNewUser(donaldTrump);

        // THEN
        assertThat(result).isNull();
        verify(userRepository, times(1)).findByEmail(anyString());
        verify(userRepository, never()).save(donaldTrump);
    }

    @Test
    @Tag("CREATE")
    @DisplayName("Create new user - ERROR - Characters for phone number")
    public void givenInvalidPhoneNumberEntry_whenCreation_thenReturnNull() {
        // GIVEN
        User donaldTrump = new User("Trump", "Donald", "dodo", "love-usa", "bad-number");

        // WHEN
        User result = userService.addNewUser(donaldTrump);

        // THEN
        assertThat(result).isNull();
        verify(userRepository, times(1)).findByEmail(anyString());
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
        verify(userRepository, times(1)).findByEmail(anyString());
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
        verify(userRepository, times(1)).findByEmail(anyString());
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
        verify(userRepository, times(1)).findByEmail(anyString());
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
        verify(userRepository, times(1)).findByEmail(anyString());
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
        verify(userRepository, times(1)).findByEmail(anyString());
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
        verify(userRepository, times(1)).findByEmail(anyString());
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

    @Test
    @Tag("LOGIN")
    @DisplayName("Login - OK - Existant user & correct id")
    public void givenExistingUser_whenLoginWithCorrectId_thenReturnUser() {
        // GIVEN
        String password = BCrypt.hashpw("love", BCrypt.gensalt());

        User donaldTrump = new User("Trump", "Donald", "d.trump@gmail.com", password, "000111222");
        AppAccount donaldAccount = new AppAccount(donaldTrump, new BigDecimal("0.00"));
        donaldAccount.setUserId(donaldTrump);

        when(userRepository.save(donaldTrump)).thenReturn(donaldTrump);
        when(appAccountRepository.save(donaldAccount)).thenReturn(donaldAccount);
        when(userRepository.findByEmail("d.trump@gmail.com")).thenReturn(donaldTrump);

        // WHEN
        User result = userService.login("d.trump@gmail.com", "love");

        // THEN
        assertThat(result).isNotNull();
        assertThat(result.getLastname()).isEqualTo("Trump");
        assertThat(result.getFirstname()).isEqualTo("Donald");
        assertThat(result.getEmail()).isEqualTo("d.trump@gmail.com");
        assertThat(result.getPassword().equals("love")).isFalse();// encrypted
        assertThat(result.getPassword().equals(password)).isTrue();// encrypted
        assertThat(result.getPhone()).isEqualTo("000111222");
        assertThat(result.getPmbFriends()).isEmpty();
        assertThat(donaldAccount.getBalance()).isEqualTo(new BigDecimal("0.00"));
        assertThat(donaldAccount.getAppAccountId()).isEqualTo(donaldTrump.getId());
    }

    @Test
    @Tag("LOGIN")
    @DisplayName("Login - Error - Bad email")
    public void givenExistingUser_whenLoginWithBadEmail_thenReturnNull() {
        // GIVEN
        String password = BCrypt.hashpw("love", BCrypt.gensalt());

        User donaldTrump = new User("Trump", "Donald", "d.trump@gmail.com", password, "000111222");
        AppAccount donaldAccount = new AppAccount(donaldTrump, new BigDecimal("0.00"));
        donaldAccount.setUserId(donaldTrump);

        when(userRepository.save(donaldTrump)).thenReturn(donaldTrump);
        when(appAccountRepository.save(donaldAccount)).thenReturn(donaldAccount);
        when(userRepository.findByEmail("d.trump@gmail.com")).thenReturn(donaldTrump);

        // WHEN
        User result = userService.login("donald.trumpidou@gmail.com", "love");

        // THEN
        assertThat(result).isNull();
    }

    @Test
    @Tag("LOGIN")
    @DisplayName("Login - Error - Bad password")
    public void givenExistingUser_whenLoginWithBadPassword_thenReturnNull() {
        // GIVEN
        String password = BCrypt.hashpw("love", BCrypt.gensalt());

        User donaldTrump = new User("Trump", "Donald", "d.trump@gmail.com", password, "000111222");
        AppAccount donaldAccount = new AppAccount(donaldTrump, new BigDecimal("0.00"));
        donaldAccount.setUserId(donaldTrump);

        when(userRepository.save(donaldTrump)).thenReturn(donaldTrump);
        when(appAccountRepository.save(donaldAccount)).thenReturn(donaldAccount);
        when(userRepository.findByEmail("d.trump@gmail.com")).thenReturn(donaldTrump);

        // WHEN
        User result = userService.login("d.trump@gmail.com", "bad-password");

        // THEN
        assertThat(result).isNull();
    }

    @Test
    @Tag("LOGIN")
    @DisplayName("Login - Error - Null email")
    public void givenExistingUser_whenLoginWithNullEmail_thenReturnNull() {
        // GIVEN
        String password = BCrypt.hashpw("love", BCrypt.gensalt());

        User donaldTrump = new User("Trump", "Donald", "d.trump@gmail.com", password, "000111222");
        AppAccount donaldAccount = new AppAccount(donaldTrump, new BigDecimal("0.00"));
        donaldAccount.setUserId(donaldTrump);

        when(userRepository.save(donaldTrump)).thenReturn(donaldTrump);
        when(appAccountRepository.save(donaldAccount)).thenReturn(donaldAccount);
        when(userRepository.findByEmail("d.trump@gmail.com")).thenReturn(donaldTrump);

        // WHEN
        User result = userService.login(null, "love");

        // THEN
        assertThat(result).isNull();
    }

    @Test
    @Tag("LOGIN")
    @DisplayName("Login - Error - Empty email")
    public void givenExistingUser_whenLoginWithEmptyEmail_thenReturnNull() {
        // GIVEN
        String password = BCrypt.hashpw("love", BCrypt.gensalt());

        User donaldTrump = new User("Trump", "Donald", "d.trump@gmail.com", password, "000111222");
        AppAccount donaldAccount = new AppAccount(donaldTrump, new BigDecimal("0.00"));
        donaldAccount.setUserId(donaldTrump);

        when(userRepository.save(donaldTrump)).thenReturn(donaldTrump);
        when(appAccountRepository.save(donaldAccount)).thenReturn(donaldAccount);
        when(userRepository.findByEmail("d.trump@gmail.com")).thenReturn(donaldTrump);

        // WHEN
        User result = userService.login("", "love");

        // THEN
        assertThat(result).isNull();
    }

    @Test
    @Tag("LOGIN")
    @DisplayName("Login - Error - Null password")
    public void givenExistingUser_whenLoginWithNullPassword_thenReturnNull() {
        // GIVEN
        String password = BCrypt.hashpw("love", BCrypt.gensalt());

        User donaldTrump = new User("Trump", "Donald", "d.trump@gmail.com", password, "000111222");
        AppAccount donaldAccount = new AppAccount(donaldTrump, new BigDecimal("0.00"));
        donaldAccount.setUserId(donaldTrump);

        when(userRepository.save(donaldTrump)).thenReturn(donaldTrump);
        when(appAccountRepository.save(donaldAccount)).thenReturn(donaldAccount);
        when(userRepository.findByEmail("d.trump@gmail.com")).thenReturn(donaldTrump);

        // WHEN
        User result = userService.login("d.trump@gmail.com", null);

        // THEN
        assertThat(result).isNull();
    }

    @Test
    @Tag("LOGIN")
    @DisplayName("Login - Error - Empty password")
    public void givenExistingUser_whenLoginWithEmptyPassword_thenReturnNull() {
        // GIVEN
        String password = BCrypt.hashpw("love", BCrypt.gensalt());

        User donaldTrump = new User("Trump", "Donald", "d.trump@gmail.com", password, "000111222");
        AppAccount donaldAccount = new AppAccount(donaldTrump, new BigDecimal("0.00"));
        donaldAccount.setUserId(donaldTrump);

        when(userRepository.save(donaldTrump)).thenReturn(donaldTrump);
        when(appAccountRepository.save(donaldAccount)).thenReturn(donaldAccount);
        when(userRepository.findByEmail("d.trump@gmail.com")).thenReturn(donaldTrump);

        // WHEN
        User result = userService.login("d.trump@gmail.com", "");

        // THEN
        assertThat(result).isNull();
    }

    @Test
    @Tag("ACCOUNT")
    @DisplayName("AppAccount - OK - Hashcode and equals")
    public void testEquals_Symmetric() {
        // GIVEN
        String password = BCrypt.hashpw("love", BCrypt.gensalt());
        BigDecimal amount = new BigDecimal("0.00");
        User user = new User("Trump", "Donald", "d.trump@gmail.com", password, "000111222");

        // WHEN
        AppAccount x = new AppAccount(user, amount); // equals and hashCode check name field value
        AppAccount y = new AppAccount(user, amount);

        // THEN
        assertThat(x.equals(y) && y.equals(x)).isTrue();
        assertThat(x.hashCode() == y.hashCode()).isTrue();
    }

}
