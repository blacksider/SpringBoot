package com.test.security;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.test.dao.UserDao;

//定义AuthenticationProvider实现数据库验证
@Component("databaseUserAuthProvicer")
public class DatabaseUserAuthProvider implements AuthenticationProvider {
	@Autowired
	private UserDao userDao;

	@Resource(name = "customUserDetailsService")
	private UserDetailsService detailsService;

	@Autowired
	private PasswordEncoder encoder;

	@Override
	public Authentication authenticate(Authentication auth)
			throws AuthenticationException {
		// 从认证登录界面获取的reqToken中获取用户名和密码和数据库中拿取到的用户数据进行比较
		UsernamePasswordAuthenticationToken reqToken = (UsernamePasswordAuthenticationToken) auth;
		final String userName = reqToken.getName();
		final String userPwd = (String) reqToken.getCredentials();
		if (StringUtils.isEmpty(userName) || StringUtils.isEmpty(userPwd)) {
			throw new BadCredentialsException("Empty user name or password");
		}
		UserDetails user = detailsService.loadUserByUsername(userName);
		if (user == null) {
			throw new UsernameNotFoundException("No such user");
		} else {
			final String dbUserPwdHash = user.getPassword();
			if (!encoder.matches(userPwd, dbUserPwdHash)) {
				throw new BadCredentialsException("error username password");
			}
		}

		UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
				userName, userPwd, user.getAuthorities());
		return authToken;
	}

	@Override
	public boolean supports(Class<?> auth) {
		return UsernamePasswordAuthenticationToken.class.equals(auth);
	}
}
