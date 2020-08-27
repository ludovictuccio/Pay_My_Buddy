package com.paymybuddy.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import javax.transaction.Transactional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.paymybuddy.model.User;
import com.paymybuddy.repository.AppAccountRepository;
import com.paymybuddy.repository.UserRepository;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application.properties")
@Sql(scripts = "classpath:dropAndCreate.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@Transactional
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @MockBean
    private AppAccountRepository appAccountRepository;

    public static User validUser = new User("Macron", "Emmanuel", "manu.macron@gmail.com", "love-france", "0212345678");
    public static User invalidUserNullEmail = new User("Invalid", "User", null, "abcd", "02");
    public static User invalidUserEmail = new User("Invalid", "User", "a@gg.fr", "abcd", "02");

    @BeforeEach
    public void setUpPerTest() {
        objectMapper = new ObjectMapper();
    }

    @Test
    @Tag("POST")
    @DisplayName("ADD user - OK")
    public void givenUserCreation_whenValidEmail_thenReturnCreated() throws Exception {
        validUser.setId(100L);
        String jsonContentValid = objectMapper.writeValueAsString(validUser);

        mockMvc.perform(MockMvcRequestBuilders.post("/registration").contentType(APPLICATION_JSON)
                .content(jsonContentValid).accept(APPLICATION_JSON)).andDo(MockMvcResultHandlers.print())
                .andExpect(status().isCreated());
    }

    @Test
    @Tag("POST")
    @DisplayName("ADD user - ERROR - Invalid email")
    public void givenUserCreation_whenInvalidEmail_thenReturnErrorConflict() throws Exception {

        String jsonContentInvalid = objectMapper.writeValueAsString(invalidUserEmail);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/registration").contentType(APPLICATION_JSON).content(jsonContentInvalid))
                .andDo(MockMvcResultHandlers.print()).andExpect(status().isConflict());
    }

    @Test
    @Tag("POST")
    @DisplayName("ADD user - ERROR - Null email")
    public void givenUserCreation_whenNullEmail_thenReturnErrorConflict() throws Exception {

        String jsonContentInvalid = objectMapper.writeValueAsString(invalidUserNullEmail);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/registration").contentType(APPLICATION_JSON).content(jsonContentInvalid))
                .andDo(MockMvcResultHandlers.print()).andExpect(status().isConflict());
    }

    @Test
    @Tag("PUT")
    @DisplayName("Put infos - OK")
    public void givenOnePersonInDb_whenUpdatePasswordAndPhone_thenReturnOk() throws Exception {
        validUser.setId(1000L);
        userRepository.save(validUser);

        User userToUpdate = new User("Macron", "Emmanuel", "manu.macron@gmail.com", "newPassword", "0988774433");
        userToUpdate.setId(2000L);
        String jsonContentValid = objectMapper.writeValueAsString(userToUpdate);

        mockMvc.perform(MockMvcRequestBuilders.put("/user-space").contentType(APPLICATION_JSON)
                .content(jsonContentValid).accept(APPLICATION_JSON)).andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk());
    }

    @Test
    @Tag("PUT")
    @DisplayName("Put infos - ERROR - Unknow email")
    public void givenUnknowEmailInDb_whenTryToUpdate_thenReturnConflict() throws Exception {

        String jsonContentInvalid = objectMapper.writeValueAsString(validUser);

        mockMvc.perform(MockMvcRequestBuilders.put("/user-space").contentType(APPLICATION_JSON)
                .content(jsonContentInvalid).accept(APPLICATION_JSON)).andDo(MockMvcResultHandlers.print())
                .andExpect(status().isConflict());
    }
}
