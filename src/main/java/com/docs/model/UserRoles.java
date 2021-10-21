package com.docs.model;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "user_roles")
public class UserRoles {

	
	@Id
	@Column(name =  "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)	
	private Long userId;
	
	@Column(name =  "role_id")
	 private Integer roleId;
	
	 public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public Integer getRoleId() {
		return roleId;
	}
	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}

	

    
}
