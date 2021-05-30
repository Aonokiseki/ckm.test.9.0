package com.trs.ckm.test.function;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.fail;

import java.lang.reflect.Method;
import java.util.List;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.trs.ckm.api.master.TRSCkmRequest;
import com.trs.ckm.api.pojo.AutocatResult;
import com.trs.ckm.api.pojo.AutocatResult.Result;
import com.trs.ckm.test.aspect.AspectConfig;
import com.trs.ckm.util.FileOperator;
import com.trs.ckm.util.Other;

public class Autocat {
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
	
	@DataProvider(name = "autocatDataProvider")
	public Object[][] autocatDataProvider(Method method){
		if(!"autocat".equals(method.getName()))
			return null;
		return new Object[][] {
			/* modzb-中文新闻分类（二级分类，一级33个类），长文本，短文本，中长文本 */
			new Object[] {1, Constant.TEST_DATA_DIR + "/autocat/1.txt", "modzb",
					"1", "操作成功", new String[] {"医疗卫生\\疾病与治疗"},new String[] {"0.49"}},
			new Object[] {2, Constant.TEST_DATA_DIR + "/autocat/2.txt", "modzb",
					"1", "操作成功", new String[] {"金融\\货币"},new String[] {"0.49"}},
			new Object[] {3, Constant.TEST_DATA_DIR + "/autocat/3.txt", "modzb",
					"1", "操作成功", new String[] {"工业\\煤炭产业"},new String[] {"0.36"}},
			/* mod_en5-英文分类（5分类: business/ entertainment/politics/sport/tech），长文本，短文本，中长文本 */
			new Object[] {4, Constant.TEST_DATA_DIR + "/autocat/4.txt", "mod_en5",
					"1", "操作成功", new String[] {"sport","tech"},new String[] {"0.7","0.7"}},
			new Object[] {5, Constant.TEST_DATA_DIR + "/autocat/5.txt", "mod_en5",
					"1", "操作成功", new String[] {"entertainment"},new String[] {"0.7"}},
			new Object[] {6, Constant.TEST_DATA_DIR + "/autocat/6.txt", "mod_en5", 
					"1", "操作成功", new String[] {"business"},new String[] {"0.7"}},
			/* mod_de9-德文分类（9分类: Etat/ Inland/ International/ Kultur/ 
			 * Panorama/ Sport/ Web/  Wirtschaft/ Wissenschaft），长文本，短文本，中长文本 */
			new Object[] {7, Constant.TEST_DATA_DIR + "/autocat/7.txt", "mod_de9",
					"1", "操作成功", new String[] {"Inland","Panorama","Sport"},new String[] {"0.7","0.7","0.7"}},
			new Object[] {8, Constant.TEST_DATA_DIR + "/autocat/8.txt", "mod_de9",
					"1", "操作成功", new String[] {"Etat","Kultur"},new String[] {"0.7","0.7"}},
			new Object[] {9, Constant.TEST_DATA_DIR + "/autocat/9.txt", "mod_de9", 
					"1", "操作成功", new String[] {"Web"},new String[] {"0.4"}},
			new Object[] {10, Constant.TEST_DATA_DIR + "/autocat/10.txt", "mod_de9",
					"1", "操作成功", new String[] {"Wirtschaft"},new String[] {"0.7"}},
			new Object[] {11, Constant.TEST_DATA_DIR + "/autocat/11.txt", "mod_de9",
					"1", "操作成功", new String[] {"Inland","International","Panorama","Wirtschaft"},
					new String[] {"0.7","0.7","0.7","0.7"}}
		};
	}
	/**
	 * 自动分类测试
	 * @param caseId
	 * @param path
	 * @param model
	 * @param code
	 * @param message
	 * @param name
	 * @param value
	 */
	@Test(dataProvider = "autocatDataProvider")
	public void autocat(int caseId, String path, String model, 
			String code, String message, String[] name, String[] value) {
		try {
			String text = FileOperator.read(path);
			AutocatResult result = request.autocat(text, model);
			assertEquals(result.getCode(), code);
			assertEquals(result.getMessage(), message);
			List<Result> autos = result.getResults();
			Result auto;
			for(int i=0,size=autos.size(); i<size; i++) {
				auto = autos.get(i);
				assertEquals(auto.getName(), name[i]);
				assertEquals(auto.getValue(), value[i]);
			}
		}catch(Exception e) {
			fail(Other.stackTraceToString(e));
		}
	}
	
	@DataProvider(name = "autocatErrDataProvider")
	public Object[][] autocatErrDataProvider(Method method){
		if(!"autocatErr".equals(method.getName()))
			return null;
		return new Object[][] {
			/* modzb-中文新闻分类; 文本使用韩语 */
			new Object[] {1, Constant.TEST_DATA_DIR + "/autocatErr/1.txt", "modzb", "-1"},
			/* modzb-中文新闻分类; 文本使用中文韩文混合, 返回code=1 */
			new Object[] {2, Constant.TEST_DATA_DIR + "/autocatErr/2.txt", "modzb", "1"},
			/* mod_en5-英文分类; 文本使用阿拉伯语 */
			new Object[] {3, Constant.TEST_DATA_DIR + "/autocatErr/3.txt", "mod_en5", "-1"},
			/* mod_en5-英文分类:文本使用阿拉伯语和英文混合，code返回为1  */
			new Object[] {4, Constant.TEST_DATA_DIR + "/autocatErr/4.txt", "mod_en5", "1"},
			/* mod_de9-德文分类, 文本使用俄语 */
			new Object[] {5, Constant.TEST_DATA_DIR + "/autocatErr/5.txt", "mod_de9", "-1"},
			/* mod_de9-德文分类, 文本使用德育和中文混合 code返回1 */
			new Object[] {6, Constant.TEST_DATA_DIR + "/autocatErr/6.txt", "mod_de9", "1"},
			/* 文本为空 */
			new Object[] {7, Constant.TEST_DATA_DIR + "/autocatErr/7.txt", "mod_de9", "0"},
			/* 模板为空 */
			new Object[] {8, Constant.TEST_DATA_DIR + "/autocatErr/8.txt", "", "0"},
			/* 模板不存在 */
			new Object[] {9, Constant.TEST_DATA_DIR + "/autocatErr/9.txt", "mod", "0"},
		};
	}
	
	@Test(dataProvider = "autocatErrDataProvider")
	public void autocatErr(int caseId, String path, String model, String code) {
		try {
			String text = FileOperator.read(path);
			AutocatResult result = request.autocat(text, model);
			assertEquals(result.getCode(), code);
		}catch(Exception e) {
			fail(Other.stackTraceToString(e));
		}
	}
}
