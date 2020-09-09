package com.paymybuddy.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;

import com.paymybuddy.model.AppAccount;
import com.paymybuddy.model.User;
import com.paymybuddy.repository.UserRepository;

@SpringBootTest
@TestPropertySource(locations = "classpath:application.properties")
@Sql(scripts = "classpath:dropAndCreate.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@Sql(scripts = { "classpath:dbTest.sql" }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class RelationServiceTest {

    @Autowired
    public IRelationService relationService;

    @Autowired
    private UserRepository userRepository;

    @Test
    @Tag("ADD")
    @DisplayName("Add relation - OK")
    public void givenTwoFriends_whenAnUserAddRelationWithValidEmail_thenReturnTrue() {
        // GIVEN
        User myUserAccount = new User("GENERIC1", "USER1", "generic1@gmail.com", "gen1", "0101010101");
        userRepository.save(myUserAccount);
        User friendToConnect = new User("GENERIC2", "USER2", "generic2@gmail.com", "gen2", "0202020202");
        userRepository.save(friendToConnect);

        // WHEN
        boolean isAdded = relationService.addRelation(myUserAccount.getEmail(), friendToConnect.getEmail());

        // THEN
        assertThat(userRepository.count()).isEqualTo(7); // 5 in dbTest
        assertThat(isAdded).isTrue();
        assertThat(userRepository.findById(1L).get().getPmbFriends().size()).isEqualTo(1);
        assertThat(userRepository.findById(2L).get().getPmbFriends().size()).isEqualTo(2);
        assertThat(userRepository.findById(3L).get().getPmbFriends().size()).isEqualTo(0);
        assertThat(userRepository.findById(4L).get().getPmbFriends().size()).isEqualTo(1);
        assertThat(userRepository.findById(5L).get().getPmbFriends().size()).isEqualTo(1);
        assertThat(userRepository.findById(myUserAccount.getId()).get().getPmbFriends().size()).isEqualTo(1);
        assertThat(userRepository.findById(friendToConnect.getId()).get().getPmbFriends().size()).isEqualTo(0);
    }

    @Test
    @Tag("ADD")
    @DisplayName("Add relation - ERROR - Unknow user")
    public void givenAnUser_whenAddUnknowEmail_thenReturnConnectionNotAdded() {
        // GIVEN
        User myUserAccount = new User("GENERIC1", "USER1", "generic1@gmail.com", "gen1", "0101010101");
        userRepository.save(myUserAccount);

        // WHEN
        boolean isAdded = relationService.addRelation(myUserAccount.getEmail(), "unknow-email@gmail.com");

        // THEN
        assertThat(userRepository.count()).isEqualTo(6); // 5 in dbTest
        assertThat(isAdded).isFalse();
        assertThat(userRepository.findById(1L).get().getPmbFriends().size()).isEqualTo(1);
        assertThat(userRepository.findById(2L).get().getPmbFriends().size()).isEqualTo(2);
        assertThat(userRepository.findById(3L).get().getPmbFriends().size()).isEqualTo(0);
        assertThat(userRepository.findById(4L).get().getPmbFriends().size()).isEqualTo(1);
        assertThat(userRepository.findById(5L).get().getPmbFriends().size()).isEqualTo(1);
        assertThat(userRepository.findById(myUserAccount.getId()).get().getPmbFriends().size()).isEqualTo(0);
    }

    @Test
    @Tag("DELETE")
    @DisplayName("Delete relation - OK - Existing relation")
    public void givenOneRelation_whenDeleteIt_thenReturnDeletedRelation() {
        // GIVEN
        // all in db test

        // WHEN
        boolean isDeleted = relationService.deleteRelation("manu.macron@gmail.com", "vlad.poutine@gmail.com");

        // THEN
        assertThat(userRepository.count()).isEqualTo(5); // 5 in dbTest
        assertThat(isDeleted).isTrue();
        assertThat(userRepository.findById(1L).get().getPmbFriends().size()).isEqualTo(1);
        assertThat(userRepository.findById(2L).get().getPmbFriends().size()).isEqualTo(1);// was 2
        assertThat(userRepository.findById(3L).get().getPmbFriends().size()).isEqualTo(0);
        assertThat(userRepository.findById(4L).get().getPmbFriends().size()).isEqualTo(1);
        assertThat(userRepository.findById(5L).get().getPmbFriends().size()).isEqualTo(1);
    }

    @Test
    @Tag("DELETE")
    @DisplayName("Delete relation - ERROR - Email non-existant in db")
    public void givenBadEmail_whenDelete_thenReturnUnchangedRelationsAndFalse() {
        // GIVEN
        // all in db test

        // WHEN
        boolean isDeleted = relationService.deleteRelation("manu.macron@gmail.com", "UNKNOW-EMAIL");

        // THEN
        assertThat(userRepository.count()).isEqualTo(5); // 5 in dbTest
        assertThat(isDeleted).isFalse();
        // unchanged relations size
        assertThat(userRepository.findById(1L).get().getPmbFriends().size()).isEqualTo(1);
        assertThat(userRepository.findById(2L).get().getPmbFriends().size()).isEqualTo(2);
        assertThat(userRepository.findById(3L).get().getPmbFriends().size()).isEqualTo(0);
        assertThat(userRepository.findById(4L).get().getPmbFriends().size()).isEqualTo(1);
        assertThat(userRepository.findById(5L).get().getPmbFriends().size()).isEqualTo(1);
    }

    @Test
    @Tag("DELETE")
    @DisplayName("Delete relation - ERROR - Email exists in db / not in the relation list")
    public void givenExistingDbUser_whenDeleteItButNotInTheUsersRelationsList_thenReturnFalse() {
        // GIVEN
        // all in db test

        // WHEN
        boolean isDeleted = relationService.deleteRelation("manu.macron@gmail.com", "donald.trump@gmail.com");

        // THEN
        assertThat(userRepository.count()).isEqualTo(5); // 5 in dbTest
        assertThat(isDeleted).isFalse();
        // unchanged relations size
        assertThat(userRepository.findById(1L).get().getPmbFriends().size()).isEqualTo(1);
        assertThat(userRepository.findById(2L).get().getPmbFriends().size()).isEqualTo(2);
        assertThat(userRepository.findById(3L).get().getPmbFriends().size()).isEqualTo(0);
        assertThat(userRepository.findById(4L).get().getPmbFriends().size()).isEqualTo(1);
        assertThat(userRepository.findById(5L).get().getPmbFriends().size()).isEqualTo(1);
    }

    @Test
    @Tag("GET")
    @DisplayName("Get relation - Ok")
    public void givenExistingRelation_whenGetRelation_thenReturnRelation() {
        // GIVEN
        // all in db test

        // WHEN
        AppAccount connectionToRetrieve = relationService.getRelationAppAccount("manu.macron@gmail.com",
                "vlad.poutine@gmail.com");

        // THEN
        assertThat(userRepository.count()).isEqualTo(5); // 5 in dbTest
        assertThat(connectionToRetrieve).isNotNull();
        assertThat(connectionToRetrieve.getBalance()).isEqualTo(new BigDecimal("2000.00")); // poutine balance
    }

    @Test
    @Tag("GET")
    @DisplayName("Get relation - Error - Not in the user's relations list")
    public void givenUnexistingRelation_whenGetRelation_thenReturnNull() {
        // GIVEN
        // all in db test

        // WHEN
        AppAccount connectionToRetrieve = relationService.getRelationAppAccount("manu.macron@gmail.com",
                "UNKNOW@gmail.com");

        // THEN
        assertThat(userRepository.count()).isEqualTo(5); // 5 in dbTest
        assertThat(connectionToRetrieve).isNull();
    }

}
