package com.test.service;

import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.test.TestConfig;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = TestConfig.class)
public class PersonServiceTest {
//	@Autowired
//	private PersonRepo repo;
//
//	@Test
//	public void testGetPersons() {
//		List<LdapGroup> groups = repo.getAllGroups();
//		Assert.assertNotNull(groups);
//		for (LdapGroup ldapGroup : groups) {
//			System.err.println(ldapGroup);
//		}
//		List<LdapPerson> list = repo.getAllPersons();
//		Assert.assertNotNull(list);
//		for (LdapPerson p : list) {
//			System.err.println(p);
//		}
//	}
}
