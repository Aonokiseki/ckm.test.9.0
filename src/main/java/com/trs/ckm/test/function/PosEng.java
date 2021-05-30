package com.trs.ckm.test.function;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.fail;

import java.lang.reflect.Method;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.trs.ckm.api.master.TRSCkmRequest;
import com.trs.ckm.api.pojo.PosEngResult;
import com.trs.ckm.api.pojo.PosEngResult.SegResult;
import com.trs.ckm.test.aspect.AspectConfig;
import com.trs.ckm.util.FileOperator;
import com.trs.ckm.util.Other;

public class PosEng {
	private final static Logger LOGGER = LogManager.getLogger(PosEng.class);
	private AnnotationConfigApplicationContext context;
	private TRSCkmRequest request;
	
	@BeforeClass
	private void beforeClass() {
		Constant.reconfigureLog4j2();
		context = new AnnotationConfigApplicationContext(AspectConfig.class);
		request = context.getBean(TRSCkmRequest.class);
	}
	@AfterClass
	private void afterClass() {
		if(context != null)
			context.close();
	}
	@DataProvider(name = "posEngDataProvider")
	private Object[][] posEngDataProvider(Method method){
		if(!"posEng".equals(method.getName()))
			return null;
		return new Object[][] {
			new Object[] {1, Constant.TEST_DATA_DIR + "/posEng/1.txt", "1", "操作成功", 
					Constant.EXPECTED_DATA_DIR + "/posEng/1.txt"},
			new Object[] {2, Constant.TEST_DATA_DIR + "/posEng/2.txt", "1", "操作成功",
					Constant.EXPECTED_DATA_DIR + "/posEng/2.txt"},
			new Object[] {3, Constant.TEST_DATA_DIR + "/posEng/3.txt", "1", "操作成功",
					Constant.EXPECTED_DATA_DIR + "/posEng/3.txt"},
		};
	}
	/**
	 * 英文 带分句的英文词性标注
	 * @param caseId
	 * @param path
	 * @param code
	 * @param message
	 * @param wordsPath
	 */
	@Test(dataProvider = "posEngDataProvider")
	private void posEng(int caseId, String path, String code, String message, String wordsPath) {
		LOGGER.debug("posEng, caseId="+caseId);
		try {
			String text = FileOperator.read(path);
			PosEngResult posEng = request.posEng(text);
			assertEquals(posEng.getCode(), code);
			assertEquals(posEng.getMessage(), message);
			List<SegResult> segResults = posEng.getSegResult();
			String[] words = FileOperator.read(wordsPath).split(System.lineSeparator());
			SegResult segResult = null;
			for(int i=0,size=segResults.size(); i<size; i++) {
				segResult = segResults.get(i);
				assertEquals(segResult.getWord(), words[i]);
			}
		}catch(Exception e) {
			fail(Other.stackTraceToString(e));
		}
	}
	
	@DataProvider(name = "posEngErrDataProvider")
	private Object[][] posEngErrDataProvider(Method method){
		if(!"posEngErr".equals(method.getName()))
			return null;
		return new Object[][] {
			//文本为除英文文以外其他语言文本
			new Object[] {1,"好好学习，天天向上", "1"},
			//文本为中文韩文混合文本
			new Object[] {2,"好好学习，天天向上열심히 공부 해서 매일 향상 하 다.", "1"},
			//文本为空
			new Object[] {3,"", "0"},
			//文本为特殊字符
			new Object[] {4,"#@%￥#%￥#%", "1"},
		};
	}
	
	@Test(dataProvider = "posEngErrDataProvider")
	private void posEngErr(int caseId, String text, String code) {
		LOGGER.debug("posEngErr, caseId="+caseId);
		try {
			PosEngResult posEngErr = request.posEng(text);
			assertEquals(posEngErr.getCode(), code);
		}catch(Exception e) {
			fail(Other.stackTraceToString(e));
		}
	}
}
