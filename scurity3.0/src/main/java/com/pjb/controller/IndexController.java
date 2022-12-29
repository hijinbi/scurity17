package com.pjb.controller;

import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.pjb.config.auth.PrincipalDetails;
import com.pjb.model.User;
import com.pjb.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class IndexController {

	private final PasswordEncoder passwordEncoder; 
	private final UserRepository userRepository;
	
	@GetMapping({ "", "/" })
	public String index() {
		return "login";
	}

	@GetMapping("/user")
	public @ResponseBody String user(@AuthenticationPrincipal PrincipalDetails auth) {

		return "유저 페이지입니다.";
	}

	@GetMapping("/admin")
	public @ResponseBody String admin(Authentication auth) {
		return "어드민 페이지입니다.";
	}
	
	@Secured("ROLE_MANAGER")
	@GetMapping("/manager")
	public @ResponseBody String manager(@AuthenticationPrincipal PrincipalDetails auth) {
		return "매니저 페이지입니다.";
	}

	@GetMapping("/login")
	public String login() {
		return "login";
	}
	
	@GetMapping("/joinForm")
	public String joinForm() {
		return "joinForm";
	}


	@PostMapping("/joinProc")
	public String joinProc(User user) {
		System.out.println("회원가입 진행 : " + user);
		String rawPassword = user.getPassword();
		String encPassword = passwordEncoder.encode(rawPassword);
		user.setPassword(encPassword);
		user.setRole("ROLE_ADMIN");
		userRepository.save(user);
		return "redirect:/";
	}
	
	@PostMapping("/loginProcess")
	public String loginProcess() {
		return "loginProcess";
	}
	
	@GetMapping("/test")
	public @ResponseBody String test(@AuthenticationPrincipal PrincipalDetails auth) {

		return "유저 페이지입니다.";
	}
}
