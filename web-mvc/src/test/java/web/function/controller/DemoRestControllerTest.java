/*
 * COPYRIGHT Beijing NetQin-Tech Co.,Ltd.                                   *
 ****************************************************************************
 * 源文件名:  web.function.controller.DemoRestControllerTest.java 													       
 * 功能: cpframework框架													   
 * 版本:	@version 1.0	                                                                   
 * 编制日期: 2014年9月1日 下午3:58:40 						    						                                        
 * 修改历史: (主要历史变动原因及说明)		
 * YYYY-MM-DD |    Author      |	 Change Description		      
 * 2014年9月1日    |    Administrator     |     Created 
 */
package web.function.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.xpath;

import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;

import web.core.AbsWebAppContextSetupTest;

/** 
 *Description: <类功能描述>. <br>
 *<p>
	<使用说明>
 </p>
 *Makedate:2014年9月1日 下午3:58:40 
 * @author Administrator  
 * @version V1.0                             
 */
public class DemoRestControllerTest extends AbsWebAppContextSetupTest{
	
	
	@Test
    public void getByIdTest() throws Exception {
       
        mockMvc.perform(get("/demorest/1")
                .accept(MediaType.APPLICATION_JSON)) //执行请求
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON+";charset=UTF-8")) //验证响应contentType
                .andExpect(jsonPath("$.age").value(20)); //使用Json path验证JSON 

    }

	@Test
//	@Rollback(false)
    public void saveJsonTest() throws Exception {
        //JSON请求/响应
//      String requestBody = "{\"name\":\"王五\",\"age\":30,\"createDate\":1408377600000,\"modifyDate\":1409068800000}";
		//因为使用了属性拦截器对日期字符串yyyy-MM-dd自动转换为date类型
        String requestBody = "{\"name\":\"王五\",\"age\":30,\"createDate\":\"2014-08-21\",\"modifyDate\":\"2014-08-30\"}";
        mockMvc.perform(post("/demorest/save")
                .contentType(MediaType.APPLICATION_JSON).content(requestBody)
                .accept(MediaType.APPLICATION_JSON)) //执行请求
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON+";charset=UTF-8")) //验证响应contentType
                .andExpect(jsonPath("$.age").value(30)); //使用Json path验证JSON 

    }
	@Test
//	@Rollback(false)
	public void saveXmlTest() throws Exception {
		//JSON请求/响应
//		String requestBody = "<demo><age>40</age><createDate>2014-08-19T00:00:00+08:00</createDate><modifyDate>2014-08-27T00:00:00+08:00</modifyDate><name>赵四</name></demo>";
		//因为使用了属性拦截器对日期字符串yyyy-MM-dd自动转换为date类型
		String requestBody = "<demo><age>40</age><createDate>2014-08-20</createDate><modifyDate>2014-08-28</modifyDate><name>赵四</name></demo>";
		mockMvc.perform(post("/demorest/save")
				.contentType(MediaType.APPLICATION_XML).content(requestBody)
				.accept(MediaType.APPLICATION_XML)) //执行请求
				.andDo(print())
				.andExpect(content().contentType(MediaType.APPLICATION_XML+";charset=UTF-8")) //验证响应contentType
				.andExpect(xpath("/demo/age").string("40")); //使用Json path验证JSON 
		
	}
}


