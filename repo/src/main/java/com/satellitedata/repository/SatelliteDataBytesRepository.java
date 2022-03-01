package com.satellitedata.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.satellitedata.model.SatelliteDataBytes;

public interface SatelliteDataBytesRepository extends JpaRepository<SatelliteDataBytes, Long>{

	@Query(value = "DECLARE @GST VARCHAR(20) = ?1\r\n"
			+ "DECLARE @DATATYPE  VARCHAR(20) = ?2\r\n"
			+ "SELECT * FROM dbo.satdatabytes WHERE gst LIKE @GST AND datatype LIKE @DATATYPE",
			nativeQuery = true)
	@Modifying
	@Transactional
	List<SatelliteDataBytes> findFilteredData(String gst, String datatype);
	
	@Query(value = "DELETE FROM dbo.satdatabytes WHERE filename = ?1", nativeQuery = true)
	@Modifying
	@Transactional
	public void deleteRowsWithFileName(String filename);
}
