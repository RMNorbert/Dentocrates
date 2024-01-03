package com.rmnnorbert.dentocrates.service.clinic.leave;

import com.rmnnorbert.dentocrates.dto.leave.LeaveDTO;
import com.rmnnorbert.dentocrates.dto.leave.LeaveDeleteDTO;
import com.rmnnorbert.dentocrates.dto.leave.LeaveRegisterDTO;
import com.rmnnorbert.dentocrates.custom.exceptions.NotFoundException;
import com.rmnnorbert.dentocrates.dao.clinic.Clinic;
import com.rmnnorbert.dentocrates.dao.leave.Leave;
import com.rmnnorbert.dentocrates.repository.clinic.ClinicRepository;
import com.rmnnorbert.dentocrates.repository.clinic.leave.LeaveRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.rmnnorbert.dentocrates.controller.ApiResponseConstants.*;

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
