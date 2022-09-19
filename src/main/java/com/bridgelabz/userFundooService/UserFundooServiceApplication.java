package com.bridgelabz.userFundooService;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableEurekaClient
public class UserFundooServiceApplication {
	public static void main(String[] args) {
		SpringApplication.run(UserFundooServiceApplication.class, args);
		System.out.println("Welcome to admin Service");
	}
}
