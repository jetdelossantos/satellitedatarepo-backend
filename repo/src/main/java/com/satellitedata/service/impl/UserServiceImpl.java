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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.apache.commons.*;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.platform.commons.util.StringUtils;

import com.satellitedata.domain.UserPrincipal;
import com.satellitedata.exception.domain.EmailExistException;
import com.satellitedata.exception.domain.UserNotFoundException;
import com.satellitedata.exception.domain.UsernameExistException;
import com.satellitedata.model.User;
import com.satellitedata.repository.UserRepository;
import com.satellitedata.service.UserService;

import static com.satellitedata.constant.UserImplConstant.*;
import static com.satellitedata.enumeration.Role.*;
	
@Service
@Transactional
@Qualifier("userDetailsService")
public class UserServiceImpl implements UserService, UserDetailsService {
	
	private static final String EMPTY = null;
	private Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);
	@Autowired BCryptPasswordEncoder passwordEncoder;	
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

    @Override
    public User register(String firstName, String lastName, String username, String email, int country) 
    		throws UserNotFoundException, UsernameExistException, EmailExistException {
    	
        validateNewUsernameAndEmail(EMPTY, username, email);
        User user = new User();
        user.setUserid(generateUserId());
        String password = generatePassword();
        user.setFirstname(firstName);
        user.setLastname(lastName);
        user.setUsername(username);
        user.setEmail(email);
        user.setCountry(country);
        user.setPassword(encodePassword(password));
        user.setIsactive(true);
        user.setIsnotlocked(true);
        user.setRole(ROLE_USER.name());
        user.setAuthorities(ROLE_USER.getAuthorities());
        userRepository.save(user);
        LOGGER.info("New user password: " + password);
        //emailService.sendNewPasswordEmail(firstName, password, email);
        return user;
    }

    private String encodePassword(String password) {
		return passwordEncoder.encode(password);
	}

	private String generatePassword() {
		return RandomStringUtils.randomAlphanumeric(10);
	}

	private String generateUserId() {
		return RandomStringUtils.randomNumeric(10);
	}

	private User validateNewUsernameAndEmail(String currentUsername, String newUsername, String newEmail) throws UserNotFoundException, UsernameExistException, EmailExistException {
        User userByNewUsername = findUserByUsername(newUsername);
        User userByNewEmail = findUserByEmail(newEmail);
        if(StringUtils.isNotBlank(currentUsername)) {
            User currentUser = findUserByUsername(currentUsername);
            if(currentUser == null) {
                throw new UserNotFoundException(NO_USER_FOUND_BY_USERNAME + currentUsername);
            }
            if(userByNewUsername != null && !currentUser.getId().equals(userByNewUsername.getId())) {
                throw new UsernameExistException(USERNAME_ALREADY_EXISTS);
            }
            if(userByNewEmail != null && !currentUser.getId().equals(userByNewEmail.getId())) {
                throw new EmailExistException(EMAIL_ALREADY_EXISTS);
            }
            return currentUser;
        } else {
            if(userByNewUsername != null) {
                throw new UsernameExistException(USERNAME_ALREADY_EXISTS);
            }
            if(userByNewEmail != null) {
                throw new EmailExistException(EMAIL_ALREADY_EXISTS);
            }
            return null;
        }
    }

	@Override
	public List<User> getUsers() {
		return userRepository.findAll();
	}

	@Override
	public User findUserByUsername(String username) {
		return userRepository.findUserByUsername(username);
	}

	@Override
	public User findUserByEmail(String email) {
		return userRepository.findUserByEmail(email);
	}

}
