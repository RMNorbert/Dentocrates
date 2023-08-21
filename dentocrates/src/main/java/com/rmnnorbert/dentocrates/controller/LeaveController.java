package com.rmnnorbert.dentocrates.controller;

import com.rmnnorbert.dentocrates.controller.dto.clinic.leave.LeaveDTO;
import com.rmnnorbert.dentocrates.controller.dto.clinic.leave.LeaveDeleteDTO;
import com.rmnnorbert.dentocrates.controller.dto.clinic.leave.LeaveRegisterDTO;
import com.rmnnorbert.dentocrates.service.LeaveService;
import com.rmnnorbert.dentocrates.service.NotificationService;
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
    public ResponseEntity<String> registerLeaveToClinic(@RequestBody LeaveRegisterDTO dto) {
        ResponseEntity<String> registeredLeave = leaveService.registerLeave(dto);
        notificationService.sendLeaveNotifications(dto);
        return registeredLeave;
    }
    @DeleteMapping("/")
    public ResponseEntity<String> deleteLeaveOfClinic(@RequestBody LeaveDeleteDTO dto) {
        return leaveService.deleteLeave(dto);
    }
}
