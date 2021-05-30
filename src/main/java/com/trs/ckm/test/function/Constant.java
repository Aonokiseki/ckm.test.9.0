package com.trs.ckm.test.function;

import java.io.File;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;

public class Constant {
	/** log4j2.xml 默认路径 */
	public final static String DEFAULT_LOG4J2_XML_PATH = "./config/log4j2.xml";
	/** 稳定性测试模板的配置文件 */
	public final static String STABILITY_TEST_CONFIG_INI = "./config/StabilityTestConfig.ini";
	/** 稳定性测试模板存放目录 */
	public final static String STABILITY_TEST_MOUDLE_DIRECTORY = "./config/modules";
	/** 稳定性测试的日志配置文件 */
	public final static String STABILITY_LOG4J2_XML_PATH = "./config/log4j2_stability.xml";
	/** 测试数据所在目录 */
	public final static String TEST_DATA_DIR = "./testdata/input";
	/** 输出文件(通常是导出的模板、聚类输出结果)存放位置 */
	public final static String OUTPUT_DIR = "./testdata/output";
	/** 断言内容较复杂时, 将预期内容写成文件, 放到此目录下 */
	public final static String EXPECTED_DATA_DIR = "./testdata/expectation";
	/** 默认文件编码 */
	public final static String DEFAULT_ENCODING = "UTF-8";
	/** 请求返回成功的code */
	public final static String SUCCESS = "1";
	/** 请求成功返回的message */
	public final static String SUCCESS_MSG = "操作成功";
	/** 请求返回结果为空的code */
	public final static String EMPTY = "-1";
	/** 结果为空 */
	public final static String RESULT_IS_EMPTY = "结果为空！";
	/** 请求失败的code */
	public final static String FAILURE = "0";
	/** 输入参数为空时, 返回的message */
	public final static String INPUT_PARAMETER_IS_EMPTY = "输入参数为空！";
	/**
	 * 重新配置 log4j2 的配置
	 */
	public static void reconfigureLog4j2() {
		reconfigureLog4j2(Constant.DEFAULT_LOG4J2_XML_PATH);
	}
	/**
	 * 指定 log4j2 的配置文件位置并重新配置
	 * @param path
	 */
	public static void reconfigureLog4j2(String path) {
		LoggerContext logContext = (LoggerContext) LogManager.getContext(false);
		logContext.setConfigLocation(new File(path).getAbsoluteFile().toURI());
		logContext.reconfigure();
	}
	
	public static class ClassName{
		public final static String TRS_CKM_REQUEST = "com.trs.ckm.api.master.TRSCkmRequest"; 
	}
}
