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
import com.satellitedata.repository.SatelliteFileDataRepository;
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
public class SatelliteFileDataController extends ExceptionHandling {
	 public static final String FILE_DELETED_SUCCESSFULLY = "File deleted successfully";
	 
	@Autowired SatelliteFileDataService satfiledataservice;
	
	@Autowired SatelliteFileDataRepository satfiledatarepo;
	//defining a location
	
	@PostMapping("/uploadfile")
	public ResponseEntity<SatelliteFileData> uploadFile(@RequestParam("file") MultipartFile multipartFile,
														@RequestParam("uploader") String uploader)  throws IOException, FileUploadErrorException {
		SatelliteFileData satelliteFileData = satfiledataservice.uploadFile(multipartFile, uploader);
		return new ResponseEntity<>(satelliteFileData, OK);
	}
	
	@GetMapping("/downloadfile/{fileid}")
	public ResponseEntity<Resource> downloadFile(@PathVariable("fileid") String fileuniqueid)  throws IOException, FileUploadErrorException {
		Resource resource = satfiledataservice.downloadFile(fileuniqueid);
		SatelliteFileData satdatafileD = satfiledatarepo.FindByFileuniqueid(fileuniqueid);
		Path filePath = get(SATDATA_FOLDER).toAbsolutePath().normalize().resolve(satdatafileD.getFilename());
		HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("File-Name", satdatafileD.getFilename());
        httpHeaders.add(CONTENT_DISPOSITION, "attachment;File-Name=" + resource.getFilename());
        return ResponseEntity.ok().contentType(MediaType.parseMediaType(Files.probeContentType(filePath)))
                .headers(httpHeaders).body(resource);
	}
	
	 @GetMapping("/list")
	    public ResponseEntity<List<SatelliteFileData>> getAllFiles() {
	        List<SatelliteFileData> satfiles = satfiledataservice.getSatelliteFileDatas();
	        if (satfiles == null) {
	        	return new ResponseEntity<>(null, NO_CONTENT);
	        }else {
	        	return new ResponseEntity<>(satfiles, OK);
	        }
	        
	    }
	 
	 @DeleteMapping("/delete/{fileid}")
	 @PreAuthorize("hasAnyAuthority('satellitedata:delete')")
	   public ResponseEntity<HttpResponse> deleteUser(@PathVariable("fileid") String fileuniqueid) throws IOException {
		  satfiledataservice.deleteFile(fileuniqueid);
	      return response(OK, FILE_DELETED_SUCCESSFULLY);
	 }
	 
	 private ResponseEntity<HttpResponse> response(HttpStatus httpStatus, String message) {
	    	HttpResponse body = new HttpResponse(httpStatus.value(), 
					 							 httpStatus,
					 							 httpStatus.getReasonPhrase().toUpperCase(),
					 							 message.toUpperCase());
	        return new ResponseEntity<>(body, httpStatus);
	    }
}
