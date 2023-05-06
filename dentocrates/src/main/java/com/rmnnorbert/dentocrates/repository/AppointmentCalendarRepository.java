package com.rmnnorbert.dentocrates.repository;

import com.rmnnorbert.dentocrates.dao.clinic.AppointmentCalendar;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppointmentCalendarRepository extends JpaRepository<AppointmentCalendar,Long> {

}
