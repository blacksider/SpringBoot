package com.test.model;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "persons")
public class LdapPerson implements Serializable {
	private static final long serialVersionUID = -573502832466303844L;

	@Id
	private String id;

	@Column(name = "fullName", nullable = false)
	private String fullName;

	@Column(name = "emial")
	private String mail;

	@Column(name = "description")
	private String description;

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "groupID")
	private LdapGroup group;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public LdapGroup getGroup() {
		return group;
	}

	public void setGroup(LdapGroup group) {
		this.group = group;
	}

	@Override
	public String toString() {
		return "LdapPerson [id={" + id + "}, fullName=" + fullName + ", mail="
				+ mail + ", description=" + description + ", group=" + group
				+ "]";
	}

}
