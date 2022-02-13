package com.satellitedata.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.satellitedata.model.User;

public interface UserRepository extends JpaRepository<User, Integer>{

    User findUserByUsername(String username);

    User findUserByEmail(String email);

	void deleteById(Long id);
}
