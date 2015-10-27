package com.test.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

@Configuration
public class TransactionConfig {

	@Bean
	@Autowired
	public TransactionTemplate defaultTransactionTemplate(
			PlatformTransactionManager transManager) {
		return new TransactionTemplate(transManager);
	}

}
