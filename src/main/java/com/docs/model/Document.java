package com.docs.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="document")
public class Document {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String fileName;
	private long size;
	private Date uploadTime;
	private String url;
	private Date dateCreated;
	private String description;
	private String uploadedBy;
	private Date updatedTime;
	
	
	public Document(Long id, String fileName, long size) {
		this.id = id;
		this.fileName = fileName;
		this.size = size;
	}
	
	
	
	
	public Document(Long id, String fileName, long size,String url,String description, Date uploadTime,  
			Date updatedTime) {
		super();
		this.id = id;
		this.fileName = fileName;
		this.size = size;		
		this.url = url;
		this.description = description;
		this.uploadTime = uploadTime;
		this.updatedTime = updatedTime;
	}

	
	
	public Document(String fileName, long size,String uploadedBy,String description, Date uploadTime,  
			Date updatedTime) {
		super();
		this.fileName = fileName;
		this.size = size;		
		this.uploadedBy = uploadedBy;
		this.description = description;
		this.uploadTime = uploadTime;
		this.updatedTime = updatedTime;
	}



	public Document() {
		
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public long getSize() {
		return size;
	}
	public void setSize(long size) {
		this.size = size;
	}
	public Date getUploadTime() {
		return uploadTime;
	}
	public void setUploadTime(Date uploadTime) {
		this.uploadTime = uploadTime;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public Date getDateCreated() {
		return dateCreated;
	}
	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getUploadedBy() {
		return uploadedBy;
	}

	public void setUploadedBy(String uploadedBy) {
		this.uploadedBy = uploadedBy;
	}

	public Date getUpdatedTime() {
		return updatedTime;
	}

	public void setUpdatedTime(Date updatedTime) {
		this.updatedTime = updatedTime;
	}
	
	
	
}
