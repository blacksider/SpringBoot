package com.test.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

//配置密码加密方式
@Configuration
public class PasswordEncoderConfig {

	private int defaultLength = 11;

	@Bean
	public PasswordEncoder bCryptPasswordEncoder() {
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(defaultLength);
		return encoder;
	}
}
