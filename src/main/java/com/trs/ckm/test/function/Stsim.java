package com.trs.ckm.test.function;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;
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
import com.trs.ckm.api.pojo.StsimResult;
import com.trs.ckm.test.aspect.AspectConfig;
import com.trs.ckm.util.Other;

public class Stsim {
	private final static Logger LOGGER = LogManager.getLogger(Stsim.class);
	private AnnotationConfigApplicationContext context;
	private TRSCkmRequest request;
	
	/**
	 * 从上下文中获取 <code>com.trs.ckm.api.TRSCkmRequest</code> 的bean,<br>
	 * 定位 log4j2.xml 的路径并重新加载配置
	 */
	@BeforeClass
	public void beforeClass() {
		context = new AnnotationConfigApplicationContext(AspectConfig.class);
		Constant.reconfigureLog4j2();
		request = context.getBean(TRSCkmRequest.class);
	}
	
	@AfterClass
	public void afterClass() {
		if(context != null)
			context.close();
	}
	@DataProvider(name = "stsimDataProvider")
	public Object[][] stsimDataProvider(Method method){
		if(!"stsim".equals(method.getName()))
			return null;
		return new Object[][] {
			/* 
			 * 关于 “中华人民共和国” 和 “中华” 分值一样的解释: 是基于知网语义词典给出的结果, 有一个义项编码是完全一样的
			 * 
			 * 中国 2388;2381;117;4597;1266;
			 * 中国 4472;3024;4597;1266;
			 * 中华 2595;4597;1304;
			 * 中华 4472;3024;4597;1304;
			 * 中华人民共和国 4472;3024;4597;1304; 
			 * 
			 * 不过现在考究这些也没有必要了, 检查标准改为结果非空就行
			 */
			new Object[] {1, "CN", "中华人民共和国", "中华"},
			new Object[] {2, "CN", "中华人民共和国", "中华人民共和国"},
			new Object[] {3, "CN", "财税金融", "纸醉金迷"},
			new Object[] {4, "CN", "年年岁岁花相似", "岁岁年年人不同"},
			/* 其它语种模型因为体积过大, 没有放到标准介质中, 因此运行下方用例一定会failure */
			/* 2020.10.19, 既然这样, 那就暂时注释以下两条case 
			new Object[] {5, "EN", "english", "english"},
			new Object[] {6, "JP", "ありがとう", "ありがとう"}
			*/
		};
	}
	/**
	 * 短文本相似度排序
	 * @param caseId
	 * @param lang 语言模型
	 * @param text1 待对比文本串
	 * @param text2 待对比文本串数组
	 */
	@Test(dataProvider = "stsimDataProvider")
	public void stsim(int caseId, String lang, String text1, String text2) {
		LOGGER.debug("stsim, caseId="+caseId);
		/* 2020.8.31: 只要不是所有文本都返回0, 就认为没问题 */
		StsimResult stsim;
		try {
			stsim = request.stsim(lang, text1, text2);
			assertEquals(stsim.getCode(), "1");
			double value = Double.valueOf(stsim.getResult());
			assertTrue(0.0 <= value && value <= 1.0);
			/* 两个文本严格一致, 分值必须是1.0; 当然, 分值为1.0的, 可不一定两个文本严格一致 */
			if(text1.equals(text2))
				assertEquals(value, 1.0);
		}catch(Exception e) {
			fail(Other.stackTraceToString(e));
		}
	}
	
	@DataProvider(name = "stsimErrDataProvider")
	public Object[][] stsimErrDataProvider(Method method){
		if(!"stsimErr".equals(method.getName()))
			return null;
		return new Object[][] {
			/* 空参数 */
			new Object[] {1, "CN", "", "", "输入参数为空！", null},
			/* 输入一个不存在的语言模型, 返回相关度就是0.0 */
			new Object[] {2, "ABC", "中华人民共和国", "中华人民共和国", "操作成功", "0.0"},
		};
	}
	
	/**
	 * 短文本相似度查询, 异常情况
	 * @param caseId
	 * @param lang
	 * @param text1
	 * @param text2
	 * @param message
	 */
	@Test(dataProvider = "stsimErrDataProvider")
	public void stsimErr(int caseId, String lang, String text1, String text2, String message, String result) {
		LOGGER.debug("stsimErr, caseId="+caseId);
		try {
			StsimResult stsim = request.stsim(lang, text1, text2);
			assertEquals(stsim.getMessage(), message);
			if("操作成功".equals(stsim.getMessage()))
				assertEquals(stsim.getResult(), result);
		} catch (Exception e) {
			assertEquals(e.getMessage(), message);
		}
	}
}
