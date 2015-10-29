package com.test.ldap.repo;

import java.util.List;

import javax.naming.InvalidNameException;
import javax.naming.directory.DirContext;
import javax.naming.directory.SearchControls;
import javax.naming.ldap.LdapName;

import org.springframework.ldap.core.ContextMapper;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.LdapContextSource;
import org.springframework.ldap.filter.AndFilter;
import org.springframework.ldap.filter.EqualsFilter;
import org.springframework.ldap.support.LdapUtils;
import org.springframework.security.crypto.codec.Base64;
import org.springframework.stereotype.Component;

import com.test.ldap.pojo.Group;
import com.test.ldap.pojo.Person;
import com.test.model.LdapGroupServerInfo;

@Component
public class CustomLdapRepositoryImpl implements CustomLdapRepository {

	private LdapTemplate ldapTemplate;

	// open a ldap connection
	private void openLdapConnection(LdapGroupServerInfo serverInfo) {
		String url = "LDAP://" + serverInfo.getIp() + ":"
				+ serverInfo.getPort();
		LdapContextSource contextSource = new LdapContextSource();
		contextSource.setUrl(url);
		contextSource.setUserDn(serverInfo.getDnName());
		contextSource.setPassword(new String(Base64.decode(serverInfo
				.getPassword().getBytes())));
		contextSource.setReferral("follow");
		contextSource.afterPropertiesSet();
		ldapTemplate = new LdapTemplate(contextSource);
		ldapTemplate.setIgnorePartialResultException(true);
	}

	@Override
	public List<Group> findGroupsByBase(LdapGroupServerInfo serverInfo,
			String base, int searchScope) {
		openLdapConnection(serverInfo);

		SearchControls searchControls = new SearchControls();
		searchControls.setSearchScope(searchScope);

		LdapName ldapName = null;
		try {
			ldapName = new LdapName(base);
		} catch (InvalidNameException e) {
			e.printStackTrace();
		}

		return ldapTemplate.findAll(ldapName, searchControls, Group.class);
	}

	@Override
	public List<Person> findPersonsByBase(LdapGroupServerInfo serverInfo,
			String base, int searchScope) {
		openLdapConnection(serverInfo);
		SearchControls searchControls = new SearchControls();
		searchControls.setSearchScope(searchScope);
		LdapName name = null;
		try {
			name = new LdapName(base);
		} catch (InvalidNameException e) {
			e.printStackTrace();
		}
		return ldapTemplate.findAll(name, searchControls, Person.class);
	}

	@Override
	public List<Person> findPersonsByEmail(LdapGroupServerInfo serverInfo,
			String base, String email) {
		openLdapConnection(serverInfo);
		// if no detail base attr, use server base to find
		if (base == null) {
			base = serverInfo.getBase();
		}

		SearchControls searchControls = new SearchControls();
		searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);

		LdapName name = null;
		try {
			name = new LdapName(base);
		} catch (InvalidNameException e) {
			e.printStackTrace();
		}

		AndFilter filter = new AndFilter();
		filter.and(new EqualsFilter("mail", email));

		return ldapTemplate.find(name, filter, searchControls, Person.class);
	}

	@Override
	public List<Person> findManagersByPersonDN(LdapGroupServerInfo serverInfo,
			String personDN) {
		openLdapConnection(serverInfo);

		personDN = personDN.substring(personDN.indexOf(",") + 1);

		SearchControls searchControls = new SearchControls();
		searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);

		LdapName name = null;
		try {
			name = new LdapName(personDN);
		} catch (InvalidNameException e) {
			e.printStackTrace();
		}

		AndFilter filter = new AndFilter();
		filter.and(new EqualsFilter("managedObjects", personDN));

		return ldapTemplate.find(name, filter, searchControls, Person.class);
	}

	@Override
	public boolean authenticateUser(LdapGroupServerInfo serverInfo,
			String userDN, String credentials) {
		openLdapConnection(serverInfo);

		DirContext ctx = null;
		try {
			ctx = ldapTemplate.getContextSource().getContext(userDN,
					credentials);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			LdapUtils.closeContext(ctx);
		}
	}

	@Override
	public List<Person> findPersonByGroupDNAndAttrMapper(
			LdapGroupServerInfo serverInfo, String base, String filter,
			ContextMapper<Person> mapper) {
		openLdapConnection(serverInfo);
		return ldapTemplate.search(base, filter, mapper);
	}
}
