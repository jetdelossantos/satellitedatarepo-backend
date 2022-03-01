package com.satellitedata.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name = "SATDATAFILES")
public class SatelliteFileData implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false, updatable = false)
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	private Long id;
	private String fileuniqueid;
	private Long filesize;
	private String filename;
	private String filenameUrl;
	@Lob
	private byte[] data;
	private String uploader;
	private String format;
	private int downloads;
	private Date created;
	private Date modified;
	
	public SatelliteFileData() {

	}
	

	public SatelliteFileData(Long id, String fileuniqueid, Long filesize, String filename, String filenameUrl, byte[] data, String uploader,
			String format, int downloads, Date created, Date modified) {
		super();
		this.id = id;
		this.fileuniqueid = fileuniqueid;
		this.filesize = filesize;
		this.filename = filename;
		this.filenameUrl = filenameUrl;
		this.data = data;
		this.uploader = uploader;
		this.format = format;
		this.downloads = downloads;
		this.created = created;
		this.modified = modified;
	}


	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getFileuniqueid() {
		return fileuniqueid;
	}

	public void setFileuniqueid(String fileuniqueid) {
		this.fileuniqueid = fileuniqueid;
	}

	public String getFilename() {
		return filename;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}
	public String getFilenameUrl() {
		return filenameUrl;
	}
	public void setFilenameUrl(String filenameUrl) {
		this.filenameUrl = filenameUrl;
	}
	public byte[] getData() {
		return data;
	}
	public void setData(byte[] data) {
		this.data = data;
	}
	public String getUploader() {
		return uploader;
	}
	public void setUploader(String uploader) {
		this.uploader = uploader;
	}
	public String getFormat() {
		return format;
	}
	public void setFormat(String format) {
		this.format = format;
	}
	public int getDownloads() {
		return downloads;
	}
	public void setDownloads(int downloads) {
		this.downloads = downloads;
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

	public Long getFilesize() {
		return filesize;
	}

	public void setFilesize(Long filesize) {
		this.filesize = filesize;
	}
	
	

	
}
