package com.docs.service;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;
import com.docs.model.Document;
import com.docs.repo.DocumentRepo;

@Service
public class DocumentServiceImpl implements IDocumentService{

	private final static Logger logger  = LoggerFactory.getLogger(DocumentServiceImpl.class);
	 
	private static String bucketName     = "file-delivery-bucket-209235793288";
	
	  @Autowired
	DocumentRepo documentDao;
	
	@Override
	public void uploadFileToS3(MultipartFile file, String description,String email) throws Exception {
		
		 logger.info("inside  uploadFileToS3 ");
		
		String fileName = StringUtils.cleanPath(file.getOriginalFilename());

		//Code to upload document on S3
		
		try {
			
			File convFile = new File(file.getOriginalFilename());
			convFile.createNewFile();
			FileOutputStream fos = new FileOutputStream(convFile);
			fos.write(file.getBytes());
			fos.close();
			
		    AWSCredentials awsCredentials =
	                new BasicAWSCredentials(accessKey, secretKey);
		    AmazonS3Client s3Client = (AmazonS3Client) AmazonS3ClientBuilder
	                .standard()
	                .withRegion("us-west-2")
	                .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
	                .build();
			
		    
		    UUID uuid=UUID.randomUUID(); //Generates random UUID.
		    
		    fileName  =  uuid + fileName;
		    
		    logger.info("Filename to be stored on S3 is " + fileName);
		    
		    PutObjectRequest request = new PutObjectRequest(bucketName, fileName, convFile);
		    s3Client.putObject(request);
		    URL s3Url = s3Client.getUrl(bucketName, fileName);
		    System.out.println("S3 url is " + s3Url.toExternalForm());
		    
		    logger.info("S3 url is " + s3Url.toExternalForm());
		    

			//Code to store file in database starts

		    Document document = new Document();
			
			document.setFileName(fileName);
			
			//document.setContent(file.getBytes());
			document.setSize(file.getSize());
			document.setUploadTime(new Date());
			document.setDateCreated(new Date());
		    document.setUrl(s3Url.toExternalForm());
		    document.setDescription(description);
		    document.setUploadedBy(email);
		    documentDao.save(document);
		    
			
			System.out.println("Docs saved succesfully in database");
			 logger.info("Docs saved succesfully in database");
			
			
		}catch(Exception e) {
			
			logger.error("uploadFileToS3 : Exception -- "+ e.getMessage());
		
		}
		

      

	}

	@Override
	public void downloadFileFromS3(Long id,HttpServletResponse response) throws Exception {
		 
		
		 logger.info("inside  downloadFileFromS3 ");
		
		
		 try {
			 
			 Optional<Document> result =  documentDao.findById(id);
				

				if(!result.isPresent()) {
					 throw new Exception("Could not find file with Id : "+ id );
					 
				}
				
				Document document = result.get(); 
				
				
				response.setContentType("application/octet-stream");
				String headerKey = "Content-Disposition";
				String headerValue = "attachment; filename = "+document.getFileName();
				
				
				AWSCredentials awsCredentials =
			                new BasicAWSCredentials(accessKey, secretKey);
				    AmazonS3Client s3Client = (AmazonS3Client) AmazonS3ClientBuilder
			                .standard()
			                .withRegion("us-west-2")
			                .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
			                .build();
					
				
			
				   S3Object s3Object = s3Client.getObject(bucketName, document.getFileName());
				   
				   S3ObjectInputStream objectContent = s3Object.getObjectContent();
				   byte[] content   =  IOUtils.toByteArray(objectContent);
				   
				   
				   response.setHeader(headerKey, headerValue);
					ServletOutputStream outputStream = response.getOutputStream();
					outputStream.write(content);
					outputStream.close();
					
				   
		 }catch (Exception e) {
			 logger.error("downloadFileFromS3 : Exception -- "+ e.getMessage());
		}
		 
		
		   
		
	}

	@Override
	public void reuploadFile(Long id, MultipartFile file,  String description,String email) throws Exception {
		
		 logger.info("inside reuploadFile ");
		 
		 try {
			 
				
				String fileName = StringUtils.cleanPath(file.getOriginalFilename());

				//Code to upload document on S3
				
				File convFile = new File(file.getOriginalFilename());
				convFile.createNewFile();
				FileOutputStream fos = new FileOutputStream(convFile);
				fos.write(file.getBytes());
				fos.close();
				
			    AWSCredentials awsCredentials =
			            new BasicAWSCredentials(accessKey, secretKey);
			    AmazonS3Client s3Client = (AmazonS3Client) AmazonS3ClientBuilder
			            .standard()
			            .withRegion("us-west-2")
			            .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
			            .build();
			    
			    
			    UUID uuid=UUID.randomUUID(); //Generates random UUID.
			    
			    fileName  =  uuid + fileName;
			    
			    logger.info("Filename to be stored on S3 is " + fileName);
				
			    PutObjectRequest request = new PutObjectRequest(bucketName, fileName, convFile);
			    s3Client.putObject(request);
			    URL s3Url = s3Client.getUrl(bucketName, fileName);
			    System.out.println("S3 url is " + s3Url.toExternalForm());
			    

				//Code to store file in database starts

			    Document document = documentDao.getById(id);
				
				document.setFileName(fileName);
				document.setSize(file.getSize());
				document.setUploadTime(new Date());
			    document.setUrl(s3Url.toExternalForm());
			    document.setDescription(description);
			    document.setUploadedBy(email);
			    document.setUpdatedTime(new Date());
			    documentDao.save(document);

			    
				System.out.println("Docs replaced succesfully in database");
			 
		 }catch (Exception e) {
			 logger.error("reuploadFile : Exception -- "+ e.getMessage());
		}

  

}

	@Override
	public void deleteFileFromS3(Long id, HttpServletResponse response) throws Exception {
		
		 logger.info("inside deleteFileFromS3 ");
		 
		 try {
				Optional<Document> result =  documentDao.findById(id);
				

				if(!result.isPresent()) {
					 throw new Exception("Could not find file with Id : "+ id );
					 
				}
				
				Document document = result.get(); 
				
				AWSCredentials awsCredentials =
		                new BasicAWSCredentials(accessKey, secretKey);
			    AmazonS3Client s3Client = (AmazonS3Client) AmazonS3ClientBuilder
		                .standard()
		                .withRegion("us-west-2")
		                .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
		                .build();
				

			    s3Client.deleteObject(bucketName, document.getFileName());
			   
			    documentDao.delete(document);
			 
		 }catch (Exception e) {
			 logger.error("deleteFileFromS3 : Exception -- "+ e.getMessage());
		}
		
		
	}

}
