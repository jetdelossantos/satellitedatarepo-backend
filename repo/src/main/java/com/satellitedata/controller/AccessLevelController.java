package com.satellitedata.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.satellitedata.model.AccessLevel;
import com.satellitedata.service.AccessLevelService;

@RestController
@RequestMapping("/accesslevel")
public class AccessLevelController {
		
	@Autowired AccessLevelService accesslevelService;
		
	@GetMapping("/allaccesslevels")
	public List<AccessLevel> findAll() {
		return accesslevelService.findAll();
	}
		
	@GetMapping("/accesslevels")
	public Optional<AccessLevel> getByAccesslevels(@RequestParam(value= "accesslevel") int accesslevel) {
		return accesslevelService.getByAccesslevels(accesslevel);
	}
		
	@GetMapping("/accessname")
	public Optional<AccessLevel> getByAccessname(@RequestParam(value= "accessname") String accessname) {
		return accesslevelService.getByAccessname(accessname);
	}

		
	@PostMapping("/save")
	public boolean saveUser(@RequestBody(required=true) AccessLevel accesslevel) {
		return accesslevelService.save(accesslevel);
	}
}
