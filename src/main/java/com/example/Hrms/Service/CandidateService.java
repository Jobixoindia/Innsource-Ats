package com.example.Hrms.Service;

import com.example.Hrms.Entity.Candidate;
import com.example.Hrms.Entity.Users;
import com.example.Hrms.Repository.CandidateRepository;
import com.example.Hrms.Repository.UserRepository;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CandidateService {

    private final CandidateRepository repository;
    private final UserRepository userRepository;

    public CandidateService(
            CandidateRepository repository,
            UserRepository userRepository
    ) {
        this.repository = repository;
        this.userRepository = userRepository;
    }

    // GET ALL
    public List<Candidate> getAllCandidates() {

        return repository.findAll();
    }


    // GET BY ID
    public Optional<Candidate> getCandidateById(
            Integer id
    ) {

        return repository.findById(id);
    }


    // CREATE
    public Candidate createCandidate(
            Candidate candidate
    ) {


        // EMAIL DUPLICATE CHECK
        if (candidate.getEmail() != null &&
                !candidate.getEmail().isBlank() &&
                repository.existsByEmailIgnoreCase(
                        candidate.getEmail().trim()
                )) {

            throw new RuntimeException(
                    "Candidate already exists with this email"
            );
        }


        // CONTACT 1 DUPLICATE CHECK
        if (candidate.getContact1() != null &&
                !candidate.getContact1().isBlank() &&
                repository.existsByContact1(
                        candidate.getContact1().trim()
                )) {

            throw new RuntimeException(
                    "Candidate already exists with this Contact No 1"
            );
        }


        if (candidate.getStatus() == null ||
                candidate.getStatus().isBlank()) {

            candidate.setStatus("Applied");
        }


        return repository.save(candidate);
    }


    // UPDATE
    public Candidate updateCandidate(
            Integer id,
            Candidate updatedCandidate,
            Integer updatedById
    ) {

        Candidate existing =
                repository.findById(id)
                        .orElseThrow(
                                () -> new RuntimeException(
                                        "Candidate not found"
                                )
                        );
        String oldStatus =
                existing.getStatus() == null
                        ? ""
                        : existing.getStatus().trim();

        String newStatus =
                updatedCandidate.getStatus() == null
                        ? ""
                        : updatedCandidate.getStatus().trim();

        boolean statusChanged =
                !oldStatus.equalsIgnoreCase(newStatus);

        if (statusChanged) {

            if (existing.getRecruiter() == null
                    || existing.getRecruiter().isBlank()) {

                throw new RuntimeException(
                        "Candidate has no assigned recruiter"
                );
            }

            Users assignedRecruiter =
                    userRepository.findByNameIgnoreCase(
                            existing.getRecruiter().trim()
                    ).orElseThrow(() ->
                            new RuntimeException(
                                    "Assigned recruiter not found"
                            )
                    );

            if ("hr".equalsIgnoreCase(
                    assignedRecruiter.getRole()
            )) {

                if (!"database".equalsIgnoreCase(newStatus)) {

                    throw new RuntimeException(
                            "Status changes require Manager or CEO approval"
                    );
                }

            } else if ("manager".equalsIgnoreCase(
                    assignedRecruiter.getRole()
            )) {

                // Manager-assigned candidates can change status directly.

            }
        }

        // EMAIL DUPLICATE CHECK
        if (updatedCandidate.getEmail() != null &&
                !updatedCandidate.getEmail().isBlank() &&
                repository.existsByEmailIgnoreCaseAndIdNot(
                        updatedCandidate.getEmail().trim(),
                        id
                )) {

            throw new RuntimeException(
                    "Candidate already exists with this email"
            );
        }


        // CONTACT 1 DUPLICATE CHECK
        if (updatedCandidate.getContact1() != null &&
                !updatedCandidate.getContact1().isBlank() &&
                repository.existsByContact1AndIdNot(
                        updatedCandidate.getContact1().trim(),
                        id
                )) {

            throw new RuntimeException(
                    "Candidate already exists with this Contact No 1"
            );
        }


        existing.setDate(
                updatedCandidate.getDate()
        );

        existing.setName(
                updatedCandidate.getName()
        );

        existing.setContact1(
                updatedCandidate.getContact1()
        );

        existing.setContact2(
                updatedCandidate.getContact2()
        );

        existing.setEmail(
                updatedCandidate.getEmail()
        );

        existing.setExperience(
                updatedCandidate.getExperience()
        );

        existing.setCurrentCompany(
                updatedCandidate.getCurrentCompany()
        );

        existing.setPreviousCompany(
                updatedCandidate.getPreviousCompany()
        );

        existing.setLocation(
                updatedCandidate.getLocation()
        );

        existing.setDomain(
                updatedCandidate.getDomain()
        );

        existing.setPostApplied(
                updatedCandidate.getPostApplied()
        );

        existing.setSubDomain(
                updatedCandidate.getSubDomain()
        );

        existing.setOthers(
                updatedCandidate.getOthers()
        );

        existing.setSource(
                updatedCandidate.getSource()
        );

        existing.setRecruiter(
                updatedCandidate.getRecruiter()
        );

        existing.setJobOpeningId(
                updatedCandidate.getJobOpeningId()
        );
        existing.setJobId(
                updatedCandidate.getJobId()
        );


        existing.setClientHiring(
                updatedCandidate.getClientHiring()
        );

        existing.setStatus(
                updatedCandidate.getStatus()
        );

        existing.setPackageOffered(
                updatedCandidate.getPackageOffered()
        );

        existing.setEmpId(
                updatedCandidate.getEmpId()
        );

        existing.setDoj(
                updatedCandidate.getDoj()
        );
        existing.setReferralPersonName(
                updatedCandidate.getReferralPersonName()
        );
        existing.setHrComments(
                updatedCandidate.getHrComments()
        );
        existing.setCustomFields(
                updatedCandidate.getCustomFields()
        );

        
        return repository.save(existing);
    }


    // DELETE
    public void deleteCandidate(
            Integer id
    ) {

        repository.deleteById(id);
    }
}