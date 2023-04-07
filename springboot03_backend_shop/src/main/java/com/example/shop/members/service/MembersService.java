package com.example.shop.members.service;

import com.example.shop.members.dto.AuthInfo;
import com.example.shop.members.dto.ChangePwdCommand;
import com.example.shop.members.dto.MembersDTO;
  
public interface MembersService {

	public AuthInfo addMemberProcss(MembersDTO dto);
	public AuthInfo loginProcess(MembersDTO dto);
	
	public MembersDTO updateMemberProcess(String memberEmail);
	public void updateMemberProcess(MembersDTO dto);
	public void updatePassProcess(String memberEmail, ChangePwdCommand changeRwd);
	
	
}
	