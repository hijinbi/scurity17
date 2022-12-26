package com.pjb.config;

import java.io.IOException;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import com.pjb.config.oauth.PrincipalOauth2UserService;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;


@Configuration // IoC 빈(bean)을 등록
@EnableWebSecurity // 필터 체인 관리 시작 어노테이션
@EnableMethodSecurity(prePostEnabled = true, securedEnabled = true) // 특정 주소 접근시 권한 및 인증을 위한 어노테이션 활성화
@RequiredArgsConstructor
public class SecurityConfig {
	private final PrincipalOauth2UserService principalOauth2UserService;
	
	@Bean
    PasswordEncoder passwordEncoder(){
		return new BCryptPasswordEncoder();
    }

    /**
     * Spring Security 5.7.x 부터 WebSecurityConfigurerAdapter 는 Deprecated.
     * -> SecurityFilterChain, WebSecurityCustomizer 를 상황에 따라 빈으로 등록해 사용한다.
     */
	@Bean
    WebSecurityCustomizer webSecurityCustomizer() {
      //  return (web) -> web.ignoring()
      //          .antMatchers(HttpMethod.POST, "/sign-up");
		return null;
    }

	
	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
			.authorizeHttpRequests(authConfig -> {
				authConfig.requestMatchers("/user/**").authenticated();
				authConfig.requestMatchers("/manager/**").hasAuthority("ROLE_MANAGER");
				authConfig.requestMatchers("/admin/**").hasAuthority("ROLE_ADMIN");
				authConfig.anyRequest().permitAll();
				
			})
			.csrf(csrf -> csrf.disable())
			.formLogin()
			   .loginPage("/login")   			// 사용자 정의 로그인 페이지
		       .defaultSuccessUrl("/admin")			// 로그인 성공 후 이동 페이지
		       .failureUrl("/login.html?error=true")  // 로그인 실패 후 이동 페이지
		       .loginProcessingUrl("/loginProcess")	
		       // 로그인 Form Action Url
		       .usernameParameter("username")			// 아이디 파라미터명 설정
		       .passwordParameter("password")			// 패스워드 파라미터명 설정
		       /*
		       .successHandler(
		                new AuthenticationSuccessHandler() {
		                    @Override
		                    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
		                        System.out.println("authentication : " + authentication.getName());
		                        response.sendRedirect("/login"); // 인증이 성공한 후에는 root로 이동
		                    }
		                }
		        )
		        */
		        .failureHandler(
		                new AuthenticationFailureHandler() {
		                    @Override
		                    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
		                        System.out.println("exception : " + exception.getMessage());
		                        response.sendRedirect("/login");
		                    }
		                }
		        )
			.and()
				.oauth2Login()
				.loginPage("/login")
				.userInfoEndpoint()
				.userService(principalOauth2UserService)
				;
		return http.build();
	}
}
 


