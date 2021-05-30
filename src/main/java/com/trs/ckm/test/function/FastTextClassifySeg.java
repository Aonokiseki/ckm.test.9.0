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
import com.trs.ckm.api.pojo.FastTextClassifySegResult;
import com.trs.ckm.api.pojo.FastTextClassifySegResult.Result;
import com.trs.ckm.test.aspect.AspectConfig;
import com.trs.ckm.util.FileOperator;
import com.trs.ckm.util.Other;

public class FastTextClassifySeg {
	private final static Logger LOGGER = LogManager.getLogger(FastTextClassifySeg.class);
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
	
	@DataProvider(name = "fastTextClassifySegDataProvider")
	private Object[][] fastTextClassifySegDataProvider(Method method){
		return new Object[][] {
			//demo中文模板,limit为1,2，为空，5,8,10,20，30
			new Object[] {1, Constant.TEST_DATA_DIR + "/fastTextClassifySeg/1_2.txt", "1", "demo", 1},
			new Object[] {2, Constant.TEST_DATA_DIR + "/fastTextClassifySeg/1_2.txt", "2", "demo", 2},
			new Object[] {3, Constant.TEST_DATA_DIR + "/fastTextClassifySeg/3_4.txt", "", "demo", 3},
			new Object[] {4, Constant.TEST_DATA_DIR + "/fastTextClassifySeg/3_4.txt", "5", "demo", 5},
			new Object[] {5, Constant.TEST_DATA_DIR + "/fastTextClassifySeg/5_6.txt", "8", "demo", 8},
			new Object[] {6, Constant.TEST_DATA_DIR + "/fastTextClassifySeg/5_6.txt", "10", "demo", 10},
			new Object[] {7, Constant.TEST_DATA_DIR + "/fastTextClassifySeg/7_8.txt", "20", "demo", 20},
			new Object[] {8, Constant.TEST_DATA_DIR + "/fastTextClassifySeg/7_8.txt", "30", "demo", 20},
			//newsclassify_en - 英文新闻分类
			/* 2021-02-02: 临发布前已经去掉了这个模板 */
//			new Object[] {9, Constant.TEST_DATA_DIR + "/fastTextClassifySeg/9_10.txt", "3", "newsclassify_en", 3},
//			new Object[] {10, Constant.TEST_DATA_DIR + "/fastTextClassifySeg/9_10.txt", "4", "newsclassify_en", 4},
//			new Object[] {11, Constant.TEST_DATA_DIR + "/fastTextClassifySeg/11_12.txt", "6", "newsclassify_en", 6},
//			new Object[] {12, Constant.TEST_DATA_DIR + "/fastTextClassifySeg/11_12.txt", "7", "newsclassify_en", 7},
//			new Object[] {13, Constant.TEST_DATA_DIR + "/fastTextClassifySeg/13_14.txt", "12", "newsclassify_en", 12},
//			new Object[] {14, Constant.TEST_DATA_DIR + "/fastTextClassifySeg/13_14.txt", "17", "newsclassify_en", 17},
//			new Object[] {15, Constant.TEST_DATA_DIR + "/fastTextClassifySeg/15_16.txt", "50", "newsclassify_en", 17},
//			new Object[] {16, Constant.TEST_DATA_DIR + "/fastTextClassifySeg/15_16.txt", "100", "newsclassify_en", 17},
		};
	}
	/**
	 * fastTextClassifySeg
	 * @param caseId
	 * @param path
	 * @param limit
	 * @param model 模板
	 * @param size 这个值等同于limit的值
	 */
	@Test(dataProvider = "fastTextClassifySegDataProvider")
	private void fastTextClassifySeg(int caseId, String path, String limit, String model, int size) {
		LOGGER.debug("fastTextClassifySet, caseId="+caseId);
		try {
			String text = FileOperator.read(path);
			FastTextClassifySegResult fastTextClassifySeg = request.fastTextClassifySeg(text, limit, model);
			assertEquals(fastTextClassifySeg.getCode(), "1");
			assertEquals(fastTextClassifySeg.getMessage(), "操作成功");
			List<Result> results = fastTextClassifySeg.getResults();
			assertEquals(results.size(), size);
		}catch(Exception e) {
			fail(Other.stackTraceToString(e));
		}
	}
	
	@DataProvider(name = "fastTextClassifySegErrDataProvider")
	private Object[][] fastTextClassifySegErrDataProvider(Method method) {
		return new Object[][] {
			//模板为空
			new Object[] {1,"好好学习", "1","","0"},
			//模板为不存在模板
			new Object[] {2,"好好学习", "2","demo1","0"},
			//中文模板，阿拉伯语文本，有返回结果
			new Object[] {3," المبعوث الخاص للرئيس ", "2","demo","1"},
			//中文模板，混合文本，有返回结果
			new Object[] {4," المبعوث الخاص للرئيس习近平主席特使 ", "2","demo","1"},
			//文本为空
			new Object[] {5,"", "2","demo","0"}
		};
	}
	/**
	  *   异常情况
	 * @param caseId
	 * @param text
	 * @param limit
	 * @param model
	 * @param code
	 */
	@Test(dataProvider = "fastTextClassifySegErrDataProvider")
	private void fastTextClassifySegErr(int caseId, String text, String limit, String model, String code) {
		LOGGER.debug("fastTextClassifySegErr, caseId="+caseId);
		try {
			FastTextClassifySegResult fastTextClassifySeg = request.fastTextClassifySeg(text, limit, model);
			assertEquals(fastTextClassifySeg.getCode(), code);
		}catch(Exception e) {
			fail(Other.stackTraceToString(e));
		}
	}
}
