package com.test.ldap.service;

import java.util.List;

import rx.Observable;

import com.test.ldap.pojo.Person;
import com.test.model.LdapGroup;
import com.test.model.LdapGroupServerInfo;

public interface LdapService {

	public abstract Observable<Long> saveNewServerGroup(LdapGroup ldapGroup);

	public abstract Observable<LdapGroupServerInfo> getGroupServerInfo(
			long ldapGroupID);

	// by given server to import all its group info from ldap and store it into
	// our database(a group tree)
	public abstract Observable<LdapGroup> loadGroupOfServer(
			LdapGroupServerInfo serverInfo);

	public abstract Observable<LdapGroup> getGroupTree(long groupID);

	// get person in current group
	public abstract Observable<List<Person>> getGroupPerson(long groupID);

	// TODO 根据Email查询人，遍历所有组还是单个组
	public abstract Observable<Person> getPersonByEmail();

	// get group and person with no db data, use ldap interface to get it
	public abstract Observable<LdapGroup> loadGroupOfServerNoDB(
			LdapGroupServerInfo serverInfo);

	// 验证用户信息
	public abstract Observable<Boolean> authenticate(
			LdapGroupServerInfo serverInfo, String userDN, String credentials);

}