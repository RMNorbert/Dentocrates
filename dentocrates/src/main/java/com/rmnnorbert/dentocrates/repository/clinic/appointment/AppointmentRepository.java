package com.rmnnorbert.dentocrates.repository.clinic.appointment;

import com.rmnnorbert.dentocrates.dao.appointment.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
@Repository
public interface AppointmentRepository extends JpaRepository<Appointment,Long> {
    List<Appointment> getAllByCustomer_Id(long id);
    List<Appointment> getAllByClinic_Id(long id);
    @Query("SELECT a FROM Appointment a WHERE a.reservation >= CURRENT_DATE AND a.reservation <= CURRENT_DATE + 7 AND a.clinic.id = :id")
    List<Appointment> getTheNextSevenDaysAppointmentsByClinicId(long id);
    @Query("SELECT a FROM Appointment a WHERE a.reservation > :startOfLeave AND a.reservation < :endOfLeave AND a.clinic.id = :id")
    List<Appointment> getAllByLeave(long id, LocalDateTime startOfLeave, LocalDateTime endOfLeave);
}
