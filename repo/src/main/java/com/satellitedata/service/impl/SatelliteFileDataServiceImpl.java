package com.satellitedata.service.impl;


import java.util.Date;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.apache.tomcat.util.http.fileupload.FileUtils;

import com.satellitedata.model.SatelliteFileData;
import com.satellitedata.repository.SatelliteDataBytesRepository;
import com.satellitedata.repository.SatelliteFileDataRepository;
import com.satellitedata.service.SatelliteDataBytesService;
import com.satellitedata.service.SatelliteFileDataService;
import com.satellitedata.exception.domain.FileUploadErrorException;
import org.springframework.util.StringUtils;

import static com.satellitedata.constant.FileConstant.*;
import static java.nio.file.Files.copy;
import static java.nio.file.Paths.get;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
@Service
@Transactional
public class SatelliteFileDataServiceImpl implements SatelliteFileDataService {
	
	
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
			
			SatelliteFileData satelliteDataFile = new SatelliteFileData();
			satelliteDataFile.setFilename(multipartFile.getOriginalFilename());
			satelliteDataFile.setUploader(uploader);	
			satelliteDataFile.setCreated(new Date());
			satelliteDataFile.setFilesize(getFileByteSize(multipartFile));
			satelliteDataFile.setData(getFileByteData(multipartFile));
			satelliteDataFile.setFilenameUrl(filePath.toString());
			satelliteDataFile.setFormat(getPacketFormat(multipartFile));
			satelliteDataFile.setDownloads(0);
			satfiledatarepo.save(satelliteDataFile);
			satdatbytesService.parseBytes(multipartFile, satelliteDataFile.getId().intValue());
			return satelliteDataFile;
		} else {
			throw new FileUploadErrorException("File upload error. Invalid file");
		}
	}
	
	private String getPacketFormat(MultipartFile multipartFile) throws IOException {
		File file = convertMultiPartToFile(multipartFile);
		Scanner scan = new Scanner(file);
		try {
			String[] bytes = scan.nextLine().split("\\s+");
			if (bytes.length == 16 || bytes.length == 32 ) {
				return (String.valueOf(bytes.length));
			} else {
				return null;
			}
		} finally {
			scan.close();
		}
	}

	private File convertMultiPartToFile(MultipartFile file ) throws IOException
    {
        File convFile = new File( file.getOriginalFilename() );
        FileOutputStream fos = new FileOutputStream( convFile );
        fos.write( file.getBytes() );
        fos.close();
        return convFile;
    }
	
	private Long getFileByteSize(MultipartFile multipartFile) throws IOException {
		byte [] bytes=multipartFile.getBytes();
		return (long) bytes.length;
	}

	private byte[] getFileByteData(MultipartFile multipartFile) throws IOException {
		byte [] bytes=multipartFile.getBytes();
		return bytes;
	}
	

	private boolean checkValidFile(MultipartFile multipartFile) throws IOException {
		String filename = multipartFile.getOriginalFilename().toUpperCase();
		int format = Integer.parseInt(getPacketFormat(multipartFile));
		if (multipartFile.isEmpty()) {
			return false;
		}
		if(!filename.endsWith(".TXT")) {
			return false;
		} else {
			if (format == 16 || format == 32) {
				return true;
			} else {
				return false;
			}
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
        	satdatbytesRepo.deleteRowsWithFileName(String.valueOf(satelliteFileData.getId())); 
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
