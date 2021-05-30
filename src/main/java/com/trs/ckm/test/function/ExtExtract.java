package com.trs.ckm.test.function;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.fail;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.trs.ckm.api.master.TRSCkmRequest;
import com.trs.ckm.api.pojo.ExtExtractResult;
import com.trs.ckm.api.pojo.ExtExtractResult.Slot;
import com.trs.ckm.test.aspect.AspectConfig;
import com.trs.ckm.util.FileOperator;
import com.trs.ckm.util.Other;

public class ExtExtract {
	private final static Logger LOGGER = LogManager.getLogger(ExtExtract.class);
	private AnnotationConfigApplicationContext context;
	private TRSCkmRequest request;
	private final static Map<String,String> READ_OPTIONS = new HashMap<String,String>();
	static {
		READ_OPTIONS.put("not.append.lineserparator", "false");
	}
	
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
	
	@DataProvider(name = "extExtractDataProvider")
	private Object[][] extExtractDataProvider(Method method){
		if(!"extExtract".equals(method.getName()))
			return null;
		return new Object[][] {
			/* 抽取论文 */
			new Object[] {1, Constant.TEST_DATA_DIR + "/extExtract/1.txt", "demo",
					Constant.EXPECTED_DATA_DIR + "/extExtract/lex/1.txt",
					Constant.EXPECTED_DATA_DIR + "/extExtract/trs/1.trs"},
			new Object[] {2, Constant.TEST_DATA_DIR + "/extExtract/2.txt", "demo",
					Constant.EXPECTED_DATA_DIR + "/extExtract/lex/2.txt",
					Constant.EXPECTED_DATA_DIR + "/extExtract/trs/2.trs"},
			/* 期刊, 请求返回结果含有不可见字符,这样断言永远不会通过, 待解决 */
			/* 2020.10.19: 解决不了, 就让它failure吧 */
			new Object[] {3, Constant.TEST_DATA_DIR + "/extExtract/3.txt", "demo",
					Constant.EXPECTED_DATA_DIR + "/extExtract/lex/3.txt",
					Constant.EXPECTED_DATA_DIR + "/extExtract/trs/3.trs"},
			new Object[] {4, Constant.TEST_DATA_DIR + "/extExtract/4.txt", "demo",
					Constant.EXPECTED_DATA_DIR + "/extExtract/lex/4.txt",
					Constant.EXPECTED_DATA_DIR + "/extExtract/trs/4.trs"},
		};
	}
	
	@Test(dataProvider = "extExtractDataProvider")
	private void extExtract(int caseId, String path, String model, 
			String lexPath, String trsPath) {
		LOGGER.debug("extExtract, caseId="+caseId);
		try {
			String text = FileOperator.read(path);
			ExtExtractResult extExtract =  request.extExtract(text, model);
			assertEquals(extExtract.getCode(), "1");
			assertEquals(extExtract.getMessage(), "操作成功");
			String[] lex = FileOperator.read(lexPath, "UTF-8", READ_OPTIONS).split(System.lineSeparator());
			List<Slot> slots = extExtract.getSlots();
			assertEquals(slots.size(), lex.length);
			for(int j=0,size=slots.size(); j<size; j++)
				assertEquals(slots.get(j).getLex(), lex[j]);
			String trs = FileOperator.read(trsPath, "UTF-8", READ_OPTIONS);
			assertEquals(extExtract.getTrsString(), trs);
		}catch(Exception e) {
			fail(Other.stackTraceToString(e));
		}
	}
	
	@DataProvider(name = "extExtractErrDataProvider")
	private Object[][] extExtractErrDataProvider(Method method){
		return new Object[][] {
			//抽取的文本不为论文或者期刊形式
			new Object[] {1,Constant.TEST_DATA_DIR + "/extExtractErr/1.txt", "demo","-1"},		
			//抽取的文本英文格式的期刊
			new Object[] {2,Constant.TEST_DATA_DIR + "/extExtractErr/2.txt", "demo","-1"},	
			//模板为不存在的模板
			new Object[] {3,Constant.TEST_DATA_DIR + "/extExtractErr/3.txt", "unknownModel","0"},	
			//模板为空
			new Object[] {4,Constant.TEST_DATA_DIR + "/extExtractErr/3.txt", "", "0"},	
			//文本为空
			new Object[] {5,Constant.TEST_DATA_DIR + "/extExtractErr/empty.txt","demo","0"},	
		};
	}
	
	@Test(dataProvider = "extExtractErrDataProvider")
	private void extExtractErr(int caseId, String path, String model, String code) {
		LOGGER.debug("extExtractErr, caseId="+caseId);
		try {
			String text = FileOperator.read(path);
			assertEquals(request.extExtract(text, model).getCode(), code);
		}catch(Exception e) {
			fail(Other.stackTraceToString(e));
		}
	}
}
