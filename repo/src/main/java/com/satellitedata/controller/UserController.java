package com.satellitedata.controller;

import org.springframework.http.HttpHeaders;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.satellitedata.domain.HttpResponse;
import com.satellitedata.domain.UserPrincipal;
import com.satellitedata.exception.ExceptionHandling;
import com.satellitedata.exception.domain.EmailExistException;
import com.satellitedata.exception.domain.EmailNotFoundException;
import com.satellitedata.exception.domain.NotAnImageFileException;
import com.satellitedata.exception.domain.PasswordIncorrectException;
import com.satellitedata.exception.domain.UserNotFoundException;
import com.satellitedata.exception.domain.UsernameExistException;
import com.satellitedata.model.User;
import com.satellitedata.repository.UserRepository;
import com.satellitedata.service.UserService;
import com.satellitedata.utility.JWTTokenProviderUtility;

import static org.springframework.http.HttpStatus.*;
import static com.satellitedata.constant.SecurityConstant.*;
import static com.satellitedata.constant.FileConstant.*;
import static org.springframework.http.MediaType.IMAGE_JPEG_VALUE;
import static com.satellitedata.constant.SecurityConstant.JWT_TOKEN_HEADER;

@RestController
@RequestMapping("/user")
public class UserController extends ExceptionHandling{
	public static final String EMAIL_SENT = "An email with a new password was sent to: ";
    public static final String USER_DELETED_SUCCESSFULLY = "User deleted successfully";
    
	@Autowired UserService userService;
	
	@Autowired UserRepository userRepo;
	
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
    
    @PostMapping("/add")
    @PreAuthorize("hasAnyAuthority('user:create')")
    public ResponseEntity<User> addNewUser(@RequestParam("firstname") String firstName,
                                           @RequestParam("lastname") String lastName,
                                           @RequestParam("username") String username,
                                           @RequestParam("email") String email,
                                           @RequestParam("country") int country,
                                           @RequestParam("role") String role,
                                           @RequestParam("isactive") String isActive,
                                           @RequestParam("isnotlocked") String isNotLocked,
                                           @RequestParam(value = "profileImage", required = false) MultipartFile profileImage) throws UserNotFoundException, UsernameExistException, EmailExistException, IOException, NotAnImageFileException {
        User newUser = userService.addNewUser(firstName, 
        									lastName, 
        									username,
        									email,
        									country, 
        									role, 
        									Boolean.parseBoolean(isNotLocked), 
        									Boolean.parseBoolean(isActive), 
        									profileImage);
        return new ResponseEntity<>(newUser, OK);
    }
	
    @PostMapping("/update")
    @PreAuthorize("hasAnyAuthority('user:update')")
    public ResponseEntity<User> update(@RequestParam("currentUsername") String currentUsername,
                                       @RequestParam("firstname") String firstName,
                                       @RequestParam("lastname") String lastName,
                                       @RequestParam("username") String username,
                                       @RequestParam("email") String email,
                                       @RequestParam("country") int country,
                                       @RequestParam("role") String role,
                                       @RequestParam("isactive") String isActive,
                                       @RequestParam("isnotlocked") String isNonLocked,
                                       @RequestParam(value = "profileImage", required = false) MultipartFile profileImage) throws UserNotFoundException, UsernameExistException, EmailExistException, IOException, NotAnImageFileException {
        User updatedUser = userService.updateUser(currentUsername, 
        										  firstName, 
       										  lastName, 
        										  username,
        										  email, 
        										  country, 
        										  role, 
        										  Boolean.parseBoolean(isNonLocked), 
        										  Boolean.parseBoolean(isActive), 
        										  profileImage);
        return new ResponseEntity<>(updatedUser, OK);
    }
    
    @PostMapping("/changepassword")
    public ResponseEntity<User> changePassword (@RequestParam("currentUsername") String currentUsername,
									            @RequestParam("oldpassword") String oldPassword,
									            @RequestParam("newpassword") String newPassword) throws UserNotFoundException, UsernameExistException, IOException, PasswordIncorrectException {
    	User updatedUser = userService.changePassword(currentUsername, oldPassword, newPassword);
    	return new ResponseEntity<>(updatedUser, OK);
    	
    }
    
    @GetMapping("/find/{username}")
    public ResponseEntity<User> getUser(@PathVariable("username") String username) {
        User user = userService.findUserByUsername(username);
        return new ResponseEntity<>(user, OK);
    }

    @GetMapping("/list")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userRepo.findAll();
        return new ResponseEntity<>(users, OK);
    }
    
    @GetMapping("/resetpassword/{email}")
    public ResponseEntity<HttpResponse> resetPassword(@PathVariable("email") String email) throws MessagingException, EmailNotFoundException {
        userService.resetPassword(email);
        return response(OK, EMAIL_SENT + email);
    }

    @DeleteMapping("/delete/{username}")
    @PreAuthorize("hasAnyAuthority('user:delete')")
    public ResponseEntity<HttpResponse> deleteUser(@PathVariable("username") String username) throws IOException {
        userService.deleteUser(username);
        return response(OK, USER_DELETED_SUCCESSFULLY);
    }

    @PostMapping("/updateProfileImage")
    public ResponseEntity<User> updateProfileImage(@RequestParam("username") String username, 
    											   @RequestParam(value = "profileImage") MultipartFile profileImage) throws UserNotFoundException, UsernameExistException, EmailExistException, IOException, NotAnImageFileException {
        User user = userService.updateProfileImage(username, profileImage);
        return new ResponseEntity<>(user, OK);
    }

    @GetMapping(path = "/image/{username}/{fileName}", produces = IMAGE_JPEG_VALUE)
    public byte[] getProfileImage(@PathVariable("username") String username, 
    							  @PathVariable("fileName") String fileName) throws IOException {
        return Files.readAllBytes(Paths.get(USER_FOLDER + username + FORWARD_SLASH + fileName));
    }

    @GetMapping(path = "/image/profile/{username}", produces = IMAGE_JPEG_VALUE)
    public byte[] getTempProfileImage(@PathVariable("username") String username) throws IOException {
        URL url = new URL(TEMP_PROFILE_IMAGE_BASE_URL + username);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try (InputStream inputStream = url.openStream()) {
            int bytesRead;
            byte[] chunk = new byte[1024];
            while((bytesRead = inputStream.read(chunk)) > 0) {
                byteArrayOutputStream.write(chunk, 0, bytesRead);
            }
        }
        return byteArrayOutputStream.toByteArray();
    }

    private ResponseEntity<HttpResponse> response(HttpStatus httpStatus, String message) {
    	HttpResponse body = new HttpResponse(httpStatus.value(), 
				 							 httpStatus,
				 							 httpStatus.getReasonPhrase().toUpperCase(),
				 							 message.toUpperCase());
        return new ResponseEntity<>(body, httpStatus);
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
