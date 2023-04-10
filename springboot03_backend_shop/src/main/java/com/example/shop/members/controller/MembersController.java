package com.example.shop.members.controller;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.shop.board.dto.BoardDTO;
import com.example.shop.common.file.fileUpload;
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
	@PutMapping("/member/update")
	public void memberUpdate(MembersDTO dto, HttpServletRequest request) throws IllegalStateException, IOException {
		System.out.println("이름" +dto.getMemberName());
		System.out.println("번호" +dto.getMemberPhone());
		System.out.println("메일" +dto.getMemberEmail());
		dto.setMemberPass(encodePassword.encode(dto.getMemberPass()));
		System.out.println("비밀 "+dto.getMemberPass());
		membersService.updateMemberProcess(dto);
		
	}
	@GetMapping("/member/editinfo/{memberEmail}")
	public MembersDTO getMember(@PathVariable("memberEmail") String memberEmail) {
		
		return membersService.updateMemberProcess(memberEmail);
	}
	@PostMapping("/member/update")
	public void updataMember(@RequestBody MembersDTO dto) {
		dto.setMemberPass(encodePassword.encode(dto.getMemberPass()));
		System.out.println("비밀 "+dto.getMemberPass());
		membersService.updateMemberProcess(dto);
	}

}
