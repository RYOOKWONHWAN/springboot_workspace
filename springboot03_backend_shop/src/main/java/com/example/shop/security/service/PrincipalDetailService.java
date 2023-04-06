package com.example.shop.security.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.shop.members.dao.MembersDAO;
import com.example.shop.members.dto.MembersDTO;
@Service
public class PrincipalDetailService implements UserDetailsService{
	@Autowired
	private MembersDAO membersDAO;

	public PrincipalDetailService() {
	
	  
	}
	@Override
	public UserDetails loadUserByUsername(String memberEmail) throws UsernameNotFoundException {
		MembersDTO userEntity= membersDAO.selectByEmail(memberEmail);
		System.out.println(userEntity.getMemberEmail());
		System.out.println(memberEmail);
		
		if (userEntity==null) {
			throw new UsernameNotFoundException(memberEmail);
		}
		
		  
		return new PrincipalDetails(userEntity);
	}  
	
}
