package com.rmnnorbert.dentocrates.repository.clinic;

import com.rmnnorbert.dentocrates.dao.clinic.Clinic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface ClinicRepository extends JpaRepository<Clinic,Long> {
    List<Clinic> findAllByDentistInContract_Id(long id);

}
