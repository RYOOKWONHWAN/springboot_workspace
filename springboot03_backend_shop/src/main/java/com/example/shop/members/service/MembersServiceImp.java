package com.example.shop.members.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.shop.common.exception.WrongEmailPasswordException;
import com.example.shop.members.dao.MembersDAO;
import com.example.shop.members.dto.AuthInfo;
import com.example.shop.members.dto.ChangePwdCommand;
import com.example.shop.members.dto.MembersDTO;

@Service
public class MembersServiceImp implements MembersService{
	
	@Autowired
	private MembersDAO membersDao;
	
	public MembersServiceImp() {
	
	}
	


	@Override
	public AuthInfo addMemberProcss(MembersDTO dto) {
		membersDao.insertMember(dto);
		return new AuthInfo(dto.getMemberEmail(), dto.getMemberName(), dto.getMemberPass());
	}

	@Override
	public AuthInfo loginProcess(MembersDTO dto) {
		
		 MembersDTO member = membersDao.selectByEmail(dto.getMemberEmail());
		 if(member == null) {
			 //System.out.println("회원이 아닙니다.");
			 throw new WrongEmailPasswordException();
		 }
		 
		 //앞에 ! not있음 못보고 지나칠 위험있음
		 if(!member.matchPassword(dto.getMemberPass())) {
			 //System.out.println("비밀번호가 다릅니다.");
			 throw new WrongEmailPasswordException();
		 }
			 
		 
		return new AuthInfo(member.getMemberEmail(), member.getMemberName(),member.getMemberPass());
	}

	@Override
	public MembersDTO updateMemberProcess(String memberEmail) {
		
		return membersDao.selectByEmail(memberEmail);
	}

	@Override
	public AuthInfo updateMemberProcess(MembersDTO dto) {
		membersDao.updateMember(dto);
		
		//구지 또 접근안하고 처리해도됨
		MembersDTO member = membersDao.selectByEmail(dto.getMemberEmail());
		
		return new AuthInfo(member.getMemberEmail(), member.getMemberName(),member.getMemberPass());
	}

	@Override
	public void updatePassProcess(String memberEmail, ChangePwdCommand changePwd) {
		
		MembersDTO member = membersDao.selectByEmail(memberEmail);
		if(member == null)
			throw new WrongEmailPasswordException();
		
		member.changePassword(changePwd.getCurrentPassword(), changePwd.getNewPassword());
		
		membersDao.updateByPass(member);
	}
}






















