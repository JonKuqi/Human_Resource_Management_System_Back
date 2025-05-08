package com.hrms.Human_Resource_Management_System_Back;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EntityScan(basePackages = {
		"com.hrms.Human_Resource_Management_System_Back.model",
		"com.hrms.Human_Resource_Management_System_Back.model.tenant"
})
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = "com.hrms.Human_Resource_Management_System_Back.repository")
public class HumanResourceManagementSystemBackApplication {
	public static void main(String[] args) {
		SpringApplication.run(HumanResourceManagementSystemBackApplication.class, args);
	}
}
