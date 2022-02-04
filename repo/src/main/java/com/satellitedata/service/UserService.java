package com.satellitedata.service;

import java.util.List;
import java.util.Optional;

import com.satellitedata.exception.domain.EmailExistException;
import com.satellitedata.exception.domain.UserNotFoundException;
import com.satellitedata.exception.domain.UsernameExistException;
import com.satellitedata.model.User;

public interface UserService {
	
	User register(String firstname, String lastname, String username, String email, int country) throws UserNotFoundException, UsernameExistException, EmailExistException;
	List<User> getUsers();
	User findUserByUsername(String username);
	User findUserByEmail(String email);
	
	Optional<User> getById(int id);
	Optional<User> getByEmail(String email);
	boolean save(User user);
	Optional<User> getByUsername(String username);
	
	List<User> findAll();
}
