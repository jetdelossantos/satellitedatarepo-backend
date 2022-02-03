package com.satellitedata.service.impl;

import java.util.Date;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.satellitedata.domain.UserPrincipal;
import com.satellitedata.model.User;
import com.satellitedata.repository.UserRepository;
import com.satellitedata.service.UserService;
	
@Service
@Transactional
@Qualifier("userDetailsService")
public class UserServiceImpl implements UserService, UserDetailsService {
	
	private Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);
			
	@Autowired UserRepository userRepository;
	
	@Override
	public List<User> findAll() {
		List<User> u = userRepository.findAll();
		return u;
	}
	
	@Override
	public Optional<User> getById(int id) {
		Optional<User> u = userRepository.findById(id);
		return u;
	}

	@Override
	public Optional<User> getByEmail(String email) {
		Optional<User> u = userRepository.findByEmail(email);
		return u;
	}
	
	@Override
	public Optional<User> getByUsername(String username) {
		Optional<User> u = userRepository.findByUsername(username);
		return u;
	}

	@Override
	public boolean save(User user) {
		userRepository.save(user);
		return true;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.findUserByUsername(username);
		if (user == null) {
			LOGGER.error("User not found by username: " + username);
			throw new UsernameNotFoundException("User not found by username: " + username);
		} else {
			user.setLastlogindate(new Date());
			userRepository.save(user);
			UserPrincipal userPrincipal = new UserPrincipal(user);
			LOGGER.info("Returning found user by username: "+ username);
			return userPrincipal;
		}	
	}

}
