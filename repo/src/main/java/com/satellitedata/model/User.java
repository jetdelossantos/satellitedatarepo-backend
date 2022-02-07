package com.satellitedata.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name = "USERS")
public class User implements Serializable{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false, updatable = false)
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	private Long id;
	private String userid;
	private int country;
	private String firstname;
	private String lastname;
	private String username;
	private String email;
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	private String password;
	private Date created;
	private Date modified;
    private String profileImageUrl;
	private Date lastlogindate;
	private boolean isactive;
	private boolean isnotlocked;
	private String[] authorities;
	private String role;
	
	public User() {

	}

	public User(Long id, String userid, int country, String firstname, String lastname, String username, String email,
			String password, Date created, Date modified, String profileImageUrl, Date lastlogindate, boolean isactive,
			boolean isnotlocked, String[] authorities, String role) {
		super();
		this.id = id;
		this.userid = userid;
		this.country = country;
		this.firstname = firstname;
		this.lastname = lastname;
		this.username = username;
		this.email = email;
		this.password = password;
		this.created = created;
		this.modified = modified;
		this.profileImageUrl = profileImageUrl;
		this.lastlogindate = lastlogindate;
		this.isactive = isactive;
		this.isnotlocked = isnotlocked;
		this.authorities = authorities;
		this.role = role;
	}

	public String getUserid() {
		return userid;
	}




	public String getProfileImageUrl() {
		return profileImageUrl;
	}

	public void setProfileImageUrl(String profileImageUrl) {
		this.profileImageUrl = profileImageUrl;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}




	public Long getId() {
		return id;
	}



	public void setId(Long id) {
		this.id = id;
	}



	public int getCountry() {
		return country;
	}
	public void setCountry(int country) {
		this.country = country;
	}
	public String getFirstname() {
		return firstname;
	}
	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}
	public String getLastname() {
		return lastname;
	}
	public void setLastname(String lastname) {
		this.lastname = lastname;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
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
	public java.util.Date getCreated() {
		return created;
	}
	public void setCreated(java.util.Date date) {
		this.created = date;
	}
	public java.util.Date getModified() {
		return modified;
	}
	public void setModified(java.sql.Date modified) {
		this.modified = modified;
	}
	public boolean isIsactive() {
		return isactive;
	}
	public void setIsactive(boolean isactive) {
		this.isactive = isactive;
	}
	
	public java.util.Date getLastlogindate() {
		return lastlogindate;
	}
	public void setLastlogindate(java.util.Date date) {
		this.lastlogindate = (Date) date;
	}

	public boolean isIsnotlocked() {
		return isnotlocked;
	}

	public void setIsnotlocked(boolean isnotlocked) {
		this.isnotlocked = isnotlocked;
	}

	public String[] getAuthorities() {
		return authorities;
	}

	public void setAuthorities(String[] authorities) {
		this.authorities = authorities;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}


	
}
