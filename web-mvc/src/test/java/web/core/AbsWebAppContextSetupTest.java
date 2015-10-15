/*
 * COPYRIGHT Beijing NetQin-Tech Co.,Ltd.                                   *
 ****************************************************************************
 * 源文件名:  web.function.controller.AbsWebAppContextSetupTest.java 													       
 * 功能: cpframework框架													   
 * 版本:	@version 1.0	                                                                   
 * 编制日期: 2014年9月1日 下午3:34:18 						    						                                        
 * 修改历史: (主要历史变动原因及说明)		
 * YYYY-MM-DD |    Author      |	 Change Description		      
 * 2014年9月1日    |    Administrator     |     Created 
 */
package web.core;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextHierarchy;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import web.config.AppConfig;
import web.config.MvcConfig;

/** 
 *Description: <测试抽象类>. <br>
 *<p>
	<1.使用xml风格测试时，需要先将/src/main/java/web/config下的全部类移动到项目外，并开启web.xml中的相关配置>
	<2.使用Bean风格测试时，需要保证/src/main/java/web/config下的全部类都存在，并关闭web.xml中的相关配置>
 </p>
 *Makedate:2014年9月1日 下午3:34:18 
 * @author Administrator  
 * @version V1.0                             
 */


//XML风格
//@RunWith(SpringJUnit4ClassRunner.class)
//@WebAppConfiguration(value = "src/main/webapp")
//@ContextHierarchy({
//      @ContextConfiguration(name = "parent", locations = "classpath:config/context/applicationContext-AppConfig.xml"),
//      @ContextConfiguration(name = "child", locations = "classpath:config/context/applicationContext-MvcConfig.xml")
//})


//注解风格
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration(value = "src/main/webapp")
@ContextHierarchy({
		@ContextConfiguration(name = "parent", classes = AppConfig.class),
		@ContextConfiguration(name = "child", classes = MvcConfig.class) })
@TransactionConfiguration(transactionManager="transactionManager",defaultRollback=true) 
@Transactional 
public abstract class AbsWebAppContextSetupTest {
	@Autowired
	private WebApplicationContext wac;
	
	public MockMvc mockMvc;

	@Before
	public void setUp() {
		mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
	}
}


