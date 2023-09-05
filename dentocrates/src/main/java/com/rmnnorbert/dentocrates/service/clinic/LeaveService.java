package com.rmnnorbert.dentocrates.service.clinic;

import com.rmnnorbert.dentocrates.controller.dto.clinic.leave.LeaveDTO;
import com.rmnnorbert.dentocrates.controller.dto.clinic.leave.LeaveDeleteDTO;
import com.rmnnorbert.dentocrates.controller.dto.clinic.leave.LeaveRegisterDTO;
import com.rmnnorbert.dentocrates.custom.exceptions.NotFoundException;
import com.rmnnorbert.dentocrates.dao.clinic.Clinic;
import com.rmnnorbert.dentocrates.dao.clinic.Leave;
import com.rmnnorbert.dentocrates.repository.ClinicRepository;
import com.rmnnorbert.dentocrates.repository.LeaveRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LeaveService {
    private final LeaveRepository leaveRepository;
    private final ClinicRepository clinicRepository;
    @Autowired
    public LeaveService(LeaveRepository leaveRepository, ClinicRepository clinicRepository) {
        this.leaveRepository = leaveRepository;
        this.clinicRepository = clinicRepository;
    }

    public List<LeaveDTO> getAllLeavesFromDateOfClinic(long id) {
        return leaveRepository.getAllByClinic_Id(id)
                .stream()
                .map(LeaveDTO::of)
                .toList();
    }

    public ResponseEntity<String> registerLeave(LeaveRegisterDTO dto) {
        Clinic clinic = getClinicById(dto.clinicId());
        Leave leave = Leave.of(dto,clinic);
        leaveRepository.save(leave);
        return ResponseEntity.ok("Leave registered successfully");
    }

    public ResponseEntity<String> deleteLeave(LeaveDeleteDTO dto) {
        Clinic clinic = getClinicById(dto.clinicId());
        if (dto.dentistId() == clinic.getDentistInContract().getId()) {
            leaveRepository.deleteById(dto.leaveId());
            return ResponseEntity.ok("Leave deleted successfully");
        }
        return ResponseEntity.badRequest().body("Invalid delete request.");
    }
    private Clinic getClinicById(long id){
        return clinicRepository.findById(id).orElseThrow(() -> new NotFoundException("Clinic"));
    }
}
