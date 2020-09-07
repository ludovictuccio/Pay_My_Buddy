package com.paymybuddy.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;

import com.paymybuddy.model.AppAccount;
import com.paymybuddy.model.User;

@SpringBootTest
@TestPropertySource(locations = "classpath:application.properties")
@Sql(scripts = "classpath:dropAndCreate.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@Sql(scripts = { "classpath:dbTest.sql" }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AppAccountRepository appAccountRepository;

    private static User userGeneric1 = new User("GENERIC1", "USER1", "generic1@gmail.com", "gen1", "0101010101");
    private static User userGeneric2 = new User("GENERIC2", "USER2", "generic2@gmail.com", "gen2", "0202020202");

    @Test
    @Tag("FindAllUsers")
    @DisplayName("FindAllUsers - size OK")
    public void givenSixPersonsSavedInDb_whenFindAll_thenReturnCorrectSize() {
        // GIVEN
        // WHEN
        userRepository.save(userGeneric1);
        List<User> users = userRepository.findAll();

        // THEN
        assertThat(users.size()).isEqualTo(6);
    }

    @Test
    @Tag("Save")
    @DisplayName("Users saved - OK")
    public void givenTwoNewUsers_whenSavedWithCorrectsValues_thenReturnPersonsSaved() {
        // GIVEN
        // WHEN
        User userSaved = userRepository.save(userGeneric1);
        User userSaved2 = userRepository.save(userGeneric2);

        // THEN
        assertThat(userSaved.getId()).isNotNull();
        assertThat(userSaved.getFirstname()).isEqualTo("USER1");
        assertThat(userSaved2.getId()).isNotNull();
        assertThat(userSaved2.getFirstname()).isEqualTo("USER2");
    }

    @Test
    @Tag("FindByEmail")
    @DisplayName("FindByEmail - OK")
    public void givenPersonsSavedInDb_whenFindByEmail_thenReturnExistingPersonsFound() {
        // GIVEN
        // WHEN
        userRepository.save(userGeneric1);

        // THEN
        assertThat(userRepository.findByEmail("unknowEmail@mail.com")).isNull(); // Inexistent
        assertThat(userRepository.findByEmail("generic1@gmail.com")).isNotNull(); // saved
        assertThat(userRepository.findByEmail("generic2@gmail.com")).isNull(); // not saved
        assertThat(userRepository.findByEmail("kim.jong@gmail.com")).isNotNull();// existing in dbTest
    }

    @Test
    @Tag("FindById")
    @DisplayName("FindById - OK")
    public void givenPersonsInDb_whenFindById_thenReturnCorrectValues() {

        // THEN
        assertThat(userRepository.findById(1L)).isNotNull();
        assertThat(userRepository.findById(5L)).isNotNull();
        assertThat(userRepository.findById(999L)).isEmpty();
    }

    @Test
    @Tag("deleteUserByEmail")
    @DisplayName("deleteUserByEmail - OK")
    public void givenUserInDb_whenDeleteAnEmail_thenReturnUserWithThisEmailDeleted() {
        // GIVEN
        userRepository.save(userGeneric1);
        // WHEN
        userRepository.deleteUserByEmail("generic1@gmail.com");

        // THEN
        List<User> users = userRepository.findAll();
        assertThat(users.size()).isEqualTo(5); // 5 in dbTest

        List<AppAccount> appAccounts = appAccountRepository.findAll();
        assertThat(appAccounts.size()).isEqualTo(5); // 5 in dbTest
    }

    @Test
    @Tag("deleteUserByEmail")
    @DisplayName("deleteUserByEmail - ERROR - Unknow email")
    public void givenUsersInDb_whenDeleteUnknowEmail_thenReturnDbSizeUnchanged() {
        // GIVEN

        // WHEN
        userRepository.deleteUserByEmail("UNKNOW-email@gmail.com");

        // THEN
        List<User> users = userRepository.findAll();
        assertThat(users.size()).isEqualTo(5); // 5 in dbTest

        List<AppAccount> appAccounts = appAccountRepository.findAll();
        assertThat(appAccounts.size()).isEqualTo(5); // 5 in dbTest
    }

}
