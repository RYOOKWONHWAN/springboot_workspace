package com.example.shop.security.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.example.shop.members.dao.MembersDAO;
import com.example.shop.security.jwt.JwtAuthenticationFilter;
import com.example.shop.security.jwt.JwtAuthorizationFilter;

@Configuration
@EnableWebSecurity //SpringSecurityFilterChain 에 등록  로그인 및 세션분리 관련 기능 제공
public class SecurityConfig {
	
	
	@Autowired
	private MembersDAO userRepository;
	
	
	@Bean
	public BCryptPasswordEncoder encodePassword() {
		return new BCryptPasswordEncoder();
	}
	

	@Bean
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
		//csrf() : 사이트간 위조 요청으로 정상적인 사용자가 의도치않은 위조 요청을 보내는 것을 의미한다.
		//.disable 로 못하게 막음
		http.csrf().disable();
		
		//api 를 사용하므로 기본 제공하는 formlogin 페이지를 끈다
		http.formLogin().disable();
		//httpBasic 방식 대신 JWT 를 사용하기 때문에 httpBasic() 끈다.
		http.httpBasic().disable();
		
		//session 끊음
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		
		http.apply(new MyCustomerFilter());
		return http.build();
		
	}
	
	
	public class MyCustomerFilter extends AbstractHttpConfigurer<MyCustomerFilter, HttpSecurity>{
		@Override
		public void configure(HttpSecurity http) throws Exception {
			AuthenticationManager authenticationManager = http.getSharedObject(AuthenticationManager.class);
			
			//addFilter(): FilterComparator 에 등록되어있는 Filter들을 활성화할때 사용
			//addFilterBefore(), addFilterAfter() : CustomFilter를 등록할 때 사용
			http.addFilter(new JwtAuthenticationFilter(authenticationManager)).addFilter(new JwtAuthorizationFilter(authenticationManager , userRepository) );
			
		}
	}
	
	
	
	
	
	
	
	
	
}
