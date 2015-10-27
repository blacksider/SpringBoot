//package com.test.config;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.ldap.core.LdapTemplate;
//import org.springframework.ldap.core.support.LdapContextSource;
//
//@Configuration
//public class LdapConfigure {
//	@Value("${application.ldap.url}")
//	private String url;
//	@Value("${application.ldap.userName}")
//	private String userName;
//	@Value("${application.ldap.password}")
//	private String password;
//
//	@Bean
//	public LdapContextSource ldapContextSource() {
//		LdapContextSource contextSource = new LdapContextSource();
//		contextSource.setUrl(url);
//		contextSource.setUserDn(userName);
//		contextSource.setPassword(password);
//		contextSource.setReferral("follow");
//		return contextSource;
//	}
//
//	@Bean
//	@Autowired
//	public LdapTemplate ldapTemplate(LdapContextSource contextSource) {
//		LdapTemplate ldapTemplate = new LdapTemplate(contextSource);
//		ldapTemplate.setIgnorePartialResultException(true);
//		return ldapTemplate;
//	}
//}
