package com.trs.ckm.test.function;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.fail;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.trs.ckm.api.master.TRSCkmRequest;
import com.trs.ckm.api.pojo.AboutModulesResult;
import com.trs.ckm.api.pojo.AboutResult;
import com.trs.ckm.test.aspect.AspectConfig;
import com.trs.ckm.util.Other;

public class About {
	private AnnotationConfigApplicationContext context = null;
	private TRSCkmRequest request;
	
	@BeforeClass
	public void before() {
		context = new AnnotationConfigApplicationContext(AspectConfig.class);
		request = context.getBean(TRSCkmRequest.class);
	}
	
	@AfterClass
	public void after() {
		if(context != null)
			context.close();
	}
	/**
	 * 获取版本号
	 */
	@Test
	public void about() {
		try {
			AboutResult about = request.about();
			assertEquals(about.getCode(), Constant.SUCCESS);
		}catch(Exception e) {
			fail(Other.stackTraceToString(e));
		}
	}
	/**
	 * 获取已启用功能
	 */
	@Test
	public void aboutModules() {
		try {
			AboutModulesResult modules = request.aboutModules();
			assertEquals(modules.getCode(), Constant.SUCCESS);
		}catch(Exception e) {
			fail(Other.stackTraceToString(e));
		}
	}
}
