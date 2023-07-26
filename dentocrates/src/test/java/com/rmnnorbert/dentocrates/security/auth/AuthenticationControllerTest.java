package com.rmnnorbert.dentocrates.security.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rmnnorbert.dentocrates.controller.dto.client.customer.CustomerRegisterDTO;
import com.rmnnorbert.dentocrates.controller.dto.client.dentist.DentistRegisterDTO;
import com.rmnnorbert.dentocrates.custom.exceptions.InvalidCredentialException;
import jakarta.servlet.ServletException;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class AuthenticationControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private AuthenticationService service;
    final static String MOCK_USERNAME = "testUser";
    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
    }
    @Test
    @WithMockUser(username = MOCK_USERNAME)
    void registerCustomerShouldReturnOkStatusAndExpectedValue() throws Exception {
        CustomerRegisterDTO dto = new CustomerRegisterDTO("email@e.com",
                                                       "password",
                                                       "name",
                                                       "last");
        String jwtToken = "token";
        String jsonRequest = new ObjectMapper().writeValueAsString(dto);
        AuthenticationResponse expectedResponseBody = AuthenticationResponse.builder()
                .token(jwtToken)
                .id(0)
                .build();

        when(service.register(dto)).thenReturn(expectedResponseBody);


        mockMvc.perform(post("/api/register/customer")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token",Matchers.is(expectedResponseBody.token())));
    }
    @Test
    @WithMockUser(username = MOCK_USERNAME)
    void registerCustomerShouldReturnBadRequestStatusAndServletException() throws Exception {
        CustomerRegisterDTO dto = new CustomerRegisterDTO("email@e.com",
                                                       "password",
                                                       "name",
                                                        "last");
        String jsonRequest = new ObjectMapper().writeValueAsString(dto);

        when(service.register(dto)).thenThrow(InvalidCredentialException.class);

        ServletException exception = assertThrows(ServletException.class, () -> mockMvc.perform(post("/api/register/customer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.error").value("Bad Request")));
    }

    @Test
    @WithMockUser(username = MOCK_USERNAME)
    void registerCustomerWithInvalidRequestShouldReturnBadRequestStatusAndValidRequestDetailsMessage() throws Exception {
        CustomerRegisterDTO dto = new CustomerRegisterDTO("email", "   ", " ", " ");
        String expectedBodyContent = "lastName : must not be blank";
        String expectedBodyContent2 = "firstName : must not be blank";
        String expectedBodyContent3 = "password : must not be blank";
        String expectedBodyContent4 = "email : must be a well-formed email address";
        String expectedBodyContent5 = "password : size must be between 8 and 2147483647";
        String jsonRequest = new ObjectMapper().writeValueAsString(dto);

        mockMvc.perform(post("/api/register/customer")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(Matchers.containsString(expectedBodyContent)))
                .andExpect(content().string(Matchers.containsStringIgnoringCase(expectedBodyContent2)))
                .andExpect(content().string(Matchers.containsString(expectedBodyContent3)))
                .andExpect(content().string(Matchers.containsString(expectedBodyContent4)))
                .andExpect(content().string(Matchers.containsString(expectedBodyContent5)));

        verify(service,times(0)).register(dto);
    }
    @Test
    @WithMockUser(username = MOCK_USERNAME)
    void registerDentistShouldReturnOkStatusAndExpectedValue() throws Exception {
        DentistRegisterDTO dto = new DentistRegisterDTO("email@e.com",
                                                     "password",
                                                     "name",
                                                     "last",
                                             "opLicence");

        String jsonRequest = new ObjectMapper().writeValueAsString(dto);
        String jwtToken = "token";

        AuthenticationResponse expectedResponseBody = AuthenticationResponse.builder()
                .token(jwtToken)
                .id(0)
                .build();

        when(service.register(dto)).thenReturn(expectedResponseBody);


        mockMvc.perform(post("/api/register/dentist")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.token",Matchers.is(expectedResponseBody.token())));
    }
    @Test
    @WithMockUser(username = MOCK_USERNAME)
    void registerDentistShouldReturnBadRequestStatusAndServletException() throws Exception {
        DentistRegisterDTO dto = new DentistRegisterDTO("email@e.com",
                                                     "password",
                                                     "name",
                                                     "last",
                                             "opLicence");
        String jsonRequest = new ObjectMapper().writeValueAsString(dto);

        when(service.register(dto)).thenThrow(InvalidCredentialException.class);

        ServletException exception = assertThrows(ServletException.class, () -> mockMvc.perform(post("/api/register/dentist")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.error").value("Bad Request")));
    }
    @Test
    @WithMockUser(username = MOCK_USERNAME)
    void registerDentistShouldReturnOkStatusAndValidRequestDetailsMessage() throws Exception {
        DentistRegisterDTO dto = new DentistRegisterDTO("email",
                                                     " ",
                                                     " ",
                                                     " ",
                                             " ");
        String expectedBodyContent = "lastName : must not be blank";
        String expectedBodyContent2 = "firstName : must not be blank";
        String expectedBodyContent3 = "password : must not be blank";
        String expectedBodyContent4 = "email : must be a well-formed email address";
        String expectedBodyContent5 = "password : size must be between 8 and 2147483647";
        String expectedBodyContent6 = "operatingLicenceNo : must not be blank";
        String jsonRequest = new ObjectMapper().writeValueAsString(dto);

        mockMvc.perform(post("/api/register/dentist")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(Matchers.containsString(expectedBodyContent)))
                .andExpect(content().string(Matchers.containsStringIgnoringCase(expectedBodyContent2)))
                .andExpect(content().string(Matchers.containsString(expectedBodyContent3)))
                .andExpect(content().string(Matchers.containsString(expectedBodyContent4)))
                .andExpect(content().string(Matchers.containsString(expectedBodyContent5)))
                .andExpect(content().string(Matchers.containsString(expectedBodyContent6)));

        verify(service,times(0)).register(dto);
    }

    @Test
    @WithMockUser(username = MOCK_USERNAME)
    void authenticateShouldReturnOkStatusAndExpectedValue() throws Exception {
        AuthenticationRequest request = new AuthenticationRequest(
                                                            "email@e.com",
                                                         "password"
                                                                 );
        String token = "token";
        long id = 0;
        String jsonRequest = new ObjectMapper().writeValueAsString(request);
        AuthenticationResponse expected = AuthenticationResponse.builder()
                .token(token)
                .id(id)
                .build();

        when(service.authenticate(request)).thenReturn(expected);

        mockMvc.perform(post("/api/authenticate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token",Matchers.is(expected.token())));
        verify(service,times(1)).authenticate(request);
    }
    @Test
    @WithMockUser(username = MOCK_USERNAME)
    void authenticateShouldReturnOkStatusAndValidRequestDetailsMessage() throws Exception {
        AuthenticationRequest request = new AuthenticationRequest(
                "email@e.com",
                "password"
        );
        String jsonRequest = new ObjectMapper().writeValueAsString(request);

        when(service.authenticate(request)).thenThrow(InvalidCredentialException.class);

        ServletException exception = assertThrows(ServletException.class, () -> mockMvc.perform(post("/api/authenticate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.error").value("Bad Request")));
    }
}
