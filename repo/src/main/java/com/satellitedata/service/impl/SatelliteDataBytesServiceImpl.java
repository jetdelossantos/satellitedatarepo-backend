package com.satellitedata.service.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.satellitedata.model.SatelliteDataBytes;
import com.satellitedata.repository.SatelliteDataBytesRepository;
import com.satellitedata.service.SatelliteDataBytesService;

@Service
@Transactional
public class SatelliteDataBytesServiceImpl implements SatelliteDataBytesService {
	
	@Autowired SatelliteDataBytesRepository satdatbytesRepo;

	@Override
	public List<SatelliteDataBytes> getSatelliteDataBytes() {
		return satdatbytesRepo.findAll();
	}

	@Override
	public List<SatelliteDataBytes> findFilteredSatelliteDataBytes(String gst, 
																   String datatpye,
																   String format) {
		return satdatbytesRepo.findFilteredData(gst, datatpye, format);
	}

	@Override
	public void parseBytes(MultipartFile multipartFile, String fileuniqueid) throws IOException {
		File file = convertMultiPartToFile(multipartFile);
		Scanner scan = new Scanner(file, StandardCharsets.UTF_8);
		try {
			while(scan.hasNextLine()) {
				String line = scan.nextLine().trim();
				String[] bytes = line.split("\\s+");
				if (bytes.length == 16) {
					SatelliteDataBytes satdataByte = new SatelliteDataBytes();
					satdataByte.setGst(bytes[0] + "\t" + 
									   bytes[1] + "\t");
					satdataByte.setDatatype(bytes[2] + "\t");
					satdataByte.setTime(bytes[3] + "\t" + 
										bytes[4] + "\t" +
										bytes[5] + "\t" +
										bytes[6] + "\t");
					satdataByte.setSensor(bytes[7] + "\t" + 
										  bytes[8] + "\t" +
										  bytes[9] + "\t" +
										  bytes[10] + "\t" +
										  bytes[11] + "\t" +
										  bytes[12] + "\t" +
										  bytes[13] + "\t" +
										  bytes[14] + "\t");
					satdataByte.setChecksum(bytes[15] + "\t");
					satdataByte.setFilename(String.valueOf(fileuniqueid));
					satdataByte.setCreated(new Date());
					satdataByte.setFormat(String.valueOf(bytes.length));
					satdatbytesRepo.save(satdataByte);
				} else if (bytes.length == 32) {
					SatelliteDataBytes satdataByte = new SatelliteDataBytes();
					satdataByte.setGst(bytes[0] + "\t" + 
									   bytes[1] + "\t");
					satdataByte.setDatatype(bytes[2] + "\t");
					satdataByte.setTime(bytes[3] + "\t" + 
										bytes[4] + "\t" +
										bytes[5] + "\t" +
										bytes[6] + "\t");
					satdataByte.setSensor(bytes[7] + "\t" + 
										  bytes[8] + "\t" +
										  bytes[9] + "\t" +
										  bytes[10] + "\t" +
										  bytes[11] + "\t" +
										  bytes[12] + "\t" +
										  bytes[13] + "\t" +
										  bytes[14] + "\t" +
										  bytes[15] + "\t" +
										  bytes[16] + "\t" +
										  bytes[17] + "\t" +
										  bytes[18] + "\t" +
										  bytes[19] + "\t" +
										  bytes[20] + "\t" +
										  bytes[21] + "\t" +
										  bytes[22] + "\t" +
										  bytes[23] + "\t" +
										  bytes[24] + "\t" +
										  bytes[25] + "\t" +
										  bytes[26] + "\t" +
										  bytes[27] + "\t" +
										  bytes[28] + "\t" +
										  bytes[29] + "\t" +
										  bytes[30] + "\t");
					satdataByte.setChecksum(bytes[31] + "\t");
					satdataByte.setFilename(String.valueOf(fileuniqueid));
					satdataByte.setCreated(new Date());
					satdataByte.setFormat(String.valueOf(bytes.length));
					satdatbytesRepo.save(satdataByte);
				}
				else {
					continue;
				}		
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

}
