package com.test.dao;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.test.model.User;

@Transactional
public interface UserDao extends CrudRepository<User, Integer> {
	@Query("select user from User user where user.id = :id")
	User findById(@Param("id") Integer id);

	// 自定义返回内容时可以指定Query注解实现自定义的SQL，使用HQL语言
	@Query("select user from User user where user.username = :name")
	User findByName(@Param("name") String name);

}
