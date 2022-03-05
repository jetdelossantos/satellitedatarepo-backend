package com.satellitedata.service;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.satellitedata.model.SatelliteDataBytes;

public interface SatelliteDataBytesService {
	
	List<SatelliteDataBytes> getSatelliteDataBytes();
	
	List<SatelliteDataBytes> findFilteredSatelliteDataBytes(String gst,
													  String datatpye,
													  String format
													  );
	
	void parseBytes(MultipartFile multipartFile, String fileuniqueid) throws IOException;
}
