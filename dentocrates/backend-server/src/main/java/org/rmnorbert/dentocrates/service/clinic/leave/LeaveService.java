package org.rmnorbert.dentocrates.service.clinic.leave;

import org.rmnorbert.dentocrates.custom.exceptions.NotFoundException;
import org.rmnorbert.dentocrates.dao.clinic.Clinic;
import org.rmnorbert.dentocrates.dao.leave.Leave;
import org.rmnorbert.dentocrates.dto.leave.LeaveDTO;
import org.rmnorbert.dentocrates.dto.leave.LeaveDeleteDTO;
import org.rmnorbert.dentocrates.dto.leave.LeaveRegisterDTO;
import org.rmnorbert.dentocrates.repository.clinic.ClinicRepository;
import org.rmnorbert.dentocrates.repository.clinic.leave.LeaveRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.rmnorbert.dentocrates.controller.ApiResponseConstants.*;

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
        return ResponseEntity.ok("Leave" + SUCCESSFUL_REGISTER_RESPONSE_CONTENT);
    }

    public ResponseEntity<String> deleteLeave(LeaveDeleteDTO dto) {
        Clinic clinic = getClinicById(dto.clinicId());
        if (dto.dentistId() == clinic.getDentistInContract().getId()) {
            leaveRepository.deleteById(dto.leaveId());
            return ResponseEntity.ok("Leave" + DELETE_RESPONSE_CONTENT);
        }
        return ResponseEntity.badRequest().body(INVALID_REQUEST_RESPONSE_CONTENT + " delete leave");
    }
    private Clinic getClinicById(long id){
        return clinicRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Clinic"));
    }
}
