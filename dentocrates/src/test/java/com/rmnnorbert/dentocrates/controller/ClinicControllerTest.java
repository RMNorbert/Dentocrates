package com.rmnnorbert.dentocrates.controller;

import com.rmnnorbert.dentocrates.service.ClinicService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
@SpringBootTest
@AutoConfigureMockMvc
class ClinicControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ClinicService service;
    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
    }
    @Test
    void getAllClinic() {
    }

    @Test
    void getAllClinicByDentist() {
    }

    @Test
    void getClinicById() {
    }

    @Test
    void registerClinic() {
    }

    @Test
    void deleteClinic() {
    }
}
