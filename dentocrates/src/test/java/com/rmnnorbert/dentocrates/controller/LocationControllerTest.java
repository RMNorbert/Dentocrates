package com.rmnnorbert.dentocrates.controller;

import com.rmnnorbert.dentocrates.service.LocationService;
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
class LocationControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private LocationService service;
    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
    }
    @Test
    void getAllLocation() {

    }

    @Test
    void registerLocation() {

    }

    @Test
    void deleteLocation() {

    }
}
