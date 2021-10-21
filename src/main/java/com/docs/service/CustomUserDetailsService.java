package com.docs.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.docs.model.CustomUserDetails;
import com.docs.model.Users;
import com.docs.repo.UserRepo;

public class CustomUserDetailsService implements UserDetailsService{

	 @Autowired
	  private UserRepo userRepo;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {  
		
		//Users user = userRepo.findByEmail(username);
		Users user = userRepo.getUserByUsername(username);
    if (user == null) {
        throw new UsernameNotFoundException("User not found");
    }
    return new CustomUserDetails(user);
}

}
