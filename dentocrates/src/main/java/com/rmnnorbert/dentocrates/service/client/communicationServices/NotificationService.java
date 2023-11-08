package com.rmnnorbert.dentocrates.service.client.communicationServices;

import com.rmnnorbert.dentocrates.dto.leave.LeaveRegisterDTO;
import com.rmnnorbert.dentocrates.dao.appointmentCalendar.AppointmentCalendar;
import com.rmnnorbert.dentocrates.repository.clinic.appointmentCalendar.AppointmentCalendarRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationService {
    private final GMailerService emailService;
    private final AppointmentCalendarRepository appointmentCalendarRepository;

    public NotificationService(GMailerService emailService, AppointmentCalendarRepository appointmentCalendarRepository) {
        this.emailService = emailService;
        this.appointmentCalendarRepository = appointmentCalendarRepository;
    }

    public boolean sendLeaveNotifications(LeaveRegisterDTO dto) {
        String leaveSubject = "Clinic Vacation/Leave Notice";
        String message = "Dear patient,\n\nWe would like to inform you that the clinic will be closed for vacation/leave from " +
                    dto.startOfTheLeave().toLocalDate() + " to " + dto.endOfTheLeave().toLocalDate() + ". During this time, the clinic won't be able to provide appointments or services. " +
                    "Your previously booked appointment('s) related to this clinic will be deleted between the given period. "+
                    "We apologize for any inconvenience this may cause.\n\nThank you for your understanding.\n\nBest regards,\nThe Dentocrates Team";


        List<String> emailsList = getEmailsToNotify(dto);

        try {
        for (String email: emailsList) {
            emailService.sendMail(email, leaveSubject, message, "http://localhost:3000/");
        }
        return true;
        }
        catch (Exception e) {
            return false;
        }
    }

    private List<AppointmentCalendar> getAffectedAppointmentsByLeave(LeaveRegisterDTO dto) {
        return appointmentCalendarRepository.getAllByLeave(dto.clinicId(),
                                                           dto.startOfTheLeave(),
                                                           dto.endOfTheLeave()
                                                          );
    }
    private List<String> getEmailsToNotify(LeaveRegisterDTO dto) {
        List<AppointmentCalendar> affectedAppointments = getAffectedAppointmentsByLeave(dto);
        return affectedAppointments.stream()
                .map(appointmentCalendar -> appointmentCalendar.getCustomer().getEmail())
                .toList();
    }
}
