package com.test.ldap.service;

import java.util.List;

import rx.Observable;

import com.test.ldap.pojo.Person;
import com.test.model.LdapGroup;
import com.test.model.LdapGroupServerInfo;

public interface LdapService {

	Observable<Long> saveNewServerGroup(LdapGroup ldapGroup);

	Observable<LdapGroupServerInfo> getGroupServerInfo(
			long ldapGroupID);

	// by given server to import all its group info from ldap and store it into
	// our database(a group tree)
	Observable<LdapGroup> loadGroupOfServer(
			LdapGroupServerInfo serverInfo);

	Observable<LdapGroup> getGroupTree(long groupID);

	// get person in current group
	Observable<List<Person>> getGroupPerson(long groupID);

	// TODO 根据Email查询人，遍历所有组还是单个组
	Observable<Person> getPersonByEmail();

	// get group and person with no db data, use ldap interface to get it
	Observable<LdapGroup> loadGroupOfServerNoDB(
			LdapGroupServerInfo serverInfo);

	// 验证用户信息
	Observable<Boolean> authenticate(
			LdapGroupServerInfo serverInfo, String userDN, String credentials);

	Observable<List<Person>> getGroupPersonWithAttributesMapper(long groupID);

}