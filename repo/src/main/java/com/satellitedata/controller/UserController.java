package com.satellitedata.controller;

import org.springframework.http.HttpHeaders;
import java.util.List;
import java.util.Optional;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.satellitedata.domain.UserPrincipal;
import com.satellitedata.exception.ExceptionHandling;
import com.satellitedata.exception.domain.EmailExistException;
import com.satellitedata.exception.domain.UserNotFoundException;
import com.satellitedata.exception.domain.UsernameExistException;
import com.satellitedata.model.User;
import com.satellitedata.service.UserService;
import com.satellitedata.utility.JWTTokenProviderUtility;

import static org.springframework.http.HttpStatus.*;
import static com.satellitedata.constant.SecurityConstant.*;

@RestController
@RequestMapping("/user")
public class UserController extends ExceptionHandling{
	
	@Autowired UserService userService;
	
	@Autowired AuthenticationManager authenticationManager;
	
	@Autowired JWTTokenProviderUtility jwtTokenProvider;
	
	@GetMapping("/home")
	public String showUser() throws EmailExistException {
		//return "application works";
		throw new EmailExistException("Pooop email");
	}
	
	@PostMapping("/register")
	public ResponseEntity<User> register(@RequestBody User user) throws EmailExistException, UserNotFoundException, UsernameExistException, MessagingException {
		User newUser = userService.register(user.getFirstname(), user.getLastname(), user.getUsername(), user.getEmail(), user.getCountry());
		return new ResponseEntity<>(newUser, HttpStatus.OK);
	}
	
    @PostMapping("/login")
    public ResponseEntity<User> login(@RequestBody User user) {
        authenticate(user.getUsername(), user.getPassword());
        User loginUser = userService.findUserByUsername(user.getUsername());
        UserPrincipal userPrincipal = new UserPrincipal(loginUser);
        HttpHeaders jwtHeader = getJwtHeader(userPrincipal);
        return new ResponseEntity<>(loginUser, jwtHeader, OK);
    }
	
    private HttpHeaders getJwtHeader(UserPrincipal user) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(JWT_TOKEN_HEADER, jwtTokenProvider.generateJwtToken(user));
        return headers;
    }
    
    private void authenticate(String username, String password) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
    }
}
