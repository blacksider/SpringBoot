package com.test.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.test.common.Constants.LdapGroupType;

@Entity
@Table(name = "groups")
public class LdapGroup implements Serializable {
	private static final long serialVersionUID = 717000633892038463L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@Column(name = "dn", unique = true)
	private String dn;

	@Column(name = "server_dn")
	private String serverDN;

	@Column(name = "groupName")
	private String groupName;

	@Column(name = "description")
	private String description;

	@Column(name = "groupType")
	private String groupTypeStr;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "group_serverID", unique = true)
	@Fetch(FetchMode.JOIN)
	private LdapGroupServerInfo serverInfo;

	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "parentGroupID")
	private LdapGroup parentGroup;

	@OneToMany(mappedBy = "parentGroup", fetch = FetchType.LAZY)
	private List<LdapGroup> subGroups;

	@JsonIgnore
	@OneToMany(mappedBy = "group", fetch = FetchType.LAZY)
	private List<LdapPerson> persons;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getDn() {
		return dn;
	}

	public void setDn(String dn) {
		this.dn = dn;
	}

	public String getServerDN() {
		return serverDN;
	}

	public void setServerDN(String serverDN) {
		this.serverDN = serverDN;
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

	public LdapGroupType getGroupType() {
		return LdapGroupType.parse(this.groupTypeStr);
	}

	public void setGroupType(LdapGroupType groupType) {
		this.groupTypeStr = groupType.getValue();
	}

	public LdapGroupServerInfo getServerInfo() {
		return serverInfo;
	}

	public void setServerInfo(LdapGroupServerInfo serverInfo) {
		this.serverInfo = serverInfo;
	}

	public LdapGroup getParentGroup() {
		return parentGroup;
	}

	public void setParentGroup(LdapGroup parentGroup) {
		this.parentGroup = parentGroup;
	}

	public List<LdapGroup> getSubGroups() {
		return subGroups;
	}

	public void setSubGroups(List<LdapGroup> subGroups) {
		this.subGroups = subGroups;
	}

	public List<LdapPerson> getPersons() {
		return persons;
	}

	public void setPersons(List<LdapPerson> persons) {
		this.persons = persons;
	}

	@Override
	public String toString() {
		return "LdapGroup [id=" + id + ", dn=" + dn + ", serverDN=" + serverDN
				+ ", groupName=" + groupName + ", description=" + description
				+ ", groupTypeStr=" + groupTypeStr + ", serverInfo="
				+ serverInfo + "]";
	}

}
