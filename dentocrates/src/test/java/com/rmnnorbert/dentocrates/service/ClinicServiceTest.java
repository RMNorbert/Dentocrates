package com.rmnnorbert.dentocrates.service;

import com.rmnnorbert.dentocrates.dto.clinic.ClinicRegisterDTO;
import com.rmnnorbert.dentocrates.dto.clinic.ClinicResponseDTO;
import com.rmnnorbert.dentocrates.custom.exceptions.NotFoundException;
import com.rmnnorbert.dentocrates.dao.client.Dentist;
import com.rmnnorbert.dentocrates.dao.clinic.Clinic;
import com.rmnnorbert.dentocrates.dao.location.Location;
import com.rmnnorbert.dentocrates.data.clinic.ClinicType;
import com.rmnnorbert.dentocrates.repository.clinic.ClinicRepository;
import com.rmnnorbert.dentocrates.repository.client.DentistRepository;
import com.rmnnorbert.dentocrates.repository.clinic.location.LocationRepository;
import com.rmnnorbert.dentocrates.service.clinic.ClinicService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class ClinicServiceTest {
    @Mock
    private ClinicRepository clinicRepository;
    @Mock
    private DentistRepository dentistRepository;
    @Mock
    private LocationRepository locationRepository;
    @InjectMocks
    private ClinicService clinicService;
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllClinicWhenThereAreClinics() {
        long id = 1;
        long id2 = 2;
        Location clinicLocation = Location.builder().id(id).zipCode(1020).city("city").build();
        Dentist dentist = Dentist.builder().id(id).build();
        Clinic clinic = Clinic.builder()
                .id(id)
                .location(clinicLocation)
                .dentistInContract(dentist)
                .clinicType(ClinicType.COMMUNITY_DENTAL_CLINIC)
                .build();
        Clinic clinic2 = Clinic.builder()
                .id(id2)
                .location(clinicLocation)
                .dentistInContract(dentist)
                .clinicType(ClinicType.COMMUNITY_DENTAL_CLINIC)
                .build();
        List<Clinic> clinicList = List.of(clinic,clinic2);

        when(clinicRepository.findAll()).thenReturn(clinicList);

        List<ClinicResponseDTO> actual = clinicService.getAllClinic();

        assertEquals(clinicList.size(),actual.size());
        assertEquals(id,actual.get(0).id());
        assertEquals(id2,actual.get(1).id());
    }
    @Test
    void getAllClinicWhenThereAreNoClinics() {
        List<Clinic> clinicList = new ArrayList<>();

        when(clinicRepository.findAll()).thenReturn(clinicList);

        List<ClinicResponseDTO> actual = clinicService.getAllClinic();

        assertEquals(clinicList.size(),actual.size());
    }
    @Test
    void registerClinicWithValidLocation() {
        long id = 1;
        ClinicRegisterDTO dto = new ClinicRegisterDTO("name",
                ClinicType.PRIVATE_DENTAL_CLINIC.toString(),
                "1",
                "website",
                1020,
                "city",
                "street",
                "6-12",
                id);
        Dentist dentist = Dentist.builder().id(id).build();
        Location location = Location.builder().build();

        when(dentistRepository.getReferenceById(id)).thenReturn(dentist);
        when(locationRepository.getByZipCode(dto.zipCode())).thenReturn(Optional.ofNullable(location));

        ResponseEntity<String> actual = clinicService.registerClinic(dto);
        ResponseEntity<String> expected = ResponseEntity.ok("Clinic successfully registered");

        assertEquals(expected,actual);
    }
    @Test
    void registerClinicWithInvalidDentistId() {
        long id = 1;
        ClinicRegisterDTO dto = new ClinicRegisterDTO("name",
                ClinicType.PRIVATE_DENTAL_CLINIC.toString(),
                "1",
                "website",
                1020,
                "city",
                "street",
                "6-12",
                id);

        when(dentistRepository.getReferenceById(id)).thenThrow(new NotFoundException("Dentist"));

        assertThrows(NotFoundException.class, () -> clinicService.registerClinic(dto));
        verify(dentistRepository,times(1)).getReferenceById(id);
    }
    @Test
    void registerClinicWithInvalidLocation() {
        long id = 1;
        ClinicRegisterDTO dto = new ClinicRegisterDTO("name",
                ClinicType.PRIVATE_DENTAL_CLINIC.toString(),
                "1",
                "website",
                1020,
                "city",
                "street",
                "6-12",
                id);
        Dentist dentist = Dentist.builder().id(id).build();

        when(dentistRepository.getReferenceById(id)).thenReturn(dentist);
        when(locationRepository.getByZipCode(dto.zipCode())).thenReturn(Optional.empty());

        ResponseEntity<String> actual = clinicService.registerClinic(dto);
        ResponseEntity<String> expected = ResponseEntity.badRequest().body("Location not found.");

        assertEquals(expected,actual);
    }

    @Test
    void deleteClinicByIdWhenClinicExist() {
        long id = 1;
        Location clinicLocation = Location.builder().id(id).zipCode(1020).city("city").build();
        Dentist dentist = Dentist.builder().id(id).build();
        Clinic clinic = Clinic.builder()
                .id(id)
                .location(clinicLocation)
                .dentistInContract(dentist)
                .clinicType(ClinicType.COMMUNITY_DENTAL_CLINIC)
                .build();

        when(clinicRepository.findById(id)).thenReturn(Optional.ofNullable(clinic));

        ResponseEntity<String> actual = clinicService.deleteClinicById(id);
        ResponseEntity<String> expected = ResponseEntity.ok("Clinic deleted successfully");

        assertEquals(expected,actual);
    }
    @Test
    void deleteClinicByIdWhenClinicDoesNotExist() {
        long id = 1;
        assertThrows(NotFoundException.class, () -> clinicService.deleteClinicById(id));
    }
    @Test
    void getClinicByIdWhenClinicExist() {
        long id = 1;
        Location clinicLocation = Location.builder().id(id).zipCode(1020).city("city").build();
        Dentist dentist = Dentist.builder().id(id).build();
        Clinic clinic = Clinic.builder()
                .id(id)
                .location(clinicLocation)
                .dentistInContract(dentist)
                .clinicType(ClinicType.COMMUNITY_DENTAL_CLINIC)
                .build();

        when(clinicRepository.findById(id)).thenReturn(Optional.ofNullable(clinic));

        ClinicResponseDTO actual = clinicService.getClinicResponseDTOById(id);
        ClinicResponseDTO expected = ClinicResponseDTO.of(clinic);

        assertEquals(expected,actual);
    }
    @Test
    void getClinicByIdWhenClinicDoesNotExist() {
        long id = 1;
        assertThrows(NotFoundException.class, () -> clinicService.getClinicResponseDTOById(id));
    }
    @Test
    void getAllClinicByDentistWhenClinicsExist() {
        long id = 1;
        long id2 = 2;
        Location clinicLocation = Location.builder().id(id).zipCode(1020).city("city").build();
        Dentist dentist = Dentist.builder().id(id).build();
        Clinic clinic = Clinic.builder()
                .id(id)
                .location(clinicLocation)
                .dentistInContract(dentist)
                .clinicType(ClinicType.COMMUNITY_DENTAL_CLINIC)
                .build();
        Clinic clinic2 = Clinic.builder()
                .id(id2)
                .location(clinicLocation)
                .dentistInContract(dentist)
                .clinicType(ClinicType.COMMUNITY_DENTAL_CLINIC)
                .build();
        List<Clinic> clinicList = List.of(clinic,clinic2);

        when(clinicRepository.findAllByDentistInContract_Id(id)).thenReturn(clinicList);

        List<ClinicResponseDTO> actual = clinicService.getAllClinicByDentist(id);

        assertEquals(clinicList.size(),actual.size());
        assertEquals(id,actual.get(0).id());
        assertEquals(id2,actual.get(1).id());
    }
    @Test
    void getAllClinicByDentistClinicsDoNotExist() {
        long id = 1;
        List<Clinic> clinicList = new ArrayList<>();

        when(clinicRepository.findAllByDentistInContract_Id(id)).thenReturn(clinicList);

        List<ClinicResponseDTO> actual = clinicService.getAllClinicByDentist(id);

        assertEquals(clinicList.size(), actual.size());
    }
}
