package com.rmnnorbert.dentocrates.repository;

import com.rmnnorbert.dentocrates.dao.clinic.AppointmentCalendar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface AppointmentCalendarRepository extends JpaRepository<AppointmentCalendar,Long> {
    List<AppointmentCalendar> getAllByCustomer_Id(long id);

    List<AppointmentCalendar> getAllByClinic_Id(long id);
}
