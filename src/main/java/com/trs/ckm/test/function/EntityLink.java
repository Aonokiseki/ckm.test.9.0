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
import com.trs.ckm.api.pojo.EntityLinkResult;
import com.trs.ckm.test.aspect.AspectConfig;
import com.trs.ckm.util.FileOperator;
import com.trs.ckm.util.Other;

public class EntityLink {
	private AnnotationConfigApplicationContext context;
	private TRSCkmRequest request;
	private final static Map<String,String> READ_OPTIONS = new HashMap<String,String>();
	static {
		READ_OPTIONS.put("not.append.lineseparator", "true");
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
	
	@DataProvider(name = "entityLinkDataProvider")
	public Object[][] entityLinkDataProvider(Method method){
		if(!"entityLink".equals(method.getName()))
			return null;
		return new Object[][] {
			new Object[] {1, Constant.TEST_DATA_DIR + "/entityLink/1.txt", "习近平", 
					"1", "操作成功", "Human", Constant.EXPECTED_DATA_DIR + "/entityLink/1.txt"},
			new Object[] {2, Constant.TEST_DATA_DIR + "/entityLink/2.txt", "黄河", 
					"1", "操作成功", "Place", Constant.EXPECTED_DATA_DIR + "/entityLink/2.txt"},
			new Object[] {3, Constant.TEST_DATA_DIR + "/entityLink/3.txt", "总统",
					"1", "操作成功", "TVPlay", Constant.EXPECTED_DATA_DIR + "/entityLink/3.txt"},
			new Object[] {4, Constant.TEST_DATA_DIR + "/entityLink/4.txt", "中国",
					"1", "操作成功", "Thing", Constant.EXPECTED_DATA_DIR + "/entityLink/4.txt"},
			new Object[] {5, Constant.TEST_DATA_DIR + "/entityLink/5.txt", "作家",
					"1", "操作成功", "Vocabulary", Constant.EXPECTED_DATA_DIR + "/entityLink/5.txt"},
		};
	}
	
	/**
	  *  实体链接 中文<br>
	  *  识别文档中的实体, 和后台知识库的实体映射
	 * @param caseId
	 * @param desc
	 * @param entity
	 * @param code
	 * @param message
	 * @param expectedEntity
	 * @param expectedDesc
	 */
	@Test(dataProvider = "entityLinkDataProvider")
	public void entityLink(int caseId, String path, String entity, 
			String code, String message, String expectedEntity, String expectedDescPath) {
		try {
			String desc = FileOperator.read(path);
			EntityLinkResult entityLink = request.entityLink(desc, entity);
			assertEquals(entityLink.getCode(), code);
			assertEquals(entityLink.getMessage(), message);
			assertEquals(entityLink.getResult().getName(), expectedEntity);
			String expectedDesc = FileOperator.read(expectedDescPath, "UTF-8", READ_OPTIONS);
			assertEquals(entityLink.getResult().getDesc().trim(), expectedDesc.trim());
		}catch(Exception e) {
			fail(Other.stackTraceToString(e));
		}
	}
	
	@DataProvider(name = "entityLinkErrDataProvider")
	public Object[][] entityLinkErrDataProvider(Method method){
		if(!"entityLinkErr".equals(method.getName()))
			return null;
		return new Object[][] {
			 //实体和实体句子为英文
			new Object[] {1,"On June, special envoy of President Xi Jinping", "Xi Jinping","0"},
			//实体为空
			new Object[] {2,"On June, special envoy of President Xi Jinping", "","0"},
			//实体句子为空
			new Object[] {3,"", "习近平","0"},		
		};
	}
	
	@Test(dataProvider = "entityLinkErrDataProvider")
	public void entityLinkErr(int caseId, String desc, String entity, String code) {
		try {
			EntityLinkResult entityLink = request.entityLink(desc, entity);
			assertEquals(entityLink.getCode(), code);
		}catch(Exception e) {
			fail(Other.stackTraceToString(e));
		}
	}
	
	
}
