package com.satellitedata.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.satellitedata.model.User;

public interface UserRepository extends JpaRepository<User, Integer>{

	Optional<User> findByEmail(String email);

	Optional<User> findByUsername(String username);

	Optional<User> findById(int id);
	
	List<User> findAll();
	
	User findUserByUsername(String username);

	User findUserByEmail(String email);
}
