package com.satellitedata.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.satellitedata.exception.ExceptionHandling;
import com.satellitedata.exception.domain.EmailExistException;
import com.satellitedata.exception.domain.UserNotFoundException;
import com.satellitedata.exception.domain.UsernameExistException;
import com.satellitedata.model.User;
import com.satellitedata.service.UserService;

@RestController
@RequestMapping("/user")
public class UserController extends ExceptionHandling{
	
	@Autowired UserService userService;
	
	@GetMapping("/allusers")
	public List<User> findAll() {
		return userService.findAll();
	}
	
	@GetMapping("/id")
	public Optional<User> getById(@RequestParam(value= "id") int id) {
		return userService.getById(id);
	}
	
	@GetMapping("/email")
	public Optional<User> getByEmail(@RequestParam(value= "email") String email) {
		return userService.getByEmail(email);
	}
	@GetMapping("/username")
	public Optional<User> getById(@RequestParam(value= "username") String username) {
		return userService.getByUsername(username);
	}
	
	@PostMapping("/save")
	public boolean saveUser(@RequestBody(required=true) User user) {
		return userService.save(user);
	}
	
	@GetMapping("/home")
	public String showUser() throws EmailExistException {
		//return "application works";
		throw new EmailExistException("Pooop email");
	}
	
	@PostMapping("/register")
	public ResponseEntity<User> register(@RequestBody User user) throws EmailExistException, UserNotFoundException, UsernameExistException {
		User newUser = userService.register(user.getFirstname(), user.getLastname(), user.getUsername(), user.getEmail(), 0);
		return new ResponseEntity<>(newUser, HttpStatus.OK);
	}
}
