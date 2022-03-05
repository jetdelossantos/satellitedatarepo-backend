package com.satellitedata.controller;

import static org.springframework.http.HttpHeaders.CONTENT_DISPOSITION;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;

import java.io.IOException;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.satellitedata.domain.HttpResponse;
import com.satellitedata.exception.domain.FileUploadErrorException;
import com.satellitedata.model.SatelliteDataBytes;
import com.satellitedata.model.SatelliteFileData;
import com.satellitedata.repository.SatelliteDataBytesRepository;

@RestController
@RequestMapping("/satbytes")
public class SatelliteDataBytesController {

	@Autowired SatelliteDataBytesRepository satdatabytesRepo;
	
	
	
	@PostMapping("/getsatbytes")
	public ResponseEntity<List<SatelliteDataBytes>> getbytes(@RequestParam("gst") String gst,
															 @RequestParam("format") String format,
													  		 @RequestParam("datatype") String datatype)
	{
		List<SatelliteDataBytes> satellitedatabytes = satdatabytesRepo.findFilteredData(gst, datatype, format);
		 if (satellitedatabytes.isEmpty()) {
	        return new ResponseEntity<>(null, NO_CONTENT);
	     } else {
	        return new ResponseEntity<>(satellitedatabytes, OK);
	     }
	}
	
	@PostMapping("/downloadsatbytes")
	public ResponseEntity<String> downloadbytes(@RequestParam("gst") String gst,
			 									@RequestParam("datatype") String datatype,
			 									@RequestParam("format") String format,
			 									@RequestParam("requester") String requester) {
		List<SatelliteDataBytes> satellitedatabytes = satdatabytesRepo.findFilteredData(gst, datatype, format);
		 if (satellitedatabytes.isEmpty()) {
	        return new ResponseEntity<>(null, NO_CONTENT);
	     } else {
	    	 StringBuilder buf = new StringBuilder();
	    	 for (SatelliteDataBytes satellitedatabyte: satellitedatabytes) {
	    		 buf.append(satellitedatabyte.getGst())
	    		 	.append(satellitedatabyte.getDatatype())
	    		 	.append(satellitedatabyte.getTime())
	    		 	.append(satellitedatabyte.getSensor())
	    		 	.append(satellitedatabyte.getChecksum())
	    		 	.append("\r\n");
	    	 }
	    	 String filename = generateFilePrefix() +"."+ requester + ".txt" ;
	    	 HttpHeaders httpHeaders = new HttpHeaders();
	         httpHeaders.add("File-Name", generateFilePrefix() +"."+ requester + ".txt"  );
	         httpHeaders.add(CONTENT_DISPOSITION, "attachment;File-Name=" + filename);
	         return ResponseEntity.ok()
	        		 .contentType(MediaType.TEXT_PLAIN)
	                 .headers(httpHeaders)
	                 .body(buf.toString());
	     }
	}
	
	private String generateFilePrefix() {
		return new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
	}
	
	 private ResponseEntity<HttpResponse> response(HttpStatus httpStatus, String message) {
	    	HttpResponse body = new HttpResponse(httpStatus.value(), 
					 							 httpStatus,
					 							 httpStatus.getReasonPhrase().toUpperCase(),
					 							 message.toUpperCase());
	        return new ResponseEntity<>(body, httpStatus);
	}
}
