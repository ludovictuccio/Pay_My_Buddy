package com.paymybuddy.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

import com.paymybuddy.repository.AppAccountRepository;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application.properties")
@Sql(scripts = "classpath:dropAndCreate.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@Sql(scripts = { "classpath:dbTest.sql" }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class TransferControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AppAccountRepository appAccountRepository;

    @Test
    @Tag("GET")
    @DisplayName("Personal payment - OK")
    public void givenCorrectInformations_whenRun_thenReturnOk() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/user-space/personal-payment").contentType(APPLICATION_JSON)
                .param("myEmail", "manu.macron@gmail.com").param("amount", "50").param("cbNumber", "5136483066664444")
                .param("cbExpirationDateMonth", "09").param("cbExpirationDateYear", "24").param("cbSecurityKey", "773")
                .accept(APPLICATION_JSON)).andDo(MockMvcResultHandlers.print()).andExpect(status().isOk());

    }

    @Test
    @Tag("GET")
    @DisplayName("Personal payment - OK")
    public void givenIncorrectsInformations_whenRunWithBadCardNumber_thenReturnConflict() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/user-space/personal-payment").contentType(APPLICATION_JSON)
                .param("myEmail", "manu.macron@gmail.com").param("amount", "50").param("cbNumber", "3")
                .param("cbExpirationDateMonth", "09").param("cbExpirationDateYear", "24").param("cbSecurityKey", "773")
                .accept(APPLICATION_JSON)).andDo(MockMvcResultHandlers.print()).andExpect(status().isNotFound());

    }

}
