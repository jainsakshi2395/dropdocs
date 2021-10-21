package com.docs;

import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.docs.model.Document;
import com.docs.model.Users;
import com.docs.repo.DocumentRepo;
import com.docs.repo.UserRepo;
import com.docs.service.IDocumentService;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;



@Controller
public class ApplicationController {

	@Autowired
	DocumentRepo documentDao;
	
	@Autowired
	UserRepo userRepo;
	
	@Autowired
	IDocumentService documentService;
	
	@GetMapping("/")
	public String viewHomePage(Model model ){
		
		//List<Document> listDocs = documentDao.getAllFilesByUsername();
		// model.addAttribute("listDocs", listDocs);
		return "home";
		
	}
	
	@PostMapping("/upload")
	public String uploadFile(@Param("email") String email,@RequestParam("document") MultipartFile multipartFile, @RequestParam("description") String description, RedirectAttributes ra) throws IOException {
			
		
		try {
			documentService.uploadFileToS3(multipartFile,description, email);
	
		} catch (Exception e) {
			
			System.out.println(e.getMessage());
		
		}
		
		ra.addFlashAttribute("message","File uploaded successfully");
		
		
		return "redirect:/dashboard";
		
	}
	
	@GetMapping("/download")
	public void downloadFile(@Param("id") Long id , HttpServletResponse response) throws Exception {
		
		try {
			documentService.downloadFileFromS3(id,response);
		} catch (Exception e) {
		
			System.out.println(e.getMessage());
		}
		
		
		
	}
	
	@GetMapping("/register")
	public String showRegistrationForm(Model model) {
	    model.addAttribute("user", new Users());
	     
	    return "signup";
	}
	
	@PostMapping("/signup")
	public String signupNewUSer(Users user) {

	    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
	    String encodedPassword = passwordEncoder.encode(user.getPassword());
	    user.setPassword(encodedPassword);
		user.setDateCreated(new Date());
	    userRepo.save(user);
	    return "signup_success";
	}
	
	@GetMapping("/admindashboard")
	public String getAdminDashboard(Model model) {
		
		List<Document> listDocs = documentDao.getAllFiles();
		 model.addAttribute("listDocs", listDocs);

	    return "dd_admindashboard";
	}
	
	@GetMapping("/dashboard")
	public String getUserDashboard(Model model) {
		
		List<Document> listDocs = documentDao.getAllFiles();
		 model.addAttribute("listDocs", listDocs);

	    return "dashboard";
	}
	
	
	@GetMapping("/test")
	public String gettest(Model model) {
		
		List<Document> listDocs = documentDao.getAllFiles();
		 model.addAttribute("listDocs", listDocs);

	    return "test";
	}
	
	
	@GetMapping("/userdashboard")
	public String getDashboardForUser(@Param("email") String email,Model model) {
			
		
		if(email.equalsIgnoreCase("portaladmin@test.com")) {
				 
			return "redirect:/admindashboard"; 
			
		}else {
			List<Document> listDocs = documentDao.getAllFilesByUsername(email);
			 model.addAttribute("listDocs", listDocs);

		    return "userdashboard";
		}
		
	}
	
	@PostMapping("/reupload")
	public String reUploadFile(@Param("id") Long id,@Param("email") String email,@RequestParam("document") MultipartFile multipartFile, @RequestParam("description") String description, RedirectAttributes ra) throws IOException {
			
		
		try {
			documentService.reuploadFile(id,multipartFile, description, email);
	
		} catch (Exception e) {
			
			System.out.println(e.getMessage());
		
		}
		
		ra.addFlashAttribute("message","File edited successfully.. ");
		
		
		return "redirect:/userdashboard?email="+email; 
		
	}
	
	@GetMapping("/delete")
	public String deleteFile(@Param("id") Long id , HttpServletResponse response) throws Exception {
		
		try {
			documentService.deleteFileFromS3(id,response);
		} catch (Exception e) {
		
			System.out.println(e.getMessage());
		}
		
		 return "dashboard";
		
	}
	
	@PostMapping("/getallfiles")
	public String getfilesByUser(@Param("email") String email,RedirectAttributes ra,Model model) throws IOException {
			
		
		try {
			
			List<Document> listDocs = documentDao.getAllFilesByUsername(email);
			 model.addAttribute("listDocs", listDocs);
			
	
		} catch (Exception e) {
			
			System.out.println(e.getMessage());
		
		}
	
		return "redirect:/userdashboard?email="+email;
		
	}
	
    @RequestMapping(value="/edit/{id}/{email}")    
    public String edit(@PathVariable Long id, @PathVariable String email,Model m){    
           m.addAttribute("email",email);
           m.addAttribute("id",id);
        return "editfile";    
    }  
    
    
    /* It updates model object. */    
    @RequestMapping(value="/editsave/{email}/{id}",method = RequestMethod.POST)    
    public String editsave(@PathVariable String email,@PathVariable Long id,@RequestParam("document") MultipartFile multipartFile, @RequestParam("description") String description, Document doc){    
    	try {
			documentService.reuploadFile(id,multipartFile, description, email);
	
		} catch (Exception e) {
			
			System.out.println(e.getMessage());
		
		}
		
        return "redirect:/userdashboard?email="+email;   
    } 
	
}
