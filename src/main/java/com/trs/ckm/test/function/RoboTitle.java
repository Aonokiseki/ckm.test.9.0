package com.trs.ckm.test.function;

import static org.junit.Assert.assertTrue;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.fail;

import java.lang.reflect.Method;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.trs.ckm.api.master.TRSCkmRequest;
import com.trs.ckm.api.pojo.RoboTitleResult;
import com.trs.ckm.test.aspect.AspectConfig;
import com.trs.ckm.util.FileOperator;
import com.trs.ckm.util.Other;

public class RoboTitle {
	private final static Logger LOGGER = LogManager.getLogger(RoboTitle.class);
	private AnnotationConfigApplicationContext context;
	private TRSCkmRequest request;
	
	@BeforeClass
	public void beforeClass() {
		Constant.reconfigureLog4j2();
		context = new AnnotationConfigApplicationContext(AspectConfig.class);
		request = context.getBean(TRSCkmRequest.class);
	}
	
	@AfterClass
	public void afterClass() {
		if(context != null)
			context.close();
	}
	
	@DataProvider(name = "roboTitleDataProvider")
	public Object[][] roboTitleDataProvider(Method method){
		if(!"roboTitle".equals(method.getName()))
			return null;
		return new Object[][] {
			new Object[] {1, Constant.TEST_DATA_DIR + "/roboTitle/1.txt", 
					Constant.SUCCESS, Constant.SUCCESS_MSG},
			/* 2020-12-18: 开发组回复说这些测试文本是有“可能”返回结果为空的; 因此case2我决定隐藏, 但文本还是留下来 */
//			new Object[] {2, Constant.TEST_DATA_DIR + "/roboTitle/2.txt", 
//					Constant.SUCCESS, Constant.SUCCESS_MSG, "习近平会见奥巴马"},
			new Object[] {3, Constant.TEST_DATA_DIR + "/roboTitle/3.txt", 
					Constant.SUCCESS, Constant.SUCCESS_MSG},
			new Object[] {4, Constant.TEST_DATA_DIR + "/roboTitle/4.txt", 
					Constant.SUCCESS, Constant.SUCCESS_MSG},
			/* 带有换行符, 以前会导致进程崩溃, 放到这里检查*/
			new Object[] {5, Constant.TEST_DATA_DIR + "/roboTitle/5.txt", 
					Constant.SUCCESS, Constant.SUCCESS_MSG},
			new Object[] {6, Constant.TEST_DATA_DIR + "/roboTitle/6.txt", 
					Constant.SUCCESS, Constant.SUCCESS_MSG},
			new Object[] {7, Constant.TEST_DATA_DIR + "/roboTitle/7.txt",
					Constant.SUCCESS, Constant.SUCCESS_MSG}
		};
	}
	/*
	 * robo/title(机器短标题生成), 这个接口一般和聚类接口搭配使用, 给某一类提供一个标题
	 * 像测试工程这样直接给短句或文本的用法比较少见。再加上内部算法微调, 是有可能出现
	 * “更新前的测试数据是一个结果, 更新后又是另一个结果”的情况, 对此我决定放弃检查短标题的内容
	 * 就检查短标题长度大于0 
	 */
	@Test(dataProvider = "roboTitleDataProvider")
	public void roboTitle(int caseId, String path, String code, String message) {
		LOGGER.debug("roboTitle, caseId="+caseId);
		try {
			String text = FileOperator.read(path);
			RoboTitleResult roboTitle = request.roboTitle(text);
			assertEquals(roboTitle.getCode(), code);
			assertEquals(roboTitle.getMessage(), message);
			if(Constant.SUCCESS.equals(roboTitle.getCode()))
				assertTrue(roboTitle.getResult().length() > 0);
		}catch(Exception e) {
			fail(Other.stackTraceToString(e));
		}
	}
	
	@DataProvider(name = "roboTitleErrDataProvider")
	public Object[][] roboTitleErrDataProvider(Method method){
		if(!"roboTitleErr".equals(method.getName()))
			return null;
		return new Object[][] {
			new Object[] {3, Constant.TEST_DATA_DIR + "/roboTitleErr/empty.txt", Constant.FAILURE},
			new Object[] {4, Constant.TEST_DATA_DIR + "/roboTitleErr/symbol.txt", Constant.EMPTY}
		};
	}
	
	@Test(dataProvider = "roboTitleErrDataProvider")
	public void roboTitleErr(int caseId, String path, String code) {
		LOGGER.debug("roboTitleErr, caseId="+caseId);
		try {
			String text = FileOperator.read(path);
			RoboTitleResult result = request.roboTitle(text);
			assertEquals(result.getCode(), code);
		}catch(Exception e) {
			fail(Other.stackTraceToString(e));
		}
	}
}
