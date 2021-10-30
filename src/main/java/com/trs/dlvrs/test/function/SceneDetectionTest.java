package com.trs.dlvrs.test.function;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.google.gson.JsonSyntaxException;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;
import com.trs.ckm.test.function.Constant;
import com.trs.ckm.util.FakeShell;
import com.trs.ckm.util.Other;
import com.trs.dlvrs.api.master.TRSVRSRequest;
import com.trs.dlvrs.api.pojo.NSFWImageResult;
import com.trs.dlvrs.api.pojo.TerroristImageResult;
import com.trs.dlvrs.test.aspect.DLVRSAspectConfig;

public class SceneDetectionTest {
	private final static Logger LOGGER = LogManager.getLogger(SceneDetectionTest.class);
	private final static String TEST_DATA_DIRECTORY = "./testdata/input/dlvrs/SceneDetection";
	
	private AnnotationConfigApplicationContext context;
	private TRSVRSRequest request;
	private FakeShell fakeShell;
	private String remoteDirectory;
	private String className = this.getClass().getName();
	
	@BeforeClass
	public void before() {
		context = new AnnotationConfigApplicationContext(DLVRSAspectConfig.class);
		request = context.getBean(TRSVRSRequest.class);
		fakeShell = context.getBean(FakeShell.class);
		remoteDirectory = (String) context.getBean("remoteDirectory");
		Constant.reconfigureLog4j2("./config/log4j2_dlvrs.xml");
	}
	
	@AfterClass
	public void after() {
		if(context != null)
			context.close();
	}
	
	/* NSFW, 百度上查是Not Suitable For Work的意思, 
	 * 多指黄图(搜索结果甚至关联到github深度学习模型来鉴别黄图, 那就认为NSFW就是黄图吧)
	 * 现在贴内衣和泳装图片并不会识别为不正常的图片, emmm, 也没法找三点全漏的图, 所以……有结果就行了 */
	@DataProvider(name = "nsfwImageDataProvider")
	public Object[][] nsfwImageDataProvider(Method method){
		return new Object[][] {
			new Object[] {1, TEST_DATA_DIRECTORY + "/fate.jpg", "1", "正常"},
			new Object[] {2, TEST_DATA_DIRECTORY + "/error.jpg", "1", "正常"}
		};
	}
	/**
	 * NSFW图像鉴别检查
	 * @param caseId
	 * @param filePath
	 * @param code
	 * @param nsfw_type
	 */
	@Test(dataProvider = "nsfwImageDataProvider")
	public void nsfwImageFile(int caseId, String filePath, String code, String nsfw_type) {
		LOGGER.info(String.format("%s.%s, caseId=%d", className, Other.getMethodName(), caseId));
		NSFWImageResult result = request.nsfwImageFile(filePath);
		assertEquals(result.getCode(), code);
		NSFWImageResult.Result subResult = result.fetchResult();
		assertEquals(subResult.getNsfw_type(), nsfw_type);
	}
	/**
	 * NSFW图像鉴别检查
	 * @param caseId
	 * @param filePath
	 * @param code
	 * @param nsfw_type
	 */
	@Test(dataProvider = "nsfwImageDataProvider")
	public void nsfwImagePath(int caseId, String filePath, String code, String nsfw_type) {
		LOGGER.info(String.format("%s.%s, caseId=%d", className, Other.getMethodName(), caseId));
		File file = new File(filePath);
		String remoteFilePath = remoteDirectory + "/" + file.getName();
		try {
			fakeShell.connect();
			fakeShell.upload(remoteDirectory, filePath);
			NSFWImageResult result = request.nsfwImagePath(remoteFilePath);
			assertEquals(result.getCode(), code);
			NSFWImageResult.Result subResult = result.fetchResult();
			assertEquals(subResult.getNsfw_type(), nsfw_type);
		} catch (JSchException | SftpException | JsonSyntaxException | IOException e) {
			String failureLog = Other.stackTraceToString(e);
			LOGGER.error(failureLog);
			fail(failureLog);
		} finally {
			try {
				fakeShell.executeCommand("rm -f "+remoteFilePath, "utf-8");
			} catch (JSchException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			fakeShell.disconnect();
		}
	}
	/*暴恐图片识别, 还是和黄图识别一样, 有结果就行了, 如果真的有识别出暴恐的就更好了, 但不强求*/
	@DataProvider(name = "terroristImageDataProvider")
	public Object[][] terroristImageDataProvider(Method method){
		return new Object[][] {
			new Object[] {1, TEST_DATA_DIRECTORY + "/terrorist.jpg", "1", "军服"},
			new Object[] {1, TEST_DATA_DIRECTORY + "/terrorist2.jpg", "1", "普通"},
			new Object[] {1, TEST_DATA_DIRECTORY + "/terrorist3.jpg", "1", "普通"}
		};
	}
	/**
	 * 暴恐图像识别
	 * @param caseId
	 * @param filePath
	 * @param code
	 * @param terr_type
	 */
	@Test(dataProvider = "terroristImageDataProvider")
	public void terroristImageFile(int caseId, String filePath, String code, String terr_type) {
		LOGGER.info(String.format("%s.%s, caseId=%d", className, Other.getMethodName(), caseId));
		TerroristImageResult result = request.terroristImageFile(filePath);
		assertEquals(result.getCode(), code);
		TerroristImageResult.Result subResult = result.fetchResult();
		assertEquals(subResult.getTerr_type(), terr_type);
	}
	/**
	 * 暴恐图像识别
	 * @param caseId
	 * @param filePath
	 * @param code
	 * @param terr_type
	 */
	@Test(dataProvider = "terroristImageDataProvider")
	public void terroristImagePath(int caseId, String filePath, String code, String terr_type) {
		LOGGER.info(String.format("%s.%s, caseId=%d", className, Other.getMethodName(), caseId));
		File file = new File(filePath);
		String remoteFilePath = remoteDirectory + "/" + file.getName();
		try {
			fakeShell.connect();
			fakeShell.upload(remoteDirectory, filePath);
			TerroristImageResult result = request.terroristImagePath(remoteFilePath);
			assertEquals(result.getCode(), code);
			TerroristImageResult.Result subResult = result.fetchResult();
			assertEquals(subResult.getTerr_type(), terr_type);
		} catch (JSchException | IOException | SftpException e) {
			String failureLog = Other.stackTraceToString(e);
			LOGGER.error(failureLog);
			fail(failureLog);
		} finally {
			try {
				fakeShell.executeCommand("rm -f "+remoteFilePath, "utf-8");
			} catch (JSchException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			fakeShell.disconnect();
		}
	}
}