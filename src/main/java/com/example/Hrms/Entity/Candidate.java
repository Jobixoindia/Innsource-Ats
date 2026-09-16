package com.example.Hrms.Entity;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import jakarta.persistence.*;

@Entity
@Table(name = "candidates")
public class Candidate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String date;
    private LocalDateTime updatedAt;

    @Column(nullable = false)
    private String name;

    private String contact1;

    private String contact2;

    private String email;

    private String experience;

    private String currentCompany;

    private String previousCompany;

    private String location;

    private String domain;

    private String postApplied;

    private String subDomain;

    private String others;

    private String source;
    private String referralPersonName;

    private String recruiter;

    private Integer jobOpeningId;
    private String jobId;

    private String clientHiring;

    @Column(nullable = false)
    private String status = "Applied";

    private String packageOffered;

    private String empId;

    private String doj;

    @Column(columnDefinition = "TEXT")
    private String hrComments;
 
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "JSON")
    private Map<String, String> customFields = new HashMap<>();

    public Candidate() {
    }
    @PrePersist
    protected void onCreate() {
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getContact1() {
        return contact1;
    }

    public void setContact1(String contact1) {
        this.contact1 = contact1;
    }


    public String getContact2() {
        return contact2;
    }

    public void setContact2(String contact2) {
        this.contact2 = contact2;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }


    public String getCurrentCompany() {
        return currentCompany;
    }

    public void setCurrentCompany(String currentCompany) {
        this.currentCompany = currentCompany;
    }


    public String getPreviousCompany() {
        return previousCompany;
    }

    public void setPreviousCompany(String previousCompany) {
        this.previousCompany = previousCompany;
    }


    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }


    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }


    public String getPostApplied() {
        return postApplied;
    }

    public void setPostApplied(String postApplied) {
        this.postApplied = postApplied;
    }


    public String getSubDomain() {
        return subDomain;
    }

    public void setSubDomain(String subDomain) {
        this.subDomain = subDomain;
    }


    public String getOthers() {
        return others;
    }

    public void setOthers(String others) {
        this.others = others;
    }


    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }


    public String getReferralPersonName() {
		return referralPersonName;
	}


	public void setReferralPersonName(String referralPersonName) {
		this.referralPersonName = referralPersonName;
	}


	public String getRecruiter() {
        return recruiter;
    }

    public void setRecruiter(String recruiter) {
        this.recruiter = recruiter;
    }


    public Integer getJobOpeningId() {
        return jobOpeningId;
    }

    public void setJobOpeningId(Integer jobOpeningId) {
        this.jobOpeningId = jobOpeningId;
    }


    public String getClientHiring() {
        return clientHiring;
    }

    public void setClientHiring(String clientHiring) {
        this.clientHiring = clientHiring;
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    public String getPackageOffered() {
        return packageOffered;
    }

    public void setPackageOffered(String packageOffered) {
        this.packageOffered = packageOffered;
    }


    public String getEmpId() {
        return empId;
    }

    public void setEmpId(String empId) {
        this.empId = empId;
    }


    public String getDoj() {
        return doj;
    }

    public void setDoj(String doj) {
        this.doj = doj;
    }


    public String getHrComments() {
        return hrComments;
    }

    public void setHrComments(String hrComments) {
        this.hrComments = hrComments;
    }


	public Map<String, String> getCustomFields() {
		return customFields;
	}


	public void setCustomFields(Map<String, String> customFields) {
		this.customFields = customFields;
	}
	public String getJobId() {
		return jobId;
	}
	public void setJobId(String jobId) {
		this.jobId = jobId;
	}
    
    
}