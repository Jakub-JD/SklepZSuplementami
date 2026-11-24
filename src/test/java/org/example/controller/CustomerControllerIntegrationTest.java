package org.example.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.model.Customer;
import org.example.model.User;
import org.example.repository.CustomerRepository;
import org.example.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Testy integracyjne dla CustomerController weryfikujące operacje CRUD.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
public class CustomerControllerIntegrationTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private CustomerRepository repository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        repository.deleteAll();
        userRepository.deleteAll();

        User testUser = new User();
        testUser.setUsername("admin");
        testUser.setPasswordHash(passwordEncoder.encode("haslo1234"));
        testUser.setRole("ROLE_ADMIN");
        userRepository.saveAndFlush(testUser);
    }

    @Test
    public void postCustomerTest() throws Exception {
        Customer jan = createCustomerObject("Jakub", "Gwozdowski", "jakub.gwozdowski@temp.com");

        mvc.perform(post("/api/customers")
                        .with(httpBasic("admin", "haslo1234"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(jan)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is("Jakub")))
                .andExpect(jsonPath("$.lastName", is("Gwozdowski")))
                .andExpect(jsonPath("$.email", is("jakub.gwozdowski@temp.com")));

        List<Customer> found = repository.findAll();
        assertThat(found).extracting(Customer::getEmail).containsOnly("jakub.gwozdowski@temp.com");
    }

    @Test
    public void getAllCustomersTest() throws Exception {
        saveTestCustomer("Jakub", "Fus", "jakub.fus@temp.com");
        saveTestCustomer("Jakub", "Gwozdowski", "jakub.gwozdowski@temp.com");

        mvc.perform(get("/api/customers")
                        .with(httpBasic("admin", "haslo1234"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].firstName", is("Jakub")))
                .andExpect(jsonPath("$[1].lastName", is("Gwozdowski")));
    }

    @Test
    public void getCustomerByIdTest() throws Exception {
        Customer saved = saveTestCustomer("Tester", "Tester", "tester.tester@temp.com");
        saveTestCustomer("Jakub", "Fus", "jakub.fus@temp.com");
        saveTestCustomer("Jakub", "Gwozdowski", "jakub.gwozdowski@temp.com");

        mvc.perform(get("/api/customers/" + saved.getId())
                        .with(httpBasic("admin", "haslo1234"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(saved.getId().intValue())))
                .andExpect(jsonPath("$.firstName", is("Tester")))
                .andExpect(jsonPath("$.email", is("tester.tester@temp.com")));
    }

    @Test
    public void updateCustomerTest() throws Exception {
        Customer saved = saveTestCustomer("Jakub", "Gwozdowski", "jakub.gwozdowski@temp.com");
        Customer updatedData = createCustomerObject("Test", "Tester", "tester.tester@temp.com");

        mvc.perform(put("/api/customers/" + saved.getId())
                        .with(httpBasic("admin", "haslo1234"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedData)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is("Test")))
                .andExpect(jsonPath("$.lastName", is("Tester")))
                .andExpect(jsonPath("$.email", is("tester.tester@temp.com")));

        Customer fromDb = repository.findById(saved.getId()).orElseThrow();
        assertThat(fromDb.getFirstName()).isEqualTo("Test");
        assertThat(fromDb.getEmail()).isEqualTo("tester.tester@temp.com");
    }

    @Test
    public void deleteCustomerTest() throws Exception {
        Customer saved = saveTestCustomer("Jakub", "Gwozdowski", "jakub.gwozdowski@temp.com");

        mvc.perform(delete("/api/customers/" + saved.getId())
                        .with(httpBasic("admin", "haslo1234")))
                .andDo(print())
                .andExpect(status().isOk());

        assertThat(repository.findById(saved.getId())).isEmpty();
    }

    private Customer saveTestCustomer(String firstName, String lastName, String email) {
        return repository.saveAndFlush(createCustomerObject(firstName, lastName, email));
    }

    private Customer createCustomerObject(String firstName, String lastName, String email) {
        Customer customer = new Customer();
        customer.setFirstName(firstName);
        customer.setLastName(lastName);
        customer.setEmail(email);
        return customer;
    }
}