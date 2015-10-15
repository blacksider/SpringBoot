package com.test;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;

public class MainService {

	public Observable<List<String>> query(String text){
		Observable<List<String>> observable = Observable.create(observer -> {
			List<String> list = new ArrayList<>();
			list.add("Test1 " + text);
			list.add("Test2 " + text);
			observer.onNext(list);
			observer.onCompleted();
		});
		return observable;
	}

	public Observable<String> changeUrl(String url) {
		Observable<String> ob = Observable.create(observer -> {
			String rs = url + "changed";
			observer.onNext(rs);
			observer.onCompleted();
		});
		return ob;
	}
}
