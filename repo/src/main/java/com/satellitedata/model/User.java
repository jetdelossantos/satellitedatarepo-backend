package com.satellitedata.model;

import java.io.Serializable;
import java.sql.Date;
import java.time.LocalDateTime;

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
	private String[] permissions;
	
	public User() {

	}
	
	public User(int id, int country, int accesslevel, String firstname, String lastname, String username, String email,
			String password, String contact, Date created, Date modified, Date lastlogindate, boolean isactive,
			boolean islocked) {
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
		this.isnotlocked = islocked;
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
	public boolean isIsNotlocked() {
		return isnotlocked;
	}
	public void setIslocked(boolean isnotlocked) {
		this.isnotlocked = isnotlocked;
	}
	public java.sql.Date getLastlogindate() {
		return lastlogindate;
	}
	public void setLastlogindate(java.util.Date date) {
		this.lastlogindate = (Date) date;
	}

	public String[] getPermissions() {
		return permissions;
	}

	public void setPermissions(String[] permissions) {
		this.permissions = permissions;
	}

	
}
