package com.satellitedata.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.satellitedata.model.User;
import com.satellitedata.repository.UserRepository;
import com.satellitedata.service.UserService;
	
@Service
public class UserServiceImpl implements UserService {

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

}
