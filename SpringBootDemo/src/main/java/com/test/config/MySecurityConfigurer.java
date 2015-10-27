package com.test.config;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.servlet.configuration.EnableWebMvcSecurity;

@Configuration
@EnableWebMvcSecurity
public class MySecurityConfigurer extends WebSecurityConfigurerAdapter {
	@Resource(name = "databaseUserAuthProvider")
	public AuthenticationProvider authenticationProvider;

	// @Override
	// protected void configure(AuthenticationManagerBuilder builder)
	// throws Exception { // 内存认证 //
	// builder.inMemoryAuthentication().withUser("user").password("user") //
	// .roles("USER").and().withUser("admin").password("admin") //
	// .roles("ADMIN");
	//
	// // provider提供数据库认证方式
	// builder.authenticationProvider(authenticationProvider);
	// }

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.httpBasic().disable();
		http.csrf().disable();

		// 设置拦截规则
		http.authorizeRequests().antMatchers("/login").permitAll()// 可以直接访问/login
				.antMatchers("/user/*").permitAll()
				.antMatchers("/ldap/**").permitAll()
				.anyRequest().authenticated();// 其他的请求都要验证过

		// 开启默认登录页面 关闭
		 http.formLogin().disable();

		// session管理
		http.sessionManagement().sessionFixation().changeSessionId()
				.maximumSessions(1).expiredUrl("/");
	}

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth)
			throws Exception {
		auth.authenticationProvider(authenticationProvider);
	}
}
