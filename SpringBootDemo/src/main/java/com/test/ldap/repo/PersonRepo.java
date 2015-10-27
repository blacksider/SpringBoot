//package com.test.ldap.repo;
//
//import static org.springframework.ldap.query.LdapQueryBuilder.query;
//
//import java.util.Iterator;
//import java.util.List;
//
//import javax.naming.InvalidNameException;
//import javax.naming.directory.SearchControls;
//import javax.naming.ldap.LdapName;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.ldap.core.DirContextOperations;
//import org.springframework.ldap.core.LdapTemplate;
//import org.springframework.ldap.core.support.AbstractContextMapper;
//import org.springframework.stereotype.Service;
//
//import com.test.dao.LdapGroupRepository;
//import com.test.dao.LdapPersonReposiry;
//import com.test.ldap.pojo.Group;
//import com.test.ldap.pojo.Person;
//import com.test.vo.LdapGroup;
//import com.test.vo.LdapPerson;
//
//@Service
//public class PersonRepo {
//
//	@Autowired
//	private LdapTemplate ldapTemplate;
//
//	@Autowired
//	private LdapPersonReposiry personReposiry;
//
//	@Autowired
//	private LdapGroupRepository groupRepository;
//
//	public List<LdapPerson> getAllPersons() {
//		SearchControls searchControls = new SearchControls();
//		searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);
//
//		LdapName base = null;
//		try {
//			base = new LdapName("DC=dlp,DC=com");
//		} catch (InvalidNameException e) {
//			e.printStackTrace();
//		}
//		List<Person> persons = ldapTemplate.findAll(base, searchControls,
//				Person.class);
//
//		for (Iterator<Person> iterator = persons.iterator(); iterator.hasNext();) {
//			Person person = iterator.next();
//			LdapPerson ldapPerson = new LdapPerson();
//			ldapPerson.setId(person.getDn().toString());
//			ldapPerson.setFullName(person.getFullName());
//			ldapPerson.setMail(person.getMail());
//			ldapPerson.setDescription(person.getDescription());
//
//			String dn = person.getDn().toString();
//
//			LdapGroup group = groupRepository.findOne(dn.substring(dn
//					.indexOf(",") + 1));
//
//			if (group != null) {
//				ldapPerson.setGroup(group);
//				personReposiry.save(ldapPerson);
//			}
//		}
//
//		return personReposiry.findAll();
//	}
//
//	public List<LdapGroup> getAllGroups() {
//		SearchControls searchControls = new SearchControls();
//		searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);
//		LdapName base = null;
//		try {
//			base = new LdapName("DC=dlp,DC=com");
//		} catch (InvalidNameException e) {
//			e.printStackTrace();
//		}
//
//		// set root group
//		LdapGroup rootGroup = new LdapGroup();
//		rootGroup.setId("DC=dlp,DC=com");
//		rootGroup.setGroupName("Synitalent");
//		rootGroup.setDescription("www.synitalent.com");
//		groupRepository.save(rootGroup);
//
//		// get all groups form ldap and save the simple info
//		List<Group> groups = ldapTemplate.findAll(base, searchControls,
//				Group.class);
//		for (Iterator<Group> iterator = groups.iterator(); iterator.hasNext();) {
//			Group group = iterator.next();
//			LdapGroup ldapGroup = new LdapGroup();
//			ldapGroup.setId(group.getDn().toString());
//			ldapGroup.setGroupName(group.getGroupName());
//			ldapGroup.setDescription(group.getDescription());
//			groupRepository.save(ldapGroup);
//		}
//
//		// get all groups from db and check parent group
//		List<LdapGroup> allDBGroups = groupRepository.findAll();
//		for (Iterator<LdapGroup> iterator = allDBGroups.iterator(); iterator
//				.hasNext();) {
//			LdapGroup ldapGroup = iterator.next();
//			String id = ldapGroup.getId();
//			LdapGroup parent = groupRepository.findOne(id.substring(id
//					.indexOf(",") + 1));
//			if (parent == null && !"DC=dlp,DC=com".equals(ldapGroup.getId())) {
//				ldapGroup.setParentGroup(rootGroup);
//			} else {
//				ldapGroup.setParentGroup(parent);
//			}
//			groupRepository.save(ldapGroup);
//		}
//
//		return allDBGroups;
//	}
//
//	// no ODM
//	public List<Person> getPersons() {
//		List<Person> persons = ldapTemplate.search(
//				query().base("OU=Server,DC=dlp,DC=com").where("objectclass")
//						.is("person").and("objectclass")
//						.is("organizationalPerson"), new PersonContextMapper());
//		return persons;
//	}
//
//	public List<Group> getGroups() {
//		List<Group> groups = ldapTemplate.search(
//				query().base("DC=advdbg,DC=org").where("objectclass")
//						.is("organizationalUnit"), new GroupContextMapper());
//		return groups;
//	}
//
//	// origin mapper
//	private static class PersonContextMapper extends
//			AbstractContextMapper<Person> {
//		public Person doMapFromContext(DirContextOperations context) {
//			Person person = new Person();
//			person.setFullName(context.getStringAttribute("cn"));
//			person.setLastName(context.getStringAttribute("sn"));
//			person.setMail(context.getStringAttribute("mail"));
//			person.setDescription(context.getStringAttribute("description"));
//			return person;
//		}
//	}
//
//	private static class GroupContextMapper extends
//			AbstractContextMapper<Group> {
//		public Group doMapFromContext(DirContextOperations context) {
//			Group group = new Group();
//			group.setGroupName(context.getStringAttribute("ou"));
//			group.setDescription(context.getStringAttribute("description"));
//			return group;
//		}
//	}
//}
