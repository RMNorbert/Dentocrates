package com.rmnnorbert.dentocrates.controller.clinic;

import com.rmnnorbert.dentocrates.dto.leave.LeaveDTO;
import com.rmnnorbert.dentocrates.dto.leave.LeaveDeleteDTO;
import com.rmnnorbert.dentocrates.dto.leave.LeaveRegisterDTO;
import com.rmnnorbert.dentocrates.service.clinic.leave.LeaveService;
import com.rmnnorbert.dentocrates.service.client.communicationServices.NotificationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/leave")
public class LeaveController {
    private final LeaveService leaveService;
    private final NotificationService notificationService;
    @Autowired
    public LeaveController(LeaveService leaveService, NotificationService notificationService) {
        this.leaveService = leaveService;
        this.notificationService = notificationService;
    }
    @GetMapping("/{id}")
    public List<LeaveDTO> getLeavesFromDateOfClinic(@PathVariable long id) {
        return leaveService.getAllLeavesFromDateOfClinic(id);
    }
    @PostMapping("/")
    public ResponseEntity<String> registerLeaveToClinic(@Valid @RequestBody LeaveRegisterDTO dto) {
        ResponseEntity<String> registeredLeave = leaveService.registerLeave(dto);
        notificationService.sendLeaveNotifications(dto);
        return registeredLeave;
    }
    @DeleteMapping("/")
    public ResponseEntity<String> deleteLeaveOfClinic(@Valid @RequestBody LeaveDeleteDTO dto) {
        return leaveService.deleteLeave(dto);
    }
}
