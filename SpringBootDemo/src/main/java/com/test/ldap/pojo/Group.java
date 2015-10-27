package com.test.ldap.pojo;

import javax.naming.Name;

import org.springframework.ldap.odm.annotations.Attribute;
import org.springframework.ldap.odm.annotations.Entry;
import org.springframework.ldap.odm.annotations.Id;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entry(objectClasses = { "organizationalUnit", "top" })
public final class Group {
	@Id
	@JsonIgnore
	private Name dn;

	@Attribute(name = "ou")
	private String groupName;

	@Attribute(name = "description")
	private String description;

	public Name getDn() {
		return dn;
	}

	public void setDn(Name dn) {
		this.dn = dn;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		return "Group [dn={" + dn + "}, groupName=" + groupName
				+ ", description=" + description + "]";
	}
}
