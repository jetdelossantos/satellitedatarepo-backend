package com.satellitedata.model;

import java.io.Serializable;
import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "USERS")
public class User implements Serializable{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false, updatable = false)
	private int Id;
	private int country;
	private int accesslevel;
	private String firstname;
	private String lastname;
	private String username;
	private String email;
	private String password;
	private String contact;
	private Date created;
	private Date modified;
	private Date lastlogindate;
	private boolean isactive;
	private boolean isnotlocked;
	private String[] authorities;
	private String role;
	
	public User() {

	}


	public User(int id, int country, int accesslevel, String firstname, String lastname, String username, String email,
			String password, String contact, Date created, Date modified, Date lastlogindate, boolean isactive,
			boolean isnotlocked, String[] authorities, String role) {
		super();
		Id = id;
		this.country = country;
		this.accesslevel = accesslevel;
		this.firstname = firstname;
		this.lastname = lastname;
		this.username = username;
		this.email = email;
		this.password = password;
		this.contact = contact;
		this.created = created;
		this.modified = modified;
		this.lastlogindate = lastlogindate;
		this.isactive = isactive;
		this.isnotlocked = isnotlocked;
		this.authorities = authorities;
		this.role = role;
	}



	public int getId() {
		return Id;
	}
	public void setId(int id) {
		Id = id;
	}
	public int getCountry() {
		return country;
	}
	public void setCountry(int country) {
		this.country = country;
	}
	public int getAccesslevel() {
		return accesslevel;
	}
	public void setAccesslevel(int accesslevel) {
		this.accesslevel = accesslevel;
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
	public String getContact() {
		return contact;
	}
	public void setContact(String contact) {
		this.contact = contact;
	}
	public java.sql.Date getCreated() {
		return created;
	}
	public void setCreated(java.sql.Date created) {
		this.created = created;
	}
	public java.sql.Date getModified() {
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
	
	public java.sql.Date getLastlogindate() {
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


	public String getRoles() {
		return role;
	}


	public void setRoles(String role) {
		this.role = role;
	}

	
}
