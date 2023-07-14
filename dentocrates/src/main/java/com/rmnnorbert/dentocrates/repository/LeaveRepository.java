package com.rmnnorbert.dentocrates.repository;

import com.rmnnorbert.dentocrates.dao.clinic.Leave;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface LeaveRepository extends JpaRepository<Leave, Long> {
    List<Leave> getAllByClinic_Id(long id);
}
