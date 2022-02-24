package com.satellitedata.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.satellitedata.model.SatelliteFileData;

public interface SatelliteFileDataRepository extends JpaRepository <SatelliteFileData, Long> {

	SatelliteFileData findSatelliteFileDataById(Long id);

	void deleteById(Long id);

}
