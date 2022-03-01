package com.satellitedata.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.satellitedata.model.User;

public interface UserRepository extends JpaRepository<User, Integer>{

    User findUserByUsername(String username);
    
    User findUserByEmail(String email);

	void deleteById(Long id);
	
	@Query(value = "SELECT u.*, c.countryname AS countryname, c.gst AS countrygst FROM dbo.users u INNER JOIN dbo.country c ON u.username = c.countryname", nativeQuery = true)
	@Transactional
	List<User> findAllDatabaseWithCountryJoin();
}
