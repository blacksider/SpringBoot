package com.test;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.test.config.MySecurityConfigurer;
import com.test.config.PasswordEncoderConfig;
import com.test.config.TransactionConfig;

@Configuration
@EnableAutoConfiguration
@ComponentScan({ "com.test.service", "com.test.security", "com.test.vo",
		"com.test.dao", "com.test.ldap" })
@Import({ PasswordEncoderConfig.class, MySecurityConfigurer.class,
		TransactionConfig.class })
public class TestConfig {

}
