package com.satellitedata.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.satellitedata.model.SatelliteFileData;

public interface SatelliteFileDataRepository extends JpaRepository <SatelliteFileData, Long> {

	SatelliteFileData findSatelliteFileDataById(Long id);

	void deleteById(Long id);

	@Query(value = "Select TOP 1 * from dbo.satdatafiles WHERE fileuniqueid = ?1", nativeQuery = true)
	@Transactional
	SatelliteFileData FindByFileuniqueid(String fileid);
	
	@Query(value = "SELECT * From dbo.satdatafiles order by created desc", nativeQuery = true)
	@Transactional
	List<SatelliteFileData> findAllOrderByCreatedAsc();
	
	@Query(value = "SELECT filesize from dbo.satdatafiles", nativeQuery = true)
	@Transactional
	List<Integer> findTotalFileSizes();
	
	@Query(value = "SELECT Count(*) from dbo.satdatafiles", nativeQuery = true)
	@Transactional
	Integer findTotalFileCount();

}
