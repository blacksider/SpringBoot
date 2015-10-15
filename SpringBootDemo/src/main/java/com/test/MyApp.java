package com.test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//@ComponentScan
//@EnableAutoConfiguration
//@Configuration

// same as @Configuration @EnableAutoConfiguration @ComponentScan
@SpringBootApplication
public class MyApp {

	public static void main(String[] args) {
		// SpringApplication.run(MyApp.class, args);
		SpringApplication app = new SpringApplication(MyApp.class);
		app.setShowBanner(false);
		app.run(args);
	}

}
