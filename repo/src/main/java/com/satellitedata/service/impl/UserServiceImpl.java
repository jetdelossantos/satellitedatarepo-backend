package com.satellitedata.service.impl;

import java.util.Arrays;
import java.util.Date;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import javax.mail.MessagingException;

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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.apache.commons.*;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.junit.platform.commons.util.StringUtils;

import com.satellitedata.domain.UserPrincipal;
import com.satellitedata.enumeration.Role;
import com.satellitedata.exception.domain.EmailExistException;
import com.satellitedata.exception.domain.EmailNotFoundException;
import com.satellitedata.exception.domain.NotAnImageFileException;
import com.satellitedata.exception.domain.PasswordIncorrectException;
import com.satellitedata.exception.domain.UserNotFoundException;
import com.satellitedata.exception.domain.UsernameExistException;
import com.satellitedata.model.User;
import com.satellitedata.repository.UserRepository;
import com.satellitedata.service.EmailService;
import com.satellitedata.service.LoginAttemptService;
import com.satellitedata.service.UserService;

import static com.satellitedata.constant.UserImplConstant.*;
import static com.satellitedata.enumeration.Role.*;
import static com.satellitedata.constant.FileConstant.*;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import static org.springframework.http.MediaType.*;
	
@Service
@Transactional
@Qualifier("userDetailsService")
public class UserServiceImpl implements UserService, UserDetailsService {
	//Declarations
	private static final String EMPTY = null;
	private Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);
	@Autowired BCryptPasswordEncoder passwordEncoder;	
	@Autowired UserRepository userRepository;
	@Autowired LoginAttemptService loginAttemptService;
	@Autowired EmailService emailService;
	
	//LOGIN USER
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.findUserByUsername(username);
		if (user == null) {
			LOGGER.error("User not found by username: " + username);
			throw new UsernameNotFoundException("User not found by username: " + username);
		} else {
			validateLoginAttempt(user);
			user.setLastlogindate(new Date());
			userRepository.save(user);
			UserPrincipal userPrincipal = new UserPrincipal(user);
			LOGGER.info("Returning found user by username: "+ username);
			return userPrincipal;
		}	
	}
	
	//Multiple Login Catcher
    private void validateLoginAttempt(User user) {
		if(user.isIsnotlocked()) {
			if(loginAttemptService.hasExceededMaxAttempts(user.getUsername())) {
				user.setIsnotlocked(false);
			}
		}else {
			loginAttemptService.evictUserFromLoginAttemptCache(user.getUsername());
		}
	}

    //USER REGISTRATION
	@Override
    public User register(String firstName, 
    					 String lastName,
    					 String username, 
    					 String email, 
    					 int country) throws UserNotFoundException, UsernameExistException, EmailExistException, MessagingException {
    	
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
        user.setCreated(new Date());
        user.setIsnotlocked(true);
        user.setRole(ROLE_SUPER_ADMIN.name());
        user.setProfileImageUrl(getTemporaryProfileImageUrl(username));
        user.setAuthorities(ROLE_SUPER_ADMIN.getAuthorities());
        userRepository.save(user);
        LOGGER.info("New user password: " + password);
        emailService.sendNewPasswordEmail(firstName, password, email);
        return user;
    }
	
	//DELETE USER
    @Override
    public void deleteUser(String username) throws IOException {
        User user = userRepository.findUserByUsername(username);
        Path userFolder = Paths.get(USER_FOLDER + user.getUsername()).toAbsolutePath().normalize();
        FileUtils.deleteDirectory(new File(userFolder.toString()));
        userRepository.deleteById(user.getId());
    }
    

	private User validateNewUsernameAndEmail(String currentUsername, 
											 String newUsername, 
											 String newEmail) throws UserNotFoundException, UsernameExistException, EmailExistException {
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
	
	private User validateCurrentUsernameAndPassword(String currentUsername, String oldPassword) throws UserNotFoundException, PasswordIncorrectException {
		if(StringUtils.isNotBlank(currentUsername) && StringUtils.isNotBlank(oldPassword) ) {
			User currentUser = findUserByUsername(currentUsername);
			if(currentUser == null) {
				throw new UserNotFoundException(NO_USER_FOUND_BY_USERNAME + currentUsername);
			} else {
				String userpassword = currentUser.getPassword();
				Boolean matchedpassword = passwordEncoder.matches(oldPassword, userpassword);
				if (matchedpassword == true) {
					return currentUser;
				} else {
					throw new PasswordIncorrectException(PASSWORD_INCORRECT);
				}
			}
		} else {
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

	@Override
	public User addNewUser(String firstname, 
						   String lastname, 
						   String username, 
						   String email, 
						   int country, 
						   String role,
						   boolean isNotLocked, 
						   boolean isActive, 
						   MultipartFile profileImage) throws UserNotFoundException, UsernameExistException, EmailExistException, IOException, NotAnImageFileException{
        validateNewUsernameAndEmail (EMPTY, username, email);
		User user = new User();
        String password = generatePassword();
        user.setUserid(generateUserId());
        user.setFirstname(firstname);
        user.setLastname(lastname);
        user.setCreated(new Date());
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(encodePassword(password));
        user.setIsactive(isActive);
        user.setCountry(country);
        user.setIsnotlocked(isNotLocked);
        user.setRole(getRoleEnumName(role).name());
        user.setAuthorities(getRoleEnumName(role).getAuthorities());
        user.setProfileImageUrl(getTemporaryProfileImageUrl(username));
        userRepository.save(user);
        saveProfileImage(user, profileImage);
        LOGGER.info("New user password: " + password);
        return user;
	}

	private Role getRoleEnumName(String role) {
		return Role.valueOf(role.toUpperCase());
	}

    @Override
    public User updateUser(String currentUsername, 
    					   String newFirstName, 
    					   String newLastName, 
    					   String newUsername, 
    					   String newEmail, 
    					   int newCountry, 
    					   String role, 
    					   boolean isNonLocked, 
    					   boolean isActive, 
    					   MultipartFile profileImage) throws UserNotFoundException, UsernameExistException, EmailExistException, IOException, NotAnImageFileException {
    	
        User currentUser = validateNewUsernameAndEmail(currentUsername, newUsername, newEmail);
        currentUser.setFirstname(newFirstName);
        currentUser.setLastname(newLastName);
        currentUser.setUsername(newUsername);
        currentUser.setEmail(newEmail);
        currentUser.setIsactive(isActive);
        currentUser.setCountry(newCountry);
        currentUser.setIsnotlocked(isNonLocked);
        currentUser.setRole(getRoleEnumName(role).name());
        currentUser.setAuthorities(getRoleEnumName(role).getAuthorities());
        userRepository.save(currentUser);
        saveProfileImage(currentUser, profileImage);
        return currentUser;
    }
    
    private void saveProfileImage(User user, MultipartFile profileImage) throws IOException, NotAnImageFileException {
        if (profileImage != null) {
            if(!Arrays.asList(IMAGE_JPEG_VALUE, IMAGE_PNG_VALUE, IMAGE_GIF_VALUE).contains(profileImage.getContentType())) {
                throw new NotAnImageFileException(profileImage.getOriginalFilename() + NOT_AN_IMAGE_FILE);
            }
            Path userFolder = Paths.get(USER_FOLDER + user.getUsername()).toAbsolutePath().normalize();
            if(!Files.exists(userFolder)) {
                Files.createDirectories(userFolder);
                LOGGER.info(DIRECTORY_CREATED + userFolder);
            }
            Files.deleteIfExists(Paths.get(userFolder + user.getUsername() + DOT + JPG_EXTENSION));
            Files.copy(profileImage.getInputStream(), userFolder.resolve(user.getUsername() + DOT + JPG_EXTENSION), REPLACE_EXISTING);
            user.setProfileImageUrl(setProfileImageUrl(user.getUsername()));
            userRepository.save(user);
            LOGGER.info(FILE_SAVED_IN_FILE_SYSTEM + profileImage.getOriginalFilename());
        }
    }

    private String setProfileImageUrl(String username) {
        return ServletUriComponentsBuilder.fromCurrentContextPath().path(USER_IMAGE_PATH + username + FORWARD_SLASH
        + username + DOT + JPG_EXTENSION).toUriString();
    }

	@Override
    public void resetPassword(String email) throws MessagingException, EmailNotFoundException {
        User user = userRepository.findUserByEmail(email);
        if (user == null) {
            throw new EmailNotFoundException(NO_USER_FOUND_BY_EMAIL + email);
        }
        String password = generatePassword();
        user.setPassword(encodePassword(password));
        userRepository.save(user);
        LOGGER.info("New user password: " + password);
        emailService.sendNewPasswordEmail(user.getFirstname(), password, user.getEmail());
    }

    @Override
    public User updateProfileImage(String username, MultipartFile profileImage) throws UserNotFoundException, UsernameExistException, EmailExistException, IOException, NotAnImageFileException {
        User user = validateNewUsernameAndEmail(username, null, null);
        saveProfileImage(user, profileImage);
        return user;
    }

    private String getTemporaryProfileImageUrl(String username) {
        return ServletUriComponentsBuilder.fromCurrentContextPath().path(DEFAULT_USER_IMAGE_PATH + username).toUriString();
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

	@Override
	public User changePassword(String currentUsername, String oldPassword, String newPassword) throws UserNotFoundException, UsernameExistException, IOException, PasswordIncorrectException {
		User currentUser = validateCurrentUsernameAndPassword(currentUsername, oldPassword);
		currentUser.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(currentUser);
        return currentUser;	
	}	
}
