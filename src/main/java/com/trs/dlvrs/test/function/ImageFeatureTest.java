package com.trs.dlvrs.test.function;

import static org.junit.Assert.assertNotNull;
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
import com.trs.dlvrs.api.pojo.ImageFeatureResult;
import com.trs.dlvrs.test.aspect.DLVRSAspectConfig;

public class ImageFeatureTest {
	private final static Logger LOGGER = LogManager.getLogger(ImageFeatureTest.class);
	private final static String TEST_DATA_DIRECTORY = "./testdata/input/dlvrs/HashImage";
	
	private AnnotationConfigApplicationContext context = null;
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
	
	@DataProvider(name = "hashDataProvider")
	public Object[][] aHashImageDataProvider(Method method){
		return new Object[][] {
			new Object[] {1, TEST_DATA_DIRECTORY + "/09.jpg"},
			new Object[] {2, TEST_DATA_DIRECTORY + "/10.jpg"}
		};
	}
	/**
	 * 均值哈希检查
	 * @param caseId
	 * @param filePath
	 */
	@Test(dataProvider = "hashDataProvider")
	public void aHashImageFile(int caseId, String filePath) {
		LOGGER.info(String.format("%s.%s, caseId=%d", className, Other.getMethodName(), caseId));
		ImageFeatureResult result = request.aHashImageFile(filePath);
		assertEquals(result.getCode(), Constant.SUCCESS);
		assertNotNull(result.getResult());
	}
	/**
	 * 均值哈希检查
	 * @param caseId
	 * @param filePath
	 */
	@Test(dataProvider = "hashDataProvider")
	public void aHashImagePath(int caseId, String filePath) {
		LOGGER.info(String.format("%s.%s, caseId=%d", className, Other.getMethodName(), caseId));
		File filePointer = new File(filePath);
		String fileName = filePointer.getName();
		String remoteServerFilePath = remoteDirectory + "/" + fileName;
		try {
			/* 连接远程服务器 */
			fakeShell.connect();
			/* 上传本地文件 */
			LOGGER.debug(String.format("[%s].upload(%s, %s)", fakeShell.toString(), remoteDirectory, filePath));
			fakeShell.upload(remoteDirectory, filePath);
			/* 接口要求通过服务器上的文件路径指定欲发送给接口的文件, 因此就发送刚才上传的文件的路径 */
			ImageFeatureResult result = request.aHashImagePath(remoteServerFilePath);
			/* 断言 */
			assertEquals(result.getCode(), Constant.SUCCESS);
			assertNotNull(result.getResult());
		} catch (JSchException | SftpException | JsonSyntaxException | IOException e) {
			String failureLog = Other.stackTraceToString(e);
			LOGGER.error(failureLog);
			fail(failureLog);
		} finally {
			String deleteCommand = "rm -f " + remoteServerFilePath;
			try {
				/* 清理环境 */
				LOGGER.debug(String.format("[%s].executeCommand(%s)", fakeShell.toString(), deleteCommand));
				fakeShell.executeCommand(deleteCommand, "utf-8");
			} catch (JSchException | IOException e) {
				e.printStackTrace();
			}
			fakeShell.disconnect();
		}
	}
	/**
	 * 感知哈希检查
	 * @param caseId
	 * @param filePath
	 */
	@Test(dataProvider="hashDataProvider")
	public void pHashImageFile(int caseId, String filePath) {
		LOGGER.info(String.format("%s.%s, caseId=%d", className, Other.getMethodName(), caseId));
		ImageFeatureResult result = request.pHashImageFile(filePath);
		assertEquals(result.getCode(), Constant.SUCCESS);
		assertNotNull(result.getResult());
	}
	/**
	 * 感知哈希检查
	 * @param caseId
	 * @param filePath
	 */
	@Test(dataProvider="hashDataProvider")
	public void pHashImagePath(int caseId, String filePath) {
		LOGGER.info(String.format("%s.%s, caseId=%d", className, Other.getMethodName(), caseId));
		File file = new File(filePath);
		String fileName = file.getName();
		String remoteServerFilePath = remoteDirectory + "/" + fileName;
		try {
			fakeShell.connect();
			LOGGER.debug(String.format("[%s].upload(%s, %s)", fakeShell.toString(), remoteDirectory, filePath));
			fakeShell.upload(remoteDirectory, filePath);
			ImageFeatureResult result = request.pHashImagePath(remoteServerFilePath);
			assertEquals(result.getCode(), Constant.SUCCESS);
			assertNotNull(result.getResult());
		} catch (JSchException | IOException | SftpException e) {
			String failureLog = Other.stackTraceToString(e);
			LOGGER.error(failureLog);
			fail(failureLog);
		} finally {
			String deleteCommand = "rm -f " + remoteServerFilePath;
			try {
				LOGGER.debug(String.format("[%s].executeCommand(%s)", fakeShell.toString(), deleteCommand));
				fakeShell.executeCommand(deleteCommand, "utf-8");
			} catch (JSchException | IOException e) {
				e.printStackTrace();
			}
			fakeShell.disconnect();
		}
	}
	/**
	 * 差值哈希检查
	 * @param caseId
	 * @param filePath
	 */
	@Test(dataProvider="hashDataProvider")
	public void dHashImageFile(int caseId, String filePath) {
		LOGGER.info(String.format("%s.%s, caseId=%d", className, Other.getMethodName(), caseId));
		ImageFeatureResult result = request.dHashImageFile(filePath);
		assertEquals(result.getCode(), Constant.SUCCESS);
		assertNotNull(result.getResult());
	}
	/**
	 * 差值哈希检查
	 * @param caseId
	 * @param filePath
	 */
	@Test(dataProvider="hashDataProvider")
	public void dHashImagePath(int caseId, String filePath) {
		LOGGER.info(String.format("%s.%s, caseId=%d", className, Other.getMethodName(), caseId));
		File file = new File(filePath);
		String fileName = file.getName();
		String remoteServerFilePath = remoteDirectory + "/" + fileName;
		try {
			fakeShell.connect();
			LOGGER.debug(String.format("[%s].upload(%s, %s)", fakeShell.toString(), remoteDirectory, filePath));
			fakeShell.upload(remoteDirectory, filePath);
			ImageFeatureResult result = request.dHashImagePath(remoteServerFilePath);
			assertEquals(result.getCode(), Constant.SUCCESS);
			assertNotNull(result.getResult());
		} catch (JSchException | IOException | SftpException e) {
			String failureLog = Other.stackTraceToString(e);
			LOGGER.error(failureLog);
			fail(failureLog);
		} finally {
			String deleteCommand = "rm -f " + remoteServerFilePath;
			try {
				LOGGER.debug(String.format("[%s].executeCommand(%s)", fakeShell.toString(), deleteCommand));
				fakeShell.executeCommand(deleteCommand, "utf-8");
			} catch (JSchException | IOException e) {
				e.printStackTrace();
			}
			fakeShell.disconnect();
		}
	}
	
