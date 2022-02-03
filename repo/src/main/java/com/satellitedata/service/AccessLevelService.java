package com.satellitedata.service;

import java.util.List;
import java.util.Optional;

import com.satellitedata.model.AccessLevel;


public interface AccessLevelService {
	
	Optional<AccessLevel> getByAccesslevels(int id);
	Optional<AccessLevel> getByAccessname(String accessname);
	boolean save(AccessLevel accesslevel);
	List<AccessLevel> findAll();
}
