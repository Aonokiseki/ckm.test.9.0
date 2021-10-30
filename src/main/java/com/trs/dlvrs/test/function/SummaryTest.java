package com.trs.dlvrs.test.function;

import static org.junit.Assert.assertNotNull;
import static org.testng.Assert.assertEquals;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.trs.ckm.test.function.Constant;
import com.trs.dlvrs.api.master.TRSVRSRequest;
import com.trs.dlvrs.api.pojo.BasicResult;
import com.trs.dlvrs.api.pojo.ImageSupportResult;
import com.trs.dlvrs.api.pojo.ModulesResult;
import com.trs.dlvrs.test.aspect.DLVRSAspectConfig;

public class SummaryTest {
	private final static Logger LOGGER = LogManager.getLogger(SummaryTest.class);
	private AnnotationConfigApplicationContext context = null;
	private TRSVRSRequest request;
	
	@BeforeClass
	public void before() {
		context = new AnnotationConfigApplicationContext(DLVRSAspectConfig.class);
		request = context.getBean(TRSVRSRequest.class);
		Constant.reconfigureLog4j2("./config/log4j2_dlvrs.xml");
	}
	
	@AfterClass
	public void after() {
		if(context != null)
			context.close();
	}
	
	@Test
	public void about() {
		LOGGER.info("SummaryTest.about()");
		BasicResult result = request.about();
		assertEquals(result.getCode(), Constant.SUCCESS);
		assertNotNull(result.getResult());
	}
	
	@Test
	public void modules() {
		LOGGER.info("SummaryTest.modules()");
		ModulesResult result = request.modules();
		assertEquals(result.getCode(), Constant.SUCCESS);
		assertNotNull(result.getResult());
	}
	
	@Test
	public void healthCheck() {
		LOGGER.info("SummaryTest.healthCheck()");
		BasicResult result = request.healthCheck();
		assertEquals(result.getCode(), Constant.SUCCESS);
		assertNotNull(result.getResult());
	}
	
	@Test
	public void imageSupport() {
		LOGGER.info("SummaryTest.imageSupport()");
		ImageSupportResult result = request.imageSupport();
		assertEquals(result.getCode(), Constant.SUCCESS);
		assertNotNull(result.getResult());
	}
}
