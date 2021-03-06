package com.trs.dlvrs.test.function;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.testng.Assert.fail;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.DigestUtils;
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
import com.trs.dlvrs.api.pojo.BasicResult;
import com.trs.dlvrs.api.pojo.CompareFaceImageResult;
import com.trs.dlvrs.api.pojo.FaceRecognitionResult;
import com.trs.dlvrs.test.aspect.DLVRSAspectConfig;

public class FaceRecognitionTest {
	private final static Logger LOGGER = LogManager.getLogger(FaceRecognitionTest.class);
	private final static String TEST_DATA_DIRECTORY = "./testdata/input/dlvrs/FaceRecognition";
	
	private AnnotationConfigApplicationContext context;
	private TRSVRSRequest request;
	private JdbcTemplate jdbc;
	private String faceRecognitionTable;
	private FakeShell fakeShell;
	private String remoteDirectory;
	private String className = this.getClass().getName();
	
	@BeforeClass
	public void before() {
		context = new AnnotationConfigApplicationContext(DLVRSAspectConfig.class);
		request = context.getBean(TRSVRSRequest.class);
		jdbc = context.getBean(JdbcTemplate.class);
		faceRecognitionTable = (String) context.getBean("faceRecognitionTable");
		fakeShell = context.getBean(FakeShell.class);
		remoteDirectory = (String) context.getBean("remoteDirectory");
		Constant.reconfigureLog4j2("./config/log4j2_dlvrs.xml");
	}
	@AfterClass
	public void after() {
		if(context != null)
			context.close();
	}
	
	@Test
	public void reloadFaceDatabase() {
		LOGGER.info(String.format("%s.%s", className, Other.getMethodName()));
		BasicResult result = request.reloadFaceDatabase();
		assertEquals(result.getCode(), Constant.SUCCESS);
	}
	
	@DataProvider(name = "uploadFaceDataProvider")
	public Object[][] uploadFaceDataProvider(Method method){
		return new Object[][] {
			new Object[] {1, TEST_DATA_DIRECTORY + "/4.jpg", "4", "4", "??????4", 
					String.format("select * from %s where face_id='%s' and img_id='%s' and face_name='%s';", 
							faceRecognitionTable, "4", "4", "??????4")},
		};
	}
	
