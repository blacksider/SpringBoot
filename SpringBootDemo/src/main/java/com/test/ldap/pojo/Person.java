package com.test.ldap.pojo;

import javax.naming.Name;

import org.springframework.ldap.odm.annotations.Attribute;
import org.springframework.ldap.odm.annotations.Entry;
import org.springframework.ldap.odm.annotations.Id;
import org.springframework.ldap.odm.annotations.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entry(objectClasses = { "organizationalPerson", "person", "top", "user" })
public class Person {
	@Id
	@JsonIgnore
	private Name dn;

	@Attribute(name = "cn")
	private String fullName;

	@Attribute(name = "sn")
	private String lastName;

	@Attribute(name = "mail")
	private String mail;

	@Attribute(name = "managedObjects")
	private String managerObject;

	@Transient
	private String group;

	@Attribute(name = "description")
	private String description;

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public Name getDn() {
		return dn;
	}

	public void setDn(Name dn) {
		this.dn = dn;
	}

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public String getManagerObject() {
		return managerObject;
	}

	public void setManagerObject(String managerObject) {
		this.managerObject = managerObject;
	}

	@Override
	public String toString() {
		return "Person [dn=" + dn + ", fullName=" + fullName + ", lastName="
				+ lastName + ", mail=" + mail + ", managerObject="
				+ managerObject + ", group=" + group + ", description="
				+ description + "]";
	}

}
