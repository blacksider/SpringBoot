package com.test.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import rx.Observable;
import rx.schedulers.Schedulers;

import com.test.dao.MongoUserDao;
import com.test.dao.UserDao;
import com.test.vo.MongoUser;
import com.test.vo.User;

@Service
public class UserService {
	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private UserDao userDao;

	@Autowired
	private MongoUserDao mongoUserDao;

	public Observable<MongoUser> saveMongoUser(MongoUser mongoUser) {
		Observable<MongoUser> ob = Observable.create(observer -> {
			try {
				MongoUser saved = mongoUserDao.save(mongoUser);
				observer.onNext(saved);
				observer.onCompleted();
			} catch (Exception e) {
				e.printStackTrace();
				observer.onError(e);
			}
		});

		return ob.subscribeOn(Schedulers.io());
	}

	public Observable<List<MongoUser>> findMongoUserByName(String name) {
		Observable<List<MongoUser>> ob = Observable.create(observer -> {
			try {
				List<MongoUser> list = mongoUserDao.findByName(name);
				observer.onNext(list);
				observer.onCompleted();
			} catch (Exception e) {
				e.printStackTrace();
				observer.onError(e);
			}
		});

		return ob.subscribeOn(Schedulers.io());
	}

	// MySQL
	public Observable<User> getById(Integer id) {
		Observable<User> ob = Observable.create(observer -> {
			try {
				User user = userDao.findById(id);
				observer.onNext(user);
				observer.onCompleted();
			} catch (Exception e) {
				e.printStackTrace();
				observer.onError(e);
			}
		});

		return ob.subscribeOn(Schedulers.io());
	}

	public Observable<List<User>> getAll() {
		Observable<List<User>> ob = Observable.create(observer -> {
			try {
				List<User> list = (List<User>) userDao.findAll();
				observer.onNext(list);
				observer.onCompleted();
			} catch (Exception e) {
				e.printStackTrace();
				observer.onError(e);
			}
		});

		return ob.subscribeOn(Schedulers.io());
	}

	public Observable<User> findByName(String name) {
		Observable<User> ob = Observable.create(observer -> {
			try {
				User user = userDao.findByName(name);
				observer.onNext(user);
				observer.onCompleted();
			} catch (Exception e) {
				e.printStackTrace();
				observer.onError(e);
			}
		});

		return ob.subscribeOn(Schedulers.io());
	}

	// 保存用户信息
	public Observable<User> saveUser(User user) {
		Observable<User> ob = Observable.create(observer -> {
			try {
				User saved = userDao.save(user);
				observer.onNext(saved);
				observer.onCompleted();
			} catch (Exception e) {
				e.printStackTrace();
				observer.onError(e);
			}
		});

		return ob.subscribeOn(Schedulers.io());
	}

	// 验证用户信息
	public Observable<Authentication> authenticate(
			UsernamePasswordAuthenticationToken token) {

		Observable<Authentication> ob = Observable
				.create(observer -> {
					try {
						Authentication auth = authenticationManager
								.authenticate(token);
						observer.onNext(auth);
						observer.onCompleted();
					} catch (Exception e) {
						e.printStackTrace();
						observer.onError(e);
					}
				});

		return ob.subscribeOn(Schedulers.io());

	}
}
