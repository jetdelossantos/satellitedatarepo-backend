package com.satellitedata.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.satellitedata.model.User;

public interface UserRepository extends JpaRepository<User, Integer>{

    User findUserByUsername(String username);

    User findUserByEmail(String email);

	void deleteById(Long id);
}
