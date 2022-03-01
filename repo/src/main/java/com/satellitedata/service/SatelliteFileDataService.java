package com.satellitedata.service;

import java.io.IOException;
import java.util.List;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import com.satellitedata.exception.domain.FileUploadErrorException;
import com.satellitedata.model.SatelliteFileData;


public interface SatelliteFileDataService {
	
	  List<SatelliteFileData> getSatelliteFileDatas();
	  
	  void deleteFile(String fileuniqueid) throws IOException;
	  
	  Resource downloadFile(String fileuniqueid) throws IOException;

	  SatelliteFileData uploadFile(MultipartFile MultipartFile, String uploader) throws FileUploadErrorException, IOException;
}

