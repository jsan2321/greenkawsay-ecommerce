package com.greenkawsay.configuration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class SecurityConfigTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void publicEndpoint_shouldBeAccessibleWithoutAuth() throws Exception {
        mockMvc.perform(get("/api/test/public"))
               .andExpect(status().isOk())
               .andExpect(content().string("¡Este es un endpoint público!"));
    }

    @Test
    void authenticatedEndpoint_shouldRequireAuth() throws Exception {
        mockMvc.perform(get("/api/test/authenticated"))
               .andExpect(status().isUnauthorized());
    }

    @Test
    void adminEndpoint_shouldRequireAdminRole() throws Exception {
        mockMvc.perform(get("/api/test/admin"))
               .andExpect(status().isUnauthorized());
    }

    @Test
    void swaggerUI_shouldBeAccessibleWithoutAuth() throws Exception {
        mockMvc.perform(get("/swagger-ui/index.html"))
               .andExpect(status().isOk());
    }

    @Test
    void actuatorHealth_shouldBeAccessibleWithoutAuth() throws Exception {
        mockMvc.perform(get("/actuator/health"))
               .andExpect(status().isOk());
    }
}