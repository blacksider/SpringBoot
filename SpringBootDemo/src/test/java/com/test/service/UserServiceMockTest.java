package com.test.service;

import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatcher;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.test.TestConfig;
import com.test.dao.MongoUserDao;
import com.test.vo.MongoUser;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = TestConfig.class)
public class UserServiceMockTest {

	@InjectMocks
	@Autowired
	private UserService service;

	@Mock
	private MongoUserDao mongoUserDao;

	@Rule
	public ExpectedException expectedException = ExpectedException.none();

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testGetMongoUser(){
		List<MongoUser> list = new ArrayList<MongoUser>();
		MongoUser user = new MongoUser();
		user.setId(UUID.randomUUID().toString());
		user.setName("user");
		list.add(user);

		when(mongoUserDao.findByName(anyString())).thenReturn(list);
		when(mongoUserDao.findByName(null)).thenThrow(
				new NullPointerException("username cannot be null"));

		List<MongoUser> listRs = service.findMongoUserByName("user")
				.toBlocking().first();
		
		verify(mongoUserDao).findByName(anyString());

		Assert.assertNotNull(listRs);
		Assert.assertTrue(listRs.containsAll(list));

		expectedException.expect(NullPointerException.class);
		expectedException.expectMessage("username cannot be null");
		service.findMongoUserByName(null).toBlocking().first();
	}

	@Test
	public void testSaveMongoUser() {
		String[] ids = new String[1];
		when(mongoUserDao.save(argThat(new ArgumentMatcher<MongoUser>() {
			@Override
			public boolean matches(Object argument) {
				return argument instanceof MongoUser;
			}
		}))).thenAnswer(new Answer<MongoUser>() {
			@Override
			public MongoUser answer(InvocationOnMock invocation)
					throws Throwable {
				MongoUser user = invocation.getArgumentAt(0, MongoUser.class);
				user.setId(ids[0] = UUID.randomUUID().toString());
				return user;
			}
		});
		MongoUser mongoUser = new MongoUser();
		mongoUser.setName("test");
		MongoUser saved = service.saveMongoUser(mongoUser).toBlocking().first();

		Assert.assertNotNull(saved);
		Assert.assertEquals(mongoUser.getName(), saved.getName());
		Assert.assertEquals(ids[0], saved.getId());
	}
}
