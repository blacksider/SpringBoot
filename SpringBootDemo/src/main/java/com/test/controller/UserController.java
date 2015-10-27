package com.test.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import com.test.model.Group;
import com.test.model.MongoUser;
import com.test.model.User;
import com.test.service.UserService;

@RestController
@RequestMapping("/user")
public class UserController {
	@Autowired
	private PasswordEncoder encoder;

	@Autowired
	private UserService userService;

	@RequestMapping(value = "/saveGroup", method = RequestMethod.POST)
	public DeferredResult<?> testGroupEnum(@RequestBody Group group) {
		DeferredResult<ResponseEntity<?>> deferredResult = new DeferredResult<>();

		ResponseEntity<Group> responseEntity = new ResponseEntity<>(group,
				HttpStatus.OK);
		deferredResult.setResult(responseEntity);

		return deferredResult;
	}

	// MongoDB
	@RequestMapping(value = "/saveMongoUser", method = RequestMethod.GET)
	public DeferredResult<?> saveMongoUser(HttpServletRequest request) {
		DeferredResult<ResponseEntity<?>> deferredResult = new DeferredResult<>();
		String name = request.getParameter("name");
		MongoUser mongoUser = new MongoUser();
		mongoUser.setName(name);

		userService
				.saveMongoUser(mongoUser)
				.subscribe(
						user -> {
							ResponseEntity<MongoUser> responseEntity = new ResponseEntity<MongoUser>(
									user, HttpStatus.OK);
							deferredResult.setResult(responseEntity);
						},
						error -> {
							ResponseEntity<String> resEntity = new ResponseEntity<String>(
									error.getMessage(),
									HttpStatus.INTERNAL_SERVER_ERROR);
							deferredResult.setResult(resEntity);
						});

		return deferredResult;
	}

	@RequestMapping(value = "/getMongoUser", method = RequestMethod.GET)
	public DeferredResult<?> getMongoUserbyName(HttpServletRequest request) {
		DeferredResult<ResponseEntity<?>> deferredResult = new DeferredResult<>();
		String name = request.getParameter("name");

		userService
				.findMongoUserByName(name)
				.subscribe(
						list -> {
							ResponseEntity<List<MongoUser>> responseEntity = new ResponseEntity<List<MongoUser>>(
									list, HttpStatus.OK);
							deferredResult.setResult(responseEntity);
						},
						error -> {
							ResponseEntity<String> resEntity = new ResponseEntity<String>(
									error.getMessage(),
									HttpStatus.INTERNAL_SERVER_ERROR);
							deferredResult.setResult(resEntity);
						});

		return deferredResult;
	}

	// MySQL
	@RequestMapping(value = "/get", method = RequestMethod.GET)
	public DeferredResult<?> getUser(HttpServletRequest request) {
		DeferredResult<ResponseEntity<?>> deferredResult = new DeferredResult<>();
		String id = request.getParameter("id");
		userService
				.getById(new Integer(id))
				.subscribe(
						user -> {
							ResponseEntity<User> resEntity = new ResponseEntity<User>(
									user, HttpStatus.OK);
							deferredResult.setResult(resEntity);
						},
						error -> {
							ResponseEntity<String> resEntity = new ResponseEntity<String>(
									error.getMessage(),
									HttpStatus.INTERNAL_SERVER_ERROR);
							deferredResult.setResult(resEntity);
						});

		return deferredResult;
	}

	@RequestMapping(value = "/getAll", method = RequestMethod.GET)
	public DeferredResult<?> getAllUser() {
		DeferredResult<ResponseEntity<?>> deferredResult = new DeferredResult<>();

		userService
				.getAll()
				.subscribe(
						list -> {
							ResponseEntity<List<User>> resEntity = new ResponseEntity<>(
									list, HttpStatus.OK);
							deferredResult.setResult(resEntity);
						},
						error -> {
							ResponseEntity<String> resEntity = new ResponseEntity<String>(
									error.getMessage(),
									HttpStatus.INTERNAL_SERVER_ERROR);
							deferredResult.setResult(resEntity);
						});

		return deferredResult;
	}

	@RequestMapping(value = "/getByName", method = RequestMethod.GET)
	public DeferredResult<?> getByName(HttpServletRequest request, ModelMap map) {
		DeferredResult<ResponseEntity<?>> deferredResult = new DeferredResult<>();
		String name = request.getParameter("name");
		// 做长连接异步请求处理

		userService
				.findByName(name)
				.subscribe(
						user -> {
							ResponseEntity<User> resEntity = new ResponseEntity<User>(
									user, HttpStatus.OK);
							deferredResult.setResult(resEntity);
						},
						error -> {
							ResponseEntity<String> resEntity = new ResponseEntity<String>(
									error.getMessage(),
									HttpStatus.INTERNAL_SERVER_ERROR);
							deferredResult.setResult(resEntity);
						});

		return deferredResult;
	}

	// 保存用户信息
	@RequestMapping(value = "/saveUser", method = RequestMethod.GET)
	public DeferredResult<?> saveUser(HttpServletRequest request) {
		DeferredResult<ResponseEntity<?>> deferredResult = new DeferredResult<>();
		String id = request.getParameter("id");
		String name = request.getParameter("name");
		String age = request.getParameter("age");
		String pass = request.getParameter("password");
		pass = encoder.encode(pass);
		User user = new User(name, new Integer(age));

		user.setPassword(pass);
		if (id != null) {
			user.setId(new Integer(id));
		}

		userService
				.saveUser(user)
				.subscribe(
						u -> {
							ResponseEntity<User> resEntity = new ResponseEntity<User>(
									u, HttpStatus.OK);
							deferredResult.setResult(resEntity);
						},
						error -> {
							ResponseEntity<String> resEntity = new ResponseEntity<String>(
									error.getMessage(),
									HttpStatus.INTERNAL_SERVER_ERROR);
							deferredResult.setResult(resEntity);
						});

		return deferredResult;
	}
}
