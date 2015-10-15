package com.test.dao;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.test.vo.MongoUser;
import java.lang.String;
import java.util.List;

public interface MongoUserDao extends MongoRepository<MongoUser, String> {
	List<MongoUser> findByName(String name);
}
