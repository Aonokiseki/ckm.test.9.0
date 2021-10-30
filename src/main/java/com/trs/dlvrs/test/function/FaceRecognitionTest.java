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
			new Object[] {1, TEST_DATA_DIRECTORY + "/4.jpg", "4", "4", "演员4", 
					String.format("select * from %s where face_id='%s' and img_id='%s' and face_name='%s';", 
							faceRecognitionTable, "4", "4", "演员4")},
		};
	}
	
	/**
	 * 人脸库上传图片测试
	 * @param caseId 
	 * @param filePath
	 * @param img_id 图像唯一id
	 * @param face_id 人物唯一id
	 * @param face_name 人物名称
	 * @param sql 使用的SQL检索表达式
	 */
	@Test(dataProvider = "uploadFaceDataProvider")
	public void uploadFaceFile(int caseId, String filePath, String img_id, String face_id, String face_name, String sql) {
		LOGGER.info(String.format("%s.%s, caseId=%d", className, Other.getMethodName(), caseId));
		/*  上传文件 */
		BasicResult result = request.uploadFaceFile(filePath, img_id, face_id, face_name);
		assertEquals(result.getCode(), Constant.SUCCESS);
		/* 再到MySQL的表中检索 */
		List<Map<String,Object>> resultSet = jdbc.queryForList(sql);
		assertEquals(resultSet.size(), 1);
		/* 清理环境 */
		Map<String, Object> record = resultSet.get(0);
		long deleteId = (long)record.get("id");
		String deleteSql = "delete from " + faceRecognitionTable + " where id=" + deleteId + ";";
		jdbc.execute(deleteSql);
	}
	/**
	 * 人脸库记录上传图片测试
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
	 * 人脸库记录删除检查<br>
	 * 检查的时候是先上传一张图片然后再删除它
	 * @param caseId
	 * @param filePath
	 * @param img_id 图像唯一id
	 * @param face_id
	 * @param face_name
	 * @param sql
	 */
	@Test(dataProvider = "uploadFaceDataProvider")
	public void deleteFace(int caseId, String filePath, String img_id, String face_id, String face_name, String sql) {
		LOGGER.info(String.format("%s.%s, caseId=%d", className, Other.getMethodName(), caseId));
		/* 上传图片 */
		BasicResult result = request.uploadFaceFile(filePath, img_id, face_id, face_name);
		assertEquals(result.getCode(), Constant.SUCCESS);
		List<Map<String,Object>> resultSet = jdbc.queryForList(sql);
		assertEquals(resultSet.size(), 1);
		try {
			/* 再删除这张图片 */
			result = request.deleteFace(img_id);
			assertEquals(result.getCode(), Constant.SUCCESS);
		} catch (JsonSyntaxException | IOException e) {
			String failureLog = Other.stackTraceToString(e);
			LOGGER.error(failureLog);
			fail(failureLog);
		}
		/* 检索这张图片, 命中0条记录 */
		resultSet = jdbc.queryForList(sql);
		assertEquals(resultSet.size(), 0);
	}
	
	@DataProvider(name = "faceRecognitionImageDataProvider")
	public Object[][] faceRecognitionImageDataProvider(Method method){
		return new Object[][] {
			new Object[] {1, TEST_DATA_DIRECTORY + "/28.jpg", Constant.SUCCESS, true},
			new Object[] {2, TEST_DATA_DIRECTORY + "/4.jpg", Constant.SUCCESS, true},
			/* 注意, 没有结果时返回0, 和CKM不一样 */
			new Object[] {3, TEST_DATA_DIRECTORY + "/0.jpg", "0", false}
		};
	}
	/**
	 * 人脸识别测试
	 * @param caseId
	 * @param filePath
	 * @param code 预期结果号
	 * @param containsKeyWord 预期是否包含关键字Location, 这个参数用于辅助判定真的有结果返回
	 */
	@Test(dataProvider = "faceRecognitionImageDataProvider")
	public void faceRecognitionImageFile(int caseId, String filePath, String code, boolean containsKeyWord) {
		LOGGER.info(String.format("%s.%s, caseId=%d", className, Other.getMethodName(), caseId));
		FaceRecognitionResult result = request.faceRecognitionImageFile(filePath);
		assertEquals(result.getCode(), code);
		assertEquals(result.getResult().contains("Result") && result.getResult().contains("Location"), containsKeyWord);
	}
	/**
	 * 人脸识别测试
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
	 * 人脸特征提取测试
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
	 * 人脸特征提取测试
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
			/* 20210924, 怎么回事? 两张相同的图片, 相似度还偶尔不是1??? */
			new Object[] {1, TEST_DATA_DIRECTORY + "/28.jpg", TEST_DATA_DIRECTORY + "/28.jpg"},
			new Object[] {2, TEST_DATA_DIRECTORY + "/4.jpg", TEST_DATA_DIRECTORY + "/28.jpg"}
		};
	}
	/**
	 * 人脸相似度比对测试
	 * @param caseId
	 * @param filePath1
	 * @param filePath2
	 */
	@Test(dataProvider = "compareFaceImageDataProvider")
	public void compareFaceImageFile(int caseId, String filePath1, String filePath2) {
		LOGGER.info(String.format("%s.%s, caseId=%d", className, Other.getMethodName(), caseId));
		File file1 = new File(filePath1);
		File file2 = new File(filePath2);
		/* 对比两张图片, 获得结果 */
		CompareFaceImageResult result = request.compareFaceImageFile(file1.getAbsolutePath(), file2.getAbsolutePath());
		assertEquals(result.getCode(), Constant.SUCCESS);
		double similarity = result.fetchResult().similarity();
		LOGGER.debug(String.format("similarity=%f", similarity));
		String file1Md5; String file2Md5;
		try {
			/* 如果两张图片相同(指MD5相同), 则相似度应该锁定为1.0; 其它情况在0~1之间 */
			/* 先获取两张图片MD5, 再决定断言方法 */
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
	 * 人脸相似度比对测试
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
			/* 先对比两张本体图片的MD5 */
			String file1Md5 = DigestUtils.md5DigestAsHex(new FileInputStream(file1));
			LOGGER.debug(String.format("[%s].md5==%s", file1.getName(), file1Md5));
			String file2Md5 = DigestUtils.md5DigestAsHex(new FileInputStream(file2));
			LOGGER.debug(String.format("[%s].md5==%s", file2.getName(), file2Md5));
			fakeShell.connect();
			/* 上传第一张图片到远程linux服务器中 */
			fakeShell.upload(remoteDirectory, file1.getAbsolutePath());
			if(!file1Md5.equals(file2Md5)) {
				/* 若第二张图片的MD5和第一张不同, 再上传第二张图片, 然后记下两张图片在服务器中的位置, 传给接口比较 */
				fakeShell.upload(remoteDirectory, file2.getAbsolutePath());
				CompareFaceImageResult result = request.compareFaceImagePath(remoteFilePath1, remoteFilePath2);
				double similarity = result.fetchResult().similarity();
				/* 相似度在0~1之间即可 */
				assertTrue(0.0 < similarity && similarity < 1.0);
				return;
			}
			/* 若第二张图片的MD5和第一张一样, 就不用再上传了. 就用第一张图片在服务器上的路径传参, 变成自己和自己比较相似度 */
			CompareFaceImageResult result = request.compareFaceImagePath(remoteFilePath1, remoteFilePath1);
			double similarity = result.fetchResult().similarity();
			/* 则相似度必须是1.0 */
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