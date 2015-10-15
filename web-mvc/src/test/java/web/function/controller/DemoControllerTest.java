/*
 * COPYRIGHT Beijing NetQin-Tech Co.,Ltd.                                   *
 ****************************************************************************
 * 源文件名:  web.function.controller.DemoControllerWebAppContextSetupTest.java 													       
 * 功能: cpframework框架													   
 * 版本:	@version 1.0	                                                                   
 * 编制日期: 2014年9月1日 上午10:34:31 						    						                                        
 * 修改历史: (主要历史变动原因及说明)		
 * YYYY-MM-DD |    Author      |	 Change Description		      
 * 2014年9月1日    |    Administrator     |     Created 
 */
package web.function.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.xpath;

import java.util.Date;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MvcResult;

import web.core.AbsWebAppContextSetupTest;
import web.function.model.oracle.Demo;

/**
 * Description: <类功能描述>. <br>
 * <p>
 * <使用说明>
 * </p>
 * Makedate:2014年9月1日 上午10:34:31
 * 
 * @author Administrator
 * @version V1.0
 */

public class DemoControllerTest extends AbsWebAppContextSetupTest{

	

	@Test
	public void testView() throws Exception {
		mockMvc.perform(get("/demo/findall"))
				.andExpect(view().name("demo"))
				.andExpect(forwardedUrl("/WEB-INF/views/jsp/function/demo.jsp"))
				.andExpect(model().attributeExists("results"))
				.andExpect(status().isOk()).andDo(print());
	}
	
	@Test
	public void testXml() throws Exception{
		 mockMvc.perform(post("/demo/xml/write/1.xml")
	                .accept(MediaType.APPLICATION_XML)) //执行请求
	                .andDo(print())
	                .andExpect(content().contentType(MediaType.APPLICATION_XML+";charset=UTF-8")) //验证响应contentType
	                .andExpect(xpath("/demo/age").string("20")); //使用XPath表达式验证XML 

	}
	
	@Test
	public void testJson() throws Exception{
		mockMvc.perform(post("/demo/json/write/1.json")
				.accept(MediaType.APPLICATION_JSON)) //执行请求
				.andDo(print())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON+";charset=UTF-8")) //验证响应contentType  application/json;charset=UTF-8
				.andExpect(jsonPath("$.age").value(20)); //使用XPath表达式验证XML 
		
	}
	
	
	@Test
//	@Rollback(false)
	public void testSave() throws Exception {
		
		MvcResult result = 	mockMvc.perform(post("/demo/save").param("age", "39").param("name", "炭烧咖啡").param("createDate", "2014-09-01").param("modifyDate", "2014-09-02"))
				.andExpect(view().name("demo"))
				.andExpect(forwardedUrl("/WEB-INF/views/jsp/function/demo.jsp"))
				.andExpect(model().attributeExists("id"))
				.andExpect(model().attributeExists("demo"))
				.andExpect(status().isOk()).andDo(print()).andReturn();
		Assert.assertNotNull(result.getModelAndView().getModel().get("demo"));
	}
}
