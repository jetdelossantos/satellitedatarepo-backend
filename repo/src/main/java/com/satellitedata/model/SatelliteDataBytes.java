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
@Table(name = "SATDATABYTES")
public class SatelliteDataBytes implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false, updatable = false)
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	private Long id;
	private String gst;
	private String datatype;
	private String time;
	private String sensor;
	private String checksum;
	private String filename;
	private String format;
	private Date created;
	private Date modified;
	
	public SatelliteDataBytes() {
		
	}
	
	public SatelliteDataBytes(Long id, String gst, String datatype, String time, String sensor, String checksum,
			String filename, String format, Date created, Date modified) {
		super();
		this.id = id;
		this.gst = gst;
		this.datatype = datatype;
		this.time = time;
		this.sensor = sensor;
		this.checksum = checksum;
		this.filename = filename;
		this.format = format;
		this.created = created;
		this.modified = modified;
	}

	public Long getId() {
		return id;
	}

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getGst() {
		return gst;
	}

	public void setGst(String gst) {
		this.gst = gst;
	}

	public String getDatatype() {
		return datatype;
	}

	public void setDatatype(String datatype) {
		this.datatype = datatype;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getSensor() {
		return sensor;
	}

	public void setSensor(String sensor) {
		this.sensor = sensor;
	}

	public String getChecksum() {
		return checksum;
	}

	public void setChecksum(String checksum) {
		this.checksum = checksum;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public Date getModified() {
		return modified;
	}

	public void setModified(Date modified) {
		this.modified = modified;
	}

	
}

