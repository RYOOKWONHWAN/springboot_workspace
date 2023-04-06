package com.example.shop.members.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.shop.members.dto.AuthInfo;
import com.example.shop.members.dto.MembersDTO;
import com.example.shop.members.service.MembersService;

import oracle.jdbc.proxy.annotation.Post;

//@CrossOrigin(origins = {"http://localhost:3000"})
@CrossOrigin("*")
@RestController
public class MembersController {
	
	
	@Autowired  
	private MembersService membersService;
	
	@Autowired
	private BCryptPasswordEncoder encodePassword;
	  
	
	public MembersController() {
		
	}

	
	//디비에 비밀번호를 그대로 넣지 않는다. 암호화후 디비에 저장
	// 회원가입 폼
	@PostMapping("/member/signup")
	public String addMember(@RequestBody MembersDTO membersDTO) {
		
		membersDTO.setMemberPass(encodePassword.encode(membersDTO.getMemberPass()));
		
		AuthInfo authInfo = membersService.addMemberProcss(membersDTO);
		

		return null;
	}
	//로그인
	@PostMapping("/member/login")
	public String loginMember(MembersDTO membersDTO) {
		return null;
	}

}