	/**
	 * ???????????????????????????
	 * @param caseId 
	 * @param filePath
	 * @param img_id ????????????id
	 * @param face_id ????????????id
	 * @param face_name ????????????
	 * @param sql ?????????SQL???????????????
	 */
	@Test(dataProvider = "uploadFaceDataProvider")
	public void uploadFaceFile(int caseId, String filePath, String img_id, String face_id, String face_name, String sql) {
		LOGGER.info(String.format("%s.%s, caseId=%d", className, Other.getMethodName(), caseId));
		/*  ???????????? */
		BasicResult result = request.uploadFaceFile(filePath, img_id, face_id, face_name);
		assertEquals(result.getCode(), Constant.SUCCESS);
		/* ??????MySQL??????????????? */
		List<Map<String,Object>> resultSet = jdbc.queryForList(sql);
		assertEquals(resultSet.size(), 1);
		/* ???????????? */
		Map<String, Object> record = resultSet.get(0);
		long deleteId = (long)record.get("id");
		String deleteSql = "delete from " + faceRecognitionTable + " where id=" + deleteId + ";";
		jdbc.execute(deleteSql);
	}
	/**
	 * ?????????????????????????????????
	 * @param caseId
	 * @param filePath
	 * @param img_id
	 * @param face_id
	 * @param face_name
	 * @param sql
	 */
	@Test(dataProvider = "uploadFaceDataProvider")
	public void uploadFacePath(int caseId, String filePath, String img_id, String face_id, String face_name, String sql) {
		LOGGER.info(String.format("%s.%s, caseId=%d", className, Other.getMethodName(), caseId));
		File filePointer = new File(filePath);
		String fileName = filePointer.getName();
		String remoteFilePath = remoteDirectory + "/" + fileName;
		try {
			fakeShell.connect();
			fakeShell.upload(remoteDirectory, filePath);
			BasicResult result = request.uploadFacePath(remoteFilePath, img_id, face_id, face_name);
			assertEquals(result.getCode(), Constant.SUCCESS);
			List<Map<String, Object>> resultSet = jdbc.queryForList(sql);
			assertEquals(resultSet.size(), 1);
			Map<String, Object> record = resultSet.get(0);
			long id = (long)record.get("id");
			String deleteSql = String.format("delete from %s where id=%d", faceRecognitionTable, id);
			jdbc.execute(deleteSql);
			fakeShell.executeCommand("rm -f " + remoteFilePath, "utf-8");
		} catch (JSchException | IOException | SftpException e) {
			String failureLog = Other.stackTraceToString(e);
			LOGGER.error(failureLog);
			fail(failureLog);
		} finally {
			fakeShell.disconnect();
		}
	}
	/**
	 * ???????????????????????????<br>
	 * ?????????????????????????????????????????????????????????
	 * @param caseId
	 * @param filePath
	 * @param img_id ????????????id
	 * @param face_id
	 * @param face_name
	 * @param sql
	 */
	@Test(dataProvider = "uploadFaceDataProvider")
	public void deleteFace(int caseId, String filePath, String img_id, String face_id, String face_name, String sql) {
		LOGGER.info(String.format("%s.%s, caseId=%d", className, Other.getMethodName(), caseId));
		/* ???????????? */
		BasicResult result = request.uploadFaceFile(filePath, img_id, face_id, face_name);
		assertEquals(result.getCode(), Constant.SUCCESS);
		List<Map<String,Object>> resultSet = jdbc.queryForList(sql);
		assertEquals(resultSet.size(), 1);
		try {
			/* ????????????????????? */
			result = request.deleteFace(img_id);
			assertEquals(result.getCode(), Constant.SUCCESS);
		} catch (JsonSyntaxException | IOException e) {
			String failureLog = Other.stackTraceToString(e);
			LOGGER.error(failureLog);
			fail(failureLog);
		}
		/* ??????????????????, ??????0????????? */
		resultSet = jdbc.queryForList(sql);
		assertEquals(resultSet.size(), 0);
	}
	
