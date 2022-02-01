package com.satellitedata.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "USERS")
public class User {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int Id;
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
	private int country;
	private int accesslevel;
	private String firstname;
	private String lastname;
	private String username;
	private String email;
	private String password;
	private String contact;
	private java.sql.Date created;
	private java.sql.Date modified;


}
