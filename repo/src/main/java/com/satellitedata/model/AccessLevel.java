package com.satellitedata.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "ACCESSLEVEL")
public class AccessLevel {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int accesslevels;
	private String accessname;
	private java.sql.Date created;
	private java.sql.Date modified;
	
	public int getAccesslevels() {
		return accesslevels;
	}
	public void setAccesslevels(int accesslevels) {
		this.accesslevels = accesslevels;
	}
	public String getAccessname() {
		return accessname;
	}
	public void setAccessname(String accessname) {
		this.accessname = accessname;
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

}
