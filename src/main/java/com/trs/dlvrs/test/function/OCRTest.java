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
import com.trs.dlvrs.api.pojo.OCRImageResult;
import com.trs.dlvrs.api.pojo.TableOcrImageResult;
import com.trs.dlvrs.test.aspect.DLVRSAspectConfig;

public class OCRTest {
	
	private final static Logger LOGGER = LogManager.getLogger(OCRTest.class);
	private final static String TEST_DATA_DIRECTORY = "./testdata/input/dlvrs/OCR";
	
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
	
	@DataProvider(name = "ocrImageDataProvider")
	public Object[][] ocrImageDataProvider(Method method){
		return new Object[][] {
			new Object[] {1, TEST_DATA_DIRECTORY + "/0.jpg", "1", 13},
			new Object[] {2, TEST_DATA_DIRECTORY + "/越南国旗.jpg", "0", 0},
			new Object[] {3, TEST_DATA_DIRECTORY + "/PCSponser.jpg", "1", 22},
			new Object[] {4, TEST_DATA_DIRECTORY + "/1856.jpg", "1", 180}
		};
	}
	
	@Test(dataProvider = "ocrImageDataProvider")
	public void ocrImageFile(int caseId, String filePath, String code, int expectedSize) {
		LOGGER.info(String.format("%s.%s, caseId=%d", className, Other.getMethodName(), caseId));
		OCRImageResult result = request.ocrImageFile(filePath);
		assertEquals(result.getCode(), code);
		assertEquals(result.fetchResult().size(), expectedSize);
	}
	@Test(dataProvider = "ocrImageDataProvider")
	public void ocrImagePath(int caseId, String filePath, String code, int expectedSize) {
		LOGGER.info(String.format("%s.%s, caseId=%d", className, Other.getMethodName(), caseId));
		File file = new File(filePath);
		String fileName = file.getName();
		String remoteFilePath = remoteDirectory + "/" + fileName;
		try {
			fakeShell.connect();
			fakeShell.upload(remoteDirectory, filePath);
			OCRImageResult result = request.ocrImagePath(remoteFilePath);
			assertEquals(result.getCode(), code);
			assertEquals(result.fetchResult().size(), expectedSize);
		} catch (JSchException | JsonSyntaxException | IOException | SftpException e) {
			String failureLog = Other.stackTraceToString(e);
			LOGGER.error(failureLog);
			fail(failureLog);
		} finally {
			try {
				fakeShell.executeCommand("rm -f " + remoteFilePath, "utf-8");
			} catch (JSchException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			fakeShell.disconnect();
		}
	}
	
	@DataProvider(name = "tableOcrImageDataProvider")
	public Object[][] tableOcrImageDataProvider(Method method){
		return new Object[][] {
			new Object[] {1, TEST_DATA_DIRECTORY + "/1856.jpg", "1", 1, 0},
			new Object[] {2, TEST_DATA_DIRECTORY + "/63.jpg", "1", 1, 0},
			/* 干扰项, 没有表格, 只有文字 -> 放到OtherResult里 */
			new Object[] {3, TEST_DATA_DIRECTORY + "/0.jpg", "1", 0, 13},
			/* 干扰项, 只有图, 两个结果的size都是0 */
			new Object[] {4, TEST_DATA_DIRECTORY + "/越南国旗.jpg", "0", 0, 0}
		};
	}
	
	@Test(dataProvider = "tableOcrImageDataProvider")
	public void tableOcrImageFile(int caseId, String filePath, String code, int tableSize, int otherSize) {
		LOGGER.info(String.format("%s%s%s.%s, caseId=%d", 
				"================", System.lineSeparator(), className, Other.getMethodName(), caseId));
		TableOcrImageResult result = request.tableOcrImageFile(filePath);
		assertEquals(result.getCode(), Constant.SUCCESS);
		/* 这……要怎么断言……头大*/
		int otherResultSize = result.fetchResult().getOther_results().size();
		int tableResultSize = result.fetchResult().getTable_results().size();
		LOGGER.debug(String.format("tableResultSize=%d, otherResultSize=%d", tableResultSize, otherResultSize));
		assertEquals(tableResultSize, tableSize);
		assertEquals(otherResultSize, otherSize);
	}
	
	@Test(dataProvider = "tableOcrImageDataProvider")
	public void tableOcrImagePath(int caseId, String filePath, String code, int tableSize, int otherSize) {
		LOGGER.info(String.format("%s.%s, caseId=%d", className, Other.getMethodName(), caseId));
		File file = new File(filePath);
		String fileName = file.getName();
		String remoteFilePath = remoteDirectory + "/" + fileName;
		try {
			fakeShell.connect();
			fakeShell.upload(remoteDirectory, filePath);
			TableOcrImageResult result = request.tableOcrImagePath(remoteFilePath);
			assertEquals(result.getCode(), Constant.SUCCESS);
			int otherResultSize = result.fetchResult().getOther_results().size();
			int tableResultSize = result.fetchResult().getTable_results().size();
			LOGGER.debug(String.format("tableResultSize=%d, otherResultSize=%d", tableResultSize, otherResultSize));
			assertEquals(tableResultSize, tableSize);
			assertEquals(otherResultSize, otherSize);
		} catch (JSchException | SftpException | JsonSyntaxException | IOException e) {
			String failureLog = Other.stackTraceToString(e);
			LOGGER.error(failureLog);
			fail(failureLog);
		} finally {
			try {
				fakeShell.executeCommand("rm -f " + remoteFilePath, "utf-8");
			} catch (JSchException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			fakeShell.disconnect();
		}
	}
}
