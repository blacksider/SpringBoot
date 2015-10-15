/*
 * COPYRIGHT Beijing NetQin-Tech Co.,Ltd.                                   *
 ****************************************************************************
 * 源文件名:  web.function.controller.DemoControllerRestTemplateTest.java 													       
 * 功能: cpframework框架													   
 * 版本:	@version 1.0	                                                                   
 * 编制日期: 2014年9月2日 下午3:21:17 						    						                                        
 * 修改历史: (主要历史变动原因及说明)		
 * YYYY-MM-DD |    Author      |	 Change Description		      
 * 2014年9月2日    |    Administrator     |     Created 
 */
package web.function.controller;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.xml.Jaxb2RootElementHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import web.function.model.oracle.Demo;

/**
 * Description: <RestTemplate测试>. <br>
 * <p>
 * <需要启动服务器后进行测试，远程测试>
 * </p>
 * Makedate:2014年9月2日 下午3:21:17
 * 
 * @author Administrator
 * @version V1.0
 */
public class DemoControllerRestTemplateTest 
//extends AbsRestTemplateJettyTest
{
	static RestTemplate restTemplate;

	@Before
	public void before() throws Exception {

		restTemplate = new RestTemplate();

	}

	@Test
	public void testJsonSave() throws Exception {
		Demo demo = new Demo();
		demo.setAge(45);
		demo.setName("RestTemplateJson");
		demo.setCreateDate(new Date());
		demo.setModifyDate(new Date());

		restTemplate
				.setMessageConverters(Arrays
						.<HttpMessageConverter<?>> asList(new MappingJackson2HttpMessageConverter()));
		// 返回页面视图的内容
		ResponseEntity<Demo> responseEntity = restTemplate.postForEntity(
				"http://localhost:8080/webmvc/demorest/save", demo, Demo.class);

		assertEquals(demo.getName(), responseEntity.getBody().getName());

	}
	
	@Test
	public void testXmlSave() throws Exception {
		Demo demo = new Demo();
		demo.setAge(45);
		demo.setName("RestTemplateXml");
		demo.setCreateDate(new Date());
		demo.setModifyDate(new Date());
		
		restTemplate
		.setMessageConverters(Arrays
				.<HttpMessageConverter<?>> asList(new Jaxb2RootElementHttpMessageConverter()));
		// 返回页面视图的内容
		ResponseEntity<Demo> responseEntity = restTemplate.postForEntity(
				"http://localhost:8080/webmvc/demorest/savexml", demo, Demo.class);
		
		assertEquals(demo.getName(), responseEntity.getBody().getName());
		
	}
}
