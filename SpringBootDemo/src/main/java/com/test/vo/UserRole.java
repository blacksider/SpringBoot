package com.test.vo;

import org.springframework.security.core.GrantedAuthority;

//自定义的假的role， 必须要实现AuthenticationProvider接口，在getAuthority方法中返回role
public class UserRole implements GrantedAuthority {
	private static final long serialVersionUID = -4627456391931906877L;

	@Override
	public String getAuthority() {
		return "ROLE_ADMIN";
	}

}
