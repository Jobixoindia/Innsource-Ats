package com.example.Hrms.Entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "requests")
public class Request {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /*
     * reject       = old reject request
     * delete       = delete candidate request
     * status_change = candidate status approval request
     */
    @Column(nullable = false)
    private String type;

    /*
     * Existing candidate ID.
     * NULL when creating a new candidate
     * that needs approval.
     */
    private Integer candidateId;

    private String candidateName;

    /*
     * Status requested by HR.
     *
     * Example:
     * Joined
     * Shortlisted
     * Rejected
     * Interview
     */
    private String requestedStatus;

    /*
     * Complete candidate data for a NEW candidate
     * waiting for approval.
     */
    @Column(columnDefinition = "LONGTEXT")
    private String candidateData;

    @Column(nullable = false)
    private Integer requestedById;

    @Column(nullable = false)
    private String requestedByName;

    @Column(columnDefinition = "TEXT")
    private String note;

    /*
     * pending
     * approved
     * rejected
     */
    @Column(nullable = false)
    private String status = "pending";

    private Integer resolvedById;

    private String resolvedByName;

    private String resolvedByRole;

    private LocalDateTime createdAt =
            LocalDateTime.now();

    private LocalDateTime resolvedAt;

    public Request() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getCandidateId() {
        return candidateId;
    }

    public void setCandidateId(Integer candidateId) {
        this.candidateId = candidateId;
    }

    public String getCandidateName() {
        return candidateName;
    }

    public void setCandidateName(String candidateName) {
        this.candidateName = candidateName;
    }

    public String getRequestedStatus() {
        return requestedStatus;
    }

    public void setRequestedStatus(
            String requestedStatus
    ) {
        this.requestedStatus = requestedStatus;
    }

    public String getCandidateData() {
        return candidateData;
    }

    public void setCandidateData(
            String candidateData
    ) {
        this.candidateData = candidateData;
    }

    public Integer getRequestedById() {
        return requestedById;
    }

    public void setRequestedById(
            Integer requestedById
    ) {
        this.requestedById = requestedById;
    }

    public String getRequestedByName() {
        return requestedByName;
    }

    public void setRequestedByName(
            String requestedByName
    ) {
        this.requestedByName =
                requestedByName;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getResolvedById() {
        return resolvedById;
    }

    public void setResolvedById(
            Integer resolvedById
    ) {
        this.resolvedById = resolvedById;
    }

    public String getResolvedByName() {
        return resolvedByName;
    }

    public void setResolvedByName(
            String resolvedByName
    ) {
        this.resolvedByName =
                resolvedByName;
    }

    public String getResolvedByRole() {
        return resolvedByRole;
    }

    public void setResolvedByRole(
            String resolvedByRole
    ) {
        this.resolvedByRole =
                resolvedByRole;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(
            LocalDateTime createdAt
    ) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getResolvedAt() {
        return resolvedAt;
    }

    public void setResolvedAt(
            LocalDateTime resolvedAt
    ) {
        this.resolvedAt = resolvedAt;
    }
}