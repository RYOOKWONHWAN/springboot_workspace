package com.example.shop.security.jwt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;

import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.example.shop.members.dao.MembersDAO;

public class JwtAuthorizationFilter extends BasicAuthenticationFilter{
	@Autowired
	private MembersDAO userRepository;


	public JwtAuthorizationFilter(AuthenticationManager authManager, MembersDAO userRepository) {
		super(authManager);
		
		this.userRepository= userRepository;
	}
	 
	
	

}
