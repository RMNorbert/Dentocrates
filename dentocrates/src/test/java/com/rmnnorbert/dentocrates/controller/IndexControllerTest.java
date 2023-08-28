package com.rmnnorbert.dentocrates.controller;

import com.rmnnorbert.dentocrates.security.config.JwtService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@ExtendWith(SpringExtension.class)
@WebMvcTest(IndexController.class)
class IndexControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private JwtService service;
    @MockBean
    private UserDetailsService detailsService;
    final static String MOCK_USERNAME = "testUser";
    final static String MOCK_ROLE = "CUSTOMER";
    @Test
    @WithMockUser(username = MOCK_USERNAME, roles = MOCK_ROLE)
    public void testIndex() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("/index.html"));
    }
}
