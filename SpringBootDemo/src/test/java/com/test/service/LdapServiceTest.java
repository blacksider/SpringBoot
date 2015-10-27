package com.test.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import javax.naming.directory.SearchControls;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.test.TestConfig;
import com.test.dao.LdapGroupRepository;
import com.test.ldap.pojo.Group;
import com.test.ldap.pojo.Person;
import com.test.ldap.repo.CustomLdapRepository;
import com.test.ldap.service.LdapService;
import com.test.model.LdapGroup;
import com.test.model.LdapGroupServerInfo;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = TestConfig.class)
@ActiveProfiles("DevTest")
public class LdapServiceTest {
	@Autowired
	private LdapService ldapService;
	@Autowired
	private LdapGroupRepository groupRepository;
	@Autowired
	private CustomLdapRepository ldapRepository;

	@Test
	public void testGetLdapGroupDirectly() {
		LdapGroupServerInfo groupServerInfo = new LdapGroupServerInfo();
		groupServerInfo.setBase("DC=dlp,DC=com");
		groupServerInfo.setIp("192.168.28.123");
		groupServerInfo.setPort("389");
		groupServerInfo.setDnName("syni@dlp.com");
		groupServerInfo.setPassword("q1w2E#R$");
		List<Group> groups = ldapRepository.findGroupsByBase(groupServerInfo,
				"DC=dlp,DC=com", SearchControls.SUBTREE_SCOPE);
		assertNotNull(groups);
	}

	@Test
	public void testSaveGroup() {
		LdapGroup group = new LdapGroup();
		group.setDn("DC=dlp,DC=com");
		group.setGroupName("SyniServer");
		group.setDescription("Main company of synitalent.");

		LdapGroupServerInfo groupServerInfo = new LdapGroupServerInfo();
		groupServerInfo.setBase("DC=dlp,DC=com");
		groupServerInfo.setIp("192.168.28.123");
		groupServerInfo.setPort("389");
		groupServerInfo.setDnName("syni@dlp.com");
		groupServerInfo.setPassword("q1w2E#R$");
		group.setServerInfo(groupServerInfo);

		long savedID = ldapService.saveNewServerGroup(group).toBlocking()
				.first();
		System.err.println(savedID);
		assertTrue(savedID > 0);
		LdapGroup saved = groupRepository.findByDn("DC=dlp,DC=com").get(0);
		assertNotNull(saved);
		assertEquals(group.getGroupName(), saved.getGroupName());
	}

	@Test
	public void testGetGroupOfLdap() {
		LdapGroup group = new LdapGroup();
		group.setDn("DC=dlp,DC=com");
		group.setGroupName("SyniServer");
		group.setDescription("Main company of synitalent.");

		LdapGroupServerInfo groupServerInfo = new LdapGroupServerInfo();
		groupServerInfo.setBase("DC=dlp,DC=com");
		groupServerInfo.setIp("192.168.28.123");
		groupServerInfo.setPort("389");
		groupServerInfo.setDnName("syni@dlp.com");
		groupServerInfo.setPassword("q1w2E#R$");
		group.setServerInfo(groupServerInfo);

		long savedID = ldapService.saveNewServerGroup(group).toBlocking()
				.first();
		assertTrue(savedID > 0);

		LdapGroup groupTree = ldapService
				.loadGroupOfServer(
						ldapService.getGroupServerInfo(savedID).toBlocking()
								.first()).toBlocking().first();
		assertNotNull(groupTree);
		assertTrue(groupTree.getSubGroups().size() > 0);
		loadTree(groupTree);
	}

	@Test
	public void testGetGroupPerson() {
		List<LdapGroup> all = groupRepository.findAll();
		assertNotNull(all);
		assertTrue(!all.isEmpty());
		LdapGroup group = all.get(0);
		assertNotNull(group);
		List<Person> persons = ldapService.getGroupPerson(group.getId())
				.toBlocking().first();
		assertNotNull(persons);
		assertTrue(persons.size() > 0);
		for (Person person : persons) {
			System.err.println(person);
		}
	}

	@Test
	public void testGetPersonByEmai() {
		LdapGroupServerInfo groupServerInfo = new LdapGroupServerInfo();
		groupServerInfo.setBase("DC=dlp,DC=com");
		groupServerInfo.setIp("192.168.28.123");
		groupServerInfo.setPort("389");
		groupServerInfo.setDnName("syni@dlp.com");
		groupServerInfo.setPassword("cTF3MkUjUiQ=");

		List<Person> persons = ldapRepository.findPersonsByEmail(
				groupServerInfo, null, "lu.xianyu@synitalent.com");

		assertNotNull(persons);
		assertTrue(persons.size() > 0);
		for (Person person : persons) {
			System.err.println(person);
		}
	}

	@Test
	public void testGetGroupTreeNoDB() {
		LdapGroupServerInfo groupServerInfo = new LdapGroupServerInfo();
		groupServerInfo.setBase("DC=dlp,DC=com");
		groupServerInfo.setIp("192.168.28.123");
		groupServerInfo.setPort("389");
		groupServerInfo.setDnName("syni@dlp.com");
		groupServerInfo.setPassword("cTF3MkUjUiQ=");
		LdapGroup groupTree = ldapService
				.loadGroupOfServerNoDB(groupServerInfo).toBlocking().first();
		assertNotNull(groupTree);
		loadTree(groupTree);
	}

	@Test
	public void testCheckUser() {
		LdapGroupServerInfo groupServerInfo = new LdapGroupServerInfo();
		groupServerInfo.setBase("DC=dlp,DC=com");
		groupServerInfo.setIp("192.168.28.123");
		groupServerInfo.setPort("389");
		groupServerInfo.setDnName("syni@dlp.com");
		groupServerInfo.setPassword("cTF3MkUjUiQ=");

		String userDN = "macong@dlp.com";// or macong
		String credentials = "syni@777";
		boolean rs = ldapService
				.authenticate(groupServerInfo, userDN, credentials)
				.toBlocking().first();
		assertTrue(rs);
	}

	public void loadTree(LdapGroup groupTree) {
		System.err.println(groupTree);
		List<LdapGroup> subGroups = groupTree.getSubGroups();
		if (subGroups != null && subGroups.size() > 0) {
			for (LdapGroup subGroup : subGroups) {
				loadTree(subGroup);
			}
		}
	}
}
