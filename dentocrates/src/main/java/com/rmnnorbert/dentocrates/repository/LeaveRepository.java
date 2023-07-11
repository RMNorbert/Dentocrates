package com.rmnnorbert.dentocrates.repository;

import com.rmnnorbert.dentocrates.dao.clinic.Leave;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LeaveRepository extends JpaRepository<Leave, Long> {
    List<Leave> getAllByClinic_Id(long id);
}
