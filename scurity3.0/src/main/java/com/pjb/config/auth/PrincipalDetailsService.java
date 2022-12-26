package com.pjb.config.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.pjb.model.User;
import com.pjb.repository.UserRepository;

@Service
public class PrincipalDetailsService implements UserDetailsService{
	
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder; //추가
	
	//테스트 유저 1건 생성 
	public PrincipalDetailsService(
	        @Autowired UserRepository userRepository,
	        @Autowired PasswordEncoder passwordEncoder //추가
	) {
	    this.userRepository = userRepository;
	    
	    this.passwordEncoder = passwordEncoder;
	    final User testUserEntity = new User();
	    testUserEntity.setUsername("user");
	    testUserEntity.setPassword(passwordEncoder.encode("1234")); //
	    testUserEntity.setRole("ROLE_USER");
	    this.userRepository.save(testUserEntity);
	}
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.findByUsername(username);
		if(user == null) {
			return null;
		}else {
			return new PrincipalDetails(user);
		}
		
	}

}
