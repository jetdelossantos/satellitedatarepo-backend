package com.satellitedata.service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import javax.mail.MessagingException;

import org.springframework.web.multipart.MultipartFile;

import com.satellitedata.exception.domain.*;
import com.satellitedata.model.User;

public interface UserService {
	
	User register(String firstname, 
				  String lastname, 
				  String username, 
				  String email, 
				  int country) throws UserNotFoundException, UsernameExistException, EmailExistException, MessagingException;
	
	List<User> getUsers();
	
	User findUserByUsername(String username);
	
	User findUserByEmail(String email);
	
	User addNewUser(String firstname, 
					String lastname,
					String username, 
					String email, 
					int country, 
					String role, 
					boolean isNotLocked, 
					boolean isActive, 
					MultipartFile profileImage) throws UserNotFoundException, UsernameExistException, EmailExistException, IOException, NotAnImageFileException;
	
	User updateUser(String currentUsername, 
					String newFirstName, 
					String newLastName, 
					String newUsername, 
					String newEmail, 
					int newCountry, 
					String role, 
					boolean isNonLocked, 
					boolean isActive,
					MultipartFile profileImage) throws UserNotFoundException, UsernameExistException, EmailExistException, IOException, NotAnImageFileException;	
	
	void resetPassword(String email) throws MessagingException, EmailNotFoundException;
	
	User updateProfileImage(String username, MultipartFile profileImage) throws UserNotFoundException, UsernameExistException, EmailExistException, IOException, NotAnImageFileException;

	void deleteUser(String username) throws IOException;
}

