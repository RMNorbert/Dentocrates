package com.rmnnorbert.dentocrates.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.rmnnorbert.dentocrates.controller.dto.appointment.AppointmentDTO;
import com.rmnnorbert.dentocrates.controller.dto.appointment.AppointmentRegisterDTO;
import com.rmnnorbert.dentocrates.custom.exceptions.NotFoundException;
import com.rmnnorbert.dentocrates.service.AppointmentCalendarService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

import static com.rmnnorbert.dentocrates.controller.IndexControllerTest.MOCK_ROLE;
import static com.rmnnorbert.dentocrates.controller.IndexControllerTest.MOCK_USERNAME;
import static org.hamcrest.Matchers.emptyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class AppointmentControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private AppointmentCalendarService service;
    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
        objectMapper.registerModule(new JavaTimeModule());
    }
    @ParameterizedTest
    @MethodSource("provideIdAndExpectedList")
    @WithMockUser(username = MOCK_USERNAME,roles = MOCK_ROLE)
    void getCustomerAppointmentsShouldReturnOkStatusAndExpectedList(Long id, List<AppointmentDTO> expected) throws Exception {
        when(service.getAllAppointmentById(id)).thenReturn(expected);

        mockMvc.perform(get("/calendar/customer/" + id))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", Matchers.hasSize(expected.size())));

        verify(service,times(1)).getAllAppointmentById(id);
    }

    @Test
    @WithMockUser(username = MOCK_USERNAME, roles = MOCK_ROLE)
    void addCalendarShouldReturnOkStatusAndExpectedValue() throws Exception {
        ResponseEntity<String> expected = ResponseEntity.ok("Appointment registered successfully");
        AppointmentRegisterDTO dto = new AppointmentRegisterDTO(1, 1, LocalDateTime.now());
        when(service.registerAppointment(dto)).thenReturn(expected);
        String jsonRequest = objectMapper.writeValueAsString(dto);

        mockMvc.perform(post("/calendar/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                    .andExpect(status().isOk())
                    .andExpect(content().string(Matchers.is(expected.getBody())));

        verify(service,times(1)).registerAppointment(dto);
    }
    @Test
    @WithMockUser(username = MOCK_USERNAME, roles = MOCK_ROLE)
    void addCalendarWithInvalidRequestShouldReturnBadRequestStatusAndValidRequestDetailsMessage() throws Exception {
        AppointmentRegisterDTO dto = new AppointmentRegisterDTO(0, 0, LocalDateTime.now());
        String jsonRequest = objectMapper.writeValueAsString(dto);
        String expectedBodyContent = "customerId : must be greater than or equal to 1";
        String expectedBodyContent2 = "clinicId : must be greater than or equal to 1";

        mockMvc.perform(post("/calendar/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(Matchers.containsString(expectedBodyContent)))
                .andExpect(content().string(Matchers.containsStringIgnoringCase(expectedBodyContent2)));

        verify(service,times(0)).registerAppointment(dto);
    }
    @Test
    @WithMockUser(username = MOCK_USERNAME, roles = MOCK_ROLE)
    void addCalendarWithUnregisteredIdShouldReturnNotFoundStatusAndEmptyBody() throws Exception {
        AppointmentRegisterDTO dto = new AppointmentRegisterDTO(2, 1, LocalDateTime.now());
        String jsonRequest = objectMapper.writeValueAsString(dto);
        String expectedBodyContent2 = "clinicId : must be greater than or equal to 1";

        when(service.registerAppointment(dto)).thenThrow(NotFoundException.class);

        mockMvc.perform(post("/calendar/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isNotFound())
                .andExpect(content().string(emptyString()));

        verify(service,times(1)).registerAppointment(dto);
    }


    @Test
    @WithMockUser(username = MOCK_USERNAME)
    void removeAppointment() {

    }

    @Test
    @WithMockUser(username = MOCK_USERNAME)
    void getAppointmentsByClinic() {

    }

    @Test
    @WithMockUser(username = MOCK_USERNAME)
    void updateAppointment() {

    }
    private static Stream<Arguments> provideIdAndExpectedList() {
        return Stream.of(
            Arguments.of(1L,List.of(new AppointmentDTO(1,1,1, LocalDateTime.now(),false))),
            Arguments.of(2L,List.of())
        );
    }
}
