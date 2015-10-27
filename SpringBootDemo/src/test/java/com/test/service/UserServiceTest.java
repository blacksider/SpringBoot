package com.test.service;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.test.TestConfig;
import com.test.model.MongoUser;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = TestConfig.class)
public class UserServiceTest {
	
	@Autowired
	private UserService service;
	
	@Test
	public void testSaveMongoUser(){
		MongoUser mongoUser = new MongoUser();
		mongoUser.setName("test");
		MongoUser saved = service.saveMongoUser(mongoUser).toBlocking().first();
		
		Assert.assertNotNull(saved);
		Assert.assertNotNull(saved.getId());
		Assert.assertEquals(mongoUser.getName(), saved.getName());
	}
}
