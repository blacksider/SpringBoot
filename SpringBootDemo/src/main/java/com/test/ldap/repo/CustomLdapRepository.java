package com.test.ldap.repo;

import java.util.List;

import org.springframework.ldap.core.ContextMapper;

import com.test.ldap.pojo.Group;
import com.test.ldap.pojo.Person;
import com.test.model.LdapGroupServerInfo;

public interface CustomLdapRepository {

	List<Group> findGroupsByBase(LdapGroupServerInfo serverInfo, String base,
			int searchScope);

	List<Person> findPersonsByBase(LdapGroupServerInfo serverInfo, String base,
			int searchScope);

	List<Person> findPersonsByEmail(LdapGroupServerInfo serverInfo,
			String base, String email);

	List<Person> findManagersByPersonDN(LdapGroupServerInfo serverInfo,
			String personDN);

	boolean authenticateUser(LdapGroupServerInfo serverInfo, String userDN,
			String credentials);

	List<Person> findPersonByGroupDNAndAttrMapper(
			LdapGroupServerInfo serverInfo, String base, String filter,
			ContextMapper<Person> mapper);

}