package com.trs.dlvrs.test.function;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.List;

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
import com.trs.dlvrs.api.pojo.BasicResult;
import com.trs.dlvrs.api.pojo.GraphSearchResult;
import com.trs.dlvrs.api.pojo.GraphSearchResult.Result;
import com.trs.dlvrs.api.pojo.IRSFeatureImageResult;
import com.trs.dlvrs.test.aspect.DLVRSAspectConfig;
import com.trs.hybase.client.TRSConnection;
import com.trs.hybase.client.TRSException;
import com.trs.hybase.client.TRSRecord;
import com.trs.hybase.client.TRSResultSet;
import com.trs.hybase.client.params.SearchParams;

public class GraphSearchTest {
	private final static Logger LOGGER = LogManager.getLogger(GraphSearchTest.class);
	private final static String TEST_DATA_DIRECTORY = "./testdata/input/dlvrs/GraphSearch";
	
	private AnnotationConfigApplicationContext context;
	private TRSVRSRequest request;
	private FakeShell fakeShell;
	private String remoteDirectory;
	private TRSConnection conn;
	private String dbName;
	private String className = this.getClass().getName();
	
	@BeforeClass
	public void before() {
		context = new AnnotationConfigApplicationContext(DLVRSAspectConfig.class);
		request = context.getBean(TRSVRSRequest.class);
		conn = context.getBean(TRSConnection.class);
		fakeShell = context.getBean(FakeShell.class);
		remoteDirectory = (String) context.getBean("remoteDirectory");
		dbName = (String) context.getBean("graphSearchDBName");
		Constant.reconfigureLog4j2("./config/log4j2_dlvrs.xml");
	}
	
	@AfterClass
	public void after() {
		if(context != null)
			context.close();
		if(conn != null)
			conn.close();
	}
	
	/**
	 * Hybase重启连接测试<br>
	 * 20211015: 研发组似乎去掉了这个接口
	 */
	public void reloadHybase() {
		
	}
	