	@DataProvider(name = "faceRecognitionImageDataProvider")
	public Object[][] faceRecognitionImageDataProvider(Method method){
		return new Object[][] {
			new Object[] {1, TEST_DATA_DIRECTORY + "/28.jpg", Constant.SUCCESS, true},
			new Object[] {2, TEST_DATA_DIRECTORY + "/4.jpg", Constant.SUCCESS, true},
			/* ??????, ?????????????????????0, ???CKM????????? */
			new Object[] {3, TEST_DATA_DIRECTORY + "/0.jpg", "0", false}
		};
	}
	/**
	 * ??????????????????
	 * @param caseId
	 * @param filePath
	 * @param code ???????????????
	 * @param containsKeyWord ???????????????????????????Location, ???????????????????????????????????????????????????
	 */
	@Test(dataProvider = "faceRecognitionImageDataProvider")
	public void faceRecognitionImageFile(int caseId, String filePath, String code, boolean containsKeyWord) {
		LOGGER.info(String.format("%s.%s, caseId=%d", className, Other.getMethodName(), caseId));
		FaceRecognitionResult result = request.faceRecognitionImageFile(filePath);
		assertEquals(result.getCode(), code);
		assertEquals(result.getResult().contains("Result") && result.getResult().contains("Location"), containsKeyWord);
	}
	/**
	 * ??????????????????
	 * @param caseId
	 * @param filePath
	 * @param code
	 * @param containsKeyWord
	 */
	@Test(dataProvider = "faceRecognitionImageDataProvider")
	public void faceRecognitionImagePath(int caseId, String filePath, String code, boolean containsKeyWord) {
		LOGGER.info(String.format("%s.%s, caseId=%d", className, Other.getMethodName(), caseId));
		File file = new File(filePath);
		String fileName = file.getName();
		String remoteFilePath = remoteDirectory + "/" + fileName;
		try {
			fakeShell.connect();
			fakeShell.upload(remoteDirectory, filePath);
			FaceRecognitionResult result = request.faceRecognitionImagePath(remoteFilePath);
			assertEquals(result.getCode(), code);
			assertEquals(result.getResult().contains("Result") && result.getResult().contains("Location"), containsKeyWord);
			fakeShell.executeCommand("rm -f " + remoteFilePath, "utf-8");
		} catch (IOException | JSchException | SftpException e) {
			String failureLog = Other.stackTraceToString(e);
			LOGGER.error(failureLog);
			fail(failureLog);
		} finally {
			fakeShell.disconnect();
		}
	}
	/**
	 * ????????????????????????
	 * @param caseId
	 * @param filePath
	 * @param code
	 * @param containsKeyWord
	 */
	@Test(dataProvider = "faceRecognitionImageDataProvider")
	public void faceFeatureImageFile(int caseId, String filePath, String code, boolean containsKeyWord) {
		LOGGER.info(String.format("%s.%s, caseId=%d", className, Other.getMethodName(), caseId));
		FaceRecognitionResult result = request.faceFeatureImageFile(filePath);
		assertEquals(result.getCode(), code);
		assertEquals(result.getResult().contains("Result") && result.getResult().contains("Location"), containsKeyWord);
	}
	/**
	 * ????????????????????????
	 * @param caseId
	 * @param filePath
	 * @param code
	 * @param containsKeyWord
	 */
	@Test(dataProvider = "faceRecognitionImageDataProvider")
	public void faceFeatureImagePath(int caseId, String filePath, String code, boolean containsKeyWord) {
		LOGGER.info(String.format("%s.%s, caseId=%d", className, Other.getMethodName(), caseId));
		File file = new File(filePath);
		String fileName = file.getName();
		String remoteFilePath = remoteDirectory + "/" + fileName;
		try {
			fakeShell.connect();
			fakeShell.upload(remoteDirectory, filePath);
			FaceRecognitionResult result = request.faceFeatureImagePath(remoteFilePath);
			assertEquals(result.getCode(), code);
			assertEquals(result.getResult().contains("Result") && result.getResult().contains("Location"), containsKeyWord);
			fakeShell.executeCommand("rm -f " + remoteFilePath, "utf-8");
		} catch (IOException | JSchException | SftpException e) {
			String failureLog = Other.stackTraceToString(e);
			LOGGER.error(failureLog);
			fail(failureLog);
		} finally {
			fakeShell.disconnect();
		}
	}
	
