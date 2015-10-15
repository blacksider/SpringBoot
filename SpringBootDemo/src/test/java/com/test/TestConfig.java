package com.test;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.test.config.MySecurityConfigurer;
import com.test.config.PasswordEncoderConfig;

@Configuration
@EnableAutoConfiguration
@ComponentScan({ "com.test.service", "com.test.security", "com.test.vo",
		"com.test.dao" })
@Import({ PasswordEncoderConfig.class, MySecurityConfigurer.class })
public class TestConfig {

}
