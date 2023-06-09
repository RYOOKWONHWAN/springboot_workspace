package com.example.shop.security.service;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.shop.members.dto.MembersDTO;

public class PrincipalDetails  implements UserDetails{
	

	private MembersDTO membersDTO;
	
	public PrincipalDetails() {
		// TODO Auto-generated constructor stub
	}

	public PrincipalDetails(MembersDTO membersDTO) {
	
		this.membersDTO = membersDTO;
	}  
	//권한 목록 리턴
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		Collection<GrantedAuthority> collect= new ArrayList<GrantedAuthority>();
		collect.add(new GrantedAuthority() {
			
			@Override
			public String getAuthority() {
				// TODO Auto-generated method stub
				return membersDTO.getAuthrole();
			}
		});
		return collect;
	}

	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return membersDTO.getMemberPass();
	}

	@Override  
	public String getUsername() {
		// TODO Auto-generated method stub
		return membersDTO.getMemberEmail();
		
	}
	// 만료 여부  true => 만료안됨
	@Override 
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}
	//계정 잠김 여부 true=> 안됨
	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return true;
	}
	//비밀번호잠김여부 
	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}
	//게정활성화
	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return true;
	}

	public MembersDTO getMembersDTO() {
		return membersDTO;
	}

	public void setMembersDTO(MembersDTO membersDTO) {
		this.membersDTO = membersDTO;
	}
	
}