	@DataProvider(name = "compareFaceImageDataProvider")
	public Object[][] compareFaceImageDataProvider(Method method){
		return new Object[][] {
			/* 20210924, ????????????? ?????????????????????, ????????????????????????1??? */
			new Object[] {1, TEST_DATA_DIRECTORY + "/28.jpg", TEST_DATA_DIRECTORY + "/28.jpg"},
			new Object[] {2, TEST_DATA_DIRECTORY + "/4.jpg", TEST_DATA_DIRECTORY + "/28.jpg"}
		};
	}
	/**
	 * ???????????????????????????
	 * @param caseId
	 * @param filePath1
	 * @param filePath2
	 */
	@Test(dataProvider = "compareFaceImageDataProvider")
	public void compareFaceImageFile(int caseId, String filePath1, String filePath2) {
		LOGGER.info(String.format("%s.%s, caseId=%d", className, Other.getMethodName(), caseId));
		File file1 = new File(filePath1);
		File file2 = new File(filePath2);
		/* ??????????????????, ???????????? */
		CompareFaceImageResult result = request.compareFaceImageFile(file1.getAbsolutePath(), file2.getAbsolutePath());
		assertEquals(result.getCode(), Constant.SUCCESS);
		double similarity = result.fetchResult().similarity();
		LOGGER.debug(String.format("similarity=%f", similarity));
		String file1Md5; String file2Md5;
		try {
			/* ????????????????????????(???MD5??????), ???????????????????????????1.0; ???????????????0~1?????? */
			/* ?????????????????????MD5, ????????????????????? */
			file1Md5 = DigestUtils.md5DigestAsHex(new FileInputStream(file1));
			file2Md5 = DigestUtils.md5DigestAsHex(new FileInputStream(file2));
			LOGGER.debug(String.format("[%s].md5==%s, [%s],md5==%s", 
					file1.getName(), file1Md5, file2.getName(), file2Md5));
			if(file1Md5.equals(file2Md5))
				assertTrue(similarity == 1.0);
			else
				assertTrue(0.0 < similarity && similarity < 1.0);
		} catch (IOException e) {
			String failureLog = Other.stackTraceToString(e);
			LOGGER.error(failureLog);
			fail(failureLog);
		}
	}
	/**
	 * ???????????????????????????
	 * @param caseId
	 * @param filePath1
	 * @param filePath2
	 */
	@Test(dataProvider = "compareFaceImageDataProvider")
	public void compareFaceImagePath(int caseId, String filePath1, String filePath2) {
		LOGGER.info(String.format("%s.%s, caseId=%d", className, Other.getMethodName(), caseId));
		File file1 = new File(filePath1);
		String remoteFilePath1 = remoteDirectory + "/" + file1.getName();
		File file2 = new File(filePath2);
		String remoteFilePath2 = remoteDirectory + "/" + file2.getName();
		try {
			/* ??????????????????????????????MD5 */
			String file1Md5 = DigestUtils.md5DigestAsHex(new FileInputStream(file1));
			LOGGER.debug(String.format("[%s].md5==%s", file1.getName(), file1Md5));
			String file2Md5 = DigestUtils.md5DigestAsHex(new FileInputStream(file2));
			LOGGER.debug(String.format("[%s].md5==%s", file2.getName(), file2Md5));
			fakeShell.connect();
			/* ??????????????????????????????linux???????????? */
			fakeShell.upload(remoteDirectory, file1.getAbsolutePath());
			if(!file1Md5.equals(file2Md5)) {
				/* ?????????????????????MD5??????????????????, ????????????????????????, ????????????????????????????????????????????????, ?????????????????? */
				fakeShell.upload(remoteDirectory, file2.getAbsolutePath());
				CompareFaceImageResult result = request.compareFaceImagePath(remoteFilePath1, remoteFilePath2);
				double similarity = result.fetchResult().similarity();
				/* ????????????0~1???????????? */
				assertTrue(0.0 < similarity && similarity < 1.0);
				return;
			}
			/* ?????????????????????MD5??????????????????, ?????????????????????. ???????????????????????????????????????????????????, ???????????????????????????????????? */
			CompareFaceImageResult result = request.compareFaceImagePath(remoteFilePath1, remoteFilePath1);
			double similarity = result.fetchResult().similarity();
			/* ?????????????????????1.0 */
			assertTrue(similarity == 1.0);
		} catch (IOException | JSchException | SftpException e) {
			String failureLog = Other.stackTraceToString(e);
			LOGGER.error(failureLog);
			fail(failureLog);
		} finally {
			try {
				fakeShell.executeCommand("rm -f " + remoteFilePath1, "utf-8");
			} catch (JSchException | IOException e) {
				e.printStackTrace();
			}
			try {
				fakeShell.executeCommand("rm -f " + remoteFilePath2, "utf-8");
			} catch (JSchException |IOException e) {
				e.printStackTrace();
			}
			fakeShell.disconnect();
		}
	}
}