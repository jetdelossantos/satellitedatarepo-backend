package com.satellitedata.service.impl;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

import javax.mail.MessagingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
import org.apache.commons.lang3.RandomStringUtils;

import com.satellitedata.domain.UserPrincipal;
import com.satellitedata.enumeration.Role;
import com.satellitedata.exception.domain.EmailExistException;
import com.satellitedata.exception.domain.EmailNotFoundException;
import com.satellitedata.exception.domain.NotAnImageFileException;
import com.satellitedata.exception.domain.PasswordIncorrectException;
import com.satellitedata.exception.domain.UserNotFoundException;
import com.satellitedata.exception.domain.UsernameExistException;
import com.satellitedata.model.SatelliteFileData;
import com.satellitedata.model.User;
import com.satellitedata.repository.SatelliteDataBytesRepository;
import com.satellitedata.repository.SatelliteFileDataRepository;
import com.satellitedata.repository.UserRepository;
import com.satellitedata.service.EmailService;
import com.satellitedata.service.LoginAttemptService;
import com.satellitedata.service.SatelliteDataBytesService;
import com.satellitedata.service.SatelliteFileDataService;
import com.satellitedata.service.UserService;
import com.satellitedata.exception.domain.FileUploadErrorException;
import org.springframework.util.StringUtils;

import static com.satellitedata.constant.UserImplConstant.*;
import static com.satellitedata.enumeration.Role.*;
import static com.satellitedata.constant.FileConstant.*;
import static java.nio.file.Files.copy;
import static java.nio.file.Paths.get;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import static org.springframework.http.HttpHeaders.CONTENT_DISPOSITION;
import static org.springframework.http.MediaType.*;

@Service
@Transactional
public class SatelliteFileDataServiceImpl implements SatelliteFileDataService {
	
	private Logger LOGGER = LoggerFactory.getLogger(SatelliteFileDataServiceImpl.class);
	
	@Autowired SatelliteFileDataRepository satfiledatarepo;

	@Autowired SatelliteDataBytesService satdatbytesService;
	
	@Autowired SatelliteDataBytesRepository satdatbytesRepo;
	
	@Override
	public List<SatelliteFileData> getSatelliteFileDatas() {
		return satfiledatarepo.findAll();
	}

	@Override
	public SatelliteFileData uploadFile(MultipartFile multipartFile, String uploader) throws FileUploadErrorException, IOException {
		
		if(checkValidFile(multipartFile)) {
			Path filePath = saveFileInStorage(multipartFile);
			satdatbytesService.parseBytes(multipartFile,multipartFile.getOriginalFilename());
			SatelliteFileData satelliteDataFile = new SatelliteFileData();
			satelliteDataFile.setFilename(multipartFile.getOriginalFilename());
			satelliteDataFile.setUploader(uploader);	
			satelliteDataFile.setCreated(new Date());
			satelliteDataFile.setFilesize(getFileByteSize(multipartFile));
			satelliteDataFile.setData(getFileByteData(multipartFile));
			satelliteDataFile.setFilenameUrl(filePath.toString());
			satelliteDataFile.setFormat(null);
			satelliteDataFile.setDownloads(0);
			satfiledatarepo.save(satelliteDataFile);
			return satelliteDataFile;
		} else {
			throw new FileUploadErrorException("File upload error. Invalid file");
		}
	}
	
	private Long getFileByteSize(MultipartFile multipartFile) throws IOException {
		byte [] bytes=multipartFile.getBytes();
		return (long) bytes.length;
	}

	private byte[] getFileByteData(MultipartFile multipartFile) throws IOException {
		byte [] bytes=multipartFile.getBytes();
		return bytes;
	}
	

	private boolean checkValidFile(MultipartFile multipartFile) {
		String filename = multipartFile.getOriginalFilename().toUpperCase();
		if (multipartFile.isEmpty()) {
			return false;
		}
		if(!filename.endsWith(".TXT")) {
			return false;
		} else {
			return true;
		}
	}

	private Path saveFileInStorage(MultipartFile multipartFile) throws IOException {
		String filename = StringUtils.cleanPath(multipartFile.getOriginalFilename());
		Path fileStorage = get(SATDATA_FOLDER, filename).toAbsolutePath().normalize();
		if(!Files.exists(fileStorage)) {
            Files.createDirectories(fileStorage);
        }
		try {
			copy(multipartFile.getInputStream(), fileStorage, REPLACE_EXISTING);
			return fileStorage;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return fileStorage;
	}

	@Override
	public void deleteFile(Long id) throws IOException {
		SatelliteFileData satelliteFileData = satfiledatarepo.findSatelliteFileDataById(id);
        Path satfiledataFolder = Paths.get(SATDATA_FOLDER + satelliteFileData.getFilename()).toAbsolutePath().normalize();
        if (!Files.exists(satfiledataFolder)) {
        	FileUtils.deleteDirectory(new File(satfiledataFolder.toString()));
        }
        if (satelliteFileData != null) {
        	satdatbytesRepo.deleteRowsWithFileName(satelliteFileData.getFilename()); 
            satfiledatarepo.deleteById(satelliteFileData.getId());    
        }
	}

	@Override
	public Resource downloadFile(Long id) throws IOException {
		SatelliteFileData satellitefiledata = satfiledatarepo.getById(id);
		String filename = satellitefiledata.getFilename();
		Path filePath = get(SATDATA_FOLDER).toAbsolutePath().normalize().resolve(filename);
        if(!Files.exists(filePath)) {
            throw new FileNotFoundException(filename + " was not found on the server");
        }
        Resource resource = new UrlResource(filePath.toUri());
       return resource;
	
	}

}
