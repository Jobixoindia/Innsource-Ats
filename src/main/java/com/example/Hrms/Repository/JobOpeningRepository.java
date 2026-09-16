package com.example.Hrms.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.Hrms.Entity.JobOpening;

public interface JobOpeningRepository extends JpaRepository<JobOpening, Integer> 
{
    Optional<JobOpening> findById(Integer id);
    Optional<JobOpening> findByTitle(String title);

}