package com.example.Hrms.Entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;

@Entity
public class JobOpening {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String title;

    private String clientName;

    private String location;

    @Column(nullable = false)
    private Integer vacancies;

    @Column(nullable = false)
    private String status = "Open";

    @Column(columnDefinition = "TEXT")
    private String assignedRecruiters;

    /*
     * Actual JD file
     */
    @Lob
    @Column(columnDefinition = "LONGBLOB")
    private byte[] jobDescriptionFile;

    /*
     * Original file name
     *
     * Example:
     * Java_Developer.pdf
     */
    private String jobDescriptionFileName;

    /*
     * File type
     *
     * Example:
     * application/pdf
     */
    private String jobDescriptionFileType;



    public JobOpening() {
    }

    

    public Integer getId() {
		return id;
	}



	public void setId(Integer id) {
		this.id = id;
	}



	public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Integer getVacancies() {
        return vacancies;
    }

    public void setVacancies(Integer vacancies) {
        this.vacancies = vacancies;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    

    


	public String getAssignedRecruiters() {
		return assignedRecruiters;
	}



	public void setAssignedRecruiters(String assignedRecruiters) {
		this.assignedRecruiters = assignedRecruiters;
	}



	public byte[] getJobDescriptionFile() {
		return jobDescriptionFile;
	}



	public void setJobDescriptionFile(byte[] jobDescriptionFile) {
		this.jobDescriptionFile = jobDescriptionFile;
	}



	public String getJobDescriptionFileName() {
		return jobDescriptionFileName;
	}



	public void setJobDescriptionFileName(String jobDescriptionFileName) {
		this.jobDescriptionFileName = jobDescriptionFileName;
	}



	public String getJobDescriptionFileType() {
		return jobDescriptionFileType;
	}



	public void setJobDescriptionFileType(String jobDescriptionFileType) {
		this.jobDescriptionFileType = jobDescriptionFileType;
	}



	
}