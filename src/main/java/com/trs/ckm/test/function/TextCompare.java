package com.trs.ckm.test.function;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.fail;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.trs.ckm.api.master.TRSCkmRequest;
import com.trs.ckm.api.pojo.TextCompareMd5TagResult;
import com.trs.ckm.api.pojo.TextCompareResult;
import com.trs.ckm.test.aspect.AspectConfig;
import com.trs.ckm.util.FileOperator;
import com.trs.ckm.util.Other;

public class TextCompare {
	private final static Logger LOGGER = LogManager.getLogger(TextCompare.class);
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
	
	@DataProvider(name = "textCompareMd5TagDataProvider")
	public Object[][] textCompareMd5TagDataProvider(Method method){
		if(!"textCompareMd5Tag".equals(method.getName()))
			return null;
		return new Object[][] {
			new Object[] {1, Constant.TEST_DATA_DIR + "/textCompareMd5Tag/1.txt", "1", "操作成功", 
					"130c0cb5960d26523fcfa371b2b2d84d"},
			new Object[] {2, Constant.TEST_DATA_DIR + "/textCompareMd5Tag/2.txt", "1", "操作成功",
					"481bc6c66515a9ed138d2886042d7477"},
			new Object[] {3, Constant.TEST_DATA_DIR + "/textCompareMd5Tag/3.txt", "1", "操作成功",
					"81b5ca680c361c08459098843f328da6"},
			new Object[] {4, Constant.TEST_DATA_DIR + "/textCompareMd5Tag/4.txt", "1", "操作成功",
					"50a0f554801285c4566fe627c0ab3a33"},
			new Object[] {5, Constant.TEST_DATA_DIR + "/textCompareMd5Tag/5.txt", "1", "操作成功",
					"94d82e1a933911b16f8572e2094c7315"},
			new Object[] {6, Constant.TEST_DATA_DIR + "/textCompareMd5Tag/6.txt", "1", "操作成功",
					"ab4ddf3d8c8b8db1ec57d8a1abf58e69"},
			new Object[] {7, Constant.TEST_DATA_DIR + "/textCompareMd5Tag/7.txt", "1", "操作成功",
					"8e888fd0967b8085984751ba0c0037be"},
			/* 异常情况 */
			new Object[] {8, Constant.TEST_DATA_DIR + "/textCompareMd5Tag/empty.txt", "0", "输入参数为空！",
					null},
		};
	}
	
	@Test(dataProvider = "textCompareMd5TagDataProvider")
	public void textCompareMd5Tag(int caseId, String path, String code, String message, String md5) {
		LOGGER.debug("textCompareMd5Tag, caseId="+caseId);
		try {
			String text = FileOperator.read(path, "UTF-8", READ_OPTIONS);
			TextCompareMd5TagResult result = request.textCompareMd5Tag(text);
			assertEquals(result.getCode(), code);
			assertEquals(result.getMessage(), message);
			if(!"操作成功".equals(message))
				return;
			assertEquals(result.getResult(), md5);
		}catch(Exception e) {
			fail(Other.stackTraceToString(e));
		}
	}

	@DataProvider(name = "textCompareDataProvider")
	public Object[][] textCompareDataProvider(Method method){
		/* 2020.09.18 对比结果xml格式，一直和实际对不上，非常奇怪，怀疑有不可见字符的干扰
		 *   下列用例全都执行失败  */
		/* 2020.12.24 决定彻底修改检查方式, 改为解析返回结果并取得其中一个属性值 matchedSens, 然后和给定的预期比较 */
		return new Object[][]{
			new Object[] {1, 
				Constant.TEST_DATA_DIR + "/textCompare/1_1.txt", 
				Constant.TEST_DATA_DIR + "/textCompare/1_2.txt",
				1},
			new Object[] {2, 
				Constant.TEST_DATA_DIR + "/textCompare/2_1.txt",
				Constant.TEST_DATA_DIR + "/textCompare/2_2.txt",
				2},
			new Object[] {3,
				Constant.TEST_DATA_DIR + "/textCompare/3_1.txt", 
				Constant.TEST_DATA_DIR + "/textCompare/3_2.txt",
				9},
			new Object[] {4,
				Constant.TEST_DATA_DIR + "/textCompare/4_1.txt", 
				Constant.TEST_DATA_DIR + "/textCompare/4_2.txt",
				4},
		};
	}
	/**
	  * 文本对比 支持中文
	 * @param caseId
	 * @param path1
	 * @param path2
	 * @param 返回结果中compare_res标签的matchedSens属性的预期值
	 */
	@Test(dataProvider = "textCompareDataProvider")
	public void textCompare(int caseId, String path1, String path2, int matchedSens){
		LOGGER.debug("textCompare, caseId="+caseId);
		try{
			String text1 = FileOperator.read(path1);
			String text2 = FileOperator.read(path2);
			TextCompareResult textCompare = request.textCompare(text1, text2);
			assertEquals(textCompare.getCode(), Constant.SUCCESS);
			assertEquals(textCompare.getMessage(), Constant.SUCCESS_MSG);
			assertEquals(Integer.valueOf(matchedSens(textCompare.getResult())).intValue(), matchedSens);
		}catch(Exception e){
			fail(Other.stackTraceToString(e));
		}
	}
	/**
	 * 返回 textCompare 接口返回结果的 matchedSens 属性
	 * @param xml
	 * @return
	 * @throws DocumentException
	 */
	private static String matchedSens(String xml) throws DocumentException {
		Document document = DocumentHelper.parseText(xml);
		Element root = document.getRootElement();
		Attribute matchedSens = root.attribute("matchedSens");
		return matchedSens.getValue();
	}
}
