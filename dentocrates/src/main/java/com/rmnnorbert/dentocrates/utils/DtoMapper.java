package com.rmnnorbert.dentocrates.utils;

import com.rmnnorbert.dentocrates.controller.dto.appointment.AppointmentDTO;
import com.rmnnorbert.dentocrates.controller.dto.client.customer.CustomerRegisterDTO;
import com.rmnnorbert.dentocrates.controller.dto.client.dentist.DentistRegisterDTO;
import com.rmnnorbert.dentocrates.dao.client.Customer;
import com.rmnnorbert.dentocrates.dao.client.Dentist;
import com.rmnnorbert.dentocrates.dao.clinic.AppointmentCalendar;
import com.rmnnorbert.dentocrates.data.Role;

public class DtoMapper {
    private static final Role CUSTOMER_ROLE = Role.CUSTOMER;
    private static final Role DENTIST_ROLE = Role.DENTIST;
    public static Customer toEntity(CustomerRegisterDTO request, String password){
        return Customer.builder()
                .email(request.email())
                .password(password)
                .firstName(request.firstName())
                .lastName(request.lastName())
                .role(CUSTOMER_ROLE)
                .build();
    }
    public static Dentist toEntity(DentistRegisterDTO request, String password){
        return Dentist.builder()
                .email(request.email())
                .password(password)
                .firstName(request.firstName())
                .lastName(request.lastName())
                .role(DENTIST_ROLE)
                .operatingLicenceNo(request.operatingLicenceNo())
                .build();
    }

}
