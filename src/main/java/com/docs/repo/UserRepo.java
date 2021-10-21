package com.docs.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.docs.model.Users;

@Repository
public interface UserRepo  extends JpaRepository<Users, Long>{
	
	@Query("SELECT u FROM Users u WHERE u.email = ?1")
    public Users findByEmail(String email);
	
	
	@Query("SELECT u FROM Users u WHERE u.email = :username")
    public Users getUserByUsername(@Param("username") String username);

}
