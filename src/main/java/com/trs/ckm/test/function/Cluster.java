package com.trs.ckm.test.function;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

import java.io.File;
import java.lang.reflect.Method;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.trs.ckm.api.master.TRSCkmRequest;
import com.trs.ckm.api.pojo.ClusterGraphResult;
import com.trs.ckm.api.pojo.ClusterGraphTaskResult;
import com.trs.ckm.test.aspect.AspectConfig;
import com.trs.ckm.util.FileOperator;
import com.trs.ckm.util.Other;

public class Cluster {
	private AnnotationConfigApplicationContext context;
	private TRSCkmRequest request;
	private final static Logger LOGGER = LogManager.getLogger(Cluster.class);
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
	
	@DataProvider(name = "clusterGraphDataProvider")
	private Object[][] clusterGraphDataProvider(Method method) {
		return new Object[][] {
			new Object[] {1,"UTF-8","./testdata/input/clu/5k.zip","200","person","100","2"},
			new Object[] {2,"UTF-8","./testdata/input/clu/5k.zip","300","org","50","4"},
			new Object[] {3,"UTF-8","./testdata/input/clu/5k.zip","700","theme","200","1"},
			new Object[] {4,"UTF-8","./testdata/input/clu/1.zip","200","person","100","2"},
			new Object[] {5,"UTF-8","./testdata/input/clu/1.zip","300","org","50","4"},
			new Object[] {6,"UTF-8","./testdata/input/clu/1.zip","700","theme","200","1"},
		};
	}
	
	/**
	 * 图聚类<br>
	 * 获取到文件之后手工检查，参数的效果不是绝对的，只是给算法提供一个参考,<br>
	 * 不能保证最终出来的结果和给定的参数完全一致。设计如此。
	 * @param caseId 
	 * @param encoding 文本编码
	 * @param filePath 上传zip文件的路径
	 * @param maxClusterNum 最大类别个数
	 * @param method 关键词提取方式: person-人物|org-组织机构|theme-主题词
	 * @param minClusterNum 最小类别个数
	 * @param minNumOfClu 每个类别下最小文章个数
	 */
	@Test(dataProvider = "clusterGraphDataProvider")
	private void clusterGraph(int caseId, String encoding, String filePath, String maxClusterNum,
			String method, String minClusterNum, String minNumOfClu) {
		LOGGER.debug("clusterGraph, caseId="+caseId);
		try {
			//指定文件, 发送图聚类请求
			ClusterGraphResult graphResult = 
					request.clusterGraph(encoding, filePath, maxClusterNum, method, minClusterNum, minNumOfClu);
			assertEquals(graphResult.getCode(), "1");
			assertEquals(graphResult.getMessage(), "操作成功");
			//根据taskid 获取图聚类状态
			ClusterGraphTaskResult taskResult = request.clusterGraphTask(graphResult.getTaskId());
			int loop = 120;
			//当图聚类状态为“未完成”时, 反复获取状态, 直到其显示为成功或超过阈值
			while(!taskResult.getResult().getPhase().equals("Finished") && loop-- > 0){
				try {
					Thread.sleep(3000);
				}catch(InterruptedException e) {}
				taskResult = request.clusterGraphTask(graphResult.getTaskId());
			}
			//断言聚类状态为完成
			assertEquals(taskResult.getResult().getPhase(), "Finished");
			// 状态为“成功”后，以二进制方式获取聚类结果
			byte[] bytes = request.clusterGraphDownload(graphResult.getTaskId());
			// 将二进制数组转换为文件, 在 ./testdata/clu/ 这个目录下生成
			File file = FileOperator.writeBytesToFile(bytes, "clusterGraph_", ".xml",
					new File(Constant.OUTPUT_DIR + "/clu/"));
			// 读文件, 没有抛出异常, 内容非空即可, 其余需要手工检查
			String content = FileOperator.read(file.getAbsolutePath());
			assertTrue(content.length() > 0);
		} catch (Exception e) {
			fail(Other.stackTraceToString(e));
		}	
	}
}
