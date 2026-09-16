package com.example.Hrms.Entity;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Users 
{
	 @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Integer id;

	    private String name;

	    @Column(unique = true, nullable = false)
	    private String email;

	    @Column(nullable = false)
	    private String password;

	    @Column(nullable = false)
	    private String role;
	    
	    @Column(nullable = false)
	    private boolean active = true;
	    
	    @JdbcTypeCode(SqlTypes.JSON)
	    @Column(columnDefinition = "json")
	    private List<String> pageAccess = new ArrayList<>();

	    @Column(nullable = false)
	    private boolean canAddJobOpening = false;

	    @Column(nullable = false)
	    private boolean canApproveReject = false;
	    

	    public List<String> getPageAccess() {
			return pageAccess;
		}

		public void setPageAccess(List<String> pageAccess) {
			this.pageAccess = pageAccess;
		}

		public boolean isCanAddJobOpening() {
			return canAddJobOpening;
		}

		public void setCanAddJobOpening(boolean canAddJobOpening) {
			this.canAddJobOpening = canAddJobOpening;
		}

		public boolean isCanApproveReject() {
			return canApproveReject;
		}

		public void setCanApproveReject(boolean canApproveReject) {
			this.canApproveReject = canApproveReject;
		}

		public Integer getId() {
			return id;
		}

		public void setId(Integer id) {
			this.id = id;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getEmail() {
			return email;
		}

		public void setEmail(String email) {
			this.email = email;
		}

		public String getPassword() {
			return password;
		}

		public void setPassword(String password) {
			this.password = password;
		}

		public String getRole() {
			return role;
		}

		public void setRole(String role) {
			this.role = role;
		}

		public boolean isActive() {
			return active;
		}

		public void setActive(boolean active) {
			this.active = active;
		}
	    
	    
}
