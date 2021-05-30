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
import com.trs.ckm.api.pojo.EventQuickResult;
import com.trs.ckm.test.aspect.AspectConfig;
import com.trs.ckm.util.FileOperator;
import com.trs.ckm.util.Other;

public class EventQuick {
	private AnnotationConfigApplicationContext context;
	private TRSCkmRequest request;
	private final static Logger LOGGER = LogManager.getLogger(LogManager.getLogger());
	
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
	
	@DataProvider(name = "eventQkDataProvider")
	public Object[][] eventQkDataProvider(Method method){
		if(!"eventQuick".equals(method.getName()))
			return null;
		return new Object[][] {
			/* 单句 */
			/* 2020.11.09: 这句话突然识别不出来, 提交到禅道 */
			new Object[] {1, "", Constant.TEST_DATA_DIR + "/eventQk/HUANENG.txt", 
					"1", "操作成功", 1},
			/* 篇级文本(CKM示例文本) */
			/* 2020-12-23: 开发组一直在优化算法, 结果有变化实属正常, 最容易出现变化的是resultSize */
			new Object[] {2, "", Constant.TEST_DATA_DIR + "/eventQk/demo.txt", 
					"1", "操作成功", 5},
			/* 空文本 */
			new Object[] {3, "", Constant.TEST_DATA_DIR + "/eventQk/empty.txt", 
					"0", "输入参数为空！", -1},
			/* 2020/08/28：意外发现的执行效果不佳的语句, 但是目前不知道正确结果应该是什么样的, 暂时先不做判断, 在此处先做个标记 */
			/* 2020/10/19: 效果问题不好解决, 开发团队在上一轮已经回复延期处理 */
			new Object[] {4, "", Constant.TEST_DATA_DIR + "/eventQk/MH370.txt", 
					"1", "", 1},
			/* 纯符号串 */
			new Object[] {5, "", Constant.TEST_DATA_DIR + "/eventQk/symbol.txt", 
					"-1", "结果为空！", -1}
		};
		
	}
	
	/**
	 * 事件快速抽取<br>
	 * P.S. 需要检查的内容有点儿多, 无法以硬编码方式写在dataProvider里, 改为将预期保存为json文件,<br>
	 * 运行结果和json文件解析后的内存模型做对比<br>
	 * <br>
	 * 2020.10.19: 写好json直接对比检查复杂,效果不如人工检查, 而且一旦变动不好调整<br>
	 * 简化检查点
	 * @param caseid
	 * @param baseDate 基准日期
	 * @param filePath 源文件路径
	 * @param jsonPath 通过json文件保存的预期结果
	 */
	@Test(dataProvider = "eventQkDataProvider")
	public void eventQuick(int caseid, String baseDate, String filePath,
			String code, String message, int resultSize) {
		LOGGER.info("eventQuick, caseId=="+caseid);
		try {
			String sentences = FileOperator.read(filePath);
			EventQuickResult eventQk = request.eventQk(baseDate, sentences);
			assertEquals(eventQk.getCode(), code);
			assertEquals(eventQk.getMessage(), message);
			if(!"操作成功".equals(eventQk.getMessage()))
				return;
			List<EventQuickResult.Result> actualResults =  eventQk.getResult();
			for(int i=0, size=actualResults.size(); i<size; i++) {
				LOGGER.debug(String.format("actualResults.get(%d).getDesc()=%s", 
						i, actualResults.get(i).getDesc()));
			}
			assertEquals(actualResults.size(), resultSize);
		} catch (Exception e) {
			fail(Other.stackTraceToString(e));
		}
	}
}
