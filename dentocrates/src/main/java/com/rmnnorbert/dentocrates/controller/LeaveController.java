package com.rmnnorbert.dentocrates.controller;

import com.rmnnorbert.dentocrates.controller.dto.clinic.leave.LeaveDTO;
import com.rmnnorbert.dentocrates.controller.dto.clinic.leave.LeaveDeleteDTO;
import com.rmnnorbert.dentocrates.controller.dto.clinic.leave.LeaveRegisterDTO;
import com.rmnnorbert.dentocrates.service.LeaveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/leave")
public class LeaveController {
    private final LeaveService leaveService;
    @Autowired
    public LeaveController(LeaveService leaveService) {
        this.leaveService = leaveService;
    }
    @GetMapping("/{id}")
    public List<LeaveDTO> getLeavesFromDateOfClinic(@PathVariable long id) {
        return leaveService.getAllLeavesFromDateOfClinic(id);
    }
    @PostMapping("/")
    public ResponseEntity<String> registerLeaveToClinic(@RequestBody LeaveRegisterDTO dto) {
        return leaveService.registerLeave(dto);
    }
    @DeleteMapping("/")
    public ResponseEntity<String> deleteLeaveOfClinic(@RequestBody LeaveDeleteDTO dto) {
        return leaveService.deleteLeave(dto);
    }
}
