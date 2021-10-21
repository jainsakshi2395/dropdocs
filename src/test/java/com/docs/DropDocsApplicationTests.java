package com.docs;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import com.docs.model.Document;
import com.docs.repo.DocumentRepo;



@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
class DropDocsApplicationTests {

	@Autowired
	private DocumentRepo repo;
	
	@Autowired
	private TestEntityManager entityManager;
	
	@Test
	@Rollback(false)
	void testInsertDocument() throws IOException {
		
		File file  = new File("/Users/sakshijain/Downloads/passport-images/0001.jpg");
		
		Document document =  new Document();
		document.setFileName(file.getName());
		
		System.out.println(file.getName());
		
		byte[] bytes = FileUtils.readFileToByteArray(file);
		//document.setContent(bytes);
		long fileSize  = bytes.length;
		document.setSize(fileSize);
		
		Document savedDoc = repo.save(document);
		
		Document existDoc = entityManager.find(Document.class, savedDoc.getId());
		
		assertThat(existDoc.getSize()).isEqualTo(fileSize);
		
	}

}
