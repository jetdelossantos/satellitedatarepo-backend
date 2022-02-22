package com.satellitedata.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.satellitedata.model.SatelliteFileData;

public interface SatelliteFileDataRepository extends JpaRepository <SatelliteFileData, Long> {

	SatelliteFileData findSatelliteFileDataById(Long id);

	void deleteById(Long id);

}
