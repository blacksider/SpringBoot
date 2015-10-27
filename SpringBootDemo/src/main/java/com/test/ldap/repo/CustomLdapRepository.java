package com.test.ldap.repo;

import java.util.List;

import com.test.ldap.pojo.Group;
import com.test.ldap.pojo.Person;
import com.test.model.LdapGroupServerInfo;

public interface CustomLdapRepository {

	public abstract List<Group> findGroupsByBase(
			LdapGroupServerInfo serverInfo, String base, int searchScope);

	public abstract List<Person> findPersonsByBase(
			LdapGroupServerInfo serverInfo, String base, int searchScope);

	public abstract List<Person> findPersonsByEmail(
			LdapGroupServerInfo serverInfo, String base, String email);

	public abstract List<Person> findManagersByPersonDN(
			LdapGroupServerInfo serverInfo, String personDN);

	public abstract boolean authenticateUser(LdapGroupServerInfo serverInfo,
			String userDN, String credentials);

}