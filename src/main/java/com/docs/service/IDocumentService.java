package com.docs.service;

import javax.servlet.http.HttpServletResponse;

import org.springframework.web.multipart.MultipartFile;

public interface IDocumentService {

	void uploadFileToS3(MultipartFile file, String description, String email)  throws Exception ;
	
	void downloadFileFromS3(Long id, HttpServletResponse response) throws Exception;
	
	void reuploadFile(Long id, MultipartFile file, String description,String email) throws Exception;
	
	void deleteFileFromS3(Long id, HttpServletResponse response) throws Exception;
	
	
} 
