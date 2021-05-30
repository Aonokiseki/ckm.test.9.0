package com.trs.ckm.test.cluster;

import java.io.File;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.trs.ckm.api.master.TRSCkmRequest;
import com.trs.ckm.api.pojo.ClusterGraphResult;
import com.trs.ckm.api.pojo.ClusterGraphTaskResult;
import com.trs.ckm.util.FileOperator;
import com.trs.ckm.util.Other;

public class ClusterTask implements Runnable{
	private final static Logger LOGGER = LogManager.getLogger();
	private TRSCkmRequest request;
	private Configuration configuration;
	private ResultSet resultSet;
	
	public ClusterTask(TRSCkmRequest request, Configuration configuration, ResultSet resultSet) {
		this.request = request;
		this.configuration = configuration;
		this.resultSet = resultSet;
	}

	public void run() {
		int randomFileIndex = -1;
		List<File> zips = configuration.getZips();
		ClusterGraphResult firstResult;
		ClusterGraphTaskResult secondResult;
		randomFileIndex = (int)(Math.random() * zips.size());
		File file = zips.get(randomFileIndex);
		try {
			firstResult = request.clusterGraph(
				configuration.getEncoding(), 
				file.getAbsolutePath(), 
				configuration.getMaxClusterNum(), 
				configuration.getMethod(), 
				configuration.getMinClusterNum(), 
				configuration.getMinNumOfClu());
			String taskId = firstResult.getTaskId();
			secondResult = request.clusterGraphTask(taskId);
			boolean missionCompleted = "Finished".equals(secondResult.getResult().getPhase());
			while(!missionCompleted) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				secondResult = request.clusterGraphTask(taskId);
				missionCompleted = "Finished".equals(secondResult.getResult().getPhase());
			}
			byte[] bytes = request.clusterGraphDownload(taskId);
			File clusterOutputFile = 
					FileOperator.writeBytesToFile(bytes, "clusterGraph_", ".xml", new File(configuration.getOutput()));
			if(!clusterOutputFile.exists()) {
				resultSet.appendFailureInfoList(
						String.format("file [%s] not exist!", clusterOutputFile.getAbsolutePath()));
				resultSet.addFailureAndGet();
				return;
			}
			LOGGER.info("clusterOutputFile="+clusterOutputFile.getAbsolutePath());
			resultSet.addSuccessAndGet();
		}catch(Throwable e) {
			StringBuilder exceptionInfo = new StringBuilder();
			exceptionInfo.append(file.getAbsolutePath())
						 .append(System.lineSeparator())
						 .append(Other.stackTraceToString(e))
						 .append(System.lineSeparator())
						 .append(System.lineSeparator());
			resultSet.addFailureAndGet();
			LOGGER.error(String.format("resultSet.appendFailureInfoList(%s)", exceptionInfo.toString()));
			resultSet.appendFailureInfoList(exceptionInfo.toString());
		}		
	}
}
