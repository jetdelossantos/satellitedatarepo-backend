package com.satellitedata.service;

import java.io.IOException;
import java.util.List;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import com.satellitedata.exception.domain.FileUploadErrorException;
import com.satellitedata.model.SatelliteFileData;


public interface SatelliteFileDataService {
	
	/**
	  public  SatelliteFileData store(MultipartFile file) throws IOException {
	    String fileName = StringUtils.cleanPath(file.getOriginalFilename());
	    SatelliteFileData SatelliteFileData = new SatelliteFileData(null, fileName, file.getContentType(), file.getBytes(), fileName, fileName, 0, null, null);
	    return satfileDataRepository.save(SatelliteFileData);
	  }
	  
	  Long id, String filename, String filenameUrl, byte[] data, String uploader, String format,
		int downloads, Date created, Date modified
		
	  public SatelliteFileData getFile(String id) {
	    return satfileDataRepository.findAllById(id).get();
	  }
	  
	  public Stream<SatelliteFileData> getAllFiles() {
	    return satfileDataRepository.findAll().stream();
	  }
	  **/
	  List<SatelliteFileData> getSatelliteFileDatas();
	  
	  void deleteFile(Long id) throws IOException;
	  
	  Resource downloadFile(Long id) throws IOException;

	  SatelliteFileData uploadFile(MultipartFile MultipartFile, String uploader) throws FileUploadErrorException, IOException;
}

