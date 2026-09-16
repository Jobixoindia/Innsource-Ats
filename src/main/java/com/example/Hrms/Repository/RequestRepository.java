package com.example.Hrms.Repository;

import com.example.Hrms.Entity.Request;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RequestRepository
        extends JpaRepository<Request, Integer> {

    List<Request> findByRequestedByIdOrderByCreatedAtDesc(
            Integer requestedById
    );

    Optional<Request> findFirstByCandidateIdAndTypeAndStatusIgnoreCaseOrderByCreatedAtDesc(
            Integer candidateId,
            String type,
            String status
    );
}