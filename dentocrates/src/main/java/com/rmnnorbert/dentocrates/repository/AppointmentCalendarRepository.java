package com.rmnnorbert.dentocrates.repository;

import com.rmnnorbert.dentocrates.dao.clinic.AppointmentCalendar;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AppointmentCalendarRepository extends JpaRepository<AppointmentCalendar,Long> {
    List<AppointmentCalendar> getAllByCustomer_Id(long id);

    List<AppointmentCalendar> getAllByClinic_Id(long id);
}
