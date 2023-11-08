package com.rmnnorbert.dentocrates.repository.clinic.leave;

import com.rmnnorbert.dentocrates.dao.leave.Leave;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface LeaveRepository extends JpaRepository<Leave, Long> {
    List<Leave> getAllByClinic_Id(long id);
}
