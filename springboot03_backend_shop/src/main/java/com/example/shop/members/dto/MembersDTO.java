package com.example.shop.members.dto;

import org.springframework.stereotype.Component;

import com.example.shop.common.exception.WrongEmailPasswordException;


@Component
public class MembersDTO {
	
	private String memberEmail; //이메일
	private String memberPass;	//비밀번호
	private String memberName;	//이름
	private String memberPhone;	//전화번호
	private int memberType;		//회원구분 일반회원 1, 관리자 2
	private boolean rememberEmail; // 자동 로그인 
	private String authrole;
	
	public MembersDTO() {
	
	}

	public String getAuthrole() {
		return authrole;
	}

	public void setAuthrole(String authrole) {
		this.authrole = authrole;
	}

	public String getMemberEmail() {
		return memberEmail;
	}

	public void setMemberEmail(String memberEmail) {
		this.memberEmail = memberEmail;
	}

	public String getMemberPass() {
		return memberPass;
	}

	public void setMemberPass(String memberPass) {
		this.memberPass = memberPass;
	}

	public String getMemberName() {
		return memberName;
	}

	public void setMemberName(String memberName) {
		this.memberName = memberName;
	}

	public String getMemberPhone() {
		return memberPhone;
	}

	public void setMemberPhone(String memberPhone) {
		this.memberPhone = memberPhone;
	}

	public int getMemberType() {
		return memberType;
	}

	public void setMemberType(int memberType) {
		this.memberType = memberType;
	}

	public boolean isRememberEmail() {
		return rememberEmail;
	}

	public void setRememberEmail(boolean rememberEmail) {
		this.rememberEmail = rememberEmail;
	}
	
	
	public boolean matchPassword(String memberPass) {
		return this.memberPass.equals(memberPass);
	}
	
	public void changePassword(String oldPassword, String newPassword) {
		
		if(!this.memberPass.equals(oldPassword))//본인인지 검증
			throw new WrongEmailPasswordException();
		this.memberPass= newPassword;
		
	}
	
}









