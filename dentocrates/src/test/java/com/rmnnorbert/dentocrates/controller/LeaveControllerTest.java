package com.rmnnorbert.dentocrates.controller;

import com.rmnnorbert.dentocrates.service.LeaveService;
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
class LeaveControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private LeaveService leaveService;
    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
    }
    @Test
    void getLeavesFromDateOfClinic() {
    }

    @Test
    void registerLeaveToClinic() {
    }

    @Test
    void deleteLeaveOfClinic() {
    }
}
