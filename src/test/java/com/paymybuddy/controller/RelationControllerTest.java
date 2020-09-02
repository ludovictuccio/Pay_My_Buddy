package com.paymybuddy.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
import com.paymybuddy.service.RelationService;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application.properties")
@Sql(scripts = "classpath:dropAndCreate.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@Sql(scripts = { "classpath:dbTest.sql" }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)

public class RelationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RelationService relationService;

    @Autowired
    private UserRepository userRepository;

    @MockBean
    private AppAccountRepository appAccountRepository;

    public static User myUserAccount = new User("GENERIC1", "USER1", "generic1@gmail.com", "gen1", "0101010101");
    public static User friend = new User("GENERIC2", "USER2", "generic2@gmail.com", "gen2", "0202020202");

    @BeforeEach
    public void setUpPerTest() {
        objectMapper = new ObjectMapper();
    }

    @Test
    @Tag("POST")
    @DisplayName("ADD relation - OK")
    public void givenTwoCorrectEmail_whenAddRelation_thenReturnCreated() throws Exception {
        userRepository.save(myUserAccount);
        userRepository.save(friend);

        mockMvc.perform(MockMvcRequestBuilders.post("/connection").contentType(APPLICATION_JSON)
                .param("myEmail", "generic1@gmail.com").param("emailToConnect", "generic2@gmail.com")
                .accept(APPLICATION_JSON)).andDo(MockMvcResultHandlers.print()).andExpect(status().isCreated());
    }

    @Test
    @Tag("POST")
    @DisplayName("ADD relation - ERROR - Invalid email")
    public void givenAnUnknowEmailEntry_whenAddRelation_thenReturnConflict() throws Exception {
        userRepository.save(myUserAccount);
        userRepository.save(friend);

        mockMvc.perform(MockMvcRequestBuilders.post("/connection").contentType(APPLICATION_JSON)
                .param("myEmail", "generic1@gmail.com").param("emailToConnect", "UNKNOW-EMAIL")
                .accept(APPLICATION_JSON)).andDo(MockMvcResultHandlers.print()).andExpect(status().isConflict());
    }

    @Test
    @Tag("DELETE")
    @DisplayName("DELETE relation - OK")
    public void givenARelation_whenDeleteRelation_thenReturnOk() throws Exception {
        userRepository.save(myUserAccount);
        userRepository.save(friend);

        mockMvc.perform(MockMvcRequestBuilders.delete("/connection").contentType(APPLICATION_JSON)
                .param("myEmail", "manu.macron@gmail.com").param("emailToDelete", "vlad.poutine@gmail.com")
                .accept(APPLICATION_JSON)).andDo(MockMvcResultHandlers.print()).andExpect(status().isOk());
    }

    @Test
    @Tag("DELETE")
    @DisplayName("DELETE relation - ERROR - Email no in the relations list")
    public void givenEmailNoInARelation_whenDelete_thenReturnConflict() throws Exception {
        userRepository.save(myUserAccount);
        userRepository.save(friend);

        mockMvc.perform(MockMvcRequestBuilders.delete("/connection").contentType(APPLICATION_JSON)
                .param("myEmail", "manu.macron@gmail.com").param("emailToDelete", "NO-IN-RELATION-email@gmail.com")
                .accept(APPLICATION_JSON)).andDo(MockMvcResultHandlers.print()).andExpect(status().isConflict());
    }

}
