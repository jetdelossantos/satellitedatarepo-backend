package com.satellitedata.controller;

import org.springframework.http.HttpHeaders;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.util.StringUtils;
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
import com.satellitedata.exception.domain.FileUploadErrorException;
import com.satellitedata.exception.domain.NotAnImageFileException;
import com.satellitedata.exception.domain.PasswordIncorrectException;
import com.satellitedata.exception.domain.UserNotFoundException;
import com.satellitedata.exception.domain.UsernameExistException;
import com.satellitedata.model.SatelliteFileData;
import com.satellitedata.model.User;
import com.satellitedata.service.UserService;
import com.satellitedata.service.SatelliteFileDataService;
import com.satellitedata.utility.JWTTokenProviderUtility;

import static org.springframework.http.HttpStatus.*;
import static com.satellitedata.constant.SecurityConstant.*;
import static com.satellitedata.constant.FileConstant.*;
import static org.springframework.http.MediaType.IMAGE_JPEG_VALUE;
import static com.satellitedata.constant.SecurityConstant.JWT_TOKEN_HEADER;

import static java.nio.file.Files.copy;
import static java.nio.file.Paths.get;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import static org.springframework.http.HttpHeaders.CONTENT_DISPOSITION;

@RestController
@RequestMapping("/satfile")
public class SatelliteFileDataController {
	
	@Autowired SatelliteFileDataService satfiledataservice;
	//defining a location
	
	//defining a method to upload
	@PostMapping("/uploadsatfile")
	public ResponseEntity<List<String>> uploadFiles(@RequestParam("files") List<MultipartFile> multipartFiles)  throws IOException {
		List<String> filenames = new ArrayList<>();
		for(MultipartFile file: multipartFiles) {
			String filename = StringUtils.cleanPath(file.getOriginalFilename());
			//method to parse data 
			Path fileStorage = get(SATDATA_FOLDER, filename).toAbsolutePath().normalize();
			try {
				copy(file.getInputStream(),fileStorage, REPLACE_EXISTING);
			} catch (IOException e) {
				e.printStackTrace();
			}
			filenames.add(filename);
		}
		return ResponseEntity.ok().body(filenames);
	}
	//define a method to download files
	@GetMapping("downloadsatfile/{filename}")
    public ResponseEntity<Resource> downloadFiles(@PathVariable("filename") String filename) throws IOException {
        Path filePath = get(SATDATA_FOLDER).toAbsolutePath().normalize().resolve(filename);
        if(!Files.exists(filePath)) {
            throw new FileNotFoundException(filename + " was not found on the server");
        }
        Resource resource = new UrlResource(filePath.toUri());
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("File-Name", filename);
        httpHeaders.add(CONTENT_DISPOSITION, "attachment;File-Name=" + resource.getFilename());
        return ResponseEntity.ok().contentType(MediaType.parseMediaType(Files.probeContentType(filePath)))
                .headers(httpHeaders).body(resource);
    }
	
	@PostMapping("/uploadfile")
	public ResponseEntity<SatelliteFileData> uploadFile(@RequestParam("file") MultipartFile multipartFile,
											 @RequestParam("uploader") String uploader)  throws IOException, FileUploadErrorException {
		SatelliteFileData satelliteFileDate = satfiledataservice.uploadFile(multipartFile, uploader);
		return new ResponseEntity<>(satelliteFileDate, OK);
	}
	
}
