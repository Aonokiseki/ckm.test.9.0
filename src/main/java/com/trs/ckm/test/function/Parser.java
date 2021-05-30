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
import com.trs.ckm.api.pojo.ParserEngResult;
import com.trs.ckm.api.pojo.ParserEngResult.KeyWord;
import com.trs.ckm.test.aspect.AspectConfig;
import com.trs.ckm.util.FileOperator;
import com.trs.ckm.util.Other;

public class Parser {
	private final static Logger LOGGER = LogManager.getLogger(Parser.class);
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
	
	@DataProvider(name = "parserEngDataProvider")
	private Object[][] parserEngDataProvider(Method method) {
		return new Object[][] {
			/* seg为1 */
			new Object[] {1, Constant.TEST_DATA_DIR + "/parserEng/1.txt", "1", new String[] {"r"}},
			new Object[] {2, Constant.TEST_DATA_DIR + "/parserEng/2.txt", "1", 
					new String[] {"p","nz",",","a","n","v","a","p","r","n",",","c","p","a","n","c",
							"n",",","EX","v","r","a","n","p","n","rr","u","d","v","r","n",".","p",
							"nz",",","r","n","u","v","n","TO","r","n","r","n",",","v","TO","r","n",
							"n","TO","v","r","n","r","u","d","v","r","n","p","v",",","c","r","n","c",
							"n","u","v","p","r","a","."}},
			/* seg为0 指定词性是什么就返回什么 */
			new Object[] {3, Constant.TEST_DATA_DIR + "/parserEng/3.txt", "0", 
					new String[] {"a","a","a","c","d","d","d","e","m","n","n","nz","nz","p","r","r","r","r","r"}},
			new Object[] {4, Constant.TEST_DATA_DIR + "/parserEng/4.txt", "0",
					new String[] {"u","v","v","v","v","v","v","w","rr"}},
			new Object[] {5, Constant.TEST_DATA_DIR + "/parserEng/5.txt", "0",
					new String[] {"FW","LS","PDT","POS","TO"}},
			/* seg为空 */
			new Object[] {6, Constant.TEST_DATA_DIR + "/parserEng/6.txt", "",
					new String[] {"p","r","n","p","r","a","n",",","nz","nz","d","v","n",",","a",
							"n",",","c","a","c","a","n","p","m","n","p","n","p","r","a","n"}}
		};
	}
	
	/**
	 * 英文句法分析
	 * @param caseId
	 * @param path
	 * @param seg
	 * @param postags 预期词性列表
	 */
	@Test(dataProvider = "parserEngDataProvider")
	private void parserEng(int caseId, String path, String seg, String[] postags) {
		LOGGER.debug("parserEng, caseId="+caseId);
		try {
			String text = FileOperator.read(path);
			ParserEngResult parserEng = request.parserEng(text, seg);
			assertEquals(parserEng.getCode(), "1");
			assertEquals(parserEng.getMessage(), "操作成功");
			List<KeyWord> keywords = parserEng.getKeywords();
			assertEquals(keywords.size(), postags.length);
			for(int i=0,size=keywords.size(); i<size; i++)
				assertEquals(keywords.get(i).getPostag(), postags[i]);
		}catch(Exception e) {
			fail(Other.stackTraceToString(e));
		}
	}
	
	@DataProvider(name = "parserEngErrDataProvider")
	private Object[][] parserEngErrDataProvider(Method method){
		return new Object[][] {
			//seg为2，默认为1，有返回结果
			new Object[] {1,"On June, special envoy of President Xi Jinping","2","1"},
		    //文本为中文，有返回结果
		    new Object[] {2,"提升公共卫生合作。中方将在澜湄合作专项基金框架下设立公共卫生专项资金，"
		   		+ "在力所能及范围内提供物资和技术支持。中方新冠疫苗研制完成并投入使用后，将优先向湄公河国家提供。"
		   		+ "开展重大突发公共卫生事件信息通报和联合处置。共同支持世界卫生组织更好发挥作用。", "1","1"},
		    //文本为混合文本，有返回结果
		    new Object[] {3,"提升公共卫生合作。中方将在澜湄合作专项基金框架下设立公共卫生专项资金，"
		   		+ "在力所能及范围内提供物资和技术支持。中方新冠疫苗研制完成并投入使用后，将优先向湄公河国家提供。"
		   		+ "开展重大突发公共卫生事件信息通报和联合处置。共同支持世界卫生组织更好发挥作用。"
		   		+ "On June, special envoy of President Xi Jinping", "1","1"},	
		    //文本为空
		    new Object[] {4,"","1","0"},
		};
	}
	
	@Test(dataProvider = "parserEngErrDataProvider")
	private void parserEngErr(int caseId, String text, String seg, String code) {
		LOGGER.debug("parserEngErr, caseId="+caseId);
		try {
			ParserEngResult parserEng = request.parserEng(text, seg);
			assertEquals(parserEng.getCode(), code);
		}catch(Exception e) {
			fail(Other.stackTraceToString(e));
		}
	}
}
