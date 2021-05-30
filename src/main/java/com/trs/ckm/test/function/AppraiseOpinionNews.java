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
import com.trs.ckm.api.pojo.AppraiseOpinionResult;
import com.trs.ckm.api.pojo.AppraiseOpinionResult.Result;
import com.trs.ckm.test.aspect.AspectConfig;
import com.trs.ckm.util.FileOperator;
import com.trs.ckm.util.Other;

public class AppraiseOpinionNews {
	private AnnotationConfigApplicationContext context;
	private TRSCkmRequest request;
	private final static Logger LOGGER = LogManager.getLogger();
	
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
	@DataProvider(name = "appraiseOpinionNewsDataProvider")
	private Object[][] appraiseOpinionNewsDataProvider(Method method){
		if(!"appraiseOpinionNews".equals(method.getName()))
			return null;
		return new Object[][] {
			new Object[] {1, Constant.TEST_DATA_DIR + "/appraiseOpinionNews/1.txt",
					"config.ini", "1", "操作成功", Constant.EXPECTED_DATA_DIR + "/appraiseOpinionNews/1.txt",
					new String[] {"pos"}
			},
			new Object[] {2, Constant.TEST_DATA_DIR + "/appraiseOpinionNews/2.txt",
					"config.ini", "1", "操作成功", Constant.EXPECTED_DATA_DIR + "/appraiseOpinionNews/2.txt",
					new String[] {"neu", "neg"}
			},
			new Object[] {3, Constant.TEST_DATA_DIR + "/appraiseOpinionNews/3.txt",
					"config.ini", "1", "操作成功", Constant.EXPECTED_DATA_DIR + "/appraiseOpinionNews/3.txt",
					new String[] {"neu"}
			},
			new Object[] {4, Constant.TEST_DATA_DIR + "/appraiseOpinionNews/4.txt",
					"config.ini", "1", "操作成功", Constant.EXPECTED_DATA_DIR + "/appraiseOpinionNews/4.txt",
					new String[] {"pos", "pos", "pos"}
			},
			/* 2020.10.16: 结果还有争议,等待开发团队回复 */
			/* 2020.11.09: 算法对依存关系更加严格, 目前只有一句话(对应上版结果的第二句)能被抽取出来, 性质也变为中性 */
			new Object[] {5, Constant.TEST_DATA_DIR + "/appraiseOpinionNews/5.txt",
					"config.ini", "1", "操作成功", Constant.EXPECTED_DATA_DIR + "/appraiseOpinionNews/5.txt",
					new String[] {"neu"}
			},
			/* 除了不指定配置文件外, 和case1完全一致, 检查默认值 */
			new Object[] {6, Constant.TEST_DATA_DIR + "/appraiseOpinionNews/1.txt",
					"", "1", "操作成功", Constant.EXPECTED_DATA_DIR + "/appraiseOpinionNews/1.txt",
					new String[] {"pos"}
			},
		};
	}
	/**
	 * 中文观点褒贬判断 - 对新闻数据、提取主体、客体、观点以及观点情感
	 * @param caseId
	 * @param path
	 * @param config 服务器端指定的配置文件名, 不指定时为config.ini
	 * @param code 
	 * @param message
	 * @param descriptions 预期人物观点
	 * @param polarities 预期返回的情感倾向
	 */
	@Test(dataProvider = "appraiseOpinionNewsDataProvider")
	private void appraiseOpinionNews(int caseId, String path, String config, 
			String code, String message, String desPath, String[] polarities) {
		LOGGER.debug("appraiseOpinionNews, caseId="+caseId);
		try {
			String text = FileOperator.read(path);
			AppraiseOpinionResult appraiseOpinionNew = request.appraiseOpinionNews(text, config);
			assertEquals(appraiseOpinionNew.getCode(), code);
			assertEquals(appraiseOpinionNew.getMessage(), message);
			List<Result> results = appraiseOpinionNew.getResult();
			Result result = null;
			String[] descriptions = FileOperator.read(desPath).split(System.lineSeparator());
			assertEquals(results.size(), descriptions.length);
			for(int i=0, size=results.size(); i<size; i++) {
				result = results.get(i);
//				System.out.println(String.format("result.des=%s"+System.lineSeparator()+
//						"result.polarity=%s"+System.lineSeparator(), 
//						result.getDes(), result.getPolarity()));
				assertEquals(result.getDes(), descriptions[i]);
				assertEquals(result.getPolarity(), polarities[i]);
			}
		}catch(Exception e) {
			fail(Other.stackTraceToString(e));
		}
	}
	@DataProvider(name = "appraiseOpinionErrDataProvider")
	private Object[][] appraiseOpinionErrDataProvider(Method method){
		if(!"appraiseOpinionErr".equals(method.getName()))
			return null;
		return new Object[][] {
			/* 配置文件错误 */
			new Object[] {1, Constant.TEST_DATA_DIR + "/appraiseOpinionErr/2.txt", "config1.txt", "0"},
			/* 外文 */
			new Object[] {2, Constant.TEST_DATA_DIR + "/appraiseOpinionErr/3.txt", "config.ini", "-1"},
			/* 混合文本, 可以正常抽取 */
			new Object[] {3, Constant.TEST_DATA_DIR + "/appraiseOpinionErr/4.txt", "config.ini", "1"},
			/* 短文本, 不含人物和观点 */
			new Object[] {4, Constant.TEST_DATA_DIR + "/appraiseOpinionErr/5.txt", "config.ini", "-1"},
			/* 空文本 */
			new Object[] {5, Constant.TEST_DATA_DIR + "/appraiseOpinionErr/empty.txt", "config.ini", "0"},
			
		};
	}
	@Test(dataProvider = "appraiseOpinionErrDataProvider")
	private void appraiseOpinionErr(int caseId, String path, String config, String code) {
		LOGGER.debug("appraiseOpinionErr, caseId="+caseId);
		try {
			String text = FileOperator.read(path);
			AppraiseOpinionResult appraiseOpinionErr = request.appraiseOpinionNews(text, config);
			assertEquals(appraiseOpinionErr.getCode(), code);
		}catch(Exception e) {
			fail(Other.stackTraceToString(e));
		}
	}
	
}
