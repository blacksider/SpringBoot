package com.test;

import java.util.Iterator;

import org.junit.Test;

import rx.Observable;
import rx.Observable.OnSubscribe;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.Subscriber;

public class MainController {
	private MainService mainService = new MainService();

	@Test
	public void test1() {
		// 创建一个事件源
		Observable<String> observable = Observable
				.create(new OnSubscribe<String>() {

					@Override
					public void call(Subscriber<? super String> t) {
						t.onNext("hello1");
						t.onCompleted();
					}
				});
		// 创建一个观察者
		Subscriber<String> subscriber = new Subscriber<String>() {

			@Override
			public void onNext(String t) {
				System.out.println(t);
			}

			@Override
			public void onError(Throwable e) {

			}

			@Override
			public void onCompleted() {

			}
		};
		// subscriber订阅observable,observable就是调用subscriber对象的onNext和onComplete方法
		observable.subscribe(subscriber);
	}

	@Test
	public void test2() {
		Observable<String> observable = Observable.just("hello2");
		Action1<String> action1 = new Action1<String>() {

			@Override
			public void call(String t) {
				System.out.println(t);
			}
		};
		observable.subscribe(action1);

		// -----------------------------
		Observable.just("hello2.1").subscribe(new Action1<String>() {

			@Override
			public void call(String t) {
				System.out.println(t);
			}
		});

		// -----------------lambda表达式
		Observable.just("hello2.2").subscribe(s -> System.out.println(s));

	}

	@Test
	public void test3() {
		Observable.just("Hello3").map(new Func1<String, String>() {
			@Override
			public String call(String s) {
				return s + " -Dan";
			}
		}).subscribe(s -> System.out.println(s));

		// ------------------
		Observable.just("Hello3.1").map(s -> s + " -Dan")
				.subscribe(s -> System.out.println(s));

		// ----------------------
		Observable.just("Hello 3.2").map(new Func1<String, Integer>() {
			@Override
			public Integer call(String s) {
				return s.hashCode();
			}
		}).subscribe(i -> System.out.println(Integer.toString(i)));

		// ---------------------
		Observable.just("Hello 3.3").map(s -> s.hashCode())
				.map(i -> Integer.toString(i))
				.subscribe(s -> System.out.println(s));
	}

	@Test
	public void test4() {
		mainService.query("Hello 4.1").subscribe(
				list -> {
					for (Iterator<String> iterator = list.iterator(); iterator
							.hasNext();) {
						String string = iterator.next();
						System.out.println(string);
					}
				});

		// ---------------------
		mainService.query("Hello 4.2").subscribe(urls -> {
			Observable.from(urls).subscribe(url -> System.out.println(url));
		});

		// ----------------------
		mainService
				.query("Hello 4.3")
				// from 接收一个集合作为输入
				// flatmap接收一个Observable的输出作为输入，同时输出另外一个Observable,将多个独立的返回Observable对象的方法组合在一起
				.flatMap(urls -> Observable.from(urls))
				.flatMap(url -> mainService.changeUrl(url))
				// 过滤返回的数据流
				.filter(url -> url != null)
				// 输出最多指定数量的结果
				.take(3)
				// 在每次输出一个元素之前做一些额外的事情
				.doOnNext(url -> System.out.println("befor-->" + url))
				.subscribe(url -> System.out.println(url));

	}

}
