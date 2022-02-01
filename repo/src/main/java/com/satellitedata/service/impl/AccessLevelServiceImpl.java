package com.satellitedata.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.satellitedata.model.AccessLevel;
import com.satellitedata.repository.AccessLevelRepository;
import com.satellitedata.service.AccessLevelService;

@Service
public class AccessLevelServiceImpl implements AccessLevelService {

	@Autowired 
	AccessLevelRepository accesslevelRepository;

	@Override
	public Optional<AccessLevel> getByAccesslevels(int accesslevel) {
		Optional<AccessLevel> a = accesslevelRepository.findByAccesslevels(accesslevel);
		return a;
	}

	@Override
	public Optional<AccessLevel> getByAccessname(String accessname) {
		Optional<AccessLevel> a = accesslevelRepository.findByAccessname(accessname);
		return a;
	}


	@Override
	public boolean save(AccessLevel accesslevel) {
		accesslevelRepository.save(accesslevel);
		return true;
	}

	@Override
	public List<AccessLevel> findAll() {
		List<AccessLevel> a = accesslevelRepository.findAll();
		return a;
	}
}
