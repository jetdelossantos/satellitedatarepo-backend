package com.satellitedata.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.satellitedata.model.AccessLevel;

public interface AccessLevelRepository extends JpaRepository<AccessLevel, Integer>{

	Optional<AccessLevel> findByAccessname(String Accessname);

	Optional<AccessLevel> findByAccesslevels(int accesslevels);
	
	List<AccessLevel> findAll();
}
