package com.test.reqVo;

import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ReqUser {

	@NotEmpty
	@JsonProperty("username")
	private String username;

	@NotEmpty
	@JsonProperty("password")
	private String password;

	@JsonProperty("id")
	private String id;

	@JsonProperty("age")
	private String age;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}

}