	@DataProvider(name = "irsFeatureImageDataProvider")
	public Object[][] irsFeatureImageDataProvider(Method method){
		return new Object[][] {
			new Object[] {1, TEST_DATA_DIRECTORY + "/1752.jpg"},
			new Object[] {2, TEST_DATA_DIRECTORY + "/1856.jpg"}
		};
	}
	/**
	 * 图像特征提取
	 * @param caseId
	 * @param filePath
	 */
	@Test(dataProvider = "irsFeatureImageDataProvider")
	public void irsFeatureImageFile(int caseId, String filePath) {
		LOGGER.info(String.format("%s.%s, caseId=%d", className, Other.getMethodName(), caseId));
		IRSFeatureImageResult result = request.irsFeatureImageFile(filePath);
		assertEquals(result.getCode(), Constant.SUCCESS);
		assertNotNull(result.fetchResult().getImage_org_feature());
	}
	/**
	 * 图像特征提取
	 * @param caseId
	 * @param filePath
	 */
	@Test(dataProvider = "irsFeatureImageDataProvider")
	public void irsFeatureImagePath(int caseId, String filePath) {
		LOGGER.info(String.format("%s.%s, caseId=%d", className, Other.getMethodName(), caseId));
		File file = new File(filePath);
		String fileName = file.getName();
		String remoteFilePath = remoteDirectory + "/" + fileName;
		try {
			fakeShell.connect();
			fakeShell.upload(remoteDirectory, filePath);
			IRSFeatureImageResult result = request.irsFeatureImagePath(remoteFilePath);
			/* 只检查提取的特征值串非空 */
			assertEquals(result.getCode(), Constant.SUCCESS);
			assertNotNull(result.fetchResult().getImage_org_feature());
		} catch (JsonSyntaxException | IOException | JSchException | SftpException e) {
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
	
	@DataProvider(name = "irsImageDataProvider")
	public Object[][] irsImageDataProvider(Method method){
		return new Object[][] {
			new Object[] {1, TEST_DATA_DIRECTORY + "/1752.jpg", "5", "0.2"},
		};
	}
	
	/**
	 * 图像检索测试
	 * @param caseId
	 * @param filePath
	 * @param show_num
	 * @param thresh
	 */
	@Test(dataProvider = "irsImageDataProvider")
	public void irsImageFile(int caseId, String filePath, String show_num, String thresh) {
		LOGGER.info(String.format("%s.%s, caseId=%d", className, Other.getMethodName(), caseId));
		GraphSearchResult graphSearchResult = request.irsImageFile(filePath, show_num, thresh);
		/* 请求发送到DL-VRS进程并获取结果后, 收集返回的图片id */
		assertEquals(graphSearchResult.getCode(), Constant.SUCCESS);
		List<Result> results = graphSearchResult.fetchResult();
		assertTrue(results.size() > 0);
		/* 将图片id拼接成hybase的检索表达式 */
		StringBuilder queriesBuilder = new StringBuilder();
		for(int i=0, size=results.size(); i<size; i++) {
			String imageId = results.get(i).getId();
			queriesBuilder.append("image_id:").append(imageId);
			if(i < size - 1)
				queriesBuilder.append(" OR ");
		}
		try {
			/* 放到hybase里检索, 参数num之所以为 results.size() + 1, 
			 * 就是为了确定真的就命中了results.size()条记录, 而不是因为hybase的限制强行裁剪结果集导致的 */
			TRSResultSet resultSet = conn.executeSelect(dbName, queriesBuilder.toString(), 0, results.size() + 1, new SearchParams());
			assertEquals(resultSet.size(), results.size());
		} catch (TRSException e) {
			String failureLog = String.format("%d, %s%s%s", 
					e.getErrorCode(), e.getErrorString(), System.lineSeparator(), Other.stackTraceToString(e));
			LOGGER.error(failureLog);
			fail(failureLog);
		}
	}
	/**
	 * 图像检索测试
	 * @param caseId
	 * @param filePath
	 * @param show_num
	 * @param thresh
	 */
	@Test(dataProvider = "irsImageDataProvider")
	public void irsImagePath(int caseId, String filePath, String show_num, String thresh) {
		LOGGER.info(String.format("%s.%s, caseId=%d", className, Other.getMethodName(), caseId));
		File file = new File(filePath);
		String fileName = file.getName();
		String remoteFilePath = remoteDirectory + "/" + fileName;
		try {
			fakeShell.connect();
			fakeShell.upload(remoteDirectory, file.getAbsolutePath());
			GraphSearchResult graphSearchResult = request.irsImagePath(remoteFilePath, show_num, thresh);
			assertEquals(graphSearchResult.getCode(), Constant.SUCCESS);
			List<Result> results = graphSearchResult.fetchResult();
			assertTrue(results.size() > 0);
			StringBuilder queryBuilder = new StringBuilder();
			for(int i=0, size=results.size(); i<size; i++) {
				String imageId = results.get(i).getId();
				queryBuilder.append("image_id:").append(imageId);
				if(i < size - 1)
					queryBuilder.append(" OR ");
			}
			TRSResultSet resultSet = conn.executeSelect(dbName, queryBuilder.toString(), 0, results.size() + 1, new SearchParams());
			assertEquals(resultSet.size(), results.size());
		} catch (JSchException | IOException | SftpException | TRSException e) {
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
	
	
	@DataProvider(name = "insertImageDataProvider")
	public Object[][] insertImageDataProvider(Method method){
		return new Object[][] {
			new Object[] {1, TEST_DATA_DIRECTORY + "/1752.jpg", "1752"},
			new Object[] {2, TEST_DATA_DIRECTORY + "/1856.jpg", "1856"}
		};
	}
	/**
	 * 插入记录测试
	 * @param caseId
	 * @param filePath
	 * @param id
	 */
	@Test(dataProvider = "insertImageDataProvider")
	public void insertImageFile(int caseId, String filePath, String id) {
		LOGGER.info(String.format("%s.%s, caseId=%d", className, Other.getMethodName(), caseId));
		/* 发送插入记录请求 */
		BasicResult result = request.insertImageFile(filePath, id);
		assertEquals(result.getCode(), Constant.SUCCESS);
		TRSResultSet resultSet;
		/* 向DL-VRS发送完插入记录请求后, 检查能在hybase的表中找到这条记录 */
		try {
			resultSet = conn.executeSelect(dbName, "image_id:"+id, 0, 10, new SearchParams());
			assertEquals(resultSet.size(), 1);
			TRSRecord record = null;
			while(resultSet.moveNext()) {
				record = resultSet.get();
				LOGGER.info(recordToString(record, false));
			}
		} catch (TRSException e) {
			String failureLog = String.format("errorCode=%d, errorString=%s%s%s", 
					e.getErrorCode(), e.getErrorString(), System.lineSeparator(), Other.stackTraceToString(e));
			LOGGER.error(failureLog);
			fail(failureLog);
		} finally {
			try {
				conn.executeDeleteQuery(dbName, "image_id:"+id);
			} catch (TRSException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 插入记录测试
	 * @param caseId
	 * @param filePath
	 * @param id
	 */
	@Test(dataProvider = "insertImageDataProvider")
	public void insertImagePath(int caseId, String filePath, String id) {
		LOGGER.info(String.format("%s.%s, caseId=%d", className, Other.getMethodName(), caseId));
		File file = new File(filePath);
		String fileName = file.getName();
		String remoteFilePath = remoteDirectory + "/" + fileName;
		try {
			fakeShell.connect();
			fakeShell.upload(remoteDirectory, file.getAbsolutePath());
			BasicResult result = request.insertImagePath(remoteFilePath, id);
			assertEquals(result.getCode(), Constant.SUCCESS);
			TRSResultSet resultSet = conn.executeSelect(dbName, "image_id:"+id, 0, 10, new SearchParams());
			assertEquals(resultSet.size(), 1);
			TRSRecord record = null;
			while(resultSet.moveNext()) {
				record = resultSet.get();
				LOGGER.info(recordToString(record, false));
			}
		} catch (IOException | JSchException | SftpException | TRSException e) {
			String failureLog = String.format("%s", Other.stackTraceToString(e));
			LOGGER.error(failureLog);
			fail(failureLog);
		} finally {
			try {
				conn.executeDeleteQuery(dbName, "image_id:"+id);
			} catch (TRSException e) {
				e.printStackTrace();
			}
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
	
	@DataProvider(name = "deleteImageIdDataProvider")
	public Object[][] deleteImageIdDataProvider(Method method){
		return new Object[][] {
			new Object[] {1, TEST_DATA_DIRECTORY + "/1752.jpg", "1752"}
		};
	}
	/**
	 * 删除记录测试
	 * @param caseId
	 * @param filePath
	 * @param id
	 */
	@Test(dataProvider = "deleteImageIdDataProvider")
	public void deleteImageId(int caseId, String filePath, String id) {
		LOGGER.info(String.format("%s.%s, caseId=%d", className, Other.getMethodName(), caseId));
		BasicResult result = request.insertImageFile(filePath, id);
		assertEquals(result.getCode(), Constant.SUCCESS);
		try {
			result = request.deleteImageId(id);
			assertEquals(result.getCode(), Constant.SUCCESS);
			TRSResultSet resultSet = conn.executeSelect(dbName, "image_id:"+id, 0, 10, new SearchParams());
			assertEquals(resultSet.size(), 0);
		} catch (JsonSyntaxException | IOException | TRSException e) {
			String failureLog = String.format("%s", Other.stackTraceToString(e));
			LOGGER.error(failureLog);
			fail(failureLog);
		}
	}
	
	/**
	 * 将TRSRecord对象的每个字段值拼接成一个字符串
	 * @param record
	 * @param returnUid
	 * @return
	 * @throws TRSException
	 */
	public static String recordToString(TRSRecord record, boolean returnUid) throws TRSException {
		String[] columnNames = record.getColumnNames();
		StringBuilder sb = new StringBuilder();
		String value = null;
		sb.append(System.lineSeparator()).append("<REC>").append(System.lineSeparator());
		if(returnUid)
			sb.append("Uid=").append(record.getUid()).append(System.lineSeparator());
		for(int j=0; j<columnNames.length; j++) {
			value = record.getString(columnNames[j]);
			value = (value != null && !value.isEmpty()) ? value.substring(0, Math.min(value.length(), 30)) : "";
			sb.append("<").append(columnNames[j]).append(">=").append(value).append(System.lineSeparator());
		}
		return sb.toString();
	}
}
