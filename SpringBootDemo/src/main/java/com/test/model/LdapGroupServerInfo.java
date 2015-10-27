package com.test.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "server_groupInfo")
public class LdapGroupServerInfo implements Serializable {
	private static final long serialVersionUID = -6593963167852392917L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@Column(name = "ip")
	private String ip;

	@Column(name = "port")
	private String port;

	@Column(name = "base")
	private String base;

	@Column(name = "dnName")
	private String dnName;

	@Column(name = "password")
	private String password;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public String getBase() {
		return base;
	}

	public void setBase(String base) {
		this.base = base;
	}

	public String getDnName() {
		return dnName;
	}

	public void setDnName(String dnName) {
		this.dnName = dnName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String toString() {
		return "LdapGroupServerInfo [id=" + id + ", ip=" + ip + ", port="
				+ port + ", base=" + base + ", dnName=" + dnName
				+ ", password=" + password + "]";
	}
}
