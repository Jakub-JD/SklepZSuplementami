package org.example.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.model.Supplement;
import org.example.model.User;
import org.example.repository.SupplementRepository;
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
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Testy integracyjne dla SupplementController weryfikujące operacje CRUD.
 */

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
public class SupplementControllerIntegrationTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private SupplementRepository repository;

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
        testUser.setUsername("user");
        testUser.setPasswordHash(passwordEncoder.encode("haslo1234"));
        testUser.setRole("ROLE_USER");
        userRepository.saveAndFlush(testUser);
    }

    @Test
    public void postSupplementTest() throws Exception {
        Supplement kreatyna = createSupplementObject("Kreatyna Monohydrat", "Siła", 79.99);

        mvc.perform(post("/api/supplements")
                        .with(httpBasic("user", "haslo1234"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(kreatyna)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Kreatyna Monohydrat")))
                .andExpect(jsonPath("$.price", is(79.99)));

        List<Supplement> found = repository.findAll();
        assertThat(found).extracting(Supplement::getName).containsOnly("Kreatyna Monohydrat");
    }

    @Test
    public void getAllSupplementTest() throws Exception {
        saveTestSupplement("Białko WPC", "Białko", 119.99);
        saveTestSupplement("Witamina D3", "Witaminy", 29.99);

        mvc.perform(get("/api/supplements")
                        .with(httpBasic("user", "haslo1234"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name", is("Białko WPC")))
                .andExpect(jsonPath("$[1].name", is("Witamina D3")));
    }

    @Test
    public void getSupplementByIdTest() throws Exception {
        Supplement saved = saveTestSupplement("Magnez B6", "Minerały", 24.50);
        saveTestSupplement("Białko WPC", "Białko", 119.99);
        saveTestSupplement("Witamina D3", "Witaminy", 29.99);

        mvc.perform(get("/api/supplements/" + saved.getId())
                        .with(httpBasic("user", "haslo1234"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(saved.getId().intValue())))
                .andExpect(jsonPath("$.name", is("Magnez B6")));
    }

    @Test
    public void updateSupplementTest() throws Exception {
        Supplement saved = saveTestSupplement("Kreatyna", "Siła", 60.00);
        Supplement updatedData = createSupplementObject("Kreatyna Jabłczan", "Siła i Wytrzymałość", 85.00);

        mvc.perform(put("/api/supplements/" + saved.getId())
                        .with(httpBasic("user", "haslo1234"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedData)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Kreatyna Jabłczan")))
                .andExpect(jsonPath("$.category", is("Siła i Wytrzymałość")))
                .andExpect(jsonPath("$.price", is(85.00)));

        Supplement fromDb = repository.findById(saved.getId()).orElseThrow();
        assertThat(fromDb.getName()).isEqualTo("Kreatyna Jabłczan");
        assertThat(fromDb.getPrice()).isEqualTo(85.00);
    }

    @Test
    public void deleteSupplementTest() throws Exception {
        Supplement saved = saveTestSupplement("Omega 3", "Zdrowie", 39.99);

        mvc.perform(delete("/api/supplements/" + saved.getId()).with(httpBasic("user", "haslo1234")))
                .andDo(print())
                .andExpect(status().isOk());

        assertThat(repository.findById(saved.getId())).isEmpty();
    }

    private Supplement saveTestSupplement(String name, String category, double price) {
        return repository.saveAndFlush(createSupplementObject(name, category, price));
    }

    private Supplement createSupplementObject(String name, String category, double price) {
        Supplement supplement = new Supplement();
        supplement.setName(name);
        supplement.setCategory(category);
        supplement.setPrice(price);
        return supplement;
    }
}