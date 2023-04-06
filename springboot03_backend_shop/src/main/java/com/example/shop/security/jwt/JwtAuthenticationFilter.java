
package com.example.shop.security.jwt;


import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.shop.members.dto.MembersDTO;
import com.example.shop.members.dto.User;
import com.example.shop.security.service.PrincipalDetails;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
	@Autowired
	private AuthenticationManager authManager;

	public JwtAuthenticationFilter(AuthenticationManager authManager) {

		this.authManager = authManager;
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {

		try {
			ObjectMapper om = new ObjectMapper();
			MembersDTO user = om.readValue(request.getInputStream(), MembersDTO.class);
			System.out.printf("email : %s , pass: %s ", user.getMemberEmail(), user.getMemberPass());
			System.out.printf("name : %s , phone: %s ", user.getMemberName(), user.getMemberPhone());
			// 인증시작점

			UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
					user.getMemberEmail(), user.getMemberPass());

			System.out.println(authenticationToken);
			// 인증 확인 및 인증
			Authentication authentication = authManager.authenticate(authenticationToken);

			PrincipalDetails principaDetails = (PrincipalDetails) authentication.getPrincipal();
			System.out.println(authenticationToken);
			System.out.printf("로그인 완료 %s %s", principaDetails.getUsername(), principaDetails.getPassword());
			return authentication;
		} catch (IOException e) {
			e.printStackTrace();

		}

		return null;
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {
		// TODO Auto-generated method stub
		System.out.println("su");
		PrincipalDetails principalDetails = (PrincipalDetails) authResult.getPrincipal();
		String jwtToken = JWT.create().withSubject("mycors")
				.withExpiresAt(new Date(System.currentTimeMillis() + (60 * 1000 * 60 * 1L)))
				.withClaim("memberName", principalDetails.getMembersDTO().getMemberName())
				.withClaim("authRole", principalDetails.getMembersDTO().getAuthrole())
				.withClaim("memberEmail", principalDetails.getMembersDTO().getMemberEmail())
				.sign(Algorithm.HMAC512("mySecurityCos"));
		System.out.println("jwtToken: "+ jwtToken);
		//response 응답헤더에 jwtToken 추가 
		//Bearer 반드시 한칸 띄고
		response.addHeader("Authorization", "Bearer "+ jwtToken);
		final Map<String, Object> body= new HashMap<String,Object>();
		body.put("memberName",principalDetails.getMembersDTO().getMemberName());
		body.put("memberEmail",principalDetails.getMembersDTO().getMemberEmail());
		body.put("authRole",principalDetails.getMembersDTO().getAuthrole());
		ObjectMapper mapper = new ObjectMapper();
		mapper.writeValue(response.getOutputStream(), body);
		super.successfulAuthentication(request, response, chain, authResult);
	}

	@Override
	protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException failed) throws IOException, ServletException {
		// TODO Auto-generated method stub
		System.out.println("no");
		response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("code", HttpStatus.UNAUTHORIZED.value());
        body.put("error", failed.getMessage());


        new ObjectMapper().writeValue(response.getOutputStream(), body);



		super.unsuccessfulAuthentication(request, response, failed);
	}

}
