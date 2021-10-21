package com.docs.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.docs.model.Document;

import java.util.List;



@Repository
public interface DocumentRepo  extends JpaRepository<Document, Long>{
	
	@Query("SELECT new Document(d.id, d.fileName, d.size,d.url,d.description,d.uploadTime,d.updatedTime) FROM Document d where d.uploadedBy = ?1 ORDER BY d.uploadTime DESC")
	List<Document> getAllFilesByUsername(String email);
	
	@Query("SELECT d FROM Document d WHERE d.id = ?1")
	Document getDocumentById(Long id);
	
	@Query("SELECT new Document(d.fileName, d.size,d.uploadedBy,d.description,d.uploadTime,d.updatedTime) FROM Document d  ORDER BY d.uploadTime DESC")
	List<Document> getAllFiles();

}
