package com.example.Hrms.Repository;

import com.example.Hrms.Entity.Candidate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CandidateRepository
        extends JpaRepository<Candidate, Integer> {

    
    boolean existsByEmailIgnoreCase(String email);

    boolean existsByContact1(String contact1);

  

    boolean existsByEmailIgnoreCaseAndIdNot(
            String email,
            Integer id
    );

    boolean existsByContact1AndIdNot(
            String contact1,
            Integer id
    );
}