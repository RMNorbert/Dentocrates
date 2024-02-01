package com.rmnnorbert.dentocrates.service.client.communicationServices;

import com.rmnnorbert.dentocrates.dto.leave.LeaveRegisterDTO;
import com.rmnnorbert.dentocrates.dao.appointment.Appointment;
import com.rmnnorbert.dentocrates.repository.clinic.appointment.AppointmentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AppointmentNotificationService {
    private final GMailerService emailService;
    private final AppointmentRepository appointmentRepository;
    public AppointmentNotificationService(GMailerService emailService,
                                          AppointmentRepository appointmentRepository) {
        this.emailService = emailService;
        this.appointmentRepository = appointmentRepository;
    }

    /**
     * Sends vacation/leave notifications to affected patients.
     *
     * This method constructs a standardized leave notification message indicating the clinic's closure
     * for vacation or leave between the specified start and end dates. The message is then sent to all
     * patients with scheduled appointments during this period. The notification includes information about
     * the clinic closure, the deletion of booked appointments, and an apology for any inconvenience caused.
     *
     * @param dto The LeaveRegisterDTO containing details about the start and end dates of the clinic's leave.
     * @return True if the notifications were sent successfully to all affected patients; false otherwise.
     */
    public boolean sendLeaveNotifications(LeaveRegisterDTO dto) {
        String leaveSubject = "Clinic Vacation/Leave Notice";
        String message = "Dear patient,\n\n" +
                "We would like to inform you that the clinic will be closed for vacation/leave" +
                " from " + dto.startOfTheLeave().toLocalDate() + " to " + dto.endOfTheLeave().toLocalDate() +
                ". During this time, the clinic won't be able to provide appointments or services. Your previously " +
                "booked appointment('s) related to this clinic will be deleted between the given period. We apologize "+
                "for any inconvenience this may cause.\n\nThank you for your understanding.\n\nBest regards," +
                "\nThe Dentocrates Team";

        List<String> emailsList = getEmailsToNotify(dto);

        try {
            for (String email: emailsList) {
                emailService.sendMail(email, leaveSubject, message, GMailerService.BASE_URL);
            }
            return true;
        }
        catch (Exception e) {
            return false;
        }
    }

    /**
     * Retrieves a list of appointments affected by the clinic's leave between the specified dates.
     *
     * @param dto The LeaveRegisterDTO containing details about the clinic, start and end dates of the leave.
     * @return List of affected Appointment instances.
     */
    private List<Appointment> getAffectedAppointmentsByLeave(LeaveRegisterDTO dto) {
        return appointmentRepository.getAllByLeave(dto.clinicId(),
                                                           dto.startOfTheLeave(),
                                                           dto.endOfTheLeave()
                                                          );
    }

    private List<String> getEmailsToNotify(LeaveRegisterDTO dto) {
        List<Appointment> affectedAppointments = getAffectedAppointmentsByLeave(dto);
        return affectedAppointments.stream()
                .map(appointment -> appointment.getCustomer().getEmail())
                .toList();
    }
}
