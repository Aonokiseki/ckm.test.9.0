package com.trs.ckm.test.function;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
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
import com.trs.ckm.api.pojo.NerResult;
import com.trs.ckm.test.aspect.AspectConfig;
import com.trs.ckm.util.FileOperator;
import com.trs.ckm.util.Other;

public class Ner {
	private final static Logger LOGGER = LogManager.getLogger(Ner.class);
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
	/**
	 *    同时给ner()以及nerLang()提供参数<br/>
	 *    给ner接口提供参数时, 语言名作为预期; 给nerLang提供参数时, 语言名作为接口参数传入
	 * @param method
	 * @return
	 */
	@DataProvider(name = "nerDataProvider")
	private Object[][] nerDataProvider(Method method){
		if(!"ner".equals(method.getName()) && !"nerLang".equals(method.getName()))
			return null;
		return new Object[][] {
			/* 英语 */
			new Object[] {1, Constant.TEST_DATA_DIR + "/ner/1.txt", "English"},
			new Object[] {2, Constant.TEST_DATA_DIR + "/ner/2.txt", "English"},
			new Object[] {3, Constant.TEST_DATA_DIR + "/ner/3.txt", "English"},
			/* 法语 */
			new Object[] {4, Constant.TEST_DATA_DIR + "/ner/4.txt", "FRENCH"},
			new Object[] {5, Constant.TEST_DATA_DIR + "/ner/5.txt", "FRENCH"},
			new Object[] {6, Constant.TEST_DATA_DIR + "/ner/6.txt", "FRENCH"},
			/* 德语 */
			new Object[] {7, Constant.TEST_DATA_DIR + "/ner/7.txt", "GERMAN"},
			new Object[] {8, Constant.TEST_DATA_DIR + "/ner/8.txt", "GERMAN"},
			new Object[] {9, Constant.TEST_DATA_DIR + "/ner/9.txt", "GERMAN"},
			/* 越南语 */
			new Object[] {10, Constant.TEST_DATA_DIR + "/ner/10.txt", "VIETNAMESE"},
			new Object[] {11, Constant.TEST_DATA_DIR + "/ner/11.txt", "VIETNAMESE"},
			new Object[] {12, Constant.TEST_DATA_DIR + "/ner/12.txt", "VIETNAMESE"},
			/* 西班牙语 */
			new Object[] {13, Constant.TEST_DATA_DIR + "/ner/13.txt", "SPANISH"},
			new Object[] {14, Constant.TEST_DATA_DIR + "/ner/14.txt", "SPANISH"},
			new Object[] {15, Constant.TEST_DATA_DIR + "/ner/15.txt", "SPANISH"},
			/* 土耳其语 */
			new Object[] {16, Constant.TEST_DATA_DIR + "/ner/16.txt", "TURKISH"},
			new Object[] {17, Constant.TEST_DATA_DIR + "/ner/17.txt", "TURKISH"},
			new Object[] {18, Constant.TEST_DATA_DIR + "/ner/18.txt", "TURKISH"},
			/* 乌尔都语 */
			new Object[] {19, Constant.TEST_DATA_DIR + "/ner/19.txt", "URDU"},
			new Object[] {20, Constant.TEST_DATA_DIR + "/ner/20.txt", "URDU"},
			new Object[] {21, Constant.TEST_DATA_DIR + "/ner/21.txt", "URDU"},
			/* 荷兰语 */
			new Object[] {22, Constant.TEST_DATA_DIR + "/ner/22.txt", "DUTCH"},
			new Object[] {23, Constant.TEST_DATA_DIR + "/ner/23.txt", "DUTCH"},
			new Object[] {24, Constant.TEST_DATA_DIR + "/ner/24.txt", "DUTCH"},
		};
	}
	
	@Test(dataProvider = "nerDataProvider")
	private void ner(int caseId, String path, String expectedLang) {
		LOGGER.debug("ner, caseId="+caseId);
		try {
			String text = FileOperator.read(path);
			NerResult ner = request.ner(text);
			assertEquals(ner.getCode(), "1");
			assertEquals(ner.getMessage(), "操作成功");
			assertEquals(ner.getLanguage(), expectedLang);
			assertFalse(ner.getMap().isEmpty());
		}catch(Exception e) {
			fail(Other.stackTraceToString(e));
		}
	}
	
	@DataProvider(name = "nerErrDataProvider")
	private Object[][] nerErrDataProvider(Method method){
		if(!"nerErr".equals(method.getName()))
			return null;
		return new Object[][] {
			new Object[] {1, Constant.TEST_DATA_DIR + "/nerErr/1.txt", "0"},
			new Object[] {2, Constant.TEST_DATA_DIR + "/nerErr/2.txt", "0"},
			new Object[] {3, Constant.TEST_DATA_DIR + "/nerErr/empty.txt", "0"},
		};
	}
	
	@Test(dataProvider = "nerErrDataProvider")
	private void nerErr(int caseId, String path, String code) {
		LOGGER.debug("nerErr, caseId="+caseId);
		try {
			String text = FileOperator.read(path);
			NerResult ner = request.ner(text);
			assertEquals(ner.getCode(), code);
		}catch(Exception e) {
			fail(Other.stackTraceToString(e));
		}
	}
	
	@Test(dataProvider = "nerDataProvider")
	private void nerLang(int caseId, String path, String lang) {
		LOGGER.debug("nerLang, caseId="+caseId);
		try {
			String text = FileOperator.read(path);
			NerResult ner = request.ner_lang(text, lang);
			assertEquals(ner.getCode(), "1");
			assertEquals(ner.getMessage(), "操作成功");
			assertFalse(ner.getMap().isEmpty());
		}catch(Exception e) {
			fail(Other.stackTraceToString(e));
		}
	}
	
	@DataProvider(name = "nerLangErrDataProvider")
	private Object[][] nerLangErrDataProvider(Method method){
		if(!"nerLangErr".equals(method.getName()))
			return null;
		return new Object[][] {
			 //语言为中文，文本为中文
			new Object[] {1,"提升公共卫生合作。中方将在澜湄合作专项基金框架下设立公共卫生专项资金，"
					+ "在力所能及范围内提供物资和技术支持。中方新冠疫苗研制完成并投入使用后，将优先向湄公河国家提供。"
					+ "开展重大突发公共卫生事件信息通报和联合处置。共同支持世界卫生组织更好发挥作用。",
					"Chinese","0"},
			//语言为法语，文本为中文，有返回结果
			new Object[] {2,"商业理事会",
					"FRENCH","1"},
			//语言为英语，文本为混合文本，有返回结果
			new Object[] {3,"商业理事会Xi Jinping, special envoy of President Alecken Eminbahe Standing Committee",
					"English","1"},
			//文本为空
			new Object[] {4,"",
					"English","0"},
			//语言为空，默认语言
			new Object[] {5,"Xi Jinping, special envoy of President Alecken Eminbahe Standing Committee",
					"","1"},
		};
	}
	
	@Test(dataProvider = "nerLangErrDataProvider")
	private void nerLangErr(int caseId, String text, String lang, String code) {
		LOGGER.debug("nerLangErr, caseId="+caseId);
		try {
			NerResult nerLang = request.ner_lang(text, lang);
			assertEquals(nerLang.getCode(), code);
		}catch(Exception e) {
			fail(Other.stackTraceToString(e));
		}
	}
}
