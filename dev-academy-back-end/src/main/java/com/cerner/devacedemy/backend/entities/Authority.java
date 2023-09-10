package com.cerner.devacedemy.backend.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.security.core.GrantedAuthority;

import lombok.Data;

@Data
@Entity
@Table(name = "AUTH_USER_DETAILS")
public class Authority implements GrantedAuthority {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@Column(name = "ROLE_CODE")
	private String roleCode;
	
	@Column(name = "ROLE_DESCRIPTION")
	private String roleDescription;
	
	@Override
	public String getAuthority() {
		return roleCode;
	}

	public Authority() {
		super();
	}
	
	public Authority(String roleCode, String roleDescription) {
		super();
		this.roleCode = roleCode;
		this.roleDescription = roleDescription;
	}

}
