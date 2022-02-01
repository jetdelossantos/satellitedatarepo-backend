package com.satellitedata.service;

import java.util.List;
import java.util.Optional;

import com.satellitedata.model.User;

public interface UserService {
	
	Optional<User> getById(int id);
	Optional<User> getByEmail(String email);
	boolean save(User user);
	Optional<User> getByUsername(String username);
	List<User> findAll();
}
