package com.satellitedata;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.satellitedata.service.impl.UserServiceImpl;

import static com.satellitedata.constant.FileConstant.*;

import java.io.File;

@SpringBootApplication
@ComponentScan(basePackages = "com.satellitedata")
@EntityScan ("com.satellitedata.model")
@EnableJpaRepositories(basePackages = "com.satellitedata.repository")
public class RepoApplication {
	
	private static Logger LOGGER = LoggerFactory.getLogger(RepoApplication.class);
	
	public static void main(String[] args) {
		SpringApplication.run(RepoApplication.class, args);
		
		new File(USER_FOLDER).mkdirs();
		try {
			LOGGER.info(USER_FOLDER);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	@Bean
	public BCryptPasswordEncoder bCryptPPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
