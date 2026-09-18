package com.example.Hrms.Service;

import com.example.Hrms.Entity.Candidate;
import com.example.Hrms.Entity.Request;
import com.example.Hrms.Entity.Users;
import com.example.Hrms.Repository.CandidateRepository;
import com.example.Hrms.Repository.RequestRepository;
import com.example.Hrms.Repository.UserRepository;

import tools.jackson.databind.ObjectMapper;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class RequestService {

    private final RequestRepository requestRepository;
    private final CandidateRepository candidateRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;
    private final ObjectMapper objectMapper;

    public RequestService(
            RequestRepository requestRepository,
            CandidateRepository candidateRepository,
            UserRepository userRepository,
            NotificationService notificationService,
            ObjectMapper objectMapper
    ) {
        this.requestRepository = requestRepository;
        this.candidateRepository = candidateRepository;
        this.userRepository = userRepository;
        this.notificationService = notificationService;
        this.objectMapper = objectMapper;
    }
    /*
     * HR creates reject/delete request
     */
  
    @Transactional
    public Request createRequest(
            String type,
            Integer candidateId,
            Integer requestedById,
            String note,
            String requestedStatus,
            String candidateData
    ) {

        Users requestedBy =
                userRepository.findById(requestedById)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Requesting user not found"
                                )
                        );
        if (!"hr".equalsIgnoreCase(requestedBy.getRole())
                && !"manager".equalsIgnoreCase(requestedBy.getRole())) {

            throw new RuntimeException(
                    "Only the assigned person can create this request"
            );
        }
        Candidate candidate = null;

        /*
         * Existing candidate request
         */
        if (candidateId != null) {

            candidate =
                    candidateRepository.findById(
                            candidateId
                    ).orElseThrow(() ->
                            new RuntimeException(
                                    "Candidate not found"
                            )
                    );
        }

        /*
         * status_change must contain
         * the requested status.
         */
        /*
         * status_change must contain
         * the requested status.
         */
        if ("status_change".equalsIgnoreCase(type)) {

            if (requestedStatus == null
                    || requestedStatus.isBlank()) {

                throw new RuntimeException(
                        "Requested status is required"
                );
            }

            /*
             * Find the candidate data.
             *
             * For an existing candidate, use the
             * candidate from the database.
             *
             * For a new candidate, read the recruiter
             * from candidateData.
             */
            Candidate approvalCandidate = candidate;

            if (approvalCandidate == null
                    && candidateData != null
                    && !candidateData.isBlank()) {

                try {

                    approvalCandidate =
                            objectMapper.readValue(
                                    candidateData,
                                    Candidate.class
                            );

                } catch (Exception error) {

                    throw new RuntimeException(
                            "Invalid candidate data"
                    );
                }
            }

            if (approvalCandidate == null
                    || approvalCandidate.getRecruiter() == null
                    || approvalCandidate.getRecruiter().isBlank()) {

                throw new RuntimeException(
                        "Candidate has no assigned recruiter"
                );
            }

            /*
             * Find assigned recruiter.
             */
            Users assignedRecruiter =
                    userRepository.findByNameIgnoreCase(
                            approvalCandidate
                                    .getRecruiter()
                                    .trim()
                    ).orElseThrow(() ->
                            new RuntimeException(
                                    "Assigned recruiter not found"
                            )
                    );

            /*
             * Manager-assigned candidates never
             * require approval.
             */
            if ("manager".equalsIgnoreCase(
                    assignedRecruiter.getRole()
            )) {

                throw new RuntimeException(
                        "Manager-assigned candidates do not require approval"
                );
            }

            /*
             * Only HR-assigned candidates can
             * require status approval.
             */
            if (!"hr".equalsIgnoreCase(
                    assignedRecruiter.getRole()
            )) {

                throw new RuntimeException(
                        "Status approval is only required for HR-assigned candidates"
                );
            }

            /*
             * Database status is always direct.
             */
            if ("database".equalsIgnoreCase(
                    requestedStatus
            )) {

                throw new RuntimeException(
                        "Database status does not require approval"
                );
            }
        }

        /*
         * Prevent duplicate pending request
         * for an existing candidate.
         *
         * Only a pending HR status-change request
         * should block another HR status-change request.
         */
        if (candidateId != null) {

            Optional<Request> existingRequest =
                    requestRepository
                            .findFirstByCandidateIdAndTypeAndStatusIgnoreCaseOrderByCreatedAtDesc(
                                    candidateId,
                                    type,
                                    "pending"
                            );

            if (existingRequest.isPresent()) {

                Request existing = existingRequest.get();

                Users existingRequester =
                        userRepository.findById(
                                existing.getRequestedById()
                        ).orElse(null);

                /*
                 * Only an HR's pending status request
                 * should block another status request.
                 */
                if ("status_change".equalsIgnoreCase(type)
                        && existingRequester != null
                        && "hr".equalsIgnoreCase(
                                existingRequester.getRole()
                        )) {

                    throw new RuntimeException(
                            "Status_change request already sent by "
                                    + existing.getRequestedByName()
                    );
                }

                /*
                 * For other request types,
                 * keep the existing duplicate protection.
                 */
                if (!"status_change".equalsIgnoreCase(type)) {

                    throw new RuntimeException(
                            capitalize(type)
                                    + " request already sent by "
                                    + existing.getRequestedByName()
                    );
                }
            }
        }

        Request request = new Request();

        request.setType(type);

        request.setCandidateId(
                candidateId
        );

        request.setCandidateName(
                candidate != null
                        ? candidate.getName()
                        : getCandidateName(candidateData)
        );

        request.setRequestedStatus(
                requestedStatus
        );

        request.setCandidateData(
                candidateData
        );

        request.setRequestedById(
                requestedBy.getId()
        );

        request.setRequestedByName(
                requestedBy.getName()
        );

        request.setNote(note);

        request.setStatus("pending");

        request.setCreatedAt(
                LocalDateTime.now()
        );

        Request savedRequest =
                requestRepository.save(request);

        /*
         * Find Managers
         */
        List<Users> managers =
                userRepository.findByRoleIgnoreCase(
                        "manager"
                );

        /*
         * Find CEOs
         */
        List<Users> ceos =
                userRepository.findByRoleIgnoreCase(
                        "ceo"
                );

        String title;

        if ("status_change".equalsIgnoreCase(type)) {
            title =
                    savedRequest.getCandidateName()
                            + " - "
                            + capitalize(requestedStatus)
                            + " request from "
                            + requestedBy.getName();
        }else {

            title =
                    capitalize(type)
                            + " request from "
                            + requestedBy.getName();
        }

        String body =
                "Candidate: "
                        + savedRequest.getCandidateName();

        /*
         * Notify Managers
         */
        for (Users manager : managers) {

            notificationService.createNotification(
                    manager.getId(),
                    savedRequest.getId(),
                    "request",
                    title,
                    body
            );
        }

        /*
         * Notify CEOs
         */
        for (Users ceo : ceos) {

            notificationService.createNotification(
                    ceo.getId(),
                    savedRequest.getId(),
                    "request",
                    title,
                    body
            );
        }

        return savedRequest;
    }
    @Transactional
    public Request resolveRequest(
            Integer requestId,
            String decision,
            Integer resolvedById
    ) {

        /*
         * Find request
         */
        Request request =
                requestRepository.findById(requestId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Request not found"
                                )
                        );

        /*
         * Prevent second action
         */
        if (!"pending".equalsIgnoreCase(
                request.getStatus()
        )) {

            throw new RuntimeException(
                    "This request has already been resolved"
            );
        }

        /*
         * Find person resolving request
         */
        Users resolvedBy =
                userRepository.findById(resolvedById)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "User not found"
                                )
                        );

        /*
         * Only Manager or CEO can resolve
         */
        boolean allowed =
                "manager".equalsIgnoreCase(
                        resolvedBy.getRole()
                )
                ||
                "ceo".equalsIgnoreCase(
                        resolvedBy.getRole()
                );

        if (!allowed) {

            throw new RuntimeException(
                    "Only Manager or CEO can resolve requests"
            );
        }

        /*
         * Validate decision
         */
        if (!decision.equalsIgnoreCase("approved")
                &&
                !decision.equalsIgnoreCase("rejected")) {

            throw new RuntimeException(
                    "Invalid decision"
            );
        }

        /*
         * Save decision
         */
        request.setStatus(
                decision.toLowerCase()
        );

        request.setResolvedById(
                resolvedBy.getId()
        );

        request.setResolvedByName(
                resolvedBy.getName()
        );

        request.setResolvedByRole(
                resolvedBy.getRole()
        );

        request.setResolvedAt(
                LocalDateTime.now()
        );

        /*
         * If approved:
         *
         * reject request -> candidate becomes Rejected
         *
         * delete request -> candidate is deleted
         */
        if ("approved".equalsIgnoreCase(decision)) {

            /*
             * ==========================================
             * STATUS CHANGE
             * ==========================================
             */
            if ("status_change".equalsIgnoreCase(
                    request.getType()
            )) {

                try {

                    /*
                     * NEW CANDIDATE
                     *
                     * Candidate does not exist yet.
                     */
                	if (request.getCandidateId() == null) {

                	    if (request.getCandidateData() == null
                	            || request.getCandidateData().isBlank()) {

                	        throw new RuntimeException(
                	                "Candidate data is missing from the request"
                	        );
                	    }

                	    Candidate newCandidate =
                	            objectMapper.readValue(
                	                    request.getCandidateData(),
                	                    Candidate.class
                	            );

                	    newCandidate.setStatus(
                	            request.getRequestedStatus()
                	    );

                	    newCandidate.setId(null);

                	    Candidate savedCandidate =
                	            candidateRepository.save(
                	                    newCandidate
                	            );

                	    request.setCandidateId(
                	            savedCandidate.getId()
                	    );

                	    request.setCandidateName(
                	            savedCandidate.getName()
                	    );
                	}else {

                        /*
                         * EXISTING CANDIDATE
                         */
                        Candidate existingCandidate =
                                candidateRepository.findById(
                                        request.getCandidateId()
                                ).orElseThrow(() ->
                                        new RuntimeException(
                                                "Candidate not found"
                                        )
                                );

                        /*
                         * For an existing candidate,
                         * ONLY change the status.
                         */
                        existingCandidate.setStatus(
                                request.getRequestedStatus()
                        );

                        candidateRepository.save(
                                existingCandidate
                        );
                    }

                } catch (Exception error) {

                    throw new RuntimeException(
                            "Failed to apply status change: "
                                    + error.getMessage()
                    );
                }
            }

            /*
             * ==========================================
             * OLD REJECT REQUEST
             * ==========================================
             */
            if ("reject".equalsIgnoreCase(
                    request.getType()
            )) {

                Candidate candidate =
                        candidateRepository.findById(
                                request.getCandidateId()
                        ).orElse(null);

                if (candidate != null) {

                    candidate.setStatus(
                            "Rejected"
                    );

                    candidateRepository.save(
                            candidate
                    );
                }
            }

            /*
             * ==========================================
             * DELETE REQUEST
             * ==========================================
             */
            if ("delete".equalsIgnoreCase(
                    request.getType()
            )) {

                if (request.getCandidateId()
                        != null) {

                    candidateRepository.deleteById(
                            request.getCandidateId()
                    );
                }
            }
        }        Request savedRequest =
                requestRepository.save(request);


        /*
         * ==========================================
         * NOTIFY ORIGINAL HR
         * ==========================================
         */

        String decisionText;

        if ("approved".equalsIgnoreCase(decision)) {
            decisionText = "approved";
        } else {
            decisionText = "rejected";
        }

        String notificationTitle;

        if ("status_change".equalsIgnoreCase(
                request.getType()
        )) {
            notificationTitle =
                    capitalize(
                            request.getRequestedStatus()
                    )
                    + " request "
                    + decisionText;
        } else {
            notificationTitle =
                    capitalize(request.getType())
                            + " request "
                            + decisionText;
        }

        String notificationBody =
                "Candidate: "
                        + request.getCandidateName();
        /*
         * Send notification to the HR who
         * originally created the request.
         */
        notificationService.createNotification(
                request.getRequestedById(),
                savedRequest.getId(),
                "request",
                notificationTitle,
                notificationBody
        );

        return savedRequest;
    }


    /*
     * Get requests created by a particular user
     */
    public List<Request> getRequestsForUser(
            Integer userId
    ) {

        return requestRepository
                .findByRequestedByIdOrderByCreatedAtDesc(
                        userId
                );
    }


    /*
     * Get one request by ID
     */
    public Request getRequestById(
            Integer requestId
    ) {

        return requestRepository
                .findById(requestId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Request not found"
                        )
                );
    }


    /*
     * Capitalize first letter
     */
    
    private String getCandidateName(
            String candidateData
    ) {

        if (candidateData == null
                || candidateData.isBlank()) {

            return "New Candidate";
        }

        try {

            Candidate candidate =
                    objectMapper.readValue(
                            candidateData,
                            Candidate.class
                    );

            return candidate.getName();

        } catch (Exception error) {

            return "New Candidate";
        }
    }
    private String capitalize(
            String value
    ) {

        if (value == null || value.isBlank()) {
            return value;
        }

        return value.substring(0, 1).toUpperCase()
                + value.substring(1);
    }
}