	/**
	 * md5值检查
	 * @param caseId
	 * @param filePath
	 */
	@Test(dataProvider="hashDataProvider")
	public void md5ImageFile(int caseId, String filePath) {
		LOGGER.info(String.format("%s.%s, caseId=%d", className, Other.getMethodName(), caseId));
		ImageFeatureResult result = request.md5ImageFile(filePath);
		assertEquals(result.getCode(), Constant.SUCCESS);
		assertNotNull(result.getResult());
	}
	/**
	 * md5值检查
	 * @param caseId
	 * @param filePath
	 */
	@Test(dataProvider="hashDataProvider")
	public void md5ImagePath(int caseId, String filePath) {
		LOGGER.info(String.format("%s.%s, caseId=%d", className, Other.getMethodName(), caseId));
		File file = new File(filePath);
		String fileName = file.getName();
		String remoteServerFilePath = remoteDirectory + "/" + fileName;
		try {
			fakeShell.connect();
			LOGGER.debug(String.format("[%s].upload(%s, %s)", fakeShell.toString(), remoteDirectory, filePath));
			fakeShell.upload(remoteDirectory, filePath);
			ImageFeatureResult result = request.md5ImagePath(remoteServerFilePath);
			assertEquals(result.getCode(), Constant.SUCCESS);
			assertNotNull(result.getResult());
		} catch (JSchException | IOException | SftpException e) {
			String failureLog = Other.stackTraceToString(e);
			LOGGER.error(failureLog);
			fail(failureLog);
		} finally {
			String deleteCommand = "rm -f " + remoteServerFilePath;
			try {
				LOGGER.debug(String.format("[%s].executeCommand(%s)", fakeShell.toString(), deleteCommand));
				fakeShell.executeCommand(deleteCommand, "utf-8");
			} catch (JSchException | IOException e) {
				e.printStackTrace();
			}
			fakeShell.disconnect();
		}
	}
}
