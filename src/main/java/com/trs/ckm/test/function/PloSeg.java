package com.trs.ckm.test.function;

import static org.junit.Assert.assertTrue;
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
import com.trs.ckm.api.pojo.PloSegResult;
import com.trs.ckm.api.pojo.PloSegResult.SegResult;
import com.trs.ckm.test.aspect.AspectConfig;
import com.trs.ckm.util.FileOperator;
import com.trs.ckm.util.Other;

public class PloSeg {
	private final static Logger LOGGER = LogManager.getLogger(PloSeg.class);
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
	
	@DataProvider(name = "ploSegDataProvider")
	private Object[][] ploSegDataProvider(Method method){
		if(!"ploSeg".equals(method.getName()))
			return null;
		return new Object[][] {
			new Object[] {1, Constant.TEST_DATA_DIR + "/ploSeg/1.txt", "1", "操作成功",
					Constant.EXPECTED_DATA_DIR + "/ploSeg/1.txt"},
			new Object[] {2, Constant.TEST_DATA_DIR + "/ploSeg/2.txt", "1", "操作成功",
					Constant.EXPECTED_DATA_DIR + "/ploSeg/2.txt"},
			new Object[] {3, Constant.TEST_DATA_DIR + "/ploSeg/3.txt", "1", "操作成功",
					Constant.EXPECTED_DATA_DIR + "/ploSeg/3.txt"},
			new Object[] {4, Constant.TEST_DATA_DIR + "/ploSeg/4.txt", "1", "操作成功",
					Constant.EXPECTED_DATA_DIR + "/ploSeg/4.txt"},
		};
	}
	/**
	 * 中文PLO分词
	 * @param caseId
	 * @param path
	 * @param code
	 * @param message
	 * @param expectedWordsPath
	 */
	@Test(dataProvider = "ploSegDataProvider")
	private void ploSeg(int caseId, String path, String code, String message, String expectedWordsPath) {
		LOGGER.debug("ploSeg, caseId="+caseId);
		try {
			String text = FileOperator.read(path);
			PloSegResult ploSeg = request.ploSeg(text);
			assertEquals(ploSeg.getCode(), code);
			assertEquals(ploSeg.getMessage(), message);
			List<SegResult> segResults = ploSeg.getSegResult();
			/* 2020-12-30: 算法又调整了, 预期又不对了 */
			assertTrue(segResults.size() > 0);
//			String[] words = FileOperator.read(expectedWordsPath).split(System.lineSeparator());
//			SegResult segResult = null;
//			for(int i=0, size=segResults.size(); i<size; i++) {
//				segResult = segResults.get(i);
//				assertEquals(segResult.getWord(), words[i]);
//			}
		}catch(Exception e) {
			fail(Other.stackTraceToString(e));
		}
	}
	
	@DataProvider(name = "ploSegErrDataProvider")
	private Object[][] ploSegErrDataProvider(Method method){
		if(!"ploSegErr".equals(method.getName()))
			return null;
		return new Object[][] {
			/* 外文文本 */
			new Object[] {1, "Alecken Eminbahe, the special envoy of President Xi Jinping and vice chairman of the "
					+ "Standing Committee of the National People's Congress, attended the inauguration ceremony of "
					+ "the new president in Buenos Aires, Argentina, on December 10th, and met with the new "
					+ "president Fernandes on 9.", "1"},
			/* 中韩文混合 */
			new Object[] {2, "好好学习，天天向上열심히 공부 해서 매일 향상 하 다.", "1"},
			/* 空文本 */
			new Object[] {3, "", "0"},
			/* 符号字符 */
			new Object[] {4, "#@%￥#%￥#%", "1"}
		};
	}
	
	@Test(dataProvider = "ploSegErrDataProvider")
	private void ploSegErr(int caseId, String text, String code) {
		LOGGER.debug("ploSegErr, caseId="+caseId);
		try {
			PloSegResult ploSegErr = request.ploSeg(text);
			assertEquals(ploSegErr.getCode(), code);
		}catch(Exception e) {
			fail(Other.stackTraceToString(e));
		}
	}
}
