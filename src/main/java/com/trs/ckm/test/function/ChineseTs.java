package com.trs.ckm.test.function;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.fail;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.trs.ckm.api.master.TRSCkmRequest;
import com.trs.ckm.api.pojo.ChineseS2tResult;
import com.trs.ckm.test.aspect.AspectConfig;
import com.trs.ckm.util.FileOperator;
import com.trs.ckm.util.Other;

public class ChineseTs {
	private AnnotationConfigApplicationContext context;
	private TRSCkmRequest request;
	private final static Map<String,String> READ_OPTIONS = new HashMap<String,String>();
	static {
		READ_OPTIONS.put("not.append.lineserparator", "true");
	}
	
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
	
	@DataProvider(name = "chineseTsDataProvider")
	public Object[][] chineseTsDataProvider(Method method){
		if(!"chineseTs".equals(method.getName()))
			return null;
		return new Object[][] {
			new Object[] {1, Constant.TEST_DATA_DIR + "/chineseTs/1.txt", "1", "操作成功",
					Constant.EXPECTED_DATA_DIR + "/chineseTs/1.txt"},
			new Object[] {2, Constant.TEST_DATA_DIR + "/chineseTs/2.txt", "1", "操作成功",
					Constant.EXPECTED_DATA_DIR + "/chineseTs/2.txt"},
			new Object[] {3, Constant.TEST_DATA_DIR + "/chineseTs/3.txt", "1", "操作成功",
					Constant.EXPECTED_DATA_DIR + "/chineseTs/3.txt"}
		};
	}
	/**
	 * 新闻数据繁体转简体
	 * @param caseId
	 * @param path 待转换文本存放路径
	 * @param code
	 * @param message
	 * @param expectedPath 预期文本存放路径
	 */
	@Test(dataProvider = "chineseTsDataProvider")
	public void chineseTs(int caseId, String path, String code, String message, String expectedPath) {
		try {
			String text = FileOperator.read(path);
			ChineseS2tResult s2t = request.chineseTs(text);
			assertEquals(s2t.getCode(), code);
			assertEquals(s2t.getMessage(), message);
			String expectation = FileOperator.read(expectedPath, "UTF-8", READ_OPTIONS);
			assertEquals(s2t.getResult().trim(), expectation.trim());
		}catch(Exception e) {
			fail(Other.stackTraceToString(e));
		}
	}
	
	@DataProvider(name = "chineseTsErrDataProvider")
	public Object[][] chineseTsErrDataProvider(Method method){
		if(!"chineseTsErr".equals(method.getName()))
			return null;
		return new Object[][] {
			/* 中文以外的任意文本 */
			new Object[] {1, "Chinese President Xi Jinping, also general secretary of the Communist Party of "
				+ "China Central Committee and chairman of the Central Military Commission, "
				+ "chairs a symposium on economic and social work in Beijing, capital of China, Aug. 24, 2020", "1"},
			/* 文本为特殊字符, 也有返回值, 底层调用trsbean的TRSUtils, 具体行为不清楚 */
			new Object[] {2, "!@#$%^&*()", "1"},
			/* 文本为空 */
			new Object[] {3, "", "0"},
		};
	}
	/**
	 * 异常情况
	 * @param caseId
	 * @param text
	 * @param code
	 */
	@Test(dataProvider = "chineseTsErrDataProvider")
	public void chineseTsErr(int caseId, String text, String code) {
		try {
			ChineseS2tResult s2t = request.chineseTs(text);
			assertEquals(s2t.getCode(), code);
		}catch(Exception e) {
			fail(Other.stackTraceToString(e));
		}
	}
}
