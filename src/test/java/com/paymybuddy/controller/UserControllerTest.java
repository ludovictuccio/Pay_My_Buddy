package com.paymybuddy.controller;

import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.paymybuddy.model.User;
import com.paymybuddy.repository.AppAccountRepository;
import com.paymybuddy.repository.UserRepository;
import com.paymybuddy.service.UserService;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private AppAccountRepository appAccountRepository;

    @MockBean
    private UserService userService;

//    public static User validUser = new User("Macron", "Emmanuel", "manu.macron@gmail.com", "love-france", "0212345678");
//    public static User invalidUser = new User("Invalid", "User", null, "abcd", "02");
//
//    @Before
//    public void setUp() {
//        userRepository.deleteAllInBatch();
//        appAccountRepository.deleteAllInBatch();
//        // mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
//    }

    private User user;

    @Before
    public void setUp() {
        // Initialisation du setup avant chaque test

        user = new User("New", "Person", "newperson@gmail.com", "love", "000111222");
        List<User> allUsers = Arrays.asList(user);
        objectMapper = new ObjectMapper();

        // Mock de la couche de service
        when(userService.addNewUser(user));

    }

//    @Test
//    public void testSaveUser() throws Exception {
//
//        User userToSave = new User("New", "Person", "newperson@gmail.com", "love", "000111222");
//        String jsonContent = objectMapper.writeValueAsString(userToSave);
//
//        MvcResult result = mockMvc.perform(post("/registration").contentType(APPLICATION_JSON).content(jsonContent))
//                .andExpect(status().isCreated()).andReturn();
//
//        assertEquals("Erreur de sauvegarde", HttpStatus.CREATED.value(), result.getResponse().getStatus());
//        verify(userService).saveOrUpdateUser(any(User.class));
//        User userResult = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<User>() {
//        });
//        assertNotNull(userResult);
//        assertEquals(userToSave.getLogin(), userResult.getLogin());
//        assertEquals(userToSave.getPassword(), userResult.getPassword());
//
//    }

//    @Test
//    @Tag("CreatePerson")
//    @DisplayName("CreatePerson - OK")
//    public void givenPersonCreation_whenAllCorrectInfos_thenReturnPersonCreated() throws Exception {
//        mockMvc.perform(
//                post("/registration").contentType(APPLICATION_JSON).content(objectMapper.writeValueAsString(validUser)))
//                .andExpect(status().isCreated());
//
//        User user = userRepository.findByEmail("manu.macron@gmail.com");
//        assertThat(user.getLastname()).isEqualTo("Macron");
//    }

    @Test
    @Tag("CreatePerson")
    @DisplayName("CreatePerson - OK")
    public void aa() throws Exception {
        mockMvc.perform(post("/registration").contentType(APPLICATION_JSON)
                .content("{\r\n" + "    \"lastname\": \"Macron\",\r\n" + "    \"firstname\": \"Emmanuel\",\r\n"
                        + "    \"email\": \"manu.macron@gmail.com\",\r\n" + "    \"password\": \"love-france\",\r\n"
                        + "    \"phone\": \"0212345678\"\r\n" + "}"))
                .andExpect(status().isCreated());
    }

//    @Test
//    @Tag("CreatePerson")
//    @DisplayName("CreatePerson - ERROR ")
//    public void givenPersonCreation_whenAlreadyExistingPerson_thenReturnErrorConflict() throws Exception {
//        this.mockMvc
//                .perform(MockMvcRequestBuilders.post("/registration").andExpect(status().isConflict());
//    }
}
