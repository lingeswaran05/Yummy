package com.yummiee;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class SecurityTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testPublicEndpoints_AllowedWithoutAuth() throws Exception {
        mockMvc.perform(get("/api/recipes"))
                .andExpect(status().isOk());
    }

    @Test
    void testProtectedRecipeEndpoint_RejectsUnauthenticated() throws Exception {
        mockMvc.perform(post("/api/recipes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Test Recipe\"}"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testProtectedWishlistEndpoint_RejectsUnauthenticated() throws Exception {
        mockMvc.perform(get("/api/wishlist"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testProtectedShoppingListEndpoint_RejectsUnauthenticated() throws Exception {
        mockMvc.perform(get("/api/shopping-list"))
                .andExpect(status().isUnauthorized());
    }
}
