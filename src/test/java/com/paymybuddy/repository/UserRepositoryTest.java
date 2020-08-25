package com.paymybuddy.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.paymybuddy.model.User;

//@TestPropertySource(locations = "classpath:application-test.properties")
//@DataJpaTest
@SpringBootTest
@AutoConfigureTestDatabase
@ActiveProfiles("test")
public class UserRepositoryTest {

//    @Autowired
//    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AppAccountRepository appAccountRepository;

    private User user = new User("New", "person1", "newperson@gmail.com", "love", "000111222");

//    @Before
//    public void setup() {
//        entityManager.persist(user);
//        entityManager.flush();
//    }

    @Test
    public void testFindAllUsers() {
        userRepository.save(user);
        List<User> users = userRepository.findAll();

        assertThat(users.size()).isEqualTo(1);

    }

    @Test
    public void testSaveUser() {

        User user2 = new User("New", "person2", "newperson2@gmail.com", "love", "000111222");

        User userSaved = userRepository.save(user);
        User userSaved2 = userRepository.save(user2);

        assertThat(userSaved.getId()).isNotNull();
        assertThat(userSaved.getFirstname()).isEqualTo("person1");
        assertThat(userSaved2.getId()).isNotNull();
        assertThat(userSaved2.getFirstname()).isEqualTo("person2");

    }

    @Test
    @Tag("CREATE")
    @DisplayName("FindByEmail - OK")
    public void givenNewPerson_whenCreation_thenReturnPersonSavedInDb() {

        // GIVEN
        User newPerson = new User("New", "Person", "newperson@gmail.com", "love", "000111222");
        newPerson.setId(999L);

        // WHEN
        User savedEntity = userRepository.save(newPerson);

        // THEN

        assertThat(userRepository.findByEmail("unknowEmail@mail.com")).isNull();
        assertThat(userRepository.findByEmail("newperson@gmail.com")).isNotNull();
        assertThat(userRepository.findByEmail("kim.jong@gmail.com")).isNotNull();// existing in dbTest
    }

//    @Test
//    @Sql({ "/dbTest.sql" })
//    @Tag("CREATE")
//    @DisplayName("FindByEmail - OK")
//    public void givenNewPerson_whenCreation_thenReturnPersonSavedInDb() {
//
//        // GIVEN
//        User newPerson = new User("New", "Person", "newperson@gmail.com", "love", "000111222");
//        newPerson.setId(999L);
//
//        // WHEN
//        User savedEntity = userRepository.save(newPerson);
//
//        // THEN
//
//        assertThat(userRepository.findByEmail("unknowEmail@mail.com")).isNull();
//        assertThat(userRepository.findByEmail("newperson@gmail.com")).isNotNull();
//        assertThat(userRepository.findByEmail("kim.jong@gmail.com")).isNotNull();// existing in dbTest
//    }
//    @Test
//    @Sql({ "/dbTest.sql" })
//    @Tag("CREATE")
//    @DisplayName("Create new user - ERROR - Email already exist")
//    public void givenAlreadyExistingEmailPersonInDb_whenCreation_thenReturnPersonNotSavedInDb() {
//
//        // GIVEN
//        User donaldTrump = new User("Trump", "Donald", "donald.trump@gmail.com", "love-usa", "000111222");
//        userRepository.save(donaldTrump);
//        AppAccount appAccount = new AppAccount(donaldTrump, 0.0);
//        appAccountRepository.save(appAccount);
//
//        User existingEmailInDb = new User("Trumpidou", "Donaldidou", "donald.trump@gmail.com", "lovidou", "000111000");
//
//        when(userRepository.findByEmail("donald.trump@gmail.com")).thenReturn(donaldTrump);
//        // WHEN
//        userService.addNewUser(existingEmailInDb);
//
//        // THEN
//        assertThat(userRepository.findByEmail("unknow.email@gmail.com")).isNull();
//        assertThat(userRepository.findByEmail("donald.trump@gmail.com")).isNotNull();
//    }
//
//    @Test
//    @Sql({ "/dbTest.sql" })
//    @Tag("CREATE")
//    @DisplayName("Create new user - ERROR - Unexisting email insered")
//    public void givenNullEmailAddressEntry_whenCreation_thenReturnNull() {
//
//        // GIVEN
//        User donaldTrump = new User("Trump", "Donald", null, "love-usa", "000111222");
//
//        // WHEN
//        User result = userService.addNewUser(donaldTrump);
//
//        // THEN
//        assertThat(result.getEmail()).isNull();
//        assertThat(userRepository.count()).isEqualTo(0);
//        assertThat(userRepository.findByEmail(donaldTrump.getEmail())).isNull();
//    }

}
